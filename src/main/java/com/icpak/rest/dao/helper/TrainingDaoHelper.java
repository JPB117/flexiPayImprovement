package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationFormTraining;
import com.workpoint.icpak.shared.model.ApplicationFormTrainingDto;
import com.workpoint.icpak.shared.model.EduType;

@Transactional
public class TrainingDaoHelper {

	@Inject UsersDao userDao;
	@Inject ApplicationFormDao dao;
	
	public List<ApplicationFormTrainingDto> getAllTrainingEntrys(String uriInfo, 
			String applicationId,Integer offset,	Integer limit) {

		Collection<ApplicationFormTraining> list = dao.getTraining(applicationId);
		
		List<ApplicationFormTrainingDto> dtos = new ArrayList<>();
		for(ApplicationFormTraining e: list){
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public ApplicationFormTrainingDto getTrainingEntryById(String applicationId, String eduEntryId) {

		ApplicationFormTraining eduEntry = dao.findByRefId(eduEntryId, ApplicationFormTraining.class);
		return eduEntry.toDto();
	}
	
	public ApplicationFormTrainingDto createTrainingEntry(String applicationId, 
			ApplicationFormTrainingDto dto) {
		ApplicationFormHeader application = dao.findByApplicationId(applicationId);
		
		ApplicationFormTraining edu = new ApplicationFormTraining();
		edu.setApplicationRefId(application.getRefId());
		edu.copyFrom(dto);
		
		dao.save(edu);		
		
		return edu.toDto();
	}

	public ApplicationFormTrainingDto updateTrainingEntry(String applicationId,String eduEntryId,
			ApplicationFormTrainingDto eduEntry) {
		assert eduEntry.getRefId()!=null;
		
		ApplicationFormTraining poTrainingEntry = dao.findByRefId(eduEntryId, ApplicationFormTraining.class);
		poTrainingEntry.copyFrom(eduEntry);
		dao.save(poTrainingEntry);
		return poTrainingEntry.toDto();
	}

	public void deleteTrainingEntry(String applicationId, String eduEntryId) {
		ApplicationFormTraining eduEntry = dao.findByRefId(eduEntryId, ApplicationFormTraining.class);
		dao.delete(eduEntry);
	}

	public List<ApplicationFormTrainingDto> getAllTrainingEntrys(String uriInfo,
			String applicationId, EduType academia, Integer offset,
			Integer limit) {
		return new ArrayList<>();
	}

}
