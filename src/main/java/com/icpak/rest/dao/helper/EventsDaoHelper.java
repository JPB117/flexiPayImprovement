package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.event.Event;

@Transactional
public class EventsDaoHelper {

	@Inject EventsDao dao;
	
	public ResourceCollectionModel<Event> getAllEvents(UriInfo uriInfo, Integer offset,
			Integer limit) {

		ResourceCollectionModel<Event> events = new ResourceCollectionModel<>(offset,limit, dao.getEventCount(),uriInfo);
		
		List<Event> list = dao.getAllEvents(offset, limit);
		List<Event> eventsList = new ArrayList<>();
		
		for(Event e : list){
			Event event = e.clone();
			e.setUri(uriInfo.getAbsolutePath().toString()+"/"+event.getRefId());
			eventsList.add(e);
		}
		events.setItems(eventsList);
		
		return events;
	}

	public Event getEventById(String eventId) {

		Event event = dao.getByEventId(eventId);
		return event;
	}
	
	public void createEvent(Event event) {
		
		assert event.getRefId()==null;
		event.setRefId(IDUtils.generateId());
		dao.save(event);
		
		assert event.getId()!=null;
	}

	public void updateEvent(String eventId, Event event) {
		assert event.getRefId()!=null;
		
		Event poEvent = getEventById(eventId);
		poEvent.setDescription(event.getDescription());
		poEvent.setName(event.getName());
		poEvent.setRefId(event.getRefId());
		dao.save(poEvent);
	}

	public void deleteEvent(String eventId) {
		Event event = getEventById(eventId);
		dao.delete(event);
	}
}
