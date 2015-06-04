package com.icpak.rest.models;

import javax.ws.rs.core.Response.Status;

public enum ErrorCodes implements ErrorCode {

	NOTFOUND("404","{0} {1} was not found", Status.NOT_FOUND), //e.g {Member} {XYX} was not found
	SERVER_ERROR("500","{0} : {1}", Status.INTERNAL_SERVER_ERROR), //e.g {ResourceCollectionModel.class} : {Collection Total Count is null}
	ILLEGAL_ARGUMENT("422","Illegal argument {0} '{1}' not expected", Status.BAD_REQUEST),//e.g {Permission} {SomeNonExistentPermission} not expected
	DUPLICATEVALUE("500", "Duplicate value error, {0} must be unique.",Status.INTERNAL_SERVER_ERROR),
	UNAUTHORIZEDACCESS("401", "Unauthorized", Status.UNAUTHORIZED)
	;

	private String code;
	private String message;
	private Status status=Status.OK;
	
	private ErrorCodes(String code, String message){
		this.code = code;
		this.message = message;
	}
	
	private ErrorCodes(String code, String message, Status status) {
		this(code, message);
		this.status = status;
	}
	
	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public Status getStatus() {
		return status;
	}

}
