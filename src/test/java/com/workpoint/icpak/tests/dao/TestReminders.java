package com.workpoint.icpak.tests.dao;

import java.io.IOException;

import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.RemindersDaoHelper;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;
import com.workpoint.icpak.shared.model.reminders.ReminderType;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestReminders extends AbstractDaoTest {

	@Inject
	RemindersDaoHelper reminderHelper;

	@Test
	public void testReminderCreation() {
		String message = "Dear {fullName}, Its time to go home..because this thing is now working fine";
		ReminderDto r = new ReminderDto();
		r.setReminderFrom("ICPAK");
		r.setReminderTo("ALLMEMBERS");
		r.setReminderName("Testing Reminders");
		r.setCopiesTo("ME");
		r.setReminderType(ReminderType.SMS);
		r.setMessage(message);
		r.setExecutionString("0 days before 2016-09-01 at 21:10");

		reminderHelper.createReminder(r);

	}

	// @Test
	public void testWindowsSchedulor() throws IOException, InterruptedException {
		// reminderHelper.createReminderInSchedulor();

	}

	// @Test
	public void testQuartzImplementation() throws SchedulerException {

		JobDetail j1 = JobBuilder.newJob(QuartzJob.class).build();

		Trigger t1 = TriggerBuilder.newTrigger().withIdentity("SimpleTrigger").startNow().build();

		Scheduler sc = new StdSchedulerFactory().getScheduler();

		sc.start();

		sc.scheduleJob(j1, t1);

		// System.err.println("Hallo world!!");
	}

	public class QuartzJob implements Job {
		@Override
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			// System.err.println("First Quartz Job::" +
			// ServerDateUtils.FULLHOURMINUTESTAMP.format(new Date()));
			System.out.println("Hallo ,,,,,Hallo!!");
		}

	}

}
