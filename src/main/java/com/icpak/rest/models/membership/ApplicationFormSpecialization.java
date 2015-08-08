package com.icpak.rest.models.membership;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.SpecializationCategory;

@Entity
@Table(name="`Application form Specialization`")
public class ApplicationFormSpecialization extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name="`timestamp`",columnDefinition="timestamp NOT NULL default current_timestamp")
	private Timestamp timestamp;
	
	@Column(nullable=true, name="`Line No_`")
	private int lineNo;
	
	@Column(nullable=true, name="`Application No_`", length=20)
	private String applicationNo;
	
	@Column(nullable=true, name="`Code`", length=20)
	private String code;
	
	@Column(nullable=true, name="`Description`", length=50)
	private String description;
	
	@Column(nullable=true, name="`Sector`", length=10)
	private String sector;

	@Column(nullable=true, name="`Sector Desc_`", length=50)
	private String sectorDesc;
	
	@Column(nullable=true, name="`Name of Firm`", length=50)
	private String nameOfFirm;
	 
	@Column(nullable=true, name="`From`", columnDefinition="datetime")
	private Date from;

	@Column(nullable=true, name="`to`", columnDefinition="datetime")
	private String to;
	
	@Column(nullable=true, name="`Position Held`", length=30)
	private String positionHeld;

	@Column(nullable=true, name="`Main clients Handled`", length=50)
	private String mainClientsHandled;
	
	@Column(nullable=true, name="`Address of Firm`", length=50)
	private String addressOfFirm;
	
	@Column(nullable=true, name="`Profesional Function`", length=100)
	private String professionalFunction;
	
	@Column(nullable=true, name="`Customer Name`", length=50)
	private String customerName;
	
	@Column(nullable=true, name="`Up To Date`", columnDefinition="datetime")
	private Date upToDate;
	
	private String specializationName;
	private SpecializationCategory category;
	
	@Column( name = "`ApplicationRefId`", length = 20)
	private String applicationRefId;

	
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

	public ApplicationFormSpecializationDto toDto() {
		ApplicationFormSpecializationDto dto = new ApplicationFormSpecializationDto();
		dto.setRefId(getRefId());
		dto.setCategory(category);
		dto.setSpecializationName(specializationName);
		
		return dto;
	}

	public void copyFrom(ApplicationFormSpecializationDto eduEntry) {
		setCategory(eduEntry.getCategory());
		setSpecializationName(eduEntry.getSpecializationName());
	}

	public String getSpecializationName() {
		return specializationName;
	}

	public void setSpecializationName(String specializationName) {
		this.specializationName = specializationName;
	}

	public SpecializationCategory getCategory() {
		return category;
	}

	public void setCategory(SpecializationCategory category) {
		this.category = category;
	}

	public String getApplicationRefId() {
		return applicationRefId;
	}

	public void setApplicationRefId(String applicationRefId) {
		this.applicationRefId = applicationRefId;
	}
	
}
