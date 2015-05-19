/**   
 * @Title: BankDao.java 
 * @Package com.sp2p.dao 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月15日 上午10:38:09 
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
 * 乾多多支付支持银行.
 * 
 * @ClassName: BankDao
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月15日 上午10:38:09
 * 
 */
public class BankDao {

	/**
	 * 查询支持的银行列表.
	 * 
	 * @Title: queryBankList
	 * @param
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryBankList(Connection conn)
			throws SQLException, DataException {
		Dao.Tables.t_bank t_bank = new Dao().new Tables().new t_bank();
		/**
		 * 类型为1,支持
		 */
		DataSet dataSet = t_bank.open(conn, "bankcode,bankname", "status=1", "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}
}
