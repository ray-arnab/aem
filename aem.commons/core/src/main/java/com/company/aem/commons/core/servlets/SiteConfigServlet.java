package com.company.aem.commons.core.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.config.MultiConfigService;
import com.company.aem.commons.core.config.impl.SampleFactoryConfig;

/**
 * Utility servlet to fetch site specific configuration in JSON
 * Usage: /bin/company/commons/sites.<siteId>.do
 * @author 
 */
@SlingServlet(paths = "/bin/company/commons/sites",	extensions = "*")
public class SiteConfigServlet extends SlingSafeMethodsServlet {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SiteConfigServlet.class);

	private static final long serialVersionUID = -3960692666512058180L;

//	@Reference
//	private SampleFactoryConfigConsumer sampleConfig;
	
//	@Reference(target = "(label=SampleConfig)")
//	private MultiConfigService sampleConfig;
	
	@Reference
	private MultiConfigService multiConfigService;


	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServletException,
			IOException {
		response.setHeader("Content-Type", "application/json");
		response.setContentType("application/json ; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");  
		
		String uri = request.getRequestURI();
		LOGGER.debug("URI : " + uri);
		
		String[] tokens = uri.split("\\.");		
		LOGGER.debug("No of tokens : " + tokens.length);
		
		String siteId = tokens[1];
		LOGGER.debug("SiteID : " + siteId);
		
//		ResourceResolver resourceResolver = request.getResourceResolver();
//		PageManager pageManager = resourceResolver.adaptTo(PageManager.class);

		JSONObject jsonObject = new JSONObject();
		try {
			Map<String, Object> siteConfig = multiConfigService.getConfigMap(SampleFactoryConfig.SERVICE_NAME, siteId);
			for (String key : siteConfig.keySet()) {
				jsonObject.put(key, siteConfig.get(key));
			}
			response.getWriter().print(jsonObject.toString());
		} catch (Exception e) {
			LOGGER.error("Exception populating JSON", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().print("Exception populating JSON");
		}
	}

}
