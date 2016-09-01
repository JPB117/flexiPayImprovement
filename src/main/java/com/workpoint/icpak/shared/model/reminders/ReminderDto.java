package com.workpoint.icpak.shared.model.reminders;

import java.util.Date;

import com.workpoint.icpak.shared.model.SerializableObj;

public class ReminderDto extends SerializableObj {

	private static final long serialVersionUID = 1L;

	private String reminderName;
	private String reminderFrom;
	private String reminderTo;
	private String copiesTo;
	private String subject;
	private String message;
	private ReminderType reminderType;
	private String executionDateString;
	private Date executionDate;

	public String getExecutionString() {
		return executionDateString;
	}

	public void setExecutionString(String executionTime) {
		this.executionDateString = executionTime;
	}

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

	public Date getExecutionDate() {
		return executionDate;
	}

	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}

	public String getReminderName() {
		return reminderName;
	}

	public void setReminderName(String reminderName) {
		this.reminderName = reminderName;
	}
}
