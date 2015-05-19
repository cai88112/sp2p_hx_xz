/**
 * 
 */
package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.DateHelper;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.constants.IInformTemplateConstants;
import com.sp2p.dao.AccountUsersDao;
import com.sp2p.dao.AssignmentDebtDao;
import com.sp2p.dao.AuctionDebtDao;
import com.sp2p.dao.FinanceDao;
import com.sp2p.dao.FundRecordDao;
import com.sp2p.dao.InvestDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.UserDao;
import com.sp2p.dao.admin.AdminDao;
import com.sp2p.dao.admin.BorrowManageDao;
import com.sp2p.dao.admin.RiskManageDao;

/**
 * 债权转让
 * 
 * @author Administrator
 * 
 */
public class AssignmentDebtService extends BaseService {
	public static Log log = LogFactory.getLog(AssignmentDebtService.class);

	private AssignmentDebtDao assignmentDebtDao;

	private UserDao userDao;

	private AuctionDebtDao auctionDebtDao;

	private RiskManageDao riskManageDao;

	private FundRecordDao fundRecordDao;

	private SelectedService selectedService;

	private FinanceDao financeDao;

	private InvestDao investDao;

	private AwardService awardService;

	private AccountUsersDao accountUsersDao;

	private BorrowManageDao borrowManageDao;

	private OperationLogDao operationLogDao;
	private AdminDao adminDao;

	public AccountUsersDao getAccountUsersDao() {
		return accountUsersDao;
	}

	public void setAccountUsersDao(AccountUsersDao accountUsersDao) {
		this.accountUsersDao = accountUsersDao;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
	}

	public void setFinanceDao(FinanceDao financeDao) {
		this.financeDao = financeDao;
	}

	public void setAssignmentDebtDao(AssignmentDebtDao assignmentDebtDao) {
		this.assignmentDebtDao = assignmentDebtDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setInvestDao(InvestDao investDao) {
		this.investDao = investDao;
	}

	public void setAuctionDebtDao(AuctionDebtDao auctionDebtDao) {
		this.auctionDebtDao = auctionDebtDao;
	}

	public void setFundRecordDao(FundRecordDao fundRecordDao) {
		this.fundRecordDao = fundRecordDao;
	}

	/**
	 * 添加债权转让
	 * 
	 * @param paramMap
	 *            参数值
	 * @return
	 * @throws SQLException
	 */
	public long addAssignmentDebt(Map<String, String> paramMap)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long result = -1;
		try {
			if (!assignmentDebtDao.isHaveAssignmentDebt(conn, Convert
					.strToLong(paramMap.get("investId"), -1), Convert
					.strToLong(paramMap.get("alienatorId"), -1))) {

				result = assignmentDebtDao.addAssignmentDebt(conn, paramMap);
				String borrowTitle = assignmentDebtDao.getBorrowTitle(conn,
						result);
				// 添加用户动态
				String cotent = "债权转让了借款<a href=queryDebtDetail.do?id="
						+ result + " target=_blank>" + borrowTitle + "</a>";
				financeDao.addUserDynamic(conn, Convert.strToLong(paramMap
						.get("alienatorId"), -1), cotent);
				userMap = userDao.queryUserById(conn, Convert.strToLong(
						paramMap.get("alienatorId"), -1));
				operationLogDao.addOperationLog(conn, "t_assignment_debt",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT, Convert.strToStr(userMap
								.get("lastIP"), ""), 0, "发布债权转让", 1);
				result = 1;
			}

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
	 * 添加债权转让
	 * 
	 * @param paramMap
	 *            参数值
	 * @return
	 * @throws SQLException
	 */
	public long updateAssignmentDebt(long id, long debtStatus,
			Map<String, String> paramMap) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = assignmentDebtDao.updateAssignmentDebt(conn, id,
					debtStatus + "", paramMap);
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
	 * 删除债权转让，可删除多个
	 * 
	 * @param conn
	 * @param ids
	 *            id字符串，用,隔开
	 * @return
	 * @throws SQLException
	 */
	public long deleteAssignmentDebt(String ids) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = assignmentDebtDao.deleteAssignmentDebt(conn, ids);
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
	 * 根据ID获取债权转让信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getAssignmentDebt(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = assignmentDebtDao.getAssignmentDebt(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * 查询前台的全部债权
	 * 
	 * @param pageBean
	 * @throws SQLException
	 */
	public void queryAllDebt(String borrowTitle, long debtSum,
			long auctionBasePrice, long auctionMode, long isLate,
			long publishDays, String debtStatus, PageBean pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (StringUtils.isNotBlank(debtStatus)) {
			String idStr = StringEscapeUtils.escapeSql("'" + debtStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" AND  debtStatus  in(");
			condition.append(idSQL);
			condition.append(") ");
		}

		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" AND  borrowTitle  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowTitle));
			condition.append("%' ");
		}
		if (debtSum > 0) {
			if (debtSum == 1) {
				condition.append(" AND  debtSum  < 3000 ");
			} else if (debtSum == 2) {
				condition
						.append(" AND  debtSum  >= 3000 and  debtSum  < 5000 ");
			} else if (debtSum == 3) {
				condition
						.append(" AND  debtSum  >= 5000 and  debtSum  < 10000 ");
			} else if (debtSum == 4) {
				condition
						.append(" AND  debtSum  >= 10000 and  debtSum  < 20000 ");
			} else if (debtSum == 5) {
				condition
						.append(" AND  debtSum  >= 20000 and  debtSum  < 50000 ");
			} else if (debtSum == 6) {
				condition.append(" AND  debtSum  >= 50000 ");
			}
		}
		if (auctionBasePrice > 0) {
			if (auctionBasePrice == 1) {
				condition.append(" AND  auctionBasePrice  < 3000 ");
			} else if (auctionBasePrice == 2) {
				condition
						.append(" AND  auctionBasePrice  >= 3000 and  auctionBasePrice  < 5000 ");
			} else if (auctionBasePrice == 3) {
				condition
						.append(" AND  auctionBasePrice  >= 5000 and  auctionBasePrice  < 10000 ");
			} else if (auctionBasePrice == 4) {
				condition
						.append(" AND  auctionBasePrice  >= 10000 and  auctionBasePrice  < 20000 ");
			} else if (auctionBasePrice == 5) {
				condition
						.append(" AND  auctionBasePrice  >= 20000 and  auctionBasePrice  < 50000 ");
			} else if (auctionBasePrice == 6) {
				condition.append(" AND  auctionBasePrice  >= 50000 ");
			}
		}
		if (auctionMode > 0) {
			condition.append(" AND  auctionMode  =");
			condition.append(auctionMode);

		}
		if (isLate > 0) {
			condition.append(" AND  isLate  =");
			condition.append(isLate);
		}
		if (publishDays > 0) {
			String nowDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
			if (publishDays > 30) {
				condition.append(" AND datediff(now(),publishTime) >= ");
				condition.append(publishDays);
			} else {
				condition.append(" AND datediff(now(),publishTime) <= ");
				condition.append(publishDays);
			}
		}

		try {
			dataPage(
					conn,
					pageBean,
					"v_t_debt_borrow_person",
					" id  , debtSum,actionTime  ,f_formatting_username( alienatorName) as alienatorName   ,  auctionBasePrice  ,debtLimit  ,  auctionMode  , alienatorId  ,  auctionerId  ,  publishTime  ,  auctionDays  ,  debtStatus  ,  borrowId  , borrowTitle  ,publisher  , annualRate  , f_formatting_username(  borrowerName ) as borrowerName ,  auctionerName  ,   auctionHighPrice  ,  creditratingIco  ,  isLate  ,  personalHead,borrowWay  ",
					" order by  publishTime  desc ", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询可以转让的借款
	 * 
	 * @param borrowTitle
	 * @param borrowerName
	 * @param pageBean
	 * @throws SQLException
	 */
	public void queryCanAssignmentDebt(long userId, String borrowTitle,
			String borrowerName, PageBean pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (userId > 0) {
			condition.append(" and  investor =");
			condition.append(userId);
			condition.append(" ");
		}
		if (StringUtils.isNotBlank(borrowerName)) {
			condition.append(" and  borrowerName  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowerName));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" and  borrowTitle  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowTitle));
			condition.append("%' ");
		}
		condition.append(" and remainDay > 5 ");
		try {
			dataPage(conn, pageBean, "v_t_can_assignment_borrow", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 竞拍中的债权
	 * 
	 * @param userId
	 * @param borrowTitle
	 * @param borrowerName
	 * @param pageBean
	 * @throws SQLException
	 */
	public void queryAuctingDebt(long userId, String borrowTitle,
			String borrowerName, String debtStatus, PageBean pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (userId > 0) {
			condition.append(" and  alienatorId =");
			condition.append(userId);
			condition.append(" ");
		}
		if (StringUtils.isNotBlank(borrowerName)) {
			condition.append(" and  borrowerName  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowerName));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" and  borrowTitle  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowTitle));
			condition.append("%' ");
		}

		if (StringUtils.isNotBlank(debtStatus)) {
			String idStr = StringEscapeUtils.escapeSql("'" + debtStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" and  debtStatus  in(");
			condition.append(idSQL);
			condition.append(") ");
		}

		try {
			dataPage(conn, pageBean, "v_t_auction_assignmentdebt", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 申请转让中的债权
	 * 
	 * @param borrowerName
	 * @param alienatorName
	 * @param debtStatus
	 * @param pageBean
	 * @throws SQLException
	 */
	public void queryApplyDebt(String borrowerName, String alienatorName,
			String debtStatus, PageBean pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();

		if (StringUtils.isNotBlank(debtStatus)) {
			String idStr = StringEscapeUtils.escapeSql("'" + debtStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" AND  debtStatus  in(");
			condition.append(idSQL);
			condition.append(") ");
		}
		if (StringUtils.isNotBlank(borrowerName)) {
			condition.append(" AND  borrowerName  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowerName));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(alienatorName)) {
			condition.append(" AND  alienatorName  like '%");
			condition.append(StringEscapeUtils.escapeSql(alienatorName));
			condition.append("%' ");
		}

		try {
			dataPage(conn, pageBean, "v_t_assignment_debt_audit", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

	/**
	 * 查询还款债权
	 * 
	 * @param borrowId
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryDebtBacking(long borrowId,
			long userId, long investId) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> result = null;
		try {
			result = assignmentDebtDao.queryDebtBacking(conn, borrowId, userId,
					investId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 添加债务信息
	 * 
	 * @param id
	 * @param userId
	 * @param msgContent
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public long addDebtMsg(long id, Long userId, String msgContent)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long returnId = -1;
		try {
			returnId = assignmentDebtDao.addDebtMsg(conn, id, userId,
					msgContent);
			userMap = userDao.queryUserById(conn, userId);
			operationLogDao
					.addOperationLog(conn, "t_msgboard", Convert.strToStr(
							userMap.get("username"), ""), IConstants.INSERT,
							Convert.strToStr(userMap.get("lastIP"), ""), 0,
							"发布债权留言", 1);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}

		return returnId;
	}

	/**
	 * 根据Id查询债权留言板
	 * 
	 * @param id
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryDebtMSGBord(long id, PageBean pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_debt_msgbord", " * ",
					" order by  id  desc ", " and  modeId =" + id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询债权借款信息
	 * 
	 * @param borrowerName
	 * @param alienatorName
	 * @param debtStatus
	 * @throws SQLException
	 */
	public void queryAssignmentDebt(String borrowerName, String alienatorName,
			String debtStatus, PageBean pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();

		if (StringUtils.isNotBlank(debtStatus)) {
			String idStr = StringEscapeUtils.escapeSql("'" + debtStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" AND  debtStatus  in(");
			condition.append(idSQL);
			condition.append(") ");
		}
		if (StringUtils.isNotBlank(borrowerName)) {
			condition.append(" AND  borrowerName  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowerName));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(alienatorName)) {
			condition.append(" AND  alienatorName  like '%");
			condition.append(StringEscapeUtils.escapeSql(alienatorName));
			condition.append("%' ");
		}

		try {
			dataPage(conn, pageBean, "v_admin_assignment_debt_borrow", "*",
					" order by auctionEndTime desc", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 结束债权转让
	 * 
	 * @throws SQLException
	 * @throws DataException
	 */
	public long auctingDebtSuccess(long debtId, long userId, int type)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long result = -1;
		try {
			if (assignmentDebtDao.isDebtInStatus(conn, debtId, "1,2")) {
				long dealResult = auctDebtSuccess(conn, debtId);
				if (dealResult == 1) {
					if (type == 1) {
						userMap = userDao.queryUserById(conn, userId);
						operationLogDao.addOperationLog(conn,
								"t_assignment_debt", Convert.strToStr(userMap
										.get("username"), ""),
								IConstants.UPDATE, Convert.strToStr(userMap
										.get("lastIP"), ""), 0, "用户结束债权转让", 1);
					} else {
						// 查询后台管理员
						userMap = adminDao.queryAdminById(conn, userId);
						operationLogDao.addOperationLog(conn,
								"t_assignment_debt", Convert.strToStr(userMap
										.get("userName"), ""),
								IConstants.UPDATE, Convert.strToStr(userMap
										.get("lastIP"), ""), 0, "管理员结束债权转让", 2);
					}
					conn.commit();
					result = 1;
				} else {
					conn.rollback();
				}
			} else {
				conn.rollback();
			}
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
	 * 结束债权转让
	 * 
	 * @throws Exception
	 */
	private long auctDebtSuccess(Connection conn, long debtId) throws Exception {
		long result = -1;
		Map<String, String> debtMap = null;
		Date nowDate = new Date();
		Map<String, String> maxMap = null;
		Map<String, String> auctionerUserMap = null;
		Map<String, String> userMap = null;
		Map<String, String> fundRecordMap = null;
		StringBuffer notice = new StringBuffer();
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.DATE_SIMPLE);
		Map<String, String> noticeMap = null;
		Map<String, String> investMap = null;
		Map<String, String> investHistoryMap = null;
		Map<String, String> investUpdateMap = null;
		List<Map<String, Object>> auctionList = null;
		Map<String, String> userAmountMap = null;
		Map<String, String> alienatorUserMap = null;
		Map<String, String> riskMap = null;
		Map<String, String> updateDebtMap = null;
		try {
			debtMap = assignmentDebtDao.getAssignmentDebt(conn, debtId);
			/**
			 * 转让人
			 */
			long alienatorId = Convert.strToLong(debtMap.get("alienatorId"), -1);
			long borrowId = Convert.strToLong(debtMap.get("borrowId"), -1);
			/**
			 * 竞拍者
			 */
			long auctionerId = Convert.strToLong(debtMap.get("auctionerId"), -1);
			long investId = Convert.strToLong(debtMap.get("investId"), -1);

			double debtSum = Convert.strToDouble(debtMap.get("debtSum"), 0.0);
			maxMap = auctionDebtDao.queryAuctionMaxPriceAndCount(conn, debtId);
			nowDate = new Date();
			String now = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
			// -- 7 - 9
			// 查询借款信息得到借款时插入的平台收费标准
			Map<String, String> maps = borrowManageDao.queryBorrowInfo(conn,
					borrowId);
			// 得到收费标准的json代码
			String feelog = Convert.strToStr(maps.get("feelog"), "");
			Map<String, Double> feeMap = (Map<String, Double>) JSONObject
					.toBean(JSONObject.fromObject(feelog), HashMap.class);
			// 得到收费标准的说明信息
			String feestate = Convert.strToStr(maps.get("feestate"), "");
			Map<String, String> feestateMap = (Map<String, String>) JSONObject
					.toBean(JSONObject.fromObject(feestate), HashMap.class);
			// --end
			if (maxMap != null
					&& StringUtils.isNotBlank(maxMap.get("auctionCount"))
					&& !"0".equals(maxMap.get("auctionCount"))) {
				// 转让者扣除手续费
				double manageFee = debtSum
						* Convert.strToDouble(feeMap
								.get(IAmountConstants.CREDIT_TRANSFER_FEE_RATE)
								+ "", 0);
				/* costManagerDao.getCostManagerNumberByType(conn, 7); */
				// 修改转让表
				updateDebtMap = new HashMap<String, String>();
				updateDebtMap.put("auctionEndTime", now);
				updateDebtMap.put("debtStatus", "3");
				updateDebtMap.put("manageFee", manageFee + "");
				long updateCount = assignmentDebtDao.updateAssignmentDebt(conn,
						debtId, "2", updateDebtMap);
				if (updateCount != 0) {
					result = 1;

					String basePath = System.getProperty("web.root",IConstants.WEB_URL);

					long maxAuctionId = Convert.strToLong(maxMap.get("id"), -1);
					double maxPrice = Convert.strToDouble(maxMap
							.get("maxAuctionPrice"), 0.0);

					// 更新投资还款记录是债权转让的状态
					investDao.updateInvestDebtStatus(conn, investId,
							auctionerId);

					// 操作投资表
					// 添加投资历史表
					investMap = investDao.getInvest(conn, investId);
					investHistoryMap = new HashMap<String, String>();
					investHistoryMap.putAll(investMap);
					// investHistoryMap.remove("id");
					investHistoryMap.put("recivedPrincipal", investMap
							.get("hasPrincipal"));
					investHistoryMap.put("recievedInterest", investMap
							.get("hasInterest"));
					investHistoryMap.put("repayStatus", "2");
					investHistoryMap.put("recivedFI", investMap.get("hasFI"));
					investHistoryMap.put("manageFee", "0");

					investDao.addInvestHistory(conn, investHistoryMap);

					// 修改投资表
					investUpdateMap = new HashMap<String, String>();
					investUpdateMap.put("investor", auctionerId + "");
					investUpdateMap.put("hasPI", "0");

					double realAmount = (Convert.strToDouble(investMap
							.get("recivedPrincipal"), 0.0) - Convert
							.strToDouble(investMap.get("hasPrincipal"), 0.0));
					double recievedInterest = (Convert.strToDouble(investMap
							.get("recievedInterest"), 0.0) - Convert
							.strToDouble(investMap.get("hasInterest"), 0.0));
					investUpdateMap.put("realAmount", realAmount + "");
					investUpdateMap.put("recivedPrincipal", realAmount + "");
					investUpdateMap.put("recievedInterest", recievedInterest
							+ "");
					investUpdateMap.put("hasPrincipal", "0");
					investUpdateMap.put("hasInterest", "0");
					investUpdateMap.put("isDebt", "2");
					investUpdateMap.put("manageFee", "0");
					investUpdateMap.put("recivedFI", (Convert.strToDouble(
							investMap.get("recivedFI"), 0.0) - Convert
							.strToDouble(investMap.get("hasFI"), 0.0))
							+ "");
					investUpdateMap.put("hasFI", "0");

					investDao.updateInvest(conn, investId, investUpdateMap);

					// 扣除竞拍者金额
					auctionerUserMap = auctionDebtDao.getUserById(conn,
							auctionerId);

					userMap = new HashMap<String, String>();
					double usableSum = Convert.strToDouble(auctionerUserMap
							.get("usableSum"), 0.0);
					double freezeSum = Convert.strToDouble(auctionerUserMap
							.get("freezeSum"), 0.0);
					double dueinSum = Convert.strToDouble(auctionerUserMap
							.get("dueinSum"), 0.0);
					userMap.put("dueinSum", (dueinSum + debtSum) + "");
					userMap.put("freezeSum", (freezeSum - maxPrice) + "");
					userDao.updateUser(conn, auctionerId, userMap);

					// 竞拍者竞拍成功资金记录表
					userAmountMap = financeDao.queryUserAmountAfterHander(conn,
							auctionerId);
					if (userAmountMap == null) {
						userAmountMap = new HashMap<String, String>();
					}
					String borrowTitle = assignmentDebtDao.getBorrowTitle(conn,
							debtId);

					String remark = "债权转让[<a href=" + basePath
							+ "/queryDebtDetail.do?id=" + debtId
							+ " target='_blank'>" + borrowTitle + "</a>]竞拍成功扣除";
					fundRecordMap = new HashMap<String, String>();
					fundRecordMap.put("userId", auctionerId + "");
					fundRecordMap.put("fundMode", "债权转让竞拍成功扣除");
					fundRecordMap.put("handleSum", maxPrice + "");
					fundRecordMap.put("usableSum", userAmountMap
							.get("usableSum"));
					fundRecordMap.put("freezeSum", userAmountMap
							.get("freezeSum"));
					fundRecordMap.put("dueinSum",
							(Convert.strToDouble(userAmountMap.get("forPI"),
									0.0) - (realAmount + recievedInterest))
									+ "");
					fundRecordMap.put("trader", alienatorId + "");
					fundRecordMap.put("recordTime", now);
					fundRecordMap.put("remarks", remark);
					fundRecordMap.put("spending", maxPrice+"");
					fundRecordMap.put("operateType", 726 + "");
					fundRecordDao.addFundRecord(conn, fundRecordMap);

					String remarks = "债权转让[<a href=" + basePath
							+ "/queryDebtDetail.do?id=" + debtId
							+ " target='_blank'>" + borrowTitle
							+ "</a>]成功,待收金额增加";
					// 债权转让成功待收增加
					auctionerUserMap = auctionDebtDao.getUserById(conn,
							auctionerId);
					fundRecordMap = new HashMap<String, String>();
					fundRecordMap.put("userId", auctionerId + "");
					fundRecordMap.put("fundMode", "待收金额增加");
					fundRecordMap.put("handleSum",
							(realAmount + recievedInterest) + "");
					fundRecordMap.put("usableSum", userAmountMap
							.get("usableSum"));
					fundRecordMap.put("freezeSum", userAmountMap
							.get("freezeSum"));
					fundRecordMap.put("dueinSum", userAmountMap.get("forPI"));
					fundRecordMap.put("trader", alienatorId + "");
					fundRecordMap.put("recordTime", now);
					fundRecordMap.put("remarks", remarks);
					fundRecordMap.put("income", debtSum+"");
					fundRecordMap.put("operateType", 1005 + "");
					fundRecordDao.addFundRecord(conn, fundRecordMap);

					// 发送通知，通过通知模板
					Map<String, Object> informTemplateMap = getInformTemplate();

					noticeMap = new HashMap<String, String>();

					// 竞拍者消息提醒
					// 消息模版
					// 站内信
					String informTemplate = informTemplateMap.get(
							IInformTemplateConstants.SUCCESS_BID).toString();
					informTemplate = informTemplate.replace("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					informTemplate = informTemplate.replace("describe",
							"<a href=" + basePath + "/queryDebtDetail.do?id="
									+ debtId + " target=_blank>" + borrowTitle
									+ "</a>");
					informTemplate = informTemplate.replace("money", maxPrice
							+ "");
					noticeMap.put("mail", informTemplate);

					// 邮件
					String e_informTemplate = informTemplateMap.get(
							IInformTemplateConstants.E_SUCCESS_BID).toString();
					e_informTemplate = e_informTemplate.replace("date",
							DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					e_informTemplate = e_informTemplate.replace("describe",
							"<a href=" + basePath + "/queryDebtDetail.do?id="
									+ debtId + ">" + borrowTitle + "</a>");
					e_informTemplate = e_informTemplate.replace("money",
							maxPrice + "");
					noticeMap.put("email", e_informTemplate);

					// 短信
					String s_informTemplate = informTemplateMap.get(
							IInformTemplateConstants.S_SUCCESS_BID).toString();
					s_informTemplate = s_informTemplate.replace("userName",
							assignmentDebtDao.queryUserNameById(conn,
									auctionerId));
					s_informTemplate = s_informTemplate.replace("date",
							DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					s_informTemplate = s_informTemplate.replace("describe",
							borrowTitle);
					s_informTemplate = s_informTemplate.replace("money",
							maxPrice + "");
					noticeMap.put("note", e_informTemplate);

					selectedService.sendNoticeMSG(conn, auctionerId,
							"债权转让竞拍报告", noticeMap, IConstants.NOTICE_MODE_5);

					// 解除其它未中拍的竞拍者资金
					auctionList = auctionDebtDao.queryAuctionDebtByDebtId(conn,
							debtId);
					Map<Long, Double> frezeMap = new HashMap<Long, Double>(); // 记录解冻的资金
					for (Map<String, Object> map : auctionList) {
						long id = Convert.strToLong(map.get("id") + "", -1);
						long userId = Convert.strToLong(map.get("userId") + "",
								-1);
						if (id != maxAuctionId && userId != auctionerId) {
							auctionerUserMap = auctionDebtDao.getUserById(conn,
									userId);
							userMap = new HashMap<String, String>();
							usableSum = Convert.strToDouble(auctionerUserMap
									.get("usableSum"), 0.0);
							freezeSum = Convert.strToDouble(auctionerUserMap
									.get("freezeSum"), 0.0);
							double auctionPrice = Convert.strToDouble(map
									.get("auctionPrice")
									+ "", 0.0);
							// 防止解冻重复解冻金额
							if (frezeMap.containsKey(userId)) {
								double oldAcutionPrice = frezeMap.get(userId);
								if (oldAcutionPrice >= auctionPrice) {
									continue;
								} else {
									frezeMap.put(userId, auctionPrice);// 记录该用户最大竞拍值
									auctionPrice = auctionPrice
											- oldAcutionPrice;
								}

							} else {
								frezeMap.put(userId, auctionPrice);// 记录该用户竞拍
							}
							userMap.put("usableSum", (usableSum + auctionPrice)
									+ "");
							userMap.put("freezeSum", (freezeSum - auctionPrice)
									+ "");
							if (userDao.updateUser(conn, userId, userMap) > 0) {
								// 解冻资金操作记录
								userAmountMap = financeDao
										.queryUserAmountAfterHander(conn,
												userId);
								if (userAmountMap == null) {
									userAmountMap = new HashMap<String, String>();
								}
								String rms = "债权转让[<a href=" + basePath
										+ "/queryDebtDetail.do?id=" + debtId
										+ " target=_blank>" + borrowTitle
										+ "</a>]竞拍失败解冻金额";
								double usableSumAfter = Convert.strToDouble(
										userAmountMap.get("usableSum") + "", 0);
								double freezeSumAfter = Convert.strToDouble(
										userAmountMap.get("freezeSum") + "", 0);
								double forPI = Convert.strToDouble(
										userAmountMap.get("forPI") + "", 0);
								fundRecordDao.addFundRecord(conn, userId,
										"债权转让竞拍解冻", auctionPrice,
										usableSumAfter, freezeSumAfter, forPI,
										-1, rms, auctionPrice, 0.0, borrowId,
										-1, 202, 0.0);

								// 竞拍者消息提醒
								notice = new StringBuffer();

								noticeMap = new HashMap<String, String>();

								// 消息模版
								// 站内信
								informTemplate = informTemplateMap.get(
										IInformTemplateConstants.FAIL_BID)
										.toString();
								informTemplate = informTemplate.replace("date",
										DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
								informTemplate = informTemplate.replace(
										"describe", "<a href=" + basePath
												+ "/queryDebtDetail.do?id="
												+ debtId + " target=_blank>"
												+ borrowTitle + "</a>");
								informTemplate = informTemplate.replace(
										"money", auctionPrice + "");
								noticeMap.put("mail", informTemplate);

								// 邮件
								e_informTemplate = informTemplateMap.get(
										IInformTemplateConstants.E_FAIL_BID)
										.toString();
								e_informTemplate = e_informTemplate.replace(
										"date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
								e_informTemplate = e_informTemplate.replace(
										"describe", "<a href='" + basePath
												+ "/queryDebtDetail.do?id="
												+ debtId + "' target='_blank'>"
												+ borrowTitle + "</a>");
								e_informTemplate = e_informTemplate.replace(
										"money", auctionPrice + "");
								noticeMap.put("email", e_informTemplate);

								// 短信
								s_informTemplate = informTemplateMap.get(
										IInformTemplateConstants.S_FAIL_BID)
										.toString();
								s_informTemplate = s_informTemplate.replace(
										"userName",
										assignmentDebtDao.queryUserNameById(
												conn, userId));
								s_informTemplate = s_informTemplate.replace(
										"date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
								s_informTemplate = s_informTemplate.replace(
										"describe", borrowTitle);
								s_informTemplate = s_informTemplate.replace(
										"money", auctionPrice + "");
								noticeMap.put("note", e_informTemplate);

								selectedService.sendNoticeMSG(conn, userId,
										"债权转让竞拍报告", noticeMap,
										IConstants.NOTICE_MODE_5);
							}
						}
						// 回收对象
						map = null;
					}

					// 转让者待收金额减少资金记录
					userAmountMap = financeDao.queryUserAmountAfterHander(conn,
							alienatorId);
					if (userAmountMap == null) {
						userAmountMap = new HashMap<String, String>();
					}

					String remark1 = "债权转让[<a href=" + basePath
							+ "/queryDebtDetail.do?id=" + debtId
							+ " target='_blank'>" + borrowTitle
							+ "</a>]竞拍成功,待收金额减少";
					fundRecordMap = new HashMap<String, String>();
					fundRecordMap.put("userId", alienatorId + "");
					fundRecordMap.put("fundMode", "待收金额减少");
					fundRecordMap.put("handleSum",
							(realAmount + recievedInterest) + "");
					fundRecordMap.put("usableSum", userAmountMap
							.get("usableSum"));
					fundRecordMap.put("freezeSum", userAmountMap
							.get("freezeSum"));
					fundRecordMap.put("dueinSum", userAmountMap.get("forPI"));
					fundRecordMap.put("trader", auctionerId + "");
					fundRecordMap.put("recordTime", now);
					fundRecordMap.put("remarks", remark1);
					fundRecordMap.put("operateType", 1003 + "");
					fundRecordDao.addFundRecord(conn, fundRecordMap);

					// 添加转让者金额
					alienatorUserMap = auctionDebtDao.getUserById(conn,
							alienatorId);
					userMap = new HashMap<String, String>();
					usableSum = Convert.strToDouble(alienatorUserMap
							.get("usableSum"), 0.0);
					userMap.put("usableSum", (usableSum + maxPrice) + "");
					dueinSum = Convert.strToDouble(alienatorUserMap
							.get("dueinSum"), 0.0);
					userMap.put("dueinSum", (dueinSum - debtSum) + "");
					userDao.updateUser(conn, alienatorId, userMap);

					// 转让者转让成功资金记录
					userAmountMap = financeDao.queryUserAmountAfterHander(conn,
							alienatorId);
					if (userAmountMap == null) {
						userAmountMap = new HashMap<String, String>();
					}
					String remark2 = "债权转让[<a href=" + basePath
							+ "/queryDebtDetail.do?id=" + debtId
							+ " target='_blank'>" + borrowTitle + "</a>]竞拍成功收入";
					fundRecordMap = new HashMap<String, String>();
					fundRecordMap.put("userId", alienatorId + "");
					fundRecordMap.put("fundMode", "债权转让成功");
					fundRecordMap.put("handleSum", maxPrice + "");
					fundRecordMap.put("usableSum", userAmountMap
							.get("usableSum"));
					fundRecordMap.put("freezeSum", userAmountMap
							.get("freezeSum"));
					fundRecordMap.put("dueinSum", userAmountMap.get("forPI"));
					fundRecordMap.put("trader", auctionerId + "");
					fundRecordMap.put("recordTime", now);
					fundRecordMap.put("remarks", remark2);
					fundRecordMap.put("income", maxPrice + "");
					fundRecordMap.put("operateType", 201 + "");
					fundRecordDao.addFundRecord(conn, fundRecordMap);

					alienatorUserMap = auctionDebtDao.getUserById(conn,
							alienatorId);
					userMap = new HashMap<String, String>();
					usableSum = Convert.strToDouble(alienatorUserMap
							.get("usableSum"), 0.0);
					userMap.put("usableSum", (usableSum - manageFee) + "");
					userDao.updateUser(conn, alienatorId, userMap);

					fundRecordMap = new HashMap<String, String>();
					userAmountMap = financeDao.queryUserAmountAfterHander(conn,
							alienatorId);
					if (userAmountMap == null) {
						userAmountMap = new HashMap<String, String>();
					}
					String remark3 = "债权转让[<a href=" + basePath
							+ "/queryDebtDetail.do?id=" + debtId
							+ " target='_blank'>" + borrowTitle
							+ "</a>]转让手续费扣除";
					fundRecordMap.put("userId", alienatorId + "");
					fundRecordMap.put("fundMode", "债权转让手续费扣除");
					fundRecordMap.put("handleSum", manageFee + "");
					fundRecordMap.put("usableSum", userAmountMap
							.get("usableSum"));
					fundRecordMap.put("freezeSum", userAmountMap
							.get("freezeSum"));
					fundRecordMap.put("dueinSum", userAmountMap.get("forPI"));
					fundRecordMap.put("recordTime", now);
					fundRecordMap.put("remarks", remark3);
					fundRecordMap.put("spending", manageFee + "");
					fundRecordMap.put("operateType", 701 + "");
					fundRecordDao.addFundRecord(conn, fundRecordMap);

					// 转让者消息提醒
					noticeMap = new HashMap<String, String>();
					// 消息模版
					// 站内信
					informTemplate = informTemplateMap.get(
							IInformTemplateConstants.SUCCESS_CREDIT).toString();
					informTemplate = informTemplate.replace("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					informTemplate = informTemplate.replace("describe",
							"<a href=" + basePath + "/queryDebtDetail.do?id="
									+ debtId + " target=_blank>" + borrowTitle
									+ "</a>");
					informTemplate = informTemplate.replace("money", maxPrice
							+ "");
					noticeMap.put("mail", informTemplate);

					// 邮件
					e_informTemplate = informTemplateMap.get(
							IInformTemplateConstants.E_SUCCESS_CREDIT)
							.toString();
					e_informTemplate = e_informTemplate.replace("date",
							DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					e_informTemplate = e_informTemplate.replace("describe",
							"<a href=" + basePath + "/queryDebtDetail.do?id="
									+ debtId + ">" + borrowTitle + "</a>");
					e_informTemplate = e_informTemplate.replace("money",
							maxPrice + "");
					noticeMap.put("email", e_informTemplate);

					// 短信
					s_informTemplate = informTemplateMap.get(
							IInformTemplateConstants.S_SUCCESS_CREDIT)
							.toString();
					s_informTemplate = s_informTemplate.replace("userName",
							assignmentDebtDao.queryUserNameById(conn,
									alienatorId));
					s_informTemplate = s_informTemplate.replace("date",
							DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
					s_informTemplate = s_informTemplate.replace("describe",
							borrowTitle);
					s_informTemplate = s_informTemplate.replace("money",
							maxPrice + "");
					noticeMap.put("note", e_informTemplate);

					selectedService.sendNoticeMSG(conn, alienatorId, "债权转让报告",
							noticeMap, IConstants.NOTICE_MODE_5);

					// 添加风险保障金
					Map<String, String> map = riskManageDao
							.queryRiskBalance(conn);
					String riskBalance = map.get("riskBalance");
					double riskBalanceDouble = Convert.strToDouble(
							riskBalance, 0);
					String riskType = "收入";
					String resource = "债权转让手续费";
					String remark_ = "债权手续费转让";
					riskManageDao.addRisk(conn, manageFee, -1, remark_,
							riskBalanceDouble, nowDate, riskType, resource);
					//
					// // 团队长，经纪人奖励提成
					// // 转让人
					// BigDecimal money = new BigDecimal(realAmount);
					// awardService.updateMoney(conn, alienatorId, money,
					// IConstants.MONEY_TYPE_1, investId);
					// // 竞拍者
					// awardService.updateMoney(conn, auctionerId, money,
					// IConstants.MONEY_TYPE_1, investId);
					//				
					informTemplateMap = null;
				}
			} else {
				// 修改转让表
				updateDebtMap = new HashMap<String, String>();
				updateDebtMap.put("auctionEndTime", now);
				updateDebtMap.put("debtStatus", "5");
				long updateCount = assignmentDebtDao.updateAssignmentDebt(conn,
						debtId, "2", updateDebtMap);
				if (updateCount != 0) {
					result = 1;
				}
			}
			return result;
		} finally {
			debtMap = null;
			nowDate = null;
			maxMap = null;
			auctionerUserMap = null;
			userMap = null;
			fundRecordMap = null;
			notice = null;
			sf = null;
			noticeMap = null;
			investMap = null;
			investHistoryMap = null;
			investUpdateMap = null;
			auctionList = null;
			userAmountMap = null;
			alienatorUserMap = null;
			riskMap = null;
			updateDebtMap = null;
			System.gc();
		}
	}

	public void setRiskManageDao(RiskManageDao riskManageDao) {
		this.riskManageDao = riskManageDao;
	}

	/**
	 * 提前还款时处理正在竞拍中的债权
	 * 
	 * @param repayId
	 *            还款Id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public long preRepayment(Connection conn, long repayId) throws Exception {
		long result = -1;
		try {
			long borrowId = auctionDebtDao
					.queryBorrowIdByRepayId(conn, repayId);
			List<Map<String, Object>> debtList = assignmentDebtDao
					.queryAssignmentDebtIds(conn, borrowId, "1,2");
			if (debtList != null) {
				for (Map<String, Object> map : debtList) {
					long id = Convert.strToLong(map.get("id") + "", -1);
					assignmentDebt(conn, id, "1,2", 7);
				}
			}

			result = 1;
		} catch (Exception e) {
			log.error(e);
			throw e;
		}

		return result;
	}

	/**
	 * 撤销转让
	 * 
	 * @param debtId
	 * @param debtStatus
	 *            5：撤销,7：提前还款
	 * @throws SQLException
	 */
	public long cancelAssignmentDebt(long debtId, int debtStatus, long userId,
			int type) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long result = -1;
		try {
			if (assignmentDebtDao.isDebtInStatus(conn, debtId, "1,2")) {
				long dealResult = assignmentDebt(conn, debtId, "1,2",
						debtStatus);
				if (dealResult == 1) {
					if (type == 1) {
						userMap = userDao.queryUserById(conn, userId);
						operationLogDao.addOperationLog(conn,
								"t_assignment_debt", Convert.strToStr(userMap
										.get("username"), ""),
								IConstants.UPDATE, Convert.strToStr(userMap
										.get("lastIP"), ""), 0, "用户取消债权转让", 1);
					} else {
						userMap = adminDao.queryAdminById(conn, userId);
						operationLogDao.addOperationLog(conn,
								"t_assignment_debt", Convert.strToStr(userMap
										.get("userName"), ""),
								IConstants.UPDATE, Convert.strToStr(userMap
										.get("lastIP"), ""), 0, "管理员取消债权转让", 1);
					}
					conn.commit();
					result = 1;
				} else {
					conn.rollback();
				}

			} else {
				conn.commit();
			}
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
	 * 债权转让失败操作
	 * 
	 * @param conn
	 * @param debtId
	 * @param debtStatus
	 *            5：撤销,7：提前还款
	 * @throws DataException
	 * @throws SQLException
	 */
	private long assignmentDebt(Connection conn, long debtId,
			String preDebtStatus, int debtStatus) throws Exception {
		long result = -1;
		List<Map<String, Object>> auctionList = auctionDebtDao
				.queryAuctionDebtByDebtId(conn, debtId);
		String borrowTitle = assignmentDebtDao.getBorrowTitle(conn, debtId);
		String basePath = System.getProperty("web.root",IConstants.WEB_URL);

		Map<String, String> updateDebtMap = new HashMap<String, String>();
		updateDebtMap.put("auctionEndTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		updateDebtMap.put("debtStatus", debtStatus + "");
		long updateCount = assignmentDebtDao.updateAssignmentDebt(conn, debtId,
				preDebtStatus, updateDebtMap);
		if (updateCount != 0) {
			result = 1;
			if (auctionList != null) {
				Map<Long, Double> frezeMap = new HashMap<Long, Double>(); // 记录解冻的资金
				for (Map<String, Object> map : auctionList) {
					long userId = Convert.strToLong(map.get("userId") + "", -1);
					Map<String, String> auctionerUserMap = auctionDebtDao
							.getUserById(conn, userId);
					Map<String, String> userMap = new HashMap<String, String>();
					double usableSum = Convert.strToDouble(auctionerUserMap
							.get("usableSum"), 0.0);
					double freezeSum = Convert.strToDouble(auctionerUserMap
							.get("freezeSum"), 0.0);
					double auctionPrice = Convert.strToDouble(map
							.get("auctionPrice")
							+ "", 0.0);
					// 防止解冻重复解冻金额

					if (frezeMap.containsKey(userId)) {
						double oldAcutionPrice = frezeMap.get(userId);
						if (oldAcutionPrice >= auctionPrice) {
							continue;
						} else {
							frezeMap.put(userId, auctionPrice);// 记录该用户最大竞拍值
							auctionPrice = auctionPrice - oldAcutionPrice;
						}
					} else {
						frezeMap.put(userId, auctionPrice);// 记录该用户竞拍
					}
					userMap.put("usableSum", (usableSum + auctionPrice) + "");
					userMap.put("freezeSum", (freezeSum - auctionPrice) + "");

					if (userDao.updateUser(conn, userId, userMap) > 0) {
						// 解冻资金操作记录
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
						String remark = "[<a href=" + basePath
								+ "/queryDebtDetail.do?id=" + debtId
								+ " target=_blank>" + borrowTitle
								+ "</a>]竞拍失败解冻";
						fundRecordDao.addFundRecord(conn, userId, "债权转让竞拍解冻",
								auctionPrice, usableSumAfter, freezeSumAfter,
								forPI, -1, remark, auctionPrice, 0.0, -1, -1,
								202, 0.0);
						// 竞拍者消息提醒
						// 发送通知，通过通知模板
						Map<String, Object> informTemplateMap = getInformTemplate();

						Map<String, String> noticeMap = new HashMap<String, String>();

						// 消息模版
						// 站内信
						String informTemplate = informTemplateMap.get(
								IInformTemplateConstants.FAIL_BID).toString();
						if (informTemplate == null) {
							conn.rollback();
							return -1L;
						}
						informTemplate = informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						informTemplate = informTemplate.replace("describe",
								"<a href=" + basePath
										+ "/queryDebtDetail.do?id=" + debtId
										+ " target=_blank>" + borrowTitle
										+ "</a>");
						informTemplate = informTemplate.replace("money",
								auctionPrice + "");
						noticeMap.put("mail", informTemplate);

						// 邮件
						String e_informTemplate = informTemplateMap.get(
								IInformTemplateConstants.E_FAIL_BID).toString();
						if (e_informTemplate == null) {
							conn.rollback();
							return -1L;
						}
						e_informTemplate = e_informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						e_informTemplate = e_informTemplate.replace("describe",
								"<a href=" + basePath
										+ "/queryDebtDetail.do?id=" + debtId
										+ ">" + borrowTitle + "</a>");
						e_informTemplate = e_informTemplate.replace("money",
								auctionPrice + "");
						noticeMap.put("email", e_informTemplate);

						// 短信
						String s_informTemplate = informTemplateMap.get(
								IInformTemplateConstants.S_FAIL_BID).toString();
						if (s_informTemplate == null) {
							conn.rollback();
							return -1L;
						}
						s_informTemplate = s_informTemplate.replace("userName",
								assignmentDebtDao.queryUserNameById(conn,
										userId));
						s_informTemplate = s_informTemplate.replace("date",
								DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
						s_informTemplate = s_informTemplate.replace("describe",
								borrowTitle);
						s_informTemplate = s_informTemplate.replace("money",
								auctionPrice + "");
						noticeMap.put("note", e_informTemplate);

						selectedService.sendNoticeMSG(conn, userId, "债权转让竞拍报告",
								noticeMap, IConstants.NOTICE_MODE_5);
					}
				}
			}
		}

		return result;
	}

	/**
	 * 检查债权是否过期并设置过期参数
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean checkDueDebt() throws Exception {
		Connection conn = MySQL.getConnection();
		boolean result = false;
		try {
			List<Map<String, Object>> debtList = assignmentDebtDao
					.queryDueDebt(conn);
			if (debtList != null) {
				for (Map<String, Object> map : debtList) {
					if ("2".equals(map.get("debtStatus") + "")) {
						Long id = Convert.strToLong(map.get("id") + "", -1);
						auctDebtSuccess(conn, id);
					}
				}
			}
			conn.commit();
			result = true;
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
	 * 查询竞拍成功的用户和转让者
	 * 
	 * @param aid
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryDebtUserName(long aid) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = assignmentDebtDao.queryDebtUserName(conn, aid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	public Map<String, String> queryApplyDebtDetail() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = assignmentDebtDao.queryApplyDebtDetail(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return map;
	}

	public Map<String, String> queryApplySuccessDebtDetail() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = assignmentDebtDao.queryApplySuccessDebtDetail(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return map;
	}

	public Map<String, String> queryApplyFailDebtDetail() throws SQLException,
			DataException {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = assignmentDebtDao.queryApplyFailDebtDetail(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return map;
	}
	public void changeDateToStr(PageBean<Map<String, Object>> pageBean){
		List<Map<String,Object>> list = pageBean.getPage();
		if(list != null){
			Date nowDate = new Date();
			for(Map<String,Object> map : list){
				Date date = (Date)map.get("remainAuctionTime");
				map.put("remainDays", DateHelper.remainDateToString(nowDate, date));
			}
		}
	}
	
	
	
	public void queryAllDebtApp(String borrowTitle, long debtSum,
			long auctionBasePrice, long auctionMode, long isLate,
			long publishDays, String debtStatus, PageBean pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (StringUtils.isNotBlank(debtStatus)) {
			String idStr = StringEscapeUtils.escapeSql("'" + debtStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" AND  debtStatus  in(");
			condition.append(idSQL);
			condition.append(") ");
		}

		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" AND  borrowTitle  like '%");
			condition.append(StringEscapeUtils.escapeSql(borrowTitle));
			condition.append("%' ");
		}
		if (debtSum > 0) {
			if (debtSum == 1) {
				condition.append(" AND  debtSum  < 3000 ");
			} else if (debtSum == 2) {
				condition
						.append(" AND  debtSum  >= 3000 and  debtSum  < 5000 ");
			} else if (debtSum == 3) {
				condition
						.append(" AND  debtSum  >= 5000 and  debtSum  < 10000 ");
			} else if (debtSum == 4) {
				condition
						.append(" AND  debtSum  >= 10000 and  debtSum  < 20000 ");
			} else if (debtSum == 5) {
				condition
						.append(" AND  debtSum  >= 20000 and  debtSum  < 50000 ");
			} else if (debtSum == 6) {
				condition.append(" AND  debtSum  >= 50000 ");
			}
		}
		if (auctionBasePrice > 0) {
			if (auctionBasePrice == 1) {
				condition.append(" AND  auctionBasePrice  < 3000 ");
			} else if (auctionBasePrice == 2) {
				condition
						.append(" AND  auctionBasePrice  >= 3000 and  auctionBasePrice  < 5000 ");
			} else if (auctionBasePrice == 3) {
				condition
						.append(" AND  auctionBasePrice  >= 5000 and  auctionBasePrice  < 10000 ");
			} else if (auctionBasePrice == 4) {
				condition
						.append(" AND  auctionBasePrice  >= 10000 and  auctionBasePrice  < 20000 ");
			} else if (auctionBasePrice == 5) {
				condition
						.append(" AND  auctionBasePrice  >= 20000 and  auctionBasePrice  < 50000 ");
			} else if (auctionBasePrice == 6) {
				condition.append(" AND  auctionBasePrice  >= 50000 ");
			}
		}
		if (auctionMode > 0) {
			condition.append(" AND  auctionMode  =");
			condition.append(auctionMode);

		}
		if (isLate > 0) {
			condition.append(" AND  isLate  =");
			condition.append(isLate);
		}
		if (publishDays > 0) {
			String nowDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
			if (publishDays > 30) {
				condition.append(" AND datediff(now(),publishTime) >= ");
				condition.append(publishDays);
			} else {
				condition.append(" AND datediff(now(),publishTime) <= ");
				condition.append(publishDays);
			}
		}
		condition.append(" AND borrowWay in (1,2,3,5) ");
		try {
			dataPage(
					conn,
					pageBean,
					"v_t_debt_borrow_person",
					" id  , debtSum,actionTime  ,f_formatting_username( alienatorName) as alienatorName   ,  auctionBasePrice  ,debtLimit  ,  auctionMode  , alienatorId  ,  auctionerId  ,  publishTime  ,  auctionDays  ,  debtStatus  ,  borrowId  , borrowTitle  ,publisher  , annualRate  , f_formatting_username(  borrowerName ) as borrowerName ,  auctionerName  ,   auctionHighPrice  ,  creditratingIco  ,  isLate  ,  personalHead,borrowWay  ",
					" order by  publishTime  desc ", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	public void setBorrowManageDao(BorrowManageDao borrowManageDao) {
		this.borrowManageDao = borrowManageDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}
}
