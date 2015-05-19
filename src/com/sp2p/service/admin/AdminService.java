package com.sp2p.service.admin;

import java.sql.Connection;

import com.fp2p.helper.AmountHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.dao.MySQL;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataRow;
import com.shove.data.DataSet;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.admin.AdminDao;
import com.sp2p.dao.admin.RelationDao;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_admin;
import com.sp2p.entity.Admin;

public class AdminService extends BaseService {

	public static Log log = LogFactory.getLog(AdminService.class);

	private AdminDao adminDao;
	private RelationDao relationDao;

	/**
	 * 添加管理员
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param enable
	 *            状态
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addAdmin(String userName, String password, int enable,
			long roleId, String realName, String telphone, String qq,
			String email, String img, String isLeader) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = adminDao.queryAdminList(conn,
				userName, null);
		if (list != null && list.size() > 0) {
			return -2L;
		}
		Long adminId = -1L;
		try {
			adminId = addAdminInfo(conn, userName, password, enable, roleId,
					realName, telphone, qq, email, img, isLeader);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return adminId;
	}

	/**
	 * 修改管理员
	 * 
	 * @param adminId
	 *            管理员编号
	 * @param password
	 *            密码
	 * @param enable
	 *            状态
	 * @param lastIP
	 *            最后登录IP
	 * @return Long
	 * @throws SQLException
	 */
	public Long updateAdmin(long adminId, String password, Integer enable,
			String lastIP, Long roleId, String realName, String telphone,
			String qq, String email, String img, String isLeader)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long returnId = -1L;
		try {
			returnId = updateAdminInfo(conn, adminId, password, enable, lastIP,
					roleId, realName, telphone, qq, email, img, isLeader);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return returnId;
	}

	/**
	 * 删除管理员
	 * 
	 * @param adminIds
	 *            管理员编号拼接成的字符串
	 * @throws Exception
	 */
	public void deleteAdmin(String adminIds) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			adminDao.deleteAdmins(conn, adminIds);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 根据管理员编号查询管理员信息
	 * 
	 * @param id
	 *            管理员编号
	 * @return Map<String,String>
	 * @throws Exception
	 * @throws DataException
	 */
	public Map<String, String> queryAdminById(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = adminDao.queryAdminById(conn, id);
			conn.commit();
		} catch (Exception e) {
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
	 * 更加用户名获取id
	 * 
	 * @param userName
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public Map<String, String> queryIdByUser(String userName) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = adminDao.queryIdByUser(conn, userName);
			conn.commit();
		} catch (Exception e) {
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
	 * 根据用户名，密码进行查询（登录功能）
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @return Map<String,String>
	 * @throws Exception
	 * @throws DataException
	 */
	public Map<String, String> queryAdminByNamePass(String userName,
			String password) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = adminDao.queryAdminByNamePass(conn, userName, password);
			conn.commit();
		} catch (Exception e) {
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
	 * 根据条件查询管理员信息
	 * 
	 * @param userName
	 *            用户名
	 * @param enable
	 *            状态
	 * @return List<Map<String,Object>>
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryAdminList(String userName,
			Integer enable) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = adminDao.queryAdminList(conn, userName, enable);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * 根据条件分页查询管理员信息
	 * 
	 * @param userName
	 *            用户名
	 * @param enable
	 *            状态
	 * @param pageBean
	 * @throws Exception
	 * @throws DataException
	 */
	public void queryAdminPage(String userName, Integer enable, Long roleId,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			StringBuffer condition = new StringBuffer();
			if (StringUtils.isNotBlank(userName)) {
				condition.append(" and  userName  like '%"
						+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
			}
			if (enable != null && enable >= 0) {
				condition.append(" AND enable = " + enable);
			}
			if (roleId != null && roleId >= -1) {
				condition.append(" AND roleId = " + roleId);
			}
			dataPage(conn, pageBean, " v_t_admin", " * ", "", condition
					.toString());
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 管理员登陆
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 * @param IP
	 * @return Admin
	 * @throws Exception
	 * @throws DataException
	 */
	public Admin adminLogin(String userName, String password, String IP)
			throws Exception, DataException {
		Connection conn = MySQL.getConnection();
		Admin admin = null;
		try {
			password = StringEscapeUtils.escapeSql(password.trim());
			if ("1".equals(IConstants.ENABLED_PASS)) {
				password = com.shove.security.Encrypt.MD5(password.trim());
			} else {
				password = com.shove.security.Encrypt.MD5(password.trim()
						+ IConstants.PASS_KEY);
			}
			DataSet dss = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.p_admin_init(conn, dss, outParameterValues, userName, password, -1, "");
			
			Dao.Tables.t_admin t_admin = new Dao().new Tables().new t_admin();
			DataSet ds = t_admin.open(conn, "", " userName ='"
					+ StringEscapeUtils.escapeSql(userName.trim())
					+ "' and password = '" + password + "'", "", -1, -1);
			int returnId = ds.tables.get(0).rows.getCount();
			if (returnId <= 0) {
				conn.close();
				return null;
			} else {
				admin = new Admin();
				DataRow dr = ds.tables.get(0).rows.get(0);
				admin.setId((Long) dr.get("id"));
				admin.setUserName(dr.get("userName") + "");
				admin.setPassword(dr.get("password") + "");
				admin.setLastIP(dr.get("lastIP") + "");
				admin.setLastTime((Date) dr.get("lastTime"));
				admin.setEnable(Integer.parseInt(dr.get("enable") + ""));
				admin.setRoleId(Long.parseLong(dr.get("roleId") + ""));
			}
			if (StringUtils.isNotBlank(IP)) {
				t_admin.lastTime.setValue(new Date());
				t_admin.lastIP.setValue(IP);
				t_admin.update(conn, " id=" + admin.getId());
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
		return admin;
	}

	/**
	 * 获得管理员列表
	 * 
	 * @param enable
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryAdministors(Integer enable)
			throws Exception, DataException {
		Connection conn = MySQL.getConnection();

		String command = "SELECT DISTINCT userName,id from t_admin";

		try {
			DataSet dataSet = MySQL.executeQuery(conn, command);
			dataSet.tables.get(0).rows.genRowsMap();
			conn.commit();
			return dataSet.tables.get(0).rows.rowsMap;
		} finally {
			conn.close();
		}

	}

	public Long addAdminGroup(String userName, String password, int enable,
			long roleId, String realName, String telphone, String qq,
			String email, String img, String isLeader, Integer sex,
			String card, String summary, Integer nativePlacePro,
			Integer nativePlaceCity, String address, long parentId, int level)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = adminDao.queryAdminList(conn,
				userName, null);
		if (list != null && list.size() > 0) {
			return -2L;
		}
		Long adminId = -1L;
		try {
			adminId = addAdminGroupInfo(conn, userName, password, enable,
					roleId, realName, telphone, qq, email, img, isLeader, sex,
					card, summary, nativePlacePro, nativePlaceCity, address);
			if (adminId <= 0) {
				return adminId;
			}
			long returnId = relationDao.addRelation(conn, adminId, parentId,
					level, 1);
			if (returnId <= 0) {
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
		return adminId;
	}

	public Long updateAdminGroup(long adminId, String userName,
			String password, Integer enable, long roleId, String realName,
			String telphone, String qq, String email, String img,
			String isLeader, Integer sex, String card, String summary,
			Integer nativePlacePro, Integer nativePlaceCity, String address,
			Long relationId, Long parentId) throws Exception {
		long returnId = -1;
		Connection conn = MySQL.getConnection();
		try {
			returnId = updateAdminGroupInfo(conn, adminId, password, enable,
					roleId, realName, telphone, qq, email, img, isLeader, sex,
					card, summary, nativePlacePro, nativePlaceCity, address);
			if (returnId <= 0) {
				return returnId;
			}
			if (relationId != null && relationId > 0) {
				returnId = relationDao.updateRelation(conn, relationId, null,
						parentId, null, null);
			}
			if (returnId <= 0) {
				return returnId;
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
		return returnId;
	}

	/**
	 * 查询团队长信息
	 * 
	 * @param userName
	 * @param realName
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
	public void queryGroupCaptain(String userName, String realName,
			String startDate, String endDate,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		StringBuffer condition = new StringBuffer();
		condition.append(" AND roleId=1 ");
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" AND userName like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'");
		}
		if (StringUtils.isNotBlank(realName)) {
			condition.append(" AND realName like '%"
					+ StringEscapeUtils.escapeSql(realName.trim()) + "%'");
		}
		if (StringUtils.isNotBlank(startDate)) {
			condition.append(" AND addDate >= '"
					+ StringEscapeUtils.escapeSql(startDate) + "'");
		}
		if (StringUtils.isNotBlank(endDate)) {
			condition.append(" AND addDate <= '"
					+ StringEscapeUtils.escapeSql(endDate) + "'");
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" t_admin ",
					" id,userName,realName,card,telphone,summary,date_format(addDate,'%Y-%m-%d %H:%i:%s' ) as addDate ,enable ",
					" order by id desc ", condition.toString());
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
	}

	public List<Map<String, Object>> queryAdminByRoleId(long roleId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = adminDao.queryAdminByRoleId(conn, roleId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	public long addrelation(long peopleId, long parentId, int level, int enable)
			throws Exception {
		// Connection conn = MySQL.getConnection();
		// conn.setAutoCommit(false);
		// long returnId = 0;
		// for (int i = 0; i < 10; i++) {
		// returnId = relationDao.addRelation(conn, peopleId, parentId,
		// level, enable);
		// relationDao.updateRelation(conn, 282 + i, peopleId + 2,
		// parentId + 1, level + 1, enable + 1);
		// }
		// conn.commit();
		// conn.close();
		// return returnId;

		Connection conn = MySQL.getConnection();
		long returnId = 0;
		try {
			for (int i = 0; i < 10; i++) {
				returnId = relationDao.updateRelation(conn, 282 + i,
						peopleId + 2, parentId + 1, level + 1, enable + 1);
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
		return returnId;
	}

	/**
	 * 添加管理人
	 * 
	 * @param conn
	 * @param userName
	 * @param password
	 * @param enable
	 * @param roleId
	 * @param realName
	 * @param telphone
	 * @param qq
	 * @param email
	 * @param img
	 * @param isLeader
	 * @return
	 * @throws Exception
	 */
	private Long addAdminInfo(Connection conn, String userName,
			String password, int enable, long roleId, String realName,
			String telphone, String qq, String email, String img,
			String isLeader) throws Exception {
		Long id = adminDao.addAdmin(conn, userName, password, enable, roleId,
					realName, telphone, qq, email, img, isLeader, null, null,
					null, null, null, null);
		return id;
	}

	/**
	 * 添加军团长/经济人
	 * 
	 * @return
	 * @throws Exception
	 */
	private Long addAdminGroupInfo(Connection conn, String userName,
			String password, int enable, long roleId, String realName,
			String telphone, String qq, String email, String img,
			String isLeader, Integer sex, String card, String summary,
			Integer nativePlacePro, Integer nativePlaceCity, String address)
			throws Exception {
		Long id = adminDao.addAdmin(conn, userName, password, enable, roleId,
				realName, telphone, qq, email, img, isLeader, sex, card,
				summary, nativePlacePro, nativePlaceCity, address);
		return id;
	}

	/**
	 * 修改管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	private long updateAdminInfo(Connection conn, long adminId,
			String password, Integer enable, String lastIP, Long roleId,
			String realName, String telphone, String qq, String email,
			String img, String isLeader) throws Exception {
		return adminDao.updateAdmin(conn, adminId, password, enable, lastIP,
				roleId, realName, telphone, qq, email, img, isLeader, null,
				null, null, null, null, null);
	}

	private long updateAdminGroupInfo(Connection conn, long adminId,
			String password, Integer enable, Long roleId, String realName,
			String telphone, String qq, String email, String img,
			String isLeader, Integer sex, String card, String summary,
			Integer nativePlacePro, Integer nativePlaceCity, String address)
			throws Exception {
		return adminDao.updateAdmin(conn, adminId, password, enable, null,
				roleId, realName, telphone, qq, email, img, isLeader, sex,
				card, summary, nativePlacePro, nativePlaceCity, address);
	}

	public Map<String, String> queryCheckCount(long adminId) throws Exception {
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			Procedures.pr_examine(conn, ds, outParameterValues, adminId);
			map = DataSetHelper.dataSetToMap(ds);
			conn.commit();
		} catch (Exception e) {
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
	 * 查询是否存在该用户名
	 * 
	 * @param conn
	 * @param userName
	 * @return 存在返回true
	 * @throws Exception
	 */
	public boolean isExistUserName(String userName) throws Exception {
		Connection conn = MySQL.getConnection();
		boolean result = false;
		try {
			result = adminDao.isExistUserName(conn, userName);
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
	 * 根据状态禁用 或开启 管理员权限
	 * 
	 * @param id
	 * @param enable
	 * @return
	 * @throws Exception
	 */
	public long isenableAdmin(long id, int enable) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			result = adminDao.isenableAdmin(conn, id, enable);
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
	 * 查询经纪人信息
	 * 
	 * @param userName
	 * @param realName
	 * @param startDate
	 * @param endDate
	 * @throws Exception
	 */
	public void queryrelationeconomicInfo(String userName, String realName,
			String startDate, String endDate,
			PageBean<Map<String, Object>> pageBean) throws Exception {
		StringBuffer condition = new StringBuffer();
		condition.append(" AND roleId=2 ");
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" AND userName like '%"
					+ StringEscapeUtils.escapeSql(userName.trim()) + "%'");
		}
		if (StringUtils.isNotBlank(realName)) {
			condition.append(" AND realName like '%"
					+ StringEscapeUtils.escapeSql(realName.trim()) + "%'");
		}
		if (StringUtils.isNotBlank(startDate)) {
			condition.append(" AND addDate >= '"
					+ StringEscapeUtils.escapeSql(startDate.trim()) + "'");
		}
		if (StringUtils.isNotBlank(endDate)) {
			condition.append(" AND addDate <= '"
					+ StringEscapeUtils.escapeSql(endDate.trim()) + "'");
		}
		Connection conn = MySQL.getConnection();
		try {
			dataPage(
					conn,
					pageBean,
					" t_admin ",
					" id,userName,realName,card,telphone,summary,addDate,enable ",
					" order by id desc ", condition.toString());
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}
	}

	public void setAdminDao(AdminDao adminDao) {
		this.adminDao = adminDao;
	}

	public void setRelationDao(RelationDao relationDao) {
		this.relationDao = relationDao;
	}
}