package com.sp2p.dao.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_userintegraldetail;

public class UserManageDao {

	/**
	 * 用户基本信息里面的查看用户的基本信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryUserManageInnerMsg(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_usermanage_baseinfoinner baseinfoinner = new Dao().new Views().new v_t_usermanage_baseinfoinner();

		DataSet dataSet = baseinfoinner.open(conn, "", " id = " + id, "", -1,
				-1);
		return DataSetHelper.dataSetToMap(dataSet);
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
	public Map<String, String> queryUserManageaddInteral(Connection conn,
			Long userId) throws SQLException, DataException {
		DataSet dataSet = MySQL
				.executeQuery(
						conn,
						"select  tuser.id,tuser.username as username,tuser.creditrating as creditrating,tuser.rating as rating ,tp.realName as realName  from t_user tuser left join t_person tp on tuser.id = tp.userId where tuser.id = "
								+ userId);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	public Map<String, String> queryUserInfo(Connection conn, long userId)
			throws SQLException, DataException {
		DataSet dataSet = MySQL
				.executeQuery(
						conn,
						"select tuser.id as id,tuser.username as username,tuser.creditrating as creditrating,tuser.rating as rating ,tuser.createTime as createTime,tp.realName as realName,tp.qq as qq,tuser.email as email,tuser.lastIP as lastIP,tp.cellPhone as cellPhone  from t_user tuser left join t_person tp on tuser.id = tp.userId where tuser.id = "
								+ userId);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	public Long updateUserqq(Connection conn, Long userId, String qq)
			throws SQLException {
		Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		person.qq.setValue(qq);
		return person.update(conn, " userId = " + userId);
	}

	/**
	 * * 向user表插入数据
	 * 
	 * @param conn
	 * @param userId
	 * @param score
	 * @param type
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addUserManageaddInteral(Connection conn, Long userId,
			Integer score, Integer type) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Map<String, String> userMap = null;
		DataSet dataSet = user.open(conn, "creditrating,rating", " id = "
				+ userId, "", -1, -1);
		userMap = DataSetHelper.dataSetToMap(dataSet);
		Integer precreditrating = null;
		Integer prerating = null;
		if (userMap != null && userMap.size() > 0) {
			precreditrating = Convert.strToInt(userMap.get("creditrating"), -1);
			prerating = Convert.strToInt(userMap.get("rating"), -1);
			if (precreditrating != -1 && type == 1) {
				user.creditrating.setValue(precreditrating + score);
				return user.update(conn, " id = " + userId);
			}
			if (prerating != -1 && type == 2) {
				user.rating.setValue(prerating + score);
				return user.update(conn, " id = " + userId);
			}
		}
		return -1L;
	}

	/**
	 * 向积分记录表添加记录
	 * 
	 * @param conn
	 * @param userId
	 * @param score
	 * @param type
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addserintegraldetail(Connection conn, Long investId,
			Long userId, Double score, String typeStr, Integer type,
			String remark, String changetype) throws SQLException,
			DataException {
		Dao.Tables.t_userintegraldetail integraldetail = new Dao().new Tables().new t_userintegraldetail();
		integraldetail.changerecore.setValue(score);
		integraldetail.intergraltype.setValue(typeStr);
		integraldetail.remark.setValue(remark);
		integraldetail.changetype.setValue(changetype);// 先设置成增加
		integraldetail.time.setValue(new Date());
		integraldetail.userid.setValue(userId);

		if (type == 1) {// 信用积分
			integraldetail.type.setValue(1);
		}
		if (type == 2) {// vip积分
			integraldetail.type.setValue(2);
		}

		return integraldetail.insert(conn);
	}

	/**
	 * add by houli 查询用户资金信息
	 * 
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryUserCashInfo(Connection conn, Long userId)
			throws SQLException, DataException {
		String sqlStr = "SELECT (usableSum+freezeSum) as totalSum,usableSum from t_user where id="
				+ userId;
		DataSet dataSet = MySQL.executeQuery(conn, sqlStr);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 根据用户名模糊获取id.
	 * 
	 * @Title: getUserIdByUsername
	 * @param @param conn
	 * @param @param userName
	 * @param @return 设定文件
	 * @return List<Map<String,String>> 返回类型
	 * @throws
	 */
	public List<Map<String, String>> getUserIdByUsername(Connection conn,
			String userName) {
		Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();
		String condition = "";
		if(userName !=null || !"".equals(userName)){
			condition = "username like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'";	
		}
		DataSet dsDataSet = t_user.open(conn, "id",condition , "", -1,
				-1);
		dsDataSet.tables.get(0).rows.genRowsMap();
		return dsDataSet.tables.get(0).rows.rowsMap;
	}

}
