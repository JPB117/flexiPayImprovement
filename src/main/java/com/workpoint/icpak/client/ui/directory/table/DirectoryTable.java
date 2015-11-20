package com.workpoint.icpak.client.ui.directory.table;

import java.util.ArrayList;
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
import com.workpoint.icpak.client.ui.directory.table.row.DirectoryTableRow;

public class DirectoryTable extends Composite {

	private static TransactionTableUiBinder uiBinder = GWT.create(TransactionTableUiBinder.class);

	interface TransactionTableUiBinder extends UiBinder<Widget, DirectoryTable> {
	}

	@UiField
	PagingTable tblView;
	
	private String[] towns = { "all", "Nairobi", "BONDO", "Bungoma", "BURU BURU, NAIROBI", "Busia", "City Square",
			"Eldoret", "EMBU", "GPO NAIROBI", "Juba", "KAKAMEGA", "Kapsabet", "Karatina", "Kericho", "Kerugoya",
			"KIAMBU", "Kigali", "Kigali, Rwanda", "KISERIAN", "Kisii", "Kisumu", "Kitale", "Kitui", "KNH", "Lamu",
			"Limuru", "Luanda", "Machakos", "Malindi", "Meru", "Mombasa", "Mumias", "Murang'a", "Muranga", "Nairobi",
			"Nairobi,Kenya", "Naivasha", "Nakuru", "Nanyuki", "Naro Moru", "NGARA NAIROBI", "Niarobi", "Nyahururu",
			"Nyamira", "Nyeri", "OLKALOU", "RUARAKA, NAIROBI", "Ruaraka- Nairobi", "SARIT CENTRE", "Siaya", "SOTIK",
			"Suna", "Tala", "Thika", "Ugunja", "Vienna Australia", "Voi", "Webuye" };

	public DirectoryTable() {
		initWidget(uiBinder.createAndBindUi(this));
		tblView.setAutoNumber(false);
		tblView.setSearchSectionVisible(true);
		tblView.setSearchFieldVisible(true);
		tblView.setDatesVisible(false);
		tblView.getDownloadPdf().setVisible(false);
		tblView.setTownListVisible(true);
		setTowns(towns);
		createHeader();
	}

	public void createHeader() {
		List<TableHeader> th = new ArrayList<TableHeader>();
		th.add(new TableHeader("NAME OF FIRM / PARTNERS"));
		th.add(new TableHeader("ADDRESS"));
		th.add(new TableHeader("CONTACTS"));
		tblView.setTableHeaders(th);
	}

	public void createRow(DirectoryTableRow row) {
		tblView.addRow(row);
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
		return tblView.getSearchKeyDownHander();
	}
	
	public void setTowns(String[] towns){
		List<Towns> townsList = new ArrayList<>();
		for(int i = 0 ; i < towns.length ; i++){
			Towns town = tblView.new Towns(towns[i]);
			townsList.add(town);
		}
	    tblView.setTowns(townsList);
	}
	
	public DropDownList<Towns> getTowns(){
		return tblView.getTowns();
	}

	public String getSelectedTownName() {
		return tblView.getSelectedTownName();
	}

}
