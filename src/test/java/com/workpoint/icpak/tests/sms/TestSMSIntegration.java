package com.workpoint.icpak.tests.sms;

import java.util.Date;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.SMSDao;
import com.icpak.rest.models.sms.SMSLog;
import com.icpak.rest.models.sms.SmsStatus;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestSMSIntegration extends AbstractDaoTest {

	@Inject
	SMSIntegration integration;

	@Inject
	SMSDao smsDao;

	// @Test
	public void sendSMS() {
		System.err.println(integration.send("0721239821", "Testing sms!!!!"));
	}

	@Test
	public void testSaveLog() {
		SMSLog sms = new SMSLog();
		sms.setTstamp(new Date());
		sms.setText("Testing SMS");
		sms.setSmsFrom("ICPAK");
		sms.setCost(Double.valueOf("1.5") * Double.valueOf("KES 2.000".substring(4)));
		sms.setSmsId("ATXid_cba0dd7cd1dac62e1c4448e09cb5694f");
		sms.setSmsTo("+254729472421");
		sms.setStatus(SmsStatus.valueOf("Success".toUpperCase()));
		smsDao.save(sms);

	}

}
