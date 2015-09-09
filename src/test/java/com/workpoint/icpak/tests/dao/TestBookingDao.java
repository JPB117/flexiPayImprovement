package com.workpoint.icpak.tests.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
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

	@Inject
	BookingsDaoHelper bookingsHelper;
	@Inject
	CPDDaoHelper daoHelper;
	@Inject
	EventsDao eventDao;

	@Test
	public void getTrx(){
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
		delegate.setEmail("kim@wira.io");
		delegate.setOtherNames("Kimani");
		delegate.setSurname("Duggan");
		delegate.setMemberId("10000");
		//delegate.setMemberRefId("LCJ4fe1eoOdxwq69");
		delegates.add(delegate);
		dto.setDelegates(delegates);

		// dto.setCurrency(currency);
		bookingsHelper.createBooking("Jx4Ca6HpOutf2ic7", dto);

	}
}
