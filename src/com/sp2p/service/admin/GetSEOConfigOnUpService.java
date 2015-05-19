package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.dao.MySQL;
import com.sp2p.dao.admin.GetSEOConfigOnUpDao;

public class GetSEOConfigOnUpService extends BaseService{
	public static Log log = LogFactory.getLog(GetMailMsgOnUpService.class);
	
	private GetSEOConfigOnUpDao getSEOConfigOnUpDao;

	public GetSEOConfigOnUpDao getGetSEOConfigOnUpDao() {
		return getSEOConfigOnUpDao;
	}

	public void setGetSEOConfigOnUpDao(GetSEOConfigOnUpDao getSEOConfigOnUpDao) {
		this.getSEOConfigOnUpDao = getSEOConfigOnUpDao;
	}
	
	/**
	 * 当项目加载时候读取数据库
	 */
	public Map<String, String> getSEOConfig()
			throws Exception {
		Map<String, String> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = getSEOConfigOnUpDao.getSEOConfig(conn);
			conn.commit();
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}
	
	
	public List<Map<String, Object>> getRegistCode() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = getSEOConfigOnUpDao.queryRegistCode(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

}
