package com.sp2p.dao.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.constants.IConstants;
import com.sp2p.database.Dao;

public class FIManageDao {

	public Map<String,String> queryOneFirstChargeDetails(Connection conn, long rechargeId) throws SQLException, DataException{
		Dao.Views.v_t_user_rechargefirst_lists t_recharge_detail = 
			new Dao().new Views().new v_t_user_rechargefirst_lists();
		DataSet ds = t_recharge_detail.open(conn, "*", "id="+rechargeId, "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}
	
	public Map<String,String> queryOneChargeDetails(Connection conn, long rechargeId) throws SQLException, DataException{
		Dao.Views.v_t_user_rechargedetails_list t_recharge_detail = 
			new Dao().new Views().new v_t_user_rechargedetails_list();
		DataSet ds = t_recharge_detail.open(conn, "*", "id="+rechargeId, "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}
	
	public Map<String,String> queryOneWithdraw(Connection conn, long wid) throws SQLException, DataException{
		Dao.Views.v_t_user_withdraw_lists t_list = 
			new Dao().new Views().new v_t_user_withdraw_lists();
		DataSet ds = t_list.open(conn, "*", "id="+wid, "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}
	
	/**
	 * 后台 进行充值扣费信息添加处理
	 * @param conn
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public long addBackR_W(Connection conn, Long userId,Long adminId,
			Integer type,double money,String remark,Date date) throws SQLException {
		Dao.Tables.t_recharge_withdraw_info t_info = new Dao().new Tables().new t_recharge_withdraw_info();
		t_info.userId.setValue(userId);
		t_info.type.setValue(type);
		t_info.dealMoney.setValue(money);
		t_info.remark.setValue(remark);
		t_info.checkUser.setValue(adminId);
		t_info.checkTime.setValue(date);
		return t_info.insert(conn);
		
	}
	
	/**
	 * 更新资金流动信息表
	 * @param conn
	 * @param userId
	 * @param handleSum
	 * @param usableSum
	 * @return
	 * @throws SQLException 
	 */
	public Long updateFundrecord(Connection conn,Long userId,double money,int type) throws SQLException{
		if(type == IConstants.WITHDRAW){//扣费
			return MySQL.executeNonQuery(conn,
					" update t_user set `usableSum` = usableSum-" + money
							+" where id=" + userId);
		}else if(type == IConstants.RECHARAGE){//充值
			return MySQL.executeNonQuery(conn,
					" update t_user set `usableSum` = usableSum+" + money
							+" where id=" + userId);
		}else if(type == IConstants.WITHDRAW_FAIL){//审核或者转账失败（将提现的金额变成可用余额）
			return MySQL.executeNonQuery(conn,
					" update t_user set `usableSum` = usableSum+" + money
							+", `freezeSum`=freezeSum-"+money+ " where id=" + userId);
		}else if(type == 100){//转账成功（将冻结余额扣除）IConstants.WITHDRAW_SUCCESS = IConstants.WITHDRAW
			return MySQL.executeNonQuery(conn,
					" update t_user set  `freezeSum`=freezeSum-"+money+ " where id=" + userId);
		}
		else{
			return -1L;
		}
	}
	
	/**
	 * 某用户充值成功总额
	 * @param conn
	 * @param userId
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String,String> querySuccessRecharge(Connection conn,Long userId,int result) 
		throws SQLException, DataException{
		String command = "SELECT sum(rechargeMoney) as r_total from t_recharge_detail where userId="
			+userId+" and result="+result;
		DataSet dataSet = MySQL.executeQuery(conn, command);
		command=null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 某用户提现成功总额
	 * @param conn
	 * @param userId
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String,String> querySuccessWithdraw(Connection conn,Long userId,int result) 
	throws SQLException, DataException{
		String command = "SELECT sum(sum) as w_total from t_withdraw where userId="
			+userId+" and status="+result;
		DataSet dataSet = MySQL.executeQuery(conn, command);
		command=null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 投标成功总额
	 * @throws SQLException 
	 */
	public Map<String,String> querySuccessBid(Connection conn,Long userId) 
	throws SQLException, DataException{
		Dao.Views.v_t_user_successtotalbid_lists lists = new Dao().new Views().new 
		v_t_user_successtotalbid_lists();
		DataSet dataSet = lists.open(conn, "*", "investor="+userId, "", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	//查询状态，如果已经转账过了，就不再转账
	public Map<String,String> queryTransStatus(Connection conn,Long wid) throws SQLException, DataException{
		Dao.Tables.t_withdraw t_info = new Dao().new Tables().new t_withdraw();
		DataSet dataSet = t_info.open(conn, "*", "id="+wid, "", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	public Long updateWithdraw(Connection conn,Long wid,double money,float poundage,int status,String remark,
			Long adminId,int oldStatus) throws SQLException{
		Dao.Tables.t_withdraw t_info = new Dao().new Tables().new t_withdraw();
		if(status >= 0)
			t_info.status.setValue(status);
		if(money >= 0)
			t_info.sum.setValue(money);
		if(remark != null)
			t_info.remark.setValue(remark);
		if(poundage >=0)
			t_info.poundage.setValue(poundage);
		if(adminId != -100)
			t_info.checkId.setValue(adminId);
		t_info.checkTime.setValue(new Date());
		return t_info.update(conn, " id="+wid+" and status="+oldStatus);
	}
	
	
	public Long updateWithdraws(Connection conn,String wids,int status,Long adminId) throws SQLException{
		String idStr = StringEscapeUtils.escapeSql("'"+wids+"'");
		String idSQL = "-2";
		idStr = idStr.replaceAll("'", "");
		String [] array = idStr.split(",");
		for(int n=0;n<=array.length-1;n++){
		   idSQL += ","+array[n];
		}
		Dao.Tables.t_withdraw t_info = new Dao().new Tables().new t_withdraw();
		t_info.checkId.setValue(adminId);
		t_info.status.setValue(status);
		
		return t_info.update(conn, " id in("+idSQL+")");
	}
	
	public Map<String,String> queryR_WInfo(Connection conn,Long rwId) 
	throws SQLException, DataException{
		Dao.Views.v_t_user_backrw_lists t_list = new Dao().new Views().new v_t_user_backrw_lists();
		
		DataSet ds = t_list.open(conn, "*", "id="+rwId, "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	} 
	
	public Map<String,String> queryDueInSum(Connection conn, Long userId) throws SQLException, DataException{
		StringBuffer command = new StringBuffer();
		command.append("select a.usableSum as usableSum ,a.freezeSum as freezeSum,sum(b.recivedPrincipal+b.recievedInterest-b.hasPrincipal-b.hasInterest) forPI");
		command.append(" from t_user a left join t_invest b on a.id = b.investor where a.id="+userId+" group by a.id");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command=null;
		return DataSetHelper.dataSetToMap(dataSet);

	}
	
	public List<Map<String,Object>> queryLastRecord(Connection conn) throws SQLException, DataException{
		StringBuffer command = new StringBuffer();
		command.append("SELECT * from (select * from t_fundrecord  ORDER BY recordTime desc ) a GROUP BY userId");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		command=null;
		return dataSet.tables.get(0).rows.rowsMap;

	}
}
