package com.icpak.rest.models.membership;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

@ApiModel(description="Applications Made by a member")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

@Entity
@Table(name="application")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Application extends PO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applicationNo;
	private ApplicationType applicationType;
	private ApplicationStatus status;
	private Date submissionDate;
	private String fileNo;
	private String receiptNo;
	private String receiptDate;
	private Date dateReceived;
	private String approvalMinNo;
	private String defferalMinNo;
	private Date dateNotificationSent;
	private String registrationNo;
	private Date registrationDate;
	private IndustrySector emplSector;
	private String gazetteNoticeNo;
	private boolean acknowledged;
	private Date dateAcknowledge;
	private MemberStanding memberStanding;
	private Date licenceCollectedOrDispatched;
	private boolean isStopPractice;
	@Column(length=500)
	private String reasonForStopPractice;

	@OneToOne(mappedBy="application")
	private AuditLicenseApplicationDetails auditDetails;
	
	@ManyToOne
	@JoinColumn(name="member_id")
	private Member member;

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public ApplicationType getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(ApplicationType applicationType) {
		this.applicationType = applicationType;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	public Date getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(Date submissionDate) {
		this.submissionDate = submissionDate;
	}

	public String getFileNo() {
		return fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(Date dateReceived) {
		this.dateReceived = dateReceived;
	}

	public String getApprovalMinNo() {
		return approvalMinNo;
	}

	public void setApprovalMinNo(String approvalMinNo) {
		this.approvalMinNo = approvalMinNo;
	}

	public String getDefferalMinNo() {
		return defferalMinNo;
	}

	public void setDefferalMinNo(String defferalMinNo) {
		this.defferalMinNo = defferalMinNo;
	}

	public Date getDateNotificationSent() {
		return dateNotificationSent;
	}

	public void setDateNotificationSent(Date dateNotificationSent) {
		this.dateNotificationSent = dateNotificationSent;
	}

	public String getRegistrationNo() {
		return registrationNo;
	}

	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public IndustrySector getEmplSector() {
		return emplSector;
	}

	public void setEmplSector(IndustrySector emplSector) {
		this.emplSector = emplSector;
	}

	public String getGazetteNoticeNo() {
		return gazetteNoticeNo;
	}

	public void setGazetteNoticeNo(String gazetteNoticeNo) {
		this.gazetteNoticeNo = gazetteNoticeNo;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public Date getDateAcknowledge() {
		return dateAcknowledge;
	}

	public void setDateAcknowledge(Date dateAcknowledge) {
		this.dateAcknowledge = dateAcknowledge;
	}

	public MemberStanding getMemberStanding() {
		return memberStanding;
	}

	public void setMemberStanding(MemberStanding memberStanding) {
		this.memberStanding = memberStanding;
	}

	public Date getLicenceCollectedOrDispatched() {
		return licenceCollectedOrDispatched;
	}

	public void setLicenceCollectedOrDispatched(Date licenceCollectedOrDispatched) {
		this.licenceCollectedOrDispatched = licenceCollectedOrDispatched;
	}

	public boolean isStopPractice() {
		return isStopPractice;
	}

	public void setStopPractice(boolean isStopPractice) {
		this.isStopPractice = isStopPractice;
	}

	public AuditLicenseApplicationDetails getAuditDetails() {
		return auditDetails;
	}

	public void setAuditDetails(AuditLicenseApplicationDetails auditDetails) {
		this.auditDetails = auditDetails;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Application clone(String ... token) {
		return this;
	}

	public String getReasonForStopPractice() {
		return reasonForStopPractice;
	}

	public void setReasonForStopPractice(String reasonForStopPractice) {
		this.reasonForStopPractice = reasonForStopPractice;
	}
}
