package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.icpak.rest.factory.ResourceFactory;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

@Path("members")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "members", description = "Handles CRUD for Members")
public class MemberResourceImpl implements MemberResource {

	@Inject
	ResourceFactory resourceFactory;

	@Inject
	private InvoiceDaoHelper helper;

	@Inject
	MemberDaoHelper membersHelper;
	@Inject
	CPDDaoHelper cpdHelper;
	@Inject
	BookingsDaoHelper bookingsDaoHelper;

	@GET
	@ApiOperation(value = "Retrieve all active members")
	public List<MemberDto> getAll(
			@ApiParam(value = "Starting index", required = true) @QueryParam("offset") Integer offset,
			@ApiParam(value = "Number of items to retrieve", required = true) @QueryParam("limit") Integer limit) {
		return membersHelper.getAllMembers(offset, limit, "", null);
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return membersHelper.getCount();
	}

	@Path("/{memberId}")
	@GET
	@ApiOperation(value = "Retrieve a member")
	public MemberDto getMember(
			@ApiParam(value = "memberId", required = true) @PathParam("memberId") String memberId) {
		return membersHelper.getMemberById(memberId);
	}

	@GET
	@Path("/{memberId}/standing")
	public MemberStanding getMemberStanding(
			@ApiParam(value = "memberId", required = true) @PathParam("memberId") String memberId) {

		return cpdHelper.getMemberStanding(memberId);
	}

	@GET
	@Path("/{memberId}/loadFromErp")
	public Boolean getDataFromErp(
			@ApiParam(value = "memberId", required = true) @PathParam("memberId") String memberId,
			@QueryParam("forceRefresh") Boolean forceRefresh) {
		return membersHelper.loadFromErp(memberId, forceRefresh);
	}

	@GET
	@Path("/search/{searchTerm}")
	@ApiOperation(value = "Search for members based on a search term (MemberId/ Names)")
	public List<MemberDto> search(
			@ApiParam(value = "Search term to use", required = true) @PathParam("searchTerm") String searchTerm,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {

		return membersHelper.getAllMembers(offset, limit, "", searchTerm);
	}

	// @GET
	// @Consumes(MediaType.APPLICATION_JSON)
	// @ApiParam(value="Number of items to retrieve", required=true)
	// public List<MemberDto> getAll(@QueryParam("offset") Integer offset,
	// @QueryParam("limit") Integer limit, @QueryParam("limit") String
	// searchTerm){
	//
	// return membersHelper.getAllMembers(offset, limit, "", searchTerm);
	// }
	//

	@Path("/{memberId}/invoice")
	@GET
	@ApiOperation(value = "Search for member invoices")
	public List<InvoiceDto> getAllInvoicesForMember(
			@ApiParam(value = "Member Id", required = true) @PathParam("memberId") String memberId) {

		return helper.getAllInvoices(memberId, 0, 50);
	}

	@Path("/{memberId}/statements")
	public StatementResourceImpl statements(
			@PathParam("memberId") String memberId) {
		return resourceFactory.createStatementResource(memberId);
	}

	@Path("/cpd")
	public CPDResourceImpl cpd(@QueryParam("memberId") String memberId) {
		return resourceFactory.createCPDResource(memberId);
	}

	@Path("/{memberId}/enquiries")
	public EnquiriesResourceImpl enquiries(
			@PathParam("memberId") String memberId) {
		return resourceFactory.createEnquiriesResource(memberId);
	}

	@GET
	@Path("/{memberId}/bookings")
	public List<MemberBookingDto> getMemberBookings(
			@PathParam("memberId") String memberId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return bookingsDaoHelper.getMemberBookings(memberId, offset, limit);
	}

	@GET
	@Path("/frontMembers")
	public List<MemberDto> getMembers(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return membersHelper.getMembers(offset, limit);
	}

	@GET
	@Path("/frontMemberCount")
	public Integer getMembersCount() {
		return membersHelper.getMembersCount("all", "all", "all");
	}

	@GET
	@Path("/frontMembers/{searchTerm}/{citySearchTerm}/{categoryName}")
	public List<MemberDto> searchMembers(
			@PathParam("searchTerm") String searchTerm,
			@PathParam("citySearchTerm") String citySearchTerm,
			@PathParam("categoryName") String categoryName,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return membersHelper.getMembersFromOldTable(searchTerm, citySearchTerm,
				categoryName, offset, limit);
	}

	@GET
	@Path("/frontMembers/searcCount/{searchTerm}/{citySearchTerm}/{categoryName}")
	public Integer getMembersSearchCount(
			@PathParam("searchTerm") String searchTerm,
			@PathParam("citySearchTerm") String citySearchTerm,
			@PathParam("categoryName") String categoryName) {
		return membersHelper.getMembersCount(searchTerm, citySearchTerm,
				categoryName);
	}

}
