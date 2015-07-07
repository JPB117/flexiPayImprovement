package com.workpoint.icpak.shared.api;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("members")
@Produces
public interface MemberResource {

	@Path("/{memberId}/cpd")
	public CPDResource cpd(@PathParam("memberId") String memberId);
}
