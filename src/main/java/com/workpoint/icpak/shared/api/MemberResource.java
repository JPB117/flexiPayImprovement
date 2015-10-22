package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

@Path("members")
@Produces(MediaType.APPLICATION_JSON)
public interface MemberResource extends BaseResource {

	@GET
	public List<MemberDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/search/{searchTerm}")
	public List<MemberDto> search(@PathParam("searchTerm") String searchTerm,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@Path("/{memberId}/cpd")
	public CPDResource cpd(@PathParam("memberId") String memberId);

	@GET
	@Path("/{memberId}/standing")
	public MemberStanding getMemberStanding(
			@PathParam("memberId") String memberId);

	@GET
	@Path("/{memberId}/loadFromErp")
	public Boolean getDataFromErp(@PathParam("memberId") String memberId,
			@PathParam("forceRefresh") Boolean forceRefresh);

	@Path("/{memberId}/statements")
	public StatementsResource statements(@PathParam("memberId") String memberId);

	@Path("/{memberId}/invoice")
	@GET
	public List<InvoiceDto> getAllInvoicesForMember(
			@PathParam("memberId") String memberId);

	@Path("/{memberId}/enquiries")
	public EnquiriesResource enquiries(@PathParam("memberId") String memberId);

	@GET
	@Path("/{memberId}/bookings")
	public List<MemberBookingDto> getMemberBookings(
			@PathParam("memberId") String memberId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

}
