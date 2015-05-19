/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.ServletHelper;

/**
 * ServletHelper测试类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class ServletHelperTest {

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
	 * 获取根目录 测试serverRootDirectory返回String的方法.
	 * {@link com.fp2p.helper.ServletHelper#serverRootDirectory()}.
	 */
	@Test
	public final void testServerRootDirectory() {
	}

	/**
	 * 获取请求地址 测试serverUrl返回String的方法.
	 * {@link com.fp2p.helper.ServletHelper#serverUrl()}.
	 */
	@Test
	public final void testServerUrl() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ServletHelper.serverUrl(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("request为空指针");
		assertTrue("request为空指针时失败", result);
	}

	/**
	 * 获取当前请求的URL地址域参数. 测试执行getPrams方法,返回Map<String, Object> 的方法.
	 * {@link com.fp2p.helper.ServletHelper#getPrams #buildHtmlForm()}.
	 */
	@Test
	public final void testGetPrams() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ServletHelper.getPrams(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("request为空指针");
		assertTrue("request为空指针时失败", result);
	}

	/**
	 * 获取Ip地址 测试getIpAddress返回String的方法.
	 * {@link com.fp2p.helper.ServletHelper#getIpAddress()}.
	 */
	@Test
	public final void testGetIpAddress() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ServletHelper.getIpAddress(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("request为空指针.");
		assertTrue("request为空指针时失败", result);
	}

	/**
	 * 测试向客户端返回字符串的方法. {@link com.fp2p.helper.ServletHelper#returnStr()}.
	 */
	@Test
	public final void testReturnStr() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ServletHelper.returnStr(null, null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("response为空指针.");
		assertTrue("response为空指针时失败", result);
	}
}
