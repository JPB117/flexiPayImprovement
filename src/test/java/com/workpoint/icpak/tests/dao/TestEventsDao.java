package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
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

	@Ignore
	public void testCrud() {
		createEvent();
	}

	@Ignore
	public void createEvent() {
		EventDto event = new EventDto();
		event.setName("The Financial Reporting Workshop: Mt. Kenya Branch, Nyeri");
		event.setDescription("");
		event.setStartDate("2015-05-30 08:00:00");
		event.setEndDate("2015-06-04 08:00:00");
		event.setNonMemberPrice(30000.00);
		event.setMemberPrice(20000.00);
		event.setVenue("GreenHills Hotel, Nyeri");
		event.setType(EventType.EVENT);
		event.setCategoryName("Conferences");
		event.setCpdHours(14);
		helper.createEvent(event);
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
	
	@Test
	public void testSerch(){
		List<EventDto> searchResults = helper.getAllEvents("", null, 100, null, "F");
		for(EventDto e : searchResults){
			System.err.println("Event Size>>>" + e.getCategoryName());
		}
	}

}
