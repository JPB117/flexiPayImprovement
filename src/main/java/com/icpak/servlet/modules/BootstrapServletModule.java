package com.icpak.servlet.modules;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.guice.web.GuiceShiroFilter;

import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.ServletModule;
import com.icpak.rest.BookingsResourceImpl;
import com.icpak.rest.EducationResourceImpl;
import com.icpak.rest.factory.ResourceFactory;
import com.icpak.rest.filters.CORSFilter;
import com.icpak.servlet.config.GenericBootstrapConstants;
import com.icpak.servlet.swagger.SwaggerApiServlet;
import com.icpak.servlet.upload.GetReport;
import com.icpak.servlet.upload.UploadServlet;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.workpoint.icpak.shared.api.BookingsResource;
import com.workpoint.icpak.shared.api.EducationResource;

/**
 * This class bootstraps the application Servlet (Jersey 1.18.1). 
 * @author pablo.biagioli
 *
 */
public class BootstrapServletModule extends ServletModule{

	private static final String propertyPackages= GenericBootstrapConstants.JERSEY_PROPERTY_PACKAGES;
	
	@Override
	protected void configureServlets() {
		super.configureServlets();
		
		//get the bootstrapping Properties file
		install(new BootstrapPropertiesModule());

		//Install subresource factory
		install(new FactoryModuleBuilder()
		.build(ResourceFactory.class));
		
		
		//Initialize Persistence JPA Unit of Work if present
		//install(new MyUnitOfWorkModule());
		install(new JpaPersistModule("icpak_pu"));
        //if you had a Persistence Service like JPA Unit of Work you would need to add this PersistFilter also.
        filter("/*").through(PersistFilter.class);
		
		//Initialize Apache Shiro if present
		install(new ICPAKShiroWebModule(getServletContext()));
		
        //if you had a ShiroWebModule installed above you would need to add this GuiceShiroFilter also.
        filter("/*").through(GuiceShiroFilter.class);
        //serve("/services/*").with(GuiceContainer.class, params);
        Map<String, String> params = new HashMap<String, String>();
        params.put(PackagesResourceConfig.PROPERTY_PACKAGES, propertyPackages);
        params.put("javax.ws.rs.container.ContainerRequestFilter", "com.icpak.rest.ResourceRequestFilter");
        params.put("com.sun.jersey.api.json.POJOMappingFeature","true");
        serve("/api/*").with(GuiceContainer.class, params);
        filter("/api/*").through(CORSFilter.class);
        
//        Map<String, String> params2 = new HashMap<String, String>();
//        params2.put("load-on-startup", "1");
        
        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("loadonstartup", "1");
        serve("/upload/*").with(UploadServlet.class, params2);
        serve("/getreport/*").with(GetReport.class, params2);
        
        
        serve("").with(SwaggerApiServlet.class,params2);
        
	}
}
