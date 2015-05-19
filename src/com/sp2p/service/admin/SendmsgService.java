package com.sp2p.service.admin;

import java.sql.Connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp2p.dao.admin.SendmsgDao;
import com.shove.base.BaseService;
import com.shove.data.dao.MySQL;

public class SendmsgService extends BaseService {
	public static Log log = LogFactory.getLog(SendmsgService.class);
	private SendmsgDao sendmsgDao;

	public void setSendmsgDao(SendmsgDao sendmsgDao) {
		this.sendmsgDao = sendmsgDao;
	}
   /**
    * 发送审核后的信息
    * @param reciver
    * @param mailTitle
    * @param content
    * @param mailType
    * @param sender
    * @return
    * @throws Exception
    */
	public Long sendCheckMail( long reciver, String mailTitle,
			String content, int mailType, long sender) throws Exception {
		Connection conn = MySQL.getConnection();
		Long resultId = -1L;
		try {
			resultId = sendmsgDao.sendCheckMail(conn,  reciver, mailTitle,
					content, mailType, sender);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
		return resultId;
	}

}
