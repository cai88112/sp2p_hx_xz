package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.sp2p.dao.CapitalEnsureDao;
/**   
 * @ClassName: CallCenterService.java
 * @Author: li.hou
 * @Descrb: 客服中心帮助问题处理
 */
public class CapitalEnsureService extends BaseService {

	public static Log log = LogFactory.getLog(FinanceService.class);
	
	private CapitalEnsureDao capitalEnsureDao;

	/**
	 * 获得bt_config配置表中的静态信息
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String,Object>> queryConfigList() throws Exception{
		Connection conn = MySQL.getConnection();
		List<Map<String,Object>> arrayList = new ArrayList<Map<String,Object>>();
		try {
			arrayList = capitalEnsureDao.queryConfigList(conn, -1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally{
			conn.close();
		}
		
		return arrayList;
	}
	
	public CapitalEnsureDao getCapitalEnsureDao() {
		return capitalEnsureDao;
	}

	public void setCapitalEnsureDao(CapitalEnsureDao capitalEnsureDao) {
		this.capitalEnsureDao = capitalEnsureDao;
	}
}
