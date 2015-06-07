package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.EduType;

@Transactional
public class EducationDaoHelper {

	@Inject UsersDao userDao;
	@Inject ApplicationFormDao dao;
	
	public ResourceCollectionModel<ApplicationFormEducational> getAllEducationEntrys(UriInfo uriInfo, 
			String applicationId,Integer offset,	Integer limit) {

		ApplicationFormHeader application = dao.findByApplicationId(applicationId);
		int size = dao.getEducationCount(applicationId);
		ResourceCollectionModel<ApplicationFormEducational> educationEntries = new ResourceCollectionModel<>(offset,limit,
				size,uriInfo);
		Collection<ApplicationFormEducational> list = dao.getEducation(applicationId);
		
		List<ApplicationFormEducational> clones = new ArrayList<>();
		for(ApplicationFormEducational eduEntry: list){
			clones.add(eduEntry);
//			ApplicationFormEducational clone = eduEntry.clone();
//			clone.setUri(uriInfo.getAbsolutePath()+"/"+clone.getRefId());
//			clone.setMemberId(applicationId);
//			clones.add(clone);
		}
		
		educationEntries.setItems(clones);
		return educationEntries;
	}
	
	public ResourceCollectionModel<ApplicationFormEducational> getAllEducationEntrys(UriInfo uriInfo, 
			String applicationId,EduType type, Integer offset,	Integer limit) {
		ApplicationFormHeader application = dao.findByApplicationId(applicationId);
		int size =dao.getEducationCount(applicationId, type);
		Collection<ApplicationFormEducational> list = dao.getEducation(applicationId, type);
		
		ResourceCollectionModel<ApplicationFormEducational> educationEntries = new ResourceCollectionModel<>(offset,limit,
				size,uriInfo);

		List<ApplicationFormEducational> clones = new ArrayList<>();
		for(ApplicationFormEducational eduEntry: list){
//			ApplicationFormEducational clone = eduEntry.clone();
//			clone.setUri(uriInfo.getAbsolutePath()+"/"+clone.getRefId());
//			clone.setMemberId(applicationId);
//			clones.add(clone);
			clones.add(eduEntry);
		}
		
		educationEntries.setItems(clones);
		return educationEntries;
	}

	public ApplicationFormEducational getEducationEntryById(String applicationId, String eduEntryId) {

		ApplicationFormEducational eduEntry = dao.findByRefId(eduEntryId, ApplicationFormEducational.class);
		return eduEntry;
	}
	
	public ApplicationFormEducational createEducationEntry(String applicationId, ApplicationFormEducational eduEntry) {
		assert eduEntry.getRefId()==null;
		ApplicationFormHeader application = dao.findByApplicationId(applicationId);
		//eduEntry.setApplication(application);
		eduEntry.setApplicationRefId(applicationId);
		dao.save(eduEntry);		
		
		return eduEntry;
	}

	public ApplicationFormEducational updateEducationEntry(String applicationId,String eduEntryId, ApplicationFormEducational eduEntry) {
		assert eduEntry.getRefId()!=null;
		
		ApplicationFormEducational poEducationEntry = dao.findByRefId(eduEntryId, ApplicationFormEducational.class);
		poEducationEntry.copyFrom(eduEntry);
		
		dao.save(eduEntry);
		return poEducationEntry;
	}

	public void deleteEducationEntry(String applicationId, String eduEntryId) {
		ApplicationFormEducational eduEntry = dao.findByRefId(eduEntryId, ApplicationFormEducational.class);
		dao.delete(eduEntry);
	}

}
