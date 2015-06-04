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
import com.icpak.rest.models.membership.CriminalOffense;
import com.icpak.rest.models.membership.Member;

@Transactional
public class CriminalOffensesDaoHelper {

	@Inject UsersDao userDao;
	@Inject MemberDao dao;
	
	public ResourceCollectionModel<CriminalOffense> getAllCriminalOffenses(UriInfo uriInfo, 
			String memberId,Integer offset,	Integer limit) {

		Member member = dao.findByMemberId(memberId);
		int size = member.getOffenses().size();
		ResourceCollectionModel<CriminalOffense> educationEntries = new ResourceCollectionModel<>(offset,limit,
				size,uriInfo);
		Collection<CriminalOffense> list = member.getOffenses();
		
		List<CriminalOffense> clones = new ArrayList<>();
		for(CriminalOffense offenseEntry: list){
			CriminalOffense clone = offenseEntry.clone();
			clone.setUri(uriInfo.getAbsolutePath()+"/"+clone.getRefId());
			clone.setMemberId(memberId);
			clones.add(clone);
		}
		
		educationEntries.setItems(clones);
		return educationEntries;
	}

	public CriminalOffense getCriminalOffenseById(String memberId,String offenseEntryId) {

		CriminalOffense offenseEntry = dao.findByRefId(offenseEntryId, CriminalOffense.class);
		return offenseEntry.clone();
	}
	
	public CriminalOffense createCriminalOffense(String memberId,CriminalOffense offenseEntry) {
		assert offenseEntry.getRefId()==null;
		Member member = dao.findByMemberId(memberId);
		offenseEntry.setMember(member);
		dao.save(offenseEntry);		
		
		return offenseEntry.clone();
	}

	public CriminalOffense updateCriminalOffense(String memberId,String offenseEntryId, CriminalOffense offenseEntry) {
		assert offenseEntry.getRefId()!=null;
		
		CriminalOffense poCriminalOffense = dao.findByRefId(offenseEntryId, CriminalOffense.class);
		poCriminalOffense.copyFrom(offenseEntry);
		dao.save(offenseEntry);
		return poCriminalOffense.clone();
	}

	public void deleteCriminalOffense(String memberId, String offenseEntryId) {
		CriminalOffense offenseEntry = dao.findByRefId(offenseEntryId, CriminalOffense.class);
		dao.delete(offenseEntry);
	}

}
