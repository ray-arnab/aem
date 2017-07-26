package com.company.aem.commons.core.models;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.config.MultiConfigService;
import com.company.aem.commons.core.config.impl.SEOOpenGraphFactoryConfig;
import com.company.aem.commons.core.utils.JCRHelper;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;

@Model(adaptables={
		SlingHttpServletRequest.class
		})
public class SEOOpenGraphModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(SEOOpenGraphModel.class);
	
	private static final String PROP_TYPE = "seo.meta.og.type";
	private static final String PROP_SITE = "seo.meta.og.site";
	private static final String PROP_TITLE = "seo.meta.og.title";
	private static final String PROP_DESC = "seo.meta.og.description";

	@Inject @Source("sling-object")
    private ResourceResolver resourceResolver;
	
	@Inject @Source("script-bindings")
    private Page currentPage;
	
	@Inject
	private MultiConfigService multiConfigService;
	
	private Map<String, Object> configMap;
	private String configId;
	private boolean enabled;
	private String imageUrl;
	private String url;
	private String locale;
	
	@Inject @Optional @Via("resource") @Named(PROP_TITLE)
	private String title;
	
	@Inject @Optional @Via("resource") @Named(PROP_DESC)
	private String description;
	
	@Inject @Optional @Via("resource") @Named(PROP_TYPE) 
	private String type;
	
	@Inject @Optional @Via("resource") @Named(PROP_SITE) 
	private String siteName;
	
	@Inject 
	SlingHttpServletRequest request;
	
	private Externalizer externalizer;
	
	@PostConstruct
    protected void init() {
		
		LOGGER.debug("** SEOOpenGraph : Post Construct init **");
		
		configId = currentPage.getAbsoluteParent(1).getName();
		if(configId != null) {
			configMap = multiConfigService.getConfigMap(SEOOpenGraphFactoryConfig.SERVICE_NAME, configId);
		} else {
			LOGGER.debug("** Cannot resolve configId **");
		}
		
		externalizer = resourceResolver.adaptTo(Externalizer.class);
    }
	

	public String getType() {
		LOGGER.debug("** Type : {} **", type);
		if (StringUtils.isBlank(type)) {
			type = (String)JCRHelper.getInheritedProperty(request.getResource(), PROP_TYPE, String.class);
			if(StringUtils.isBlank(type) && configMap != null) {
				type = (String)configMap.get(SEOOpenGraphFactoryConfig.PROP_OG_TYPE);
			}
        }
		LOGGER.debug("** Type : {} **", type);
		
		return type;
	}

	public String getSiteName() {
		LOGGER.debug("** Site : {} **", siteName);
		if (StringUtils.isBlank(siteName)) {
			// Try among inherited properties
			siteName = (String)JCRHelper.getInheritedProperty(request.getResource(), PROP_SITE, String.class);
			
			// Try the OSGI fallback
			if(StringUtils.isBlank(siteName) && configMap != null) {
				siteName = (String)configMap.get(SEOOpenGraphFactoryConfig.PROP_OG_SITE_NAME);
			}
		}
		LOGGER.debug("** Site : {} **", siteName);
		
		return siteName;
	}
 
	public String getTitle() {
		LOGGER.debug("** Title : {} **", title);
		if (StringUtils.isBlank(title)) {
			title = StringUtils.defaultIfEmpty(currentPage.getPageTitle(), currentPage.getTitle());
        }
		LOGGER.debug("** Title : {} **", title);
		
		return title;
	}

	public String getDescription() {
		if (StringUtils.isBlank(description)) {
            description = currentPage.getDescription();
        }
		
		return description;
	}

	public String getImageUrl() {
		if(configMap != null) {
			imageUrl = (String)configMap.get(SEOOpenGraphFactoryConfig.PROP_OG_IMG_URL);
			if(StringUtils.isBlank(imageUrl) && !imageUrl.equalsIgnoreCase("")) {
				imageUrl = externalizer.absoluteLink(request, request.getScheme(), imageUrl);
			}
		}
		return imageUrl;
	}

	public String getUrl() {
		url = externalizer.absoluteLink(request, request.getScheme(), resourceResolver.map(currentPage.getPath() + ".html"));
		return url;
	}

	public boolean isEnabled() {
		if(configMap != null) {
			enabled = (Boolean)configMap.get(SEOOpenGraphFactoryConfig.PROP_OG_ENABLED);
		}
		return enabled;
	}
	
	public String getLocale() {
		locale = currentPage.getLanguage(false).toString();
		return locale;
	}
}
