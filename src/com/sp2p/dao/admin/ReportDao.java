package com.sp2p.dao.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;
/**
 * 用户举报管理
 * 
 * @author Administrator
 * 
 */
public class ReportDao {

	

	/**
	 * 删除举报信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long deleteReport(Connection conn, Long id) throws SQLException,
			DataException {
		Dao.Tables.t_report report=new Dao().new Tables().new t_report();
		
		
		return report.delete(conn, "id="+id);
	}

	
	/**
	 * 更新举报信息
	 * 
	 * @param conn
	 * @param id
	 * @param sort
	 * @param title
	 * @param content
	 * @param publishTime
	 * @param publisher
	 * @param visits
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateReport(Connection conn, Long id, String remark,
			Integer status) throws SQLException, DataException {
		Dao.Tables.t_report report=new Dao().new Tables().new t_report();
		if (status!=null) {
		   report.status.setValue(status);
		}
		
		if (StringUtils.isNotBlank(remark)) {
			report.remark.setValue(remark);
		}
		
		

		return report.update(conn, "id=" + id);

	}

	/**
	 * 获取举报详情
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getReportById(Connection conn, Long id)
			throws SQLException, DataException {
		String sql = " SELECT a.*,a.user as userId,b.username as username2,c.username as username1,c.email,d.realName,d.cellPhone as mobilePhone,d.idNo from t_report a LEFT JOIN " +
				" 	 t_user b on a.reporter = b.id LEFT JOIN t_user c on a.user = c.id LEFT JOIN t_person d on d.userId = a.reporter where a.id = " + id;
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	
}
