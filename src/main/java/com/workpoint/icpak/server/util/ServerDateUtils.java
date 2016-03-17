package com.workpoint.icpak.server.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerDateUtils {

	static String datepattern = "dd/MM/yyyy";
	static String datepattern_sys = "yyyy-MM-dd";
	static String yearpattern = "yyyy";
	static String createdpattern = "dd/MM/yyyy HH:mm";
	static String fullPattern = "EEE,MMM d,yyyy";
	static String halfPattern = "EEEE, d MMMM yyyy";
	static String monthDayPattern = "MMM d";
	static String monthPattern = "MMM (MM)";
	static String monthOnlyPattern = "MM";
	static String monthDayYearPattern = "MMM d yyyy";
	static String Time = "hh:mm a";
	static String MonthTime = "MMM d, hh:mm a";
	static String shortTimeStamp = "yyyy-MM-dd";
	static String fullHourMinuteStamp = "yyyy-MM-dd hh:mm";
	static String fullTimeStamp = "yyyy-MM-dd hh:mm:ss";

	public static final SimpleDateFormat CREATEDFORMAT = getFormat(createdpattern);

	public static final SimpleDateFormat DATEFORMAT = getFormat(datepattern);
	public static final SimpleDateFormat DATEFORMAT_SYS = getFormat(datepattern_sys);
	public static final SimpleDateFormat SHORTTIMESTAMP = getFormat(shortTimeStamp);
	public static final SimpleDateFormat YEARFORMAT = getFormat(yearpattern);
	public static final SimpleDateFormat HALFDATEFORMAT = getFormat(halfPattern);
	public static final SimpleDateFormat FULLDATEFORMAT = getFormat(fullPattern);
	public static final SimpleDateFormat MONTHDAYFORMAT = getFormat(monthDayPattern);
	public static final SimpleDateFormat MONTHDAYYEARFORMAT = getFormat(monthDayYearPattern);
	public static final SimpleDateFormat TIMEFORMAT12HR = getFormat(Time);
	public static final SimpleDateFormat MONTHTIME = getFormat(MonthTime);
	public static final SimpleDateFormat MONTHFORMAT = getFormat(monthPattern);
	public static final SimpleDateFormat MONTHONLYFORMAT = getFormat(monthOnlyPattern);
	public static final SimpleDateFormat FULLHOURMINUTESTAMP = getFormat(fullHourMinuteStamp);
	public static final SimpleDateFormat FULLTIMESTAMP = getFormat(fullTimeStamp);

	static long minInMillis = 60 * 1000;
	static long hourInMillis = 60 * minInMillis;
	static long dayInMillis = 24 * hourInMillis;
	static long monthInMillis = 30 * dayInMillis;
	static long yearInMillis = 12 * monthInMillis;

	public static int getDaysBetween(Date startDate, Date endDate) {
		int days = (int) ((endDate.getTime() - startDate.getTime()) / dayInMillis);

		return days;
	}

	/**
	 * Assumes a 30 day month
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int getMonthsBetween(Date startDate, Date endDate) {
		int months = (int) ((endDate.getTime() - startDate.getTime()) / monthInMillis);
		return months;
	}

	public static double getYearsBetween(Date startDate, Date endDate) {
		double years = (double) ((endDate.getTime() - startDate.getTime()) / yearInMillis);
		return years;
	}

	private static SimpleDateFormat getFormat(String pattern) {
		return new SimpleDateFormat(pattern);
	}

	public static boolean isDueInMins(int mins, Date endDate) {
		long currentTime = new Date().getTime();
		long endTime = endDate.getTime();
		long diff = endTime - currentTime;

		if (diff > 0 && diff < mins * 60 * 1000) {
			return true;
		}

		return false;
	}

	public static boolean isOverdue(Date endDate) {
		Date currDate = new Date();
		return currDate.after(endDate);
	}

	public static Date addDays(Date created, int days) {

		return new Date(created.getTime() + dayInMillis * days);
	}

	public static String format(Date date, SimpleDateFormat formatter) {
		if (date == null) {
			return null;
		}
		return formatter.format(date);
	}

	public static Date parse(String date, SimpleDateFormat formatter) {
		if (date == null || date.isEmpty()) {
			return null;
		}

		try {
			return formatter.parse(date);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
