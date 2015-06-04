package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.base.ExpandTokens;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.membership.Application;

@Transactional
public class ApplicationsDaoHelper {

	
	@Inject ApplicationDao applicationDao;
	@Inject MemberDao memberDao;
	
	public void createApplication(Application application){
		if(application.getRefId()!=null){
			updateApplication(application.getRefId(), application);
			return;
		}
		
		if(application.getMember()!=null){
			application.setMember(memberDao.findByMemberId(application.getMember().getRefId()));
		}
		applicationDao.createApplication(application);
		
		assert application.getId()!=null;
	}
	
	public void updateApplication(String applicationId, Application application){
		Application po = applicationDao.findByApplicationId(applicationId,true);
		po.setStatus(application.getStatus());
		po.setAcknowledged(application.isAcknowledged());
		po.setApplicationNo(application.getApplicationNo());
		po.setApplicationType(application.getApplicationType());
		po.setApprovalMinNo(application.getApprovalMinNo());
		
		if(po.getAuditDetails()==null){
			po.setAuditDetails(application.getAuditDetails());
		}else{
			po.getAuditDetails().copy(application.getAuditDetails());
		}
		
		po.setDateAcknowledge(application.getDateAcknowledge());
		po.setDateNotificationSent(application.getDateNotificationSent());
		po.setDateReceived(application.getDateReceived());
		po.setDefferalMinNo(application.getDefferalMinNo());
		po.setEmplSector(application.getEmplSector());
		po.setFileNo(application.getFileNo());
		po.setGazetteNoticeNo(application.getGazetteNoticeNo());
		po.setLicenceCollectedOrDispatched(application.getLicenceCollectedOrDispatched());
		if(application.getMember()!=null){
			po.setMember(memberDao.findByMemberId(application.getMember().getRefId()));
		}
		po.setMemberStanding(application.getMemberStanding());
		po.setStopPractice(application.isStopPractice());
		po.setReasonForStopPractice(application.getReasonForStopPractice());
		po.setReceiptDate(application.getReceiptDate());
		po.setReceiptNo(application.getReceiptNo());
		po.setRegistrationDate(application.getRegistrationDate());
		po.setRegistrationNo(application.getRegistrationNo());
		po.setStatus(application.getStatus());
		po.setSubmissionDate(application.getSubmissionDate());
		applicationDao.updateApplication(po);
	}
	
	public void deleteApplication(String applicationId){
		Application application = applicationDao.findByApplicationId(applicationId);
		applicationDao.delete(application);
	}
	public ResourceCollectionModel<Application> getAllApplications(Integer offset, Integer limit,
			UriInfo uriInfo) {
		int total = applicationDao.getApplicationCount();
		
		ResourceCollectionModel<Application> collection = new ResourceCollectionModel<>(offset,limit,total, uriInfo);
		List<Application> applications = applicationDao.getAllApplications(offset, limit);
		
		List<Application> rtn = new ArrayList<>();
		for(Application application: applications){
			application.setUri(uriInfo.getAbsolutePath().toString()+"/"+application.getRefId());
			rtn.add(application.clone(ExpandTokens.DETAIL.toString()));
		}
		
		collection.setItems(rtn);
		return collection;
	}
	
	public Application getApplicationById(String applicationId) {
		Application application = applicationDao.findByApplicationId(applicationId);
		if(application==null){
			throw new ServiceException(ErrorCodes.NOTFOUND,"'"+applicationId+"'");
		}
		
		return application;
	}


}
