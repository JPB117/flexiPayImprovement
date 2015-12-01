package com.workpoint.icpak.client;

import org.apache.log4j.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class ClientExemptionHandler implements EntryPoint{
	Logger logger = Logger.getLogger(ClientExemptionHandler.class);

	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			
			@Override
			public void onUncaughtException(Throwable e) {
				logger.debug(logger);
				
			}
		});
		
		DeferredCommand.addCommand(new Command() {
		      public void execute() {
		        onModuleLoad2();
		      }
		    });
	}
	
	 private void onModuleLoad2() {
		 
		  }

}
