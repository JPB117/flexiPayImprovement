package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.CoursesDaoHelper;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.CPDResource;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDFooterDto;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.MemberCPDDto;

@Produces(MediaType.APPLICATION_JSON)
@Api(value = "", description = "Handles CRUD on cpd data")
public class CPDResourceImpl implements CPDResource {
	Logger logger = Logger.getLogger(CPDResourceImpl.class);

	@Inject
	CPDDaoHelper helper;
	@Inject
	CPDDao cpdDao;
	@Inject
	CoursesDaoHelper cHelper;

	private final String memberId;

	@Inject
	public CPDResourceImpl(@Assisted String memberId) {
		this.memberId = memberId;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a list of all cpds", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
	public List<CPDDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {
		return helper.getAllCPD(memberId, offset, limit, startDate, endDate,
				null);
	}

	@GET
	@Path("/cpdFormatted")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<CPDDto> getAllWithDates(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {
		return helper.getAllCPDWithStringDates(memberId, offset, limit,
				startDate, endDate, null);
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/cpdmemberSummary")
	@ApiOperation(value = "Get a list of all cpds", response = MemberCPDDto.class, consumes = MediaType.APPLICATION_JSON)
	public List<MemberCPDDto> getAllMemberSummary(
			@QueryParam("searchTerm") String searchTerm,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return helper.getAllMemberCPDSummary(searchTerm, offset, limit);
	}

	@GET
	@Path("/summary")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public CPDSummaryDto getCPDSummary(@QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {
		return helper.getCPDSummary(memberId, startDate, endDate);
	}

	@GET
	@Path("/filteredcount")
	public Integer getCount(@QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {
		return helper.getCount(memberId, startDate, endDate);
	}

	@GET
	@Path("/{cpdId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a cpd by cpdId", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
	public CPDDto getById(
			@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId) {
		CPDDto cpd = helper.getCPDFromRefId(cpdId);
		return cpd;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new cpd", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
	public CPDDto create(CPDDto cpd) {
		logger.error("++++++ API CALL ++++++++");
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.error(mapper.writeValueAsString(cpd));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		if (cpd.getLmsMemberId() == null) {
			return helper.create(memberId, cpd);
		} else {
			logger.error("++++++ API CALL EXECUTE ++++++++");
			return helper.create(cpd);
		}

	}

	@PUT
	@Path("/{cpdId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing cpd", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public CPDDto update(
			@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId,
			CPDDto cpd) {

		return helper.update(memberId, cpdId, cpd);

	}

	@DELETE
	@Path("/{cpdId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing cpd")
	public void delete(
			@ApiParam(value = "CPD Id of the cpd to fetch", required = true) @PathParam("cpdId") String cpdId) {

		helper.delete(memberId, cpdId);
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return helper.getCount(memberId, null, null);
	}

	@GET
	@Path("/summaryCount")
	public Integer getMemberSummaryCount(
			@QueryParam("searchTerm") String searchTerm) {
		return helper.getMemberCPDCount(searchTerm);
	}

	@GET
	@Path("/SearchCPD")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<CPDDto> searchCPd(@QueryParam("offset") int offset,
			@QueryParam("limit") int limit,
			@QueryParam("searchTerm") String searchTerm,
			@QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {
		return helper.getAllCPD(memberId, offset, limit, startDate, endDate,
				searchTerm);
	}

	@GET
	@Path("/countCPDSearch")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getCPDsearchCount(@QueryParam("searchTerm") String searchTerm) {
		return helper.cpdSearchCount(searchTerm);
	}

	@GET
	@Path("/yearSummaries")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<CPDFooterDto> getYearSummaries(
			@QueryParam("startDate") Long startDate,
			@QueryParam("endDate") Long endDate) {
		return helper.getYearSummaries(memberId, startDate, endDate);
	}

}
