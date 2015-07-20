package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.membership.Member;

/**
 * 
 * @author duggan
 *
 */
public class MemberDao extends BaseDao {

	public void createMember(Member member) {
		save(member);
	}

	public List<Member> getAllMembers(Integer offSet, Integer limit,
			String searchTerm) {

		if (searchTerm != null) {
			return getResultList(
					getEntityManager().createQuery(
							"select m from Member m inner join m.user u "
									+ "where m.isActive=1 and "
									+ "(m.memberId like :searchTerm or "
									+ " u.userData.fullNames like :searchTerm)")
							.setParameter("searchTerm", "%" + searchTerm + "%"),
					offSet, limit);
		}

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