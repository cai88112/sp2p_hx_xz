package com.fp2p.helper;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

/**
 * ServletHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class ServletHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(ServletHelper.class
			.getName());

	/**
	 * 80端口.
	 */
	private static final int PORT_OF_80 = 80;

	/**
	 * 私有的构造方法.
	 */
	private ServletHelper() {
	}

	/**
	 * 获取根目录.
	 * 
	 * @return 返回根目录的路径.
	 */
	public static String serverRootDirectory() {
		logger.debug("进入serverRootDirectory方法.");
		String result = ServletActionContext.getServletContext().getRealPath(
				File.separator);
		logger.debug("退出serverRootDirectory方法.");
		return result;
	}

	/**
	 * 获取请求地址.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 返回请求地址 .
	 */
	public static String serverUrl(final HttpServletRequest request) {
		logger.debug("进入serverUrl方法.");
		String strRes = null;
		try {
			if (request == null) {
				throw new NullPointerException("request为空指针.");
			}
			StringBuffer res = new StringBuffer();
			String temp = "";
			if (request.getServerPort() == PORT_OF_80) {
				temp = "";
			} else {
				temp = ":" + request.getServerPort();
			}
			res.append(request.getScheme()).append("://")
					.append(request.getServerName()).append(temp)
					.append(request.getContextPath()).append("/");
			strRes = res.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出serverUrl方法.");

		return strRes;
	}

	/**
	 * 获取当前请求的URL地址域参数.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 返回当前请求的URL地址域参数.
	 */
	public static Map<String, Object> getPrams(final HttpServletRequest request) {
		logger.debug("进入getPrams方法.");
		Map<String, Object> params = null;
		try {
			if (request == null) {
				throw new NullPointerException("request为空指针.");
			}

			@SuppressWarnings("unchecked")
			Map<String, String[]> requestParams = request.getParameterMap();
			if (requestParams.size() > 0) {
				params = new HashMap<String, Object>();
				Iterator<String> iter = requestParams.keySet().iterator();
				while (iter.hasNext()) {
					String name = iter.next();
					String[] values = requestParams.get(name);
					StringBuffer valueStr = new StringBuffer();
					for (int i = 0; i < values.length; i++) {
						valueStr.append("values[i]").append(",");
					}
					valueStr.deleteCharAt(valueStr.lastIndexOf(","));
					params.put(name, valueStr.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getPrams方法.");
		return params;
	}

	/**
	 * getIpAddress获取Ip地址.
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 返回Ip地址.
	 */
	public static String getIpAddress(final HttpServletRequest request) {
		logger.debug("进入getIpAddress方法.");
		String ip = null;
		try {
			if (request == null) {
				throw new NullPointerException("request为空指针.");
			}

			ip = request.getHeader("x-forwarded-for");
			String localIP = "127.0.0.1";
			if (isUsedIp(ip, localIP)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (isUsedIp(ip, localIP)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (isUsedIp(ip, localIP)) {
				ip = request.getRemoteAddr();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getIpAddress方法.");
		return ip;
	}

	/**
	 * isUsedIp方法.
	 * 
	 * @param ip
	 *            String
	 * @param localIP
	 *            "127.0.0.1"
	 * @return 返回boolean类型结果，result为true时表示ip需要重新赋值.
	 */
	private static boolean isUsedIp(final String ip, final String localIP) {
		logger.debug("进入isUsedIp方法.");
		boolean result = false;
		try {
			if (localIP == null) {
				throw new NullPointerException("localIP为空指针.");
			}

			result = (ip == null) || (ip.length() == 0)
					|| (ip.equalsIgnoreCase(localIP))
					|| "unknown".equalsIgnoreCase(ip);

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("退出isUsedIp方法.");
		return result;
	}

	/**
	 * @Description: 将字符串以json格式输出
	 * @Author Yang Cheng
	 * @Date: Feb 9, 2012 1:53:02 AM
	 * @param jsonStr
	 * @throws IOException
	 * @return void
	 */
	/**
	 * 向客户端返回字符串.
	 * 
	 * @param response
	 *            HttpServletResponse
	 * @param outputStr
	 *            返回的字符串
	 */
	public static void returnStr(final HttpServletResponse response,
			final String outputStr) {
		try {
			if (response == null) {
				throw new NullPointerException("response为空指针.");
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/x-json;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.print(outputStr);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
