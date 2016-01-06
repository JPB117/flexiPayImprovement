package com.icpak.rest.util;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class StatementSchedular {	
	Logger logger = Logger.getLogger(StatementSchedular.class);

	public void invockTrigger(long dateTimeValue) {
		logger.info(" INVOKING TRIGGER ");
		
		logger.info("DaoHelper = "+ScheduleInjector.getStatementDaoHelper());
		final Date date = new Date(dateTimeValue);

		//Specifying job details
		JobDetail jobDetail = JobBuilder.newJob(StatementsJob.class)
				.withIdentity("Quaterly Annual CPD Reports")
				.build();
		
		//Specifying the schedule time
		Trigger trigger = TriggerBuilder
				.newTrigger()
				.startAt(date)
				.withIdentity("dummyTriggerName", "group1")
				.withSchedule(
					SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInSeconds(5).withRepeatCount(0))
				.build();
		
		//Scheduling the trigger
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			scheduler.start();
			scheduler.scheduleJob(jobDetail , trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}
}
