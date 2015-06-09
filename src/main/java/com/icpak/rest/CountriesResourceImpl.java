package com.icpak.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.api.CountriesResource;
import com.workpoint.icpak.shared.model.Country;


@Path("countries")
@Api(value="countries", description="Handles CRUD for Countries")
public class CountriesResourceImpl implements CountriesResource{

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="Retrieve all countries")
	public List<Country> getAll() {
		
		String[] locales = Locale.getISOCountries();
		List<Country> countries = new ArrayList<Country>();
		for (String countryCode : locales) {
			
			Locale obj = new Locale("en-UK", countryCode);
			Country country = new Country(obj.getCountry(), obj.getDisplayCountry());
			countries.add(country);
		}
		
		return countries;
	}
	
}
