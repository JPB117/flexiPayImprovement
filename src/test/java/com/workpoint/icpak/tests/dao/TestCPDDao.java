package com.workpoint.icpak.tests.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.util.SMSIntegration;
import com.icpak.servlet.upload.GetReport;
import com.workpoint.icpak.server.util.ServerDateUtils;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.MemberCPDDto;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestCPDDao extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestCPDDao.class);

	@Inject
	CPDDaoHelper helper;
	@Inject
	CPDDao cpdDao;
	@Inject
	UsersDao usersDao;

	@Inject
	GetReport reporter;

	@Inject
	BookingsDao bookingsDao;
	@Inject
	BookingsDaoHelper bookingsDaoHelper;
	@Inject
	UsersDaoHelper userDaoHelper;
	@Inject
	UsersDao userDao;
	@Inject
	SMSIntegration smsIntergration;
	@Inject
	CPDDaoHelper cpdDaoHelper;

	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	@Ignore
	public void getCPD2() throws ParseException {
	}

	@Ignore
	public void generateGoodStandingCert() {

	}

	@Ignore
	public void testUpdateCPD() {
		CPD cpd = cpdDao.findByCPDId("NgmZcYUU0mu7JEyr");
		CPDDto dto = cpd.toDTO();
		dto.setManagementComment("Please provide the necessary attachments");
		helper.update("3pzAyw110E2i5VTE", "NgmZcYUU0mu7JEyr", dto);
	}

	@Ignore
	public void testCreateCPD() {
		CPDDto dto = new CPDDto();
		dto.setCategory(CPDCategory.CATEGORY_A);
		dto.setEndDate(new Date(1452373200000L));
		dto.setStartDate(new Date(1451509200000L));
		dto.setMemberRegistrationNo("18134");
		dto.setTitle("FUTURE");
		dto.setOrganizer("DAKNDFKANDKFA");
		helper.create("QpkHIcVizijvuVTH", dto);
	}

	// @Test
	public void updateAllSummaries() {
		List<User> users = usersDao.getAllUsers(0, 0, null, "");
		int i = users.size();
		for (User user : users) {

			cpdDao.updateSummary(user);

			logger.warn(" COUNT === " + i);
			i--;
		}
	}

	// @Test
	public void generateAllMemberStatements() {
		// Create a new folder
		String folderName = "all_members_cpd" + ServerDateUtils.FULLTIMESTAMP.format(new Date());
		// Get All members
		List<User> allUsers = usersDao.getAllUsers();

		for (User u : allUsers) {
			// byte[] data = processMemberCPDStatementRequest(memberNo,
			// memberRefId, finalStartDate, finalEndDate, resp);

			// IOUtils.write(data, new FileOutputStream(new
			// File("cpd_statement_"
			// + u.getMemberNo())));
		}
	}

	@Test
	public void testCsvImport() {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		String eventRefId = "Q4OZnEbLeRbIo19S";
		Event event = null;
		if (eventRefId != null) {
			event = bookingsDao.findByRefId(eventRefId, Event.class);
		}

		try {
			br = new BufferedReader(new FileReader("C:\\Users\\user\\Desktop\\Book3.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while ((line = br.readLine()) != null) {
				// Check if this Line has multiple columns -split them
				// and take the first column.

				String[] memberNos = line.split(cvsSplitBy);
				if (memberNos.length >= 1) {
					// Search if this delegate booked this event;
					List<DelegateDto> delegateList = bookingsDao.getAllDelegates(eventRefId, 0, 10, null, false, null,
							null, memberNos[0]);
					logger.debug("found this number of delegates::" + delegateList.size());
					if (delegateList.size() == 1) {
						// Member Found..
						DelegateDto delegateDto = delegateList.get(0);
						logger.debug(
								"Existing delegate::" + delegateDto.getFullName() + "::" + delegateDto.getMemberNo());

						// Find the Delegate Record & Update CPD Record.
						Delegate delegateInDb = bookingsDao.findByRefId(delegateDto.getRefId(), Delegate.class);
						cpdDaoHelper.updateCPDFromAttendance(delegateInDb, AttendanceStatus.ATTENDED);

						// send an SMS confirming the attendance
						if (delegateInDb.getMemberRegistrationNo() != null
								&& !(delegateInDb.getMemberRegistrationNo().equals(""))) {
							// sendDelegateSms(delegateInDb.toDto(), event);
						}
						logger.debug("Successfully Imported>> " + delegateInDb.getMemberRegistrationNo());
					} else {
						// Creating a booking Record
						logger.debug("This delegate did not book>>> " + memberNos[0]);
						DelegateDto d = new DelegateDto();
						d.setMemberNo(memberNos[0]);

						BookingDto booking = bookingsDaoHelper.createBooking(eventRefId, d);
						List<DelegateDto> dels = booking.getDelegates();

						for (DelegateDto del1 : dels) {
							// Find the Delegate Record.
							Delegate delegateInDb = bookingsDao.findByRefId(del1.getRefId(), Delegate.class);
							if (delegateInDb != null) {
								cpdDaoHelper.updateCPDFromAttendance(delegateInDb, AttendanceStatus.ATTENDED);
								logger.debug("Successfully Imported>> " + delegateInDb.getMemberRegistrationNo());
							}

							// send an SMS confirming the attendance
							if (del1.getMemberNo() != null && !del1.getMemberNo().equals("")) {
								// sendDelegateSms(del1, event);
							}
						}
					}
				} else {
					logger.debug("Tried spliting line but found less than one>>>" + line);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Ignore
	public void getCPDHrs() {
		double val = helper.getCPDHours(null);
		System.err.println(val);
	}

	@Ignore
	public void getCPD() throws ParseException {
		String memberId = "pabGC3dh0OOzLzSC";
		List<CPDDto> list = helper.getAllCPD("ALL", 0, 1000, formatter.parse("01/01/2000").getTime(),
				new Date().getTime(), "Another");
		for (CPDDto dto : list) {
			System.err.println(dto.getTitle());
		}

	}

	@Ignore
	public void testCount() throws ParseException {
		System.err.println("Total Archive>>>>"
				+ helper.getCPDSummary("ALL", 1420059600000L, 1451494912593L).getTotalArchive() + "Total Returns"
				+ helper.getCPDSummary("ALL", 1420059600000L, 1451494912593L).getTotalReturns());
	}

	@Ignore
	public void testMemberCPD() throws ParseException {
		List<MemberCPDDto> memberCPDDtos = cpdDao.getMemberCPD("1020", 0, 10);
		System.err.println("Count>>" + cpdDao.getMemberCPDCount("1020"));
		for (MemberCPDDto memberCPD : memberCPDDtos) {
			System.err.println("No_" + memberCPD.getMemberNo());
			System.err.println("Category::" + memberCPD.getCustomerType());
			System.err.println("Status::" + memberCPD.getStatus());
			System.err.println("2015::" + memberCPD.getYear2015());
			System.err.println("2011::" + memberCPD.getYear2011());
		}

		// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		// Date another = formatter.parse("2013-1-1");
		//
		// cpdDao.getYearSummaries("iypUNLFxbsEFMJLB", another, new Date());
	}

	@Ignore
	public void testSearchCount() {
		int count = helper.cpdSearchCount("kimani");
		logger.error("========= CPP search count=== " + count);
	}

	@Ignore
	public void testSearch() {
		Long startDate = 1420059600000L;
		Long endDate = 1450645200000L;

	}

	@Ignore
	public void testGetAllCPD() throws ParseException {
		Date today = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date another = formatter.parse("2015-1-1");

		List<CPDDto> cpdDtos = helper.getAllCPD("returnArchive", 0, 100, another.getTime(), today.getTime(), "");

		// Integer count = helper.getCount("ALLRETURNS", another.getTime(),
		// today.getTime());

		for (CPDDto dto : cpdDtos) {
			System.err.println("Title>>" + dto.getTitle());
		}
		logger.error("======= List length=== " + cpdDtos.size());
	}

	@Ignore
	public void transferCPDBlob() {
		cpdDao.dumpBlobToFile();
	}

	@Ignore
	public void commitTrx() {
		// commit();
	}
}
