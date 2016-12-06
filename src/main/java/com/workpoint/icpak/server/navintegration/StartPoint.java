package com.workpoint.icpak.server.navintegration;

import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;

public class StartPoint {
	OnlineMemberPaymentsService systemService = new OnlineMemberPaymentsService();
	OnlineMemberPaymentsPort port = systemService.getOnlineMemberPaymentsPort();
	BindingProvider bindingProvider = (BindingProvider) port;

	public StartPoint() {
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return (new PasswordAuthentication("ICPAK\\tom.muriranja",
						new char[] { 'N', 'a', 'i', 'r', 'o', 'b', 'i', '1', '6' }));
			}
		};
		Authenticator.setDefault(authenticator);

		bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				"http://41.139.138.165:8055/NavOnlinePaymentsPilot/WS/ICPAK/Page/Online_Member_Payments");
	}

	public static void main(String[] args) throws ParseException, DatatypeConfigurationException {
		StartPoint t = new StartPoint();
		OnlineMemberPayments memberPayment = new OnlineMemberPayments();
		memberPayment.setAccountNo("5305");
		memberPayment.setAmount(new BigDecimal(10000.0));
		memberPayment.setDescription("Subscription payments for CPA Daniel Ngugi Mugo");
		memberPayment.setTransactionCode("KL28EJXCEHN");
		memberPayment.setPaymentMode(PaymentMode.MPESA);
		memberPayment.setTransactionNo(1);
		String dt = "2016-12-01 11:01";

		// t.create(memberPayment);
		t.list();
	}

	public void list() {
		String bookmarkKey = null;
		List<OnlineMemberPaymentsFilter> filters = new ArrayList<OnlineMemberPaymentsFilter>();
		
		// OnlineMemberPaymentsFilter f = new OnlineMemberPaymentsFilter();
		// f.setField(OnlineMemberPaymentsFields.STATUS);
		// f.setCriteria("<>'RECEIVED'");
		// filters.add(f);
		
		OnlineMemberPaymentsList list = port.readMultiple(filters, bookmarkKey, 50);
		System.out.println("Size>>>" + list.getOnlineMemberPayments().size());

		for (OnlineMemberPayments c : list.getOnlineMemberPayments()) {
			System.err.println("Payment = " + c.getAccountNo() + " - " + c.getDescription() + " - " + c.getAmount()
					+ " - " + c.getStatus());
		}

	}

	public void create(OnlineMemberPayments memberPayment) throws ParseException, DatatypeConfigurationException {
		Holder<OnlineMemberPayments> holder = new Holder<OnlineMemberPayments>(memberPayment);
		try {
			port.create(holder);
			System.err.println("Successfully Posted::" + memberPayment.getTransactionCode());
		} catch (Exception e) {
			System.err.println(
					"Exception thrown while posting to Nav for transaction::" + memberPayment.getTransactionCode());
			e.printStackTrace();
		}

	}
}
