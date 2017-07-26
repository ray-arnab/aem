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
import com.company.aem.commons.core.config.impl.SEOTwitterFactoryConfig;
import com.company.aem.commons.core.utils.JCRHelper;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;

@Model(adaptables={
		SlingHttpServletRequest.class
		})
public class SEOTwitterModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(SEOTwitterModel.class);

	private static final String PROP_CARD = "seo.meta.twitter.card";
	private static final String PROP_SITE = "seo.meta.twitter.site";
	private static final String PROP_TITLE = "seo.meta.twitter.title";
	private static final String PROP_DESC = "seo.meta.twitter.description";
	
	@Inject @Source("sling-object")
    private ResourceResolver resourceResolver;

	@Inject @Source("script-bindings")
    private Page currentPage;
	
	@Inject
	private MultiConfigService multiConfigService;
	
	@Inject @Optional @Via("resource") @Named(PROP_CARD) //@Source(InheritedPropertyInjector.INJECTOR_NAME)		
	private String card;
	
	@Inject @Optional @Via("resource") @Named(PROP_SITE) //@Source(InheritedPropertyInjector.INJECTOR_NAME)		
	private String site;
	
	@Inject @Optional @Via("resource") @Named(PROP_TITLE)
	private String title;
	
	@Inject @Optional @Via("resource") @Named(PROP_DESC)
	private String description;
	
	private Map<String, Object> configMap;
	private String configId;
	private boolean enabled;
	private String imageUrl;
	private String url;
	private Externalizer externalizer;
	
	@Inject 
	SlingHttpServletRequest request;
	
	@PostConstruct
    protected void init() {
		
		LOGGER.debug("** SEOTwitter : Post construct init **");
		
		configId = currentPage.getAbsoluteParent(1).getName();
		
		if(configId != null) {
			configMap = multiConfigService.getConfigMap(SEOTwitterFactoryConfig.SERVICE_NAME, configId);
		} else {
			LOGGER.debug("** Cannot resolve configId **");
		}
		
		externalizer = resourceResolver.adaptTo(Externalizer.class);
    }
	

	public String getCard() {
		LOGGER.debug("** Card : {} **", card);
		if (StringUtils.isBlank(card)) {
			card = (String)JCRHelper.getInheritedProperty(request.getResource(), PROP_CARD, String.class);
			if(StringUtils.isBlank(card) && configMap != null) {
				card = (String)configMap.get(SEOTwitterFactoryConfig.PROP_TW_CARD);
			}
        }
		LOGGER.debug("** Card : {} **", card);
		
		return card;
	}

	public String getSite() {
		LOGGER.debug("** Site : {} **", site);
		if (StringUtils.isBlank(site)) {
			// Try among inherited properties
			site = (String)JCRHelper.getInheritedProperty(request.getResource(), PROP_SITE, String.class);
			
			// Try the OSGI fallback
			if(StringUtils.isBlank(site) && configMap != null) {
				site = (String)configMap.get(SEOTwitterFactoryConfig.PROP_TW_SITE);
			}
		}
		LOGGER.debug("** Site : {} **", site);
		
		return site;
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
			imageUrl = (String)configMap.get(SEOTwitterFactoryConfig.PROP_TW_IMG_URL);
			if(StringUtils.isBlank(imageUrl) && !imageUrl.equalsIgnoreCase("")) {
				imageUrl = externalizer.absoluteLink(request, request.getScheme(), imageUrl);
				// TODO: project may use separate domain for static assets - does that take effect using resourceResolver.map()?
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
			enabled = (Boolean)configMap.get(SEOTwitterFactoryConfig.PROP_TW_ENABLED);
		}
		return enabled;
	}
}
