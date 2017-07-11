package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.membership.ApplicationFormAccountancy;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.workpoint.icpak.shared.model.ApplicationFormAccountancyDto;
import com.workpoint.icpak.shared.model.EduType;

@Transactional
public class AccountancyDaoHelper {

	@Inject
	UsersDao userDao;
	@Inject
	ApplicationFormDao dao;

	public List<ApplicationFormAccountancyDto> getAllAccountancyEntrys(
			String uriInfo, String applicationId, Integer offset, Integer limit) {

		Collection<ApplicationFormAccountancy> list = dao
				.getAccountancy(applicationId);
		List<ApplicationFormAccountancyDto> dtos = new ArrayList<>();
		for (ApplicationFormAccountancy e : list) {
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public ApplicationFormAccountancyDto getAccountancyEntryById(
			String applicationId, String eduEntryId) {

		ApplicationFormAccountancy eduEntry = dao.findByRefId(eduEntryId,
				ApplicationFormAccountancy.class);
		return eduEntry.toDto();
	}

	public ApplicationFormAccountancyDto createAccountancyEntry(
			String applicationId, ApplicationFormAccountancyDto dto) {
		ApplicationFormHeader application = dao
				.findByApplicationId(applicationId);
		ApplicationFormAccountancy edu = new ApplicationFormAccountancy();
		edu.setApplicationRefId(application.getRefId());
		edu.copyFrom(dto);
		dao.save(edu);

		return edu.toDto();
	}

	public ApplicationFormAccountancyDto updateAccountancyEntry(
			String applicationId, String eduEntryId,
			ApplicationFormAccountancyDto eduEntry) {
		assert eduEntry.getRefId() != null;

		ApplicationFormAccountancy poAccountancyEntry = dao.findByRefId(
				eduEntryId, ApplicationFormAccountancy.class);
		poAccountancyEntry.copyFrom(eduEntry);
		dao.save(poAccountancyEntry);
		return poAccountancyEntry.toDto();
	}

	public void deleteAccountancyEntry(String applicationId, String eduEntryId) {
		ApplicationFormAccountancy eduEntry = dao.findByRefId(eduEntryId,
				ApplicationFormAccountancy.class);
		dao.delete(eduEntry);
	}

	public List<ApplicationFormAccountancyDto> getAllAccountancyEntrys(
			String uriInfo, String applicationId, EduType academia,
			Integer offset, Integer limit) {
		return new ArrayList<>();
	}

}
