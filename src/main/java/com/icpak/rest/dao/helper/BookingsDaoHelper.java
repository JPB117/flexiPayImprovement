package com.icpak.rest.dao.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Category;
import com.icpak.rest.models.membership.Contact;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.utils.Doc;
import com.icpak.rest.utils.DocumentHTMLMapper;
import com.icpak.rest.utils.DocumentLine;
import com.icpak.rest.utils.EmailServiceHelper;
import com.icpak.rest.utils.HTMLToPDFConvertor;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

@Transactional
public class BookingsDaoHelper {

	@Inject
	BookingsDao dao;
	@Inject
	UsersDao userDao;
	@Inject
	EventsDao eventDao;
	
	@Inject TransactionDaoHelper trxHelper;

	public List<BookingDto> getAllBookings(String uriInfo, String eventId,
			Integer offset, Integer limit) {

		List<Booking> list = dao.getAllBookings(offset, limit);

		List<BookingDto> clones = new ArrayList<>();
		for (Booking booking : list) {
			BookingDto dto = booking.toDto();
			dto.setUri(uriInfo + "/" + dto.getRefId());
			// dto.getEvent().setUri(uriInfo.getBaseUri()+"events/"+clone.getEvent().getRefId());
			clones.add(dto);
		}

		return clones;
	}

	public BookingDto getBookingById(String eventId, String bookingId) {

		Booking booking = dao.getByBookingId(bookingId);
		return booking.toDto();
	}

	public BookingDto createBooking(String eventId, BookingDto dto) {
		assert dto.getRefId() == null;

		Booking booking = new Booking();
		booking.setRefId(IDUtils.generateId());
		booking.setEvent(eventDao.getByEventId(eventId));
		if (dto.getContact() != null) {
			Contact poContact = new Contact();
			ContactDto contactDto = dto.getContact();
			poContact.copyFrom(contactDto);
			booking.setContact(poContact);
		}
		booking.copyFrom(dto);
		dao.createBooking(booking);
		
		List<DelegateDto> dtos = dto.getDelegates();
		Collection<Delegate> delegates = new ArrayList<>();
		for (DelegateDto delegateDto : dtos) {
			Delegate d = get(delegateDto);
			
			d.setBooking(booking);
			dao.save(d);
			delegates.add(d);
		}
		
		dao.getEntityManager().merge(booking);
		
		//Copy into dto
		dto.setRefId(booking.getRefId());
		int i=0;
		for(Delegate delegate: booking.getDelegates()){
			dto.getDelegates().get(i).setRefId(delegate.getRefId());
		}
		
		sendProInvoice(booking);
		
		return dto;
	}

	private void sendProInvoice(Booking booking) {
		try{
			Map<String,Object> values  = new HashMap<String, Object>();
			values.put("companyName", booking.getContact().getCompany());
			values.put("companyAddress", booking.getContact().getAddress());
			values.put("quoteNo", booking.getId());
			values.put("date", booking.getBookingDate());
			values.put("firstName", booking.getContact().getContactName());
			values.put("DocumentURL", "http://127.0.0.1:8888/icpakportal.html");
			values.put("email", booking.getContact().getEmail());
			values.put("eventId", booking.getEvent().getRefId());
			values.put("bookingId", booking.getRefId());
			Doc doc = new Doc(values);
			
			Event event = booking.getEvent();
			
			double amount = 0.0;
			for(Delegate delegate : booking.getDelegates()){
				Map<String,Object> line  = new HashMap<String, Object>();
				line.put("description", delegate.getSurname()+" "+delegate.getOtherNames());
				Double price = event.getNonMemberPrice();
				if(delegate.getMemberRegistrationNo()!=null){
					price = event.getMemberPrice();
				}
				amount+=price;
				line.put("unitPrice", price);
				line.put("amount", price);
				doc.addDetail(new DocumentLine("invoiceDetails",line));
			}
			
			values.put("totalAmount", amount);
			
			String subject = booking.getEvent().getName()+"' Event Registration";
			//PDF Invoice Generation
			InputStream inv = EmailServiceHelper.class.getClassLoader().getResourceAsStream("proforma-invoice.html");
			String invoiceHTML = IOUtils.toString(inv);
			byte[] invoicePDF = new HTMLToPDFConvertor().convert(doc, new String(invoiceHTML));
			Attachment attachment = new Attachment();
			attachment.setAttachment(invoicePDF);
			attachment.setName("ProForma Invoice_"+booking.getContact().getContactName()+".pdf");
			
			//Email Template parse and map variables
			InputStream is = EmailServiceHelper.class.getClassLoader().getResourceAsStream("booking-email.html");
			String html = IOUtils.toString(is);
			html = new DocumentHTMLMapper().map(doc, html);
			EmailServiceHelper.sendEmail(html, "RE: ICPAK '"+subject,
					Arrays.asList(booking.getContact().getEmail()),
					Arrays.asList(booking.getContact().getContactName()), attachment);	
			
			trxHelper.charge(booking.getUserId(),
					booking.getBookingDate(), subject, event.getStartDate(), amount,
					"Booking #"+booking.getId());
			
		}catch(Exception e){
			e.printStackTrace();
		}
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

		return poBooking.toDto();
	}

	private Delegate get(DelegateDto delegateDto) {
		Delegate delegate = new Delegate();
		if(delegateDto.getRefId()!=null){
			delegate = eventDao.findByRefId(delegate.getRefId(), Delegate.class);
		}
		
		delegate.copyFrom(delegateDto);
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

}
