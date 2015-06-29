package com.icpak.rest;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.core.Cookie;

import com.icpak.rest.security.authentication.Authenticator;
import com.icpak.rest.security.authentication.CurrentUserDtoProvider;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;

public class SessionResourceImpl implements SessionResource {
    private final Logger logger;
    private final Authenticator authenticator;
    private final CurrentUserDtoProvider currentUserDtoProvider;

    @Inject
    SessionResourceImpl(
            Logger logger,
            Authenticator authenticator,
            CurrentUserDtoProvider currentUserDtoProvider) {
        this.logger = logger;
        this.authenticator = authenticator;
        this.currentUserDtoProvider = currentUserDtoProvider;
    }

    @Override
    public CurrentUserDto getCurrentUser() {
        return currentUserDtoProvider.get();
    }

    @Override
    public void logout() {
        authenticator.logout();
    }

    @Override
    public void rememberMe(Cookie cookie) {
        logger.info("Remember me: " + String.valueOf(cookie));
    }
}
