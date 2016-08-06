package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.ParseException;
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

import javax.mail.MessagingException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.InvoiceDao;
import com.icpak.rest.dao.InvoiceDaoHelper;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.event.Accommodation;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.ApplicationFormHeader;
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
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.BookingSummaryDto;
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
	ApplicationFormDao applicationDao;

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

	/*
	 * Gets All delegates and updates delegate phone details from member records
	 */
	public List<DelegateDto> getAllDelegates(String uriInfo, String eventId,
			Integer offset, Integer limit, String searchTerm,
			String accomodationRefId, String bookingStatus,
			boolean updatePhoneContacts) {
		List<DelegateDto> delegateDtos = dao.getAllDelegates(eventId, offset,
				limit, searchTerm, accomodationRefId, bookingStatus);

		if (updatePhoneContacts) {
			// Ensure that there is delegate phoneNumber
			for (DelegateDto del : delegateDtos) {
				if ((del.getMemberNo() != null)
						&& (del.getDelegatePhoneNumber() == null || del
								.getDelegatePhoneNumber().isEmpty())) {
					logger.info("Updating phoneNumber for::"
							+ del.getMemberNo() + "::" + del.getFullName());
					User u = userDao.findUserByMemberNo(del.getMemberNo());

					if (u != null) {
						if (u.getPhoneNumber() != null
								&& !(u.getPhoneNumber().isEmpty())) {
							del.setDelegatePhoneNumber(u.getPhoneNumber());
							// Save this Information
							Delegate d = new Delegate();
							d.setPhoneNumber(u.getPhoneNumber());
							d.copyFrom(del);
							dao.save(d);
						}
					}
				}
			}
		}

		return delegateDtos;
	}

	/*
	 * Gets All delegates but does not updates delegate phone details from
	 * member records
	 */
	public List<DelegateDto> getAllDelegates(String uriInfo, String eventId,
			Integer offset, Integer limit, String searchTerm,
			String accomodationRefId, String bookingStatus) {
		return dao.getAllDelegates(eventId, offset, limit, searchTerm,
				accomodationRefId, bookingStatus);
	}

	public List<DelegateDto> getDelegateByQrCode(String uriInfo,
			String eventId, Integer offset, Integer limit, String searchTerm) {
		List<DelegateDto> delegateDtos = dao.getAllDelegates(eventId, offset,
				limit, searchTerm, true, "", "");
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

	/*
	 * Instantly create booking from memberNo, picking user saved information
	 */
	public BookingDto createBooking(String eventId, DelegateDto delegate) {

		if (delegate.getMemberNo() == null) {
			throw new ServiceException(ErrorCodes.SERVER_ERROR,
					"MemberNo is required!");
		}

		// Find the member
		User user = userDao.findUserByMemberNo(delegate.getMemberNo());
		if (user == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND,
					"Member not found. Please pass a valid memberNo");
		}

		// Find members application detail
		ApplicationFormHeader application = applicationDao
				.getApplicationByUserRef(user.getRefId());
		if (application == null) {
			throw new ServiceException(
					ErrorCodes.NOTFOUND,
					"Member's ApplicationForm not found. Kindly check with Admin why applicationForm is not found!");

		}

		// Create a new bookingDto
		BookingDto booking = new BookingDto();

		// Contact Information
		ContactDto contact = new ContactDto();
		if (application.getEmployer() == null) {
			application.setEmployer("NOT SET");
		}
		contact.setCompany(application.getEmployer());
		contact.setTelephoneNumbers(user.getPhoneNumber());
		contact.setAddress((application.getAddress1() == null ? "NOT SET"
				: application.getAddress1()));
		contact.setPostCode((application.getPostCode() == null ? "NOT SET"
				: application.getPostCode()));
		contact.setContactName(user.getFullName() == null ? "NOT SET" : user
				.getFullName());
		contact.setEmail(user.getEmail() == null ? "NOT SET" : user.getEmail());
		booking.setContact(contact);

		// Delegate Information
		delegate.setFullName(user.getFullName());
		delegate.setDelegatePhoneNumber(user.getPhoneNumber());
		delegate.setMemberQrCode(user.getMember().getMemberQrCode());
		booking.setDelegates(Arrays.asList(delegate));

		JSONObject json = new JSONObject(booking);
		System.err.println(json);

		return createBooking(eventId, booking);

	}

	/*
	 * Mother function of create Booking
	 */

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

		// Contact information
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
		updateBookingStats(bookingInDb);
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

		InvoiceLineDto associateInvoiceLine = new InvoiceLineDto();
		associateInvoiceLine.setQuantity(0);
		associateInvoiceLine.setMemberNames("");

		InvoiceLineDto nonMemberInvoiceLine = new InvoiceLineDto();
		nonMemberInvoiceLine.setQuantity(0);
		nonMemberInvoiceLine.setMemberNames("");

		InvoiceLineDto memberDiscountLine = new InvoiceLineDto();
		memberDiscountLine.setQuantity(0);
		memberDiscountLine.setMemberNames("");

		InvoiceLineDto associateDiscountLine = new InvoiceLineDto();
		associateDiscountLine.setQuantity(0);
		associateDiscountLine.setMemberNames("");

		InvoiceLineDto nonMemberDiscountLine = new InvoiceLineDto();
		nonMemberDiscountLine.setQuantity(0);
		nonMemberDiscountLine.setMemberNames("");

		InvoiceLineDto memberPenaltyLine = new InvoiceLineDto();
		memberPenaltyLine.setQuantity(0);
		memberPenaltyLine.setMemberNames("");

		InvoiceLineDto associatePenaltyLine = new InvoiceLineDto();
		associatePenaltyLine.setQuantity(0);
		associatePenaltyLine.setMemberNames("");

		InvoiceLineDto nonMemberPenaltyLine = new InvoiceLineDto();
		nonMemberPenaltyLine.setQuantity(0);
		nonMemberPenaltyLine.setMemberNames("");

		Map<String, InvoiceLineDto> memberRefLineMap = new HashMap<>();
		Map<String, InvoiceLineDto> nonMemberRefLineMap = new HashMap<>();

		Event event = booking.getEvent();
		for (Delegate delegate : delegates) {
			delegate.setErn(dao.getErn(delegate.getRefId()));

			// Member
			if (delegate.getMemberRegistrationNo() != null
					&& !delegate.getMemberRegistrationNo().contains("ASSOC/")) {
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

			} else if (delegate.getMemberRegistrationNo() != null
					&& delegate.getMemberRegistrationNo().contains("ASSOC/")) {
				String description = "%s - %s fees for %d associate member(s): %s";
				associateInvoiceLine.setMemberNames(associateInvoiceLine
						.getMemberNames().concat(
								(associateInvoiceLine.getMemberNames()
										.isEmpty() ? "" : ", ")
										+ delegate.toString()));

				int qty = associateInvoiceLine.getQuantity() + 1;

				associateInvoiceLine.setEventDelegateRefId(delegate.getRefId());
				description = String.format(description, event.getName(), event
						.getType().getDisplayName(), qty, associateInvoiceLine
						.getMemberNames());
				associateInvoiceLine.setDescription(description);
				associateInvoiceLine.setQuantity(qty);
				associateInvoiceLine.setUnitPrice(delegate.getAmount());
				associateInvoiceLine.setTotalAmount(qty * delegate.getAmount());
				totalAmount += delegate.getAmount();// memberInvoice.getTotalAmount();

				// Calculate the Discount
				// Early Bird Discount for Member(Payment before 21st Jan 2016)
				Event eventIndb = booking.getEvent();
				if (eventIndb.getDiscountDate() != null) {
					String discountDescription = "Associate Member Early Bird Discount(Payment before %s) ";
					discountDescription = String.format(discountDescription,
							formatter.format(eventIndb.getDiscountDate()));
					associateDiscountLine.setDescription(discountDescription);
					associateDiscountLine.setQuantity(qty);
					associateDiscountLine.setUnitPrice(eventIndb
							.getDiscountAssociatePrice());
					associateDiscountLine.setTotalAmount(qty
							* eventIndb.getDiscountMemberPrice());
					associateDiscountLine.setType(InvoiceLineType.Discount);

					totalDiscountAmount += eventIndb.getDiscountMemberPrice();

					logger.error(discountDescription + "|"
							+ associateDiscountLine.getQuantity() + " | " + qty
							* eventIndb.getDiscountMemberPrice() + " | "
							+ totalDiscountAmount);
				}

				// Penalty Calculation
				// Member Penalty(Payment after 21st Jan 2016)
				if (eventIndb.getPenaltyDate() != null) {
					String penaltyDescription = "Associate Member Penalty(Payment after %s) ";
					penaltyDescription = String.format(penaltyDescription,
							formatter.format(eventIndb.getPenaltyDate()));
					associatePenaltyLine.setDescription(penaltyDescription);
					associatePenaltyLine.setQuantity(qty);
					associatePenaltyLine.setUnitPrice(eventIndb
							.getPenaltyAssociatePrice());
					associatePenaltyLine.setTotalAmount(qty
							* eventIndb.getPenaltyAssociatePrice());
					associatePenaltyLine.setType(InvoiceLineType.Penalty);
					totalPenaltyAmount += eventIndb.getPenaltyMemberPrice();

					logger.error(penaltyDescription + "|"
							+ associatePenaltyLine.getQuantity() + " | " + qty
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

					String description = "%s - Accommodation at %s "
							+ "for %d members: %s";
					line.setMemberNames(line.getMemberNames().concat(
							", " + delegate.toString()));
					int qty = line.getQuantity() + 1;

					line.setEventDelegateRefId(delegate.getRefId());
					description = String.format(description, event.getName(),
							delegate.getAccommodation().getHotel(), qty,
							line.getMemberNames());
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

					String description = "%s - Accommodation at %s "
							+ "for %d members: %s";
					line.setMemberNames(line.getMemberNames().concat(
							", " + delegate.toString()));
					int qty = line.getQuantity() + 1;

					line.setEventDelegateRefId(delegate.getRefId());
					description = String.format(description, event.getName(),
							delegate.getAccommodation().getHotel(), qty,
							line.getMemberNames());
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

		if (associateInvoiceLine.getQuantity() != 0) {
			invoice.addLine(associateInvoiceLine);
		}

		if (associateDiscountLine.getQuantity() != 0) {
			invoice.addLine(associateDiscountLine);
		}

		if (associatePenaltyLine.getQuantity() != 0) {
			invoice.addLine(associatePenaltyLine);
		}

		invoice.addLines(memberRefLineMap.values());
		invoice.addLines(nonMemberRefLineMap.values());
		invoice.setAmount(totalAmount);
		if (booking.getBookingDate() == null) {
			// throw new ServiceException(ErrorCodes.SERVER_ERROR,
			// "Booking cannot be null, there was a problem saving the booking..!");
			booking.setBookingDate(new Date());
		}
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

		return invoice;
	}

	public BookingDto updateBooking(String eventId, String bookingId,
			BookingDto dto) {
		logger.error("==== Booking ref Id === " + bookingId);
		dto.setRefId(bookingId);
		return createBooking(eventId, dto);
	}

	public boolean cancelBooking(String bookingId) {
		Booking booking = null;
		logger.info("Cancelling a Booking called..");
		if (bookingId != null) {
			booking = dao.findByRefId(bookingId, Booking.class, false);
			if (booking != null) {
				booking.setIsActive(0);
				booking = (Booking) dao.save(booking);
				updateBookingStats(booking);
				return true;
			} else {
				Delegate del = dao.findByRefId(bookingId, Delegate.class);
				if (del != null) {
					del.setIsActive(0);
					dao.save(del);
					updateBookingStats(del.getBooking());
					return true;
				}
			}
		}
		return false;
	}

	public void correctDoubleBookings(String eventRefId) {
		// All MemberNos
		List<String> memberNos = dao.correctDoubleBookings(eventRefId);
		for (String memberNo : memberNos) {
			System.err.println("Correcting booking for memberNo::" + memberNo);
			List<DelegateDto> dels = dao.getAllDelegates(eventRefId,
					new Integer(0), 10000, null, false, null, null, memberNo);
			if (dels.size() == 0) {
				System.err.println("Problem:There is no double booking....");
			}

			List<DelegateDto> toBeUpdated = new ArrayList<>();
			boolean hasPaid = false;
			// Cancel all except the ones which are paid
			for (DelegateDto d : dels) {
				if (d.getBookingPaymentStatus() == PaymentStatus.PAID) {
					hasPaid = true;
				} else {
					toBeUpdated.add(d);
					System.err.println("To be Cancelled>>>" + d.getErn()
							+ "bookingRefId>>>" + d.getBookingRefId() + " >>"
							+ d.getBookingPaymentStatus());
				}
			}

			// Cancel All except the first one in the List
			if (!hasPaid) {
				System.err.println("Cancel all except >>"
						+ " >>>bookingRefId>>" + dels.get(0).getBookingRefId()
						+ dels.get(0).getErn());
				activeRefIds.add(dels.get(0).getBookingRefId());
				toBeUpdated.remove(0);
			}

			System.err.println("To be updated Size::" + toBeUpdated.size());
			for (DelegateDto d : toBeUpdated) {
				if (!checkIfAlreadyCancelled(d.getBookingRefId())) {
					System.err.println("Cancelling bookingRefId>>"
							+ d.getBookingRefId() + " ERN>>" + d.getErn());
					cancelBooking(d.getBookingRefId());
				} else {
					System.err
							.println("This booking is not to be cancelled "
									+ "because it exist in the ones marked for active:::"
									+ d.getBookingRefId());
				}
			}
		}
	}

	List<String> activeRefIds = new ArrayList<>();

	public boolean checkIfAlreadyCancelled(String passedRefId) {
		for (String bookingRefId : activeRefIds) {
			if (bookingRefId.trim().contains(passedRefId))
				return true;
		}
		return false;
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
				delegate.setAccommodation(accommodation);
				dao.save(accommodation);
			}
		}

		// Event Pricing
		Double price = event.getNonMemberPrice();
		if (delegateDto.getMemberNo() != null) {
			if (delegateDto.getMemberNo().contains("ASSOC/")) {
				price = event.getAssociatePrice();
			} else {
				price = event.getMemberPrice();
			}
			logger.info("Delegate Price applied for "
					+ delegateDto.getMemberNo() + " is:::" + price);
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
		Delegate delegate = dao.findByRefId(delegateId, Delegate.class);
		Event event = dao.findByRefId(delegate.getBooking().getEvent()
				.getRefId(), Event.class);
		Booking booking = delegate.getBooking();

		/* Updating Booking */
		if (booking != null) {
			//Update Payment Status
			if ((delegateDto.getBookingPaymentStatus() == PaymentStatus.PAID)
					|| (delegateDto.getBookingPaymentStatus() == PaymentStatus.NOTPAID)
					|| (delegateDto.getBookingPaymentStatus() == PaymentStatus.Credit)) {
				// Copy details from the Dto....
				delegate.setLpoNo(delegateDto.getLpoNo());
				delegate.setIsCredit(delegateDto.getIsCredit());
				delegate.setReceiptNo(delegateDto.getReceiptNo());
				delegate.setClearanceNo(delegateDto.getClearanceNo());
				delegate.setPaymentStatus(delegateDto
						.getDelegatePaymentStatus());
				delegate.setUpdatedBy(delegateDto.getUpdatedBy());
				dao.save(delegate);

				System.err.println("Successfully saved::"
						+ delegate.getFullName());

				int totalDelegates = booking.getDelegates().size();
				int totalPaid = 0;
				for (Delegate d : booking.getDelegates()) {
					if (delegateDto.getBookingPaymentStatus() == PaymentStatus.PAID
							|| (delegateDto.getBookingPaymentStatus() == PaymentStatus.Credit)) {
						totalPaid = totalPaid + 1;
					}
				}

				// Update Booking to PAID - If All the Delegates have Paid
				if (totalPaid == totalDelegates) {
					booking.setPaymentStatus(delegateDto
							.getBookingPaymentStatus());
					booking.setUpdatedBy(delegateDto.getUpdatedBy());
					dao.save(booking);
				}
				updateBookingStats(booking);
				// updatePaymentStats(booking.getEvent().getRefId());
			}
			/*
			 * Undo Booking Cancellation
			 */
			if ((delegateDto.getIsBookingActive() == 1)
					&& ((booking.getIsActive() == 0) || delegate.getIsActive() == 0)) {
				if ((booking.getIsActive() == 0)) {
					booking.setIsActive(delegateDto.getIsBookingActive());
					dao.save(booking);
				} else if (delegate.getIsActive() == 0) {
					delegate.setIsActive(1);
					dao.save(delegate);
				}
				updateBookingStats(booking);
			}

		} else {
			System.err.println("Booking is null...cannot be updated!!");
		}

		/* Updating attendance and CPD */
		String applicationContext = settings.getProperty("app_context");

		// Member
		if (delegate.getMemberRefId() != null
				&& delegate.getAttendance() != delegateDto.getAttendance()
				&& event.getType() != EventType.COURSE
				&& applicationContext.equals("online")) {
			// send and SMS
			Member member = dao.findByRefId(delegate.getMemberRefId(),
					Member.class);
			String smsMessage = "Dear" + " " + delegateDto.getFullName()
					+ ",Thank you for attending the " + event.getName() + "."
					+ "Your ERN No. is " + delegate.getErn();
			smsIntergration.send(member.getUser().getPhoneNumber(), smsMessage);
			delegate.copyFrom(delegateDto);
			dao.save(delegate);
			cpdDao.updateCPDFromAttendance(delegate, delegate.getAttendance());

		// Non-Member
		} else if (delegate.getMemberRefId() == null
				&& delegate.getAttendance() != delegateDto.getAttendance()
				&& event.getType() != EventType.COURSE
				&& applicationContext.equals("online")) {
			String smsMessage = "Dear" + " " + delegateDto.getFullName()
					+ ",Thank you for attending the " + event.getName() + "."
					+ "Your ERN No. is " + delegate.getErn();
			if (delegate.getBooking().getContact().getTelephoneNumbers() != null) {
				smsIntergration.send(delegate.getBooking().getContact()
						.getTelephoneNumbers(), smsMessage);
			}
			delegate.copyFrom(delegateDto);
			dao.save(delegate);

		} else if (event.getType() == EventType.COURSE) {
			List<DelegateDto> delegates = new ArrayList<>();
			delegates.add(delegate.toDto());
			try {
				enrolDelegateToLMS(delegates, event);
			} catch (JSONException | IOException e) {
				e.printStackTrace();
			}
		}

		// respons
		DelegateDto d = delegate.toDto();
		d.setBookingPaymentStatus(booking.getPaymentStatus());
		return d;
	}

	public void sendConfirmationMessages(Booking booking) {
		/* Payers message */
		String smsMessage = "";

		smsMessage = " Thank-you for your payment" + " for "
				+ booking.getEvent().getName()
				+ ". The booking status is PAID. ";

		for (Delegate d : booking.getDelegates()) {
			if (d.getPhoneNumber() != null) {
				String finalPhoneNumber = d.getPhoneNumber()
						.replace("254", "0");
				if (finalPhoneNumber != null) {
					smsIntergration.send(finalPhoneNumber, smsMessage);
					logger.error("sending sms to :" + finalPhoneNumber);
				}
			}
		}

		if (booking.getContact().getEmail() != null) {
			String subject = "PAYMENT CONFIRMATION FOR "
					+ booking.getEvent().getName().toUpperCase();
			try {
				EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '"
						+ subject,
						Arrays.asList(booking.getContact().getEmail()),
						Arrays.asList(booking.getContact().getContactName()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public Integer getDelegatesCount(String eventId, String searchTerm,
			String accomodationRefId, String bookingStatus) {
		logger.error("== Counting delegates ===");
		return dao.getDelegateCount(eventId, searchTerm, accomodationRefId,
				bookingStatus);
	}

	public void enrolDelegateToLMS(List<DelegateDto> delegates, Event event)
			throws JSONException, IOException {
		System.err.println("Enrol delegate to LMS called...");
		logger.info("Delgates size::" + delegates.size());
		String smsMessage = "";
		for (DelegateDto delegate : delegates) {
			if (delegate.getMemberNo() == null
					|| delegate.getMemberNo().isEmpty()) {
				logger.info("Cannot enrol non-members to LMS");
				return;
			}

			CourseRegDetailsPojo details = new CourseRegDetailsPojo();
			User user = userDao.findUserByMemberNo(delegate.getMemberNo());
			if (user == null) {
				logger.info("Cannot find user with this member No....");
				return;
			}

			details.setPhoneNumber(user.getPhoneNumber());
			details.setEmail(user.getEmail());
			details.setMemberRefId(user.getMember().getRefId());
			details.setGender("");
			details.setCourseId(event.getLmsCourseId() + "");
			details.setMembershipID(delegate.getMemberNo());
			details.setFullName(delegate.getFullName());

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

			if (user.getEmail() != null) {
				String subject = "PAYMENT CONFIRMATION FOR "
						+ event.getName().toUpperCase();
				try {
					EmailServiceHelper.sendEmail(smsMessage, "RE: ICPAK '"
							+ subject, Arrays.asList(user.getEmail()),
							Arrays.asList(user.getFullName()));
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
			/* Update Delegate Record */
			Delegate d = dao.findByRefId(delegate.getRefId(), Delegate.class);
			d.copyFrom(delegate);
			dao.save(d);
		}
	}

	public Integer syncWithServer(String eventRefId) {
		List<DelegateDto> allDelegates = getAllDelegates(null, eventRefId, 0,
				1000000, "", "", "");

		int successCount = 0;
		for (DelegateDto d : allDelegates) {
			// Post Delegate to server
			d.setCreatedDate(new Date(1447850439000L));
			try {
				String response = updateDelegateToServer(eventRefId,
						d.getBookingId(), d);
				if (response.equals("Success")) {
					successCount = successCount + 1;
				}
			} catch (URISyntaxException | ParseException | JSONException e) {
				e.printStackTrace();
			}
		}

		return successCount;
	}

	public String updateDelegateToServer(String eventRefId, String bookingId,
			DelegateDto delegate) throws URISyntaxException, ParseException,
			JSONException {
		final HttpClient httpClient = new DefaultHttpClient();

		JSONObject payLoad = new JSONObject(delegate);
		payLoad.remove("attendance");
		payLoad.remove("paymentStatus");
		payLoad.remove("createdDate");

		// Create the understood versions
		if (delegate.getBookingPaymentStatus() == PaymentStatus.PAID) {
			payLoad.put("paymentStatus", "PAID");
		} else {
			payLoad.put("paymentStatus", "NOTPAID");
		}

		if (delegate.getAttendance() == AttendanceStatus.ATTENDED) {
			payLoad.put("attendance", "ATTENDED");
		} else {
			payLoad.put("attendance", "NOTATTENDED");
		}

		payLoad.put("createdDate", 1458215175000L);

		logger.info("payload to Server>>>>" + payLoad.toString());
		String serverAddress = settings.getProperty("delegate_sync_url");
		String params = eventRefId + "/bookings/" + bookingId + "/delegates/"
				+ delegate.getRefId();

		URI uri = new URI(serverAddress + params);
		final HttpPut request = new HttpPut();
		request.setURI(uri);

		String res = "";
		HttpResponse response = null;
		StringBuffer result = null;
		try {
			request.setHeader("accept", "application/json");
			@SuppressWarnings("deprecation")
			StringEntity stringEntity = new StringEntity(payLoad.toString(),
					"application/json", "UTF-8");
			request.setEntity(stringEntity);

			response = httpClient.execute(request);
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			result = new StringBuffer();

			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			return "Success";
		}
		return "Failed";
	}

	public byte[] Backupdbtosql() {
		try {

			/* NOTE: Getting path to the Jar file being executed */
			/*
			 * NOTE: YourImplementingClass-> replace with the class executing
			 * the code
			 */
			// CodeSource codeSource = BookingsDaoHelper.class
			// .getProtectionDomain().getCodeSource();
			// File jarFile = new
			// File(codeSource.getLocation().toURI().getPath());
			// String jarDir = jarFile.getParentFile().getPath();

			String jarDir = settings.getProperty("offline_backup_path");
			String mysqlPath = settings.getProperty("mysql_path");

			/* NOTE: Creating Database Constraints */
			String dbName = settings.getProperty("offline_backup_dbname");
			String dbUser = settings.getProperty("offline_backup_dbuser");
			String dbPass = settings.getProperty("offline_backup_dbpassword");
			String dbPort = settings.getProperty("offline_backup_dbport");

			/* NOTE: Creating Path Constraints for folder saving */
			/* NOTE: Here the backup folder is created for saving inside it */
			String folderPath = jarDir + "\\backup\\";

			/* NOTE: Creating Folder if it does not exist */
			File f1 = new File(folderPath);
			f1.mkdir();

			/* NOTE: Creating Path Constraints for backup saving */
			/*
			 * NOTE: Here the backup is saved in a folder called backup with the
			 * name backup.sql
			 */
			String savePath = "\"" + jarDir + "\\backup\\"
					+ "offlineBackup.sql\"";

			/* NOTE: Used to create a cmd command */
			String executeCmd = mysqlPath
					+ "mysqldump -u"
					+ dbUser
					+ " -p"
					+ dbPass
					+ " -P"
					+ dbPort
					+ " "
					+ dbName
					+ " event accommodation booking delegate invoice invoiceLine docnums doctypeseq"
					+ " -r " + savePath;
			System.err.println(executeCmd);

			/* NOTE: Executing the command here */
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();

			/*
			 * NOTE: processComplete=0 if correctly executed, will contain other
			 * values if not
			 */
			if (processComplete == 0) {
				System.out.println("Backup Complete");

				FileInputStream file = new FileInputStream(new File(folderPath,
						"offlineBackup.sql"));

				assert (file != null);
				// Convert this to bytes
				return IOUtils.toByteArray(file);

			} else {
				System.out.println("Backup Failure with code:"
						+ processComplete);
			}
		} catch (IOException | InterruptedException ex) {
			System.err.println("Error at Backuprestore>>" + ex.getMessage());
		}
		return null;
	}

	public BookingSummaryDto getBookingStats(String eventRefId) {
		return dao.getBookingSummary(eventRefId);
	}

	public void updateBookingStats(String eventRefId) {
		Event e = dao.findByRefId(eventRefId, Event.class);
		if (e == null) {
			System.err.println("Event Not found. Please check the refId sent");
			return;
		}
		updatePaymentStats(eventRefId);

		/* Get All Booking Stats */
		List<Booking> allBookings = dao.getAllBookings(e.getId());
		for (Booking b : allBookings) {
			updateBookingStats(b);
		}
	}

	public void updateBookingStats(Booking booking) {
		logger.info("Updating booking stats....");
		int totalMembers = 0;
		int totalNonMembers = 0;
		int totalPaidMembers = 0;
		int totalPaidNonMembers = 0;
		int totalAccomodatedMembers = 0;
		int totalAccomodatedNonMembers = 0;
		int totalAttendedMembers = 0;
		int totalAttendedNonMembers = 0;
		int totalCancelledMembers = 0;
		int totalCancelledNonMembers = 0;

		Integer totalDelegates = dao.getGenericDelegateCount(booking.getId(),
				"all");
		Integer totalPaid = 0;
		/* How many Paid */
		if (booking.getPaymentStatus() == PaymentStatus.PAID
				|| booking.getPaymentStatus() == PaymentStatus.Credit) {
			totalPaid = totalDelegates;
		} else {
			totalPaid = dao.getGenericDelegateCount(booking.getId(), "paid");
		}

		/* Get Members vs Non Members Summary */
		List<Delegate> delegates = new ArrayList<>(booking.getDelegates());
		for (Delegate d : delegates) {
			if (d.getMemberRegistrationNo() != null) {
				totalMembers = totalMembers + 1;
				if (d.getPaymentStatus() == PaymentStatus.PAID
						|| d.getPaymentStatus() == PaymentStatus.Credit) {
					totalPaidMembers = totalPaidMembers + 1;
				}
				if (d.getAccommodation() != null) {
					totalAccomodatedMembers = totalAccomodatedMembers + 1;
				}
				if (d.getAttendance() == AttendanceStatus.ATTENDED) {
					totalAttendedMembers = totalAttendedMembers + 1;
				}
			} else {
				// Non-Members Summation
				totalNonMembers = totalNonMembers + 1;

				if (d.getPaymentStatus() == PaymentStatus.PAID
						|| d.getPaymentStatus() == PaymentStatus.Credit) {
					totalPaidNonMembers = totalPaidNonMembers + 1;
				}
				if (d.getAccommodation() != null) {
					totalAccomodatedNonMembers = totalAccomodatedNonMembers + 1;
				}
				if (d.getAttendance() == AttendanceStatus.ATTENDED) {
					totalAttendedNonMembers = totalAttendedNonMembers + 1;
				}
			}
		}

		/* Calculate cancellation */
		Integer totalCancelled = 0;
		if (booking.getIsActive() == 0) {
			totalCancelled = totalDelegates;
			totalCancelledMembers = totalMembers;
			totalCancelledNonMembers = totalNonMembers;
		} else if (booking.getIsActive() == 1) {
			totalCancelledMembers = dao.getGenericDelegateCount(
					booking.getId(), "cancelled", true);
			totalCancelledNonMembers = dao.getGenericDelegateCount(
					booking.getId(), "cancelled", false);
			totalCancelled = totalCancelledMembers + totalCancelledNonMembers;
		}

		Integer totalAccomodated = dao.getGenericDelegateCount(booking.getId(),
				"withAccomodation");
		Integer totalAttended = dao.getGenericDelegateCount(booking.getId(),
				"attended");

		// Update Booking:
		booking.setDelegatesCount(totalDelegates);
		booking.setTotalPaid(totalPaid);
		booking.setTotalWithAccomodation(totalAccomodated);
		booking.setTotalCancelled(totalCancelled);
		booking.setTotalAttended(totalAttended);
		booking.setTotalPaidMembers(totalPaidMembers);
		booking.setTotalAccomodatedMembers(totalAccomodatedMembers);
		booking.setTotalCancelledMembers(totalCancelledMembers);
		booking.setTotalAttendedMembers(totalAttendedMembers);
		booking.setTotalMembers(totalMembers);
		booking.setTotalNonMembers(totalNonMembers);
		booking.setTotalPaidNonMembers(totalPaidNonMembers);
		booking.setTotalAccomodatedNonMembers(totalAccomodatedNonMembers);
		booking.setTotalAttendedNonMembers(totalAttendedNonMembers);
		booking.setTotalCancelledNonMembers(totalCancelledNonMembers);

	}

	public void updatePaymentStats(String eventRefId) {
		Event e = dao.findByRefId(eventRefId, Event.class);
		if (e == null) {
			System.err.println("Event Not found. Please check the refId sent");
			return;
		}
		String searchTerm = "Payment for " + e.getName();
		System.err.println("searching for " + searchTerm);
		int totalOffline = 0;

		/* Online Payment modes */
		for (PaymentMode paymentMode : PaymentMode.values()) {
			int count = invoiceDao.getTransactionsCount(null, searchTerm, null,
					null, PaymentType.BOOKING.name(), paymentMode.name());
			switch (paymentMode) {
			case MPESA:
				e.setTotalMpesaPayments(count);
				break;
			case CARDS:
				e.setTotalCardsPayment(count);
				break;
			case BANKTRANSFER:
				totalOffline = totalOffline + count;
				break;
			case DIRECTBANKING:
				totalOffline = totalOffline + count;
				break;
			default:
				break;
			}
			e.setTotalOfflinePayment(totalOffline);
		}
		/* Modes Updated by ICPAK Staff */
		int totalReceipt = dao.getStaffTransactionCount(e.getId(), "receipt");
		int totalLpo = dao.getStaffTransactionCount(e.getId(), "lpo");
		e.setTotalReceiptPayment(totalReceipt);
		e.setTotalCredit(totalLpo);

		System.err.println("Total Mpesa>>" + e.getTotalMpesaPayments());
		System.err.println("Total Cards>>" + e.getTotalCardsPayment());
		System.err.println("Total Offline>>" + totalOffline);
		System.err.println("Total Receipt>>" + totalReceipt);
		System.err.println("Total Offline>>" + totalLpo);

		dao.save(e);

	}
}
