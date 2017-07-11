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
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.model.ApplicationCategoryDto;
import com.workpoint.icpak.shared.model.settings.SettingDto;

@Path("settings")
public interface SettingResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<SettingDto> getAll();

	@GET
	@Path("/{settingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public SettingDto getById(@PathParam("settingId") String categoryId);

	@GET
	@Path("/getBySettingName/{settingName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a setting by settingName", response = SettingDto.class, consumes = MediaType.APPLICATION_JSON)
	public SettingDto getBySettingName(
			@ApiParam(value = "settingDto Name of the fetched", required = true) @PathParam("settingName") String settingName);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SettingDto create(SettingDto category);

	@PUT
	@Path("/{settingId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SettingDto update(@PathParam("settingId") String categoryId, SettingDto category);

	@DELETE
	@Path("/{settingId}")
	@Produces(MediaType.APPLICATION_JSON)
	public void delete(@PathParam("settingId") String categoryId);

}
