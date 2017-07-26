package com.company.aem.commons.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HttpHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpHelper.class);
	
	private HttpHelper() {
		
	}
	
	/**
	 * Method retrieves a cookie from the HttpServletRequest
	 * @param cookieName
	 * @param request
	 * @return
	 */
	public static Cookie getCookie(final String cookieName, final HttpServletRequest request) {
		if(request == null) {
			throw new IllegalArgumentException("HttpServletRequest is null");
		}
		if(cookieName == null || cookieName.length() == 0) {
			throw new IllegalArgumentException("Cookie name is null or empty");
		}
		
		Cookie[] cookies = request.getCookies();	    
	    if (cookies != null) {
	        for(int i = 0; i < cookies.length; i++) {
	            if (cookies[i].getName().equals(cookieName)) {
	            	return cookies[i];
	            }
	        }
	    }
	    return null;
	}
	
	/**
	 * 
	 * @param cookieName
	 * @param cookieValue
	 * @param cookieExpiry
	 * @param cookiePath
	 * @param response
	 */
	public static void setCookie(final String cookieName, final String cookieValue, final Integer cookieExpiry, final String cookiePath, final HttpServletResponse response) {
		
		if(response == null) {
			throw new IllegalArgumentException("HttpServletResponse is null");
		}
		if(response.isCommitted()) {
			throw new IllegalStateException("Response is committed");
		}
		if(cookieName == null || cookieName.length() == 0) {
			throw new IllegalArgumentException("Cookie name is null or empty");
		}
		Cookie newCookie = new Cookie(cookieName, encodeURIComponent(cookieValue));
		
		if(cookieExpiry != null) {
			newCookie.setMaxAge(cookieExpiry);
		} 
		if(cookiePath != null) {
			newCookie.setPath(cookiePath);
		}
	    response.addCookie(newCookie);	    
	}
	
	
	public static String encodeURIComponent(String s){
		
		try {
			return URLEncoder.encode(s,"UTF-8")
			.replaceAll("\\+", "%20")
			.replaceAll("\\%21", "!")
			.replaceAll("\\%27", "'")
			.replaceAll("\\%28", "(")
			.replaceAll("\\%29", ")")
			.replaceAll("\\%7E", "~");
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("encodeURIComponent : " + e);
		}
		return s;
	}
	
	public static String encodeURI(String s) {
        return encodeURIComponent(s)
            .replaceAll("%3A", ":")
            .replaceAll("%2F", "/")
            .replaceAll("%3F", "?")
            .replaceAll("%3D", "=")
            .replaceAll("%26", "&");
	}
	
	public static String decodeURIComponent(String s){
		
		try {
			return URLDecoder.decode(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOGGER.warn("decodeURIComponent : " + e);
		}
		return s;
	}
}
