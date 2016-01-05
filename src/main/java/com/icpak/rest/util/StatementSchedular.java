package com.icpak.rest.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.StatementDaoHelper;

public class StatementSchedular {
	Logger logger = Logger.getLogger(StatementSchedular.class);
	/*
	 * Report generator Class
	 */
	public static class ReportGenerator extends TimerTask {
		Logger log = Logger.getLogger(ReportGenerator.class);
		@Override
		public void run() {
			// TODO Auto-generated method stub
			log.info(" RUNNING SCHEDULAR TRUE ");
			StatementDaoHelper daoHelper = new StatementDaoHelper();
			daoHelper.initiTimer();
			log.info(" RUNNING SCHEDULAR DONE ");
		}
	}

	@SuppressWarnings("deprecation")
	public void scheduleReport(Date futureDate) {
		logger.info(" RUNNING SCHEDULAR ++++");
		Timer timer = new Timer();

		Calendar date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_YEAR, futureDate.getDay());
		date.set(Calendar.HOUR, 12);
		date.set(Calendar.MINUTE, 25);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);
		// Schedule to run every Tuesday in midnight
		timer.schedule(new ReportGenerator(), date.getTime(), 1000 * 60 * 60 * 24 * 7);
	}
}
