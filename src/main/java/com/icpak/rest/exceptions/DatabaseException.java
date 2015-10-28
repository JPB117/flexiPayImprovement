package com.icpak.rest.exceptions;

import javax.persistence.PersistenceException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.icpak.rest.models.ErrorObj;

@Provider
public class DatabaseException implements ExceptionMapper<PersistenceException> {

	@Override
	public Response toResponse(PersistenceException exception) {
		ErrorObj error = new ErrorObj(Status.INTERNAL_SERVER_ERROR, "500", exception.getCause().getMessage());
		exception.printStackTrace();
		error.setDeveloperMessage(ExceptionUtils.getStackTrace(exception));
		
		Response response = Response
		.status(error.getStatus())
		.entity(error)
		.type(MediaType.APPLICATION_JSON)
		.build();
		
		return response;
	}
}
