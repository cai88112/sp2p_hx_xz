/**   
 * @Title: BankService.java 
 * @Package com.sp2p.service 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月15日 下午2:01:57 
 * @version V1.0   
 */
package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.data.dao.MySQL;
import com.sp2p.dao.BankDao;

/**
 * 乾多多支付支持银行.
 * 
 * @ClassName: BankService
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月15日 下午2:01:57
 * 
 */
public class BankService {
	private BankDao bankDao;
	public static Log log = LogFactory.getLog(BankService.class);

	/**
	 * @throws SQLException
	 *             查询省列表.
	 * 
	 * @Title: queryProvinceList
	 * @param
	 * @return List<Map<String,Object>> 返回类型
	 * @throws
	 */
	public List<Map<String, Object>> queryBankList() throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection conn = MySQL.getConnection();
		try {
			list = bankDao.queryBankList(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return list;
	}

	public final BankDao getBankDao() {
		return bankDao;
	}

	public final void setBankDao(BankDao bankDao) {
		this.bankDao = bankDao;
	}
}
