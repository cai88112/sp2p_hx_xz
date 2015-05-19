/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper.shuangqian;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fp2p.helper.shuangqian.ShuangqianSignHelper;

/**
 * ShuangqianSignHelper测试类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class ShuangqianSignHelperTest {

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
	 * {@link com.fp2p.helper.shuangqianSign.ShuangqianSignHelper#signMap()}.
	 */
	@Test
	public final void testShuangqianSignHelper() {
		String[] md5Map = new String[Integer.parseInt("5")];
		md5Map[0] = "http://www.baidu.com";
		md5Map[1] = "GET";
		md5Map[2] = "sign_type";
		md5Map[Integer.parseInt("3")] = "btnname";
		md5Map[Integer.parseInt("4")] = "fdasfd";
		String result = null;
		String securityKey = "451271";
		String type = "RES"; // REQ/RES
		result = ShuangqianSignHelper.signMap(md5Map, securityKey, type);
		assertTrue("执行访问时失败", result != null);

		type = "REQ"; // REQ/RES
		result = ShuangqianSignHelper.signMap(md5Map, securityKey, type);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		result = ShuangqianSignHelper.signMap(null, securityKey, type);
		String resultMessage = byteArrayOutputStream.toString();
		boolean res3 = -1 != resultMessage.indexOf("md5Map为空指针.");
		assertTrue("md5Map为空指针时失败", res3);

		result = ShuangqianSignHelper.signMap(md5Map, null, type);
		String resultMessage1 = byteArrayOutputStream.toString();
		boolean res4 = -1 != resultMessage1.indexOf("securityKey为空指针.");
		assertTrue("securityKey为空指针时失败", res4);

		result = ShuangqianSignHelper.signMap(md5Map, securityKey, null);
		String resultMessage2 = byteArrayOutputStream.toString();
		boolean res5 = -1 != resultMessage2.indexOf("type为空指针");
		assertTrue("type为空指针时失败", res5);

		result = ShuangqianSignHelper.signMap(md5Map, securityKey, "fdsfds");
		String resultMessage3 = byteArrayOutputStream.toString();
		boolean res6 = -1 != resultMessage3.indexOf("type不符合格式.");
		assertTrue("type不符合格式.", res6);

	}

}
