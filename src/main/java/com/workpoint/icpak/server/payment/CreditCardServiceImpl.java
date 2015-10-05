package com.workpoint.icpak.server.payment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;

/**
 *
 * Created by achachiez on 02/03/15.
 *
 */
public class CreditCardServiceImpl implements CreditCardService {
	Logger log = LoggerFactory.getLogger(CreditCardServiceImpl.class);

	@Override
	public CreditCardDto setInitials(CreditCardDto creditCardDto) {
		CreditCardDto creditCardDetails1 = new CreditCardDto();

		creditCardDetails1.setAccount_number(creditCardDto.getAccount_number());
		creditCardDetails1.setAddress1(creditCardDto.getAddress1());
		creditCardDetails1.setAddress2(creditCardDto.getAddress2());
		creditCardDetails1.setAmount(creditCardDto.getPostedAmount()
				.toPlainString());
		creditCardDetails1.setCard_holder_name(creditCardDto
				.getCard_holder_name());
		creditCardDetails1.setAccount_number(creditCardDto.getAccount_number());
		creditCardDetails1.setEmail(creditCardDto.getEmail());
		creditCardDetails1.setCountry("KE");
		creditCardDetails1.setState("Nairobi");
		creditCardDetails1.setSecurity_code(creditCardDto.getSecurity_code());
		creditCardDetails1.setZip(creditCardDto.getZip());
		creditCardDetails1.setCurrency("KES");
		creditCardDetails1.setMobile_number(creditCardDto.getMobile_number());
		creditCardDetails1.setCard_number(creditCardDto.getCard_number());
		creditCardDetails1.setExpiry(creditCardDto.getExpiry());

		return creditCardDetails1;
	}

//	@Override
//	public void RequestPayment(CreditCardDto cardDetails) throws JSONException,
//			CardAuthorizationException {
//		CreditCardResponse data = authorizeCardTransaction(cardDetails);
//		System.out.println("Result==========>" + data);
//		JSONObject content = data.getJSONObject("content");
//		JSONObject status = data.getJSONObject("status");
//		String transaction_status = status.getString("status");
//		if (Objects.equals(transaction_status, "SUCCESS")) {
//			String transaction_index = content.getString("transaction_index");
//			String transaction_reference = content
//					.getString("transaction_reference");
//			completeCardTransaction(transaction_reference, transaction_index);
//		} else {
//			throw new CardAuthorizationException("Card Authorization Failed");
//		}
//	}

	public CreditCardResponse authorizeCardTransaction(CreditCardDto cardDetails) {

		log.warn("================= Authorizing card payment");

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				Utilities.LIPISHA_AUTHORIZE_CARD_TRANSACTION_FUNCTION_URL);
		String res = "";
		HttpResponse response = null;
		List<NameValuePair> urlParameters = new ArrayList<>();
		urlParameters.add(new BasicNameValuePair("api_key",
				"6f55b237d1e078f5ed60eb85f365908b"));
		urlParameters
				.add(new BasicNameValuePair(
						"api_signature",
						"Pq+zdEV0UuAVZ1pFYYU4HeH15eFbM7hLsS2VEg8Bjyz/31NoYEmKDR4zFtN3LaSx1Sl4bgaliv1e42p6J/FtmwjkgRng07u/CpTd1whxwPfmeoswkkPsV1luSINCUkmW8dfPnp2gtBcIrA25Y85TsAR5jQx3HpjIBb7R82d1FcM="));
		urlParameters.add(new BasicNameValuePair("api_version",
				Utilities.LIPISHA_API_VERSION));
		urlParameters.add(new BasicNameValuePair("api_type",
				Utilities.LIPISHA_API_TYPE));
		urlParameters.add(new BasicNameValuePair("account_number", "03369"));
		urlParameters.add(new BasicNameValuePair("card_number", cardDetails
				.getCard_number()));
		urlParameters.add(new BasicNameValuePair("address1", cardDetails
				.getAddress1()));
		urlParameters.add(new BasicNameValuePair("address2", cardDetails
				.getAddress2()));
		urlParameters.add(new BasicNameValuePair("expiry", cardDetails
				.getExpiry()));
		urlParameters.add(new BasicNameValuePair("name", cardDetails
				.getCard_holder_name()));
		urlParameters.add(new BasicNameValuePair("country", cardDetails
				.getCountry()));
		urlParameters.add(new BasicNameValuePair("email", cardDetails
				.getEmail()));
		urlParameters.add(new BasicNameValuePair("mobile_number", cardDetails
				.getMobile_number()));
		urlParameters.add(new BasicNameValuePair("state", cardDetails
				.getState()));
		urlParameters.add(new BasicNameValuePair("zip", cardDetails.getZip()));
		urlParameters.add(new BasicNameValuePair("security_code", cardDetails
				.getSecurity_code()));
		urlParameters.add(new BasicNameValuePair("amount", cardDetails
				.getAmount().toString()));
		urlParameters.add(new BasicNameValuePair("currency", cardDetails
				.getCurrency()));
		StringBuffer result = null;
		try {
			log.warn("Payment Params>> "+cardDetails);
			StringEntity e;
			System.out.println("Payment Params>> "+cardDetails);
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(urlParameters);
			post.setEntity(entity);
			response = client.execute(post);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert result != null;
		res = result.toString();
//		System.out.println("Lipisha ====================================="
//				+ res);
		log.info("gateway feedback authorize " + res);
		// writeToFile(res);
		JSONObject jObject = null;
		CreditCardResponse creditCardResp = new CreditCardResponse();
		try {
			jObject = new JSONObject(res);
			JSONObject content = (JSONObject) jObject.getJSONObject("content");

			if(content!=null){
				creditCardResp.setInvalidParams(content.getString("invalid_parameters"));
			}

			JSONObject status= (JSONObject) jObject.getJSONObject("status");
			if(status!=null){
				creditCardResp.setStatusCode(status.getInt("status_code")+"");
				creditCardResp.setStatusDesc(status.getString("status_description"));
				creditCardResp.setStatus(status.getString("status"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RuntimeException();

		}
		return creditCardResp;
	}

	public JSONObject reverseAuthorizedFund() {
		JSONObject jsonExceptionReturns = new JSONObject();
		String res = null;
		String url = Utilities.LIPISHA_REVERSE_CARD_AUTHORIZATION_FUNCTION_URL;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("api_key", System
				.getProperty("lipisha_api_key")));
		urlParameters.add(new BasicNameValuePair("api_signature", System
				.getProperty("lipisha_api_signature")));
		urlParameters.add(new BasicNameValuePair("api_version",
				Utilities.LIPISHA_API_VERSION));
		urlParameters.add(new BasicNameValuePair("api_type",
				Utilities.LIPISHA_API_TYPE));
		// urlParameters.add(new BasicNameValuePair("transaction_index",
		// removeLeadingTrailing(getIndex())));
		// urlParameters.add(new BasicNameValuePair("transaction_reference",
		// Long.toString(getReference())));
		// log.info("Now in reverse card transaction ");
		// Url Encoding the POST parameters
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException uee) {
			log.error("URL encoding exception reverse card transaction "
					+ uee.getStackTrace());
		}
		// Making HTTP Request
		try {
			response = client.execute(post);
		} catch (IOException ioe) {
			// log.severe("IOException here reverse card " +
			// HarambesaUtils.getStackTrace(ioe));
		} catch (Exception ex) {
			log.warn("General Exception on http Request reverse card  "
					+ ex.getStackTrace());
			// jsonExceptionReturns.put("success",0);
			// jsonExceptionReturns.put("message","Unknown error. Please try again");
			return jsonExceptionReturns;
		}
		StringBuffer result = null;
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception ex) {
			// log.severe("General Exception on buffering: complete card " +
			// HarambesaUtils.getStackTrace(ex));
			// jsonExceptionReturns.put("success",0);
			// jsonExceptionReturns.put("message","Unknown error. Please try again");
		}

		res = result.toString();
		JSONObject jObject = null;
		try {
			jObject = new JSONObject(res);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	public JSONObject completeCardTransaction(String transaction_reference,
			String transaction_index) {
		JSONObject jsonExceptionReturns = new JSONObject();
		// log.info("Now in complete card transaction ");
		String res = null;
		String url = Utilities.LIPISHA_COMPLETE_CARD_TRANSACTION_FUNCTION_URL;
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpResponse response = null;
		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("api_key",
				"c727ee27a6dfca41c7c9dee5d21ba73a"));
		urlParameters
				.add(new BasicNameValuePair(
						"api_signature",
						"Q86FIhxIj+t7LPiC7m2QAoAieoJWLjwj/OocUnf+B7Fy8nAfDlbsc8X8JzI6Wj0Dy15V/tYr6Y8EidUPy9vJbg78pKeqEWZT5qXZboo9HX+nOpCARjYFC+J0pLi4Q06gZdW0xq+DyRFPKoXV6iA9mSWcy9ehgNvff4bQ08Ql944="));
		urlParameters.add(new BasicNameValuePair("api_version",
				Utilities.LIPISHA_API_VERSION));
		urlParameters.add(new BasicNameValuePair("api_type",
				Utilities.LIPISHA_API_TYPE));
		urlParameters.add(new BasicNameValuePair("transaction_index",
				transaction_index));
		urlParameters.add(new BasicNameValuePair("transaction_reference",
				transaction_reference));

		// log.info("transaction_reference=============== " +
		// removeLeadingTrailing(getIndex()));
		// Url Encoding the POST parameters
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException uee) {
			// log.severe("URL encoding exception complete card " +
			// HarambesaUtils.getStackTrace(uee));
		}

		// Making HTTP Request
		try {
			response = client.execute(post);
		} catch (IOException ioe) {
			// log.severe("IOException here complete card " +
			// HarambesaUtils.getStackTrace(ioe));
		} catch (Exception ex) {
			// .severe("General Exception on http Request complete card  " +
			// HarambesaUtils.getStackTrace(ex));
			try {
				jsonExceptionReturns.put("success", 0);
				jsonExceptionReturns.put("message",
						"Unknown error. Please try again");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return jsonExceptionReturns;
		}
		StringBuffer result = null;
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
		} catch (Exception ex) {
			// log.severe("General Exception on buffering: complete card " +
			// HarambesaUtils.getStackTrace(ex));
			try {
				jsonExceptionReturns.put("success", 0);
				jsonExceptionReturns.put("message",
						"Unknown error. Please try again");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonExceptionReturns;
	}
}
