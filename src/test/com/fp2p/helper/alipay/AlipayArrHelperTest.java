/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper.alipay;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.alipay.AlipayArrHelper;
import com.fp2p.helper.httpclient.HttpClientHelper;

/**
 * HttpClient方式访问,获取远程HTTP数据测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class AlipayArrHelperTest {

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
	 * {@link com.fp2p.helper.alipay.AlipayArrHelper#paraFilter()}.
	 */
	@Test
	public final void testParaFilter() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("sign", "dfasd");
		sArray.put("sign_type", "11111");
		sArray.put("www", "");
		sArray.put("signeee_type", null);

		Map<String, String> result = AlipayArrHelper.paraFilter(sArray);
		assertTrue("执行访问时失败", result != null);

		Map<String, String> sArray1 = new HashMap<String, String>();
		Map<String, String> result1 = AlipayArrHelper.paraFilter(sArray1);
		assertTrue("执行访问时失败", result1 != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		Map<String, String> sArray2 = null;
		AlipayArrHelper.paraFilter(sArray2);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("sArray为空指针");
		assertTrue("sArray为空指针时失败", result3);

	}

	/**
	 * 测试执行Http请求,返回String的方法.
	 * {@link com.fp2p.helper.alipay.AlipayArrHelper#createLinkString()}.
	 */
	@Test
	public final void testCreateLinkString() {
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
