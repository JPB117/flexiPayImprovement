package com.workpoint.icpak.shared.model.statement;

import com.workpoint.icpak.shared.model.SerializableObj;

/**
 * Created by wladek on 9/18/15.
 */
public class StatementSummaryDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Double totalDebit;
	private Double totalCredit;
	private Double totalBalance;

	public Double getTotalDebit() {
		return totalDebit;
	}

	public void setTotalDebit(Double totalDebit) {
		this.totalDebit = totalDebit;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}

	public Double getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Double totalBalance) {
		this.totalBalance = totalBalance;
	}
}