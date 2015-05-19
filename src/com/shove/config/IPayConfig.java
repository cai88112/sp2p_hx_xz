package com.shove.config;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.fp2p.helper.infusion.SqlInfusionHelper;


@SuppressWarnings("unchecked")
public class IPayConfig {
	
	//商户编号
	public static String ipay_mer_code = "";
	
	//前端返回地址
	public static String ipay_mer_chanurl = "";
	
	//后端地址
	public static String ipay_server_url = "";
	
	//环迅支付网关
	public static String ipay_gateway = "";
	
	//支付加密方式
	public static String ipay_order_encode_type = "";
	
	//加密方式
	public static String ipay_see_key = "";
	
	//商户内部证书
	public static String ipay_certificate = "";
	
	public static String ipay_ps2surl ="";
	
	
	public static String ipay_pweburl ="";
	
	//
	public static String des_key	="";
	public static String des_iv =	""; 
	public static String cert_md5	= "";
	public static String ipay_web_service = "";
	public static String pubKey = "";
	//环迅支付webservice网关
	public static String ipay_web_gateway = "";
	public static String terraceNoOne = "";
	static{
		
		com.shove.io.file.PropertyFile pf = new com.shove.io.file.PropertyFile();
		ipay_mer_chanurl = pf.read("ipay_mer_chanurl");
		des_key= pf.read("des_key");
		des_iv= pf.read("des_iv");
		cert_md5= pf.read("cert_md5");
		ipay_mer_code = pf.read("ipay_mer_code");
		ipay_server_url = pf.read("ipay_server_url");
		ipay_gateway = pf.read("ipay_gateway");
		ipay_order_encode_type = pf.read("ipay_order_encode_type");
		ipay_see_key = pf.read("ipay_see_key");
		ipay_web_service= pf.read("ipay_web_service");
		pubKey = pf.read("pubKey");
		ipay_web_gateway = pf.read("ipay_web_gateway");
		
		try {
			Map<String, String> map = com.sp2p.service.admin.FundManagementService.queryAccountPaymentByIdS("IPS");
			String config = map.get("config");
			if (StringUtils.isNotBlank(config)) {
				Map<String, String> maps = (Map<String, String>) JSONObject
				.toBean(JSONObject.fromObject(config), HashMap.class);
				if (map != null) {
					map.putAll(maps);
				}
				ipay_mer_code = map.get("customerID");
				ipay_certificate = map.get("privatekey");
				des_key = map.get("des_key");
				pubKey = map.get("pubKey").replace("\\n", "\n");
				cert_md5 = map.get("cert_md5");
				des_iv = map.get("des_iv");
				ipay_gateway = map.get("ipay_gateway");
				ipay_web_service = map.get("ipay_web_service");
				terraceNoOne = map.get("terraceNoOne");
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
