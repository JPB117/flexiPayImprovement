package com.icpak.rest.dao.helper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.mail.MessagingException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.EnquiriesDao;
import com.icpak.rest.dao.EnquiriesDialogueDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.util.Enquiries;
import com.icpak.rest.models.util.EnquiriesDialogue;
import com.icpak.rest.utils.EmailServiceHelper;
import com.workpoint.icpak.shared.model.EnquiriesDto;

@Transactional
public class EnquiriesDaoHelper {

	Logger logger = Logger.getLogger(EnquiriesDaoHelper.class);

	@Inject
	EnquiriesDao dao;

	@Inject
	EnquiriesDialogueDao enquiriesDialogueDao;

	@Inject
	RolesDao rolesDao;

	@Inject
	EmailServiceHelper emailServiceHelper;

	public List<EnquiriesDto> getAll(String memberRefId, Integer offset, Integer limit) {

		List<Enquiries> list = null;
		if (memberRefId.equals("ALL")) {
			list = dao.getAllEnquiries(null, offset, limit);
		} else {
			list = dao.getAllEnquiries(memberRefId, offset, limit);
		}

		List<EnquiriesDto> dtos = new ArrayList<>();
		for (Enquiries e : list) {
			dtos.add(e.toDto());
		}
		return dtos;
	}

	public EnquiriesDto create(String memberRefId, EnquiriesDto dto) {
		dto.setMemberRefId(memberRefId);
		Enquiries enquiries = new Enquiries();
		enquiries.copyFrom(dto);
		if (memberRefId.equals("ALL")) {
			dto.setMemberRefId(null);
		}
		dao.save(enquiries);

		sendEnquiryMail(enquiries);

		return enquiries.toDto();
	}

	public EnquiriesDto update(String memberRefId, String enquiriesRefId, EnquiriesDto dto) {
		dto.setMemberRefId(memberRefId);
		Enquiries enquiries = dao.findByRefId(enquiriesRefId, Enquiries.class);
		enquiries.copyFrom(dto);
		dao.save(enquiries);

		return enquiries.toDto();
	}

	public Integer getCount(String memberRefId) {
		if (memberRefId.equals("ALL")) {
			return dao.getCount(null);
		}

		return dao.getCount(memberRefId);
	}

	public void delete(String memberId, String enquiryId) {
		Enquiries e = dao.findByRefId(enquiryId, Enquiries.class);
		dao.delete(e);
	}

	public String respondToInquiry(String enquiryRefId, String memberRefId, String responseMessage) {
		String result = null;

		Enquiries enquiryInDb = dao.findByRefId(enquiryRefId, Enquiries.class);

		EnquiriesDialogue newEnquiriesDialogue = new EnquiriesDialogue();
		newEnquiriesDialogue.setFromMemberRefId(memberRefId);
		newEnquiriesDialogue.setText(responseMessage);
		newEnquiriesDialogue.setEnquiry(enquiryInDb);

		enquiriesDialogueDao.save(newEnquiriesDialogue);

		result = "Success";

		return result;
	}

	public List<EnquiriesDialogue> getEnquiryDialogue(String enquiryRefId) {
		Enquiries enquiryInDb = dao.findByRefId(enquiryRefId, Enquiries.class);
		return enquiryInDb.getDialogue();
	}

	public void sendEnquiryMail(Enquiries enquiries) {
		logger.error(" === Send inquiry mail == ");

		Role roleInDb = rolesDao.getByRoleName("ADMIN", true);

		Enquiries enquiryInDb = dao.findByRefId(enquiries.getRefId(), Enquiries.class);

		Collection<User> admins = roleInDb.getUsers();

		logger.error(" === ADMINS FOUND == " + admins.size());

		int counter = 0;

		for (User admin : admins) {
			try {
				counter++;
				emailServiceHelper.sendEmail(enquiryInDb.getMessage(), enquiryInDb.getSubject(), admin.getEmail());

				logger.error(" === Send inquiry mail to admin == " + counter);

			} catch (UnsupportedEncodingException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
