package com.icpak.rest.models.erpModels;

public class ErpReceiptHeader {
	private String amount_received;
	private String received_from;

	public String getAmount_received() {
		return amount_received;
	}

	public void setAmount_received(String amount_received) {
		this.amount_received = amount_received;
	}

	public String getReceived_from() {
		return received_from;
	}

	public void setReceived_from(String received_from) {
		this.received_from = received_from;
	}
}
