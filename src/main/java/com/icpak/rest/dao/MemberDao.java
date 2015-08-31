package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.List;

import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.MemberDto;

/**
 * 
 * @author duggan
 *
 */
public class MemberDao extends BaseDao {

	public void createMember(Member member) {
		save(member);
	}

	public List<MemberDto> getAllMembers(Integer offSet, Integer limit,
			String searchTerm) {
		List<Object[]> rows =  getResultList(getEntityManager().createNativeQuery(
				"select u.refId userRefId, u.email, u.firstName, u.lastName, u.title,"
				+ " m.memberId, m.refId memberRefId from Member m inner join user u on (m.userId=u.id) "
				+ "where (u.memberId like :searchTerm or u.Name like :searchTerm)")
				.setParameter("searchTerm", "%"+searchTerm+"%"),
				offSet, limit);
		
		List<MemberDto> memberList = new ArrayList<>();
		
		for(Object[] row: rows){
			int i=0;
			Object value=null;
			String userRefId = (value=row[i++])==null? null: value.toString();
			String email=(value=row[i++])==null? null: value.toString();
			String firstName = (value=row[i++])==null? null: value.toString();
			String lastName=(value=row[i++])==null? null: value.toString();
			String title=(value=row[i++])==null? null: value.toString();
			String memberId = (value=row[i++])==null? null: value.toString();
			String memberRefId = (value=row[i++])==null? null: value.toString();
			
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

}