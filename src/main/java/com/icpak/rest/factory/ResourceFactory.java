package com.icpak.rest.factory;

import com.icpak.rest.BookingsResourceImpl;
import com.icpak.rest.EducationResourceImpl;

public interface ResourceFactory {

	public BookingsResourceImpl createBookingResource(String eventId);
	public EducationResourceImpl createEducationResource(String applicationId);
}
