package com.sp2p.action.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.data.DataRow;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_award;

public class SysTemOptions extends BaseFrontAction {

	/**
	 * 获得系统选项表
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String GetsysTemOptionsTable() throws SQLException {
		Connection conn = MySQL.getConnection();
		int out_ret = 0;
		String out_desc = "";
		StringBuilder sb = new StringBuilder();
		DataSet ds = new DataSet();
		List<Map<String, Object>> Group = null;
		List<Map<String, Object>> List = null;

		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Dao.Procedures.p_getoptions(conn, ds, outParameterValues, out_ret, out_desc); 

			// 获得t_options的分组
			ds.tables.get(0).rows.genRowsMap();
			Group = ds.tables.get(0).rows.rowsMap;

			// 获得t_options所有数据
			ds.tables.get(1).rows.genRowsMap();
			List = ds.tables.get(1).rows.rowsMap;

		} catch (Exception e) {

		} finally {
			conn.close();
		}

		if (ds == null) {
			return ERROR;
		}
		if (ds.tables.getCount() < 2) {
			return ERROR;
		}
		if (Group == null) {
			return ERROR;
		}
		if (List == null) {
			return ERROR;
		}
		// 根据分组进行循环
		for (Map<String, Object> map : Group) {
			String GroupName = map.get("group_name").toString();
			sb.append("<tr><th colspan=\"3\">" + GroupName + "</th></tr>");

			sb.append("<tr>");
			sb.append("<td>");
			sb.append("选项");
			sb.append("</td>");
			sb.append("<td>");
			sb.append("值");
			sb.append("</td>");
			sb.append("<td>");
			sb.append("描述");
			sb.append("</td>");
			sb.append("</tr>");

			// sb.append("<tr>");
			// sb.append("<td>123</td>");
			// sb.append("<td>值</td>");
			// sb.append("<td>2343</td>");
			// sb.append("</tr>");
			// 循环分组内容
			for (Map<String, Object> map_List : List) {
				String GroupName_List = map_List.get("group_name").toString();
				if (GroupName_List.equals(GroupName)) {
					System.out.print(GroupName_List + "---asdhgasdhkjadirhiu");
					String Name = map_List.get("paramname").toString();
					String Description = map_List.get("description").toString();
					String Values = map_List.get("value").toString();
					String Key = map_List.get("key").toString();
					boolean IsPassword = com.shove.Convert.strToBoolean(
							map_List.get("is_password").toString(), false);
					sb.append("<tr>");

					sb.append("<td>" + Name + "</td>");
					sb.append("<td>");
					if (Values.equals("true")) {
						sb
								.append("<input name=\""
										+ Key
										+ "_\" onclick=\"Setradio(this,'true')\" type=\"radio\" checked=\"checked\">是");
						sb
								.append("<input name=\""
										+ Key
										+ "_\" onclick=\"Setradio(this,'false')\" type=\"radio\">否");
						sb
								.append("<input type=\"hidden\"  value=\"true\" name=\""
										+ Key + "\"/>");
					} else if (Values.equals("false")) {
						sb
								.append("<input name=\""
										+ Key
										+ "_\" onclick=\"Setradio(this,'true')\" type=\"radio\">是");
						sb
								.append("<input name=\""
										+ Key
										+ "_\" onclick=\"Setradio(this,'false')\" type=\"radio\" checked=\"checked\">否");
						sb
								.append("<input type=\"hidden\"  value=\"false\" name=\""
										+ Key + "\"/>");
					} else {
						sb.append("<input name=\"" + Key
								+ "\" style=\"width:95%\" type=\""
								+ (IsPassword == false ? "text" : "password")
								+ "\" value=\"" + Values + "\">");
					}
					sb.append("</td>");
					sb.append("<td>" + Description + "</td>");
					sb.append("</tr>");
				}
			}

		}
		System.out.print(sb.toString() + "asdhgasdhkjadirhiu");
		request().setAttribute("table", sb.toString());
		return SUCCESS;
	}

	/**
	 * 获得提交的系统选项
	 * 
	 * @throws SQLException
	 */
	public String GetSysTemOptionsValues() throws SQLException {
		Enumeration<String> keyNames = request().getParameterNames();
		Map<String, String> map = new HashMap<String, String>();
		while (keyNames.hasMoreElements()) {
			String attrName = keyNames.nextElement();

			map.put(attrName, request(attrName));
		}
		Dao.Tables.t_options Edit_Info = new Dao().new Tables().new t_options();
		for (Entry<String, String> entry : map.entrySet()) {
			String Key = SqlInfusionHelper.filteSqlInfusion(entry.getKey().toString());
			String Value =SqlInfusionHelper.filteSqlInfusion( entry.getValue().toString());
			long Result = 0;
			 
			Edit_Info.value.setValue(Value);
			Connection conn = MySQL.getConnection();
			try {
				Result = Edit_Info.update(conn, "`Key` = '" + Key + "'");
				if (Result > 0) {
					conn.commit();
				}
			} catch (Exception e) {
				conn.rollback();
			} finally {
				conn.close();
			}
			// if(Result>0)
			// {
			// conn.commit();
			// }
		}

		return SUCCESS;

	}
}
