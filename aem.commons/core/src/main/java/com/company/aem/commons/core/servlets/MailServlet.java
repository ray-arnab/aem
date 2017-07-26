package com.company.aem.commons.core.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import com.company.aem.commons.core.services.MailService;
import com.company.aem.commons.core.services.exceptions.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 
 */
@SlingServlet(paths = "/bin/company/commons/mail")
public class MailServlet extends SlingSafeMethodsServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(MailServlet.class);

	private static final long serialVersionUID = -3960692666512058180L;

	@Reference
	private MailService mailService;


	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		Map<String, String> emailParams = new HashMap<String, String>();
		emailParams.put("firstName", "Arnab");
		emailParams.put("lastName", "Ray");
		
		//String templatePath = "/etc/notification/email/company/sample.txt";
		String templatePath = "/content/campaigns/geometrixx-outdoors/e-mails/polar-googles-offer.html";
		String mailFrom = "ray.arnab.82@gmail.com";
		List<String> mailToList = new ArrayList<String>();
		mailToList.add("ray.arnab.82@gmail.com");
		
		try {
			//mailService.sendMail(emailParams, templatePath, null, mailToList, mailFrom, "Arnab Ray", "Company Commons");
			mailService.sendMail(emailParams, templatePath, request, null, mailToList, mailFrom, "Arnab Ray", "Company Commons");
		} catch (ServiceException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			e.printStackTrace(response.getWriter());
		}
		
		
//		try {
//			mailService.sendMail("Hi", null, mailToList, mailFrom, null, "Company Commons");
//		} catch (MailServiceException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			e.printStackTrace(response.getWriter());
//		}
	}

}
