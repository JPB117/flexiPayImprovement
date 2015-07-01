package com.workpoint.icpak.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {
	@Override
	public void onFailure(Throwable caught) {
//		StringWriter sw = new StringWriter();
//		PrintWriter pw = new PrintWriter(sw);
//		caught.printStackTrace(pw);
//		sw.toString(); // stack trace as a string
	}
}