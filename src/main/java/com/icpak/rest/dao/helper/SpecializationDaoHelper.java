package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.membership.ApplicationFormEmpSector;
import com.icpak.rest.models.membership.ApplicationFormEmployment;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.ApplicationFormSpecialization;
import com.workpoint.icpak.shared.model.ApplicationFormEducationalDto;
import com.workpoint.icpak.shared.model.ApplicationFormEmploymentDto;
import com.workpoint.icpak.shared.model.ApplicationFormSpecializationDto;
import com.workpoint.icpak.shared.model.EduType;
import com.workpoint.icpak.shared.model.Specializations;

@Transactional
public class SpecializationDaoHelper {

	@Inject
	UsersDao userDao;
	@Inject
	ApplicationFormDao dao;

	public List<ApplicationFormSpecializationDto> getAllSpecializationEntrys(
			String uriInfo, String applicationId, Integer offset, Integer limit) {
		Collection<ApplicationFormSpecialization> list = dao
				.getSpecialization(applicationId);

		List<ApplicationFormSpecializationDto> dtos = new ArrayList<>();
		for (ApplicationFormSpecialization e : list) {
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public List<ApplicationFormEmploymentDto> getAllEmploymentEntrys(
			String uriInfo, String applicationId, Integer offset, Integer limit) {
		Collection<ApplicationFormEmpSector> list = dao
				.getEmployment(applicationId);
		List<ApplicationFormEmploymentDto> dtos = new ArrayList<>();
		for (ApplicationFormEmpSector e : list) {
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public ApplicationFormSpecializationDto getSpecializationEntryById(
			String applicationId, String eduEntryId) {
		ApplicationFormSpecialization eduEntry = dao.findByRefId(eduEntryId,
				ApplicationFormSpecialization.class);
		return eduEntry.toDto();
	}

	public ApplicationFormEmploymentDto getEmploymentEntryById(
			String applicationId, String eduEntryId) {
		ApplicationFormEmpSector eduEntry = dao.findByRefId(eduEntryId,
				ApplicationFormEmpSector.class);
		return eduEntry.toDto();
	}

	public ApplicationFormSpecializationDto createSpecializationEntry(
			String applicationId, ApplicationFormSpecializationDto dto) {
		ApplicationFormHeader application = dao
				.findByApplicationId(applicationId);

		ApplicationFormSpecialization edu = new ApplicationFormSpecialization();
		edu.setApplicationRefId(application.getRefId());
		edu.copyFrom(dto);

		dao.save(edu);

		return edu.toDto();
	}

	public ApplicationFormEmploymentDto createEducationEntry(
			String applicationId, ApplicationFormEmploymentDto dto) {
		ApplicationFormHeader application = dao
				.findByApplicationId(applicationId);

		ApplicationFormEmpSector edu = new ApplicationFormEmpSector();
		edu.setApplicationRefId(application.getRefId());
		edu.copyFrom(dto);

		dao.save(edu);

		return edu.toDto();
	}

	public ApplicationFormSpecializationDto updateSpecializationEntry(
			String applicationId, String eduEntryId,
			ApplicationFormSpecializationDto eduEntry) {
		assert eduEntry.getRefId() != null;

		ApplicationFormSpecialization poSpecializationEntry = dao.findByRefId(
				eduEntryId, ApplicationFormSpecialization.class);
		poSpecializationEntry.copyFrom(eduEntry);
		dao.save(poSpecializationEntry);
		return poSpecializationEntry.toDto();
	}

	public void deleteSpecializationEntry(String applicationId,
			String specializationName) {
		Specializations spec = null;

		try {
			spec = Specializations.valueOf(specializationName);
		} catch (Exception e) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Specialization ",
					"'" + specializationName + "'");
		}

		ApplicationFormSpecialization eduEntry = dao.getSpecializationByName(
				applicationId, spec);
		dao.delete(eduEntry);
	}

	public void deleteEmploymentEntry(String applicationId,
			String specializationName) {
		Specializations spec = null;

		try {
			spec = Specializations.valueOf(specializationName);
		} catch (Exception e) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Specialization ",
					"'" + specializationName + "'");
		}
		ApplicationFormEmpSector eduEntry = dao.getEmploymentByName(
				applicationId, spec);
		dao.delete(eduEntry);
	}

	public List<ApplicationFormSpecializationDto> getAllSpecializationEntrys(
			String uriInfo, String applicationId, EduType academia,
			Integer offset, Integer limit) {
		return new ArrayList<>();
	}

}
