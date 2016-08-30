package com.workpoint.icpak.tests.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.RemindersDao;
import com.icpak.rest.dao.helper.RemindersDaoHelper;
import com.icpak.rest.models.sms.Reminder;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestExecuteReminders extends AbstractDaoTest {

	@Inject
	RemindersDaoHelper reminderHelper;

	@Inject
	RemindersDao reminderDao;

	@Test
	public void executeReminders() {
		Date executionDate = null;
		try {
			executionDate = ServerDateUtils.DATEFORMAT_SYS.parse("2016-08-20");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (executionDate != null) {
			System.err.println("Execution Date:::" + executionDate);
			List<Reminder> reminders = reminderDao.getAllReminders("2016-08-20");
			System.err.println("Reminders to be executed::" + reminders.size());
		}
	}
}
