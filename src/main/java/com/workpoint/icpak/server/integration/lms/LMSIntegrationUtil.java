package com.workpoint.icpak.server.integration.lms;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.ApplicationType;

/**
 * 25/08/2015
 * 
 * @author duggan
 *
 */
public class LMSIntegrationUtil {

	static Logger logger = Logger.getLogger(LMSIntegrationUtil.class);
	String baseUri = null;

	/**
	 * Jersey Client
	 */
	private Client jclient;
	private static LMSIntegrationUtil util = null;

	public static LMSIntegrationUtil getInstance() throws IOException {
		if (util == null) {
			synchronized (logger) {
				if (util == null) {
					util = new LMSIntegrationUtil();
					logger.error(">>>>>>Util Class:"
							+ util.getResourceUri("/registter"));
				}
			}
		}
		return util;
	}

	private LMSIntegrationUtil() throws IOException {
		DefaultClientConfig config = new DefaultClientConfig();
		// config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
		// Boolean.TRUE);
		jclient = Client.create(config);
		jclient.setConnectTimeout(15000);

		Properties props = new Properties();
		props.load(LMSIntegrationUtil.class.getClassLoader()
				.getResourceAsStream("bootstrap.properties"));
		baseUri = props.getProperty("lms_base_path");
		assert baseUri != null && !baseUri.isEmpty();
	}

	/**
	 * 
	 * @param resourcePath
	 *            e.g /users. The path to the resource after the base path
	 * @param payLoad
	 * @return
	 */
	public <T> T executeCall(String resourcePath, Object payLoad,
			final Class<T> returnTypeClazz) {

		if (resourcePath == null || resourcePath.isEmpty()) {
			throw new IllegalArgumentException(
					"ResourcePath cannot be null for rest service");
		}

		logger.info("Submitting payload to " + getResourceUri(resourcePath)
				+ " ;payLoad " + payLoad);

		WebResource resource = jclient.resource(getResourceUri(resourcePath));

		ClientResponse clientResponse = null;

		try {
			clientResponse = resource.type(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, payLoad);

			// response.getHeaders()
		} catch (Exception e) {
			// server unavailable
			throw new RuntimeException(e);
		}

		T response = null;

		if (clientResponse.getClientResponseStatus() != ClientResponse.Status.OK) {
			throw new RuntimeException(clientResponse.getEntity(String.class));
			// response = (T) clientResponse.getEntity(String.class);
		} else {
			response = clientResponse.getEntity(returnTypeClazz);
		}
		return response;

	}

	public <T> LMSResponse executeLMSCall(String resourcePath, Object payLoad,
			final Class<T> returnTypeClazz) throws IOException {

		Client client = Client.create();

		if (resourcePath == null || resourcePath.isEmpty()) {
			throw new IllegalArgumentException(
					"ResourcePath cannot be null for rest service");
		}

		logger.info("Submitting payload to " + getResourceUri(resourcePath)
				+ " ;payLoad " + payLoad.toString());
		JSONObject json = new JSONObject(payLoad);
		System.err.println(">>>>>>>>" + json);

		WebResource resource = client.resource(getResourceUri(resourcePath));

		ClientResponse clientResponse = resource
				.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, payLoad);

		LMSResponse response = new LMSResponse();
		if (clientResponse.getClientResponseStatus() != ClientResponse.Status.OK) {
			response.setMessage(clientResponse.getEntity(String.class));
			response.setStatus("Failed");
			logger.error(response);
		} else if (clientResponse.getClientResponseStatus() == ClientResponse.Status.OK
				&& (clientResponse.getEntity(String.class)).isEmpty()) {
			response.setMessage("No Response from LMS...");
			response.setStatus("Failed");
			logger.error(response);
		} else {
			response.setMessage(clientResponse.getEntity(String.class));
			response.setStatus("Success");
		}
		return response;
	}

	private String getResourceUri(String resourcePath) {
		if (baseUri == null) {
			logger.error(">>> base uri cannot be null");
		}
		return baseUri + (resourcePath.startsWith("/") ? "" : "/")
				+ resourcePath;
	}

	public static void main(String[] args) throws IOException {
		ApplicationFormHeaderDto header = new ApplicationFormHeaderDto();
		header.setSurname("Macharia");
		header.setOtherNames("Duggan");
		header.setEmail("mdkimani@gmail6.com");
		header.setApplicationType(ApplicationType.ASSOCIATE);
		header.setAddress1("p.o Box 37425 Nrb");
		header.setPostCode("00100");
		header.setCity1("Nairobi");
		header.setEmployer("Workpoint Limited");
		header.setApplicationType(ApplicationType.NON_PRACTISING);
		ApplicationFormHeaderDto saved = LMSIntegrationUtil.getInstance()
				.executeCall("/applications", header,
						ApplicationFormHeaderDto.class);
		assert saved.getRefId() != null;

		System.err.println(saved.getRefId());
	}
}
