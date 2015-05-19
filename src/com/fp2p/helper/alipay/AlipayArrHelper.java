/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package com.fp2p.helper.alipay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * AlipayArrHelper工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class AlipayArrHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(AlipayArrHelper.class
			.getName());

	/**
	 * 私有构造方法.
	 */
	private AlipayArrHelper() {
	}

	/**
	 * 除去数组中的空值和签名参数.
	 * 
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, String> paraFilter(
			final Map<String, String> sArray) {
		logger.debug("进入paraFilter方法");
		Map<String, String> result = null;
		try {
			if (sArray == null) {
				throw new NullPointerException("sArray为空指针");
			}
			result = new HashMap<String, String>();
			if (sArray != null && sArray.size() > 0) {
				for (String key : sArray.keySet()) {
					String value = sArray.get(key);
					if (value == null || value.equals("")
							|| key.equalsIgnoreCase("sign")
							|| key.equalsIgnoreCase("sign_type")) {
						continue;
					}
					result.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出paraFilter方法");
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串.
	 * 
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	protected static String createLinkString(final Map<String, String> params) {
		logger.debug("进入createLinkString方法");
		StringBuffer prestr = new StringBuffer();
		String result = null;
		try {
			if (params == null) {
				throw new NullPointerException("params为空指针");
			}
			// prestr = "";
			List<String> keys = new ArrayList<String>(params.keySet());
			Collections.sort(keys);

			for (String key : keys) {
				String value = params.get(key);
				prestr.append(key).append("=").append(value).append("&");
			}
			// prestr = prestr.substring(0, prestr.length() - 1);
			prestr.deleteCharAt(prestr.lastIndexOf("&"));
			result = prestr.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出createLinkString方法");
		return result;
	}
}
