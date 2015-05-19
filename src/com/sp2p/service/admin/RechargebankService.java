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
import com.shove.vo.PageBean;
import com.sp2p.dao.admin.RechargebankDao;

/**
 * 线下充值银行编辑
 * 
 * @author Administrator
 * 
 */
public class RechargebankService extends BaseService {
	private RechargebankDao rechargebankDao;
	public static Log log = LogFactory.getLog(RechargebankService.class);

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryrechargeBankById(Long id)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = MySQL.getConnection();
		try {
			map = rechargebankDao.queryrechargeBankById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public Map<String, String> queryFundRecordTypeAmount(long userId,
			String startTime, String endTime, Map<String, String> typeMap)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = MySQL.getConnection();
		try {
			map = rechargebankDao.queryFundRecordTypeAmount(conn, userId,
					startTime, endTime, typeMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public Map<String, String> queryrechargeBank() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = MySQL.getConnection();
		try {
			map = rechargebankDao.queryrechargeBank(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public long updaterechargeBankById(long id, String bankname,
			String Account, String accountbank, String bankimage,
			String accountname) throws Exception {
		long result = -1;
		Connection conn = MySQL.getConnection();
		try {
			result = rechargebankDao.updaterechargeBankById(conn, id, bankname,
					Account, accountbank, bankimage, accountname);
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

	public long addRechargeBankInit(String bankname, String Account,
			String accountbank, String bankimage, String accountname)
			throws Exception {
		long result = -1;
		Connection conn = MySQL.getConnection();
		try {
			result = rechargebankDao.addRechargeBankInit(conn, bankname,
					Account, accountbank, bankimage, accountname);
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

	public List<Map<String, Object>> queryFundRecordType() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = rechargebankDao.queryFundRecordType(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	public List<Map<String, Object>> queryrechargeBanklist()
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = rechargebankDao.queryrechargeBanklist(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	public void queryRechargebanklist(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		StringBuffer condition = new StringBuffer();
		/*
		 * if (StringUtils.isNotBlank(username)) {
		 * condition.append(" AND uername  LIKE CONCAT('%','" +
		 * StringEscapeUtils.escapeSql(username.trim()) + "','%')"); } if
		 * (StringUtils.isNotBlank(starttime)) {
		 * condition.append(" and applyTime >= '" +
		 * StringEscapeUtils.escapeSql(starttime.trim()) + "'"); } if
		 * (StringUtils.isNotBlank(endTime)) {
		 * condition.append(" and applyTime <= '" +
		 * StringEscapeUtils.escapeSql(endTime.trim()) + "'"); } if (autiStatus
		 * != null && autiStatus != -1) { condition.append(" AND applystatus = "
		 * + autiStatus); }
		 */
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "t_rechargebank", "*", "", condition
					.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		} finally {
			conn.close();
		}

	}

	public void setRechargebankDao(RechargebankDao rechargebankDao) {
		this.rechargebankDao = rechargebankDao;
	}
}
