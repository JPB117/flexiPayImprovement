package com.workpoint.icpak.client.ui.members.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.ActionLink;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.members.row.MembersTableRow;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.auth.ApplicationStatus;

public class MembersTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, MembersTable> {
	}

	@UiField
	PagingTable tblView;
	CheckBox selected = null;

	public MembersTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		createHeader();
		tblView.setSearchSectionVisible(true);
		tblView.getDownloadPdf().setVisible(false);
		tblView.setApplicationAndPaymentVisible(true);

		tblView.getLstApplicationStatus().setItems(
				Arrays.asList(ApplicationStatus.values()),
				"-Select Application Status-");
		tblView.getLstPaymentStatus().setItems(
				Arrays.asList(PaymentStatus.values()),
				"-Select Payment Status-");

		tblView.getaSearch().removeStyleName("hide");

	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Registration Date"));
		th.add(new TableHeader("Submitted Date"));
		th.add(new TableHeader("Applicant Name"));
		th.add(new TableHeader("Email"));
		th.add(new TableHeader("Payment Status"));
		th.add(new TableHeader("Application Status"));
		tblView.setTableHeaders(th);
	}

	public void createRow(MembersTableRow row) {
		tblView.addRow(row);
	}

	public void clearRows() {
		tblView.clearRows();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void setAutoNumber(boolean autoNumber) {
		tblView.setAutoNumber(false);
	}

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasKeyDownHandlers getSearchKeyDownHandler() {
		return tblView.getSearchKeyDownHandler();
	}

	public DropDownList<ApplicationStatus> getLstApplicationStatus() {
		return tblView.getLstApplicationStatus();
	}

	public DropDownList<PaymentStatus> getLstPaymentStatus() {
		return tblView.getLstPaymentStatus();
	}

	public ActionLink getaSearch() {
		return tblView.getaSearch();
	}
}
