package com.workpoint.icpak.tests.dao;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.RemindersDao;
import com.icpak.rest.dao.helper.RemindersDaoHelper;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestExecuteReminders extends AbstractDaoTest {

	@Inject
	RemindersDaoHelper reminderHelper;

	@Inject
	RemindersDao reminderDao;

	@Test
	public void executeReminders() {
		// reminderHelper.executeReminder();

		reminderHelper.executeReminder("6kPa2FYP6SAgpzOU");
	}
}
