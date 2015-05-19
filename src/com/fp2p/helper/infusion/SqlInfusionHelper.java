package com.fp2p.helper.infusion;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 防止sql注入.
 * 
 * @author 殷梓淞.
 * @since v1.0.0
 */
public final class SqlInfusionHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(SqlInfusionHelper.class
			.getName());

	/**
	 * 私有的构造方法.
	 */
	private SqlInfusionHelper() {
	}

	/**
	 * 过滤sql注入字符串.
	 * 
	 * @param input
	 *            过滤前字符串
	 * @return 过滤后字符串
	 */
	public static String filteSqlInfusion(final String input) {
		logger.debug("进入filteSqlInfusion方法");
		String result = "";
		if (input != null && input.trim() != "") {
			if (!StringUtils.isNumeric(input)) {
				result = input.replace("'", "’").replace("update", "ｕｐｄａｔｅ")
						.replace("drop", "ｄｒｏｐ").replace("delete", "ｄｅｌｅｔｅ")
						.replace("exec", "ｅｘｅｃ").replace("create", "ｃｒｅａｔｅ")
						.replace("execute", "ｅｘｅｃｕｔｅ")
						.replace("where", "ｗｈｅｒｅ")
						.replace("truncate", "ｔｒｕｎｃａｔｅ")
						.replace("insert", "ｉｎｓｅｒｔ");
			} else {
				result = input;
			}
		}
		logger.debug("退出filteSqlInfusion方法");
		return result;
	}
}
