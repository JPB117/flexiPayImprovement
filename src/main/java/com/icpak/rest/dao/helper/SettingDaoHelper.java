package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.SettingDao;
import com.icpak.rest.models.settings.Setting;
import com.workpoint.icpak.shared.model.settings.SettingDto;

@Transactional
public class SettingDaoHelper {

	@Inject
	SettingDao settingDao;

	public Setting getSettingById(String settingId) {
		return settingDao.getSettingById(settingId);
	}

	public Setting getSettingByName(String settingName) {
		return settingDao.getBySettingName(settingName);
	}

	public List<SettingDto> getAllSettings() {
		List<Setting> setting = settingDao.getAllSettings();

		List<SettingDto> dtos = new ArrayList<>();
		for (Setting c : setting) {
			dtos.add(c.toDto());
		}
		return dtos;
	}

	public void createSetting(SettingDto dto) {
		if (dto.getRefId() != null) {
			updateSetting(dto.getRefId(), dto);
			return;
		}
		Setting c = new Setting();
		c.copyFrom(dto);
		settingDao.save(c);
		dto.setRefId(c.getRefId());

	}

	public void updateSetting(String settingId, SettingDto dto) {
		Setting c = getSettingById(settingId);
		c.copyFrom(dto);
		settingDao.save(c);
	}

	public void deleteSetting(String settingId) {
		settingDao.delete(getSettingById(settingId));
	}

}
