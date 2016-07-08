package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.model.events.MpesaDTO;
import com.workpoint.icpak.shared.trx.OldTransactionDto;

@Path("transactions")
public interface TransactionsResource {

	@GET
	@Path("/payment")
	public String makePayment(@QueryParam("mpesa_code") String paymentRef,
			@QueryParam("business_number") String businessNo,
			@QueryParam("mpesa_acc") String accountNo,
			@QueryParam("id") String mpesaRef,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("trxNumber") String trxNumber,
			@QueryParam("mpesaAmt") String mpesaAmt,
			@QueryParam("msisdn") String phoneNumber,
			@QueryParam("tstamp") String trxDate,
			@QueryParam("mpesa_sender") String payerNames);

	@GET
	public List<OldTransactionDto> getAllTrxs(
			@QueryParam("userId") String userId);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Receives Mpesa Info via the new api", consumes = MediaType.APPLICATION_JSON)
	public String receiveMpesaG2(MpesaDTO mpesaTrx);
}
