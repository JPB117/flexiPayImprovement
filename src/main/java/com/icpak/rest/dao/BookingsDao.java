package com.icpak.rest.dao;

import java.util.List;

import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.event.Booking;

public class BookingsDao extends BaseDao {

	public Booking getByBookingId(String refId) {
		
		Booking booking = getSingleResultOrNull(getEntityManager().createQuery(
				"from Booking u where u.refId=:refId").setParameter("refId",
				refId));
		if(booking==null){
			throw new ServiceException(ErrorCodes.NOTFOUND, "Booking", "'"+refId+"'");
		}
		return booking;
	}

	public void createBooking(Booking booking) {
		save(booking);
	}

	public List<Booking> getAllBookings(Integer offSet, Integer limit) {
		return getResultList(getEntityManager().createQuery("from Booking where isActive=1 order by created"),
				offSet, limit);
	}

	public void updateBooking(Booking booking) {
		createBooking(booking);
	}

	public int getBookingCount() {
		Number number = getSingleResultOrNull(getEntityManager()
				.createNativeQuery("select count(*) from booking where isactive=1"));

		return number.intValue();
	}

	public boolean isPaymentValid(String paymentRef) {
		return false;
	}

}
