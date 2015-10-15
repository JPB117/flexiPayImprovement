package com.workpoint.icpak.tests.dao;

import java.util.Date;
import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.icpak.rest.models.event.Booking;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestInvoiceDao extends AbstractDaoTestTestoTest {

	@Inject
	InvoiceDaoHelper invoiceHelper;
	@Inject
	BookingsDaoHelper bookingHelper;
	@Inject
	BookingsDao bookingsDao;
	@Inject
	TransactionDaoHelper trxHelper;

	@Inject
	InvoiceDao invoiceDao;

	@Ignore
	public void createFromBooking() {
		Booking booking = bookingsDao.findByRefId("xTNqziYTSzZrh4E8",
				Booking.class);
		Assert.assertNotNull(booking);

		bookingHelper.generateInvoice(booking);
	}

	@Ignore
	public void getCounts() {
		int count = invoiceHelper.getInvoiceCount();
		System.err.println(count);
	}

	@Ignore
	public void getInvoices() {
		// CQuyDBuiNLZPwPRX
		// List<InvoiceDto> dtos = invoiceHelper.getAllInvoices("ALL", 0, 10);
		// System.err.println(dtos.size());

		InvoiceDto invoice = invoiceDao.getInvoiceByDocumentNo("INV-0024");
		System.err.println(invoice.getDescription());
	}

	@Test
	public void testPayment() {
		trxHelper.receivePaymentUsingInvoiceNo("INV-0030", "722722",
				"INV-0030", "", "JGV1SYJTR5", "");
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
