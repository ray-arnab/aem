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
	label = "Company Commons - Meta Robots Config Factory",
	description = "Site specific meta robots proerties", 
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
		     value = SEORobotsConfig.SERVICE_NAME,
		     propertyPrivate = true
		    ),
	@Property(
			name ="webconsole.configurationFactory.nameHint", 
			value = "Factory Item: {company.seo.robots.id}"
			)
})
@Service
public class SEORobotsConfig extends AbstractFactoryConfig{

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SEORobotsConfig.class);
	
	/* Constants */
	
	public static final String SERVICE_NAME = "seo-robots-factory-config";

	public static final String LABEL_ENABLED = "Enabled";
	
	
	/* OSGi Properties */
	
	@Property(label = "Config identifier", value = "company-commons", description = "An ID for the factory item. To apply this config on '/content/tenantX' and below, please enter 'tenantX'.")
	public static final String CONFIG_ID = "company.seo.robots.id";
	
	
	@Property(label = LABEL_ENABLED,
            description = "Enable Robots meta properties",
            boolValue = true)
	public static final String PROP_ROBOTS_ENABLED = "company.seo.robots.enabled";
	
    /* OSGi Properties */
	
	
	@Activate
	protected void activate(ComponentContext componentContext){
		configure(componentContext.getProperties());
	}
	
	
	protected void configure(Dictionary<?, ?> properties) {
		
		String configId = PropertiesUtil.toString(properties.get(CONFIG_ID),"");
		
		configMap.put(CONFIG_ID, configId);

    	configMap.put(PROP_ROBOTS_ENABLED, PropertiesUtil.toBoolean(properties.get(PROP_ROBOTS_ENABLED), true));

    	LOGGER.debug("** New Config for id {} **", configId);
	}

	
	@Override
	public String getId() {
		return (String)configMap.get(CONFIG_ID);
	}
	
}
