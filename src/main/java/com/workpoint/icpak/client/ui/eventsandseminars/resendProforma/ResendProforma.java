package com.workpoint.icpak.client.ui.eventsandseminars.resendProforma;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.TextField;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class ResendProforma extends Composite {

	private static ResendProformaUiBinder uiBinder = GWT
			.create(ResendProformaUiBinder.class);

	interface ResendProformaUiBinder extends UiBinder<Widget, ResendProforma> {
	}

	@UiField
	TextField txtEmail;
	private DelegateDto delegate;

	public ResendProforma() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public ResendProforma(DelegateDto delegate) {
		this();
		this.delegate = delegate;
		txtEmail.setValue(delegate.getContactEmail());
	}

	public ResendModel getResendObject() {
		ResendModel model = new ResendModel(txtEmail.getValue(), delegate);
		return model;
	}

}
