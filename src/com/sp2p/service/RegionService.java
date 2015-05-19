package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.ConnectionManager;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.sp2p.dao.RegionDao;

public class RegionService extends BaseService {
	public static Log log = LogFactory.getLog(RegionService.class);
	private RegionDao regionDao;

	/**
	 * 查询地区
	 * 
	 * @param regionId
	 * @param parentId
	 * @param regionType
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryRegionList(Long regionId,
			Long parentId, Integer regionType) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> listMap = null;
		try {
			listMap = regionDao.queryRegionList(conn, regionId, parentId,
					regionType);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return listMap;
	}

	public void setRegionDao(RegionDao regionDao) {
		this.regionDao = regionDao;
	}
}
