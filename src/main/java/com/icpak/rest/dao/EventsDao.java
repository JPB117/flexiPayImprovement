package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Event;

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

	public List<Event> getAllEvents(Integer offSet, Integer limit) {
		return getResultList(getEntityManager().createQuery("from Event where isActive=1 order by name"),
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

}
