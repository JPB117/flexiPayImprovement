package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;

@Produces(MediaType.APPLICATION_JSON)
public interface RemindersResource {

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<ReminderDto> getAll(@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void executeReminder(@PathParam("reminderRefId") String reminderRefId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void executeMonthlyContribution(@PathParam("startDate") String reminderRefId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void executeMonthlyPenalty(@PathParam("startDate") String reminderRefId);

}
