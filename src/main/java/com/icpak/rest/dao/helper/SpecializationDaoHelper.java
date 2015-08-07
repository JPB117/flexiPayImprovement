package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationFormSpecialization;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.EduType;

@Transactional
public class SpecializationDaoHelper {

	@Inject UsersDao userDao;
	@Inject ApplicationFormDao dao;
	
	public List<ApplicationFormSpecializationDto> getAllSpecializationEntrys(String uriInfo, 
			String applicationId,Integer offset,	Integer limit) {

		Collection<ApplicationFormSpecialization> list = dao.getSpecialization(applicationId);
		
		List<ApplicationFormSpecializationDto> dtos = new ArrayList<>();
		for(ApplicationFormSpecialization e: list){
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public ApplicationFormSpecializationDto getSpecializationEntryById(String applicationId, String eduEntryId) {

		ApplicationFormSpecialization eduEntry = dao.findByRefId(eduEntryId, ApplicationFormSpecialization.class);
		return eduEntry.toDto();
	}
	
	public ApplicationFormSpecializationDto createSpecializationEntry(String applicationId, 
			ApplicationFormSpecializationDto dto) {
		ApplicationFormHeader application = dao.findByApplicationId(applicationId);
		
		ApplicationFormSpecialization edu = new ApplicationFormSpecialization();
		edu.setApplicationRefId(application.getRefId());
		edu.copyFrom(dto);
		
		dao.save(edu);		
		
		return edu.toDto();
	}

	public ApplicationFormSpecializationDto updateSpecializationEntry(String applicationId,String eduEntryId,
			ApplicationFormSpecializationDto eduEntry) {
		assert eduEntry.getRefId()!=null;
		
		ApplicationFormSpecialization poSpecializationEntry = dao.findByRefId(eduEntryId, ApplicationFormSpecialization.class);
		poSpecializationEntry.copyFrom(eduEntry);
		dao.save(poSpecializationEntry);
		return poSpecializationEntry.toDto();
	}

	public void deleteSpecializationEntry(String applicationId, String eduEntryId) {
		ApplicationFormSpecialization eduEntry = dao.findByRefId(eduEntryId, ApplicationFormSpecialization.class);
		dao.delete(eduEntry);
	}

	public List<ApplicationFormSpecializationDto> getAllSpecializationEntrys(String uriInfo,
			String applicationId, EduType academia, Integer offset,
			Integer limit) {
		return new ArrayList<>();
	}

}
