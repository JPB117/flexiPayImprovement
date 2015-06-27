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

public class UserSessionDao  extends BaseDao {
    private static final int TWO_WEEKS_AGO_IN_DAYS = -14;

    private final Logger logger;
    private final UsersDao userDao;

    @Inject
    UserSessionDao(
            Logger logger,
            UsersDao userDao) {
        this.logger = logger;
        this.userDao = userDao;
    }

    public String createSessionCookie(UserDto userDto) {
        String cookie = UUID.randomUUID().toString();
        UserSession userSession = new UserSession(userDto.getRefId(), cookie);
        save(userSession);

        logger.info("UserSessionDao.createLoggedInCookie(user) user=" + userDto + " userSessionCookie="
                + userSession.getCookie());

        return userSession.getCookie();
    }

    public void removeLoggedInCookie(UserDto userDto) {
        UserSession userSession = findUserSession(userDto.getRefId());
        if (userSession != null) {
            delete(userSession);
        }

        logger.info("UserSessionDao.removeLoggedInCookie(user): Cookie is removed from database.");
    }

    public UserDto getUserFromCookie(String loggedInCookie) {
        Date twoWeeksAgo = getTwoWeeksAgo();

        Query query = getEntityManager().createQuery("FROM UserSession where cookie=:cookie and "
        				+ "dateCreated>:twoWeeksAgo")
                .setParameter("cookie", loggedInCookie)
                .setParameter("twoWeeksAgo", twoWeeksAgo);
        				
        UserSession userSession = getSingleResultOrNull(query);

        if (userSession == null) {
            return null;
        }

        String userRefId = userSession.getUserRefId();

        UserDto userDto = null;
        if (userRefId != null) {
        	User user = findByRefId(userRefId, User.class);
        	userDto = user.getDTO();
        }

        return userDto;
    }

    private Date getTwoWeeksAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, TWO_WEEKS_AGO_IN_DAYS);

        return calendar.getTime();
    }

    private UserSession findUserSession(String userRefId) {
    	Query query  = getEntityManager().
    			createQuery("FROM UserSession where userRefId=:refId order by id desc")
    			.setFirstResult(0)
    			.setMaxResults(1);
    	
    		return getSingleResultOrNull(query);
       }
    }
     