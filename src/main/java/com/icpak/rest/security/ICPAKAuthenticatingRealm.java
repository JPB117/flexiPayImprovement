package com.icpak.rest.security;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;

public class ICPAKAuthenticatingRealm extends AuthenticatingRealm {

	@Inject
	Provider<UsersDao> userDao = null;

	public ICPAKAuthenticatingRealm() {
		setCredentialsMatcher(new Sha256CredentialsMatcher());
	}

	UsersDao getUserDao() {
		return userDao.get();
	}

	public AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = getUserDao().findUser(token.getUsername());
		if (user != null) {
			return new SimpleAuthenticationInfo(user.getId(),
					user.getPassword(), getName());
		}
		return null;

	}

	public AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		Long userId = (Long) principals.fromRealm(getName()).iterator().next();
		User user = getUserDao().getById(User.class, userId);
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			for (Role role : user.getRoles()) {
				info.addRole(role.getName());
				info.addStringPermissions(role.getPermissions());
			}
			return info;
		} else {
			return null;
		}
	}

}
