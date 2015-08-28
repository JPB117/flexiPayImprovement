package com.icpak.servlet.upload;

//import java.io.File;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.google.inject.Singleton;
import com.workpoint.icpak.client.model.UploadContext;

@Singleton
public class UploadServlet extends UploadAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {		
		return  execute(request, sessionFiles);
	}
	
	private String execute(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException{
		
		FileExecutor executor = getExecutor(request);
		String response = executor.execute(request, sessionFiles);
		
		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);
		// / Send your customized message to the client. 
		return response;
	}
	
	public FileExecutor getExecutor(HttpServletRequest request){
		String action = request.getParameter(UploadContext.ACTION);
		
		UploadContext.UPLOADACTION uploadAction = UploadContext.UPLOADACTION.valueOf(action.toUpperCase());
		FileExecutor executor = null;
		
		switch (uploadAction) {
		case UPLOADDOCFILE:
//		case ATTACHDOCUMENT:
//			executor = new DocumentAttachmentExecutor();
//			break;
//		case UPLOADCHANGESET:
//			executor = new ProcessChangesetsExecutor();
//			break;
//		case UPLOADBPMNPROCESS:
//			executor = new ProcessChangesetsExecutor();
//			break;
//		case IMPORTFORM:
//			executor = new ImportFileExecutor();
//			break;
//		case UPLOADUSERIMAGE:
//			executor = new UserImageExecutor();
//			break;
//		case UPLOADLOGO:
//			executor = new LogoImageExecutor();
//			break;
//		case UPLOADOUTPUTDOC:
//			executor = new UploadOutputDocExecutor();
//			break;
		}
		
		if(executor==null){
			throw new RuntimeException("Could not find executor for action : "+action);
		}
		
		return executor;

	}


	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
//		String fieldName = request.getParameter(UConsts.PARAM_SHOW);
//		File f = receivedFiles.get(fieldName);
//		if (f != null) {
//			response.setContentType(receivedContentTypes.get(fieldName));
//			FileInputStream is = new FileInputStream(f);
//			copyFromInputStreamToOutputStream(is, response.getOutputStream());
//		} else {
//			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
//		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
	
		FileExecutor executor = getExecutor(request);
		
		executor.removeItem(request, fieldName);
		
	}	
	
}
