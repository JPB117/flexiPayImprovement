package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.DirectoryDto;

@Path("directory")
@Produces(MediaType.APPLICATION_JSON)
public interface DirectoryResource extends BaseResource {

	@GET
	@Path("/")
	public List<DirectoryDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/search/{searchTerm}/{townSearchTerm}")
	public List<DirectoryDto> search(
			@PathParam("searchTerm") String searchTerm,
			@PathParam("townSearchTerm") String townSearchTerm,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/count")
	public Integer getCount();

	@GET
	@Path("/searchCount/{searchTerm}/{citySearchTerm}")
	public Integer getSearchCount(@PathParam("searchTerm") String searchTerm,
			@PathParam("citySearchTerm") String citySearchTerm);

}
