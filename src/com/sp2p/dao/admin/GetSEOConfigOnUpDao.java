package com.sp2p.dao.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;

public class GetSEOConfigOnUpDao {
	/**
	 * 获取SEO配置信息
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String,String>  getSEOConfig(Connection conn) 
			throws SQLException, DataException{
		Dao.Tables.t_seoconfig seo = new Dao().new Tables().new t_seoconfig();
		DataSet dataSet = seo.open(conn, "*", "",
				"", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);	
	}
	
	public List<Map<String,Object>>  queryRegistCode(Connection conn) 
	throws SQLException, DataException{
		String command = "SELECT * FROM t_options WHERE id in(3,4)";
		DataSet dataSet =MySQL.executeQuery(conn, command);
		command = null;
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
}
}
