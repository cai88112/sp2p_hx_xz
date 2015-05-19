package com.sp2p.service.admin;

import java.sql.Connection;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp2p.dao.admin.AwardDetailDao;
import com.sp2p.service.AwardService;
import com.shove.base.BaseService;
import com.shove.data.dao.MySQL;

public class AwardDetailService extends BaseService {

	public static Log log = LogFactory.getLog(AwardService.class);

	private AwardDetailDao awardDetailDao;

	public Long addAwardDetail(long userId, double handleSum, long checkId,
			Date checkTime, String remark) throws Exception {
		Connection conn = MySQL.getConnection();
		long result;
		try {
			// 添加团队长、经纪人提成结算
			result = awardDetailDao.addAwardDetail(conn, userId, handleSum,
					checkId, checkTime, remark);
			if (result <= 0) {
				conn.rollback();
				return result;
			}
			conn.commit();
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

	public AwardDetailDao getAwardDetailDao() {
		return awardDetailDao;
	}

	public void setAwardDetailDao(AwardDetailDao awardDetailDao) {
		this.awardDetailDao = awardDetailDao;
	}

}