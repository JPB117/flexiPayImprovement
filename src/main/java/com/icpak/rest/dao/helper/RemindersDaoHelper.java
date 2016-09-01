package com.icpak.rest.dao.helper;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.RemindersDao;
import com.icpak.rest.models.sms.Reminder;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;
import com.workpoint.icpak.shared.model.reminders.ReminderType;

@Transactional
public class RemindersDaoHelper {

	@Inject
	RemindersDao reminderDao;

	@Inject
	SMSIntegration smsIntegration;

	Logger logger = Logger.getLogger(BookingsDaoHelper.class);

	public ReminderDto createReminder(ReminderDto reminder) {
		Reminder r = new Reminder();
		r.copyFrom(reminder);

		// Set Execution Time
		// "5 days before 2016-08-25 at 10Am"

		String executionString = reminder.getExecutionString();
		if (!executionString.isEmpty()) {
			String[] splitExecutionArray = executionString.split(" ");
			if (splitExecutionArray.length == 6) {
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

					try {
						createReminderInSchedulor(reminder);
					} catch (IOException | InterruptedException e) {
						e.printStackTrace();
					}
					reminderDao.save(r);
				} catch (ParseException e) {
					e.printStackTrace();
				}

			} else {
				logger.debug("The execution string is less than 6.");
			}
		}

		return reminder;
	}

	/*
	 * Creates this reminder in Windows Schedulor
	 */
	public void createReminderInSchedulor(ReminderDto reminder) throws IOException, InterruptedException {
		String curlFullPath = "\"C:\\Program Files\\curl-7.50.1-win64-mingw\\curl-7.50.1-win64-mingw\\bin\\curl.exe";
		String curl = curlFullPath + " http://localhost:8888/api/reminders -X POST\"";
		String executeCmd = " SchTasks /Create /SC ONCE /TN \"" + reminder.getReminderName() + "_"
				+ IDUtils.generateId() + "\" /TR " + curl + " /ST "
				+ ServerDateUtils.TWENTYFOURHOURTIMESTAMP.format(reminder.getExecutionDate());
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
			// throw new RuntimeException();
		}
	}

	public void executeReminder() {
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
					if (r.getReminderType() == ReminderType.SMS) {
						logger.debug("Sending reminder sms...");
						smsIntegration.send(r.getReminderTo(), r.getMessage());
					} else {
						logger.debug("Sending another type of reminder");
					}
				}
			}
		}
	}
}
