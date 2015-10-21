package com.workpoint.icpak.tests.integration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.icpak.rest.dao.helper.MemberDaoHelper;
import com.icpak.rest.dao.helper.StatementDaoHelper;
import com.workpoint.icpak.tests.base.AbstractDaoTest;

public class TestRestClient extends AbstractDaoTest{
	@Inject
	MemberDaoHelper memberDaoHelper;
	
	@Inject StatementDaoHelper StatementDaoHelper;

	@Ignore
	public void getMember(){
		String url = "http://41.139.138.165/members/memberdata.php";
		Map<String, String> params = new HashMap<>();
		params.put("type", "member");
		params.put("reg_no", "1");
//		String json = new RestClient().executeHttpCall(url, params);
//		System.err.println(json);
		UriBuilder l;
	}
	
	@Test
	public void testMemberRecord() throws JSONException, IllegalStateException, IOException, ParseException{
		String memberRefId = "xi7Qdd8VbSQyGpiL";
		memberDaoHelper.updateMemberRecord(memberRefId);
	}
	
	@Ignore
	public void testMemberStementRecord() throws JSONException, IllegalStateException, IOException, ParseException, URISyntaxException, com.amazonaws.util.json.JSONException{
		String memberRefId = "P7Et9RjniWTAi2ik";
		StatementDaoHelper.updateStatementsRecord(memberRefId);
	}
}
