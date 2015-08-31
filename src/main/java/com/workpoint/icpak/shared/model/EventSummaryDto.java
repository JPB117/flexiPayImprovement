package com.workpoint.icpak.shared.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class EventSummaryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer open;
	public Integer closed;
	
	public EventSummaryDto() {
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}
	
}
