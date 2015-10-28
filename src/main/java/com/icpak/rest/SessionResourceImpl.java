package com.icpak.rest;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import com.icpak.rest.security.authentication.Authenticator;
import com.icpak.rest.security.authentication.CurrentUserDtoProvider;
import com.workpoint.icpak.shared.api.SessionResource;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;

@Path("/session")
@Produces(MediaType.APPLICATION_JSON)
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

    @GET
    public CurrentUserDto getCurrentUser() {
        return currentUserDtoProvider.get();
    }
    
    @DELETE
    public void logout() {
        authenticator.logout();
    }

    @POST
    @Path("/remember-me")
    public void rememberMe(@CookieParam("LoggedInCookie") Cookie cookie) {
        logger.info("Remember me: " + String.valueOf(cookie));
    }
}
