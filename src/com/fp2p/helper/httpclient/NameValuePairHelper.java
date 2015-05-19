package com.fp2p.helper.httpclient;

import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * NameValuePairHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class NameValuePairHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager
			.getLogger(NameValuePairHelper.class.getName());

	/**
	 * 私有的构造方法.
	 */
	private NameValuePairHelper() {
	}

	/**
	 * 使用MAP中的参数填充NameValuePair.
	 * 
	 * @param properties
	 *            待填充的参数
	 * @return NameValuePair类型数组
	 */
	public static NameValuePair[] mapToNameValuePair(
			final Map<String, String> properties) {
		logger.debug("进入MapToNameValuePair方法.");
		NameValuePair[] nameValuePair = null;
		try {
			if (properties == null) {
				throw new NullPointerException("properties为空指针.");
			}

			if (properties.size() > 0) {
				nameValuePair = new NameValuePair[properties.size()];
				int i = 0;
				for (Map.Entry<String, String> entry : properties.entrySet()) {
					nameValuePair[i++] = new NameValuePair(entry.getKey(),
							entry.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出MapToNameValuePair方法.");
		return nameValuePair;
	}
}
