package com.sp2p.service.admin;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.web.context.ContextLoader;

import com.fp2p.helper.AmountHelper;
import com.sp2p.dao.BorrowDao;
import com.sp2p.dao.NoticeTaskDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.RepaymentRecordDao;
import com.sp2p.dao.FundRecordDao;
import com.sp2p.dao.UserDao;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.services.QianduoduoPayUtil;
import com.shove.vo.PageBean;
import com.shove.web.action.IPaymentAction;
import com.shove.web.action.IPaymentUtil;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.constants.IInformTemplateConstants;
import com.sp2p.dao.AccountUsersDao;
import com.sp2p.dao.InvestDao;
import com.sp2p.dao.RepamentDao;
import com.sp2p.dao.admin.AdminDao;
import com.sp2p.dao.admin.AuditExperienceMoneyDao;
import com.sp2p.dao.admin.BorrowManageDao;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.database.Dao.Views.intentionfund_user;
import com.sp2p.service.AwardService;
import com.sp2p.service.SelectedService;
import com.sp2p.service.UserIntegralService;
import com.sp2p.service.UserService;

/**
 * @ClassName: BorrowManageService.java
 * @Author: gang.lv
 * @Date: 2013-3-10 下午10:07:28
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 后台借款管理业务处理
 */
public class BorrowManageService extends BaseService {

	public static Log log = LogFactory.getLog(BorrowManageService.class);

	private BorrowManageDao borrowManageDao;
	private SelectedService selectedService;
	private AwardService awardService;
	private InvestDao investDao;
	private AccountUsersDao accountUsersDao;
	private RepamentDao repamentDao;
	private UserDao userDao;
	private FundRecordDao fundRecordDao;
	private RepaymentRecordDao repaymentRecordDao;
	private AdminDao adminDao;
	private BorrowDao borrowDao;
	private PlatformCostService platformCostService;
	private NoticeTaskDao noticeTaskDao;
	private OperationLogDao operationLogDao;
	private FundManagementService fundManagementService;
	private UserIntegralService userIntegralService;
	private UserService userService;
	private AuditExperienceMoneyDao auditExperienceMoneyDao;

	public AuditExperienceMoneyDao getAuditExperienceMoneyDao() {
		return auditExperienceMoneyDao;
	}

	public void setAuditExperienceMoneyDao(
			AuditExperienceMoneyDao auditExperienceMoneyDao) {
		this.auditExperienceMoneyDao = auditExperienceMoneyDao;
	}

	public UserIntegralService getUserIntegralService() {
		return userIntegralService;
	}

	public void setUserIntegralService(UserIntegralService userIntegralService) {
		this.userIntegralService = userIntegralService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	/**
	 * @MethodName: queryAllCirculationRepayRecordByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-5-23 下午11:24:18
	 * @Return:
	 * @Descb: 根据条件查询流转标还款记录
	 * @Throws:
	 */
	public void queryAllCirculationRepayRecordByCondition(String userName,
			int borrowStatus, String borrowTitle,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		String resultFeilds = " a.*";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and a.username  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(borrowTitle)) {
			condition.append(" and a.borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(borrowTitle.trim())
					+ "','%')");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowStatus) {
			condition.append(" and borrowStatus =" + borrowStatus);
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_circulation_repayrecord a",
					resultFeilds, " order by a.id asc", condition.toString());
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
	 * @MethodName: updateBorrowCirculationStatus
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-5-20 下午03:21:21
	 * @Return:
	 * @Descb: 更新流转标状态
	 * @Throws:
	 */
	public long updateBorrowCirculationStatus(long borrowId, long reciverId,
			long statusLong, String auditOpinion, Long adminId,
			String basePath, Map<String, Object> platformCostMap)
			throws Exception {
		Connection conn = MySQL.getConnection();
		int circulationStatus = -1;
		double borrowSum = 0;
		double yearRate = 0;
		int deadline = 0;
		int isDayThe = 1;
		Long result = -1L;
		try {
			Map<String, String> borrowMap = borrowManageDao.queryBorrowInfo(
					conn, borrowId);
			if (borrowMap == null)
				borrowMap = new HashMap<String, String>();
			borrowSum = Convert.strToDouble(borrowMap.get("borrowAmount") + "",
					0);
			yearRate = Convert.strToDouble(borrowMap.get("annualRate") + "", 0);
			deadline = Convert.strToInt(borrowMap.get("deadline") + "", 0);
			isDayThe = Convert.strToInt(borrowMap.get("isDayThe") + "", 1);
			// 处理流转标
			result = borrowManageDao.updateBorrowCirculationStatus(conn,
					borrowId, statusLong, auditOpinion, circulationStatus, 10);
			if (result > 0) {
				// 审核通过添加还款记录
				if (statusLong == 2) {
					// AmountUtil au = new AmountUtil();
					List<Map<String, Object>> rateCalculateOneList = AmountHelper
							.rateCalculateOne(borrowSum, yearRate, deadline,
									isDayThe);
					for (Map<String, Object> oneMap : rateCalculateOneList) {
						String repayPeriod = oneMap.get("repayPeriod") + "";
						String repayDate = oneMap.get("repayDate") + "";
						// 添加还款记录,还款金额和利息在投资时进行累加
						result = borrowManageDao.addRepayRecord(conn,
								repayPeriod, 0, 0, borrowId, 0, 0, repayDate);
					}
				}
			}
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

	/**
	 * @MethodName: queryBorrowCirculationDetailById
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-5-20 下午02:52:55
	 * @Return:
	 * @Descb: 查询流转标详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowCirculationDetailById(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowCirculationDetailById(conn, id);
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
	 * @MethodName: queryCirculationRepayRecord
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-7-23 下午07:37:39
	 * @Return:
	 * @Descb: 查询流转标还款记录
	 * @Throws:
	 */
	public List<Map<String, Object>> queryCirculationRepayRecord(long borrowId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = null;
		try {
			list = borrowManageDao.queryCirculationRepayRecord(conn, borrowId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * @MethodName: queryAllCirculationByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-5-20 上午11:37:27
	 * @Return:
	 * @Descb: 查询流转标借款
	 * @Throws:
	 */
	public void queryAllCirculationByCondition(String userName, int borrowWay,
			int borrowStatus, String undertaker,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		String resultFeilds = " a.*,b.userid,b.counts,c.userName as undertakerName ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and a.username  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(undertaker)) {
			condition
					.append(" and c.userName  LIKE CONCAT('%','"
							+ StringEscapeUtils.escapeSql(undertaker.trim())
							+ "','%')");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowStatus) {
			condition.append(" and a.borrowStatus =" + borrowStatus);
		}
		condition.append(" and a.borrowShow=2");
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" v_t_borrow_circulation a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userid=b.userid"
							+ " left join t_admin c on a.undertaker=c.id",
					resultFeilds, " order by a.borrowStatus asc,a.id desc",
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
	 * @MethodName: queryBorrowFistAuditByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:09:16
	 * @Return:
	 * @Descb: 查询后台借款管理中的初审借款记录
	 * @Throws:
	 */
	public void queryBorrowFistAuditByCondition(String userName, int borrowWay,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();

		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and borrowWay =" + borrowWay);
		}

		String filed = " a.*,b.counts";

		String table = "(select  "
				+ filed
				+ " from v_t_borrow_h_firstaudit a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userId=b.userid "
				+ " INNER JOIN v_t_user_approve_lists c on a.userId = c.uid where a.borrowWay >2 and c.counts = 5 "
				+ "UNION ALL SELECT  "
				+ filed
				+ " from v_t_borrow_h_firstaudit a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth "
				+ "where auditStatus = 3  GROUP BY userid) b ON a.userId=b.userid   "
				+ "INNER JOIN v_t_user_base_approve_lists d  on a.userId=d.uuid where a.borrowWay <3 and d.auditStatus=3) t ";
		// ----
		Connection conn = MySQL.getConnection();
		try {
			// 秒还净值个人资料通过审核即可 其它借款需要个人资料+工作认证+5项基本认证
			dataPage(conn, pageBean, table,
			// ---
					resultFeilds, " order by id desc ", condition.toString());
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
	 * add by houli 查询等待资料审核的数据
	 * 
	 * @param userName
	 * @param borrowWay
	 * @param pageBean
	 * @throws Exception
	 * @throws DataException
	 */
	public void queryBorrowWaitingAuditByCondition(String userName,
			int borrowWay, PageBean<Map<String, Object>> pageBean)
			throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();

		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and borrowWay =" + borrowWay);
		}
		String filed = "a.id,a.userId,a.username,a.realName,b.counts as counts,a.borrowWay,a.borrowTitle,"
				+ "a.borrowAmount,a.annualRate,a.deadline,a.raiseTerm,a.isDayThe ,a.borrowShow";
		String table = " (select  "
				+ filed
				+ " from v_t_borrow_h_firstaudit a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userId=b.userid "

				+ " inner join v_t_user_un_approve_lists c on a.userid = c.uid where a.borrowway >2 "
				+ "union all select  "
				+ filed
				+ " from v_t_borrow_h_firstaudit a left join (select userid,count(1) as counts from t_materialsauth "
				+ "where auditstatus = 3  group by userid) b on a.userid=b.userid   "
				+ "inner join v_t_user_base_approve_lists d  on a.userid=d.uuid where a.borrowway <3 and d.auditstatus=1) t";

		Connection conn = MySQL.getConnection();
		try {
			// 秒还净值个人资料通过审核即可 其它借款需要个人资料+工作认证+5项基本认证
			dataPage(conn, pageBean, table, resultFeilds, "order by  id desc ",
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
	 * @MethodName: queryBorrowFistAuditDetailById
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:24:35
	 * @Return:
	 * @Descb: 后台借款初审中的借款详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowFistAuditDetailById(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowFistAuditDetailById(conn, id);
			map.put("mailContent", "");
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
	 * @MethodName: queryBorrowTenderInByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:09:16
	 * @Return:
	 * @Descb: 查询后台借款管理中的招标中借款记录
	 * @Throws:
	 */
	public void queryBorrowTenderInByCondition(String userName, int borrowWay,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and borrowWay =" + borrowWay);
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" v_t_borrow_h_tenderin a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userid=b.userid ",
					resultFeilds, " order by id desc", condition.toString());
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
	 * @MethodName: queryBorrowTenderInDetailById
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:24:35
	 * @Return:
	 * @Descb: 后台借款招标中的借款详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowTenderInDetailById(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> mapNotick = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowTenderInDetailById(conn, id);
			if (map != null) {
				long userId = Convert.strToLong(map.get("userId"), -1L);
				mapNotick = noticeTaskDao.queryNoticeTask(conn, userId, id);
				if (mapNotick == null) {
					Map<String, String> maps = noticeTaskDao
							.queryNoticeTasklog(conn, userId, id);
					if (maps == null) {
						maps = new HashMap<String, String>();
					}
					map.put("mailContent",
							Convert.strToStr(maps.get("mail_info") + "", ""));
				} else {
					map.put("mailContent", Convert.strToStr(
							mapNotick.get("mail_info") + "", ""));
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
	 * @MethodName: queryBorrowFullScaleByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:09:16
	 * @Return:
	 * @Descb: 查询后台借款管理中的满标借款记录
	 * @Throws:
	 */
	public void queryBorrowFullScaleByCondition(String userName, int borrowWay,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and borrowWay =" + borrowWay);
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" v_t_borrow_h_fullscale a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userid=b.userid ",
					resultFeilds, " order by id desc", condition.toString());
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
	 * @MethodName: queryBorrowFullScaleDetailById
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:24:35
	 * @Return:
	 * @Descb: 后台借款满标的借款详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowFullScaleDetailById(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> mapNotick = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowFullScaleDetailById(conn, id);
			if (map != null) {
				long userId = Convert.strToLong(map.get("userId"), -1L);
				mapNotick = noticeTaskDao.queryNoticeTask(conn, userId, id);
				if (mapNotick == null) {
					Map<String, String> maps = noticeTaskDao
							.queryNoticeTasklog(conn, userId, id);
					map.put("mailContent",
							Convert.strToStr(maps.get("mail_info") + "", ""));
				} else {
					map.put("mailContent", Convert.strToStr(
							mapNotick.get("mail_info") + "", ""));
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
	 * @MethodName: queryBorrowFlowMarkByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:09:16
	 * @Return:
	 * @Descb: 查询后台借款管理中的流标借款记录
	 * @Throws:
	 */
	public void queryBorrowFlowMarkByCondition(String userName, int borrowWay,
			String timeStart, String timeEnd,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and borrowWay =" + borrowWay);
		}
		if (StringUtils.isNotBlank(timeStart)) {
			condition.append(" and publishTime >='"
					+ StringEscapeUtils.escapeSql(timeStart) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			condition.append(" and publishTime <='"
					+ StringEscapeUtils.escapeSql(timeEnd) + "'");
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" v_t_borrow_h_flowmark a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userid=b.userid ",
					resultFeilds, " order by id desc", condition.toString());
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
	 * add by houli 获得所有等待资料审核的借款
	 * 
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryAllWaitingBorrow() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> returnMap = new ArrayList<Map<String, Object>>();
		try {
			returnMap = borrowManageDao.queryAllWaitingBorrow(conn);
			conn.commit();
			return returnMap;
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
	 * @MethodName: queryBorrowFlowMarkDetailById
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:24:35
	 * @Return:
	 * @Descb: 后台借款流标的借款详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowFlowMarkDetailById(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowFlowMarkDetailById(conn, id);
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
	 * @MethodName: queryBorrowAllByCondition
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:09:16
	 * @Return:
	 * @Descb: 查询后台借款管理中的借款记录
	 * @Throws:
	 */
	public void queryBorrowAllByCondition(String userName, int borrowWay,
			int borrowStatus, PageBean<Map<String, Object>> pageBean)
			throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and borrowWay =" + borrowWay);
		}
		if (IConstants.DEFAULT_NUMERIC != borrowStatus) {
			condition.append(" and borrowStatus =" + borrowStatus);
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" v_t_borrow_h a LEFT JOIN (SELECT userid,COUNT(1) AS counts FROM t_materialsauth where auditStatus = 3 GROUP BY userid) b ON a.userid=b.userid ",
					resultFeilds, " order by id desc", condition.toString());
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
	 * @MethodName: queryBorrowAllDetailById
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:24:35
	 * @Return:
	 * @Descb: 后台借款的借款详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowAllDetailById(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowAllDetailById(conn, id);
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
	 * @throws DataException
	 * @MethodName: updateBorrowFistAuditStatus
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-13 下午01:19:48
	 * @Return:
	 * @Descb: 更新初审中的借款状态
	 * @Throws:
	 */
	public Long updateBorrowFistAuditStatus(long id, long reciver, int status,
			String msg, String auditOpinion, long sender, String basePath)
			throws Exception {
		Long result = -1L;
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		Map<String, String> map_ret = new HashMap<String, String>();
		Map<String, String> adminMap = new HashMap<String, String>();
		Connection conn = MySQL.getConnection();
		// 得到管理员信息
		adminMap = adminDao.queryAdminById(conn, sender);
		if (status == IConstants.BORROW_STATUS_2) {

			try {
				Procedures.p_borrow_audit(conn, ds, outParameterValues, id,
						sender, status, msg, auditOpinion, basePath,
						new Date(), 0, "");
				map_ret.put("out_ret", outParameterValues.get(0) + "");
				map_ret.put("out_desc", outParameterValues.get(1) + "");
				result = Convert.strToLong(outParameterValues.get(0) + "", -1);
				if (result < 1) {
					conn.rollback();
					return -1L;
				} else {
					// 添加操作日志
					operationLogDao.addOperationLog(conn, "t_borrow",
							Convert.strToStr(adminMap.get("userName"), ""),
							IConstants.UPDATE,
							Convert.strToStr(adminMap.get("lastIP"), ""), 0,
							"初审通过", 2);
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
		} else {
			conn.close();
			// 撤消初审中的借款
			result = reBackBorrowFistAudit(id, sender, basePath, msg,
					auditOpinion, null);
			// Procedures.p_borrow_cancel(conn, ds, outParameterValues, id,
			// sender, status, auditOpinion, basePath, 0, "");
			// map_ret.put("out_ret", outParameterValues.get(0)+"");
			// map_ret.put("out_desc", outParameterValues.get(1)+"");
			// 添加操作日志
			// operationLogDao.addOperationLog(conn, "t_borrow",
			// Convert.strToStr(adminMap.get("userName"), ""),
			// IConstants.UPDATE, Convert.strToStr(adminMap.get("lastIP"), ""),
			// 0, "管理员撤销借款", 2);
		}
		return result;
	}

	/**
	 * @throws DataException
	 * @MethodName: updateBorrowTenderInStatus
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-13 下午01:20:27
	 * @Return:
	 * @Descb: 更新招标中的借款状态
	 * @Throws:
	 */
	public Long updateBorrowTenderInStatus(long id, String auditOpinion)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		try {
			result = borrowManageDao.updateBorrowTenderInStatus(conn, id,
					auditOpinion);
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

	/**
	 * @throws Exception
	 * @throws DataException
	 * @throws Exception
	 * @MethodName: reBackBorrowTenderIn
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-13 下午08:40:22
	 * @Return:
	 * @Descb: 撤消借款
	 * @Throws:
	 */
	public long reBackBorrow(long id, long aId, String basePath,
			String pMerBillNo) throws Exception {
		Connection conn = MySQL.getConnection();
		long returnId = -1;
		ContextLoader
				.getCurrentWebApplicationContext()
				.getServletContext()
				.getAttribute(
						IInformTemplateConstants.INFORM_TEMPLATE_APPLICATION);
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		Map<String, String> map_ret = new HashMap<String, String>();
		Map<String, String> adminMap = new HashMap<String, String>();
		try {
			Procedures.p_borrow_cancel(conn, ds, outParameterValues, id, aId,
					6, "", basePath, pMerBillNo, -1, "");
			map_ret.put("out_ret", outParameterValues.get(0) + "");
			map_ret.put("out_desc", outParameterValues.get(1) + "");
			returnId = Convert.strToLong(outParameterValues.get(0) + "", -1);
			if (returnId < 1) {
				conn.rollback();
				return -1L;
			}
			// 添加操作日志
			adminMap = adminDao.queryAdminById(conn, aId);
			operationLogDao.addOperationLog(conn, "t_borrow",
					Convert.strToStr(adminMap.get("userName"), ""),
					IConstants.UPDATE,
					Convert.strToStr(adminMap.get("lastIP"), ""), 0, "管理员撤销借款",
					2);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			return -1L;
		} finally {
			conn.close();
		}
		return returnId;
	}

	/**
	 * 满标审核前判断处理.
	 * 
	 * @Title: borrowAuthFullscale
	 * @param
	 * @return long 返回类型
	 * @throws SQLException
	 */
	public Map<String, String> borrowAuthFullscale(long id, long status)
			throws SQLException {
		long ret = -1;
		Map<String, String> map = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		Connection conn = MySQL.getConnection();
		DataSet ds = new DataSet();
		// 满标审核前判断处理
		try {
			Procedures.p_borrow_auth_fullscale(conn, ds, outParameterValues,
					id, status, -1, "", new BigDecimal(0.00), new BigDecimal(
							0.00), 0, 0, 0);
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			return map;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 使用乾多多支付处理审核
	 * 
	 * @Title: updateBorrowFullScaleStatusUserQianduoduo
	 * @param
	 * @author yinzisong
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	public Map<String, String> updateBorrowFullScaleStatusUserQianduoduo(
			long id, long status, String auditOpinion, long adminId,
			String basePath, String tradeNo, String fundOrderNo)
			throws Exception {
		Map<String, String> fundMap = fundManagementService
				.queryFundRecord(fundOrderNo);
		Map<String, String> map = new HashMap<String, String>();
		log.info("fundMap值:" + fundMap);
		if (fundMap == null) {
			Connection conn = MySQL.getConnection();
			int isDayThe = 0;
			double investFeeRate = 0;// 投资管理费
			double borrowFeeRateOne = 0;// 秒还借款管理费
			double borrowInmonthFeeRateOne = 0;// 净值借款2个月内管理费率
			double borrowOutmonthFeeRateOne = 0;// 净值借款2个月外管理费率
			double borrowDayFeeRateOne = 0;// 净值借款天标管理费率
			double borrowInmonthFeeRateTwo = 0;// 信用借款2个月内管理费率
			double borrowOutmonthFeeRateTwo = 0;// 信用借款2个月外管理费率
			double borrowDayFeeRateTwo = 0;// 信用借款天标管理费率
			double borrowInmonthFeeRateThree = 0;// 机构担保借款2个月内管理费率
			double borrowOutmonthFeeRateThree = 0;// 机构担保借款2个月外管理费率
			double borrowDayFeeRateThree = 0;// 机构担保借款天标管理费率
			double borrowInmonthFeeRateFour = 0;// 实地考察借款2个月内管理费率
			double borrowOutmonthFeeRateFour = 0;// 实地考察借款2个月外管理费率
			double borrowDayFeeRateFour = 0;// 实地考察借款天标管理费率
			double borrowAmount = 0;
			String identify = id + "_" + System.currentTimeMillis() + "";
			long ret = -1;
			DataSet ds = new DataSet();

			Map<String, String> adminMap = new HashMap<String, String>();
			List<Object> outParameterValues = new ArrayList<Object>();
			List<Object> outParameters = new ArrayList<Object>();

			try {
				// 满标审核前判断处理
				Procedures.p_borrow_auth_fullscale(conn, ds,
						outParameterValues, id, status, -1, "", new BigDecimal(
								0.00), new BigDecimal(0.00), 0, 0, 0);
				ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
				if (ret <= 0) {
					map.put("ret", ret + "");
					map.put("ret_desc", outParameterValues.get(1) + "");
					conn.rollback();
					return map;
				}
				// 审核通过才生成还款记录
				if (ret == 4) {
				
					borrowAmount = Convert.strToDouble(
							outParameterValues.get(2) + "", 0);
					double annualRate = Convert.strToDouble(
							outParameterValues.get(3) + "", 0);
					
					//如果是老米，启用老米护盾计划，
					
					int deadline = Convert.strToInt(outParameterValues.get(4)
							+ "", 0);
					isDayThe = Convert.strToInt(outParameterValues.get(5) + "",
							1);
					int paymentMode = Convert.strToInt(
							outParameterValues.get(6) + "", 1);

					// 生成还款记录
					List<Map<String, Object>> repayMapList = null;
					// AmountUtil au = new AmountUtil();
					if (paymentMode == 1) {
						// 按月等额还款
						repayMapList = AmountHelper.rateCalculateMonth(
								borrowAmount, annualRate, deadline, isDayThe);
					} else if (paymentMode == 2) {
						// 先息后本还款
						repayMapList = AmountHelper.rateCalculateSum(
								borrowAmount, annualRate, deadline, isDayThe);
					} else if (paymentMode == 3) {
						// 秒还还款
						repayMapList = AmountHelper.rateSecondsSum(
								borrowAmount, annualRate, deadline);
					}// add by c_j 13.07.25增加一次性还款
					else if (paymentMode == 4) {
						repayMapList = AmountHelper.rateCalculateOne(
								borrowAmount, annualRate, deadline, isDayThe);
					}
					String repayPeriod = ""; // 还款期数
					double stillPrincipal = 0; // 应还本金
					double stillInterest = 0; // 应还利息
					double principalBalance = 0; // 剩余本金
					double interestBalance = 0; // 剩余利息
					double totalSum = 0; // 本息余额
					double totalAmount = 0; // 还款总额
					double mRate = 0; // 月利率
					String repayDate = "";
					int count = 1;
					for (Map<String, Object> paymentMap : repayMapList) {
						repayPeriod = paymentMap.get("repayPeriod") + "";
						stillPrincipal = Convert.strToDouble(
								paymentMap.get("stillPrincipal") + "", 0);
						stillInterest = Convert.strToDouble(
								paymentMap.get("stillInterest") + "", 0);
						principalBalance = Convert.strToDouble(
								paymentMap.get("principalBalance") + "", 0);
						interestBalance = Convert.strToDouble(
								paymentMap.get("interestBalance") + "", 0);
						totalSum = Convert.strToDouble(
								paymentMap.get("totalSum") + "", 0);
						totalAmount = Convert.strToDouble(
								paymentMap.get("totalAmount") + "", 0);
						repayDate = paymentMap.get("repayDate") + "";
						mRate = Convert.strToDouble(paymentMap.get("mRate")
								+ "", 0);
						// 添加预还款记录
						ret = repamentDao.addPreRepament(conn, id, identify,
								repayPeriod, stillPrincipal, stillInterest,
								principalBalance, interestBalance, totalSum,
								totalAmount, mRate, repayDate, count);
						count++;
						if (ret <= 0) {
							break;
						}
					}

					if (ret <= 0) {
						map.put("ret", ret + "");
						map.put("ret_desc", "执行失败");
						conn.rollback();
						return map;
					}
					// 查询借款信息得到借款时插入的平台收费标准
					Map<String, String> mapacc = borrowDao.queryBorrowCost(
							conn, id);
					String feelog = Convert.strToStr(mapacc.get("feelog"), "");
					Map<String, Double> feeMap = (Map<String, Double>) JSONObject
							.toBean(JSONObject.fromObject(feelog),
									HashMap.class);
					investFeeRate = Convert.strToDouble(
							feeMap.get(IAmountConstants.INVEST_FEE_RATE) + "",
							0);
					borrowFeeRateOne = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_FEE_RATE_1)
											+ "", 0);
					borrowInmonthFeeRateOne = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_1)
											+ "", 0);
					borrowOutmonthFeeRateOne = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_1)
											+ "", 0);
					borrowDayFeeRateOne = Convert.strToDouble(
							feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_1)
									+ "", 0);
					borrowInmonthFeeRateTwo = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_2)
											+ "", 0);
					borrowOutmonthFeeRateTwo = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_2)
											+ "", 0);
					borrowDayFeeRateTwo = Convert.strToDouble(
							feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_2)
									+ "", 0);
					borrowInmonthFeeRateThree = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_3)
											+ "", 0);
					borrowOutmonthFeeRateThree = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_3)
											+ "", 0);
					borrowDayFeeRateThree = Convert.strToDouble(
							feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_3)
									+ "", 0);
					borrowInmonthFeeRateFour = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_4)
											+ "", 0);
					borrowOutmonthFeeRateFour = Convert
							.strToDouble(
									feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_4)
											+ "", 0);
					borrowDayFeeRateFour = Convert.strToDouble(
							feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_4)
									+ "", 0);
					// 如果是天标，默认成功(殷梓淞添加体验标使用）-->修改点开始
					if (isDayThe == 2) {
						map = this.updateBorrowFullScaleStatusforDateThe(conn,
								id, status, identify, borrowAmount,
								investFeeRate, adminMap, adminId);
					} else {
						log.info("满标审核处理开始（存储）");
						// 满标审核处理
						Procedures.p_borrow_deal_fullscale(conn, ds,
								outParameters, id, adminId, status, new Date(),
								auditOpinion, identify, basePath,
								new BigDecimal(investFeeRate), new BigDecimal(
										borrowFeeRateOne), new BigDecimal(
										borrowInmonthFeeRateOne),
								new BigDecimal(borrowOutmonthFeeRateOne),
								new BigDecimal(borrowDayFeeRateOne),
								new BigDecimal(borrowInmonthFeeRateTwo),
								new BigDecimal(borrowOutmonthFeeRateTwo),
								new BigDecimal(borrowDayFeeRateTwo),
								new BigDecimal(borrowInmonthFeeRateThree),
								new BigDecimal(borrowOutmonthFeeRateThree),
								new BigDecimal(borrowDayFeeRateThree),
								new BigDecimal(borrowInmonthFeeRateFour),
								new BigDecimal(borrowOutmonthFeeRateFour),
								new BigDecimal(borrowDayFeeRateFour),
								fundOrderNo, -1, "");
						ret = Convert.strToLong(outParameters.get(0) + "", -1);
						if (ret > 0 && status == 4) {
							// 添加系统操作日志
							adminMap = adminDao.queryAdminById(conn, adminId);
							operationLogDao.addOperationLog(conn, "t_borrow",
									Convert.strToStr(adminMap.get("userName"),
											""), IConstants.UPDATE, Convert
											.strToStr(adminMap.get("lastIP"),
													""), 0, "满标复审通过", 2);
							// 提成奖励
							DataSet ds1 = MySQL
									.executeQuery(
											conn,
											" select DISTINCT a.id as id,a.investor as userId,a.realAmount as realAmount,c.publisher as publisher from t_invest a left join t_repayment b on a.borrowId = b.borrowId  left join t_borrow c on a.borrowId = c.id where c.id ="
													+ id);
							ds1.tables.get(0).rows.genRowsMap();
							List<Map<String, Object>> list = ds1.tables.get(0).rows.rowsMap;
							for (Map<String, Object> map2 : list) {
								long uId = Convert.strToLong(map2.get("userId")
										+ "", -1);
								long investId = Convert.strToLong(
										map2.get("id") + "", -1);
								Object obj = map2.get("realAmount");
								BigDecimal amounts = BigDecimal.ZERO;
								if (obj != null) {
									amounts = new BigDecimal(obj + "");
								}
								ret = awardService.updateMoneyNew(conn, uId,
										amounts, IConstants.MONEY_TYPE_1,
										investId);
							}

						}
						map.put("ret", ret + "");
						map.put("ret_desc", outParameters.get(1) + "");
						if (ret <= 0) {
							conn.rollback();
						} else {
							conn.commit();
						}
					}
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
		} else {
			return map;
		}

	}

	/**
	 * 更新满标的借款状态
	 * 
	 * @throws Exception
	 * @MethodName: updateBorrowFullScaleStatus
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-13 下午01:21:09
	 * @Return:
	 * @Descb: 更新满标的借款状态
	 * @Throws:
	 */
	public Map<String, String> updateBorrowFullScaleStatus(long id,
			long status, String auditOpinion, long adminId, String basePath,
			String tradeNo) throws Exception {
		IPaymentAction ips = new IPaymentAction();

		Connection conn = MySQL.getConnection();
		int isDayThe = 0;
		double investFeeRate = 0;// 投资管理费
		double borrowFeeRateOne = 0;// 秒还借款管理费
		double borrowInmonthFeeRateOne = 0;// 净值借款2个月内管理费率
		double borrowOutmonthFeeRateOne = 0;// 净值借款2个月外管理费率
		double borrowDayFeeRateOne = 0;// 净值借款天标管理费率
		double borrowInmonthFeeRateTwo = 0;// 信用借款2个月内管理费率
		double borrowOutmonthFeeRateTwo = 0;// 信用借款2个月外管理费率
		double borrowDayFeeRateTwo = 0;// 信用借款天标管理费率
		double borrowInmonthFeeRateThree = 0;// 机构担保借款2个月内管理费率
		double borrowOutmonthFeeRateThree = 0;// 机构担保借款2个月外管理费率
		double borrowDayFeeRateThree = 0;// 机构担保借款天标管理费率
		double borrowInmonthFeeRateFour = 0;// 实地考察借款2个月内管理费率
		double borrowOutmonthFeeRateFour = 0;// 实地考察借款2个月外管理费率
		double borrowDayFeeRateFour = 0;// 实地考察借款天标管理费率
		double borrowAmount = 0;
		String identify = id + "_" + System.currentTimeMillis() + "";
		long ret = -1;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> adminMap = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		List<Object> outParameters = new ArrayList<Object>();

		try {
			// 满标审核前判断处理
			Procedures.p_borrow_auth_fullscale(conn, ds, outParameterValues,
					id, status, -1, "", new BigDecimal(0.00), new BigDecimal(
							0.00), 0, 0, 0);
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", outParameterValues.get(1) + "");
				conn.rollback();
				return map;
			}
			// 审核通过才生成还款记录
			if (ret == 4) {
				borrowAmount = Convert.strToDouble(outParameterValues.get(2)
						+ "", 0);
				double annualRate = Convert.strToDouble(
						outParameterValues.get(3) + "", 0);
				int deadline = Convert.strToInt(outParameterValues.get(4) + "",
						0);
				isDayThe = Convert.strToInt(outParameterValues.get(5) + "", 1);
				int paymentMode = Convert.strToInt(outParameterValues.get(6)
						+ "", 1);

				// 生成还款记录
				List<Map<String, Object>> repayMapList = null;
				// AmountUtil au = new AmountUtil();
				if (paymentMode == 1) {
					// 按月等额还款
					repayMapList = AmountHelper.rateCalculateMonth(
							borrowAmount, annualRate, deadline, isDayThe);
				} else if (paymentMode == 2) {
					// 先息后本还款
					repayMapList = AmountHelper.rateCalculateSum(borrowAmount,
							annualRate, deadline, isDayThe);
				} else if (paymentMode == 3) {
					// 秒还还款
					repayMapList = AmountHelper.rateSecondsSum(borrowAmount,
							annualRate, deadline);
				}// add by c_j 13.07.25增加一次性还款
				else if (paymentMode == 4) {
					repayMapList = AmountHelper.rateCalculateOne(borrowAmount,
							annualRate, deadline, isDayThe);
				}
				String repayPeriod = ""; // 还款期数
				double stillPrincipal = 0; // 应还本金
				double stillInterest = 0; // 应还利息
				double principalBalance = 0; // 剩余本金
				double interestBalance = 0; // 剩余利息
				double totalSum = 0; // 本息余额
				double totalAmount = 0; // 还款总额
				double mRate = 0; // 月利率
				String repayDate = "";
				int count = 1;
				for (Map<String, Object> paymentMap : repayMapList) {
					repayPeriod = paymentMap.get("repayPeriod") + "";
					stillPrincipal = Convert.strToDouble(
							paymentMap.get("stillPrincipal") + "", 0);
					stillInterest = Convert.strToDouble(
							paymentMap.get("stillInterest") + "", 0);
					principalBalance = Convert.strToDouble(
							paymentMap.get("principalBalance") + "", 0);
					interestBalance = Convert.strToDouble(
							paymentMap.get("interestBalance") + "", 0);
					totalSum = Convert.strToDouble(paymentMap.get("totalSum")
							+ "", 0);
					totalAmount = Convert.strToDouble(
							paymentMap.get("totalAmount") + "", 0);
					repayDate = paymentMap.get("repayDate") + "";
					mRate = Convert
							.strToDouble(paymentMap.get("mRate") + "", 0);
					// 添加预还款记录
					ret = repamentDao.addPreRepament(conn, id, identify,
							repayPeriod, stillPrincipal, stillInterest,
							principalBalance, interestBalance, totalSum,
							totalAmount, mRate, repayDate, count);
					count++;
					if (ret <= 0) {
						break;
					}
				}

				if (ret <= 0) {
					map.put("ret", ret + "");
					map.put("ret_desc", "执行失败");
					conn.rollback();
					return map;
				}
				// 查询借款信息得到借款时插入的平台收费标准
				Map<String, String> mapacc = borrowDao
						.queryBorrowCost(conn, id);
				String feelog = Convert.strToStr(mapacc.get("feelog"), "");
				Map<String, Double> feeMap = (Map<String, Double>) JSONObject
						.toBean(JSONObject.fromObject(feelog), HashMap.class);
				investFeeRate = Convert.strToDouble(
						feeMap.get(IAmountConstants.INVEST_FEE_RATE) + "", 0);
				borrowFeeRateOne = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_FEE_RATE_1) + "", 0);
				borrowInmonthFeeRateOne = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_1)
								+ "", 0);
				borrowOutmonthFeeRateOne = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_1)
								+ "", 0);
				borrowDayFeeRateOne = Convert
						.strToDouble(
								feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_1)
										+ "", 0);
				borrowInmonthFeeRateTwo = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_2)
								+ "", 0);
				borrowOutmonthFeeRateTwo = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_2)
								+ "", 0);
				borrowDayFeeRateTwo = Convert
						.strToDouble(
								feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_2)
										+ "", 0);
				borrowInmonthFeeRateThree = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_3)
								+ "", 0);
				borrowOutmonthFeeRateThree = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_3)
								+ "", 0);
				borrowDayFeeRateThree = Convert
						.strToDouble(
								feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_3)
										+ "", 0);
				borrowInmonthFeeRateFour = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_INMONTH_FEE_RATE_4)
								+ "", 0);
				borrowOutmonthFeeRateFour = Convert.strToDouble(
						feeMap.get(IAmountConstants.BORROW_OUTMONTH_FEE_RATE_4)
								+ "", 0);
				borrowDayFeeRateFour = Convert
						.strToDouble(
								feeMap.get(IAmountConstants.BORROW_DAY_FEE_RATE_4)
										+ "", 0);
			}
			// 第三方投标审核
			Map<String, String> data = ips.checkTrade(tradeNo,
					Long.toString(status), tradeNo);
			if (data != null && data.size() > 0) {
				String pErrCode = data.get("pErrCode");
				String pMerCode = data.get("pMerCode");
				String pErrMsg = data.get("pErrMsg");
				// 如果是天标，默认成功(殷梓淞添加体验标使用）-->修改点开始
				if (isDayThe == 2) {
					map = this.updateBorrowFullScaleStatusforDateThe(conn, id,
							status, identify, borrowAmount, investFeeRate,
							adminMap, adminId);
				} else {
					String p3DesXmlPara = data.get("p3DesXmlPara");
					Map<String, String> parseXml = IPaymentUtil
							.parseXmlToJson(p3DesXmlPara);
					String pMerBillNo = parseXml.get("pMerBillNo");

					String pSign = data.get("pSign");// 签名
					boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode,
							pErrMsg, p3DesXmlPara, pSign);
					if (!sign) {
						map.put("ret", -1 + "");
						map.put("ret_desc", pErrMsg);
					} else {
						Map<String, String> fundMap = fundManagementService
								.queryFundRecord(pMerBillNo);
						log.info("fundMap值:" + fundMap);
						if (fundMap == null) {

							if (!pErrCode.equals("0000")) {
								map.put("ret", -1 + "");
								map.put("ret_desc", pErrMsg);
							} else {
								log.info("满标审核处理开始（存储）");
								// 满标审核处理
								Procedures
										.p_borrow_deal_fullscale(
												conn,
												ds,
												outParameters,
												id,
												adminId,
												status,
												new Date(),
												auditOpinion,
												identify,
												basePath,
												new BigDecimal(investFeeRate),
												new BigDecimal(borrowFeeRateOne),
												new BigDecimal(
														borrowInmonthFeeRateOne),
												new BigDecimal(
														borrowOutmonthFeeRateOne),
												new BigDecimal(
														borrowDayFeeRateOne),
												new BigDecimal(
														borrowInmonthFeeRateTwo),
												new BigDecimal(
														borrowOutmonthFeeRateTwo),
												new BigDecimal(
														borrowDayFeeRateTwo),
												new BigDecimal(
														borrowInmonthFeeRateThree),
												new BigDecimal(
														borrowOutmonthFeeRateThree),
												new BigDecimal(
														borrowDayFeeRateThree),
												new BigDecimal(
														borrowInmonthFeeRateFour),
												new BigDecimal(
														borrowOutmonthFeeRateFour),
												new BigDecimal(
														borrowDayFeeRateFour),
												pMerBillNo, -1, "");
								ret = Convert.strToLong(outParameters.get(0)
										+ "", -1);
								if (ret > 0 && status == 4) {
									// 添加系统操作日志
									adminMap = adminDao.queryAdminById(conn,
											adminId);
									operationLogDao
											.addOperationLog(
													conn,
													"t_borrow",
													Convert.strToStr(adminMap
															.get("userName"),
															""),
													IConstants.UPDATE,
													Convert.strToStr(adminMap
															.get("lastIP"), ""),
													0, "满标复审通过", 2);
									// 提成奖励
									DataSet ds1 = MySQL
											.executeQuery(
													conn,
													" select DISTINCT a.id as id,a.investor as userId,a.realAmount as realAmount,c.publisher as publisher from t_invest a left join t_repayment b on a.borrowId = b.borrowId  left join t_borrow c on a.borrowId = c.id where c.id ="
															+ id);
									ds1.tables.get(0).rows.genRowsMap();
									List<Map<String, Object>> list = ds1.tables
											.get(0).rows.rowsMap;
									for (Map<String, Object> map2 : list) {
										long uId = Convert.strToLong(
												map2.get("userId") + "", -1);
										long investId = Convert.strToLong(
												map2.get("id") + "", -1);
										Object obj = map2.get("realAmount");
										BigDecimal amounts = BigDecimal.ZERO;
										if (obj != null) {
											amounts = new BigDecimal(obj + "");
										}
										ret = awardService.updateMoneyNew(conn,
												uId, amounts,
												IConstants.MONEY_TYPE_1,
												investId);
									}

								}
								map.put("ret", ret + "");
								map.put("ret_desc", outParameters.get(1) + "");
								if (ret <= 0) {
									conn.rollback();
								} else {
									conn.commit();
								}
							}
						}
					}
				}
			} else {
				map.put("ret", -1 + "");
				map.put("ret_desc", "操作失败，输出参数为空");
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
	 * 更新满标的借款状态(体验金)
	 * 
	 * @Title: updateBorrowFullScaleStatusforDateThe
	 * @param
	 * @return Map<String,String> 返回类型
	 * @throws
	 */
	private Map<String, String> updateBorrowFullScaleStatusforDateThe(
			Connection conn, long id, long status, String identify,
			double borrowAmount, double investFeeRate,
			Map<String, String> adminMap, long adminId) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String pMerBillNo = QianduoduoPayUtil.getOrderNo("");
		long ret = -1;
		Map<String, String> borrowinfoMap = auditExperienceMoneyDao
				.getBorrowInfo(conn, id);
		// 满标审核处理
		if (status == 4) {
			// 审核通过处理
			Map<String, String> preRepaymentMap = auditExperienceMoneyDao
					.getPreRepaymentInfo(conn, identify);
			long publisher = Convert.strToLong(borrowinfoMap.get("publisher"),
					-1);
			// 更新借款状态
			ret = auditExperienceMoneyDao.updateBorrowStatus(conn, status, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，更新借款状态时出错");
				conn.rollback();
				return map;
			}
			// 生成还款计划
			ret = auditExperienceMoneyDao.createRepaymentRecord(conn, identify);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，生成还款计划时出错");
				conn.rollback();
				return map;
			}
			// 生成投资收款记录
			ret = auditExperienceMoneyDao.createInvestGatheringRecord(conn,
					borrowAmount, investFeeRate, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，生成还款计划时出错");
				conn.rollback();
				return map;
			}
			// 纠偏投资应收款明细资金
			ret = auditExperienceMoneyDao
					.correctInvestScheduleOfAccountsReceivable(conn, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，纠偏投资应收款明细资金时出错");
				conn.rollback();
				return map;
			}
			// 处理投资应收款
			ret = auditExperienceMoneyDao.dealInvestAccountReceivable(conn, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，处理投资应收款时出错");
				conn.rollback();
				return map;
			}
			// 纠偏投资收款
			ret = auditExperienceMoneyDao.correctInvestGathering(conn, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，纠偏投资收款时出错");
				conn.rollback();
				return map;
			}
			// 更新投资表
			ret = auditExperienceMoneyDao.updateTInvest(conn, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，更新投资表时出错");
				conn.rollback();
				return map;
			}
			// 更新投资还款表
			ret = auditExperienceMoneyDao.updateTinvestRepayment(conn, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，更新投资还款表时出错");
				conn.rollback();
				return map;
			}
			// 返还发布借款时的冻结金额
			double frozen_margin = Convert.strToDouble(
					borrowinfoMap.get("frozen_margin"), -1);
			if (frozen_margin > 0) {
				ret = auditExperienceMoneyDao.returnSecondFreezeFund(conn,
						frozen_margin, id);
				if (ret <= 0) {
					map.put("ret", ret + "");
					map.put("ret_desc", "操作失败，返还发布借款时的冻结金额时出错");
					conn.rollback();
					return map;
				}

				String t_content = "解冻保证金["
						+ String.format("%.2f", frozen_margin) + "]元";

				ret = auditExperienceMoneyDao
						.insertReturnSecondFreezeFundRecord(conn,
								frozen_margin, publisher, id, t_content,
								pMerBillNo);
				if (ret <= 0) {
					map.put("ret", ret + "");
					map.put("ret_desc", "操作失败，返还发布借款时的冻结金额时出错");
					conn.rollback();
					return map;
				}
			}
			// 借款成功处理
			if (borrowAmount > 0) {
				// String content = "借款[<a href=\""
				// + basePath
				// + "/financeDetail.do?id="
				// + id + "\"  target=\"_blank\">"
				// + borrowinfoMap.get("borrowTitle") +
				// "</a>],复审通过,借款成功筹到资金["+borrowAmount+"]元";
				// 借款成功处理，将可用金额增加
				// 没有地方增加可用体验金金额，不处理
			} else {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，借款金额低于0时出错");
				conn.rollback();
				return map;
			}

			// 生成通知模版
			int paymentMode = Convert.strToInt(
					borrowinfoMap.get("paymentMode"), -1);
			String t_payment_mode = "";
			String t_other = "";
			String t_date = "天";
			if (paymentMode == 1) {
				t_payment_mode = "按月等额还款";
				double t_total_sum = Convert.strToDouble(
						preRepaymentMap.get("totalSum"), 0);
				t_other = "每个月将偿还金额[" + t_total_sum + "]元";
			} else if (paymentMode == 2) {
				t_payment_mode = "按月先息后本";
				double t_still_interest = Convert.strToDouble(
						preRepaymentMap.get("stillInterest"), 0);
				t_other = "每个月将偿还利息[" + t_still_interest + "]元";
			} else if (paymentMode == 3) {
				t_payment_mode = "秒还";
			} else if (paymentMode == 4) {
				t_payment_mode = "一次性";
			} else {
				t_payment_mode = "";
			}

			Map<String, String> mailTemplateMap = auditExperienceMoneyDao
					.getMailTemplate(conn);
			Map<String, String> eMailTemplateMap = auditExperienceMoneyDao
					.getEmailTemplate(conn);
			Map<String, String> smsTemplateMap = auditExperienceMoneyDao
					.getSmsTemplate(conn);
			Map<String, String> borrowerInfoMap = auditExperienceMoneyDao
					.getBorrowerInfo(conn, publisher);
			// 邮件模板
			String t_mail_template = mailTemplateMap.get("template");
			t_mail_template = t_mail_template.replace("title",
					borrowinfoMap.get("borrowTitle"));
			t_mail_template = t_mail_template.replace("borrowAmount",
					Double.toString(borrowAmount));
			t_mail_template = t_mail_template.replace("excitationSum",
					borrowinfoMap.get("excitationSum"));
			t_mail_template = t_mail_template.replace("realAmount",
					Double.toString(borrowAmount));
			t_mail_template = t_mail_template.replace("totalAmount",
					preRepaymentMap.get("totalAmount"));
			t_mail_template = t_mail_template.replace("mRate",
					preRepaymentMap.get("mRate"));
			t_mail_template = t_mail_template.replace("[deadline]",
					borrowinfoMap.get("deadline"));
			t_mail_template = t_mail_template.replace("[yue]", t_date);
			t_mail_template = t_mail_template.replace("[month]", t_other);
			t_mail_template = t_mail_template.replace("moshi", t_payment_mode);
			t_mail_template = t_mail_template.replace("bManageFee", "0");
			t_mail_template = t_mail_template.replace("[<br/>]", "<br />");

			// 邮件模板
			String t_email_template = eMailTemplateMap.get("template");
			t_email_template = t_email_template.replace("title",
					borrowinfoMap.get("borrowTitle"));
			t_email_template = t_email_template.replace("borrowAmount",
					Double.toString(borrowAmount));
			t_email_template = t_email_template.replace("excitationSum",
					borrowinfoMap.get("excitationSum"));
			t_email_template = t_email_template.replace("realAmount",
					Double.toString(borrowAmount));
			t_email_template = t_email_template.replace("totalAmount",
					preRepaymentMap.get("totalAmount"));
			t_email_template = t_email_template.replace("mRate",
					preRepaymentMap.get("mRate"));
			t_email_template = t_email_template.replace("[deadline]",
					borrowinfoMap.get("deadline"));
			t_email_template = t_email_template.replace("[yue]", t_date);
			t_email_template = t_email_template.replace("[month]", t_other);
			t_email_template = t_email_template
					.replace("moshi", t_payment_mode);
			t_email_template = t_email_template.replace("bManageFee", "0");
			t_email_template = t_email_template.replace("[<br/>]", "<br />");

			// 短信模板
			String t_sms_template = smsTemplateMap.get("template");
			t_sms_template = t_sms_template.replace("title",
					borrowinfoMap.get("borrowTitle"));
			t_sms_template = t_sms_template.replace("borrowAmount",
					Double.toString(borrowAmount));
			t_sms_template = t_sms_template.replace("excitationSum",
					borrowinfoMap.get("excitationSum"));
			t_sms_template = t_sms_template.replace("realAmount",
					Double.toString(borrowAmount));
			t_sms_template = t_sms_template.replace("totalAmount",
					preRepaymentMap.get("totalAmount"));
			t_sms_template = t_sms_template.replace("mRate",
					preRepaymentMap.get("mRate"));
			t_sms_template = t_sms_template.replace("[deadline]",
					borrowinfoMap.get("deadline"));
			t_sms_template = t_sms_template.replace("[yue]", t_date);
			t_sms_template = t_sms_template.replace("[month]", t_other);
			t_sms_template = t_sms_template.replace("moshi", t_payment_mode);
			t_sms_template = t_sms_template.replace("bManageFee", "0");
			t_sms_template = t_sms_template.replace("date",
					DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			t_sms_template = t_sms_template.replace("username",
					borrowerInfoMap.get("username"));

			ret = auditExperienceMoneyDao.insertNoticeTask(conn,
					Convert.strToLong(borrowerInfoMap.get("userId"), -1),
					borrowerInfoMap.get("username"),
					borrowerInfoMap.get("email"),
					borrowerInfoMap.get("cellphone"), t_mail_template,
					t_email_template, t_sms_template, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，发送通知时出错");
				conn.rollback();
				return map;
			}

			// 获取所有投资人
			List<Map<String, String>> investorsList = auditExperienceMoneyDao
					.getAllInvestors(conn, id);
			if (investorsList == null || investorsList.size() <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，获取所有投资人时出错");
				conn.rollback();
				return map;
			} else {
				for (Map<String, String> investor : investorsList) {
					// 投资表id
					long investId = Convert.strToLong(
							String.valueOf(investor.get("id")), 0);
					// 投资金额
					double t_invest_amount = Convert.strToDouble(
							String.valueOf(investor.get("investAmount")), -1);
					// 投资人id
					long investorId = Convert.strToLong(
							String.valueOf(investor.get("investor")), -1);
					// 投资期数
					int investDeadline = Convert.strToInt(
							String.valueOf(investor.get("deadline")), -1);
					// 月利率
					double monthRate = Convert.strToDouble(
							String.valueOf(investor.get("monthRate")), -1);
					ret = auditExperienceMoneyDao
							.updateInvestorExpreienceMoney(conn, investId);
					if (ret <= 0) {
						map.put("ret", ret + "");
						map.put("ret_desc", "操作失败，修改投资者应收本金和应收利息出错");
						conn.rollback();
						return map;
					}
					// 更新用户冻结金额
					ret = auditExperienceMoneyDao
							.updateInvestorExpreienceMoney(conn,
									t_invest_amount, investorId);
					if (ret <= 0) {
						map.put("ret", ret + "");
						map.put("ret_desc", "操作失败，更新用户冻结金额出错");
						conn.rollback();
						return map;
					}
					// 添加资金记录

					// String updateContent = "借款[<a href=\""
					// + basePath
					// + "/financeDetail.do?id="
					// + id
					// + "\"  target=\"_blank\">"
					// + borrowinfoMap.get("borrowTitle")
					// + "</a>],复审通过,扣除体验标投标冻结金额["
					// + Convert
					// .strToDouble(
					// String.valueOf(investor
					// .get("investAmount")),
					// -1) + "]元";
					// ret = auditExperienceMoneyDao
					// .insertUpdateInvestorExpreienceMoneyRecord(
					// conn, t_invest_amount,
					// investorId, investId, id,
					// updateContent, pMerBillNo);
					// if (ret <= 0) {
					// map.put("ret", ret + "");
					// map.put("ret_desc", "操作失败，添加资金记录出错");
					// conn.rollback();
					// return map;
					// }

					// 发送通知
					Map<String, String> mailTemplateForInvestorMap = auditExperienceMoneyDao
							.getNoticeTemplate(conn, "tender_success");
					Map<String, String> eMailTemplateForInvestorMap = auditExperienceMoneyDao
							.getNoticeTemplate(conn, "e_tender_success");
					Map<String, String> smsTemplateForInvestorMap = auditExperienceMoneyDao
							.getNoticeTemplate(conn, "s_tender_success");
					// 投资人的联系信息
					Map<String, String> investorInfoMap = auditExperienceMoneyDao
							.getBorrowerInfo(conn, investorId);

					// 校验利息
					double check_interest = Convert.strToDouble(
							String.valueOf(investor.get("check_interest")), -1);
					// 校验本金
					double check_principal = Convert
							.strToDouble(String.valueOf(investor
									.get("check_principal")), -1);
					// username
					String payment_mode = "投资期数"
							+ investDeadline
							+ "天,月利率："
							+ monthRate
							+ "%<br/>投资金额["
							+ t_invest_amount
							+ "]元<br/>到期收益利息["
							+ String.format("%.2f", check_interest)
							+ "]元<br/>到期扣除投资管理费[0]元<br/>到期收益总额["
							+ String.format("%.2f", check_principal
									+ check_interest) + "]元";
					String strmailTemplateForInvestor = mailTemplateForInvestorMap
							.get("template");
					String streMailTemplateForInvestor = eMailTemplateForInvestorMap
							.get("template");
					String strsmsTemplateForInvestor = smsTemplateForInvestorMap
							.get("template");
					strmailTemplateForInvestor = strmailTemplateForInvestor
							.replace("title", borrowinfoMap.get("borrowTitle"))
							.replace("[msg]", payment_mode);
					streMailTemplateForInvestor = streMailTemplateForInvestor
							.replace("title", borrowinfoMap.get("borrowTitle"))
							.replace("[msg]", payment_mode);

					strsmsTemplateForInvestor = strsmsTemplateForInvestor
							.replace("title", borrowinfoMap.get("borrowTitle"))
							.replace("[msg]", payment_mode)
							.replace(
									"date",
									DateFormatUtils.format(new Date(),
											"yyyy-MM-dd HH:mm:ss"))
							.replace("username",
									investorInfoMap.get("username"));
					// 插入通知
					ret = auditExperienceMoneyDao.insertNoticeTask(conn,
							investorId, investorInfoMap.get("username"),
							investorInfoMap.get("email"),
							investorInfoMap.get("cellphone"), t_mail_template,
							t_email_template, t_sms_template, id);
					if (ret <= 0) {
						map.put("ret", ret + "");
						map.put("ret_desc", "操作失败，插入通知时出错");
						conn.rollback();
						return map;
					}

				}
			}

			if (ret > 0 && status == 4) {
				// 添加系统操作日志
				adminMap = adminDao.queryAdminById(conn, adminId);
				operationLogDao.addOperationLog(conn, "t_borrow",
						Convert.strToStr(adminMap.get("userName"), ""),
						IConstants.UPDATE,
						Convert.strToStr(adminMap.get("lastIP"), ""), 0,
						"满标复审通过", 2);
			}
			map.put("ret", ret + "");
			map.put("ret_desc", "审核通过");
			if (ret <= 0) {
				conn.rollback();
			} else {
				conn.commit();
			}
		} else {
			// 审核不通过处理
			ret = auditExperienceMoneyDao.updateBorrowStatus(conn, status, id);
			if (ret <= 0) {
				map.put("ret", ret + "");
				map.put("ret_desc", "操作失败，审核不通过时出错");
				conn.rollback();
				return map;
			}
			// 通知

			if (ret <= 0) {
				conn.rollback();
			} else {
				conn.commit();
			}
		}
		return map;
	}

	public long reBackBorrowFistAudit(long idLong, Long id, String basePath,
			String msg, String auditOpinion, String pMerBillNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long returnId = -1;
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		Map<String, String> map_ret = new HashMap<String, String>();
		Map<String, String> adminMap = new HashMap<String, String>();
		try {
			Procedures.p_borrow_cancel(conn, ds, outParameterValues, idLong,
					id, 6, auditOpinion, basePath, pMerBillNo, -1, "");
			map_ret.put("out_ret", outParameterValues.get(0) + "");
			map_ret.put("out_desc", outParameterValues.get(1) + "");
			returnId = Convert.strToLong(outParameterValues.get(0) + "", -1);
			if (returnId <= 0) {
				conn.rollback();
				return Convert.strToLong(map_ret.get("out_ret"), -1);
			}
			// 添加操作日志
			adminMap = adminDao.queryAdminById(conn, id);
			operationLogDao.addOperationLog(conn, "t_borrow",
					Convert.strToStr(adminMap.get("userName"), ""),
					IConstants.UPDATE,
					Convert.strToStr(adminMap.get("lastIP"), ""), 0, "管理员撤销借款",
					2);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return Convert.strToLong(map_ret.get("out_ret"), 1);
	}

	/**
	 * @MethodName: addCirculationRepay
	 * @Param: BorrowManageService
	 * @Author: gang.lv
	 * @Date: 2013-7-23 下午04:32:03
	 * @Return:
	 * @Descb: 添加流转标还款记录
	 * @Throws:
	 */
	public long addCirculationRepay(long repayId, double amountDouble, Long id,
			String remark) throws Exception {
		Connection conn = MySQL.getConnection();
		long returnId = -1;
		try {
			returnId = repaymentRecordDao.addRepayMentRecord(conn, repayId,
					amountDouble, id, remark);
			if (returnId <= 0) {
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
		}
		return returnId;
	}

	public Map<String, String> queryBorrowInfo(long idLong) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowInfo(conn, idLong);
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
	 * 查看借款协议中的内容
	 * 
	 * @param borrowId
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Map<String, String> queryBorrowMany(long borrowId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = investDao.queryBorrowMany(conn, borrowId);
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
	 * 根据借款id 和投资id 查询
	 * 
	 * @param borrowId
	 * @param invest_id
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryInvestMomey(long borrowId,
			long invest_id) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = investDao.queryInvestMomey(conn, invest_id, borrowId);
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
	 * 查询所有投资人信息
	 * 
	 * @param borrowId
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryUsername(long borrowId, long invest_id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = investDao.queryUsername(conn, borrowId, invest_id);
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
	 * 根据借款查询借款应还的金额
	 * 
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Map<String, String> queryBorrowSumMomeny(long borrowId,
			long invest_id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = investDao.queryBorrowSumMomeny(conn, borrowId, invest_id);
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

	// 借款管理模块中，查询各个报表的总额
	public Map<String, String> queryBorrowTotalFistAuditDetail()
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowTotalFistAuditDetail(conn);
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

	public Map<String, String> queryBorrowTotalWait() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowTotalWait(conn);
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

	public Map<String, String> queryBorrowTotalTenderDetail() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowTotalTenderDetail(conn);
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

	public Map<String, String> queryBorrowFlowMarkDetail() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowFlowMarkDetail(conn);
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

	public Map<String, String> queryBorrowTotalFullScaleDetail()
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowTotalFullScaleDetail(conn);
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

	public Map<String, String> queryAuthProtocol(long borrowId, long investId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryAuthProtocol(conn, borrowId, investId);
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
	 * 根据借款id查询借款id,version,deadline,annualRate,isDayThe信息.
	 * 
	 * @param borrowid
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryBorrowByid(long borrowid) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowManageDao.queryBorrowInfoById(conn, borrowid);
			conn.commit();
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
	 * 根据borrow查询乾多多流水号列表
	 * 
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryInvestLoanNoByBorrowId
	 * @Param: InvestDao
	 * @Author: gang.lv
	 * @Date: 2013-6-2 下午07:26:48
	 * @Return:
	 * @Descb: 根据借款id查询投资信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryInvestLoanNoByBorrowId(long borrowId) {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = investDao.queryInvestLoanNoByBorrowId(conn, borrowId);
		} catch (Exception e) {
		}
		return list;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public AwardService getAwardService() {
		return awardService;
	}

	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
	}

	public InvestDao getInvestDao() {
		return investDao;
	}

	public void setInvestDao(InvestDao investDao) {
		this.investDao = investDao;
	}

	public BorrowManageDao getBorrowManageDao() {
		return borrowManageDao;
	}

	public void setBorrowManageDao(BorrowManageDao borrowManageDao) {
		this.borrowManageDao = borrowManageDao;
	}

	public void setAccountUsersDao(AccountUsersDao accountUsersDao) {
		this.accountUsersDao = accountUsersDao;
	}

	public RepamentDao getRepamentDao() {
		return repamentDao;
	}

	public void setRepamentDao(RepamentDao repamentDao) {
		this.repamentDao = repamentDao;
	}

	public BorrowDao getBorrowDao() {
		return borrowDao;
	}

	public void setBorrowDao(BorrowDao borrowDao) {
		this.borrowDao = borrowDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setFundRecordDao(FundRecordDao fundRecordDao) {
		this.fundRecordDao = fundRecordDao;
	}

	public void setRepaymentRecordDao(RepaymentRecordDao repaymentRecordDao) {
		this.repaymentRecordDao = repaymentRecordDao;
	}

	public AdminDao getAdminDao() {
		return adminDao;
	}

	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public PlatformCostService getPlatformCostService() {
		return platformCostService;
	}

	public void setPlatformCostService(PlatformCostService platformCostService) {
		this.platformCostService = platformCostService;
	}

	public AccountUsersDao getAccountUsersDao() {
		return accountUsersDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public FundRecordDao getFundRecordDao() {
		return fundRecordDao;
	}

	public RepaymentRecordDao getRepaymentRecordDao() {
		return repaymentRecordDao;
	}

	public NoticeTaskDao getNoticeTaskDao() {
		return noticeTaskDao;
	}

	public void setNoticeTaskDao(NoticeTaskDao noticeTaskDao) {
		this.noticeTaskDao = noticeTaskDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}
}
