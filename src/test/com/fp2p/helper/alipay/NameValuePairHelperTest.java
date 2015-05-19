package test.com.fp2p.helper.alipay;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.httpclient.NameValuePairHelper;
/**
 * NameValuePairHelper工具类测试.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class NameValuePairHelperTest {

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
	 * {@link com.fp2p.helper.httpclient.NameValuePairHelper#mapToNameValuePair()}
	 * .
	 */
	@Test
	public final void testMapToNameValuePair() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("sign", "dfasd");
		sArray.put("sign_type", "11111");
		sArray.put("www", "");
		sArray.put("signeee_type", null);

		NameValuePair[] result = NameValuePairHelper.mapToNameValuePair(sArray);
		assertTrue("执行访问时失败", result != null);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		NameValuePairHelper.mapToNameValuePair(null);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("properties为空指针.");
		assertTrue("sArray为空指针时失败", result3);

	}

}
