package com.icpak.rest.factory;

import com.icpak.rest.AccommodationsResourceImpl;
import com.icpak.rest.BookingsResourceImpl;
import com.icpak.rest.CPDResourceImpl;
import com.icpak.rest.EducationResourceImpl;
import com.icpak.rest.EnrollmentsResourceImpl;

public interface ResourceFactory {

	public BookingsResourceImpl createBookingResource(String eventId);
	public EducationResourceImpl createEducationResource(String applicationId);
	public CPDResourceImpl createCPDResource(String memberId);
	public AccommodationsResourceImpl createAccommodationsResource(String eventId);
	public EnrollmentsResourceImpl createEnrollmentsResource(String courseId);
}
