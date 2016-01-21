package com.workpoint.icpak.tests.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.icpak.rest.models.event.Booking;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestInvoiceDao extends AbstractDaoTest {

	Logger logger = Logger.getLogger(TestInvoiceDao.class);

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
		InvoiceDto invoice = invoiceDao.getInvoiceByDocumentNo("INV-0024");
		System.err.println(invoice.getDescription());
	}

	@Ignore
	public void testPayment() {
		trxHelper.receivePaymentUsingInvoiceNo("INV-0121", "722722",
				"INV-0121", "MPESA", "KAL7VZQU55", "254729472421", "26000");
	}

	@Ignore
	public void checkPaymentStatus() {
		System.err.println("Invoice>>>"
				+ invoiceHelper.checkInvoicePaymentStatus("c0qgum0Bn8oxyLhO"));
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

	@Test
	public void testInvoiceLine() throws FileNotFoundException, IOException,
			SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		List<InvoiceLineDto> invoiceLineDtos = invoiceDao
				.getByLinesByDocumentNo("INV-0045");

		byte[] doc = bookingHelper.generateInvoicePdf("vp5euYle2YLN2bxn");

		FileOutputStream output = new FileOutputStream(new File(
				"/home/wladek/Documents/profoma.pdf"));
		IOUtils.write(doc, output);

	}

}
