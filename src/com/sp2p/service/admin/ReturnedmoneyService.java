package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sun.util.logging.resources.logging;

import com.fp2p.helper.shove.DataSetHelper;
import com.google.gson.Gson;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.config.QianduoduoConfig;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.services.QianduoduoPayUtil;
import com.shove.vo.LoanInfoBean;
import com.shove.vo.LoanInfoSecondaryBean;
import com.shove.vo.PageBean;
import com.sp2p.dao.FrontMyPaymentDao;
import com.sp2p.dao.ReturnedmoneyDao;
import com.sp2p.service.FinanceService;
import com.sp2p.service.FrontMyPaymentService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserService;

/**
 * 回款续投Service.
 * 
 * @ClassName: ReturnedmoneyService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2014年12月31日 下午2:17:01
 *
 */
public class ReturnedmoneyService extends BaseService {
	/**
	 * log日志.
	 */
	private static Log log = LogFactory.getLog(ReturnedmoneyService.class);

	/**
	 * ReturnedmoneyDao实例.
	 */
	private ReturnedmoneyDao returnedmoneyDao;

	private FrontMyPaymentDao frontpayDao;

	private FrontMyPaymentService frontpayService;

	private FinanceService financeService;

	private UserService userService;

	private RechargeService rechargeService;

	/**
	 * 分页查询.
	 * 
	 * @param conn
	 * @param pageBean
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws DataException
	 */
	public void queryReturnedmoneyPageAll(
			PageBean<Map<String, Object>> pageBean, String userName)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String command = "";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'";
		}
		StringBuffer cmd = new StringBuffer();
		cmd.append("(select r.id,r.userId,r.sumreturnedmoney,(r.recievedreinvestreward - r.hasreinvestreward) as recievedreinvestreward,r.hasreinvestreward,a.username,p.realName ");
		cmd.append(" from t_user a inner join t_returnedmoney r on a.id = r.userId inner join t_person p on a.id = p.userId ");
		cmd.append(" ) u");
		try {
			dataPage(conn, pageBean, cmd.toString(), "*", "", command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 投标审核加续投奖励.
	 * 
	 * @param pMerBillNo
	 * @param basePath
	 * @param fundOrderNo
	 * @return
	 * @throws Exception
	 */
	public long addReturnMoneyDetail(String pMerBillNo, String basePath,
			String fundOrderNo) throws Exception {
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

			Map<String, String> borrowMapRe = returnedmoneyDao
					.getRewardRuleByBorrowId(conn, borrowId);
			double rewardrule = Double.parseDouble(borrowMapRe
					.get("rewardrule"));
			int isDayThe = Integer.parseInt(borrowMapRe.get("isDayThe"));
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
						double sumreturnedmoney = Convert.strToDouble(
								investReMap.get("sumreturnedmoney").toString(),
								0.00);
						long investid = Convert.strToLong(investReMap.get("id")
								.toString(), -1);
						long oneOfUserId = Convert.strToLong(
								investReMap.get("investor").toString(), -1);
						double returnedmoneyamount = 0.00;
						double reinvestreward = 0.00;
						if (investAmount > sumreturnedmoney) {
							returnedmoneyamount = sumreturnedmoney;
						} else {
							returnedmoneyamount = investAmount;
						}
						reinvestreward = returnedmoneyamount * rewardrule;
						res = returnedmoneyDao.addReturnedMoneyDetail(conn,
								investid, returnedmoneyamount, reinvestreward);
						long res1 = returnedmoneyDao
								.updateReturnedMoneyByUserId(conn, oneOfUserId,
										returnedmoneyamount, reinvestreward);
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

	/**
	 * 还款时加回款.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param sumreturnedmoney
	 *            累计回款金额.
	 * @return 返回更新结果.
	 * @throws Exception
	 *             抛出异常.
	 */
	public long updateSumReturnedMoneyByUserId(long repayId, long bIdLong)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long res = -1L;
		try {

			// 查询所有还款对象（投资人）
			List<Map<String, Object>> investorList = frontpayService
					.queryRepay(repayId);
			Double excitationSum = 0.00;// 奖励金额
			Double borrowAmount = 0.00;
			// 是否第一次还款
			Map<String, String> statusMap = frontpayService
					.queryInvestStatus(bIdLong);
			int statusCount = 0;// 第一次还款标识
			if (statusMap != null && statusMap.size() > 0) {
				statusCount = Convert.strToInt(statusMap.get("statusCount"), 0);
			}

			// 借款信息
			Map<String, String> investDetailMap = financeService
					.queryBorrowInvest(bIdLong);
			if (investDetailMap != null && investDetailMap.size() > 0) {
				borrowAmount = Convert.strToDouble(
						investDetailMap.get("borrowAmount"), 0.00);
				if (statusCount == 0) {
					// 奖励方式
					String excitationType = investDetailMap
							.get("excitationType");
					if ("2".equals(excitationType)) {
						excitationSum = Convert.strToDouble(
								investDetailMap.get("excitationSum"), 0.00);
					}
					if ("3".equals(excitationType)) {
						excitationSum = borrowAmount
								* Convert.strToDouble(
										investDetailMap.get("excitationSum"),
										0.00) * 0.01;
					}
				}
			}

			// 总共罚息
			double sumRecivedFI = 0.00;
			// 平台收取的罚息总和
			double remainRecivedFI = 0.00;
			// 循环投资人
			for (int i = 0; i < investorList.size(); i++) {
				Map<String, Object> investorMap = investorList.get(i);
				Double amt = Convert.strToDouble(
						investorMap.get("recivedPrincipal").toString(), 0)
						+ Convert.strToDouble(investorMap
								.get("recivedInterest").toString(), 0)
						+ Convert.strToDouble(investorMap.get("recivedFI")
								.toString(), 0) / 2;
				sumRecivedFI = sumRecivedFI
						+ Convert.strToDouble(investorMap.get("recivedFI")
								.toString(), 0);
				remainRecivedFI = remainRecivedFI
						+ Convert.strToDouble(String.format("%.2f", Convert
								.strToDouble(investorMap.get("recivedFI")
										.toString(), 0) / 2), 0);

				Double awardAmount = 0.00;// 奖励
				if (statusCount == 0) {
					Double investAmount = Convert.strToDouble(
							investorMap.get("investAmount").toString(), 0);
					if (excitationSum > 0) {
						awardAmount = (investAmount / borrowAmount)
								* excitationSum;
					}
				}
				String pTTrdAmt = String.format("%.2f", amt + awardAmount);// 还款金额

				int owner = Integer.parseInt(investorMap.get("owner")
						.toString());

				res = returnedmoneyDao.updateSumReturnedMoneyByUserId(conn,
						owner, Double.parseDouble(pTTrdAmt));

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

	/**
	 * 提现扣回款.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param widLong
	 *            提现表的id.
	 * @return 返回更新结果.
	 * @throws Exception
	 *             抛出异常.
	 */
	public long withdrawCash(long userId, long widLong) throws Exception {
		Connection conn = MySQL.getConnection();
		Long res = -1L;
		try {
			Map<String, String> withdrawMap = rechargeService
					.getWithdrawByWithdrawId(widLong);
			if (withdrawMap != null) {
				double withdrawMoney = Double.parseDouble(withdrawMap
						.get("sum"));

				Map<String, String> userMap = userService.queryUserById(userId);
				if (userMap != null) {
					double usableSum = Double.parseDouble(userMap
							.get("usableSum"));

					Map<String, String> returnMoneyMap = returnedmoneyDao
							.getReturnMoneyByUserID(conn, userId);
					if (returnMoneyMap != null) {
						double sumreturnedmoney = Double
								.parseDouble(returnMoneyMap
										.get("sumreturnedmoney"));

						double remainingSum = usableSum - withdrawMoney;
						if (remainingSum <= sumreturnedmoney) {
							res = returnedmoneyDao
									.updateMinusSumReturnedMoneyByUserId(conn,
											userId, withdrawMoney);
						}
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

	/**
	 * 系统发放全部续投奖励.
	 * 
	 * @throws DataException
	 */
	public void giveContinuedInvestmentReward(String pMerBillNo) throws SQLException,
			DataException {
		log.debug("进入系统发放全部续投奖励方法giveContinuedInvestmentReward");
		Connection conn = MySQL.getConnection();

		try {
			List<Map<String, Object>> needReturnmoneyList = returnedmoneyDao
					.getReturnmoneyByRecievedMinusHasre(conn);

			if (needReturnmoneyList != null && needReturnmoneyList.size() > 0) {
				for (int i = 0; i < needReturnmoneyList.size(); i++) {
					Map<String, Object> investReMap = needReturnmoneyList
							.get(i);
					double rewardmoney = Convert.strToDouble(
							investReMap.get("money").toString(), 0.00);
					long id = Convert.strToLong(investReMap.get("id")
							.toString(), -1);
					long userId = Convert.strToLong(investReMap.get("userId")
							.toString(), -1);
					if (id > 0 && userId > 0) {
						// 更新回款续投表
						long res = returnedmoneyDao
								.updateHasreInvestRewardById(conn, id,
										rewardmoney);
						if (res > 0) {
							// 更新用户表
							long res1 = userService.addUserUsableAmount(conn,
									rewardmoney, userId);
							// 添加资金记录
							if (res1 > 0) {
								returnedmoneyDao.insertFundRecord(conn,
										rewardmoney, userId,pMerBillNo);
							}
						}
					}

				}
			}

			conn.commit();
		} catch (SQLException e) {
			log.error(e);
			conn.rollback();
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}
		log.debug("退出系统发放全部续投奖励方法giveContinuedInvestmentReward");
	}

	public ReturnedmoneyDao getReturnedmoneyDao() {
		return returnedmoneyDao;
	}

	public void setReturnedmoneyDao(ReturnedmoneyDao returnedmoneyDao) {
		this.returnedmoneyDao = returnedmoneyDao;
	}

	public void setFrontpayService(FrontMyPaymentService frontpayService) {
		this.frontpayService = frontpayService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setFrontpayDao(FrontMyPaymentDao frontpayDao) {
		this.frontpayDao = frontpayDao;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

}
