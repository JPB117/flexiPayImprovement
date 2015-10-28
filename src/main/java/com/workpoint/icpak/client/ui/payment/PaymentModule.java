package com.workpoint.icpak.client.ui.payment;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PaymentModule extends AbstractPresenterModule {
	@Override
	protected void configure() {
		bindPresenterWidget(PaymentPresenter.class,
				PaymentPresenter.MyView.class, PaymentView.class);
	}
}