package com.company.aem.commons.core.models;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.config.MultiConfigService;
import com.company.aem.commons.core.config.impl.SEORobotsConfig;
import com.day.cq.wcm.api.Page;

@Model(adaptables={
		SlingHttpServletRequest.class
		})
public class SEORobotsModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(SEORobotsModel.class);
	
	private static final String PROP_CONTENT = "seo.meta.robots.content";
	
	private static final String DEFAUL_CONTENT = "index, follow";

	@Inject @Source("script-bindings")
    private Page currentPage;
	
	@Inject
	private MultiConfigService multiConfigService;
	
	private Map<String, Object> configMap;
	private String configId;
	
	private boolean enabled;
	
	@Inject @Optional @Via("resource") @Named(PROP_CONTENT)
	private String content;
	

	@PostConstruct
    protected void init() {
		
		LOGGER.debug("** SEO Robots post construct init **");
		
		configId = currentPage.getAbsoluteParent(1).getName();
		if(configId != null) {
			configMap = multiConfigService.getConfigMap(SEORobotsConfig.SERVICE_NAME, configId);
		} else {
			LOGGER.debug("** Cannot resolve configId **");
		}

    }
	

	public String getContent() {
		if(content == null) {
			content = DEFAUL_CONTENT;
		}
		return content;
	}

	public boolean isEnabled() {
		if(configMap != null) {
			enabled = (Boolean)configMap.get(SEORobotsConfig.PROP_ROBOTS_ENABLED);
		}
		return enabled;
	}
}
