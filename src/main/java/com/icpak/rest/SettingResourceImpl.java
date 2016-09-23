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
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.SettingDaoHelper;
import com.icpak.rest.models.settings.Setting;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.SettingResource;
import com.workpoint.icpak.shared.model.settings.SettingDto;

@Path("settings")
@Api(value = "settings", description = "Handles CRUD for Settings")
public class SettingResourceImpl implements SettingResource {

	@Inject
	SettingDaoHelper helper;

	private String getUri() {
		return "";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve all active Categories")
	public List<SettingDto> getAll() {
		List<SettingDto> dtos = helper.getAllSettings();
		return dtos;
	}

	@GET
	@Path("/{settingId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a setting by settingId", response = SettingDto.class, consumes = MediaType.APPLICATION_JSON)
	public SettingDto getById(
			@ApiParam(value = "settingDto Id of the setting to fetch", required = true) @PathParam("settingId") String settingId) {
		String uri = getUri();
		SettingDto dto = helper.getSettingById(settingId).toDto();
		dto.setUri(uri);
		return dto;
	}

	@GET
	@Path("/getBySettingName/{settingName}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Get a setting by settingName", response = SettingDto.class, consumes = MediaType.APPLICATION_JSON)
	public SettingDto getBySettingName(
			@ApiParam(value = "settingDto Id of the setting to fetch", required = true) @PathParam("settingName") String settingName) {
		Setting setting = helper.getSettingByName(settingName);
		if (setting != null) {
			return setting.toDto();
		} else {
			return new SettingDto();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new setting", response = SettingDto.class, consumes = MediaType.APPLICATION_JSON)
	public SettingDto create(SettingDto setting) {
		helper.createSetting(setting);
		String uri = getUri() + "/" + setting.getRefId();
		setting.setUri(uri);
		return setting;
	}

	@PUT
	@Path("/{settingId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update an existing setting", response = SettingDto.class, consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public SettingDto update(
			@ApiParam(value = "settingDto Id of the setting to update", required = true) @PathParam("settingId") String settingId,
			SettingDto setting) {
		helper.updateSetting(settingId, setting);
		setting.setUri(getUri());
		return setting;
	}

	@DELETE
	@Path("/{settingId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete an existing setting")
	public void delete(
			@ApiParam(value = "settingDto Id of the setting to delete", required = true) @PathParam("settingId") String settingId) {
		helper.deleteSetting(settingId);
	}

}
