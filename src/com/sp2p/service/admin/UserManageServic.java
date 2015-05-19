package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp2p.dao.UserIntegralDao;
import com.sp2p.dao.admin.UserManageDao;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;

/**
 * 后台用户管理
 * 
 * @author lw
 * 
 */
public class UserManageServic extends BaseService {
	public static Log log = LogFactory.getLog(UserManageServic.class);
	private UserManageDao userManageDao;
	private UserIntegralDao userIntegralDao;

	public UserIntegralDao getUserIntegralDao() {
		return userIntegralDao;
	}

	public void setUserIntegralDao(UserIntegralDao userIntegralDao) {
		this.userIntegralDao = userIntegralDao;
	}

	private List<Map<String, Object>> paymentMode;
//	private List<Map<String, Object>> deadline;

	public void setPaymentMode(List<Map<String, Object>> paymentMode) {
		this.paymentMode = paymentMode;
	}

//	public void setDeadline(List<Map<String, Object>> deadline) {
//		this.deadline = deadline;
//	}

	public void setUserManageDao(UserManageDao userManageDao) {
		this.userManageDao = userManageDao;
	}

	/**
	 * 用户基本信息管理
	 * 
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryUserManageBaseInfo(PageBean<Map<String, Object>> pageBean,
			String userName, String realName) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		try {
			if (StringUtils.isNotBlank(userName)) {
				condition.append(" and username  like '%"
						+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
			}
			if (StringUtils.isNotBlank(realName)) {
				condition.append(" and realName like '%"
						+ StringEscapeUtils.escapeSql(realName.trim()) + "%' ");
			}

			dataPage(conn, pageBean, "v_t_usermanage_baseinfo", "*",
					" order by id ", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 用户基本信息管理
	 * 
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryUserManageBaseInfo(PageBean<Map<String, Object>> pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, "v_t_usermanage_baseinfo", "*", "", "");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	// 用户基本信息列表查看
	public void queryUserManageInfo(PageBean<Map<String, Object>> pageBean,
			String userName, String realName) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		try {
			if (StringUtils.isNotBlank(userName)) {
				condition.append(" and username  like '%"
						+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
			}
			if (StringUtils.isNotBlank(realName)) {
				condition.append(" and realName like '%"
						+ StringEscapeUtils.escapeSql(realName.trim()) + "%' ");
			}

			dataPage(conn, pageBean, "v_t_usermanage_info", "*",
					" order by id ", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 用户积分管理
	 * 
	 * @param pageBean
	 * @param username
	 * @param viprecode
	 * @param creditcode
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryUserManageintegralinfo(
			PageBean<Map<String, Object>> pageBean, String username,
			int viprecode, int creditcode) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(username)) {
			condition.append(" and  username  like '%"
					+ StringEscapeUtils.escapeSql(username.trim()) + "%' ");
		}
		StringBuffer ordercondition = new StringBuffer();
		if (viprecode != -1 && viprecode == 1) {
			ordercondition.append(" ORDER BY   rating ");
		}
		if (viprecode != -1 && viprecode == 2) {
			ordercondition.append(" ORDER BY  rating  DESC");
		}
		if (creditcode != -1 && creditcode == 1 && viprecode == -1) {
			ordercondition.append(" ORDER BY   creditrating ");
		}
		if (creditcode != -1 && creditcode == 2 && viprecode == -1) {
			ordercondition.append("  ORDER BY   creditrating  DESC");// 大到小
		}
		if (creditcode != -1 && creditcode == 1 && viprecode != -1) {
			ordercondition.append("  , creditrating ");
		}
		if (creditcode != -1 && creditcode == 2 && viprecode != -1) {// 大到小
			ordercondition.append("  , creditrating DESC ");
		}

		try {
			dataPage(conn, pageBean, "v_t_usermanage_integralinfo", "*",
					ordercondition.toString(), condition.toString());
			// dataPage(conn, pageBean, "v_t_usermanage_integralinfo", "*", "",
			// "");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * vip记录表
	 * 
	 * @param pageBean
	 * @param username
	 * @param apptime
	 * @param lasttime
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryUservipRecoderinfo(PageBean<Map<String, Object>> pageBean,
			String username, String apptime, String lasttime,String appendtime,String lastendtime) throws Exception {
		Connection conn = MySQL.getConnection();
		;
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(username)) {
			condition.append(" and  username  like '%"
					+ StringEscapeUtils.escapeSql(username.trim()) + "%' ");
		}
		if (StringUtils.isNotBlank(apptime)) {
			condition.append(" and vipCreateTime >= '"
					+ StringEscapeUtils.escapeSql(apptime.trim()) + "'");
		}
		if (StringUtils.isNotBlank(appendtime)) {
			condition.append(" and vipCreateTime <= '"
					+ StringEscapeUtils.escapeSql(appendtime.trim()) + "'");
		}
		if (StringUtils.isNotBlank(lasttime)) {
			condition.append(" and vip >= '"
					+ StringEscapeUtils.escapeSql(lasttime.trim()) + "'");

		}
		if (StringUtils.isNotBlank(lastendtime)) {
			condition.append(" and vip <= '"
					+ StringEscapeUtils.escapeSql(lastendtime.trim()) + "'");
		}
		try {
			dataPage(conn, pageBean, "v_t_usermanage_viprecordinfo", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 用户基本信息里面的查看用户的基本信息
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public Map<String, String> queryUserManageInnerMsg(Long userId)
			throws Exception {
		Map<String, String> map = null;

		Connection conn = MySQL.getConnection();
		;
		try {
			map = userManageDao.queryUserManageInnerMsg(conn, userId);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 弹出框显示信息初始化
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */

	public Map<String, String> queryUserManageaddInteral(Long userId)
			throws Exception {
		Map<String, String> map = null;

		Connection conn = MySQL.getConnection();
		;
		try {
			map = userManageDao.queryUserManageaddInteral(conn, userId);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询用户信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryUserInfo(long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		;
		try {
			return userManageDao.queryUserInfo(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	public long updateUserqq(long id, String qq) throws Exception {
		long result = -1L;
		Connection conn = MySQL.getConnection();
		try {
			result = userManageDao.updateUserqq(conn, id, qq);
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
	 * 跳转到会员分明细info
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public void userintegralcreditinfo(PageBean<Map<String, Object>> pageBean,
			Long userid, Integer type) throws Exception {
		Connection conn = MySQL.getConnection();
		;
		StringBuffer condition = new StringBuffer();
		try {
			if (userid != -1L) {
				condition.append(" AND id = " + userid + " AND type = " + type);
				dataPage(conn, pageBean, "v_t_usermanage_integralinner", "*",
						"", condition.toString());
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询用投资管理
	 * 
	 * @param pageBean
	 * @param userid
	 * @throws SQLException
	 */
	public void queryUserManageInvest(PageBean<Map<String, Object>> pageBean,
			Long userid, String createtimeStart, String createtimeEnd)
			throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		try {
			if (userid != -1L) {
				condition.append(" AND investor = " + userid);
			}
			if (StringUtils.isNotBlank(createtimeStart)) {
				condition.append(" and investTime >'"
						+ StringEscapeUtils.escapeSql(createtimeStart.trim())
						+ "'");
			}
			if (StringUtils.isNotBlank(createtimeEnd)) {
				condition.append(" and investTime <'"
						+ StringEscapeUtils.escapeSql(createtimeEnd.trim())
						+ "'");
			}
			dataPage(conn, pageBean, "v_t_usermanage_invest", "*", "",
					condition.toString());

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

	}

	/**
	 * 查询用户积分详情
	 * 
	 * @param userId
	 * @param score
	 * @param type
	 * @param typeStr
	 * @param remark
	 * @param time
	 * @param changetype
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addIntervalDelt(Long userId, Integer score, Integer type,
			String typeStr, String remark, String changetype)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		Long result1 = -1L;
		try {
			// 向t_user表增加积分
			result = userManageDao.addUserManageaddInteral(conn, userId, score,
					type);
			if (result < 0) {
				conn.rollback();
				return -1L;
			}

			if (type == 1) { // 向积分明细添加信用积分明细
				result1 = userManageDao.addserintegraldetail(conn, null, userId,
						Double.parseDouble(score+""), typeStr, type, remark, changetype);
			}// 向积分明细添加会员积分明细
			else {
				Map<String, String> map = userIntegralDao.queryUserIntegral2(
						conn, userId, 2, typeStr);
				if (map == null) {
					result1 = userManageDao.addserintegraldetail(conn, null, userId,
							Double.parseDouble(score+""), typeStr, type, remark, changetype);
				} else {

					long changerecore = Convert.strToInt((String) map
							.get("changerecore"), 1);
					long minId = Convert.strToInt(map.get("minId"), 1);
					result1 = userIntegralDao.updateUserIntegral(conn,
							changerecore, Double.parseDouble(score+""), minId);
				}
			}
			if (result1 < 0) {
				conn.rollback();
				return -1L;
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		}finally{
			conn.close();
		}
		return result;
	}

	/**
	 * 查询用户资金信息
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryUserCashInfo(Long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		;
		try {
			return userManageDao.queryUserCashInfo(conn, userId);
		}catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			
			throw e;
		}finally{
			conn.close();
		}
	}

	public void changeFigure(PageBean<Map<String, Object>> pageBean) {
		List<Map<String, Object>> ll = pageBean.getPage();
		if (ll != null && ll.size() > 0) {// result rechargeType 中文显示
			for (Map<String, Object> mp : ll) {
				if (mp.get("paymentMode") != null) {
					String typeId = mp.get("paymentMode").toString();
					for (Map<String, Object> cc : this.getpaymentMode()) {
						if (cc.get("typeId").toString().equals(typeId)) {
							mp.put("paymentMode", cc.get("typeValue"));
							break;
						}
					}
				}
				if (mp.get("deadline") != null && mp.get("isDayThe") != null) {
					if (mp.get("isDayThe").equals(1)) {
						mp.put("deadline", mp.get("deadline") + "个月");
					} else
						mp.put("deadline", mp.get("deadline") + "天");

				}
			}
		}
	}

	public List<Map<String, Object>> getpaymentMode() {
		if (paymentMode == null) {
			paymentMode = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			mp = new HashMap<String, Object>();
			mp.put("typeId", 1);
			mp.put("typeValue", " 按月等额本息还款");
			paymentMode.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 2);
			mp.put("typeValue", "按先息后本还款");
			paymentMode.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 3);
			mp.put("typeValue", "秒还");
			paymentMode.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 4);
			mp.put("typeValue", "一次性还款");
			paymentMode.add(mp);

		}
		return paymentMode;
	}

}
