package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.fp2p.helper.ExcelFunctionHelper;

/**
 * ExcelFunctionHelper工具类测试.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public class ExcelFunctionHelperTest {

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
	 * 测试执行ExcelFunctionHelper类,返String类型结果的方法.
	 * {@link com.fp2p.helper.ExcelFunctionHelper#ExcelRate()}.
	 */
	@Test
	public final void testExcelRate() {

		double pv = Double.parseDouble("1000000");
		double pmt = Double.parseDouble("10000");
		double nper = Double.parseDouble("240");
		int cnt = Integer.parseInt("200");
		int ina = Integer.parseInt("10");
		double res = 0;
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		// 0.008770092393830146
		boolean result = false;
		result = res >= Double.parseDouble("0.008770092393830146")
				&& res <= Double.parseDouble("0.008770092393830147");
		assertTrue("运算结果错误", result);

		cnt = Integer.parseInt("1");
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		result = Double.isNaN(res);
		assertTrue("运算结果错误", result);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		pv = 0;
		pmt = Double.parseDouble("10000");
		nper = Double.parseDouble("240");
		cnt = Integer.parseInt("200");
		ina = Integer.parseInt("10");
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		String resultMessage1 = byteArrayOutputStream.toString();
		boolean result1 = -1 != resultMessage1.indexOf("pv不能为0.");
		assertTrue("params为空指针时失败", result1);

		pv = Double.parseDouble("1000000");
		pmt = 0;
		nper = Double.parseDouble("240");
		cnt = Integer.parseInt("200");
		ina = Integer.parseInt("10");
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		String resultMessage2 = byteArrayOutputStream.toString();
		boolean result2 = -1 != resultMessage2.indexOf("pmt不能为0.");
		assertTrue("pmt为0时失败", result2);

		pv = Double.parseDouble("1000000");
		pmt = Double.parseDouble("10000");
		cnt = Integer.parseInt("200");
		ina = Integer.parseInt("10");
		nper = 0;
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		String resultMessage3 = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage3.indexOf("nper不能为0.");
		assertTrue("nper为0时失败", result3);

		pv = Double.parseDouble("1000000");
		pmt = Double.parseDouble("10000");
		ina = Integer.parseInt("10");
		nper = Double.parseDouble("240");
		cnt = 0;
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		String resultMessage4 = byteArrayOutputStream.toString();
		boolean result4 = -1 != resultMessage4.indexOf("cnt不能为0.");
		assertTrue("cnt为0时失败", result4);

		pv = Double.parseDouble("1000000");
		pmt = Double.parseDouble("10000");
		nper = Double.parseDouble("240");
		cnt = Integer.parseInt("200");
		ina = 0;
		res = ExcelFunctionHelper.excelRate(pv, pmt, nper, cnt, ina);
		String resultMessage5 = byteArrayOutputStream.toString();
		boolean result5 = -1 != resultMessage5.indexOf("ina不能为0.");
		assertTrue("ina为0时失败", result5);

	}

	/**
	 * 测试执行ExcelFunctionHelper类,返String类型结果的方法.
	 * {@link com.fp2p.helper.ExcelFunctionHelper#rateTotal()}.
	 */
	@Test
	public final void testRateTotal() {

		double planTotal = Double.parseDouble("100000");
		double putIn = Double.parseDouble("50000");
		int time = Integer.parseInt("24");
		ExcelFunctionHelper.rateTotal(planTotal, putIn, time);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		System.setErr(printStream);

		planTotal = 0;
		putIn = Double.parseDouble("50000");
		time = Integer.parseInt("24");
		ExcelFunctionHelper.rateTotal(planTotal, putIn, time);
		String resultMessage1 = byteArrayOutputStream.toString();
		boolean result1 = -1 != resultMessage1.indexOf("planTotal不能为0.");
		assertTrue("planTotal为0时失败", result1);

		planTotal = Double.parseDouble("100000");
		time = Integer.parseInt("24");
		putIn = 0;
		ExcelFunctionHelper.rateTotal(planTotal, putIn, time);
		String resultMessage2 = byteArrayOutputStream.toString();
		boolean result2 = -1 != resultMessage2.indexOf("putIn不能为0.");
		assertTrue("putIn为0时失败", result2);

		planTotal = Double.parseDouble("100000");
		putIn = Double.parseDouble("50000");
		time = 0;
		ExcelFunctionHelper.rateTotal(planTotal, putIn, time);
		String resultMessage3 = byteArrayOutputStream.toString();
		boolean result3 = -1 != resultMessage3.indexOf("time不能为0.");
		assertTrue("time为0时失败", result3);

	}

}
