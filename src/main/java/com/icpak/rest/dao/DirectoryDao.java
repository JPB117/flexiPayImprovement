package com.icpak.rest.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.Directory;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;
import com.workpoint.icpak.shared.model.CPDCategory;
import com.workpoint.icpak.shared.model.CPDDto;
import com.workpoint.icpak.shared.model.CPDStatus;
import com.workpoint.icpak.shared.model.CPDSummaryDto;
import com.workpoint.icpak.shared.model.DirectoryDto;

/**
 * 
 * @author duggan
 *
 */
public class DirectoryDao extends BaseDao {
	Logger logger = Logger.getLogger(DirectoryDao.class);

	public void createCPD(CPD cpd) {
		save(cpd);
	}

	public List<Directory> getAll(Integer offSet, Integer limit) {
		StringBuffer sql = new StringBuffer("FROM Directory");

		boolean isFirstParam = true;
		Map<String, Object> params = new HashMap<>();

		sql.append(" and isActive=1");
		sql.append(" order by startDate asc");

		Query query = getEntityManager().createQuery(sql.toString());
		for (String key : params.keySet()) {
			query.setParameter(key, params.get(key));
		}

		return getResultList(query, offSet, limit);
	}

	public void updateCPD(CPD cpd) {
		createCPD(cpd);
	}

	public int getDirectoryCount() {

		Number number = getSingleResultOrNull(
				getEntityManager().createNativeQuery("select count(*) from directory d "));
		return number.intValue();
	}

	public Directory findByDirectoryRefId(String refId) {
		return findByDirectoryRefId(refId, true);
	}

	public Directory findByDirectoryRefId(String refId, boolean throwExceptionIfNull) {
		Directory directory = getSingleResultOrNull(
				getEntityManager().createQuery("from Directory d where d.refId=:refId").setParameter("refId", refId));

		if (directory == null && throwExceptionIfNull) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Directory", "'" + refId + "'");
		}

		return directory;
	}

	public List<DirectoryDto> searchDirectory(String searchTerm, Integer offset, Integer limit) {

		String sql = "select d.refId,d.firmId,d.firmName,d.address1,d.address2,"
				+ "d.address3,d.typeOfFirm,d.city,d.telephone,d.fax,d.email,"
				+ "d.paidup,d.sector,d.partners,d.regno,d.branch "
				+ "from "
				+ "directory d "
				+ "where "
				+ "d.firmId like :searchTerm or d.firmName like :searchTerm or "
				+ "d.typeOfFirm like :searchTerm or d.city like :searchTerm or "
				+ "d.email like :searchTerm or d.regno like :searchTerm "
				+ "order by firmId desc;";

		Query query = getEntityManager().createNativeQuery(sql).setParameter("searchTerm", "%" + searchTerm + "%");

		List<Object[]> rows = getResultList(query, offset, limit);

		List<DirectoryDto> directoryDtos = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString().trim();
			String firmId = (value = row[i++]) == null ? null : value.toString().trim();
			String firmName = (value = row[i++]) == null ? null : value.toString().trim();
			String address1 = (value = row[i++]) == null ? null : value.toString().trim();
			String address2 = (value = row[i++]) == null ? null : value.toString().trim();
			String address3 = (value = row[i++]) == null ? null : value.toString().trim();
			String typeOfFirm = (value = row[i++]) == null ? null : value.toString().trim();
			String city = (value = row[i++]) == null ? null : value.toString().trim();
			String telephone = (value = row[i++]) == null ? null : value.toString().trim();
			String fax = (value = row[i++]) == null ? null : value.toString().trim();
			String email = (value = row[i++]) == null ? null : value.toString().trim();
			String paidUp = (value = row[i++]) == null ? null : value.toString().trim();
			String sector = (value = row[i++]) == null ? null : value.toString().trim();
			String partners = (value = row[i++]) == null ? null : value.toString().trim();
			String regNo = (value = row[i++]) == null ? null : value.toString().trim();
			String branch = (value = row[i++]) == null ? null : value.toString().trim();

			DirectoryDto directoryDto = new DirectoryDto();
		}
		

		return directoryDtos;

	}
}
