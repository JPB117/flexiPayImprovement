package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.Branch;
import com.icpak.rest.models.membership.Practice;

public class PracticeDao extends BaseDao{

    public void createPractice(Practice practice) {
        save( practice );
    }

    public List<Practice> getAllPractices(Integer offSet, Integer limit) {
    	return getResultList(getEntityManager().createQuery("select u from Practice u"
    			+ " where u.isActive=1")
    			, offSet, limit);
    }

    public void updatePractice(Practice practice) {
        createPractice(practice);
    }

	public int getPracticeCount() {
		return getPracticeCount(null);
	}

	public int getPracticeCount(String roleId) {
		
		Number number = null;
		if(roleId==null){
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from practice "
					+ "where isactive=1"));
		}else{
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from practice u "
					+ "where u.isactive=1"));
		}
		
		return number.intValue();
	}

	public Practice findByPracticeId(String refId) {
		return findByPracticeId(refId, true);
	}
	public Practice findByPracticeId(String refId, boolean throwExceptionIfNull) {
		Practice practice = getSingleResultOrNull(
				getEntityManager().createQuery("from Practice u where u.refId=:refId")
				.setParameter("refId", refId));
		
		if(practice==null && throwExceptionIfNull){
			throw new ServiceException(ErrorCodes.NOTFOUND,"Practice", "'"+refId+"'");
		}
		
		return practice;
	}

	public Branch getBranch(String refId, boolean throwExceptionIfNull) {
		
		Branch branch = getSingleResultOrNull(
				getEntityManager().createQuery("from Branch u where u.refId=:refId")
				.setParameter("refId", refId));
		
		if(branch==null && throwExceptionIfNull){
			throw new ServiceException(ErrorCodes.NOTFOUND,"Practice", "'"+refId+"'");
		}
		
		return branch;
	}

	public void deleteBranch(String branchId) {
		delete(getBranch(branchId, true));
	}

	public int getBranchCount(String practiceId) {
		Number number = getSingleResultOrNull(getEntityManager().createQuery("select count(b.id) from Branch b "
					+ "where b.practice.refId=:refId")
					.setParameter("refId", practiceId));
		return number.intValue();
	}

	public List<Branch> getAllBranches(String practiceId, Integer offset,
			Integer limit) {
		
		return getSingleResultOrNull(getEntityManager().createQuery("select b from Branch b "
				+ "where b.practice.refId=:refId")
				.setParameter("refId", practiceId).setMaxResults(limit).setFirstResult(offset));
	}

}