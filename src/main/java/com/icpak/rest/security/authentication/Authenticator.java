/*
 * Copyright 2013 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.icpak.rest.security.authentication;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;

import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.UserSessionDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.security.ICPAKAuthenticatingRealm;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;

@Transactional
public class Authenticator {
	private final UsersDao userDao;
	private final Provider<HttpSession> sessionProvider;
	private final CurrentUserDtoProvider currentUserDtoProvider;
	private final UserSessionDao userSessionDao;
	private final ICPAKAuthenticatingRealm realm;

	@Inject
	Authenticator(UsersDao userDao, Provider<HttpSession> sessionProvider,
			CurrentUserDtoProvider currentUserDtoProvider,
			UserSessionDao userSessionDao, ICPAKAuthenticatingRealm realm) {
		this.userDao = userDao;
		this.sessionProvider = sessionProvider;
		this.currentUserDtoProvider = currentUserDtoProvider;
		this.userSessionDao = userSessionDao;
		this.realm = realm;
	}

	public UserDto authenticateCredentials(String username, String password) {

		User user = userDao.findUserByUsername(username);

		if (authenticate(username, password) != null) {
			UserDto userDto = user.getDTO();
			persistHttpSessionCookie(userDto);

			return userDto;
		} else {
			throw new AuthenticationException();
		}

	}

	@SuppressWarnings("deprecation")
	private UserDto authenticate(String username, String password) {

		Sha256CredentialsMatcher matcher = new Sha256CredentialsMatcher();
		User user = userDao.findUserByUsername(username);
		if (password == null || user == null) {
			throw new ServiceException(ErrorCodes.UNAUTHORIZEDACCESS);
		}

		AuthenticationToken token = new UsernamePasswordToken(username,
				password);
		UsernamePasswordToken authcToken = new UsernamePasswordToken(username,
				password);
		boolean isMatch = false;
		try {
			isMatch = matcher.doCredentialsMatch(token,
					realm.getAuthenticationInfo(authcToken));
		} catch (IncorrectCredentialsException e) {
			throw new ServiceException(ErrorCodes.UNAUTHORIZEDACCESS);
		}

		if (!isMatch) {
			throw new ServiceException(ErrorCodes.UNAUTHORIZEDACCESS);
		}

		return user.getDTO();
	}

	public UserDto authenticatCookie(String loggedInCookie)
			throws AuthenticationException {
		UserDto userDto = userSessionDao.getUserFromCookie(loggedInCookie);

		if (userDto == null) {
			throw new AuthenticationException();
		} else {
			persistHttpSessionCookie(userDto);
		}

		return userDto;
	}

	public void logout() {
		removeCurrentUserLoginCookie();

		HttpSession httpSession = sessionProvider.get();
		httpSession.invalidate();
	}

	/**
	 * Session support has to be enabled in the appengine-web.xml.
	 */
	private void persistHttpSessionCookie(UserDto user) {
		HttpSession session = sessionProvider.get();
		session.setAttribute(SecurityParameters.getUserSessionKey(),
				user.getRefId());
	}

	public Boolean isUserLoggedIn() {
		HttpSession session = sessionProvider.get();
		String userRefId = (String) session.getAttribute(SecurityParameters
				.getUserSessionKey());

		return userRefId != null;
	}

	private void removeCurrentUserLoginCookie() {
		CurrentUserDto currentUserDto = currentUserDtoProvider.get();
		UserDto userDto = currentUserDto.getUser();
		userSessionDao.removeLoggedInCookie(userDto);
	}
}
