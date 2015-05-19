package com.sp2p.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.sp2p.constants.IAmountConstants;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.security.Encrypt;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.BorrowDao;
import com.sp2p.dao.FrontMyPaymentDao;
import com.sp2p.dao.InvestDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.UserDao;
import com.sp2p.database.Dao.Procedures;

public class FrontMyPaymentService extends BaseService {

	public static Log log = LogFactory.getLog(FrontMyPaymentService.class);

	private FrontMyPaymentDao frontpayDao;
	private AwardService awardService;
	private SelectedService selectedService;
	private AssignmentDebtService assignmentDebtService;
	private InvestDao investDao;
	private BorrowDao borrowDao;
	private OperationLogDao operationLogDao;
	private UserDao userDao;

	// 用于查询逾期的记录

	public void queryMySuccessBorrowList(PageBean pageBean, long userId,
			String startTime, String endTime, String title, int borrowStatus)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String condition = " and publisher=" + userId
				+ "   and borrowId is not null ";
		if (endTime != null) {
			condition += " and publishTime<'"
					+ StringEscapeUtils.escapeSql(endTime) + "'";
		}
		if (startTime != null) {
			condition += " and publishTime>'"
					+ StringEscapeUtils.escapeSql(startTime) + "'";
		}
		if (title != null) {
			condition += " and borrowTitle like '%"
					+ StringEscapeUtils.escapeSql(title) + "%'";
		}
		if (borrowStatus != -1) {// 还款中的记录
			condition += " and borrowStatus =" + borrowStatus;
		} else {
			condition += " and borrowStatus in(4,5)";
		}
		String filed = "id,borrowTitle,borrowWay,borrowAmount,annualRate,deadline,date_format(publishTime,'%Y-%m-%d')  as publishTime,publisher,borrowStatus,paymentMode, isDayThe,borrowId,stillTotalSum,hasPI,hasSum,hasFI,date_format(auditTime,'%Y-%m-%d') as auditTime ";
		try {
			dataPage(conn, pageBean, "t_borrow_success_list", filed, " ",
					condition);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryOneBorrowInfo(long userId, long borrowId)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = frontpayDao
					.queryOneBorrowInfo(conn, userId, borrowId, -1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 根据还款id查询需要还款的投资人列表.
	* @Title: queryRepay 
	* @param
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	public List<Map<String, Object>> queryRepay(long repayId) throws Exception {
		Connection conn = MySQL.getConnection();

		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		try {
			arrayList = frontpayDao.queryRepay(conn, repayId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return arrayList;
	}

	public void queryPayingDetails(PageBean pageBean, long borrowId,
			Integer repayStatus) throws Exception {
		Connection conn = MySQL.getConnection();
		// 去掉已还状态的明细
		String condition = "";
		if (repayStatus == IConstants.PAYING_STATUS_SUCCESS) {
			condition = " and borrowId=" + borrowId + " and repayStatus="
					+ IConstants.PAYING_STATUS_SUCCESS;
		} else {
			condition = " and borrowId=" + borrowId;
		}
		try {
			// 按还款日期升序排
			dataPage(conn, pageBean, "t_success_paying_details", " * ",
					" order by repayDate asc", condition);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryAllDetails(PageBean pageBean, long userId,
			String startTime, String endTime, String title) throws Exception {
		Connection conn = MySQL.getConnection();
		// 去掉已还状态的明细
		String condition = " and publisher=" + userId;
		if (endTime != null) {
			condition += " and publishTime<='"
					+ StringEscapeUtils.escapeSql(endTime) + "'";
		}
		if (startTime != null) {
			condition += " and publishTime>='"
					+ StringEscapeUtils.escapeSql(startTime) + "'";
		}
		if (title != null) {
			condition += " and borrowTitle like '%"
					+ StringEscapeUtils.escapeSql(title) + "%'";
		}
		try {
			// 按还款日期升序排
			dataPage(conn, pageBean, "v_t_repayment_detail", "*", "", condition);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryBorrowInvestorInfo(PageBean pageBean, long userId,
			String investor) throws Exception {
		Connection conn = MySQL.getConnection();
		// 去掉已还状态的明细
		String condition = " and a.publisher=" + userId
				+ " and a.borrowId is not null ";

		if (StringUtils.isNotBlank(investor)) {
			condition += " and a.username  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(investor.trim()) + "','%')";
		}
		try {
			// 按还款日期升序排
			dataPage(
					conn,
					pageBean,
					"(SELECT * FROM v_t_bacount_detail UNION ALL SELECT * FROM v_t_bacount_history_detail) a",
					"*", " ", condition);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public List<Map<String, Object>> queryPayingBorrowIds(long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();

		List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
		try {
			arrayList = frontpayDao.queryPayingBorrowIds(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return arrayList;
	}

	public Map<String, String> queryMyPayData(long payId) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = frontpayDao.queryMyPayData(conn, payId, -1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 提交还款.
	 * 
	 * @throws Exception
	 * @MethodName: submitPay
	 * @Param: FrontMyPaymentService
	 * @Author: gang.lv
	 * @Date: 2013-4-11 下午05:50:31
	 * @Return:
	 * @Descb: 提交还款
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> submitPay(long id, long userId, String basePath,
			String username, String pMerBillNo) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		/*
		 * if ("1".equals(IConstants.ENABLED_PASS)) { dealPWD =
		 * com.shove.security.Encrypt.MD5(dealPWD.trim()); } else { dealPWD =
		 * com.shove.security.Encrypt.MD5(dealPWD.trim() + IConstants.PASS_KEY);
		 * }
		 */
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		long ret = -1;
		try {
			// String result = paySubmit(conn,id,userId,dealPWD,basePath,
			// username);
			// 查询借款信息得到借款时插入的平台收费标准
			Map<String, String> mapacc = borrowDao.queryBorrowCostByPayId(conn,
					id);
			String feelog = Convert.strToStr(mapacc.get("feelog"), "");
			Map<String, Double> feeMap = (Map<String, Double>) JSONObject
					.toBean(JSONObject.fromObject(feelog), HashMap.class);
			double investFeeRate = Convert.strToDouble(
					feeMap.get(IAmountConstants.INVEST_FEE_RATE) + "", 0);
			Procedures.p_borrow_repayment(conn, ds, outParameterValues, id,
					userId, null, basePath, new Date(), new BigDecimal(
							investFeeRate), pMerBillNo, -1, "");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (ret <= 0) {
				map.put("msg", "操作失败");
				conn.rollback();
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_repayment",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.UPDATE,
						Convert.strToStr(userMap.get("lastIP"), ""), 0, "用户还款",
						1);
				/* //还款成功修改奖励机制 */
				if ("1".equals(ret + "")) {
					// // 还款处理积分
					DataSet ds1 = MySQL
							.executeQuery(
									conn,
									" select a.id,a.investor userId,((a.realAmount/c.borrowAmount)*b.stillPrincipal) principal from t_invest a left join t_repayment b on a.borrowId = b.borrowId  left join t_borrow c on a.borrowId = c.id where b.id ="
											+ id);
					ds1.tables.get(0).rows.genRowsMap();
					List<Map<String, Object>> list = ds1.tables.get(0).rows.rowsMap;

					for (Map<String, Object> map2 : list) {
						long uId = Convert.strToLong(map2.get("userId") + "",
								-1);
						Object obj = map2.get("principal");
						BigDecimal amounts = BigDecimal.ZERO;
						if (obj != null) {
							amounts = new BigDecimal(obj + "").setScale(2,
									BigDecimal.ROUND_HALF_UP);
						}
						awardService.updateMoneyNew(conn, uId, amounts,
								IConstants.MONEY_TYPE_2, id);
					}
					assignmentDebtService.preRepayment(conn, id);
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

		return map;
	}

	/**
	 * 提交还款(体验金).
	 * 
	 * @throws Exception
	 * @MethodName: submitPay
	 * @Param: FrontMyPaymentService
	 * @Author: gang.lv
	 * @Date: 2013-4-11 下午05:50:31
	 * @Return:
	 * @Descb: 提交还款
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> submitExperienceMoneyPay(long id, long userId,
			String basePath, String username, String pMerBillNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		long ret = -1;
		try {
			// 查询借款信息得到借款时插入的平台收费标准
			Map<String, String> mapacc = borrowDao.queryBorrowCostByPayId(conn,
					id);
			String feelog = Convert.strToStr(mapacc.get("feelog"), "");
			Map<String, Double> feeMap = (Map<String, Double>) JSONObject
					.toBean(JSONObject.fromObject(feelog), HashMap.class);
			double investFeeRate = Convert.strToDouble(
					feeMap.get(IAmountConstants.INVEST_FEE_RATE) + "", 0);
			Procedures.p_borrow_repayment(conn, ds, outParameterValues, id,
					userId, null, basePath, new Date(), new BigDecimal(
							investFeeRate), pMerBillNo, -1, "");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (ret <= 0) {
				map.put("msg", "操作失败");
				conn.rollback();
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_repayment",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.UPDATE,
						Convert.strToStr(userMap.get("lastIP"), ""), 0, "用户还款",
						1);
				/* //还款成功修改奖励机制 */
				if ("1".equals(ret + "")) {
					// // 还款处理积分
					DataSet ds1 = MySQL
							.executeQuery(
									conn,
									" select a.id,a.investor userId,((a.realAmount/c.borrowAmount)*b.stillPrincipal) principal from t_invest a left join t_repayment b on a.borrowId = b.borrowId  left join t_borrow c on a.borrowId = c.id where b.id ="
											+ id);
					ds1.tables.get(0).rows.genRowsMap();
					List<Map<String, Object>> list = ds1.tables.get(0).rows.rowsMap;

					for (Map<String, Object> map2 : list) {
						long uId = Convert.strToLong(map2.get("userId") + "",
								-1);
						Object obj = map2.get("principal");
						BigDecimal amounts = BigDecimal.ZERO;
						if (obj != null) {
							amounts = new BigDecimal(obj + "").setScale(2,
									BigDecimal.ROUND_HALF_UP);
						}
						awardService.updateMoneyNew(conn, uId, amounts,
								IConstants.MONEY_TYPE_2, id);
					}
					assignmentDebtService.preRepayment(conn, id);
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

		return map;
	}

	/**
	 * 查询当前用户数、总投资金额、总投资收益、总待收金额、本月累计投资
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> querInvesttou() throws Exception {
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			int result = Procedures.pr_investStatistics(conn, ds,
					outParameterValues, 0);
			if (result < 0) {
				conn.rollback();

				return map;
			}

			conn.commit();
			map = DataSetHelper.dataSetToMap(ds);
			List<Map<String, Object>> allUserNum = userDao
					.queryUserAllCount(conn);
			map.put("usernum", allUserNum.get(0).get("usernum").toString()
					+ "人");

			List<Map<String, Object>> investSumInterest = investDao
					.queryInvestSumInterest(conn);
			map.put("investSumInterest", investSumInterest.get(0).get("sum")
					.toString()
					+ "元");

			SimpleDateFormat sf = new SimpleDateFormat(DateHelper.UNDERLINE_DATE_SHORT);
			Date now = new Date();
			String timeEnd = sf.format(now);
			String timeStart = sf.format(new Date(now.getTime() - 2592000000l));
			List<Map<String, Object>> investSumInPeriod = investDao
					.queryInvestSumInPeriod(conn, timeStart, timeEnd);
			if (investSumInPeriod != null && investSumInPeriod.get(0)
					.get("sum") != null) {
				map.put("investSumInPeriod", investSumInPeriod.get(0)
						.get("sum").toString()
						+ "元");
			} else {
				map.put("investSumInPeriod", "0.00元");
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 查询是否第一次还款
	 * 
	 * @param borrowId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryInvestStatus(long borrowId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			String command = "select count(repayStatus) AS statusCount from t_invest_repayment where repayStatus=2 and borrow_id = "
					+ borrowId;
			DataSet dataSet = MySQL.executeQuery(conn, command);
			command = null;
			map = DataSetHelper.dataSetToMap(dataSet);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public InvestDao getInvestDao() {
		return investDao;
	}

	public void setInvestDao(InvestDao investDao) {
		this.investDao = investDao;
	}

	public AssignmentDebtService getAssignmentDebtService() {
		return assignmentDebtService;
	}

	public void setAssignmentDebtService(
			AssignmentDebtService assignmentDebtService) {
		this.assignmentDebtService = assignmentDebtService;
	}

	public FrontMyPaymentDao getFrontpayDao() {
		return frontpayDao;
	}

	public void setFrontpayDao(FrontMyPaymentDao frontpayDao) {
		this.frontpayDao = frontpayDao;
	}

	public AwardService getAwardService() {
		return awardService;
	}

	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
	}

	public BorrowDao getBorrowDao() {
		return borrowDao;
	}

	public void setBorrowDao(BorrowDao borrowDao) {
		this.borrowDao = borrowDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
