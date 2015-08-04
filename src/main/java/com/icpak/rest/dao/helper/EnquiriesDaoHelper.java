package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.EnquiriesDao;
import com.icpak.rest.models.util.Enquiries;
import com.workpoint.icpak.shared.model.EnquiriesDto;

@Transactional
public class EnquiriesDaoHelper {

	@Inject EnquiriesDao dao;
	
	public List<EnquiriesDto> getAll(String memberRefId,Integer offset,
			Integer limit){
		
		List<Enquiries> list = null;
		if(memberRefId.equals("ALL")){
			list = dao.getAllEnquiries(null,offset, limit);
		}
		else{
			list = dao.getAllEnquiries(memberRefId, offset, limit);
		}
		
		List<EnquiriesDto> dtos = new ArrayList<>();
		for(Enquiries e: list){
			dtos.add(e.toDto());
		}
		return dtos;
	}
	
	public EnquiriesDto create(String memberRefId,EnquiriesDto dto){
		dto.setMemberRefId(memberRefId);
		Enquiries enquiries = new Enquiries();
		enquiries.copyFrom(dto);
		if(memberRefId.equals("ALL")){
			dto.setMemberRefId(null);
		}
		dao.save(enquiries);
		
		return enquiries.toDto();
	}
	
	public EnquiriesDto update(String memberRefId,String enquiriesRefId, EnquiriesDto dto){
		dto.setMemberRefId(memberRefId);
		Enquiries enquiries = dao.findByRefId(enquiriesRefId, Enquiries.class);
		enquiries.copyFrom(dto);
		dao.save(enquiries);
		
		return enquiries.toDto();
	}

	public Integer getCount(String memberRefId) {
		if(memberRefId.equals("ALL")){
			return dao.getCount(null);
		}
		
		return dao.getCount(memberRefId);
	}

	public void delete(String memberId, String enquiryId) {
		Enquiries e = dao.findByRefId(enquiryId, Enquiries.class);
		dao.delete(e);
	}
}
