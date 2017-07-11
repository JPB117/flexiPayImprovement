package com.icpak.rest.models.settings;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.icpak.rest.models.base.PO;
import com.workpoint.icpak.shared.model.settings.SettingDto;

@Entity
@Table(name = "setting")
public class Setting extends PO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String settingName;
	private String settingValue;

	public void copyFrom(SettingDto setting) {
		setSettingName(setting.getSettingName());
		setSettingValue(setting.getSettingValue());
	}

	public SettingDto toDto() {
		SettingDto setting = new SettingDto();
		setting.setRefId(refId);
		setting.setSettingName(settingName);
		setting.setSettingValue(settingValue);
		return setting;
	}

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
