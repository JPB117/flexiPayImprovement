package com.workpoint.icpak.client.ui.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * 
 * @author duggan
 *
 */
public class DateUtils {
	static String datepattern = "dd/MM/yyyy";
	static String datepattern_sys = "yyyy-MM-dd";
	static String yearpattern = "yyyy";
	static String createdpattern = "dd/MM/yyyy HH:mm";
	static String fullPattern = "EEE,MMM d,yyyy";
	static String halfPattern = "EEEE, MMM d";
	static String monthDayPattern = "MMM d";
	static String monthPattern = "MMM (MM)";
	static String monthOnlyPattern = "MM";
	static String monthDayYearPattern = "MMM d yyyy";
	static String Time = "hh:mm a";
	static String MonthTime = "MMM d, hh:mm a";
	static String shortTimeStamp = "yyyy-MM-dd";
	static String fullTimeStamp = "yyyy-MM-dd hh:mm:ss";

	public static final DateTimeFormat CREATEDFORMAT = DateTimeFormat
			.getFormat(createdpattern);
	public static final DateTimeFormat DATEFORMAT = DateTimeFormat
			.getFormat(datepattern);
	public static final DateTimeFormat DATEFORMAT_SYS = DateTimeFormat
			.getFormat(datepattern_sys);
	public static final DateTimeFormat SHORTTIMESTAMP = DateTimeFormat
			.getFormat(shortTimeStamp);
	public static final DateTimeFormat YEARFORMAT = DateTimeFormat
			.getFormat(yearpattern);
	public static final DateTimeFormat HALFDATEFORMAT = DateTimeFormat
			.getFormat(halfPattern);
	public static final DateTimeFormat FULLDATEFORMAT = DateTimeFormat
			.getFormat(fullPattern);
	public static final DateTimeFormat MONTHDAYFORMAT = DateTimeFormat
			.getFormat(monthDayPattern);
	public static final DateTimeFormat MONTHDAYYEARFORMAT = DateTimeFormat
			.getFormat(monthDayYearPattern);
	public static final DateTimeFormat TIMEFORMAT12HR = DateTimeFormat
			.getFormat(Time);
	public static final DateTimeFormat MONTHTIME = DateTimeFormat
			.getFormat(MonthTime);
	public static final DateTimeFormat MONTHFORMAT = DateTimeFormat
			.getFormat(monthPattern);
	public static final DateTimeFormat MONTHONLYFORMAT = DateTimeFormat
			.getFormat(monthOnlyPattern);
	public static final DateTimeFormat FULLTIMESTAMP = DateTimeFormat
			.getFormat(fullTimeStamp);

	static long dayInMillis = 24 * 3600 * 1000;
	static long hourInMillis = 3600 * 1000;
	static long minInMillis = 60 * 1000;

	public static String getTimeDifference(Date createdDate) {
		if (createdDate == null) {
			return "";
		}
		Date today = new Date();
		long now = today.getTime();
		long created = createdDate.getTime();
		long diff = now - created;

		StringBuffer buff = new StringBuffer();

		if (diff > dayInMillis) {
			int days = CalendarUtil.getDaysBetween(createdDate, today);
			if (days == 1) {
				return "1 day";
			}

			return days + " days";
		}

		if (!CalendarUtil.isSameDate(createdDate, new Date())) {
			return "1 day";
		}

		if (diff > hourInMillis) {
			long hrs = diff / hourInMillis;
			buff.append(hrs + " " + ((hrs) == 1 ? "hr" : "hrs"));
			diff = diff % hourInMillis;
		}

		if (diff > minInMillis && buff.length() == 0) {
			long mins = diff / minInMillis;
			buff.append(mins + " " + ((mins) == 1 ? "min" : "mins"));
			diff = diff % minInMillis;
		}

		if (buff.length() == 0) {
			long secs = diff / 1000;
			buff.append(secs + " " + (secs == 1 ? "sec" : "secs"));
		}

		return buff.toString();

	}

	public static String getTimeDifferenceAsString(Date createdDate) {

		if (createdDate == null) {
			return "";
		}

		Date today = new Date();
		long now = today.getTime();
		long created = createdDate.getTime();
		long diff = now - created;
		
		if(diff<0){
			//Date in th future
			int days = (int) (diff/dayInMillis);
			
			return days+"";
		}
		

		StringBuffer buff = new StringBuffer();

		if (diff > 2 * dayInMillis) {
			return DateUtils.DATEFORMAT.format(createdDate);
		}

		if (diff > dayInMillis) {
			int days = CalendarUtil.getDaysBetween(createdDate, today);
			if (days == 1) {
				return "yesterday";
			}

			return days + " days ago";
		}

		if (!CalendarUtil.isSameDate(createdDate, new Date())) {
			return "yesterday";
		}

		if (diff > hourInMillis) {
			long hrs = diff / hourInMillis;
			buff.append(hrs + " " + ((hrs) == 1 ? "hr" : "hrs"));
			diff = diff % hourInMillis;
		}

		if (diff > minInMillis && buff.length() == 0) {
			long mins = diff / minInMillis;
			buff.append(mins + " " + ((mins) == 1 ? "min" : "mins"));
			diff = diff % minInMillis;
		}

		if (buff.length() == 0) {
			long secs = diff / 1000;
			buff.append(secs + " " + (secs == 1 ? "sec" : "secs"));
		}

		buff.append(" ago");
		return buff.toString();

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

	public static String getDateGroupDescription(Date date) {
		Date today = new Date();
		if (CalendarUtil.isSameDate(date, today)) {
			return "Today";
		}

		if (CalendarUtil.getDaysBetween(date, today) == 1) {
			return "Yesterday";
		}

		if (CalendarUtil.getDaysBetween(date, today) < 8) {
			return "This Week";
		}

		if (CalendarUtil.getDaysBetween(date, today) < 31) {
			return "This Month";
		}

		return "Older Than One Month";
	}

	public static boolean isSameDate(Date dateGroup, Date created) {
		if (dateGroup == null) {
			return false;
		}

		return CalendarUtil.isSameDate(dateGroup, created);
	}

	public static String getTimeDifference(Date start, Date finish) {
		int days = CalendarUtil.getDaysBetween(start, finish);

		if (days == 0) {
			long diff = finish.getTime() - start.getTime();
			StringBuffer buff = new StringBuffer();

			if (diff > hourInMillis) {
				long hrs = diff / hourInMillis;
				buff.append(hrs + " " + ((hrs) == 1 ? "hr" : "hrs"));
				diff = diff % hourInMillis;
			}

			if (diff > minInMillis && buff.length() == 0) {
				long mins = diff / minInMillis;
				buff.append(mins + " " + ((mins) == 1 ? "min" : "mins"));
				diff = diff % minInMillis;
			}

			if (buff.length() == 0) {
				long secs = diff / 1000;
				buff.append(secs + " " + (secs == 1 ? "sec" : "secs"));
			}

			return buff.toString();
		}
		return days + " day" + (days == 1 ? "" : "s");
	}

	public static Date getDateByRange(DateRange range, boolean setToMidnight) {
		Date today = new Date();
		switch (range) {
		case NOW:
			return today;
		case TODAY:
			return setToMidnight(today);
		case YESTERDAY:
			CalendarUtil.addDaysToDate(today, -1);
			if (setToMidnight) {
				return setToMidnight(today);
			} else {
				return today;
			}
		case THISWEEK:
			CalendarUtil.addDaysToDate(today, -7);
			return setToMidnight(today);
		case THISMONTH:
			CalendarUtil.setToFirstDayOfMonth(today);
			return setToMidnight(today);

		case LASTMONTH:
			CalendarUtil.addMonthsToDate(today, -1);
			CalendarUtil.setToFirstDayOfMonth(today);
			return setToMidnight(today);

		case THISQUARTER:
			CalendarUtil.addMonthsToDate(today, -3);
			CalendarUtil.setToFirstDayOfMonth(today);
			return setToMidnight(today);

		case HALFYEAR:
			CalendarUtil.addMonthsToDate(today, -6);
			CalendarUtil.setToFirstDayOfMonth(today);
			return setToMidnight(today);
		case THISYEAR:
			CalendarUtil.addMonthsToDate(today, -today.getMonth());
			CalendarUtil.setToFirstDayOfMonth(today);
			return setToMidnight(today);

		default:
			return today;

		}
	}

	public static Date setToMidnight(Date passedDate) {
		String todayText = SHORTTIMESTAMP.format(passedDate);
		return SHORTTIMESTAMP.parse(todayText);
	}
	
	
	public static String format(Date date, DateTimeFormat formatter) {
		if (date == null) {
			return null;
		}
		return formatter.format(date);
	}

	public static Date parse(String date, DateTimeFormat formatter) {
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
