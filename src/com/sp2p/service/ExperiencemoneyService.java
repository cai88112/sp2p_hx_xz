/**
 * 
 */
package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.dao.ExperiencemoneyDao;
import com.sp2p.dao.InvestDao;

/**
 * 体验金servcie层.
* @ClassName: ExperiencemoneyService 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yinzisong 690713748@qq.com 
* @date 2014年12月31日 下午2:33:57 
*
 */
public class ExperiencemoneyService {
	private static Log log = LogFactory.getLog(ExperiencemoneyService.class);

	private ExperiencemoneyDao experiencemoneyDao;
	private UserService userService;
	private InvestDao investDao;

	public InvestDao getInvestDao() {
		return investDao;
	}

	public void setInvestDao(InvestDao investDao) {
		this.investDao = investDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ExperiencemoneyDao getExperiencemoneyDao() {
		return experiencemoneyDao;
	}

	public void setExperiencemoneyDao(ExperiencemoneyDao experiencemoneyDao) {
		this.experiencemoneyDao = experiencemoneyDao;
	}

	/**
	 * 添加体验金.
	 * 
	 * @param userId
	 *            用户id.
	 * @return
	 * @throws Exception
	 */
	public Long addExperiencemoney(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			experiencemoneyDao.addExperiencemoney(conn, userId, 1000F, 0F);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}

		return null;
	}

	/**
	 * 更新体验金.
	 * 
	 * @param userId
	 *            用户id.
	 * @param usableMoney
	 *            可用金额.
	 * @param freezeMoney
	 *            冻结金额.
	 * @param status
	 *            到期标识.
	 * @return
	 * @throws Exception
	 */
	public Long updateExperiencemoney(Long userId, Float usableMoney,
			Float freezeMoney,Float collectMoney, int status) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			experiencemoneyDao.updateExperiencemoney(conn, userId, usableMoney,
					usableMoney,collectMoney, status);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}

		return null;
	}

	/**
	 * 查询用户的体验金信息.
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryExperiencemoneyByUserID(long userId) throws Exception{
		Connection conn = MySQL.getConnection();
		Map<String, String> experiencemoneyMap = new HashMap<String, String>();
		try {
			experiencemoneyMap = experiencemoneyDao
					.queryExperiencemoney(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return experiencemoneyMap;
	}
	
	/**
	 * 分页查询
	 * 
	 * @param conn
	 * @param pageBean
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws DataException
	 */
	public void queryExperiencemoneyPageAll(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			experiencemoneyDao.queryExperiencemoneyPageAll(conn, pageBean);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}
	

}
