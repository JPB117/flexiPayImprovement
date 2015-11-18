package com.workpoint.icpak.shared.model;

public class DirectoryDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String firmId;
	private String firmName;
	private String address1;
	private String address2;
	private String address3;
	private String typeOfFirm;
	private String city;
	private String telephone;
	private String fax;
	private String email;
	private String paidup;
	private String sector;
	private String partners;
	private String regno;
	private String branch;

	public DirectoryDto() {
	}

	public String getFirmId() {
		return firmId;
	}

	public void setFirmId(String firmId) {
		this.firmId = firmId;
	}

	public String getFirmName() {
		return firmName;
	}

	public void setFirmName(String firmName) {
		this.firmName = firmName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getTypeOfFirm() {
		return typeOfFirm;
	}

	public void setTypeOfFirm(String typeOfFirm) {
		this.typeOfFirm = typeOfFirm;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPaidup() {
		return paidup;
	}

	public void setPaidup(String paidup) {
		this.paidup = paidup;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getPartners() {
		return partners;
	}

	public void setPartners(String partners) {
		this.partners = partners;
	}

	public String getRegno() {
		return regno;
	}

	public void setRegno(String regno) {
		this.regno = regno;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

}
