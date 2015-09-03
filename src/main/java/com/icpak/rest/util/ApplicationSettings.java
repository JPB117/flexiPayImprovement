package com.icpak.rest.util;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.inject.Singleton;

@Singleton
public class ApplicationSettings {

	Properties properties = new Properties();
	private static Logger log = Logger.getLogger(ApplicationSettings.class);
	
	public ApplicationSettings() {
		try {
			properties.load(ApplicationSettings.class.getClassLoader()
					.getResourceAsStream("bootstrap.properties"));
		}catch(Exception e){
			log.error("Load Application Settings Failed cause: "+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String getApplicationPath(){
		return getProperty("application.path");
	}
	
	public String getProperty(String key){
		return properties.getProperty(key);
	}
}
