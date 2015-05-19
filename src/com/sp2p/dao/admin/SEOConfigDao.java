package com.sp2p.dao.admin;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.database.Dao;


/**
 * SEO标准配置
 * 
 * @author Administrator
 * 
 */
public class SEOConfigDao {
	
	/**
	 * 更新SEO配置信息
	 * 
	 * @param conn
	 * @param title
	 * @param description
	 * @param keywords
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public long updateSEOConfig(Connection conn,int siteMap, String otherTags,String title,String description,String keywords) 
			throws SQLException, DataException{
		Dao.Tables.t_seoconfig seo = new Dao().new Tables().new t_seoconfig();		
		if(querySEOConfig(conn) == null){
			return addSEOConfig(conn,siteMap, otherTags, title, description, keywords);
		}
		else{
			if (StringUtils.isNotBlank(title)) {
				seo.title.setValue(title);
			}
			else if(title.equals("")){
				seo.title.setValue("");
			}
			if (StringUtils.isNotBlank(description)) {
				seo.description.setValue(description);
			}
			else if(description.equals("")){
				seo.description.setValue("");
			}
			if (StringUtils.isNotBlank(keywords)) {
				seo.keywords.setValue(keywords);
			}
			else if(keywords.equals("")){
				seo.keywords.setValue("");
			}
			if (StringUtils.isNotBlank(otherTags)) {
				seo.otherTags.setValue(otherTags);
			}
			else if(otherTags.equals("")){
				seo.otherTags.setValue("");
			}
			seo.siteMap.setValue(siteMap);
			return seo.update(conn,"");
		}
	}
	

	public long  updateOptions(Connection  conn,String keyy,String value) throws SQLException, IOException, DataException{
		Dao.Tables.t_options options = new Dao().new Tables().new t_options();
		if (StringUtils.isNotBlank(value)) {
			options.value.setValue(value);
		}
		
		return options.update(conn, " `t_options`.`key` = '" + StringEscapeUtils.escapeSql(keyy) + "'");
		
	}

	
	/**
	 * 查看SEO配置信息
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String,String>  querySEOConfig(Connection conn) 
			throws SQLException, DataException{
		Dao.Tables.t_seoconfig seo = new Dao().new Tables().new t_seoconfig();
		DataSet dataSet = seo.open(conn, "*", "",
				"", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);	
	}
	
	public List<Map<String,Object>>  queryRegistCode(Connection conn) 
	throws SQLException, DataException{
		String command = "SELECT * FROM t_options WHERE `key` in('serial_number','serial_key')";
		DataSet dataSet =MySQL.executeQuery(conn, command);
		command = null;
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
}
	
	public long  updateRegistCode(Connection  conn,String keyy,String value) throws SQLException, IOException, DataException{
		Dao.Tables.t_options options = new Dao().new Tables().new t_options();
		long result=-1L;
		if (StringUtils.isNotBlank(keyy)) {
			options.value.setValue(keyy);
			result= options.update(conn, " id = "+4);
		}
		if (StringUtils.isNotBlank(value)) {
			options.value.setValue(value);
			result= options.update(conn, " id = "+3);
		}
		
		
		return result;
		
	}
	
	
	/**
	 * 添加SEO配置信息
	 * 
	 * @param conn
	 * @param title
	 * @param description
	 * @param keywords
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public long addSEOConfig(Connection conn,int siteMap, String otherTags,String title,String description,String keywords) 
			throws SQLException, DataException{
		Dao.Tables.t_seoconfig seo = new Dao().new Tables().new t_seoconfig();
		if (StringUtils.isNotBlank(title)) {
			seo.title.setValue(title);
		}
		if (StringUtils.isNotBlank(description)) {
			seo.description.setValue(description);
		}
		if (StringUtils.isNotBlank(keywords)) {
			seo.keywords.setValue(keywords);
		}		
		if (StringUtils.isNotBlank(otherTags)) {
			seo.otherTags.setValue(otherTags);
		}
		seo.siteMap.setValue(siteMap);
		return seo.insert(conn);
	}

	public Map<String, String> queryRegistKeyConfig(Connection conn) throws SQLException, DataException {
		Dao.Tables.t_options t_options = new Dao().new Tables().new t_options();
		DataSet dataSet = t_options.open(conn, " value ", " id=3",
				"", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);	
	}
	
	
}
