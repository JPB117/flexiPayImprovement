package com.icpak.rest.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.icpak.rest.models.ErrorCode;
import com.icpak.rest.models.ErrorObj;

public class ServiceException extends WebApplicationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceException(ErrorCode error, String...messageParameters) {
		super(Response
				.status(error.getStatus())
				.entity(new ErrorObj(error.getStatus(), 
						error.getCode(),
						error.getMessage(),
						messageParameters))
				.type(MediaType.APPLICATION_JSON)
				.build());
	}
	
}
