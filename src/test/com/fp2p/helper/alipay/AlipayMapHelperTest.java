package test.com.fp2p.helper.alipay;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.alipay.AlipayMapHelper;

/**
 * AlipayMapHelper工具类测试.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class AlipayMapHelperTest {

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
	 * {@link com.fp2p.helper.alipay.AlipayMapHelper.buildRequestPara#buildRequestPara()}
	 * .
	 */
	@Test
	public final void testbuildRequestPara() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("sign", "dfasd");
		sArray.put("sign_type", "11111");
		sArray.put("www", "");
		sArray.put("signeee_type", null);

		Map<String, String> result = AlipayMapHelper.buildRequestPara(sArray);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		AlipayMapHelper.buildRequestPara(null);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("sParaTemp为空指针.");
		assertTrue("sParaTemp为空指针时失败.", result3);

		Map<String, String> sArray1 = new HashMap<String, String>();
		AlipayMapHelper.buildRequestPara(sArray1);
		String resultMessage4 = byteArrayOutputStream.toString();
		boolean result4 = -1 != resultMessage4.indexOf("sParaTemp为空指针.");
		assertTrue("sParaTemp为空指针时失败.", result4);

	}

}
