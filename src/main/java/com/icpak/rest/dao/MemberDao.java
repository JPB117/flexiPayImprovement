package com.icpak.rest.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.EduType;
import com.icpak.rest.models.membership.Education;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.TrainingAndExperience;
import com.icpak.rest.models.membership.TrainingExperienceType;

/**
 * 
 * @author duggan
 *
 */
public class MemberDao extends BaseDao{

    public void createMember(Member member) {
        save( member );
    }

    public List<Member> getAllMembers(Integer offSet, Integer limit) {
    	return getResultList(getEntityManager().createQuery("select u from Member u"
    			+ " where u.isActive=1")
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

	public Member findByMemberId(String refId) {
		return findByMemberId(refId, true);
	}
	public Member findByMemberId(String refId, boolean throwExceptionIfNull) {
		Member member = getSingleResultOrNull(
				getEntityManager().createQuery("from Member u where u.refId=:refId")
				.setParameter("refId", refId));
		
		if(member==null && throwExceptionIfNull){
			throw new ServiceException(ErrorCodes.NOTFOUND,"Member", "'"+refId+"'");
		}
		
		return member;
	}

	public int getEducationCount(Member member, EduType type) {
		
		Number number = null;
		if(type==null){
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from education e "
					+ "where e.type=:type and e.memberid=:memberid and e.isactive=1")
					.setParameter("memberid", member.getId()));
		}else{
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from education e "
					+ "where e.type=:type and e.memberid=:memberid and e.isactive=1")
					.setParameter("type", type.ordinal())
					.setParameter("memberid", member.getId()));
		}
		
		return number.intValue();
	}

	public Collection<Education> getEducation(Member member, EduType type) {
		
		if(type!=null){

			return getResultList(getEntityManager().createQuery("FROM Education e where e.isActive=1 "
					+ "and e.member=:member and e.type=:type")
					.setParameter("member", member)
					.setParameter("type", type));
		}else{
			return getResultList(getEntityManager().createQuery("FROM Education e where e.isActive=1 "
					+ "and e.member=:member")
					.setParameter("member", member));
		}
	}

	public int getTrainingAndExpCount(Member member, TrainingExperienceType type) {
		Number number = null;
		if(type==null){
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from TrainingAndExperience e "
					+ "where e.type=:type and e.memberid=:memberid and e.isactive=1")
					.setParameter("memberid", member.getId()));
		}else{
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from TrainingAndExperience e "
					+ "where e.type=:type and e.memberid=:memberid and e.isactive=1")
					.setParameter("type", type.ordinal())
					.setParameter("memberid", member.getId()));
		}
		
		return number.intValue();
	}

	public Collection<TrainingAndExperience> getTrainingAndExp(Member member,
			TrainingExperienceType type) {
		if(type!=null){

			return getResultList(getEntityManager().createQuery("FROM TrainingAndExperience e where e.isActive=1 "
					+ "and e.member=:member and e.type=:type")
					.setParameter("member", member)
					.setParameter("type", type));
		}else{
			return getResultList(getEntityManager().createQuery("FROM TrainingAndExperience e where e.isActive=1 "
					+ "and e.member=:member")
					.setParameter("member", member));
		}
	}

}
