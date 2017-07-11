package com.icpak.rest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.RemindersDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.api.RemindersResource;
import com.workpoint.icpak.shared.model.reminders.ReminderDto;

@Path("reminders")
@Api(value = "", description = "Api for all reminders")
public class ReminderResourceImpl implements RemindersResource {

	@Inject
	RemindersDaoHelper reminderHelper;

	@Inject
	StatementDaoHelper statementHelper;

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

	@POST
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("executeMonthlyContribution/{startDate}")
	public void executeMonthlyContribution(@PathParam("startDate") String reminderRefId) {
		System.err.println("--------Executing Monthly Contribution called.---------");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_WEEK, 7);
		statementHelper.createMonthlyStatementForAllMembers(ServerDateUtils.SHORTTIMESTAMP.format(new Date()),
				ServerDateUtils.SHORTTIMESTAMP.format(c.getTime()), -2000.0);
		System.err.println("Execution succesful");

	}

	@POST
	@Override
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("executePenalty/{startDate}")
	public void executeMonthlyPenalty(@PathParam("startDate") String reminderRefId) {
		System.err.println("--------Executing Reminder called.---------");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DAY_OF_WEEK, 7);
		statementHelper.applyPenaltiesBetweenDateRange(ServerDateUtils.SHORTTIMESTAMP.format(new Date()),
				ServerDateUtils.SHORTTIMESTAMP.format(c.getTime()));
		System.err.println("Execution succesful");
	}
}
