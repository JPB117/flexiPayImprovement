package com.workpoint.icpak.shared.model.events;

import com.workpoint.icpak.shared.model.SerializableObj;

public class BookingSummaryDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalDelegates;
	private Integer totalCancelled;
	private Integer totalPaid;
	private Integer totalWithAccomodation;
	private Integer totalAttended;
	private Integer totalMpesaPayments;
	private Integer totalCardsPayment;
	private Integer totalOfflinePayment;
	private Integer totalCredit;
	private Integer totalReceiptPayment;
	private Integer totalMembers;
	private Integer totalNonMembers;
	private Integer totalPaidMembers;
	private Integer totalPaidNonMembers;
	private Integer totalCancelledMembers;
	private Integer totalCancelledNonMembers;

	public BookingSummaryDto() {

	}

	public Integer getTotalDelegates() {
		return totalDelegates;
	}

	public Integer getTotalNonMembers() {
		return totalNonMembers;
	}

	public void setTotalNonMembers(Integer totalNonMembers) {
		this.totalNonMembers = totalNonMembers;
	}

	public void setTotalDelegates(Integer totalDelegates) {
		this.totalDelegates = totalDelegates;
	}

	public Integer getTotalPaidNonMembers() {
		return totalPaidNonMembers;
	}

	public void setTotalPaidNonMembers(Integer totalPaidNonMembers) {
		this.totalPaidNonMembers = totalPaidNonMembers;
	}

	public Integer getTotalCancelled() {
		return totalCancelled;
	}

	public void setTotalCancelled(Integer totalCancelled) {
		this.totalCancelled = totalCancelled;
	}

	public Integer getTotalPaid() {
		return totalPaid;
	}

	public void setTotalPaid(Integer totalPaid) {
		this.totalPaid = totalPaid;
	}

	public Integer getTotalWithAccomodation() {
		return totalWithAccomodation;
	}

	public void setTotalWithAccomodation(Integer totalWithAccomodation) {
		this.totalWithAccomodation = totalWithAccomodation;
	}

	public Integer getTotalAttended() {
		return totalAttended;
	}

	public void setTotalAttended(Integer totalAttended) {
		this.totalAttended = totalAttended;
	}

	public Integer getTotalMpesaPayments() {
		return totalMpesaPayments;
	}

	public void setTotalMpesaPayments(Integer totalMpesaPayments) {
		this.totalMpesaPayments = totalMpesaPayments;
	}

	public Integer getTotalCardsPayment() {
		return totalCardsPayment;
	}

	public void setTotalCardsPayment(Integer totalCardsPayment) {
		this.totalCardsPayment = totalCardsPayment;
	}

	public Integer getTotalOfflinePayment() {
		return totalOfflinePayment;
	}

	public void setTotalOfflinePayment(Integer totalOfflinePayment) {
		this.totalOfflinePayment = totalOfflinePayment;
	}

	public Integer getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Integer totalCredit) {
		this.totalCredit = totalCredit;
	}

	public Integer getTotalReceiptPayment() {
		return totalReceiptPayment;
	}

	public void setTotalReceiptPayment(Integer totalReceiptPayment) {
		this.totalReceiptPayment = totalReceiptPayment;
	}

	public Integer getTotalMembers() {
		return totalMembers;
	}

	public void setTotalMembers(Integer totalMembers) {
		this.totalMembers = totalMembers;
	}

	public Integer getTotalPaidMembers() {
		return totalPaidMembers;
	}

	public void setTotalPaidMembers(Integer totalPaidMembers) {
		this.totalPaidMembers = totalPaidMembers;
	}

	public Integer getTotalCancelledMembers() {
		return totalCancelledMembers;
	}

	public void setTotalCancelledMembers(Integer totalCancelledMembers) {
		this.totalCancelledMembers = totalCancelledMembers;
	}

	public Integer getTotalCancelledNonMembers() {
		return totalCancelledNonMembers;
	}

	public void setTotalCancelledNonMembers(Integer totalCancelledNonMembers) {
		this.totalCancelledNonMembers = totalCancelledNonMembers;
	}

}
