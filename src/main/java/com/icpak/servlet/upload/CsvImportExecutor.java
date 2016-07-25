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
import com.icpak.rest.dao.helper.CPDDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.icpak.rest.models.event.Delegate;
import com.icpak.rest.models.event.Event;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;

@Transactional
public class CsvImportExecutor extends FileExecutor {

	@Inject
	BookingsDao bookingsDao;
	@Inject
	SMSIntegration smsIntergration;
	@Inject
	CPDDaoHelper cpdDao;
	Logger logger = Logger.getLogger(StatementDaoHelper.class);

	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles)
			throws UploadActionException {
		String err = null;
		String eventRefId = request.getParameter("eventRefId");
		Event event = null;
		if (eventRefId != null) {
			event = bookingsDao.findByRefId(eventRefId, Event.class);
		}

		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					BufferedReader br = null;
					String line = "";

					String encoding = "UTF-8";
					br = new BufferedReader(new InputStreamReader(
							item.getInputStream(), encoding));
					while ((line = br.readLine()) != null) {
						// Search if this delegate booked this event;
						List<DelegateDto> delegateList = bookingsDao
								.getAllDelegates(eventRefId, 0, 10, null,
										false, null, null, line);

						if (delegateList.size() == 1) {
							DelegateDto delegateDto = delegateList.get(0);
							delegateDto
									.setAttendance(AttendanceStatus.ATTENDED);

							// Find the Delegate Record.
							Delegate delegateInDb = bookingsDao.findByRefId(
									delegateDto.getRefId(), Delegate.class);

							// send an SMS confirming the attendance
							Member member = bookingsDao.findByRefId(
									delegateDto.getMemberRefId(), Member.class);
							String eventName = (event != null ? event.getName()
									: "");
							String smsMessage = "Dear" + " "
									+ delegateDto.getFullName()
									+ ",Thank you for attending the "
									+ eventName + "." + "Your ERN No. is "
									+ delegateDto.getErn();
							smsIntergration.send(member.getUser()
									.getPhoneNumber(), smsMessage);
							delegateInDb.copyFrom(delegateDto);
							bookingsDao.save(delegateInDb);
							cpdDao.updateCPDFromAttendance(delegateInDb,
									delegateInDb.getAttendance());
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

	public void removeItem(HttpServletRequest request, String fieldName) {
		Hashtable<String, Long> receivedFiles = getSessionFiles(request, false);
		Long fileId = receivedFiles.get(fieldName);

		Attachment attachment = null;
		File file = null;

	}

}
