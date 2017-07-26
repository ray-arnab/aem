package com.company.aem.commons.core.services.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.ByteArrayDataSource;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.services.HttpService;
import com.company.aem.commons.core.services.MailAttachment;
import com.company.aem.commons.core.services.MailService;
import com.company.aem.commons.core.services.exceptions.ServiceException;
import com.company.aem.commons.core.utils.HttpHelper;
import com.company.aem.commons.core.utils.ResolverFactoryHelper;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;

@Component(immediate = true, metatype = false)
@Service
public class MailServiceImpl implements MailService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MailServiceImpl.class);
	
	private static final String AEM_LOGIN_COOKIE = "login-token";

	@Reference
	private MessageGatewayService messageGatewayService;

	@Reference
	private ResourceResolverFactory resolverFactory;
	
	@Reference
	private HttpService httpService;

	@Override
	public void sendMail(Map<String, String> emailParams, String templatePath,
			MailAttachment[] mailAttachments, List<String> mailToList,
			String mailFrom, String mailFromName, String subject)
			throws ServiceException {

		Session session = null;
		ResourceResolver resourceResolver = null;

		try {
			resourceResolver = ResolverFactoryHelper
					.getReaderResourceResolver(resolverFactory);
			LOGGER.debug("Created resourceResolver from factory");

			session = resourceResolver.adaptTo(Session.class);
			LOGGER.debug("Session created successfully");
			
			final MailTemplate mailTemplate = MailTemplate.create(templatePath,
					session);
			LOGGER.debug("MailTemplate created successfully from templatePath "
					+ templatePath);

			final HtmlEmail email = mailTemplate.getEmail(
					StrLookup.mapLookup(emailParams), HtmlEmail.class);
			LOGGER.debug("HtmlEmail created successfully substituting params");
			
			if (mailAttachments != null) {
				for (MailAttachment mailAttachment : mailAttachments) {
					attach(mailAttachment, email);
				}
			}
			LOGGER.debug("Attachments processed");

			if (mailFromName == null || mailFromName.equalsIgnoreCase("")) {
				email.setFrom(mailFrom);
			} else {
				email.setFrom(mailFrom, mailFromName);
			}
			if (mailToList != null && !mailToList.isEmpty()) {
				email.setTo(convertStringListToAddressList(mailToList));
			}
			if (subject != null && !subject.equalsIgnoreCase("")) {
				email.setSubject(subject);
			}
			LOGGER.debug("To/From/Subject processed");

			MessageGateway<HtmlEmail> messageGateway = messageGatewayService
					.getGateway(email.getClass());
			LOGGER.debug("Obtained handle to MessageGateway");

			messageGateway.send(email);
			LOGGER.debug("Email sent successfully");
		} catch (Exception e) {
			LOGGER.error("Exception sending email", e);
			throw new ServiceException(e);
		} finally {
			if (session != null) {
				session.logout();
			}
			if (resourceResolver != null && resourceResolver.isLive()) {
				resourceResolver.close();
			}
		}
	}

	@Override
	public void sendMail(String message, MailAttachment[] mailAttachments,
			List<String> mailToList, String mailFrom, String mailFromName,
			String subject) throws ServiceException {
		try {
			LOGGER.debug("Creating MultiPart email");

			MultiPartEmail email = new MultiPartEmail();

			if (mailFromName == null || mailFromName.equalsIgnoreCase("")) {
				email.setFrom(mailFrom);
			} else {
				email.setFrom(mailFrom, mailFromName);
			}
			email.setTo(convertStringListToAddressList(mailToList));
			if (subject != null && !subject.equalsIgnoreCase("")) {
				email.setSubject(subject);
			}
			email.setMsg(message);
			LOGGER.debug("To/From/Subject/Message processed");

			if (mailAttachments != null) {
				for (MailAttachment mailAttachment : mailAttachments) {
					attach(mailAttachment, email);
				}
			}
			LOGGER.debug("Attachments processed");

			MessageGateway<Email> messageGateway = messageGatewayService
					.getGateway(Email.class);
			LOGGER.debug("Obtained handle to MessageGateway");

			messageGateway.send(email);
			LOGGER.debug("Email sent successfully");
		} catch (Exception e) {
			LOGGER.error("Exception sending email", e);
			throw new ServiceException(e);
		} finally {
			//
		}

	}

	private void attach(MailAttachment emailAttachment, Email email)
			throws Exception {
		if (emailAttachment != null && emailAttachment.getFileName() != null
				&& emailAttachment.getFileName().equalsIgnoreCase("") == false) {
			InputStream is = emailAttachment.getByteArrayInputStream();

			LOGGER.debug("ContentType :: "
					+ emailAttachment.getContentType());
			LOGGER.debug("FileName :: "
					+ emailAttachment.getFileName());
			LOGGER.debug("InputStream size :: "
					+ is.available());

			ByteArrayDataSource ea = new ByteArrayDataSource(is,
					emailAttachment.getContentType());

			if (email instanceof MultiPartEmail) {
				((MultiPartEmail) email).attach(ea,
						emailAttachment.getFileName(),
						emailAttachment.getContentType());
			} else if (email instanceof HtmlEmail) {
				((HtmlEmail) email).attach(ea, emailAttachment.getFileName(),
						emailAttachment.getContentType());
			}
		}
	}

	private List<InternetAddress> convertStringListToAddressList(
			List<String> emailList) {

		List<InternetAddress> addressList = new ArrayList<InternetAddress>();
		for (String email : emailList) {
			try {
				InternetAddress address = new InternetAddress(email);
				addressList.add(address);
			} catch (AddressException e) {
				LOGGER.warn("Exception converting <" + email + ">", e);
			}
		}
		return addressList;
	}

	@Override
	public void sendMail(Map<String, String> emailParams,
			String newsletterPath, HttpServletRequest request,
			MailAttachment[] mailAttachments, List<String> mailToList,
			String mailFrom, String mailFromName, String subject)
			throws ServiceException {
		
		try {
			//UrlValidator defaultValidator = new UrlValidator(); // default schemes
			//if (!defaultValidator.isValid(newsletterPath)) {
				StringBuffer prefix = new StringBuffer();
				prefix.append(request.getScheme()).append("://").append(request.getServerName()).append(":").append(request.getLocalPort());
				LOGGER.debug("Calculated prefix : " + prefix);
				newsletterPath = prefix + (newsletterPath.startsWith("/") ? newsletterPath : "/" + newsletterPath);
			//}
			LOGGER.debug("Path : " + newsletterPath);
			
			List<Cookie> cookies = null;
			Cookie tokenCookie = HttpHelper.getCookie(AEM_LOGIN_COOKIE, request);
			if(tokenCookie != null) {
				cookies = new ArrayList<Cookie>();
				
				// These info about the cookie is not received in the server side
				// So they need to be explicitly set
				tokenCookie.setDomain(request.getServerName()); 
				tokenCookie.setPath("/");
				
				cookies.add(tokenCookie);
				LOGGER.debug("Cookie : " + AEM_LOGIN_COOKIE);
			}
			String responseStr = httpService.connect(newsletterPath, HttpServiceImpl.Method.GET, null, null, cookies);
			LOGGER.debug("Retrieved content : " + responseStr);

			final MultiPartEmail email = new MultiPartEmail();
			email.addPart(responseStr, "text/html; charset=UTF-8");
			if (mailAttachments != null) {
				for (MailAttachment mailAttachment : mailAttachments) {
					attach(mailAttachment, email);
				}
			}
			LOGGER.debug("Attachments processed");

			if (mailFromName == null || mailFromName.equalsIgnoreCase("")) {
				email.setFrom(mailFrom);
			} else {
				email.setFrom(mailFrom, mailFromName);
			}
			if (mailToList != null && !mailToList.isEmpty()) {
				email.setTo(convertStringListToAddressList(mailToList));
			}
			if (subject != null && !subject.equalsIgnoreCase("")) {
				email.setSubject(subject);
			}
			LOGGER.debug("To/From/Subject processed");

			MessageGateway<MultiPartEmail> messageGateway = messageGatewayService.getGateway(email.getClass());
			LOGGER.debug("Obtained handle to MessageGateway");

			messageGateway.send(email);
			LOGGER.debug("Email sent successfully");
		} catch (Exception e) {
			LOGGER.error("Exception sending email", e);
			throw new ServiceException(e);
		} finally {
			//
		}
		
	}
}
