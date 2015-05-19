package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.UserDao;
import com.sp2p.dao.UserIntegralDao;
import com.sp2p.dao.admin.UserManageDao;
/**
 * 用户积分Service.
 * @author 殷梓淞.
 *
 */
public class UserIntegralService extends BaseService {
	public static Log log = LogFactory.getLog(UserIntegralService.class);
	private UserIntegralDao userIntegralDao;
	private UserDao userDao;
	private OperationLogDao operationLogDao;
	private UserManageDao userManageDao;

	public UserManageDao getUserManageDao() {
		return userManageDao;
	}

	public void setUserManageDao(UserManageDao userManageDao) {
		this.userManageDao = userManageDao;
	}

	public void setUserIntegralDao(UserIntegralDao userIntegralDao) {
		this.userIntegralDao = userIntegralDao;
	}

	/**
	 * 查询用户积分.
	 * @param pageBean
	 * @param userid
	 * @param type
	 * @throws Exception
	 */
	public void queryUserIntegral(PageBean<Map<String, Object>> pageBean,
			Long userid, int type) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			StringBuffer condition = new StringBuffer();
			// condition.append(" 1 = 1");
			condition.append(" AND userid = " + userid + " AND type = " + type);
			dataPage(conn, pageBean, "t_userintegraldetail", "*",
					" ORDER BY id", condition.toString());

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 添加用户积分明细记录
	 * 
	 * @param userid
	 * @param intergraltype
	 * @param remark
	 * @param changetype
	 * @param changerecore
	 * @param time
	 * @param type
	 * @return
	 * @throws Exception
	 */
	/**
	 * 登录添加积分处理
	 * 
	 * @return
	 * @throws Exception
	 */

	/**
	 * 用户登录日志表
	 */
	public Long addUserLoginLog(Long id) throws Exception {
		Long resultId = -1L;
		Connection conn = MySQL.getConnection();
		try {
			resultId = userIntegralDao.addUserLoginLog(conn, id);
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
	
	
	/**
	 * 实名认证积分
	 * @param id
	 * @param score
	 * @param prescore
	 * @return
	 * @throws Exception
	 */
	public Long updateAuthentication(Long id, int score, int prescore)
			throws Exception {
			Long resultId = -1L;
			Connection conn = MySQL.getConnection();
			String intergraltype = "实名认证";
			String remark = "实名认证成功";
			String changetype = "增加";
			Integer type = IConstants.USER_INTERGRALTYPEVIP;
			try {
			
				resultId = userIntegralDao.UpdateUserRating(conn, id, score,
						prescore);
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
				}
				Map<String, String> map = userIntegralDao.queryUserIntegral2(conn,
						id, 2, intergraltype);
				if (map == null) {
					resultId = userManageDao.addserintegraldetail(conn, null, id, Double.parseDouble(score+""),
							intergraltype, type, remark, changetype);
				} else {
			
					long changerecore = Convert.strToInt((String) map
							.get("changerecore"), 1);
					long minId = Convert.strToInt(map.get("minId"), 1);
					resultId = userIntegralDao.updateUserIntegral(conn,
							changerecore, Double.parseDouble(score+""), minId);
				}
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
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
			return resultId;
		}
	
	/**
	 * 手机认证积分
	 * @param id
	 * @param score
	 * @param prescore
	 * @return
	 * @throws Exception
	 */
	public Long updatePhoneVerification(Long id, int score, int prescore)
			throws Exception {
			Long resultId = -1L;
			Connection conn = MySQL.getConnection();
			String intergraltype = "手机认证";
			String remark = "手机认证成功";
			String changetype = "增加";
			Integer type = IConstants.USER_INTERGRALTYPEVIP;
			try {
			
				resultId = userIntegralDao.UpdateUserRating(conn, id, score,
						prescore);
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
				}
				Map<String, String> map = userIntegralDao.queryUserIntegral2(conn,
						id, 2, intergraltype);
				if (map == null) {
					resultId = userManageDao.addserintegraldetail(conn, null, id, Double.parseDouble(score+""),
							intergraltype, type, remark, changetype);
				} else {
			
					long changerecore = Convert.strToInt((String) map
							.get("changerecore"), 1);
					long minId = Convert.strToInt(map.get("minId"), 1);
					resultId = userIntegralDao.updateUserIntegral(conn,
							changerecore, Double.parseDouble(score+""), minId);
				}
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
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
			return resultId;
		}
	
	/**
	 * 第三方托管账号注册积分
	 * @param id
	 * @param score
	 * @param prescore
	 * @return
	 * @throws Exception
	 */
	public Long updateEscrowRegister(Long id, int score, int prescore)
			throws Exception {
			Long resultId = -1L;
			Connection conn = MySQL.getConnection();
			String intergraltype = "第三方托管账号注册";
			String remark = "第三方托管账号注册成功";
			String changetype = "增加";
			Integer type = IConstants.USER_INTERGRALTYPEVIP;
			try {
			
				resultId = userIntegralDao.UpdateUserRating(conn, id, score,
						prescore);
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
				}
				Map<String, String> map = userIntegralDao.queryUserIntegral2(conn,
						id, 2, intergraltype);
				if (map == null) {
					resultId = userManageDao.addserintegraldetail(conn, null, id, Double.parseDouble(score+""),
							intergraltype, type, remark, changetype);
				} else {
			
					long changerecore = Convert.strToInt((String) map
							.get("changerecore"), 1);
					long minId = Convert.strToInt(map.get("minId"), 1);
					resultId = userIntegralDao.updateUserIntegral(conn,
							changerecore, Double.parseDouble(score+""), minId);
				}
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
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
			return resultId;
		}
	
	
	// 用户投标积分
	public Long UpdateFinnceRating(Long id, int score, int prescore)
		throws Exception {
		Long resultId = -1L;
		Connection conn = MySQL.getConnection();
		String intergraltype = "投标";
		String remark = "投标成功";
		String changetype = "增加";
		Integer type = IConstants.USER_INTERGRALTYPEVIP;
		try {
		
			resultId = userIntegralDao.UpdateUserRating(conn, id, score,
					prescore);
			if (resultId <= 0) {
				conn.rollback();
				return -1L;
			}
			Map<String, String> map = userIntegralDao.queryUserIntegral2(conn,
					id, 2, intergraltype);
			if (map == null) {
				resultId = userManageDao.addserintegraldetail(conn, null, id, Double.parseDouble(score+""),
						intergraltype, type, remark, changetype);
			} else {
		
				long changerecore = Convert.strToInt((String) map
						.get("changerecore"), 1);
				long minId = Convert.strToInt(map.get("minId"), 1);
				resultId = userIntegralDao.updateUserIntegral(conn,
						changerecore, Double.parseDouble(score+""), minId);
			}
			if (resultId <= 0) {
				conn.rollback();
				return -1L;
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
		return resultId;
	}

	/**
	 * 举报审核通过后积分添加
	 * 
	 * @param id
	 * @param score
	 * @param prescore
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long UpdateJubaoRating(Long id, int score, int prescore)
		throws Exception {
		Long resultId = -1L;
		Connection conn = MySQL.getConnection();
		String intergraltype = "举报";
		String remark = "举报属实";
		String changetype = "增加";
		Integer type = IConstants.USER_INTERGRALTYPEVIP;
		try {
		
			resultId = userIntegralDao.UpdateUserRating(conn, id, score,
					prescore);
			if (resultId <= 0) {
				conn.rollback();
				return -1L;
			}
			Map<String, String> map = userIntegralDao.queryUserIntegral2(conn,
					id, 2, intergraltype);
			if (map == null) {
				resultId = userManageDao.addserintegraldetail(conn, null, id, Double.parseDouble(score+""),
						intergraltype, type, remark, changetype);
			} else {
		
				long changerecore = Convert.strToInt((String) map
						.get("changerecore"), 1);
				long minId = Convert.strToInt(map.get("minId"), 1);
				resultId = userIntegralDao.updateUserIntegral(conn,
						changerecore, Double.parseDouble(score+""), minId);
			}
		
			if (resultId <= 0) {
				conn.rollback();
				return -1L;
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
		
		return resultId;
	}

	/**
	 * 更新登录积分.
	 * @param id
	 * @param score
	 * @param prescore
	 * @param loignCount
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long UpdateLoginRating(Long id, int score, int prescore,
			Integer loignCount) throws Exception, DataException {
		Long resultId = -1L;
		Connection conn = MySQL.getConnection();
		String intergraltype = "登录";
		String remark = "登录积分";
		String changetype = "增加";
		Integer type = IConstants.USER_INTERGRALTYPEVIP;
		try {
			if (loignCount <= 2) {
				resultId = userIntegralDao.UpdateUserRating(conn, id, score,
						prescore);
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
				}
				Map<String, String> map = userIntegralDao.queryUserIntegral2(
						conn, id, 2, intergraltype);
				if (map == null) {
					resultId = userManageDao.addserintegraldetail(conn, null, id,
							Double.parseDouble(score+""), intergraltype, type, remark, changetype);
				} else {

					long changerecore = Convert.strToInt((String) map
							.get("changerecore"), 1);
					long minId = Convert.strToInt(map.get("minId"), 1);
					resultId = userIntegralDao.updateUserIntegral(conn,
							changerecore, Double.parseDouble(score+""), minId);
				}
				if (resultId <= 0) {
					conn.rollback();
					return -1L;
				}
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
		return resultId;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryUserLoginLong(Long id) throws Exception {
		Map<String, String> map = null;
		Map<String, String> userMap = new HashMap<String, String>();
		Connection conn = MySQL.getConnection();
		try {
			map = userIntegralDao.queryUserLoginLong(conn, id);
			userMap = userDao.queryUserById(conn, id);
			operationLogDao.addOperationLog(conn, "t_user", Convert.strToStr(
					userMap.get("username"), ""), IConstants.UPDATE, Convert
					.strToStr(userMap.get("lastIP"), ""), 0, "用户登陆", 1);
			
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
		} finally {
			conn.close();
		}
		return map;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public UserIntegralDao getUserIntegralDao() {
		return userIntegralDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}
}
