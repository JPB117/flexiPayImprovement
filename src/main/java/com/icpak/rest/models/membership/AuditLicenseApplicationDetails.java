package com.icpak.rest.models.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.wordnik.swagger.annotations.ApiModel;


@ApiModel(description="Annual Audit License Application Details")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@Table(name="auditlicenceapplication")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class AuditLicenseApplicationDetails extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(nullable=false)
	private int staffCount;
	
	@Column(nullable=false)
	private int annualAuditFees;
	
	@Column(nullable=false)
	private int propAnnualFeesAudit; //Proportion of annual audit fees billed to total billings
	private int topFiveAuditFeesProp;//Proportion of fees charged to top five customers to toal billings
	
	@Column(nullable=false)
	private int isICPAKReviewDone; //1=true, 0=false;
	private Date lastICPAKAuditDate;
	private int reviewIssued;
	private int preferredReviewQtr=0;
	private int isMemberOfOtherProfessionalFirms;
	
	@Column(length=500)
	private String otherProfessionalBodies;
	private MembershipStatus memberShipStatus;
	private int isNonPractice;
	
	@Column(length=500)
	private String nonPracticeReason;
	
	@Column(columnDefinition="int default 1")
	private int complianceNoDisplinaryIssues;
	
	@Column(columnDefinition="int default 1")
	private int complianceCPD;
	
	@Column(columnDefinition="int default 1")
	private int complianceIdenmnity;
	
	@Column(columnDefinition="int default 1")
	private int complianceAntiMoneyLaudering;
	
	@OneToOne
	@JoinColumn(name="applicationid")
	private Application application;

	private String practiceId;
	
	@Embedded
	private Compliance compliance;
	
	public AuditLicenseApplicationDetails() {
	}

	public int getStaffCount() {
		return staffCount;
	}

	public void setStaffCount(int staffCount) {
		this.staffCount = staffCount;
	}

	public int getAnnualAuditFees() {
		return annualAuditFees;
	}

	public void setAnnualAuditFees(int annualAuditFees) {
		this.annualAuditFees = annualAuditFees;
	}

	public int getPropAnnualFeesAudit() {
		return propAnnualFeesAudit;
	}

	public void setPropAnnualFeesAudit(int propAnnualFeesAudit) {
		this.propAnnualFeesAudit = propAnnualFeesAudit;
	}

	public int getTopFiveAuditFeesProp() {
		return topFiveAuditFeesProp;
	}

	public void setTopFiveAuditFeesProp(int topFiveAuditFeesProp) {
		this.topFiveAuditFeesProp = topFiveAuditFeesProp;
	}

	public int getIsICPAKReviewDone() {
		return isICPAKReviewDone;
	}

	public void setIsICPAKReviewDone(int isICPAKReviewDone) {
		this.isICPAKReviewDone = isICPAKReviewDone;
	}

	public Date getLastICPAKAuditDate() {
		return lastICPAKAuditDate;
	}

	public void setLastICPAKAuditDate(Date lastICPAKAuditDate) {
		this.lastICPAKAuditDate = lastICPAKAuditDate;
	}

	public int getReviewIssued() {
		return reviewIssued;
	}

	public void setReviewIssued(int reviewIssued) {
		this.reviewIssued = reviewIssued;
	}

	public int getPreferredReviewQtr() {
		return preferredReviewQtr;
	}

	public void setPreferredReviewQtr(int preferredReviewQtr) {
		this.preferredReviewQtr = preferredReviewQtr;
	}

	public int getIsMemberOfOtherProfessionalFirms() {
		return isMemberOfOtherProfessionalFirms;
	}

	public void setIsMemberOfOtherProfessionalFirms(
			int isMemberOfOtherProfessionalFirms) {
		this.isMemberOfOtherProfessionalFirms = isMemberOfOtherProfessionalFirms;
	}

	public String getOtherProfessionalBodies() {
		return otherProfessionalBodies;
	}

	public void setOtherProfessionalBodies(String otherProfessionalBodies) {
		this.otherProfessionalBodies = otherProfessionalBodies;
	}

	public MembershipStatus getMemberShipStatus() {
		return memberShipStatus;
	}

	public void setMemberShipStatus(MembershipStatus memberShipStatus) {
		this.memberShipStatus = memberShipStatus;
	}

	public int getIsNonPractice() {
		return isNonPractice;
	}

	public void setIsNonPractice(int isNonPractice) {
		this.isNonPractice = isNonPractice;
	}

	public String getNonPracticeReason() {
		return nonPracticeReason;
	}

	public void setNonPracticeReason(String nonPracticeReason) {
		this.nonPracticeReason = nonPracticeReason;
	}

	public int getComplianceNoDisplinaryIssues() {
		return complianceNoDisplinaryIssues;
	}

	public void setComplianceNoDisplinaryIssues(int complianceNoDisplinaryIssues) {
		this.complianceNoDisplinaryIssues = complianceNoDisplinaryIssues;
	}

	public int getComplianceCPD() {
		return complianceCPD;
	}

	public void setComplianceCPD(int complianceCPD) {
		this.complianceCPD = complianceCPD;
	}

	public int getComplianceIdenmnity() {
		return complianceIdenmnity;
	}

	public void setComplianceIdenmnity(int complianceIdenmnity) {
		this.complianceIdenmnity = complianceIdenmnity;
	}

	public int getComplianceAntiMoneyLaudering() {
		return complianceAntiMoneyLaudering;
	}

	public void setComplianceAntiMoneyLaudering(int complianceAntiMoneyLaudering) {
		this.complianceAntiMoneyLaudering = complianceAntiMoneyLaudering;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public String getPracticeId() {
		return practiceId;
	}

	public void setPracticeId(String practiceId) {
		this.practiceId = practiceId;
	}

	public void copy(AuditLicenseApplicationDetails auditDetails) {
		setAnnualAuditFees(auditDetails.getAnnualAuditFees());
		setComplianceAntiMoneyLaudering(auditDetails.getComplianceAntiMoneyLaudering());
		setComplianceCPD(auditDetails.getComplianceCPD());
		setComplianceIdenmnity(auditDetails.getComplianceIdenmnity());
		setComplianceNoDisplinaryIssues(auditDetails.getComplianceNoDisplinaryIssues());
		setIsICPAKReviewDone(auditDetails.getIsICPAKReviewDone());
		setIsMemberOfOtherProfessionalFirms(auditDetails.getIsMemberOfOtherProfessionalFirms());
		setIsNonPractice(auditDetails.getIsNonPractice());
		setLastICPAKAuditDate(auditDetails.getLastICPAKAuditDate());
		setMemberShipStatus(auditDetails.getMemberShipStatus());
		setNonPracticeReason(auditDetails.getNonPracticeReason());
		setOtherProfessionalBodies(auditDetails.getOtherProfessionalBodies());
		setPracticeId(auditDetails.getPracticeId());
		setPreferredReviewQtr(auditDetails.getPreferredReviewQtr());
		setPropAnnualFeesAudit(auditDetails.getPropAnnualFeesAudit());
		setReviewIssued(auditDetails.getReviewIssued());
		setStaffCount(auditDetails.getStaffCount());
		setTopFiveAuditFeesProp(auditDetails.getTopFiveAuditFeesProp());

	}
	
}
