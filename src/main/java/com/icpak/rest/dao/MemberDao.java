package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MembershipStatus;

/**
 * 
 * @author duggan
 *
 */
public class MemberDao extends BaseDao {

	public void createMember(Member member) {
		save(member);
	}

	public List<MemberDto> getAllMembers(Integer offSet, Integer limit, String searchTerm) {
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery("select u.refId userRefId, u.email, u.firstName, u.lastName, u.title,"
						+ " m.memberNo, m.refId memberRefId from Member m inner join user u on (m.userId=u.id) "
						+ "where (u.memberNo like :searchTerm or u.Name like :searchTerm)")
				.setParameter("searchTerm", "%" + searchTerm + "%"), offSet, limit);

		List<MemberDto> memberList = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String userRefId = (value = row[i++]) == null ? null : value.toString();
			String email = (value = row[i++]) == null ? null : value.toString();
			String firstName = (value = row[i++]) == null ? null : value.toString();
			String lastName = (value = row[i++]) == null ? null : value.toString();
			String title = (value = row[i++]) == null ? null : value.toString();
			String memberId = (value = row[i++]) == null ? null : value.toString();
			String memberRefId = (value = row[i++]) == null ? null : value.toString();

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

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select concat(u.firstName,' ',u.lastName),m.memberShipStatus,a.`Customer Type` " + "from "
				+ "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo)";

		Query query = getEntityManager().createNativeQuery(sql);

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String fullName = (value = row[i++]) == null ? null : value.toString();
			MembershipStatus memberShipStatus = (value = row[i++]) == null ? null
					: MembershipStatus.valueOf(value.toString());
			String customerType = (value = row[i++]) == null ? null : value.toString();

			MemberDto memberDto = new MemberDto();

			memberDto.setFullName(fullName);
			memberDto.setMembershipStatus(memberShipStatus);
			memberDto.setCustomerType(customerType);
			memberDto.setMember("MEMBER");

			memberDtos.add(memberDto);
		}

		return memberDtos;

	}
	
	public Integer getMembersCount() {

		String sql = "select count(*) " 
		        + "from "
				+ "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo)";

		Query query = getEntityManager().createNativeQuery(sql);
		
		Number number = getSingleResultOrNull(query);
		return number.intValue();

	}

	public List<MemberDto> searchMembers(String searchTerm , Integer offSet, Integer limit) {

		List<MemberDto> memberDtos = new ArrayList<>();

		String sql = "select concat(u.firstName,' ',u.lastName),m.memberShipStatus,a.`Customer Type` " 
		        + "from "
				+ "user u inner join member m on (u.id=m.userId) "
				+ "inner join `Application Form Header` a on (a.memberNo=m.memberNo) "
				+ "where "
				+ "concat(u.firstName,' ',u.lastName) like :searchTerm or "
				+ "m.memberShipStatus like :searchTerm or "
				+ "a.`Customer Type` like :searchTerm";

		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", "%"+searchTerm+"%");

		List<Object[]> rows = getResultList(query, offSet, limit);

		for (Object[] row : rows) {

			int i = 0;
			Object value = null;

			String fullName = (value = row[i++]) == null ? null : value.toString();
			MembershipStatus memberShipStatus = (value = row[i++]) == null ? null
					: MembershipStatus.valueOf(value.toString());
			String customerType = (value = row[i++]) == null ? null : value.toString();

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

		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", "%"+searchTerm+"%");
		
		Number number = getSingleResultOrNull(query);

		return number.intValue();

	}

}