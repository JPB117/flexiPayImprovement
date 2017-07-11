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
	@Path("/frontMembers")
	public List<MemberDto> getMembers(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/frontMemberCount")
	public Integer getMembersCount();

	@GET
	@Path("/frontMembers/{searchTerm}/{citySearchTerm}/{categoryName}")
	public List<MemberDto> searchMembers(
			@PathParam("searchTerm") String searchTerm,
			@PathParam("citySearchTerm") String citySearchTerm,
			@PathParam("categoryName") String categoryName,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/frontMembers/searcCount/{searchTerm}/{citySearchTerm}/{categoryName}")
	public Integer getMembersSearchCount(
			@PathParam("searchTerm") String searchTerm,
			@PathParam("citySearchTerm") String citySearchTerm,
			@PathParam("categoryName") String categoryName);

	@GET
	@Path("/search")
	public List<MemberDto> search(@QueryParam("searchTerm") String searchTerm,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/searchIndividualMember")
	public MemberDto searchIndividualByMemberNo(
			@QueryParam("searchTerm") String searchTerm);

	@Path("/{memberId}/cpd")
	public CPDResource cpd(@PathParam("memberId") String memberId);

	@GET
	@Path("/{memberId}/standing")
	public MemberStanding getMemberStanding(
			@PathParam("memberId") String memberId);

	@GET
	@Path("/{memberId}/loadFromErp")
	public Boolean getDataFromErp(@PathParam("memberId") String memberId,
			@QueryParam("forceRefresh") Boolean forceRefresh);

	@Path("/{memberId}/statements")
	public StatementsResource statements(@PathParam("memberId") String memberId);

	@GET
	@Path("/{memberId}/invoice")
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
