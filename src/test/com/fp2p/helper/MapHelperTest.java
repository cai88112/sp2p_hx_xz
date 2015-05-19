/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.MapHelper;

/**
 * MapHelper测试类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class MapHelperTest {

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
	 * 测试执行Http请求,返回byte数组的方法. {@link com.fp2p.helper.MapHelper#joinMapValue()}.
	 */
	@Test
	public final void testJoinMapValuem() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("url", "http://www.baidu.com");
		map.put("method", "GET");
		map.put("sign", "dfasd");
		map.put("sign_type", "11111");
		map.put("www", "");
		map.put("signeee_type", null);
		char connector = 0;
		String result = null;
		result = MapHelper.joinMapValue(map, connector);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		result = MapHelper.joinMapValue(null, connector);
		String resultMessage = byteArrayOutputStream.toString();
		boolean res3 = -1 != resultMessage.indexOf("map为空指针.");
		assertTrue("map为空指针时失败", res3);

	}

}
