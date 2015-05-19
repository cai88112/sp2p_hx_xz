package com.sp2p.service.admin;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
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
import org.springframework.web.context.ContextLoader;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.constants.IInformTemplateConstants;
import com.sp2p.dao.AccountUsersDao;
import com.sp2p.dao.FundRecordDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.admin.AccountPaymentDao;
import com.sp2p.dao.admin.FIManageDao;
import com.sp2p.dao.admin.RiskManageDao;
import com.sp2p.dao.admin.UserBankManagerDao;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_account_payment;
import com.sp2p.database.Dao.Tables.t_fundrecord;
import com.sp2p.service.FinanceService;
import com.sp2p.service.SelectedService;

public class FundManagementService extends BaseService {

	public static Log log = LogFactory.getLog(FinanceService.class);

	private FIManageDao fiManageDao;
	private FundRecordDao fundRecordDao;
	private AccountUsersDao accountUsersDao;
	private SelectedService selectedService;
	private UserBankManagerDao userBankDao;
	private RiskManageDao riskManageDao;
	private AccountPaymentDao accountPaymentDao;
	private OperationLogDao operationLogDao;
	private List<Map<String, Object>> rechargeTypes;
	private List<Map<String, Object>> results;
	private List<Map<String, Object>> checkers;
	private AdminService adminService;

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	public FIManageDao getFiManageDao() {
		return fiManageDao;
	}

	public void setFiManageDao(FIManageDao fiManageDao) {
		this.fiManageDao = fiManageDao;
	}

	public FundRecordDao getFundRecordDao() {
		return fundRecordDao;
	}

	public void setFundRecordDao(FundRecordDao fundRecordDao) {
		this.fundRecordDao = fundRecordDao;
	}

	public AccountUsersDao getAccountUsersDao() {
		return accountUsersDao;
	}

	public void setAccountUsersDao(AccountUsersDao accountUsersDao) {
		this.accountUsersDao = accountUsersDao;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public UserBankManagerDao getUserBankDao() {
		return userBankDao;
	}

	public void setUserBankDao(UserBankManagerDao userBankDao) {
		this.userBankDao = userBankDao;
	}

	public RiskManageDao getRiskManageDao() {
		return riskManageDao;
	}

	public void setRiskManageDao(RiskManageDao riskManageDao) {
		this.riskManageDao = riskManageDao;
	}

	public AccountPaymentDao getAccountPaymentDao() {
		return accountPaymentDao;
	}

	public void setAccountPaymentDao(AccountPaymentDao accountPaymentDao) {
		this.accountPaymentDao = accountPaymentDao;
	}

	/**
	 * 根据pMerBillNo查询资金记录
	 * 
	 * @param pMerBillNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryFundRecord(String pMerBillNo)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			Dao.Tables.t_fundrecord t_fundrecord = new Dao().new Tables().new t_fundrecord();
			DataSet dataSet = t_fundrecord.open(conn, "*", " pMerBillNo="
					+ pMerBillNo, "", -1, -1);
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

	/**
	 * 财务管理 充值记录查询
	 * 
	 * @return
	 */
	@SuppressWarnings("null")
	public Map<String, String> queryRechargeRecordList(
			PageBean<Map<String, Object>> pageBean, String userName,
			String reStartTime, String reEndTime, int rechargeType,
			Integer result) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态不为空
		String command = "";
		if (userName != null && !userName.equals("")) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		if (rechargeType != -100) {//
			command += " and rechargeType =" + rechargeType;
		}
		if (reStartTime != null && !reStartTime.equals("")) {
			command += " and rechargeTime >='"
					+ StringEscapeUtils.escapeSql(reStartTime) + "'";
		}
		if (reEndTime != null && !reEndTime.equals("")) {
			command += " and rechargeTime <='"
					+ StringEscapeUtils.escapeSql(reEndTime) + "'";
		}
		if (result >= 0) {
			command += " and result =" + result;
		}
		try {
			dataPage(conn, pageBean, "v_t_user_rechargedetails_list", "*",
					" order by id desc ", command);
			DataSet ds = MySQL
					.executeQuery(
							conn,
							"select sum(rechargeMoney) as rechargeMoneys,"
									+ " sum(realMoney) as realMoneys from v_t_user_rechargedetails_list where 1=1 "
									+ command.toString());
			conn.commit();
			return DataSetHelper.dataSetToMap(ds);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryRechargeFirstList(PageBean<Map<String, Object>> pageBean,
			String userName, String reStartTime, String reEndTime,
			int rechargeType) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态不为空
		String command = "";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		if (rechargeType != -100) {//
			command += " and rechargeType =" + rechargeType;
		}
		if (reStartTime != null) {
			command += " and first_recharge >='"
					+ StringEscapeUtils.escapeSql(reStartTime) + "'";
		}
		if (reEndTime != null) {
			command += " and first_recharge <='"
					+ StringEscapeUtils.escapeSql(reEndTime) + "'";
		}
		try {
			dataPage(conn, pageBean, "v_t_user_rechargefirst_lists", "*", "",
					command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryOneFirstChargeDetails(Long rechargeId,
			boolean first) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			if (first) {
				return fiManageDao.queryOneFirstChargeDetails(conn, rechargeId);
			} else {
				return fiManageDao.queryOneChargeDetails(conn, rechargeId);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryAllWithdrawList(
			PageBean<Map<String, Object>> pageBean, String userName,
			String startTime, String endTime, Integer status) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态不为空
		String command = "";
		if (StringUtils.isNotBlank(userName)) {
			command += " and name like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		if (status > 0) {//
			command += " and status =" + status;
		}
		if (StringUtils.isNotBlank(startTime)) {
			command += " and applyTime >='"
					+ StringEscapeUtils.escapeSql(startTime) + "'";
		}
		if (StringUtils.isNotBlank(endTime)) {
			command += " and applyTime <='"
					+ StringEscapeUtils.escapeSql(endTime) + "'";
		}
		try {
			dataPage(conn, pageBean, "v_t_user_withdraw_lists", "*", "",
					command);
			DataSet ds = MySQL
					.executeQuery(
							conn,
							"select sum(sum) as sums,sum(realMoney) as realMoneys,sum(poundage) as poundages from v_t_user_withdraw_lists where 1=1 "
									+ command.toString());
			conn.commit();
			return DataSetHelper.dataSetToMap(ds);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryOneWithdraw(Long wid) throws Exception,
			DataException {
		Connection conn = MySQL.getConnection();
		// 手机变更状态不为空
		try {
			return fiManageDao.queryOneWithdraw(conn, wid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryUserCashList(
			PageBean<Map<String, Object>> pageBean, String userName)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String command = "";
		String command2 = "";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'";
			command2 += " where u.username like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'";
		}
		StringBuffer cmd = new StringBuffer();
		cmd.append("(select a.id as userId,a.username,IFNULL(f.forRePaySum,0) as dueoutSum,a.usableSum,a.freezeSum,");
		cmd.append("round(sum(IFNULL(b.recivedPrincipal+b.recievedInterest-b.hasPrincipal-b.hasInterest,0)),2) as dueinSum,d.realName realName from t_user a left join t_invest b on a.id = b.investor ");
		cmd.append(" left join t_person d on d.userId=a.id left join ");
		cmd.append("(select forRePaySum,publisher from (select sum(IFNULL((c.stillPrincipal+c.stillInterest-c.hasPI+c.lateFI-c.hasFI),0)) as forRePaySum,d.publisher  from t_repayment c left join t_borrow d on c.borrowId = d.id where c.repayStatus = 1 GROUP BY d.publisher) t) f");
		cmd.append(" on f.publisher = a.id  group by a.ID,a.usableSum,a.freezeSum,f.forRePaySum,d.realName,a.username) u");
		try {
			dataPage(conn, pageBean, cmd.toString(), "*", "", command);
			DataSet ds = MySQL
					.executeQuery(
							conn,
							"select sum(u.dueoutSum) as dueoutSums, sum(u.usableSum) as usableSums,"
									+ " sum(u.freezeSum) as freezeSums, sum(u.dueinSum) as dueinSums from "
									+ cmd.toString() + command2);
			conn.commit();
			return DataSetHelper.dataSetToMap(ds);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryBackCashList(PageBean<Map<String, Object>> pageBean,
			Long userId, String checkUser, String startTime, String endTime,
			Integer type, String uname) throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			String condition = " ";
			if (userId != -100) {
				condition += " and userId=" + userId;
			}
			if (StringUtils.isNotBlank(checkUser)) {
				condition += " and userName like '%"
						+ StringEscapeUtils.escapeSql(checkUser) + "%'";
			}
			if (StringUtils.isNotBlank(startTime)) {
				condition += " and checkTime >= '"
						+ StringEscapeUtils.escapeSql(startTime) + "'";
			}
			if (StringUtils.isNotBlank(endTime)) {
				condition += " and checkTime <= '"
						+ StringEscapeUtils.escapeSql(endTime) + "'";
			}
			if (type > 0) {
				condition += " and type = " + type;
			}
			if (StringUtils.isNotBlank(uname)) {
				condition += " and uname like '%"
						+ StringEscapeUtils.escapeSql(uname) + "%'";
			}
			dataPage(conn, pageBean, "v_t_user_backrw_lists", "*",
					" order by checkTime desc", condition);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Long addBackR(Long userId, Long adminId, Integer type, double money,
			String remark, Date date, String fundMode, String addIP,
			String userName, String remarks) throws Exception {
		Connection conn = MySQL.getConnection();
		Long ret = -1L;
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.p_useraddmoneymanual(conn, ds, outParameterValues,
					userId, type, money, remarks, adminId, -1, "");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			if (ret > 0) {
				// 发送通知，通过通知模板
				Map<String, Object> informTemplateMap = (Map<String, Object>) ContextLoader
						.getCurrentWebApplicationContext()
						.getServletContext()
						.getAttribute(
								IInformTemplateConstants.INFORM_TEMPLATE_APPLICATION);

				Map<String, String> noticeMap = new HashMap<String, String>();
				// 消息模版
				// 站内信
				String informTemplate = informTemplateMap.get(
						IInformTemplateConstants.HAND_RECHARGE).toString();
				if (type == IConstants.WITHDRAW) {//
					informTemplate = informTemplateMap.get(
							IInformTemplateConstants.HAND_WITHDRAW).toString();
				}
				informTemplate = informTemplate.replace("date", DateFormatUtils
						.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				informTemplate = informTemplate.replace("money",
						new BigDecimal(money) + "");
				informTemplate = informTemplate.replace("remark", remark);
				noticeMap.put("mail", informTemplate);

				// 邮件
				String e_informTemplate = informTemplateMap.get(
						IInformTemplateConstants.E_HAND_RECHARGE).toString();
				if (type == IConstants.WITHDRAW) {//
					e_informTemplate = informTemplateMap.get(
							IInformTemplateConstants.E_HAND_WITHDRAW)
							.toString();
				}
				e_informTemplate = e_informTemplate.replace("date",
						DateFormatUtils.format(new Date(),
								"yyyy-MM-dd HH:mm:ss"));
				e_informTemplate = e_informTemplate.replace("money",
						new BigDecimal(money) + "");
				e_informTemplate = e_informTemplate.replace("remark", remark);
				noticeMap.put("email", e_informTemplate);

				// 短信
				String s_informTemplate = informTemplateMap.get(
						IInformTemplateConstants.S_HAND_RECHARGE).toString();
				if (type == IConstants.WITHDRAW) {//
					s_informTemplate = informTemplateMap.get(
							IInformTemplateConstants.S_HAND_WITHDRAW)
							.toString();
				}
				s_informTemplate = s_informTemplate.replace("userName",
						userName);
				s_informTemplate = s_informTemplate.replace("date",
						DateFormatUtils.format(new Date(),
								"yyyy-MM-dd HH:mm:ss"));
				s_informTemplate = s_informTemplate.replace("money",
						new BigDecimal(money) + "");
				s_informTemplate = s_informTemplate.replace("remark", remark);
				noticeMap.put("note", e_informTemplate);

				selectedService.sendNoticeMSG(conn, userId, "充值成功", noticeMap,
						IConstants.NOTICE_MODE_5);
				conn.commit();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return ret;
	}

	public Long addBackW(Long userId, Long adminId, Integer type, double money,
			String remark, Date date, String fundMode, String addIP,
			String userName, String remarks) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			// 更新用户金额
			long result2 = fiManageDao.updateFundrecord(conn, userId, money,
					type);
			if (result2 <= 0) {
				conn.rollback();
				return result2;
			}

			result = fiManageDao.addBackR_W(conn, userId, adminId, type, money,
					remark, date);
			if (result <= 0) {
				conn.rollback();
				return result;
			}

			// 查询待收金额
			Map<String, String> dueinMap = fiManageDao.queryDueInSum(conn,
					userId);
			double dueinSum = 0d;
			if (dueinMap != null && dueinMap.size() > 0) {
				dueinSum = Convert.strToDouble(dueinMap.get("forPI"), 0);
			}
			long result3 = -1;
			result3 = fundRecordDao.addFundRecord(conn, userId, fundMode,
					money, Convert.strToDouble(dueinMap.get("usableSum"), 0d),
					Convert.strToDouble(dueinMap.get("freezeSum"), 0d),
					dueinSum, -1L, remarks, 0.0, money, -1, -1, 502, 0.0);

			if (result3 <= 0) {
				conn.rollback();
				return result3;
			}
			// 发送通知，通过通知模板
			Map<String, Object> informTemplateMap = (Map<String, Object>) ContextLoader
					.getCurrentWebApplicationContext()
					.getServletContext()
					.getAttribute(
							IInformTemplateConstants.INFORM_TEMPLATE_APPLICATION);

			Map<String, String> noticeMap = new HashMap<String, String>();

			// 消息模版
			// 站内信
			String informTemplate = informTemplateMap.get(
					IInformTemplateConstants.HAND_RECHARGE).toString();
			if (type == IConstants.WITHDRAW) {// 扣费
				informTemplate = informTemplateMap.get(
						IInformTemplateConstants.HAND_WITHDRAW).toString();
			}
			informTemplate = informTemplate.replace("date",
					DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			informTemplate = informTemplate.replace("money", money + "");
			informTemplate = informTemplate.replace("remark", remark);
			noticeMap.put("mail", informTemplate);

			// 邮件
			String e_informTemplate = informTemplateMap.get(
					IInformTemplateConstants.E_HAND_RECHARGE).toString();
			if (type == IConstants.WITHDRAW) {// 扣费
				e_informTemplate = informTemplateMap.get(
						IInformTemplateConstants.E_HAND_WITHDRAW).toString();
			}
			e_informTemplate = e_informTemplate.replace("date",
					DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			e_informTemplate = e_informTemplate.replace("money", money + "");
			e_informTemplate = e_informTemplate.replace("remark", remark);
			noticeMap.put("email", e_informTemplate);

			// 短信
			String s_informTemplate = informTemplateMap.get(
					IInformTemplateConstants.S_HAND_RECHARGE).toString();
			if (type == IConstants.WITHDRAW) {// 扣费
				s_informTemplate = informTemplateMap.get(
						IInformTemplateConstants.S_HAND_WITHDRAW).toString();
			}
			s_informTemplate = s_informTemplate.replace("userName", userName);
			s_informTemplate = s_informTemplate.replace("date",
					DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			s_informTemplate = s_informTemplate.replace("money", money + "");
			s_informTemplate = s_informTemplate.replace("remark", remark);
			noticeMap.put("note", e_informTemplate);

			selectedService.sendNoticeMSG(conn, userId, "扣费成功", noticeMap,
					IConstants.NOTICE_MODE_5);

			conn.commit();
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

	public Map<String, String> queryR_WInfo(Long rwId) throws Exception,
			DataException {
		Connection conn = MySQL.getConnection();

		try {
			return fiManageDao.queryR_WInfo(conn, rwId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 更新资金流动信息表
	 * 
	 * @param userId
	 * @param handleSum
	 * @param usableSum
	 * @return
	 */
	public Long updateFundrecord(long userId, double money, int type)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		try {
			result = fiManageDao.updateFundrecord(conn, userId, money, type);
			if (result <= 0) {
				conn.rollback();
				return -1L;
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
		return result;
	}

	public void queryUserFundRecordList(PageBean<Map<String, Object>> pageBean,
			Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			String command = " and userId=" + userId;
			dataPage(conn, pageBean, "v_t_user_fundrecord_lists", "*",
					" order by recordTime desc", command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryAllUserFundRecordList(
			PageBean<Map<String, Object>> pageBean, String userName)
			throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer cmd = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			cmd.append(" and b.username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%'");
		}
		try {
			String fields = "a.id as id,a.userId,a.fundMode,a.handleSum,a.usableSum,a.freezeSum,a.dueinSum,a.trader,a.recordTime as recordTime,a.dueoutSum,a.totalSum,a.traderName,b.username  as username,a.spending  as spending ,a.income as income ";
			dataPage(
					conn,
					pageBean,
					"v_t_user_fundrecord_lists a LEFT JOIN t_user b on a.userId = b.id",
					fields, " order by userId desc ", cmd + "");
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 后台 提现列表
	 * 
	 * @param pageBean
	 * @param userName
	 * @throws Exception
	 * @throws Exception
	 */
	public void queryUserFundWithdrawInfo(
			PageBean<Map<String, Object>> pageBean, String userName,
			String startTime, String endTime, double sum, int status,
			long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态为空
		String command = " and userId=" + userId;
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		if (sum > 0) {
			command += " and sum =" + sum;
		}
		if (startTime != null) {
			command += " and applyTime >='"
					+ StringEscapeUtils.escapeSql(startTime) + "'";
		}
		if (endTime != null) {
			command += " and applyTime <='"
					+ StringEscapeUtils.escapeSql(endTime) + "'";
		}
		if (status > 0) {// (1 默认审核中 2 已提现 3 取消 4转账中 5 失败)
			command += " and status =" + status;
		}
		try {
			dataPage(conn, pageBean, "v_t_user_fundwithdraw_lists", "*", "",
					command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryUserFundRechargeInfo(
			PageBean<Map<String, Object>> pageBean, String startTime,
			String endTime, int status, long userId, int rechargeType)
			throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态为空
		String command = " and userId=" + userId;
		if (startTime != null) {
			command += " and rechargeTime >='"
					+ StringEscapeUtils.escapeSql(startTime) + "'";
		}
		if (endTime != null) {
			command += " and rechargeTime <='"
					+ StringEscapeUtils.escapeSql(endTime) + "'";
		}
		if (status >= 0) {// (0 失败 1 成功)
			command += " and result =" + status;
		}
		if (rechargeType > 0) {// (1 失败 2 成功)
			command += " and type =" + rechargeType;
		}
		try {
			dataPage(conn, pageBean, "v_t_user_rechargeall_lists", "*", "",
					command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 获得充值提现审核中的审核信息
	 * 
	 * @param userId
	 * @param money
	 * @param type
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public Map<String, String> queryUserRWInfo(long userId, int rs, int ws)
			throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			Map<String, String> map = fiManageDao.querySuccessRecharge(conn,
					userId, rs);
			Map<String, String> mp = fiManageDao.querySuccessBid(conn, userId);

			Map<String, String> _map = fiManageDao.querySuccessWithdraw(conn,
					userId, ws);
			if (_map == null || _map.size() <= 0) {
				map = new HashMap<String, String>();
				map.put("w_total", "0.00");
			}

			if (map == null || map.size() <= 0) {
				_map.put("r_total", "0.00");

			} else {
				_map.put("r_total", map.get("r_total"));
			}
			if (mp == null || mp.size() <= 0) {
				_map.put("real_Amount", "0.00");

			} else {
				_map.put("real_Amount", mp.get("realAmount"));
			}
			conn.commit();
			return _map;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 添加数据到资金记录表（转账成功的情况下）
	 * 
	 * @return
	 * @throws Exception
	 */
	public Long addFundRecord(Long userId, Map<String, String> map)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		try {
			result = fundRecordDao.addFundRecord(conn, userId,
					map.get("fundMode"),
					Convert.strToDouble(map.get("handleSum"), 0d),
					Convert.strToDouble(map.get("usableSum"), 0d),
					Convert.strToDouble(map.get("freezeSum"), 0d),
					Convert.strToDouble(map.get("dueinSum"), 0d), -1,
					map.get("remark"), 0.0,
					Convert.strToDouble(map.get("handleSum"), 0d), -1, -1, 801,
					Convert.strToDouble(map.get("dueOutSum"), 0d));
			if (result <= 0) {
				conn.rollback();
				return -1L;
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
		return result;
	}

	public Map<String, String> queryTransStatus(Long wid) throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			return fiManageDao.queryTransStatus(conn, wid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> updateWithdraw(Long wid, double money,
			double poundage, int status, String remark, long adminId,
			Long userId, String ipAddress) throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Procedures.p_amount_withdraw_auth(conn, ds, outParameterValues,
					userId, adminId, wid, new BigDecimal(money),
					new BigDecimal(poundage), status, ipAddress, remark, "",
					-1, "");
			result = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", outParameterValues.get(0) + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (result <= 0) {
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
			conn = null;
			ds = null;
			outParameterValues = null;
		}
		return map;
	}

	/**
	 * 查询待收金额
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public Map<String, String> queryDueInSum(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			return fiManageDao.queryDueInSum(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public List<Map<String, Object>> queryLastRecord() throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			return fiManageDao.queryLastRecord(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 描述: 提现 时间: 2014-2-13 下午02:24:03 返回值类型: Map<String,String>
	 * 
	 * @param userId
	 * @param moneyD
	 * @param dealpwd
	 * @param bankId
	 * @param type
	 * @param ipAddress
	 * @param status
	 * @param pMerBillNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> updateWithdraw1(long userId, double moneyD,
			String dealpwd, long bankId, String type, String ipAddress,
			Integer status, String pMerBillNo) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.DATE_SIMPLE);
		String date = sf.format(new Date());
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Procedures.p_amount_withdraw1(conn, ds, outParameterValues, userId,
					dealpwd, new BigDecimal(moneyD), bankId, type, ipAddress,
					date, status, pMerBillNo, -1, "");
			result = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", outParameterValues.get(0) + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (result <= 0) {
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
			conn = null;
			ds = null;
			outParameterValues = null;
		}
		return map;
	}

	public Map<String, String> updateWithdrawTransfer(Long wid, Integer status,
			Long adminId, String ipAddress, String pMerBillNo) throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Procedures.p_amount_withdraw_transfer(conn, ds, outParameterValues,
					adminId, wid, status, ipAddress, "", pMerBillNo, -1, "");
			result = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", outParameterValues.get(0) + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (result <= 0) {
				conn.rollback();
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
			conn = null;
			ds = null;
			outParameterValues = null;
		}
		return map;
	}

	// userBank
	/**
	 * 查询银行卡信息
	 * 
	 * @param pageBean
	 * @param userName
	 * @param realName
	 * @param checkUser
	 * @param moStartTime
	 * @param moEndTime
	 * @param checkStartTime
	 * @param checkEndTime
	 * @throws Exception
	 * @throws Exception
	 */
	public void queryBankCardInfos(PageBean<Map<String, Object>> pageBean,
			String userName, String realName, long checkUser,
			String moStartTime, String moEndTime, String checkStartTime,
			String checkEndTime) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态为空
		String command = " and modifiedCardStatus is null";
		if (StringUtils.isNotBlank(userName)) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		if (StringUtils.isNotBlank(realName)) {
			command += " and realName like '%"
					+ StringEscapeUtils.escapeSql(realName) + "%'";
		}
		if (checkUser != -100) {// admin的用户id目前为 -1
			command += " and checkUser =" + checkUser;
		}
		if (StringUtils.isNotBlank(moStartTime)) {
			command += " and commitTime >='"
					+ StringEscapeUtils.escapeSql(moStartTime) + "'";
		}
		if (StringUtils.isNotBlank(moEndTime)) {
			command += " and commitTime <='"
					+ StringEscapeUtils.escapeSql(moEndTime) + "'";
		}
		if (StringUtils.isNotBlank(checkStartTime)) {
			command += " and checkTime >='"
					+ StringEscapeUtils.escapeSql(checkStartTime) + "'";
		}
		if (StringUtils.isNotBlank(checkEndTime)) {
			command += " and checkTime <='"
					+ StringEscapeUtils.escapeSql(checkEndTime) + "'";
		}
		try {
			dataPage(conn, pageBean, "t_bankcard_lists", "*", "", command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询修改银行卡信息
	 * 
	 * @param pageBean
	 * @param userName
	 * @param realName
	 * @param checkUser
	 * @param cStartTime
	 * @param cEndTime
	 * @param cardStatus
	 * @throws Exception
	 * @throws Exception
	 */
	public void queryModifyBankCardInfos(
			PageBean<Map<String, Object>> pageBean, String userName,
			String realName, long checkUser, String cStartTime,
			String cEndTime, int cardStatus) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态不为空
		String command = " and modifiedCardStatus is not null";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		if (realName != null) {
			command += " and realName like '%"
					+ StringEscapeUtils.escapeSql(realName) + "%'";
		}
		if (checkUser != -100) {// admin的用户id目前为 -1
			command += " and checkUser =" + checkUser;
		}
		if (cStartTime != null) {
			command += " and modifiedTime >='"
					+ StringEscapeUtils.escapeSql(cStartTime) + "'";
		}
		if (cEndTime != null) {
			command += " and modifiedTime <='"
					+ StringEscapeUtils.escapeSql(cEndTime) + "'";
		}
		if (cardStatus > -1) {
			command += " and modifiedCardStatus = " + cardStatus;
		}
		try {
			dataPage(conn, pageBean, "t_bankcard_lists", "*", "", command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> queryOneBankCardInfo(Long bankId)
			throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			return userBankDao.queryBankCardInfos(conn, bankId, -1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 审核 银行卡
	 * 
	 * @param checkUserId
	 * @param bankId
	 * @param remark
	 * @param result
	 * @param username
	 * @param lastIP
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public Long updateBankInfo(Long checkUserId, Long bankId, String remark,
			Integer result, String username, String lastIP) throws Exception {
		Connection conn = MySQL.getConnection();
		long resultId = -1L;
		try {

			resultId = userBankDao.updateBankInfo(conn, checkUserId, bankId,
					remark, result);
			if (resultId > 0) {
				operationLogDao.addOperationLog(conn, "t_bankcard", username,
						IConstants.UPDATE, lastIP, 0, "银行卡审核", 2);
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
		return resultId;
	}

	public Long updateModifyBankInfo(Long checkUserId, Long bankId,
			String remark, Integer result, String bankName,
			String branchBankName, String bankCardNo, String date,
			boolean success, String modifiedBankCode,
			String modifiedProvinceCode, String modifiedCityCode,String modifiedCardType)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long _result = -1L;
		try {

			_result = userBankDao.updateModifyBankInfo(conn, checkUserId,
					bankId, remark, result, bankName, branchBankName,
					bankCardNo, date, success, modifiedBankCode,
					modifiedProvinceCode, modifiedCityCode,modifiedCardType);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return _result;
	}

	public Map<String, String> queryOneBank(Long bankId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {

			return userBankDao.queryOneBankInfo(conn, bankId, -1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	public Long updateChangeBank(Long bankId, String bankName,
			String subBankName, String bankCard, int status, Date date,
			boolean modify, String modifiedBankCode,
			String modifiedProvinceCode, String modifiedCityCode,String modifiedCardType)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {

			result = userBankDao.updateChangeBankInfo(conn, bankId, bankName,
					subBankName, bankCard, status, date, modify,
					modifiedBankCode, modifiedProvinceCode, modifiedCityCode,modifiedCardType);
			conn.commit();
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

	// riskManage 风险保证金
	/**
	 * @MethodName: queryRiskByCondition
	 * @Param: RiskManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-7 上午11:22:29
	 * @Return:
	 * @Descb: 查询风险保障金列表
	 * @Throws:
	 */
	public void queryRiskByCondition(String resource, String timeStart,
			String timeEnd, String riskType,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(resource)) {
			// condition.append(" and resource ='"+StringEscapeUtils.escapeSql(resource.trim())+"'");
			condition.append(" and resource  like '%"
					+ StringEscapeUtils.escapeSql(resource.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(riskType)) {
			condition.append(" and riskType ='"
					+ StringEscapeUtils.escapeSql(riskType.trim()) + "'");
		}
		if (StringUtils.isNotBlank(timeStart)) {
			condition.append(" and riskDate >='"
					+ StringEscapeUtils.escapeSql(timeStart) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			condition.append(" and riskDate <='"
					+ StringEscapeUtils.escapeSql(timeEnd) + "'");
		}
		try {
			dataPage(conn, pageBean, "v_t_risk_list_h", "*", " ",
					condition.toString());
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @throws Exception
	 * @throws Exception
	 * @MethodName: queryRiskDetailById
	 * @Param: RechargeService
	 * @Author: gang.lv
	 * @Date: 2013-4-6 下午11:10:26
	 * @Return:
	 * @Descb: 查询风险保证金详情
	 * @Throws:
	 */
	public Map<String, String> queryRiskDetailById(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = riskManageDao.queryRiskDetailById(conn, id);
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
	 * @MethodName: addRisk
	 * @Param: RiskManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-7 下午03:03:45
	 * @Return:
	 * @Descb: 手动添加风险保障金
	 * @Throws:
	 */
	public long addRisk(double amountDouble, long adminId, String remark)
			throws Exception {
		Connection conn = MySQL.getConnection();

		long result = -1L;
		try {
			Map<String, String> map = riskManageDao.queryRiskBalance(conn);
			if (map == null) {
				map = new HashMap<String, String>();
			}
			String riskBalance = map.get("riskBalance");
			double riskBalanceDouble = Convert.strToDouble(riskBalance, 0);
			Date riskDate = new Date();
			String riskType = "收入";
			String resource = "手动添加风险保障金";
			result = riskManageDao.addRisk(conn, amountDouble, adminId, remark,
					riskBalanceDouble, riskDate, riskType, resource);
			if (result <= 0) {
				conn.rollback();
				return -1;
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
		return result;
	}

	/**
	 * @MethodName: deductedRisk
	 * @Param: RiskManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-7 下午03:03:33
	 * @Return:
	 * @Descb: 手动扣除风险保障金
	 * @Throws:
	 */
	public long deductedRisk(double amountDouble, long adminId, String remark)
			throws Exception {
		Connection conn = MySQL.getConnection();

		long result = -1L;
		try {
			Map<String, String> map = riskManageDao.queryRiskBalance(conn);
			if (map == null) {
				map = new HashMap<String, String>();
			}
			String riskBalance = map.get("riskBalance");
			double riskBalanceDouble = Convert.strToDouble(riskBalance, 0);
			Date riskDate = new Date();
			String riskType = "支出";
			String resource = "手动扣除风险保障金";
			result = riskManageDao.deductedRisk(conn, amountDouble, adminId,
					remark, riskBalanceDouble, riskDate, riskType, resource);
			if (result <= 0) {
				conn.rollback();
				return -1;
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
		return result;
	}

	// AccountPayment 支付方式

	/**
	 * 增加支付方式
	 * 
	 * @param conn
	 * @param name
	 * @param nid
	 * @param status
	 * @param litpic
	 * @param style
	 * @param config
	 * @param description
	 * @param order
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public long addAccountPayment(String name, String nid, long status,
			String litpic, int style, String config, String description,
			int order) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			result = accountPaymentDao.addAccountPayment(conn, name, nid,
					status, litpic, style, config, description, order);
			conn.commit();
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
	 * 分页查询所有
	 * 
	 * @param conn
	 * @param pageBean
	 * @throws Exception
	 * @throws Exception
	 */
	public void queryAccountPaymentPage(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			accountPaymentDao.queryAccountPaymentPage(conn, pageBean);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 修改
	 * 
	 * @param conn
	 * @param id
	 * @param name
	 * @param nid
	 * @param status
	 * @param litpic
	 * @param style
	 * @param config
	 * @param description
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public long updateAccountPaymentPage(long id, String name, String litpic,
			String config, String description, int order) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			result = accountPaymentDao.updateAccountPaymentPage(conn, id, name,
					litpic, config, description, order);
			conn.commit();
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
	 * 删除
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public long deleteAccountPaymentPage(long id, long status) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			result = accountPaymentDao.deleteAccountPaymentPage(conn, id,
					status);
			conn.commit();
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
	 * 根据ID 查询
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public Map<String, String> queryAccountPaymentById(String nid)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = accountPaymentDao.queryAccountPaymentById(conn, nid);
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

	public static Map<String, String> queryAccountPaymentByIdS(String nid)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			// map = accountPaymentDao.queryAccountPaymentById(conn, nid);
			Dao.Tables.t_account_payment t_account_payment = new Dao().new Tables().new t_account_payment();
			DataSet ds = t_account_payment.open(conn, " * ", " nid = '"
					+ StringEscapeUtils.escapeSql(nid) + "'", "", -1, -1);
			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
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
	 * 查询所有支付信息
	 * 
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryAccountPaymentList() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		try {
			mapList = accountPaymentDao.queryAccountPaymentList(conn);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return mapList;
	}

	public void changeFigure(PageBean<Map<String, Object>> pageBean) {
		List<Map<String, Object>> ll = pageBean.getPage();
		if (ll != null && ll.size() > 0) {// result rechargeType 中文显示
			for (Map<String, Object> mp : ll) {
				if (mp.get("rechargeType") != null) {
					String typeId = mp.get("rechargeType").toString();
					for (Map<String, Object> cc : this.getRechargeTypes()) {
						if (cc.get("typeId").toString().equals(typeId)) {
							mp.put("rechargeType", cc.get("typeValue"));
							break;
						}
					}
				}
				if (mp.get("result") != null) {
					String resultId = mp.get("result").toString();
					for (Map<String, Object> cc : this.getResults()) {
						if (cc.get("resultId").toString().equals(resultId)) {
							if (resultId.equals(0 + "")) {// 失败
								mp.put("realMoney", "0.00");
							}
							mp.put("result", cc.get("resultValue"));
							break;
						}
					}
				}
			}
		}
	}

	public void changeFigure2(PageBean<Map<String, Object>> pageBean) {
		List<Map<String, Object>> list = pageBean.getPage();
		if (list != null) {

			for (Map<String, Object> map : list) {

				if (map.get("type").toString().equals("1")) {
					map.put("type", "手动充值");
				} else {
					map.put("type", "手动扣费");
				}
			}
		}
	}

	public void changeTraderName(PageBean<Map<String, Object>> pageBean) {
		List<Map<String, Object>> lists = pageBean.getPage();
		if (lists != null) {
			for (Map<String, Object> map : lists) {
				// 从后台管理员表中查询用户信息
				if (map.get("traderName") == null) {
					map.put("traderName", IConstants.OPERATOR_ONLINE);
				}
			}
		}
	}

	public void changeNumToName(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		checkers = getCheckers();
		List<Map<String, Object>> ll = pageBean.getPage();
		if (ll != null && ll.size() > 0) {
			for (Map<String, Object> mp : ll) {
				if (mp.get("checkUser") != null) {
					String chechUser = mp.get("checkUser").toString();
					for (Map<String, Object> cc : checkers) {
						if (cc.get("id").toString().equals(chechUser)) {
							mp.put("checkUser", cc.get("userName"));
							break;
						}
					}
				}
			}
		}
	}

	public List<Map<String, Object>> getCheckers() throws Exception {
		if (checkers == null) {
			// 加载审核人员列表
			checkers = adminService.queryAdministors(IConstants.ADMIN_ENABLE);
		}
		return checkers;
	}

	public void setCheckers(List<Map<String, Object>> checkers) {
		this.checkers = checkers;
	}

	public List<Map<String, Object>> getRechargeTypes() {
		if (rechargeTypes == null) {
			rechargeTypes = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			mp = new HashMap<String, Object>();
			mp.put("typeId", 1);
			mp.put("typeValue", "支付宝支付");
			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 2);
			mp.put("typeValue", "环迅支付");
			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 3);
			mp.put("typeValue", "国付宝");
			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 4);
			mp.put("typeValue", "线下充值");
			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 5);
			mp.put("typeValue", "手工充值");
			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 6);
			mp.put("typeValue", "虚拟充值");
			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 7);
			mp.put("typeValue", "奖励充值");
			rechargeTypes.add(mp);

		}
		return rechargeTypes;
	}

	public void setRechargeTypes(List<Map<String, Object>> rechargeTypes) {
		this.rechargeTypes = rechargeTypes;
	}

	public List<Map<String, Object>> getResults() {
		if (results == null) {
			results = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			mp = new HashMap<String, Object>();
			mp.put("resultId", -100);
			mp.put("resultValue", "全部");
			results.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("resultId", 1);
			mp.put("resultValue", "成功");
			results.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("resultId", 0);
			mp.put("resultValue", "失败");
			results.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("resultId", 2);
			mp.put("resultValue", "审核中");
			results.add(mp);
		}
		return results;
	}

	public static void main(String[] args) {
		BigDecimal money = new BigDecimal(10000000);
		log.info(String.valueOf(money));
	}
}
