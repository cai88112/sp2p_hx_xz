/**
 * Project Name:sp2p_hx_xz
 * File Name:ProtectOldUserDao.java
 * Package Name:com.sp2p.dao.admin
 * Date:2015年3月11日下午5:32:47
 * Copyright (c) 2015, wteamfly All Rights Reserved.
 * 请勿修改或删除版权声明及文件头部.
 */

package com.sp2p.dao.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_help_type;


/**
 * ClassName: ProtectOldUserDao <br/>
 * date: 2015年3月11日 下午5:32:47 <br/>
 *
 * @author 殷梓淞
 * @version v1.0.0
 * @since JDK 1.7
 */
public class ProtectOldUserDao {

	/**
	 * 根据用户id查询用户是老米护盾信息.
	 * 
	 * @author 殷梓淞
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getProtectUserDetailByUserId(Connection conn,
			long userId) throws SQLException, DataException {
		Dao.Tables.t_protect_vip t_protect_vip = new Dao().new Tables().new t_protect_vip();
		DataSet ds = t_protect_vip.open(conn, "*", " userId = " + userId
				+ " and status = 1 ", "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}

	/**
	 * 根据月份查询利率.
	 *
	 * @author 殷梓淞
	 * @param conn
	 * @param month
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getProtectRateByMonth(Connection conn, int month)
			throws SQLException, DataException {
		Dao.Tables.t_protect_rate t_protect_rate = new Dao().new Tables().new t_protect_rate();
		DataSet ds = t_protect_rate.open(conn, "*", " month = " + month, "",
				-1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}

	/**
	 * 根据批次查询护盾标准. getProtectStandardByBatch:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author 殷梓淞
	 * @param conn
	 * @param batch
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getProtectStandardByBatch(Connection conn,
			int batch) throws SQLException, DataException {
		Dao.Tables.t_protect_standard t_protect_standard = new Dao().new Tables().new t_protect_standard();
		DataSet ds = t_protect_standard.open(conn, "*", " batch = " + batch,
				"", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}

	/***
	 * 更新t_protect_vip表,取消老米护盾超级vip资格.
	 * 
	 * @author 戴志越
	 * @param conn
	 * @param userId
	 *            userId.
	 * @return
	 * @throws SQLException
	 */
	public long updateProtectVipStatusToZero(Connection conn, long userId)
			throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append(" update t_protect_vip set status = 0"
				+ " where userId = " + userId);
		long result = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}

	/***
	 * 根据批次和标准查询不符合老米护盾资格的Vip列表.
	 * 
	 * @author 戴志越
	 * @param conn
	 * @param batch
	 *            "批次"参数.
	 * @param duestandard
	 *            "待收标准"参数.
	 * @return 不符合老米护盾资格的Vip用户的Map结果集.
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getProtectVipByBatch(Connection conn,
			int batch, double duestandard) throws SQLException {

		StringBuffer sql = new StringBuffer();
		sql.append("select  v.batch,u.dueinSum,v.userId from t_protect_vip v INNER JOIN t_protect_standard s "
				+ " on v.batch = s.batch INNER JOIN t_user u ON v.userId = u.Id "
				+ " where s.batch = "
				+ batch
				+ " and u.dueinSum < "
				+ duestandard);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sql = null;
		return dataSet.tables.get(0).rows.rowsMap;

	}

	/**
	 * 获取投资人当月待收.
	 *
	 * @author 殷梓淞
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getDueinSumOfMonth(Connection conn, long userId)
			throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select sum(recivedPrincipal+recievedInterest-hasPrincipal-hasInterest) as dueinSumOfMonth from  t_invest ");
		sql.append(" where  investTime > DATE_ADD(curdate(),interval -day(curdate())+1 day) and investTime<last_day(curdate()) and investor = "
				+ userId + ";");
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	
	/**
	 * 删除老米护盾
	 * @author 金庸
	 * @param conn
	 * @param userId 用户ID
	 * @return
	 * @throws SQLException
	 */
	public long deleteOldUser(Connection conn,long userId) throws SQLException{
		Dao.Tables.t_protect_vip t_protect = new Dao().new Tables().new t_protect_vip();
		return t_protect.delete(conn, " userId="+userId );
	}

	/**
	 * 查询全部护盾标准.
	 * 
	 * @author 戴志越
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> getAllProtectStandard(Connection conn)
			throws SQLException, DataException {
		Dao.Tables.t_protect_standard t_protect_standard = new Dao().new Tables().new t_protect_standard();
		DataSet dataSet = t_protect_standard.open(conn, "*", "", "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 弹出框显示信息初始化
	 * 
	 * @param conn
	 * @param userId
	 *            用户id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryProtectVipAddInteral(Connection conn,
			Long userId) throws SQLException, DataException {
		DataSet dataSet = MySQL
				.executeQuery(
						conn,
						"select  tuser.id,tuser.username as username,tuser.creditrating as creditrating,tuser.rating as rating ,tp.realName as realName, tuser.dueinSum from t_user tuser left join t_person tp on tuser.id = tp.userId where tuser.id = "
								+ userId);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 根据批次查询护盾标准. getProtectStandardByBatch:(这里用一句话描述这个方法的作用). <br/>
	 * 
	 * @author 殷梓淞
	 * @param conn
	 * @param batch
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, String>> getBatchList(Connection conn)
			throws SQLException, DataException {
		Dao.Tables.t_protect_standard t_protect_standard = new Dao().new Tables().new t_protect_standard();
		DataSet dataSet = t_protect_standard.open(conn, "*", "", "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
	/**
	 * * 向t_protect_vip表插入数据
	 * 
	 * @param conn
	 * @param userId
	 * @param score
	 * @param type
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addProtectVip(Connection conn, Long userId,
			Integer batch) throws SQLException, DataException {
		long returnId = 0;
		Dao.Tables.t_protect_vip vip = new Dao().new Tables().new t_protect_vip();
		vip.userId.setValue(userId);
		vip.batch.setValue(batch);
		vip.status.setValue(1);
		returnId = vip.insert(conn);
		return returnId;
	}
	
	/**
	 * 添加利息
	 * addInvest:(这里用一句话描述这个方法的作用). <br/>
	 *
	 * @author 殷梓淞
	 * @param conn
	 * @param in_userid
	 * @param in_money
	 * @return
	 * @throws SQLException
	 * @since JDK 1.7
	 */
	public Long addInvest(Connection conn, Long in_userid,Double in_money)throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append("update t_user set usableSum = ifnull(usableSum, 0.00) + "+in_money+" where id = "+in_userid+";");
		long result = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}
	/**
	 * 老米投标成功增加补偿资金记录.
	 * @param conn
	 * @param frozen_margin
	 * @param borrowerId
	 * @param borrow_id
	 * @param t_content
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertFundRecord(Connection conn,double amount, long userId,String in_pMerBillNo,long borrowId)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
		command.append(" select "+userId+",now(), -1 ,'双乾支付',"+amount+", a.usableSum, a.freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0), a.dueoutSum,"+userId+",'平台补偿老米投标利息',");
		command.append(" 0,-1,'双乾支付',"+amount+","+amount+","+borrowId+",'"+in_pMerBillNo+"' ");
		command.append(" from t_user a left join t_invest b on a.id = b.investor where a.id = "+userId+" group by a.id;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;			
	}
	
}
