package com.icpak.rest.models.auth;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.icpak.rest.models.base.PO;

//@Index
@Entity
public class UserSession extends PO {
   
    private String userRefId;
    private String cookie;
    private Date dateCreated;

    public UserSession() {
    }

    public UserSession(
            String userRefId,
            String cookie) {
        super();

        this.userRefId = userRefId;
        this.cookie = cookie;
        this.dateCreated = new Date();
    }

    public String getCookie() {
        return cookie;
    }

    public String getUserRefId() {
        return userRefId;
    }
    
    public void setDateCreated(Date date){
    	this.dateCreated=date;
    }
}