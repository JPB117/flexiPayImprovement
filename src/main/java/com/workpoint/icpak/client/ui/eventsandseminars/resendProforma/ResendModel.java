package com.workpoint.icpak.client.ui.eventsandseminars.resendProforma;

import com.workpoint.icpak.shared.model.events.DelegateDto;

public class ResendModel {
	private String emails;
	private DelegateDto delegate;

	public ResendModel(String emails, DelegateDto delegate) {
		this.emails = emails;
		this.delegate = delegate;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}

	public DelegateDto getDelegate() {
		return delegate;
	}

	public void setDelegate(DelegateDto delegate) {
		this.delegate = delegate;
	}
}
