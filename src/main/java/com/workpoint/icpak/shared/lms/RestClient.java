package com.workpoint.icpak.shared.lms;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class RestClient {
	
	Client client;
	
	/**
	 * 
	 */
	public RestClient() {
		client = ClientBuilder.newClient();
	}
	
	/**
	 * Returns JSon response
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public void executeHttpCall(String url, Map<String, String> params) {
		WebTarget target = client.target(url);
		for(String key: params.keySet()){
			target.queryParam(key, params.get(key));
		}
		
		Response a = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		if(a.getStatus()!=200){
//			throw new RuntimeException(a.readEntity(String.class));
		}
		
//		String json = a.readEntity(String.class);
		
//		return json;
	}

	
}