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
	
	public List<EnquiriesDto> getAll(String memberId,Integer offset,
			Integer limit){
		
		List<Enquiries> list = null;
		if(memberId.equals("ALL")){
			list = dao.getAllEnquiries(null,offset, limit);
		}
		else{
			list = dao.getAllEnquiries(memberId, offset, limit);
		}
		
		List<EnquiriesDto> dtos = new ArrayList<>();
		for(Enquiries e: list){
			dtos.add(e.toDto());
		}
		return dtos;
	}
	
	public EnquiriesDto create(String memberId,EnquiriesDto dto){
		Enquiries enquiries = new Enquiries();
		enquiries.copyFrom(dto);
		if(memberId.equals("ALL")){
			dto.setMemberRefId(null);
		}
		dao.save(enquiries);
		
		return enquiries.toDto();
	}
	
	public EnquiriesDto update(String memberId,String enquiriesRefId, EnquiriesDto dto){
		Enquiries enquiries = dao.findByRefId(enquiriesRefId, Enquiries.class);
		enquiries.copyFrom(dto);
		dao.save(enquiries);
		
		return enquiries.toDto();
	}

	public Integer getCount(String memberId) {
		if(memberId.equals("ALL")){
			return dao.getCount(null);
		}
		
		return dao.getCount(memberId);
	}

	public void delete(String memberId, String enquiryId) {
		Enquiries e = dao.findByRefId(enquiryId, Enquiries.class);
		dao.delete(e);
	}
}
