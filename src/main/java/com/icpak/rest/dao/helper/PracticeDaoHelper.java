package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.IDUtils;
import com.icpak.rest.dao.PracticeDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.base.ExpandTokens;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.Branch;
import com.icpak.rest.models.membership.Contact;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.Practice;

@Transactional
public class PracticeDaoHelper {
	@Inject PracticeDao dao;
	@Inject UsersDao userDao;
	
	public void createPractice(Practice practice){
		practice.setRefId(IDUtils.generateId());
		dao.createPractice(practice);
		assert practice.getId()!=null;
	}
	
	public void updatePractice(String practiceId, Practice practice){
		Practice po = dao.findByPracticeId(practiceId);
		dao.updatePractice(po);
	}
	
	public void deletePractice(String practiceId){
		Practice practice = dao.findByPracticeId(practiceId);
		dao.delete(practice);
	}
	
	public ResourceCollectionModel<Practice> getAllPractices(Integer offset, Integer limit,
			UriInfo uriInfo) {
		int total = dao.getPracticeCount();
		ResourceCollectionModel<Practice> collection = new ResourceCollectionModel<>(offset,limit,total, uriInfo);
		List<Practice> members = dao.getAllPractices(offset, limit);
		
		List<Practice> rtn = new ArrayList<>();
		for(Practice practice: members){
			practice.setUri(uriInfo.getAbsolutePath().toString()+"/"+practice.getRefId());
			rtn.add(practice.clone(ExpandTokens.DETAIL.toString()));
		}
		collection.setItems(rtn);
		return collection;
	}

	public Practice getPracticeById(String practiceId) {
		Practice practice = dao.findByPracticeId(practiceId);
		if(practice==null){
			throw new ServiceException(ErrorCodes.NOTFOUND,"'"+practiceId+"'");
		}
		
		return practice;
	}

	public Branch getBranchById(String branchId) {
		
		Branch po = dao.getBranch(branchId, true);
		return po.clone();
	}

	public ResourceCollectionModel<Branch> getAllBranches(String practiceId,
			Integer offset, Integer limit, UriInfo uriInfo) {
		int total = dao.getBranchCount(practiceId);
		ResourceCollectionModel<Branch> collection = new ResourceCollectionModel<>(offset,limit,total, uriInfo);
		List<Branch> members = dao.getAllBranches(practiceId,offset, limit);
		
		List<Branch> rtn = new ArrayList<>();
		for(Branch branch: members){
			branch.setUri(uriInfo.getAbsolutePath().toString()+"/"+branch.getRefId());
			rtn.add(branch.clone(ExpandTokens.DETAIL.toString()));
		}
		collection.setItems(rtn);
		return collection;
	}

	public void deleteBranch(String branchId) {
		dao.deleteBranch(branchId);
	}

	public Branch updateBranch(String branchId, Branch branch) {
		
		Branch po = dao.getBranch(branchId, true);
//		
//		if(branch.getContacts()!=null){
//			List<Contact> contacts = new ArrayList<>();
//			for(Contact contact: branch.getContacts()){
//				if(contact.getRefId()!=null){
//					Contact contactPO = userDao.findByRef(Contact.class, contact.getRefId(), true);
//					contactPO.setContactName(contact.getContactName());
//					contactPO.setEmail(contact.getEmail());
//					contactPO.setFax(contact.getFax());
//					if(contact.getMember()!=null && contact.getMember().getRefId()!=null){
//						Member member = userDao.findByRef(Member.class, contact.getMember().getRefId(), true);
//						contactPO.setMember(member);
//					}
//					contactPO.setMobileNumbers(contact.getMobileNumbers());
//					contactPO.setPhysicalAddress(contact.getPhysicalAddress());
//					contactPO.setPostalCode(contact.getPostalCode());
//					contactPO.setPrimaryContact(contact.isPrimaryContact());
//					contactPO.setTelephoneNumbers(contact.getTelephoneNumbers());
//					contactPO.setType(contact.getType());
//					contactPO.setWebsite(contact.getWebsite());
//					contacts.add(contactPO);
//				}
//			}
//			po.setContacts(contacts);
//		}
		
		po.setMemberRegNo(branch.getMemberRegNo());
		po.setName(branch.getName());
		
		if(branch.getPractice()!=null && branch.getPractice().getRefId()!=null){
			po.setPractice(dao.findByPracticeId(branch.getPractice().getRefId()));
		}
		po.setPracticingCertNo(branch.getPracticingCertNo());

		dao.save(po);
		
		return po.clone();
	}

}
