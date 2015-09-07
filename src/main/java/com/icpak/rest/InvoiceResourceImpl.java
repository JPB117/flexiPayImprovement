package com.icpak.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.workpoint.icpak.shared.api.InvoiceResource;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceSummary;

@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResourceImpl implements InvoiceResource {

	@Inject
	private InvoiceDaoHelper helper;

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
	public InvoiceSummary getSummary(@PathParam("memberId") String memberId){
		return helper.getSummary(memberId);
	}

	@GET
	@Path("/{memberId}/list")
	@Produces(MediaType.APPLICATION_JSON)
	public List<InvoiceDto> getInvoices(@PathParam("memberId") String memberId,
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

}
