package com.shove.config;

import java.util.HashMap;
import java.util.Map;

/* *
 *通联支付配置
 */
public class TlpayConfig {
	//表单的参数
	/**
	 * 字符集 固定值 1、2、3 1是UTF-8  2是GBK  3是GB2312 默认值是1
	 */
	public static String tlpay_inputCharset ="";
	/**
	 * 客户的取货地址
	 */
	public static String tlpay_pickupUrl="";
	/**
	 * 通知商户网站支付结果的url 地址
	 */
	public static String tlpay_receiveUrl="";
	/**
	 * 版本 固定值 v1.0
	 */
	public static String tlpay_version="";
	/**
	 * 语言 固定值1代表的是中文
	 */
	public static String tlpay_language="";
	/**
	 * 签名的方式 固定值1 代表的是证书签名的方式
	 */
	public static String tlpay_signType="";
	//买卖双方信息参数
	/**
	 * 商户号
	 */
	public static String tlpay_merchantId="";
	/**
	 * 合作伙伴的商户号
	 */
	public static String tlpay_Pid="";
	
	/**
	 * 支付方式payType
	 */
	public static String  tlpay_payType="";
	/**
	 * 密钥
	 */
	public static String  tlpay_key="";
//	/**
//	 * 订单的时间orderDatetime
//	 */
//	public static String  tlpay_orderDatetime="";
//	//**
	/**
	 * 订单的金额类型
	 */
	public static String  tlpay_orderCurrency="";
	/**
	 *商品和的描述 productDescription
	 */
	public static String  tlpay_productDescription="";
	/**
	 * 通联的网关地址gateway 
	 */
	public static String  tlpay_gateway="";
	/**
	 * 支付证书
	 */
	public static String tlpay_public_cer = "";
	
	public static Map<String, String> bankMap = new HashMap<String, String>();

	static{
		com.shove.io.file.PropertyFile pf = new com.shove.io.file.PropertyFile();
		tlpay_pickupUrl = pf.read("tlpay_pickupUrl");
		tlpay_receiveUrl = pf.read("tlpay_receiveUrl");
		tlpay_Pid = pf.read("tlpay_Pid");
		tlpay_language = pf.read("tlpay_language");
		tlpay_signType = pf.read("tlpay_signType");
		tlpay_inputCharset=pf.read("tlpay_inputCharset");
		tlpay_merchantId = pf.read("tlpay_merchantId");
		tlpay_payType = pf.read("tlpay_payType");
		tlpay_gateway=pf.read("tlpay_gateway");
		tlpay_version=pf.read("tlpay_version");
		tlpay_key=pf.read("tlpay_key");
		tlpay_productDescription = pf.read("tlpay_productDescription");
		
		//合作的银行
		bankMap.put("CCB", "建设银行");
		bankMap.put("CMB", "招商银行");
		bankMap.put("ICBC", "工商银行");
		bankMap.put("BOC", "中国银行");
		bankMap.put("ABC", "农业银行");
		bankMap.put("BOCOM", "交通银行");
		bankMap.put("HXCB", "华夏银行");
		bankMap.put("CIB", "兴业银行");
		bankMap.put("CITIC", "中信银行");
		bankMap.put("CEB", "光大银行");
		bankMap.put("BOS", "上海银行");
		bankMap.put("PAB", "平安银行");
		bankMap.put("SPDB", "浦发银行");
		
	}
}
