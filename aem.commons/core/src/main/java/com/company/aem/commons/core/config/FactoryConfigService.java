package com.company.aem.commons.core.config;

import java.util.Map;

public interface FactoryConfigService {
	
    public String PROP_NAME = "config-service-name";

	
	/**
	 * Method returns ID of a factory item for the given configuration factory
	 * Implementation should ensure they are unique between each of the items of a given factory
	 * 
	 * @return
	 */
	public String getId();
	
	/**
	 * Method returns a map of OSSI configurations for a factory item
	 * @return
	 */
	public Map<String, Object> getConfigMap();
	
}
