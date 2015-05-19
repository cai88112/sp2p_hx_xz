package com.fp2p.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ExcelFunctionHelper工具类.
 * 
 * @author 戴志越
 * @since v1.0.0
 */
public final class ExcelFunctionHelper {

	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager
			.getLogger(ExcelFunctionHelper.class.getName());

	/**
	 * 私有的构造方法.
	 */
	private ExcelFunctionHelper() {
	}

	/**
	 * 按月还款的月利率 excel 中rate()函数.
	 * 
	 * @param pv
	 *            单笔之期初金额
	 * @param pmt
	 *            年金之每期金额
	 * @param nper
	 *            期数
	 * @param cnt
	 *            循环次数
	 * @param ina
	 *            精确到小数点后10位
	 * @return double
	 */
	public static double excelRate(final double pv, final double pmt,
			final double nper, final int cnt, final int ina) {
		logger.debug("进入excelRate方法.");
		double result = 0;
		try {
			if (pv <= 0) {
				throw new Exception("pv不能为0.");
			}
			if (pmt <= 0) {
				throw new Exception("pmt不能为0.");
			}
			if (nper <= 0) {
				throw new Exception("nper不能为0.");
			}
			if (cnt <= 0) {
				throw new Exception("cnt不能为0.");
			}
			if (ina <= 0) {
				throw new Exception("ina不能为0.");
			}

			double rate = 1;
			double x;
			double jd = Double.parseDouble("0.1");
			double side = Double.parseDouble("0.1");
			double i = 1;
			do {
				x = pv / pmt - (Math.pow(1 + rate, nper) - 1)
						/ (Math.pow(rate + 1, nper) * rate);
				if (x * side > 0) {
					side = -side;
					jd *= Double.parseDouble("10");
				}
				rate += side / jd;
			} while (i++ < cnt
					&& Math.abs(x) >= 1 / Math.pow(Double.parseDouble("10"),
							ina));
			if (i > cnt) {
				result = Double.NaN;
			} else {
				result = rate;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.debug("进入excelRate方法.");
		return result;
	}

	/**
	 * 每月付息，到期还本 年平均收益率 = (预计到期本金收益和 / 计划投资金额) ^ (1 / 投资年限) - 1.
	 * 
	 * @param planTotal
	 *            预计到期本金收益和
	 * @param putIn
	 *            计划投资金额
	 * @param time
	 *            时间单位为月
	 * @return 年平均收益率
	 */
	public static double rateTotal(final double planTotal, final double putIn,
			final int time) {
		logger.debug("进入rateTotal方法.");
		double rate = 0;
		try {
			if (planTotal <= 0) {
				throw new Exception("planTotal不能为0.");
			}
			if (putIn <= 0) {
				throw new Exception("putIn不能为0.");
			}
			if (time <= 0) {
				throw new Exception("time不能为0.");
			}
			// 月份转换成年
			float year = time * 1.0f / Float.parseFloat("12");
			rate = Math.pow(planTotal / putIn, 1 / year) - 1;

		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("进入rateTotal方法.");
		return rate;
	}
}
