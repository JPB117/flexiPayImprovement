package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONObject;
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
	Logger logger = Logger.getLogger(TestEventsDao.class);

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

	@Test
	public void createEvent() {
		EventDto event = new EventDto();
		event.setName("ICPAK NAIROBI TRAINING ON ICT");
		event.setDescription("");
		event.setStartDate("2015-12-18");
		event.setEndDate("2015-12-18");
		event.setNonMemberPrice(23750.00);
		event.setDiscountNonMemberPrice(21750.00);
		event.setPenaltyNonMemberPrice(24750.00);
		event.setMemberPrice(20000.00);
		event.setDiscountMemberPrice(18000.00);
		event.setPenaltyMemberPrice(21000.00);
		event.setAssociatePrice(15000.00);
		event.setDiscountAssociatePrice(13000.00);
		event.setPenaltyAssociatePrice(16000.00);
		event.setDiscountDate("2015-12-09");
		event.setPenaltyDate("2015-12-16");
		event.setVenue("Acacia Premier Hotel");
		event.setType(EventType.EVENT);
		event.setCategoryName("Conferences");
		event.setCpdHours(new Double(2.5));
		helper.createEvent(event);
		// helper.getAllEvents(null, 0, 10);
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
		course.setCpdHours(new Double(14));
		courseHelper.createEvent(course);
		eventId1 = course.getRefId();
		System.out.println("Event Id::" + eventId1);

	}

	@Ignore
	public void testSerch() {
		System.err.println(helper.getEventDelegatesReport("QEyf2DasD3X7Pybt")
				.size());
	}

	@Ignore
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

	@Ignore
	public void testFindbyLongId() {
		CourseDto courseDto = courseHelper.getCourseByLongId(22);

		JSONObject json = new JSONObject(courseDto);
		logger.info(" +++ Result ++ " + json.toString());
	}

	@Ignore
	public void testUpdate() {
		CourseDto courseDto = new CourseDto();
		courseDto.setStartDate("2015-4-20");
		courseDto.setEndDate("2015-8-20");
		courseDto.setCode("8767");
		courseDto.setDescription("Description");

		CourseDto updatedDto = courseHelper.updateEvent("QEyf2DasD3X7Pybt",
				courseDto);

		JSONObject json = new JSONObject(updatedDto);
		logger.info(" +++ Result ++ " + json.toString());
	}

}
