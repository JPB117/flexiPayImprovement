package com.icpak.rest.dao.helper;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.EventsDao;
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
import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

@Transactional
public class BookingsDaoHelper {

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
	SMSIntegration smsIntergration;

	@Inject
	ApplicationSettings settings;
	
	@Inject
	AccommodationsDaoHelper accommodationsDaoHelper;

	public List<BookingDto> getAllBookings(String uriInfo, String eventId,
			Integer offset, Integer limit) {

		List<Booking> list = null;
		if (eventId != null) {
			list = dao.getAllBookings(eventId, offset, limit);
		} else {
			list = dao.getAllBookings(offset, limit);
		}

		List<BookingDto> clones = new ArrayList<>();
		for (Booking booking : list) {
			BookingDto dto = booking.toDto();
			dto.setUri(uriInfo + "/" + dto.getRefId());
			// dto.getEvent().setUri(uriInfo.getBaseUri()+"events/"+clone.getEvent().getRefId());
			clones.add(dto);
		}

		return clones;
	}

	public Integer getCount(String eventId) {
		return dao.getBookingCount(eventId);
	}

	public BookingDto getBookingById(String eventId, String bookingId) {
		Booking booking = dao.getByBookingId(bookingId);
		BookingDto bookingDto = booking.toDto();
		bookingDto.setInvoiceRef(dao.getInvoiceRef(bookingId));
		return bookingDto;
	}

	public BookingDto createBooking(String eventId, BookingDto dto) {
		assert dto.getRefId() == null;
		Event event = eventDao.getByEventId(eventId);

		Booking booking = new Booking();
		booking.setRefId(IDUtils.generateId());
		booking.setEvent(event);
		if (dto.getContact() != null) {
			Contact poContact = new Contact();
			ContactDto contactDto = dto.getContact();
			poContact.copyFrom(contactDto);
			booking.setContact(poContact);
		}
		booking.copyFrom(dto);

		List<DelegateDto> dtos = dto.getDelegates();
		Collection<Delegate> delegates = new ArrayList<>();

		double total = 0.0;
		if (dtos != null)
			for (DelegateDto delegateDto : dtos) {
				Delegate d = get(delegateDto);
				if (delegateDto.getAccommodation() != null) {
					Accommodation accommodation = dao
							.findByRefId(delegateDto.getAccommodation()
									.getRefId(), Accommodation.class);
					if (accommodation != null) {
						d.setAccommodation(accommodation);
					}
				}

				// Event Pricing
				Double price = event.getNonMemberPrice();
				if (delegateDto.getMemberId() != null) {
					price = event.getMemberPrice();
				}

				// Add Accommodation Charge
				if (d.getAccommodation() != null) {
					price += d.getAccommodation().getFee();
				}
				d.setAmount(price); // Charge for delegate
				total += price;

				// Set Booking
				// d.setBooking(booking);
				delegates.add(d);
				// dao.save(d);

			}
		booking.setAmountDue(total);// Total
		booking.setDelegates(delegates);
		dao.createBooking(booking);

		sendProInvoice(booking);
		sendDelegateSMS(booking);

		// Copy into dto
		dto.setRefId(booking.getRefId());
		int i = 0;
		for (Delegate delegate : booking.getDelegates()) {
			dto.getDelegates().get(i).setRefId(delegate.getRefId());
			dto.getDelegates().get(i).setErn(delegate.getErn());
		}

		// dao.getEntityManager().merge(booking);
		dto.setInvoiceRef(dao.getInvoiceRef(booking.getRefId()));
		
		//update accomodation spaces available
		updateAccomodationEntry(event);
		
		return dto;
	}

	private void sendProInvoice(Booking booking) {
		InvoiceDto invoice = generateInvoice(booking);
		Event event = booking.getEvent();
		String subject = booking.getEvent().getName() + "' Event Registration";
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d Y");
		try {
			Map<String, Object> emailValues = new HashMap<String, Object>();
			emailValues.put("companyName", invoice.getCompanyName());
			emailValues.put("companyAddress", invoice.getCompanyAddress());
			emailValues.put("companyLocation", booking.getContact()
					.getPhysicalAddress());
			emailValues.put("contactPhone", booking.getContact()
					.getPhysicalAddress());

			emailValues.put("quoteNo", invoice.getDocumentNo());
			emailValues.put("date", invoice.getDate());
			emailValues.put("firstName", invoice.getContactName());
			emailValues.put("eventName", booking.getEvent().getName());
			emailValues.put("eventStartDate",
					formatter.format(booking.getEvent().getStartDate()));
			emailValues.put("DocumentURL", settings.getApplicationPath());
			emailValues.put("email", booking.getContact().getEmail());
			emailValues.put("eventId", booking.getEvent().getRefId());
			emailValues.put("bookingId", booking.getRefId());
			Doc emailDocument = new Doc(emailValues);

			// Collection of Delegates
			Collection<Delegate> delegates = booking.getDelegates();
			int counter = 0;

			for (Delegate delegate : delegates) {
				counter++;
				emailValues.put("counter", counter);
				emailValues.put("delegateNames", delegate.getSurname() + " "
						+ delegate.getOtherNames());

				if (delegate.getMemberRegistrationNo() != null) {
					emailValues.put("memberType", "Member");
				} else {
					emailValues.put("memberType", "Non-Member");
				}
				emailValues.put("ernNo", delegate.getErn());

				emailValues.put("accomodationName", (delegate
						.getAccommodation() == null ? "None" : delegate
						.getAccommodation().getHotel()
						+ " "
						+ delegate.getAccommodation().getNights()));

				DocumentLine docLine = new DocumentLine("accomadationDetails",
						emailValues);
				emailDocument.addDetail(docLine);
			}

			Map<String, Object> line = new HashMap<String, Object>();
			Doc proformaDocument = new Doc(emailValues);
			for (InvoiceLineDto dto : invoice.getLines()) {
				line.put("description", dto.getDescription());
				line.put("unitPrice", dto.getUnitPrice());
				line.put("amount", dto.getTotalAmount());
				proformaDocument.addDetail(new DocumentLine("invoiceDetails",
						line));
			}
			emailValues.put("totalAmount", invoice.getInvoiceAmount());

			// PDF Invoice Generation
			InputStream inv = EmailServiceHelper.class.getClassLoader()
					.getResourceAsStream("proforma-invoice.html");
			String invoiceHTML = IOUtils.toString(inv);
			byte[] invoicePDF = new HTMLToPDFConvertor().convert(
					proformaDocument, new String(invoiceHTML));
			Attachment attachment = new Attachment();
			attachment.setAttachment(invoicePDF);
			attachment.setName("ProForma Invoice_"
					+ booking.getContact().getContactName() + ".pdf");

			// Email Template parse and map variables
			InputStream is = EmailServiceHelper.class.getClassLoader()
					.getResourceAsStream("booking-email.html");
			String html = IOUtils.toString(is);
			html = new DocumentHTMLMapper().map(emailDocument, html);

			EmailServiceHelper.sendEmail(html, "RE: ICPAK '" + subject,
					Arrays.asList(booking.getContact().getEmail()),
					Arrays.asList(booking.getContact().getContactName()),
					attachment);

		} catch (Exception e) {
			e.printStackTrace();
		}

		String trxRef = trxHelper.charge(booking.getMemberId(),
				booking.getBookingDate(), subject, event.getStartDate(),
				invoice.getInvoiceAmount(), "Booking #" + booking.getId(),
				invoice.getRefId());
	}

	public InvoiceDto generateInvoice(Booking booking) {
		InvoiceDto invoice = new InvoiceDto();
		invoice.setDescription("Event Booking Invoice");
		double amount = 0.0;

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

		InvoiceLineDto memberInvoice = new InvoiceLineDto();
		memberInvoice.setQuantity(0);
		memberInvoice.setMemberNames("");

		InvoiceLineDto nonMemberInvoice = new InvoiceLineDto();
		nonMemberInvoice.setQuantity(0);
		nonMemberInvoice.setMemberNames("");

		Map<String, InvoiceLineDto> memberRefLineMap = new HashMap<>();
		Map<String, InvoiceLineDto> nonMemberRefLineMap = new HashMap<>();

		Event event = booking.getEvent();
		for (Delegate delegate : delegates) {
			delegate.setErn(dao.getErn(delegate.getRefId()));

			if (delegate.getMemberRegistrationNo() != null) {
				String description = "%s - %s fees for %d members: %s";
				memberInvoice.setMemberNames(memberInvoice.getMemberNames()
						.concat((memberInvoice.getMemberNames().isEmpty() ? ""
								: ", ") + delegate.toString()));

				int qty = memberInvoice.getQuantity() + 1;

				memberInvoice.setEventDelegateRefId(delegate.getRefId());
				description = String.format(description, event.getName(), event
						.getType().getDisplayName(), qty, memberInvoice
						.getMemberNames());
				memberInvoice.setDescription(description);
				memberInvoice.setQuantity(qty);
				memberInvoice.setUnitPrice(delegate.getAmount());
				memberInvoice.setTotalAmount(qty * delegate.getAmount());
				amount += memberInvoice.getTotalAmount();
			} else {
				String description = "%s - %s fees for %d non-members: %s";
				int qty = nonMemberInvoice.getQuantity() + 1;
				nonMemberInvoice.setEventDelegateRefId(delegate.getRefId());

				nonMemberInvoice
						.setMemberNames(nonMemberInvoice.getMemberNames()
								.concat((nonMemberInvoice.getMemberNames()
										.isEmpty() ? "" : ", ")
										+ delegate.toString()));

				description = String.format(description, event.getName(), event
						.getType().getDisplayName(), qty, nonMemberInvoice
						.getMemberNames());
				nonMemberInvoice.setDescription(description);
				nonMemberInvoice.setQuantity(qty);
				nonMemberInvoice.setUnitPrice(delegate.getAmount());
				nonMemberInvoice.setTotalAmount(qty * delegate.getAmount());
				amount += nonMemberInvoice.getTotalAmount();
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
					amount += line.getTotalAmount();
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
					amount += line.getTotalAmount();
				}

				// delegate.getAccommodation().getRefId();

			}
		}

		if (memberInvoice.getQuantity() != 0) {
			invoice.addLine(memberInvoice);
		}
		if (nonMemberInvoice.getQuantity() != 0) {
			invoice.addLine(nonMemberInvoice);
		}

		invoice.addLines(memberRefLineMap.values());
		invoice.addLines(nonMemberRefLineMap.values());

		invoice.setAmount(amount);
		invoice.setDocumentNo(booking.getId() + "");
		invoice.setCompanyName(booking.getContact().getCompany());
		invoice.setDate(booking.getBookingDate().getTime());
		invoice.setCompanyAddress(booking.getContact().getAddress());
		invoice.setContactName(booking.getContact().getContactName());
		invoice.setPhoneNumber(booking.getContact().getTelephoneNumbers());
		invoice.setBookingRefId(booking.getRefId());

		invoice = invoiceHelper.save(invoice);

		return invoice;
	}

	public BookingDto updateBooking(String eventId, String bookingId,
			BookingDto dto) {
		assert dto.getRefId() != null;

		Booking poBooking = dao.getByBookingId(bookingId);
		poBooking.copyFrom(dto);
		poBooking.setEvent(eventDao.getByEventId(eventId));
		if (dto.getContact() != null) {
			Contact poContact = poBooking.getContact();
			if (poContact == null) {
				poContact = new Contact();
			}

			ContactDto contactDto = dto.getContact();
			poContact.copyFrom(contactDto);
		}

		List<DelegateDto> dtos = dto.getDelegates();
		Collection<Delegate> delegates = new ArrayList<>();
		for (DelegateDto delegateDto : dtos) {
			Delegate delegate = get(delegateDto);
			dao.save(delegate);
			delegates.add(delegate);
		}
		poBooking.setDelegates(delegates);

		dao.save(poBooking);
		sendProInvoice(poBooking);
		sendDelegateSMS(poBooking);

		BookingDto bookingDto = poBooking.toDto();
		dto.setInvoiceRef(dao.getInvoiceRef(bookingId));
		return bookingDto;
	}

	private void sendDelegateSMS(Booking booking) {
		Event event = booking.getEvent();
		Collection<Delegate> delegates = booking.getDelegates();
		List<Delegate> delegateList = new ArrayList<>();
		delegateList.addAll(delegates);

		for (Delegate delegate : delegateList) {
			String startDate = new SimpleDateFormat("dd/MM/yyyy").format(event
					.getStartDate());
			String endDate = new SimpleDateFormat("dd/MM/yyyy").format(event
					.getEndDate());
			String smsMemssage = "Dear" + " " + delegate.getSurname() + ","
					+ booking.getContact().getContactName()
					+ " has booked you for " + event.getName()
					+ " held between " + startDate + " to " + endDate
					+ ".Call us for any query.";

			if (delegate.getMemberRefId() != null) {
				Member member = memberDao.findByRefId(
						delegate.getMemberRefId(), Member.class);
				System.err.println("Sending SMS to "
						+ member.getUser().getPhoneNumber());

				if (member.getUser().getPhoneNumber() != null) {
					smsIntergration.send(member.getUser().getPhoneNumber(),
							smsMemssage);
				}
			} else {
				System.err.println("Non-member cannot be send sms..");
			}
		}

	}

	private Delegate get(DelegateDto delegateDto) {
		Delegate delegate = new Delegate();
		if (delegateDto.getRefId() != null) {
			delegate = eventDao
					.findByRefId(delegate.getRefId(), Delegate.class);
		}

		delegate.copyFrom(delegateDto);
		// assert delegate.getMemberRegistrationNo()!=null;

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

	public DelegateDto updateDelegate(String bookingId, String delegateId,
			DelegateDto delegateDto) {

		Delegate delegate = dao.findByRefId(delegateId, Delegate.class);

		if (delegate.getMemberRefId() != null
				&& delegate.getAttendance() != delegateDto.getAttendance()) {
			// send and SMS
			Member member = dao.findByRefId(delegate.getMemberRefId(),
					Member.class);
			Event event = dao
					.findByRefId(delegateDto.getEventId(), Event.class);

			String startDate = new SimpleDateFormat("dd/MM/yyyy").format(event
					.getStartDate());
			String endDate = new SimpleDateFormat("dd/MM/yyyy").format(event
					.getEndDate());
			String smsMemssage = "Dear"
					+ " "
					+ delegateDto.getSurname()
					+ ",You have been marked as "
					+ delegateDto.getAttendance().getDisplayName()
							.toLowerCase()
					+ " for "
					+ event.getName()
					+ " held between "
					+ startDate
					+ " to "
					+ endDate
					+ (delegateDto.getAttendance() == AttendanceStatus.ATTENDED ? ". You have earned "
							+ event.getCpdHours() + " Hrs."
							: "");
			smsIntergration
					.send(member.getUser().getPhoneNumber(), smsMemssage);
		}
		delegate.setAttendance(delegateDto.getAttendance());
		dao.save(delegate);

		cpdDao.updateCPDFromAttendance(delegate, delegate.getAttendance());

		return delegate.toDto();
	}

	public List<MemberBookingDto> getMemberBookings(String memberRefId,
			int offset, int limit) {
		return dao.getMemberBookings(memberRefId, offset, limit);
	}
	
	public void updateAccomodationEntry(Event event){
		Set<Accommodation> accommodations = event.getAccommodation();
		
		for(Accommodation accommodation : accommodations){
			Set<Delegate> delegates = accommodation.getDelegates();
			int spacesOccupied = delegates.size();
			accommodation.setSpaces(accommodation.getSpaces() - spacesOccupied);
			
			accommodationsDaoHelper.update(event.getRefId(), accommodation.getRefId(), accommodation.toDto());
		}
		
	}
}
