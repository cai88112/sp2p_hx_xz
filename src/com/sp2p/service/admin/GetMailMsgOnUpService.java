package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp2p.dao.admin.GetMailMsgOnUpDao;
import com.shove.base.BaseService;
import com.shove.data.dao.MySQL;

/**
 * 当启动时候读取发送emal的信息
 * 
 * @author Administrator
 * 
 */
public class GetMailMsgOnUpService extends BaseService {
	public static Log log = LogFactory.getLog(GetMailMsgOnUpService.class);
	private GetMailMsgOnUpDao getMailMsgOnUpDao;

	public void setGetMailMsgOnUpDao(GetMailMsgOnUpDao getMailMsgOnUpDao) {
		this.getMailMsgOnUpDao = getMailMsgOnUpDao;
	}

	/*
	 * public void invokeme(){
	 * log.info("a............................"); }
	 */
	/**
	 * 当项目加载时候读取数据库
	 */
	public Map<String, String> getMailSet() throws Exception {
		Map<String, String> map = null;
		// ServletContext application =
		// (ServletContext)ActionContext.getContext().getApplication();//获取application对象
		/*
		 * application.setAttribute("aaaa", "我是");
		 * log.info(application.getAttribute("aaaa"));
		 */
		Connection conn = MySQL.getConnection();
		try {
			map = getMailMsgOnUpDao.getMailSet(conn);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

}
