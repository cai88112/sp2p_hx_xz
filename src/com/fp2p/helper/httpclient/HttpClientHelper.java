/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package com.fp2p.helper.httpclient;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fp2p.httpcon.HttpConnectionPool;

/**
 * HttpClient方式访问,获取远程HTTP数据.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class HttpClientHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(HttpClientHelper.class
			.getName());

	/**
	 * DEFAULT_CHARSET默认编码格式.
	 */
	private static final String DEFAULT_CHARSET = "GBK";

	/**
	 * 连接超时时间，由bean factory设置，缺省为8秒钟.
	 */
	private static final int DEFAULT_CONNECTION_TIMEOUT = 8000;

	/**
	 * 回应超时时间, 由bean factory设置，缺省为30秒钟.
	 */
	private static final int DEFAULT_SO_TIMEOUT = 30000;

	/**
	 * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：3秒 .
	 **/
	private static final long DEFAULT_HTTPCONNECTIONMANAGER_TIMEOUT = 3000;

	/**
	 * HTTP连接管理器，该连接管理器必须是线程安全的，并用于获取连接.
	 */
	private static HttpConnectionManager connectionManager;

	/**
	 * 初始化http连接源.
	 */
	static {
		logger.info("开始初始化http连接源");
		connectionManager = new HttpConnectionPool();
		logger.info("结束初始化http连接源");
	}

	/**
	 * 私有构造器.
	 */
	private HttpClientHelper() {
	}

	/**
	 * 对method进行参数配置.
	 * 
	 * @param parmMap
	 *            method中的参数
	 * @return 设置好的method
	 */
	private static HttpMethod setExecuteMethod(final Map<String, Object> parmMap) {
		logger.debug("进入setExecuteMethod方法");
		HttpMethod method = null;
		try {
			if (parmMap == null) {
				throw new NullPointerException("parmMap为空指针");
			}

			String charset = (String) parmMap.get("charset");
			if (charset == null) {
				charset = DEFAULT_CHARSET;
			}
			
			String contentCharset = (String) parmMap.get("contentCharset");

			String reMethod = (String) parmMap.get("method");
			String reUrl = (String) parmMap.get("url");

			if (reMethod.equals("GET")) {
				method = new GetMethod(reUrl);
				method.getParams().setCredentialCharset(charset);
				if (contentCharset != null) {
					method.getParams().setContentCharset(contentCharset);  
				}
				String reqQueryString = (String) parmMap.get("queryString");
				// parseNotifyConfig会保证使用GET方法时，request一定使用QueryString
				method.setQueryString(reqQueryString);
			} else {
				method = new PostMethod(reUrl);
				NameValuePair[] reParameters = (NameValuePair[]) parmMap
						.get("parameters");
				((PostMethod) method).addParameters(reParameters);
				method.addRequestHeader("Content-Type",
						"application/x-www-form-urlencoded; text/html; charset="
								+ charset);
			}

			// 设置Http Header中的User-Agent属性
			method.addRequestHeader("User-Agent", "Mozilla/4.0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出setExecuteMethod方法");
		return method;
	}

	/**
	 * 对httpclient进行参数配置.
	 * 
	 * @param parmMap
	 *            httpclient中的参数
	 * @return 设置好的httpclient
	 */
	private static HttpClient setHttpClient(final Map<String, Object> parmMap) {
		logger.debug("进入setHttpClient方法");
		HttpClient httpclient = null;
		try {
			if (parmMap == null) {
				throw new NullPointerException("parmMap为空指针");
			}
			httpclient = new HttpClient(connectionManager);

			// 设置连接超时
			Integer connectionTimeout = (Integer) parmMap
					.get("connectionTimeout");
			if (connectionTimeout == null || connectionTimeout <= 0) {
				connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
			}
			httpclient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(connectionTimeout);

			// 设置回应超时
			Integer soTimeout = (Integer) parmMap.get("timeout");
			if (soTimeout == null || soTimeout <= 0) {
				soTimeout = DEFAULT_SO_TIMEOUT;
			}
			httpclient.getHttpConnectionManager().getParams()
					.setSoTimeout(soTimeout);

			// 设置等待ConnectionManager释放connection的时间
			httpclient.getParams().setConnectionManagerTimeout(
					DEFAULT_HTTPCONNECTIONMANAGER_TIMEOUT);

			// 设置Cookie策略
			Object cookiePolicy = parmMap.get("cookiePolicy");
			if (cookiePolicy != null && !"".equals(cookiePolicy)) {
				httpclient.getParams().setCookiePolicy((String) cookiePolicy);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出setHttpClient方法");
		return httpclient;
	}

	/**
	 * 执行Http请求,返回byte数组.
	 * 
	 * @param parmMap
	 *            httpclient以及method中的参数
	 * @return 请求返回的数据以字节数组形式展现
	 */
	public static byte[] executeForByte(final Map<String, Object> parmMap) {
		logger.debug("进入executeForByte方法");
		byte[] result = null;
		try {
			if (parmMap == null) {
				throw new NullPointerException("parmMap为空指针");
			}
			HttpMethod method = null;
			try {
				HttpClient httpclient = setHttpClient(parmMap);
				method = setExecuteMethod(parmMap);
				httpclient.executeMethod(method);
				result = method.getResponseBody();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				method.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出executeForByte方法");
		return result;
	}

	/**
	 * 执行Http请求,返回String.
	 * 
	 * @param parmMap
	 *            httpclient以及method中的参数
	 * @return 请求返回的数据以字符串形式展现
	 */
	public static String executeForString(final Map<String, Object> parmMap) {
		logger.debug("进入executeForString方法");
		String result = null;
		try {
			if (parmMap == null) {
				throw new NullPointerException("parmMap为空指针");
			}
			HttpMethod method = null;
			try {
				HttpClient httpclient = setHttpClient(parmMap);
				method = setExecuteMethod(parmMap);
				httpclient.executeMethod(method);
				result = method.getResponseBodyAsString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				method.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出executeForString方法");
		return result;
	}
	
	/**
	 * 
	 * @param parmMap
	 * @return
	 */
	public static String executeForStringbbs(final Map<String, Object> parmMap) {
		logger.debug("进入executeForString方法");
		String result = null;
		try {
			if (parmMap == null) {
				throw new NullPointerException("parmMap为空指针");
			}
			HttpMethod method = null;
			try {
				HttpClient httpclient = setHttpClient(parmMap);
				method = setExecuteMethod(parmMap);
				httpclient.executeMethod(method);
				result = method.getResponseBodyAsString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				method.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出executeForString方法");
		return result;
	}
	
	public static String executeForStringupdatepassword(final Map<String, Object> parmMap) {
		logger.debug("进入executeForString方法");
		String result = null;
		try {
			if (parmMap == null) {
				throw new NullPointerException("parmMap为空指针");
			}
			HttpMethod method = null;
			try {
				HttpClient httpclient = setHttpClient(parmMap);
				method = setExecuteMethod(parmMap);
				httpclient.executeMethod(method);
				result = method.getResponseBodyAsString();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				method.releaseConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出executeForString方法");
		return result;
	}
}
