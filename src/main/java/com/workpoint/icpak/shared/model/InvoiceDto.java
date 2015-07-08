package com.workpoint.icpak.shared.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceDto extends SerializableObj{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date date;
	private String documentNo;
	private String companyName;
	private String companyAddress;
	private String contactName;
	private String phoneNumber;
	private Double amount;
	private String bookingRefId;
	private List<InvoiceLineDto> lines = new ArrayList<InvoiceLineDto>();
	
	public InvoiceDto() {
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public List<InvoiceLineDto> getLines() {
		return lines;
	}

	public void setLines(List<InvoiceLineDto> lines) {
		this.lines = lines;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void addLine(InvoiceLineDto invoiceLineDto) {
		lines.add(invoiceLineDto);
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public String getBookingRefId() {
		return bookingRefId;
	}

	public void setBookingRefId(String bookingRefId) {
		this.bookingRefId = bookingRefId;
	}
	
	
}
