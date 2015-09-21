package com.icpak.rest;

import java.util.List;

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

@Api(value="", description="Handles CRUD for Statements")
@Produces(MediaType.APPLICATION_JSON)
public class StatementResourceImpl implements StatementsResource{

	@Inject StatementDaoHelper helper;
	
	
	private final String memberId;
	
	@Inject
	public StatementResourceImpl(@Assisted String memberId) {
		this.memberId = memberId;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all member statement")
	public List<StatementDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		if(offset==null){
			offset = 0;
		}
		if(limit==null){
			limit = 30;
		}
		
		List<StatementDto> dtos = helper.getAllStatements(memberId,offset, limit);
		return dtos;
	}
	
	@GET
	@Path("/count")
	public Integer getCount(){
		return helper.getCount(memberId);
	}
}
