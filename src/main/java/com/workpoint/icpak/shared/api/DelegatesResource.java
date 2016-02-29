package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public interface DelegatesResource extends BaseResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<DelegateDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/checkExist")
	public BookingDto checkExist(@QueryParam("email") String email);

	@GET
	@Path("/searchCount")
	public Integer getSearchCount(@QueryParam("searchTerm") String searchTerm);

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Retrieve booking by qr code")
	@Path("/qrCodeSearch")
	public List<DelegateDto> getByQrCode(
			@QueryParam("searchTerm") String searchTerm);

}
