package com.workpoint.icpak.client.service;

import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.gwtplatform.dispatch.rest.client.RestCallback;
import com.workpoint.icpak.client.ui.events.AfterSaveEvent;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent;
import com.workpoint.icpak.client.ui.events.ErrorEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.util.AppContext;

public abstract class AbstractAsyncCallback<T> implements RestCallback<T> {

	@Override
	public void onFailure(Throwable caught) {
		// if (caught instanceof ServiceException) {
		// Window.alert("On Error SE >> " + caught.getClass());
		// String msg = "Cannot connect to server...";
		// if (caught.getMessage() != null && caught.getMessage().length() > 5)
		// {
		// msg = caught.getMessage();
		// }
		// AppContext.getEventBus().fireEvent(
		// new ClientDisconnectionEvent(msg));
		// return;
		// }
		//
		// if (caught instanceof InvocationException) {
		// Window.alert("On Error IE >> " + caught.getClass());
		// String msg = "Cannot connect to server...";
		// if (caught.getMessage() != null && caught.getMessage().length() > 5)
		// {
		// msg = caught.getMessage();
		// }
		// AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		// AppContext.getEventBus().fireEvent(
		// new ClientDisconnectionEvent(msg));
		// return;
		// }
		//
		// if (caught instanceof RequestTimeoutException) {
		// Window.alert("On Error RT >> " + caught.getClass());
		// // HTTP Request Timeout
		// AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		// AppContext.getEventBus()
		// .fireEvent(
		// new ClientDisconnectionEvent(
		// "Cannot connect to server..."));
		// }
		//
		// if (caught instanceof ActionException) {
		// ActionException ex = (ActionException) caught;
		// Window.alert(ex.getMessage() + " : " + ex.getLocalizedMessage());
		// }
		//
		// // caught.printStackTrace();
		//
		// String message = caught.getMessage();
		// if (caught.getCause() != null) {
		// message = caught.getClass() + "\n" + caught.getCause().getMessage();
		// }
		//

		AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		AppContext.getEventBus().fireEvent(
				new AfterSaveEvent("The was a problem submitting your request.Contact Administrator.", false));
	}

	@Override
	public void setResponse(Response aResponse) {
		int code = aResponse.getStatusCode();
		// AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());

		if (code == 200 || code == 202 || code == 203 || code == 204 || code == 304) {
			return;
		}

		AppContext.getEventBus().fireEvent(new ErrorEvent(code, aResponse.getStatusText()));

		boolean isContinue = handleCustomError(aResponse);

		if (!isContinue) {
			return;
		}

		String message = aResponse.getStatusText();
		if (code == 500) {
			AppContext.getEventBus().fireEvent(new ErrorEvent(message, 0L));
			return;
		}

		if (code == 401) {
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			// AppContext.getEventBus().fireEvent(new
			// ErrorEvent("Unauthorized Access", 0L));
			return;
		}

		if (code == 404) {
			// Not Found
			return;
		}

		if (code == 408 || code == 0) {
			// HTTP Request Timeout
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent("Cannot connect to server..."));
			return;
		}

		AppContext.getEventBus().fireEvent(new ErrorEvent("Code=" + code + "; " + aResponse.getText(), 0L));
	}

	/**
	 * 
	 * @param aResponse
	 * @return true to allow Super Class to show error popups
	 */
	public boolean handleCustomError(Response aResponse) {
		return true;
	}
}