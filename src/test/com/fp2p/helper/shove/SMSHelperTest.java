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

import com.fp2p.helper.shove.SMSHelper;

/**
 * 短信接口，对短信接口地址进行拼接，提供公用测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class SMSHelperTest {

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
	 * 测试发送短信的方法. {@link com.fp2p.helper.shove.SMSHelper#sendSMS()}.
	 */
	@Test
	public final void testSendSMS() {
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		SMSHelper.sendSMS(null, "", "", "", null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("userName为空指针");
		assertTrue("userName为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		SMSHelper.sendSMS("", null, "", "", null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("password为空指针");
		assertTrue("password为空时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		SMSHelper.sendSMS("", "", null, "", null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("content为空指针");
		assertTrue("content为空时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		SMSHelper.sendSMS("", "", "", null, null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("phone为空指针");
		assertTrue("phone为空时失败", result);
	}
}
