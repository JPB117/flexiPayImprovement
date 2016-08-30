package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.sms.Reminder;

public class RemindersDao extends BaseDao {

	public List<Reminder> getAllReminders(String executionDate) {
		assert executionDate != null;
		String query = "from Reminder u where u.executionTime like :executionDate";
		return getResultList(getEntityManager().createQuery(query).setParameter("executionDate", executionDate + "%"));
	}

}
