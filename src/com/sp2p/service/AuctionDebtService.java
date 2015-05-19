/**
 * 
 */
package com.sp2p.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.ServletHelper;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.constants.IFundConstants;
import com.sp2p.constants.IInformTemplateConstants;
import com.sp2p.dao.AccountUsersDao;
import com.sp2p.dao.AssignmentDebtDao;
import com.sp2p.dao.AuctionDebtDao;
import com.sp2p.dao.FinanceDao;
import com.sp2p.dao.FundRecordDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.UserDao;
import com.sp2p.database.Dao.Procedures;


/**
 * 竞拍债权
 * 
 * 
 */
public class AuctionDebtService extends BaseService {
	public static Log log = LogFactory.getLog(AuctionDebtService.class);

	private AuctionDebtDao auctionDebtDao;
	private AssignmentDebtDao assignmentDebtDao;
	private SelectedService selectedService;
	private FundRecordDao fundRecordDao;
	private AccountUsersDao accountUsersDao;
	private OperationLogDao operationLogDao;

	private UserDao userDao;

	private FinanceDao financeDao;

	public void setAuctionDebtDao(AuctionDebtDao auctionDebtDao) {
		this.auctionDebtDao = auctionDebtDao;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public void setFinanceDao(FinanceDao financeDao) {
		this.financeDao = financeDao;
	}

	public void setAssignmentDebtDao(AssignmentDebtDao assignmentDebtDao) {
		this.assignmentDebtDao = assignmentDebtDao;
	}

	/**
	 * 添加竞拍债权
	 * 
	 * @param paramMap
	 *            参数值
	 * @return
	 * @throws SQLException
	 */
	public long addAuctionDebt(Map<String, String> paramMap,
			Map<String, String> userMap) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			long userId = Convert.strToLong(paramMap.get("userId"), -1);

			if (userMap != null) {
				double useMoney = Convert.strToDouble(userMap.get("usableSum"),
						0);
				double freezeSum = Convert.strToDouble(
						userMap.get("freezeSum"), 0);
				double djMoney = Convert.strToDouble(paramMap
						.get("auctionPrice"), 0);
				if (useMoney >= djMoney) {
					userMap = new HashMap<String, String>();
					userMap.put("usableSum", (useMoney - djMoney) + "");
					userMap.put("freezeSum", (freezeSum + djMoney) + "");
					String borrowTitle = assignmentDebtDao.getBorrowTitle(conn,
							Convert.strToLong(paramMap.get("debtId"), -1));
					// 冻结资金
					if (userDao.updateUser(conn, userId, userMap) > 0) {
						// 查询竞拍后的账户金额
						Map<String, String> userAmountMap = financeDao
								.queryUserAmountAfterHander(conn, userId);
						if (userAmountMap == null) {
							userAmountMap = new HashMap<String, String>();
						}
						double usableSumAfter = Convert.strToDouble(
								userAmountMap.get("usableSum") + "", 0);
						double freezeSumAfter = Convert.strToDouble(
								userAmountMap.get("freezeSum") + "", 0);
						double forPI = Convert.strToDouble(userAmountMap
								.get("forPI")
								+ "", 0);
						String remark = "竞拍[<a href="
								+ paramMap.get("basePath")
								+ "/queryDebtDetail.do?id="
								+ Convert.strToLong(paramMap.get("debtId"), -1)
								+ " target=_blank>" + borrowTitle + "</a>]资金冻结";

						fundRecordDao.addFundRecord(conn, userId, "债权转让竞拍冻结",
								djMoney, usableSumAfter, freezeSumAfter, forPI,
								-1, remark, 0.0, djMoney, -1, -1, 702, 0.0);

						// 发送通知，通过通知模板
						Map<String, Object> informTemplateMap = getInformTemplate();

						Map<String, String> noticeMap = new HashMap<String, String>();

						// 消息模版
						// 站内信
						String informTemplate = informTemplateMap.get(
								IInformTemplateConstants.FREEZE_BID)+"";
						if (informTemplate == null) {
							conn.rollback();
							return -1L;
						}
						informTemplate = informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						informTemplate = informTemplate.replace("describe",
								"<a href=" + paramMap.get("basePath")
										+ "/queryDebtDetail.do?id="
										+ paramMap.get("debtId")
										+ " target=_blank>" + borrowTitle
										+ "</a>");
						informTemplate = informTemplate.replace("money",
								paramMap.get("auctionPrice"));
						noticeMap.put("mail", informTemplate);

						// 邮件
						String e_informTemplate = informTemplateMap.get(
								IInformTemplateConstants.E_FREEZE_BID)
								+"";
						if (e_informTemplate == null) {
							conn.rollback();
							return -1L;
						}
						e_informTemplate = e_informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						e_informTemplate = e_informTemplate.replace("describe",
								"<a href=" + paramMap.get("basePath")
										+ "/queryDebtDetail.do?id="
										+ paramMap.get("debtId") + ">"
										+ borrowTitle + "</a>");
						e_informTemplate = e_informTemplate.replace("money",
								paramMap.get("auctionPrice"));
						noticeMap.put("email", e_informTemplate);

						// 短信
						String s_informTemplate = informTemplateMap.get(
								IInformTemplateConstants.S_FREEZE_BID)
								.toString();
						if (s_informTemplate == null) {
							conn.rollback();
							return -1L;
						}
						s_informTemplate = s_informTemplate.replace("userName",
								paramMap.get("userName"));
						s_informTemplate = s_informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						s_informTemplate = s_informTemplate.replace("describe",
								borrowTitle);
						s_informTemplate = s_informTemplate.replace("money",
								paramMap.get("auctionPrice"));
						noticeMap.put("note", e_informTemplate);

						selectedService.sendNoticeMSG(conn, userId, "债权转让竞拍报告",
								noticeMap, IConstants.NOTICE_MODE_5);

						result = auctionDebtDao.addAuctionDebt(conn, paramMap);
						long debtId = Convert.strToLong(paramMap.get("debtId"),
								-1);
						Map<String, String> debtMap = auctionDebtDao
								.queryAuctionMaxPriceAndCount(conn, debtId);
						debtMap.put("auctionHighPrice", debtMap
								.get("maxAuctionPrice"));
						debtMap.put("auctionerId", debtMap.get("userId"));
						debtMap.remove("id");
						assignmentDebtDao.updateAssignmentDebt(conn, debtId,
								"2", debtMap);

					}
				}

			}

			conn.commit();
			result = 1;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 添加竞拍债权
	 * 
	 * @param paramMap
	 *            参数值
	 * @return
	 * @throws SQLException
	 */
	public long updateAuctionDebt(long id, Map<String, String> paramMap)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = auctionDebtDao.updateAuctionDebt(conn, id, paramMap);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 删除竞拍债权，可删除多个
	 * 
	 * @param conn
	 * @param ids
	 *            id字符串，用,隔开
	 * @return
	 * @throws SQLException
	 */
	public long deleteAuctionDebt(String ids) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = auctionDebtDao.deleteAuctionDebt(conn, ids);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 根据ID获取竞拍债权信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getAuctionDebt(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = auctionDebtDao.getAuctionDebt(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 根据债权转让Id查询竞拍记录
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryAuctionDebtByDebtId(long debtId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> result = null;
		try {
			result = auctionDebtDao.queryAuctionDebtByDebtId(conn, debtId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 查询条数和最大竞拍金额数
	 * 
	 * @param debtId
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> queryAuctionMaxPriceAndCount(long debtId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = auctionDebtDao.queryAuctionMaxPriceAndCount(conn, debtId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 根据用户Id获取用户信息
	 * 
	 * @param alienatorId
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> getUserAddressById(long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = auctionDebtDao.getUserAddressById(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 根据用户Id获取用户信息
	 * 
	 * @param alienatorId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getUserById(long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = auctionDebtDao.getUserById(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * 查询参与竞拍的债权
	 * 
	 * @param borrowTitle
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @param pageBean
	 * @throws SQLException
	 */
	public void queryAuctionDebt(String borrowTitle, String startTime,
			String endTime, long userId, String debtStatus, PageBean pageBean)
			throws Exception {
		//
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (userId > 0) {
			condition.append(" and userId=");
			condition.append(userId);
			condition.append(" ");
		}
		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" and borrowTitle like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowTitle));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(startTime)) {
			condition.append(" and auctionTime >= '");
			condition.append(StringEscapeUtils.escapeSql(startTime));
			condition.append("' ");
		}
		if (StringUtils.isNotBlank(endTime)) {
			condition.append(" and auctionTime <= '");
			condition.append(StringEscapeUtils.escapeSql(endTime));
			condition.append("' ");
		}

		if (StringUtils.isNotBlank(debtStatus)) {
			String idStr = StringEscapeUtils.escapeSql("'" + debtStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" and debtStatus in(");
			condition.append(idSQL);
			condition.append(") ");
		}

		try {
			dataPage(conn, pageBean, " v_debt_aucting_borrow ", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询成功的竞拍债权
	 * 
	 * @param borrowTitle
	 * @param startTime
	 * @param endTime
	 * @param userId
	 * @param pageBean
	 * @throws SQLException
	 */
	public void querySuccessAuctionDebt(String borrowTitle, String startTime,
			String endTime, long userId, PageBean pageBean) throws Exception {
		//
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (userId > 0) {
			condition.append(" and auctionerId=");
			condition.append(userId);
			condition.append(" ");
		}
		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" and borrowTitle like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowTitle));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(startTime)) {
			condition.append(" and auctionEndTime >= '");
			condition.append(StringEscapeUtils.escapeSql(startTime));
			condition.append("' ");
		}
		if (StringUtils.isNotBlank(endTime)) {
			condition.append(" and auctionEndTime <= '");
			condition.append(StringEscapeUtils.escapeSql(endTime));
			condition.append("' ");
		}

		try {
			dataPage(conn, pageBean, "v_t_assignment_debt_success", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询用户的竞拍次数
	 * 
	 * @param debtId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public long queryAuctionUserTimes(long debtId, long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long result = 0;
		try {
			result = auctionDebtDao.queryAuctionUserTimes(conn, debtId, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 查找借款人Id
	 * 
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 */
	public long queryBorrowerByBorrowId(long borrowId) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = 0;
		try {
			result = auctionDebtDao.queryBorrowerByBorrowId(conn, borrowId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 统计债务信息
	 * 
	 * @param debtId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> getAuctionDebt(long debtId, long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = auctionDebtDao.getAuctionDebt(conn, debtId, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 用户参与竞拍，债权信息的更新
	 * 
	 * @param paramMap
	 * @param id
	 * @param userMap
	 * @return
	 * @throws SQLException
	 */
	public long upadteAuctionDebt(Map<String, String> paramMap, long id,
			Map<String, String> userMap) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			long userId = Convert.strToLong(paramMap.get("userId"), -1);

			if (userMap != null) {
				double useMoney = Convert.strToDouble(userMap.get("usableSum"),
						0);
				double freezeSum = Convert.strToDouble(
						userMap.get("freezeSum"), 0);
				double djMoney = Convert.strToDouble(paramMap
						.get("auctionPrice"), 0)
						- Convert.strToDouble(paramMap.get("oldAuctionPrice"),
								0);
				if (useMoney >= djMoney) {
					userMap = new HashMap<String, String>();
					userMap.put("usableSum", (useMoney - djMoney) + "");
					userMap.put("freezeSum", (freezeSum + djMoney) + "");
					String borrowTitle = assignmentDebtDao.getBorrowTitle(conn,
							Convert.strToLong(paramMap.get("debtId"), -1));
					// 冻结资金
					if (userDao.updateUser(conn, userId, userMap) > 0) {
						paramMap.put("autiontimes", "2");
						auctionDebtDao.addAuctionDebt(conn, paramMap);
						// 查询竞拍后的账户金额
						Map<String, String> userAmountMap = financeDao
								.queryUserAmountAfterHander(conn, userId);
						if (userAmountMap == null) {
							userAmountMap = new HashMap<String, String>();
						}
						double usableSumAfter = Convert.strToDouble(
								userAmountMap.get("usableSum") + "", 0);
						double freezeSumAfter = Convert.strToDouble(
								userAmountMap.get("freezeSum") + "", 0);
						double forPI = Convert.strToDouble(userAmountMap
								.get("forPI")
								+ "", 0);
						String remark = "您第二次竞拍了债权转让[<a href="
								+ paramMap.get("basePath")
								+ "/queryDebtDetail.do?id="
								+ Convert.strToLong(paramMap.get("debtId"), -1)
								+ " target=_blank>" + borrowTitle + "</a>]资金冻结";

						fundRecordDao.addFundRecord(conn, userId, "债权转让竞拍冻结",
								djMoney, usableSumAfter, freezeSumAfter, forPI,
								-1, remark, 0.0, djMoney, -1, -1, 702, 0.0);

						// 发送通知，通过通知模板
						Map<String, Object> informTemplateMap = getInformTemplate();

						Map<String, String> noticeMap = new HashMap<String, String>();

						// 消息模版
						// 站内信
						String informTemplate = informTemplateMap.get(
								IInformTemplateConstants.FREEZE_BID).toString();
						informTemplate = informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						informTemplate = informTemplate.replace("describe",
								"<a href=" + paramMap.get("basePath")
										+ "/queryDebtDetail.do?id="
										+ paramMap.get("debtId")
										+ " target=_blank>" + borrowTitle
										+ "</a>");
						informTemplate = informTemplate.replace("money",
								paramMap.get("auctionPrice"));
						noticeMap.put("mail", informTemplate);

						// 邮件
						String e_informTemplate = informTemplateMap.get(
								IInformTemplateConstants.E_FREEZE_BID)
								+"";
						e_informTemplate = e_informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						e_informTemplate = e_informTemplate.replace("describe",
								"<a href=" + paramMap.get("basePath")
										+ "/queryDebtDetail.do?id="
										+ paramMap.get("debtId") + ">"
										+ borrowTitle + "</a>");
						e_informTemplate = e_informTemplate.replace("money",
								paramMap.get("auctionPrice"));
						noticeMap.put("email", e_informTemplate);

						// 短信
						String s_informTemplate = informTemplateMap.get(
								IInformTemplateConstants.S_FREEZE_BID)
								+"";
						s_informTemplate = s_informTemplate.replace("userName",
								paramMap.get("userName"));
						s_informTemplate = s_informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						s_informTemplate = s_informTemplate.replace("describe",
								borrowTitle);
						s_informTemplate = s_informTemplate.replace("money",
								paramMap.get("auctionPrice"));
						noticeMap.put("note", e_informTemplate);

						selectedService.sendNoticeMSG(conn, userId, "债权转让竞拍报告",
								noticeMap, IConstants.NOTICE_MODE_5);

						long debtId = Convert.strToLong(paramMap.get("debtId"),
								-1);
						Map<String, String> debtMap = auctionDebtDao
								.queryAuctionMaxPriceAndCount(conn, debtId);
						debtMap.put("auctionHighPrice", debtMap
								.get("maxAuctionPrice"));
						debtMap.put("auctionerId", debtMap.get("userId"));
						debtMap.remove("id");

						assignmentDebtDao.updateAssignmentDebt(conn, debtId,
								"2", debtMap);

					}
				}
			}
			conn.commit();
			result = 1;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return result;

	}

	/**
	 * 存储过程 处理债权转让
	 * 
	 * @param debtId
	 * @param userId
	 * @param aucionPrice
	 * @param debtPwd
	 * @param basePath
	 * @return
	 * @author C_J
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> procedure_Debts(long debtId, long userId,
			double aucionPrice, String debtPwd, String basePath)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long ret = -1L;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Procedures.p_borrow_debt_add(conn, ds, outParameterValues, debtId,
					userId, new BigDecimal(aucionPrice), debtPwd, basePath, 0,
					"");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", outParameterValues.get(0) + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (ret <= 0) {
				conn.rollback();
				return map;
			}
			// 添加操作日志
			userMap = userDao.queryUserById(conn, userId);
			operationLogDao.addOperationLog(conn, "t_auction_debt", Convert
					.strToStr(userMap.get("username"), ""), IConstants.INSERT,
					Convert.strToStr(userMap.get("lastIP"), ""), 0, "债权竞拍", 1);

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

	public AccountUsersDao getAccountUsersDao() {
		return accountUsersDao;
	}

	public void setAccountUsersDao(AccountUsersDao accountUsersDao) {
		this.accountUsersDao = accountUsersDao;
	}

	public FundRecordDao getFundRecordDao() {
		return fundRecordDao;
	}

	public void setFundRecordDao(FundRecordDao fundRecordDao) {
		this.fundRecordDao = fundRecordDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	public AuctionDebtDao getAuctionDebtDao() {
		return auctionDebtDao;
	}

	public AssignmentDebtDao getAssignmentDebtDao() {
		return assignmentDebtDao;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public FinanceDao getFinanceDao() {
		return financeDao;
	}
}
