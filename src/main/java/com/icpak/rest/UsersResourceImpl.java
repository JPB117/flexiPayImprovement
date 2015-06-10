package com.icpak.rest;

import java.io.IOException;
import java.io.InputStream;

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

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.util.Attachment;
import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataParam;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.UsersResource;
import com.workpoint.icpak.shared.model.UserDto;

/**
 * StormPath REST API Design Ideas ->
 * https://www.youtube.com/watch?v=hdSrT4yjS1g
 * 
 * <p>
 * <b>1 Big Gotcha</b> <br>
 * Resource Classes are managed by Jersey and not Guice. This is a problem if
 * you need to annotate methods with @Transactional amongst other functionality.
 *  i.e. Putting @Transactional
 * in this class (MemberResource) has no effect. Guice will not begin a
 * transaction for your request. <br>
 * A workaround is to delegate execution to a guice managed class, then inject
 * such classes to the resource class.
 * <p>
 * 
 * @author duggan
 *
 */
//@RequiresAuthentication
@Path("users")
@Api(value = "users", description = "Handles CRUD on User data")
public class UsersResourceImpl extends BaseResource<User> implements UsersResource {

	@Inject UsersDaoHelper helper;

	private HttpContext httpContext;
	
	public UsersResourceImpl(@Context HttpContext httpContext) {
		this.httpContext = httpContext;
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a list of all users", response = User.class, consumes = MediaType.APPLICATION_JSON)
	public Response getAll(@Context UriInfo uriInfo,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {

		return buildCollectionResponse(helper.getAllUsers(offset, limit,
				uriInfo));
	}

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a user by userId", response = User.class, consumes = MediaType.APPLICATION_JSON)
	public UserDto getById(
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		User user = helper.getUser(userId);
		return user.getDTO();
	}
	
	@GET
	@Path("/auth")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Authenticate a user", 
	response = User.class, consumes = MediaType.APPLICATION_JSON)
	public UserDto login(
			@ApiParam(value = "Username of the user to authenticate", required = true) @QueryParam("username") String username,
			@ApiParam(value = "Password of the user", required = true) @QueryParam("password") String password){
		
		UserDto loggedIn = helper.authenticate(username, password);
		String uri = httpContext.getRequest().getPath();
		//String uri = uriInfo.getAbsolutePath().toString().replace("auth", loggedIn.getRefId());
		loggedIn.setUri(uri);
		
		return loggedIn;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new user", response = User.class, consumes = MediaType.APPLICATION_JSON)
	public Response create(@Context UriInfo uriInfo, User user) {

		helper.create(user);
		String uri = uriInfo.getAbsolutePath().toString() + "/"
				+ user.getRefId();
		return buildCreateEntityResponse(uri, user);
	}
	
	
	/**
	 * To test use this uri:
	 * <p/>
	 * curl -v -F 'filename=POvBCBE-PO-NRB-1_1.pdf' -F 'file=@/home/duggan/Downloads/PO_BCBE-PO-NRB-1_1.pdf;type=application/pdf' http://localhost:8080/icpak/api/users/xIXcSQNcXmqMDrth/profile
	 * <p/>
	 * @param userId
	 * @param inputStream
	 * @param fileDisposition
	 * @return
	 * @throws IOException
	 */
	@POST
	@Path("/{userId}/profile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Upload profile photo", consumes = MediaType.MULTIPART_FORM_DATA)
	public Response create(@ApiParam(value = "User Id of the user", required = true) @PathParam("userId") String userId,
			@FormDataParam("file") FormDataBodyPart body,
			@FormDataParam("file") InputStream inputStream,
		    @FormDataParam("file") FormDataContentDisposition fileDisposition) throws IOException {
		
		byte[] bites = IOUtils.toByteArray(inputStream);
		
		String type = body.getMediaType().toString();
		System.err.format("{userId:%s, fileName:%s, size:%d, type:%s }",userId, fileDisposition.getFileName(),
				bites.length, type).println();
		
		helper.setProfilePic(userId, bites, fileDisposition.getFileName(), type);
		
		return buildEmptySuccessResponse();
	}

	@GET
	@Path("/{userId}/profile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@ApiOperation(value = "Upload profile photo", produces = MediaType.APPLICATION_OCTET_STREAM)
	public Response getProfile(
			@ApiParam(value = "User Id of the user", required = true) @PathParam("userId") String userId
			) throws IOException {
		
		Attachment attachment = helper.getProfilePic(userId);
		return buildFileResponse(attachment.getName(), attachment.getContentType(),
				attachment.getAttachment(),attachment.getSize());
	}

	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing user", response = User.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Response update(
			@Context UriInfo uriInfo,
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId,
			User user) {

		helper.update(userId, user);
		String uri = uriInfo.getAbsolutePath().toString();
		return buildUpdateEntityResponse(uri, user);
	}
	
	@PUT
	@Path("/{userId}/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing user", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Response updatePassword(
			@Context UriInfo uriInfo,
			@ApiParam(value = "New Password", required = true) @QueryParam("newpassword") String newPassword,
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		helper.updatePassword(userId, newPassword);
		return buildEmptySuccessResponse();
	}

	@DELETE
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing user")
	public Response delete(
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		helper.delete(userId);

		return buildDeleteEntityResponse();
	}
}
