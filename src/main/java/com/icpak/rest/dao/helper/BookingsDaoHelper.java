package com.icpak.rest.dao.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Accommodation;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Contact;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.util.SMSIntegration;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.itextpdf.text.DocumentException;
import com.workpoint.icpak.server.integration.lms.LMSIntegrationUtil;
import com.workpoint.icpak.server.integration.lms.LMSResponse;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.InvoiceLineType;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.CourseRegDetailsPojo;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

@Transactional
public class BookingsDaoHelper {

	Logger logger = Logger.getLogger(BookingsDaoHelper.class);

	@Inject
	BookingsDao dao;
	@Inject
	UsersDao userDao;
	@Inject
	MemberDao memberDao;
	@Inject
	MemberDaoHelper memberDaoHelper;
	@Inject
	EventsDao eventDao;
	@Inject
	CPDDaoHelper cpdDao;
	@Inject
	InvoiceDaoHelper invoiceHelper;
	@Inject
	TransactionDaoHelper trxHelper;
	@Inject
	InvoiceDao invoiceDao;

	@Inject
	SMSIntegration smsIntergration;

	@Inject
	ApplicationSettings settings;

	@Inject
	AccommodationsDaoHelper accommodationsDaoHelper;

	SimpleDateFormat formatter = new SimpleDateFormat("MMM d Y");
	Locale locale = new Locale("en", "KE");
	NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

	private Booking booking;

	public List<BookingDto> getAllBookings(String uriInfo, String eventId,
			Integer offset, Integer limit, String searchTerm) {
		List<Booking> list = null;
		List<BookingDto> clones = new ArrayList<>();

		if (searchTerm != null) {
			list = dao.getAllBookings(eventId, offset, limit, searchTerm);
		} else {
			list = dao.getAllBookings(eventId, offset, limit);
		}

		for (Booking booking : list) {
			BookingDto dto = booking.toDto();
			dto.setUri(uriInfo + "/" + dto.getRefId());
			clones.add(dto);
		}
		return clones;
	}

	public List<DelegateDto> getAllDelegates(String uriInfo, String eventId,
			Integer offset, Integer limit, String searchTerm) {
		List<DelegateDto> delegateDtos = dao.getAllDelegates(eventId, offset,
				limit, searchTerm);
		return delegateDtos;
	}

	public List<DelegateDto> getDelegateByQrCode(String uriInfo,
			String eventId, Integer offset, Integer limit, String searchTerm) {
		List<DelegateDto> delegateDtos = dao.getAllDelegates(eventId, offset,
				limit, searchTerm, true);
		return delegateDtos;
	}

	public Integer getCount(String eventId) {
		return dao.getDelegateCount(eventId);
	}

	public BookingDto getBookingById(String eventId, String bookingId) {
		Booking booking = dao.getByBookingId(bookingId);
		BookingDto bookingDto = booking.toDto();
		bookingDto.setInvoiceRef(dao.getInvoiceRef(bookingId));
		return bookingDto;
	}

	public BookingDto createBooking(String eventId, BookingDto dto) {
		Event event = eventDao.getByEventId(eventId);

		// Check if this booking is existing...
		Booking booking = null;
		if (dto.getRefId() != null) {
			booking = dao.getByBookingId(dto.getRefId());
			logger.info("Booking already exist so its a matter of ammending...");
		} else {
			booking = new Booking();
			logger.info("Creating a new booking");
		}
		booking.setEvent(event);

		// Contact
		if (dto.getContact() != null) {
			Contact poContact = booking.getContact();
			if (poContact == null) {
				logger.info("Creating a new contact,since the booking had no contacts");
				poContact = new Contact();
			}
			ContactDto contactDto = dto.getContact();
			poContact.copyFrom(contactDto);
			booking.setContact(poContact);
		}
		booking.copyFrom(dto);

		// Delegates

		// Delete all existing delegates for this booking from the db
		deleteExistingDelegates(booking.getRefId());
		List<DelegateDto> dtos = dto.getDelegates();
		Collection<Delegate> delegates = new ArrayList<>();
		double total = 0.0;
		if (dtos != null)
			for (DelegateDto delegateDto : dtos) {
				Delegate d = initDelegate(delegateDto, event);
				delegates.add(d);
				total += d.getAmount();
			}
		booking.setAmountDue(total);// Total
		booking.setDelegates(delegates);
		dao.createBooking(booking);

		// Delete All Existing Invoices..
		deleteExistingInvoices(booking.getRefId());

		// Generate An Invoice
		InvoiceDto invoice = generateInvoice(booking);
		dto.setInvoiceRef(invoice.getRefId());
		dto.setRefId(eventId);

		// Copy into dto
		dto.setRefId(booking.getRefId());
		int i = 0;
		for (Delegate delegate : booking.getDelegates()) {
			dto.getDelegates().get(i).setRefId(delegate.getRefId());
			dto.getDelegates().get(i).setErn(delegate.getErn());
		}
		return dto;
	}

	private void deleteExistingDelegates(String bookingId) {
		if (bookingId == null)
			return;
		dao.deleteExistingDelegates(bookingId);
	}

	private void deleteExistingInvoices(String bookingId) {
		if (bookingId == null)
			return;
		dao.deleteAllBookingInvoice(bookingId);
	}

	public byte[] generateInvoicePdf(String bookingRefId)
			throws FileNotFoundException, IOException, SAXException,
			ParserConfigurationException, FactoryConfigurationError,
			DocumentException {
		assert bookingRefId != null;
		Booking bookingInDb = dao.findByRefId(bookingRefId, Booking.class);
		this.booking = bookingInDb;
		InvoiceDto invoice = invoiceHelper.getInvoice(dao
				.getInvoiceRef(bookingRefId));
		// Generate Email Document to be used to Map to HTML
		Map<String, Object> emailValues = generateEmailValues(invoice,
				bookingInDb);
		byte[] invoicePdf = generatePDFDocument(invoice, emailValues);

		return invoicePdf;
	}

	public void sendProInvoice(String bookingRefId) {
		assert bookingRefId != null;
		Booking bookingInDb = dao.findByRefId(bookingRefId, Booking.class);
		booking = bookingInDb;
		InvoiceDto invoice = invoiceHelper.getInvoice(dao
				.getInvoiceRef(bookingRefId));

		String subject = bookingInDb.getEvent().getName()
				+ "' Event Registration";

		// Generate Email Document to be used to Map to HTML
		Map<String, Object> emailValues = generateEmailValues(invoice,
				bookingInDb);
		Doc emailDocument = new Doc(emailValues);

		try {
			byte[] invoicePdf = generatePDFDocument(invoice, emailValues);
			Attachment attachment = new Attachment();
			attachment.setAttachment(invoicePdf);
			attachment.setName("ProForma Invoice_"
					+ bookingInDb.getContact().getContactName() + ".pdf");

			// Email Template parse and map variables
			InputStream is = EmailServiceHelper.class.getClassLoader()
					.getResourceAsStream("booking-email.html");
			String html = IOUtils.toString(is);
			html = new DocumentHTMLMapper().map(emailDocument, html);

			EmailServiceHelper.sendEmail(html, "RE: ICPAK '" + subject,
					Arrays.asList(bookingInDb.getContact().getEmail()),
					Arrays.asList(bookingInDb.getContact().getContactName()),
					attachment);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] generatePDFDocument(InvoiceDto invoice,
			Map<String, Object> emailValues) throws FileNotFoundException,
			IOException, SAXException, ParserConfigurationException,
			FactoryConfigurationError, DocumentException {

		String documentNo = invoice.getDocumentNo();
		List<InvoiceLineDto> invoiceLines = invoiceDao
				.getInvoiceLinesByDocumentNo(documentNo);

		Doc proformaDocument = new Doc(emailValues);

		for (InvoiceLineDto dto : invoiceLines) {
			if (dto.getType() == null) {
				dto.setType(InvoiceLineType.Normal);
			}

			if (dto.getType() == InvoiceLineType.Discount) {
				// Discount Line
				Map<String, Object> line = new HashMap<>();
				line.put("discDescription", dto.getDescription());
				line.put("discQuantity", NumberFormat.getNumberInstance()
						.format(dto.getQuantity()));
				line.put("discUnitPrice",
						numberFormat.format(dto.getUnitPrice()));
				line.put("discAmount",
						numberFormat.format(dto.getTotalAmount()));
				proformaDocument.addDetail(new DocumentLine("DiscountDetails",
						line));
				// System.err.println("InvoiceLineDto: " + dto.getRefId() +
				// " | "
				// + dto.getDescription() + " | " + dto.getTotalAmount()
				// + " | " + dto.getType());

			} else if (dto.getType() == InvoiceLineType.Penalty) {
				// Penalty Line
				Map<String, Object> line = new HashMap<>();
				line.put("penaltyDescription", dto.getDescription());
				line.put("penaltyQuantity", NumberFormat.getNumberInstance()
						.format(dto.getQuantity()));
				line.put("penaltyUnitPrice",
						numberFormat.format(dto.getUnitPrice()));
				line.put("penaltyAmount",
						numberFormat.format(dto.getTotalAmount()));
				proformaDocument.addDetail(new DocumentLine("PenaltyDetails",
						line));
				// System.err.println("InvoiceLineDto: " + dto.getRefId() +
				// " | "
				// + dto.getDescription() + " | " + dto.getTotalAmount()
				// + " | " + dto.getType());
			} else {
				Map<String, Object> line = new HashMap<>();
				line.put("description", dto.getDescription());
				line.put(
						"quantity",
						NumberFormat.getNumberInstance().format(
								dto.getQuantity()));
				line.put("unitPrice", numberFormat.format(dto.getUnitPrice()));
				line.put("amount", numberFormat.format(dto.getTotalAmount()));
				logger.info("InvoiceDto: " + dto.getRefId() + " | "
						+ dto.getDescription() + " | " + dto.getTotalAmount()
						+ " | " + dto.getType());

				proformaDocument.addDetail(new DocumentLine("invoiceDetails",
						line));
			}

		}

		emailValues.put("totalAmount",
				numberFormat.format(invoice.getInvoiceAmount()));
		if (booking.getEvent().getDiscountDate() != null) {
			String discountDescription = "Total Discount(Payment before %s) ";
			discountDescription = String.format(discountDescription,
					formatter.format(booking.getEvent().getDiscountDate()));
			emailValues.put("totalDiscountDesc", discountDescription);
			if (invoice.getTotalDiscount() != null) {
				emailValues.put("totalDiscAmount",
						numberFormat.format(invoice.getTotalDiscount()));
			}
		} else {
			String discountDescription = "Total Discount ";
			emailValues.put("totalDiscountDesc", discountDescription);
			emailValues.put("totalDiscAmount", numberFormat.format(0.0));
		}

		if (booking.getEvent().getPenaltyDate() != null) {
			String penaltyDescription = "Total Penalty(Payment after %s) ";
			penaltyDescription = String.format(penaltyDescription,
					formatter.format(booking.getEvent().getPenaltyDate()));
			emailValues.put("totalPenaltyDesc", penaltyDescription);
			if (invoice.getTotalPenalty() != null) {
				emailValues.put("totalPenaltyAmount",
						numberFormat.format(invoice.getTotalPenalty()));
			}
		} else {
			String discountDescription = "Total Penalties ";
			emailValues.put("totalPenaltyDesc", discountDescription);
			emailValues.put("totalPenaltyAmount", numberFormat.format(0.0));
		}

		// PDF Invoice Generation
		InputStream inv = EmailServiceHelper.class.getClassLoader()
				.getResourceAsStream("proforma-invoice.html");
		String invoiceHTML = IOUtils.toString(inv);

		byte[] invoicePDF = new HTMLToPDFConvertor().convert(proformaDocument,
				new String(invoiceHTML));

		return invoicePDF;
	}

	public Map<String, Object> generateEmailValues(InvoiceDto invoice,
			Booking bookingInDb) {
		Map<String, Object> emailValues = new HashMap<String, Object>();
		emailValues.put("companyName", invoice.getCompanyName());
		String companyAddress = bookingInDb.getContact().getAddress() + " "
				+ bookingInDb.getContact().getPostCode();
		emailValues.put("companyAddress", companyAddress);
		emailValues.put("companyLocation", bookingInDb.getContact().getCity());
		emailValues.put("contactPhone", bookingInDb.getContact()
				.getPhysicalAddress());
		emailValues.put("quoteNo", invoice.getDocumentNo());
		emailValues.put("date", formatter.format(invoice.getDate()));
		emailValues.put("firstName", invoice.getContactName());
		emailValues.put("eventName", bookingInDb.getEvent().getName());
		emailValues.put("eventStartDate",
				formatter.format(bookingInDb.getEvent().getStartDate()));
		emailValues.put("DocumentURL", settings.getApplicationPath());
		emailValues.put("email", bookingInDb.getContact().getEmail());
		emailValues.put("eventId", bookingInDb.getEvent().getRefId());
		emailValues.put("bookingId", bookingInDb.getRefId());
		Doc emailDocument = new Doc(emailValues);

		// Collection of Delegates
		Collection<Delegate> delegates = bookingInDb.getDelegates();
		int counter = 0;

		for (Delegate delegate : delegates) {
			counter++;
			emailValues.put("counter", counter);
			emailValues.put("delegateNames", delegate.getSurname() + " "
					+ delegate.getOtherNames());

			emailValues.put("memberType",
					(delegate.getMemberRegistrationNo() == null ? "Non-Member"
							: "Member"));

			emailValues.put("ernNo", (delegate.getErn() == null ? "----"
					: delegate.getErn()));

			emailValues.put("accomodationName",
					(delegate.getAccommodation() == null ? "None" : delegate
							.getAccommodation().getHotel()
							+ " "
							+ delegate.getAccommodation().getNights()));
			DocumentLine docLine = new DocumentLine("accomadationDetails",
					emailValues);
			emailDocument.addDetail(docLine);
		}

		return emailValues;
	}

	public InvoiceDto generateInvoice(Booking booking) {
		this.booking = booking;
		InvoiceDto invoice = new InvoiceDto();
		invoice.setDescription("Event Booking Invoice");
		double totalAmount = 0.0;
		double totalDiscountAmount = 0.0;
		double totalPenaltyAmount = 0.0;

		// THE ANNUAL MANAGEMENT ACCOUNTING CONFERENCE - Conference fees for 2
		// members:JUSTUS MUSASIAH(6061, ERN: 430-0236),EVANS MULERA(4295, ERN:
		// 430-0237)

		List<Delegate> delegates = new ArrayList<>();
		delegates.addAll(booking.getDelegates());
		Collections.sort(delegates, new Comparator<Delegate>() {
			@Override
			public int compare(Delegate o1, Delegate o2) {
				return o1.getMemberRegistrationNo() == null ? -1 : o2
						.getMemberRegistrationNo() == null ? 1 : 0;
			}
		});

		InvoiceLineDto memberInvoiceLine = new InvoiceLineDto();
		memberInvoiceLine.setQuantity(0);
		memberInvoiceLine.setMemberNames("");

		InvoiceLineDto nonMemberInvoiceLine = new InvoiceLineDto();
		nonMemberInvoiceLine.setQuantity(0);
		nonMemberInvoiceLine.setMemberNames("");

		InvoiceLineDto memberDiscountLine = new InvoiceLineDto();
		memberDiscountLine.setQuantity(0);
		memberDiscountLine.setMemberNames("");

		InvoiceLineDto nonMemberDiscountLine = new InvoiceLineDto();
		nonMemberDiscountLine.setQuantity(0);
		nonMemberDiscountLine.setMemberNames("");

		InvoiceLineDto memberPenaltyLine = new InvoiceLineDto();
		memberPenaltyLine.setQuantity(0);
		memberPenaltyLine.setMemberNames("");

		InvoiceLineDto nonMemberPenaltyLine = new InvoiceLineDto();
		nonMemberPenaltyLine.setQuantity(0);
		nonMemberPenaltyLine.setMemberNames("");

		Map<String, InvoiceLineDto> memberRefLineMap = new HashMap<>();
		Map<String, InvoiceLineDto> nonMemberRefLineMap = new HashMap<>();

		Event event = booking.getEvent();
		for (Delegate delegate : delegates) {
			delegate.setErn(dao.getErn(delegate.getRefId()));

			if (delegate.getMemberRegistrationNo() != null) {
				String description = "%s - %s fees for %d member(s): %s";
				memberInvoiceLine
						.setMemberNames(memberInvoiceLine.getMemberNames()
								.concat((memberInvoiceLine.getMemberNames()
										.isEmpty() ? "" : ", ")
										+ delegate.toString()));

				int qty = memberInvoiceLine.getQuantity() + 1;

				memberInvoiceLine.setEventDelegateRefId(delegate.getRefId());
				description = String.format(description, event.getName(), event
						.getType().getDisplayName(), qty, memberInvoiceLine
						.getMemberNames());
				memberInvoiceLine.setDescription(description);
				memberInvoiceLine.setQuantity(qty);
				memberInvoiceLine.setUnitPrice(delegate.getAmount());
				memberInvoiceLine.setTotalAmount(qty * delegate.getAmount());
				totalAmount += delegate.getAmount();// memberInvoice.getTotalAmount();

				// Calculate the Discount
				// Early Bird Discount for Member(Payment before 21st Jan 2016)
				Event eventIndb = booking.getEvent();
				if (eventIndb.getDiscountDate() != null) {
					String discountDescription = "Member Early Bird Discount(Payment before %s) ";
					discountDescription = String.format(discountDescription,
							formatter.format(eventIndb.getDiscountDate()));
					memberDiscountLine.setDescription(discountDescription);
					memberDiscountLine.setQuantity(qty);
					memberDiscountLine.setUnitPrice(eventIndb
							.getDiscountMemberPrice());
					memberDiscountLine.setTotalAmount(qty
							* eventIndb.getDiscountMemberPrice());
					memberDiscountLine.setType(InvoiceLineType.Discount);

					totalDiscountAmount += eventIndb.getDiscountMemberPrice();

					logger.error(discountDescription + "|"
							+ memberDiscountLine.getQuantity() + " | " + qty
							* eventIndb.getDiscountMemberPrice() + " | "
							+ totalDiscountAmount);
				}

				// Penalty Calculation
				// Member Penalty(Payment after 21st Jan 2016)
				if (eventIndb.getPenaltyDate() != null) {
					String penaltyDescription = "Member Penalty(Payment after %s) ";
					penaltyDescription = String.format(penaltyDescription,
							formatter.format(eventIndb.getPenaltyDate()));
					memberPenaltyLine.setDescription(penaltyDescription);
					memberPenaltyLine.setQuantity(qty);
					memberPenaltyLine.setUnitPrice(eventIndb
							.getPenaltyMemberPrice());
					memberPenaltyLine.setTotalAmount(qty
							* eventIndb.getPenaltyMemberPrice());
					memberPenaltyLine.setType(InvoiceLineType.Penalty);
					totalPenaltyAmount += eventIndb.getPenaltyMemberPrice();

					logger.error(penaltyDescription + "|"
							+ memberPenaltyLine.getQuantity() + " | " + qty
							* eventIndb.getPenaltyMemberPrice() + " | "
							+ totalPenaltyAmount);
				}

			} else {
				String description = "%s - %s fees for %d non-member(s): %s";
				int qty = nonMemberInvoiceLine.getQuantity() + 1;
				nonMemberInvoiceLine.setEventDelegateRefId(delegate.getRefId());

				nonMemberInvoiceLine.setMemberNames(nonMemberInvoiceLine
						.getMemberNames().concat(
								(nonMemberInvoiceLine.getMemberNames()
										.isEmpty() ? "" : ", ")
										+ delegate.toString()));

				description = String.format(description, event.getName(), event
						.getType().getDisplayName(), qty, nonMemberInvoiceLine
						.getMemberNames());
				nonMemberInvoiceLine.setDescription(description);
				nonMemberInvoiceLine.setQuantity(qty);
				nonMemberInvoiceLine.setUnitPrice(delegate.getAmount());
				nonMemberInvoiceLine.setTotalAmount(qty * delegate.getAmount());
				totalAmount += delegate.getAmount();// nonMemberInvoice.getTotalAmount();

				// Calculate the Non-Member Discount
				// Early Bird Discount for Non Member(Payment before 21st Jan
				// 2016)
				Event eventIndb = booking.getEvent();
				if (eventIndb.getDiscountDate() != null) {
					String discountDescription = "Non-Member Early Bird Discount(Payment before %s)";
					discountDescription = String.format(discountDescription,
							formatter.format(eventIndb.getDiscountDate()));
					nonMemberDiscountLine.setDescription(discountDescription);
					nonMemberDiscountLine.setQuantity(qty);
					nonMemberDiscountLine.setUnitPrice(eventIndb
							.getDiscountNonMemberPrice());
					nonMemberDiscountLine.setTotalAmount(qty
							* eventIndb.getDiscountNonMemberPrice());
					nonMemberDiscountLine.setType(InvoiceLineType.Discount);
					totalDiscountAmount += eventIndb
							.getDiscountNonMemberPrice();
					logger.info(discountDescription + "|" + qty + " | " + qty
							* eventIndb.getDiscountNonMemberPrice() + " | "
							+ totalDiscountAmount);
				}

				// Calculate the Non-Member Penalty
				// Non Member Penalty(Payment before 21st Jan
				// 2016)
				if (eventIndb.getPenaltyDate() != null) {
					String penaltyDescription = "Non-Member Penalty(Payment after %s)";
					penaltyDescription = String.format(penaltyDescription,
							formatter.format(eventIndb.getPenaltyDate()));
					nonMemberPenaltyLine.setDescription(penaltyDescription);
					nonMemberPenaltyLine.setQuantity(qty);
					nonMemberPenaltyLine.setUnitPrice(eventIndb
							.getPenaltyNonMemberPrice());
					nonMemberPenaltyLine.setTotalAmount(qty
							* eventIndb.getPenaltyNonMemberPrice());
					nonMemberPenaltyLine.setType(InvoiceLineType.Penalty);
					totalPenaltyAmount += eventIndb.getPenaltyNonMemberPrice();
					logger.info(penaltyDescription + "|" + qty + " | " + qty
							* eventIndb.getPenaltyNonMemberPrice() + " | "
							+ totalPenaltyAmount);
				}

			}

			if (delegate.getAccommodation() != null) {
				if (delegate.getMemberRegistrationNo() != null) {
					InvoiceLineDto line = memberRefLineMap.get(delegate
							.getAccommodation().getRefId());
					if (line == null) {
						line = new InvoiceLineDto();
						line.setMemberNames("");
						memberRefLineMap.put(delegate.getAccommodation()
								.getRefId(), line);
					}

					String description = "%s - Accommodation at %s %d Nights HB "
							+ "for %d members: %s";
					line.setMemberNames(line.getMemberNames().concat(
							", " + delegate.toString()));
					int qty = line.getQuantity() + 1;

					line.setEventDelegateRefId(delegate.getRefId());
					description = String.format(description, event.getName(),
							delegate.getAccommodation().getHotel(), delegate
									.getAccommodation().getNights(), qty, line
									.getMemberNames());
					line.setDescription(description);
					line.setQuantity(qty);
					line.setUnitPrice(delegate.getAccommodation().getFee());
					line.setTotalAmount(qty
							* delegate.getAccommodation().getFee());
					totalAmount += delegate.getAccommodation().getFee();// line.getTotalAmount();
				} else {

					InvoiceLineDto line = nonMemberRefLineMap.get(delegate
							.getAccommodation().getRefId());
					if (line == null) {
						line = new InvoiceLineDto();
						line.setMemberNames("");
						nonMemberRefLineMap.put(delegate.getAccommodation()
								.getRefId(), line);
					}

					String description = "%s - Accommodation at %s %d Nights HB "
							+ "for %d members: %s";
					line.setMemberNames(line.getMemberNames().concat(
							", " + delegate.toString()));
					int qty = line.getQuantity() + 1;

					line.setEventDelegateRefId(delegate.getRefId());
					description = String.format(description, event.getName(),
							delegate.getAccommodation().getHotel(), delegate
									.getAccommodation().getNights(), qty, line
									.getMemberNames());
					line.setDescription(description);
					line.setQuantity(qty);
					line.setUnitPrice(delegate.getAccommodation().getFee());
					line.setTotalAmount(qty
							* delegate.getAccommodation().getFee());
					totalAmount += delegate.getAccommodation().getFee();// line.getTotalAmount();
				}
			}
		}

		if (memberInvoiceLine.getQuantity() != 0) {
			invoice.addLine(memberInvoiceLine);
		}

		if (nonMemberInvoiceLine.getQuantity() != 0) {
			invoice.addLine(nonMemberInvoiceLine);
		}

		if (memberDiscountLine.getQuantity() != 0) {
			invoice.addLine(memberDiscountLine);
		}

		if (nonMemberDiscountLine.getQuantity() != 0) {
			invoice.addLine(nonMemberDiscountLine);
		}

		if (memberPenaltyLine.getQuantity() != 0) {
			invoice.addLine(memberPenaltyLine);
		}

		if (nonMemberPenaltyLine.getQuantity() != 0) {
			invoice.addLine(nonMemberPenaltyLine);
		}

		invoice.addLines(memberRefLineMap.values());
		invoice.addLines(nonMemberRefLineMap.values());

		invoice.setAmount(totalAmount);
		invoice.setDocumentNo(booking.getId() + "");
		invoice.setCompanyName(booking.getContact().getCompany());
		invoice.setDate(booking.getBookingDate().getTime());
		invoice.setCompanyAddress(booking.getContact().getAddress());
		invoice.setContactName(booking.getContact().getContactName());
		invoice.setPhoneNumber(booking.getContact().getTelephoneNumbers());
		invoice.setBookingRefId(booking.getRefId());
		invoice.setTotalDiscount(totalDiscountAmount);
		invoice.setTotalPenalty(totalPenaltyAmount);

		invoice = invoiceHelper.save(invoice);

		// System.err.println("Invoice RefId>>>" + invoice.getRefId());
		return invoice;
	}

	public BookingDto updateBooking(String eventId, String bookingId,
			BookingDto dto) {
		logger.error("==== Booking ref Id === " + bookingId);
		dto.setRefId(bookingId);
		return createBooking(eventId, dto);
	}

	public BookingDto checkEmailExists(String email, String eventRefId) {
		Event event = dao.findByRefId(eventRefId, Event.class);
		Booking booking = dao.getBySponsorEmail(email, event.getId());
		if (booking != null) {
			String invoiceRefId = dao.getInvoiceRef(booking.getRefId());
			BookingDto bookingDto = booking.toDto();
			bookingDto.setInvoiceRef(invoiceRefId);
			return bookingDto;
		} else {
			return null;
		}
	}

	public void sendDelegateSMS(String bookingRefId) {
		Booking bookingInDb = dao.findByRefId(bookingRefId, Booking.class);
		Event event = bookingInDb.getEvent();
		Collection<Delegate> delegates = bookingInDb.getDelegates();
		List<Delegate> delegateList = new ArrayList<>();
		delegateList.addAll(delegates);

		for (Delegate delegate : delegateList) {
			String smsMemssage = "Dear" + " " + delegate.getFullName() + ","
					+ "Thank you for booking for the " + event.getName()
					+ ". Your booking status is NOT PAID. Your ERN No. is "
					+ delegate.getErn();

			if (delegate.getMemberRefId() != null) {
				Member member = memberDao.findByRefId(
						delegate.getMemberRefId(), Member.class);
				System.err.println("Sending SMS to "
						+ member.getUser().getPhoneNumber());

				if (member.getUser().getPhoneNumber() != null) {
					try {
						smsIntergration.send(member.getUser().getPhoneNumber(),
								smsMemssage);
					} catch (RuntimeException e) {
						System.err.println("Invalid Phone Number...!");
						e.printStackTrace();
					}
				}
			} else {
				System.err.println("Non-member cannot be send sms..");
			}
		}

	}

	private Delegate initDelegate(DelegateDto delegateDto, Event event) {
		Delegate delegate = new Delegate();
		// if (delegateDto.getRefId() != null) {
		// delegate = eventDao.findByRefId(delegateDto.getRefId(),
		// Delegate.class);
		// }
		delegate.copyFrom(delegateDto);

		// Accomodation
		if (delegateDto.getAccommodation() != null) {
			Accommodation accommodation = dao.findByRefId(delegateDto
					.getAccommodation().getRefId(), Accommodation.class);
			if (accommodation != null) {
				accommodation.setSpaces(accommodation.getSpaces() - 1);
				delegate.setAccommodation(accommodation);
				dao.save(accommodation);
			}
		}

		// Event Pricing
		Double price = event.getNonMemberPrice();
		if (delegateDto.getMemberNo() != null) {
			price = event.getMemberPrice();
		}

		// Add Accommodation Charge
		if (delegate.getAccommodation() != null) {
			price += delegate.getAccommodation().getFee();
		}
		delegate.setAmount(price); // Charge for delegate
		return delegate;
	}

	public void deleteBooking(String eventId, String bookingId) {
		Booking booking = dao.getByBookingId(bookingId);
		dao.delete(booking);
	}

	public BookingDto processPayment(String eventId, String bookingId,
			String paymentMode, String paymentRef) {
		Booking booking = dao.getByBookingId(bookingId);
		// Check if payment ref already exists
		boolean exists = dao.isPaymentValid(paymentRef);
		if (exists) {
			throw new ServiceException(ErrorCodes.DUPLICATEVALUE, "Payment Ref");
		}

		if (booking != null) {
			booking.setPaymentRef(paymentRef);
			booking.setPaymentMode(paymentMode);
			booking.setPaymentDate(new Date());
		}

		dao.save(booking);

		return booking.toDto();
	}

	public DelegateDto updateDelegate(String delegateId, DelegateDto delegateDto) {
		logger.error("+++++ <><>>>>>>>>>>>>> UPDATE DELEGATW +++++++++++++++++");

		int counter = 0;

		Delegate delegate = dao.findByRefId(delegateId, Delegate.class);
		Event event = dao.findByRefId(delegate.getBooking().getEvent()
				.getRefId(), Event.class);

		if (delegate.getMemberRefId() != null
				&& delegate.getAttendance() != delegateDto.getAttendance()
				&& event.getType() != EventType.COURSE) {
			// send and SMS
			Member member = dao.findByRefId(delegate.getMemberRefId(),
					Member.class);
			String smsMessage = "Dear" + " " + delegateDto.getFullName()
					+ ",Thank you for attending the " + event.getName() + "."
					+ "Your ERN No. is " + delegate.getErn();
			smsIntergration.send(member.getUser().getPhoneNumber(), smsMessage);
		} else {
			List<DelegateDto> delegates = new ArrayList<>();
			delegates.add(delegate.toDto());
			try {
				enrolDelegateToLMS(delegates, event);
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}

		}

		logger.error(delegateDto.getReceiptNo() + "== RCPT NO");
		delegate.copyFrom(delegateDto);

		dao.save(delegate);

		cpdDao.updateCPDFromAttendance(delegate, delegate.getAttendance());

		return delegate.toDto();
	}

	public List<MemberBookingDto> getMemberBookings(String memberRefId,
			int offset, int limit) {
		return dao.getMemberBookings(memberRefId, offset, limit);
	}

	public void updateAccomodationEntry(Event event) {
		Set<Accommodation> accommodations = event.getAccommodation();

		for (Accommodation accommodation : accommodations) {
			Set<Delegate> delegates = accommodation.getDelegates();
			int spacesOccupied = delegates.size();
			accommodation.setSpaces(accommodation.getSpaces() - spacesOccupied);

			accommodationsDaoHelper.update(event.getRefId(),
					accommodation.getRefId(), accommodation.toDto());
		}

	}

	public Integer getDelegatesCount(String eventId, String searchTerm) {
		logger.error("== Counting delegates ===");
		return dao.getDelegateCount(eventId, searchTerm);
	}

	public void enrolDelegateToLMS(List<DelegateDto> delegates, Event event)
			throws JSONException, IOException {
		System.err.println("Enrol delegates called!!!");
		logger.info("Delgates size::" + delegates.size());
		String smsMessage = "";
		for (DelegateDto delegate : delegates) {
			CourseRegDetailsPojo details = new CourseRegDetailsPojo();
			details.setCourseId(event.getLmsCourseId() + "");
			details.setMembershipID(delegate.getMemberNo());
			JSONObject json = new JSONObject(details);
			logger.info("JSON::" + json);
			LMSResponse response = LMSIntegrationUtil.getInstance()
					.executeLMSCall("/Course/EnrollCourse", json, String.class);
			logger.info("LMS Response::" + response.getMessage());
			logger.info("LMS Status::" + response.getStatus());
			delegate.setEventRefId(event.getRefId());
			delegate.setLmsResponse(response.getMessage());
			if (response.getStatus().equals("Success")) {
				delegate.setAttendance(AttendanceStatus.ENROLLED);
				smsMessage = "Dear "
						+ delegate.getFullName()
						+ ","
						+ " Thank-you for your "
						+ " payment for "
						+ event.getName()
						+ ".Your booking status is now PAID. "
						+ "You have been enrolled to CPD Online to perform this course.";

			} else {
				smsMessage = "Dear "
						+ delegate.getFullName()
						+ ","
						+ " Thank-you for your "
						+ " payment for "
						+ event.getName()
						+ ".Your booking status is now PAID. The was a problem enrolling you to CPD Online."
						+ " Please contact administrator.";
			}

			if (delegate.getMemberRefId() != null) {
				Member member = memberDao.findByRefId(
						delegate.getMemberRefId(), Member.class);
				logger.info("Sending SMS to "
						+ member.getUser().getPhoneNumber());

				if (member.getUser().getPhoneNumber() != null) {
					try {
						smsIntergration.send(member.getUser().getPhoneNumber(),
								smsMessage);
					} catch (RuntimeException e) {
						logger.error("Invalid Phone Number");
						e.printStackTrace();
					}
				}
			} else {
				logger.info("Non-member cannot be send sms..");
			}

			/* Update Delegate Record */
			Delegate d = dao.findByRefId(delegate.getRefId(), Delegate.class);
			d.copyFrom(delegate);
			dao.save(d);
		}

	}
}
