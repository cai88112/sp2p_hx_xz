package com.fp2p.helper.alipay;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fp2p.helper.httpclient.HttpClientHelper;
import com.fp2p.helper.httpclient.NameValuePairHelper;
import com.shove.config.AlipayConfig;

/**
 * AlipayHttpHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class AlipayHttpHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(AlipayHttpHelper.class
			.getName());

	/**
	 * 私有的构造方法.
	 */
	private AlipayHttpHelper() {
	}

	/**
	 * 构造模拟远程HTTP的POST请求，获取支付宝的返回XML处理结果.
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param gateway
	 *            网关地址
	 * @return 支付宝返回XML格式的字符串结果
	 */
	public static String sendPostInfo(final Map<String, String> sParaTemp,
			final String gateway) {
		logger.debug("进入sendPostInfo方法.");
		String strResult = null;
		try {
			if (sParaTemp == null) {
				throw new NullPointerException("sParaTemp为空指针.");
			}
			if (sParaTemp.size() <= 0) {
				throw new Exception("sParaTemp为空.");
			}
			if (gateway == null) {
				throw new NullPointerException("gateway为空指针.");
			}

			// 待请求参数数组
			Map<String, String> sPara = AlipayMapHelper
					.buildRequestPara(sParaTemp);

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("METHOD_GET", "GET");
			map.put("METHOD_POST", "POST");
			/* 待请求的url */
			map.put("url", gateway + "_input_charset="
					+ AlipayConfig.input_charset);
			map.put("method", "POST");
			map.put("timeout", 0);
			map.put("connectionTimeout", 0);
			/* Post方式请求时组装好的参数值对 */
			map.put("parameters", NameValuePairHelper.mapToNameValuePair(sPara));
			/* Get方式请求时对应的参数 */
			map.put("queryString", null);
			/* 默认的请求编码方式 */
			map.put("charset", AlipayConfig.input_charset);
			/* 请求发起方的ip地址 */
			map.put("clientIp", null);

			byte[] byteResult = HttpClientHelper.executeForByte(map);
			if (byteResult != null) {
				strResult = new String(byteResult, AlipayConfig.input_charset);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出sendPostInfo方法.");
		return strResult;
	}
}
