package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.models.event.Accommodation;
import com.icpak.rest.models.event.Event;
import com.workpoint.icpak.shared.model.events.AccommodationDto;

@Transactional
public class AccommodationsDaoHelper {

	@Inject
	EventsDao dao;

	public List<AccommodationDto> getAllAccommodations(String eventId,
			Integer offset, Integer limit) {

		Collection<Accommodation> accommodations = null;
		if (eventId.equals("ALL")) {
			accommodations = dao.getAllAccommodations();
		} else {
			accommodations = dao.getAllAccommodations(eventId);
		}

		List<AccommodationDto> dtos = new ArrayList<>();
		for (Accommodation a : accommodations) {
			AccommodationDto dto = a.toDto(true);
			dto.setTotalBooking(dao.getAccommodationBookingCount(a.getRefId()));
			dtos.add(dto);
		}

		return dtos;
	}

	public AccommodationDto getAccommodation(String eventId,
			String accommodationId) {

		Event event = dao.findByRefId(eventId, Event.class);
		Accommodation a = dao.getAccommodation(event, accommodationId);
		return a.toDto(true);
	}

	public AccommodationDto create(String eventId,
			AccommodationDto accommodation) {
		Event event = dao.findByRefId(eventId, Event.class);
		Accommodation a = new Accommodation();
		a.copyFrom(accommodation);
		a.setEvent(event);
		dao.save(a);

		return a.toDto(true);
	}

	public AccommodationDto update(String eventId, String accommodationId,
			AccommodationDto accommodation) {

		Accommodation a = dao.findByRefId(accommodationId, Accommodation.class);
		a.copyFrom(accommodation);
		dao.save(a);
		return a.toDto(true);
	}

	public void delete(String eventId, String accommodationId) {
		Accommodation a = dao.findByRefId(accommodationId, Accommodation.class);
		dao.delete(a);
	}

}
