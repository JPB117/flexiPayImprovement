package com.workpoint.icpak.tests.dao;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.TransactionDaoHelper;
import com.workpoint.icpak.server.payment.CreditCardServiceImpl;
import com.workpoint.icpak.shared.model.CreditCardDto;
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
		creditCardDetails.setCard_holder_name("Tom Muriranja");
		creditCardDetails.setAmount("10");
		creditCardDetails.setCurrency("KES");
		creditCardDetails.setCountry("KE");
		creditCardDetails.setCard_number("4574670000197093");
		creditCardDetails.setExpiry("062017");
		creditCardDetails.setSecurity_code("238");
		creditCardDetails.setZip("0100");
		creditCardDetails.setAddress2("");
		creditCardDetails.setState("Nairobi");

		// CreditCardResponse response = creditCardService
		// .authorizeCardTransaction(creditCardDetails);

		// if (response.getStatusCode().equals("0000")) {
		//trxDaoHelper.receivePaymentUsingInvoiceNo("INV-0033", "N/A", "N/A", "Credit/Debit Cards", "45626161", "","");
		// }

		// {amount=12950, card_holder_name=John Doe,
		// card_number=4242424242424242, expiry=112024, security_code=232}
	}
}