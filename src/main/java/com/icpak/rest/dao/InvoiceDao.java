package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.models.trx.Invoice;

public class InvoiceDao extends BaseDao{

	public List<Invoice> getAllInvoices() {
		return getResultList(getEntityManager().createQuery("FROM Invoice where isActive=1"));
	}

	public List<Invoice> getAllInvoicesForMember(String memberId) {
		
		return getResultList(getEntityManager().createQuery("FROM Invoice where isActive=1 and memberId=:memberId")
				.setParameter("memberId", memberId));
	}
	
}
