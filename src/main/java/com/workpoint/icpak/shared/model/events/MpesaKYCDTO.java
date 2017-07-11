package com.workpoint.icpak.shared.model.events;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MpesaKYCDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MpesaKYCDTO() {
	}

	/*
	 * "KYCName": "[Personal Details][First Name]", "KYCValue": "TOM"
	 */

	private String KYCName;
	private String KYCValue;

	public String getKycName() {
		return KYCName;
	}

	public void setKycName(String kycName) {
		this.KYCName = kycName;
	}

	public String getKycValue() {
		return KYCValue;
	}

	public void setKycValue(String kycValue) {
		this.KYCValue = kycValue;
	}
}
