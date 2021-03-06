package com.workpoint.icpak.shared.model;


public enum DataType {
	STRING,
	STRINGLONG,
	BOOLEAN,
	INTEGER,
	DOUBLE,
	DATE,
	CHECKBOX,
	MULTIBUTTON,
	SELECTBASIC,
	SELECTAUTOCOMPLETE,
	SELECTMULTIPLE,
	LABEL,
	BUTTON,
	LAYOUTHR,
	GRID,
	COLUMNPROPERTY, 
	FILEUPLOAD, FORM, LINK;
	
	public boolean isDropdown(){
		return this.equals(SELECTBASIC);
	}
	//GRID;
	
	public boolean isLookup(){
		return this.equals(SELECTBASIC) || this.equals(SELECTMULTIPLE) || this.equals(BOOLEAN) || this.equals(SELECTAUTOCOMPLETE);
	}
}