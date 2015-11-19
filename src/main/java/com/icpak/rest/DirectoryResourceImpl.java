package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.DirectoryDaoHelper;
import com.icpak.rest.factory.ResourceFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.DirectoryResource;
import com.workpoint.icpak.shared.model.DirectoryDto;

@Path("directory")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "directory", description = "Handles CRUD for Directory")
public class DirectoryResourceImpl implements DirectoryResource {

	@Inject
	ResourceFactory resourceFactory;
	@Inject
	DirectoryDaoHelper directoryDaoHelper;

	@GET
	@Path("/")
	@ApiOperation(value = "Retrieve all directories")
	public List<DirectoryDto> getAll(
			@ApiParam(value = "Starting index", required = true) @QueryParam("offset") Integer offset,
			@ApiParam(value = "Number of items to retrieve", required = true) @QueryParam("limit") Integer limit) {
		return directoryDaoHelper.getAll(offset , limit);
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return directoryDaoHelper.getCount();
	}

	@GET
	@Path("/search/{searchTerm}")
	@ApiOperation(value = "Search directory based on a search term")
	public List<DirectoryDto> search(
			@ApiParam(value = "Search term to use", required = true) @PathParam("searchTerm") String searchTerm,
			@QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {

		return directoryDaoHelper.searchDirectory(searchTerm);
	}

	@GET
	@Path("/searchCount/{searchTerm}")
	public Integer getSearchCount(@PathParam("searchTerm") String searchTerm) {
		return directoryDaoHelper.getSerchCount(searchTerm);
	}

}
