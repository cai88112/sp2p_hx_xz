package com.shove.config;

import java.util.HashMap;
import java.util.Map;

/* *
 *汇潮支付接口配置
 */
public class EcpssPayConfig {
	/**
	 * 商户号
	 */
	public static String ecpssPay_merchantID = "";
	/**
	 * 加密key值
	 */
	public static String ecpssPay_mdkey = "";
	
	
	/**
	 * 汇潮网关地址
	 */
	public static String ecpssPay_gateway = "";
	
	
	public static String ecpssPay_backgroundMerUrl = "";
	
	public static String ecpssPay_frontMerUrl = "";
	
	
	public static Map<String, String> bankMap = new HashMap<String, String>();

	static{
		com.shove.io.file.PropertyFile pf = new com.shove.io.file.PropertyFile();
		ecpssPay_merchantID=pf.read("ecpssPay_merchantID");
		ecpssPay_mdkey=pf.read("ecpssPay_mdkey");
		ecpssPay_gateway=pf.read("ecpssPay_gateway");
		ecpssPay_backgroundMerUrl=pf.read("ecpssPay_backgroundMerUrl");
		ecpssPay_frontMerUrl=pf.read("ecpssPay_frontMerUrl");
		
		bankMap.put("CCB", "建设银行");
		bankMap.put("CMB", "招商银行");
		bankMap.put("ICBC", "工商银行");
		bankMap.put("BOC", "中国银行");
		bankMap.put("ABC", "农业银行");
		bankMap.put("BOCOM", "交通银行");
		bankMap.put("CMBC", "民生银行");
		bankMap.put("HXCB", "华夏银行");
		bankMap.put("CIB", "兴业银行");
		bankMap.put("SPDB", "上海浦发银行");
		bankMap.put("GDB", "广东发展银行");
		bankMap.put("CITIC", "中信银行");
		bankMap.put("CEB", "光大银行");
		bankMap.put("PSBC", "中国邮政储蓄银行");
		bankMap.put("BOBJ", "北京银行");
		bankMap.put("TCCB", "天津银行");
		bankMap.put("BOS", "上海银行");
		bankMap.put("SRCB", "上海农商银行");
		bankMap.put("PAB", "平安银行");
		

	}
}
