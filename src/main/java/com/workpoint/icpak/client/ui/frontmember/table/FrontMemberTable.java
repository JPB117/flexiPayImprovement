package com.workpoint.icpak.client.ui.frontmember.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.DropDownList;
import com.workpoint.icpak.client.ui.component.PagingPanel;
import com.workpoint.icpak.client.ui.component.PagingTable;
import com.workpoint.icpak.client.ui.component.TableHeader;
import com.workpoint.icpak.client.ui.component.TableView.Towns;
import com.workpoint.icpak.client.ui.directory.table.DirectoryTable;
import com.workpoint.icpak.client.ui.frontmember.model.MemberCategory;
import com.workpoint.icpak.client.ui.frontmember.table.row.FrontMemberTableRow;

public class FrontMemberTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT
			.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends
			UiBinder<Widget, FrontMemberTable> {
	}

	@UiField
	PagingTable tblView;

	public FrontMemberTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.setSearchSectionVisible(true);
		tblView.setSearchFieldVisible(true);
		tblView.setDatesVisible(false);
		tblView.getDownloadPdf().setVisible(false);

		tblView.setMemberCategoryVisible(true);
		tblView.setTownListVisible(true);
		createHeader();
		setCategories();
		setTowns();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("Member No"));
		th.add(new TableHeader("NAME"));
		th.add(new TableHeader("Category"));
		th.add(new TableHeader("Membership"));
		th.add(new TableHeader("Practising Status"));
		tblView.setTableHeaders(th);
	}

	public void createRow(FrontMemberTableRow row) {
		tblView.addRow(row);
	}

	public void setCategories() {
		// { "MEMBER", "PRACTICING RT","PRAC MEMBER", "FOREIGN", "RETIRED" };

		tblView.setMemberCategories(Arrays.asList(new MemberCategory("MEMBER",
				"MEMBER"), new MemberCategory("PRACTICING RT",
				"Practising-Retired"), new MemberCategory("PRAC MEMBER",
				"Practising"), new MemberCategory("FOREIGN", "Foreign"),
				new MemberCategory("RETIRED", "Retired")));
	}

	public void setTowns() {
		List<Towns> townsList = new ArrayList<>();
		for (int i = 0; i < DirectoryTable.towns.length; i++) {
			Towns town = tblView.new Towns(DirectoryTable.towns[i]);
			townsList.add(town);
		}
		tblView.setTowns(townsList);
	}

	public void clearRows() {
		tblView.clearRows();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void setNoRecords(int size) {
		tblView.setNoRecords(size);
	}

	public PagingPanel getPagingPanel() {
		return tblView.getPagingPanel();
	}

	public String getSearchValue() {
		return tblView.getSearchValue();
	}

	public HasValueChangeHandlers<String> getSearchKeyDownHander() {
		return tblView.getSearchValueChangeHander();
	}

	public DropDownList<Towns> getTownDropDown() {
		return tblView.getTowns();
	}

	public String getSelectedTown() {
		return tblView.getSelectedTownName();
	}

	public DropDownList<MemberCategory> getCategoryDropDown() {
		return tblView.getCategoryDropdown();
	}

	public String getCategorySelected() {
		return tblView.getCategorySelected();
	}

}
