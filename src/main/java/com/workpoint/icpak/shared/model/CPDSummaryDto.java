package com.workpoint.icpak.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class CPDSummaryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int confirmedCPD = 0;
	private int unconfirmedCPD = 0;

	private int totalArchive = 0;
	private int totalReturns = 0;
	/**
	 * Additional fields for Administrator
	 */
	private int processedCount;
	private int pendingCount;

	public CPDSummaryDto() {
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

	public int getPendingCount() {
		return pendingCount;
	}

	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}

	public int getProcessedCount() {
		return processedCount;
	}

	public void setProcessedCount(int processedCount) {
		this.processedCount = processedCount;
	}

	public int getTotalArchive() {
		return totalArchive;
	}

	public void setTotalArchive(int totalArchive) {
		this.totalArchive = totalArchive;
	}

	public int getTotalReturns() {
		return totalReturns;
	}

	public void setTotalReturns(int totalReturns) {
		this.totalReturns = totalReturns;
	}

}
