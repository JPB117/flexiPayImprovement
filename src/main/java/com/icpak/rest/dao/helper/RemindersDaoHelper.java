package com.icpak.rest.dao.helper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.RemindersDao;
import com.icpak.rest.models.sms.Reminder;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;

@Transactional
public class RemindersDaoHelper {

	@Inject
	RemindersDao reminderDao;
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
	public void createReminderInSchedulor() {
		
	}
}
