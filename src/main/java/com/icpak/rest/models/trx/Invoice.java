package com.icpak.rest.models.trx;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.InvoiceDto;

@Entity
public class Invoice extends PO {

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
	private String memberId; // Invoice owner
	private String description;

	@OneToMany(mappedBy = "invoice", cascade = { CascadeType.PERSIST,
			CascadeType.REMOVE })
	private Set<InvoiceLine> lines = new HashSet<InvoiceLine>();

	public Invoice() {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void addLine(InvoiceLine invoiceLine) {
		lines.add(invoiceLine);
		invoiceLine.setInvoice(this);
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

	public InvoiceDto toDto() {

		InvoiceDto dto = new InvoiceDto();
		dto.setAmount(amount);
		dto.setRefId(getRefId());
		dto.setCompanyAddress(companyAddress);
		dto.setCompanyName(companyName);
		dto.setContactName(contactName);

		if (date != null)
			dto.setDate(date.getTime());

		dto.setDescription(description);

		for (InvoiceLine line : getLines()) {
			dto.addLine(line.toDto());
		}
		dto.setPhoneNumber(phoneNumber);
		dto.setDocumentNo(documentNo);
		return dto;
	}

	public Set<InvoiceLine> getLines() {
		return lines;
	}

	public void setLines(Set<InvoiceLine> lines) {
		this.lines = lines;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Invoice)) {
			return false;
		}

		Invoice other = (Invoice) obj;
		return other.refId.equals(refId);
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 17 + companyName.hashCode();
		hash = hash * 31 + companyAddress.hashCode() + contactName.hashCode();
		hash = hash * 13 + (date == null ? 0 : date.hashCode());
		return hash;
	}

	public void copyFrom(InvoiceDto dto) {
		setAmount(dto.getAmount());
		setCompanyAddress(dto.getCompanyAddress());
		setCompanyName(dto.getCompanyName());
		setContactName(dto.getContactName());
		setDate(dto.getDate()==null? null : new Date(dto.getDate()));
		setPhoneNumber(dto.getPhoneNumber());
		setDocumentNo(dto.getDocumentNo());
		setBookingRefId(dto.getBookingRefId());
		setDescription(dto.getDescription());
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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
