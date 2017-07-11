package com.icpak.rest.dao.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.DirectoryDao;
import com.icpak.rest.models.Directory;
import com.workpoint.icpak.shared.model.DirectoryDto;

@Transactional
public class DirectoryDaoHelper {

	@Inject
	DirectoryDao directoryDao;

	static Logger logger = Logger.getLogger(DirectoryDaoHelper.class);

	public DirectoryDto createDirectory(DirectoryDto dto) {
		Directory newDir = new Directory();
		newDir.copyFromDto(dto);
		directoryDao.createDirectory(newDir);
		return dto;
	}

	public List<DirectoryDto> getAll(Integer offset, Integer limit) {
		List<Directory> directories = directoryDao.getAll(offset, limit);
		List<DirectoryDto> directoryDtos = new ArrayList<>();

		for (Directory d : directories) {
			DirectoryDto dto = d.toDto();
			directoryDtos.add(dto);
		}

		return directoryDtos;
	}

	public List<DirectoryDto> searchDirectory(String searchTerm,
			String townSearch, int offset, int limit) {
		return directoryDao.searchDirectory(searchTerm, townSearch, offset,
				limit);
	}

	public int getCount() {
		return directoryDao.getDirectoryCount();
	}

	public int getSerchCount(String searchTerm, String townSearch) {
		return directoryDao.getDirectorySearchCount(searchTerm,townSearch);
	}

	public DirectoryDto getByRefId(String refId) {
		Directory directory = directoryDao.findByDirectoryRefId(refId);

		return directory.toDto();
	}

	public DirectoryDto update(DirectoryDto directoryDto) {
		directoryDao.updateDirectory(directoryDto);
		return directoryDto;
	}

}
