package com.workpoint.icpak.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by wladek on 9/17/15.
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberResponse implements Serializable {

//	{"reg_no":"3002","Practising No_":"P\/1337","E-Mail":"nikita@primebank.co.ke",
//		"Name":"CPA Bhatt Neekita Arunkumar","Address":"P.O. Box 39831","Address 2":"",
//		"Post Code":"00623","Phone No_":"4203145,","Mobile No_":"","Customer Type":"PRAC MEMBER",
//		"Date Registered":"2001-09-20","Practicing Cert Date":"2002-04-18"}

	private String invalidParams;
	private String statusCode;
	private String statusDesc;
	private String status;
	private String transactionIndex;
	private String transactionReference;

	public String getInvalidParams() {
		return invalidParams;
	}

	public void setInvalidParams(String invalidParams) {
		this.invalidParams = invalidParams;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getTransactionIndex() {
		return transactionIndex;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public MemberResponse() {
	}

	@Override
	public String toString() {
		return "CreditCardResponse{" + "invalidParams='" + invalidParams + '\''
				+ ", statusCode='" + statusCode + '\'' + ", statusDesc='"
				+ statusDesc + '\'' + ", status='" + status + '\'' + '}';
	}

	public void setTransactionIndex(String transactionIndex) {
		this.transactionIndex = transactionIndex;
	}

	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
}