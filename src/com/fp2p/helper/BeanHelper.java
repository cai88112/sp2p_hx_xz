package com.fp2p.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * JavaBean工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class BeanHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(BeanHelper.class
			.getName());

	/**
	 * 用于将date对象转换为字符串的时间格式.
	 */
	public static final String DATE_FORMATE = "yyyy-MM-dd HH:mm:ss";

	/**
	 * JavaBean工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private BeanHelper() {
	}

	/**
	 * 将JavaBean转为Map.
	 * 
	 * @param bean
	 *            符合javabean标准的实体对象
	 * @return 与JavaBean对象对应的map
	 */
	public static Map<String, String> beanToMap(final Object bean) {
		logger.debug("进入beanToMap方法");
		Map<String, String> parm = null;
		try {
			if (bean == null) {
				throw new NullPointerException("bean为空指针");
			}
			BeanMap beanMap = new BeanMap(bean);
			parm = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			Iterator<Object> keyIterator = beanMap.keySet().iterator();
			String propertyName = null;
			while (keyIterator.hasNext()) {
				propertyName = (String) keyIterator.next();
				if (!propertyName.equals("class")) {
					parm.put(propertyName, parmToStr(beanMap.get(propertyName)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("进入beanToMap方法");
		return parm;
	}

	/**
	 * 将bean中的值转为对应的字符串.
	 * 
	 * @param parm
	 *            符合javabean中的值
	 * @return 值对应的字符串
	 */
	public static String parmToStr(final Object parm) {
//		logger.debug("进入parmToStr方法");
		String result = null;
		if (parm == null) {
			result = "";
		} else if (parm instanceof Number || parm instanceof Boolean) {
			result = parm.toString();
		} else if (parm instanceof Date) {
			result = new SimpleDateFormat(DATE_FORMATE).format(parm);
		} else {
			result = (String) parm;
		}
//		logger.debug("退出parmToStr方法");
		return result;
	}
}