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
	public int open;
	public int closed;
	
	public EventSummaryDto() {
	}

	public int getClosed() {
		return closed;
	}

	public void setClosed(int closed) {
		this.closed = closed;
	}

	public int getOpen() {
		return open;
	}

	public void setOpen(int open) {
		this.open = open;
	}
	
}
