package com.workpoint.icpak.shared.model;

import java.io.Serializable;

public class EventSummaryDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer open;
	public Integer closed;
	public Integer upcoming;
	
	public EventSummaryDto() {
	}

	public Integer getClosed() {
		return closed;
	}

	public void setClosed(Integer closed) {
		this.closed = closed;
	}

	public Integer getUpcoming() {
		return upcoming;
	}

	public void setUpcoming(Integer upcoming) {
		this.upcoming = upcoming;
	}

	public Integer getOpen() {
		return open;
	}

	public void setOpen(Integer open) {
		this.open = open;
	}
	
}
