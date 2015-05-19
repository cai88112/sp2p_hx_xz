package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp2p.dao.admin.EmalAndMessageDao;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;

public class EmalAndMessageService extends BaseService {
	public static Log log = LogFactory.getLog(EmalAndMessageService.class);

	private EmalAndMessageDao emalAndMessageDao;

	public void setEmalAndMessageDao(EmalAndMessageDao emalAndMessageDao) {
		this.emalAndMessageDao = emalAndMessageDao;
	}

	/**
	 * 插入邮件设置表
	 * 
	 * @param mailaddress
	 * @param mailpassword
	 * @param sendmail
	 * @param sendname
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addUserWorkData(String mailaddress, String mailpassword,
			String sendmail, String sendname, String port, String host)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addMailSet(conn, mailaddress,
					mailpassword, sendmail, sendname, port, host);
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
	 * 添加或者修改短信设置
	 * 
	 * @param id
	 * @param username
	 * @param password
	 * @param url
	 * @param enable
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addMessageSet(Long id, String username, String password,
			String url, Integer enable) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addMessageSet(conn, id, username,
					password, url, enable);
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

	public Long addTarage(String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addTarage(conn, name);
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
	 * 增加担保方式
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addDan(String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addDan(conn, name);
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
	 * 增加反担保机构
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long addInver(String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addInver(conn, name);
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
	 * 添加系统头像
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long addSysImage(String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addSysImage(conn, name);
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

	public Long updateTage(Long id, String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.updateTage(conn, id, name);
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
	 * 修改担保机构
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long updateAccount(Long id, String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.updateAccount(conn, id, name);
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
	 * 更改系统头像
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long updateSysImage(Long id, String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.updateSysImage(conn, id, name);
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
	 * 修改投标金额
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long updateInvers(Long id, String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.updateAccount(conn, id, name);
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
	 * 修改借款期限
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long updateDeadline(Long id, String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.updateDeadline(conn, id, name);
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

	public Long deleteTage(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.deleteTage(conn, id);
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
	 * 删除反担保fangsh
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long deleteacc(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.deleteacc(conn, id);
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
	 * 删除系统头像
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long deletSImage(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.deletSImage(conn, id);
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
	 * 查询信息设置
	 * 
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public Map<String, String> queryMessageSet(long id) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = emalAndMessageDao.queryMessageSet(conn, id);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		}finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 得到当前最大的ID
	 * 
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryMailsetMaxId() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = emalAndMessageDao.queryMailsetMaxId(conn);
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

	public Map<String, String> queryMailSet(long id) throws DataException,
			Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = emalAndMessageDao.queryMailSet(conn, id);
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
	 * 查询借款目的内容
	 * 
	 * @param pageBean
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> querySelectInfo(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		condition.append(" AND ts.typeId = 1 AND ts.deleted = 1");
		/*
		 * if(userId!=null&&userId>0){ condition.append(" AND id = "+userId); }
		 */
		// =============================
		StringBuffer sqlresult = new StringBuffer();
		sqlresult.append(" ts.id as 'id' , ");
		sqlresult.append(" ts.selectName as 'name' ");
		// ==========================
		StringBuffer sql = new StringBuffer();
		sql.append(" t_select ts ");
		// ================================
		try {
			dataPage(conn, pageBean, sql.toString(), sqlresult.toString(),
					" order by id ", condition.toString());
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
	 * 查询反担保方式
	 * 
	 * @param pageBean
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryIversInof(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		condition.append(" AND ts.typeId = 3");
		StringBuffer sql = new StringBuffer();
		sql.append(" t_select ts ");
		try {
			dataPage(conn, pageBean, sql.toString(), " * ", " order by id ",
					condition.toString());
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
	 * 查询系统头像
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> querySysImageInfo(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		condition.append(" AND ts.enable = 1 ");
		/*
		 * if(userId!=null&&userId>0){ condition.append(" AND id = "+userId); }
		 */
		// =============================
		StringBuffer sqlresult = new StringBuffer();
		sqlresult.append(" ts.id as id , ");
		sqlresult.append(" ts.imagePath as imagePath ");
		// ==========================
		StringBuffer sql = new StringBuffer();
		sql.append(" t_sysimages ts ");
		// ================================
		try {
			dataPage(conn, pageBean, sql.toString(), sqlresult.toString(),
					" order by id ", condition.toString());
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
	 * 查询机构担保列表
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryAccountInfo(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		condition.append(" AND typeId = 2 ");
		try {
			dataPage(conn, pageBean, " t_select ", " * ", " order by id ",
					condition.toString());
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
	 * 修改
	 * 
	 * @param type
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public long updateSelectType(int type, long id) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			result = emalAndMessageDao.updateSelectType(conn, type, id);
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
	 * 查询所有担保机构
	 * 
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryinstitution() throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = emalAndMessageDao.queryinstitution(conn);
			conn.commit();
		} catch (Exception e) {
			log.info(e);
			e.printStackTrace();
			conn.rollback();
			
			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 查询所有反担保方式
	 * 
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryguarantee() throws Exception {

		List<Map<String, Object>> map = null;

		Connection conn = MySQL.getConnection();
		try {
			map = emalAndMessageDao.queryguarantee(conn);
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
	 * 查询借款期限列表
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryDeadlineInfo(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		condition.append(" AND typeId = 4 ");
		try {
			dataPage(conn, pageBean, " t_select ", " * ",
					" order by selectValue ", condition.toString());
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
	 * 查询金额范围列表
	 * 
	 * @param pageBean
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryMomeyInfo(
			PageBean<Map<String, Object>> pageBean) throws Exception {
		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		StringBuffer condition = new StringBuffer();
		condition.append(" AND typeId = 5 ");
		try {
			dataPage(conn, pageBean, " t_select ", " * ",
					" order by selectValue ", condition.toString());
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
	 * 修改金额范围 和借款期限
	 * 
	 * @param id
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long updateMoney(Long id, String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.updateMoney(conn, id, name);
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
	 * 增加借款期限和金额范围
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long addDeadline(String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addDeadline(conn, name);
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
	 * 增加借款期限和金额范围
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Long addMoney(String name) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = emalAndMessageDao.addMomey(conn, name);
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
}
