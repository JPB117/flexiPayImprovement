package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.cpd.CPD;
import com.workpoint.icpak.shared.model.CPDDto;

@Transactional
public class CPDDaoHelper {

	@Inject CPDDao dao;
	@Inject EventsDao eventDao;
	@Inject UsersDao userDao;
	
	public List<CPDDto> getAllCPD(String memberId, Integer offset,
			Integer limit) {
		
		List<CPD> cpds = null;
		
		if(memberId!=null && memberId.equals("ALL")){
			cpds = dao.getAllCPDs(offset, limit);
		}else{
			cpds = dao.getAllCPDs(memberId,offset, limit);
		}
		
		List<CPDDto> rtn = new ArrayList<>();
		for(CPD cpd: cpds){
			CPDDto dto = cpd.toDTO();
			dto.setFullNames(userDao.getFullNames(cpd.getMemberId()));
			rtn.add(dto);
		}
		
		return rtn;
	}

	public CPDDto getCPD(String memberId, String cpdId) {
		CPD cpd = dao.findByCPDId(cpdId);
		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberId));
		return rtn;
	}

	public CPDDto create(String memberId,CPDDto cpdDto) {
		
		CPD cpd = new CPD();
		cpd.copyFrom(cpdDto);
		cpd.setMemberId(memberId);
		dao.save(cpd);
		
		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberId));
		return rtn;
	}
	
	public CPDDto update(String memberId, String cpdId, CPDDto cpd) {
		CPD poCPD = dao.findByCPDId(cpdId);
		poCPD.copyFrom(cpd);		
		poCPD.setMemberId(memberId);
		dao.save(poCPD);
		
		CPDDto rtn = poCPD.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberId));
		return rtn;
	}

	public void delete(String memberId, String cpdId) {
		dao.delete(dao.findByCPDId(cpdId));
	}

}
