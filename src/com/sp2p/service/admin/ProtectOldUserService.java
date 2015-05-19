package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.LoanInfoBean;
import com.shove.vo.PageBean;
import com.shove.web.action.BasePageAction;
import com.sp2p.dao.admin.ProtectOldUserDao;

/**
 * ClassName: ProtectOldUserService <br/>
 * date: 2015年3月13日 上午10:13:03 <br/>
 *
 * @author 殷梓淞
 * @version v1.0.0
 * @since JDK 1.7
 */
public class ProtectOldUserService extends  BaseService  {
	public static Log log = LogFactory.getLog(ProtectOldUserService.class);
	private ProtectOldUserDao protectOldUserDao;
	/**
	 * 根据用户id查询用户是老米护盾信息.
	 * @author 殷梓淞
	 * @param userId
	 * @throws Exception
	 */
	public Map<String, String> getProtectUserDetailByUserId(long userId)
			throws Exception {
		Map<String, String> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = protectOldUserDao.getProtectUserDetailByUserId(conn, userId);
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
	
	/**
	 * 根据月份查询利率.
	 * @author 殷梓淞
	 * @param userId
	 * @throws Exception
	 */
	public Map<String, String> getProtectRateByMonth(int month)
			throws Exception {
		Map<String, String> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = protectOldUserDao.getProtectRateByMonth(conn, month);
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
	
	/**
	 * 根据批次查询护盾标准.
	 * @author 殷梓淞
	 * @param userId
	 * @throws Exception
	 */
	public Map<String, String> getProtectStandardByBatch(int batch)
			throws Exception {
		Map<String, String> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = protectOldUserDao.getProtectStandardByBatch(conn, batch);
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

	/**
	 * 获取投资人当月待收
	 * @author 殷梓淞
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getDueinSumOfMonth(long userId) throws Exception{
		Map<String, String> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = protectOldUserDao.getDueinSumOfMonth(conn, userId);
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
	
	/**
	 * 删除老米护盾
	 * 
	 * @author 金庸
	 * @param userId 用户ID
	 * @return
	 * @throws Exception
	 */
	public long deleteOldUser(long userId) throws Exception {
		Connection conn = MySQL.getConnection();

		try {
			protectOldUserDao.deleteOldUser(conn, userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return 1;
	}
	
	
	/**
	 * 分页查询.
	 * 
	 * @param conn
	 * @param pageBean
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws DataException
	 */
	public void queryProtectOldUserPage(
			PageBean<Map<String, Object>> pageBean, String userName)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String command = "";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'";
		}
		StringBuffer cmd = new StringBuffer();
		cmd.append("(SELECT v.batch,v.userId,u.username,p.realName,s.duestandard,v.`status` FROM t_user u ");
		cmd.append("  LEFT JOIN t_person p ON u.id=p.userId INNER JOIN t_protect_vip v ON u.id=v.userId ");
		cmd.append("  INNER JOIN t_protect_standard s ON v.batch= s.batch ORDER BY s.batch ) u");
		try {
			dataPage(conn, pageBean, cmd.toString(), "*", "", command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}
	
	/**
	 * 查找"非老米用户列表"分页列表信息.
	 * 
	 * @param conn
	 * @param pageBean
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws DataException
	 */
	public void queryNotProtectVipPage(
			PageBean<Map<String, Object>> pageBean, String userName)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String command = "";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'";
		}
		StringBuffer cmd = new StringBuffer();
		cmd.append("(SELECT u.id,u.username,p.realName,u.dueinSum,u.creditrating,u.rating  ");
		cmd.append("  FROM t_user u LEFT JOIN t_person p ON u.id=p.userId ");
		cmd.append("  WHERE u.id NOT IN(SELECT v.userId FROM t_protect_vip v) ) u");
		try {
			dataPage(conn, pageBean, cmd.toString(), "*", "", command);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}
		
	/**
	 * 弹出框显示信息初始化
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */

	public Map<String, String> queryProtectVipAddInteral(Long userId)
			throws Exception {
		Map<String, String> map = null;

		Connection conn = MySQL.getConnection();
		;
		try {
			map = protectOldUserDao.queryProtectVipAddInteral(conn, userId);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public List<Map<String, String>> getBatchList()throws Exception {
		List<Map<String, String>> mapList = null;
		Connection conn = MySQL.getConnection();
		try {
			mapList = protectOldUserDao.getBatchList(conn);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return mapList;
	}
	
	/**
	 * * 向t_protect_vip表插入数据
	 * 
	 * @param conn
	 * @param userId
	 * @param score
	 * @param type
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addProtectVip(Long userId,
			Integer batch) throws  Exception {
		Long res = -1L;
		Connection conn = MySQL.getConnection();
		try {
			res = protectOldUserDao.addProtectVip(conn, userId, batch);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return res;
	}
	
	public final ProtectOldUserDao getProtectOldUserDao() {
		return protectOldUserDao;
	}

	public final void setProtectOldUserDao(ProtectOldUserDao protectOldUserDao) {
		this.protectOldUserDao = protectOldUserDao;
	}
}

