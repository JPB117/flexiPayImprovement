package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.icpak.rest.models.sms.Reminder;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;
import com.workpoint.icpak.shared.model.reminders.ReminderType;

public class RemindersDao extends BaseDao {

	/*
	 * Give me all Reminders between a range of 5mins
	 */
	public List<ReminderDto> getAllReminders(Date executionStartDateTime, Date executionEndDateTime) {
		String query = "select reminderFrom,reminderTo,copiesTo,subject,message,reminderType"
				+ " from Reminder r where r.executionTime>=:executionStartDateTime"
				+ " and r.executionTime<=:executionEndDateTime";

		List<Object[]> rows = getResultList(
				getEntityManager().createQuery(query).setParameter("executionStartDateTime", executionStartDateTime)
						.setParameter("executionEndDateTime", executionEndDateTime));
		List<ReminderDto> reminders = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			ReminderDto r = new ReminderDto();
			r.setReminderFrom((value = row[i++]) == null ? null : value.toString());
			r.setReminderTo((value = row[i++]) == null ? null : value.toString());
			r.setCopiesTo((value = row[i++]) == null ? null : value.toString());
			r.setSubject((value = row[i++]) == null ? null : value.toString());
			r.setMessage((value = row[i++]) == null ? null : value.toString());
			r.setReminderType((value = row[i++]) == null ? null : ReminderType.valueOf(value.toString()));
			reminders.add(r);
		}
		return reminders;
	}

}
