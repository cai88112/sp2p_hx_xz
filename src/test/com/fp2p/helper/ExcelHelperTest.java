/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.ExcelHelper;

/**
 * Excel工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class ExcelHelperTest {

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
	 * 测试读取Excel的内容的方法. {@link com.fp2p.helper.ExcelHelper#getData()}.
	 */
	@Test
	public final void testGetData() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		boolean result = false;
		ExcelHelper.getData(null, 0);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("file为空指针");
		assertTrue("file为空指针时失败", result);
	}

	/**
	 * 测试导出内容为Excel文件的方法. {@link com.fp2p.helper.ExcelHelper#exportExcel()}.
	 */
	@Test
	public final void testExportExcel() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		boolean result = false;
		String[] titles = new String[1];
		String[] fieldNames = new String[1];
		ExcelHelper.exportExcel(null, null, titles, fieldNames);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("list为空指针");
		assertTrue("list为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ExcelHelper.exportExcel(null, list, null, fieldNames);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("titles为空指针");
		assertTrue("titles为空指针时失败", result);

		byteArrayOutputStream = new ByteArrayOutputStream();
		printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		ExcelHelper.exportExcel(null, list, titles, null);
		resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("fieldNames为空指针");
		assertTrue("fieldNames为空指针时失败", result);
	}
}
