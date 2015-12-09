package com.icpak.rest.models.util;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.membership.ApplicationFormAccountancy;
import com.icpak.rest.models.membership.ApplicationFormEducational;
import com.icpak.rest.models.membership.ApplicationFormEmployment;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationFormTraining;
import com.icpak.rest.models.membership.Education;
import com.icpak.rest.models.membership.GoodStandingCertificate;
import com.icpak.rest.models.membership.TrainingAndExperience;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel(description = "File attachment model")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attachment extends PO {

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private String name;
	private long size;
	private String contentType;

	@Lob
	private byte[] attachment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "goodstandingcertid")
	private GoodStandingCertificate goodStandingCert;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicationEducationId")
	private ApplicationFormEducational applicationEducation;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employmentId")
	private ApplicationFormEmployment employment;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountancyId")
	private ApplicationFormAccountancy applicationAccountancy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "applicationTrainingId")
	private ApplicationFormTraining applicationTraining;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trainingExperienceId")
	private TrainingAndExperience trainingAndExperience;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "educationId")
	private Education education;

	@ManyToOne
	@JoinColumn(name = "cpdid")
	private CPD cpd;

	@ManyToOne
	@JoinColumn(name = "applicationId")
	private ApplicationFormHeader application;

	// Profile Pic
	private String profilePicUserId;

	public Attachment() {
		super();
	}

	public Attachment(Long id, String name, byte[] attachment) {
		this();
		this.name = name;
		this.attachment = attachment;
		setId(id);
	}

	public String getName() {
		return name;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public ApplicationFormAccountancy getApplicationAccountancy() {
		return applicationAccountancy;
	}

	public void setApplicationAccountancy(
			ApplicationFormAccountancy applicationAccountancy) {
		this.applicationAccountancy = applicationAccountancy;
	}

	public Attachment clone(String detail) {
		Attachment a = new Attachment();

		if (detail != null && detail != null) {
			a.setAttachment(attachment);
		}
		a.setContentType(contentType);
		a.setName(name);
		a.setSize(size);
		a.setRefId(refId);

		return a;
	}

	public String getProfilePicUserId() {
		return profilePicUserId;
	}

	public ApplicationFormTraining getApplicationTraining() {
		return applicationTraining;
	}

	public void setApplicationTraining(
			ApplicationFormTraining applicationTraining) {
		this.applicationTraining = applicationTraining;
	}

	public void setProfilePicUserId(String profilePicUserId) {
		this.profilePicUserId = profilePicUserId;
	}

	public CPD getCpd() {
		return cpd;
	}

	public void setCpd(CPD cpd) {
		this.cpd = cpd;
	}

	public GoodStandingCertificate getGoodStandingCert() {
		return goodStandingCert;
	}

	public void setGoodStandingCert(GoodStandingCertificate goodStandingCert) {
		this.goodStandingCert = goodStandingCert;
	}

	public void setApplication(ApplicationFormHeader application) {
		this.application = application;
	}

	public ApplicationFormHeader getApplication() {
		return application;
	}

	public ApplicationFormEducational getEducation() {
		return applicationEducation;
	}

	public void setEducation(ApplicationFormEducational education) {
		this.applicationEducation = education;
	}
}
