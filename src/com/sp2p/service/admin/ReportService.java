package com.sp2p.service.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sp2p.dao.admin.ReportDao;

import com.shove.base.BaseService;
import com.shove.data.ConnectionManager;
import com.shove.data.DataException;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;

/**
 * 用户举报管理
 * 
 * @author zhongchuiqing
 * 
 */
public class ReportService extends BaseService {

	public static Log log = LogFactory.getLog(ReportService.class);

	private ReportDao reportDao;

	private ConnectionManager connectionManager;

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public void setConnectionManager(ConnectionManager connectionManager) {
		this.connectionManager = connectionManager;
	}

	public ReportDao getReportDao() {
		return reportDao;
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}

	/**
	 * 更新用户举报 处理用户举报
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
	public Long updateReport(Long id, String remark, Integer status)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = reportDao.updateReport(conn, id, remark, status);
			conn.commit();
		} catch (SQLException e) {
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
	 * 删除团队信息
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long deleteReport(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = 0L;
		try {
			result = reportDao.deleteReport(conn, id);
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
	 * 获取举报详情
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getReportById(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = null;
		try {
			map = reportDao.getReportById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public void queryReportPage(PageBean<Map<String, Object>> pageBean,
			Integer username, Integer reporter, Integer status)
			throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer buffer = new StringBuffer();
		try {
			if (username != null && username != -1) {
				buffer.append(" and user=");
				buffer.append(username);
			}
			if (reporter != null && reporter != -1) {
				buffer.append(" and reporter=");
				buffer.append(reporter);
			}

			if (status != null && status != -1) {
				buffer.append(" and status=");
				buffer.append(status);
			}
			dataPage(conn, pageBean, "t_report", "*",
					"order by reportTime desc", buffer.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

}
