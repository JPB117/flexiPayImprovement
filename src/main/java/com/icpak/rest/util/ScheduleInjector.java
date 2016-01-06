package com.icpak.rest.util;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.icpak.rest.dao.helper.StatementDaoHelper;

public class ScheduleInjector {

	@Inject
	private static Provider<StatementDaoHelper> helper;
	
	public static StatementDaoHelper getStatementDaoHelper(){
		System.out.println("Retrieving helper >>> "+helper);
		return helper.get();
	}
}
