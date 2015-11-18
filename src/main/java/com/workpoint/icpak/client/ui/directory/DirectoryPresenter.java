package com.workpoint.icpak.client.ui.directory;

import java.util.List;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.EditModelEvent;
import com.workpoint.icpak.client.ui.events.EditModelEvent.EditModelHandler;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.shared.api.DirectoryResource;
import com.workpoint.icpak.shared.model.DirectoryDto;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;

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
		super(eventBus, view, proxy);
		this.directoryResourceDelegate = directoryResourceDelegate;

	}
	
	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
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
		super.onReveal();
		loadCount();
	}

	private void loadCount() {
		directoryResourceDelegate.withCallback(new AbstractAsyncCallback<Integer>(){

			@Override
			public void onSuccess(Integer result) {
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(result);
				PagingConfig config = panel.getConfig();
				
				loadDirectory(config.getOffset(),config.getLimit());
			}
			
		}).getCount();
	}

	private void loadDirectory(int offset, int limit) {
		
		directoryResourceDelegate.withCallback(new AbstractAsyncCallback<List<DirectoryDto>>(){

			@Override
			public void onSuccess(List<DirectoryDto> results) {
				getView().bindResults(results);
			}
			
		}).getAll(offset, limit);
	}
}