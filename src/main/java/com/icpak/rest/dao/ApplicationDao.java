package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.Application;

public class ApplicationDao extends BaseDao{

	 public void createApplication(Application application) {
	        save( application );
	    }

	    public List<Application> getAllApplications(Integer offSet, Integer limit) {
	    	return getResultList(getEntityManager().createQuery("select u from Application u"
	    			+ " where u.isActive=1")
	    			, offSet, limit);
	    }

	    public void updateApplication(Application application) {
	        createApplication(application);
	    }

		public int getApplicationCount() {
			return getApplicationCount(null);
		}

		public int getApplicationCount(String roleId) {
			
			Number number = null;
			if(roleId==null){
				number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from application "
						+ "where isactive=1"));
			}else{
				number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from application u "
						+ "where u.isactive=1"));
			}
			
			return number.intValue();
		}

		public Application findByApplicationId(String refId) {
			return findByApplicationId(refId, true);
		}
		public Application findByApplicationId(String refId, boolean throwExceptionIfNull) {
			Application application = getSingleResultOrNull(
					getEntityManager().createQuery("from Application u where u.refId=:refId")
					.setParameter("refId", refId));
			
			if(application==null && throwExceptionIfNull){
				throw new ServiceException(ErrorCodes.NOTFOUND,"Application", "'"+refId+"'");
			}
			
			return application;
		}

}
