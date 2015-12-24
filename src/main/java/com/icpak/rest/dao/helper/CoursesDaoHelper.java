package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.events.CourseDto;

@Transactional
public class CoursesDaoHelper {
	Logger logger = Logger.getLogger(CoursesDaoHelper.class);

	@Inject
	EventsDao dao;
	@Inject
	MemberDao memberDao;
	@Inject
	CPDDaoHelper cpdDaoHelper;

	public List<CourseDto> getAllEvents(String uri, Integer offset,
			Integer limit) {
		return getAllEvents(uri, offset, limit, null);
	}

	public List<CourseDto> getAllEvents(String uri, Integer offset,
			Integer limit, String eventType) {
		logger.info(" +++ Fetching courses ++++ ");

		EventType type = null;
		if (eventType != null) {
			type = EventType.valueOf(eventType);
		}

		List<Event> list = dao.getAllEvents(offset, limit, type, eventType);
		List<CourseDto> eventsList = new ArrayList<>();

		for (Event e : list) {
			CourseDto event = e.toCourseDto();
			// int[] counts = dao.getEventCounts();
			// event.setDelegateCount(dao.getDelegateCount(e.getRefId()));
			// event.setTotalPaid(dao.getTotalEventAmount(e.getRefId(),
			// PaymentStatus.PAID));
			// event.setTotalUnpaid(dao.getTotalEventAmount(e.getRefId(),
			// PaymentStatus.NOTPAID));
			event.setUri(uri + "/" + event.getRefId());
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
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.error(mapper.writeValueAsString(dto));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assert dto.getRefId() == null;
		Event event = new Event();
		event.setRefId(IDUtils.generateId());
		event.copyFromCourse(dto);
		dao.save(event);
		dto.setRefId(event.getRefId());
		assert event.getId() != null;
		return event.toCourseDto();
	}

	public CourseDto updateEvent(String eventId, CourseDto dto) {
		logger.info(" ++ Updating Course ++ ");
		dto.setType(EventType.COURSE);
		assert dto.getRefId() != null;
		Event poEvent = dao.getByEventId(eventId);
		poEvent.copyFromCourse(dto);
		dao.save(poEvent);

		Event updateEvent = dao.getByEventId(eventId);

		return updateEvent.toCourseDto();
	}

	public void deleteEvent(String eventId) {
		Event event = dao.getByEventId(eventId);
		dao.delete(event);
	}

	public String updateCPDCOurse(Integer lmsCourseId, String memberNo) {
		logger.info("++++ Updating course from LMS ++++");
		String result = null;

		Event eventInDb = dao.getByEventLongId(lmsCourseId);

		Member memberInDb = memberDao.getByMemberNo(memberNo);

		if (eventInDb == null) {
			result = "That course was not found in the portal db";
		}

		if (memberInDb == null) {
			if (eventInDb == null) {
				result = result + "and no member with that member number";
			} else {
				result = "There is no member registered with that number";
			}
		}

		if (eventInDb != null && memberInDb != null) {

			CPDDto cpdDto = new CPDDto();
			cpdDto.setCategory(CPDCategory.CATEGORY_A);
			cpdDto.setEndDate(eventInDb.getEndDate());
			cpdDto.setStartDate(eventInDb.getStartDate());
			cpdDto.setCpdHours(eventInDb.getCpdHours());
			cpdDto.setStatus(CPDStatus.Approved);
			cpdDto.setMemberRefId(memberInDb.getRefId());
			cpdDto.setMemberRegistrationNo(memberInDb.getMemberNo());
			cpdDto.setEventId(eventInDb.getRefId());
			cpdDto.setTitle(eventInDb.getName());
			cpdDto.setEventLocation(eventInDb.getVenue());

			cpdDaoHelper.create(memberInDb.getRefId(), cpdDto);

			result = "Success";
		} else {
			result = "Failed !!: " + result;
		}

		return result;
	}

	public CourseDto getCourseByLongId(Integer id) {
		Event event = dao.getByEventLongId(id);
		return event.toCourseDto();
	}
}
