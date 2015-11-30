package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.models.event.Event;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.CourseDto;

@Transactional
public class CoursesDaoHelper {
	Logger logger = Logger.getLogger(CoursesDaoHelper.class);

	@Inject EventsDao dao;
	
	public List<CourseDto> getAllEvents(String uri, Integer offset,
			Integer limit){
		return getAllEvents(uri, offset, limit, null);
	}
			
	public List<CourseDto> getAllEvents(String uri, Integer offset,
			Integer limit, String eventType) {

		EventType type = null;
		if(eventType!=null){
			type = EventType.valueOf(eventType);
		}
		
		List<Event> list = dao.getAllEvents(offset, limit, type, eventType);
		List<CourseDto> eventsList = new ArrayList<>();
		
		for(Event e : list){
			CourseDto event = e.toCourseDto();
			//int[] counts = dao.getEventCounts();
//			event.setDelegateCount(dao.getDelegateCount(e.getRefId()));
//			event.setTotalPaid(dao.getTotalEventAmount(e.getRefId(), PaymentStatus.PAID));
//			event.setTotalUnpaid(dao.getTotalEventAmount(e.getRefId(), PaymentStatus.NOTPAID));
			event.setUri(uri+"/"+event.getRefId());
			eventsList.add(event);
		}
		
		return eventsList;
	}

	public CourseDto getEventById(String eventId) {

		Event event = dao.getByEventId(eventId);
		return event.toCourseDto();
	}
	
	public CourseDto createEvent(CourseDto dto) {
		dto.setType(EventType.COURSE);
		assert dto.getRefId()==null;
		
		Event event = new Event();
		event.setRefId(IDUtils.generateId());
		event.copyFromCourse(dto);
		dao.save(event);
		dto.setRefId(event.getRefId());
		assert event.getId()!=null;
		
		return event.toCourseDto();
	}

	public void updateEvent(String eventId, CourseDto dto) {
		dto.setType(EventType.COURSE);
		assert dto.getRefId()!=null;
		Event poEvent = dao.getByEventId(eventId);
		poEvent.copyFromCourse(dto);
		dao.save(poEvent);
	}

	public void deleteEvent(String eventId) {
		Event event = dao.getByEventId(eventId);
		dao.delete(event);
	}
}
