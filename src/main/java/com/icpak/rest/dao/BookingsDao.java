package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Booking;
import com.icpak.rest.models.event.Delegate;
import com.workpoint.icpak.shared.model.EventStatus;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AccommodationDto;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.shared.model.events.DelegateType;
import com.workpoint.icpak.shared.model.events.MemberBookingDto;

public class BookingsDao extends BaseDao {
	Logger logger = Logger.getLogger(BookingsDao.class);

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

	public void createBooking(Booking booking) {
		save(booking);
	}

	public List<Booking> getAllBookings(String eventId, Integer offSet,
			Integer limit, String searchTerm) {

		if (searchTerm.isEmpty()) {
			return getResultList(
					getEntityManager().createQuery(
							"from Booking where isActive=1 order by created"),
					offSet, limit);
		}

		String query = "select count(*) from Booking b where b.isActive=1 "
				+ "and b.event.refId=:refId and"
				+ "(e.name like :searchTerm or "
				+ "e.description like :searchTerm or "
				+ "e.venue like :searchTerm or "
				+ "e.categoryName like :searchTerm or "
				+ "e.type like :searchTerm " + ")" + "order by e.name";

		return getResultList(getEntityManager().createQuery(query)
				.setParameter("searchTerm", searchTerm), offSet, limit);
	}

	public List<Booking> getAllBookings(String eventId, Integer offSet,
			Integer limit) {
		return getResultList(getEntityManager().createQuery(
				"from Booking b where b.isActive=1 "
						+ "and b.event.refId=:refId order by created")
				.setParameter("refId", eventId), offSet, limit);
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
		logger.error("Deleteing invoice line ====== ><><<>>>>>>>>===");
		Query deleteQuery = getEntityManager().createNativeQuery(
				"delete from InvoiceLine " + "where invoiceId in ( "
						+ "select d.id from Invoice d "
						+ "where d.bookingRefId = :bookingRefId)");
		deleteQuery.setParameter("bookingRefId", bookingRefId);
		deleteQuery.executeUpdate();

		logger.error("Deleteing invoice ====== ><><<>>>>>>>>===");
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
				+ "d.attendance," + "b.paymentStatus, "
				+ "t.status trxStatus, " + "a.hotel " + "from event e "
				+ "inner join booking b on (e.id=b.event_Id)  "
				+ "inner join delegate d on (d.booking_id=b.id) "
				+ "left join accommodation a on (a.id=d.accommodationid) "
				+ "left join invoice i on (i.bookingRefId=b.refId) "
				+ "left join transaction t on (t.invoiceRef=i.refId) "
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
					: ((Number) value).intValue();// Should
													// remove
													// this
													// field
			String trxStatus = (value = row[i++]) == null ? null : value
					.toString();// From
								// Transactions
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
			// dto.setPaymentStatus(PaymentStatus.PAID.ordinal()==paymentStatus?
			// PaymentStatus.PAID: PaymentStatus.NOTPAID);
			dto.setPaymentStatus(trxStatus == null ? PaymentStatus.NOTPAID
					: PaymentStatus.valueOf(trxStatus));

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
		return getDelegateCount(eventId, null);
	}

	public int getDelegateCount(String eventId, String searchTerm) {

		Number number = null;

		if (eventId != null && searchTerm != null && !searchTerm.isEmpty()) {
			String sql = "select count(*) "
					+ "from delegate d inner join booking b on (d.booking_id=b.id) "
					+ "inner join event e on (b.event_id=e.id) "
					+ "left join accommodation a on (a.eventId=e.id) "
					+ "where e.refId=:eventRefId";

			sql = sql.concat(" and "
					+ "d.memberRegistrationNo like :searchTerm or "
					+ "a.hotel like :searchTerm or "
					+ "d.title like :searchTerm or "
					+ "d.email like :searchTerm or "
					+ "d.otherNames like :searchTerm or "
					+ "d.ern like :searchTerm");

			Query query = getEntityManager().createNativeQuery(sql)
					.setParameter("eventRefId", eventId);
			if (searchTerm != null && !searchTerm.isEmpty()) {
				query.setParameter("searchTerm", "%" + searchTerm + "%");
			}

			number = getSingleResultOrNull(query);

		} else {

			number = getSingleResultOrNull(getEntityManager().createQuery(
					"select count(*) from  Delegate where isactive=1"));
		}

		return number.intValue();
	}

	public List<DelegateDto> getAllDelegates(String passedRefId,
			Integer offset, Integer limit, String searchTerm) {

		List<DelegateDto> delegateList = new ArrayList<>();

		String sql = "select d.refId,d.memberRegistrationNo,d.ern,"
				+ "d.title,d.otherNames,a.hotel,b.paymentStatus,"
				+ "d.attendance,d.surname,d.email,e.refid "
				+ "from delegate d inner join booking b on (d.booking_id=b.id) "
				+ "inner join event e on (b.event_id=e.id) "
				+ "left join accommodation a on (a.eventId=e.id) "
				+ "where e.refId=:eventRefId";

		if (searchTerm != null && !searchTerm.isEmpty()) {
			sql = sql.concat(" and "
					+ "d.memberRegistrationNo like :searchTerm or "
					+ "a.hotel like :searchTerm or "
					+ "d.title like :searchTerm or "
					+ "d.email like :searchTerm or "
					+ "d.otherNames like :searchTerm or "
					+ "d.ern like :searchTerm");
		}

		Query query = getEntityManager().createNativeQuery(sql).setParameter(
				"eventRefId", passedRefId);

		if (searchTerm != null && !searchTerm.isEmpty()) {
			query.setParameter("searchTerm", "%" + searchTerm + "%");
		}

		System.err.println(sql);

		List<Object[]> rows = getResultList(query, offset, limit);
		for (Object o[] : rows) {
			int i = 0;
			Object value = null;

			String memberRefId = (value = o[i++]) == null ? null : value
					.toString();
			String memberNo = (value = o[i++]) == null ? null : value
					.toString();
			String ern = (value = o[i++]) == null ? null : value.toString();
			String title = (value = o[i++]) == null ? null : value.toString();
			String otherNames = (value = o[i++]) == null ? null : value
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

			DelegateDto delegateDto = new DelegateDto();
			delegateDto.setMemberRefId(memberRefId);
			delegateDto.setMemberNo(memberNo);
			delegateDto.setTitle(title);
			delegateDto.setOtherNames(otherNames);
			delegateDto.setHotel(hotel);
			delegateDto.setErn(ern);

			if (paymentStatus == 1) {
				delegateDto.setPaymentStatus(PaymentStatus.PAID);
			} else {
				delegateDto.setPaymentStatus(PaymentStatus.NOTPAID);
			}
			if (attendance == 1) {
				delegateDto.setAttendance(AttendanceStatus.ATTENDED);
			} else {
				delegateDto.setAttendance(AttendanceStatus.NOTATTENDED);
			}
			delegateDto.setSurname(surname);
			delegateDto.setEmail(email);
			delegateDto.setEventRefId(eventRefId);

			delegateList.add(delegateDto);

		}

		return delegateList;
	}
}
