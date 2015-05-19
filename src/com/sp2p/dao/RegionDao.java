package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.sp2p.database.Dao;

public class RegionDao {
	/**
	 * 查询地区列表
	 * @param conn
	 * @param regionId
	 * @param parentId
	 * @param regionType
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryRegionList(Connection conn, Long regionId, Long parentId, Integer regionType) throws SQLException, DataException{
		Dao.Tables.t_region region = new Dao(). new Tables(). new t_region();
		StringBuffer condition = new StringBuffer();
		condition.append(" 1=1 ");
		if (regionId != null && regionId > 0) {
			condition.append(" AND regionId=" + regionId);
		}
		if (parentId != null && parentId > 0) {
			condition.append(" AND parentId=" + parentId);
		}
		if (regionType != null && regionType > 0) {
			condition.append(" AND regionType=" + regionType);
		}
		DataSet dataSet = region.open(conn, "*", condition.toString(), "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		condition = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}
}
