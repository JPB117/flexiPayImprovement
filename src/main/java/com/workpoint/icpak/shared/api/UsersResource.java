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

import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;

@Path("users")
@Produces(MediaType.APPLICATION_JSON)
public interface UsersResource extends BaseResource {

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	public List<UserDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm);

	@GET
	@Path("/{userId}")
	public UserDto getById(@PathParam("userId") String userId);

	@GET
	@Path("/auth")
	public UserDto login(@QueryParam("username") String username,
			@QueryParam("password") String password);

	@GET
	@Path("/searchCount")
	public Integer getSearchCount(@QueryParam("searchTerm") String searchTerm);

	@POST
	@Path("/reset/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void resetAccount(@PathParam("userId") String userId);

	@POST
	@Path("/auth2")
	@Consumes(MediaType.APPLICATION_JSON)
	public LogInResult execLogin(LogInAction loginData);

	@POST
	@Path("/{userId}/account-status/activate")
	public void activateAccount(@PathParam("userId") String userId);

	@GET
	@Path("/sendActivationEmail/{userId}")
	public void sendActivationEmail(@PathParam("userId") String userId);

	@POST
	@Path("/{userId}/password")
	@Consumes(MediaType.APPLICATION_JSON)
	public void changePassword(@PathParam("userId") String userId,
			@QueryParam("password") String newPassword);

	@POST
	@Path("/{userId}/lmsPost")
	@Consumes(MediaType.APPLICATION_JSON)
	public String postUserLMS(@PathParam("userId") String userId,
			@PathParam("password") String password);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public UserDto create(UserDto user,
			@QueryParam("isSendEmail") boolean isSendEmail);

	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public UserDto update(@PathParam("userId") String userId, UserDto user);

	@DELETE
	@Path("/{userId}")
	public void delete(@PathParam("userId") String userId);

	// @PUT
	// @Path("/{userId}/password")
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public RestAction<Void> updatePassword(
	// @QueryParam("newpassword") String newPassword,
	// @PathParam("userId") String userId);
	//

}
