package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
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

import com.fp2p.helper.AmountHelper;
import com.fp2p.helper.DateHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.MyHomeDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.UserDao;
import com.sp2p.database.Dao.Procedures;

/**
 * @ClassName: MyHomeService.java
 * @Author: gang.lv
 * @Date: 2013-3-18 下午10:25:00
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 我的个人主页服务层
 */
public class MyHomeService extends BaseService {

	public static Log log = LogFactory.getLog(MyHomeService.class);

	private MyHomeDao myHomeDao;
	private UserDao userDao;
	private OperationLogDao operationLogDao;

	public MyHomeDao getMyHomeDao() {
		return myHomeDao;
	}

	public void setMyHomeDao(MyHomeDao myHomeDao) {
		this.myHomeDao = myHomeDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	/**
	 * @MethodName: queryBorrowFinishByCondition
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午10:26:03
	 * @Return:
	 * @Descb: 查询已发布的借款列表
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public void queryBorrowFinishByCondition(String title,
			String publishTimeStart, String publishTimeEnd,
			String borrowStatus, long userId, PageBean pageBean)
			throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(publishTimeStart)) {
			condition.append(" and publishTime >'"
					+ StringEscapeUtils.escapeSql(publishTimeStart.trim())
					+ "'");
		}
		if (StringUtils.isNotBlank(publishTimeEnd)) {
			condition.append(" and publishTime <'"
					+ StringEscapeUtils.escapeSql(publishTimeEnd.trim()) + "'");
		}
		if (StringUtils.isNotBlank(borrowStatus)) {
			String idStr = StringEscapeUtils
					.escapeSql("'" + borrowStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" and borrowStatus in(" + idSQL + ")");
		}
		condition.append(" and userId =" + userId);
		log.info(condition.toString());
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_borrow_publish", resultFeilds,
					" order by id desc", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: queryHomeInfo
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-22 下午07:09:41
	 * @Return:
	 * @Descb: 查询统计后的个人信息
	 * @Throws:
	 */
	public Map<String, String> queryHomeInfo(long userId) throws SQLException,
			DataException {
		Connection conn = MySQL.getConnection();
		String startTime = DateFormatUtils.format(DateHelper.getMonthFirstDay(), "yyyy-MM-dd HH:mm:ss");
		String endTime = DateHelper.getMonthLastDay().toString();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.pr_getUserInfo(conn, ds, outParameterValues, userId,
					startTime, endTime);

			conn.commit();

			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
			// log.info(map);
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * @MethodName: queryAccountStatisInfo
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-26 上午09:27:44
	 * @Return:
	 * @Descb: 查询账户统计
	 * @Throws:
	 */
	public Map<String, String> queryAccountStatisInfo(long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.pr_getUserAmountSumary(conn, ds, outParameterValues,
					userId);
			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryLoanStatis
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午02:58:07
	 * @Return:
	 * @Descb: 查询借款统计
	 * @Throws:
	 */
	public Map<String, String> queryLoanStatis(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.pr_getBorrowStatis(conn, ds, outParameterValues, id,
					new Date());
			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryRepaymentByOwner
	 * @Param: MyHomeService
	 * @Author:
	 * @Date:
	 * @Return:
	 * @Descb: 查询最近还款日及还款金额
	 * @Throws:
	 */

	public Map<String, String> queryRepaymentByOwner(long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = myHomeDao.queryRepaymentByOwner(conn, userId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryFinanceStatis
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午02:58:20
	 * @Return:
	 * @Descb: 查询理财统计
	 * @Throws:
	 */
	public Map<String, String> queryFinanceStatis(Long id) throws SQLException,
			DataException {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.pr_getFinanceStatis(conn, ds, outParameterValues, id);
			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * @MethodName: queryBorrowInvestByCondition
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午11:13:56
	 * @Return:
	 * @Descb: 成功投资借款记录
	 * @Throws:
	 */
	public void queryBorrowInvestByCondition(String title,
			String publishTimeStart, String publishTimeEnd,
			String borrowStatus, Long id, PageBean pageBean) throws Exception {
		// modify by houli 2013-04-25 添加了b.isDayThe字段
		String resultFeilds = "a.id,a.borrowId,b.borrower,b.borrowTitle,b.borrowWay,b.paymentMode,"
				+ "b.annualRate,b.deadline,b.publishTime,b.credit,b.creditrating, round(a.investAmount,2) as investAmount,"
				+ "b.schedules,b.times,b.isDayThe ,b.borrowShow ,DATE_FORMAT(a.investTime,'%Y-%m-%d') investTime,b.borrowAmount";
		StringBuffer condition = new StringBuffer();
		if (!"2".equals(borrowStatus)) {
			condition.append(" and ( ( 1=1   ");
		}

		if (StringUtils.isNotBlank(title)) {
			condition.append(" and b.borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(publishTimeStart)) {
			condition.append(" and b.publishTime >'"
					+ StringEscapeUtils.escapeSql(publishTimeStart.trim())
					+ "'");
		}
		if (StringUtils.isNotBlank(publishTimeEnd)) {
			condition.append(" and b.publishTime <'"
					+ StringEscapeUtils.escapeSql(publishTimeEnd.trim()) + "'");
		}
		if (StringUtils.isNotBlank(borrowStatus)) {
			String idStr = StringEscapeUtils
					.escapeSql("'" + borrowStatus + "'");
			String idSQL = "-2";
			idStr = idStr.replaceAll("'", "");
			String[] array = idStr.split(",");
			for (int n = 0; n <= array.length - 1; n++) {
				idSQL += "," + array[n];
			}
			condition.append(" and b.borrowStatus in(" + idSQL + ")");
		}
		if (!"2".equals(borrowStatus)) {
			condition.append(" ) or (b.borrowShow=2) )");
		}
		condition.append("   and a.investor =" + id
				+ " order by b.publishTime desc");
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" v_t_invest_borrow a  LEFT JOIN   v_t_invest_borrow_list b  ON a.borrowId=b.id ",
					resultFeilds, "", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: queryBorrowRecycleByCondition
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-27 上午11:57:07
	 * @Return:
	 * @Descb: 查询回收中的借款
	 * @Throws:
	 */
	public void queryBorrowRecycleByCondition(String title, Long id,
			PageBean pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and borrowTitle  LIKE '%"
					+ StringEscapeUtils.escapeSql(title.trim()) + "%'");
		}
		condition.append(" and investor =" + id);
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_invest_recycling_my ", resultFeilds,
					"", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public void queryBorrowRecycleByConditionApp(String title, long userId,
			PageBean pageBean) throws SQLException, DataException {
		String resultFeilds = "a.REPAYPERIOD as \"repayPeriod\",date_format(a.REPAYDATE,'%Y-%m-%d') as \"repayDate\",round(a.RECIVEDPRINCIPAL,2) AS \"forpayPrincipal\",round(a.RECIVEDINTEREST,2) AS \"forpayInterest\", round(a.PRINCIPALBALANCE,2) AS \"principalBalance\", "
				+ "round(("
				+ IConstants.I_MANAGE
				+ "*a.RECIVEDINTEREST),2) AS \"manage\",a.ISLATE as \"isLate\",a.LATEDAY as \"lateDay\",round(a.RECIVEDFI,2) AS \"forFI\", (a.RECIVEDINTEREST -round(a.RECIVEDINTEREST*"
				+ IConstants.I_MANAGE
				+ ",2)+a.RECIVEDFI ) AS \"earn\","
				+ "d.USERNAME as \"username\",a.ISWEBREPAY as \"isWebRepay\",a.REPAYSTATUS as \"repayStatus\",(round(a.RECIVEDINTEREST,2)+round(a.RECIVEDPRINCIPAL,2)) as \"reciveSum\" ,a.INVEST_ID AS \"id\" ";

		StringBuffer condition = new StringBuffer();
		// if (StringUtils.isNotBlank(title)) {
		// condition.append(" and b.\"borrowTitle\"  LIKE '%"+StringEscapeUtils.escapeSql(title.trim())+"%'");
		// }
		condition.append(" and a.REPAYSTATUS=1 and a.OWNER =" + userId);
		Connection conn = connectionManager.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					"T_INVEST_REPAYMENT a LEFT JOIN T_REPAYMENT b on a.REPAYID=b.id LEFT JOIN T_BORROW c on b.borrowId=c.id LEFT JOIN T_USER d on c.PUBLISHER=d.id",
					resultFeilds, " order by a.REPAYDATE ", condition
							.toString());

		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}

	}

	/**
	 * @MethodName: queryBorrowRecycledByCondition
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午02:09:03
	 * @Return:
	 * @Descb: 查询已回收的借款
	 * @Throws:
	 */
	public void queryBorrowRecycledByCondition(String title, Long id,
			PageBean pageBean) throws Exception {
		String resultFeilds = "t.bid,t.investor,t.borrowId,t.borrower,t.borrowTitle,t.borrowWay,t.credit,t.creditrating,t.annualRate,t.deadline"
				+ " ,t.isDayThe " + // add by houli 区别天标还是月标
				",round(t.realAmount,2) as realAmount,round(t.forTotalSum,2) forTotalSum,t.isDebt ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and t.borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		condition.append(" and t.investor =" + id
				+ " and IFNULL(t.forTotalSum,0)>0 ");
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					"  (select *  from v_t_invest_recycled_sum_ a LEFT JOIN v_t_invest_borrow_list b ON a.borrowId = b.id where  b.borrowShow = 1 "
							+ " union all"
							+ " select * from  v_t_invest_flow a left join v_t_invest_borrow_list c on  a.borrowId = c.id where  c.borrowShow = 2 "
							+ " ) t", resultFeilds, "", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: queryBorrowForpayById
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午06:55:11
	 * @Return:
	 * @Descb: 查询投资人回收中的还款详情
	 * @Throws:
	 */
	public List<Map<String, Object>> queryBorrowForpayById(long id,
			long userId, long iid) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = myHomeDao.queryBorrowForpayById(conn, id, userId, iid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryBorrowHaspayById
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午06:55:36
	 * @Return:
	 * @Descb: 查询投资人已回收的还款详情
	 * @Throws:
	 */
	public List<Map<String, Object>> queryBorrowHaspayById(long idLong,
			long userId, long iid) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = myHomeDao.queryBorrowHaspayById(conn, idLong, userId, iid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	/**
	 * @MethodName: queryBorrowBackAcountByCondition
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-28 上午12:02:11
	 * @Return:
	 * @Descb: 借款回账查询
	 * @Throws:
	 */
	public void queryBorrowBackAcountByCondition(String title,
			String publishTimeStart, String publishTimeEnd, Long id,
			PageBean pageBean) throws Exception {
		String resultFeilds = " a.id as investId,c.username as borrower,b.borrowTitle,b.id as borrowId,b.annualRate,b.deadline,"
				+ " round(a.realAmount,2) as realAmount,round((a.hasPrincipal+a.hasInterest),2) forHasSum,round((a.recivedPrincipal+a.recievedInterest-a.hasPrincipal-a.hasInterest),2) forTotalSum"
				+ ",b.isDayThe,b.borrowWay  ";// add by houli 添加是否为天标标志
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and b.borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(publishTimeStart)) {
			condition.append(" and b.publishTime >'"
					+ StringEscapeUtils.escapeSql(publishTimeStart.trim())
					+ " 00:00:00'");
		}
		if (StringUtils.isNotBlank(publishTimeEnd)) {
			condition.append(" and b.publishTime <'"
					+ StringEscapeUtils.escapeSql(publishTimeEnd.trim())
					+ " 23:59:59'");
		}
		condition.append(" and a.investor =" + id);
		condition
				.append(" and (a.recivedPrincipal+a.recievedInterest-a.hasPrincipal-a.hasInterest) > 0");
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" t_invest a left join t_borrow b on a.borrowId = b.id left join t_user c on b.publisher= c.id  ",
					resultFeilds, "", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: queryAutomaticBid
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-28 下午03:09:33
	 * @Return:
	 * @Descb: 查询用户自动投标设置
	 * @Throws:
	 */
	public Map<String, String> queryAutomaticBid(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = myHomeDao.queryAutomaticBid(conn, id);
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
	 * @throws DataException
	 * @MethodName: automaticBidSet
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-28 下午04:33:29
	 * @Return:
	 * @Descb: 自动投标状态设置
	 * @Throws:
	 */
	public long automaticBidSet(long status, long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			result = myHomeDao.automaticBidSet(conn, status, userId);
			if (result <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				if (status == 1) {
					operationLogDao.addOperationLog(conn, "t_automaticbid",
							Convert.strToStr(userMap.get("username"), ""),
							IConstants.UPDATE, Convert.strToStr(userMap
									.get("lastIP"), ""), 0, "开启自动投标", 1);
				} else {
					operationLogDao.addOperationLog(conn, "t_automaticbid",
							Convert.strToStr(userMap.get("username"), ""),
							IConstants.UPDATE, Convert.strToStr(userMap
									.get("lastIP"), ""), 0, "关闭自动投标", 1);
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

		return result;
	}

	/**
	 * @throws DataException
	 * @MethodName: automaticBidModify
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-28 下午05:03:37
	 * @Return:
	 * @Descb: 修改自动投标内容
	 * @Throws:
	 */
	public long automaticBidModify(Double bidAmount, Double rateStart,
			Double rateEnd, Double deadlineStart, Double deadlineEnd,
			Double creditStart, Double creditEnd, Double remandAmount, Long id,
			String borrowWay) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			Map<String, String> map = myHomeDao.hasAutomaticBid(conn, id);
			if (map != null && map.size() > 0) {
				// 更新内容
				result = myHomeDao.automaticBidUpdate(conn, bidAmount,
						rateStart, rateEnd, deadlineStart, deadlineEnd,
						creditStart, creditEnd, remandAmount, id, borrowWay);
			} else {
				// 修改内容
				result = myHomeDao.automaticBidAdd(conn, bidAmount, rateStart,
						rateEnd, deadlineStart, deadlineEnd, creditStart,
						creditEnd, remandAmount, id, borrowWay);
			}
			if (result <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, id);
				operationLogDao.addOperationLog(conn, "t_automaticbid", Convert
						.strToStr(userMap.get("username"), ""),
						IConstants.UPDATE, Convert.strToStr(userMap
								.get("lastIP"), ""), 0, "设置自动投标", 1);
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
	 * @MethodName: queryBackAcountStatis
	 * @Param: MyHomeService
	 * @Author: gang.lv
	 * @Date: 2013-3-29 下午11:47:49
	 * @Return:
	 * @Descb: 回账统计
	 * @Throws:
	 */
	public Map<String, String> queryBackAcountStatis(long userId,
			String startTime, String endTime, String title) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		title = StringEscapeUtils.escapeSql(title.trim());
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.pr_getBackAcountStatis(conn, ds, outParameterValues,
					startTime, endTime, title, userId);
			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public void queryBorrowRecycleByConditionApp2(String title, Long id,
			PageBean pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and borrowTitle LIKE '%"
					+ StringEscapeUtils.escapeSql(title.trim()) + "%'");
		}
		condition.append(" and investor =" + id);
		Connection conn = MySQL.getConnection();
		StringBuffer comm = new StringBuffer();
		try {
			dataPage(conn, pageBean, " v_t_invest_recycling_my ", resultFeilds,
					"", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

}