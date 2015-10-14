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
import com.icpak.rest.models.membership.Education;
import com.icpak.rest.models.membership.GoodStandingCertificate;
import com.icpak.rest.models.membership.TrainingAndExperience;
import com.wordnik.swagger.annotations.ApiModel;



@ApiModel(description="File attachment model")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)

@Entity
@Table(name="attachment")
@Cache(usage= CacheConcurrencyStrategy.READ_WRITE)
public class Attachment extends PO{

	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	private String name;
	private long size;
	private String contentType;

	@Lob
	private byte[] attachment;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="goodstandingcertid")
	private GoodStandingCertificate goodStandingCert;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="educationid")
	private Education education;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="trainingExperienceId")
	private TrainingAndExperience trainingAndExperience;
	
	//Profile Pic
	private String profilePicUserId;
	
	@ManyToOne
	@JoinColumn(name="cpdid")
	private CPD cpd;
	
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

	public Attachment clone(String detail) {
		Attachment a = new Attachment();
		
		if(detail!=null && detail!=null){
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
}
