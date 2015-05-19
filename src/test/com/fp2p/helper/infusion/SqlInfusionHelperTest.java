/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper.infusion;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.infusion.SqlInfusionHelper;

/**
 * 防止sql注入工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class SqlInfusionHelperTest {

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
	 * 测试过滤sql注入字符串的方法.
	 * {@link com.fp2p.helper.infusion.SqlInfusionHelper#filteSqlInfusion()}.
	 */
	@Test
	public final void testFilteSqlInfusion() {
		String returnStr = SqlInfusionHelper.filteSqlInfusion("' update drop "
				+ "delete exec create where truncate insert");
		boolean result = false;
		result = returnStr.equals("’ ｕｐｄａｔｅ ｄｒｏｐ "
				+ "ｄｅｌｅｔｅ ｅｘｅｃ ｃｒｅａｔｅ ｗｈｅｒｅ ｔｒｕｎｃａｔｅ ｉｎｓｅｒｔ");
		assertTrue("执行过滤sql注入字符串时失败", result);
		
		returnStr = SqlInfusionHelper.filteSqlInfusion("111");
		result = returnStr.equals("111");
		assertTrue("执行过滤sql注入字符串时失败", result);
	}
}
