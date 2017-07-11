package com.workpoint.icpak.shared.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;


@Path("creditCardPayments")
public interface CreditCardResource extends BaseResource {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreditCardResponse postPayment(CreditCardDto dto);
}