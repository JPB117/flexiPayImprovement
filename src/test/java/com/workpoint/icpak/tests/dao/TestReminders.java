package com.workpoint.icpak.tests.dao;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.RemindersDaoHelper;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;

public class TestReminders {

	@Inject
	RemindersDaoHelper reminderHelper;

	@Test
	public void testReminderCreation() {
		ReminderDto r = new ReminderDto();
		r.setReminderFrom("ICPAK");
		r.setReminderTo("ALLMEMBERS");
		r.setCopiesTo("ME");
		r.setExecutionString("5 days before 2016-08-25 at 10Am");
		
	}

}
