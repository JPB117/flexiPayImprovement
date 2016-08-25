package com.icpak.rest.dao.helper;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.icpak.rest.dao.RemindersDao;
import com.icpak.rest.models.sms.Reminder;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;

public class RemindersDaoHelper {

	@Inject
	RemindersDao reminderDao;
	Logger logger = Logger.getLogger(BookingsDaoHelper.class);

	public ReminderDto createReminder(ReminderDto reminder) {
		Reminder r = new Reminder();
		r.copyFrom(reminder);

		// Set Execution Time
		// "5 days before 2016-08-25 at 10Am"

		String executionTime = reminder.getExecutionString();
		if (!executionTime.isEmpty()) {
			String[] splitExecutionTime = executionTime.split(" ");
			if (splitExecutionTime.length == 7) {
				logger.debug("Execution time is in the correct length...");
				// Read the DueDate
				try {
					Date dueDate = ServerDateUtils.DATEFORMAT_SYS.parse(splitExecutionTime[3]);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dueDate);
					if (splitExecutionTime[1].equals("days")) {
						calendar.add(Calendar.DATE, Integer.valueOf(splitExecutionTime[0]));
					} else if (splitExecutionTime[1].equals("month")) {
						calendar.add(Calendar.MONTH, Integer.valueOf(splitExecutionTime[0]));
					}
					Date executionDate = calendar.getTime();
					r.setExecutionTime(executionDate);

					// Set the Response
					reminder.setExecutionDate(executionDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

		reminderDao.save(r);
		return reminder;
	}
}
