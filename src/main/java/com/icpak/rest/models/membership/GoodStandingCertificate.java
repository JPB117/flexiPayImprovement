package com.icpak.rest.models.membership;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.icpak.rest.models.base.PO;
import com.icpak.rest.models.util.Attachment;

@Entity
public class GoodStandingCertificate extends PO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String refNo;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="member_id")
	private Member member;

	@OneToMany(mappedBy="goodStandingCert",
			cascade={CascadeType.DETACH, CascadeType.REMOVE})
	private List<Attachment> attachment = new ArrayList<>();

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public List<Attachment> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Attachment> attachment) {
		this.attachment = attachment;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
