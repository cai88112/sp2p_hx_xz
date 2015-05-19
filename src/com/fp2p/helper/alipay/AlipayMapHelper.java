package com.fp2p.helper.alipay;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shove.config.AlipayConfig;

/**
 * AlipayMapHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class AlipayMapHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(AlipayMapHelper.class
			.getName());

	/**
	 * 私有的构造方法.
	 */
	private AlipayMapHelper() {
	}

	/**
	 * 生成要请求给支付宝的参数map.
	 * 
	 * @param sParaTemp
	 *            请求前的参数
	 * @return 要请求的参数数组
	 */
	public static Map<String, String> buildRequestPara(
			final Map<String, String> sParaTemp) {
		logger.debug("进入buildRequestPara方法.");
		Map<String, String> sPara = null;
		try {
			if (sParaTemp == null) {
				throw new NullPointerException("sParaTemp为空指针.");
			}

			if (sParaTemp.size() > 0) {
				// 除去数组中的空值和签名参数
				sPara = AlipayArrHelper.paraFilter(sParaTemp);
				// 生成签名结果
				String mysign = AlipaySignHelper.buildMysign(sPara);

				// 签名结果与签名方式加入请求提交参数组中
				sPara.put("sign", mysign);
				sPara.put("sign_type", AlipayConfig.sign_type);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出buildRequestPara方法.");
		return sPara;
	}

}
