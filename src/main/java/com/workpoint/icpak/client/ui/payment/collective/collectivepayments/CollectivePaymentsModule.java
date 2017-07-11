package com.workpoint.icpak.client.ui.payment.collective.collectivepayments;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class CollectivePaymentsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(CollectivePaymentsPresenter.class, CollectivePaymentsPresenter.MyView.class, CollectivePaymentsView.class, CollectivePaymentsPresenter.MyProxy.class);
    }
}