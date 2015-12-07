package com.workpoint.icpak.server.integration.lms;

import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

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
					logger.error(">>>>>>Util Class:" + util.getResourceUri("/registter"));
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
		props.load(LMSIntegrationUtil.class.getClassLoader().getResourceAsStream("bootstrap.properties"));
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
	public <T> T executeCall(String resourcePath, Object payLoad, final Class<T> returnTypeClazz) {

		if (resourcePath == null || resourcePath.isEmpty()) {
			throw new IllegalArgumentException("ResourcePath cannot be null for rest service");
		}

		logger.info("Submitting payload to " + getResourceUri(resourcePath) + " ;payLoad " + payLoad);

		WebResource resource = jclient.resource(getResourceUri(resourcePath));

		ClientResponse clientResponse = null;

		try {
			clientResponse = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
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

	public <T> LMSResponse executeLMSCall(String resourcePath, Object payLoad, final Class<T> returnTypeClazz)
			throws IOException {

		final HttpClient httpClient = new DefaultHttpClient();
		logger.error("=== Payload =====" + resourcePath);
		HttpPost request = new HttpPost("http://www2.icpak.com:8082/api/Course/EnrollCourse");
		request.setHeader("accept", "application/json");
		@SuppressWarnings("deprecation")
		StringEntity stringEntity = new StringEntity(payLoad.toString(), "application/json", "UTF-8");
		request.setEntity(stringEntity);

		HttpResponse httpResponse = null;

		String responseString = null;

		int rsponseCode = 0;

		try {

			httpResponse = httpClient.execute(request);

			rsponseCode = httpResponse.getStatusLine().getStatusCode();

			HttpEntity entity = httpResponse.getEntity();

			/**
			 * Get the response String
			 */
			responseString = EntityUtils.toString(entity, "UTF-8");

			logger.info(" >>>>>>>>>>>> httpResponseStatus <<<<<<<<< " + responseString + "  == Code " + rsponseCode);

		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("Submitting payload to " + getResourceUri(resourcePath) + " ;payLoad " + payLoad);
		System.err.println(">>>>>>>>" + payLoad);

		LMSResponse response = new LMSResponse();
		response.setPayload(payLoad.toString());

		if (rsponseCode != 200) {
			response.setMessage(responseString);
			response.setStatus("Failed");
			logger.error(response);
		} else if (responseString.isEmpty()) {
			response.setMessage("No Response from LMS...");
			response.setStatus("Failed");
			logger.error(response);
		} else {
			response.setMessage(responseString);
			response.setStatus("Success");
		}
		return response;
	}

	private String getResourceUri(String resourcePath) {
		if (baseUri == null) {
			logger.error(">>> base uri cannot be null");
		}
		return baseUri + (resourcePath.startsWith("/") ? "" : "/") + resourcePath;
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
		ApplicationFormHeaderDto saved = LMSIntegrationUtil.getInstance().executeCall("/applications", header,
				ApplicationFormHeaderDto.class);
		assert saved.getRefId() != null;

		System.err.println(saved.getRefId());
	}
}
