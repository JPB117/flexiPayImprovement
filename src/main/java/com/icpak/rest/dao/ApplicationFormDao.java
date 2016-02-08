package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.ApplicationCategory;
import com.icpak.rest.models.membership.ApplicationFormAccountancy;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.ApplicationFormEmpSector;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationFormSpecialization;
import com.icpak.rest.models.membership.ApplicationFormTraining;
import com.icpak.rest.models.membership.MemberImport;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationSummaryDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.EduType;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.Specializations;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class ApplicationFormDao extends BaseDao {

	Logger logger = Logger.getLogger(CPDDao.class);

	public void createApplication(ApplicationFormHeader application) {
		save(application);
	}

	public List<ApplicationFormHeader> getAllApplications(Integer offSet,
			Integer limit, String searchTerm) {
		if (searchTerm.isEmpty()) {
			return getResultList(
					getEntityManager()
							.createQuery(
									"select u from ApplicationFormHeader u"
											+ " where u.isActive=1 and applicationStatus<>:status "
											+ "order by id desc").setParameter(
									"status", ApplicationStatus.APPROVED),
					offSet, limit);
		} else {
			return getResultList(
					getEntityManager()
							.createQuery(
									"select u from ApplicationFormHeader u"
											+ " where u.isActive=1 and applicationStatus<>:status "
											+ "and (surname like :searchTerm or otherNames like :searchTerm "
											+ "or email like :searchTerm or "
											+ "concat(surname,' ',otherNames) like :searchTerm) "
											+ "order by id desc")
							.setParameter("status", ApplicationStatus.APPROVED)
							.setParameter("searchTerm", "%" + searchTerm + "%"),
					offSet, limit);
		}

	}

	public List<ApplicationFormHeaderDto> getAllApplicationDtos(Integer offset,
			Integer limit, String searchTerm, String applicationStatus,
			String paymentStatus) {
		logger.info("++++Getting all applications");
		if (paymentStatus != null && applicationStatus != null) {
			System.err.println(" with application status>>" + applicationStatus
					+ " and payment status >>>" + paymentStatus);
		}
		StringBuffer sql = new StringBuffer(
				"select a.refId,a.created,a.Surname,a.`Other Names`,a.`E-mail`,a.applicationStatus,a.paymentStatus "
						+ "from `Application Form Header` a ");

		Map<String, Object> params = appendParameters(sql, applicationStatus,
				paymentStatus, searchTerm);
		sql.append(" order by a.created desc");
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		List<Object[]> rows = getResultList(query, offset, limit);
		List<ApplicationFormHeaderDto> applications = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			ApplicationFormHeaderDto app = new ApplicationFormHeaderDto();
			String refId = (value = row[i++]) == null ? null : value.toString();
			app.setRefId(refId);
			Date created = (value = row[i++]) == null ? null : (Date) value;
			app.setCreated(created);
			String surName = (value = row[i++]) == null ? null : value
					.toString();
			app.setSurname(surName);
			String otherNames = (value = row[i++]) == null ? null : value
					.toString();
			app.setOtherNames(otherNames);
			String email = (value = row[i++]) == null ? null : value.toString();
			app.setEmail(email);
			String applicationStatusDb = (value = row[i++]) == null ? null
					: value.toString();
			if (applicationStatusDb != null) {
				app.setApplicationStatus(ApplicationStatus
						.valueOf(applicationStatusDb));
			}
			String paymentStatusDb = (value = row[i++]) == null ? null : value
					.toString();
			if (paymentStatusDb != null) {
				app.setPaymentStatus(PaymentStatus.valueOf(paymentStatusDb));
			}
			applications.add(app);
		}
		return applications;
	}

	private Map<String, Object> appendParameters(StringBuffer sql,
			String applicationStatus, String paymentStatus, String searchTerm) {
		Map<String, Object> params = new HashMap<>();
		boolean isFirstParam = true;

		if (applicationStatus != null && !applicationStatus.equals("")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("applicationStatus", applicationStatus);
			sql.append(" applicationStatus=:applicationStatus");
		}

		if (paymentStatus != null && !paymentStatus.equals("")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("paymentStatus", paymentStatus);
			sql.append(" paymentStatus=:paymentStatus");
		}

		if (searchTerm != null && (!searchTerm.equals(""))) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			sql.append("(surName like :searchTerm or `other Names` like :searchTerm "
					+ "or `E-mail` like :searchTerm or "
					+ "concat(surName,' ',`other Names`) like :searchTerm)");
			params.put("searchTerm", "%" + searchTerm + "%");
		}
		return params;
	}

	public List<MemberImport> importMembers(Integer offSet, Integer limit) {
		return getResultList(
				getEntityManager().createQuery("select u from MemberImport u"),
				offSet, limit);
	}

	public void updateApplication(ApplicationFormHeader application) {
		createApplication(application);
	}

	public int getApplicationCount(String searchTerm, String paymentStatus,
			String applicationStatus) {
		Number number = null;
		StringBuffer sql = new StringBuffer(
				"select count(*) from `Application Form Header`");
		Map<String, Object> params = appendParameters(sql, applicationStatus,
				paymentStatus, searchTerm);

		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		number = getSingleResultOrNull(query);
		return number.intValue();
	}

	public ApplicationFormHeader findByApplicationId(String refId) {
		return findByApplicationId(refId, true);
	}

	public ApplicationFormHeader findByApplicationId(String refId,
			boolean throwExceptionIfNull) {
		ApplicationFormHeader application = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from ApplicationFormHeader u where u.refId=:refId")
				.setParameter("refId", refId));

		if (application == null && throwExceptionIfNull) {
			throw new ServiceException(ErrorCodes.NOTFOUND,
					"ApplicationFormHeader", "'" + refId + "'");
		}

		return application;
	}

	public ApplicationCategory findApplicationCategory(ApplicationType type) {
		return getSingleResultOrNull(getEntityManager().createQuery(
				"select u from ApplicationCategory u"
						+ " where u.isActive=1 and u.type=:type").setParameter(
				"type", type));
	}

	public int getEducationCount(String applicationId) {
		Number number = null;
		number = getSingleResultOrNull(getEntityManager().createNativeQuery(
				"select count(*) from `Application Form Educational` "
						+ "where ApplicationRefId=:refId and isactive=1")
				.setParameter("refId", applicationId));

		return number.intValue();
	}

	public Collection<ApplicationFormEducational> getEducation(
			String applicationId) {
		return getResultList(getEntityManager().createQuery(
				"from ApplicationFormEducational "
						+ "where ApplicationRefId=:refId and isactive=1")
				.setParameter("refId", applicationId));
	}

	public Collection<ApplicationFormAccountancy> getAccountancy(
			String applicationId) {
		return getResultList(getEntityManager().createQuery(
				"from ApplicationFormAccountancy "
						+ "where ApplicationRefId=:refId and isactive=1")
				.setParameter("refId", applicationId));
	}

	public int getEducationCount(String applicationId, EduType type) {

		Number number = null;
		number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery(
						"select count(*) from `Application Form Educational` "
								+ "where ApplicationRefId=:refId and type=:type and isactive=1")
				.setParameter("refId", applicationId)
				.setParameter("type", type.ordinal()));

		return number.intValue();
	}

	public Collection<ApplicationFormEducational> getEducation(
			String applicationId, EduType type) {

		return getResultList(getEntityManager()
				.createQuery(
						"from ApplicationFormEducational "
								+ "where ApplicationRefId=:refId and type=:type and isactive=1")
				.setParameter("refId", applicationId)
				.setParameter("type", type.ordinal()));
	}

	public List<ApplicationCategory> getAllCategories() {

		return getResultList(getEntityManager().createQuery(
				"FROM ApplicationCategory c where c.isActive=1"));
	}

	public ApplicationCategory getCategory(String categoryId) {

		return getSingleResultOrNull(getEntityManager().createQuery(
				"FROM ApplicationCategory c where c.refId=:refId")
				.setParameter("refId", categoryId));
	}

	public Collection<ApplicationFormTraining> getTraining(String applicationId) {

		return getResultList(getEntityManager().createQuery(
				"from ApplicationFormTraining "
						+ "where applicationRefId=:refId and isactive=1")
				.setParameter("refId", applicationId));
	}

	public Collection<ApplicationFormSpecialization> getSpecialization(
			String applicationId) {
		return getResultList(getEntityManager().createQuery(
				"from ApplicationFormSpecialization "
						+ "where applicationRefId=:refId and isactive=1")
				.setParameter("refId", applicationId));
	}

	public ApplicationFormSpecialization getSpecializationByName(
			String applicationRefId, Specializations specialization) {

		return getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from ApplicationFormSpecialization "
								+ "where specialization=:specialization "
								+ "and applicationRefId=:refId and isactive=1")
				.setParameter("specialization", specialization)
				.setParameter("refId", applicationRefId));
	}

	public Collection<ApplicationFormEmpSector> getEmployment(
			String applicationId) {
		return getResultList(getEntityManager().createQuery(
				"from ApplicationFormEmpSector "
						+ "where applicationRefId=:refId and isactive=1")
				.setParameter("refId", applicationId));
	}

	public ApplicationFormEmpSector getEmploymentByName(
			String applicationRefId, Specializations specialization) {
		return getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from ApplicationFormEmpSector "
								+ "where specialization=:specialization "
								+ "and applicationRefId=:refId and isactive=1")
				.setParameter("specialization", specialization)
				.setParameter("refId", applicationRefId));
	}

	public ApplicationSummaryDto getApplicationsSummary() {
		String sql = "select count(*), applicationStatus from `Application Form Header` "
				+ "group by applicationStatus";

		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql));

		ApplicationSummaryDto summary = new ApplicationSummaryDto();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer count = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			String status = (value = row[i++]) == null ? "PENDING" : value
					.toString();

			if (!status.isEmpty() || status != null) {
				if (ApplicationStatus.valueOf(status) == ApplicationStatus.APPROVED) {
					summary.setProcessedCount(count);
				} else {
					summary.setPendingCount(count);
				}
			}

		}

		return summary;

	}

	public ApplicationFormHeader getApplicationByInvoiceRef(String invoiceRefId) {
		return getSingleResultOrNull(getEntityManager()
				.createQuery(
						"FROM ApplicationFormHeader h where h.invoiceRef=:invoiceRefId")
				.setParameter("invoiceRefId", invoiceRefId));
	}

	public ApplicationFormHeader getApplicationByUserRef(String userRefId) {
		return getSingleResultOrNull(getEntityManager().createQuery(
				"FROM ApplicationFormHeader h where h.userRefId=:userRefId")
				.setParameter("userRefId", userRefId));
	}

}
