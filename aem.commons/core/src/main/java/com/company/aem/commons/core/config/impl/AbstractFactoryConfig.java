package com.company.aem.commons.core.config.impl;

import java.util.HashMap;
import java.util.Map;

import com.company.aem.commons.core.config.FactoryConfigService;


public abstract class AbstractFactoryConfig implements FactoryConfigService {

	protected final Map<String, Object> configMap = new HashMap<String, Object>();
	
	@Override
	public final Map<String, Object> getConfigMap() {
		return configMap;
	}
}
