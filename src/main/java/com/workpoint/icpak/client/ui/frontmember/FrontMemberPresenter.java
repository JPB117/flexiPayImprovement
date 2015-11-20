package com.workpoint.icpak.client.ui.frontmember;

import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rest.delegates.client.ResourceDelegate;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.NoGatekeeper;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.workpoint.icpak.client.place.NameTokens;
import com.workpoint.icpak.client.service.AbstractAsyncCallback;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.shared.api.MemberResource;
import com.workpoint.icpak.shared.model.MemberDto;

public class FrontMemberPresenter
		extends Presenter<FrontMemberPresenter.MyFrontMemberView, FrontMemberPresenter.MyFrontMemberProxy>
		implements TableActionHandler {
	public interface MyFrontMemberView extends View {

		void bindResults(List<MemberDto> result);

		PagingPanel getPagingPanel();

		HasValueChangeHandlers<String> getSearchValueChangeHander();

		String getSearchValue();
	}

	protected final ResourceDelegate<MemberResource> memberResourceDelegate;

	@ProxyCodeSplit
	@NameToken(NameTokens.frontMember)
	@NoGatekeeper
	public interface MyFrontMemberProxy extends ProxyPlace<FrontMemberPresenter> {
	}

	ValueChangeHandler<String> frontMemberValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			searchMembers(getView().getSearchValue().trim());
		}
	};

	@Inject
	FrontMemberPresenter(EventBus eventBus, MyFrontMemberView view, MyFrontMemberProxy proxy,
			ResourceDelegate<MemberResource> memberResourceDelegate) {
		super(eventBus, view, proxy);
		this.memberResourceDelegate = memberResourceDelegate;

	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		addRegisteredHandler(TableActionEvent.TYPE, this);
		getView().getPagingPanel().setLoader(new PagingLoader() {
			@Override
			public void onLoad(int offset, int limit) {
				loadMembers(offset, limit);
			}
		});
		
		getView().getSearchValueChangeHander().addValueChangeHandler(frontMemberValueChangeHandler);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		loadCount();
	}

	private void loadCount() {
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				PagingPanel pagingPanel = getView().getPagingPanel();
				pagingPanel.setTotal(result);
				PagingConfig pagingConfig = pagingPanel.getConfig();

				loadMembers(pagingConfig.getOffset(), pagingConfig.getLimit());
			}
		}).getMembersCount();
	}

	private void loadMembers(int offset, int limit) {
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<List<MemberDto>>() {

			@Override
			public void onSuccess(List<MemberDto> results) {
				getView().bindResults(results);
			}
		}).getMembers(offset, limit);
	}

	public void searchMembers(final String searchTerm) {
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(result);
				PagingConfig pagingConfig = panel.getConfig();
				searchMembers(searchTerm, pagingConfig.getOffset(), pagingConfig.getLimit());
			}

		}).getMembersSearchCount(searchTerm);
	}

	public void searchMembers(String searchTerm, int offset, int limit) {
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<List<MemberDto>>() {

			@Override
			public void onSuccess(List<MemberDto> results) {
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(results.size());
				getView().bindResults(results);
			}
		}).searchMembers(searchTerm, offset, limit);
	}

	@Override
	public void onTableAction(TableActionEvent event) {
		// TODO Auto-generated method stub
		
	}
}