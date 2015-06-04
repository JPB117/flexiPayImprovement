package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;

@Transactional
public class CPDDaoHelper {

	@Inject CPDDao dao;
	@Inject MemberDao memberDao;
	@Inject EventsDao eventDao;
	
	public ResourceCollectionModel getAllCPD(String memberId, Integer offset,
			Integer limit, UriInfo uriInfo) {
		
		int total = dao.getCPDCount(memberId);
		
		ResourceCollectionModel<CPD> collection = new ResourceCollectionModel<>(offset,limit,total, uriInfo);
		List<CPD> members = dao.getAllCPDs(memberId,offset, limit);
		
		List<CPD> rtn = new ArrayList<>();
		for(CPD cpd: members){
			CPD clone = cpd.clone();
			clone.setUri(uriInfo.getAbsolutePath().toString()+"/"+clone.getRefId());
			rtn.add(clone);
		}
		
		collection.setItems(rtn);
		return collection;
	}

	public CPD getCPD(String memberId, String cpdId) {
		CPD cpd = dao.findByCPDId(cpdId);
		return cpd.clone();
	}

	public void create(String memberId,CPD cpd) {
		Member member = memberDao.findByMemberId(memberId);
		Event event = eventDao.getByEventId(cpd.getEventId());
		cpd.setEvent(event);
		cpd.setMember(member);
		
		dao.save(cpd);
	}
	
	public void update(String memberId, String cpdId, CPD cpd) {
		CPD poCPD = dao.findByCPDId(cpdId);
		Event event = eventDao.getByEventId(cpd.getEventId());
		poCPD.setEvent(event);
		poCPD.setCpdHours(cpd.getCpdHours());
		poCPD.setStatus(cpd.getStatus());
		poCPD.setStartDate(cpd.getStartDate());
		poCPD.setEndDate(cpd.getEndDate());
		dao.save(poCPD);
		
	}

	public void delete(String memberId, String cpdId) {
		dao.delete(dao.findByCPDId(cpdId));
	}

}
