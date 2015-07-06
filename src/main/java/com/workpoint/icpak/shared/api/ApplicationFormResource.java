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

import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.InvoiceDto;

@Path("applications")
public interface ApplicationFormResource{

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ApplicationFormHeaderDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	@GET
	@Path("/{applicationId}")
	@Produces(MediaType.APPLICATION_JSON)
	public ApplicationFormHeaderDto getById(@PathParam("applicationId") String applicationId);
	
	@GET
	@Path("/{applicationId}/invoice")
	@Produces(MediaType.APPLICATION_JSON)
	public InvoiceDto getInvoiceByBookingId(@PathParam("applicationId") String applicationId,
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
	
	/**
	 * Member CPD
	 * 
	 * @param resource
	 * @return
	 */
//	@Path("/{applicationId}/cpd")
//	public CPDResource bookings(@InjectParam CPDResource resource){
//		return resource;
//	}
	
	/**
	 * Member Education
	 * 
	 * @param resource
	 * @return
	 */
//	@Path("/{applicationId}/education")
//	public EducationResource education(@InjectParam EducationResource resource){
//		return resource;
//	}
	
	/**
	 * Member Training And Experience
	 * 
	 * @param resource
	 * @return
	 */
//	@Path("/{applicationId}/training")
//	public TrainingAndExperienceResource bookings(@InjectParam TrainingAndExperienceResource resource){
//		return resource;
//	}s
	
	/**
	 * Member Training And Experience
	 * 
	 * @param resource
	 * @return
	 */
//	@Path("/{applicationId}/specialization")
//	public SpecializationResource bookings(@InjectParam SpecializationResource resource){
//		return resource;
//	}
	
	/**
	 * Member Criminal Offenses
	 * 
	 * @param resource
	 * @return
	 */
//	@Path("/{applicationId}/offenses")
//	public CriminalOffensesResource bookings(@InjectParam CriminalOffensesResource resource){
//		return resource;
//	}
}
