package com.shove.web.action;

import com.sp2p.constants.IConstants;

public class IPaymentConstants {
	/**
	 * 开户
	 */
	public static String createIpsAcct = "createIpsAcct.aspx";
	
	/**
	 * 充值
	 */
	public static String dpTrade = "dpTrade.aspx";
	/**
	 * 投标（普通标）
	 */
	public static String tenderTrade = "tenderTrade.aspx";
	/**
	 * 投标审核
	 */
	public static String auditTender = "AuditTender";
	/**
	 * 自动投标
	 */
	public static String autoSigning = "autoSigning.aspx";
	/**
	 * 还款
	 */
	public static String repaymentTrade = "repaymentTrade.aspx";
    /**
     * 提现
     */
	public static String dwTrade = "dwTrade.aspx";
	/**
	 * 商户端获取银行列表查询
	 */
	public static String getBankList = "GetBankList";
	/**
	 * 账户余额查询
	 */
	public static String queryForAccBalance = "QueryForAccBalance";

	/**
	 * 冻结保证金
	 */
	public static String guaranteeFreeze = "guaranteeFreeze.aspx";
	
	/**
	 * 担保交易资金划扣
	 */
	public static String guaranTransfer = "GuaranteeTransfer";
	
	/**
	 * 解冻保证金
	 */
	public static String guaranteeUnfreeze = "GuaranteeUnfreeze";
	
	/**
	 * 跳转地址
	 */
	public static String url = IConstants.WEB_URL;
}
