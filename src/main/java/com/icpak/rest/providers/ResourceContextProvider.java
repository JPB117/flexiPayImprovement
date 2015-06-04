package com.icpak.rest.providers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.icpak.rest.models.base.ResourceCollectionModel;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
public class ResourceContextProvider implements ContextResolver<JAXBContext>{

	private JAXBContext context;
    private Class<?>[] types = {ResourceCollectionModel.class};
    
    public ResourceContextProvider() throws JAXBException {
    	Map<String, Object> props = new HashMap<String, Object>();
        props.put(JSONJAXBContext.JSON_NOTATION, JSONJAXBContext.JSONNotation.MAPPED);
        props.put(JSONJAXBContext.JSON_ROOT_UNWRAPPING, Boolean.TRUE);
        //props.put(JSONJAXBContext.JSON_ARRAYS, new HashSet<String>(){});
        this.context = new JSONJAXBContext(types, props);
	}
    
	@Override
	public JAXBContext getContext(Class<?> type) {
		
		return (types[0].equals(type)) ? context : null;
	}
	
}
