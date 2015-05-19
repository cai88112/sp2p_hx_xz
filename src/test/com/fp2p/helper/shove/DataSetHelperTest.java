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

import com.fp2p.helper.shove.DataSetHelper;

/**
 * DataSet工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class DataSetHelperTest {

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
	 * 测试将DataSet转为Map的方法. {@link com.fp2p.helper.DataSet#dataSetToMap()}.
	 */
	@Test
	public final void testBeanToMap(){
//		DataSet dataSet = new DataSet();
//		List<Object> outParameterValues = new ArrayList<Object>();
//		dataSet.parseJaxwsSoapResult(outParameterValues);
//		Map<String, String> map = DataSetHelper.dataSetToMap(dataSet);
//		System.out.println(map);
		boolean result = false;

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);
		DataSetHelper.dataSetToMap(null);
		String resultMessage = byteArrayOutputStream.toString();
		result = -1 != resultMessage.indexOf("dataSet为空指针");
		assertTrue("dataSet为空指针时失败", result);
	}
}
