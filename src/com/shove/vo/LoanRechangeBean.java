package com.shove.vo;

/**
 * 充值接口参数.
 * 
 * @ClassName: LoanRechangeBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月7日 下午9:25:01
 *
 */
public class LoanRechangeBean {
	/*
	 * 充值人小Ws.
	 */
	private String rechargeMoneymoremore;

	/*
	 * 平台乾多多标识.
	 */
	private String platformMoneymoremore;

	/*
	 * 平台的充值订单号.
	 */
	private String orderNo;
	/*
	 * 金额.
	 */
	private String amount;

	/*
	 * 金额.
	 */
	private String rechargeType;

	/*
	 * 手续费类型.
	 */
	private String feeType;

	/*
	 * 银行卡号.
	 */
	private String cardNo;

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
	public final String getRechargeMoneymoremore() {
		return rechargeMoneymoremore;
	}
	public final void setRechargeMoneymoremore(String rechargeMoneymoremore) {
		this.rechargeMoneymoremore = rechargeMoneymoremore;
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
	public final String getCardNo() {
		return cardNo;
	}
	public final void setCardNo(String cardNo) {
		this.cardNo = cardNo;
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
	public final String getAmount() {
		return amount;
	}
	public final void setAmount(String amount) {
		this.amount = amount;
	}
	public final String getRechargeType() {
		return rechargeType;
	}
	public final void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	public final String getFeeType() {
		return feeType;
	}
	public final void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	
}
