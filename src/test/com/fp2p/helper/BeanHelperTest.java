/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.BeanHelper;
import com.sp2p.entity.Admin;

/**
 * JavaBean工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class BeanHelperTest {

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
	 * 测试将JavaBean转为Map的方法. {@link com.fp2p.helper.BeanHelper#beanToMap()}.
	 */
	@Test
	public final void testBeanToMap() {
		Admin admin = new Admin();
		admin.setId(1L);
		admin.setPassword("1");
		Map<String, String> map = BeanHelper.beanToMap(admin);
		boolean result = false;
		result = map.get("id").equals("1");
		assertTrue("执行转换时失败", result);
		result = map.get("password").equals("1");
		assertTrue("执行转换时失败", result);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		BeanHelper.beanToMap(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("bean为空指针");
		assertTrue("bean为空指针时失败", result);
	}

	/**
	 * 测试将bean中的值转为对应的字符串的方法. {@link com.fp2p.helper.BeanHelper#parmToStr()}.
	 */
	@Test
	public final void testParmToStr() {
		String resultStr = BeanHelper.parmToStr(null);
		boolean result = false;
		result = resultStr.equals("");
		assertTrue("参数为空时转换失败", result);
		resultStr = BeanHelper.parmToStr(1);
		result = resultStr.equals("1");
		assertTrue("参数为1时转换失败", result);
		resultStr = BeanHelper.parmToStr(new Date());
		result = resultStr.indexOf("20") != -1;
		assertTrue("参数为Date时转换失败", result);
		resultStr = BeanHelper.parmToStr("String");
		result = resultStr.indexOf("String") != -1;
		assertTrue("参数为String时转换失败", result);
	}
}
