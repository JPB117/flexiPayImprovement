package com.icpak.servlet.upload;

import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.AttachmentsDao;
import com.icpak.rest.dao.CPDDao;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.util.ApplicationSettings;

@Transactional
public class CPDAttachmentsExecutor extends FileExecutor {
	Logger logger  = Logger.getLogger(CPDAttachmentsExecutor.class);

	@Inject
	CPDDao dao;

	@Inject
	AttachmentsDao attachmentsDao;

	@Inject
	ApplicationSettings settings;
	
	@Override
	String execute(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		String err = null;
		String cpdRefId = request.getParameter("cpdRefId");
		assert cpdRefId != null;

		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					String fieldName = item.getFieldName();
					String contentType = item.getContentType();
					String name = item.getName();
					long size = item.getSize();

					Attachment attachment = new Attachment();
					attachment.setCreated(new Date());
					attachment.setContentType(contentType);
					attachment.setId(null);
					attachment.setName(name);
					attachment.setSize(size);
					String fileName = getFilePath() + "/"+attachment.getRefId()+ "-CPD-" + name;
					attachment.setFileName(attachment.getRefId()+fileName);
					// attachment.setAttachment(item.get());
					CPD cpd = dao.findByCPDId(cpdRefId);
					assert cpd != null;
					attachment.setCpd(cpd);

					attachmentsDao.save(attachment);

					IOUtils.write(item.get(),
							new FileOutputStream(new File(fileName)));

					registerFile(request, getFilePath() + "/"+ fieldName, attachment.getId());
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

		if (fileId != null)
			attachment = attachmentsDao.getById(Attachment.class, fileId);
		logger.info(" File name "+attachment.getFileName());
			file = new File(attachment.getFileName());
			logger.info(" removing file ");
			file.delete();
			attachmentsDao.delete(attachment);
	}
	
	private String getFilePath(){
		return settings.getProperty("upload_path");
	}

}
