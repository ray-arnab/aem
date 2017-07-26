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
	label = "Company Commons - OpenGraph Config Factory",
	description = "Site specific opengraph meta proerties", 
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
		     value = SEOOpenGraphFactoryConfig.SERVICE_NAME,
		     propertyPrivate = true
		    ),
	@Property(
			name ="webconsole.configurationFactory.nameHint", 
			value = "Factory Item: {company.seo.opengraph.id}"
			)
})
@Service
public class SEOOpenGraphFactoryConfig extends AbstractFactoryConfig{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SEOOpenGraphFactoryConfig.class);
	
	/* Constants */
	
	public static final String SERVICE_NAME = "seo-opengraph-factory-config";

	private static final String LABEL_ENABLED = "Enabled";
	private static final String LABEL_TYPE = "Type";
	private static final String LABEL_SITE_NAME = "Site_name";
	private static final String LABEL_IMAGE = "Image URL";
	private static final String DEFAULT_TYPE_VALUE = "website";
	
	
	
	/* OSGi Properties */
	
	@Property(label = "Config identifier", value = "company-commons", description = "An ID for the factory item. To apply this config on '/content/tenantX' and below, please enter 'tenantX'.")
	public static final String CONFIG_ID = "company.seo.opengraph.id";
	
	
	@Property(label = LABEL_ENABLED,
            description = "Enable OpenGraph meta properties",
            boolValue = true)
	public static final String PROP_OG_ENABLED = "company.seo.opengraph.enabled";
	
    @Property(label = LABEL_SITE_NAME,
            description = "OpenGraph Site Name (Eg: 'Company Name'). Value configured here will be overriden by values configured in page properties.",
            value = "")
    public static final String PROP_OG_SITE_NAME = "company.seo.opengraph.siteName";
    
    @Property(label = LABEL_TYPE,
            description = "OpenGraph Type (eg: website). Value configured here will be overriden by values configured in page properties.",
            value = DEFAULT_TYPE_VALUE)
    public static final String PROP_OG_TYPE = "company.seo.opengraph.type";
    
    @Property(label = LABEL_IMAGE,
            description = "OpenGraph ImageUrl (Configure a common Image URL)",
            value = "")
    public static final String PROP_OG_IMG_URL = "company.seo.opengraph.imageUrl";
    
    /* OSGi Properties */
	
	
	@Activate
	protected void activate(ComponentContext componentContext){
		configure(componentContext.getProperties());
	}
	
	
	protected void configure(Dictionary<?, ?> properties) {
		
		String configId = PropertiesUtil.toString(properties.get(CONFIG_ID),"");
		
		configMap.put(CONFIG_ID, configId);

    	configMap.put(PROP_OG_ENABLED, PropertiesUtil.toBoolean(properties.get(PROP_OG_ENABLED), true));

    	configMap.put(PROP_OG_SITE_NAME, PropertiesUtil.toString(properties.get(PROP_OG_SITE_NAME), ""));
    	
    	configMap.put(PROP_OG_TYPE, PropertiesUtil.toString(properties.get(PROP_OG_TYPE),DEFAULT_TYPE_VALUE));
    	
    	configMap.put(PROP_OG_IMG_URL, PropertiesUtil.toString(properties.get(PROP_OG_IMG_URL),""));
    	
    	LOGGER.debug("** New Config for id {} **", configId);
	}

	
	@Override
	public String getId() {
		return (String)configMap.get(CONFIG_ID);
	}
	
}
