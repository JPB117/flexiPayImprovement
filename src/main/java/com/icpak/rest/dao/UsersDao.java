package com.icpak.rest.dao;

import java.util.List;

import org.apache.shiro.crypto.hash.Sha256Hash;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.util.Attachment;

public class UsersDao extends BaseDao {

	public User findUserByUsername(String username) {
		assert username != null;
		String query = "from User u where u.username = :username";
		return getSingleResultOrNull(getEntityManager().createQuery(query)
				.setParameter("username", username));
	}
	
	public User findUserByMemberNo(String memberNo) {
		assert memberNo != null;
		String query = "from User u where u.memberNo = :memberNo";
		return getSingleResultOrNull(getEntityManager().createQuery(query)
				.setParameter("memberNo", memberNo));
	}
	
	public User getUserByUsernameOrMemberNo(String username) {
		
		User user = findUserByUsername(username);
		if(user==null){
			user = findUserByMemberNo(username);
		}
		return user;
	}


	public void createUser(User user) {
		if (user.getHashedPassword() != null && user.getId() == null)
			user.setPassword(encrypt(user.getHashedPassword()));

		save(user);
	}

	public void changePassword(User user) {
		user.setPassword(encrypt(user.getHashedPassword()));
		createUser(user);
	}

	public String encrypt(String password) {
		return new Sha256Hash(password).toHex();
	}

	public List<User> getAllUsers(Integer offSet, Integer limit, Role role,
			String searchTerm) {
		if (role == null) {
			String query = "from User u where isActive=1 and "
					+ "(u.memberNo like :searchTerm or "
					+ "u.userData.fullNames like :searchTerm or "
					+ "u.email like :searchTerm or "
					+ "u.memberNo like :searchTerm" + ")" + "order by username";

			return getResultList(getEntityManager().createQuery(query)
					.setParameter("searchTerm", "%" + searchTerm + "%"),
					offSet, limit);
		}

		return getResultList(getEntityManager().createQuery(
				"select u from User u" + " inner join u.roles roles "
						+ " where roles=:role " + " and u.isActive=1"
						+ " order by username").setParameter("role", role),
				offSet, limit);
	}

	public void updateUser(User user) {
		createUser(user);
	}

	public int getUserCount() {
		return getUserCount(null);
	}

	public int getUserCount(String roleId) {

		Number number = null;
		if (roleId == null) {
			number = getSingleResultOrNull(getEntityManager()
					.createNativeQuery(
							"select count(*) from user where isactive=1"));
		} else {
			number = getSingleResultOrNull(getEntityManager()
					.createNativeQuery(
							"select count(*) from user u "
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
		User user = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from User u where u.isActive=1 and (u.refId=:refId or u.email=:email)")
				.setParameter("refId", refId).setParameter("email", refId));

		if (user == null && throwExceptionIfNull) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "User", "'" + refId
					+ "'");
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

		String sql = "SELECT a FROM Attachment a where a.isActive=1 and a.profilePicUserId=:userid";

		Attachment attachment = getSingleResultOrNull(getEntityManager()
				.createQuery(sql).setParameter("userid", userId));
		if (attachment == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Profile Picture",
					" for user " + userId);
		}
		return attachment;
	}

	public String getApplicationRefId(String userRef) {

		String sql = "select refid from `Application Form Header` where userRefId=:userRef";
		return getSingleResultOrNull(getEntityManager().createNativeQuery(sql)
				.setParameter("userRef", userRef));
	}

	public boolean hasMember(User po) {

		Number val = getSingleResultOrNull(getEntityManager().createQuery(
				"select count(*) from Member where userRefId=:userId")
				.setParameter("userId", po.getRefId()));

		return val.intValue() > 0;
	}

	public String getMemberRefId(String userRefId) {

		String memberNo = getSingleResultOrNull(getEntityManager().createQuery(
				"select refId from Member where userRefId=:userId")
				.setParameter("userId", userRefId));

		return memberNo;
	}

	public String getFullNames(String memberNo) {

		return getSingleResultOrNull(getEntityManager()
				.createNativeQuery(
						"select concat(firstName,' ',lastName) from user u "
								+ "inner join Member m on m.userRefId=u.refId where m.refId=:memberNo")
				.setParameter("memberNo", memberNo));
	}


}
