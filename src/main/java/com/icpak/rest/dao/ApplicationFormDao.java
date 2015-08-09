package com.icpak.rest.dao;

import java.util.Collection;
import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationCategory;
import com.icpak.rest.models.membership.ApplicationFormSpecialization;
import com.icpak.rest.models.membership.ApplicationFormTraining;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.EduType;
import com.workpoint.icpak.shared.model.Specializations;

public class ApplicationFormDao extends BaseDao{

	 public void createApplication(ApplicationFormHeader application) {
	        save( application );
	    }

	    public List<ApplicationFormHeader> getAllApplications(Integer offSet, Integer limit) {
	    	return getResultList(getEntityManager().createQuery("select u from ApplicationFormHeader u"
	    			+ " where u.isActive=1 order by id desc")
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

		public ApplicationCategory findApplicationCategory(ApplicationType type) {
			return getSingleResultOrNull(getEntityManager().createQuery("select u from ApplicationCategory u"
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

		public List<ApplicationCategory> getAllCategories() {
			
			return getResultList(getEntityManager().createQuery("FROM ApplicationCategory c where c.isActive=1"));
		}

		public ApplicationCategory getCategory(String categoryId) {
			
			return getSingleResultOrNull(getEntityManager()
					.createQuery("FROM ApplicationCategory c where c.refId=:refId")
					.setParameter("refId", categoryId));
		}

		public Collection<ApplicationFormTraining> getTraining(
				String applicationId) {
			
			return getResultList(getEntityManager().createQuery(
					"from ApplicationFormTraining "
					+ "where applicationRefId=:refId and isactive=1").setParameter("refId", applicationId));
		}

		public Collection<ApplicationFormSpecialization> getSpecialization(
				String applicationId) {
			
			return getResultList(getEntityManager().createQuery(
					"from ApplicationFormSpecialization "
					+ "where applicationRefId=:refId and isactive=1").setParameter("refId", applicationId));
		}

		public ApplicationFormSpecialization getSpecializationByName(String applicationRefId,
				Specializations specialization) {
			
			return getSingleResultOrNull(getEntityManager().createQuery(
					"from ApplicationFormSpecialization "
					+ "where specialization=:specialization "
					+ "and applicationRefId=:refId and isactive=1")
					.setParameter("specialization", specialization)
					.setParameter("refId", applicationRefId)
					);
		}

}
