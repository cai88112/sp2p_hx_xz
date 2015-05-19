package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sp2p.dao.admin.ReferralBonusesDao;
import com.shove.base.BaseService;
import com.shove.data.ConnectionManager;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;

/**
 * 推荐好友奖励设置
 * 
 * @author Administrator
 * 
 */
public class ReferralBonusesService extends BaseService {
	public static Log log = LogFactory.getLog(ReferralBonusesService.class);

	private ReferralBonusesDao referralBonusesDao;

	private ConnectionManager connectionManager;

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public ReferralBonusesDao getReferralBonusesDao() {
		return referralBonusesDao;
	}

	public void setReferralBonusesDao(ReferralBonusesDao referralBonusesDao) {
		this.referralBonusesDao = referralBonusesDao;
	}

	/**
	 *更新信好友奖励
	 * 
	 * @param id
	 * @param sort
	 * @param userName
	 * @param imgPath
	 * @param intro
	 * @param publishTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateMReferralBonuses(Integer typeId, String general,
			String fieldVisit, String organization, String netWorth)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = referralBonusesDao.updateMReferralBonuses(conn, typeId,
					general, fieldVisit, organization, netWorth);
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

	/**
	 * 根据typeId获取好友奖励
	 * 
	 * @param conn
	 * @param typeId
	 *            类型
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getReferralBonusersByTypeId(Integer typeId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = referralBonusesDao.getReferralBonusersByTypeId(conn, typeId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public List<Map<String, Object>> queryReferralBonusersList()
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = null;
		try {
			list = referralBonusesDao.queryReferralBonusersList(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

}
