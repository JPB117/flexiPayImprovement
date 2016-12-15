package com.workpoint.icpak.server.navintegration;

import java.math.BigDecimal;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
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
		memberPayment.setPaymentMode(NavPaymentMode.MPESA);
		memberPayment.setTransactionNo(1);
		String dt = "2016-12-01 11:01";

		// t.create(memberPayment);
		t.list();
	}

	public void list() {
		String bookmarkKey = null;
		List<OnlineMemberPaymentsFilter> filters = new ArrayList<OnlineMemberPaymentsFilter>();

		OnlineMemberPaymentsFilter f = new OnlineMemberPaymentsFilter();
		f.setField(OnlineMemberPaymentsFields.TRANSACTION_NO);
		f.setCriteria("'13726'");
		filters.add(f);

		OnlineMemberPaymentsList list = port.readMultiple(filters, bookmarkKey, 1000);
		System.out.println("Size>>>" + list.getOnlineMemberPayments().size());

		for (OnlineMemberPayments c : list.getOnlineMemberPayments()) {
			System.err.println("Payment = " + c.getTransactionDate() + "-" + c.getAccountNo() + " - "
					+ c.getDescription() + " - " + c.getAmount() + " - " + c.getStatus() + " - " + c.getKey());
			c.setTransactionCode("AT4P0LIRNR");
			update(c);
		}

	}

	public void update(OnlineMemberPayments onlineC) {
		Holder<OnlineMemberPayments> onlineHolder = new Holder<OnlineMemberPayments>(onlineC);
		port.update(onlineHolder);
		System.err.println("Successfully Updated");
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

	public void delete(String key) {
		port.delete(key);
	}
}
