package com.workpoint.icpak.client.ui.directory.table.row;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.ui.component.RowWidget;
import com.workpoint.icpak.shared.model.DirectoryDto;

public class DirectoryTableRow extends RowWidget{
	
	private static ActivitiesTableRowUiBinder uiBinder = GWT
			.create(ActivitiesTableRowUiBinder.class);

	interface ActivitiesTableRowUiBinder extends UiBinder<Widget, DirectoryTableRow> {
	}
	
	private DirectoryDto directoryDto;
	
	@UiField
	HTMLPanel row;
	@UiField
	HTMLPanel divFirmName;
	@UiField
	HTMLPanel divAddress;
	@UiField
	HTMLPanel divContacts;
	@UiField
	Label lFirmName;
	@UiField
	Label lPartners;
	@UiField
	Label lAddress1;
	@UiField
	Label lAddress2;
	@UiField
	Label lAddress3;
	@UiField
	Label lTelephone;
	@UiField
	Label lEmail;
	
	public DirectoryTableRow(){
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public DirectoryTableRow(DirectoryDto directoryDto){
		this();
		this.directoryDto = directoryDto;
		lFirmName.setText(directoryDto.getFirmName());
		lPartners.setText(directoryDto.getPartners());
		lAddress1.setText(directoryDto.getAddress1());
		lAddress2.setText(directoryDto.getAddress2());
		lAddress3.setText(directoryDto.getAddress3());
		lTelephone.setText(directoryDto.getTelephone());
		lEmail.setText(directoryDto.getEmail());
		
	}
}
