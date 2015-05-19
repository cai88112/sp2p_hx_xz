package test.com.fp2p.helper.alipay;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.alipay.AlipaySignHelper;
/**
 * AlipaySignHelper工具类测试.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class AlipaySignHelperTest {

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
	 * 测试执行AlipaySignHelper类,返String类型结果的方法.
	 * {@link com.fp2p.helper.alipay.AlipaySignHelper#buildMysign()}.
	 */
	@Test
	public final void testBuildMysign() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("sign", "dfasd");
		sArray.put("sign_type", "11111");
		sArray.put("www", "");
		sArray.put("signeee_type", null);

		String result = AlipaySignHelper.buildMysign(sArray);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		AlipaySignHelper.buildMysign(null);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("sArray为空指针");
		assertTrue("sArray为空指针时失败", result3);

	}
	
	/**
	 * 测试执行AlipaySignHelper类,返String类型结果的方法.
	 * {@link com.fp2p.helper.alipay.AlipaySignHelper#getMysign()}.
	 */
	@Test
	public final void testgetMysign() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("url", "http://www.baidu.com");
		params.put("method", "GET");
		params.put("sign", "dfasd");
		params.put("sign_type", "11111");
		params.put("www", "");
		params.put("signeee_type", null);

		String result = AlipaySignHelper.getMysign(params);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		AlipaySignHelper.getMysign(null);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("params为空指针");
		assertTrue("params为空指针时失败", result3);

	}

}
