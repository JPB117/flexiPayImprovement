package com.icpak.rest.dao;

import java.util.List;

import javax.persistence.Query;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Accommodation;
import com.icpak.rest.models.event.Event;
import com.workpoint.icpak.shared.model.EventSummaryDto;
import com.workpoint.icpak.shared.model.EventType;
import com.workpoint.icpak.shared.model.PaymentStatus;

public class EventsDao extends BaseDao {

	public Event getByEventId(String refId) {
		return getByEventId(refId, true);
	}
	
	public Event getByEventId(String refId, boolean throwExceptionIfNull) {
		
		Event event = getSingleResultOrNull(getEntityManager().createQuery(
				"from Event u where u.refId=:refId").setParameter("refId",
				refId));
		
		if(throwExceptionIfNull && event==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Event", "'"+refId+"'");
		}
		
		return event;
	}

	public void createEvent(Event event) {
		save(event);
	}

	public List<Event> getAllEvents(Integer offSet, Integer limit,EventType type) {
		
		if(type==null)
		return getResultList(getEntityManager().createQuery("from Event where isActive=1 order by name"),
				offSet, limit);
		
		
		return getResultList(getEntityManager().createQuery("from Event where type=:type "
				+ "and isActive=1 order by name").setParameter("type", type),
				offSet, limit);
	}

	public void updateEvent(Event event) {
		createEvent(event);
	}

	public int getEventCount() {
		Number number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery("select count(*) from event where isactive=1"));

		return number.intValue();
	}

	public int getDelegateCount(String eventId) {
		
		Number count = getSingleResultOrNull(getEntityManager().createNativeQuery("select count(d.refId) from"
				+ " delegate d inner join booking b on (d.booking_id=b.id)"
				+ " inner join event e on (e.id=b.event_id) where e.refId=:eventId")
				.setParameter("eventId", eventId));
		
		return count.intValue();
	}

	public Double getTotalEventAmount(String eventId,PaymentStatus paymentStatus) {
		String sql = "select sum(b.amountDue) from booking b "
				+ "inner join event e on (b.event_id=e.id) "
				+ "where e.refId=:eventId";
		
		if(paymentStatus!=null){
			sql = sql+" and paymentStatus=:status";
		}
		
		
		Query query = getEntityManager().createNativeQuery(sql).setParameter("eventId", eventId);
		if(paymentStatus!=null){
			query.setParameter("status", paymentStatus.ordinal());
		}
		
		Number value = getSingleResultOrNull(query);
		if(value==null){
			return 0.0;
		}
		
		
		return value.doubleValue();
	}

	public Accommodation getAccommodation(Event event, String accommodationId) {
		
		return getSingleResultOrNull(getEntityManager()
				.createQuery("FROM Accommodation a where a.refId=:refId")
				.setParameter("refId", accommodationId));
	}

	public List<Accommodation> getAllAccommodations() {
		return getResultList(getEntityManager()
				.createQuery("FROM Accommodation"));
	}
	
	
	public EventSummaryDto getEventsSummary(){
		String sql = "select count(*),dateStatus from "
				+ "(select "
				+ "(case when enddate<current_date then 'CLOSED' else 'OPEN' end) "
				+ "as dateStatus from event) as q1";
		
		List<Object[]> rows =  getEntityManager().createNativeQuery(sql).getResultList();
		EventSummaryDto summary = new EventSummaryDto();
		
		for(Object[] row: rows){
			int i=0;
			Object value=null;
			Integer count = (value=row[i++])==null? null: ((Number)value).intValue();
			String status=(value=row[i++])==null? null: value.toString();
			if(status.equals("OPEN")){
				summary.setOpen(count);
			}else{
				summary.setClosed(count);
			}
		}
		
		return summary;
	
	}

}
