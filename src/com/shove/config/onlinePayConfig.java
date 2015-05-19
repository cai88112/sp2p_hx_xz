package com.shove.config;

public class onlinePayConfig {
	
	//商户编号
	public static String v_mid = "";
	
	//币种
	public static String v_moneytype = "";
	
	//URL地址(前台)
	public static String v_url = "";
	
	public static String v_online_backgroud_url ="";
	public static String v_md5_key = "";
	public static String v_online_gateway = "";
	
	//后端地址
	//public static String ipay_server_url = "";
	
	//环迅支付网关
/*	public static String ipay_gateway = "";
	
	//支付加密方式
	public static String ipay_order_encode_type = "";
	
	//加密方式
	public static String ipay_see_key = "";
	
	//商户内部证书
	public static String ipay_certificate = "";*/
	

	static{
		com.shove.io.file.PropertyFile pf = new com.shove.io.file.PropertyFile();
		v_mid = pf.read("v_mid");
		v_moneytype = pf.read("v_moneytype");
		v_url =  pf.read("v_url");
		v_online_backgroud_url = pf.read("v_online_backgroud_url");
		v_md5_key=pf.read("v_md5_key");
		v_online_gateway = pf.read("v_online_gateway");
	}
}
