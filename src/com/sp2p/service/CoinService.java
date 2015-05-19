/**
 * Copyright (c) 2014 Wteamfly.  All rights reserved. 网飞公司 版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package com.sp2p.service;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;
import com.opensymphony.xwork2.inject.util.Strings;
import com.shove.Convert;
import com.shove.data.dao.MySQL;
import com.sp2p.dao.BorrowDao;
import com.sp2p.dao.CoinDao;
import com.sp2p.dao.FrontMyPaymentDao;

/**
 * 金币系统servie.
 * 
 * @author 殷梓淞
 * @since v1.0.0
 * @date 2015年1月19日 上午10:47:12
 */
public class CoinService {
	/**
	 * log日志.
	 */
	private static Log log = LogFactory.getLog(CoinService.class);
	private UserService userService;
	private CoinDao coinDao;
	private FrontMyPaymentDao frontpayDao;
	private BorrowDao borrowDao;

	/**
	 * 添加金币.
	 * 
	 * @param pMerBillNo
	 *            订单流水号.
	 * @param basePath
	 * @param fundOrderNo
	 *            资金流水号.
	 * @return
	 * @throws Exception
	 */
	public long addCoin(String pMerBillNo, String basePath, String fundOrderNo)
			throws Exception {
		log.info("处理添加金币业务");
		Connection conn = MySQL.getConnection();
		Long res = -1L;
		try {
			long borrowId = -1;
			Map<String, String> userMap = userService.quePortUserId(null,
					pMerBillNo);
			Map<String, String> hxBuMapXml = new HashMap<String, String>();
			Gson gson = new Gson();
			if (userMap != null && userMap.size() > 0) {

				List<Map<String, String>> list = gson
						.fromJson(
								userMap.get("hxBuMap"),
								new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
								}.getType());
				if (list != null && list.size() > 0) {
					hxBuMapXml = list.get(0);
				}
				borrowId = Convert.strToLong(hxBuMapXml.get("borrowId"), -1);// 借款id
			}

			Map<String, String> borrowMap = borrowDao.queryBorroeById(conn,
					borrowId);

			int isDayThe = Integer.parseInt(borrowMap.get("isDayThe"));
			// 如果不为天标时。
			if (isDayThe == 1) {
				List<Map<String, Object>> investByBIdList = frontpayDao
						.queryInvestByBorrowId(conn, borrowId);
				if (investByBIdList != null && investByBIdList.size() > 0) {

					for (int i = 0; i < investByBIdList.size(); i++) {
						Map<String, Object> investReMap = investByBIdList
								.get(i);
						double investAmount = Convert.strToDouble(investReMap
								.get("investAmount").toString(), 0.00);
						Long investor = Convert.strToLong(investReMap.get("investor")
								.toString(), -1);

						// 增加的金币
						Double coinDouble = Math.floor(investAmount / 100);
						res = coinDao.updateCoin(conn, investor, coinDouble,
								0.00);

						res = coinDao.addCoinDetail(conn, investor, coinDouble,
								0.00, "投资添加" + coinDouble + "个金币");
					}
				}
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return res;
	}

	public final UserService getUserService() {
		return userService;
	}

	public final void setUserService(UserService userService) {
		this.userService = userService;
	}

	public final CoinDao getCoinDao() {
		return coinDao;
	}

	public final void setCoinDao(CoinDao coinDao) {
		this.coinDao = coinDao;
	}

	public final FrontMyPaymentDao getFrontpayDao() {
		return frontpayDao;
	}

	public final void setFrontpayDao(FrontMyPaymentDao frontpayDao) {
		this.frontpayDao = frontpayDao;
	}

	public final BorrowDao getBorrowDao() {
		return borrowDao;
	}

	public final void setBorrowDao(BorrowDao borrowDao) {
		this.borrowDao = borrowDao;
	}
}
