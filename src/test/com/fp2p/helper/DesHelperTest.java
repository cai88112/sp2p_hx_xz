/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.DesHelper;

/**
 * Des加密工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class DesHelperTest {

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
	 * 测试加密字符串的方法. {@link com.fp2p.helper.DesHelper#encrypt()}.
	 */
	@Test
	public final void testEncrypt() {
		String deStr = DesHelper.encrypt("hello world", "houjx");
		boolean result = false;
		result = deStr.equals("3fae73f5d2411f9f2227a050e0ad8c22");
		assertTrue("执行加密时失败", result);
	}

	/**
	 * 测试加密字符串的方法. {@link com.fp2p.helper.DesHelper#encrypt()}.
	 */
	@Test
	public final void testEncrypt2() {
		String deStr = DesHelper.encrypt("hello world");
		boolean result = false;
		result = deStr.equals("dbfd2864d6cc4d89c379a8e4e096cb33");
		assertTrue("执行加密时失败", result);
	}

	/**
	 * 测试解密字符串的方法. {@link com.fp2p.helper.DesHelper#decrypt()}.
	 */
	@Test
	public final void testDecrypt() {
		String deStr = DesHelper.decrypt("3fae73f5d2411f9f2227a050e0ad8c22",
				"houjx");
		boolean result = false;
		result = deStr.equals("hello world");
		assertTrue("执行解密时失败", result);
	}

	/**
	 * 测试解密字符串的方法. {@link com.fp2p.helper.DesHelper#decrypt()}.
	 */
	@Test
	public final void testDecrypt2() {
		String deStr = DesHelper.decrypt("dbfd2864d6cc4d89c379a8e4e096cb33");
		boolean result = false;
		result = deStr.equals("hello world");
		assertTrue("执行解密时失败", result);
	}
}
