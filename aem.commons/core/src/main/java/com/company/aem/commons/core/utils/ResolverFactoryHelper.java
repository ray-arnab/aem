package com.company.aem.commons.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;

public class ResolverFactoryHelper {

	public static final String READ_SERVICE = "jcrReadService";
	public static final String WRITE_SERVICE = "jcrWriteService";
	
	private ResolverFactoryHelper() {
		
	}

	public static ResourceResolver getReaderResourceResolver(
			ResourceResolverFactory resourceResolverFactory)
			throws LoginException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, READ_SERVICE);
		return resourceResolverFactory.getServiceResourceResolver(param);
	}

	public static ResourceResolver getWriterResourceResolver(
			ResourceResolverFactory resourceResolverFactory)
			throws LoginException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put(ResourceResolverFactory.SUBSERVICE, WRITE_SERVICE);
		return resourceResolverFactory.getServiceResourceResolver(param);
	}

}
