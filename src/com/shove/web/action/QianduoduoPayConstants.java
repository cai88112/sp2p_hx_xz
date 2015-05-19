/**   
* @Title: QianduoduoPayConstants.java 
* @Package com.shove.web.action 
* @Description: TODO(用一句话描述该文件做什么) 
* @author yinzisong 690713748@qq.com   
* @date 2015年1月4日 下午3:25:40 
* @version V1.0   
*/ 
package com.shove.web.action;

import com.sp2p.constants.IConstants;

/**
 * 乾多多常量. 
 * @ClassName: QianduoduoPayConstants 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author yinzisong 690713748@qq.com 
 * @date 2015年1月4日 下午3:25:40 
 *  
 */
public class QianduoduoPayConstants {

	/**
	 * 开户.
	 */
	public static String createIpsAcct = "loan/toloanregisterbind.action";
	/**
	 * 转账.
	 */
	public static String transferAccount = "loan/loan.action";
	
	/**
	 * 充值.
	 */
	public static String recharge = "loan/toloanrecharge.action";
	
	/**
	 * 审核.
	 */
	public static String transferaudit = "loan/toloantransferaudit.action";
	
	/**
	 * 提现.
	 */
	public static String withdraws = "loan/toloanwithdraws.action";
	
	/**
	 * 跳转地址
	 */
	public static String url = IConstants.WEB_URL;
}
