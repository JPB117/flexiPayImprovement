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
import com.icpak.rest.models.event.Booking;
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

		BookingDto dto = bookingsHelper.getBookingById("Jx4Ca6HpOutf2ic7", "Ac920rNN12ILqKFN");
		for (DelegateDto delegate : dto.getDelegates()) {
			delegate.getAccommodation();
		}

	}

	@Ignore
	public void testGenerateEmail() {
		bookingsHelper.sendProInvoice("C6lcgHyPYUDCcUBi");
	}

	@Ignore
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
		delegate.setMemberNo("1");
		delegates.add(delegate);
		dto.setDelegates(delegates);

		// dto.setCurrency(currency);
		// BookingDto booking = bookingsHelper.createBooking("PrjIf8x4RIDaPZIv
		// ",
		// dto);
		//
		// System.err.println(booking.getRefId());

		// bookingsHelper.sendProInvoice(booking.getRefId());
		// Booking bookingModel = new Booking();
		// bookingModel.copyFrom(booking);

		// bookingsHelper.generateInvoice(bookingModel);

		List<DelegateDto> delegateDtos = bookingsHelper.getAllDelegates(null, "Jx4Ca6HpOutf2ic7", null, 1000,
				"wainaina");

		for (DelegateDto delegateDto : delegateDtos) {
			logger.error(" booking ref id " + delegateDto.getBookingId());
		}
	}

	@Ignore
	public void TestImportBookings() {
		bookingDao.importAllEvents();
	}

	@Ignore
	public void testDeleteInvoiceFrombooking() {
		String bookingRefid = "RVfeXiMtWWETmQRi";
		bookingDao.deleteAllBookingInvoice(bookingRefid);
	}

	@Test
	public void testSearch() {
		List<DelegateDto> delegates = bookingsHelper.getAllDelegates("", "Jx4Ca6HpOutf2ic7", null, 1000, "ki");
		System.err.println(bookingsHelper.getDelegatesCount("Jx4Ca6HpOutf2ic7", "ki"));
		
		for(DelegateDto d : delegates){
			logger.error(" Delegate Ref Id = "+ d.getRefId());
		}
		
		System.err.println(delegates.size());

	}

	@Ignore
	public void testSearchCount() {
		System.err.println(bookingsHelper.getDelegatesCount("Jx4Ca6HpOutf2ic7", "ki"));
	}

	@Ignore
	public void testEnrolCourse() {
		// bookingsHelper.enrolDelegateToLMS(delegates, bookingId);
	}

	@Ignore
	public void testSearchEvents() {
		System.err.println(eventDao.getAllEvents(0, 1000, null, "Fina").size());
		System.err.println(eventDao.getSearchEventCount("Fin"));
	}

	@Ignore
	public void testUpdate(){
		
		Delegate delInDb = eventDao.findByRefId("ZOrfIhI5IBnVvuNn", Delegate.class);
		DelegateDto delegate  = delInDb.toDto();
		
		delegate.setClearanceNo("5252852");
		delegate.setLpoNo("2828662368238");
		delegate.setReceiptNo("552528528525");
		delegate.setIsCredit(1);
		delegate.setAttendance(AttendanceStatus.ATTENDED);
		delegate.setEventRefId("Jx4Ca6HpOutf2ic7");
		
		bookingsHelper.updateDelegate(null,delegate.getRefId(),delegate);
		}
}
