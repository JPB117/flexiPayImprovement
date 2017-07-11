package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.Directory;
import com.icpak.rest.models.ErrorCodes;
import com.workpoint.icpak.shared.model.DirectoryDto;

/**
 * 
 * @author Wladek
 *
 */
public class DirectoryDao extends BaseDao {
	Logger logger = Logger.getLogger(DirectoryDao.class);

	public void createDirectory(Directory directory) {
		save(directory);
	}

	public List<Directory> getAll(Integer offSet, Integer limit) {
		StringBuffer sql = new StringBuffer("FROM Directory");

		sql.append(" order by firmId desc");

		Query query = getEntityManager().createQuery(sql.toString());

		return getResultList(query, offSet, limit);
	}

	public void updateDirectory(DirectoryDto directoryDto) {
		Directory dirInDb = findByDirectoryRefId(directoryDto.getRefId());
		dirInDb.copyFromDto(directoryDto);
		createDirectory(dirInDb);
	}

	public int getDirectoryCount() {

		Number number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery("select count(*) from directory d "));
		return number.intValue();
	}

	public Directory findByDirectoryRefId(String refId) {
		return findByDirectoryRefId(refId, true);
	}

	public Directory findByDirectoryRefId(String refId,
			boolean throwExceptionIfNull) {
		Directory directory = getSingleResultOrNull(getEntityManager()
				.createQuery("from Directory d where d.refId=:refId")
				.setParameter("refId", refId));

		if (directory == null && throwExceptionIfNull) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Directory", "'"
					+ refId + "'");
		}

		return directory;
	}

	public List<DirectoryDto> searchDirectory(String searchTerm,
			String citySearchTerm, Integer offset, Integer limit) {

		StringBuffer sql = new StringBuffer(
				"select d.refId,d.firmId,d.firmName,d.address1,d.address2,"
						+ "d.address3,d.typeOfFirm,d.city,d.telephone,d.fax,d.email,"
						+ "d.paidup,d.sector,d.partners,d.regno,d.branch "
						+ "from " + "directory d ");

		Map<String, Object> params = appendParameters(searchTerm,
				citySearchTerm, sql);

		sql.append(" order by d.firmId desc");
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		List<Object[]> rows = getResultList(query, offset, limit);

		List<DirectoryDto> directoryDtos = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString()
					.trim();
			String firmId = (value = row[i++]) == null ? null : value
					.toString().trim();
			String firmName = (value = row[i++]) == null ? null : value
					.toString().trim();
			String address1 = (value = row[i++]) == null ? null : value
					.toString().trim();
			String address2 = (value = row[i++]) == null ? null : value
					.toString().trim();
			String address3 = (value = row[i++]) == null ? null : value
					.toString().trim();
			String typeOfFirm = (value = row[i++]) == null ? null : value
					.toString().trim();
			String city = (value = row[i++]) == null ? null : value.toString()
					.trim();
			String telephone = (value = row[i++]) == null ? null : value
					.toString().trim();
			String fax = (value = row[i++]) == null ? null : value.toString()
					.trim();
			String email = (value = row[i++]) == null ? null : value.toString()
					.trim();
			String paidUp = (value = row[i++]) == null ? null : value
					.toString().trim();
			String sector = (value = row[i++]) == null ? null : value
					.toString().trim();
			String partners = (value = row[i++]) == null ? null : value
					.toString().trim();
			String regNo = (value = row[i++]) == null ? null : value.toString()
					.trim();
			String branch = (value = row[i++]) == null ? null : value
					.toString().trim();

			DirectoryDto directoryDto = new DirectoryDto();
			directoryDto.setRefId(refId);
			directoryDto.setFirmId(firmId);
			directoryDto.setFirmName(firmName);
			directoryDto.setAddress1(address1);
			directoryDto.setAddress2(address2);
			directoryDto.setAddress3(address3);
			directoryDto.setTypeOfFirm(typeOfFirm);
			directoryDto.setCity(city);
			directoryDto.setTelephone(telephone);
			directoryDto.setFax(fax);
			directoryDto.setEmail(email);
			directoryDto.setPaidup(paidUp);
			directoryDto.setSector(sector);
			directoryDto.setPartners(partners);
			directoryDto.setRegno(regNo);
			directoryDto.setBranch(branch);

			directoryDtos.add(directoryDto);

		}

		return directoryDtos;

	}

	public int getDirectorySearchCount(String searchTerm, String citySearchTerm) {

		StringBuffer sql = new StringBuffer("select count(*) " + "from "
				+ "directory d ");

		Map<String, Object> params = appendParameters(searchTerm,
				citySearchTerm, sql);
		Query query = getEntityManager().createNativeQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		Number number = getSingleResultOrNull(query);

		return number.intValue();

	}

	/**
	 * 
	 * 
	 * @param filter
	 * @param sqlQuery
	 * @return Filter parameter values
	 */
	private Map<String, Object> appendParameters(String searchTerm,
			String citySearchTerm, StringBuffer sqlQuery) {
		boolean isFirst = true;
		Map<String, Object> params = new HashMap<>();

		if (!searchTerm.equals("all")) {
			sqlQuery.append(isFirst ? " where" : " And");
			sqlQuery.append(" (d.firmName like :searchTerm or");
			sqlQuery.append(" d.partners like :searchTerm)");
			params.put("searchTerm", "%" + searchTerm + "%");
			isFirst = false;
		}

		if (!citySearchTerm.equals("all")) {
			sqlQuery.append(isFirst ? " where" : " And");
			sqlQuery.append(" d.city=:citySearchTerm ");
			params.put("citySearchTerm", citySearchTerm);
			isFirst = false;
		}
		return params;
	}
}
