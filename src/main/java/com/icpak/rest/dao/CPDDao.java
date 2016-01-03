package com.icpak.rest.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.util.Attachment;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;

/**
 * 
 * @author duggan
 *
 */
public class CPDDao extends BaseDao {
	Logger logger = Logger.getLogger(CPDDao.class);

	public void createCPD(CPD cpd) {
		save(cpd);
	}

	public List<CPD> getAllCPDs(Integer offSet, Integer limit, Date startDate,
			Date endDate) {
		return getAllCPDs(null, offSet, limit, startDate, endDate);
	}

	public List<CPD> getAllCPDs(String passedMemberRefId, Integer offSet,
			Integer limit, Date startDate, Date endDate) {
		logger.info(" +++ GET CPPD FOR +++++ REFID == " + passedMemberRefId);
		StringBuffer sql = new StringBuffer(
				"select c.id,c.refId as cpdRefId,c.startDate,c.endDate,"
						+ "c.title,c.organizer,c.category,c.cpdHours,"
						+ "c.status,c.memberRegistrationNo,c.memberRefId, concat(u.firstName,' ',u.lastName) "
						// + ",a.refId "
						+ "from cpd c "
						+ "inner join member m on (c.memberRefId=m.refId) "
						+ "inner join user u on (u.id=m.userId) "
		// + "left join attachment a on (c.id=a.cpdid)"
		);

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();

		if (startDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("startDate", startDate);
			sql.append(" startDate>=:startDate");
		}
		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" endDate<=:endDate");
		}
		if (passedMemberRefId != null && !passedMemberRefId.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("memberRefId", passedMemberRefId);
			sql.append(" c.memberRefId=:memberRefId");
		}
		// sql.append(" and isActive=1");
		sql.append(" order by c.created desc");

		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		logger.info(" +++ EXECUTING QUERY ...... SQL " + sql);

		List<Object[]> rows = getResultList(query, offSet, limit);

		List<CPD> cpds = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer id = (value = row[i++]) == null ? null
					: (Integer) value;
			String refId = (value = row[i++]) == null ? null : value.toString();
			Date startDt = (value = row[i++]) == null ? null : (Date) value;
			Date endDt = (value = row[i++]) == null ? null : (Date) value;
			String title = (value = row[i++]) == null ? null : value.toString();
			String organizer = (value = row[i++]) == null ? null : value
					.toString();
			String categry = (value = row[i++]) == null ? null : value
					.toString();
			Double cpdHours = (value = row[i++]) == null ? null
					: (Double) value;
			String status = (value = row[i++]) == null ? null : value
					.toString();
			String memberRegNo = (value = row[i++]) == null ? null : value
					.toString();
			String memberRefId = (value = row[i++]) == null ? null : value
					.toString();
			String fullNames = (value = row[i++]) == null ? null : value
					.toString();
			CPD cpd = new CPD();
			cpd.setRefId(refId);
			cpd.setStartDate(startDt);
			cpd.setEndDate(endDt);
			cpd.setTitle(title);
			cpd.setOrganizer(organizer);
			cpd.setCategory(CPDCategory.valueOf(categry));
			cpd.setCpdHours(cpdHours);
			cpd.setStatus(CPDStatus.valueOf(status));
			cpd.setMemberRegistrationNo(memberRegNo);
			cpd.setMemberRefId(memberRefId);
			cpd.setFullnames(fullNames);
			Set<Attachment> attachments = getAllAttachment(id);
			cpd.setAttachments(attachments);
			cpds.add(cpd);

		}
		return cpds;
	}

	private Set<Attachment> getAllAttachment(Integer id) {
		logger.info(" +++ GET Attachments FOR +++++ cpdId == " + id);
		StringBuffer sql = new StringBuffer(
				"select refId,name from attachment where cpdid=:passedId");
		Query query = getEntityManager().createNativeQuery(sql.toString())
				.setParameter("passedId", id);
		List<Object[]> rows = getResultList(query, 0, 1000);
		Set<Attachment> attachments = new HashSet<>();
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString();
			Attachment attachment = this.findByRefId(refId, Attachment.class);
			attachments.add(attachment);
		}
		logger.info(" Found this number of attachments == "
				+ attachments.size());
		return attachments;
	}

	public void updateCPD(CPD cpd) {
		createCPD(cpd);
	}

	public int getCPDCount(String memberRefId, Date startDate, Date endDate) {
		logger.info(" +++ GET RESULT COUNT FOR CPD +++++ REFID == "
				+ memberRefId);
		Number number = null;
		if (memberRefId != null) {
			if (memberRefId.equals("ALL")) {
				logger.info(" +++ COUNT FOR ALL ++++ ");
				logger.info(" +++ startDate ++++ " + startDate);
				logger.info(" +++ endDate ++++ " + endDate);
				number = getSingleResultOrNull(getEntityManager()
						.createNativeQuery(
								"select count(*) from cpd c "
										+ "where c.isactive=1 and startDate>=:startDate and endDate<=:endDate")
						.setParameter("startDate", startDate)
						.setParameter("endDate", endDate));
			} else {
				logger.info(" +++ COUNT FOR MEMBER ++++ ");
				number = getSingleResultOrNull(getEntityManager()
						.createNativeQuery(
								"select count(*) from cpd c inner join Member m on (c.memberRefId=m.refId) "
										+ "where c.isactive=1 and m.refId=:refId and startDate>=:startDate and endDate<:endDate")
						.setParameter("startDate", startDate)
						.setParameter("endDate", endDate)
						.setParameter("refId", memberRefId));
			}
		} else {
			throw new ServiceException(ErrorCodes.ILLEGAL_ARGUMENT, "CPD",
					"'MemberRefId'");
		}

		logger.info(" +++ COUNT RESULT +++++ == " + number.intValue());

		return number.intValue();
	}

	public CPD findByCPDId(String refId) {
		return findByCPDId(refId, true);
	}

	public CPD findByCPDId(String refId, boolean throwExceptionIfNull) {
		CPD cpd = getSingleResultOrNull(getEntityManager().createQuery(
				"from CPD u where u.refId=:refId").setParameter("refId", refId));

		if (cpd == null && throwExceptionIfNull) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "CPD", "'" + refId
					+ "'");
		}

		return cpd;
	}

	public void deleteCPDByMemberAndEvent(String memberRefId, String eventId) {

		delete(getCPDByMemberAndEvent(memberRefId, eventId));
	}

	public CPD getCPDByMemberAndEvent(String memberRefId, String eventId) {
		CPD cpd = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from CPD u where u.memberRefId=:memberRefId and u.eventId=:eventId")
				.setParameter("memberRefId", memberRefId)
				.setParameter("eventId", eventId));

		return cpd;
	}

	public CPDSummaryDto getCPDSummary(String memberRefId, Date startDate,
			Date endDate) {
		logger.info(" +++++ GET CPD SUMMARY FOR MEMBER +++++++ ");
		String sql = "select sum(cpdHours), status from cpd where "
				+ "memberRefId=:memberRefId and startDate>=:startDate "
				+ "and endDate<=:endDate group by status";

		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql)
				.setParameter("memberRefId", memberRefId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate));

		CPDSummaryDto summary = new CPDSummaryDto();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer count = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			String status = (value = row[i++]) == null ? null : value
					.toString();

			if (status != null && status.equals(CPDStatus.Unconfirmed.name())) {
				summary.setUnconfirmedCPD(count);
			} else {
				summary.setConfirmedCPD(count);
			}
		}

		return summary;
	}

	public CPDSummaryDto getCPDSummary(Date startDate, Date endDate) {
		logger.info(" +++++ GET ALL CPD COUNT GROUP BY STATUS +++++++ ");
		String sql = "select count(*), status from cpd where"
				+ " startDate>=:startDate and endDate<=:endDate group by status";
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql).setParameter("startDate", startDate)
				.setParameter("endDate", endDate));

		CPDSummaryDto summary = new CPDSummaryDto();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer count = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			String status = (value = row[i++]) == null ? null : value
					.toString();

			if (status != null && status.equals(CPDStatus.Unconfirmed.name())) {
				summary.setPendingCount(count);
			} else {
				summary.setProcessedCount(count);
			}
		}

		return summary;
	}

	public Double getCPDHours(String memberRefId) {
		String sql = "select sum(cpdhours) cpdhours from cpd "
				+ "where memberRefId= :memberRefId and status='Approved' limit 1 ";

		Number value = (Number) getSingleResultOrNull(getEntityManager()
				.createNativeQuery(sql)
				.setParameter("memberRefId", memberRefId));

		return value == null ? 0.0 : value.doubleValue();

	}

	public List<CPD> getAllCPDS(String memberRefId, Date startDate,
			Date endDate, Integer offset, Integer limit) {

		logger.debug("Member reg no ===<<>>==" + memberRefId);
		logger.debug("startDate ===<<>>==" + startDate);
		logger.debug("endDate ===<<>>==" + endDate);

		StringBuffer sql = new StringBuffer("FROM CPD");

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();

		if (startDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			}
			params.put("startDate", startDate);
			sql.append(" startDate>=:startDate");
		}

		if (endDate != null) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and");
			}
			params.put("endDate", endDate);
			sql.append(" endDate<=:endDate");
		}

		if (memberRefId != null && !memberRefId.equals("ALL")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("memberRefId", memberRefId);
			sql.append(" memberRefId=:memberRefId");
		}

		sql.append(" order by startDate desc");
		logger.debug("jpql= " + sql);
		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offset, limit);

	}

	public List<CPDDto> searchCPD(String searchTerm, Integer offset,
			Integer limit) {

		String sql = "select c.refId,c.startdate,c.enddate, concat(u.firstName,' ',u.lastName),"
				+ "c.title,c.organizer,c.category,c.cpdhours,c.status "
				+ "from cpd c "
				+ "inner join member m on (c.memberRefId=m.refId) "
				+ "inner join user u on (u.id=m.userId) "
				+ "where "
				+ "u.firstName like :searchTerm or "
				+ "u.lastName like :searchTerm or "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or "
				+ "c.title like :searchTerm or "
				+ "c.organizer like :searchTerm or "
				+ "c.memberRegistrationNo like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"searchTerm", "%" + searchTerm + "%");

		List<Object[]> rows = getResultList(query, offset, limit);

		List<CPDDto> cpdDtos = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString()
					.trim();
			Date startDate = (value = row[i++]) == null ? null : (Date) value;
			Date endDate = (value = row[i++]) == null ? null : (Date) value;
			String fullName = (value = row[i++]) == null ? null : value
					.toString();
			String title = (value = row[i++]) == null ? null : value.toString();
			String organizer = (value = row[i++]) == null ? null : value
					.toString();
			String category = (value = row[i++]) == null ? null : value
					.toString().trim();
			Double cpdHours = (value = row[i++]) == null ? null
					: (Double) value;
			String status = (value = row[i++]) == null ? null : value
					.toString().trim();

			CPDDto cpdDto = new CPDDto();
			cpdDto.setRefId(refId);
			cpdDto.setStartDate(startDate);
			cpdDto.setEndDate(endDate);
			cpdDto.setFullNames(fullName);
			cpdDto.setTitle(title);
			cpdDto.setOrganizer(organizer);

			if (category != null) {
				if (category.equals("CATEGORY_A")) {
					cpdDto.setCategory(CPDCategory.CATEGORY_A);
				}

				if (category.equals("CATEGORY_B")) {
					cpdDto.setCategory(CPDCategory.CATEGORY_B);
				}

				if (category.equals("CATEGORY_C")) {
					cpdDto.setCategory(CPDCategory.CATEGORY_C);
				}

				if (category.equals("CATEGORY_D")) {
					cpdDto.setCategory(CPDCategory.CATEGORY_D);
				}

				if (category.equals("NO_CATEGORY")) {
					cpdDto.setCategory(CPDCategory.NO_CATEGORY);
				}

			}

			cpdDto.setCpdHours(cpdHours);

			if (status != null) {
				if (category.equals("Unconfirmed")) {
					cpdDto.setStatus(CPDStatus.Unconfirmed);
				}

				if (category.equals("Approved")) {
					cpdDto.setStatus(CPDStatus.Approved);
				}

				if (category.equals("Rejected")) {
					cpdDto.setStatus(CPDStatus.Rejected);
				}

				if (category.equals("Cancelled")) {
					cpdDto.setStatus(CPDStatus.Cancelled);
				}
			}

			cpdDtos.add(cpdDto);
		}

		return cpdDtos;

	}

	public BigInteger cpdSearchCount(String searchTerm) {

		String sql = "select count(*) "
				+ "from cpd c "
				+ "inner join member m on (c.memberRefId=m.refId) "
				+ "inner join user u on (u.id=m.userId) "
				+ "where "
				+ "u.firstName like :searchTerm or "
				+ "u.lastName like :searchTerm or "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or "
				+ "c.title like :searchTerm or "
				+ "c.organizer like :searchTerm or c.memberRegistrationNo like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"searchTerm", "%" + searchTerm + "%");

		return getSingleResultOrNull(query);
	}
}
