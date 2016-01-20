package com.workpoint.icpak.client.ui.frontmember;

import java.util.List;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
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
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingConfig;
import com.workpoint.icpak.client.ui.component.PagingLoader;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.TableView;
import com.workpoint.icpak.client.ui.component.TableView.Towns;
import com.workpoint.icpak.client.ui.events.TableActionEvent;
import com.workpoint.icpak.client.ui.events.TableActionEvent.TableActionHandler;
import com.workpoint.icpak.client.ui.frontmember.model.MemberCategory;
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

		String getTownName();

		DropDownList<Towns> getTownList();

		DropDownList<MemberCategory> getCategoryList();

		String getCategorySelected();

		void showmask(boolean isProcessing);
	}

	protected final ResourceDelegate<MemberResource> memberResourceDelegate;
	private String searchTerm = "all";
	private String townSearchTerm = "all";
	private String categoryName = "all";

	@ProxyCodeSplit
	@NameToken(NameTokens.frontMember)
	@NoGatekeeper
	public interface MyFrontMemberProxy extends ProxyPlace<FrontMemberPresenter> {
	}

	ValueChangeHandler<String> frontMemberValueChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			searchTerm = getView().getSearchValue().trim();
			categoryName = getView().getCategorySelected().trim();
			townSearchTerm = getView().getTownName().trim();
			
			if (categoryName.isEmpty() || categoryName == null) {
				categoryName = "all";
			}
			
			if (townSearchTerm.isEmpty() || townSearchTerm == null) {
				townSearchTerm = "all";
			}

			if (searchTerm.isEmpty() || searchTerm == null) {
				searchTerm = "all";
				searchMembers(searchTerm, townSearchTerm, categoryName);
			} else {
				searchMembers(searchTerm, townSearchTerm, categoryName);
			}
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

				loadMembers(offset, 20);
			}
		});

		getView().getSearchValueChangeHander().addValueChangeHandler(frontMemberValueChangeHandler);

		getView().getTownList().addValueChangeHandler(new ValueChangeHandler<TableView.Towns>() {
			@Override
			public void onValueChange(ValueChangeEvent<Towns> towns) {
				searchTerm = getView().getSearchValue().trim();
				categoryName = getView().getCategorySelected().trim();
				townSearchTerm = getView().getTownName().trim();

				if (searchTerm.isEmpty() || searchTerm == null) {
					searchTerm = "all";
				}

				if (categoryName.isEmpty() || categoryName == null) {
					categoryName = "all";
				}

				if (townSearchTerm == null || townSearchTerm.isEmpty()) {
					townSearchTerm = "all";
					searchMembers(searchTerm, townSearchTerm, categoryName);
				} else {
					searchMembers(searchTerm, townSearchTerm, categoryName);
				}
			}
		});

		getView().getCategoryList().addValueChangeHandler(new ValueChangeHandler<MemberCategory>() {
			@Override
			public void onValueChange(ValueChangeEvent<MemberCategory> selectedCategory) {
				searchTerm = getView().getSearchValue().trim();
				townSearchTerm = getView().getTownName().trim();
				categoryName = getView().getCategorySelected().trim();

				if (searchTerm.isEmpty() || searchTerm == null || searchTerm.equals(" ")) {
					searchTerm = "all";
				}

				if (townSearchTerm.isEmpty() || townSearchTerm == null) {
					townSearchTerm = "all";
				}

				if (categoryName == null || categoryName.isEmpty()) {
					categoryName = "all";
					searchMembers(searchTerm, townSearchTerm, categoryName);
				} else {
					categoryName = getView().getCategorySelected().trim();
					searchMembers(searchTerm, townSearchTerm, categoryName);
				}
			}
		});
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
		getView().showmask(true);
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<List<MemberDto>>() {
			@Override
			public void onSuccess(List<MemberDto> results) {
				getView().bindResults(results);
				getView().showmask(false);
			}
		}).searchMembers(searchTerm, townSearchTerm, categoryName, offset, limit);
	}

	public void searchMembers(final String searchTerm, final String citySearchTerm, final String categoryName) {
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<Integer>() {

			@Override
			public void onSuccess(Integer result) {
				PagingPanel panel = getView().getPagingPanel();
				panel.setTotal(result);
				PagingConfig pagingConfig = panel.getConfig();
				searchMembers(searchTerm, citySearchTerm, categoryName, pagingConfig.getOffset(),
						pagingConfig.getLimit());
			}

		}).getMembersSearchCount(searchTerm, citySearchTerm, categoryName);
	}

	public void searchMembers(String searchTerm, String citySearchTerm, String categoryName, int offset, int limit) {
		getView().showmask(true);
		memberResourceDelegate.withCallback(new AbstractAsyncCallback<List<MemberDto>>() {
			@Override
			public void onSuccess(List<MemberDto> results) {
				getView().bindResults(results);
				getView().showmask(false);
			}
		}).searchMembers(searchTerm, citySearchTerm, categoryName, offset, limit);
	}

	@Override
	public void onTableAction(TableActionEvent event) {

	}
}