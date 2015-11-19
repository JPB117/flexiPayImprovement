package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.CoursesDaoHelper;
import com.icpak.rest.dao.helper.EventsDaoHelper;
import com.icpak.servlet.upload.GetDelegatesReport;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.CourseDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestEventsDao extends AbstractDaoTest {

	@Inject
	EventsDaoHelper helper;

	@Inject
	CoursesDaoHelper courseHelper;

	String eventId1;
	String eventId2;

	@Ignore
	public void testCrud() {
		createEvent();
	}

	@Ignore
	public void createEvent() {
		EventDto event = new EventDto();
		event.setName("The Financial Reporting Workshop: Mt. Kenya Branch, Nyeri");
		event.setDescription("");
		event.setStartDate("2015-11-06");
		event.setEndDate("2015-11-06");
		event.setNonMemberPrice(30000.00);
		event.setMemberPrice(20000.00);
		event.setVenue("GreenHills Hotel, Nyeri");
		event.setType(EventType.EVENT);
		event.setCategoryName("Conferences");
		event.setCpdHours(14);
		// helper.createEvent(event);
		helper.getAllEvents(null, 0, 10);
		eventId1 = event.getRefId();

		System.out.println("Event Id::" + eventId1);
	}

	@Ignore
	public void retrieveEvents() {
		List<EventDto> events = helper.getAllEvents("", 0, 10);
		System.err.println("Event Size>>>" + events.size());
	}

	@Ignore
	public void getById() {
		EventDto event = helper.getEventById(eventId1);
		Assert.assertNotNull(event);

		event = helper.getEventById(eventId2);
		Assert.assertNotNull(event);
	}

	@Ignore
	public void delete() {
		helper.deleteEvent(eventId1);
		helper.deleteEvent(eventId2);
	}

	@Ignore
	public void retrieveEventsAfterDelete() {
		List<EventDto> events = helper.getAllEvents("", 0, 10);
		Assert.assertSame(events.size(), 0);
	}

	@Ignore
	public void createCourse() {
		CourseDto course = new CourseDto();
		course.setName("Taxation Course");
		course.setDescription("");
		course.setStartDate("2015-12-01");
		course.setEndDate("2015-12-30");
		course.setNonMemberPrice(30000.00);
		course.setMemberPrice(20000.00);
		course.setVenue("GreenHills Hotel, Nyeri");
		course.setType(EventType.COURSE);
		course.setCpdHours(14);
		courseHelper.createEvent(course);
		eventId1 = course.getRefId();
		System.out.println("Event Id::" + eventId1);

	}

	@Ignore
	public void testSerch() {
		System.err.println(helper.getEventDelegatesReport("QEyf2DasD3X7Pybt")
				.size());
	}

	@Test
	public void testDeleagtesReport() throws Exception {
		String eventRefId = "QEyf2DasD3X7Pybt";
		String docType = "xls";

		List<DelegateDto> delegateDtos = helper
				.getEventDelegatesReport(eventRefId);

		for (DelegateDto dto : delegateDtos) {
			System.err.println("<>><<< ERN NO>>>" + dto.getErn());
		}

		GetDelegatesReport report = new GetDelegatesReport();
		report.generateDelegateReport(delegateDtos, docType);
	}

}
