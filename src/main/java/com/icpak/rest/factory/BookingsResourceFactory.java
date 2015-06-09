package com.icpak.rest.factory;

import com.icpak.rest.BookingsResourceImpl;

public interface BookingsResourceFactory {

	public BookingsResourceImpl create(String eventId);
}
