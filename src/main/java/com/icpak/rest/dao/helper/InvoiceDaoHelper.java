package com.icpak.rest.dao.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.Query;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.trx.Invoice;
import com.icpak.rest.models.trx.InvoiceLine;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.TransactionDto;

@Transactional
public class InvoiceDaoHelper {

	@Inject
	InvoiceDao dao;
	@Inject
	BookingsDao bookingsDao;
	@Inject
	InvoiceDao invoiceDao;

	Locale locale = new Locale("en", "KE");
	NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
	Logger logger = Logger.getLogger(InvoiceDaoHelper.class);

	public InvoiceDto getInvoice(String invoiceRef) {
		Invoice invoice = dao.findByRefId(invoiceRef, Invoice.class, false);

		if (invoice != null) {
			InvoiceDto dto = invoice.toDto();

			// Is this a booking invoice - Add discount and Penalty details
			if (invoice.getBookingRefId() != null) {
				Booking booking = dao.findByRefId(invoice.getBookingRefId(), Booking.class);
				if (booking != null) {
					// Apply Discount && Penalties based on todays Date
					Date todaysDate = null;
					try {
						todaysDate = ServerDateUtils.DATEFORMAT_SYS
								.parse(ServerDateUtils.DATEFORMAT_SYS.format(new Date()));
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Date penaltyDate = null;
					Date discountDate = null;
					Double chargableAmount = invoice.getAmount();
					Event event = booking.getEvent();

					if (event.getPenaltyDate() != null) {
						try {
							penaltyDate = ServerDateUtils.DATEFORMAT_SYS
									.parse(ServerDateUtils.DATEFORMAT_SYS.format(event.getPenaltyDate()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (todaysDate.getTime() >= penaltyDate.getTime()) {
							Double penaltyPrice = invoice.getAmount() + invoice.getTotalPenalty();
							chargableAmount = penaltyPrice;
							logger.debug("Applied penalty of >>>" + chargableAmount);
						}
					}

					if (event.getDiscountDate() != null) {
						try {
							discountDate = ServerDateUtils.DATEFORMAT_SYS
									.parse(ServerDateUtils.DATEFORMAT_SYS.format(event.getDiscountDate()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (todaysDate.getTime() <= discountDate.getTime()) {
							Double discountedPrice = invoice.getAmount() - invoice.getTotalDiscount();
							chargableAmount = discountedPrice;
							logger.debug("Applied Discount of >>>" + chargableAmount);
						}
					}
					dto.setChargeableAmount(chargableAmount);
				}

			}

			for (InvoiceLineDto line : dto.getLines()) {
				// For Events Only
				String eventDelegateRefId = line.getEventDelegateRefId();
				if (eventDelegateRefId != null) {
					Delegate delegate = bookingsDao.findByRefId(eventDelegateRefId, Delegate.class);
					line.setMemberId(delegate.getMemberRegistrationNo());

					line.setDelegateERN(delegate.getErn());
					line.setAccommodation(
							delegate.getAccommodation() == null ? null : delegate.getAccommodation().toDto());
				}
			}
			return dto;
		}
		return null;// If no invoice was found!
	}

	public InvoiceDto checkInvoicePaymentStatus(String invoiceRef) {
		List<InvoiceDto> dtos = dao.checkInvoicePaymentStatus(invoiceRef);
		if (dtos.size() == 0) {
			return null;
		} else {
			return dtos.get(0);
		}
	}

	public InvoiceDto save(InvoiceDto dto) {
		Invoice invoice = new Invoice();
		invoice.copyFrom(dto);

		for (InvoiceLineDto lineDto : dto.getLines()) {
			InvoiceLine line = new InvoiceLine();
			line.copyFrom(lineDto);
			invoice.addLine(line);
		}

		dao.save(invoice);
		dao.merge(invoice);
		dao.flush(); // force the SQL INSERT
		dao.refresh(invoice);// re-read the state (after the trigger executes)

		return invoice.toDto();
	}

	public InvoiceDto update(String invoiceRef, InvoiceDto dto) {
		Invoice invoice = dao.findByRefId(invoiceRef, Invoice.class);
		invoice.copyFrom(dto);

		for (InvoiceLineDto lineDto : dto.getLines()) {
			InvoiceLine line = new InvoiceLine();
			if (line.getRefId() != null) {
				line = dao.findByRefId(line.getRefId(), Invoice.class);
			}
			line.copyFrom(lineDto);
			invoice.addLine(line);
		}

		dao.save(invoice);

		return invoice.toDto();
	}

	public List<TransactionDto> getAllTransactions(String memberId, String searchTerm, String fromDate, String endDate,
			String paymentType, String paymentMode, Integer offset, Integer limit) {
		List<TransactionDto> invoices = dao.getAllTransactions(memberId, searchTerm, fromDate, endDate, paymentType,
				paymentMode, offset, limit);
		for (TransactionDto trx : invoices) {
			if (trx.getPaymentMode() != null) {
				if (trx.getPaymentMode() == PaymentMode.BANKTRANSFER
						|| trx.getPaymentMode() == PaymentMode.DIRECTBANKING) {
					trx.setAllAttachment(dao.getAllAttachment(trx.getId()));
				}
			}
		}
		return invoices;
	}

	public int getInvoiceCount() {
		return 0;
	}

	public byte[] generatePDFDocument(InvoiceDto invoice) throws FileNotFoundException, IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError, DocumentException {

		String documentNo = invoice.getDocumentNo();

		List<InvoiceLineDto> invoiceLines = invoiceDao.getInvoiceLinesByDocumentNo(documentNo);

		Map<String, Object> emailValues = new HashMap<>();
		Doc proformaDocument = new Doc(emailValues);

		for (InvoiceLineDto dto : invoiceLines) {
			Map<String, Object> line = new HashMap<>();
			line.put("description", dto.getDescription());
			line.put("quantity", NumberFormat.getNumberInstance().format(dto.getQuantity()));
			line.put("unitPrice", numberFormat.format(dto.getUnitPrice()));
			line.put("amount", numberFormat.format(dto.getTotalAmount()));
			proformaDocument.addDetail(new DocumentLine("invoiceDetails", line));
		}

		// PDF Invoice Generation
		InputStream inv = EmailServiceHelper.class.getClassLoader().getResourceAsStream("proforma-invoice.html");
		String invoiceHTML = IOUtils.toString(inv);

		byte[] invoicePDF = new HTMLToPDFConvertor().convert(proformaDocument, new String(invoiceHTML));

		return invoicePDF;
	}

	public Integer getTransactionCount(String memberId, String searchTerm, String fromDate, String endDate,
			String paymentType, String paymentMode) {
		return dao.getTransactionsCount(memberId, searchTerm, fromDate, endDate, paymentType, paymentMode);
	}

	public InvoiceSummary getSummary(String memberId) {
		// TODO Auto-generated method stub
		return dao.getSummary(memberId);
	}

	public void insertIds() {
		Query query = dao.getEntityManager().createNativeQuery("select id from cpd");
		List<BigInteger> array = query.getResultList();

		int i = 0;
		for (BigInteger col : array) {
			int val = col.intValue();
			i++;
			Query updateQuery = dao.getEntityManager()
					.createNativeQuery("update cpd set refId=:refId " + "where id=:id")
					.setParameter("refId", IDUtils.generateId()).setParameter("id", val);
			updateQuery.executeUpdate();

			if (i % 1000 == 0) {
				System.out.println(i);
			}
		}
	}
}
