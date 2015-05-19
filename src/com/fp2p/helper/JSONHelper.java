package com.fp2p.helper;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

/**
 * Json工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class JSONHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(JSONHelper.class
			.getName());

	/**
	 * 私有的构造方法.
	 */
	private JSONHelper() {
	}

	/**
	 * 将对象以json格式返回给客户端.
	 * 
	 * @param obj
	 *            待返回的对象
	 */
	public static void printObject(final Object obj) {
		logger.debug("进入printObject方法");
		JSONObject jsObject = JSONObject.fromObject(obj);
		ServletHelper.returnStr(ServletActionContext.getResponse(),
				jsObject.toString());
		logger.debug("退出printObject方法");
	}

	/**
	 * 将集合以json格式返回给客户端.
	 * 
	 * @param list
	 *            待返回的集合
	 */
	public static void printArray(final List<?> list) {
		logger.debug("进入printArray方法");
		JSONArray jsArray = JSONArray.fromObject(list);
		ServletHelper.returnStr(ServletActionContext.getResponse(),
				jsArray.toString());
		logger.debug("退出printArray方法");
	}

	/**
	 * 根据key获取json对象中的值.
	 * 
	 * @param json
	 *            json对象
	 * @param key
	 *            key
	 * @return json对象中与key对应的值
	 */
	public static String getString(final JSONObject json, final String key) {
		logger.debug("进入getString方法");
		String result = null;
		if (json.has(key)) {
			result = json.getString(key);
		}
		logger.debug("退出getString方法");
		return result;
	}
}
