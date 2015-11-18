package com.workpoint.icpak.client.ui.directory;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.home.HomePresenter;
import com.workpoint.icpak.shared.api.DirectoryResource;
import com.workpoint.icpak.shared.model.DirectoryDto;
import com.workpoint.icpak.client.place.NameTokens;

public class DirectoryPresenter
		extends Presenter<DirectoryPresenter.MyDirectoryView, DirectoryPresenter.MyDirectoryProxy>
		implements EditModelHandler, TableActionHandler {
	public interface MyDirectoryView extends View {

		void bindResults(List<DirectoryDto> result);

		PagingPanel getPagingPanel();
	}

	protected final ResourceDelegate<DirectoryResource> directoryResourceDelegate;

	@ProxyCodeSplit
	@NameToken(NameTokens.directory)
	@NoGatekeeper
	public interface MyDirectoryProxy extends ProxyPlace<DirectoryPresenter> {
	}

	@Inject
	DirectoryPresenter(EventBus eventBus, MyDirectoryView view,
			ResourceDelegate<DirectoryResource> directoryResourceDelegate, MyDirectoryProxy proxy) {
		super(eventBus, view, proxy, HomePresenter.SLOT_SetTabContent);
		this.directoryResourceDelegate = directoryResourceDelegate;

	}

	@Override
	public void onTableAction(TableActionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEditModel(EditModelEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onReveal() {
		// TODO Auto-generated method stub
		super.onReveal();
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getPagingPanel().setLoader(new PagingLoader() {

			@Override
			public void onLoad(int offset, int limit) {
				loadDirectory(offset, limit);

			}
		});
	}

	private void loadDirectory(int offset, int limit) {
		// TODO Auto-generated method stub

	}
}