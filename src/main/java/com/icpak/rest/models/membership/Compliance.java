package com.icpak.rest.models.membership;

import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description = "Audit Compliance in audit application")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({ Member.class })

@Embeddable
public class Compliance{

	private int isContinuityArrangementsMade;
	private String partnershipAgreementAttachmentRef;
	private String agreementAttachmentRef;
	private int commitmentToICPAKAuditReview;
	private String commitmentToICPAKEthics;
	private String acknowledgementToPublicDuty;
	private String acknowledgementOfLiability;
	private int insuranceAttachmentRef;
	private int commitmentToCollectAndDisplayLicence;
	
	public Compliance() {
	}

	public int getIsContinuityArrangementsMade() {
		return isContinuityArrangementsMade;
	}

	public void setIsContinuityArrangementsMade(int isContinuityArrangementsMade) {
		this.isContinuityArrangementsMade = isContinuityArrangementsMade;
	}

	public String getPartnershipAgreementAttachmentRef() {
		return partnershipAgreementAttachmentRef;
	}

	public void setPartnershipAgreementAttachmentRef(
			String partnershipAgreementAttachmentRef) {
		this.partnershipAgreementAttachmentRef = partnershipAgreementAttachmentRef;
	}

	public String getAgreementAttachmentRef() {
		return agreementAttachmentRef;
	}

	public void setAgreementAttachmentRef(String agreementAttachmentRef) {
		this.agreementAttachmentRef = agreementAttachmentRef;
	}

	public int getCommitmentToICPAKAuditReview() {
		return commitmentToICPAKAuditReview;
	}

	public void setCommitmentToICPAKAuditReview(int commitmentToICPAKAuditReview) {
		this.commitmentToICPAKAuditReview = commitmentToICPAKAuditReview;
	}

	public String getCommitmentToICPAKEthics() {
		return commitmentToICPAKEthics;
	}

	public void setCommitmentToICPAKEthics(String commitmentToICPAKEthics) {
		this.commitmentToICPAKEthics = commitmentToICPAKEthics;
	}

	public String getAcknowledgementToPublicDuty() {
		return acknowledgementToPublicDuty;
	}

	public void setAcknowledgementToPublicDuty(String acknowledgementToPublicDuty) {
		this.acknowledgementToPublicDuty = acknowledgementToPublicDuty;
	}

	public String getAcknowledgementOfLiability() {
		return acknowledgementOfLiability;
	}

	public void setAcknowledgementOfLiability(String acknowledgementOfLiability) {
		this.acknowledgementOfLiability = acknowledgementOfLiability;
	}

	public int getInsuranceAttachmentRef() {
		return insuranceAttachmentRef;
	}

	public void setInsuranceAttachmentRef(int insuranceAttachmentRef) {
		this.insuranceAttachmentRef = insuranceAttachmentRef;
	}

	public int getCommitmentToCollectAndDisplayLicence() {
		return commitmentToCollectAndDisplayLicence;
	}

	public void setCommitmentToCollectAndDisplayLicence(
			int commitmentToCollectAndDisplayLicence) {
		this.commitmentToCollectAndDisplayLicence = commitmentToCollectAndDisplayLicence;
	}
}
