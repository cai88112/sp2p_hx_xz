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

import com.fp2p.helper.FileHelper;

/**
 * 文件操作工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class FileHelperTest {

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
	 * 测试创建目录的方法. {@link com.fp2p.helper.FileHelper#mkdirs()}.
	 */
	@Test
	public final void testMkdirs() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		FileHelper.mkdirs(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("path为空指针");
		assertTrue("path为空指针时失败", result);
	}

	/**
	 * 测试创建自定义文件名的方法. {@link com.fp2p.helper.FileHelper#getFileName()}.
	 */
	@Test
	public final void testGetFileName() {
	}

	/**
	 * 测试返回指定路径下的所有文件的方法. {@link com.fp2p.helper.FileHelper#getFiles()}.
	 */
	@Test
	public final void testGetFiles() {
		boolean result = false;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		FileHelper.getFiles(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("path为空指针");
		assertTrue("path为空指针时失败", result);
	}

	/**
	 * 测试删除文件的方法. {@link com.fp2p.helper.FileHelper#removeFile()}.
	 */
	@Test
	public final void testRemoveFile() {
	}
}
