package com.workpoint.icpak.client.ui.upload.custom;

import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnCancelUploaderHandler;
import gwtupload.client.IUploader.OnChangeUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.MultiUploader;
import gwtupload.client.PreloadedImage;
import gwtupload.client.PreloadedImage.OnLoadPreloadedImageHandler;
import gwtupload.client.SingleUploader;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.ui.events.UploadEndedEvent;
import com.workpoint.icpak.client.ui.events.UploadStartedEvent;
import com.workpoint.icpak.client.util.AppContext;

/**
 * 
 * @author duggan
 *
 */
public class Uploader extends Composite {

	interface Binder extends UiBinder<Widget, Uploader> {
	}

	Binder binder = GWT.create(Binder.class);

	@UiField
	FlowPanel panelImages;
	@UiField
	HTMLPanel uploaderPanel;

	IUploader uploader;

	UploadContext context;

	public Uploader() {
		this(false);
	}

	public Uploader(boolean isSingleUploader) {
		initWidget(binder.createAndBindUi(this));
		if (isSingleUploader) {
			uploader = new SingleUploader();
		} else {
			uploader = new MultiUploader();
		}

		uploader.setAutoSubmit(true);
		uploader.setMultipleSelection(!isSingleUploader);
		uploaderPanel.add(uploader);
		uploader.addOnFinishUploadHandler(onFinishHandler);
		uploader.addOnStartUploadHandler(uploadStarted);
		uploader.getFileInput().setName("Filedata");
	}

	public Uploader(UploadContext ctx) {
		this();
		setContext(ctx);
	}

	public void setAvoidRepeatFiles(boolean allow) {
		if (uploader instanceof MultiUploader) {
			((MultiUploader) uploader).setAvoidRepeatFiles(allow);
		}

		if (uploader instanceof SingleUploader) {
			((SingleUploader) uploader).setAvoidRepeatFiles(allow);
		}
	}

	public void setContext(UploadContext context) {
		this.context = context;
		StringBuffer servletPath = new StringBuffer();
		String url = context.getUrl() == null ? "upload" : context.getUrl();
		servletPath.append(url + "?" + context.getContextValuesAsURLParams());
		uploader.setServletPath(servletPath.toString());
		if (context.getAcceptTypes() != null)
			uploader.setValidExtensions(context.getAcceptTypes());
	}

	OnStartUploaderHandler uploadStarted = new OnStartUploaderHandler() {
		@Override
		public void onStart(IUploader uploader) {
			AppContext.fireEvent(new UploadStartedEvent());
		}
	};

	OnFinishUploaderHandler onFinishHandler = new OnFinishUploaderHandler() {

		@Override
		public void onFinish(IUploader uploader) {
			AppContext.fireEvent(new UploadEndedEvent());
			if (uploader.getStatus() == Status.SUCCESS) {

				new PreloadedImage(uploader.fileUrl(), showImage);

				// The server sends useful information to the client by default
				// UploadedInfo info = uploader.getServerInfo();
				// System.out.println("File name " + info.name);
				// System.out.println("File content-type " + info.ctype);
				// System.out.println("File size " + info.size);
				//
				// // You can send any customized message and parse it
				// System.out.println("Server message " + info.message);
			}
		}
	};

	OnCancelUploaderHandler handler = new OnCancelUploaderHandler() {

		@Override
		public void onCancel(IUploader uploader) {

		}
	};
	OnLoadPreloadedImageHandler showImage = new OnLoadPreloadedImageHandler() {
		@Override
		public void onLoad(PreloadedImage image) {
			panelImages.add(image);
		}
	};

	public void cancel() {
		List<IUploader> uploaders = new ArrayList<IUploader>();
		if (uploader instanceof MultiUploader) {
			uploaders = ((MultiUploader) uploader).getUploaders();
		} else {
			uploaders.add(uploader);
		}

		if (uploaders != null) {
			List<IUploader> uplodas = new ArrayList<IUploader>();
			uplodas.addAll(uploaders);
			for (IUploader uploader : uplodas) {
				uploader.cancel();
			}
		}
	}

	public void addOnFinishUploaderHandler(OnFinishUploaderHandler handler) {
		uploader.addOnFinishUploadHandler(handler);
	}

	public void addOnChangeUploaderHandler(OnChangeUploaderHandler handler) {
		uploader.addOnChangeUploadHandler(handler);
	}

	public void clear() {
		uploader.clear();
	}

	public void clearImages() {
		uploader.reset();
	}

	public void addOnCancelUploaderHandler(OnCancelUploaderHandler handler) {
		uploader.addOnCancelUploadHandler(handler);
	}

	public void setAutoSubmit(boolean isAutoSubmit) {
		uploader.setAutoSubmit(isAutoSubmit);
	}
}
