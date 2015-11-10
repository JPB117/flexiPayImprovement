package com.workpoint.icpak.tests.dao;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.AccommodationsDaoHelper;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestAccomodation extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestAccomodation.class);

	@Inject
	AccommodationsDaoHelper accommodationsDaoHelper;

	@Test
	public void testCreateAccomodation() {
		String eventId = "Jx4Ca6HpOutf2ic7";

		AccommodationDto accommodationDto = new AccommodationDto();
		accommodationDto.setHotel("Nyali");
		accommodationDto.setDescription("Nice place to stay");
		accommodationDto.setFee(new Double(25000));
		accommodationDto.setNights(8);
		accommodationDto.setSpaces(8);

		AccommodationDto savedAccomodationDto = accommodationsDaoHelper.create(
				eventId, accommodationDto);
		logger.debug(" ==== >><<<<<< ====" + savedAccomodationDto.getName());

	}

	public void testImportAccomodation() {

	}
}
