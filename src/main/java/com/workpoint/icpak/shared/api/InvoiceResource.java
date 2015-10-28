package com.workpoint.icpak.shared.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceSummary;

@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
public interface InvoiceResource extends BaseResource {

	@GET
	@Path("/{memberId}/list")
	public List<InvoiceDto> getInvoices(@PathParam("memberId") String memberId,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

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
