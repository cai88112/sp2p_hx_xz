package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.sp2p.database.Dao;

/**
 * 本金保障处理
 * @author li.hou
 *
 */
public class CapitalEnsureDao {

	public List<Map<String,Object>> queryConfigList(Connection conn,int limitStart,int limitCount)
		throws SQLException, DataException {
			Dao.Tables.bt_config t_config = new Dao().new Tables().new bt_config();
			/**
			 * 类型为1
			 */
			DataSet dataSet = t_config.open(conn, "*", " type=1", "", limitStart, limitCount);
			dataSet.tables.get(0).rows.genRowsMap();
			return dataSet.tables.get(0).rows.rowsMap;
	}
}
