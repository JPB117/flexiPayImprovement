package com.icpak.rest.models.membership;

public enum BloodGroup {

	ONEG("O−"),
	OPOS("O+"),
	ANEG("A−"),
	APOS("A+"),
	BNEG("B−"),
	BPOS("B+"),
	ABNEG("AB−"),
	ABPOS("AB+");
	
	String type=null;
	
	private BloodGroup(String type) {
		this.type = type;
	}
}
