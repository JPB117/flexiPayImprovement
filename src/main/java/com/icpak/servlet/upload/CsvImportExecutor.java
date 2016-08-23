package com.icpak.servlet.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.dao.helper.UsersDaoHelper;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;

@Transactional
public class CsvImportExecutor extends FileExecutor {

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
	CPDDaoHelper cpdDao;
	Logger logger = Logger.getLogger(StatementDaoHelper.class);

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		String err = null;
		String eventRefId = request.getParameter("eventRefId");
		Event event = null;
		if (eventRefId != null) {
			event = bookingsDao.findByRefId(eventRefId, Event.class);
		}

		logger.debug(">>>>>" + "Started Uploading csv for " + event.getName());

		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					BufferedReader br = null;
					String line = "";
					String cvsSplitBy = ",";

					String encoding = "UTF-8";
					br = new BufferedReader(new InputStreamReader(item.getInputStream(), encoding));
					while ((line = br.readLine()) != null) {
						// Check if this Line has multiple columns -split them
						// and take the first column.

						String[] memberNos = line.split(cvsSplitBy);
						if (memberNos.length >= 1) {
							// Search if this delegate booked this event;
							List<DelegateDto> delegateList = bookingsDao.getAllDelegates(eventRefId, 0, 10, null, false,
									null, null, memberNos[0]);
							logger.debug("found this number of delegates::" + delegateList.size());
							if (delegateList.size() == 1) {
								// Member Found..
								DelegateDto delegateDto = delegateList.get(0);
								logger.debug("Existing delegate::" + delegateDto.getFullName() + "::"
										+ delegateDto.getMemberNo());

								// Find the Delegate Record & Update CPD Record.
								Delegate delegateInDb = bookingsDao.findByRefId(delegateDto.getRefId(), Delegate.class);
								cpdDao.updateCPDFromAttendance(delegateInDb, AttendanceStatus.ATTENDED);

								// send an SMS confirming the attendance
								if (delegateInDb.getMemberRegistrationNo() != null
										&& !(delegateInDb.getMemberRegistrationNo().equals(""))) {
									sendDelegateSms(delegateInDb.toDto(), event);
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
										cpdDao.updateCPDFromAttendance(delegateInDb, AttendanceStatus.ATTENDED);
										logger.debug(
												"Successfully Imported>> " + delegateInDb.getMemberRegistrationNo());
									}

									// send an SMS confirming the attendance
									if (del1.getMemberNo() != null && !del1.getMemberNo().equals("")) {
										sendDelegateSms(del1, event);
									}
								}
							}
						} else {
							logger.debug("Tried spliting line but found less than one>>>" + line);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
					err = e.getMessage();
				}
			}
		}
		return err;
	}

	public void sendDelegateSms(DelegateDto del1, Event event) {
		User user = userDao.findUserByMemberNo(del1.getMemberNo());
		if (user != null) {
			String eventName = (event != null ? event.getName() : "");
			String smsMessage = "Dear" + " " + del1.getFullName() + ",Thank you for attending the " + eventName + "."
					+ "You have been awarded your cpd Hours.";
			smsIntergration.send(user.getPhoneNumber(), smsMessage);
		}
	}

	public void removeItem(HttpServletRequest request, String fieldName) {
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, false);
		Long fileId = receivedFiles.get(fieldName);
		Attachment attachment = null;
		File file = null;

	}

}
