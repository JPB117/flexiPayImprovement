package com.workpoint.icpak.shared.model.events;

import com.workpoint.icpak.shared.model.SerializableObj;

public class BookingSummaryDto extends SerializableObj {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalDelegates;
	private Integer totalActive;
	private Integer totalCancelled;
	private Integer totalPaid;
	private Integer totalUnpaid;
	private Integer totalWithAccomodation;
	private Integer totalWithoutAccomodation;
	private Integer totalAttended;
	private Integer totalUnAttended;

	public BookingSummaryDto() {

	}

}
