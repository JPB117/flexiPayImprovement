package com.icpak.rest.dao;

import com.icpak.rest.models.util.Attachment;

public class AttachmentsDao extends BaseDao {

	public void deleteUserImage(String userRefId) {
		getEntityManager()
				.createNativeQuery(
						"delete from attachment where profilePicUserId=:userRefId")
				.setParameter("userRefId", userRefId).executeUpdate();
	}

	public Attachment getUserProfileImage(String userRefId) {
		return getSingleResultOrNull(getEntityManager().createQuery(
				"FROM Attachment a where " + "a.profilePicUserId=:userRefId")
				.setParameter("userRefId", userRefId));
	}

}
