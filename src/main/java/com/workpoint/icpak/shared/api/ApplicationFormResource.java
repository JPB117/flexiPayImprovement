package com.workpoint.icpak.shared.api;

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

import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.InvoiceDto;

@Path("applications")
public interface ApplicationFormResource extends BaseResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormHeaderDto> getAll(
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm,
			@QueryParam("paymentStatus") String paymentStatus,
			@QueryParam("applicationStatus") String applicationStatus);

	@GET
	@Path("/summary")
	public ApplicationSummaryDto getSummary();

	@Path("/{applicationId}/education")
	public EducationResource education(
			@PathParam("applicationId") String applicationId);

	@Path("/{applicationId}/accountancy")
	public AccountancyResource accountancy(
			@PathParam("applicationId") String applicationId);

	@Path("/{applicationId}/trainings")
	public TrainingsResource training(
			@PathParam("applicationId") String applicationId);

	@Path("/{applicationId}/specializations")
	public SpecializationsResource specialization(
			@PathParam("applicationId") String applicationId);

	@Path("/{applicationId}/employment")
	public EmploymentResource employment(
			@PathParam("applicationId") String applicationId);

	@GET
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto getById(
			@PathParam("applicationId") String applicationId);

	@GET
	@Path("/{applicationId}/invoice")
	@Produces(MediaType.APPLICATION_JSON)
	public InvoiceDto getInvoiceByBookingId(
			@PathParam("applicationId") String applicationId,
			@QueryParam("download") String download);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto create(ApplicationFormHeaderDto application);

	@PUT
	@Path("/{applicationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto update(
			@PathParam("applicationId") String applicationId,
			ApplicationFormHeaderDto application);

	@DELETE
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("applicationId") String applicationId);

	@GET
	@Path("/searchCount")
	public Integer getSearchCount(@QueryParam("searchTerm") String searchTerm,
			@QueryParam("paymentStatus") String paymentStatus,
			@QueryParam("applicationStatus") String appStatus);

	@POST
	@Path("/subscribe/{applicationRefId}/{branchName}")
	@Consumes(MediaType.APPLICATION_JSON)
	public String subscribeToBranch(
			@PathParam("applicationRefId") String applicationRefId,
			@PathParam("branchName") String branchName);

}
