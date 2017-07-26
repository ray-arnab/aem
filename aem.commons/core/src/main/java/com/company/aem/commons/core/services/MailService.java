package com.company.aem.commons.core.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.company.aem.commons.core.services.exceptions.ServiceException;

public interface MailService {

	/**
	 * Sends email to the <pre>mailToList</pre> based on the template available in <pre>templatePath</pre> 
	 * after replacing the place-holders matching keys in <pre>emailParams</pre>
	 * 
	 * @param emailParams
	 * @param templatePath
	 * @param mailAttachments
	 * @param mailToList
	 * @param mailFrom
	 * @param mailFromName
	 * @param subject
	 * @throws ServiceException
	 */
	public void sendMail(Map<String, String> emailParams, 
			String templatePath, 
			MailAttachment[] mailAttachments,
			List<String> mailToList, 
			String mailFrom,
			String mailFromName,
			String subject) throws ServiceException;

	/**
	 * Sends email to the recipient(s) based on the message
	 * 
	 * @param message
	 * @param mailAttachments
	 * @param mailToList
	 * @param mailFrom
	 * @param mailFromName
	 * @param subject
	 * @throws ServiceException
	 */
	public void sendMail(String message, 
			MailAttachment[] mailAttachments,
			List<String> mailToList, 
			String mailFrom,
			String mailFromName,
			String subject) throws ServiceException;
	
	
	public void sendMail(Map<String, String> emailParams, 
			String newsletterPath, 
			HttpServletRequest request,
			MailAttachment[] mailAttachments,
			List<String> mailToList, 
			String mailFrom,
			String mailFromName,
			String subject) throws ServiceException;
	
}
