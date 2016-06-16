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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.security.authentication.AuthenticationException;
import com.icpak.rest.security.authentication.Authenticator;
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.utils.EmailServiceHelper;
import com.workpoint.icpak.server.integration.lms.LMSIntegrationUtil;
import com.workpoint.icpak.server.integration.lms.LMSResponse;
import com.workpoint.icpak.shared.lms.LMSMemberDto;
import com.workpoint.icpak.shared.model.ApplicationType;
import com.workpoint.icpak.shared.model.Gender;
import com.workpoint.icpak.shared.model.MembershipStatus;
import com.workpoint.icpak.shared.model.RoleDto;
import com.workpoint.icpak.shared.model.Title;
import com.workpoint.icpak.shared.model.UserDto;
import com.workpoint.icpak.shared.model.auth.AccountStatus;
import com.workpoint.icpak.shared.model.auth.ActionType;
import com.workpoint.icpak.shared.model.auth.CurrentUserDto;
import com.workpoint.icpak.shared.model.auth.LogInAction;
import com.workpoint.icpak.shared.model.auth.LogInResult;
import com.workpoint.icpak.shared.trx.OldTransactionDto;

@Transactional
public class UsersDaoHelper {

	Logger logger = Logger.getLogger(UsersDaoHelper.class.getName());
	@Inject
	UsersDao dao;
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
	ApplicationFormDao applicationFormDao;

	@Inject
	MemberDaoHelper memberDaoHelper;

	@Inject
	ApplicationFormDaoHelper applicationFormDaoHelper;

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
		ApplicationFormHeader application = applicationFormDao
				.getApplicationByUserRef(user.getRefId());
		application.setEmail(emailAddress);
		applicationFormDao.updateApplication(application);

		sendActivationEmail(user);
	}

	private void sendActivationEmail(User user) {
		String fullNames = user.getFullName();
		String subject = "Welcome to ICPAK Portal!";
		String link = settings.getApplicationPath() + "#activateacc;uid="
				+ user.getRefId();
		String body = "Dear "
				+ fullNames
				+ ","
				+ "<br/><br/>An account has been created for you on the ICPAK portal. "
				+ "You will need to create your password on the portal using the following details."
				+ "<p/><a href=" + link + ">Click this link </a>"
				+ " to create your password." + ""
				+ "<p><strong>Having trouble with above link?"
				+ " Copy below link on a new tab on browser.</strong>" + "<p/>"
				+ "<a style='text-decoration: underline;'>" + link + "</a>"
				+ "</p>" +

				"<p>Thank you";

		try {
			EmailServiceHelper.sendEmail(body, subject,
					Arrays.asList(user.getEmail()),
					Arrays.asList(user.getFullName()));

		} catch (Exception e) {
			logger.info("Activation Email for " + user.getEmail()
					+ " failed. Cause: " + e.getMessage());
			e.printStackTrace();
			// throw new Run
		}

	}

	public void sendActivationEmail2(User user) {
		String fullNames = user.getFullName();

		logger.info("Activation Email for " + user.getEmail());

		String subject = "Welcome to ICPAK Portal!";
		String link = settings.getApplicationPath() + "#activateacc;uid="
				+ user.getRefId();
		String body = "Dear "
				+ fullNames
				+ ","
				+ "<br/><br/>An account has been created for you on the ICPAK portal. "
				+ "You will need to create your password on the portal using the following details."
				+ "<p/><a href=" + link + ">Click this link </a>"
				+ " to create your password." + ""
				+ "<p><strong>Having trouble with above link?"
				+ " Copy below link on a new tab on browser.</strong>" + "<p/>"
				+ "<a style='text-decoration: underline;'>" + link + "</a>"
				+ "</p>" +

				"<p>Thank you";

		try {
			EmailServiceHelper.sendEmail(body, subject,
					Arrays.asList(user.getEmail()), Arrays.asList(fullNames));

		} catch (Exception e) {
			logger.info("Activation Email for " + user.getEmail()
					+ " failed. Cause: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Other user
	 * 
	 * @param user
	 * @return
	 */
	public UserDto create(User user) {
		return create(user, false);
	}

	public UserDto create(User user, boolean createMemberAccount) {
		dao.createUser(user);
		if (createMemberAccount) {
			createDefaultMemberForUser(user);
		}
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
		po = po.copyOnUpdate(dto);
		if (po.getUserData() == null) {
			po.setUserData(dto);
		} else {
			BioData data = po.getUserData();
			data.setFirstName(dto.getName());
			data.setLastName(dto.getSurname());
		}
		if (po.getMember() != null) {
			po.getMember().setMemberNo(dto.getMemberNo());
			po.getMember().setMemberQrCode(dto.getMemberQrCode());
		}
		// Delete all Roles for the current User
		dao.deleteAllRolesForCurrentUser(po.getId());
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
		return clone;
	}

	public User getUser(String userId) {
		User user = dao.findByUserId(userId);

		if (user == null) {
			throw new ServiceException(ErrorCodes.NOTFOUND, "'" + userId + "'");
		}

		return user.clone();
	}

	public User getUserByActivationEmail(String userEmail) {
		boolean isFullMembershipDetected = false;
		boolean isAssociateDetected = false;
		boolean isEmptyMemberNo = false;
		List<User> users = dao.findByUserActivationEmail(userEmail);
		User user = null;
		if (users.size() == 1) {
			user = users.get(0);
		} else if (users.size() == 2) {
			System.err.println("Found 2 records for user with email::"
					+ userEmail);

			// Deactivate the One Account and leave the other one
			for (User u : users) {
				if (u.getMemberNo() == null) {
					isEmptyMemberNo = true;
				} else if (u.getMemberNo().contains("Assoc/")
						|| u.getMemberNo().contains("ASSOC/")) {
					isAssociateDetected = true;
				} else {
					isFullMembershipDetected = true;
				}
			}
			System.err.println(">>>>isAssociate Detected:"
					+ isAssociateDetected + " \n>>>>isFullMember:"
					+ isFullMembershipDetected
					+ " \n>>>>isEmptyMemberDetected:" + isEmptyMemberNo);

			if (isEmptyMemberNo
					&& (isAssociateDetected || isFullMembershipDetected)) {
				System.err.println("Detected Empty MemberNo...");
				user = deleteEmptyMemberNo(users);
			}

			if (isAssociateDetected && isFullMembershipDetected) {
				System.err
						.println("Detected Associate and Full Member Accounts..");
				user = deleteAssociateAccount(users);
			}
		}

		if (user != null) {
			sendActivationEmail2(user);
			return user;
		} else {
			try {
				user = checkIfUserExistInERP(userEmail);
			} catch (URISyntaxException | ParseException | JSONException e) {
				e.printStackTrace();
			}
			return user.clone();
		}
	}

	private User deleteAssociateAccount(List<User> users) {
		User correctUser = null;
		for (User u : users) {
			if (u.getMemberNo().contains("Assoc/")
					|| u.getMemberNo().contains("ASSOC/")) {
				System.err.println("deleted refId::" + u.getRefId());
				dao.delete(u);
				ApplicationFormHeader h = applicationFormDao
						.getApplicationByUserRef(u.getRefId());
				dao.delete(h);
			} else {
				correctUser = u;
			}
		}
		return correctUser;
	}

	private User deleteEmptyMemberNo(List<User> users) {
		User correctUser = null;
		for (User u : users) {
			if (u.getMemberNo() == null) {
				System.err.println("deleted refId::" + u.getRefId());
				dao.delete(u);
				ApplicationFormHeader h = applicationFormDao
						.getApplicationByUserRef(u.getRefId());
				if (h != null) {
					dao.delete(h);
				}
			} else {
				correctUser = u;
			}
		}
		return correctUser;
	}

	public User checkIfUserExistInERP(String userEmail)
			throws URISyntaxException, ParseException, JSONException {
		logger.error(" ===>>><<<< === Checking for this User In ERP ===>><<<>>== ");
		final HttpClient httpClient = new DefaultHttpClient();
		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();

		qparams.add(new BasicNameValuePair("type", "activation"));
		qparams.add(new BasicNameValuePair("email", userEmail));

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

		logger.error(" ===>>><<<< === RESULT ===>><<<>>== " + res);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		/**
		 * Check if the erp server returns null string
		 */
		if (res.equals("null")) {
			logger.error(" ===>>><<<< === User not found in ERP ===>><<<>>== ");
			return null;
		} else {
			JSONObject jo = new JSONObject(res);
			String memberNo = jo.getString("reg_no");
			String practisingNo = jo.getString("Practising No_");
			String email = userEmail;
			String fullNames = jo.getString("Name");
			String address = jo.getString("Address");
			String postCode = jo.getString("Post Code");
			String phoneNo = jo.getString("Phone No_");
			String mobileNo = jo.getString("Mobile No_");
			String idNo = jo.getString("ID No");
			String customerType = null;
			if (jo.getString("Customer Type").equals("PRAC MEMBER")) {
				customerType = "PRAC_MEMBER";
			} else if (jo.getString("Customer Type").equals("PRACTICING RT")) {
				customerType = "PRACTISING_RT";
			} else {
				customerType = jo.getString("Customer Type");
			}

			String customerPostingGroup = jo
					.getString("Customer Posting Group");
			Date dateRegistered = formatter.parse((jo
					.getString("Date Registered")));
			Integer status = jo.getInt("Status");
			Date practisingCertDate = formatter.parse((jo
					.getString("Practicing Cert Date")));

			/*
			 * Check if this member exist either by memberNo OR this was a new
			 * applicant
			 */
			User userInDb = dao.findUserByMemberNo(memberNo);
			Member memberInDb = memberDaoHelper.findByMemberNo(memberNo);
			if (userInDb == null) {
				logger.error("Check if this was a new applicant..");
				userInDb = dao.findByUserActivationEmail(userEmail, false);
			}

			if (memberInDb != null) {
				logger.error(" ===>>><<<< === MEMBER IN DB NOT NULL - "
						+ "We are only updating this member record ===>><<<>>== ");
				memberInDb.setLastUpdate(new Date());
				memberInDb.setRegistrationDate(dateRegistered);
				memberInDb.setPractisingCertDate(practisingCertDate);
				memberInDb.setPractisingNo(practisingNo);
				memberInDb.setCustomerType(customerType);
				memberInDb.setMemberNo(memberNo);

				if (status == 0) {
					memberInDb.setMemberShipStatus(MembershipStatus.ACTIVE);
				}
				if (status == 1) {
					memberInDb.setMemberShipStatus(MembershipStatus.INACTIVE);
				}

			} else {
				logger.error(" ===>>><<<< === MEMBER IN DB NULL - Insert a new memberRecord "
						+ "+ User Record ===>><<<>>== ");
				memberInDb = new Member();
				memberInDb.setLastUpdate(new Date());
				memberInDb.setRegistrationDate(dateRegistered);
				memberInDb.setPractisingCertDate(practisingCertDate);
				memberInDb.setPractisingNo(practisingNo);
				memberInDb.setCustomerType(customerType);
				memberInDb.setMemberNo(memberNo);
				if (status == 0) {
					memberInDb.setMemberShipStatus(MembershipStatus.ACTIVE);
				}
				if (status == 1) {
					memberInDb.setMemberShipStatus(MembershipStatus.INACTIVE);
				}
			}

			logger.error(" ===>>><<<< === MEMBER EMAIL ===>><<<>>== " + email);
			if (userInDb != null) {
				logger.error(" ===>>><<<< === USER IN DB NOT NULL ===>><<<>>== ");
				memberInDb.setUser(userInDb);
				memberInDb.setUserRefId(userInDb.getRefId());
				userInDb.setAddress(address);
				userInDb.setMobileNo(mobileNo);
				userInDb.setPostalCode(postCode);
				userInDb.setPhoneNumber(phoneNo);

				BioData userData = new BioData();
				userData.setFullNames(fullNames);
				userInDb.setMobileNo(phoneNo);
				userInDb.setMemberNo(memberNo);
				userInDb.setMember(memberInDb);
				userInDb.setUserData(userData);
				userInDb.setPassword("pass1");
				userInDb.setEmail(email);
				userInDb.setUsername(email);
				userInDb.setFullName(fullNames);

				dao.deleteAllRolesForCurrentUser(userInDb.getId());
				Role r = roleDao.getByName("MEMBER");
				userInDb.addRole(r);

				dao.createUser(userInDb);

			} else {
				logger.error(" ===>>><<<< === USER IN DB NULL ===>><<<>>== ");
				userInDb = new User();
				memberInDb.setUser(userInDb);
				memberInDb.setUserRefId(userInDb.getRefId());

				userInDb.setAddress(address);
				userInDb.setMobileNo(mobileNo);
				userInDb.setPostalCode(postCode);
				userInDb.setPhoneNumber(phoneNo);

				BioData userData = new BioData();
				userData.setFullNames(fullNames);
				userInDb.setMobileNo(phoneNo);
				userInDb.setMemberNo(memberNo);
				userInDb.setMember(memberInDb);
				userInDb.setPassword("pass1");
				userInDb.setEmail(email);
				userInDb.setUsername(email);
				userInDb.setUserData(userData);
				userInDb.setFullName(fullNames);

				Role r = roleDao.getByName("MEMBER");
				if (r == null) {
					logger.error(">>>>>>>>>>>>>>>>Role cannot be found...!<<<<<<<<");
				}
				userInDb.addRole(r);

				dao.createUser(userInDb);

			}

			/* Update Application Form Header */
			ApplicationFormHeader userFormHeader = new ApplicationFormHeader();
			userFormHeader.setMobileNo(mobileNo);
			userFormHeader.setIdNumber(idNo);
			userFormHeader.setEmail(email);
			userFormHeader.setOtherNames(fullNames);
			userFormHeader.setApplicationDate(dateRegistered);
			userFormHeader.setAddress1(address);
			userFormHeader.setTelephone1(phoneNo);
			userFormHeader.setContactTelephone(phoneNo);
			userFormHeader.setApplicationType(getApplicationType(customerType
					.toUpperCase()));
			updateUserMemberRecords(userInDb, memberInDb, userFormHeader);
			return userInDb;
		}
	}

	private ApplicationType getApplicationType(String customerType) {
		if (customerType.equals("MEMBER")) {
			return ApplicationType.NON_PRACTISING;
		} else if (customerType.equals("PRACTICING RT")) {
			return ApplicationType.PRACTISING_RT;
		} else if (customerType.equals("PRAC MEMBER")) {
			return ApplicationType.PRACTISING;
		} else if (customerType.equals("FOREIGN")) {
			return ApplicationType.OVERSEAS;
		} else if (customerType.equals("RETIRED")) {
			return ApplicationType.RETIRED;
		} else {
			return ApplicationType.NON_PRACTISING;
		}
	}

	private void updateUserMemberRecords(User userInDb, Member memberInDb,
			ApplicationFormHeader userForm) {
		dao.save(memberInDb);

		logger.error(" ===>>><<<< === UPDATE MEMBER RECORDS ===>><<<>>== ");
		logger.error(" ===>>><<<< === MEMBER NO ===>><<<>>== "
				+ memberInDb.getMemberNo());

		ApplicationFormHeader userFormHeaderInDb = applicationFormDao
				.getApplicationByUserRef(userInDb.getRefId());

		if (userFormHeaderInDb != null) {
			userFormHeaderInDb.setMobileNo(userForm.getMemberNo());
			userFormHeaderInDb.setIdNumber(userForm.getIdNumber());
			userFormHeaderInDb.setEmail(userForm.getEmail());
			userFormHeaderInDb.setOtherNames(userForm.getOtherNames());
			userFormHeaderInDb
					.setApplicationDate(userForm.getApplicationDate());
			userFormHeaderInDb.setAddress1(userForm.getAddress1());
			userFormHeaderInDb.setTelephone1(userForm.getTelephone1());
			userFormHeaderInDb.setContactTelephone(userForm
					.getContactTelephone());
			userFormHeaderInDb
					.setApplicationType(userForm.getApplicationType());
			dao.save(userFormHeaderInDb);
		} else {
			User user = dao.findUserByEmail(userInDb.getEmail());
			userForm.setUserRefId(user.getRefId());
			dao.save(userForm);
		}

		sendActivationEmail2(userInDb);
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

	public List<OldTransactionDto> getTransactions(String userId) {

		List<Transaction> trxs = trxDao.getTransactions(userId);
		List<OldTransactionDto> trxDtos = new ArrayList<>();
		for (Transaction trx : trxs) {
			OldTransactionDto dto = trx.toDto();
			trxDtos.add(dto);
		}

		return trxDtos;
	}

	public LogInResult execLogin(LogInAction action) {
		UserDto userDto;
		boolean isLoggedIn = true;

		logger.debug("++++++ Log In Api Call ++++++++");
		ObjectMapper mapper = new ObjectMapper();
		try {
			logger.debug(mapper.writeValueAsString(action));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

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
			userDto.setMemberRefId(dao.getMemberRefId(userDto.getUserLongId()));

			if (userDto.getMemberNo() != null) {
				userDto.setGender(dao.getGender(userDto.getApplicationRefId()));
			}
		}

		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);
		logger.info("LogInHandlerexecut(): actiontype="
				+ action.getActionType());
		if (currentUserDto != null) {
			logger.info("LogInHandlerexecut(): currentUserDto="
					+ currentUserDto);
		}
		logger.info("LogInHandlerexecut(): loggedInCookie=" + loggedInCookie);
		// applicationFormDaoHelper.importMissingMembers(applicationFormDao.importMissingMembers());
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
		/*
		 * if (user.getMemberNo() != null && !user.getMemberNo().isEmpty()) {
		 * logger .info(
		 * " +++++++ Updating LMS password upon password change +++++++ " );
		 * 
		 * LMSResponse lmsRespone = null;
		 * 
		 * LMSPassWordDto dto = new LMSPassWordDto();
		 * dto.setMembershipID(user.getMemberNo());
		 * dto.setPassword(newPassword);
		 * 
		 * JSONObject jObject = new JSONObject(dto); try { lmsRespone =
		 * LMSIntegrationUtil.getInstance().executeLMSCall(
		 * "/Account/Updatepassword", jObject, String.class); } catch
		 * (IOException e) { e.printStackTrace(); }
		 * 
		 * if (lmsRespone != null) { if (lmsRespone.equals("Invalid User.")) {
		 * return; } }
		 * 
		 * }
		 */

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
				+ user.getFullName()
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
