package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.InvoiceLine;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;

@Transactional
public class InvoiceDaoHelper {

	@Inject InvoiceDao dao;
	
	public InvoiceDto getInvoice(String invoiceRef){
		Invoice invoice = dao.findByRefId(invoiceRef, Invoice.class);
		return invoice.toDto();
	}
	
	public InvoiceDto save(InvoiceDto dto){
		Invoice invoice = new Invoice();
		invoice.copyFrom(dto);
		
		for(InvoiceLineDto lineDto: dto.getLines()){
			InvoiceLine line = new InvoiceLine();
			line.copyFrom(lineDto);
			invoice.addLine(line);
		}
		
		dao.save(invoice);
		dao.merge(invoice);
		
		return invoice.toDto();
	}
	
	public InvoiceDto update(String invoiceRef, InvoiceDto dto){
		Invoice invoice = dao.findByRefId(invoiceRef, Invoice.class);
		invoice.copyFrom(dto);
		
		for(InvoiceLineDto lineDto: dto.getLines()){
			InvoiceLine line = new InvoiceLine();
			if(line.getRefId()!=null){
				line = dao.findByRefId(line.getRefId(), Invoice.class);
			}
			line.copyFrom(lineDto);
			invoice.addLine(line);
		}
		
		dao.save(invoice);
		
		return invoice.toDto();
	}

	public List<InvoiceDto> getAllInvoices() {
		
		List<Invoice> invoices = dao.getAllInvoices();
		List<InvoiceDto> dtos = new ArrayList<>();
		
		for(Invoice invoice: invoices){
			dtos.add(invoice.toDto());
		}
		
		return dtos;
	}

	public List<InvoiceDto> getAllInvoicesForMember(String memberId) {
		
		List<Invoice> invoices = dao.getAllInvoicesForMember(memberId);
		List<InvoiceDto> dtos = new ArrayList<>();
		
		for(Invoice invoice: invoices){
			dtos.add(invoice.toDto());
		}
		
		return dtos;
	}
}
