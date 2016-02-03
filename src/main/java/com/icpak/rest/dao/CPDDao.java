package com.icpak.rest.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.ApplicationSettings;
import com.workpoint.icpak.shared.model.AttachmentDto;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDFooterDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.MemberCPDDto;

/**
 * 
 * @author duggan
 *
 */
@Transactional
public class CPDDao extends BaseDao {
	Logger logger = Logger.getLogger(CPDDao.class);

	@Inject
	ApplicationSettings settings;

	public void createCPD(CPD cpd) {
		save(cpd);
	}

	public List<CPDDto> getAllCPDs(Integer offSet, Integer limit,
			Date startDate, Date endDate) {
		return getAllCPDs(null, offSet, limit, startDate, endDate, null);
	}

	public List<CPDDto> getAllCPDs(String passedMemberRefId, Integer offSet,
			Integer limit, Date startDate, Date endDate, String searchTerm) {
		logger.info(" +++ GET CPPD FOR +++++ REFID == " + passedMemberRefId
				+ " ++SearchTerm>>>>>" + searchTerm);
		StringBuffer sql = new StringBuffer(
				"select c.id,c.created,c.refId as cpdRefId,"
						+ "c.title,c.status,c.memberRegistrationNo,c.memberRefId, "
						+ "u.fullName,c.startDate,c.endDate,c.cpdHours "
						+ "from cpd c "
						+ "inner join member m on (c.memberRefId=m.refId) "
						+ "inner join user u on (u.id=m.userId) ");

		Map<String, Object> params = appendParameters(sql, passedMemberRefId,
				startDate, endDate, searchTerm);

		if (passedMemberRefId != null
				&& (passedMemberRefId.equals("ALLRETURNS") || passedMemberRefId
						.equals("cpdReturns"))) {
			sql.append(" order by c.created asc");
		} else {
			sql.append(" order by c.startDate desc");
		}

		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		logger.info(" +++ EXECUTING cpd query +++" + sql);

		List<Object[]> rows = getResultList(query, offSet, limit);

		List<CPDDto> cpds = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer id = (value = row[i++]) == null ? null
					: ((BigInteger) value).intValue();
			Date created = (value = row[i++]) == null ? null : (Date) value;
			String refId = (value = row[i++]) == null ? null : value.toString();
			String title = (value = row[i++]) == null ? null : value.toString();
			String status = (value = row[i++]) == null ? null : value
					.toString();
			String memberRegNo = (value = row[i++]) == null ? null : value
					.toString();
			String memberRefId = (value = row[i++]) == null ? null : value
					.toString();
			String fullNames = (value = row[i++]) == null ? null : value
					.toString();
			Date dbStartDate = (value = row[i++]) == null ? null : (Date) value;
			Date dbEndDate = (value = row[i++]) == null ? null : (Date) value;
			Double cpdHours = (value = row[i++]) == null ? null
					: (Double) value;
			CPDDto cpd = new CPDDto();
			cpd.setId(id);
			cpd.setRefId(refId);
			cpd.setCreated(created);
			cpd.setTitle(title);
			cpd.setStatus(CPDStatus.valueOf(status));
			cpd.setMemberRegistrationNo(memberRegNo);
			cpd.setMemberRefId(memberRefId);
			cpd.setFullNames(fullNames);
			cpd.setStartDate(dbStartDate);
			cpd.setEndDate(dbEndDate);
			cpd.setCpdHours(cpdHours);
			cpds.add(cpd);
		}
		return cpds;
	}

	// get All members and Years Summary
	public List<MemberCPDDto> getMemberCPD(String searchTerm, Integer offset,
			Integer limit) {
		logger.info(" +++ GETTING MEMBER CPD for searchTerm +++" + searchTerm);
		StringBuffer sql = new StringBuffer(
				"select m.refId,n.No_,n.Name,n.`customer Type`,n.status,"
						+ "n.Year2016,n.Year2015,n.Year2014,n.Year2013,n.Year2012,n.Year2011 "
						+ "from navmember n "
						+ "INNER JOIN member m ON (n.No_=m.memberNo)");

		Map<String, Object> params = appendParameters(sql, "memberCPD", null,
				null, searchTerm);
		sql.append(" order by n.No_ asc");
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		logger.info(" +++ executing member cpd query +++" + sql);

		List<Object[]> rows = getResultList(query, offset, limit);
		List<MemberCPDDto> memberCPDs = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String memberRefId = (value = row[i++]) == null ? null : value
					.toString();
			String memberNo = (value = row[i++]) == null ? null : value
					.toString();
			String memberNames = (value = row[i++]) == null ? null : value
					.toString();
			String customerType = (value = row[i++]) == null ? null : value
					.toString();
			String status = (value = row[i++]) == null ? null : value
					.toString();
			Double year2016 = (value = row[i++]) == null ? null
					: (Double) value;
			Double year2015 = (value = row[i++]) == null ? null
					: (Double) value;
			Double year2014 = (value = row[i++]) == null ? null
					: (Double) value;
			Double year2013 = (value = row[i++]) == null ? null
					: (Double) value;
			Double year2012 = (value = row[i++]) == null ? null
					: (Double) value;
			Double year2011 = (value = row[i++]) == null ? null
					: (Double) value;
			MemberCPDDto memberCPDDto = new MemberCPDDto();
			memberCPDDto.setRefId(memberRefId);
			memberCPDDto.setMemberNo(memberNo);
			memberCPDDto.setMemberNames(memberNames);
			memberCPDDto.setCustomerType(customerType);
			memberCPDDto.setStatus(status);
			memberCPDDto.setYear2016(year2016);
			memberCPDDto.setYear2015(year2015);
			memberCPDDto.setYear2014(year2014);
			memberCPDDto.setYear2013(year2013);
			memberCPDDto.setYear2012(year2012);
			memberCPDDto.setYear2011(year2011);

			memberCPDs.add(memberCPDDto);
		}
		return memberCPDs;
	}

	private Map<String, Object> appendParameters(StringBuffer sql,
			String passedMemberRefId, Date startDate, Date endDate,
			String searchTerm) {
		Map<String, Object> params = new HashMap<>();
		boolean isFirstParam = true;

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

		if (passedMemberRefId != null
				&& !passedMemberRefId.equals("ALLRETURNS")
				&& !passedMemberRefId.equals("ALLARCHIVE")
				&& !passedMemberRefId.equals("cpdReturns")
				&& !passedMemberRefId.equals("returnArchive")
				&& !passedMemberRefId.equals("memberCPD")) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("memberRefId", passedMemberRefId);
			sql.append(" c.memberRefId=:memberRefId");

		}
		if (passedMemberRefId != null
				&& (passedMemberRefId.equals("ALLRETURNS") || passedMemberRefId
						.equals("cpdReturns"))) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("status", "Unconfirmed");
			sql.append(" c.status=:status");

			if (searchTerm != null) {
				if (!searchTerm.equals("")) {

					sql.append(" and (u.fullName like :searchTerm or"
							+ " c.title like :searchTerm or "
							+ " c.memberRegistrationNo like :searchTerm or "
							+ " c.id like :searchTerm)");

					params.put("searchTerm", "%" + searchTerm + "%");
				}
			}
		}

		if (passedMemberRefId != null
				&& (passedMemberRefId.equals("ALLARCHIVE") || passedMemberRefId
						.equals("returnArchive"))) {
			if (isFirstParam) {
				isFirstParam = false;
				sql.append(" where");
			} else {
				sql.append(" and ");
			}
			params.put("status", "Unconfirmed");
			sql.append(" c.status <> :status");

			if (searchTerm != null) {
				if (!searchTerm.equals("")) {
					sql.append(" and (u.fullName like :searchTerm or"
							+ " c.title like :searchTerm or "
							+ " c.memberRegistrationNo like :searchTerm or "
							+ " c.id like :searchTerm)");

					params.put("searchTerm", "%" + searchTerm + "%");
				}
			}
		}

		if (passedMemberRefId != null && passedMemberRefId.equals("memberCPD")) {
			if (searchTerm != null) {
				if (!searchTerm.equals("")) {
					sql.append(" and (No_ like :searchTerm or"
							+ " n.Name like :searchTerm)");
					params.put("searchTerm", "%" + searchTerm + "%");
				}
			}
		}

		return params;
	}

	public List<AttachmentDto> getAllAttachment(Long id) {
		logger.info(" +++ GET Attachments FOR +++++ cpdId == " + id);
		StringBuffer sql = new StringBuffer(
				"select refId,name from attachment where cpdid=:passedId");
		Query query = getEntityManager().createNativeQuery(sql.toString())
				.setParameter("passedId", id);
		List<Object[]> rows = getResultList(query, 0, 1000);
		List<AttachmentDto> attachmentDtos = new ArrayList<>();
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString();
			String name = (value = row[i++]) == null ? null : value.toString();
			AttachmentDto attachment = new AttachmentDto();
			attachment.setRefId(refId);
			attachment.setAttachmentName(name);
			attachmentDtos.add(attachment);
		}
		logger.info(" Found this number of attachments == "
				+ attachmentDtos.size());
		return attachmentDtos;
	}

	public void updateCPD(CPD cpd) {
		createCPD(cpd);
	}

	public int getCPDCount(String memberRefId, Date startDate, Date endDate) {
		logger.info(" +++ GET RESULT COUNT FOR CPD +++++ REFID == "
				+ memberRefId);
		Number number = null;
		if (memberRefId != null) {
			logger.info(" +++ COUNT FOR MEMBER ++++ ");

			StringBuffer sql = new StringBuffer("select count(*) from cpd c ");

			Map<String, Object> params = appendParameters(sql, memberRefId,
					startDate, endDate, null);

			Query query = getEntityManager().createNativeQuery(sql.toString());
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
			number = getSingleResultOrNull(query);

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
		logger.info(" +++++ GET CPD Summary for Administrator");
		CPDSummaryDto summary = new CPDSummaryDto();
		summary.setTotalArchive(getCPDCount("ALLARCHIVE", startDate, endDate));
		summary.setTotalReturns(getCPDCount("ALLRETURNS", startDate, endDate));
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

		sql.append(" order by created desc");
		logger.debug("jpql= " + sql);
		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offset, limit);

	}

	public List<CPDDto> searchCPD(String memberId, String searchTerm,
			Integer offset, Integer limit, Date passedStartDate,
			Date passedEndDate) {
		StringBuffer sql = new StringBuffer(
				"select c.created,c.refId as cpdRefId,"
						+ "c.title,c.status,c.memberRegistrationNo,c.memberRefId, "
						+ "concat(u.firstName,' ',u.lastName),c.startDate,c.endDate,c.cpdHours "
						+ "from cpd c "
						+ "inner join member m on (c.memberRefId=m.refId) "
						+ "inner join user u on (u.id=m.userId) "
						+ "where "
						+ "u.firstName like :searchTerm or "
						+ "u.lastName like :searchTerm or "
						+ "concat(u.firstName,' ',u.lastName) like :searchTerm or "
						+ "c.title like :searchTerm or "
						+ "c.memberRegistrationNo=:memberNoSearch ");

		boolean isFirstParam = false;
		Map<String, Object> params = appendParameters(sql, memberId,
				passedStartDate, passedEndDate, searchTerm);
		Query query = getEntityManager().createNativeQuery(sql.toString())
				.setParameter("searchTerm", "%" + searchTerm + "%")
				.setParameter("memberNoSearch", searchTerm);
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

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
			String memberRegistrationNo = (value = row[i++]) == null ? null
					: value.toString().trim();
			String memberRefId = (value = row[i++]) == null ? null : value
					.toString().trim();

			CPDDto cpdDto = new CPDDto();
			cpdDto.setRefId(refId);
			cpdDto.setStartDate(startDate);
			cpdDto.setEndDate(endDate);
			cpdDto.setFullNames(fullName);
			cpdDto.setTitle(title);
			cpdDto.setOrganizer(organizer);
			cpdDto.setMemberRefId(memberRefId);
			cpdDto.setMemberRegistrationNo(memberRegistrationNo);

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
				if (status.equals("Unconfirmed")) {
					cpdDto.setStatus(CPDStatus.Unconfirmed);
				}

				if (status.equals("Approved")) {
					cpdDto.setStatus(CPDStatus.Approved);
				}

				if (status.equals("Rejected")) {
					cpdDto.setStatus(CPDStatus.Rejected);
				}

				if (status.equals("Cancelled")) {
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

	public BigInteger getMemberCPDCount(String searchTerm) {

		String sql = "select count(*) from navmember n where (No_ like :searchTerm or Name like :searchTerm)";
		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"searchTerm", "%" + searchTerm + "%");

		return getSingleResultOrNull(query);
	}

	public List<CPDFooterDto> getYearSummaries(String memberId, Date startDate,
			Date endDate) {
		Member member = findByRefId(memberId, Member.class);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);

		Calendar endCalendar = Calendar.getInstance();
		startCalendar.setTime(startDate);

		int startYear = startCalendar.get(Calendar.YEAR);
		int endYear = endCalendar.get(Calendar.YEAR);

		// We have Records Upto 2011, so nothing below this Year
		if (startYear < 2011) {
			startYear = 2011;
		}
		String sql = "select n.Year2016,n.Year2015,n.Year2014,n.Year2013,n.Year2012,n.Year2011 "
				+ "from navmember n where No_=:memberNo";
		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"memberNo", member.getMemberNo());
		Object[] row = getSingleResultOrNull(query);
		List<CPDFooterDto> cpdFooters = new ArrayList<>();

		if (row == null) {
			return cpdFooters;
		}
		int i = 0;
		Object value = null;
		Double year2016 = (value = row[i++]) == null ? null : (Double) value;
		Double year2015 = (value = row[i++]) == null ? null : (Double) value;
		Double year2014 = (value = row[i++]) == null ? null : (Double) value;
		Double year2013 = (value = row[i++]) == null ? null : (Double) value;
		Double year2012 = (value = row[i++]) == null ? null : (Double) value;
		Double year2011 = (value = row[i++]) == null ? null : (Double) value;

		Map<String, Double> allValues = new HashMap<>();
		allValues.put("Year2016", year2016);
		allValues.put("Year2015", year2015);
		allValues.put("Year2014", year2014);
		allValues.put("Year2013", year2013);
		allValues.put("Year2012", year2012);
		allValues.put("Year2011", year2011);

		Double grandTotal = 0.0;
		for (int start = startYear; start <= endYear; start++) {
			String description = "TOTAL CREDIT UNITS FOR " + start;
			Double total = allValues.get("Year" + start);
			grandTotal = grandTotal + total;
			if (total != 0.0) {
				CPDFooterDto cpdFooter = new CPDFooterDto();
				cpdFooter.setDescription(description);
				cpdFooter.setCpdUnits(total);
				cpdFooters.add(cpdFooter);
			}
		}

		CPDFooterDto grandTotalDto = new CPDFooterDto();
		grandTotalDto.setDescription("TOTAL CREDIT UNITS");
		grandTotalDto.setCpdUnits(grandTotal);
		cpdFooters.add(grandTotalDto);
		return cpdFooters;
	}

	public Integer getAllAttachmentCount() {
		String count = "select count(*) from attachment where attachment is not null";

		Query query = getEntityManager().createNativeQuery(count);

		Number number = getSingleResultOrNull(query);

		return number.intValue();
	}

	public List<Attachment> getAllCPDAttachment(int offset, int limit) {

		String sql = "From Attachment WHERE attachment is not null";

		Query query = getEntityManager().createQuery(sql);

		return getResultList(query, offset, limit);
	}

	public void dumpBlobToFile() {

		logger.info(" DUMPING TO BLOB ");

		Integer count = getAllAttachmentCount();

		logger.info(" TOTAL COUNT " + count);

		int offset = 0;
		int limit = 50;

		int trips = (count / limit) + 1;

		while (trips > 0) {

			logger.info(" TRIP = " + trips);
			logger.info(" Offset = " + offset);

			for (Attachment attachment : getAllCPDAttachment(offset, limit)) {
				if (attachment.getAttachment() != null) {
					String fileName = getFilePath() + File.separator
							+ attachment.getRefId() + "-CPD-"
							+ attachment.getName();
					attachment.setFileName(fileName);

					try {
						IOUtils.write(attachment.getAttachment(),
								new FileOutputStream(new File(fileName)));
					} catch (IOException e) {
						e.printStackTrace();
					}

					logger.info(" SAVING ATTACHMENT " + fileName);

					attachment.setAttachment(null);

					String query = "UPDATE attachment SET "
							+ "fileName=:fileName,attachment=:attachment "
							+ "WHERE id=:id";

					Query query2 = getEntityManager().createNativeQuery(query)
							.setParameter("fileName", fileName)
							.setParameter("attachment", null)
							.setParameter("id", attachment.getId());

					query2.executeUpdate();

					logger.info(" ATTACHMENT SAVED ");
				}
			}

			offset = offset + limit + 1;

			trips--;
		}

	}

	private String getFilePath() {
		return settings.getProperty("upload_path");
	}
}
