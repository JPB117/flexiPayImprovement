package com.workpoint.icpak.tests.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.dialect.MySQL5Dialect;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.EventsDaoHelper;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.EventDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestEventsDao extends AbstractDaoTest {

	@Inject
	EventsDaoHelper helper;

	String eventId1;
	String eventId2;

	@Test
	public void testCrud() {
		 createEvent();

		//retrieveEvents();
		// getById();
		// delete();
		// retrieveEventsAfterDelete();
	}

	public void createEvent() {
		EventDto event = new EventDto();
		event.setName("jua kali Show");
		event.setDescription("Some Show");
		event.setStartDate(new Date().getTime());
		event.setEndDate(new Date().getTime());
		event.setNonMemberPrice(30000.00);
		event.setMemberPrice(20000.00);
		event.setVenue("Laikipia Board Room");
		event.setType(EventType.COURSE);
		event.setCategoryName("Conferences");
		event.setCpdHours(10);
		helper.createEvent(event);
		eventId1 = event.getRefId();

		event = new EventDto();
		event.setName("Mama show");
		event.setDescription("Testing");
		event.setStartDate(new Date().getTime());
		event.setEndDate(new Date().getTime());
		event.setNonMemberPrice(30000.00);
		event.setMemberPrice(20000.00);
		event.setType(EventType.EVENT);
		event.setVenue("Strath Board Room");
		event.setCategoryName("Abroad");
		event.setCpdHours(5);
		helper.createEvent(event);
		eventId2 = event.getRefId();
		
	}

	public void retrieveEvents() {
		List<EventDto> events = helper.getAllEvents("", 0, 10);

		System.err.println("Event Size>>>" + events.size());

		// Assert.assertSame(events.size(), 2);
	}

	public void getById() {
		EventDto event = helper.getEventById(eventId1);
		Assert.assertNotNull(event);

		event = helper.getEventById(eventId2);
		Assert.assertNotNull(event);
	}

	public void delete() {
		helper.deleteEvent(eventId1);
		helper.deleteEvent(eventId2);
	}

	public void retrieveEventsAfterDelete() {
		List<EventDto> events = helper.getAllEvents("", 0, 10);
		Assert.assertSame(events.size(), 0);
	}

}
