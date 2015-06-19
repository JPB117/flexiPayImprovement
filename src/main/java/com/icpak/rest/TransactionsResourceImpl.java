package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.workpoint.icpak.shared.api.TransactionsResource;
import com.workpoint.icpak.shared.trx.TransactionDto;

@Path("transactions")
public class TransactionsResourceImpl implements TransactionsResource {

	@Inject
	TransactionDaoHelper trxDaoHelper;

	@POST
	public void makePayment(@QueryParam("refId") String paymentRef,
			@QueryParam("status") String status,
			@QueryParam("paymentMode") String paymentMode) {

		trxDaoHelper.receivePayment(paymentRef, paymentMode, status);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionDto> getAllTrxs(@QueryParam("userId") String userId) {

		return trxDaoHelper.getTransactions(userId);
	}
}
