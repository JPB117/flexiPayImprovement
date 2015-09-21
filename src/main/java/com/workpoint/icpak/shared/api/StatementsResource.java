package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.model.statement.StatementDto;


@Produces(MediaType.APPLICATION_JSON)
public interface StatementsResource extends BaseResource{
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all member statement")
	public List<StatementDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
}
