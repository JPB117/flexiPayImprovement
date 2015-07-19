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

@Path("members")
@Produces(MediaType.APPLICATION_JSON)
public interface MemberResource {
	
	
	@GET
	public List<MemberDto> getAll(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	@GET
	@Path("/search/{searchTerm}")
	public List<MemberDto> search(@QueryParam("searchTerm") String searchTerm);

	@Path("/{memberId}/cpd")
	public CPDResource cpd(@PathParam("memberId") String memberId);
	
	@Path("/{memberId}/invoice")
	@GET
	public List<InvoiceDto> getAllInvoicesForMember(@PathParam("memberId") String memberId);
}
