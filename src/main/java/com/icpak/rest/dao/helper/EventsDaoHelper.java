package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.models.event.Event;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.EventDto;

@Transactional
public class EventsDaoHelper {

	@Inject
	EventsDao dao;

	public List<EventDto> getAllEvents(String uri, Integer offset, Integer limit) {
		return getAllEvents(uri, offset, limit, null);
	}

	public List<EventDto> getAllEvents(String uri, Integer offset, Integer limit, String eventType) {
		return getAllEvents(uri, offset, limit, eventType, "");
	}

	public List<EventDto> getAllEvents(String uri, Integer offset, Integer limit, String eventType, String searchTerm) {
		EventType type = null;
		if (eventType != null) {
			type = EventType.valueOf(eventType);
		}
		List<Event> list = dao.getAllEvents(offset, limit, type, searchTerm);
		List<EventDto> eventsList = new ArrayList<>();

		for (Event e : list) {
			EventDto event = e.toDto();
			// int[] counts = dao.getEventCounts();
			event.setDelegateCount(dao.getDelegateCount(e.getRefId()));
			event.setTotalPaid(dao.getTotalEventAmount(e.getRefId(), PaymentStatus.PAID));
			event.setTotalUnpaid(dao.getTotalEventAmount(e.getRefId(), PaymentStatus.NOTPAID));
			event.setUri(uri + "/" + event.getRefId());
			eventsList.add(event);
		}

		return eventsList;
	}

	public Integer getCount() {
		return dao.getEventCount();
	}

	public EventDto getEventById(String eventId) {

		Event event = dao.getByEventId(eventId);
		return event.toDto();
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

}
