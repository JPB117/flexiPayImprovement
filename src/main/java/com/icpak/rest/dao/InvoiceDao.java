package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.workpoint.icpak.shared.model.InvoiceDto;
import com.workpoint.icpak.shared.model.InvoiceLineDto;
import com.workpoint.icpak.shared.model.InvoiceLineType;
import com.workpoint.icpak.shared.model.InvoiceSummary;
import com.workpoint.icpak.shared.model.PaymentMode;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.PaymentType;
import com.workpoint.icpak.shared.model.TransactionDto;

public class InvoiceDao extends BaseDao {

	public List<TransactionDto> getAllTransactions(String memberId,
			Integer offset, Integer limit) {
		StringBuffer sqlBuffer = new StringBuffer(
				"select created,paymentMode,paymentType,description,trxNumber,accountNo,invoiceAmount,chargableAmount,"
						+ "totalPreviousPayments,amount,balance,invoiceRef from transaction");
		Query query = null;
		if (memberId == null || memberId.equals("ALL")) {
			sqlBuffer.append(" order by created desc");
			query = getEntityManager().createNativeQuery(sqlBuffer.toString());
		}

		List<Object[]> rows = getResultList(query, offset, limit);
		List<TransactionDto> transactions = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;

			Date createdDate = (value = row[i++]) == null ? null : (Date) value;
			PaymentMode paymentMode = (value = row[i++]) == null ? null
					: PaymentMode.valueOf(value.toString());
			PaymentType paymentType = (value = row[i++]) == null ? null
					: PaymentType.valueOf(value.toString());
			String description = (value = row[i++]) == null ? null : value
					.toString();
			String trxNumber = (value = row[i++]) == null ? null : value
					.toString();
			String accountNo = (value = row[i++]) == null ? null : value
					.toString();
			Double invoiceAmount = (value = row[i++]) == null ? null
					: new Double(value.toString());
			Double chargableAmount = (value = row[i++]) == null ? null
					: new Double(value.toString());
			Double totalPreviousPayments = (value = row[i++]) == null ? null
					: new Double(value.toString());
			Double amountPaid = (value = row[i++]) == null ? null : new Double(
					value.toString());
			Double balance = (value = row[i++]) == null ? null : new Double(
					value.toString());
			String invoiceRef = (value = row[i++]) == null ? null : value
					.toString();

			TransactionDto trx = new TransactionDto();
			trx.setCreatedDate(createdDate);
			trx.setPaymentMode(paymentMode);
			trx.setPaymentType(paymentType);
			trx.setDescription(description);
			trx.setTrxNumber(trxNumber);
			trx.setAccountNo(accountNo);
			trx.setInvoiceAmt(invoiceAmount);
			trx.setChargableAmnt(chargableAmount);
			trx.setTotalPreviousPayments(totalPreviousPayments);
			trx.setAmountPaid(amountPaid);
			trx.setTotalBalance(balance);
			trx.setInvoiceRef(invoiceRef);
			transactions.add(trx);
		}

		return transactions;
	}

	public List<InvoiceDto> checkInvoicePaymentStatus(String invoiceRefId) {
		StringBuffer sqlBuffer = new StringBuffer("select i.refId, "
				+ "i.amount as invoiceAmount,i.date as invoiceDate,"
				+ "i.documentNo,i.description,i.contactName,i.status "
				+ "from Invoice i " + "where i.isActive=1 ");

		Query query = null;
		sqlBuffer.append("and i.refId=:invoiceRef");
		sqlBuffer.append(" order by i.id desc");
		query = getEntityManager().createNativeQuery(sqlBuffer.toString())
				.setParameter("invoiceRef", invoiceRefId);

		List<Object[]> rows = getResultList(query);
		List<InvoiceDto> statements = new ArrayList<>();

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

			String contactName = (value = row[i++]) == null ? null : value
					.toString();

			PaymentStatus status = (value = row[i++]) == null ? null
					: PaymentStatus.valueOf((String) value);

			InvoiceDto statement = new InvoiceDto();
			statement.setRefId(refId);
			statement.setInvoiceAmount(invoiceAmount);
			statement.setDocumentNo(documentNo);
			statement.setDescription(description);
			statement.setInvoiceDate(invoiceDate);
			statement.setContactName(contactName);
			statement.setStatus(status);
			statements.add(statement);
		}

		return statements;
	}

	public InvoiceDto getInvoiceByDocumentNo(String docNo) {
		StringBuffer sqlBuffer = new StringBuffer(
				"select i.refId, "
						+ "i.amount as invoiceAmount,i.date as invoiceDate,"
						+ "i.documentNo,i.description,i.contactName,t.refId as trxRefId,"
						+ "i.bookingRefId,i.totalDiscount,i.totalPenalty "
						+ "from Invoice i "
						+ "left join Transaction t on (i.refId=t.invoiceRef) "
						+ "where i.isActive=1 and i.documentNo=:docNo ");

		Query query = getEntityManager()
				.createNativeQuery(sqlBuffer.toString()).setParameter("docNo",
						docNo);

		List<Object[]> rows = getResultList(query);
		InvoiceDto dbInvoice = null;
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

			String contactName = (value = row[i++]) == null ? null : value
					.toString();

			String trxRefId = (value = row[i++]) == null ? null : value
					.toString();

			String bookingRefId = (value = row[i++]) == null ? null : value
					.toString();
			Double totalDiscount = (value = row[i++]) == null ? null
					: new Double(value.toString());

			Double totalPenalty = (value = row[i++]) == null ? null
					: new Double(value.toString());

			dbInvoice = new InvoiceDto(refId, invoiceAmount, documentNo,
					description, invoiceDate, contactName, trxRefId,
					bookingRefId);
			dbInvoice.setRefId(refId);
			dbInvoice.setTotalDiscount(totalDiscount);
			dbInvoice.setTotalPenalty(totalPenalty);
		}
		return dbInvoice;
	}

	// public List<Invoice> getAllInvoicesForMember(String memberId) {
	// return getResultList(getEntityManager().createQuery(
	// "FROM Invoice where isActive=1 " + "and memberId=:memberId "
	// + "order by id desc")
	// .setParameter("memberId", memberId));
	// }

	// public int getInvoiceCount() {
	// Number number = getSingleResultOrNull(getEntityManager().createQuery(
	// "SELECT count(i) from Invoice i where i.isActive=1"));
	// return number.intValue();
	// }

	public Integer getInvoiceCount(String memberId) {
		StringBuffer sqlBuffer = new StringBuffer(
				"select count(*) from transaction");
		Query query = null;
		if (memberId == null || memberId.equals("ALL")) {
			query = getEntityManager().createNativeQuery(sqlBuffer.toString());
		} else {
			sqlBuffer.append("and t.memberId=:memberId ");
			query = getEntityManager().createNativeQuery(sqlBuffer.toString())
					.setParameter("memberId", memberId);
		}
		Number number = getSingleResultOrNull(query);
		return number.intValue();
	}

	public InvoiceSummary getSummary(String memberId) {
		StringBuffer sqlBuffer = new StringBuffer(
				"select sum(i.amount),status from Invoice i "
						+ "left join Transaction t "
						+ "on (i.refId=t.invoiceRef) " + "where i.isActive=1 ");

		Query query = null;
		if (memberId == null || memberId.equals("ALL")) {
			sqlBuffer.append("group by t.status ");
			query = getEntityManager().createNativeQuery(sqlBuffer.toString());
		} else {
			sqlBuffer.append("and t.memberId=:memberId ");
			sqlBuffer.append("group by t.status ");
			query = getEntityManager().createNativeQuery(sqlBuffer.toString())
					.setParameter("memberId", memberId);
		}

		List<Object[]> rows = getResultList(query);
		InvoiceSummary summary = new InvoiceSummary();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;

			Double invoiceAmount = (value = row[i++]) == null ? null
					: new Double(value.toString());
			String status = (value = row[i++]) == null ? null : value
					.toString();
			if (status == null || status.equals("NOTPAID")) {
				summary.setUnpaid(invoiceAmount
						+ (summary.getUnpaid() == null ? 0 : summary
								.getUnpaid()));
			} else {
				summary.setPaid(invoiceAmount);
			}
		}

		return summary;
	}

	public List<InvoiceLineDto> getInvoiceLinesByDocumentNo(String documentNo) {
		List<InvoiceLineDto> invoiceLines = new ArrayList<>();

		String sql = "select "
				+ "l.refId,l.description,l.quantity,l.unitprice,l.totalamount,l.type "
				+ "from " + "invoiceline l " + "where " + "l.invoiceId="
				+ "(select n.id from invoice n where n.documentNo=:documentNo)";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"documentNo", documentNo);

		List<Object[]> rows = getResultList(query);

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String refId = (value = row[i++]) == null ? null : value.toString();
			String description = (value = row[i++]) == null ? null : value
					.toString();
			int quantity = (value = row[i++]) == null ? null : (Integer) value;
			Double unitPrice = (value = row[i++]) == null ? null
					: (Double) value;
			Double totalAmount = (value = row[i++]) == null ? null
					: (Double) value;
			InvoiceLineType type = (value = row[i++]) == null ? null
					: InvoiceLineType.valueOf((String) value);

			InvoiceLineDto dto = new InvoiceLineDto();
			dto.setRefId(refId);
			dto.setDescription(description);
			dto.setQuantity(quantity);
			dto.setUnitPrice(unitPrice);
			dto.setTotalAmount(totalAmount);
			dto.setType(type);
			invoiceLines.add(dto);
		}

		return invoiceLines;
	}

}
