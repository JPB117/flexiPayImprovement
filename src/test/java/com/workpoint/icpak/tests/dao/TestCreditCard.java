package com.workpoint.icpak.tests.dao;

import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.workpoint.icpak.server.payment.CreditCardServiceImpl;
import com.workpoint.icpak.shared.model.CreditCardDto;
import com.workpoint.icpak.shared.model.CreditCardResponse;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

/**
 * Created by wladek on 9/17/15.
 */
public class TestCreditCard extends AbstractDaoTest {

	@Inject
	CreditCardServiceImpl creditCardService;
	@Inject
	TransactionDaoHelper trxDaoHelper;

	@Test
	public void testCreditCard() throws Exception {
		CreditCardDto creditCardDetails = new CreditCardDto();
		creditCardDetails.setAddress1("Nairobi");
		creditCardDetails.setCard_holder_name("Tom K Muriranja");
		creditCardDetails.setAmount("10");
		creditCardDetails.setCurrency("KES");
		creditCardDetails.setCountry("KE");
		creditCardDetails.setCard_number("");
		creditCardDetails.setExpiry("012018");
		creditCardDetails.setSecurity_code("773");
		creditCardDetails.setZip("0100");
		creditCardDetails.setAddress2("");
		creditCardDetails.setState("Nairobi");

		CreditCardResponse response = creditCardService.authorizeCardTransaction(creditCardDetails);
		System.err.println("Status Code:" + response.getStatusCode() + ">>Status Desc:" + response.getStatusDesc());

		// if (response.getStatusCode().equals("0000")) {
		// trxDaoHelper.receivePaymentUsingInvoiceNo("INV-0033", "N/A", "N/A",
		// "Credit/Debit Cards", "45626161", "","");
		// }

		// {amount=12950, card_holder_name=John Doe,
		// card_number=4242424242424242, expiry=112024, security_code=232}
	}

	// var HashData = SECURE_SECRET;

	// @Test
	public void createOpenAPIConnection() {
		HttpsURLConnection con = null;
		String MerchantId = "00000147";
		String Amount = "10";
		String PhoneNumber = "9940414243";
		String AccessCode = "E89A33E2";
		String SECURE_SECRET = "65922790075C6D39B3EC3C17B61FB4BE";
		String ReturnUrl = "http://localhost:8084/PaymentGateway/payus.jsp";
		String DestinationUrl = "https://migs.mastercard.com.au/vpcpay?";
		String MerchantTransactionRef = Amount + RandomStringUtils.random(16, true, true);
		String Command = "Pay";
		try {
			String urlAppenddata = "vpc_Merchant=" + MerchantId;
			urlAppenddata += ("&vpc_Amount=" + Amount);
			urlAppenddata += ("&vpc_AccessCode=" + AccessCode);
			urlAppenddata += ("&vpc_Command=" + Command);
			urlAppenddata += ("&vpc_MerchTxnRef=" + MerchantTransactionRef);
			urlAppenddata += ("&vpc_ReturnURL=" + ReturnUrl);
			urlAppenddata += ("&vpc_SecureHashType=" + "SHA256");
			urlAppenddata += ("&vpc_OrderInfo=" + PhoneNumber);
			urlAppenddata += ("&vpc_Version=" + "1.0");
			urlAppenddata += ("&vpc_SecureHash=" + SECURE_SECRET);
			String method = "POST";
			DestinationUrl = DestinationUrl + urlAppenddata;
			System.out.println("DestinationUrl:" + DestinationUrl);
			URL url = new URL(DestinationUrl);
			con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setSSLSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault());
			con.setDoOutput(true);
			con.setDoInput(true);
			System.out.println("con::::" + con.getResponseCode());
			// con.setRequestMethod(urlAppenddata);
			if (urlAppenddata != null && urlAppenddata.length() > 0) {
				con.addRequestProperty("content-type", "application/xml; charset=UTF-8");
				con.addRequestProperty("content-length", Integer.toString(urlAppenddata.length()));
			}
			// con.connect();
		} catch (Exception e) {
			System.out.println("Exception found in :" + e.getMessage());
		}
	}
}