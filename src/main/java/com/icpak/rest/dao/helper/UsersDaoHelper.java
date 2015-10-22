package com.icpak.rest.dao.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.icpak.rest.BaseResource;
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
import com.icpak.rest.models.membership.Member;
import com.icpak.rest.models.trx.Transaction;
import com.icpak.rest.models.util.Attachment;
import com.icpak.rest.security.authentication.AuthenticationException;
import com.icpak.rest.security.authentication.Authenticator;
import com.icpak.rest.util.ApplicationSettings;
import com.icpak.rest.utils.EmailServiceHelper;
import com.workpoint.icpak.server.integration.lms.LMSIntegrationUtil;
import com.workpoint.icpak.shared.lms.LMSMemberDto;
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

	private void sendActivationEmail(User user) {
		String subject = "Welcome to ICPAK Portal!";
		String link = settings.getApplicationPath() + "#activateacc;uid="
				+ user.getRefId();
		String body = "Dear User,"
				+ "<br/>An account has been created for you onthe ICPAK portal. "
				+ "You will need to reset your password on the portal using the following details."
				+ "<p/>Username: " + user.getEmail() + "<br/>Click this link "
				+ link + " to reset your account." + "<p>Thank you";

		try {
			EmailServiceHelper.sendEmail(body, subject,
					Arrays.asList(user.getEmail()),
					Arrays.asList(user.getUserData().getFullNames()));

		} catch (Exception e) {
			logger.warning("Activation Email for " + user.getEmail()
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

			// Update Member Records and Statements - Only update if the User is
			// a member;

			if (!userDto.isAdmin()) {
				try {
					memberDaoHelper
							.updateMemberRecord(userDto.getMemberRefId());
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		CurrentUserDto currentUserDto = new CurrentUserDto(isLoggedIn, userDto);

		logger.info("LogInHandlerexecut(): actiontype="
				+ action.getActionType());
		logger.info("LogInHandlerexecut(): currentUserDto=" + currentUserDto);
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
		dao.changePassword(user);

	}

	public String postUserToLMS(String userId) throws IOException {
		User user = dao.findByUserId(userId);
		LMSMemberDto dto = new LMSMemberDto();
		dto.setFirstName(user.getUserData().getFirstName());
		dto.setLastName(user.getUserData().getLastName());
		dto.setGender((user.getUserData().getGender() == Gender.MALE ? Gender.MALE
				.getCode() : Gender.FEMALE.getCode()));
		dto.setMobileNo(user.getPhoneNumber());
		dto.setPassword(user.getPassword());
		dto.setTimeZone("E. Africa Standard Time");
		dto.setTitle(Title.Mr.getCode());
		if (user.getUserData().getDob() != null) {
			dto.setDOB(new SimpleDateFormat("dd-mm-yyyy").format(user
					.getUserData().getDob()));
		} else {
			dto.setDOB(new SimpleDateFormat("dd-mm-yyyy").format(new Date()));
		}
		if (user.getMemberNo() != null) {
			dto.setUserName(user.getMemberNo() + "@wira.io");
			// dto.setUserName(user.getMemberNo());
			dto.setMembershipID(user.getMemberNo());
		} else if (user.getEmail() != null) {
			dto.setUserName(user.getEmail());
			dto.setMembershipID("");
		}
		dto.setRefID(user.getRefId());

		String response = LMSIntegrationUtil.getInstance().executeLMSCall(
				"/Account/Register", dto, String.class);
		// messages -for testing
		return response;
	}

	public void activateAccount(String userId, AccountStatus activated) {
		User user = dao.findByUserId(userId);
		user.setStatus(activated);
	}

	public String getApplicationRefId(String userRef) {
		return dao.getApplicationRefId(userRef);
	}

	public void resetAccount(String userId) {
		User user = dao.findByUserId(userId);

		String subject = "ICPAK Portal Email Reset";
		String resetUrl = settings.getApplicationPath() + "/#activateacc;uid="
				+ userId;

		String body = "Password Reset Instructions For: <br/>" + "<div>"
				+ "Name: " + user.getUserData().getFullNames()
				+ "<br/>Member No: " + user.getMemberNo() == null ? "N/A"
				: user.getMemberNo()
						+ "</div>"
						+ "<a href='"
						+ resetUrl
						+ "'>Reset Your Password</a> and follow onscreen instructions."
						+ "This email can be ignored if you did not request a password reset. The link is only "
						+ "available for a short time";

		try {
			EmailServiceHelper.sendEmail(body, subject,
					Arrays.asList(user.getEmail()),
					Arrays.asList(user.getUserData().getFirstName()));
		} catch (UnsupportedEncodingException | MessagingException e) {
			logger.warning("Send Reset Email Failed: email= " + user.getEmail()
					+ ", refId= " + user.getRefId());
			e.printStackTrace();

		}
	}

}
