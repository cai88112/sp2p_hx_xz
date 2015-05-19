/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper.httpclient;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.httpclient.HttpClientHelper;

/**
 * HttpClient方式访问,获取远程HTTP数据测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class HttpClientHelperTest {

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
	 * 测试执行Http请求,返回byte数组的方法.
	 * {@link com.fp2p.helper.httpclient.HttpClientHelper#executeForByte()}.
	 */
	@Test
	public final void testExecuteForByte() {
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("url", "http://www.baidu.com");
		parm.put("method", "GET");
		parm.put("queryString", null);
		byte[] resultByte = HttpClientHelper.executeForByte(parm);
		boolean result = false;
		result = resultByte.toString().indexOf("[B@") != -1;
		assertTrue("执行访问时失败", result);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		HttpClientHelper.executeForByte(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("parmMap为空指针");
		assertTrue("parmMap为空指针时失败", result);
	}

	/**
	 * 测试执行Http请求,返回String的方法.
	 * {@link com.fp2p.helper.httpclient.HttpClientHelper#executeForString()}.
	 */
	@Test
	public final void testExecuteForString() {
		
		Map<String, Object> parm = new HashMap<String, Object>();
		parm.put("url", "http://www.baidu.com");
		parm.put("method", "POST");
		parm.put("queryString", null);
		String resultStr = HttpClientHelper.executeForString(parm);
		boolean result = false;
		result = resultStr.indexOf("<html>") != -1;
		assertTrue("执行访问时失败", result);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		HttpClientHelper.executeForString(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("parmMap为空指针");
		assertTrue("parmMap为空指针时失败", result);
	}
}
