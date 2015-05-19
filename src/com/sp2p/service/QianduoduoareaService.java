/**   
 * @Title: QianduoduoareaService.java 
 * @Package com.sp2p.service 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月15日 下午1:52:35 
 * @version V1.0   
 */
package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.data.dao.MySQL;
import com.sp2p.dao.QianduoduoareaDao;

/**
 * 乾多多支付银行地区Server层.
 * 
 * @ClassName: QianduoduoareaService
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月15日 下午1:52:35
 * 
 */
public class QianduoduoareaService {
	public static Log log = LogFactory.getLog(QianduoduoareaService.class);
	private QianduoduoareaDao qianduoduoareaDao;

	/**
	 * @throws SQLException
	 *             查询省列表.
	 * 
	 * @Title: queryProvinceList
	 * @param
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 */
	public List<Map<String, Object>> queryProvinceList() throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection conn = MySQL.getConnection();
		try {
			list = qianduoduoareaDao.queryProvinceList(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * 查询城市列表.
	 * 
	 * @Title: queryCityList
	 * @param
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 */
	public List<Map<String, Object>> queryCityList(String parentid) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection conn = MySQL.getConnection();
		try {
			list = qianduoduoareaDao.queryCityList(conn,parentid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return list;

	}

	public final QianduoduoareaDao getQianduoduoareaDao() {
		return qianduoduoareaDao;
	}

	public final void setQianduoduoareaDao(QianduoduoareaDao qianduoduoareaDao) {
		this.qianduoduoareaDao = qianduoduoareaDao;
	}

}
