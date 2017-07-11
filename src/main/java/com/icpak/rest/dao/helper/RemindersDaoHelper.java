package com.icpak.rest.dao.helper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.RemindersDao;
import com.icpak.rest.dao.StatementDao;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.sms.Reminder;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.reminders.ReminderActionType;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;
import com.workpoint.icpak.shared.model.reminders.ReminderRepetitionType;

@Transactional
public class RemindersDaoHelper {

	@Inject
	RemindersDao reminderDao;
	@Inject
	SMSIntegration smsIntegration;
	@Inject
	MemberDao memberDao;
	@Inject
	StatementDao statementDao;
	@Inject
	ApplicationFormDao applicationDao;

	Logger logger = Logger.getLogger(BookingsDaoHelper.class);

	public ReminderDto createReminder(ReminderDto reminder) {
		Reminder r = new Reminder();
		r.copyFrom(reminder);

		// Set Execution Time
		// "5 days before 2016-08-25 at 10Am"

		String executionString = reminder.getExecutionString();
		if (!executionString.isEmpty()) {
			String[] splitExecutionArray = executionString.split(" ");
			if (splitExecutionArray.length == 7) {
				logger.debug("Execution time is in the correct length...");
				// Read the DueDate
				try {
					Date dueDate = ServerDateUtils.FULLHOURMINUTESTAMP
							.parse(splitExecutionArray[3] + " " + splitExecutionArray[5]);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dueDate);

					if (splitExecutionArray[1].equals("days") && splitExecutionArray[2].equals("before")) {
						calendar.add(Calendar.DATE, -(Integer.valueOf(splitExecutionArray[0])));
					} else if (splitExecutionArray[1].equals("days") && splitExecutionArray[2].equals("after")) {
						calendar.add(Calendar.DATE, +(Integer.valueOf(splitExecutionArray[0])));
					} else if (splitExecutionArray[1].equals("month") && splitExecutionArray[2].equals("before")) {
						calendar.add(Calendar.MONTH, -(Integer.valueOf(splitExecutionArray[0])));
					} else if (splitExecutionArray[1].equals("month") && splitExecutionArray[2].equals("after")) {
						calendar.add(Calendar.MONTH, +(Integer.valueOf(splitExecutionArray[0])));
					}
					Date executionDate = calendar.getTime();
					r.setExecutionTime(executionDate);
					logger.debug("Execution Date:::" + ServerDateUtils.FULLHOURMINUTESTAMP.format(executionDate));

					// Set the Response
					reminder.setExecutionDate(executionDate);
					r.setRepetitionType(ReminderRepetitionType.valueOf(splitExecutionArray[6]));

					try {
						Reminder rem = (Reminder) reminderDao.save(r);
						createReminderInSchedulor(rem);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else {
				logger.debug("Failed to create the Reminder. " + "The length of the execution string is not correct.!");
			}
		}

		return reminder;
	}

	/*
	 * Creates this reminder in Windows Schedulor
	 */
	public void createReminderInSchedulor(Reminder reminder) throws IOException, InterruptedException {
		String curl = "\"\'C:\\Program Files\\curl-7.50.1-win64-mingw\\curl-7.50.1-win64-mingw\\bin\\curl.exe' "
				+ "http://localhost:8080/karagita/api/reminders/" + reminder.getRefId() + " -s -X POST\"";

		String additionals = "";
		if (reminder.getRepetitionType() == ReminderRepetitionType.MONTHLY) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(reminder.getExecutionTime());
			int day = cal.get(Calendar.DAY_OF_MONTH);
			additionals = additionals + " /d " + day;
		}
		String executeCmd = "SchTasks /Create /SC " + reminder.getRepetitionType() + " /TN \""
				+ reminder.getReminderName() + "_" + reminder.getRefId() + "\" /TR " + curl + " /SD "
				+ ServerDateUtils.DATEFORMATAMERICAN.format(reminder.getExecutionTime()) + " /ST "
				+ ServerDateUtils.TWENTYFOURHOURTIMESTAMP.format(reminder.getExecutionTime()) + additionals;

		System.err.println(executeCmd);
		/* NOTE: Executing the command here */
		Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
		int processComplete = runtimeProcess.waitFor();

		/*
		 * NOTE: processComplete=0 if correctly executed, will contain other
		 * values if not
		 */
		if (processComplete == 0) {
			System.out.println("Execution successful.");

		} else {
			System.out.println("Execution Failed with code:" + processComplete);
			throw new RuntimeException();
		}
	}

	/*
	 * Deprecated Method - Check the next Execute Reminder that executes from a
	 * RefId passed.
	 */

	public void executeReminder(String passedJson, String reminderId) {
		Date executionStartDate = null;
		Date executionEndDate = null;
		final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
		Calendar calendar = Calendar.getInstance();

		long t = calendar.getTimeInMillis();
		executionStartDate = (new Date(t - (5 * ONE_MINUTE_IN_MILLIS)));
		calendar.setTime(executionStartDate);
		executionEndDate = new Date(t + (5 * ONE_MINUTE_IN_MILLIS));

		if (executionStartDate != null && executionEndDate != null) {
			logger.debug("Execution StartDate::" + executionStartDate + "\nCurrentTime::" + new Date()
					+ "\nExecution EndDate::" + executionEndDate);
			List<ReminderDto> reminders = reminderDao.getAllReminders(executionStartDate, executionEndDate);
			logger.debug("Reminders to be executed::" + reminders.size());

			if (!reminders.isEmpty()) {
				for (ReminderDto r : reminders) {
					if (r.getReminderType() == ReminderActionType.SMS) {
						logger.debug("Sending reminder sms...");
						// smsIntegration.send(r.getReminderTo(),
						// r.getMessage());
					} else {
						logger.debug("Sending another type of reminder");
					}
				}
			}
		}
	}

	public void sendBulkSMS() {
		List<Member> allMembers = memberDao.getAllMembers(0, 1000000);
		for (Member m : allMembers) {
			User user = m.getUser();
			if (user != null && user.getMember() != null) {
				String statementUrl = "https://www.icpak.com/flexipay/getreport?action=GETSTATEMENT&timestamp="
						+ new Date().getTime() + "&startdate=1451595600000&memberRefId=" + user.getMember().getRefId()
						+ "";
				String message = "Karagita Welfare says: Happy new year %s. "
						+ "We have now gone digital! Every month you will receive "
						+ "you balance on SMS on the 1st day of the month. "
						+ "Failure to pay by 16th of the month will automatically "
						+ "apply a penalty to your account.You can"
						+ " now also download your statements clicking the link below. " + statementUrl;

				message = String.format(message, user.getFullName());
				String phoneNumbers = user.getPhoneNumber();
				phoneNumbers = phoneNumbers.trim();
				phoneNumbers = phoneNumbers.replaceAll(";", ",");
				System.err.println(phoneNumbers + ">>>>" + message);
				smsIntegration.send(phoneNumbers, message);
			}
		}
	}

	public void executeReminder(String refId) {
		Reminder reminder = reminderDao.findByRefId(refId, Reminder.class);
		if (reminder != null) {
			if (reminder.getReminderType() == ReminderActionType.SMS) {
				logger.debug("Sending reminder sms...");
				if (reminder.getReminderTo().equals("ALLMEMBERS")) {
					List<Member> allMembers = memberDao.getAllMembers(0, 1000000);
					for (Member m : allMembers) {
						User user = m.getUser();
						if (user != null && user.getMember() != null) {
							Double balance = (statementDao.getOpeningBalance(user.getMember().getMemberNo(), new Date())
									.getAmount());
							if (balance < 0.0) {
								balance = -balance;
							}
							DecimalFormat df2 = new DecimalFormat("###,###");
							String message = applyPlaceHolders(user, reminder.getMessage(), df2.format(balance));
							String phoneNumbers = user.getPhoneNumber();
							phoneNumbers = phoneNumbers.trim();
							phoneNumbers = phoneNumbers.replaceAll(";", ",");
							smsIntegration.send(phoneNumbers, message);
							System.err.println(phoneNumbers + ">>>>" + message);
						}
					}
				} else {
					smsIntegration.send(reminder.getReminderTo(), reminder.getMessage());
				}
			} else {
				logger.debug("Sending another type of reminder");
			}
		} else {
			logger.debug("Reminder Failed: The refId was not found.");
		}

	}

	private String applyPlaceHolders(User user, String message, String balance) {
		System.err.println(balance);
		String ammendedMessage = "";
		ammendedMessage = String.format(message, user.getFullName(), balance);
		System.err.println("Ammended>>>" + ammendedMessage);
		return ammendedMessage;
	}
}
