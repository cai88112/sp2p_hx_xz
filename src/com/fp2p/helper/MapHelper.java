package com.fp2p.helper;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * MapHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class MapHelper {

	/**
	 * 私有的构造方法.
	 */
	private MapHelper() {
	}

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(MapHelper.class
			.getName());

	/**
	 * joinMapValue.
	 * 
	 * @param map
	 *            map类型
	 * @param connector
	 *            char类型
	 * @return 返回字符串.
	 */
	public static String joinMapValue(final Map<String, String> map,
			final char connector) {
		logger.debug("进入joinMapValue方法.");
		String res = null;
		try {
			if (map == null) {
				throw new NullPointerException("map为空指针.");
			}
			if (map.size() > 0) {
				StringBuffer b = new StringBuffer();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					b.append(entry.getKey());
					b.append('=');
					if (entry.getValue() != null) {
						b.append(entry.getValue());
					}
					b.append(connector);
				}
				res = b.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出joinMapValue方法.");
		return res;
	}

}
