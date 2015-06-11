package com.workpoint.icpak.client.ui.trx;

import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;

public class TransactionsPresenter extends
		PresenterWidget<TransactionsPresenter.ITransactionView>  {

	public interface ITransactionView extends View {

		void clear();
		HasKeyDownHandlers getSearchBox();

	}

	@Inject
	PlaceManager placeManager;

	@Inject
	public TransactionsPresenter(final EventBus eventBus,
			final ITransactionView view) {
		super(eventBus, view);
	}

	@Override
	protected void onReset() {
		super.onReset();
	}

	public void loadAll() {
		
	}

}
