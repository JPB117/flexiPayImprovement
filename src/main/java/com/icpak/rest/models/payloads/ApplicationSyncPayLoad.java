package com.icpak.rest.models.payloads;

public class ApplicationSyncPayLoad {

	// [{"reg_no":"19017","Application
	// No_":"C\/19522","email":"EMMKIP06@GMAIL.COM"}]

	String reg_no;
	String ApplicationNo_;
	String email;

	public String getReg_no() {
		return reg_no;
	}

	public void setReg_no(String reg_no) {
		this.reg_no = reg_no;
	}

	public String getApplicationNo_() {
		return ApplicationNo_;
	}

	public void setApplicationNo_(String applicationNo_) {
		ApplicationNo_ = applicationNo_;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
