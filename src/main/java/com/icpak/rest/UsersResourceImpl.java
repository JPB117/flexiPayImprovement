package com.icpak.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

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
import com.icpak.rest.models.trx.Transaction;
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
import com.workpoint.icpak.shared.model.auth.AccountStatus;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;
import com.workpoint.icpak.shared.trx.TransactionDto;

/**
 * StormPath REST API Design Ideas ->
 * https://www.youtube.com/watch?v=hdSrT4yjS1g
 * 
 * <p>
 * <b>1 Big Gotcha</b> <br>
 * Resource Classes are managed by Jersey and not Guice. This is a problem if
 * you need to annotate methods with @Transactional amongst other functionality.
 * i.e. Putting @Transactional in this class (MemberResource) has no effect.
 * Guice will not begin a transaction for your request. <br>
 * A workaround is to delegate execution to a guice managed class, then inject
 * such classes to the resource class.
 * <p>
 * 
 * @author duggan
 *
 */
// @RequiresAuthentication
@Path("users")
@Api(value = "users", description = "Handles CRUD on User data")
public class UsersResourceImpl implements UsersResource {

	private final Logger logger = Logger.getLogger(UsersResourceImpl.class
			.getName());

	@Inject
	UsersDaoHelper helper;

	private HttpContext httpContext;

	public UsersResourceImpl(@Context HttpContext httpContext) {
		this.httpContext = httpContext;
	}

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a list of all users", response = User.class, consumes = MediaType.APPLICATION_JSON)
	public List<UserDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm) {
		return helper.getAllUsers(offset, limit, "", searchTerm);
	}

	@GET
	@Path("/searchCount")
	public Integer getSearchCount(@QueryParam("searchTerm") String searchTerm) {
		return helper.getCount(searchTerm);
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return helper.getCount();
	}

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a user by userId", response = User.class, consumes = MediaType.APPLICATION_JSON)
	public UserDto getById(
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		User user = helper.getUser(userId);
		return user.toDto();
	}

	@POST
	@Path("/reset/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void resetAccount(@PathParam("userId") String userId) {
		helper.resetAccount(userId);
	}

	@GET
	@Path("/auth")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Authenticate a user", response = User.class, consumes = MediaType.APPLICATION_JSON)
	public UserDto login(
			@ApiParam(value = "Username of the user to authenticate", required = true) @QueryParam("username") String username,
			@ApiParam(value = "Password of the user", required = true) @QueryParam("password") String password) {

		UserDto loggedIn = helper
				.execLogin(new LogInAction(username, password))
				.getCurrentUserDto().getUser();
		String uri = httpContext.getRequest().getPath();
		// String uri = uriInfo.getAbsolutePath().toString().replace("auth",
		// loggedIn.getRefId());
		loggedIn.setUri(uri);

		return loggedIn;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new user", response = UserDto.class, consumes = MediaType.APPLICATION_JSON)
	public UserDto create(UserDto user,
			@QueryParam("isSendEmail") boolean isSendEmail) {
		return helper.create(user, isSendEmail);
	}

	/**
	 * To test use this uri:
	 * <p/>
	 * curl -v -F 'filename=POvBCBE-PO-NRB-1_1.pdf' -F
	 * 'file=@/home/duggan/Downloads/PO_BCBE-PO-NRB-1_1.pdf;type=application/pdf
	 * ' http://localhost:8080/icpak/api/users/xIXcSQNcXmqMDrth/profile
	 * <p/>
	 * 
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
	public void create(
			@ApiParam(value = "User Id of the user", required = true) @PathParam("userId") String userId,
			@FormDataParam("Filedata") FormDataBodyPart body,
			@FormDataParam("Filedata") InputStream inputStream,
			@FormDataParam("Filedata") FormDataContentDisposition fileDisposition)
			throws IOException {

		byte[] bites = IOUtils.toByteArray(inputStream);

		String type = body.getMediaType().toString();
		System.err.format("{userId:%s, fileName:%s, size:%d, type:%s }",
				userId, fileDisposition.getFileName(), bites.length, type)
				.println();

		helper.setProfilePic(userId, bites, fileDisposition.getFileName(), type);
	}

	@GET
	@Path("/{userId}/profile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@ApiOperation(value = "Upload profile photo", produces = MediaType.APPLICATION_OCTET_STREAM)
	public Response getProfile(
			@ApiParam(value = "User Id of the user", required = true) @PathParam("userId") String userId)
			throws IOException {

		Attachment attachment = helper.getProfilePic(userId);
		return buildFileResponse(attachment.getName(),
				attachment.getContentType(), attachment.getAttachment(),
				attachment.getSize());
	}

	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing user", response = User.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public UserDto update(
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId,
			UserDto user) {

		return helper.update(userId, user);
	}

	@PUT
	@Path("/{userId}/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing user", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public void updatePassword(
			@Context UriInfo uriInfo,
			@ApiParam(value = "New Password", required = true) @QueryParam("newpassword") String newPassword,
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		helper.updatePassword(userId, newPassword);
	}

	@DELETE
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing user")
	public void delete(
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		helper.delete(userId);
	}

	@GET
	@Path("/{userId}/transactions")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a transactions by userId", response = Transaction.class, consumes = MediaType.APPLICATION_JSON)
	public List<TransactionDto> getTransactions(
			@ApiParam(value = "User Id of the user to fetch", required = true) @PathParam("userId") String userId) {

		List<TransactionDto> trxs = helper.getTransactions(userId);

		return trxs;
	}

	@POST
	@Path("/auth2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LogInResult execLogin(LogInAction loginData) {
		return helper.execLogin(loginData);
	}

	@POST
	@Path("/{userId}/account-status/activate")
	@Produces(MediaType.APPLICATION_JSON)
	public void activateAccount(@PathParam("userId") String userId) {
		helper.activateAccount(userId, AccountStatus.ACTIVATED);
	}

	@GET
	@Path("/sendActivationEmail/{userId}")
	public void sendActivationEmail(@PathParam("userId") String userId) {
		helper.sendActivationEmail(userId);
	}

	@POST
	@Path("/{userId}/password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public void changePassword(@PathParam("userId") String userId,
			@QueryParam("password") String newPassword) {
		helper.changePassword(userId, newPassword);
	}

	public Response buildFileResponse(String name, String contentType,
			final byte[] attachment, long length) {

		// StreamingOutput stream = new StreamingOutput() {
		// @Override
		// public void write(OutputStream os) throws IOException,
		// WebApplicationException {
		// //Writer writer = new BufferedWriter(new OutputStreamWriter(os));
		// IOUtils.write(attachment, os);
		// //@TODO read the file here and write to the writer
		//
		// //writer.flush();
		// }
		// };
		System.err.println("FileName= " + name);
		return Response
				.ok(attachment, contentType)
				.header("Content-Disposition",
						"attachment; filename=" + name + "; Content-Length="
								+ length).build();
	}

	@POST
	@Path("/{userId}/lmsPost")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String postUserLMS(@PathParam("userId") String userId,
			@PathParam("password") String password) {
		try {
			return helper.postUserToLMS(userId, password);
		} catch (IOException e) {
			return e.getStackTrace().toString();
		}
	}
}
