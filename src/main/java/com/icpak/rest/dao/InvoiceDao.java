package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.trx.Invoice;

public class InvoiceDao extends BaseDao {

	public List<Invoice> getAllInvoices(String memberId, Integer offset,
			Integer limit) {
		if (memberId == null || memberId.equals("ALL")) {
			return getResultList(getEntityManager().createQuery(
					"FROM Invoice where isActive=1 " + "order by id desc"),
					offset, limit);
		} else {
			return getResultList(
					getEntityManager().createQuery(
							"FROM Invoice where isActive=1 and memberId=:memberId "
									+ "order by id desc").setParameter(
							"memberId", memberId), offset, limit);
		}
	}

	public List<Invoice> getAllInvoicesForMember(String memberId) {

		return getResultList(getEntityManager().createQuery(
				"FROM Invoice where isActive=1 " + "and memberId=:memberId "
						+ "order by id desc")
				.setParameter("memberId", memberId));
	}

	public int getInvoiceCount() {
		Number number = getSingleResultOrNull(getEntityManager().createQuery(
				"SELECT count(i) from Invoice i where i.isActive=1"));
		return number.intValue();
	}

	public Integer getInvoiceCount(String memberId) {

		Number number = getSingleResultOrNull(getEntityManager().createQuery(
				"SELECT count(i) from Invoice i where i.isActive=1 and memberId=:memberId")
				.setParameter("memberId", memberId));
		
		return number.intValue();

	}

}
