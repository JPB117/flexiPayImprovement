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
import com.workpoint.icpak.shared.model.events.AccommodationDto;
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

	// @Test
	public void testDelegateCheck() {
		BookingDto booking = bookingsHelper.checkEmailExists("tomkim@wira.io",
				"UJDQSrOzKaplbgfY");
		System.err.println(booking);
	}

	@Ignore
	public void testGenerateEmail() {
		bookingsHelper.sendProInvoice("C6lcgHyPYUDCcUBi");
	}

	@Ignore
	public void testGetDelegate() {
		List<DelegateDto> delegates = bookingsHelper.getAllDelegates(null,
				"QEyf2DasD3X7Pybt", 0, 10, "", "", "");

		for (DelegateDto del : delegates) {
			System.err.println("Booking Ref>>" + del.getBookingRefId() + " "
					+ del.getErn());
		}
	}

	// @Test
	public void testUpdateBooking() {
		BookingDto dto = new BookingDto();
		dto.setRefId("TdtJ9I4RtlGqoAKT");
		dto.setPaymentStatus(PaymentStatus.NOTPAID);
		dto.setBookingDate(new Date().getTime());
		ContactDto contact = new ContactDto();
		contact.setAddress("P.o Box 3434");
		contact.setCity("Nrb");
		contact.setCompany("Nairobi City County Assembly");
		contact.setContactName("The County Assembly Clerk");
		contact.setCountry("KENYA");
		contact.setEmail("tomkim@wira.io");
		contact.setPostCode("00200");
		contact.setTelephoneNumbers("02302023");
		dto.setContact(contact);

		List<DelegateDto> delegates = new ArrayList<>();
		DelegateDto delegate = new DelegateDto();
		delegate.setEmail("tomkim@wira.io");
		delegate.setFullName("CPA TOM KIMANI MURIRANJA");
		delegates.add(delegate);

		DelegateDto delegate2 = new DelegateDto();
		delegate2.setMemberNo("7087");
		delegate2.setEmail("dmilgo@wira.io");
		delegate2.setFullName("CPA DENNIS MILGO");
		delegates.add(delegate2);

		DelegateDto delegate3 = new DelegateDto();
		delegate3.setMemberNo("7087");
		delegate3.setEmail("tkm2020@wira.io");
		delegate3.setFullName("CPA IMELDA MUENI");
		delegates.add(delegate3);
		dto.setDelegates(delegates);

		BookingDto booking = bookingsHelper.createBooking("UJDQSrOzKaplbgfY",
				dto);

		System.err.println("Ammended delegate size>>>>"
				+ booking.getDelegates().size());

	}

	// @Test
	public void createBooking() {
		BookingDto dto = new BookingDto();
		dto.setStatus("");
		dto.setPaymentStatus(PaymentStatus.NOTPAID);
		dto.setBookingDate(new Date().getTime());
		ContactDto contact = new ContactDto();
		contact.setAddress("P.o Box 3434");
		contact.setCity("Nrb");
		contact.setCompany("Nairobi City County Assembly");
		contact.setContactName("The County Assembly Clerk");
		contact.setCountry("KENYA");
		contact.setEmail("tomkim@wira.io");
		contact.setPostCode("00200");
		contact.setTelephoneNumbers("02302023");
		dto.setContact(contact);

		List<DelegateDto> delegates = new ArrayList<>();
		DelegateDto delegate = new DelegateDto();
		delegate.setEmail("tomkim@wira.io");
		delegate.setFullName("CPA TOM KIMANI MURIRANJA");
		delegates.add(delegate);
		dto.setDelegates(delegates);
		AccommodationDto a = new AccommodationDto();
		a.setRefId("b5b0I7wIh09oM7ab");
		delegate.setAccommodation(a);

		DelegateDto delegate2 = new DelegateDto();
		delegate2.setMemberNo("7087");
		delegate2.setEmail("tomkim@wira.io");
		delegate2.setFullName("CPA TOM KIMANI MURIRANJA");
		delegates.add(delegate2);
		dto.setDelegates(delegates);

		DelegateDto delegate3 = new DelegateDto();
		delegate3.setMemberNo("7087");
		delegate3.setEmail("tomkim@wira.io");
		delegate3.setFullName("CPA TOM KIMANI MURIRANJA");
		delegates.add(delegate3);
		dto.setDelegates(delegates);

		BookingDto booking = bookingsHelper.createBooking("1JKH5e8rLJjnjjwv",
				dto);
		System.err.println(booking.getRefId());

		bookingsHelper.sendProInvoice(booking.getRefId());
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

	// @Test
	public void testSearch() {
		List<DelegateDto> delegates = bookingsHelper.getAllDelegates("",
				"IWqduDqhOKXb9nxq", null, 1000, "", "", "1");
		System.err.println(bookingsHelper.getDelegatesCount("IWqduDqhOKXb9nxq",
				"", "", "1"));
		for (DelegateDto d : delegates) {
			logger.error(" Event RefId= " + d.getEventRefId());
		}
		System.err.println(delegates.size());
	}

	// @Test
	public void testSearchByQrCode() {
		List<DelegateDto> delegates = bookingsHelper.getDelegateByQrCode("",
				"UJDQSrOzKaplbgfY", 0, 10, "eqreqreididmqkerm");
		for (DelegateDto d : delegates) {
			logger.error(" Delegate Ref Id = " + d.getRefId());
		}
		System.err.println("found this size::::" + delegates.size());
	}

	@Ignore
	public void testSearchCount() {
		System.err.println(bookingsHelper.getDelegatesCount("Jx4Ca6HpOutf2ic7",
				"ki", "", ""));
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
	public void testUpdate() {
		Delegate delInDb = eventDao.findByRefId("ZOrfIhI5IBnVvuNn",
				Delegate.class);
		DelegateDto delegate = delInDb.toDto();

		delegate.setClearanceNo("5252852");
		delegate.setLpoNo("2828662368238");
		delegate.setReceiptNo("552528528525");
		delegate.setIsCredit(1);
		delegate.setAttendance(AttendanceStatus.ATTENDED);
		delegate.setEventRefId("Jx4Ca6HpOutf2ic7");
		// bookingsHelper.updateDelegate(null, delegate.getRefId(), delegate);
	}

	// @Test
	public void testDbBackUp() {
		bookingsHelper.Backupdbtosql();
	}
	
	@Test
	public void testSyncWithServer(){
		bookingsHelper.syncWithServer("dQdcmmGqPuw7wVzr");
	}
}
