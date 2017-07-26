package com.company.aem.commons.core.services.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.aem.commons.core.services.HttpService;
import com.company.aem.commons.core.services.exceptions.ServiceException;
import com.company.aem.commons.core.utils.HttpHelper;

@Component(immediate = true, metatype = false)
@Service
public class HttpServiceImpl implements HttpService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HttpServiceImpl.class);

	public static enum Method {
		GET, POST
	}

	@Override
	public String connect(String url, HttpServiceImpl.Method method,
			Map<String, String> requestHeaders,
			Map<String, Object> requestParams, List<Cookie> cookies)
			throws ServiceException {

		LOGGER.debug("Start of connect : " + url);

		String htmlResponseStr = "";
		HttpRequestBase httpRequest = null;
		HttpClient client = null;
		try {
			client = new DefaultHttpClient();
			switch (method) {
			case GET:
				httpRequest = new HttpGet(url);
				break;
			case POST:
				httpRequest = new HttpPost(url);
				break;
			}
			processHeaders(httpRequest, requestHeaders);
			processParams(httpRequest, requestHeaders);
			processCookies(client, cookies);
			HttpResponse httpResponse = client.execute(httpRequest);
			LOGGER.info("Received response :  " + httpResponse.getStatusLine());
			htmlResponseStr = EntityUtils.toString(httpResponse.getEntity());
			LOGGER.info("Response content :  " + htmlResponseStr);
		} catch (Exception ex) {
			LOGGER.info("Exception connecting to :  " + url);
			throw new ServiceException(ex);
		} finally {
			if (client != null) {
				client = null;
			}
			if (httpRequest != null) {
				httpRequest = null;
			}
		}

		LOGGER.debug("End of connect : " + url);
		return htmlResponseStr;
	}

	private void processHeaders(HttpRequestBase httpRequest,
			Map<String, String> requestHeaders) {
		if (requestHeaders != null) {
			for (String key : requestHeaders.keySet()) {
				httpRequest.setHeader(key, requestHeaders.get(key));
			}
		}
	}

	private void processParams(HttpRequestBase httpRequest,
			Map<String, String> requestParams) {
		if (requestParams != null) {
			HttpParams httpParams = new BasicHttpParams();
			for (String key : requestParams.keySet()) {
				httpParams.setParameter(key, requestParams.get(key));
			}
			httpRequest.setParams(httpParams);
		}
	}

	private void processCookies(HttpClient client, List<Cookie> cookies) {
		if (cookies != null) {
			CookieStore cookieStore = new BasicCookieStore();
			for (Cookie cookie : cookies) {
				BasicClientCookie cookie1 = new BasicClientCookie(
						cookie.getName(), HttpHelper.decodeURIComponent(cookie
								.getValue()));
				cookie1.setPath(cookie.getPath());
				cookie1.setDomain(cookie.getDomain());
				cookieStore.addCookie(cookie1);
				LOGGER.debug("Cookie : " + cookie.getName() + " : "
						+ HttpHelper.decodeURIComponent(cookie.getValue()) + " : "
						+ cookie.getPath() + " : " + cookie.getDomain());
			}
			if (client instanceof DefaultHttpClient) {
				((DefaultHttpClient) client).setCookieStore(cookieStore);
			}
		}
	}

}
