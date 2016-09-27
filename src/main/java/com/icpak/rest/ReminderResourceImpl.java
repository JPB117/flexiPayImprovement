package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.RemindersDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.workpoint.icpak.shared.api.RemindersResource;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;

@Path("reminders")
@Api(value = "", description = "Api for all reminders")
public class ReminderResourceImpl implements RemindersResource {

	@Inject
	RemindersDaoHelper reminderHelper;

	@GET
	@Override
	public List<ReminderDto> getAll(Integer offset, Integer limit) {
		return null;
	}

	@POST
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{reminderRefId}")
	public void executeReminder(@PathParam("reminderRefId") String reminderRefId) {
		System.err.println("--------Executing Reminder called.---------");
		reminderHelper.executeReminder(reminderRefId);
	}

}