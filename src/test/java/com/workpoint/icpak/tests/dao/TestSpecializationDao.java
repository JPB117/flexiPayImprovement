package com.workpoint.icpak.tests.dao;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.SpecializationDaoHelper;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.SpecializationCategory;
import com.workpoint.icpak.shared.model.Specializations;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestSpecializationDao extends AbstractDaoTest{

	@Inject SpecializationDaoHelper helper;
	
	@Test
	public void create(){
		ApplicationFormSpecializationDto specializationDto = new ApplicationFormSpecializationDto();
		specializationDto.setSpecialization(Specializations.AUDIT);
		String applicationId="fb0LdmyIyQ574RCI";
		ApplicationFormSpecializationDto dto = helper.createSpecializationEntry(applicationId, specializationDto);
		System.err.println(dto.getRefId());
		
	}
}
