package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.TransactionDto;

@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
public interface InvoiceResource extends BaseResource {

	@GET
	@Path("/{memberId}/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionDto> getAllTransactions(
			@PathParam("memberId") String memberId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("searchTerm") String searchTerm,
			@QueryParam("paymentType") String paymentType,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("fromDate") String fromDate,
			@QueryParam("endDate") String endDate);

	@GET
	@Path("/{memberId}/count")
	@Produces(MediaType.APPLICATION_JSON)
	public Integer getAllTransactionCount(
			@PathParam("memberId") String memberId,
			@QueryParam("searchTerm") String searchTerm,
			@QueryParam("paymentType") String paymentType,
			@QueryParam("paymentMode") String paymentMode,
			@QueryParam("fromDate") String fromDate,
			@QueryParam("endDate") String endDate);

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new cpd", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
	public TransactionDto create(TransactionDto trx);

	@GET
	@Path("/count/{memberId}")
	public Integer getCount(@PathParam("memberId") String memberId);

	@Path("/{invoiceref}")
	@GET
	public InvoiceDto getInvoice(@PathParam("invoiceref") String invoiceRef);

	@GET
	@Path("/{memberId}/summary")
	public InvoiceSummary getSummary(@PathParam("memberId") String memberId);

	@Path("checkpayment/{invoiceref}")
	@GET
	public InvoiceDto checkPaymentStatus(
			@PathParam("invoiceref") String invoiceRef);

}
