package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.EventsDao;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;

@Transactional
public class CPDDaoHelper {

	@Inject
	CPDDao dao;
	@Inject
	EventsDao eventDao;
	@Inject
	UsersDao userDao;
	@Inject
	MemberDao memberDao;

	static Logger logger = Logger.getLogger(CPDDaoHelper.class);

	public List<CPDDto> getAllCPD(String memberId, Integer offset, Integer limit) {

		List<CPD> cpds = null;

		if (memberId != null && memberId.equals("ALL")) {
			cpds = dao.getAllCPDs(offset, limit);
		} else {
			cpds = dao.getAllCPDs(memberId, offset, limit);
		}

		List<CPDDto> rtn = new ArrayList<>();
		for (CPD cpd : cpds) {
			CPDDto dto = cpd.toDTO();
			dto.setFullNames(userDao.getFullNames(cpd.getMemberId()));
			rtn.add(dto);
		}

		return rtn;
	}

	public Integer getCount(String memberId) {
		return dao.getCPDCount(memberId);
	}

	public CPDDto getCPD(String memberId, String cpdId) {
		CPD cpd = dao.findByCPDId(cpdId);
		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getNamesBymemberNo(cpd.getMemberRegistrationNo()));
		return rtn;
	}

	public CPDDto create(String memberId, CPDDto cpdDto) {

		CPD cpd = new CPD();
		cpd.copyFrom(cpdDto);
		cpd.setMemberId(memberId);
		dao.save(cpd);

		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberId));
		return rtn;
	}

	public CPDDto update(String memberId, String cpdId, CPDDto cpd) {
		CPD poCPD = dao.findByCPDId(cpdId);
		poCPD.copyFrom(cpd);
		poCPD.setMemberId(memberId);
		dao.save(poCPD);

		CPDDto rtn = poCPD.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberId));
		return rtn;
	}

	public void delete(String memberId, String cpdId) {
		dao.delete(dao.findByCPDId(cpdId));
	}

	public void updateCPDFromAttendance(Delegate delegate, AttendanceStatus attendance) {
		if (delegate.getMemberRefId() == null) {
			return;
		}

		Member member = dao.findByRefId(delegate.getMemberRefId(), Member.class);
		Event event = delegate.getBooking().getEvent();
		String memberId = delegate.getMemberRefId();

		if (attendance == null || attendance == AttendanceStatus.NOTATTENDED) {
			dao.deleteCPDByMemberAndEvent(memberId, event.getRefId());
			return;
		}

		CPDDto cpd = new CPDDto();
		CPD po = dao.getCPDByMemberAndEvent(memberId, event.getRefId());
		if (po != null) {
			cpd = po.toDTO();
		}

		cpd.setCpdHours(event.getCpdHours());
		cpd.setEndDate(event.getEndDate());
		cpd.setStartDate(event.getStartDate());
		cpd.setFullNames(delegate.getSurname() + " " + delegate.getOtherNames());
		cpd.setMemberId(memberId);
		cpd.setEventId(event.getRefId());
		cpd.setOrganizer("ICPAK");
		cpd.setStatus(CPDStatus.Approved);
		cpd.setEventId(event.getRefId());
		cpd.setTitle(event.getName());

		if (cpd.getRefId() != null) {
			update(memberId, cpd.getRefId(), cpd);
		} else {
			create(memberId, cpd);
		}
	}

	public CPDSummaryDto getCPDSummary(String memberRefId) {

		CPDSummaryDto dto = null;

		if (memberRefId.equals("ALL")) {
			dto = dao.getCPDSummary();
		} else {
			dto = dao.getCPDSummary(memberRefId);
		}

		return dto;
	}

	public List<CPDDto> filterMyCPD() {
		List<CPDDto> cpDtos = new ArrayList<>();
		return cpDtos;
	}

	// get a list of cpds filtered by date currently logged in member
	public List<CPDDto> getAllMemberCpd(String memberRefId, Date startDate, Date endDate, Integer limit,
			Integer offset) {

		List<CPD> cpds = dao.getAllCPDS(memberRefId, startDate, endDate, offset == null ? 0 : offset,
				limit == null ? 1000 : limit);
		logger.info("CPD Records Count = " + cpds.size());

		List<CPDDto> cpdDtos = new ArrayList<>();
		for (CPD cpd : cpds) {
			logger.debug("CPD memberr id " + cpd.getMemberId());
			cpdDtos.add(cpd.toDTO());
		}

		return cpdDtos;

	}

}
