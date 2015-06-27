package com.workpoint.icpak.shared.model.auth;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogInAction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ActionType actionType;
    private String username;
    private String password;
    private String loggedInCookie;

    protected LogInAction() {
    }

    public LogInAction(
            String username,
            String password) {
        actionType = ActionType.VIA_CREDENTIALS;
        this.password = password;
        this.username = username;
    }

    public LogInAction(String loggedInCookie) {
        actionType = ActionType.VIA_COOKIE;
        this.loggedInCookie = loggedInCookie;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLoggedInCookie() {
        return loggedInCookie;
    }
}
