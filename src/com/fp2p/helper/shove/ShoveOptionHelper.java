package com.fp2p.helper.shove;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;
import com.shove.Convert;

/**
 * 富壹代配置工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class ShoveOptionHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(ShoveOptionHelper.class
			.getName());

	/**
	 * 富壹代配置工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private ShoveOptionHelper() {
	}

	/**
	 * 获得Option表中指定Key的Value.
	 *
	 * @param key
	 *            Key值
	 * @return Option表中指定Key的Value
	 */
	public static String getOption(final String key) {
		logger.debug("进入getOption方法");
		String value = null;
		Connection conn = null;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}

			conn = MySQL.getConnection();
			value = Dao.Functions.f_getoption(conn, key).trim();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		logger.debug("退出getOption方法");
		return value;
	}

	/**
	 * 获得Option表中指定Key的Value,并转型成为String.
	 *
	 * @param key
	 *            Key值
	 * @param defaultValue
	 *            key对应的默认值
	 * @return Option表中指定Key的Value,并转型成为String
	 */
	public static String getOptionOnString(final String key,
			final String defaultValue) {
		logger.debug("进入getOptionOnString方法");
		String value = null;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}

			value = getOption(key);
			if (value == null) {
				value = defaultValue;
			} else {
				value = Convert.strToStr(value, defaultValue).trim();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getOptionOnString方法");
		return value;
	}

	/**
	 * 获得Option表中指定Key的Value,并转型成为int.
	 *
	 * @param key
	 *            Key值
	 * @param defaultValue
	 *            key对应的默认值
	 * @return Option表中指定Key的Value,并转型成为int
	 */
	public static int getOptionAsInt(final String key, final int defaultValue) {
		logger.debug("进入getOptionAsInt方法");
		int result = 0;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}

			String value = getOption(key);
			result = defaultValue;
			if (value == null) {
				result = defaultValue;
			} else {
				result = Convert.strToInt(value, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getOptionAsInt方法");
		return result;
	}

	/**
	 * 获得Option表中指定Key的Value,，并转型成为long.
	 *
	 * @param key
	 *            Key值
	 * @param defaultValue
	 *            key对应的默认值
	 * @return Option表中指定Key的Value,并转型成为long
	 */
	public static long getOptionsAsLong(final String key,
			final long defaultValue) {
		logger.debug("进入getOptionsAsLong方法");
		long result = 0;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}
			String value = getOption(key);
			result = defaultValue;
			if (value == null) {
				result = defaultValue;
			} else {
				result = Convert.strToLong(value, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getOptionsAsLong方法");
		return result;
	}

	/**
	 * 获得Option表中指定Key的Value,，并转型成为double.
	 *
	 * @param key
	 *            Key值
	 * @param defaultValue
	 *            key对应的默认值
	 * @return Option表中指定Key的Value,并转型成为double
	 */
	public static double getOptionsAsDouble(final String key,
			final double defaultValue) {
		logger.debug("进入getOptionsAsDouble方法");
		double result = 0;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}
			String value = getOption(key);
			result = defaultValue;
			if (value == null) {
				result = defaultValue;
			} else {
				result = Convert.strToDouble(value, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getOptionsAsDouble方法");
		return result;
	}

	/**
	 * 获得Option表中指定Key的Value,，并转型成为Boolean.
	 *
	 * @param key
	 *            Key值
	 * @param defaultValue
	 *            key对应的默认值
	 * @return Option表中指定Key的Value,并转型成为Boolean
	 */
	public static Boolean getOptionsAsBoolean(final String key,
			final Boolean defaultValue) {
		logger.debug("进入getOptionsAsBoolean方法");
		Boolean result = false;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}
			String value = getOption(key);
			result = defaultValue;
			if (value == null) {
				result = defaultValue;
			} else {
				result = Convert.strToBoolean(value, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getOptionsAsBoolean方法");
		return result;
	}

	/**
	 * 获得Option表中指定Key的Value,，并转型成为DateTime.
	 *
	 * @param key
	 *            Key值
	 * @param defaultValue
	 *            key对应的默认值
	 * @param format
	 *            日期格式
	 * @return Option表中指定Key的Value,并转型成为DateTime
	 */
	public static Date getOptionsAsDateTime(final String key,
			final Date defaultValue, final String format) {
		logger.debug("进入getOptionsAsDateTime方法");
		Date result = null;
		try {
			if (key == null) {
				throw new NullPointerException("key为空指针");
			}
			if (format == null) {
				throw new NullPointerException("format为空指针");
			}
			String value = getOption(key);
			result = defaultValue;
			if (value == null) {
				result = defaultValue;
			} else {
				result = Convert.strToDate(value, format, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出getOptionsAsDateTime方法");
		return result;
	}
}