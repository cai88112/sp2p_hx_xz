package com.shove.config;

import java.util.HashMap;
import java.util.Map;

/* *
 *双乾支付配置
 */
public class ShuangqianpayConfig {
	/**
	 * 商户号
	 */
	public static String sqpay_MerNo = "";
	
	/**
	 * MD5keyPayType
	 */
	public static String sqpay_MD5key = "";
	/**
	 * 支付的方式
	 */
	
	public static String sqpay_PayType = "";
	/**
	 * 双乾网关地址
	 */
	
	public static String sqpay_gateway = "";
	/**
	 * 返回地址ReturnURL
	 */
	 public static String sqpay_ReturnURL = "";
	 /**
	 * 服务端后台支付状态通知
	 */
		 public static String sqpay_NotifyURL = "";
	public static Map<String, String> bankMap = new HashMap<String, String>();

	static{
		com.shove.io.file.PropertyFile pf = new com.shove.io.file.PropertyFile();
		sqpay_MerNo = pf.read("sqpay_MerNo");
		sqpay_MD5key = pf.read("sqpay_MD5key");
		sqpay_PayType = pf.read("sqpay_PayType");
		sqpay_gateway = pf.read("sqpay_gateway");
		sqpay_ReturnURL = pf.read("sqpay_ReturnURL");
		sqpay_NotifyURL = pf.read("sqpay_NotifyURL");
		
		
		bankMap.put("ICBC", "工商银行");
		bankMap.put("ABC", "农业银行");
		bankMap.put("BOCSH", "中国银行");
		bankMap.put("CCB", "建设银行");
		bankMap.put("CMB", "招商银行");
		bankMap.put("SPDB", "浦发银行");
		bankMap.put("GDB", "广发银行");
		bankMap.put("BOCOM", "交通银行");
		bankMap.put("PSBC", "邮政储蓄银行");
		bankMap.put("CNCB", "中信银行");
		bankMap.put("CMBC", "民生银行");
		bankMap.put("CEB", "光大银行");
		bankMap.put("HXB", "华夏银行");
		bankMap.put("CIB", "兴业银行");
		

	}
}
