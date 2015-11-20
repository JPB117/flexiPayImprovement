package com.workpoint.icpak.client.ui.frontmember;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.frontmember.table.FrontMemberTable;
import com.workpoint.icpak.client.ui.frontmember.table.row.FrontMemberTableRow;
import com.workpoint.icpak.shared.model.MemberDto;

public class FrontMemberView extends ViewImpl implements FrontMemberPresenter.MyFrontMemberView {
	interface Binder extends UiBinder<Widget, FrontMemberView> {
	}

	@UiField
	HTMLPanel container;
	@UiField
	FrontMemberTable tblView;

	@Inject
	FrontMemberView(final Binder uiBinder) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void bindResults(List<MemberDto> result) {
		tblView.clearRows();
		tblView.setNoRecords(result.size());
		for (MemberDto dto : result) {
			tblView.createRow(new FrontMemberTableRow(dto));
		}
	}

	@Override
	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public HasValueChangeHandlers<String> getSearchValueChangeHander() {
		return tblView.getSearchKeyDownHander();
	}

	@Override
	public String getSearchValue() {
		return tblView.getSearchValue();
	}

}