package com.workpoint.icpak.tests.sms;

import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.util.SMSIntegration;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestSMSIntegration extends AbstractDaoTest {

	@Inject
	SMSIntegration integration;

	@Test
	public void sendSMS() {
		integration.send("0721239821", "Testing sms!!!!");
	}

}
