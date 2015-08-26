package com.workpoint.icpak.tests.dao;

import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.models.event.Booking;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestInvoiceDao extends AbstractDaoTest {

	@Inject
	InvoiceDaoHelper invoiceHelper;
	@Inject BookingsDaoHelper bookingHelper;
	@Inject BookingsDao bookingsDao;
	
	@Test
	public void createFromBooking(){
		Booking booking = bookingsDao.findByRefId("xTNqziYTSzZrh4E8", Booking.class);
		Assert.assertNotNull(booking);
		
		bookingHelper.generateInvoice(booking);
	}
	
	@Ignore
	public void getCounts(){
		int count = invoiceHelper.getInvoiceCount();
		System.err.println(count);
	}
	
	@Ignore
	public void getInvoice(){
		InvoiceDto dto = invoiceHelper.getInvoice("5JibXv2kxP9LMhyp");
		for(InvoiceLineDto line : dto.getLines()){
			System.err.println(line.getRefId()+" >> "+line.getMemberId());
		}
		
	}
	
	@Ignore
	public void create() {
		InvoiceDto invoice = new InvoiceDto();

		double amount = 0.0;
		for (int i = 0; i < 10; i++) {
			InvoiceLineDto dto = new InvoiceLineDto();
			dto.setDescription("fsafdsf" + 1);
			dto.setUnitPrice(20 * i);
			dto.setTotalAmount(20 * i);
			invoice.addLine(dto);

			amount += dto.getUnitPrice();
		}
		invoice.setAmount(amount);
		invoice.setDocumentNo((new Random()).nextInt() + "");
		invoice.setCompanyName("EEEWr");
		invoice.setDate(new Date().getTime());
		invoice.setCompanyAddress("P.o Box 23213");
		invoice.setContactName("Kimani");
		invoice.setPhoneNumber("0230343");
		invoice.setBookingRefId("433dsfsfds");
		
		invoiceHelper.save(invoice);
	}

}
