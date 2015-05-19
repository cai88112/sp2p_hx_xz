/**
 * 
 */
package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_returnedmoney;

/**
 * 回款续投dao层.
 * 
 * @author 殷梓淞
 * @since v1.0.0
 *
 */
public class ReturnedmoneyDao {

	/**
	 * 添加回款表.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @return
	 */
	public long addReturnedMoney(Connection conn, long userId)
			throws SQLException {
		long returnId = 0;
		Dao.Tables.t_returnedmoney t_returnedmoney = new Dao().new Tables().new t_returnedmoney();
		t_returnedmoney.userId.setValue(userId);
		returnId = t_returnedmoney.insert(conn);
		return returnId;
	}

	/**
	 * 更新回款表.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param sumreturnedmoney
	 *            累计回款金额.
	 * @param recievedreinvestreward
	 *            累计应收续投奖金.
	 * @param hasreinvestreward
	 *            累计已收续投奖金.
	 * @return
	 */
	public long updateReturnedMoney(Connection conn, long userId,
			double sumreturnedmoney, double recievedreinvestreward,
			double hasreinvestreward) throws SQLException {
		long returnId = 0;
		Dao.Tables.t_returnedmoney t_returnedmoney = new Dao().new Tables().new t_returnedmoney();
		t_returnedmoney.sumreturnedmoney.setValue(sumreturnedmoney);
		t_returnedmoney.recievedreinvestreward.setValue(recievedreinvestreward);
		t_returnedmoney.hasreinvestreward.setValue(hasreinvestreward);
		t_returnedmoney.update(conn, "userId = " + userId);
		return returnId;
	}

	/**
	 * 还款时加回款.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param sumreturnedmoney
	 *            累计回款金额.
	 * @return
	 */
	public long updateSumReturnedMoneyByUserId(Connection conn, long userId,
			double sumreturnedmoney) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append(" update t_returnedmoney set sumreturnedmoney = sumreturnedmoney+"
				+ sumreturnedmoney + " where userId = " + userId);
		long result = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}
	
	/**
	 * 还款时加回款.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param sumreturnedmoney
	 *            累计回款金额.
	 * @return
	 */
	public long updateMinusSumReturnedMoneyByUserId(Connection conn, long userId,
			double sumreturnedmoney) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append(" update t_returnedmoney set sumreturnedmoney = sumreturnedmoney-"
				+ sumreturnedmoney + " where userId = " + userId);
		long result = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}

	/**
	 * 投款减去sumreturnedmoney的投款量，累加累计应收续投奖金.
	 * 
	 * @param conn
	 * @param userId
	 *            用户id.
	 * @param touzimoney
	 *            投资金额.
	 * 
	 * @param rewardmoney
	 *            奖励金额.
	 * @return 执行结果.
	 */
	public long updateReturnedMoneyByUserId(Connection conn, long userId,
			double touzimoney, double rewardmoney) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append(" update t_returnedmoney set sumreturnedmoney = sumreturnedmoney-"
				+ touzimoney
				+ ",recievedreinvestreward = recievedreinvestreward+"
				+ rewardmoney + " where userId = " + userId);
		long result = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}

	/**
	 * 添加回款明细记录.
	 * 
	 * @param conn
	 * @param investid
	 *            投资表id.
	 * @param returnedmoneyamount
	 *            回款使用金额.
	 * @param reinvestreward
	 *            回款续投奖金.
	 * @return
	 */
	public long addReturnedMoneyDetail(Connection conn, long investid,
			double returnedmoneyamount, double reinvestreward)
			throws SQLException {
		long returnId = 0;
		Dao.Tables.t_returnedmoney_detail t_returnedmoney_detail = new Dao().new Tables().new t_returnedmoney_detail();
		t_returnedmoney_detail.investId.setValue(investid);
		t_returnedmoney_detail.returnedmoneyamount
				.setValue(returnedmoneyamount);
		t_returnedmoney_detail.reinvestreward.setValue(reinvestreward);
		returnId = t_returnedmoney_detail.insert(conn);
		return returnId;
	}

	/**
	 * 获取回款奖励规则.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, String>> getReturnedMoneyRule(Connection conn)
			throws SQLException {
		Dao.Tables.t_returnedmoney_rule t_returnedmoney_rule = new Dao().new Tables().new t_returnedmoney_rule();
		DataSet dsDataSet = t_returnedmoney_rule.open(conn,
				" id,deadline,rewardrule ", "", "", -1, -1);
		dsDataSet.tables.get(0).rows.genRowsMap();
		return dsDataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 分页查询所有用户回款信息
	 * 
	 * @param conn
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryReturnedmoneyPageAll(Connection conn,
			PageBean<Map<String, Object>> pageBean) throws SQLException,
			DataException {
		Dao.Tables.t_returnedmoney t_returnedmoney = new Dao().new Tables().new t_returnedmoney();
		long c = t_returnedmoney.getCount(conn, " ");
		boolean result = pageBean.setTotalNum(c);// -------->将总页数(c)放到PageBean<T>中
		if (result) {
			DataSet ds = t_returnedmoney
					.open(conn,
							" id,userId,sumreturnedmoney,(recievedreinvestreward - hasreinvestreward) as recievedreinvestreward,hasreinvestreward",
							" ", " ", pageBean.getStartOfPage(),
							pageBean.getPageSize());
			ds.tables.get(0).rows.genRowsMap();// 将DataSet转换成map
			pageBean.setPage(ds.tables.get(0).rows.rowsMap);// 放入PageBean 类
		}
	}

	// 条件查询

	/**
	 * 根据borrowId获取RewardRule.
	 * 
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> getRewardRuleByBorrowId(Connection conn,
			long borrowId) throws SQLException {

		StringBuffer sql = new StringBuffer();
		sql.append("select tr.rewardrule,tb.isDayThe from t_borrow tb left JOIN  t_returnedmoney_rule tr on tb.deadline = tr.deadline  WHERE tb.id ="
				+ borrowId);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql = null;
		return DataSetHelper.dataSetToMap(dataSet);

	}

	/**
	 * 根据userId获取T_returnMoney表的某条信息.
	 * 
	 * @param conn
	 * @param userId
	 * @return Map<String, String>一条数据.
	 * @throws SQLException
	 */
	public Map<String, String> getReturnMoneyByUserID(Connection conn,
			long userId) throws SQLException {

		StringBuffer sql = new StringBuffer();
		sql.append("select id,userId,sumreturnedmoney from  t_returnedmoney  WHERE userId ="
				+ userId);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql = null;
		return DataSetHelper.dataSetToMap(dataSet);

	}

	/**
	 * 查询全部应收奖励大于已发奖励的t_returnmoney表的信息
	 * 
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> getReturnmoneyByRecievedMinusHasre(
			Connection conn) throws SQLException {

		StringBuffer sql = new StringBuffer();
		sql.append("select id,userId,sumreturnedmoney,recievedreinvestreward,hasreinvestreward,recievedreinvestreward-hasreinvestreward AS money from t_returnedmoney where (recievedreinvestreward-hasreinvestreward)>0");
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sql = null;
		return dataSet.tables.get(0).rows.rowsMap;

	}

	/**
	 * 发放续投奖励，修改累计已收续投奖金字段，增加rewardmoney数目金额.
	 * 
	 * @param conn
	 * @param id
	 *            t_returnedmoney的id.
	 * @param rewardmoney
	 *            奖励金额.
	 * @return 执行结果.
	 */
	public long updateHasreInvestRewardById(Connection conn, long id,
			double rewardmoney) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append(" update t_returnedmoney set hasreinvestreward = hasreinvestreward+"
				+ rewardmoney + " where id = " + id);
		long result = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return result;
	}
	
	
	/**
	 * 增加资金记录
	 * @param conn
	 * @param t_invest_amount
	 * @param t_investor
	 * @param t_invest_id
	 * @param borrowerId
	 * @param t_content
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertFundRecord(Connection conn,double handleSum, long userId,String pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,spending,income,pMerBillNo)	 ");
		
		command.append("select "+userId+", now(), -1, '双乾支付', "+handleSum+",a.usableSum as usableSum, a.freezeSum as freezeSum, round(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),2),");
		command.append("-1,0, concat('回款续投平台奖励金额[',"+handleSum+",']元'), 0, -1, '双乾支付', 0,0,"+handleSum+",'"+pMerBillNo+"' from t_user a left join t_invest b on a.id = b.investor  where a.id ="+userId+" group by a.id;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	
	
}
