/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fp2p.helper.DateHelper;

/**
 * DateHelper测试类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class DateHelperTest {

	/**
	 * 测试启动前方法.
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
	}

	/**
	 * 测试结束方法.
	 */
	@AfterClass
	public static void tearDownAfterClass() {
	}

	/**
	 * 获取当月的第一天 测试getMonthFirstDay返回Date的方法.
	 * {@link com.fp2p.helper.DateHelper#getMonthFirstDay()}.
	 */
	@Test
	public final void testGetMonthFirstDay() {
		boolean result = false;
		String resultMessage = DateFormatUtils.format(
				DateHelper.getMonthFirstDay(), "yyyy-MM-dd HH:mm:ss");
		result = -1 != resultMessage.indexOf("2014-12-01 00:00:00");
		assertTrue("获取当月的第一天与结果不符合.", result);
	}

	/**
	 * 获取当月的最后一天. 测试getMonthLastDay返回Date的方法.
	 * {@link com.fp2p.helper.DateHelper#getMonthLastDay()}.
	 */
	@Test
	public final void testGetMonthLastDay() {
		boolean result = false;
		String resultMessage = DateFormatUtils.format(
				DateHelper.getMonthLastDay(), "yyyy-MM-dd HH:mm:ss");
		result = -1 != resultMessage.indexOf("2014-12-31 23:59:59");
		assertTrue("获取当月的最后一天与结果不符合.", result);
	}

	/**
	 * 计算剩余时间 (多少天多少时多少分). 测试执行remainDateToString方法,返回String的方法.
	 * {@link com.fp2p.helper.DateHelper#remainDateToString()}.
	 */
	@Test
	public final void testRemainDateToString() {
		boolean result = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse("2014-12-04 12:00:05");
			date2 = sdf.parse("2014-12-09 19:30:25");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 多少天多少时多少分
		String resultMessage = DateHelper.remainDateToString(date1, date2);
		System.out.println("resultMessage:" + resultMessage);
		result = -1 != resultMessage.indexOf("5天7小时30分");
		assertTrue("计算剩余时间 (多少天多少时多少分)与结果不符合.", result);

		resultMessage = DateHelper.remainDateToString(date2, date1);
		System.out.println("resultMessage:" + resultMessage);
		result = -1 != resultMessage.indexOf("过期");
		assertTrue("计算剩余时间 (多少天多少时多少分)与结果不符合.", result);

		resultMessage = DateHelper.remainDateToString(date2, null);
		System.out.println("resultMessage:" + resultMessage);
		result = -1 != resultMessage.indexOf("过期");
		assertTrue("计算剩余时间 (多少天多少时多少分)与结果不符合.", result);

		boolean result1 = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		resultMessage = DateHelper.remainDateToString(null, date2);
		String resultMessage1 = byteArrayOutputStream.toString();
		result1 = -1 != resultMessage1.indexOf("startDate为空指针.");
		assertTrue("date2为空指针时失败", result1);

	}

	/**
	 * 获取两个日期相差天数. {@link com.fp2p.helper.DateHelper#daysBetween()}.
	 */
	@Test
	public final void testDaysBetween() {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse("2014-12-04 12:30:25");
			date2 = sdf.parse("2014-12-09 12:30:25");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int resultMessage = DateHelper.daysBetween(date1, date2);
		boolean result = false;
		if (resultMessage == Integer.parseInt("5")) {
			result = true;
		}
		assertTrue("两个日期相差天数与结果不符合.", result);

		boolean result1 = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		resultMessage = DateHelper.daysBetween(date1, null);
		String resultMessage1 = byteArrayOutputStream.toString();
		result1 = -1 != resultMessage1.indexOf("date2为空指针.");
		assertTrue("date2为空指针时失败", result1);

		boolean result2 = false;
		resultMessage = DateHelper.daysBetween(null, date2);
		String resultMessage2 = byteArrayOutputStream.toString();
		result2 = -1 != resultMessage2.indexOf("date1为空指针.");
		assertTrue("date1为空指针时失败", result2);
	}

}
