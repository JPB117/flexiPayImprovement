package com.workpoint.icpak.client;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.UmbrellaException;
import com.gwtplatform.mvp.client.PreBootstrapper;

public class IcpakPreBootstrapper implements PreBootstrapper {
	@Override
	public void onPreBootstrap() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable e) {
				// Logger logger = Logger.getLogger("NameOfYourLogger");
				// logger.log(Level.SEVERE, "this message should get logged");
				//Window.alert(e.getStackTrace() + "");
				unwrap(e);
			}

			public Throwable unwrap(Throwable e) {
				if (e instanceof UmbrellaException) {
					UmbrellaException ue = (UmbrellaException) e;
					if (ue.getCauses().size() == 1) {
						Throwable exception = unwrap(ue.getCauses().iterator()
								.next());
						GWT.log("Uncaught exception escaped", exception);
						return exception;
					}
				}
				return e;
			}
		});
	}
}
