package com.workpoint.icpak.shared.trx;

import java.io.Serializable;

public enum TrxType implements Serializable{
	
	CR("Credit"),
	DR("DR");
	
	private String type;
	private TrxType(String type){
		this.type = type;
	}
	
	public String getDisplayName(){
		return type;
	}
}
