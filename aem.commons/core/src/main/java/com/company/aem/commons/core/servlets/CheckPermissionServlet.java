package com.company.aem.commons.core.servlets;

import java.io.IOException;
import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 
 */
@SlingServlet(paths = "/bin/company/commons/checkPermission")
@Property(name = "sling.auth.requirements", value = "-/bin/company/commons/checkPermission")
public class CheckPermissionServlet extends SlingSafeMethodsServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CheckPermissionServlet.class);

	private static final long serialVersionUID = -3960692666512058180L;

	private static final String URI_PARAM = "uri";
	
	@Override
	protected void doHead(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		String uri = request.getParameter(URI_PARAM);
		LOGGER.info("URI to check : " + uri);
		
		
		if(uri != null) {
			String uriUtf = new String(uri.getBytes("ISO-8859-1"), "UTF-8").replace(".html", "");
			LOGGER.info("URI in UTF : " + uriUtf);
			
			Resource resource = request.getResourceResolver().getResource(uriUtf);
	        if (resource != null) {
	            response.setStatus(SlingHttpServletResponse.SC_OK);
	            LOGGER.info("Resource available and authorized for access");
	        } else {
	            response.setStatus(SlingHttpServletResponse.SC_UNAUTHORIZED);
	            LOGGER.info("Resource unavailable or not authorized for access");
	        }	
		} else {
			response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            LOGGER.info("No URI provided");
		}
	}

}
