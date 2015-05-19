package com.fp2p.helper.shuangqian;

import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fp2p.helper.MapHelper;

/**
 * ShuangqianSignHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class ShuangqianSignHelper {

	/**
	 * 私有的构造方法.
	 */
	private ShuangqianSignHelper() {
	}

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager
			.getLogger(ShuangqianSignHelper.class.getName());

	/**
	 * signMap方法.
	 * 
	 * @param md5Map
	 *            String[]类型
	 * @param securityKey
	 *            String类型
	 * @param type
	 *            String类型
	 * @return 返回String字符串.
	 */
	public static String signMap(final String[] md5Map,
			final String securityKey, final String type) {
		logger.debug("进入signMap方法.");
		String strBeforeMd5 = null;
		try {
			if (md5Map == null) {
				throw new NullPointerException("md5Map为空指针.");
			}
			if (securityKey == null) {
				throw new NullPointerException("securityKey为空指针.");
			}
			if (type == null) {
				throw new NullPointerException("type为空指针.");
			}
			if (type != "REQ" && type != "RES") {
				throw new NullPointerException("type不符合格式.");
			}
			if (md5Map.length > 0) {
				String[] md5ReqMap = new String[] { "MerNo", "BillNo",
						"Amount", "ReturnURL" };
				String[] md5ResMap = new String[] { "MerNo", "BillNo",
						"Amount", "Succeed" };
				Map<String, String> map = new TreeMap<String, String>();
				if (type.equals("REQ")) {
					for (int i = 0; i < md5ReqMap.length; i++) {
						map.put(md5ReqMap[i], md5Map[i]);
					}
				} else if (type.equals("RES")) {
					for (int i = 0; i < md5ResMap.length; i++) {
						map.put(md5ResMap[i], md5Map[i]);
					}
				}
				strBeforeMd5 = MapHelper.joinMapValue(map, '&')
						+ DigestUtils.md5Hex(securityKey);

				strBeforeMd5 = DigestUtils.md5Hex(strBeforeMd5);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出signMap方法.");
		return strBeforeMd5;

	}

}
