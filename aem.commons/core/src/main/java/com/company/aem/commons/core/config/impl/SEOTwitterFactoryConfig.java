package com.company.aem.commons.core.config.impl;

import java.util.Dictionary;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.config.FactoryConfigService;

@Component(
	label = "Company Commons - Twitter Config Factory",
	description = "Site specific twitter meta proerties", 
	metatype = true,
	immediate = true,
	configurationFactory = true, 
	policy = ConfigurationPolicy.REQUIRE
)
@Properties({ 
	@Property(
			 label = "Service Name",
		     name = FactoryConfigService.PROP_NAME,
		     description = "This is to uniquely identify the service impl by name.",
		     value = SEOTwitterFactoryConfig.SERVICE_NAME,
		     propertyPrivate = true
		    ),
	@Property(
			name ="webconsole.configurationFactory.nameHint", 
			value = "Factory Item: {company.seo.twitter.id}"
			)
})
@Service
public class SEOTwitterFactoryConfig extends AbstractFactoryConfig{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SEOTwitterFactoryConfig.class);
	
	/* Constants */
	
	public static final String SERVICE_NAME = "seo-twitter-factory-config";

	private static final String LABEL_ENABLED = "Enabled";
	private static final String LABEL_CARD = "Card";
	private static final String LABEL_SITE = "Site";
	private static final String LABEL_IMAGE = "Image URL";
	private static final String DEFAULT_CARD_VALUE = "summary";
	
	
	
	/* OSGi Properties */
	
	@Property(label = "Config identifier", value = "company-commons", description = "An ID for the factory item. To apply this config on '/content/tenantX' and below, please enter 'tenantX'.")
	public static final String CONFIG_ID = "company.seo.twitter.id";
	
	
	@Property(label = LABEL_ENABLED,
            description = "Enable Twitter meta properties.",
            boolValue = true)
	public static final String PROP_TW_ENABLED = "company.seo.twitter.enabled";
	
    @Property(label = LABEL_SITE,
            description = "Twitter Site (eg: @CompanyName). Value configured here will be overridden by values configured in page properties.",
            value = "")
    public static final String PROP_TW_SITE = "company.seo.twitter.site";
    
    @Property(label = LABEL_CARD,
            description = "Twitter Card (eg: summary or summary_large_image). Value configured here will be overridden by values configured in page properties.",
            value = DEFAULT_CARD_VALUE)
    public static final String PROP_TW_CARD = "company.seo.twitter.card";
    
    @Property(label = LABEL_IMAGE,
            description = "Twitter ImageUrl (Configure a common Image URL)",
            value = "")
    public static final String PROP_TW_IMG_URL = "company.seo.twitter.imageUrl";
    
    /* OSGi Properties */
	
	
	@Activate
	protected void activate(ComponentContext componentContext){
		configure(componentContext.getProperties());
	}
	
	
	protected void configure(Dictionary<?, ?> properties) {
		
		String configId = PropertiesUtil.toString(properties.get(CONFIG_ID),"");
		
		configMap.put(CONFIG_ID, configId);

		boolean enabled = PropertiesUtil.toBoolean(properties.get(PROP_TW_ENABLED), true);
    	String site = PropertiesUtil.toString(properties.get(PROP_TW_SITE), "");
    	if(site.equals("")) {
    		enabled = false;
    	}
    	configMap.put(PROP_TW_ENABLED, enabled);
    	configMap.put(PROP_TW_SITE, site);
    	
    	configMap.put(PROP_TW_CARD, PropertiesUtil.toString(properties.get(PROP_TW_CARD),DEFAULT_CARD_VALUE));
    	
    	configMap.put(PROP_TW_IMG_URL, PropertiesUtil.toString(properties.get(PROP_TW_IMG_URL),""));
    	
    	
    	LOGGER.debug("** New Config for id {} **", configId);
	}

	
	@Override
	public String getId() {
		return (String)configMap.get(CONFIG_ID);
	}
	
}
