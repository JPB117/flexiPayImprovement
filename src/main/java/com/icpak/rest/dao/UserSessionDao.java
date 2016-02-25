package com.icpak.rest.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.Query;

import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.auth.UserSession;
import com.workpoint.icpak.shared.model.UserDto;

public class UserSessionDao extends BaseDao {
	private static final int TWO_WEEKS_AGO_IN_DAYS = -14;

	private final Logger logger;
	private final UsersDao userDao;

	@Inject
	UserSessionDao(Logger logger, UsersDao userDao) {
		this.logger = logger;
		this.userDao = userDao;
	}

	public String createSessionCookie(String currentCookie, UserDto userDto) {
		String cookie = UUID.randomUUID().toString();
		UserSession userSession = new UserSession(userDto.getRefId(), cookie);
		save(userSession);
		logger.info("UserSessionDao.createLoggedInCookie(user) user=" + userDto + " userSessionCookie="
				+ userSession.getCookie());
		return userSession.getCookie();
	}

	public void removeLoggedInCookie(UserDto userDto) {
		// List<UserSession> userSession = findUserSession(userDto.getRefId());
		if (userDto == null) {
			return;
		}
		getEntityManager().createNativeQuery("delete from UserSession where userRefId=:userId")
				.setParameter("userId", userDto.getRefId()).executeUpdate();

		logger.info("UserSessionDao.removeLoggedInCookie(user): Cookie is removed from database.");
	}

	public UserDto getUserFromCookie(String loggedInCookie) {
		UserSession userSession = getSessionFromCookie(loggedInCookie);
		if (userSession == null) {
			return null;
		}

		String userRefId = userSession.getUserRefId();

		UserDto userDto = null;
		if (userRefId != null) {
			User user = findByRefId(userRefId, User.class);
			userDto = user.toDto();
		}

		return userDto;
	}

	private UserSession getSessionFromCookie(String loggedInCookie) {
		Date twoWeeksAgo = getTwoWeeksAgo();

		Query query = getEntityManager()
				.createQuery("FROM UserSession where cookie=:cookie and " + "dateCreated>:twoWeeksAgo")
				.setParameter("cookie", loggedInCookie).setParameter("twoWeeksAgo", twoWeeksAgo);

		UserSession userSession = getSingleResultOrNull(query);

		return userSession;
	}

	private Date getTwoWeeksAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, TWO_WEEKS_AGO_IN_DAYS);

		return calendar.getTime();
	}

	private List<UserSession> findUserSession(String userRefId) {
		Query query = getEntityManager().createQuery("FROM UserSession where userRefId=:refId order by id desc");

		return getResultList(query);
	}

	public void updateLogedInCookie(String loggedInCookie) {
		int query = getEntityManager()
				.createNativeQuery("UPDATE usersession u SET u.dateCreated=:date where u.cookie=:cookie")
				.setParameter("cookie", loggedInCookie).setParameter("date", new Date()).executeUpdate();
	}
}
