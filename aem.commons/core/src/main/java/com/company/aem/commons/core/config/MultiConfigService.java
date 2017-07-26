package com.company.aem.commons.core.config;

import java.util.Map;

public interface MultiConfigService {
	
	/**
	 * Method returns the configuration map for a factory item 
	 * identified by <pre>configId</pre> of a factory configuration 
	 * service identified by <pre>serviceName</pre>
	 *  
	 * @param serviceName
	 * @param configId
	 * @return
	 */
	public Map<String, Object> getConfigMap(String serviceName, String configId);
	
}
