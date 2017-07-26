package com.company.aem.commons.core.services;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import com.company.aem.commons.core.services.exceptions.ServiceException;
import com.company.aem.commons.core.services.impl.HttpServiceImpl;

public interface HttpService {
	public String connect(String url, HttpServiceImpl.Method method,
			Map<String, String> requestHeaders,
			Map<String, Object> requestParams, 
			List<Cookie> cookies) throws ServiceException;
	
	
}
