package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.TrainingAndExperience;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.TrainingExperienceType;

@Transactional
public class TrainingAndExperienceDaoHelper {

	@Inject UsersDao userDao;
	@Inject MemberDao dao;
	
	public ResourceCollectionModel<TrainingAndExperience> getAllTrainingEntrys(UriInfo uriInfo, 
			String memberId,Integer offset,	Integer limit) {

		Member member = dao.findByMemberId(memberId);
		int size = member.getTrainingAndExperience().size();
		ResourceCollectionModel<TrainingAndExperience> educationEntries = new ResourceCollectionModel<>(offset,limit,
				size,uriInfo);
		Collection<TrainingAndExperience> list = member.getTrainingAndExperience();
		
		List<TrainingAndExperience> clones = new ArrayList<>();
		for(TrainingAndExperience eduEntry: list){
			TrainingAndExperience clone = eduEntry.clone();
			clone.setUri(uriInfo.getAbsolutePath()+"/"+clone.getRefId());
			clone.setMemberId(memberId);
			clones.add(clone);
		}
		
		educationEntries.setItems(clones);
		return educationEntries;
	}
	
	public ResourceCollectionModel<TrainingAndExperience> getAllTrainingEntrys(UriInfo uriInfo, 
			String memberId,TrainingExperienceType type,Integer offset,	Integer limit) {
		
		Member member = dao.findByMemberId(memberId);
		int size = dao.getTrainingAndExpCount(member, type);
		Collection<TrainingAndExperience> list = dao.getTrainingAndExp(member, type);
		
		ResourceCollectionModel<TrainingAndExperience> educationEntries = new ResourceCollectionModel<>(offset,limit,
				size,uriInfo);
		
		List<TrainingAndExperience> clones = new ArrayList<>();
		for(TrainingAndExperience eduEntry: list){
			TrainingAndExperience clone = eduEntry.clone();
			clone.setUri(uriInfo.getAbsolutePath()+"/"+clone.getRefId());
			clone.setMemberId(memberId);
			clones.add(clone);
		}
		
		educationEntries.setItems(clones);
		return educationEntries;
	}


	public TrainingAndExperience getTrainingEntryById(String memberId,String eduEntryId) {

		TrainingAndExperience eduEntry = dao.findByRefId(eduEntryId, TrainingAndExperience.class);
		return eduEntry.clone();
	}
	
	public TrainingAndExperience createTrainingEntry(String memberId,TrainingAndExperience eduEntry) {
		assert eduEntry.getRefId()==null;
		Member member = dao.findByMemberId(memberId);
		eduEntry.setMember(member);
		dao.save(eduEntry);		
		
		return eduEntry.clone();
	}

	public TrainingAndExperience updateTrainingEntry(String memberId,String eduEntryId, TrainingAndExperience eduEntry) {
		assert eduEntry.getRefId()!=null;
		
		TrainingAndExperience poTrainingEntry = dao.findByRefId(eduEntryId, TrainingAndExperience.class);
		poTrainingEntry.copyFrom(eduEntry);
		
		dao.save(eduEntry);
		return poTrainingEntry.clone();
	}

	public void deleteTrainingEntry(String memberId, String eduEntryId) {
		TrainingAndExperience eduEntry = dao.findByRefId(eduEntryId, TrainingAndExperience.class);
		dao.delete(eduEntry);
	}

}
