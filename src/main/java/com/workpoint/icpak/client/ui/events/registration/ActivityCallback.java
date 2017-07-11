package com.workpoint.icpak.client.ui.events.registration;

import java.util.List;

import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.BookingDto;

public interface ActivityCallback {

	public void onComplete(List<AccommodationDto> accommodations);
	public void onComplete(BookingDto accommodations);
}
