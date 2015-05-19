/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package test.com.fp2p.helper;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fp2p.helper.AmountHelper;

/**
 * 金额计算工具测试类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public class AmountHelperTest {

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
	 * 测试秒还借款的方法. {@link com.fp2p.helper.AmountHelper#rateSecondsSum()}.
	 */
	@Test
	public final void testRateSecondsSum() {
		List<Map<String, Object>> list = AmountHelper.rateSecondsSum(
				Double.parseDouble("0.1"), Double.parseDouble("0.2"), 1);
		boolean result = false;
		result = list.get(0).get("totalSum").equals("0.10");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("totalAmount").equals("0.10");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("stillInterest").equals("0.00");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("stillPrincipal").equals("0.10");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("mRate").equals("0.0167");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("repayDate").equals("2015-01-05");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("interestBalance").equals(0);
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("秒还借款失败", result);
		result = list.get(0).get("principalBalance").equals(0);
		assertTrue("秒还借款失败", result);
	}

	/**
	 * 测试秒还借款收益的方法. {@link com.fp2p.helper.AmountHelper#earnSecondsSum()}.
	 */
	@Test
	public final void testEarnSecondsSum() {
		Map<String, String> map = AmountHelper.earnSecondsSum(
				Double.parseDouble("1000"), Double.parseDouble("0.1"),
				Double.parseDouble("0.2"), 1, 1d, 1);
		boolean result = false;
		result = map.get("rewardSum").equals("10000.0");
		assertTrue("秒还借款收益失败", result);
		result = map.get("totalInterest").equals("0.1667");
		assertTrue("秒还借款收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("秒还借款收益失败", result);
		result = map.get("monthRate").equals("1.6666666666666666E-4");
		assertTrue("秒还借款收益失败", result);
		result = map.get("msg").equals(
				"投资秒还借款月利率：0.0167%<br/>其中投资金额："
						+ "￥1000.0元<br/>收益利息：￥0.17元<br/>扣除投资管理费："
						+ "￥0.02元<br/>收益总额：￥11000.15元");
		assertTrue("秒还借款收益失败", result);
		result = map.get("iManageFee").equals("0.02");
		assertTrue("秒还借款收益失败", result);

		map = AmountHelper.earnSecondsSum(Double.parseDouble("1000"),
				Double.parseDouble("0.1"), Double.parseDouble("0.2"), 1, 1d, 2);
		result = map.get("rewardSum").equals("10000.0");
		assertTrue("秒还借款收益失败", result);
		result = map.get("totalInterest").equals("0.1667");
		assertTrue("秒还借款收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("秒还借款收益失败", result);
		result = map.get("monthRate").equals("1.6666666666666666E-4");
		assertTrue("秒还借款收益失败", result);
		result = map.get("msg").equals(
				"投标1000.0元,年利率：0.2%,期限1个月,可获得利息收益：￥0.17元");
		assertTrue("秒还借款收益失败", result);
		result = map.get("iManageFee").equals("0.02");
		assertTrue("秒还借款收益失败", result);
	}

	/**
	 * 测试按月等额还款的方法. {@link com.fp2p.helper.AmountHelper#rateCalculateMonth()}.
	 */
	@Test
	public final void testRateCalculateMonth() {
		List<Map<String, Object>> list = AmountHelper.rateCalculateMonth(
				Double.parseDouble("1000"), Double.parseDouble("0.1"), 1, 1);
		boolean result = false;
		result = list.get(0).get("totalSum").equals("1000.08");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("totalAmount").equals("1000.08");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("stillInterest").equals("0.08");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("stillPrincipal").equals("1000.00");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("repayDate").equals("2015-02-05");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("interestBalance").equals("0.00");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("principalBalance").equals("0.00");
		assertTrue("按月等额还款失败", result);

		list = AmountHelper.rateCalculateMonth(Double.parseDouble("1000"),
				Double.parseDouble("0.1"), 1, 2);
		result = list.get(0).get("totalSum").equals("1000.00");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("totalAmount").equals("1000.00");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("stillInterest").equals("0.00");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("stillPrincipal").equals("1000.00");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("repayDate").equals("2015-01-06");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("interestBalance").equals(0);
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("按月等额还款失败", result);
		result = list.get(0).get("principalBalance").equals(0);
		assertTrue("按月等额还款失败", result);
	}

	/**
	 * 测试按月等额还款收益的方法. {@link com.fp2p.helper.AmountHelper#earnCalculateMonth()}.
	 */
	@Test
	public final void testEarnCalculateMonth() {
		Map<String, String> map = AmountHelper.earnCalculateMonth(
				Double.parseDouble("1000"), Double.parseDouble("1100"),
				Double.parseDouble("0.1"), 1, 1d, 1, 1,
				Double.parseDouble("0.1"));
		boolean result = false;
		result = map.get("rewardSum").equals("0.0");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("totalInterest").equals("0.0818");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("monthRate").equals("8.333333333333333E-5");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("msg").equals(
				"投资期数1个月,月利率：0.0083%<br/>投资金额：" + "￥1000.0元<br/>到期收益利息："
						+ "￥0.08元<br/>到期扣除投资管理费："
						+ "￥0.01元<br/>到期收益总额：￥1000.07元");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("iManageFee").equals("0.01");
		assertTrue("按月等额还款收益失败", result);

		map = AmountHelper.earnCalculateMonth(Double.parseDouble("1000"),
				Double.parseDouble("1100"), Double.parseDouble("0.1"), 1, 1d,
				2, 2, Double.parseDouble("0.1"));
		result = map.get("rewardSum").equals("0.0");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("totalInterest").equals("0.0027");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("monthRate").equals("8.333333333333333E-5");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("msg")
				.equals("投标1000.0元,年利率：0.1%,期限1天,可获得利息收益：￥0.00元");
		assertTrue("按月等额还款收益失败", result);
		result = map.get("iManageFee").equals("0.0");
		assertTrue("按月等额还款收益失败", result);
	}

	/**
	 * 测试按月先息后本的方法. {@link com.fp2p.helper.AmountHelper#rateCalculateSum()}.
	 */
	@Test
	public final void testRateCalculateSum() {
		List<Map<String, Object>> list = AmountHelper.rateCalculateSum(
				Double.parseDouble("1000"), Double.parseDouble("0.1"), 1, 1);
		boolean result = false;
		result = list.get(0).get("totalSum").equals("1000.08");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("totalAmount").equals("1000.08");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("stillInterest").equals("0.08");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("stillPrincipal").equals("1000.00");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("repayDate").equals("2015-02-05");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("interestBalance").equals(0);
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("principalBalance").equals(0);
		assertTrue("按月先息后本失败", result);

		list = AmountHelper.rateCalculateSum(Double.parseDouble("1000"),
				Double.parseDouble("0.1"), 2, 2);
		result = list.get(0).get("totalSum").equals("1000.01");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("totalAmount").equals("1000.01");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("stillInterest").equals("0.01");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("stillPrincipal").equals("1000.00");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("repayDate").equals("2015-01-07");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("interestBalance").equals(0);
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("principalBalance").equals(0);
		assertTrue("按月先息后本失败", result);

		list = AmountHelper.rateCalculateSum(Double.parseDouble("1000"),
				Double.parseDouble("0.1"), 2, 1);
		result = list.get(0).get("totalSum").equals("0.08");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("totalAmount").equals("1000.16");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("stillInterest").equals("0.08");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("stillPrincipal").equals(0);
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("repayDate").equals("2015-02-05");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("interestBalance").equals("0.08");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("repayPeriod").equals("1/2");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("principalBalance").equals("1000.00");
		assertTrue("按月先息后本失败", result);
	}

	/**
	 * 测试按月先息后本收益的方法. {@link com.fp2p.helper.AmountHelper#earnCalculateSum()}.
	 */
	@Test
	public final void testEarnCalculateSum() {
		Map<String, String> map = AmountHelper.earnCalculateSum(
				Double.parseDouble("1000"), Double.parseDouble("1100"),
				Double.parseDouble("0.1"), 1, 1d, 1, 1);
		boolean result = false;
		result = map.get("rewardSum").equals("0.0");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("totalInterest").equals("0.0818");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("monthRate").equals("8.333333333333333E-5");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("msg").equals(
				"月利率：0.0083%,投资期数1个月<br/>其中投资金额：" + "￥1000.0元<br/>到期收益利息："
						+ "￥0.08元<br/>到期扣除投资管理费："
						+ "￥0.01元<br/>到期收益总额：￥1000.07元");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("iManageFee").equals("0.01");
		assertTrue("按月先息后本收益失败", result);

		map = AmountHelper.earnCalculateSum(Double.parseDouble("1000"),
				Double.parseDouble("1100"), Double.parseDouble("0.1"), 1, 1d,
				2, 2);
		result = map.get("rewardSum").equals("0.0");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("totalInterest").equals("0.0027");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("monthRate").equals("8.333333333333333E-5");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("msg")
				.equals("投标1000.0元,年利率：0.1%,期限1天,可获得利息收益：￥0.00元");
		assertTrue("按月先息后本收益失败", result);
		result = map.get("iManageFee").equals("0.0");
		assertTrue("按月先息后本收益失败", result);
	}

	/**
	 * 测试一次性还款的方法. {@link com.fp2p.helper.AmountHelper#rateCalculateOne()}.
	 */
	@Test
	public final void testRateCalculateOne() {
		List<Map<String, Object>> list = AmountHelper.rateCalculateOne(
				Double.parseDouble("1000"), Double.parseDouble("0.1"), 1, 1);
		boolean result = false;
		result = list.get(0).get("totalSum").equals("1000.08");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("totalAmount").equals("1000.08");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("stillInterest").equals("0.08");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("stillPrincipal").equals("1000.00");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("repayDate").equals("2015-02-05");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("interestBalance").equals(0);
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("principalBalance").equals(0);
		assertTrue("一次性还款失败", result);

		list = AmountHelper.rateCalculateOne(Double.parseDouble("1000"),
				Double.parseDouble("0.1"), 2, 2);
		result = list.get(0).get("totalSum").equals("1000.01");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("totalAmount").equals("1000.01");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("stillInterest").equals("0.01");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("stillPrincipal").equals("1000.00");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("mRate").equals("0.0083");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("repayDate").equals("2015-01-07");
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("interestBalance").equals(0);
		assertTrue("一次性还款失败", result);
		result = list.get(0).get("repayPeriod").equals("1/1");
		assertTrue("按月先息后本失败", result);
		result = list.get(0).get("principalBalance").equals(0);
		assertTrue("一次性还款失败", result);
	}

	/**
	 * 测试一次性还款收益的方法. {@link com.fp2p.helper.AmountHelper#earnCalculateOne()}.
	 */
	@Test
	public final void testEarnCalculateOne() {
		Map<String, String> map = AmountHelper.earnCalculateOne(
				Double.parseDouble("1000"), Double.parseDouble("1100"),
				Double.parseDouble("0.1"), 1, 1d, 1, 1,
				Double.parseDouble("0.1"));
		boolean result = false;
		result = map.get("rewardSum").equals("0.0");
		assertTrue("一次性还款收益失败", result);
		result = map.get("totalInterest").equals("0.0818");
		assertTrue("一次性还款收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("一次性还款收益失败", result);
		result = map.get("monthRate").equals("8.333333333333333E-5");
		assertTrue("一次性还款收益失败", result);
		result = map.get("msg").equals(
				"月利率：0.0083%,投资期数1个月<br/>其中投资金额：" + "￥1000.0元<br/>到期收益利息："
						+ "￥0.08元<br/>到期扣除投资管理费："
						+ "￥0.01元<br/>到期收益总额：￥1000.07元");
		assertTrue("一次性还款收益失败", result);
		result = map.get("iManageFee").equals("0.01");
		assertTrue("一次性还款收益失败", result);

		map = AmountHelper.earnCalculateOne(Double.parseDouble("1000"),
				Double.parseDouble("1100"), Double.parseDouble("0.1"), 1, 1d,
				2, 2, Double.parseDouble("0.1"));
		result = map.get("rewardSum").equals("0.0");
		assertTrue("一次性还款收益失败", result);
		result = map.get("totalInterest").equals("0.0027");
		assertTrue("一次性还款收益失败", result);
		result = map.get("realAmount").equals("1000.0");
		assertTrue("一次性还款收益失败", result);
		result = map.get("monthRate").equals("8.333333333333333E-5");
		assertTrue("一次性还款收益失败", result);
		result = map.get("msg")
				.equals("投标1000.0元,年利率：0.1%,期限1天,可获得利息收益：￥0.00元");
		assertTrue("一次性还款收益失败", result);
		result = map.get("iManageFee").equals("0.0");
		assertTrue("一次性还款收益失败", result);
	}
}
