package test.com.fp2p.helper.alipay;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.alipay.AlipayNotifyHelper;
/**
 * AlipayNotifyHelper工具类测试.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class AlipayNotifyHelperTest {

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
	public final void testVerify() {
		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("url", "http://www.baidu.com");
		sArray.put("method", "GET");
		sArray.put("notify_id", "11111");
		sArray.put("www", "");
		sArray.put("signeee_type", null);

		AlipayNotifyHelper.verify(sArray);
		
		Map<String, String> sArray1 = new HashMap<String, String>();
		sArray1.put("url", "http://www.baidu.com");
		sArray1.put("method", "GET");
		sArray1.put("www", "");
		sArray1.put("notify_id", "11111");
		sArray1.put("sign", "dfasd");
		sArray1.put("signeee_type", null);
		AlipayNotifyHelper.verify(sArray1);
//		assertTrue("执行访问时失败", result != null);
		
		Map<String, String> sArray2 = new HashMap<String, String>();
		sArray2.put("url", "http://www.baidu.com");
		sArray2.put("method", "GET");
		sArray2.put("www", "");
		sArray2.put("signeee_type", null);
		AlipayNotifyHelper.verify(sArray2);
		

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		AlipayNotifyHelper.verify(null);
		String resultMessage = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage.indexOf("params为空指针");
		assertTrue("params为空指针时失败", result3);

	}
	
	
}
