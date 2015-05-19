/**   
* @Title: QianduoduoPayUtil.java 
* @Package com.shove.web.action 
* @Description: TODO(用一句话描述该文件做什么) 
* @author yinzisong 690713748@qq.com   
* @date 2015年1月6日 上午11:35:51 
* @version V1.0   
*/ 
package com.shove.services;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.web.action.IPaymentUtil;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_orderno;

/** 
 * @ClassName: QianduoduoPayUtil 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author yinzisong 690713748@qq.com 
 * @date 2015年1月6日 上午11:35:51 
 *  
 */
public class QianduoduoPayUtil {
	public static Log log = LogFactory.getLog(QianduoduoPayUtil.class);
	/**
	 * 查询自增流水号
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	public static String getOrderNo(String prefix) throws Exception{
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
		String nowTime = df.format(new Date());
		Connection conn  = MySQL.getConnection();
		if(prefix == null){
			
			prefix = "";
		}
		String orderNo = "";
		try{
			DataSet dataSet = MySQL.executeQuery(conn," select id as id, max(orderNo) as  orderNo from t_orderno where orderNo like '" + nowTime + "%'");
			Map<String, String> map = DataSetHelper.dataSetToMap(dataSet) == null ? new HashMap<String, String>() : DataSetHelper.dataSetToMap(dataSet);
			Dao.Tables.t_orderno t_orderno = new Dao().new Tables().new t_orderno();
			log.info("0----"+map.get("orderNo"));
			if(map.get("orderNo") == null || "".equals(map.get("orderNo"))){
				log.info("1----"+map.get("orderNo"));
				t_orderno.orderNo.setValue(nowTime + "1000");
				t_orderno.insert(conn);
				orderNo = nowTime + "1000";
			}else{
				log.info("2----"+map.get("orderNo"));
				String tempOrderNo = map.get("orderNo");
				String prefixOrderNo = tempOrderNo.substring(0, tempOrderNo.length() - 4);
				String suffixOrderNo = tempOrderNo.substring(tempOrderNo.length() - 4, tempOrderNo.length());			
				t_orderno.orderNo.setValue(prefixOrderNo + (Integer.parseInt(suffixOrderNo) + 1));
				t_orderno.update(conn, " id = " + map.get("id"));
				orderNo = prefixOrderNo + (Integer.parseInt(suffixOrderNo) + 1);
			}
			conn.commit();
		}
		catch(Exception e){
			conn.rollback();
			e.printStackTrace();
			throw e;
		}finally{
			conn.close();
		}
		return orderNo;
	}
}
