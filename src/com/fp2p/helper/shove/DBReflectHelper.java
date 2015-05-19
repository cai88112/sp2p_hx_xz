package com.fp2p.helper.shove;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shove.data.dao.Table;

/**
 * 数据库映射工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class DBReflectHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(DBReflectHelper.class
			.getName());

	/**
	 * 数据库映射工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private DBReflectHelper() {
	}

	/**
	 * 将Map值设置进表对象中去.
	 *
	 * @param <T>
	 *            表类型
	 * @param table
	 *            表对象
	 * @param map
	 *            Map对象
	 */
	public static <T extends Table> void mapToTableValue(final T table,
			final Map<String, String> map) {
		logger.debug("进入mapToTableValue方法");
		try {
			if (table == null) {
				throw new NullPointerException("table为空指针");
			}
			Class<?> clazz = com.shove.data.dao.Field.class;
			Method setValueMethod = clazz.getMethod("setValue", Object.class);
			Class<?> tableClazz = table.getClass();
			Field[] fields = tableClazz.getDeclaredFields();
			for (Field f : fields) {
				String value = map.get(f.getName());
				if (value != null) {
					if (StringUtils.isNotBlank(value)) {
						value = StringEscapeUtils.escapeSql(value);
						Object field = f.get(table);
						if (field != null) {
							setValueMethod.invoke(field, value);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出mapToTableValue方法");
	}
}