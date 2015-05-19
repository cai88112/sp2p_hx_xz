/**   
* @Title: LoanwithdrawBean.java 
* @Package com.shove.vo 
* @Description: TODO(用一句话描述该文件做什么) 
* @author yinzisong 690713748@qq.com   
* @date 2015年1月16日 下午3:11:39 
* @version V1.0   
*/ 
package com.shove.vo;

/** 
 * 提现接口参数.
 * @ClassName: LoanwithdrawBean 
 * @author yinzisong 690713748@qq.com 
 * @date 2015年1月16日 下午3:11:39 
 *  
 */
public class LoanwithdrawBean {

	/*
	 * 提现人乾多多标识.
	 */
	private String withdrawMoneymoremore;
	
	/*
	 * 平台乾多多标识.
	 */
	private String platformMoneymoremore;
	
	/*
	 * 平台的提现订单号.
	 */
	private String orderNo;
	
	/*
	 * 金额.
	 */
	private String amount;
	
	
	/*
	 * 平台承担的手续费比例.
	 */
	private String feePercent;
	/*
	 * 用户承担的最高手续费.
	 */
	private String feeMax;
	
	/*
	 * 上浮费率.
	 */
	private String feeRate;
	
	/*
	 * 银行卡号.
	 */
	private String cardNo;
	
	/*
	 * 银行卡类型.
	 */
	private String cardType;
	
	/*
	 * 银行代码.
	 */
	private String bankCode;
	
	/*
	 * 开户行支行名称.
	 */
	private String branchBankName;
	
	/*
	 * 开户行省份.
	 */
	private String province;
	
	/*
	 * 开户行城市.
	 */
	private String city;
	/*
	 * 随机时间戳.
	 */
	private String randomTimeStamp;
	/*
	 * 自定义备注.
	 */
	private String remark1;
	/*
	 * 自定义备注.
	 */
	private String remark2;
	/*
	 * 自定义备注.
	 */
	private String remark3;
	
	/*
	 * 页面返回网址.
	 */
	private String returnURL;
	
	/*
	 * 后台通知网址.
	 */
	private String notifyURL;
	
	/*
	 * 签名信息.
	 */
	private String signInfo;

	public final String getWithdrawMoneymoremore() {
		return withdrawMoneymoremore;
	}

	public final void setWithdrawMoneymoremore(String withdrawMoneymoremore) {
		this.withdrawMoneymoremore = withdrawMoneymoremore;
	}

	public final String getPlatformMoneymoremore() {
		return platformMoneymoremore;
	}

	public final void setPlatformMoneymoremore(String platformMoneymoremore) {
		this.platformMoneymoremore = platformMoneymoremore;
	}

	public final String getOrderNo() {
		return orderNo;
	}

	public final void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public final String getAmount() {
		return amount;
	}

	public final void setAmount(String amount) {
		this.amount = amount;
	}

	public final String getFeePercent() {
		return feePercent;
	}

	public final void setFeePercent(String feePercent) {
		this.feePercent = feePercent;
	}

	public final String getFeeMax() {
		return feeMax;
	}

	public final void setFeeMax(String feeMax) {
		this.feeMax = feeMax;
	}

	public final String getFeeRate() {
		return feeRate;
	}

	public final void setFeeRate(String feeRate) {
		this.feeRate = feeRate;
	}

	public final String getCardNo() {
		return cardNo;
	}

	public final void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public final String getCardType() {
		return cardType;
	}

	public final void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public final String getBankCode() {
		return bankCode;
	}

	public final void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public final String getBranchBankName() {
		return branchBankName;
	}

	public final void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}

	public final String getProvince() {
		return province;
	}

	public final void setProvince(String province) {
		this.province = province;
	}

	public final String getCity() {
		return city;
	}

	public final void setCity(String city) {
		this.city = city;
	}

	public final String getRandomTimeStamp() {
		return randomTimeStamp;
	}

	public final void setRandomTimeStamp(String randomTimeStamp) {
		this.randomTimeStamp = randomTimeStamp;
	}

	public final String getRemark1() {
		return remark1;
	}

	public final void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public final String getRemark2() {
		return remark2;
	}

	public final void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public final String getRemark3() {
		return remark3;
	}

	public final void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public final String getReturnURL() {
		return returnURL;
	}

	public final void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public final String getNotifyURL() {
		return notifyURL;
	}

	public final void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	public final String getSignInfo() {
		return signInfo;
	}

	public final void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}
}
