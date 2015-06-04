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
import javax.xml.bind.annotation.XmlSeeAlso;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.icpak.rest.models.auth.BioData;
import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.membership.Education;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.membership.TrainingAndExperience;
import com.wordnik.swagger.annotations.ApiModel;



@ApiModel(description="File attachment model")

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Member.class})

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
	@JoinColumn(name="educationid")
	private Education education;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="trainingExperienceId")
	private TrainingAndExperience trainingAndExperience;
	
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
}
