package com.company.aem.commons.core.config.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.References;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.config.FactoryConfigService;
import com.company.aem.commons.core.config.MultiConfigService;

@Component(	immediate = true, metatype = false)
@References({
    @Reference(
            name = "FactoryConfigService",
            referenceInterface = FactoryConfigService.class,
            policy = ReferencePolicy.DYNAMIC,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE)
})
@Service
public class MultiConfigServiceImpl implements MultiConfigService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MultiConfigServiceImpl.class);
	
	private Map<String, Map<String, Object>> factoryConfigServices
		= new ConcurrentHashMap<String, Map<String, Object>>();

	protected final synchronized void bindFactoryConfigService(
			final FactoryConfigService configService,
			final Map<Object, Object> props) {

		LOGGER.debug("** New factory configuration to bind **");
		
		final String serviceName = PropertiesUtil.toString(
				props.get(FactoryConfigService.PROP_NAME), null);
        if (serviceName != null) {
        	LOGGER.debug("** Factory config type : {} **", serviceName);
        	
        	// A map of various factory configs
        	Map<String, Object> configServiceMap = factoryConfigServices.get(serviceName);
        	if(configServiceMap == null) {
        		configServiceMap = new HashMap<String, Object>();
        		factoryConfigServices.put(serviceName, configServiceMap);
        	}
        	
        	// A map of various factory items for the given factoryconfig
        	Map<String, Object> configMap = configService.getConfigMap();
    		if(configMap != null) {
    			String id = configService.getId();
    			LOGGER.debug("** New factory configuration for {} **" + id);
    			configServiceMap.put(id, configService.getConfigMap());
    		}
        } else {
        	LOGGER.warn("** Factory config missing {} **", FactoryConfigService.PROP_NAME);
        }
		
	}

	protected final synchronized void unbindFactoryConfigService(
			final FactoryConfigService configService,
			final Map<Object, Object> props) {
		
		LOGGER.debug("** Reving factory configuration **");
		
		final String serviceName = PropertiesUtil.toString(
				props.get(FactoryConfigService.PROP_NAME), null);
        if (serviceName != null) {
        	LOGGER.debug("** Factory config type : {} **", serviceName);
        	
        	// A map of various factory configs
        	Map<String, Object> configServiceMap = factoryConfigServices.get(serviceName);
        	
        	if(configServiceMap != null) {
        		// A map of various factory items for the given factoryconfig
            	Map<String, Object> configMap = configService.getConfigMap();
            	
            	if(configMap != null) {
        			String id = configService.getId();
        			LOGGER.debug("** Removing factory configuration for {} **" + id);
        			configMap.remove(id);
        		}
        	}
        } 
	}

	@Override
	public Map<String, Object> getConfigMap(String serviceName, String configId) {
		
		LOGGER.debug("** Getting factory configuration map for {} **" + serviceName);
		
		// Get the specific factory configurations map
    	Map<String, Object> configServiceMap = factoryConfigServices.get(serviceName);
    	if(configServiceMap != null) {
    		LOGGER.debug("** Getting factory item configuration map for {} **" + configId);
    		
    		// Get the specific factory item configuration map
        	Map<String, Object> configMap = (Map<String, Object>)configServiceMap.get(configId);
        	
        	return configMap;
    	}
    	
    	return null;
	}

	
	
	
}
