package com.icpak.rest.util;

import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.icpak.rest.dao.SMSDao;
import com.icpak.rest.models.sms.SMSLog;
import com.icpak.rest.models.sms.SmsStatus;

@Singleton
public class SMSIntegration {

	Random random = new Random();
	@Inject
	SMSDao smsDao;

	Logger logger = Logger.getLogger(SMSIntegration.class);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public String execute(Map<String, String> values) {
		String to = values.get("to");
		String message = values.get("message");

		if (to == null || to.isEmpty()) {
			return "Failed: The recipient telephone number must be provided. Kindly confirm the relavant fields are provided";
		}

		if (message == null || message.isEmpty()) {
			return "Failed: SMS Message cannot be empty";
		}

		String pin = random.nextInt(10000) + "";
		message = message.replaceAll("\\{AUTHPIN\\}", pin);

		String subject = values.get("subject");
		// SMS sms = new SMS(subject, to, pin);
		// smsDao.save(sms);

		return send(to, message);
	}

	public String send(String to, String message) {

		String username = null;
		String apiKey = null;
		String from = null;
		String smsCost = null;
		boolean isSmsSendingActive = false;
		try {
			Properties props = new Properties();
			props.load(SMSIntegration.class.getClassLoader().getResourceAsStream("bootstrap.properties"));
			username = props.getProperty("africastalking.username");
			apiKey = props.getProperty("africastalking.apiKey");
			from = props.getProperty("africastalking.from");
			isSmsSendingActive = (props.getProperty("isSendingActive").equals("true") ? true : false);
			smsCost = props.getProperty("smsCost");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (!isSmsSendingActive) {
			logger.debug("Failed: SMS Sending is disabled in settings");
			return "Failed: SMS Sending is disabled in settings";
		}

		AfricasTalkingGateway gateway = new AfricasTalkingGateway(username, apiKey);
		try {
			if (to == null || to.isEmpty()) {
				logger.debug(
						"Failed: The recipient telephone number must be provided. Kindly confirm the relavant fields are provided");
				return "Failed: The recipient telephone number must be provided. Kindly confirm the relavant fields are provided";
			}

			if (message == null || message.isEmpty()) {
				logger.debug("Failed: SMS Message cannot be empty");
				return "Failed: SMS Message cannot be empty";
			}

			to = to.replaceAll("[-+.^:]", "");
			to = to.trim();
			logger.info("FinalPhone Number:" + to);

			JSONArray resp = gateway.sendMessage(to, message, from, 1);

			JSONObject object = resp.getJSONObject(0);
			String status = object.getString("status");
			String number = object.getString("number");
			String messageId = object.getString("messageId");
			String cost = object.getString("cost");

			SMSLog sms = new SMSLog();
			sms.setTstamp(new Date());
			sms.setText(message);
			sms.setFrom(from);
			sms.setCost(Double.valueOf(smsCost) * Double.valueOf(cost.substring(4)));
			sms.setSmsId(messageId);
			sms.setTo(number);
			sms.setStatus(SmsStatus.valueOf(status.toUpperCase()));
			smsDao.save(sms);

			if (!status.equals("Success")) {
				System.err.println("SMS Failed: " + status);

			}
			System.err.println(resp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return null;

	}
}
