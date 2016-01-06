package com.icpak.rest.dao.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.ws.rs.core.UriInfo;

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
import com.icpak.rest.BaseResource;
import com.icpak.rest.dao.ApplicationFormDao;
import com.icpak.rest.dao.RolesDao;
import com.icpak.rest.dao.TransactionsDao;
import com.icpak.rest.dao.UserSessionDao;
import com.icpak.rest.dao.UsersDao;
import com.icpak.rest.exceptions.ServiceException;
import com.icpak.rest.models.ErrorCodes;
import com.icpak.rest.models.auth.BioData;
import com.icpak.rest.models.auth.Role;
import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.base.ExpandTokens;
import com.icpak.rest.models.base.ResourceCollectionModel;
import com.icpak.rest.models.base.ResourceModel;
import com.icpak.rest.models.membership.ApplicationFormHeader;
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Statement;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.security.authentication.AuthenticationException;
import com.icpak.rest.security.authentication.Authenticator;
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.utils.EmailServiceHelper;
import com.workpoint.icpak.server.integration.lms.LMSIntegrationUtil;
import com.workpoint.icpak.server.integration.lms.LMSResponse;
import com.workpoint.icpak.shared.lms.LMSMemberDto;
import com.workpoint.icpak.shared.lms.LMSPassWordDto;
import com.workpoint.icpak.shared.model.ApplicationFormHeaderDto;
import com.workpoint.icpak.shared.model.Gender;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.Title;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.AccountStatus;
import com.workpoint.icpak.shared.model.auth.ActionType;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;
import com.workpoint.icpak.shared.trx.TransactionDto;

@Transactional
public class UsersDaoHelper {

	Logger logger = Logger.getLogger(UsersDaoHelper.class.getName());
	@Inject
	UsersDao dao;
	@Inject
	ApplicationFormDao applicationDao;
	@Inject
	RolesDao roleDao;
	@Inject
	TransactionsDao trxDao;
	@Inject
	Authenticator authenticator;
	@Inject
	UserSessionDao loginCookieDao;
	@Inject
	ApplicationSettings settings;

	@Inject
	MemberDaoHelper memberDaoHelper;

	/**
	 * System User
	 * 
	 * @param dto
	 * @return
	 */
	public UserDto create(UserDto dto) {
		return create(dto, true);
	}

	public UserDto create(UserDto dto, boolean sendActivationEmail) {
		User user = new User();
		user.copyFrom(dto);
		user.setPassword(dto.getPassword());
		for (RoleDto role : dto.getGroups()) {
			Role r = dao.findByRefId(role.getRefId(), Role.class);
			user.addRole(r);
		}
		dao.createUser(user);
		// createDefaultMemberForUser(user);

		if (sendActivationEmail) {
			sendActivationEmail(user);
		}

		return user.toDto();
	}

	public void sendActivationEmail(String userId, String emailAddress) {
		User user = dao.findByUserId(userId);
		user.setEmail(emailAddress);
		dao.updateUser(user);
		ApplicationFormHeader application = applicationDao
				.getApplicationByUserRef(user.getRefId());
		application.setEmail(emailAddress);
		applicationDao.updateApplication(application);

		sendActivationEmail(user);
	}

	private void sendActivationEmail(User user) {
		String subject = "Welcome to ICPAK Portal!";
		String link = settings.getApplicationPath() + "#activateacc;uid="
				+ user.getRefId();
		String body = "Dear "
				+ user.getUserData().getFullNames()
				+ ","
				+ "<br/>An account has been created for you on the ICPAK portal. "
				+ "You will need to create your password on the portal using the following details."
				+ "<p/><a href=" + link + ">Click this link </a>"
				+ " to create your password." + "<p>Thank you";

		try {
			EmailServiceHelper.sendEmail(body, subject,
					Arrays.asList(user.getEmail()),
					Arrays.asList(user.getUserData().getFullNames()));

		} catch (Exception e) {
			logger.info("Activation Email for " + user.getEmail()
					+ " failed. Cause: " + e.getMessage());
			e.printStackTrace();
			// throw new Run
		}

	}

	/**
	 * Other user
	 * 
	 * @param user
	 * @return
	 */
	public UserDto create(User user) {
		dao.createUser(user);
		createDefaultMemberForUser(user);
		return user.toDto();
	}

	/**
	 * Internal Users - Creation
	 * 
	 * @param userId
	 * @param dto
	 * @return
	 */
	public UserDto update(String userId, UserDto dto) {
		User po = dao.findByUserId(userId);
		po.setEmail(dto.getEmail());
		po.setMobileNo(dto.getPhoneNumber());

		// po.setPassword(dto.getPassword());
		// We do not update password here :Duggan 21/09/2015
		// updatePassword(userId, dto.getPassword());

		if (po.getUserData() == null) {
			po.setUserData(dto);
		} else {
			BioData data = po.getUserData();
			data.setFirstName(dto.getName());
			data.setLastName(dto.getSurname());
		}

		for (RoleDto role : dto.getGroups()) {
			Role r = dao.findByRefId(role.getRefId(), Role.class);
			po.addRole(r);
		}

		dao.updateUser(po);

		return po.toDto();
	}

	public void update(String userId, User user) {
		User po = dao.findByUserId(userId);

		po.setEmail(user.getEmail());
		// po.setUsername(user.getUsername());
		po.setAddress(user.getAddress());
		po.setCity(user.getCity());
		po.setNationality(user.getNationality());
		po.setPhoneNumber(user.getPhoneNumber());
		po.setResidence(user.getResidence());

		if (po.getUserData() == null) {
			po.setUserData(user.getUserData());
		} else {
			BioData data = po.getUserData();
			data.setAgeGroup(user.getUserData().getAgeGroup());
			data.setDob(user.getUserData().getDob());
			data.setFirstName(user.getUserData().getFirstName());
			data.setLastName(user.getUserData().getLastName());
			data.setGender(user.getUserData().getGender());
			data.setNationality(user.getUserData().getNationality());
			data.setOverseas(user.getUserData().isOverseas());
			data.setSalutation(user.getUserData().getSalutation());
			data.setTitle(user.getUserData().getTitle());
			// data.setResidence(user.getUserData().getResidence());
		}

		dao.updateUser(po);

		if (!dao.hasMember(po)) {
			createDefaultMemberForUser(po);
		}
	}

	private void createDefaultMemberForUser(User user) {
		// create and empty member a/c
		Member member = new Member(user.getRefId());
		member.setRefId(user.getRefId());
		member.setUser(user);
		if (user.getMemberNo() != null) {
			member.setMemberNo(user.getMemberNo());
		}
		dao.save(member);
	}

	public void delete(String userId) {
		User user = dao.findByUserId(userId);
		dao.delete(user);
	}

	public List<UserDto> getAllUsers(Integer offset, Integer limit,
			String uriInfo) {
		return getAllUsers(offset, limit, uriInfo, null);
	}

	public List<UserDto> getAllUsers(Integer offset, Integer limit,
			String uriInfo, String searchTerm) {
		List<User> users = dao.getAllUsers(offset, limit, null, searchTerm);
		List<UserDto> dtos = new ArrayList<>();

		for (User user : users) {
			dtos.add(user.toDto());
		}
		return dtos;
	}

	public Integer getCount(String searchTerm) {
		return dao.getUserCount(searchTerm);
	}

	public Integer getCount() {
		return getCount(null);
	}

	public ResourceCollectionModel<User> getAllUsers(Integer offSet,
			Integer limit, UriInfo uriInfo, String roleId) {
		int total = dao.getUserCount(roleId);
		Role role = null;
		if (roleId != null) {
			role = roleDao.getByRoleId(roleId);
		}

		ResourceCollectionModel<User> collection = new ResourceCollectionModel<>(
				offSet, limit, total, uriInfo);
		List<User> members = dao.getAllUsers(offSet, limit, role, null);

		List<User> rtn = new ArrayList<>();
		for (User user : members) {
			user.setUri(uriInfo.getAbsolutePath().toString() + "/"
					+ user.getRefId());
			rtn.add(user.clone(ExpandTokens.DETAIL.toString()));
		}

		collection.setItems(rtn);
		return collection;
	}

	public ResourceModel getAllUsersByRoleId(Integer offSet, Integer limit,
			UriInfo uriInfo, String roleId, String... expand) {
		if (offSet == null)
			offSet = 0;
		if (limit == null)
			limit = BaseResource.PAGE_LIMIT;

		Role role = roleDao.getByRoleId(roleId);
		if (role == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Role", "'"
					+ roleId + "'");
		}

		// int total = dao.getUserCount(roleId);
		// ResourceCollectionModel<User> collection = new
		// ResourceCollectionModel<>(offSet,limit,total, uriInfo);
		Role clone = role.clone(expand);
		clone.setUri(uriInfo.getBaseUri().toString() + "/roles/" + roleId);
		Collection<User> members = clone.getUsers();

		for (User user : members) {
			user.setUri(uriInfo.getBaseUri() + "/users/" + user.getRefId());
		}
		// clone.setUsers(members);

		// collection.setItems(members);
		return clone;
	}

	public User getUser(String userId) {
		User user = dao.findByUserId(userId);

		if (user == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "'" + userId + "'");
		}

		return user.clone();
	}

	public User getUserByActivationEmail(String userId) {
		User user = dao.findByUserActivationEmail(userId, false);

		if (user == null) {
			// Check User From ERP
			try {
				user = checkIfUserExistInERP(userId);
				if (user == null) {
					throw new ServiceException(ErrorCodes.NOTFOUND, "'"
							+ userId + "'");
				}
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user.clone();
	}

	private User checkIfUserExistInERP(String userId)
			throws URISyntaxException, ParseException, JSONException {

		logger.error(" ===>>><<<< === Checking for this User In ERP ===>><<<>>== ");
		final HttpClient httpClient = new DefaultHttpClient();
		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();

		qparams.add(new BasicNameValuePair("type", "activation"));
		qparams.add(new BasicNameValuePair("email", userId));

		final URI uri = URIUtils.createURI("http", "41.139.138.165/", -1,
				"members/memberdata.php",
				URLEncodedUtils.format(qparams, "UTF-8"), null);
		final HttpGet request = new HttpGet();
		request.setURI(uri);

		String res = "";
		HttpResponse response = null;

		StringBuffer result = null;

		try {
			request.setHeader("accept", "application/json");
			response = httpClient.execute(request);

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

		/**
		 * Check if the erp server returns null string
		 */
		if (res.equals("null")) {
			logger.error(" ===>>><<<< === User not found in ERP ===>><<<>>== ");
			return null;
		} else {
			JSONObject jo = new JSONObject(res);
			String memberNo = jo.getString("reg_no");
			String PractisingNo = jo.getString("Practising No_");
			String email = jo.getString("E-Mail");
			String fullNames = jo.getString("Name");
			String address = jo.getString("Address");
			String postCode = jo.getString("Post Code");
			String phoneNo = jo.getString("Phone No_");
			String customerType = jo.getString("Customer Type");
			String dateRegistered = jo.getString("Date Registered");
			Integer status = jo.getInt("Status");
			String practisingCertDate = jo.getString("Practicing Cert Date");

			User user = dao.findUserByMemberNo(memberNo);
			user.setEmail(email);
			BioData userData = new BioData();
			userData.setFullNames(fullNames);
			user.setMobileNo(phoneNo);
			update(user.getRefId(), user);
			return user;
		}
	}

	public void setProfilePic(String userId, byte[] bites, String fileName,
			String contentType) {
		User user = dao.findByUserId(userId);
		Attachment attachment = new Attachment();
		attachment.setAttachment(bites);
		attachment.setContentType(contentType);
		attachment.setName(fileName);
		attachment.setProfilePicUserId(userId);

		dao.save(attachment);
	}

	public Attachment getProfilePic(String userId) {
		Attachment a = dao.getProfilePic(userId);

		if (a == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "Profile Picture ",
					"for user " + userId);
		}

		return a.clone("all");
	}

	public void updatePassword(String userId, String newPassword) {
		User user = dao.findByUserId(userId);
		if (user.getHashedPassword() == null
				|| user.getHashedPassword().isEmpty()) {
			throw new ServiceException(ErrorCodes.ILLEGAL_ARGUMENT,
					"'New Password'", "'NULL'");
		}

		user.setPassword(dao.encrypt(user.getHashedPassword()));
		dao.save(user);
	}

	public List<TransactionDto> getTransactions(String userId) {

		List<Transaction> trxs = trxDao.getTransactions(userId);
		List<TransactionDto> trxDtos = new ArrayList<>();
		for (Transaction trx : trxs) {
			TransactionDto dto = trx.toDto();
			trxDtos.add(dto);
		}

		return trxDtos;
	}

	public LogInResult execLogin(LogInAction action) {
		UserDto userDto;
		boolean isLoggedIn = true;

		if (action.getActionType() == ActionType.VIA_COOKIE) {
			userDto = getUserFromCookie(action.getLoggedInCookie());
		} else {
			userDto = getUserFromCredentials(action.getUsername(),
					action.getPassword());
		}

		isLoggedIn = userDto != null;

		String loggedInCookie = "";
		if (isLoggedIn) {
			loggedInCookie = loginCookieDao.createSessionCookie(
					action.getLoggedInCookie(), userDto);
			userDto.setApplicationRefId(getApplicationRefId(userDto.getRefId()));
			userDto.setMemberRefId(dao.getMemberRefId(userDto.getRefId()));

		}

		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);

		logger.info("LogInHandlerexecut(): actiontype="
				+ action.getActionType());
		logger.info("LogInHandlerexecut(): currentUserDto="
				+ currentUserDto.getUser().getFullName() + " memberNo:"
				+ currentUserDto.getUser().getMemberNo());
		logger.info("LogInHandlerexecut(): loggedInCookie=" + loggedInCookie);

		assert action.getActionType() == null;
		return new LogInResult(action.getActionType(), currentUserDto,
				loggedInCookie);
	}

	private UserDto getUserFromCookie(String loggedInCookie) {
		UserDto userDto = null;
		try {
			userDto = authenticator.authenticatCookie(loggedInCookie);
			userDto.setApplicationRefId(getApplicationRefId(userDto.getRefId()));
		} catch (AuthenticationException e) {
			// isLoggedIn = false;
		}

		return userDto;
	}

	private UserDto getUserFromCredentials(String username, String password) {
		UserDto userDto = null;
		try {
			userDto = authenticator.authenticateCredentials(username, password);
			userDto.setApplicationRefId(getApplicationRefId(userDto.getRefId()));
		} catch (AuthenticationException e) {
			// isLoggedIn = false;
		}

		return userDto;
	}

	public void changePassword(String userId, String newPassword) {
		User user = dao.findByUserId(userId);
		user.setPassword(newPassword);

		/**
		 * Updating lms password upon password change
		 */
		if (user.getMemberNo() != null && !user.getMemberNo().isEmpty()) {
			logger.info(" +++++++ Updating LMS password upon password change +++++++ ");

			LMSResponse lmsRespone = null;

			LMSPassWordDto dto = new LMSPassWordDto();
			dto.setMembershipID(user.getMemberNo());
			dto.setPassword(newPassword);

			JSONObject jObject = new JSONObject(dto);
			try {
				lmsRespone = LMSIntegrationUtil.getInstance().executeLMSCall(
						"/Account/Updatepassword", jObject, String.class);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (lmsRespone != null) {
				if (lmsRespone.equals("Invalid User.")) {
					return;
				}
			}

		}

		dao.changePassword(user);

	}

	public String postUserToLMS(String userRefId, String password)
			throws IOException {
		User user = dao.findByUserId(userRefId);

		if (user.getLmsPayLoad() == null || user.getLmsPayLoad().isEmpty()) {
			logger.info(" ++++++++ Sending Activation mail on post user to lms +++++ ");
			sendActivationEmail(user);
		}

		LMSMemberDto dto = new LMSMemberDto();
		dto.setFirstName(user.getUserData().getFirstName());
		dto.setLastName(user.getUserData().getLastName());
		dto.setGender((user.getUserData().getGender() == Gender.MALE ? Gender.MALE
				.getCode() + ""
				: Gender.FEMALE.getCode() + ""));
		if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
			dto.setMobileNo(user.getPhoneNumber());
		} else {
			dto.setMobileNo("0722333333");
		}
		dto.setPassword(password);
		dto.setTimeZone("E. Africa Standard Time");
		dto.setTitle(Title.Mr.getCode() + "");
		if (user.getUserData().getDob() != null) {
			dto.setDOB(new SimpleDateFormat("dd-MM-yyyy").format(user
					.getUserData().getDob()));
		} else {
			dto.setDOB(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
		}

		if (user.getMemberNo() != null) {
			dto.setMembershipID(user.getMemberNo());
		}

		if (user.getEmail() != null) {
			dto.setUserName(user.getEmail());
		}
		dto.setRefID(user.getRefId());

		JSONObject jObject = new JSONObject(dto);
		LMSResponse response = LMSIntegrationUtil.getInstance().executeLMSCall(
				"/account/register", jObject, String.class);
		logger.info("LMS Response::" + response.getMessage());
		logger.info("LMS Status::" + response.getStatus());
		logger.info("LMS PayLoad::" + jObject.toString());
		user.setLmsResponse(response.getMessage());
		user.setLmsStatus(response.getStatus());
		user.setLmsPayLoad(jObject.toString());
		dao.updateUser(user);

		return response.getStatus();
	}

	public void activateAccount(String userId, AccountStatus activated) {
		User user = dao.findByUserId(userId);
		user.setStatus(activated);

		dao.save(user);
	}

	public String getApplicationRefId(String userRef) {
		return dao.getApplicationRefId(userRef);
	}

	public void resetAccount(String userId) {
		User user = dao.findByUserId(userId);

		String subject = "ICPAK Portal Email Reset";
		String resetUrl = settings.getApplicationPath() + "/#activateacc;uid="
				+ userId;

		assert (user != null);
		String body = "Dear "
				+ user.getUserData().getFirstName()
				+ ",<br/>"
				+ "Your password has been successfully reset. "
				+ "<a href='"
				+ resetUrl
				+ "'>Click Here to Create Password</a><br/>"
				+ "This email can be ignored if you did not request a password reset on the portal. The link is only "
				+ "available for a short time";

		// System.err.println(">>>>>" + body);

		try {
			EmailServiceHelper.sendEmail(body, subject,
					Arrays.asList(user.getEmail()),
					Arrays.asList(user.getUserData().getFirstName()));
		} catch (UnsupportedEncodingException | MessagingException e) {
			logger.info("Send Reset Email Failed: email= " + user.getEmail()
					+ ", refId= " + user.getRefId());
			e.printStackTrace();

		}
	}

	public UserDto rePostToLms(String userRefId) {
		logger.info(" ++++++++++++++++ REPOST TO LMS ++++++++++++++ ");
		User user = dao.findByUserId(userRefId);
		LMSResponse response = null;
		try {
			if (user.getLmsPayLoad() != null) {
				response = LMSIntegrationUtil.getInstance()
						.executeLMSCall("/account/register",
								user.getLmsPayLoad(), String.class);
			} else {
				response = new LMSResponse();

				if (user.getLmsStatus() == null
						|| user.getLmsStatus().equals("Failed")
						|| user.getLmsStatus().isEmpty()
						|| user.getLmsResponse() == null) {
					sendActivationEmail(user);
				}

				response.setMessage("No payload available");
				response.setStatus("Failed");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response != null) {
			logger.info("LMS Response on Repost::" + response.getMessage());
			logger.info("LMS Status on Repost::" + response.getStatus());
			logger.info("LMS PayLoad on Repost::" + user.getLmsPayLoad());
			user.setLmsResponse(response.getMessage());
			user.setLmsStatus(response.getStatus());
			dao.updateUser(user);
		}

		return user.toDto();
	}
}