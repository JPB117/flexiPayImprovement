package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

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

	public List<MemberDto> getAllMembers(Integer offSet, Integer limit,
			String searchTerm) {
		List<Object[]> rows = getResultList(
				getEntityManager()
						.createNativeQuery(
								"select u.refId userRefId, u.email, u.firstName, u.lastName, u.title,"
										+ " m.memberNo, m.refId memberRefId from Member m inner join user u on (m.userId=u.id) "
										+ "where (u.memberNo like :searchTerm or u.Name like :searchTerm)")
						.setParameter("searchTerm", "%" + searchTerm + "%"),
				offSet, limit);

		List<MemberDto> memberList = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String userRefId = (value = row[i++]) == null ? null : value
					.toString();
			String email = (value = row[i++]) == null ? null : value.toString();
			String firstName = (value = row[i++]) == null ? null : value
					.toString();
			String lastName = (value = row[i++]) == null ? null : value
					.toString();
			String title = (value = row[i++]) == null ? null : value.toString();
			String memberId = (value = row[i++]) == null ? null : value
					.toString();
			String memberRefId = (value = row[i++]) == null ? null : value
					.toString();

			MemberDto dto = new MemberDto();
			dto.setUserId(userRefId);
			dto.setMemberNo(memberId);
			dto.setRefId(memberRefId);
			dto.setEmail(email);
			dto.setFirstName(firstName);
			dto.setLastName(lastName);
			dto.setTitle(title);
			memberList.add(dto);
		}

		return memberList;
	}

	public List<Member> getAllMembers(Integer offSet, Integer limit) {
		return getResultList(getEntityManager().createQuery(
				"select m from Member m " + "where m.isActive=1"), offSet,
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
			number = getSingleResultOrNull(getEntityManager()
					.createNativeQuery(
							"select count(*) from Member " + "where isactive=1"));
		} else {
			number = getSingleResultOrNull(getEntityManager()
					.createNativeQuery(
							"select count(*) from Member u "
									+ "where u.isactive=1"));
		}

		return number.intValue();
	}

	public String getGoodStandingCertDocNumber(Long id) {

		return getSingleResultOrNull(getEntityManager().createNativeQuery(
				"select documentNo from goodstandingcertificate where id=:id")
				.setParameter("id", id));
	}

	/*
	 * To be used by the membersPresenter for website iframe
	 */

	public List<MemberDto> getMembers(Integer offSet, Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select concat(u.firstName,' ',u.lastName),m.memberShipStatus,a.`Customer Type` "
				+ "from "
				+ "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo)";

		Query query = getEntityManager().createNativeQuery(sql);

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String fullName = (value = row[i++]) == null ? null : value
					.toString();
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

	public Integer getMembersCount(String searchTerm, String citySearchTerm,
			String categoryName) {
		StringBuffer sql = new StringBuffer("select count(*) "
				+ " from allicpakmembers_5 m");
		Map<String, Object> params = appendParameters(searchTerm,
				citySearchTerm, categoryName, sql);
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}
		Number number = getSingleResultOrNull(query);
		return number.intValue();

	}

	public List<MemberDto> searchMembersFromOldTable(String searchTerm,
			String citySearchTerm, String categoryName, Integer offSet,
			Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		StringBuffer sql = new StringBuffer(
				"select m.No_,m.Name,m.Address,m.City,"
						+ "m.`Customer Posting Group`,m.`Customer Type`,"
						+ "m.Status,m.PractisingNo "
						+ " from allicpakmembers_5 m ");
		Map<String, Object> params = appendParameters(searchTerm,
				citySearchTerm, categoryName, sql);
		Query query = getEntityManager().createNativeQuery(sql.toString());
		
		if(!params.isEmpty()){
			for (String key : params.keySet()) {
				query.setParameter(key, params.get(key));
			}
		}

		List<Object[]> rows = getResultList(query, offSet, limit);
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;

			String memberNo = (value = row[i++]) == null ? null
					: value.toString();
			String fullName = (value = row[i++]) == null ? null : value
					.toString();
			String address = (value = row[i++]) == null ? null : value
					.toString();
			String city = (value = row[i++]) == null ? null : value.toString();
			String customerPostingGroup = (value = row[i++]) == null ? null
					: value.toString();
			String customerType = (value = row[i++]) == null ? null : value
					.toString();
			String status = (value = row[i++]) == null ? null
					: value.toString();
			String practisingNo = (value = row[i++]) == null ? null : value
					.toString();
			MemberDto memberDto = new MemberDto();
			memberDto.setMemberNo(memberNo);
			memberDto.setFullName(fullName);
			memberDto.setAddress(address);
			memberDto.setCity(city);
			// Customer Type
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

			// Set Membership Status
			if (status.equals("1")) {
				memberDto.setMembershipStatus(MembershipStatus.ACTIVE);
			} else {
				memberDto.setMembershipStatus(MembershipStatus.INACTIVE);
			}
			memberDto.setPractisingNo(practisingNo);
			memberDto.setCustomerPostingGroup(customerPostingGroup);
			memberDtos.add(memberDto);
		}

		return memberDtos;

	}

	public List<MemberDto> searchMembers(String searchTerm, Integer offSet,
			Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select m.memberNo,concat(u.firstName,' ',u.lastName),m.memberShipStatus,a.`Customer Type` "
				+ "from "
				+ "member m inner join user u on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo) "
				+ "where member where memberNo IS NOT NULL and "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or "
				+ "m.memberShipStatus like :searchTerm or "
				+ "a.`Customer Type` like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"searchTerm", "%" + searchTerm + "%");

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String fullName = (value = row[i++]) == null ? null : value
					.toString();
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

		String sql = "select count(*) "
				+ "from "
				+ "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo) "
				+ "where "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or "
				+ "m.memberShipStatus like :searchTerm or "
				+ "a.`Customer Type` like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"searchTerm", "%" + searchTerm + "%");

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
	private Map<String, Object> appendParameters(String searchTerm,
			String citySearchTerm, String categoryName, StringBuffer sqlQuery) {
		boolean isFirst = true;
		Map<String, Object> params = new HashMap<>();

		if (!searchTerm.equals("all") && !citySearchTerm.equals("all") && !categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec T T T ");
			sqlQuery.append(" where ");
			sqlQuery.append(" m.Name like :searchTerm or ");
			sqlQuery.append(" m.No_ like :searchTerm and ");
			sqlQuery.append(" (m.Address like :citySearchTerm or ");
			sqlQuery.append(" m.City like :citySearchTerm) and ");
			sqlQuery.append(" (m.`Customer Type`=:categoryName)");
			params.put("searchTerm", "%" + searchTerm + "%");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
			params.put("categoryName", categoryName);
		}
		
		if (!searchTerm.equals("all") && !citySearchTerm.equals("all") && categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec T T F ");
			sqlQuery.append(" where ");
			sqlQuery.append(" m.Name like :searchTerm or ");
			sqlQuery.append(" m.No_ like :searchTerm and ");
			sqlQuery.append(" (m.Address like :citySearchTerm or ");
			sqlQuery.append(" m.City like :citySearchTerm)");
			params.put("searchTerm", "%" + searchTerm + "%");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
		}
		
		if (!searchTerm.equals("all") && citySearchTerm.equals("all") && categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec T F F ");
			sqlQuery.append(" where ");
			sqlQuery.append(" m.Name like :searchTerm or ");
			sqlQuery.append(" m.No_ like :searchTerm");
			params.put("searchTerm", "%" + searchTerm + "%");
		}
		
		if (searchTerm.equals("all") && !citySearchTerm.equals("all") && !categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec F T T ");
			sqlQuery.append(" where ");
			sqlQuery.append(" m.Address like :citySearchTerm or ");
			sqlQuery.append(" m.City like :citySearchTerm and ");
			sqlQuery.append(" (m.`Customer Type`=:categoryName)");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
			params.put("categoryName", categoryName);
		}
		
		if (searchTerm.equals("all") && citySearchTerm.equals("all") && !categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec F F T ");
			sqlQuery.append(" where ");
			sqlQuery.append(" m.`Customer Type`=:categoryName");
			params.put("categoryName", categoryName);
		}
		
		if (searchTerm.equals("all") && !citySearchTerm.equals("all") && categoryName.equals("all")) {
			logger.error(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> exec F T F ");
			sqlQuery.append(" where ");
			sqlQuery.append(" (m.Address like :citySearchTerm or ");
			sqlQuery.append(" m.City like :citySearchTerm)");
			params.put("citySearchTerm", "%" + citySearchTerm + "%");
		}

		return params;
	}

}