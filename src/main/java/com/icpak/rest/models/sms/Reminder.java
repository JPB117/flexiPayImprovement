package com.icpak.rest.models.sms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;
import com.workpoint.icpak.shared.model.reminders.ReminderType;

@Entity
@Table(name = "reminders")
public class Reminder extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String reminderName;
	private String reminderFrom;
	private String reminderTo;
	private String copiesTo;
	private String subject;
	@Column(name = "message", columnDefinition = "TEXT")
	private String message;
	@Enumerated(EnumType.STRING)
	private ReminderType reminderType;
	private Date executionTime;

	public String getReminderFrom() {
		return reminderFrom;
	}

	public void setReminderFrom(String reminderFrom) {
		this.reminderFrom = reminderFrom;
	}

	public String getReminderTo() {
		return reminderTo;
	}

	public void setReminderTo(String reminderTo) {
		this.reminderTo = reminderTo;
	}

	public String getCopiesTo() {
		return copiesTo;
	}

	public void setCopiesTo(String copiesTo) {
		this.copiesTo = copiesTo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReminderType getReminderType() {
		return reminderType;
	}

	public void setReminderType(ReminderType reminderType) {
		this.reminderType = reminderType;
	}

	public void copyFrom(ReminderDto reminder) {
		setReminderFrom(reminder.getReminderFrom());
		setReminderTo(reminder.getReminderTo());
		setCopiesTo(reminder.getCopiesTo());
		setSubject(reminder.getSubject());
		setMessage(reminder.getMessage());
		setReminderType(reminder.getReminderType());
	}

	public Date getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	public String getReminderName() {
		return reminderName;
	}

	public void setReminderName(String reminderName) {
		this.reminderName = reminderName;
	}

}
