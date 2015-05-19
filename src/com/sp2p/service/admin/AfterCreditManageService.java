package com.sp2p.service.admin;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.BorrowDao;
import com.sp2p.dao.FrontMyPaymentDao;
import com.sp2p.dao.InvestDao;
import com.sp2p.dao.UserDao;
import com.sp2p.dao.admin.AfterCreditManageDao;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.service.AssignmentDebtService;
import com.sp2p.service.AwardService;
import com.sp2p.service.SelectedService;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.shove.web.action.IPaymentAction;
import com.shove.web.action.IPaymentUtil;

/**
 * @ClassName: AfterCreditManageService.java
 * @Author: gang.lv
 * @Date: 2013-3-19 上午10:18:35
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 贷后管理业务处理层
 */
public class AfterCreditManageService extends BaseService {

	public static Log log = LogFactory.getLog(AfterCreditManageService.class);

	private AfterCreditManageDao afterCreditManageDao;
	private SelectedService selectedService;
	private AwardService awardService;
	private AssignmentDebtService assignmentDebtService;
	private FrontMyPaymentDao frontpayDao;
	private InvestDao investDao;
	private BorrowDao borrowDao;
	private UserDao userDao;

	/**
	 * @MethodName: queryAfterCreditAuditByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-19 上午10:20:22
	 * @Return:
	 * @Descb: 贷后管理最近3天还款记录
	 * @Throws:
	 */
	@SuppressWarnings( { "static-access", "unchecked" })
	public void queryLastRepayMentByCondition(String userName, int borrowWay,
			String realName, String title, int status, String type,
			PageBean pageBean) throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.UNDERLINE_DATE_SHORT);
		Calendar calendar = Calendar.getInstance();
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(realName)) {
			condition.append(" and realName  like '%"
					+ StringEscapeUtils.escapeSql(realName.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and  borrowTitle   LIKE '%"
					+ StringEscapeUtils.escapeSql(title.trim()) + "%'");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWay) {
			condition.append(" and  borrowWay  =" + borrowWay);
		}
		if (IConstants.DEFAULT_NUMERIC != status) {
			condition.append(" and  repayStatus  =" + status);
		}
		if ("".equals(type)) {
			Date date = calendar.getTime();
			condition.append(" and repayDate ='" + sf.format(date) + "'");
		} else if ("1".equals(type)) {
			calendar.add(calendar.DAY_OF_YEAR, 1);
			Date date = calendar.getTime();
			condition.append(" and repayDate ='" + sf.format(date) + "'");
		} else if ("2".equals(type)) {
			calendar.add(calendar.DAY_OF_YEAR, 2);
			Date date = calendar.getTime();
			condition.append(" and repayDate ='" + sf.format(date) + "'");
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_repayment_h", resultFeilds,
					" order by  id  desc", condition + "");
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
	 * @MethodName: queryRepaymentAmount
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-19 上午11:32:00
	 * @Return:
	 * @Descb: 根据条件统计最近还款总额
	 * @Throws:
	 */
	public Map<String, String> queryRepaymentAmount(String userName,
			int borrowWay, String realName, String title, int status,
			String type) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryRepaymentAmount(conn, userName,
					borrowWay, realName, title, status, type);
			conn.commit();
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public AfterCreditManageDao getAfterCreditManageDao() {
		return afterCreditManageDao;
	}

	public void setAfterCreditManageDao(
			AfterCreditManageDao afterCreditManageDao) {
		this.afterCreditManageDao = afterCreditManageDao;
	}

	/**
	 * @throws DataException
	 * @MethodName: queryRepayMentNoticeByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-19 下午02:53:11
	 * @Return:
	 * @Descb: 查询还款的沟通记录
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public void queryRepayMentNoticeByCondition(long idLong, PageBean pageBean)
			throws Exception {
		String resultFeilds = " id,serviceContent,date_format(serviceTime,'%Y-%c-%d %T') as serviceTime ";
		StringBuffer condition = new StringBuffer();
		condition.append(" and  repayId  = " + idLong);
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " t_repayment_service", resultFeilds,
					" order by  id desc", condition.toString());
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
	 * @MethodName: addRepayMentNotice
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-19 下午07:02:10
	 * @Return:
	 * @Descb: 添加还款沟通记录
	 * @Throws:
	 */
	public long addRepayMentNotice(long idLong, String content)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = afterCreditManageDao.addRepayMentNotice(conn, idLong,
					content);
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
	 * @MethodName: queryForPaymentByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-20 下午06:32:51
	 * @Return:
	 * @Descb: 根据条件查询待收款记录
	 * @Throws: modify by houli 添加反选标志 inverse
	 */
	@SuppressWarnings("unchecked")
	public void queryForPaymentByCondition(String investor, String timeStart,
			String timeEnd, String title, int borrowWayInt, int groupInt,
			PageBean pageBean, boolean inverse) throws Exception {

		String resultFeilds = " investor,realName ,groupName,  investTime  ,repayPeriod,id,borrowTitle,borrowWay,repayDate,isDayThe,round(forTotalSum,2) forTotalSum,username ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(investor)) {
			condition.append(" and  investor  like '%"
					+ StringEscapeUtils.escapeSql(investor.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(timeStart)) {
			condition.append(" AND  repayDate  >= '"
					+ StringEscapeUtils.escapeSql(timeStart) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			condition.append(" AND  repayDate  <= '"
					+ StringEscapeUtils.escapeSql(timeEnd) + "'");
		}
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and  borrowTitle  like '%"
					+ StringEscapeUtils.escapeSql(title.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWayInt) {
			condition.append(" AND  borrowWay  =" + borrowWayInt);
		}
		if (IConstants.DEFAULT_NUMERIC != groupInt) {
			if (inverse) {// 如果是反选
				condition.append(" AND (  groupId  !=" + groupInt
						+ "  or  groupId  is null ) ");
			} else {
				condition.append(" AND  groupId  =" + groupInt);
			}
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_forpayment_h ", resultFeilds, "",
					condition.toString());
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		}finally {
			conn.close();
		}

	}

	/**
	 * @MethodName: queryForPaymentByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-20 下午06:32:51
	 * @Return:
	 * @Descb: 根据条件查询待还款记录
	 * @Throws: modify by houli 添加反选标志 inverse
	 */
	@SuppressWarnings("unchecked")
	public void queryForPaymentByDueIn(String investor, String timeStart,
			String timeEnd, String title, int borrowWayInt, int groupInt,
			PageBean pageBean, boolean inverse) throws Exception{

		String resultFeilds = "   *  ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(investor)) {
			condition.append(" and  investor  like '%"
					+ StringEscapeUtils.escapeSql(investor.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(timeStart)) {
			condition.append(" AND  repayDate  >= '"
					+ StringEscapeUtils.escapeSql(timeStart) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			condition.append(" AND  repayDate  <= '"
					+ StringEscapeUtils.escapeSql(timeEnd) + "'");
		}
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and  borrowTitle  like '%"
					+ StringEscapeUtils.escapeSql(title.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWayInt) {
			condition.append(" AND  borrowWay  =" + borrowWayInt);
		}
		if (IConstants.DEFAULT_NUMERIC != groupInt) {
			if (inverse) {// 如果是反选
				condition.append(" AND (  groupId  !=" + groupInt
						+ "  or  groupId  is null ) ");
			} else {
				condition.append(" AND  groupId  =" + groupInt);
			}
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_deuin_list ", resultFeilds,
					" order by id desc", condition.toString());
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
	 * @MethodName: queryForPaymentAmount
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-20 下午06:33:16
	 * @Return:
	 * @Descb: 根据条件查询待收款统计
	 * @Throws:
	 */
	public Map<String, String> queryForPaymentAmount(String investor,
			String timeStart, String timeEnd, String title, int borrowWayInt,
			int groupInt, boolean inverse) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryForPaymentAmount(conn, investor,
					timeStart, timeEnd, title, borrowWayInt, groupInt, inverse);
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
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: queryForPaymentTotalByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-20 下午10:09:16
	 * @Return:
	 * @Descb: 代收款总计记录
	 * @Throws: add by houli 2014-04-26 添加反选条件 inverse
	 */
	@SuppressWarnings("unchecked")
	public void queryForPaymentTotalByCondition(String investor,
			String timeStart, String timeEnd, int deadlineInt, int groupInt,
			PageBean pageBean, boolean inverse) throws Exception,
			DataException {
		String resultFeilds = " investor , realName,groupName,round(investAmount,2) as investAmount ,scale , publishTime , round(borrowAmount,2) as borrowAmount ,borrowWay,isDayThe,repayPeriod,repayDate,round(forTotalSum,2) as forTotalSum";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(investor)) {
			condition.append(" and  investor  like '%"
					+ StringEscapeUtils.escapeSql(investor.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(timeStart)) {
			condition.append(" and  repayDate  >= '"
					+ StringEscapeUtils.escapeSql(timeStart) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			condition.append(" and  repayDate  <= '"
					+ StringEscapeUtils.escapeSql(timeEnd) + "'");
		}
		if (IConstants.DEFAULT_NUMERIC != deadlineInt) {
			condition.append(" and  deadline  =" + deadlineInt);
		}
		if (IConstants.DEFAULT_NUMERIC != groupInt) {
			if (inverse) {
				condition.append(" and (  groupId  !=" + groupInt
						+ "  or  groupId  is null )");
			} else {
				condition.append(" and  groupId  =" + groupInt);
			}
		}

		StringBuffer sql = new StringBuffer();
		sql.append(" v_t_forpayment_h ");
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, sql.toString(), resultFeilds, "",
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
	 * @throws DataException
	 * @MethodName: queryForPaymentTotalAmount
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-20 下午10:08:58
	 * @Return:
	 * @Descb: 代收款总计统计
	 * @Throws:
	 */
	public Map<String, String> queryForPaymentTotalAmount(String investor,
			String timeStart, String timeEnd, int deadlineInt, int groupInt,
			boolean inverse) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryForPaymentTotalAmount(conn,
					investor, timeStart, timeEnd, deadlineInt, groupInt,
					inverse);
			conn.commit();
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * @MethodName: queryHasRepayByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-21 下午02:37:15
	 * @Return:
	 * @Descb: 查询已收款列表
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public void queryHasRepayByCondition(String userName, String realName,
			String timeStart, String timeEnd, int borrowWayInt,
			int deadlineInt, int repayStatusInt, PageBean pageBean,
			String timeStart1, String timeEnd1)// add by houli 添加两个时间变量
			throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username   like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ");
		}
		if (StringUtils.isNotBlank(realName)) {
			condition.append(" and realName   like '%"
					+ StringEscapeUtils.escapeSql(realName) + "%' ");
		}
		if (StringUtils.isNotBlank(timeStart)) {
			condition.append(" and realRepayDate >= '"
					+ StringEscapeUtils.escapeSql(timeStart) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			condition.append(" and realRepayDate <= '"
					+ StringEscapeUtils.escapeSql(timeEnd) + "'");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWayInt) {
			condition.append(" and borrowWay =" + borrowWayInt);
		}
		if (IConstants.DEFAULT_NUMERIC != deadlineInt) {
			condition.append(" and deadline =" + deadlineInt);
		}
		if (IConstants.DEFAULT_NUMERIC != repayStatusInt) {
			condition.append(" and repayStatus =" + repayStatusInt);
		}
		// -------add by houli
		if (StringUtils.isNotBlank(timeStart1)) {
			condition.append(" and repayDate >= '"
					+ StringEscapeUtils.escapeSql(timeStart1) + "'");
		}
		if (StringUtils.isNotBlank(timeEnd1)) {
			condition.append(" and repayDate <= '"
					+ StringEscapeUtils.escapeSql(timeEnd1) + "'");
		}
		// ---------------
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_hasrepay_h ", resultFeilds,
					" order by  id desc", condition.toString());
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
	 * @MethodName: queryHasRePayAmount
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-21 下午02:37:02
	 * @Return:
	 * @Descb: 已收款统计
	 * @Throws:
	 */
	public Map<String, String> queryHasRePayAmount(String userName,
			String realName, String timeStart, String timeEnd,
			int borrowWayInt, int deadlineInt, int repayStatusInt,
			String timeStart1, String timeEnd1) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryHasRePayAmount(conn, userName,
					realName, timeStart, timeEnd, borrowWayInt, deadlineInt,
					repayStatusInt,
					// add by houli
					timeStart1, timeEnd1);
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
	 * @MethodName: queryLateRepayByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-21 下午05:16:54
	 * @Return:
	 * @Descb: 逾期的借款记录
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public void queryLateRepayByCondition(String userName, int borrowWayInt,
			int statusInt, PageBean pageBean) throws Exception{
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and userName  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWayInt) {
			condition.append(" and  borrowWay  =" + borrowWayInt);
		}
		if (IConstants.DEFAULT_NUMERIC != statusInt) {
			condition.append(" and  repayStatus  =" + statusInt);
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_laterepay_h", resultFeilds, "",
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
	 * @throws DataException
	 * @MethodName: queryLateRepayAmount
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-21 下午05:17:01
	 * @Return:
	 * @Descb: 逾期的借款统计
	 * @Throws:
	 */
	public Map<String, String> queryLateRepayAmount(String userName,
			int borrowWayInt, int statusInt) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryLateRepayAmount(conn, userName,
					borrowWayInt, statusInt);
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
	 * 根据还款ID 查询还款详情
	 * 
	 * @throws DataException
	 * @throws Exception
	 */
	public void queryByrepayId(int id, PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			StringBuffer table = new StringBuffer();
			table
					.append(" t_invest_repayment a	LEFT JOIN t_invest b ON a.invest_id = b.id	LEFT JOIN t_user c ON b.investor = c.id	left join (select username as borrowName ,t.id from t_borrow t LEFT JOIN t_user u  on  t.publisher = u.id) e	on e.id = b. borrowId ");
			StringBuffer filed = new StringBuffer();
			filed
					.append(" c.username , a.repayPeriod,a.lateDay, a.repayId ,a.realRepayDate ,e.borrowName ,FORMAT(a.recivedInterest,2) as recivedInterest , FORMAT(a.hasPrincipal,2) as hasPrincipal,FORMAT(a.hasInterest,2) as hasInterest,a.isWebRepay,a.isLate,FORMAT( a.recivedFI ,2) as recivedFI  ");
			dataPage(conn, pageBean, table.toString(), filed.toString(), "",
					" and  a.repayStatus = 2 and a.repayId = " + id);
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
	 * 根据还款ID 查询还款详情
	 * 
	 * @throws DataException
	 * @throws Exception
	 */
	public void queryByrepayIdDueId(int id,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			StringBuffer table = new StringBuffer();
			table.append("  t_invest_repayment a LEFT JOIN  t_repayment s  on a.repayId = s.id	LEFT JOIN t_invest b ON a.invest_id = b.id	LEFT JOIN t_user c ON b.investor = c.id	left join (select username as borrowName ,t.id from t_borrow t LEFT JOIN t_user u  on  t.publisher = u.id) e	on e.id = b. borrowId ");
			StringBuffer filed = new StringBuffer();
			filed.append(" c.username , a.repayPeriod,s.lateDay,FORMAT(a.recivedInterest,2) as recivedInterest , FORMAT(a.recivedPrincipal,2) as recivedPrincipal,FORMAT(a.hasInterest,2) as hasInterest,a.isLate,FORMAT( a.recivedFI ,2) as recivedFI ,s.id ,date_format(s.repayDate, '%Y-%m-%d') as repayDate,e.borrowName ,a.isWebRepay ");
			dataPage(conn, pageBean, table.toString(), filed.toString(), "",
					" and  a.repayStatus = 1 and a.repayId = " + id);
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
	 * @throws DataException
	 * @throws Exception
	 * @MethodName: queryOverduePaymentByCondition
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-21 下午05:42:31
	 * @Return:
	 * @Descb: 逾期垫付的借款记录
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public void queryOverduePaymentByCondition(String userName,
			int borrowWayInt, int statusInt, PageBean pageBean)
			throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and userName  like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
		}
		if (IConstants.DEFAULT_NUMERIC != borrowWayInt) {
			condition.append(" and  borrowWay  =" + borrowWayInt);
		}
		if (IConstants.DEFAULT_NUMERIC != statusInt) {
			condition.append(" and  repayStatus  =" + statusInt);
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_overduepayment_h", resultFeilds, "",
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
	 * @MethodName: queryOverduePaymentAmount
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-3-21 下午05:43:05
	 * @Return:
	 * @Descb: 逾期垫付的借款统计
	 * @Throws:
	 */
	public Map<String, String> queryOverduePaymentAmount(String userName,
			int borrowWayInt, int statusInt) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryOverduePaymentAmount(conn,
					userName, borrowWayInt, statusInt);
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
	 * @MethodName: queryRepaymentDetail
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-2 下午02:12:38
	 * @Return:
	 * @Descb: 还款记录详情
	 * @Throws:
	 */
	public Map<String, String> queryRepaymentDetail(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = afterCreditManageDao.queryRepaymentDetail(conn, id);
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
	 * @MethodName: queryRepaymentService
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-2 下午02:12:08
	 * @Return:
	 * @Descb: 借款客服沟通记录
	 * @Throws:
	 */
	public List<Map<String, Object>> queryRepaymentService(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = afterCreditManageDao.queryRepaymentService(conn, id);
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
	 * @MethodName: queryRepaymentColectoin
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-2 下午02:12:27
	 * @Return:
	 * @Descb: 借款催收记录
	 * @Throws:
	 */
	public List<Map<String, Object>> queryRepaymentCollection(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = afterCreditManageDao.queryRepaymentCollection(conn, id);
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
	 * @throws Exception
	 * @MethodName: addCollection
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-2 下午04:12:57
	 * @Return:
	 * @Descb: 添加催款记录
	 * @Throws:
	 */
	public long addCollection(long idLong, String colResult, String remark)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		try {
			result = afterCreditManageDao.addCollection(conn, idLong,
					colResult, remark);
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
	 * @MethodName: delCollection
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-2 下午04:35:41
	 * @Return:
	 * @Descb: 删除催收记录
	 * @Throws:
	 */
	public long delCollection(long idLong) throws Exception {
		Connection conn = MySQL.getConnection();

		Long result = -1L;
		try {
			result = afterCreditManageDao.delCollection(conn, idLong);
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
	 * @throws Exception
	 * @MethodName: overduePaymentRepaySubmit
	 * @Param: AfterCreditManageService
	 * @Author: gang.lv
	 * @Date: 2013-4-8 下午10:02:42
	 * @Return:
	 * @Descb: 逾期垫付还款
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> overduePaymentRepaySubmit(long idLong,String repaySum,
			Long adminId, String basePath) throws Exception {

		Connection conn = MySQL.getConnection();
		Map<String, String> borrowMap = borrowDao.queryBorroeById(conn, idLong);
		//收款人
		Map<String,String> pTperson = userDao.queryPersonById(conn, Convert.strToLong(borrowMap.get("userId"),-1L));
		Map<String,String> pTuser = userDao.queryUserById(conn, Convert.strToLong(borrowMap.get("userId"),-1L));
		String pBidNo = borrowMap.get("tradeNo");
		String pContractNo = borrowMap.get("tradeNo");
		String pFreezeMerBillNo = borrowMap.get("guaranteeNo");
		String pTIdentNo = pTperson.get("idNo");
		String pTRealName = pTperson.get("realName");
		String pTIpsAcctNo = pTuser.get("portMerBillNo");
		String pTransferAmt = repaySum;
		IPaymentAction ipa = new IPaymentAction();
		Map<String,String> data = ipa.guaranteeTransfer(pBidNo, pContractNo, pFreezeMerBillNo, "", "", "", pTIdentNo, pTRealName, pTIpsAcctNo, pTransferAmt);
		
		Map<String, String> map = new HashMap<String, String>();
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		long ret = -1;
		try {

			// 查询借款信息得到借款时插入的平台收费标准
			Map<String, String> mapacc = borrowDao.queryBorrowCostByPayId(conn,
					idLong);
			String feelog = Convert.strToStr(mapacc.get("feelog"), "");
			Map<String, Double> feeMap = (Map<String, Double>) JSONObject
					.toBean(JSONObject.fromObject(feelog), HashMap.class);
			double investFeeRate = Convert.strToDouble(feeMap
					.get(IAmountConstants.INVEST_FEE_RATE)
					+ "", 0);

			if (data != null && data.size() > 0) {
				String pErrCode = data.get("pErrCode");
				String pMerCode = data.get("pMerCode");
				String pErrMsg = data.get("pErrMsg");

				String p3DesXmlPara = data.get("p3DesXmlPara");
				Map<String, String> parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
				String pMerBillNo = parseXml.get("pMerBillNo");
				
				String pSign = data.get("pSign");// 签名
				boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
			
				if(!sign){
					map.put("ret", -1 + "");
					map.put("ret_desc", pErrMsg);
				}else{
					
					if (!pErrCode.equals("0000")) {
						map.put("ret", -1 + "");
						map.put("ret_desc", pErrMsg);
					} else {
					
						Procedures.p_borrow_repayment_overdue(conn, ds, outParameterValues,
								idLong, adminId, basePath, new Date(), new BigDecimal(
										investFeeRate), 0, "");
						ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
						map.put("ret", ret + "");
						map.put("ret_desc", outParameterValues.get(1) + "");
						if (ret <= 0) {
							conn.rollback();
						} else {
							// // 还款成功修改奖励机制
							// if ("1".equals(ret)) {
							// DataSet ds1 = MySQL.executeQuery(
							// conn,
							// " select a.id,a.investor userId,c.publisher from t_invest a left join t_repayment b on a.borrowId = b.borrowId  left join t_borrow c on a.borrowId = c.id where b.id ="
							// + idLong);
							// ds1.tables.get(0).rows.genRowsMap();
							// List<Map<String, Object>> list =
							// ds1.tables.get(0).rows.rowsMap;
							// for (Map<String, Object> map2 : list) {
							// long uId = Convert.strToLong(map2.get("userId") + "",
							// -1);
							// Object obj = map2.get("principal");
							// BigDecimal amounts = BigDecimal.ZERO;
							// if (obj != null) {
							// amounts = new BigDecimal(obj + "");
							// }
							// awardService.updateMoney(conn, uId, amounts,
							// IConstants.MONEY_TYPE_2, idLong);
							// assignmentDebtService.preRepayment(conn, idLong);
							// }
							// }
							conn.commit();
							map.put("msg", outParameterValues.get(1) + "");
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

	public void changeNumToStr(PageBean<Map<String, Object>> pageBean){
		List<Map<String, Object>> list = pageBean.getPage();
		if (list != null) {

			for (Map<String, Object> map : list) {
				if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_NET_VALUE)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_NET_VALUE_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_SECONDS)) {
					map
							.put("borrowWay",
									IConstants.BORROW_TYPE_SECONDS_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_GENERAL)) {
					map
							.put("borrowWay",
									IConstants.BORROW_TYPE_GENERAL_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_FIELD_VISIT)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_FIELD_VISIT_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
					map
							.put(
									"borrowWay",
									IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
				}
				if (map.get("repayStatus").toString().equals("1")) {
					map.put("repayStatus", "未偿还");
				} else if (map.get("repayStatus").toString().equals("2")) {
					map.put("repayStatus", "已偿还");
				} else if (map.get("repayStatus").toString().equals("3")) {
					map.put("repayStatus", "偿还中");
				}
				if (map.get("servier") == null || map.get("servier") == "") {
					map.put("servier", "未分配");
				}
				log.info(map);
				
			}
		}
	}
	public void changeNumToStr2(PageBean<Map<String, Object>> pageBean){
		List<Map<String, Object>> list = pageBean.getPage();
		if (list != null) {

			for (Map<String, Object> map : list) {
				if (Convert.strToStr(map.get("borrowWay")+"", "").equals(
						IConstants.BORROW_TYPE_NET_VALUE)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_NET_VALUE_STR);
				} else if (Convert.strToStr(map.get("borrowWay")+"", "").equals(
						IConstants.BORROW_TYPE_SECONDS)) {
					map
							.put("borrowWay",
									IConstants.BORROW_TYPE_SECONDS_STR);
				} else if (Convert.strToStr(map.get("borrowWay")+"", "").equals(
						IConstants.BORROW_TYPE_GENERAL)) {
					map
							.put("borrowWay",
									IConstants.BORROW_TYPE_GENERAL_STR);
				} else if (Convert.strToStr(map.get("borrowWay")+"", "").equals(
						IConstants.BORROW_TYPE_FIELD_VISIT)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_FIELD_VISIT_STR);
				} else if (Convert.strToStr(map.get("borrowWay")+"", "").equals(
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
					map
							.put(
									"borrowWay",
									IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
				} else if (Convert.strToStr(map.get("borrowWay")+"", "").equals(
						IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
				}
				if (map.get("groupName") == null || map.get("groupName") == "") {
					map.put("groupName", "未分配");
				}
				if (map.get("realName") == null || map.get("realName") == "") {
					map.put("realName", "未填写");
				}
				if (Convert.strToLong(map.get("isDayThe") + "", -1L) == 1) {
					map.put("isDayThe", "否");
				} else {
					map.put("isDayThe", "是");
				}
			}
		}
	}
	public void changeNumToStr3(PageBean<Map<String, Object>> pageBean){
		List<Map<String, Object>> list = pageBean.getPage();
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		for (Map<String, Object> map : list) {
			if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_NET_VALUE)) {
				map.put("borrowWay", IConstants.BORROW_TYPE_NET_VALUE_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_SECONDS)) {
				map.put("borrowWay", IConstants.BORROW_TYPE_SECONDS_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_GENERAL)) {
				map.put("borrowWay", IConstants.BORROW_TYPE_GENERAL_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_FIELD_VISIT)) {
				map
						.put("borrowWay",
								IConstants.BORROW_TYPE_FIELD_VISIT_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
				map.put("borrowWay",
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
				map.put("borrowWay",
						IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
			}

			if (map.get("repayStatus").toString().equals("1")) {
				map.put("repayStatus", "未偿还");
			} else if (map.get("repayStatus").toString().equals("2")) {
				map.put("repayStatus", "已偿还");
			} else if (map.get("repayStatus").toString().equals("3")) {
				map.put("repayStatus", "偿还中");
			}
			if (map.get("groupName") == null || map.get("groupName") == "") {
				map.put("groupName", "未分配");
			}
			if (Convert.strToLong(map.get("isDayThe") + "", -1L) == 1) {
				map.put("isDayThe", "否");
			} else {
				map.put("isDayThe", "是");
			}
		}
	}
  
	public void changeNumToStr4(PageBean<Map<String, Object>> pageBean){
		List<Map<String, Object>> list = pageBean.getPage();
		if (list == null) {
			list = new ArrayList<Map<String, Object>>();
		}
		for (Map<String, Object> map : list) {
			if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_NET_VALUE)) {
				map.put("borrowWay", IConstants.BORROW_TYPE_NET_VALUE_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_SECONDS)) {
				map.put("borrowWay", IConstants.BORROW_TYPE_SECONDS_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_GENERAL)) {
				map.put("borrowWay", IConstants.BORROW_TYPE_GENERAL_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_FIELD_VISIT)) {
				map
						.put("borrowWay",
								IConstants.BORROW_TYPE_FIELD_VISIT_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
				map.put("borrowWay",
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
			} else if (map.get("borrowWay").toString().equals(
					IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
				map.put("borrowWay",
						IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
			}

			if (map.get("isWebRepay").toString().equals("1")) {
				map.put("isWebRepay", "否");
			} else if (map.get("isWebRepay").toString().equals("2")) {
				map.put("isWebRepay", "是");
			}

			if (map.get("repayStatus").toString().equals("1")) {
				map.put("repayStatus", "未偿还");
			} else if (map.get("repayStatus").toString().equals("2")) {
				map.put("repayStatus", "已偿还");
			} else if (map.get("repayStatus").toString().equals("3")) {
				map.put("repayStatus", "偿还中");
			}

		}

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

	public InvestDao getInvestDao() {
		return investDao;
	}

	public void setInvestDao(InvestDao investDao) {
		this.investDao = investDao;
	}

	public void setBorrowDao(BorrowDao borrowDao) {
		this.borrowDao = borrowDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
