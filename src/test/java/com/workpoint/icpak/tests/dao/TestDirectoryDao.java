package com.workpoint.icpak.tests.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.DirectoryDaoHelper;
import com.workpoint.icpak.shared.model.DirectoryDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestDirectoryDao extends AbstractDaoTest {
	Logger logger = Logger.getLogger(TestDirectoryDao.class);

	@Inject
	DirectoryDaoHelper directoryDaoHelper;

	@Ignore
	public void testCount() {
		int count = directoryDaoHelper.getCount();

		logger.error(" ==== Count == " + count);
	}

	@Ignore
	public void testSearchCount() {
		// int count = directoryDaoHelper.getSerchCount("7");

		// logger.error(" ==== Count == " + count);
	}

	@Test
	public void testSearch() {
		List<DirectoryDto> directoryDtos = directoryDaoHelper.searchDirectory(
				"Chege", "EMBU", 0, 10);
		int count = directoryDaoHelper.getSerchCount("Chege", "Embu");

		logger.error(" ==== Count == " + count);
		logger.error(" ==== List Size == " + directoryDtos.size());
	}

	@Ignore
	public void testGetAll() {

		List<DirectoryDto> directoryDtos = directoryDaoHelper.getAll(0, 0);
		int count = directoryDaoHelper.getCount();

		logger.error(" ==== Count == " + count);

		for (DirectoryDto dto : directoryDtos) {
			logger.error(" ==== RefId == " + dto.getRefId());
		}
	}

	@Ignore
	public void testGetByRefId() {

		DirectoryDto directoryDto = directoryDaoHelper
				.getByRefId("32b9d21c-8ded-11");

		logger.error(" ==== Address1 == " + directoryDto.getAddress1());
		logger.error(" ==== Email == " + directoryDto.getEmail());

	}

}
