package com.icpak.rest.dao;

import java.util.Collection;
import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.Category;
import com.icpak.rest.models.membership.EduType;
import com.workpoint.icpak.shared.model.ApplicationType;

public class ApplicationFormDao extends BaseDao{

	 public void createApplication(ApplicationFormHeader application) {
	        save( application );
	    }

	    public List<ApplicationFormHeader> getAllApplications(Integer offSet, Integer limit) {
	    	return getResultList(getEntityManager().createQuery("select u from ApplicationFormHeader u"
	    			+ " where u.isActive=1")
	    			, offSet, limit);
	    }

	    public void updateApplication(ApplicationFormHeader application) {
	        createApplication(application);
	    }

		public int getApplicationCount() {
			
			Number number = null;
			
			number = getSingleResultOrNull(getEntityManager().createNativeQuery(
						"select count(*) from `Application Form Header` "
						+ "where isactive=1"));
			
			return number.intValue();
		}

		public ApplicationFormHeader findByApplicationId(String refId) {
			return findByApplicationId(refId, true);
		}
		
		public ApplicationFormHeader findByApplicationId(String refId, boolean throwExceptionIfNull) {
			ApplicationFormHeader application = getSingleResultOrNull(
					getEntityManager().createQuery("from ApplicationFormHeader u where u.refId=:refId")
					.setParameter("refId", refId));
			
			if(application==null && throwExceptionIfNull){
				throw new ServiceException(ErrorCodes.NOTFOUND,"ApplicationFormHeader", "'"+refId+"'");
			}
			
			return application;
		}

		public Category findApplicationCategory(ApplicationType type) {
			return getSingleResultOrNull(getEntityManager().createQuery("select u from Category u"
	    			+ " where u.isActive=1 and u.type=:type").setParameter("type", type));
		}

		public int getEducationCount(String applicationId) {
			
			Number number = null;
			number = getSingleResultOrNull(getEntityManager().createNativeQuery(
						"select count(*) from `Application Form Educational` "
						+ "where ApplicationRefId=:refId and isactive=1").setParameter("refId", applicationId));
			
			return number.intValue();
		}

		public Collection<ApplicationFormEducational> getEducation(
				String applicationId) {
			
			return getResultList(getEntityManager().createQuery(
						"from ApplicationFormEducational "
						+ "where ApplicationRefId=:refId and isactive=1").setParameter("refId", applicationId));
		}

		public int getEducationCount(String applicationId, EduType type) {
			
			Number number = null;
			number = getSingleResultOrNull(getEntityManager().createNativeQuery(
						"select count(*) from `Application Form Educational` "
						+ "where ApplicationRefId=:refId and type=:type and isactive=1")
						.setParameter("refId", applicationId)
						.setParameter("type", type.ordinal()));
			
			return number.intValue();
		}

		public Collection<ApplicationFormEducational> getEducation(
				String applicationId, EduType type) {
			
			return getResultList(getEntityManager().createQuery(
						"from ApplicationFormEducational "
						+ "where ApplicationRefId=:refId and type=:type and isactive=1")
						.setParameter("refId", applicationId)
						.setParameter("type", type.ordinal()));
		}

		public List<Category> getAllCategories() {
			
			return getResultList(getEntityManager().createQuery("FROM Category c where c.isActive=1"));
		}

		public Category getCategory(String categoryId) {
			
			return getSingleResultOrNull(getEntityManager()
					.createQuery("FROM Category c where c.refId=:refId")
					.setParameter("refId", categoryId));
		}

}
