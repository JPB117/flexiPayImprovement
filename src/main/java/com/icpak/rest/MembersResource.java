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

import org.apache.shiro.authz.annotation.RequiresAuthentication;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.icpak.rest.models.membership.Member;
import com.sun.jersey.api.core.InjectParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@RequiresAuthentication
@Path("members")
@Api(value = "members", description = "Handles CRUD on member data")
public class MembersResource extends BaseResource<Member> {

	@Inject
	MemberDaoHelper helper;

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a list of all members", response = Member.class, consumes = MediaType.APPLICATION_JSON)
	public Response getAll(@Context UriInfo uriInfo,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {

		return buildCollectionResponse(helper.getAllMembers(offset, limit,
				uriInfo));
	}

	@GET
	@Path("/{memberId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a member by memberId", response = Member.class, consumes = MediaType.APPLICATION_JSON)
	public Response getById(
			@Context UriInfo uriInfo,
			@ApiParam(value = "Member Id of the member to fetch", required = true) @PathParam("memberId") String memberId) {

		Member member = helper.getMemberById(memberId);
		return buildGetEntityResponse(uriInfo.getAbsolutePath().toString(),
				member);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new member", response = Member.class, consumes = MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, Member member) {

		helper.createMember(member);
		String uri = uriInfo.getAbsolutePath().toString() + "/"
				+ member.getRefId();
		return buildCreateEntityResponse(uri, member);
	}

	@PUT
	@Path("/{memberId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing member", response = Member.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Response update(
			@Context UriInfo uriInfo,
			@ApiParam(value = "Member Id of the member to fetch", required = true) @PathParam("memberId") String memberId,
			Member member) {

		helper.updateMember(memberId, member);
		String uri = uriInfo.getAbsolutePath().toString();
		return buildUpdateEntityResponse(uri, member);
	}

	@DELETE
	@Path("/{memberId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing member")
	public Response delete(
			@ApiParam(value = "Member Id of the member to fetch", required = true) @PathParam("memberId") String memberId) {

		helper.deleteMember(memberId);

		return buildDeleteEntityResponse();
	}
	
	/**
	 * Member CPD
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{memberId}/cpd")
	public CPDResource bookings(@InjectParam CPDResource resource){
		return resource;
	}
	
	/**
	 * Member Education
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{memberId}/education")
	public EducationResource education(@InjectParam EducationResource resource){
		return resource;
	}
	
	/**
	 * Member Training And Experience
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{memberId}/training")
	public TrainingAndExperienceResource bookings(@InjectParam TrainingAndExperienceResource resource){
		return resource;
	}
	
	/**
	 * Member Training And Experience
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{memberId}/specialization")
	public SpecializationResource bookings(@InjectParam SpecializationResource resource){
		return resource;
	}
	
	/**
	 * Member Criminal Offenses
	 * 
	 * @param resource
	 * @return
	 */
	@Path("/{memberId}/offenses")
	public CriminalOffensesResource bookings(@InjectParam CriminalOffensesResource resource){
		return resource;
	}

}
