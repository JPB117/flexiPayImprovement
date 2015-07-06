package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.EduType;

@Transactional
public class EducationDaoHelper {

	@Inject UsersDao userDao;
	@Inject ApplicationFormDao dao;
	
	public List<ApplicationFormEducationalDto> getAllEducationEntrys(String uriInfo, 
			String applicationId,Integer offset,	Integer limit) {

		Collection<ApplicationFormEducational> list = dao.getEducation(applicationId);
		
		List<ApplicationFormEducationalDto> dtos = new ArrayList<>();
		for(ApplicationFormEducational e: list){
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public ApplicationFormEducationalDto getEducationEntryById(String applicationId, String eduEntryId) {

		ApplicationFormEducational eduEntry = dao.findByRefId(eduEntryId, ApplicationFormEducational.class);
		return eduEntry.toDto();
	}
	
	public ApplicationFormEducationalDto createEducationEntry(String applicationId, 
			ApplicationFormEducationalDto dto) {
		ApplicationFormHeader application = dao.findByApplicationId(applicationId);
		
		ApplicationFormEducational edu = new ApplicationFormEducational();
		edu.setApplicationRefId(application.getRefId());
		edu.copyFrom(dto);
		
		dao.save(edu);		
		
		return edu.toDto();
	}

	public ApplicationFormEducationalDto updateEducationEntry(String applicationId,String eduEntryId,
			ApplicationFormEducationalDto eduEntry) {
		assert eduEntry.getRefId()!=null;
		
		ApplicationFormEducational poEducationEntry = dao.findByRefId(eduEntryId, ApplicationFormEducational.class);
		poEducationEntry.copyFrom(eduEntry);
		dao.save(poEducationEntry);
		return poEducationEntry.toDto();
	}

	public void deleteEducationEntry(String applicationId, String eduEntryId) {
		ApplicationFormEducational eduEntry = dao.findByRefId(eduEntryId, ApplicationFormEducational.class);
		dao.delete(eduEntry);
	}

	public List<ApplicationFormEducationalDto> getAllEducationEntrys(String uriInfo,
			String applicationId, EduType academia, Integer offset,
			Integer limit) {
		return new ArrayList<>();
	}

}
