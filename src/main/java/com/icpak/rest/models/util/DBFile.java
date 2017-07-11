package com.icpak.rest.models.util;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Embeddable
public class DBFile {


	@Basic(fetch=FetchType.LAZY)
	@Lob
	private byte[] attachment;

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}
	
}
