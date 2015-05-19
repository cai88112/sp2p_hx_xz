package test.com.fp2p.helper.alipay;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.alipay.AlipayHttpHelper;

/**
 * AlipayHttpHelper工具类测试.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class AlipayHttpHelperTest {

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
	 * 测试执行Http请求,返回byte数组的方法.
	 * {@link com.fp2p.helper.alipay.AlipayHttpHelper.sendPostInfo#sendPostInfo()}
	 * .
	 */
	@Test
	public final void testSendPostInfo() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("tn", "56060048_4_pg");
		sArray.put("word", "sendPostInfo");
		sArray.put("searchRadio", "on");
		// http://www.baidu.com/baidu?tn=56060048_4_pg&ie=utf-8&word=sendPostInfo&searchRadio=on

		String gateway = "http://www.baidu.com/baidu?";
		String result = AlipayHttpHelper.sendPostInfo(sArray, gateway);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		AlipayHttpHelper.sendPostInfo(null, gateway);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("sParaTemp为空指针.");
		assertTrue("sParaTemp为空指针时失败.", result3);

		Map<String, String> sArray1 = new HashMap<String, String>();
		AlipayHttpHelper.sendPostInfo(sArray1, gateway);
		String resultMessage4 = byteArrayOutputStream.toString();
		boolean result4 = -1 != resultMessage4.indexOf("sParaTemp为空指针.");
		assertTrue("sParaTemp为空指针时失败.", result4);

		AlipayHttpHelper.sendPostInfo(sArray, null);
		String resultMessage5 = byteArrayOutputStream.toString();
		boolean result5 = -1 != resultMessage5.indexOf("gateway为空指针.");
		assertTrue("gateway为空指针时失败", result5);

	}

}
