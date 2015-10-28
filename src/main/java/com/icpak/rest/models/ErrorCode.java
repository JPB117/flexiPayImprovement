package com.icpak.rest.models;

import javax.ws.rs.core.Response.Status;

public interface ErrorCode {

	public String getCode();
	public String getMessage();
	public Status getStatus();
}
