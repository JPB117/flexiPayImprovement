package com.workpoint.icpak.client.service;

import com.google.gwt.http.client.RequestTimeoutException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.gwtplatform.dispatch.rpc.shared.ServiceException;
import com.workpoint.icpak.client.ui.events.ClientDisconnectionEvent;
import com.workpoint.icpak.client.ui.events.ErrorEvent;
import com.workpoint.icpak.client.ui.events.ProcessingCompletedEvent;
import com.workpoint.icpak.client.util.AppContext;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {
	@Override
	public void onFailure(Throwable caught) {
		if(caught instanceof ServiceException){
			String msg = "Cannot connect to server...";
			if(caught.getMessage()!=null && caught.getMessage().length()>5){
				msg = caught.getMessage();
			}
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent(msg));
			return;
		}
		
		if(caught instanceof InvocationException){
			String msg = "Cannot connect to server...";
			if(caught.getMessage()!=null && caught.getMessage().length()>5){
				msg = caught.getMessage();
			}
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent(msg));
			return;
		}
		
		if(caught instanceof RequestTimeoutException){
			//HTTP Request Timeout
			AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
			AppContext.getEventBus().fireEvent(new ClientDisconnectionEvent("Cannot connect to server..."));
		}
		
		//caught.printStackTrace();
		
		String message = caught.getMessage();
		
		if(caught.getCause()!=null){
			message = caught.getCause().getMessage();
		}
		
		AppContext.getEventBus().fireEvent(new ProcessingCompletedEvent());
		AppContext.getEventBus().fireEvent(new ErrorEvent(message, 0L));

	}
}