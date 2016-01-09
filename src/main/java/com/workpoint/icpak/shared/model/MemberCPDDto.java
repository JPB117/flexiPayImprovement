package com.workpoint.icpak.shared.model;

public class MemberCPDDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String memberNo;
	private String memberNames;
	private String customerType;
	private String status;
	private Double year2016;
	private Double year2015;
	private Double year2014;
	private Double year2013;
	private Double year2012;
	private Double year2011;

	public MemberCPDDto() {
	}

	public String getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}

	public String getMemberNames() {
		return memberNames;
	}

	public void setMemberNames(String memberNames) {
		this.memberNames = memberNames;
	}

	public Double getYear2016() {
		return year2016;
	}

	public void setYear2016(Double year2016) {
		this.year2016 = year2016;
	}

	public Double getYear2015() {
		return year2015;
	}

	public void setYear2015(Double year2015) {
		this.year2015 = year2015;
	}

	public Double getYear2014() {
		return year2014;
	}

	public void setYear2014(Double year2014) {
		this.year2014 = year2014;
	}

	public Double getYear2013() {
		return year2013;
	}

	public void setYear2013(Double year2013) {
		this.year2013 = year2013;
	}

	public Double getYear2012() {
		return year2012;
	}

	public void setYear2012(Double year2012) {
		this.year2012 = year2012;
	}

	public Double getYear2011() {
		return year2011;
	}

	public void setYear2011(Double year2011) {
		this.year2011 = year2011;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
