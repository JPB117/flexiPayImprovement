package com.workpoint.icpak.shared.exception;

import com.google.web.bindery.requestfactory.server.DefaultExceptionHandler;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

public class ServiceException extends DefaultExceptionHandler{

	@Override
	public ServerFailure createServerFailure(Throwable throwable) {
		return super.createServerFailure(throwable);
	}
}
