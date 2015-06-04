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
import com.icpak.rest.models.membership.Specialization;
import com.icpak.rest.models.membership.Member;

@Transactional
public class SpecializationDaoHelper {

	@Inject UsersDao userDao;
	@Inject MemberDao dao;
	
	public ResourceCollectionModel<Specialization> getAllSpecializations(UriInfo uriInfo, 
			String memberId,Integer offset,	Integer limit) {

		Member member = dao.findByMemberId(memberId);
		int size = member.getSpecializations().size();
		ResourceCollectionModel<Specialization> educationEntries = new ResourceCollectionModel<>(offset,limit,
				size,uriInfo);
		Collection<Specialization> list = member.getSpecializations();
		
		List<Specialization> clones = new ArrayList<>();
		for(Specialization offenseEntry: list){
			Specialization clone = offenseEntry.clone();
			clone.setUri(uriInfo.getAbsolutePath()+"/"+clone.getRefId());
			clone.setMemberId(memberId);
			clone.setSpecialization(offenseEntry.getSpecialization());
			if(clone.getSpecialization()==null){
				clone.setSpecialization("No Entry");
			}
			clones.add(clone);
		}
		
		educationEntries.setItems(clones);
		return educationEntries;
	}

	public Specialization getSpecializationById(String memberId,String offenseEntryId) {

		Specialization offenseEntry = dao.findByRefId(offenseEntryId, Specialization.class);
		return offenseEntry.clone();
	}
	
	public Specialization createSpecialization(String memberId,Specialization offenseEntry) {
		assert offenseEntry.getRefId()==null;
		Member member = dao.findByMemberId(memberId);
		offenseEntry.setMember(member);
		dao.save(offenseEntry);		
		
		return offenseEntry.clone();
	}

	public Specialization updateSpecialization(String memberId,String offenseEntryId, Specialization offenseEntry) {
		assert offenseEntry.getRefId()!=null;
		
		Specialization poSpecialization = dao.findByRefId(offenseEntryId, Specialization.class);
		poSpecialization.copyFrom(offenseEntry);
		dao.save(offenseEntry);
		return poSpecialization.clone();
	}

	public void deleteSpecialization(String memberId, String offenseEntryId) {
		Specialization offenseEntry = dao.findByRefId(offenseEntryId, Specialization.class);
		dao.delete(offenseEntry);
	}

}
