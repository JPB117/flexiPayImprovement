package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.icpak.rest.models.trx.Invoice;
import com.workpoint.icpak.shared.model.InvoiceDto;

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

	public List<InvoiceDto> getAll(String userId, Integer offset, Integer limit) {
		StringBuffer sqlBuffer = new StringBuffer("select i.refId, "
				+ "i.amount as invoiceAmount,i.date as invoiceDate,"
				+ "i.documentNo,i.description," + "t.date,t.dueDate,t.status,"
				+ "t.paymentMode,t.amount,t.userId " + "from Invoice i "
				+ "left join Transaction t on (i.refId=t.invoiceRef) "
				+ "where " + "i.isActive=1 ");

		Query query = null;
		if (userId == null || userId.equals("ALL")) {
			sqlBuffer.append(" order by i.id desc");
			query = getEntityManager().createNativeQuery(sqlBuffer.toString());
		} else {
			sqlBuffer.append("and t.userId=:userId");
			sqlBuffer.append(" order by i.id desc");
			query = getEntityManager().createNativeQuery(sqlBuffer.toString())
					.setParameter("userId", userId);
		}

		System.err.println(sqlBuffer.toString());
		List<Object[]> rows = getResultList(query, offset, limit);
		List<InvoiceDto> statements = new ArrayList<>();

		byte boolTrue = 1;
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;

			String refId = (value = row[i++]) == null ? null : value.toString();
			Double invoiceAmount = (value = row[i++]) == null ? null
					: new Double(value.toString());

			Date invoiceDate = (value = row[i++]) == null ? null : (Date) value;

			String documentNo = (value = row[i++]) == null ? null : value
					.toString();

			String description = (value = row[i++]) == null ? null : value
					.toString();

			Date transactionDate = (value = row[i++]) == null ? null
					: (Date) value;
			Date dueDate = (value = row[i++]) == null ? null : (Date) value;
			String paymentStatus = (value = row[i++]) == null ? null : value
					.toString();
			String paymentMode = (value = row[i++]) == null ? null : value
					.toString();
			Double transactionAmount = (value = row[i++]) == null ? null
					: new Double(value.toString());
			String userRefId = (value = row[i++]) == null ? null : value
					.toString();

			System.err.println(invoiceAmount);

			InvoiceDto statement = new InvoiceDto(refId, invoiceAmount,
					documentNo, description, invoiceDate, transactionDate,
					dueDate, paymentStatus, paymentMode, transactionAmount,
					userRefId);
			statements.add(statement);

		}

		return statements;

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

		Number number = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"SELECT count(i) from Invoice i where i.isActive=1 and memberId=:memberId")
				.setParameter("memberId", memberId));

		return number.intValue();

	}

}
