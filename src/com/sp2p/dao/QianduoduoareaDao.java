/**   
* @Title: Qianduoduoarea.java 
* @Package com.sp2p.dao 
* @Description: TODO(用一句话描述该文件做什么) 
* @author yinzisong 690713748@qq.com   
* @date 2015年1月15日 下午1:44:47 
* @version V1.0   
*/ 
package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.sp2p.database.Dao;

/** 
 * 乾多多支付银行地区表.
 * @ClassName: Qianduoduoarea 
 * @Description: 乾多多支付银行地区表.
 * @author yinzisong 690713748@qq.com 
 * @date 2015年1月15日 下午1:44:47 
 *  
 */
public class QianduoduoareaDao {

	/**
	 * 查询省列表.
	 * 
	 * @Title: queryProvinceList
	 * @param
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryProvinceList(Connection conn)
			throws SQLException, DataException {
		Dao.Tables.t_qianduoduoarea t_qianduoduoarea = new Dao().new Tables().new t_qianduoduoarea();
		/**
		 * 类型为1,支持
		 */
		DataSet dataSet = t_qianduoduoarea.open(conn, "id,code,name", "parentid=0", "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
	/**
	 * 查询城市列表.
	 * 
	 * @Title: queryCityList
	 * @param
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryCityList(Connection conn,String parentid)
			throws SQLException, DataException {
		Dao.Tables.t_qianduoduoarea t_qianduoduoarea = new Dao().new Tables().new t_qianduoduoarea();
		/**
		 * 类型为1,支持
		 */
		DataSet dataSet = t_qianduoduoarea.open(conn, "id,code,name", "parentid="+parentid, "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
}
