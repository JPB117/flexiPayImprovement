package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.models.event.Event;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.EventDto;

@Transactional
public class EventsDaoHelper {

	@Inject
	EventsDao dao;

	Logger logger = Logger.getLogger(EventsDaoHelper.class);

	public List<EventDto> getAllEvents(String uri, Integer offset, Integer limit) {
		return getAllEvents(uri, offset, limit, null);
	}

	public List<EventDto> getAllEvents(String uri, Integer offset, Integer limit, String eventType) {
		return getAllEvents(uri, offset, limit, eventType, "");
	}

	public List<EventDto> getAllEvents(String uri, Integer offset, Integer limit, String eventType, String searchTerm) {
		EventType type = null;
		List<EventDto> eventsList = new ArrayList<>();

		try {
			if (eventType != null) {
				type = EventType.valueOf(eventType.toUpperCase());
			}
			List<Event> list = dao.getAllEvents(offset, limit, type, searchTerm);

			for (Event e : list) {
				EventDto event = e.toDto();
				// int[] counts = dao.getEventCounts();
				event.setDelegateCount(dao.getDelegateCount(e.getRefId()));
				// event.setPaidCount(dao.getDelegatePaidCount(e.getId()));
				// event.setUnPaidCount(dao.getDelegateUnPaidCount(e.getId()));
				// event.setTotalPaid(dao.getTotalEventAmount(e.getRefId(),
				// PaymentStatus.PAID));
				// event.setTotalUnpaid(dao.getTotalEventAmount(e.getRefId(),
				// PaymentStatus.NOTPAID));
				event.setUri(uri + "/" + event.getRefId());
				eventsList.add(event);
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

		return eventsList;
	}

	public Integer getCount() {
		return dao.getEventCount();
	}

	public EventDto getEventById(String eventId) {
		Event event = dao.getByEventId(eventId);
		EventDto eventDto = event.toDto();
		Date today = new Date();
		if (today.after(event.getStartDate()) || event.getStatus() == EventStatus.CLOSED) {
			logger.info(event.getName() + " is a past event. Booking period has ended!");
			eventDto.setIsEventActive(0);
		} else {
			logger.info(event.getName() + " is within timeline. Booking still ongoing!");
			eventDto.setIsEventActive(1);
		}
		return eventDto;
	}

	public EventDto createEvent(EventDto dto) {
		assert dto.getRefId() == null;
		Event event = new Event();
		event.setRefId(IDUtils.generateId());
		event.copyFrom(dto);
		dao.save(event);
		dto.setRefId(event.getRefId());
		assert event.getId() != null;
		return event.toDto();
	}

	public void updateEvent(String eventId, EventDto dto) {
		assert dto.getRefId() != null;
		Event poEvent = dao.getByEventId(eventId);
		poEvent.copyFrom(dto);
		dao.save(poEvent);
	}

	public void deleteEvent(String eventId) {
		Event event = dao.getByEventId(eventId);
		dao.delete(event);
	}

	public Integer getCount(String searchTerm) {
		return dao.getSearchEventCount(searchTerm);
	}

	public List<DelegateDto> getEventDelegatesReport(String eventRefId) {
		return dao.getEventDelegates(eventRefId);
	}
}
