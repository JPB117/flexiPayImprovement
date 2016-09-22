package com.workpoint.icpak.shared.model.settings;

import com.workpoint.icpak.shared.model.SerializableObj;

public class SettingDto extends SerializableObj {

	private static final long serialVersionUID = 1L;

	private String settingName;
	private String settingValue;

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String getSettingName() {
		return settingName;
	}

	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}

}
