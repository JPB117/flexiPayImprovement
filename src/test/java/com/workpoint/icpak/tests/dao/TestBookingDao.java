package com.workpoint.icpak.tests.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.models.event.Delegate;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestBookingDao extends AbstractDaoTest {

	Logger logger = Logger.getLogger(TestBookingDao.class);

	@Inject
	BookingsDaoHelper bookingsHelper;
	@Inject
	CPDDaoHelper daoHelper;
	@Inject
	EventsDao eventDao;
	@Inject
	BookingsDao bookingDao;

	@Ignore
	public void getTrx() {
		bookingsHelper.getMemberBookings("lYEAuIVOBGoh9e9j", 0, 100);
	}

	@Ignore
	public void member() {
		Delegate d = eventDao.findByRefId("dvIGX5Qn5Y3T3oEJ", Delegate.class);
		daoHelper.updateCPDFromAttendance(d, AttendanceStatus.ATTENDED);
	}

	@Ignore
	public void getBooking() {

		BookingDto dto = bookingsHelper.getBookingById("Jx4Ca6HpOutf2ic7",
				"Ac920rNN12ILqKFN");
		for (DelegateDto delegate : dto.getDelegates()) {
			delegate.getAccommodation();
		}

	}

	@Test
	public void createBooking() {
		BookingDto dto = new BookingDto();

		dto.setStatus("");
		dto.setPaymentStatus(PaymentStatus.NOTPAID);
		dto.setBookingDate(new Date().getTime());
		ContactDto contact = new ContactDto();
		contact.setAddress("P.o Box 3434");
		contact.setCity("Nrb");
		contact.setCompany("Workpoint");
		contact.setContactName("Kimani");
		contact.setCountry("KENYA");
		contact.setEmail("tomkim@wira.io");
		contact.setPostCode("00200");
		contact.setTelephoneNumbers("02302023");
		dto.setContact(contact);

		List<DelegateDto> delegates = new ArrayList<>();
		DelegateDto delegate = new DelegateDto();
		delegate.setEmail("tomkim@wira.io");
		delegate.setOtherNames("Kimani");
		delegate.setSurname("Tom");
		delegate.setMemberNo("10000");
		delegates.add(delegate);
		dto.setDelegates(delegates);

		// dto.setCurrency(currency);
		BookingDto booking = bookingsHelper.createBooking("PrjIf8x4RIDaPZIv",
				dto);
		
		

		System.err.println(">> event RefId" + booking.getEventRefId());
		System.err.println(">> BOoking RefId" + booking.getRefId());
		
		bookingsHelper.sendProInvoice(booking.getRefId());
	}

	@Ignore
	public void testDeleteInvoiceFrombooking() {
		String bookingRefid = "RVfeXiMtWWETmQRi";
		bookingDao.deleteAllBookingInvoice(bookingRefid);
	}

	@Ignore
	public void testSearch() {
		List<DelegateDto> delegates = bookingsHelper.getAllDelegates("",
				"PrjIf8x4RIDaPZIv", null, 1000, "Kim");
		System.err.println(delegates.size());

	}

	@Ignore
	public void testSearchCount() {
		System.err.println(bookingsHelper.getDelegatesCount("PrjIf8x4RIDaPZIv",
				"Tom"));
	}

	@Ignore
	public void testSearchEvents() {
		System.err.println(eventDao.getAllEvents(0, 1000, null, "Fina").size());
		System.err.println(eventDao.getSearchEventCount("Fin"));
	}
}
