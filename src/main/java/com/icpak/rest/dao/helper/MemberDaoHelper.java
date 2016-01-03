package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.icpak.rest.dao.MemberDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.membership.Member;
import com.workpoint.icpak.shared.model.MemberDto;
import com.workpoint.icpak.shared.model.MembershipStatus;

@Transactional
public class MemberDaoHelper {
	Logger logger = Logger.getLogger(MemberDaoHelper.class);

	@Inject
	MemberDao memberDao;
	@Inject
	UsersDao userDao;
	@Inject
	RolesDao roleDao;

	@Inject
	StatementDaoHelper statementDao;

	public MemberDto createMember(MemberDto memberDto) {
		if (memberDto.getRefId() != null) {
			return updateMember(memberDto.getRefId(), memberDto);
		}

		Member member = new Member();
		member.copyFrom(memberDto);

		memberDao.createMember(member);
		return member.toDto();
	}

	public MemberDto updateMember(String memberId, MemberDto dto) {
		Member po = memberDao.findByRefId(memberId, Member.class);
		po.copyFrom(dto);
		memberDao.updateMember(po);

		return po.toDto();
	}

	public void deleteMember(String memberId) {
		Member member = memberDao.findByRefId(memberId, Member.class);
		memberDao.delete(member);
	}

	public List<MemberDto> getAllMembers(Integer offset, Integer limit, String uriInfo, String searchTerm) {

		if (searchTerm != null) {
			return memberDao.getAllMembers(offset, limit, searchTerm);
		}

		List<Member> members = memberDao.getAllMembers(offset, limit);

		List<MemberDto> rtn = new ArrayList<>();
		for (Member member : members) {
			MemberDto dto = member.toDto();

			if (member.getUserRefId() != null) {
				User user = userDao.findByUserId(member.getUserRefId(), false);
				setMemberValues(dto, user);
			}
			rtn.add(dto);
		}

		return rtn;
	}

	public Integer getCount() {

		return memberDao.getMemberCount();
	}

	private void setMemberValues(MemberDto dto, User user) {
		if (user == null) {
			return;
		}

		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getUserData().getFirstName());
		dto.setLastName(user.getUserData().getLastName());
	}

	public MemberDto getMemberById(String memberId) {
		Member member = memberDao.findByRefId(memberId, Member.class);

		if (member == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "'" + memberId + "'");
		}

		return member.toDto();
	}

	/**
	 * 
	 * @param memberRefId
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws ParseException
	 * 
	 *             update member status from erp
	 */
	public void updateMemberRecord(String memberRefId, Boolean forceRefresh)
			throws IllegalStateException, IOException, ParseException {
		Member memberInDb = memberDao.findByRefId(memberRefId, Member.class);

		try {
			Date today = new Date();

			if (memberInDb.getLastUpdate() == null) {
				try {
					// update member details
					memberDao.updateMember(getErpRequest(memberInDb));
					try {
						statementDao.updateStatementsRecord(memberRefId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			} else {
				logger.fatal(" ==== Date difference === "
						+ ((today.getTime() - memberInDb.getLastUpdate().getTime()) - (48 * 60 * 1000)));

				if (forceRefresh) {

					logger.fatal(" ==== Force refresh == " + forceRefresh);

					makeErpRequest(memberInDb);
				} else if (today.getTime() - memberInDb.getLastUpdate().getTime() > 48 * 60 * 1000) {

					logger.fatal(" ==== Force refresh == " + forceRefresh);

					makeErpRequest(memberInDb);
				} else {
					logger.fatal(" ==== FAILED === Last check is not greater than 2 days == ");
				}
			}

		} catch (NullPointerException ex) {
			logger.fatal(" ==== DEATH === No last date updated date == " + ex);
			ex.printStackTrace();
		}

	}

	public void makeErpRequest(Member memberInDb) throws ParseException, IllegalStateException, IOException {
		logger.fatal(" ==== SUCCESS === Updated from ERP == ");

		try {
			// update member details
			memberDao.updateMember(getErpRequest(memberInDb));
			try {
				statementDao.updateStatementsRecord(memberInDb.getRefId());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public Member getErpRequest(Member memberInDb)
			throws URISyntaxException, IllegalStateException, IOException, ParseException {
		final HttpClient httpClient = new DefaultHttpClient();
		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();

		qparams.add(new BasicNameValuePair("type", "member"));
		qparams.add(new BasicNameValuePair("reg_no", memberInDb.getMemberNo()));

		final URI uri = URIUtils.createURI("http", "41.139.138.165/", -1, "members/memberdata.php",
				URLEncodedUtils.format(qparams, "UTF-8"), null);
		final HttpGet request = new HttpGet();
		request.setURI(uri);

		String res = "";
		HttpResponse response = null;

		StringBuffer result = null;

		try {
			request.setHeader("accept", "text/html");
			response = httpClient.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			result = new StringBuffer();

			String line = "";

			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");

		assert result != null;
		res = result.toString();

		JSONObject jObject;
		try {

			/**
			 * {"reg_no":"10078","Practising No_"
			 * :"","E-Mail":"gakinyasam@yahoo.com", "Name":
			 * "CPA Samuel Gakinya Muthoni","Address":"P.O. Box 12725",
			 * "Address 2":"","Post Code":"00400","Phone No_":"0720 792236",
			 * "Mobile No_":"", "Customer Type":"MEMBER","Date Registered"
			 * :"2012-08-22","Practicing Cert Date":"1753-01-01"}"
			 */
			jObject = new JSONObject(res);

			memberInDb.setLastUpdate(new Date());
			memberInDb.setRegistrationDate(formatter.parse((jObject.getString("Date Registered"))));
			memberInDb.setPractisingCertDate(formatter.parse((jObject.getString("Practicing Cert Date"))));
			memberInDb.setPractisingNo(jObject.getString("Practising No_"));
			memberInDb.setCustomerType(jObject.getString("Customer Type"));

			if (jObject.getInt("Status") == 0) {
				memberInDb.setMemberShipStatus(MembershipStatus.ACTIVE);
			}

			if (jObject.getInt("Status") == 1) {
				memberInDb.setMemberShipStatus(MembershipStatus.INACTIVE);
			}

			User memberUser = memberInDb.getUser();

			memberUser.setAddress(jObject.getString("Address"));
			// memberUser.setEmail(jObject.getString("E-Mail"));
			memberUser.setMobileNo(jObject.getString("Mobile No_"));
			memberUser.setPostalCode(jObject.getString("Post Code"));
			memberUser.setPhoneNumber(jObject.getString("Phone No_"));

			/*
			 * update this member user in db
			 */
			userDao.updateUser(memberUser);

			System.out.println("======<<<<<>>>>>>>==== + Response ====" + jObject.getString("Post Code"));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return memberInDb;
	}

	public Boolean loadFromErp(String memberId, Boolean forceRefresh) {
		// Update Member Records and Statements - Only update if the User is
		// a member;
		try {
			updateMemberRecord(memberId, forceRefresh);
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * Methods to be used by members presenter for website iframe
	 */

	public int getMembersCount(String searchTerm, String citySearchTerm, String categoryName) {
		return memberDao.getMembersCount(searchTerm, citySearchTerm, categoryName);
	}

	public List<MemberDto> getMembers(Integer offSet, Integer limit) {
		return memberDao.getMembers(offSet, limit);
	}

	public int getsearchMembersCount(String searchTerm) {
		return memberDao.getMembersSearchCount(searchTerm);
	}

	public List<MemberDto> searchMembers(String searchTerm, Integer offSet, Integer limit) {
		return memberDao.searchMembers(searchTerm, offSet, limit);
	}

	public List<MemberDto> getMembersFromOldTable(String searchTerm, String citySearchTerm, String categoryName,
			int offset, int limit) {
		return memberDao.searchMembersFromOldTable(searchTerm, citySearchTerm, categoryName, offset, limit);
	}

	public void autoUPdateMembers() {
		logger.info(" ++++++++ Auto update Member Details +++++ ");
		List<Member> membersInDb = memberDao.getAllMembers(0, 0);

		for (Member member : membersInDb) {
			try {
				updateMemberRecord(member.getRefId(), true);
			} catch (IllegalStateException | IOException | ParseException e) {
				e.printStackTrace();
			}
		}
	}

}