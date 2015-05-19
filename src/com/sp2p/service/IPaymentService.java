package com.sp2p.service;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.shove.DataSetHelper;
import com.ips.security.utility.IpsCrypto;
import com.shove.base.BaseService;
import com.shove.config.IPayConfig;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.web.action.IPaymentConstants;
import com.shove.web.action.IPaymentUtil;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_orderno;

public class IPaymentService extends BaseService {
	
	public static Log log = LogFactory.getLog(IPaymentService.class);
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
	

	/**
	 * 投标审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> checkTrade(String tradeNo, String status,
			String pContractNo) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		String pBidStatus = "9999";
		if ("4".equals(status)) {
			pBidStatus = "0000";
		}
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", pContractNo);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pBidStatus", pBidStatus);
		map.put("pS2SUrl", IPaymentConstants.url + "/reCheckTradeUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		String str3DesXmlPana = IPaymentUtil.parseMapToXml(map);
		str3DesXmlPana = IpsCrypto.triDesEncrypt(str3DesXmlPana,
				IPayConfig.des_key, IPayConfig.des_iv);
		str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "");
		str3DesXmlPana = str3DesXmlPana.replaceAll("\n", "");
		String strSign = IpsCrypto.md5Sign(IPayConfig.terraceNoOne
				+ str3DesXmlPana + IPayConfig.cert_md5);
		String soap = IPaymentUtil.getSoapAuditTender(str3DesXmlPana,
				IPaymentConstants.auditTender, "argMerCode", "arg3DesXmlPara",
				"argSign", strSign);
		String url = IPayConfig.ipay_web_gateway
				+ IPaymentConstants.auditTender;
		// Map<String, String> data =
		// IPaymentUtil.webService(soap,"AuditTenderResponse");

		Map<String, String> data = IPaymentUtil.webService(soap, url);
		return data;
	}
}
