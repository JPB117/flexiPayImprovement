package com.icpak.rest;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.server.payment.CreditCardServiceImpl;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.api.CreditCardResource;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;

@Path("creditCardPayments")
public class CreditCardPaymentImpl implements CreditCardResource {

	@Inject
	CreditCardServiceImpl creditCardService;
	@Inject
	TransactionDaoHelper trxDaoHelper;

	@GET
	@Path("/count")
	public Integer getCount() {
		return null;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Post Payment", response = String.class, consumes = MediaType.APPLICATION_JSON)
	public CreditCardResponse postPayment(CreditCardDto dto) {
		CreditCardResponse response = creditCardService
				.authorizeCardTransaction(dto);
		// If Payment is successful..
		if (response.getStatusCode().equals("0000")) {
			// Complete the transaction
			creditCardService.completeCardTransaction(
					response.getTransactionReference(),
					response.getTransactionIndex());
			trxDaoHelper.receivePaymentUsingInvoiceNo(dto.getPaymentRefId(),
					"N/A", dto.getPaymentRefId(), "CARDS",
					response.getTransactionIndex(), "N/A", dto.getAmount(),
					ServerDateUtils.FULLTIMESTAMP.format(new Date()), "N/A");
		}
		return response;
	}
}