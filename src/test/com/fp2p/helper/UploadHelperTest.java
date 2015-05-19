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

import com.fp2p.helper.UploadHelper;
import com.shove.vo.FileCommon;
import com.shove.vo.Files;

/**
 * 处理上传功能的工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class UploadHelperTest {

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
	 * 测试使用完整参数上传文件的方法. {@link com.fp2p.helper.UploadHelper#getByAllParams()}.
	 */
	@Test
	public final void testGetByAllParams() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		UploadHelper.getByAllParams(null, null, null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("file为空指针");
		assertTrue("file为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		UploadHelper.getByAllParams(new Files(), null, null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("fileCommon为空指针");
		assertTrue("fileCommon为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		UploadHelper.getByAllParams(new Files(), new FileCommon(), null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("realpath为空指针");
		assertTrue("realpath为空指针时失败", result);
	}

	/**
	 * 测试从app上传文件的方法. {@link com.fp2p.helper.UploadHelper#uploadByFileapp()}.
	 */
	@Test
	public final void testUploadByFileapp() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		UploadHelper.uploadByFileapp(null, null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("file为空指针");
		assertTrue("file为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		UploadHelper.uploadByFileapp(new Files(), null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("source为空指针");
		assertTrue("source为空指针时失败", result);
	}
}
