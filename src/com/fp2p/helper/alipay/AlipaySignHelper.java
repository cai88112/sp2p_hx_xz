/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package com.fp2p.helper.alipay;

import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shove.config.AlipayConfig;

/**
 * AlipaySignHelper工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class AlipaySignHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(AlipaySignHelper.class
			.getName());

	/**
	 * 私有的构造方法.
	 */
	private AlipaySignHelper() {

	}

	/**
	 * 根据反馈回来的信息，生成签名结果.
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 生成的签名结果
	 */
	public static String getMysign(final Map<String, String> params) {
		logger.debug("进入getMysign方法.");
		String mysign = null;
		try {
			if (params == null) {
				throw new NullPointerException("params为空指针.");
			}
			// 过滤空值、sign与sign_type参数
			Map<String, String> sParaNew = AlipayArrHelper.paraFilter(params);
			// 获得签名结果
			mysign = AlipaySignHelper.buildMysign(sParaNew);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getMysign方法.");
		return mysign;
	}

	/**
	 * 生成签名结果.
	 * 
	 * @param sArray
	 *            要签名的数组.
	 * @return 签名结果字符串.
	 */
	public static String buildMysign(final Map<String, String> sArray) {
		logger.debug("进入buildMysign方法.");
		String mysign = null;
		try {
			if (sArray == null) {
				throw new NullPointerException("sArray为空指针.");
			}

			if (sArray.size() > 0) {
				// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
				String prestr = AlipayArrHelper.createLinkString(sArray);
				// 把拼接后的字符串再与安全校验码直接连接起来
				prestr = prestr + AlipayConfig.key;
				mysign = DigestUtils.md5Hex(prestr
						.getBytes(AlipayConfig.input_charset));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出buildMysign方法");
		return mysign;
	}

}
