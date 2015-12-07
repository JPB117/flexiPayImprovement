package com.workpoint.icpak.tests.lms;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.Ignore;
import org.junit.Test;

import com.amazonaws.util.json.JSONException;
import com.google.inject.Inject;
import com.icpak.rest.dao.BookingsDao;
import com.icpak.rest.dao.helper.BookingsDaoHelper;
import com.icpak.rest.models.event.Delegate;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.workpoint.icpak.server.integration.lms.LMSIntegrationUtil;
import com.workpoint.icpak.shared.lms.LMSMemberDto;
import com.workpoint.icpak.shared.model.Gender;
import com.workpoint.icpak.shared.model.Title;
import com.workpoint.icpak.shared.model.events.DelegateDto;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestLMSIntegration extends AbstractDaoTest {
	@Inject
	BookingsDao dao;
	@Inject
	BookingsDaoHelper helper;

	@Ignore
	public void sendMemberData() throws IOException {
		// System.out.println("SysResponse = " + response);
	}

	@Ignore
	public void testClient() {
		// Client client = ClientBuilder.newClient();
		// String response = client
		// .target("http://e-learning.datamedu.com:8084/mLearning/api/Account/Register")
		// .request().get(String.class);
		// System.err.println("Response>>" + response);
	}

	@Ignore
	public void testClient2() throws ParseException {
		Client client = Client.create();

		LMSMemberDto dto = new LMSMemberDto();
		dto.setTitle(Title.Dr.getCode());
		dto.setFirstName("Duggan");
		dto.setLastName("Kimani");
		dto.setMobileNo("+254721239821");
		dto.setUserName("tomkim@icpak.com");
		dto.setPassword("testpass");
		dto.setDOB(new SimpleDateFormat("dd-mm-yyyy").format(new Date()));
		dto.setGender(Gender.MALE.getCode());
		dto.setTimeZone("E. Africa Standard Time");
		dto.setMembershipID("10000");
		dto.setRefID("48fd4a84f8da");

		WebResource resource = client.resource("http://localhost:8080/messenger/webapi/messages");

		ClientResponse resp = resource.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, dto);

		String out = resp.getEntity(String.class);
		System.err.println(">>Request>>" + dto.getUserName());
		System.err.println(">>Response>>" + out);

		if (resp.getStatus() == 200) {

		}

	}

	@Test
	public void testEnrolDelegateToLms() {
		List<DelegateDto> delegates = new ArrayList<>();
		Delegate delegateInDb = dao.findByRefId("h8IeWNUed7DckOgY", Delegate.class);
		DelegateDto dto = delegateInDb.toDto();
		dto.setCourseId("22");

		delegates.add(dto);

		try {
			helper.enrolDelegateToLMS(delegates, "YeVcDDmtM7th2SgR");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	// @Test
	// public void testIn
}
