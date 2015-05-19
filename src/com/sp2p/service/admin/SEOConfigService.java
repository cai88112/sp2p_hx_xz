package com.sp2p.service.admin;



import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.dao.MySQL;
import com.sp2p.dao.admin.SEOConfigDao;


public class SEOConfigService extends BaseService {
	public static Log log = LogFactory.getLog(SendSMSService.class);

	private SEOConfigDao SEOConfigDao;

	public SEOConfigDao getSEOConfigDao() {
		return SEOConfigDao;
	}

	public void setSEOConfigDao(SEOConfigDao configDao) {
		SEOConfigDao = configDao;
	}

	/**
	 * 更新SEO标准设置
	 * 
	 * @param title
	 * @param description
	 * @param keywords
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public long updateSEOConfig(String title, String description,
			String keywords, int siteMap, String otherTags) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = 0L;
		try {
			result = SEOConfigDao.updateSEOConfig(conn, siteMap, otherTags,
					title, description, keywords);
			if (result <= 0) {
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}
	public long updateOptions(String serial_key,String serial_number) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = 0L;
		try {
			result = SEOConfigDao.updateOptions(conn,serial_key,serial_number);
			if (result <= 0) {
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		
		return result;
	}


	/**
	 * 查询SEO标准设置
	 * 
	 * @return Map<String,String>
	 * @throws Exception
	 */
	public Map<String, String> querySEOConfig() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = SEOConfigDao.querySEOConfig(conn);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}
	
	
	public List<Map<String, Object>> queryRegistCode() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = SEOConfigDao.queryRegistCode(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}
	
	
	public long updateRegistCode(String serial_key,String serial_number) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = 0L;
		try {
			result = SEOConfigDao.updateRegistCode(conn,serial_key,serial_number);
			if (result <= 0) {
				conn.rollback();
			} else {
				conn.commit();
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		
		return result;
	}
	
	public Map<String, String> queryRegistKeyConfig() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = SEOConfigDao.queryRegistKeyConfig(conn);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}
	

}
