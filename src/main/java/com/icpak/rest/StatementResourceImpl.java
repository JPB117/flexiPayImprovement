package com.icpak.rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.api.StatementsResource;
import com.workpoint.icpak.shared.model.statement.StatementDto;
import com.workpoint.icpak.shared.model.statement.StatementSummaryDto;

@Api(value = "", description = "Handles CRUD for Statements")
@Produces(MediaType.APPLICATION_JSON)
public class StatementResourceImpl implements StatementsResource {

	@Inject
	StatementDaoHelper helper;
	private final String memberId;

	@Inject
	public StatementResourceImpl(@Assisted String memberId) {
		this.memberId = memberId;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all member statement")
	public List<StatementDto> getAll(@QueryParam("startdate") Long startDate,
			@QueryParam("enddate") Long endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		if (offset == null) {
			offset = 0;
		}
		if (limit == null) {
			limit = 30;
		}

		List<StatementDto> dtos = helper.getAllStatements(memberId,
				startDate == null ? null : new Date(startDate),
				endDate == null ? null : new Date(endDate), offset, limit);
		return dtos;
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return helper.getCount(memberId, null, null);
	}

	@GET
	@Path("/filteredcount")
	public Integer getCount(@QueryParam("startdate") Long startDate,
			@QueryParam("enddate") Long endDate) {
		return helper.getCount(memberId, startDate == null ? null : new Date(
				startDate), endDate == null ? null : new Date(endDate));
	}

	@GET
	@Path("/statementSummary")
	public StatementSummaryDto getSummary(
			@QueryParam("startdate") Long startDate,
			@QueryParam("enddate") Long endDate) {
		return helper.getSummary(memberId, startDate == null ? null : new Date(
				startDate), endDate == null ? null : new Date(endDate));
	}
}
