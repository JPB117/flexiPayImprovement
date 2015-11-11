package com.icpak.rest.dao.helper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

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
import com.workpoint.icpak.server.util.DateUtils;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.MemberStanding;
import com.workpoint.icpak.shared.model.MembershipStatus;
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
	StatementDaoHelper statementHelper;
	@Inject
	MemberDao memberDao;

	static Logger logger = Logger.getLogger(CPDDaoHelper.class);

	public List<CPDDto> getAllCPD(String memberId, Integer offset, Integer limit, Long startDate, Long endDate) {

		List<CPD> cpds = null;

		if (memberId != null && memberId.equals("ALL")) {
			cpds = dao.getAllCPDs(offset, limit, new Date(startDate), new Date(endDate));
		} else {
			cpds = dao.getAllCPDs(memberId, offset, limit, new Date(startDate), new Date(endDate));
		}

		List<CPDDto> rtn = new ArrayList<>();
		for (CPD cpd : cpds) {
			CPDDto dto = cpd.toDTO();
			dto.setFullNames(userDao.getFullNames(cpd.getMemberRefId()));
			rtn.add(dto);
		}

		return rtn;
	}

	public Integer getCount(String memberId, Long startDate, Long endDate) {
		System.err.println("memberId" + memberId);
		return dao.getCPDCount(memberId, new Date(startDate), new Date(endDate));
	}

	public CPDDto getCPD(String cpdId) {
		CPD cpd = dao.findByCPDId(cpdId);
		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getNamesBymemberNo(cpd.getMemberRegistrationNo()));
		return rtn;
	}

	public CPDDto getCPDFromMemberRefId(String memberRefId, String cpdId) {
		CPD cpd = dao.findByCPDId(cpdId);
		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberRefId));
		return rtn;
	}

	public CPDDto create(String memberId, CPDDto cpdDto) {

		CPD cpd = new CPD();
		cpd.copyFrom(cpdDto);
		cpd.setMemberRefId(memberId);
		dao.save(cpd);

		CPDDto rtn = cpd.toDTO();
		rtn.setFullNames(userDao.getFullNames(memberId));
		return rtn;
	}

	public CPDDto update(String memberId, String cpdId, CPDDto cpd) {
		CPD poCPD = dao.findByCPDId(cpdId);
		poCPD.copyFrom(cpd);
		poCPD.setMemberRefId(memberId);
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
		String memberRefId = delegate.getMemberRefId();

		if (attendance == null || attendance == AttendanceStatus.NOTATTENDED) {
			dao.deleteCPDByMemberAndEvent(memberRefId, event.getRefId());
			return;
		}

		CPDDto cpd = new CPDDto();
		CPD po = dao.getCPDByMemberAndEvent(memberRefId, event.getRefId());
		if (po != null) {
			cpd = po.toDTO();
		}

		cpd.setCpdHours(event.getCpdHours());
		cpd.setEndDate(event.getEndDate());
		cpd.setStartDate(event.getStartDate());
		cpd.setFullNames(delegate.getSurname() + " " + delegate.getOtherNames());
		cpd.setMemberRefId(memberRefId);
		cpd.setEventId(event.getRefId());
		cpd.setOrganizer("ICPAK");
		cpd.setStatus(CPDStatus.Approved);
		cpd.setEventId(event.getRefId());
		cpd.setTitle(event.getName());
		cpd.setMemberRegistrationNo(delegate.getMemberRegistrationNo());
		cpd.setCategory(CPDCategory.CATEGORY_A);
		cpd.setEventLocation(event.getVenue());

		if (cpd.getRefId() != null) {
			update(memberRefId, cpd.getRefId(), cpd);
		} else {
			create(memberRefId, cpd);
		}
	}

	public CPDSummaryDto getCPDSummary(String memberRefId, Long startDate, Long endDate) {

		CPDSummaryDto dto = null;

		if (memberRefId.equals("ALL")) {
			dto = dao.getCPDSummary(new Date(startDate), new Date(endDate));
		} else {
			dto = dao.getCPDSummary(memberRefId, new Date(startDate), new Date(endDate));
		}

		return dto;
	}

	/**
	 * 
	 * @param memberRefId
	 * @return true if member meets all requirements
	 */
	public boolean isInGoodStanding(String memberRefId, List<String> messages, MemberStanding standing) {
		boolean isGenerate = true;

		Member member = dao.findByRefId(memberRefId, Member.class);

		/**
		 * Membership status must be Active
		 */
		MembershipStatus status = member.getMemberShipStatus();
		standing.setMembershipStatus(status);
		if (status != null) {
			if (status != MembershipStatus.ACTIVE) {
				isGenerate = false;
				messages.add("Your Membership status is <strong>" + status.name() + "</strong>. "
						+ "Membership must be <strong>Active</strong> be in good standing.");
			}
		} else {
			isGenerate = false;
			messages.add("No valid member status found");
		}

		/**
		 * Account Information - Statement Account balance must be <=100
		 */
		Double balance = statementHelper.getAccountBalance(memberRefId);
		standing.setMemberBalance(balance);

		if (balance > 100) {
			isGenerate = false;
			messages.add("Member account balance must be less than Ksh100.");
		}

		/**
		 * Ongoing displinary case must be false TODO: Managed by Admin, admin
		 * to set true or false
		 */
		if (member.hasDisplinaryCase()) {
			isGenerate = false;
			messages.add("Member must not have an ongoing displinary case for good standing");
		}

		/**
		 * CPD Hours Attended since registration<b> 1 year or less : 0 hrs >1
		 * year and <= 2 year : 40 hrs >2 year and <=3 year : 80 hrs >3 year :
		 * 120 hrs
		 * 
		 */
		Date registrationDate = member.getRegistrationDate();
		double cpdHours = dao.getCPDHours(memberRefId);
		if (registrationDate == null) {
			isGenerate = false;
			messages.add("Your registration date cannot be found in the portal, kindly request "
					+ "for your account update from the Administrator.");
		} else {
			Calendar regDate = Calendar.getInstance();
			regDate.setTime(registrationDate);

			Double noOfYears = DateUtils.getYearsBetween(registrationDate, new Date());
			if (noOfYears <= 0.0) {
				// do nothing - all is well<=2
			} else if (noOfYears <= 2) {
				// >1 && <=2
				if (cpdHours < 40) {
					isGenerate = false;
					messages.add("You have been a member for more than " + noOfYears + ". You have done " + cpdHours
							+ "/40 expected hours.");
				}
			} else if (noOfYears <= 3) {
				// >1 && <=2
				if (cpdHours < 80) {
					isGenerate = false;
					messages.add("You have been a member for more than " + noOfYears + ". You have done " + cpdHours
							+ "/80 expected hours.");
				}
			} else {
				// >1 && <=2
				if (cpdHours < 120) {
					isGenerate = false;
					messages.add("You have been a member for more than " + noOfYears.intValue() + "years but have done "
							+ cpdHours + " cpd hours out of 120 expected hours.");
				}
			}
		}

		return isGenerate;
	}

	public MemberStanding getMemberStanding(String memberId) {
		List<String> messages = new ArrayList<>();
		MemberStanding standing = new MemberStanding();
		boolean isInGoodStanding = isInGoodStanding(memberId, messages, standing);

		standing.setStanding(isInGoodStanding ? 1 : 0);
		standing.setReasons(messages);

		return standing;
	}

	public double getCPDHours(String memberRefId) {
		return dao.getCPDHours(memberRefId);
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
			logger.debug("CPD memberr id " + cpd.getMemberRefId());
			cpdDtos.add(cpd.toDTO());
		}

		return cpdDtos;

	}

	public List<CPDDto> searchCPD(String searchTerm, Integer offset, Integer limit) {
		return dao.searchCPD(searchTerm, offset, limit);
	}
	
	public Integer cpdSearchCount(String searchTerm) {
		BigInteger count  = dao.cpdSearchCount(searchTerm);
		return count.intValue();
	}

}
