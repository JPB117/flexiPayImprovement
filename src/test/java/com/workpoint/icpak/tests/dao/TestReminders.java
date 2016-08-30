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
		String message = "Dear {fullName}, Please note that this month contribution of Kes 2,000 is due on 10th. Paybill 510511, Account No: 2141.";
		ReminderDto r = new ReminderDto();
		r.setReminderFrom("ICPAK");
		r.setReminderTo("ALLMEMBERS");
		r.setCopiesTo("ME");
		r.setReminderType(ReminderType.SMS);
		r.setMessage(message);
		r.setExecutionString("10 days before 2016-08-31 at 10:00");

		reminderHelper.createReminder(r);

	}

	// @Test
	public void testWindowsSchedulor() throws IOException, InterruptedException {
		String executeCmd = "SchTasks /Create /SC DAILY /TN \"My Task\" /TR \"C:RunMe.bat\" /ST 09:00";
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
		}

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
