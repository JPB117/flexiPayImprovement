package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.Country;


@Path("countries")
public interface CountriesResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Country> getAll();
	
}
