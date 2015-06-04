package com.icpak.rest.dao;

import java.util.List;

import org.apache.shiro.crypto.hash.Sha256Hash;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCode;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.util.Attachment;

public class UsersDao extends BaseDao{

    public User findUser(String username) {
        assert username!=null;
        String query = "from User u where u.username = :username";
        return getSingleResultOrNull(getEntityManager().createQuery(query)
        		.setParameter("username", username));
    }

    public void createUser(User user) {
    	if(user.getPassword()!=null && user.getId()==null)
    		user.setPassword(encrypt(user.getPassword()));
    	
        save( user );
    }
    
    public String encrypt(String password){
    	return new Sha256Hash(password).toHex();
    }

    public List<User> getAllUsers(Integer offSet, Integer limit, Role role) {
    	if(role==null){
    		return getResultList(getEntityManager().createQuery("from User where isActive=1 order by username"), offSet, limit);
    	}
    	
    	return getResultList(getEntityManager().createQuery("select u from User u"
    			+ " inner join u.roles roles "
    			+ " where roles=:role "
    			+ " and u.isActive=1"
    			+ " order by username")
    			.setParameter("role", role)
    			, offSet, limit);
    }

    public void updateUser(User user) {
        createUser(user);
    }

	public int getUserCount() {
		return getUserCount(null);
	}
	

	public int getUserCount(String roleId) {
		
		Number number = null;
		if(roleId==null){
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from user where isactive=1"));
		}else{
			number = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(*) from user u "
					+ "inner join user_role ur on (ur.refId=u.id) "
					+ "inner join role r on (ur.roleid=r.id)"
					+ "where u.isactive=1 and u.isactive=1"));
		}
		
		return number.intValue();
	}

	public User findByUserId(String refId) {
		return findByUserId(refId, true);
	}
	public User findByUserId(String refId, boolean throwExceptionIfNull) {
		User user = getSingleResultOrNull(
				getEntityManager().createQuery("from User u where u.refId=:refId")
				.setParameter("refId", refId));
		
		if(user==null && throwExceptionIfNull){
			throw new ServiceException(ErrorCodes.NOTFOUND,"User", "'"+refId+"'");
		}
		
		return user;
	}

	public int disableProfilePics(String userId) {
		User user = findByUserId(userId);
		
		String update = "UPDATE Attachment set isActive=0 where user=:user";
		int rows = getEntityManager().createQuery(update)
				.setParameter("user", user).executeUpdate();
		
		return rows;
	}

	public Attachment getProfilePic(String userId) {
		
		String sql = "SELECT a FROM Attachment a where a.isActive=1 and a.user.refId=:userid";
		
		Attachment attachment=getSingleResultOrNull(getEntityManager()
				.createQuery(sql)
				.setParameter("userid", userId));
		if(attachment==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Profile Picture", " for user "+userId);
		}
		return attachment;
	}
}
