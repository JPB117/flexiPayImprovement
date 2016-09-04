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
import com.workpoint.icpak.shared.model.reminders.ReminderActionType;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestReminders extends AbstractDaoTest {

	@Inject
	RemindersDaoHelper reminderHelper;

	@Test
	public void testReminderCreation() {
		// Late Reminder
		String message = "::From Karagita Welfare:: Dear %s, Please note that our chama payment of Kes 2,000 will be due on 16/9/2016. "
				+ "MPESA Paybill 510511, Account 2141.";
		ReminderDto r = new ReminderDto();
		r.setReminderFrom("ICPAK");
		r.setReminderTo("ALLMEMBERS");
		r.setReminderName("Karagita Reminders");
		r.setCopiesTo("ME");
		r.setReminderType(ReminderActionType.SMS);
		r.setMessage(message);
		r.setExecutionString("10 days before 2016-09-15 at 10:00 ONCE");
		reminderHelper.createReminder(r);

		// Karagita Monthly Reminder
		String message2 = "::From Karagita Welfare::Dear %s, Please note that our chama payment of Kes 2,000 will be due due on 16th of this Month. "
				+ "MPESA Paybill 510511, Account No 2141.";
		ReminderDto r1 = new ReminderDto();
		r1.setReminderFrom("ICPAK");
		r1.setReminderTo("ALLMEMBERS");
		r1.setReminderName("Karagita Reminders");
		r1.setCopiesTo("ME");
		r1.setReminderType(ReminderActionType.SMS);
		r1.setMessage(message2);
		r1.setExecutionString("15 days before 2016-09-16 at 10:00 MONTHLY");
		reminderHelper.createReminder(r1);

		// Karagita Monthly 2 Reminder
		String message3 = "::From Karagita Welfare::Dear %s, Please note that our chama payment of Kes 2,000 will be due due on 16th of this Month. "
				+ "MPESA Paybill 510511, Account No 2141.";
		ReminderDto r2 = new ReminderDto();
		r2.setReminderFrom("ICPAK");
		r2.setReminderTo("ALLMEMBERS");
		r2.setReminderName("Karagita Reminders");
		r2.setCopiesTo("ME");
		r2.setReminderType(ReminderActionType.SMS);
		r2.setMessage(message3);
		r2.setExecutionString("1 days before 2016-09-16 at 10:00 MONTHLY");
		reminderHelper.createReminder(r2);

		// Karagita Monthly 2 Reminder
		String message4 = "::From Karagita Welfare::Dear %s, Please note that our chama payment of Kes 2,000 will be due due on 16th of this Month. "
				+ "MPESA Paybill 510511, Account No 2141.";
		ReminderDto r3 = new ReminderDto();
		r3.setReminderFrom("ICPAK");
		r3.setReminderTo("ALLMEMBERS");
		r3.setReminderName("Karagita Reminders");
		r3.setCopiesTo("ME");
		r3.setReminderType(ReminderActionType.SMS);
		r3.setMessage(message4);
		r3.setExecutionString("0 days before 2016-09-16 at 10:00 MONTHLY");
		reminderHelper.createReminder(r3);

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
