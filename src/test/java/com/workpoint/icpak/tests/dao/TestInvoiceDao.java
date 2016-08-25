package com.workpoint.icpak.tests.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.InvoiceDaoHelper;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.icpak.rest.models.event.Booking;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.server.payment.Utilities;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.TransactionDto;
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
		Booking booking = bookingsDao.findByRefId("xTNqziYTSzZrh4E8", Booking.class);
		Assert.assertNotNull(booking);

		bookingHelper.generateInvoice(booking);
	}

	@Ignore
	public void getCounts() {
		int count = invoiceHelper.getInvoiceCount();
		System.err.println(count);
	}

	// @Test
	public void getInvoiceByDocNo() {
		InvoiceDto invoice = invoiceDao.getInvoiceByDocumentNo("INV-0024");
		System.err.println(invoice.getDescription());
	}

	// @Test
	public void getAllInvoices() {
		List<TransactionDto> allTrxs = invoiceDao.getAllTransactions("ALL", "", "2016-03-01 00:00:00",
				"2016-03-30 00:00:00", "BOOKING", "MPESA", 0, 10000);
		System.err.println("List Size>>" + allTrxs.size());
		System.err.println("Database Count>>" + invoiceDao.getTransactionsCount("ALL", "", "2016-03-01 00:00:00",
				"2016-03-30 00:00:00", "BOOKING", "MPESA"));
	}

	// @Test
	public void TestCheckPayment() {
		List<InvoiceDto> invoice = invoiceDao.checkInvoicePaymentStatus("yO8QEZ1A8c1riDsN");
		System.err.println(">>>>" + invoice.get(0).getStatus());
	}

	// @Test
	public void testPayment() {
		trxHelper.receivePaymentUsingInvoiceNo("DFAFDAF64G", "722722", "INV5496", "MPESA", "DFAFDAF61G", "254729472421",
				"227000", "2016-01-31 18:42:08", "N/A");
	}

	@Ignore
	public void checkPaymentStatus() {
		System.err.println("Invoice>>>" + invoiceHelper.checkInvoicePaymentStatus("c0qgum0Bn8oxyLhO"));
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

	@Ignore
	public void testInvoiceLine() throws FileNotFoundException, IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {
		List<InvoiceLineDto> invoiceLineDtos = invoiceDao.getInvoiceLinesByDocumentNo("INV-0045");

		byte[] doc = bookingHelper.generateInvoicePdf("vp5euYle2YLN2bxn");

		FileOutputStream output = new FileOutputStream(new File("/home/wladek/Documents/profoma.pdf"));
		IOUtils.write(doc, output);

	}

	@Test
	public void testChargeableAmount() {
		String invoiceRefid = "jjGL44t6rxum4Dx6";
		InvoiceDto inv = invoiceHelper.getInvoice(invoiceRefid);
		System.err.println("Chargable Amount:::" + inv.getChargeableAmount());

	}

	// @Test
	public void testLocalTransaction() {
		String sample = "1{\"Id\":\"b2ff1658-c81b-cf0a-f7ae-08d3c0ec5647\",\"MSISDN\":\"254729472421\",\"BusinessShortCode\":\"722722\",\"InvoiceNumber\":\"\",\"TransID\":\"KHA7FGYRQT\",\"TransAmount\":\"10.00\",\"ThirdPartyTransID\":\"\",\"TransTime\":\"20160810100240\",\"BillRefNumber\":\"7087\",\"KYCInfoList\":[{\"KYCName\":\"[Personal Details][First Name]\",\"KYCValue\":\"TOM\"},{\"KYCName\":\"[Personal Details][Middle Name]\",\"KYCValue\":\"KIMANI\"},{\"KYCName\":\"[Personal Details][Last Name]\",\"KYCValue\":\"MURIRANJA\"}]";
		trxHelper.performServerIPN(sample);
	}

	// @Test
	public void testRedoTransactions() {

		String fileLocation = "C:\\Users\\user\\Desktop\\august_trx_json.txt";
		BufferedReader br = null;
		String fileLine = "";

		try {
			br = new BufferedReader(new FileReader(fileLocation));
		} catch (FileNotFoundException fileNotFound) {
			System.err.println("File Not found..");
			return;
		}

		try {
			while ((fileLine = br.readLine()) != null) {
				if (!fileLine.equals("=============================")) {
					trxHelper.performServerIPN(fileLine);
					System.err.println("Executed>>>" + fileLine);
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

}
