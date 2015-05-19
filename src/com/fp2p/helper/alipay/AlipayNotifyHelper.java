package com.fp2p.helper.alipay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shove.config.AlipayConfig;

/**
 * 支付宝通知处理类. 处理支付宝各接口通知
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class AlipayNotifyHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager
			.getLogger(AlipayNotifyHelper.class.getName());

	/**
	 * 支付宝通知验证地址.
	 */
	private static final String HTTPS_VERIFY_URL = "https:"
			+ "//mapi.alipay.com/gateway.do?service=notify_verify&";

	/**
	 * 私有的构造方法.
	 */
	private AlipayNotifyHelper() {
	}

	/**
	 * 验证消息是否是支付宝发出的合法消息.
	 * 
	 * @param params
	 *            通知返回来的参数数组
	 * @return 验证结果
	 */
	public static boolean verify(final Map<String, String> params) {
		logger.debug("进入verify方法.");
		boolean result = false;
		try {
			if (params == null) {
				throw new NullPointerException("params为空指针.");
			}
			String mysign = AlipaySignHelper.getMysign(params);
			String responseTxt = "true";
			if (params.get("notify_id") != null) {
				responseTxt = verifyResponse(params.get("notify_id"));
				String sign = "";
				if (params.get("sign") != null) {
					sign = params.get("sign");
					result = mysign.equals(sign) && responseTxt.equals("true");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出verify方法.");
		return result;
	}

	/**
	 * 获取远程服务器ATN结果,验证返回URL.
	 * 
	 * @param notifyId
	 *            通知校验ID
	 * @return 服务器ATN结果 验证结果集： invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空 true
	 *         返回正确信息 false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
	 */
	private static String verifyResponse(final String notifyId) {
		logger.debug("进入verifyResponse方法.");
		String inputLine = "";
		HttpURLConnection urlConnection = null;
		try {
			if (notifyId == null) {
				throw new NullPointerException("notifyId为空指针.");
			}

			// 获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
			String partner = AlipayConfig.partner;
			String veryfyUrl = HTTPS_VERIFY_URL + "partner=" + partner
					+ "&notify_id=" + notifyId;

			URL url = new URL(veryfyUrl);
			urlConnection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			inputLine = in.readLine().toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		logger.debug("退出verifyResponse方法.");
		return inputLine;
	}
}
