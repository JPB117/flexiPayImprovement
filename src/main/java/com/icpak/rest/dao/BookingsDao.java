package com.icpak.rest.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.QueryException;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Accommodation;
import com.icpak.rest.models.event.Booking;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.BookingDto;
import com.workpoint.icpak.shared.model.events.BookingSummaryDto;
import com.workpoint.icpak.shared.model.events.ContactDto;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

public class BookingsDao extends BaseDao {
	Logger logger = Logger.getLogger(BookingsDao.class);

	@Inject
	BookingsDaoHelper daoHelper;

	public Booking getByBookingId(String refId) {
		Booking booking = getSingleResultOrNull(getEntityManager().createQuery(
				"from Booking u where u.refId=:refId").setParameter("refId",
				refId));
		if (booking == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Booking", "'"
					+ refId + "'");
		}
		return booking;
	}

	public Booking getBySponsorEmail(String email, Long eventId) {
		Booking booking = getSingleResultOrNull(getEntityManager()
				.createQuery(
						"from Booking u where u.contact.email=:email and event.id=:eventId and isActive=1")
				.setParameter("email", email).setParameter("eventId", eventId));
		if (booking == null) {
			logger.debug("Booking for sponsor email:" + email
					+ " not found for event:" + eventId);
		} else {
			logger.debug("Duplicate Booking found for email:" + email
					+ " and eventRefId:" + eventId);
		}
		return booking;
	}

	public void createBooking(Booking booking) {
		save(booking);
	}

	public List<Booking> getAllBookings(String eventId, Integer offSet,
			Integer limit, String searchTerm) throws QueryException {

		if (searchTerm.isEmpty()) {
			return getResultList(
					getEntityManager()
							.createQuery(
									"from Booking where isActive=1 order by startDate desc"),
					offSet, limit);
		}

		String query = "select count(*) from Booking b where b.isActive=1 "
				+ "and b.event.refId=:refId and"
				+ "(e.name like :searchTerm or "
				+ "e.description like :searchTerm or "
				+ "e.venue like :searchTerm or "
				+ "e.categoryName like :searchTerm or "
				+ "e.type like :searchTerm " + ")"
				+ " order by e.startDate desc";

		return getResultList(getEntityManager().createQuery(query)
				.setParameter("searchTerm", searchTerm), offSet, limit);
	}

	public void importAllEvents() {
		String sqlQuery = "select oldSystemId,refId from icpakdb.event "
				+ "where oldSystemId IN (480,479,474,469)";
		// String sqlQuery =
		// "select oldSystemId,refId from icpakdb.event where oldSystemId=424";
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sqlQuery));
		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer seminarId = (value = row[i++]) == null ? null
					: (Integer) value;
			String eventRefId = (value = row[i++]) == null ? null : value
					.toString();
			importDistinctSponsor(seminarId, eventRefId);
		}
	}

	public void importDistinctSponsor(Integer seminarId, String eventRefId) {
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(
						"SELECT distinct(batch_ID) as batchId,s_name,b.booking_date,b.s_country,"
								+ "b.s_address,b.s_town,b.s_telephone,b.contact,b.s_email,b.code "
								+ "FROM icpakco_main.bookings b WHERE b.seminar_id = :seminarId "
								+ "group by b.batch_ID").setParameter(
						"seminarId", seminarId));

		int totalSponsorCount = 0;
		int totalDelegateCount = 0;
		int totalPaidCount = 0;
		int totalUnpaidCount = 0;
		int[] resultArray = new int[10];
		for (Object[] row : rows) {
			int i = 0;
			BookingDto booking = new BookingDto();
			Object value = null;
			Integer batchId = (value = row[i++]) == null ? null
					: (Integer) value;
			String sponsorName = (value = row[i++]) == null ? null : value
					.toString();

			Date bookingDate = (value = row[i++]) == null ? null : (Date) value;
			booking.setBookingDate(bookingDate.getTime());
			String sponsorCountry = (value = row[i++]) == null ? null : value
					.toString();
			String sponsorAddress = (value = row[i++]) == null ? null : value
					.toString();
			String sponsorTown = (value = row[i++]) == null ? null : value
					.toString();
			String sponsorTelephone = (value = row[i++]) == null ? null : value
					.toString();
			String sponsorContact = (value = row[i++]) == null ? null : value
					.toString();
			String sponsorEmail = (value = row[i++]) == null ? null : value
					.toString();
			String postalCode = (value = row[i++]) == null ? null : value
					.toString();

			ContactDto contact = new ContactDto();
			contact.setAddress(sponsorAddress);
			contact.setCity(sponsorTown);
			contact.setCompany(sponsorName);
			contact.setContactName(sponsorContact);
			contact.setCountry(sponsorCountry);
			contact.setEmail(sponsorEmail);
			contact.setPostCode(postalCode);
			contact.setTelephoneNumbers(sponsorTelephone);
			booking.setContact(contact);

			System.err.println(">>" + booking.getContact().getContactName());

			// System.err.println("Company Name>>" + contact.getCompany());
			resultArray = importDelegates(batchId, booking, eventRefId,
					seminarId);
			totalDelegateCount += resultArray[0];
			totalPaidCount += resultArray[1];
			totalUnpaidCount += resultArray[2];
			totalSponsorCount++;

		}

		System.err.println("Total Sponsor Count:::" + totalSponsorCount);
		System.err.println("Total Delegate Count:::" + totalDelegateCount);
		System.err.println("Total Paid Counter:::" + totalPaidCount);
		System.err.println("Total UnPaid Counter:::" + totalUnpaidCount);

	}

	public int[] importDelegates(Integer batchId, BookingDto booking,
			String eventRefId, Integer seminarId) {
		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(
						"SELECT b.title,b.surname,b.othernames,b.is_member,b.reg_no,"
								+ "b.email,a.refId,b.accomodation,b.accomodation_days,b.payment_mode,"
								+ "b.receipt,b.lpo,b.credit,b.clearance_no  "
								+ "FROM icpakco_main.bookings b left join icpakdb.accommodation a "
								+ "on (a.hotel = b.accomodation) "
								+ "WHERE b.batch_ID = :batchId and "
								+ "seminar_id=:seminarId")
				.setParameter("batchId", batchId)
				.setParameter("seminarId", seminarId));

		List<DelegateDto> delegates = new ArrayList<>();
		int paidCounter = 0;
		int unPaidCounter = 0;
		for (Object[] row : rows) {
			DelegateDto delegate = new DelegateDto();
			int i = 0;
			Object value = null;
			String delegateTitle = (value = row[i++]) == null ? null : value
					.toString();
			delegate.setTitle(delegateTitle);
			String delegateSurName = (value = row[i++]) == null ? null : value
					.toString();
			delegate.setSurname(delegateSurName);
			String delegateOtherName = (value = row[i++]) == null ? null
					: value.toString();
			delegate.setOtherNames(delegateOtherName);
			String isMember = (value = row[i++]) == null ? null
					: (String) value;
			String delegateMemberNo = (value = row[i++]) == null ? null : value
					.toString();
			if (isMember != null && isMember.equals("1")) {
				delegate.setMemberNo(delegateMemberNo);
			}
			String delegateEmail = (value = row[i++]) == null ? null : value
					.toString();
			delegate.setEmail(delegateEmail);
			String accomodationRefId = (value = row[i++]) == null ? null
					: value.toString();
			if (accomodationRefId != null) {
				Accommodation accomodation = findByRefId(accomodationRefId,
						Accommodation.class);
				delegate.setAccommodation(accomodation.toDto());
				// System.err.println("Accomodation for " +
				// delegate.getSurname()
				// + " " + accomodation.getHotel());
			}
			String delegateAccomodation = (value = row[i++]) == null ? null
					: value.toString();
			String delegateAccomodationDays = (value = row[i++]) == null ? null
					: value.toString();
			delegate.setAttendance(AttendanceStatus.NOTATTENDED);

			String paymentMode = (value = row[i++]) == null ? null : value
					.toString();
			// System.err.println("PaymentMode>>>" + paymentMode);
			if (paymentMode != null) {
				if (!paymentMode.equals("N/A") || !paymentMode.isEmpty()) {
					paidCounter++;
					booking.setPaymentMode(paymentMode);
					booking.setPaymentStatus(PaymentStatus.PAID);
				} else {
					unPaidCounter++;
					booking.setPaymentStatus(PaymentStatus.NOTPAID);
				}
			} else {
				unPaidCounter++;
				booking.setPaymentStatus(PaymentStatus.NOTPAID);
			}

			String receiptNo = (value = row[i++]) == null ? null : value
					.toString();
			delegate.setReceiptNo(receiptNo);
			String isCredit = (value = row[i++]) == null ? null : value
					.toString();
			if (isCredit != null && !isCredit.isEmpty()) {
				delegate.setIsCredit(1);
			} else {
				delegate.setIsCredit(0);
			}

			String lpoNo = (value = row[i++]) == null ? null : value.toString();
			delegate.setLpoNo(lpoNo);
			String clearanceNo = (value = row[i++]) == null ? null : value
					.toString();
			delegate.setClearanceNo(clearanceNo);
			delegates.add(delegate);
		}

		booking.setDelegates(delegates);
		daoHelper.createBooking(eventRefId, booking);

		// daoHelper.sendProInvoice(booking.getRefId());

		int[] results = new int[3];
		results[0] = delegates.size();
		results[1] = paidCounter;
		results[2] = unPaidCounter;
		return results;
	}

	public List<Booking> getAllBookings(String eventId, Integer offSet,
			Integer limit) {
		return getResultList(getEntityManager().createQuery(
				"from Booking b where b.isActive=1 "
						+ "and b.event.refId=:refId order by created")
				.setParameter("refId", eventId), offSet, limit);
	}

	public List<Booking> getAllBookings(Long eventId) {
		return getResultList(getEntityManager().createQuery(
				"from Booking b where b.event.id=:Id order by created")
				.setParameter("Id", eventId));
	}

	public void updateBooking(Booking booking) {
		createBooking(booking);
	}

	public boolean isPaymentValid(String paymentRef) {
		return false;
	}

	public String getInvoiceRef(String bookingRefId) {
		return getSingleResultOrNull(getEntityManager().createNativeQuery(
				"select refId from Invoice where bookingRefId=:bookingRefId")
				.setParameter("bookingRefId", bookingRefId));
	}

	public void deleteAllBookingInvoice(String bookingRefId) {
		logger.error("Deleting invoice line ====== ><><<>>>>>>>>===");
		Query deleteQuery = getEntityManager().createNativeQuery(
				"delete from InvoiceLine " + "where invoiceId in ( "
						+ "select d.id from Invoice d "
						+ "where d.bookingRefId = :bookingRefId)");
		deleteQuery.setParameter("bookingRefId", bookingRefId);
		deleteQuery.executeUpdate();

		logger.error("Deleting invoice ====== ><><<>>>>>>>>===");
		deleteQuery = getEntityManager().createNativeQuery(
				"delete from Invoice " + "where bookingRefId = :bookingRefId");
		deleteQuery.setParameter("bookingRefId", bookingRefId);
		deleteQuery.executeUpdate();

		logger.error("Deleteing Success ====== ><><<>>>>>>>>===");
	}

	public List<MemberBookingDto> getMemberBookings(String memberRefId,
			int offset, int limit) {
		String sql = "select b.refId bookingRefId, "
				+ "e.refId eventRefId, "
				+ "d.refId delegateRefId, "
				+ "e.name, "
				+ "e.startdate, "
				+ "e.enddate, "
				+ "e.venue, "
				+ "e.cpdhours, "
				+ "(case when e.enddate<current_date then 'CLOSED' else 'OPEN' end) eventStatus, "
				+ "d.attendance," + "b.paymentStatus,b.isActive, " + "a.hotel "
				+ "from event e "
				+ "inner join booking b on (e.id=b.event_Id)  "
				+ "inner join delegate d on (d.booking_id=b.id) "
				+ "left join accommodation a on (a.id=d.accommodationid) "
				+ "left join invoice i on (i.bookingRefId=b.refId) "
				+ "where d.memberRefId=:memberRefId";

		List<Object[]> rows = getResultList(getEntityManager()
				.createNativeQuery(sql)
				.setParameter("memberRefId", memberRefId), offset, limit);
		List<MemberBookingDto> memberEvents = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			String bookingRefId = (value = row[i++]) == null ? null : value
					.toString();
			String eventRefId = (value = row[i++]) == null ? null : value
					.toString();
			String delegateRefId = (value = row[i++]) == null ? null : value
					.toString();
			String eventName = (value = row[i++]) == null ? null : value
					.toString();
			Date startDate = (value = row[i++]) == null ? null : (Date) value;
			Date endDate = (value = row[i++]) == null ? null : (Date) value;
			String venue = (value = row[i++]) == null ? null : value.toString();
			String cpdHours = (value = row[i++]) == null ? null : value
					.toString();
			String eventStatus = (value = row[i++]) == null ? null : value
					.toString();
			Integer attendance = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			Integer paymentStatus = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			Integer bookingStatus = (value = row[i++]) == null ? null
					: ((Number) value).intValue();
			String hotel = (value = row[i++]) == null ? null : value.toString();

			MemberBookingDto dto = new MemberBookingDto();
			dto.setAccommodation(hotel);
			dto.setAttendance(attendance == AttendanceStatus.ATTENDED.ordinal() ? AttendanceStatus.ATTENDED
					: AttendanceStatus.NOTATTENDED);
			dto.setBookingRefId(bookingRefId);
			dto.setCpdHours(cpdHours);
			dto.setDelegateRefId(delegateRefId);
			dto.setEndDate(endDate);
			dto.setEventName(eventName);
			dto.setEventRefId(eventRefId);
			dto.setEventStatus(EventStatus.valueOf(eventStatus));
			dto.setLocation(venue);
			dto.setPaymentStatus(PaymentStatus.PAID.ordinal() == paymentStatus ? PaymentStatus.PAID
					: PaymentStatus.NOTPAID);
			dto.setBookingStatus(bookingStatus);
			dto.setStartDate(startDate);
			memberEvents.add(dto);
		}

		return memberEvents;
	}

	public String getErn(String refId) {
		return getSingleResultOrNull(getEntityManager().createNativeQuery(
				"select ern from delegate where refId=:refId").setParameter(
				"refId", refId));
	}

	public int getDelegateCount(String eventId) {
		return getDelegateCount(eventId, null, "", "");
	}

	public int getGenericDelegateCount(Long bookingId, String bookingType) {
		return getGenericDelegateCount(bookingId, bookingType, false);
	}

	public int getGenericDelegateCount(Long bookingId, String bookingType,
			boolean isMember) {
		Number number = null;
		String sql = "select count(*) "
				+ "from delegate d where booking_id=:bId";

		/* Query */
		if (bookingType != null && !bookingType.isEmpty()) {
			if (bookingType.equals("paid")) {
				sql = sql.concat(" and d.paymentStatus=:bookingType");
			}
			if (bookingType.equals("withAccomodation")) {
				sql = sql.concat(" and d.accommodationId IS NOT NULL");
			}
			if (bookingType.equals("attended")) {
				sql = sql.concat(" and d.attendance=0");
			}
			if (bookingType.equals("cancelled")) {
				sql = sql.concat(" and d.isActive=0");
				if (isMember) {
					sql = sql.concat(" and d.memberRegistrationNo IS NOT NULL");
				} else {
					sql = sql.concat(" and d.memberRegistrationNo IS NULL");
				}
			}

		}

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"bId", bookingId);
		/* Parameters */
		if (bookingType != null && !bookingType.isEmpty()) {
			if (bookingType.equals("paid")) {
				query.setParameter("bookingType", bookingType);
			}
		}

		number = getSingleResultOrNull(query);
		logger.error("=== Delegate Count ==== " + number.intValue());
		return number.intValue();
	}

	public int getStaffTransactionCount(Long eventId, String offlinePaymentType) {
		Number number = null;
		String sql = "select count(*) from delegate d "
				+ "inner join booking b on d.booking_id=b.id "
				+ "where b.event_id=:eventId";

		/* Query */
		if (offlinePaymentType != null && !offlinePaymentType.isEmpty()) {
			if (offlinePaymentType.equals("receipt")) {
				sql = sql
						.concat(" and receiptNo is not null and receiptNo<>''");
			}
			if (offlinePaymentType.equals("lpo")) {
				sql = sql.concat(" and d.lpoNo is not null and d.lpoNo<>''");
			}
		}

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventId", eventId);
		/* Parameters */
		if (offlinePaymentType != null && !offlinePaymentType.isEmpty()) {
			if (offlinePaymentType.equals("paid")) {
				query.setParameter("bookingType", offlinePaymentType);
			}
		}

		number = getSingleResultOrNull(query);
		logger.error("=== Delegate Count ==== " + number.intValue());
		return number.intValue();

	}

	public BookingSummaryDto getBookingSummary(String eventId) {
		String sql = "select SUM(delegatesCount),SUM(totalPaid),SUM(totalWithAccomodation),"
				+ "SUM(totalAttended),SUM(totalCancelled),SUM(totalCancelledMembers),"
				+ "SUM(totalCancelledNonMembers),SUM(totalMembers),SUM(totalNonMembers),"
				+ "SUM(totalPaidMembers),SUM(totalPaidNonMembers) from booking "
				+ "where event_id=(select id from event where refId=:eventId)";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventId", eventId);
		Object[] row = getSingleResultOrNull(query);
		int i = 0;
		Object value = null;
		Integer totalDelegates = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalPaid = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalWithAccomodation = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalAttended = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalCancelled = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalCancelledMembers = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalCancelledNonMembers = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalMembers = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalNonMembers = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalPaidMembers = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();
		Integer totalPaidNonMembers = (value = row[i++]) == null ? null
				: ((BigDecimal) value).intValue();

		System.err.println(totalDelegates + " " + totalPaid + " "
				+ totalWithAccomodation + " " + totalAttended + " "
				+ totalCancelled);

		System.err.println(totalDelegates + " " + totalPaid + " "
				+ totalWithAccomodation + " " + totalAttended + " "
				+ totalCancelled);

		BookingSummaryDto summary = new BookingSummaryDto();
		summary.setTotalDelegates(totalDelegates);
		summary.setTotalPaid(totalPaid);
		summary.setTotalWithAccomodation(totalWithAccomodation);
		summary.setTotalAttended(totalAttended);
		summary.setTotalCancelled(totalCancelled);
		summary.setTotalMembers(totalMembers);
		summary.setTotalNonMembers(totalNonMembers);
		summary.setTotalPaid(totalPaidMembers);
		summary.setTotalPaidNonMembers(totalPaidNonMembers);
		summary.setTotalPaidMembers(totalPaidMembers);
		summary.setTotalCancelledMembers(totalCancelledMembers);
		summary.setTotalCancelledNonMembers(totalCancelledNonMembers);

		return getEventSummary(summary, eventId);
	}

	private BookingSummaryDto getEventSummary(BookingSummaryDto summary,
			String eventId) {
		String sql = "select totalMpesaPayments,totalCardsPayment,totalOfflinePayment,"
				+ "totalCredit,totalReceiptPayment from event "
				+ "where refId=:eventId";
		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventId", eventId);
		Object[] row = getSingleResultOrNull(query);
		int i = 0;
		Object value = null;
		Integer totalMpesaPayments = (value = row[i++]) == null ? null
				: (Integer) value;
		Integer totalCardsPayment = (value = row[i++]) == null ? null
				: ((Integer) value);
		Integer totalOfflinePayment = (value = row[i++]) == null ? null
				: ((Integer) value);
		Integer totalCredit = (value = row[i++]) == null ? null
				: ((Integer) value);
		Integer totalReceiptPayment = (value = row[i++]) == null ? null
				: ((Integer) value);

		summary.setTotalMpesaPayments(totalMpesaPayments);
		summary.setTotalCardsPayment(totalCardsPayment);
		summary.setTotalOfflinePayment(totalOfflinePayment);
		summary.setTotalCredit(totalCredit);
		summary.setTotalReceiptPayment(totalReceiptPayment);
		return summary;
	}

	public int getDelegateCount(String eventId, String searchTerm,
			String accomodationRefId, String bookingStatus) {
		Number number = null;
		String sql = "select count(*) "
				+ "from delegate d inner join booking b on (d.booking_id=b.id) "
				+ "inner join event e on (b.event_id=e.id) "
				+ "inner join invoice i on(b.refId=i.bookingRefId)"
				+ "left join accommodation a on (d.accommodationId=a.id) "
				+ "where e.refId=:eventRefId";

		if (accomodationRefId != null && !accomodationRefId.isEmpty()) {
			sql = sql.concat(" and a.refId=:accomodationRefId");
		}

		if (bookingStatus != null && !bookingStatus.isEmpty()) {
			sql = sql.concat(" and b.isActive=:bookingStatus");
		}
		if (searchTerm != null && !searchTerm.isEmpty()) {
			sql = sql.concat(" and "
					+ "(d.memberRegistrationNo like :searchTerm or "
					+ "d.email like :searchTerm or "
					+ "d.fullName like :searchTerm or "
					+ "d.ern like :searchTerm or "
					+ "b.`E-mail` like :searchTerm or "
					+ "b.company like :searchTerm or "
					+ "b.Contact like :searchTerm or "
					+ "i.documentNo like :searchTerm)");
		}
		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventRefId", eventId);
		/* Parameters */
		if (searchTerm != null && !searchTerm.isEmpty()) {
			query.setParameter("searchTerm", "%" + searchTerm + "%");
		}
		if (accomodationRefId != null && !accomodationRefId.isEmpty()) {
			query.setParameter("accomodationRefId", accomodationRefId);
		}
		if (bookingStatus != null && !bookingStatus.isEmpty()) {
			query.setParameter("bookingStatus", bookingStatus);
		}
		number = getSingleResultOrNull(query);
		logger.error("=== Delegate Count ==== " + number.intValue());
		return number.intValue();
	}

	public List<DelegateDto> getAllDelegates(String passedRefId,
			Integer offset, Integer limit, String searchTerm,
			String accomodationRefId, String bookingStatus) {
		return getAllDelegates(passedRefId, offset, limit, searchTerm, false,
				accomodationRefId, bookingStatus);
	}

	public List<String> correctDoubleBookings(String eventRefId) {
		String sql = "SELECT d.memberRegistrationNo,COUNT(memberRegistrationNo) FROM delegate d "
				+ "LEFT JOIN booking b ON (d.booking_id = b.id) WHERE b.event_id ="
				+ " (SELECT id FROM event WHERE refId = :eventRefId)"
				+ " and b.`E-Mail`='anne.njagi@icpak.com'"
				+ " GROUP BY d.memberRegistrationNo HAVING (COUNT(d.memberRegistrationNo)> 1);";

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventRefId", eventRefId);

		List<Object[]> rows = getResultList(query);
		List<String> allMembers = new ArrayList<>();

		for (Object o[] : rows) {
			int i = 0;
			Object value = null;
			String memberRegistrationNo = (value = o[i++]) == null ? null
					: value.toString();
			Integer count = (value = o[i++]) == null ? null
					: ((BigInteger) value).intValue();
			System.err.println(">>memberNo::" + memberRegistrationNo
					+ ">>count::" + count);
			allMembers.add(memberRegistrationNo);
		}
		return allMembers;
	}

	/*
	 * Add param isByQrCode - If you want to only get results by ONLY QR CODE
	 */

	public List<DelegateDto> getAllDelegates(String passedRefId,
			Integer offset, Integer limit, String searchTerm,
			boolean isByQrCode, String accomodationRefId, String bookingStatus) {
		return getAllDelegates(passedRefId, offset, limit, searchTerm,
				isByQrCode, accomodationRefId, bookingStatus, null);
	}

	public List<DelegateDto> getAllDelegates(String passedRefId,
			Integer offset, Integer limit, String searchTerm,
			boolean isByQrCode, String accomodationRefId, String bookingStatus,
			String memberRegistrationNo) {
		logger.error("==getting delegate for this event===>" + passedRefId);
		logger.error("==Is search limitted to only QR Code==>" + isByQrCode);
		logger.error("==Search Term ==>>>>" + searchTerm);

		List<DelegateDto> delegateList = new ArrayList<>();
		String sql = "select b.refId as bookingRefId,b.bookingDate,"
				+ "b.company,b.Contact, b.`E-Mail`,b.`Phone No_`,"
				+ "d.refId,d.memberRefId,d.memberRegistrationNo,d.ern,"
				+ "d.title,d.otherNames,d.fullName,d.phoneNumber,a.hotel,b.paymentStatus,"
				+ "d.attendance,d.surname,d.email,e.refid,d.booking_id,"
				+ "d.receiptNo,d.lpoNo,d.isCredit,d.clearanceNo,b.isActive,d.isActive as delegateBookingStatus,"
				+ "i.documentNo,"
				+ "d.paymentStatus as delegatePaymentStatus,d.updated,d.updatedBy "
				+ "from delegate d inner join booking b on (d.booking_id=b.id) "
				+ "inner join event e on (b.event_id=e.id) "
				+ "inner join invoice i on(b.refId=i.bookingRefId)"
				+ "left join accommodation a on (d.accommodationId=a.id) "
				+ "where e.refId=:eventRefId";

		if (accomodationRefId != null && !accomodationRefId.isEmpty()) {
			sql = sql.concat(" and a.refId=:accomodationRefId");
		}

		if (bookingStatus != null && !bookingStatus.isEmpty()) {
			sql = sql.concat(" and b.isActive=:bookingStatus");
		}
		if (memberRegistrationNo != null && !memberRegistrationNo.isEmpty()) {
			sql = sql
					.concat(" and d.memberRegistrationNo=:memberRegistrationNo");
		}

		if (searchTerm != null && !searchTerm.isEmpty()) {
			if (isByQrCode) {
				sql = sql.concat(" and (d.memberQrCode=:searchTerm)");
			} else {
				sql = sql.concat(" and "
						+ "(d.memberRegistrationNo like :searchTerm or "
						+ "d.email like :searchTerm or "
						+ "d.fullName like :searchTerm or "
						+ "d.ern like :searchTerm or "
						+ "b.`E-mail` like :searchTerm or "
						+ "b.company like :searchTerm or "
						+ "b.Contact like :searchTerm or "
						+ "i.documentNo like :searchTerm)");
			}
		}

		sql = sql.concat(" order by d.created DESC");

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventRefId", passedRefId);
		/* Parameters */
		if (searchTerm != null && !searchTerm.isEmpty()) {
			if (isByQrCode) {
				query.setParameter("searchTerm", searchTerm);
			} else {
				query.setParameter("searchTerm", "%" + searchTerm + "%");
			}
		}
		if (accomodationRefId != null && !accomodationRefId.isEmpty()) {
			query.setParameter("accomodationRefId", accomodationRefId);
		}
		if (bookingStatus != null && !bookingStatus.isEmpty()) {
			query.setParameter("bookingStatus", bookingStatus);
		}

		if (memberRegistrationNo != null && !memberRegistrationNo.isEmpty()) {
			query.setParameter("memberRegistrationNo", memberRegistrationNo);
		}

		List<Object[]> rows = getResultList(query, offset, limit);
		for (Object o[] : rows) {
			int i = 0;
			Object value = null;
			String bookingRefId = (value = o[i++]) == null ? null : value
					.toString();
			Date bookingDate = (value = o[i++]) == null ? null : (Date) value;
			String companyName = (value = o[i++]) == null ? null : value
					.toString();
			String contactName = (value = o[i++]) == null ? null : value
					.toString();
			String contactEmail = (value = o[i++]) == null ? null : value
					.toString();
			String contactPhone = (value = o[i++]) == null ? null : value
					.toString();
			String refId = (value = o[i++]) == null ? null : value.toString();
			String memberRefId = (value = o[i++]) == null ? null : value
					.toString();
			String memberNo = (value = o[i++]) == null ? null : value
					.toString();
			String ern = (value = o[i++]) == null ? null : value.toString();
			String title = (value = o[i++]) == null ? null : value.toString();
			String otherNames = (value = o[i++]) == null ? null : value
					.toString();
			String fullName = (value = o[i++]) == null ? null : value
					.toString();
			String phoneNumber = (value = o[i++]) == null ? null : value
					.toString();
			String hotel = (value = o[i++]) == null ? null : value.toString();
			Integer paymentStatus = (value = o[i++]) == null ? null
					: (Integer) value;
			Integer attendance = (value = o[i++]) == null ? null
					: (Integer) value;
			String surname = (value = o[i++]) == null ? null : value.toString();
			String email = (value = o[i++]) == null ? null : value.toString();
			String eventRefId = (value = o[i++]) == null ? null : value
					.toString();
			BigInteger bookingId = (value = o[i++]) == null ? null
					: (BigInteger) value;
			String receiptNo = (value = o[i++]) == null ? null : value
					.toString();
			String lpoNo = (value = o[i++]) == null ? null : value.toString();
			Integer isCredit = (value = o[i++]) == null ? null
					: (Integer) value;
			String clearanceNo = (value = o[i++]) == null ? null : value
					.toString();
			Integer isBookingActive = (value = o[i++]) == null ? null
					: (Integer) value;
			Integer isDelegateActive = (value = o[i++]) == null ? null
					: (Integer) value;
			String invoiceNo = (value = o[i++]) == null ? null : value
					.toString();
			String delegatePaymentStatus = (value = o[i++]) == null ? null
					: value.toString();
			Date lastUpdateDate = (value = o[i++]) == null ? null
					: (Date) value;
			String updatedBy = (value = o[i++]) == null ? null : value
					.toString();

			DelegateDto delegateDto = new DelegateDto();
			delegateDto.setCreatedDate(bookingDate);
			delegateDto.setCompanyName(companyName);
			delegateDto.setContactName(contactName);
			delegateDto.setContactEmail(contactEmail);
			delegateDto.setContactPhoneNumber(contactPhone);
			delegateDto.setBookingRefId(bookingRefId);
			delegateDto.setFullName(fullName);
			delegateDto.setDelegatePhoneNumber(phoneNumber);
			delegateDto.setRefId(refId);
			delegateDto.setMemberRefId(memberRefId);
			delegateDto.setMemberNo(memberNo);
			delegateDto.setTitle(title);
			delegateDto.setOtherNames(otherNames);
			delegateDto.setHotel(hotel);
			delegateDto.setErn(ern);
			delegateDto.setBookingId(bookingId.toString());

			if (isBookingActive == 1) {
				delegateDto.setIsBookingActive(isDelegateActive);
			} else if (isBookingActive == 0) {
				delegateDto.setIsBookingActive(isBookingActive);
			}

			delegateDto.setInvoiceNo(invoiceNo);
			delegateDto.setLastUpdateDate(lastUpdateDate);
			delegateDto.setUpdatedBy(updatedBy);

			if (paymentStatus == 1) {
				delegateDto.setBookingPaymentStatus(PaymentStatus.PAID);
			} else if (paymentStatus == 0) {
				delegateDto.setBookingPaymentStatus(PaymentStatus.NOTPAID);
			} else if (paymentStatus == 2) {
				delegateDto.setBookingPaymentStatus(PaymentStatus.Credit);
			}
			if (delegatePaymentStatus != null) {
				delegateDto.setDelegatePaymentStatus(PaymentStatus
						.valueOf(delegatePaymentStatus));
			}
			if (attendance == 0) {
				delegateDto.setAttendance(AttendanceStatus.ATTENDED);
			}
			if (attendance == 1) {
				delegateDto.setAttendance(AttendanceStatus.NOTATTENDED);
			}
			if (attendance == 2) {
				delegateDto.setAttendance(AttendanceStatus.ENROLLED);
			}
			if (attendance == 3) {
				delegateDto.setAttendance(AttendanceStatus.NOTENROLLED);
			}
			delegateDto.setSurname(surname);
			delegateDto.setEmail(email);
			delegateDto.setEventRefId(eventRefId);
			delegateDto.setReceiptNo(receiptNo);
			delegateDto.setLpoNo(lpoNo);
			delegateDto.setIsCredit(isCredit);
			delegateDto.setClearanceNo(clearanceNo);
			delegateList.add(delegateDto);
		}
		return delegateList;
	}

	public void deleteExistingDelegates(String bookingId) {
		logger.error("Deleting existing delegates for booking id ======><><<>>>>>>>>==="
				+ bookingId);
		Query deleteQuery = getEntityManager().createNativeQuery(
				"delete from delegate " + "where booking_id = ( "
						+ "select id from booking "
						+ "where refId = :bookingRefId)");
		deleteQuery.setParameter("bookingRefId", bookingId);
		deleteQuery.executeUpdate();
		logger.error("Deleting Success ====== ><><<>>>>>>>>===");
	}
}
