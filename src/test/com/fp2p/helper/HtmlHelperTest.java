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
import com.fp2p.helper.HtmlHelper;

/**
 * HtmlHelper测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class HtmlHelperTest {

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
	 * 测试执行Http请求,返回byte数组的方法. {@link com.fp2p.helper.HtmlHelper#buildForm()}.
	 */
	@Test
	public final void testBuildForm() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("sign", "dfasd");
		sArray.put("sign_type", "11111");
		sArray.put("www", "");
		sArray.put("signeee_type", null);
		String gateway = "http://www.baidu.com";
		String strMethod = "get";
		String strButtonName = "btnname";
		String formName = "formId";
		String result = null;
		result = HtmlHelper.buildForm(sArray, gateway, strMethod,
				strButtonName, formName);
		assertTrue("执行访问时失败", result != null);

		Map<String, String> sArray1 = new HashMap<String, String>();
		result = HtmlHelper.buildForm(sArray1, gateway, strMethod,
				strButtonName, formName);
		assertTrue("执行访问时失败", result == null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		Map<String, String> sArray2 = null;
		result = HtmlHelper.buildForm(sArray2, gateway, strMethod,
				strButtonName, formName);
		String resultMessage = byteArrayOutputStream.toString();
		boolean res3 = -1 != resultMessage.indexOf("sParaTemp为空指针");
		assertTrue("sParaTemp为空指针时失败", res3);

		result = HtmlHelper.buildForm(sArray1, null, strMethod, strButtonName,
				formName);
		String resultMessage1 = byteArrayOutputStream.toString();
		boolean res4 = -1 != resultMessage1.indexOf("gateway为空指针.");
		assertTrue("gateway为空指针时失败", res4);

		result = HtmlHelper.buildForm(sArray1, gateway, null, strButtonName,
				formName);
		String resultMessage2 = byteArrayOutputStream.toString();
		boolean res5 = -1 != resultMessage2.indexOf("strMethod为空指针");
		assertTrue("strMethod为空指针时失败", res5);

		result = HtmlHelper.buildForm(sArray1, gateway, "dsfs", strButtonName,
				formName);
		String resultMessage3 = byteArrayOutputStream.toString();
		boolean res6 = -1 != resultMessage3
				.indexOf("strMethod不是get和post的类型.strMethod:");
		assertTrue("strMethod不是get和post的类型时错误.", res6);

		result = HtmlHelper.buildForm(sArray1, gateway, strMethod, null,
				formName);
		String resultMessage4 = byteArrayOutputStream.toString();
		boolean res7 = -1 != resultMessage4.indexOf("strButtonName为空指针.");
		assertTrue("strButtonName为空指针时错误.", res7);

		result = HtmlHelper.buildForm(sArray1, gateway, strMethod,
				strButtonName, null);
		String resultMessage5 = byteArrayOutputStream.toString();
		boolean res8 = -1 != resultMessage5.indexOf("formName为空指针.");
		assertTrue("formName为空指针时错误.", res8);

	}

	/**
	 * 测试执行Http请求,返回String的方法. {@link com.fp2p.helper.HtmlHelper#buildForm
	 * #buildHtmlForm()}.
	 */
	@Test
	public final void testBuildHtmlForm() {
		Map<String, String> parm = new HashMap<String, String>();
		parm.put("url", "http://www.baidu.com");
		parm.put("method", "POST");
		parm.put("queryString", null);
		String gateway = "http://www.baidu.com";
		String method = "get";
		String resultStr = HtmlHelper.buildHtmlForm(parm, gateway, method);
		boolean result = false;
		result = resultStr.indexOf("<html>") != -1;
		assertTrue("执行访问时失败", result);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		HtmlHelper.buildHtmlForm(null, gateway, method);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("sParaTemp为空指针");
		assertTrue("sParaTemp为空指针时失败", result);

		HtmlHelper.buildHtmlForm(parm, null, method);
		String resultMessage1 = byteArrayOutputStream.toString();
		result = -1 != resultMessage1.indexOf("gateway为空指针");
		assertTrue("gateway为空指针时失败", result);

		HtmlHelper.buildHtmlForm(parm, gateway, null);
		String resultMessage2 = byteArrayOutputStream.toString();
		result = -1 != resultMessage2.indexOf("method为空指针");
		assertTrue("method为空指针时失败", result);
	}
}
