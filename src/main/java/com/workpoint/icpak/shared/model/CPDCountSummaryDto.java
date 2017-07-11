package com.workpoint.icpak.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CPDCountSummaryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int approvedCPD=0;
	private int pendingCPD=0;
	
	public CPDCountSummaryDto() {
	}

	public int getApprovedCPD() {
		return approvedCPD;
	}

	public void setApprovedCPD(int approvedCPD) {
		this.approvedCPD = approvedCPD;
	}

	public int getPendingCPD() {
		return pendingCPD;
	}

	public void setPendingCPD(int pendingCPD) {
		this.pendingCPD = pendingCPD;
	}
	
}
