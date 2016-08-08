package com.workpoint.icpak.client.ui.eventsandseminars.csvimport;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.workpoint.icpak.client.model.UploadContext;
import com.workpoint.icpak.client.model.UploadContext.UPLOADACTION;
import com.workpoint.icpak.client.ui.upload.custom.Uploader;
import com.workpoint.icpak.shared.model.events.BookingSummaryDto;

public class CsvImport extends Composite {

	private static CsvImportUiBinder uiBinder = GWT.create(CsvImportUiBinder.class);

	@UiField
	Uploader uploader;

	private BookingSummaryDto summary;

	interface CsvImportUiBinder extends UiBinder<Widget, CsvImport> {
	}

	public CsvImport() {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public void setUploadContext(String eventRefId) {
		UploadContext context = new UploadContext();
		context.setContext("eventRefId", eventRefId);
		context.setAction(UPLOADACTION.UPLOADCSV);
		context.setAccept(Arrays.asList("csv"));
		uploader.setContext(context);
	}

	public void setBookingSummary(BookingSummaryDto summary) {
		this.summary = summary;
	}

	public Uploader getUploader() {
		return uploader;
	}

}
