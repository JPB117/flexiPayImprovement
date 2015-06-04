package com.icpak.servlet.config;

/**
 * Generic constants for Bootstrapping the WebApp
 * 
 * @author pablo.biagioli
 *
 */
public class GenericBootstrapConstants {

	/**
	 * "com.sun.jersey.config.property.packages", packages separated by commas
	 */
	//public static final String JERSEY_PROPERTY_PACKAGES="com.icpak.rest;com.wordnik.swagger.jersey.listing";
	public static final String JERSEY_PROPERTY_PACKAGES="com.icpak.rest;com.wordnik.swagger.jaxrs.listing";
	
	/**
	 * main properties file that gets loaded at first
	 */
	public static final String BOOTSTRAP_PROPERTIES_FILE="bootstrap.properties";
}
