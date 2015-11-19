package com.workpoint.icpak.client.ui.directory;

import java.util.List;

import javax.inject.Inject;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.directory.table.DirectoryTable;
import com.workpoint.icpak.client.ui.directory.table.row.DirectoryTableRow;
import com.workpoint.icpak.shared.model.DirectoryDto;

public class DirectoryView extends ViewImpl implements DirectoryPresenter.MyDirectoryView {
    interface Binder extends UiBinder<Widget, DirectoryView> {
    }
    
    @UiField
	HTMLPanel container;
    
	@UiField
	DirectoryTable tblView;
    
    private final Widget widget;

    @Inject
    DirectoryView(final Binder uiBinder) {
    	widget = uiBinder.createAndBindUi(this);
    }

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void bindResults(List<DirectoryDto> result) {
		tblView.clearRows();
		tblView.setNoRecords(result.size());
		for (DirectoryDto dto : result) {
			tblView.createRow(new DirectoryTableRow(dto));
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