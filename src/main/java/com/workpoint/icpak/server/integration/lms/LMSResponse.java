package com.workpoint.icpak.server.integration.lms;

public class LMSResponse {

	private String payload;
	private String message;
	private String status;

	public LMSResponse(String message, String status) {
		this.setMessage(message);
		this.setStatus(status);
	}

	public LMSResponse() {
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
