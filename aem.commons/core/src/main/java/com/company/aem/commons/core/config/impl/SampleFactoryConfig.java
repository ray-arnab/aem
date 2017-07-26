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
		configurationFactory = true, 
		policy = ConfigurationPolicy.REQUIRE, 
		metatype = true,
		immediate = true, 
		label = "Company Commons - Sample Configuration", 
		description = "Site specific sample configs")
@Properties({ 
	 @Property(
			 label = "Service Name",
		     name = FactoryConfigService.PROP_NAME,
		     description = "This is to uniquely identify the service impl by name.",
		     value = SampleFactoryConfig.SERVICE_NAME
		    ),
	@Property(
			name ="webconsole.configurationFactory.nameHint", 
			value = "Factory Item: {company.commons.sample.id}"
			)
})
@Service
public class SampleFactoryConfig extends AbstractFactoryConfig {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SampleFactoryConfig.class);
	
	// This is to uniquely identify the factory
	public static final String SERVICE_NAME = "sample-factory-config";

	// This is to uniquely identify a factory item
	@Property(label = "Config identifier", value = "siteA", description = "An ID for this configuration")
	public static final String CONFIG_ID = "company.commons.sample.id";
	
	@Property(label = "Site prefix", value = "/content/siteA", description = "Common content path prefix for this site")
	public static final String SITE_PREFIX = "company.commons.sample.contentPrefix";
	
	@Property(label = "DAM prefix", value = "/content/dam/siteA", description = "Common DAM path prefix for this site")
	public static final String DAM_PREFIX = "company.commons.sample.damPrefix";
	
	@Property(label = "Country level", intValue = {1}, description = "Level of country specific nodes for this site (/content being 0)")
	public static final String COUNTRY_LEVEL = "company.commons.sample.countryLevel";
	
	@Property(label = "Language level", intValue = {2}, description = "Level of language specific nodes for this site (/content being 0)")
	public static final String LANGUAGE_LEVEL = "company.commons.sample.languageLevel";

	
	@Activate
	protected void activate(ComponentContext componentContext){
		configure(componentContext.getProperties());
	}
	
	protected void configure(Dictionary<?, ?> properties) {
		
		String configId  = PropertiesUtil.toString(properties.get(CONFIG_ID),"");
		configMap.put(CONFIG_ID, configId);
		configMap.put(SITE_PREFIX, PropertiesUtil.toString(properties.get(SITE_PREFIX),""));
		configMap.put(DAM_PREFIX, PropertiesUtil.toString(properties.get(DAM_PREFIX),""));
		configMap.put(COUNTRY_LEVEL, PropertiesUtil.toInteger(properties.get(COUNTRY_LEVEL),0));
		configMap.put(LANGUAGE_LEVEL, PropertiesUtil.toInteger(properties.get(LANGUAGE_LEVEL),0));
	
		LOGGER.debug("** New Sample Config for id {} **", configId);
	}


	@Override
	public String getId() {
		return (String)configMap.get(CONFIG_ID);
	}
	
}
