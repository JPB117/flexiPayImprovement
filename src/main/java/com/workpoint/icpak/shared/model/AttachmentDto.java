package com.workpoint.icpak.shared.model;

public class AttachmentDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String refId;
	private String attachmentName;

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

}
