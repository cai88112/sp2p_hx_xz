/**
 * 
 */
package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_borrow_type;
import com.sp2p.database.Dao.Tables.t_invest;
import com.sp2p.database.Dao.Views.intentionfund_user;

/**
 * 
 * @author yinzisong
 *
 */
public class ExperiencemoneyDao {

	/**
	 * 添加体验金.
	 * 
	 * @param userId
	 *            用户id.
	 * @param usableMoney
	 *            可用体验金.
	 * @param freezeMoney
	 *            冻结体验金.
	 * @return
	 * @throws Exception
	 */
	public Long addExperiencemoney(Connection conn, Long userId,
			Float usableMoney, Float freezeMoney) throws Exception {
		Dao.Tables.t_experiencemoney experiencemoney = new Dao().new Tables().new t_experiencemoney();
		experiencemoney.userid.setValue(userId);
		experiencemoney.usableMoney.setValue(usableMoney);
		experiencemoney.freezeMoney.setValue(freezeMoney);
		experiencemoney.timeStart.setValue(new Date());		
		  
		experiencemoney.timeEnd.setValue(DateFormatUtils.format(DateUtils.addMonths(new Date(), 3), "yyyy-MM-dd HH:mm:ss"));
		experiencemoney.status.setValue(0);
		return experiencemoney.insert(conn);
	}

	/**
	 * 更改体验金.
	 * 
	 * @param userId
	 *            用户id.
	 * @param usableMoney
	 *            可用金额.
	 * @param freezeMoney
	 *            冻结金额.
	 * @return
	 * @throws Exception
	 */
	public Long updateExperiencemoney(Connection conn, Long userId,
			Float usableMoney, Float freezeMoney, Float collectMoney, int status) throws Exception {
		Dao.Tables.t_experiencemoney experiencemoney = new Dao().new Tables().new t_experiencemoney();
		experiencemoney.usableMoney.setValue(usableMoney);
		experiencemoney.freezeMoney.setValue(freezeMoney);
		experiencemoney.collectMoney.setValue(collectMoney);
		experiencemoney.status.setValue(status);
		return experiencemoney.update(conn, "userid = " + userId);
	}

	/**
	 * 根据userId查询体验金.
	 * 
	 * @param userId
	 *            用户id.
	 * @return
	 * @throws DataException
	 */
	public Map<String, String> queryExperiencemoney(Connection conn, Long userId)
			throws DataException {

		Dao.Tables.t_experiencemoney t_experiencemoney = new Dao().new Tables().new t_experiencemoney();

		DataSet ds = t_experiencemoney
				.open(conn,
						"  id,userid,(usableMoney+freezeMoney+collectMoney) as totalAmount,usableMoney,freezeMoney,collectMoney,timeStart,timeEnd,`status` ",
						" userid = " + userId, "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}

	/**
	 * 扣除体验金.
	 * 
	 * @param userId
	 *            用户id.
	 * @return
	 * @throws Exception
	 */
	public long deductExperiencemoney(Connection conn, Long userId, int status)
			throws Exception {
		Dao.Tables.t_experiencemoney experiencemoney = new Dao().new Tables().new t_experiencemoney();
		experiencemoney.status.setValue(status);
		return experiencemoney.update(conn, "userid = " + userId);
	}

	/**
	 * 获取未处理的到期体验金列表.
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryUndisposedexpireExperiencemoney(
			Connection conn) {
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.UNDERLINE_DATE_SHORT);
		String command = " select id,userid,usableMoney,freezeMoney,timeStart,timeEnd,collectMoney,`status` FROM t_experiencemoney WHERE `status` = 0 and timeEnd < '"
				+ sf.format(new Date()) + "';";
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sf = null;
		command = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
	
	/**
	 * 分页查询所有用户体验金信息
	 * @param conn
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryExperiencemoneyPageAll( Connection  conn , PageBean<Map<String,Object>>  pageBean) throws SQLException, DataException{
		Dao.Tables.t_experiencemoney  t_experiencemoney = new Dao().new Tables().new t_experiencemoney();
		 long c= t_experiencemoney.getCount(conn, " ");
		 boolean  result=pageBean.setTotalNum(c);//-------->将总页数(c)放到PageBean<T>中
		 if(result){
			DataSet ds= t_experiencemoney.open(conn, " id,userid,usableMoney,freezeMoney,timeStart,timeEnd,collectMoney,`status` ", " ", " ", pageBean.getStartOfPage(), pageBean.getPageSize());
			ds.tables.get(0).rows.genRowsMap();//将DataSet转换成map
			pageBean.setPage(ds.tables.get(0).rows.rowsMap);//放入PageBean 类
		}	
	}
	
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String[] args) {

		Connection conn = MySQL.getConnection();
		ExperiencemoneyDao dao = new ExperiencemoneyDao();
		PageBean<Map<String,Object>>  pageBean= new PageBean<Map<String,Object>>();
		pageBean.setPageSize(5);
		pageBean.setPageNum(1);
		
		try {
			dao.queryExperiencemoneyPageAll(conn, pageBean);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
