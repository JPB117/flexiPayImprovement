package com.icpak.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TrainingAndExperienceDaoHelper;
import com.icpak.rest.models.membership.TrainingAndExperience;
import com.icpak.rest.models.membership.TrainingExperienceType;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value="", description="Handles CRUD for event TrainingAndExperience")
public class TrainingAndExperienceResource extends BaseResource<TrainingAndExperience>{

	@Inject TrainingAndExperienceDaoHelper helper;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active training entries")
	public Response getAll(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which training entries are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllTrainingEntrys(uriInfo, memberId, offset, limit));
	}
	
	@GET
	@Path("training")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active training entries")
	public Response getAllTraining(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which training entries are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllTrainingEntrys(uriInfo, memberId, TrainingExperienceType.TRAINING, offset, limit));
	}
	
	@GET
	@Path("general")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active training entries")
	public Response getAllGeneral(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which training entries are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllTrainingEntrys(uriInfo, memberId,TrainingExperienceType.TRAINING, offset, limit));
	}
	
	@GET
	@Path("audit")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all active training entries")
	public Response getAllAudit(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which training entries are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Starting point to fetch") @QueryParam("offset") Integer offset,
			@ApiParam(value="No of Items to fetch") @QueryParam("limit") Integer limit) {
		
		return buildCollectionResponse(helper.getAllTrainingEntrys(uriInfo, memberId,TrainingExperienceType.TRAINING, offset, limit));
	}

	@GET
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Get a training by eduEntryId", response=TrainingAndExperience.class, consumes=MediaType.APPLICATION_JSON)
	public Response getById(@Context UriInfo uriInfo, 
			@ApiParam(value="Member for which the training details are requested") @PathParam("memberId") String memberId,
			@ApiParam(value="Entry Id of the training to fetch", required=true) @PathParam("eduEntryId") String eduEntryId) {
		
		TrainingAndExperience training = helper.getTrainingEntryById(memberId,eduEntryId);
		//training.getEvent().setUri(uriInfo.getBaseUri()+"events/"+training.getEvent().getRefId());
		//training.getUser().setUri(uriInfo.getBaseUri()+"users/"+training.getUser().getRefId());
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(), training);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Create a new training", response=TrainingAndExperience.class, consumes=MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo,
			@ApiParam(value="Member for which training is being created") @PathParam("memberId") String memberId,
			TrainingAndExperience training) {
		
		training = helper.createTrainingEntry(memberId,training);
		String uri = uriInfo.getAbsolutePath()+"/"+training.getRefId();
		//training.getEvent().setUri(uriInfo.getBaseUri()+"events/"+training.getEvent().getRefId());
//		training.getUser().setUri(uriInfo.getBaseUri()+"users/"+training.getUser().getRefId());
		
		return buildCreateEntityResponse(uri, training);
	}

	@PUT
	@Path("/{eduEntryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Update an existing training", response=TrainingAndExperience.class, 
	consumes=MediaType.APPLICATION_JSON, produces=MediaType.APPLICATION_JSON)
	public Response update(@Context UriInfo uriInfo, 
			@ApiParam(value="Member for which training is being updated") @PathParam("memberId") String memberId,
			@ApiParam(value="Entry Id of the training to update", required=true) @PathParam("eduEntryId") String eduEntryId, 
			TrainingAndExperience training) {
		
		training = helper.updateTrainingEntry(memberId,eduEntryId, training);
		//training.getEvent().setUri(uriInfo.getBaseUri()+"events/"+training.getEvent().getRefId());
//		training.getUser().setUri(uriInfo.getBaseUri()+"users/"+training.getUser().getRefId());
		return buildUpdateEntityResponse(uriInfo.getAbsolutePath().toString(), training);
	}

	@DELETE
	@Path("/{eduEntryId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Delete an existing training")
	public Response delete(
			@ApiParam(value="Member from which training is being deleted") @PathParam("memberId") String memberId,
			@ApiParam(value="Entry Id of the training to delete", required=true) @PathParam("eduEntryId") String eduEntryId) {
		helper.deleteTrainingEntry(memberId,eduEntryId);
		return buildDeleteEntityResponse();
	}

}
