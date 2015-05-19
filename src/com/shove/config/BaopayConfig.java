package com.shove.config;

import java.util.HashMap;
import java.util.Map;

/* *
 *国付宝配置
 */
public class BaopayConfig {
	/**
	 * 商户号
	 */
	public static String baopay_merchantID = "";
	
	/**
	 * 支付渠道手机卡支付：PayID=1   NoticeType
			网上银行支付：PayID=1000

	 */
	public static String baopay_payID = "";
	/**
	 * 通知方式   默认值为0
	 */
	public static String baopay_noticeType = "";
	/**
	 * 宝付网关地址
	 */
	public static String baopay_gateway = "";
	/**
	 * 宝付加密key
	 */
	public static String baopay_key = "";
	
	
	/**
	 * 返回地址
	 */
	public static String baopay_backgroundMerUrl = "";
	
	public static String baopay_frontMerUrl = "";
	
	public static Map<String, String> bankMap = new HashMap<String, String>();

	static{
		com.shove.io.file.PropertyFile pf = new com.shove.io.file.PropertyFile();
		baopay_merchantID = pf.read("baopay_merchantID");
		baopay_payID = pf.read("baopay_payID");
		baopay_noticeType = pf.read("baopay_noticeType");
		baopay_gateway = pf.read("baopay_gateway");
		baopay_key = pf.read("baopay_key");
		baopay_backgroundMerUrl = pf.read("baopay_backgroundMerUrl");
		baopay_frontMerUrl = pf.read("baopay_frontMerUrl");
		
		bankMap.put("CCB", "建设银行");
		bankMap.put("CMB", "招商银行");
		bankMap.put("ICBC", "工商银行");
		bankMap.put("BOC", "中国银行");
		bankMap.put("ABC", "农业银行");
		bankMap.put("BOCOM", "交通银行");
		bankMap.put("CMBC", "民生银行");
		bankMap.put("CIB", "兴业银行");
		bankMap.put("SPDB", "上海浦发银行");
		bankMap.put("GDB", "广东发展银行");
		bankMap.put("CITIC", "中信银行");
		bankMap.put("CEB", "光大银行");
		bankMap.put("BOBJ", "北京银行");
		bankMap.put("PAB", "平安银行");
		

	}
}
