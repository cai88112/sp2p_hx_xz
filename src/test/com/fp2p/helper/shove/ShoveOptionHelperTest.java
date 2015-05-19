/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper.shove;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.shove.ShoveOptionHelper;

/**
 * 富壹代配置工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class ShoveOptionHelperTest {

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
	 * 测试获得Option表中指定Key的Value的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOption()}.
	 */
	@Test
	public final void testGetOption() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOption(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);
	}

	/**
	 * 测试获得Option表中指定Key的Value,并转型成为String的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOptionOnString()}.
	 */
	@Test
	public final void testGetOptionOnString() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionOnString(null, null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);
	}

	/**
	 * 测试获得Option表中指定Key的Value,并转型成为Int的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOptionAsInt()}.
	 */
	@Test
	public final void testGetOptionAsInt() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionAsInt(null, 1);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);
	}

	/**
	 * 测试获得Option表中指定Key的Value,并转型成为Long的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOptionsAsLong()}.
	 */
	@Test
	public final void testGetOptionsAsLong() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionsAsLong(null, 1);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);
	}

	/**
	 * 测试获得Option表中指定Key的Value,并转型成为Double的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOptionsAsDouble()}.
	 */
	@Test
	public final void testGetOptionsAsDouble() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionsAsDouble(null, 1);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);
	}

	/**
	 * 测试获得Option表中指定Key的Value,并转型成为Boolean的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOptionsAsBoolean()}.
	 */
	@Test
	public final void testGetOptionsAsBoolean() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionsAsBoolean(null, null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);
	}

	/**
	 * 测试获得Option表中指定Key的Value,并转型成为DateTime的方法.
	 * {@link com.fp2p.helper.shove.ShoveOptionHelper#getOptionsAsDateTime()}.
	 */
	@Test
	public final void testGetOptionsAsDateTime() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionsAsDateTime(null, null, "ff");
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("key为空指针");
		assertTrue("key为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ShoveOptionHelper.getOptionsAsDateTime("a", null, null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("format为空指针");
		assertTrue("format为空指针时失败", result);
	}
}
