package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.dao.admin.PlatformCostDao;

/**
 * 平台收费标准 -----业务逻辑层
 * 
 * @author C_J
 * 
 */
public class PlatformCostService extends BaseService {
	public static Log log = LogFactory.getLog(PlatformCostService.class);

	private PlatformCostDao platformCostDao;

	/**
	 * 查询所有收费标准
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public void queryPlatformCostAll(PageBean<Map<String, Object>> pageBean,
			int type) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			platformCostDao.queryPlatformCostAll(conn, pageBean, type);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 根据ID修改收费标准
	 * 
	 * @param conn
	 * @param costFee
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws Exception
	 * @throws Exception
	 */
	public Long updatePlatformCostById(Double costFee, int id,
			Map<String, Object> platformMap) throws Exception {
		Long result = -1L;
		Connection conn = MySQL.getConnection();
		try {
			// 查询别名
			result = platformCostDao.updatePlatformCostById(conn, costFee, id);
			Map<String, String> maps = platformCostDao.queryPlatformCostById(
					conn, id);
			if(maps == null){
				maps = new HashMap<String, String>();
			}
			if (maps.get("costMode").equals("1")) {
				platformMap.put(maps.get("alias"), costFee * 0.01);// 将比例转换成百分比放入Map集合中
			} else {
				platformMap.put(maps.get("alias"), costFee);
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

	/**
	 * 修改
	 * 
	 * @param id
	 * @param show_view
	 * @return
	 * @throws Exception
	 */
	public long updateShow_view(int id, int show_view) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			platformCostDao.updateShow_view(conn, id, show_view);
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
	 * 根据ID查询平台收费标准
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public Map<String, String> queryPlatformCostById(int id) throws Exception {
		Map<String, String> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = platformCostDao.queryPlatformCostById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public void setPlatformCostDao(PlatformCostDao platformCostDao) {
		this.platformCostDao = platformCostDao;
	}

	/**
	 * @MethodName: queryAllPlatformCost
	 * @Param: PlatformCostService
	 * @Author: gang.lv
	 * @Date: 2013-5-29 下午03:58:51
	 * @Return:
	 * @Descb: 查询平台所有的收费
	 * @Throws:
	 */
	public List<Map<String, Object>> queryAllPlatformCost() throws Exception{
		List<Map<String, Object>> list = null;
		Connection conn = MySQL.getConnection();
		try {
			list = platformCostDao.queryPlatformCostAll(conn);
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
