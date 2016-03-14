package com.icpak.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.wordnik.swagger.annotations.ApiOperation;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.TransactionDto;

@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResourceImpl implements InvoiceResource {

	@Inject
	InvoiceDaoHelper helper;
	@Inject
	TransactionDaoHelper trxHelper;

	@GET
	@Path("/count/{memberId}")
	public Integer getCount(@PathParam("memberId") String memberId) {
		return helper.getInvoiceCount(memberId);
	}

	@GET
	@Path("/count")
	public Integer getCount() {
		return helper.getInvoiceCount();
	}

	@GET
	@Path("/{memberId}/summary")
	public InvoiceSummary getSummary(@PathParam("memberId") String memberId) {
		return helper.getSummary(memberId);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new cpd", response = CPDDto.class, consumes = MediaType.APPLICATION_JSON)
	public TransactionDto create(TransactionDto trx) {
		return trxHelper.saveTransaction(trx);
	}

	@GET
	@Path("/{memberId}/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TransactionDto> getInvoices(
			@PathParam("memberId") String memberId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit) {
		return helper.getAllInvoices(memberId, offset, limit);
	}

	@Path("/{invoiceref}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public InvoiceDto getInvoice(@PathParam("invoiceref") String invoiceRef) {
		return helper.getInvoice(invoiceRef);
	}

	@Path("checkpayment/{invoiceref}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public InvoiceDto checkPaymentStatus(
			@PathParam("invoiceref") String invoiceRef) {
		return helper.checkInvoicePaymentStatus(invoiceRef);
	}

}
