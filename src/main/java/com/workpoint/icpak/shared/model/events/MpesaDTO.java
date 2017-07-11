package com.workpoint.icpak.shared.model.events;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class MpesaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MpesaDTO() {
	}

	private String Id;
	private String MSISDN;
	private String BusinessShortCode;
	private String TransID;
	private String TransAmount;
	private String ThirdPartyTransID;
	private String TransTime;
	private String BillRefNumber;
	private String InvoiceNumber;
	private List<MpesaKYCDTO> KYCInfoList;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getMSISDN() {
		return MSISDN;
	}

	public void setMSISDN(String mSISDN) {
		MSISDN = mSISDN;
	}

	public String getBusinessShortCode() {
		return BusinessShortCode;
	}

	public void setBusinessShortCode(String businessShortCode) {
		this.BusinessShortCode = businessShortCode;
	}

	public String getTransID() {
		return TransID;
	}

	public void setTransID(String transID) {
		TransID = transID;
	}

	public String getTransAmount() {
		return TransAmount;
	}

	public void setTransAmount(String transAmount) {
		TransAmount = transAmount;
	}

	public String getThirdPartyTransID() {
		return ThirdPartyTransID;
	}

	public void setThirdPartyTransID(String thirdPartyTransID) {
		ThirdPartyTransID = thirdPartyTransID;
	}

	public String getTransTime() {
		return TransTime;
	}

	public void setTransTime(String transTime) {
		TransTime = transTime;
	}

	public String getBillRefNumber() {
		return BillRefNumber;
	}

	public void setBillRefNumber(String billRefNumber) {
		BillRefNumber = billRefNumber;
	}

	public List<MpesaKYCDTO> getKYCInfoList() {
		return KYCInfoList;
	}

	public void setKYCInfoList(List<MpesaKYCDTO> kYCInfoList) {
		KYCInfoList = kYCInfoList;
	}

	public String getInvoiceNumber() {
		return InvoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		InvoiceNumber = invoiceNumber;
	}

}
