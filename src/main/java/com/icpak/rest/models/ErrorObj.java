package com.icpak.rest.models;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

import com.icpak.rest.models.base.ResourceModel;

@XmlRootElement
public class ErrorObj extends ResourceModel{

	private Status status;
	private String code;
	private String property;
	private String message;
	private String developerMessage;
	private String moreInfo;
	
	public ErrorObj() {
	}
	public ErrorObj(Status status, String code, String message){
		this.status = status;
		this.code = code;
		this.message = message;
	}
	
	public ErrorObj(Status status, String code, String property, String message, String developerMessage){
		this.status = status;
		this.code = code;
		this.property = property;
		this.message = message;
		this.developerMessage = developerMessage;
	}
	
	public ErrorObj(Status status, String code, String message,
			String[] messageParameters) {
		this(status, code, message);
		
		if(messageParameters!=null && message!=null)
		for(int i=0; i< messageParameters.length; i++){
			this.message=this.message.replace("{"+i+"}", messageParameters[i]);
		}
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public String getMoreInfo() {
		return moreInfo;
	}
	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
