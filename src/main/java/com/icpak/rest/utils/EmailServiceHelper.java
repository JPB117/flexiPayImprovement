package com.icpak.rest.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.icpak.rest.models.auth.User;
import com.icpak.rest.models.util.Attachment;
import com.icpak.servlet.swagger.SwaggerApiServlet;

public class EmailServiceHelper {

	static Session session = null;
	static Properties props;
	int count = 0;

	static Logger log = Logger.getLogger(EmailServiceHelper.class);

	public static Properties getProperties() {
		initProperties();
		return props;
	}

	public static void initProperties() {
		if (session != null) {
			return;
		}
		try {
			props = new Properties();
			Properties sysProps = new Properties();
			sysProps.load(EmailServiceHelper.class.getClassLoader().getResourceAsStream("bootstrap.properties"));
			Object auth = sysProps.get("mail.smtp.auth");
			Object host = sysProps.get("mail.smtp.host");
			Object password = sysProps.get("mail.smtp.password");
			Object account = sysProps.get("mail.smtp.from");
			Object port = sysProps.get("mail.smtp.port");
			Object protocol = sysProps.get("mail.transport.protocol");
			Object starttls = sysProps.get("mail.smtp.starttls.enable");
			Object organizationName = sysProps.get("organization.name");
			
			props.put("mail.smtp.auth",auth);
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.password", password);
			props.put("mail.smtp.from", account);
			props.put("mail.smtp.port", port);
			props.put("mail.transport.protocol", protocol);
			props.put("mail.smtp.starttls.enable", starttls);
			props.put("organization.name", organizationName);

			for (Object prop : props.keySet()) {
				if (prop.equals("mail.smtp.password")) {
					log.debug(prop + " : xxxxxxxxx");
				} else {
					log.debug(prop + " : " + props.getProperty(prop.toString()));
				}

			}
			session = Session.getInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(props
							.getProperty("mail.smtp.from"), props
							.getProperty("mail.smtp.password"));
				}

			});

		} catch (Exception e) {
			log.warn("EmailServiceHelper.initProperties failed to initialize: "
					+ e.getMessage());
			e.printStackTrace();
		}

	}

	public static String getProperty(String name) {
		String val = getProperties().getProperty(name);
		return val;
	}

	public static void sendEmail(String body, String subject,
			List<String> emails, List<String> recipientNames, Attachment...attachments)
			throws MessagingException, UnsupportedEncodingException {
		initProperties();
		assert session != null;
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(getProperties().getProperty(
				"mail.smtp.from"), props.getProperty("organization.name") == null ? "WIRA BPMS" : props
				.getProperty("organization.name")));

		InternetAddress dests[] = new InternetAddress[emails.size()];
		for (int i = 0; i < emails.size(); i++) {
			String email = emails.get(i);
			String fullName = recipientNames.get(i);
			assert emails.get(i) != null;
			log.debug("Recipient : " + email + " : "
					+ fullName);
			dests[i] = new InternetAddress(email,
					fullName);
		}

		message.setRecipients(Message.RecipientType.TO, dests);
		message.setSubject(subject, "UTF-8");

		try {
			Multipart multipart = new MimeMultipart();
			// HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			String rootFolder = "com/icpak/rest/utils";

			{
				MimeBodyPart part = getBodyType(null, "<imageLogo>",
						rootFolder + "/logo.png");
				if (part != null) {
					multipart.addBodyPart(part);
				}
			}

			if (attachments != null) {
				for(Attachment a: attachments){
					MimeBodyPart part = getBodyType(a.getAttachment(), "","");
					part.setDescription(a.getName());
					part.setFileName(a.getName());
					
					if (part != null) {
						multipart.addBodyPart(part);
					}
				}
			} else {
				MimeBodyPart part = getBodyType(null, "<imageUser>",
						rootFolder + "/blueman(small).png");
				if (part != null) {
					multipart.addBodyPart(part);
				}
			}

			message.setContent(multipart);
			message.setSentDate(new java.util.Date());

			assert message != null;
			log.debug("Sending ........");
			Transport.send(message);
			log.debug("Email Successfully send........");
		} catch (Exception e) {
			log.fatal("Could not send email: " + subject + ": Cause "
					+ e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Deprecated Use {@link #sendEmail(String, String, List, HTUser)}
	 * 
	 * @param body
	 * @param subject
	 * @param recipient
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	public static void sendEmail(String body, String subject, String recipient)
			throws MessagingException, UnsupportedEncodingException {
		sendEmail(body, subject, recipient, null);
	}

	/**
	 * Deprecated Use {@link #sendEmail(String, String, List, HTUser)}
	 * 
	 * @param body
	 * @param subject
	 * @param recipient
	 * @param initiatorId
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
	@Deprecated
	public static void sendEmail(String body, String subject, String recipient,
			String initiatorId) throws MessagingException,
			UnsupportedEncodingException {

		initProperties();
		assert session != null;
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress("WIRA BPM", getProperties()
				.getProperty("mail.smtp.from")));

		String[] emails = recipient.split(",");
		InternetAddress dests[] = new InternetAddress[emails.length];
		for (int i = 0; i < emails.length; i++) {
			dests[i] = new InternetAddress(emails[i].trim().toLowerCase());
		}
		message.setRecipients(Message.RecipientType.TO, dests);
		message.setSubject(subject, "UTF-8");

		try {
			Multipart multipart = new MimeMultipart();
			// HTML
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(body, "text/html;charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			String rootFolder = "com/duggan/workflow/server/helper/email";

			{
				MimeBodyPart part = getBodyType(null, "<imageLogo>",
						rootFolder + "/logo.png");

				if (part != null)
					multipart.addBodyPart(part);
			}

			if (initiatorId != null) {
				MimeBodyPart part = getBodyType(null, "<imageUser>", rootFolder
						+ "/blueman(small).png");

				if (part != null)
					multipart.addBodyPart(part);

			} else {
				MimeBodyPart part = getBodyType(null, "<imageUser>", rootFolder
						+ "/blueman(small).png");

				if (part != null)
					multipart.addBodyPart(part);

			}

			message.setContent(multipart);
			message.setSentDate(new java.util.Date());

			assert message != null;
			log.warn("Sending ........");
			Transport.send(message);
			log.warn("Email Successfully send........");
		} catch (Exception e) {
			log.fatal("Could not send email: " + subject + ": Cause "
					+ e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static MimeBodyPart getBodyType(byte[] attachment, String imageId,
			String fallbackImageName) throws IOException, MessagingException {
		// Image
		MimeBodyPart imageBodyPart = new MimeBodyPart();
		DataSource fds = null;
		if (attachment != null) {
			fds = new ByteArrayDataSource(attachment, "image/png");
		} else {
			InputStream imageStream = EmailServiceHelper.class.getClass()
					.getResourceAsStream("/" + fallbackImageName);
			assert imageStream != null;

			try {
				fds = new ByteArrayDataSource(IOUtils.toByteArray(imageStream),
						"image/png");
			} catch (Exception e) {
			}

			assert fds != null;
		}

		if (fds == null) {
			return null;
		}
		imageBodyPart.setDataHandler(new DataHandler(fds));
		imageBodyPart.setHeader("Content-ID", imageId);

		return imageBodyPart;

	}

}
