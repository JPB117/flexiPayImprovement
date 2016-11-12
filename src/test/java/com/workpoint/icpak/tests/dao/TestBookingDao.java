package com.workpoint.icpak.tests.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.rest.rebind.utils.Arrays;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.EventsDaoHelper;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.util.SMSIntegration;
import com.icpak.servlet.upload.GetDelegatesReport;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;
import com.workpoint.icpak.shared.model.events.MpesaDTO;
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
	EventsDaoHelper eventDaoHelper;
	@Inject
	BookingsDao bookingDao;

	@Inject
	SMSIntegration smsIntergration;

	@Ignore
	public void getTrx() {
		bookingsHelper.getMemberBookings("lYEAuIVOBGoh9e9j", 0, 100);
	}

	@Test
	public void testUpdateDelegate() {
		Delegate d = eventDao.findByRefId("pc1YDNsd1fT4aLdd", Delegate.class);
		DelegateDto dto = d.toDto();
		dto.setIsAccomodationPaid(1);
		dto.setAccomodationPaidAmount("5800");
		bookingsHelper.updateDelegate("pc1YDNsd1fT4aLdd", dto);
	}

	// @Test
	public void getBooking() {
		BookingDto dto = bookingsHelper.getBookingById("1JKH5e8rLJjnjjwv", "DYWKsoP44X1llBKc");
		for (DelegateDto delegate : dto.getDelegates()) {
			System.err.println(delegate.getMemberNo() + " - " + delegate.getFullName());
		}
	}

	// @Test
	public void testDelegateCheck() {
		BookingDto booking = bookingsHelper.checkEmailExists("tomkim@wira.io", "UJDQSrOzKaplbgfY");
		System.err.println(booking);
	}

	@Ignore
	public void testGenerateEmail() {
		bookingsHelper.sendProInvoice("C6lcgHyPYUDCcUBi");
	}

	// @Test
	public void testGetDelegate() {
		List<DelegateDto> delegates = bookingsHelper.getAllDelegates(null, "W1L4UsrlOiHvkH6V", 0, 10, "", "", "");

		for (DelegateDto del : delegates) {
			System.err.println("Booking Ref>>" + del.getBookingRefId() + " " + del.getErn());
		}
	}

	@Inject
	GetDelegatesReport getReport;

	// @Test
	public void testNameStripping() {
		// getReport.GetDelegatesReport(Arrays.asList(new DelegateDto()), "xls",
		// "The 2nd Chairman\\'s Ball");
	}

	// @Test
	public void testUpdateBooking() {
		BookingDto dto = new BookingDto();
		// dto.setRefId("TdtJ9I4RtlGqoAKT"); //Ammending booking
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
		delegate.setFullName("TOM KIMANI MURIRANJA");
		delegates.add(delegate);

		DelegateDto delegate2 = new DelegateDto();
		delegate2.setMemberNo("7087");
		delegate2.setEmail("dmilgo@wira.io");
		delegate2.setFullName("CPA DENNIS MILGO");
		delegates.add(delegate2);

		DelegateDto delegate3 = new DelegateDto();
		delegate3.setMemberNo("5305");
		delegate3.setEmail("tkm2020@wira.io");
		delegate3.setFullName("CPA DANIEL MUTHAMA");
		delegates.add(delegate3);

		dto.setDelegates(delegates);

		BookingDto booking = bookingsHelper.createBooking("1JKH5e8rLJjnjjwv", dto);

		System.err.println("Ammended delegate size>>>>" + booking.getDelegates().size());

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

		BookingDto booking = bookingsHelper.createBooking("1JKH5e8rLJjnjjwv", dto);
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
		List<DelegateDto> delegates = bookingsHelper.getAllDelegates("", "IWqduDqhOKXb9nxq", null, 1000, "", "", "1");
		System.err.println(bookingsHelper.getDelegatesCount("IWqduDqhOKXb9nxq", "", "", "1"));
		for (DelegateDto d : delegates) {
			logger.error(" Event RefId= " + d.getEventRefId());
		}
		System.err.println(delegates.size());
	}

	// @Test
	public void testSearchByQrCode() {
		List<DelegateDto> delegates = bookingsHelper.getDelegateByQrCode("", "UJDQSrOzKaplbgfY", 0, 10,
				"eqreqreididmqkerm");
		for (DelegateDto d : delegates) {
			logger.error(" Delegate Ref Id = " + d.getRefId());
		}
		System.err.println("found this size::::" + delegates.size());
	}

	@Ignore
	public void testSearchCount() {
		System.err.println(bookingsHelper.getDelegatesCount("Jx4Ca6HpOutf2ic7", "ki", "", ""));
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

	// @Test
	public void testUpdate() {
		Delegate delInDb = eventDao.findByRefId("PBm588rZRt5eaShX", Delegate.class);
		DelegateDto delegate = delInDb.toDto();
		delegate.setClearanceNo("CLR-1");
		delegate.setLpoNo("LPO-1");
		delegate.setReceiptNo("RCPT-1");
		delegate.setIsCredit(1);
		delegate.setBookingPaymentStatus(PaymentStatus.PAID);
		bookingsHelper.updateDelegate(delegate.getRefId(), delegate);
	}

	// @Test
	public void testDbBackUp() {
		bookingsHelper.Backupdbtosql();
	}

	// @Test
	public void testAssociateMembers() {
		String memberNo = "ASSOC/7087";
		if (memberNo.contains("ASSOC/")) {
			System.err.println("Associate!");
		} else {
			System.err.println("Full Member!");
		}
	}

	// @Test
	public void testSyncWithServer() {
		bookingsHelper.syncWithServer("dQdcmmGqPuw7wVzr");
	}

	// @Test
	public void removeDoubleBookings() {
		bookingsHelper.correctDoubleBookings("IWqduDqhOKXb9nxq");
	}

	// @Test
	public void testUpdateStats() {
		List<EventDto> allEvents = eventDaoHelper.getAllEvents("", 0, 10000);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		for (EventDto e : allEvents) {
			try {
				if (new Date().getTime() < (df.parse(e.getEndDate())).getTime()) {
					// System.err.println(e.getName() + "::Updated::"
					// + e.getEndDate());
					bookingsHelper.updateBookingStats(e.getRefId());
				}
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		// bookingsHelper.updatePaymentStats("IWqduDqhOKXb9nxq");
	}

	// @Test
	public void testGetBookingStats() {
		bookingDao.getBookingSummary("IWqduDqhOKXb9nxq");
	}

	// @Test
	public void testDateConversion() {
		MpesaDTO mpesaTrx = new MpesaDTO();
		mpesaTrx.setTransTime("20160510224710");
		String tstamp = mpesaTrx.getTransTime().substring(0, 4) + "-" + mpesaTrx.getTransTime().substring(4, 6) + "-"
				+ mpesaTrx.getTransTime().substring(6, 8) + " " + mpesaTrx.getTransTime().substring(8, 10) + ":"
				+ mpesaTrx.getTransTime().substring(10, 12) + ":" + mpesaTrx.getTransTime().substring(12, 14);

		System.err.println(">>>" + tstamp);

	}

	// @Test
	public void testSMSSending() {
		smsIntergration.send("0704489471", "Test SMS.");
	}
}
