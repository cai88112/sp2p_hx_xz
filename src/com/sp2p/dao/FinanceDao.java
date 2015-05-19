package com.sp2p.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataRow;
import com.shove.data.DataSet;
import com.shove.data.dao.Database;
import com.shove.data.dao.MySQL;
import com.shove.security.Encrypt;
import com.sp2p.constants.IConstants;
import com.sp2p.database.Dao;

/**
 * @ClassName: FinanceDao.java
 * @Author: gang.lv
 * @Date: 2013-3-4 上午11:15:29
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 理财数据处理层
 */
public class FinanceDao {

	/**
	 * @MethodName: queryBorrowList
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-4 上午11:13:28
	 * @Return: Map<String, String>
	 * @Descb: 查询借款列表
	 * @Throws: SQLException, DataException
	 */
	public List<Map<String, Object>> queryBorrowList(Connection conn,
			int borrowMode, int limitStart, int limitCount)
			throws SQLException, DataException {
		Dao.Views.v_t_borrow_list borrowList = new Dao().new Views().new v_t_borrow_list();
		DataSet dataSet = borrowList.open(conn, "*", "", "", limitStart,
				limitCount);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: queryBorrowDetailById
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午06:04:15
	 * @Return:
	 * @Descb: 根据ID查询借款的详情
	 * @Throws:
	 */
	public Map<String, String> queryBorrowDetailById(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_borrow_detail borrowDetail = new Dao().new Views().new v_t_borrow_detail();
		DataSet dataSet = borrowDetail.open(conn, " * ", " id=" + id, "", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: queryUserInfoById
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午06:04:54
	 * @Return:
	 * @Descb: 根据ID查询借款信息发布者个人信息
	 * @Throws:
	 */
	public Map<String, String> queryUserInfoById(Connection conn, long id)
			throws SQLException, DataException {

		Dao.Views.v_t_borrow_user_info v_t_borrow_user_info = new Dao().new Views().new v_t_borrow_user_info();
		DataSet dataSet = v_t_borrow_user_info
				.open(conn,
						"id , userId ,f_formatting_username( username) as username ,vipStatus ,rating , personalHead ,address , credit , creditrating , createTime ,DATE_FORMAT(lastDate,'%Y-%m-%d') as lastDate  , creditLimit ,  vip , age ,maritalStatus ,  workPro , workCity , companyLine , companyScale , job ,school ,highestEdu ,eduStartDay ,  hasHourse , hasCar ,  hasHousrseLoan , hasCarLoan , auditperson ,  auditwork ,nativePlaceCity,nativePlacePro,nativePlace,sex",
						" id=" + id, "", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: queryUserIdentifiedByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:00:04
	 * @Return:
	 * @Descb: 根据ID查询用户认证信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryUserIdentifiedByid(Connection conn,
			long id) throws SQLException, DataException {
		Dao.Views.v_t_borrow_user_materialsauth user_materialsauth = new Dao().new Views().new v_t_borrow_user_materialsauth();
		DataSet dataSet = user_materialsauth.open(conn,
				" * ,count(DISTINCT materAuthTypeId)  as testnum ", " id=" + id
						+ " group by materAuthTypeId ", " ", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: queryPaymentByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:03:01
	 * @Return:
	 * @Descb: 根据ID查询本期还款信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryRePayByid(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_borrow_repayment v_t_borrow_repayment = new Dao().new Views().new v_t_borrow_repayment();
		DataSet dataSet = v_t_borrow_repayment.open(conn, " * ", " borrowId="
				+ id, "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: queryCollectionByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:04:28
	 * @Return:
	 * @Descb: 根据ID查询本期催收信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryCollectionByid(Connection conn,
			long id) throws SQLException, DataException {
		Dao.Views.v_t_borrow_collection collection = new Dao().new Views().new v_t_borrow_collection();
		DataSet dataSet = collection.open(conn, " * ", " borrowId=" + id, "",
				-1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: queryInvestByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:06:00
	 * @Return:
	 * @Descb: 根据ID查询投资记录
	 * @Throws:
	 */
	public List<Map<String, Object>> queryInvestByid(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_borrow_investrecord investrecord = new Dao().new Views().new v_t_borrow_investrecord();
		DataSet dataSet = investrecord
				.open(conn,
						"  id ,borrowId , f_formatting_username(username) as username , investAmount , investTime , investor , creditedStatus  ",
						" borrowId=" + id,
						// " id desc", -1, -1);//modify by houli 按照时间的正序排
						" investTime asc", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: getInvestStatus
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午06:47:23
	 * @Return:
	 * @Descb: 获取借款投标的状态,条件是正在招标中
	 * @Throws:
	 */
	public Map<String, String> getInvestStatus(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Tables.t_borrow t_borrow_invest = new Dao().new Tables().new t_borrow();
		DataSet dataSet = t_borrow_invest
				.open(conn,
						" id,hasPWD ,nid_log,hasCirculationNumber,circulationNumber,smallestFlowUnit ",
						" borrowStatus =2 and id=" + id, " id desc", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: queryBorrowInvest
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午06:52:43
	 * @Return:
	 * @Descb: 根据ID获取借款投标中的借款内容
	 * @Throws:
	 */
	public Map<String, String> queryBorrowInvest(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_borrow_invest v_t_borrow_invest = new Dao().new Views().new v_t_borrow_invest();
		DataSet dataSet = v_t_borrow_invest.open(conn, " * ", " id=" + id, "",
				0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: queryUserMonney
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午08:47:41
	 * @Return:
	 * @Descb: 查询用户的金额
	 * @Throws:
	 */
	public Map<String, String> queryUserMonney(Connection conn, long userId)
			throws SQLException, DataException {
		Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();
		DataSet dataSet = t_user.open(conn,
				" (usableSum+freezeSum) AS totalSum,usableSum ", " id="
						+ userId, "", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: addBrowseCount
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-5 下午03:51:36
	 * @Return:
	 * @Descb: 更新浏览量
	 * @Throws:
	 */
	public long addBrowseCount(Connection conn, Long id) throws SQLException {
		long returnId = -1L;
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.DATE_SIMPLE);
		StringBuffer condition = new StringBuffer();
		condition.append("UPDATE t_borrow  SET remainTimeStart = '"
				+ sf.format(new Date()));
		condition.append("' WHERE remainTimeEnd IS NOT NULL AND ");
		condition.append(" remainTimeStart <remainTimeEnd AND id =" + id);
		returnId = MySQL.executeNonQuery(conn,
				" update t_borrow set visitors = visitors+1 where id = " + id);
		MySQL.executeNonQuery(conn, condition.toString());
		condition = null;
		return returnId;
	}

	/**
	 * @MethodName: valideInvest
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午04:06:58
	 * @Return:
	 * @Descb: 验证投资人是否符合本次投标
	 * @Throws:
	 */
	public String valideInvest(Connection conn, long id, long userId,
			double amount) throws SQLException, DataException {
		int returnId = 0;
		Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();
		Dao.Tables.t_borrow t_borrow = new Dao().new Tables().new t_borrow();

		DataSet investAmountDataSet = t_borrow.open(conn, " id ", " id=" + id
				+ " and (borrowAmount-hasInvestAmount) >=" + amount, "", 0, 1);
		returnId = investAmountDataSet.tables.get(0).rows.getCount();
		if (returnId == 0) {
			return "您的投标金额超过本轮剩余投标金额";
		} else {
			DataSet maxTenderedSumDataSet = t_borrow.open(conn,
					" maxTenderedSum ", " id=" + id, "", 0, 1);
			returnId = maxTenderedSumDataSet.tables.get(0).rows.getCount();
			DataRow dr = maxTenderedSumDataSet.tables.get(0).rows.get(0);
			BigDecimal maxTenderedSum = (BigDecimal) dr.get("maxTenderedSum");
			if (maxTenderedSum != null) {
				if (returnId == 0) {
					return "您的投标金额超过本轮最多投标金额";
				} else {
					DataSet usableSumDataSet = t_user.open(conn, " id ", " id="
							+ userId + " and usableSum >" + amount, "", 0, 1);
					returnId = usableSumDataSet.tables.get(0).rows.getCount();
					if (returnId == 0) {
						return "您的可用余额不够进行本轮投标";
					}
				}
			} else {
				DataSet usableSumDataSet = t_user.open(conn, " id ", " id="
						+ userId + " and usableSum >" + amount, "", 0, 1);
				returnId = usableSumDataSet.tables.get(0).rows.getCount();
				if (returnId == 0) {
					return "您的可用余额不够进行本轮投标";
				}
			}
		}
		return "";
	}

	/**
	 * @MethodName: valideTradePassWord
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午04:07:23
	 * @Return:
	 * @Descb: 验证交易密码
	 * @Throws:
	 */
	public String valideTradePassWord(Connection conn, long userId, String pwd)
			throws SQLException, DataException {
		String passWord = pwd.trim();
		if ("1".equals(IConstants.ENABLED_PASS)) {
			passWord = com.shove.security.Encrypt.MD5(passWord.trim());// 对密码进行转码
		} else {
			passWord = com.shove.security.Encrypt.MD5(passWord.trim()
					+ IConstants.PASS_KEY);// 对密码进行转码
		}
		Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();

		DataSet dataSet = t_user.open(conn, " id ", " id=" + userId
				+ " and dealpwd ='" + StringEscapeUtils.escapeSql(passWord)
				+ "'", "", 0, 1);
		Map<String, String> map = DataSetHelper.dataSetToMap(dataSet);
		if (map == null || map.size() == 0) {
			return "交易密码错误";
		}
		return "";
	}

	/**
	 * @MethodName: addBorrowInvest
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午05:46:28
	 * @Return:
	 * @Descb: 添加借款投资
	 * @Throws:
	 */
	public Long addBorrowInvest(Connection conn, long id, long userId,
			double borrowSum, double annualRate, double deadlineDouble)
			throws SQLException {
		long returnId = -1;
		// 添加投资记录
		Dao.Tables.t_invest t_invest = new Dao().new Tables().new t_invest();
		t_invest.investAmount.setValue(borrowSum);
		t_invest.realAmount.setValue(borrowSum);
		t_invest.monthRate.setValue(annualRate / 12);
		t_invest.investor.setValue(userId);
		t_invest.oriInvestor.setValue(userId);
		t_invest.investTime.setValue(new Date());
		t_invest.borrowId.setValue(id);
		t_invest.deadline.setValue(deadlineDouble);
		returnId = t_invest.insert(conn);
		// 更新借款信息中的已投资总额和数量
		returnId = MySQL.executeNonQuery(conn,
				" update t_borrow set hasInvestAmount = hasInvestAmount+"
						+ borrowSum + ",investNum=investNum+1" + " where id = "
						+ id);
		// 更新投资人的资金信息
		returnId = MySQL.executeNonQuery(conn,
				" update t_user set usableSum = usableSum-" + borrowSum
						+ ",freezeSum=freezeSum+" + borrowSum + " where id = "
						+ userId);
		return returnId;
	}

	/**
	 * @MethodName: isFullSale
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-17 上午12:44:07
	 * @Return:
	 * @Descb: 判断是否符合满标的条件投资金额已经达到借款金额
	 * @Throws:
	 */
	public Map<String, String> isFullSale(Connection conn, long id)
			throws SQLException, DataException {
		StringBuffer condition = new StringBuffer();
		condition.append(" borrowStatus =" + IConstants.BORROW_STATUS_2);
		condition.append(" and borrowAmount = hasInvestAmount");
		condition.append(" and remainTimeStart < remainTimeEnd");
		condition.append(" and id=" + id);
		Dao.Tables.t_borrow t_borrow_invest = new Dao().new Tables().new t_borrow();
		DataSet dataSet = t_borrow_invest.open(conn, " id ",
				condition.toString(), " id desc", -1, -1);
		condition = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: updateBorrowFullSale
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-17 上午12:57:53
	 * @Return:
	 * @Descb: 更新借款的状态为满标
	 * @Throws:
	 */
	public long updateBorrowFullSale(Connection conn, long id, int sorts)
			throws SQLException, DataException {
		long returnId = -1L;
		StringBuffer condition = new StringBuffer();
		condition.append("update t_borrow set ");
		condition.append(" sort = " + sorts);
		condition.append(", borrowStatus =" + IConstants.BORROW_STATUS_3);
		condition.append(",remainTimeStart= remainTimeEnd");
		condition.append(" where id =" + id);
		returnId = MySQL.executeNonQuery(conn, condition.toString());
		condition = null;
		return returnId;
	}

	/**
	 * @MethodName: addBorrowMSG
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午08:15:42
	 * @Return:
	 * @Descb: 添加借款留言
	 * @Throws:
	 */
	public Long addBorrowMSG(Connection conn, long id, long userId,
			String msgContent) throws SQLException {
		Dao.Tables.t_msgboard t_msgboard = new Dao().new Tables().new t_msgboard();
		t_msgboard.msgContent.setValue(msgContent);
		t_msgboard.modeId.setValue(id);
		// 借款留言类型
		t_msgboard.msgboardType.setValue(1);
		t_msgboard.msger.setValue(userId);
		t_msgboard.msgTime.setValue(new Date());
		return t_msgboard.insert(conn);
	}

	/**
	 * @MethodName: addFocusOn
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-16 上午08:58:05
	 * @Return:
	 * @Descb: 我的关注
	 * @Throws:
	 */
	public Long addFocusOn(Connection conn, long id, long userId, int moduleType)
			throws SQLException {
		Dao.Tables.t_concern t_concern = new Dao().new Tables().new t_concern();
		t_concern.moduleId.setValue(id);
		t_concern.userId.setValue(userId);
		t_concern.moduleType.setValue(moduleType);
		return t_concern.insert(conn);
	}

	/**
	 * @MethodName: hasFocusOn
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-16 上午11:02:03
	 * @Return:
	 * @Descb: 查询用户是否已经有关注
	 * @Throws:
	 */
	public Map<String, String> hasFocusOn(Connection conn, long id,
			long userId, int moduleType) throws SQLException, DataException {
		String condition = "moduleId = " + id + " and userId = " + userId
				+ " and moduleType=" + moduleType;
		Dao.Tables.t_concern t_concern = new Dao().new Tables().new t_concern();
		DataSet dataSet = t_concern.open(conn, " id ", condition, "", 0, 1);
		condition = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: addUserMail
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午09:59:59
	 * @Return:
	 * @Descb: 添加用户站内信
	 * @Throws:
	 */
	public Long addUserMail(Connection conn, long reciver, Long userId,
			String title, String content, int mailType) throws SQLException {
		Dao.Tables.t_mail t_mail = new Dao().new Tables().new t_mail();
		t_mail.reciver.setValue(reciver);
		t_mail.sender.setValue(userId);
		t_mail.mailTitle.setValue(title);
		t_mail.mailContent.setValue(content);
		t_mail.mailType.setValue(mailType);
		t_mail.mailMode.setValue(1);
		t_mail.sendTime.setValue(new Date());
		return t_mail.insert(conn);
	}

	/**
	 * @MethodName: addUserReport
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午10:13:31
	 * @Return:
	 * @Descb:
	 * @Throws:
	 */
	public Long addUserReport(Connection conn, long reporter, Long userId,
			String title, String content) throws SQLException {
		Dao.Tables.t_report t_report = new Dao().new Tables().new t_report();
		t_report.reporter.setValue(reporter);
		t_report.user.setValue(userId);
		t_report.reportTitle.setValue(title);
		t_report.reportContent.setValue(content);
		t_report.reportTime.setValue(new Date());
		return t_report.insert(conn);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryLastBorrow
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-18 上午09:25:24
	 * @Return:
	 * @Descb:
	 * @Throws:
	 */
	public List<Map<String, Object>> queryLastestBorrow(Connection conn)
			throws SQLException, DataException {
		Dao.Views.v_t_borrow_index v_t_borrow_index = new Dao().new Views().new v_t_borrow_index();
		DataSet dataSet = v_t_borrow_index.open(conn, " * ", " sorts != 0 ",
				" sorts desc , schedules asc  ", 0,3);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 富车贷,房贷（最新n条）.
	 * 
	 * @param conn
	 * @param n
	 *            多少条.
	 * @param subject_matter
	 *            标的属性（2房贷，1车贷）
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryLastestCarOrHouseBorrow(
			Connection conn, int n, int subject_matter) throws SQLException,
			DataException {
		StringBuffer command = new StringBuffer();

		command.append("select `a`.`id` AS `id`,`a`.`publishTime` AS `publishTime`,`a`.`imgPath` AS `imgPath`,`a`.`borrowTitle` AS `borrowTitle`,`a`.`sort` AS `sorts`, ");
		command.append("`f_formatAmount`(`a`.`borrowAmount`) AS `borrowAmount`,`a`.`annualRate` AS `annualRate`,`f_injectPoint`(((`a`.`hasInvestAmount` / `a`.`borrowAmount`) * 100)) AS `schedules`, ");
		command.append("`f_formatAmount`((`a`.`borrowAmount` - `a`.`hasInvestAmount`)) AS `investNum`,`a`.`deadline` AS `deadline`,`a`.`isDayThe` AS `isDayThe`, ");
		command.append("`f_credit_rating`(`b`.`creditrating`) AS `credit`,`b`.`creditrating` AS `creditrating`,`a`.`borrowWay` AS `borrowWay`,`a`.`borrowStatus` AS `borrowStatus`, ");
		command.append("`a`.`excitationType` AS `excitationType`,`a`.`excitationSum` AS `excitationSum`,`a`.`hasPWD` AS `hasPWD`,`c`.`auditStatus` AS `auditStatus`, ");
		command.append("`a`.`borrowShow` AS `borrowShow` ,`a`.subject_matter ");
		command.append("from ((`t_borrow` `a`  ");
		command.append(" left join `t_user` `b` on((`a`.`publisher` = `b`.`id`))) ");
		command.append("   left join `t_materialsauth` `c` on((`c`.`userId` = `b`.`id`))) ");
		command.append("where ((`a`.`borrowStatus` < 6) and (`c`.`materAuthTypeId` = 14))  AND `a`.subject_matter = "
				+ subject_matter + "  ");
		command.append("order by `a`.`publishTime`desc limit " + n + ";");

		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		command = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 获取当天最新的新手标
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryLastestNoviceBorrow(Connection conn)
			throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("select `b`.id,`b`.borrowStatus,`b`.annualRate,FORMAT((10000*((`b`.annualRate/12/30/100)*`b`.deadline)),2) as annualAmount,count(t.borrowId) as investorNum,(`b`.`borrowAmount`-`b`.`hasInvestAmount`) as canInvest  ");
		command.append(" from t_borrow `b` LEFT JOIN t_invest `t` on `b`.id = t.borrowId ");

		command.append(" WHERE `b`.subject_novice = 1 and `b`.publishTime > timestamp(date(sysdate())) and `b`.borrowStatus in (2,3,4,5) ");
		command.append(" ORDER BY b.publishTime DESC LIMIT 1; ");

		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 获取活动标（最新n条）.
	 * 
	 * @param conn
	 * @param n
	 *            多少条.
	 * @param subject_activity
	 *            是否活动标（0-否,1-是）
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryLastestActivityBorrow(
			Connection conn, int n) throws SQLException,
			DataException {
		StringBuffer command = new StringBuffer();

		command.append("select `a`.`id` AS `id`,`a`.`publishTime` AS `publishTime`,`a`.`imgPath` AS `imgPath`,`a`.`borrowTitle` AS `borrowTitle`,`a`.`sort` AS `sorts`, ");
		command.append("`f_formatAmount`(`a`.`borrowAmount`) AS `borrowAmount`,`a`.`annualRate` AS `annualRate`,`f_injectPoint`(((`a`.`hasInvestAmount` / `a`.`borrowAmount`) * 100)) AS `schedules`, ");
		command.append("`f_formatAmount`((`a`.`borrowAmount` - `a`.`hasInvestAmount`)) AS `investNum`,`a`.`deadline` AS `deadline`,`a`.`isDayThe` AS `isDayThe`, ");
		command.append("`f_credit_rating`(`b`.`creditrating`) AS `credit`,`b`.`creditrating` AS `creditrating`,`a`.`borrowWay` AS `borrowWay`,`a`.`borrowStatus` AS `borrowStatus`, ");
		command.append("`a`.`excitationType` AS `excitationType`,`a`.`excitationSum` AS `excitationSum`,`a`.`hasPWD` AS `hasPWD`,`c`.`auditStatus` AS `auditStatus`, ");
		command.append("`a`.`borrowShow` AS `borrowShow` ,`a`.subject_matter ");
		command.append("from ((`t_borrow` `a`  ");
		command.append(" left join `t_user` `b` on((`a`.`publisher` = `b`.`id`))) ");
		command.append("   left join `t_materialsauth` `c` on((`c`.`userId` = `b`.`id`))) ");
		command.append("where `a`.`borrowStatus` in (2,3) and `c`.`materAuthTypeId` = 14 AND `a`.subject_activity = 1  ");
		command.append("order by  `a`.borrowStatus ASC ,`a`.`publishTime`desc limit " + n + ";");

		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		command = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
	/**
	 * @MethodName: investRank
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-18 上午11:10:04
	 * @Return:
	 * @Descb: 投资排名前8条记录
	 * @Throws:
	 */
	public List<Map<String, Object>> investRank(Connection conn,
			String starTime, String endTime) throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SUM(t.borrowSum) AS borrowSum,f_formatting_username(b.username) as username,f_rating(b.rating) AS rating FROM(");
		sql.append(" SELECT SUM(realAmount) AS borrowSum,investor,investTime FROM t_invest ");
		if (StringUtils.isNotBlank(starTime)) {
			sql.append(" where investTime >= '"
					+ StringEscapeUtils.escapeSql(starTime) + "'");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append("  and investTime <='"
					+ StringEscapeUtils.escapeSql(endTime) + "'");
		}
		sql.append(" GROUP BY investor");
		sql.append(" UNION ALL SELECT SUM(realAmount) AS borrowSum,investor,investTime FROM t_invest_history ");
		if (StringUtils.isNotBlank(starTime)) {
			sql.append(" where investTime >= '"
					+ StringEscapeUtils.escapeSql(starTime) + "'");
		}
		if (StringUtils.isNotBlank(endTime)) {
			sql.append("  and investTime <='"
					+ StringEscapeUtils.escapeSql(endTime) + "'");
		}
		sql.append("GROUP BY investor");
		sql.append(" )t LEFT JOIN t_user b ON t.investor = b.id GROUP BY t.investor ORDER BY borrowSum DESC LIMIT 0,8");
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sql = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: queryTotalRisk
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午01:33:42
	 * @Return:
	 * @Descb: 查询风险保障金总额
	 * @Throws:
	 */
	public Map<String, String> queryTotalRisk(Connection conn)
			throws SQLException, DataException {
		DataSet dataSet = MySQL
				.executeQuery(
						conn,
						"SELECT ((SUM(riskInCome)-SUM(riskSpending))) AS total FROM t_risk_detail limit 0,1");
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: queryCurrentRisk
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午01:33:46
	 * @Return:
	 * @Descb: 查询当日风险保障金收支金额
	 * @Throws:
	 */
	public Map<String, String> queryCurrentRisk(Connection conn)
			throws SQLException, DataException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sf.format(new Date());
		Dao.Tables.t_risk_detail t_risk_detail = new Dao().new Tables().new t_risk_detail();
		DataSet dataSet = t_risk_detail
				.open(conn,
						" sum(riskInCome) as riskInCome,sum(riskSpending) as riskSpending ",
						" riskDate='" + date + "'", "", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: getInvestPWD
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-5 下午05:38:27
	 * @Return:
	 * @Descb: 获取投标密码是否正确
	 * @Throws:
	 */
	public Map<String, String> getInvestPWD(Connection conn, long idLong,
			String investPWD) throws SQLException, DataException {
		Dao.Tables.t_borrow t_borrow = new Dao().new Tables().new t_borrow();
		DataSet dataSet = t_borrow.open(
				conn,
				" id ",
				" id=" + idLong + " and investPWD='"
						+ StringEscapeUtils.escapeSql(investPWD.trim()) + "'",
				"", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 查找投资人信息 add by houli
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> queryInvestorById(Connection conn,
			long investorId, int limitStart, int limitCount)
			throws SQLException, DataException {
		Dao.Tables.t_invest t_invest = new Dao().new Tables().new t_invest();
		DataSet dataSet = t_invest.open(conn, " investor ", " investor="
				+ investorId, "", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: queryInvestIdByFlag
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-11 下午05:38:44
	 * @Return:
	 * @Descb: 查询投资的id
	 * @Throws:
	 */
	public Map<String, String> queryInvestIdByFlag(Connection conn, String flag)
			throws SQLException, DataException {
		Dao.Tables.t_invest t_invest = new Dao().new Tables().new t_invest();
		DataSet dataSet = t_invest
				.open(conn, " id ", " flag=" + flag, "", 0, 1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryUserImageByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-16 上午11:21:25
	 * @Return:
	 * @Descb: 查询用户认证通过的图片
	 * @Throws:
	 */
	public List<Map<String, Object>> queryUserImageByid(Connection conn,
			long typeId, long userId) throws SQLException, DataException {
		Dao.Views.v_t_borrow_user_materauth_img user_materialsauth_img = new Dao().new Views().new v_t_borrow_user_materauth_img();
		DataSet dataSet = user_materialsauth_img.open(conn, " * ",
				" materAuthTypeId=" + typeId + " and userId=" + userId, "", -1,
				-1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * @MethodName: queBorrowInfo
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午01:56:10
	 * @Return:
	 * @Descb: 查询借款信息
	 * @Throws:
	 */
	public Map<String, String> queBorrowInfo(Connection conn, long id)
			throws DataException, SQLException {
		StringBuffer command = new StringBuffer();
		command.append(" select a.borrowWay as borrowWay,a.auditTime as auditTime, a.excitationType as excitationType,a.excitationSum as excitationSum,a.circulationNumber as circulationNumber, a.version,a.annualRate,(a.annualRate/12) monthRate");
		command.append(",a.borrowAmount,a.deadline,a.borrowTitle,a.publisher,a.isDayThe,b.username as borrowerName from");
		command.append(" t_borrow a left join t_user b on a.publisher=b.id where a.id="
				+ id);
		DataSet ds = MySQL.executeQuery(conn, command.toString());
		return DataSetHelper.dataSetToMap(ds);
	}

	/**
	 * @MethodName: addInvest
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午02:04:59
	 * @Return:
	 * @Descb: 添加投资记录
	 * @Throws:
	 */
	public long addInvest(Connection conn, double investAmount,
			double realAmount, double monthRate, long investor,
			long oriInvestor, long id, int deadline, int isAutoBid)
			throws SQLException {
		Dao.Tables.t_invest t_invest = new Dao().new Tables().new t_invest();
		t_invest.investAmount.setValue(investAmount);
		t_invest.realAmount.setValue(realAmount);
		t_invest.monthRate.setValue(monthRate);
		t_invest.investor.setValue(investor);
		t_invest.oriInvestor.setValue(oriInvestor);
		t_invest.borrowId.setValue(id);
		t_invest.deadline.setValue(deadline);
		t_invest.investTime.setValue(new Date());
		t_invest.isAutoBid.setValue(isAutoBid);
		return t_invest.insert(conn);
	}

	/**
	 * @MethodName: updateBorrowStatus
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午04:47:06
	 * @Return:
	 * @Descb: 更新借款状态
	 * @Throws:
	 */
	public long updateBorrowStatus(Connection conn, double investAmount,
			long copies, long id, int version) throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("UPDATE t_borrow SET hasInvestAmount = hasInvestAmount+ "
				+ investAmount);
		command.append(",hasCirculationNumber=hasCirculationNumber+" + copies);
		command.append(",investNum=investNum+1,version=version+1 WHERE id ="
				+ id);
		command.append(" and hasInvestAmount <borrowAmount and version="
				+ version);
		returnId = MySQL.executeNonQuery(conn, command.toString());
		return returnId;
	}

	/**
	 * @MethodName: freezeUserAmount
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午04:48:11
	 * @Return:
	 * @Descb: 投资人投资成功资金冻结
	 * @Throws:
	 */
	public long freezeUserAmount(Connection conn, double investAmount,
			long userId) throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("UPDATE t_user SET usableSum = usableSum-"
				+ investAmount + ", freezeSum=freezeSum+" + investAmount
				+ " WHERE id =" + userId);
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * @MethodName: updateFullBorrow
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午04:51:11
	 * @Return:
	 * @Descb: 更新满标的借款
	 * @Throws:
	 */
	public long updateFullBorrow(Connection conn, long id) throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("update t_borrow set borrowStatus =3,sort = 5,remainTimeStart= remainTimeEnd where borrowAmount=hasInvestAmount AND borrowStatus = 2 AND id = "
				+ id);
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * @MethodName: queryUserAmountAfterHander
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午05:06:05
	 * @Return:
	 * @Descb: 查询用户操作后的资金记录
	 * @Throws:
	 */
	public Map<String, String> queryUserAmountAfterHander(Connection conn,
			long userId) throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("select ifnull(a.usableSum,0) usableSum,ifnull(a.freezeSum,0) freezeSum,ifnull(sum(b.recivedPrincipal+b.recievedInterest-b.hasPrincipal-b.hasInterest),0.0) forPI ,a.lastIP as lastIP ");
		command.append(" from t_user a left join t_invest b on a.id = b.investor where a.id="
				+ userId + " group by a.id");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: addUserDynamic
	 * @Param: BorrowDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 上午10:28:50
	 * @Return:
	 * @Descb: 添加用户动态
	 * @Throws:
	 */
	public long addUserDynamic(Connection conn, long userId, String url)
			throws SQLException {
		Dao.Tables.t_user_recorelist t_user_recorelist = new Dao().new Tables().new t_user_recorelist();
		t_user_recorelist.userId.setValue(userId);
		t_user_recorelist.url.setValue(url);
		t_user_recorelist.time.setValue(new Date());
		return t_user_recorelist.insert(conn);
	}

	/**
	 * @MethodName: updateFullScall
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:01:50
	 * @Return:
	 * @Descb: 更新满标的状态
	 * @Throws:
	 */
	public void updateFullScall(Connection conn, long id) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append("update t_borrow set borrowStatus =3,remainTimeStart= remainTimeEnd where borrowAmount=hasInvestAmount AND borrowStatus = 2 and id ="
				+ id);
		MySQL.executeNonQuery(conn, command.toString());
		command = null;
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryBorrowTenderIn
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:03:40
	 * @Return:
	 * @Descb: 查询招标中的借款
	 * @Throws:
	 */
	public Map<String, String> queryBorrowTenderIn(Connection conn, long id)
			throws DataException, SQLException {
		StringBuffer command = new StringBuffer();
		command.append("SELECT id,version,smallestFlowUnit,(circulationNumber-hasCirculationNumber) as remainCirculationNumber FROM t_borrow WHERE "
				+ "borrowAmount>hasInvestAmount AND borrowStatus = 2 AND id ="
				+ id);
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryInvestAmount
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:06:31
	 * @Return:
	 * @Descb: 本轮剩余投标金额
	 * @Throws:
	 */
	public Map<String, String> queryInvestAmount(Connection conn, long id,
			double investAmount) throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("SELECT id FROM t_borrow WHERE (borrowAmount-hasInvestAmount) >="
				+ investAmount + "  AND id =" + id);
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryMinTenderedSum
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:19:15
	 * @Return:
	 * @Descb: 查询最小投标金额
	 * @Throws:
	 */
	public Map<String, String> queryMinTenderedSum(Connection conn, long id,
			double investAmount) throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("SELECT id FROM t_borrow WHERE minTenderedSum > (borrowAmount-hasInvestAmount) and id ="
				+ id);
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryUserUsableSum
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:19:25
	 * @Return:
	 * @Descb: 查询用户可用金额
	 * @Throws:
	 */
	public Map<String, String> queryUserUsableSum(Connection conn, long userId,
			double investAmount) throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("SELECT id,usableSum  FROM t_user WHERE usableSum < "
				+ investAmount + " and id =" + userId);
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryMaxTenderedSum
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:24:07
	 * @Return:
	 * @Descb: 查询最大投标金额
	 * @Throws:
	 */
	public Map<String, String> queryMaxTenderedSum(Connection conn, long id,
			double investAmount) throws DataException, SQLException {
		StringBuffer command = new StringBuffer();
		command.append("SELECT id FROM t_borrow WHERE maxTenderedSum < "
				+ investAmount + " and maxTenderedSum is not null and id ="
				+ id);
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: queryMinTenderedSumMaps
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-25 上午10:26:49
	 * @Return:
	 * @Descb: 查询本轮最小投标金额
	 * @Throws:
	 */
	public Map<String, String> queryMinTenderedSumMaps(Connection conn,
			long id, double investAmount) throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("SELECT id FROM t_borrow WHERE minTenderedSum > "
				+ investAmount + " and minTenderedSum is not null and id ="
				+ id);
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: addAutoBidRecord
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-4-27 上午11:47:20
	 * @Return:
	 * @Descb: 添加自动投标用户投标记录
	 * @Throws:
	 */
	public long addAutoBidRecord(Connection conn, long userId, int borrowId)
			throws SQLException {
		Dao.Tables.t_automaticbid_user t_automaticbid_user = new Dao().new Tables().new t_automaticbid_user();
		t_automaticbid_user.userId.setValue(userId);
		t_automaticbid_user.borrowId.setValue(borrowId);
		return t_automaticbid_user.insert(conn);
	}

	/**
	 * @MethodName: queryHasInvest
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-5-14 上午10:54:46
	 * @Return:
	 * @Descb: 查询已投资总额是否小于等于借款总额
	 * @Throws:
	 */
	public Map<String, String> queryHasInvest(Connection conn, long id)
			throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append("select id from t_borrow where hasInvestAmount <=borrowAmount and id ="
				+ id);
		DataSet dataSet = Database.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * @MethodName: reBackBorrowStatus
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-5-14 下午04:56:11
	 * @Return:
	 * @Descb: 回退借款信息中的已投资总额和数量
	 * @Throws:
	 */
	public long reBackBorrowStatus(Connection conn, double bidAmount,
			int borrowId) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append("UPDATE t_borrow SET hasInvestAmount = hasInvestAmount- "
				+ bidAmount + ",investNum=investNum-1 WHERE id =" + borrowId);
		long result = Database.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}

	/**
	 * @MethodName: updateRepo
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-5-21 上午09:05:09
	 * @Return:
	 * @Descb: 更新回购中的状态
	 * @Throws:
	 */
	public void updateRepo(Connection conn, Long id) throws SQLException {
		String command = "update t_borrow set borrowStatus =4 where borrowAmount=hasInvestAmount AND borrowStatus = 2 and id ="
				+ id;
		MySQL.executeNonQuery(conn, command.toString());
		command = null;
	}

	public long addCirculatioinInvest(Connection conn, double investAmount,
			double realAmount, double monthRate, long investor,
			long oriInvestor, long id, int deadline,
			int circulationForpayStatus, double circulationInterest,
			Date repayDate, double excutation, String reason, int isAutoBid)
			throws SQLException {
		Dao.Tables.t_invest t_invest = new Dao().new Tables().new t_invest();
		t_invest.investAmount.setValue(investAmount);
		t_invest.realAmount.setValue(realAmount);
		t_invest.monthRate.setValue(monthRate);
		t_invest.investor.setValue(investor);
		t_invest.oriInvestor.setValue(oriInvestor);
		t_invest.borrowId.setValue(id);
		t_invest.deadline.setValue(deadline);
		t_invest.investTime.setValue(new Date());
		if (repayDate != null) {
			t_invest.repayDate.setValue(repayDate);
		}
		t_invest.circulationForpayStatus.setValue(circulationForpayStatus);
		t_invest.circulationInterest.setValue(circulationInterest);
		t_invest.recivedPrincipal.setValue(investAmount);
		t_invest.recievedInterest.setValue(circulationInterest);
		t_invest.reward.setValue(excutation);
		t_invest.reason.setValue(reason);
		t_invest.isAutoBid.setValue(isAutoBid);

		return t_invest.insert(conn);
	}

	/**
	 * @throws SQLException
	 * @MethodName: addUserAmount
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-5-21 上午11:04:23
	 * @Return:
	 * @Descb: 添加用户金额
	 * @Throws:
	 */
	public long addUserAmount(Connection conn, double investAmount, long userId)
			throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append("update t_user set usableSum =usableSum+" + investAmount
				+ " where id =" + userId);
		return MySQL.executeNonQuery(conn, command.toString());
	}

	/**
	 * @MethodName: deductedUserAmount
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-5-21 上午11:04:14
	 * @Return:
	 * @Descb: 扣除用户金额
	 * @Throws:
	 */
	public long deductedUserAmount(Connection conn, double investAmount,
			long userId) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append("update t_user set usableSum =usableSum-" + investAmount
				+ " where id =" + userId);
		return MySQL.executeNonQuery(conn, command.toString());
	}

}
