package com.icpak.rest.util;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class StatementsJob implements Job{
	Logger logger = Logger.getLogger(StatementsJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		logger.info(" Executing job ");
		ScheduleInjector.getStatementDaoHelper().initiTimer();
	}

}
