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

@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResourceImpl implements InvoiceResource{

	
	@Inject
	private InvoiceDaoHelper helper;
	
	@GET
	@Path("/count")
	public Integer getCount() {
		return helper.getInvoiceCount();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<InvoiceDto> getInvoices(@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit){
		return helper.getAllInvoices(offset,limit);
	}

	@Path("/{invoiceref}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public InvoiceDto getInvoice(@PathParam("invoiceref") String invoiceRef){
		return helper.getInvoice(invoiceRef);
	}
	
}
