package com.workpoint.icpak.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.gwtplatform.mvp.client.PreBootstrapper;

public class IcpakPreBootstrapper implements PreBootstrapper {
	private static final Logger LOGGER = Logger.getLogger("ICPAK Logger..");

	@Override
	public void onPreBootstrap() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				unwrap(e);
			}

			public Throwable unwrap(Throwable e) {
				if (e instanceof UmbrellaException) {
					UmbrellaException ue = (UmbrellaException) e;
					if (ue.getCauses().size() == 1) {
						Throwable exception = unwrap(ue.getCauses().iterator()
								.next());
						LOGGER.log(Level.SEVERE, exception.getStackTrace() + "");
						return exception;
					}
				}
				return e;
			}
		});
		
		LOGGER.info(" ++++ ON PRE BOOTSTRAP ++++ ");
	}
}
