package com.workpoint.icpak.client.ui.enquiries;

import java.util.List;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.enquiries.table.EnquiriesTable;
import com.workpoint.icpak.client.ui.enquiries.table.row.EnquiriesTableRow;
import com.workpoint.icpak.shared.model.EnquiriesDto;

public class EnquiriesView extends ViewImpl implements
		EnquiriesPresenter.IEnquiriesView {

	private final Widget widget;

	@UiField
	HTMLPanel container;
	@UiField
	HTMLPanel panelListing;
	@UiField
	ActionLink aCreate;

	@UiField
	EnquiriesTable tblView;

	public interface Binder extends UiBinder<Widget, EnquiriesView> {
	}

	@Inject
	public EnquiriesView(final Binder binder) {
		widget = binder.createAndBindUi(this);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HasClickHandlers getCreateButton() {
		return aCreate;
	}

	@Override
	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	@Override
	public void bindEnquiries(List<EnquiriesDto> list) {
		tblView.clearRows();
		for (EnquiriesDto dto : list) {
			tblView.createRow(new EnquiriesTableRow(dto));
		}

	}

}
