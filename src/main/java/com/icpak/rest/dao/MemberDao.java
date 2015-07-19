package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.membership.Member;

/**
 * 
 * @author duggan
 *
 */
public class MemberDao extends BaseDao{

    public void createMember(Member member) {
        save( member );
    }

    public List<Member> getAllMembers(Integer offSet, Integer limit, String searchTerm) {
    	return getResultList(getEntityManager().createQuery("select u from Member u "
    			+ "where u.isActive=1 and "
    			+ "(u.memberId ilike :searchTerm or "
    			+ "u.firstName")
    			, offSet, limit);
    }

    public void updateMember(Member member) {
        createMember(member);
    }

	public int getMemberCount() {
		return getMemberCount(null);
	}

	public int getMemberCount(String roleId) {
		
		Number number = null;
		if(roleId==null){
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from member "
					+ "where isactive=1"));
		}else{
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from member u "
					+ "where u.isactive=1"));
		}
		
		return number.intValue();
	}

}