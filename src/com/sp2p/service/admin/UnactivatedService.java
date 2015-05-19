package com.sp2p.service.admin;

import java.sql.Connection;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.base.BaseService;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;

public class UnactivatedService extends BaseService {
	public static Log log = LogFactory.getLog(UnactivatedService.class);

	/**
	 * 查询为激活的用户
	 */
	public void queryUserUnactivated(PageBean<Map<String, Object>> pageBean,
			String userName, String createtimeStart, String createtimeEnd,
			String email) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			StringBuffer condition = new StringBuffer();
			if (StringUtils.isNotBlank(userName)) {
				condition.append(" and  username  like '%"
						+ StringEscapeUtils.escapeSql(userName.trim()) + "%' ");
			}
			if (StringUtils.isNotBlank(email)) {
				condition.append(" and  email  like '%"
						+ StringEscapeUtils.escapeSql(email.trim()) + "%' ");
			}
			if (StringUtils.isNotBlank(createtimeStart)) {
				condition.append(" and createTime >'"
						+ StringEscapeUtils.escapeSql(createtimeStart.trim())
						+ "'");
			}
			if (StringUtils.isNotBlank(createtimeEnd)) {
				condition.append(" and createTime <'"
						+ StringEscapeUtils.escapeSql(createtimeEnd.trim())
						+ "'");
			}
			// condition.append(" 1 = 1");
			condition.append(" AND enable =  2 ");// 2为状态未激活的值
			dataPage(conn, pageBean, "t_user", "*", " ORDER BY id", condition
					.toString());

		}  catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

}
