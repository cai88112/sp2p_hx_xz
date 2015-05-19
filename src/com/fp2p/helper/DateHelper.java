package com.fp2p.helper;

import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * DateHelper类，自定义订单类. 工具类，可以用作获取系统日期、订单编号等
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class DateHelper {

	/**
	 * 私有的构造方法.
	 */
	private DateHelper() {
	}

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(DateHelper.class
			.getName());

	/**
	 * 一天的小时数.一天等于24个小时.
	 */
	public static final int DAY_HOURS_24 = 24;

	/**
	 * 一小时的分钟数.小时等于60个分钟.
	 */
	public static final int HOURS_MINUTE_60 = 60;

	/**
	 * 一分钟的秒数.一分钟等于60秒.
	 */
	public static final int MINUTE_SENCONDS_60 = 60;

	/**
	 * 一个小时的秒数.一小时等于3600秒.
	 */
	public static final int HOUR_SENCONDS_3600 = 3600;

	/**
	 * 一秒的毫秒数.一秒等于1000毫秒.
	 */
	public static final int SENCOND_MILLISECOND_1000 = 1000;

	/**
	 * 完整时间 yyyy-MM-dd HH:mm:ss.
	 */
	public static final String DATE_SIMPLE = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 年月日(下划线) yyyy-MM-dd.
	 */
	public static final String UNDERLINE_DATE_SHORT = "yyyy-MM-dd";

	/**
	 * 年的格式 yyyy.
	 */
	public static final String YEAR = "yyyy";

	/**
	 * 获取当月的第一天.
	 * 
	 * @return 返回Date类型的当月第一天.
	 */
	public static Date getMonthFirstDay() {
		logger.debug("进入getMonthFirstDay方法");
		Calendar cal = Calendar.getInstance();
		Calendar f = (Calendar) cal.clone();
		f.clear();
		f.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		f.set(Calendar.MONTH, cal.get(Calendar.MONTH));
		f.set(Calendar.HOUR, 00);
		f.set(Calendar.SECOND, 00);
		f.set(Calendar.MINUTE, 00);
		f.set(Calendar.MILLISECOND, 0);
		Date firstday = f.getTime();
		logger.debug("退出getMonthFirstDay方法");
		return firstday;

	}

	/**
	 * 获取当月的最后一天.
	 * 
	 * @return 返回Date类型的当月的最后一天.
	 */
	public static Date getMonthLastDay() {
		logger.debug("进入getMonthLastDay方法");
		// SimpleDateFormat sdf = new
		// SimpleDateFormat(" yyyy-MM-dd HH:mm:ss ");
		Calendar cal = Calendar.getInstance();
		Calendar l = (Calendar) cal.clone();
		l.clear();
		l.set(Calendar.YEAR, cal.get(Calendar.YEAR));
		l.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		l.set(Calendar.MILLISECOND, -1);
		cal.set(Calendar.HOUR, Integer.parseInt("23"));
		cal.set(Calendar.SECOND, Integer.parseInt("59"));
		cal.set(Calendar.MINUTE, Integer.parseInt("59"));
		cal.set(Calendar.MILLISECOND, 0);
		Date lastday = l.getTime();
		// lastday = lastday + " 23:59:59";
		logger.debug("退出getMonthLastDay方法");
		return lastday;
	}

	/**
	 * 获取两个日期相差天数.
	 * 
	 * @param date1
	 *            对比日期相差的时间之一
	 * @param date2
	 *            对比日期相差的时间之一
	 * @return 返回int类型的日期相差天数
	 */
	public static int daysBetween(final Date date1, final Date date2) {
		logger.debug("进入daysBetween方法");
		int result = 0;
		try {
			if (date1 == null) {
				throw new NullPointerException("date1为空指针.");
			}
			if (date2 == null) {
				throw new NullPointerException("date2为空指针.");
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(date1);
			long time1 = cal.getTimeInMillis();
			cal.setTime(date2);
			long time2 = cal.getTimeInMillis();
			long betweenDays = (time2 - time1)
					/ (SENCOND_MILLISECOND_1000 * HOUR_SENCONDS_3600 * DAY_HOURS_24);
			result = Integer.parseInt(String.valueOf(betweenDays));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出daysBetween方法");
		return result;
	}

	/**
	 * 计算剩余时间 (多少天多少时多少分).
	 * 
	 * @param startDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return 返回剩余时间 (多少天多少时多少分).
	 */
	public static String remainDateToString(final Date startDate,
			final Date endDate) {
		logger.debug("进入remainDateToString方法");
		String res = null;
		try {
			if (startDate == null) {
				throw new NullPointerException("startDate为空指针.");
			}
			StringBuilder result = new StringBuilder();
			if (endDate == null) {
				return "过期";
			}
			long times = endDate.getTime() - startDate.getTime();
			if (times < -1) {
				result.append("过期");
			} else {
				long temp = SENCOND_MILLISECOND_1000 * HOURS_MINUTE_60
						* MINUTE_SENCONDS_60 * DAY_HOURS_24;
				// 天数
				long d = times / temp;

				// 小时数
				times %= temp;
				temp /= DAY_HOURS_24;
				long m = times / temp;
				// 分钟数
				times %= temp;
				temp /= HOURS_MINUTE_60;
				long s = times / temp;

				result.append(d);
				result.append("天");
				result.append(m);
				result.append("小时");
				result.append(s);
				result.append("分");
			}
			res = result.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出remainDateToString方法");
		return res;
	}

}
