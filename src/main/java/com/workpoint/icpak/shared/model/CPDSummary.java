package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public class CPDSummary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int confirmedCPD=0;
	private int unconfirmedCPD=0;
	
	public CPDSummary() {
	}

	public int getConfirmedCPD() {
		return confirmedCPD;
	}

	public void setConfirmedCPD(int confirmedCPD) {
		this.confirmedCPD = confirmedCPD;
	}

	public int getUnconfirmedCPD() {
		return unconfirmedCPD;
	}

	public void setUnconfirmedCPD(int unconfirmedCPD) {
		this.unconfirmedCPD = unconfirmedCPD;
	}
	
}
