/**   
 * @Title: QianduoduoConfig.java 
 * @Package com.shove.config 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2014年12月31日 下午5:50:28 
 * @version V1.0   
 */
package com.shove.config;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * @ClassName: QianduoduoConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2014年12月31日 下午5:50:28
 * 
 */
public class QianduoduoConfig {
	// 平台乾多多标识
	public static String platformMoneymoremore = "";

	// 公钥
	public static String publicKey = "";

	// 私钥
	public static String privateKeyPKCS8 = "";

	// 测试接口地址
	public static String submitURLPrefix_test = "";

	// 正式接口地址
	public static String submitURLPrefix = "";
	
	// 注册类型 1表示全自动，2表示半自动 全自动会生成随机的登录密码和支付密码发送到用户的手机，安保问题需要登录乾多多后台设置
	public static String registerType = "";
	
	//是否启用 防抵赖功能
	public static String antistate = "";

	static {
		com.shove.shuanquanUtil.PropertyFile pf = new com.shove.shuanquanUtil.PropertyFile(
				"/qianduoduoConfig.properties");
		platformMoneymoremore = pf.read("platformMoneymoremore");
		publicKey = pf.read("publicKey");
		privateKeyPKCS8 = pf.read("privateKeyPKCS8");
		submitURLPrefix_test = pf.read("SubmitURLPrefix_test");
		submitURLPrefix = pf.read("SubmitURLPrefix");
		registerType = pf.read("registerType");
		antistate = pf.read("antistate");
		
		//后台配置方式配置 （预留）
//		try {
//			Map<String, String> map = com.sp2p.service.admin.FundManagementService.queryAccountPaymentByIdS("IPS");
//			String config = map.get("config");
//			if (StringUtils.isNotBlank(config)) {
//				Map<String, String> maps = (Map<String, String>) JSONObject.toBean(JSONObject.fromObject(config), HashMap.class);
//				if (map != null) {
//					map.putAll(maps);
//				}
//				
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
