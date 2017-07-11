package com.workpoint.icpak.client.ui.members.applicationSettings;

import static com.workpoint.icpak.client.ui.util.StringUtils.isNullOrEmpty;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.workpoint.icpak.client.ui.component.DateField;
import com.workpoint.icpak.client.ui.component.IssuesPanel;
import com.workpoint.icpak.client.ui.util.DateUtils;
import com.workpoint.icpak.shared.model.settings.SettingDto;

public class ApplicationSettingsForm extends Composite {

	private static ApplicationSettingsFormUiBinder uiBinder = GWT.create(ApplicationSettingsFormUiBinder.class);

	interface ApplicationSettingsFormUiBinder extends UiBinder<Widget, ApplicationSettingsForm> {
	}

	@UiField
	DateField dtNextRQA;
	@UiField
	IssuesPanel issuesPanel;
	private SettingDto setting;

	public ApplicationSettingsForm() {
		initWidget(uiBinder.createAndBindUi(this));
		dtNextRQA.addStyleName("rqa-date");
	}

	public boolean isValid() {
		boolean isValid = true;
		issuesPanel.clear();
		if (isNullOrEmpty(dtNextRQA.getValueDate())) {
			issuesPanel.addError("The NextRQA field is required!");
			isValid = false;
		}

		return isValid;
	}

	public SettingDto getConfiguredSetting() {
		if (setting == null) {
			setting = new SettingDto();
		}
		setting.setSettingName("next_rqa_meeting");
		setting.setSettingValue(DateUtils.DATEFORMAT.format(dtNextRQA.getValueDate()));
		return setting;
	}

	public void setSetting(SettingDto setting) {
		this.setting = setting;
		if (!isNullOrEmpty(setting.getSettingValue())) {
			dtNextRQA.setValue(DateUtils.DATEFORMAT.parse(setting.getSettingValue()));
		}
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Date today = new Date();
		CalendarUtil.addMonthsToDate(today, 10);
		dtNextRQA.setMaxDate(today);
	}

}
