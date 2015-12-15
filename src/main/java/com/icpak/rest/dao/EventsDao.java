package com.icpak.rest.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.cpd.CPD;
import com.icpak.rest.models.event.Accommodation;
import com.icpak.rest.models.event.Event;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.PaymentStatus;
import com.workpoint.icpak.shared.model.events.AttendanceStatus;
import com.workpoint.icpak.shared.model.events.DelegateDto;

public class EventsDao extends BaseDao {
	Logger logger = Logger.getLogger(EventsDao.class);

	public Event getByEventId(String refId) {
		return getByEventId(refId, true);
	}

	public Event getByEventId(String refId, boolean throwExceptionIfNull) {

		Event event = getSingleResultOrNull(
				getEntityManager().createQuery("from Event u where u.refId=:refId").setParameter("refId", refId));

		if (throwExceptionIfNull && event == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Event", "'" + refId + "'");
		}

		return event;
	}

	public void createEvent(Event event) {
		save(event);
	}

	public List<Event> getAllEvents(Integer offSet, Integer limit, EventType type, String searchTerm) {
		List<Event> events = null;

		if (searchTerm == null) {
			searchTerm = "";
		}

		try {
			if (searchTerm.isEmpty()) {
				logger.error("===== Empty search term ======= ");

				if (type == null) {

					events = getResultList(
							getEntityManager().createQuery("from Event where isActive=1 order by endDate DESC"), offSet,
							limit);

				} else {
					events = getResultList(getEntityManager()
							.createQuery("from Event where type=:type " + "and isActive=1 order by name startDate DESC")
							.setParameter("type", type), offSet, limit);
				}

			} else {

				String query = "from Event e where isActive=1 and " + "(e.name like :searchTerm or "
						+ "e.description like :searchTerm or " + "e.venue like :searchTerm or "
						+ "e.categoryName like :searchTerm or " + "e.type like :searchTerm " + ")"
						+ "order by e.startDate DESC";

				logger.error("===== Executing search  ======= ");

				events = getResultList(
						getEntityManager().createQuery(query).setParameter("searchTerm", "%" + searchTerm + "%"),
						offSet, limit);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return events;
	}

	public int getSearchEventCount(String searchTerm) {

		String query = "select count(*) from event where isActive=1 and " + "(name like :searchTerm or "
				+ "description like :searchTerm or " + "venue like :searchTerm or "
				+ "categoryName like :searchTerm or " + "type like :searchTerm " + ")";

		logger.error("===== Executing search  count ======= ");

		Number number = getSingleResultOrNull(
				getEntityManager().createNativeQuery(query).setParameter("searchTerm", "%" + searchTerm + "%"));

		logger.error("===== Search  count value ======= " + number.intValue());
		return number.intValue();
	}

	public void updateEvent(Event event) {
		createEvent(event);
	}

	public int getEventCount() {
		Number number = getSingleResultOrNull(
				getEntityManager().createNativeQuery("select count(*) from event where isactive=1"));

		return number.intValue();
	}

	public int getDelegateCount(String eventId) {

		Number count = getSingleResultOrNull(getEntityManager()
				.createNativeQuery(
						"select count(d.refId) from" + " delegate d inner join booking b on (d.booking_id=b.id)"
								+ " inner join event e on (e.id=b.event_id) where e.refId=:eventId")
				.setParameter("eventId", eventId));

		return count.intValue();
	}

	public Double getTotalEventAmount(String eventId, PaymentStatus paymentStatus) {
		String sql = "select sum(b.amountDue) from booking b " + "inner join event e on (b.event_id=e.id) "
				+ "where e.refId=:eventId";

		if (paymentStatus != null) {
			sql = sql + " and paymentStatus=:status";
		}

		Query query = getEntityManager().createNativeQuery(sql).setParameter("eventId", eventId);
		if (paymentStatus != null) {
			query.setParameter("status", paymentStatus.ordinal());
		}

		Number value = getSingleResultOrNull(query);
		if (value == null) {
			return 0.0;
		}

		return value.doubleValue();
	}

	public Accommodation getAccommodation(Event event, String accommodationId) {

		return getSingleResultOrNull(getEntityManager().createQuery("FROM Accommodation a where a.refId=:refId")
				.setParameter("refId", accommodationId));
	}

	public List<Accommodation> getAllAccommodations() {
		return getResultList(getEntityManager().createQuery("FROM Accommodation"));
	}

	public List<Accommodation> getAllAccommodations(String eventId) {
		Event event = findByRefId(eventId, Event.class);
		return getResultList(
				getEntityManager().createQuery("FROM Accommodation a where a.event=:event order by a.hotel")
						.setParameter("event", event));
	}

	public EventSummaryDto getEventsSummary() {
		String sql = "select count(*),dateStatus from " + "(select "
				+ "(case when enddate<current_date then 'CLOSED' else 'OPEN' end) " + "as dateStatus from event) as q1";

		List<Object[]> rows = getResultList(getEntityManager().createNativeQuery(sql));
		EventSummaryDto summary = new EventSummaryDto();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;
			Integer count = (value = row[i++]) == null ? null : ((Number) value).intValue();
			String status = (value = row[i++]) == null ? null : value.toString();

			if (status.equals("OPEN")) {
				summary.setOpen(count);
			} else {
				summary.setClosed(count);
			}
		}

		return summary;

	}

	public int getAccommodationBookingCount(String accommodationRefId) {
		String sql = "select count(*) from delegate where "
				+ "accommodationid=(select id from accommodation where refId=:accommodationRefId)";

		Query query = getEntityManager().createNativeQuery(sql).setParameter("accommodationRefId", accommodationRefId);
		Number number = getSingleResultOrNull(query);
		return number.intValue();
	}

	public Integer getDelegateUnPaidCount(Long eventId) {
		String sql = "SELECT count(*) FROM icpakdb.delegate d " + "inner join booking b on (d.booking_id = b.id) "
				+ "where paymentStatus='0' and b.event_id=:eventId";
		Query query = getEntityManager().createNativeQuery(sql).setParameter("eventId", eventId);
		Number number = getSingleResultOrNull(query);
		System.err.println(number);
		return number.intValue();
	}

	public Integer getDelegatePaidCount(Long eventId) {
		String sql = "SELECT count(*) FROM icpakdb.delegate d " + "inner join booking b on (d.booking_id = b.id) "
				+ "where paymentStatus='1' and b.event_id=:eventId";
		Query query = getEntityManager().createNativeQuery(sql).setParameter("eventId", eventId);
		Number number = getSingleResultOrNull(query);
		System.err.println(number);
		return number.intValue();
	}

	public List<DelegateDto> getEventDelegates(String eventRefId) {
		String sql = "select " + "d.memberRegistrationNo,d.ern,d.email,b.contact,b.bookingDate,a.hotel,b.paymentStatus,"
				+ "b.`E-Mail`,d.lpoNo,d.isCredit,d.clearanceNo,d.attendance " + "from "
				+ "event e inner join booking b on (e.id=b.event_id) " + "inner join delegate d on (d.booking_id=b.id) "
				+ "left join accommodation a on (d.accommodationId=a.id) " + "where " + "e.refId =:eventRefId";
		Query query = getEntityManager().createNativeQuery(sql).setParameter("eventRefId", eventRefId);

		List<Object[]> rows = getResultList(query);

		List<DelegateDto> delegateDtos = new ArrayList<>();

		for (Object[] row : rows) {
			int i = 0;
			Object value = null;

			String memberNo = (value = row[i++]) == null ? null : value.toString();
			String ern = (value = row[i++]) == null ? null : value.toString();
			String email = (value = row[i++]) == null ? null : value.toString();
			String contact = (value = row[i++]) == null ? null : value.toString();
			Date bookingDate = (value = row[i++]) == null ? null : (Date) value;
			String hotel = (value = row[i++]) == null ? null : value.toString();
			Integer paymentStatus = (value = row[i++]) == null ? null : (Integer) value;
			String contactEmail = (value = row[i++]) == null ? null : value.toString();
			String lpoNo = (value = row[i++]) == null ? null : value.toString();
			Integer isCredit = (value = row[i++]) == null ? null : (Integer) value;
			String clearanceNo = (value = row[i++]) == null ? null : value.toString();
			Integer attendance = (value = row[i++]) == null ? null : (Integer) value;

			DelegateDto delegateDto = new DelegateDto();

			if (memberNo != null) {
				delegateDto.setMemberNo(memberNo);
			} else {
				delegateDto.setMemberNo("Non memeber");
			}

			if (ern != null) {
				delegateDto.setErn(ern);
			} else {
				delegateDto.setErn("Not provided");
			}

			if (email != null) {
				delegateDto.setEmail(email);
			} else {
				delegateDto.setEmail("Not provided");
			}

			if (contact != null) {
				delegateDto.setContact(contact);
			} else {
				delegateDto.setContact("Not provided");
			}

			delegateDto.setCreatedDate(bookingDate);

			if (hotel != null) {
				delegateDto.setHotel(hotel);
			} else {
				delegateDto.setHotel("Not provided");
			}

			if (paymentStatus == 1) {
				delegateDto.setPaymentStatus(PaymentStatus.PAID);
			} else {
				delegateDto.setPaymentStatus(PaymentStatus.PAID);
			}

			if (contactEmail != null) {
				delegateDto.setContactEmail(contactEmail);
			} else {
				delegateDto.setContactEmail("Not provided");
			}

			if (lpoNo != null) {
				delegateDto.setLpoNo(lpoNo);
			} else {
				delegateDto.setLpoNo("Not provided");
			}

			if (clearanceNo != null) {
				delegateDto.setClearanceNo(clearanceNo);
			} else {
				delegateDto.setClearanceNo("Not provided");
			}

			delegateDto.setIsCredit(isCredit);

			if (attendance == 1) {
				delegateDto.setAttendance(AttendanceStatus.ATTENDED);
			} else {
				delegateDto.setAttendance(AttendanceStatus.NOTATTENDED);
			}

			delegateDtos.add(delegateDto);

		}

		return delegateDtos;
	}

	public String createCourseCpd(CPD newCpd) {
		save(newCpd);
		return null;
	}

	public Event getByEventLongId(Long lmsCourseId, boolean throwExceptionIfNull) {
		Event event = getSingleResultOrNull(getEntityManager()
				.createQuery("from Event u where u.lmsCourseId=:lmsCourseId").setParameter("lmsCourseId", lmsCourseId));

		if (throwExceptionIfNull && event == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Event", "'" + lmsCourseId + "'");
		}

		return event;
	}

	public Event getByEventLongId(Long id) {
		return getByEventLongId(id, true);
	}

}
