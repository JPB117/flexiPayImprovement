package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;

@Entity
@Table(name="`Application form Specialization`")
public class ApplicationFormSpecialization extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="`timestamp`",columnDefinition="timestamp NOT NULL default current_timestamp")
	private Timestamp timestamp;
	
	@Column(nullable=false, name="`Line No_`")
	private int lineNo;
	
	@Column(nullable=false, name="`Application No_`", length=20)
	private String applicationNo;
	
	@Column(nullable=false, name="`Code`", length=20)
	private String code;
	
	@Column(nullable=false, name="`Description`", length=50)
	private String description;
	
	@Column(nullable=false, name="`Sector`", length=10)
	private String sector;

	@Column(nullable=false, name="`Sector Desc_`", length=50)
	private String sectorDesc;
	
	@Column(nullable=false, name="`Name of Firm`", length=50)
	private String nameOfFirm;
	 
	@Column(nullable=false, name="`From`", columnDefinition="datetime")
	private Date from;

	@Column(nullable=false, name="`to`", columnDefinition="datetime")
	private String to;
	
	@Column(nullable=false, name="`Position Held`", length=30)
	private String positionHeld;

	@Column(nullable=false, name="`Main clients Handled`", length=50)
	private String mainClientsHandled;
	
	@Column(nullable=false, name="`Address of Firm`", length=50)
	private String addressOfFirm;
	
	@Column(nullable=false, name="`Profesional Function`", length=100)
	private String professionalFunction;
	
	@Column(nullable=false, name="`Customer Name`", length=50)
	private String customerName;
	
	@Column(nullable=false, name="`Up To Date`", columnDefinition="datetime")
	private Date upToDate;
	
	public ApplicationFormSpecialization() {
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSectorDesc() {
		return sectorDesc;
	}

	public void setSectorDesc(String sectorDesc) {
		this.sectorDesc = sectorDesc;
	}

	public String getNameOfFirm() {
		return nameOfFirm;
	}

	public void setNameOfFirm(String nameOfFirm) {
		this.nameOfFirm = nameOfFirm;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getPositionHeld() {
		return positionHeld;
	}

	public void setPositionHeld(String positionHeld) {
		this.positionHeld = positionHeld;
	}

	public String getMainClientsHandled() {
		return mainClientsHandled;
	}

	public void setMainClientsHandled(String mainClientsHandled) {
		this.mainClientsHandled = mainClientsHandled;
	}

	public String getAddressOfFirm() {
		return addressOfFirm;
	}

	public void setAddressOfFirm(String addressOfFirm) {
		this.addressOfFirm = addressOfFirm;
	}

	public String getProfessionalFunction() {
		return professionalFunction;
	}

	public void setProfessionalFunction(String professionalFunction) {
		this.professionalFunction = professionalFunction;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getUpToDate() {
		return upToDate;
	}

	public void setUpToDate(Date upToDate) {
		this.upToDate = upToDate;
	}
	
}
