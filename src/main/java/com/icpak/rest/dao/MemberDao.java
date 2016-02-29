package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MembershipStatus;

/**
 * 
 * @author duggan
 *
 */
public class MemberDao extends BaseDao {
	Logger logger = Logger.getLogger(MemberDao.class);

	public void createMember(Member member) {
		save(member);
	}

	public List<MemberDto> getAllMembers(Integer offSet, Integer limit, String searchTerm) {
		List<Object[]> rows = getResultList(
				getEntityManager()
						.createNativeQuery(
								"select u.refId as userRefId, u.email, u.firstName, u.lastName,u.fullName,u.title,"
										+ " m.memberNo, m.refId memberRefId,m.memberQrCode from Member m inner join user u on (m.userId=u.id) "
										+ " where (u.memberNo like :searchTerm or u.Name like :searchTerm or "
										+ "u.fullName like :searchTerm)")
						.setParameter("searchTerm", "%" + searchTerm + "%"),
				offSet, limit);

		List<MemberDto> memberList = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String userRefId = (value = row[i++]) == null ? null : value.toString();
			String email = (value = row[i++]) == null ? null : value.toString();
			String firstName = (value = row[i++]) == null ? null : value.toString();
			String lastName = (value = row[i++]) == null ? null : value.toString();
			String fullName = (value = row[i++]) == null ? null : value.toString();
			String title = (value = row[i++]) == null ? null : value.toString();
			String memberId = (value = row[i++]) == null ? null : value.toString();
			String memberRefId = (value = row[i++]) == null ? null : value.toString();
			String memberQrCode =(value = row[i++]) == null ? null : value.toString();

			MemberDto dto = new MemberDto();
			dto.setUserId(userRefId);
			dto.setMemberNo(memberId);
			dto.setRefId(memberRefId);
			dto.setFullName(fullName);
			dto.setEmail(email);
			dto.setFirstName(firstName);
			dto.setLastName(lastName);
			dto.setTitle(title);
			dto.setMemberQrCode(memberQrCode);
			memberList.add(dto);
		}

		return memberList;
	}

	public List<Member> getAllMembers(Integer offSet, Integer limit) {
		return getResultList(getEntityManager().createQuery("select m from Member m " + "where m.isActive=1"), offSet,
				limit);
	}

	public void updateMember(Member member) {
		createMember(member);
	}

	public int getMemberCount() {
		return getMemberCount(null);
	}

	public int getMemberCount(String roleId) {

		Number number = null;
		if (roleId == null) {
			number = getSingleResultOrNull(
					getEntityManager().createNativeQuery("select count(*) from Member " + "where isactive=1"));
		} else {
			number = getSingleResultOrNull(
					getEntityManager().createNativeQuery("select count(*) from Member u " + "where u.isactive=1"));
		}

		return number.intValue();
	}

	public String getGoodStandingCertDocNumber(Long id) {

		return getSingleResultOrNull(
				getEntityManager().createNativeQuery("select documentNo from goodstandingcertificate where id=:id")
						.setParameter("id", id));
	}

	/*
	 * To be used by the membersPresenter for website iframe
	 */

	public List<MemberDto> getMembers(Integer offSet, Integer limit) {

		logger.info(" GETTING MEMBERS ");

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select concat(u.firstName,' ',u.lastName),u.email,m.memberShipStatus,m.refId,m.memberNo,a.`Customer Type` "
				+ "from " + "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo)";

		Query query = getEntityManager().createNativeQuery(sql);

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String fullName = (value = row[i++]) == null ? null : value.toString();
			String email = (value = row[i++]) == null ? null : value.toString();
			MembershipStatus memberShipStatus = (value = row[i++]) == null ? null
					: MembershipStatus.valueOf(value.toString());
			String refId = (value = row[i++]) == null ? null : value.toString();
			String memberNo = (value = row[i++]) == null ? null : value.toString();
			ApplicationType customerType = (value = row[i++]) == null ? null
					: ApplicationType.valueOf(value.toString());

			MemberDto memberDto = new MemberDto();

			memberDto.setFullName(fullName);
			memberDto.setEmail(email);
			memberDto.setMembershipStatus(memberShipStatus);
			memberDto.setRefId(refId);
			memberDto.setMemberNo(memberNo);
			memberDto.setCustomerType(customerType);
			memberDto.setMember("MEMBER");

			memberDtos.add(memberDto);
		}

		return memberDtos;

	}

	public List<MemberDto> getMembersForschedular(Integer offSet, Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select u.email,m.refId,m.memberNo " + "from " + "member m inner join user u on (m.userId=u.id)";

		Query query = getEntityManager().createNativeQuery(sql);

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String email = (value = row[i++]) == null ? null : value.toString();
			String refId = (value = row[i++]) == null ? null : value.toString();
			String memberNo = (value = row[i++]) == null ? null : value.toString();
			MemberDto memberDto = new MemberDto();

			memberDto.setEmail(email);
			memberDto.setRefId(refId);
			memberDto.setMemberNo(memberNo);

			memberDtos.add(memberDto);
		}

		return memberDtos;

	}

	public Integer getMembersCount(String searchTerm, String citySearchTerm, String categoryName) {
		StringBuffer sql = new StringBuffer("select count(*) from " 
	            + " member m inner join user u on (m.userId=u.id) "
				+ "inner join allicpakmembers_5 a on (a.No_=m.memberNo)");
		Map<String, Object> params = appendParameters(searchTerm, citySearchTerm, categoryName, sql);
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		Number number = getSingleResultOrNull(query);
		return number.intValue();

	}

	public List<MemberDto> searchMembersFromOldTable(String searchTerm, String citySearchTerm, String categoryName,
			Integer offSet, Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		StringBuffer sql = new StringBuffer("select m.memberNo,u.fullName,m.customerType,m.memberShipStatus " + "from "
				+ "member m inner join user u on (m.userId=u.id) "
				+ "inner join allicpakmembers_5 a on (a.No_=m.memberNo)");
		Map<String, Object> params = appendParameters(searchTerm, citySearchTerm, categoryName, sql);
		Query query = getEntityManager().createNativeQuery(sql.toString());

		if (!params.isEmpty()) {
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}

		List<Object[]> rows = getResultList(query, offSet, limit);
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;

			String memberNo = (value = row[i++]) == null ? null : value.toString();
			String fullName = (value = row[i++]) == null ? null : value.toString();
			String customerType = (value = row[i++]) == null ? null : value.toString();
			String status = (value = row[i++]) == null ? null : value.toString();

			MemberDto memberDto = new MemberDto();

			memberDto.setMemberNo(memberNo == null ? "Not Available" : memberNo);
			memberDto.setFullName(fullName);
			// Customer Type
			if (customerType != null) {
				if (customerType.equals("MEMBER")) {
					memberDto.setCustomerType(ApplicationType.NON_PRACTISING);
				} else if (customerType.equals("PRACTICING RT")) {
					memberDto.setCustomerType(ApplicationType.PRACTISING_RT);
				} else if (customerType.equals("PRAC MEMBER")) {
					memberDto.setCustomerType(ApplicationType.PRACTISING);
				} else if (customerType.equals("FOREIGN")) {
					memberDto.setCustomerType(ApplicationType.OVERSEAS);
				} else if (customerType.equals("RETIRED")) {
					memberDto.setCustomerType(ApplicationType.RETIRED);
				} else {
					memberDto.setCustomerType(ApplicationType.NON_PRACTISING);
				}
			}

			if (status != null) {
				memberDto.setMembershipStatus(MembershipStatus.valueOf(status.toUpperCase()));
			}
			memberDtos.add(memberDto);
		}

		return memberDtos;

	}

	public List<MemberDto> searchMembers(String searchTerm, Integer offSet, Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select m.memberNo,concat(u.firstName,' ',u.lastName),m.memberShipStatus,a.`Customer Type` "
				+ "from " + "member m inner join user u on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo) "
				+ "where member where memberNo IS NOT NULL and "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or " + "m.memberShipStatus like :searchTerm or "
				+ "a.`Customer Type` like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%");

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String memberNo = (value = row[i++]) == null ? null : value.toString();
			String fullName = (value = row[i++]) == null ? null : value.toString();
			MembershipStatus memberShipStatus = (value = row[i++]) == null ? null
					: MembershipStatus.valueOf(value.toString());
			ApplicationType customerType = (value = row[i++]) == null ? null
					: ApplicationType.valueOf(value.toString());

			MemberDto memberDto = new MemberDto();

			memberDto.setFullName(fullName);
			memberDto.setMembershipStatus(memberShipStatus);
			memberDto.setCustomerType(customerType);
			memberDto.setMember("MEMBER");

			memberDtos.add(memberDto);
		}

		return memberDtos;

	}

	public Integer getMembersSearchCount(String searchTerm) {

		String sql = "select count(*) " + "from " + "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo) " + "where "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or " + "m.memberShipStatus like :searchTerm or "
				+ "a.`Customer Type` like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%");

		Number number = getSingleResultOrNull(query);

		return number.intValue();

	}

	/**
	 * 
	 * 
	 * @param filter
	 * @param sqlQuery
	 * @return Filter parameter values
	 */
	private Map<String, Object> appendParameters(String searchTerm, String citySearchTerm, String categoryName,
			StringBuffer sqlQuery) {
		Map<String, Object> params = new HashMap<>();

		if (!searchTerm.equals("all") && !citySearchTerm.equals("all") && !categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec T T T ");
			sqlQuery.append(" where ");
			sqlQuery.append(" u.fullName like :searchTerm or ");
			sqlQuery.append(" m.memberNo like :searchTerm and ");
			sqlQuery.append(" (a.Address like :citySearchTerm or ");
			sqlQuery.append(" a.City like :citySearchTerm) and ");
			sqlQuery.append(" (m.customerType=:categoryName)");
			params.put("searchTerm", "%" + searchTerm + "%");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
			params.put("categoryName", categoryName);
		}

		if (!searchTerm.equals("all") && !citySearchTerm.equals("all") && categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec T T F ");
			sqlQuery.append(" where ");
			sqlQuery.append(" u.fullName like :searchTerm or ");
			sqlQuery.append(" m.memberNo like :searchTerm and ");
			sqlQuery.append(" (a.Address like :citySearchTerm or ");
			sqlQuery.append(" a.City like :citySearchTerm)");
			params.put("searchTerm", "%" + searchTerm + "%");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
		}

		if (!searchTerm.equals("all") && citySearchTerm.equals("all") && categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec T F F ");
			sqlQuery.append(" where ");
			sqlQuery.append(" u.fullName like :searchTerm or ");
			sqlQuery.append(" m.memberNo like :searchTerm");
			params.put("searchTerm", "%" + searchTerm + "%");
		}

		if (searchTerm.equals("all") && !citySearchTerm.equals("all") && !categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec F T T ");
			sqlQuery.append(" where ");
			sqlQuery.append(" a.Address like :citySearchTerm or ");
			sqlQuery.append(" a.City like :citySearchTerm and ");
			sqlQuery.append(" (m.customerType=:categoryName)");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
			params.put("categoryName", categoryName);
		}

		if (searchTerm.equals("all") && citySearchTerm.equals("all") && !categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec F F T ");
			sqlQuery.append(" where ");
			sqlQuery.append(" m.customerType=:categoryName");
			params.put("categoryName", categoryName);
		}

		if (searchTerm.equals("all") && !citySearchTerm.equals("all") && categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec F T F ");
			sqlQuery.append(" where ");
			sqlQuery.append(" (a.Address like :citySearchTerm or ");
			sqlQuery.append(" a.City like :citySearchTerm)");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
		}

		return params;
	}

	public Member getByMemberNo(String memberNo, boolean throwExceptionIfNull) {
		Member member = getSingleResultOrNull(getEntityManager().createQuery("from Member m where m.memberNo=:memberNo")
				.setParameter("memberNo", memberNo));

		if (throwExceptionIfNull && member == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Member", "'" + memberNo + "'");
		}

		return member;
	}

	public Member getByMemberNo(String memberNo) {
		return getByMemberNo(memberNo, true);
	}

	public List<String> getAllMemberNumbers(int offset, int limit) {
		String mememberNos = "select m.refId from member m where m.memberNo is not null";
		Query query = getEntityManager().createNativeQuery(mememberNos);
		return getResultList(query, offset, limit);

	}

	public Integer getMembersCount() {
		String count = "select count(*) from member where memberNo is not null";

		Query query = getEntityManager().createNativeQuery(count);

		Number number = getSingleResultOrNull(query);

		return number.intValue();
	}

	public Member findByMemberNo(String memberNo) {
		String sql = "FROM Member where memberNo=:memberNo";

		Query query = getEntityManager().createQuery(sql).setParameter("memberNo", memberNo);

		return getSingleResultOrNull(query);
	}

}