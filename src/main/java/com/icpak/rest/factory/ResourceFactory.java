package com.icpak.rest.factory;

import com.icpak.rest.AccommodationsResourceImpl;
import com.icpak.rest.BookingsResourceImpl;
import com.icpak.rest.CPDResourceImpl;
import com.icpak.rest.EducationResourceImpl;
import com.icpak.rest.EnquiriesResourceImpl;
import com.icpak.rest.EnrollmentsResourceImpl;
import com.icpak.rest.SpecializationsResourceImpl;
import com.icpak.rest.TrainingsResourceImpl;

public interface ResourceFactory {

	public BookingsResourceImpl createBookingResource(String eventId);

	public EducationResourceImpl createEducationResource(String applicationId);

	public TrainingsResourceImpl createTrainingsResource(String applicationId);

	public SpecializationsResourceImpl createSpecializationResource(
			String applicationId);

	public CPDResourceImpl createCPDResource(String memberId);

	public AccommodationsResourceImpl createAccommodationsResource(
			String eventId);

	public EnrollmentsResourceImpl createEnrollmentsResource(String courseId);

	public EnquiriesResourceImpl createEnquiriesResource(String memberId);

	public CPDResourceImpl downloadCert(String cpdRefId);
}
