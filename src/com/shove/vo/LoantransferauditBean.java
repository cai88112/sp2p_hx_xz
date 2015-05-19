/**   
 * @Title: LoantransferauditBean.java 
 * @Package com.shove.vo 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月12日 下午4:01:07 
 * @version V1.0   
 */
package com.shove.vo;

/**
 * 审核接口参数.
 * @ClassName: LoantransferauditBean
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月12日 下午4:01:07
 * 
 */
public class LoantransferauditBean {
	/*
	 * 乾多多流水号列表.LN12345,LN54321将所有流水号用英文逗号(,)连成一个字符串；一次最多操作200笔
	 */
	private String LoanNoList;

	/*
	 * 平台乾多多标识.
	 */
	private String PlatformMoneymoremore;
	
	/*
	 * 审核类型.1.通过2.退回3.二次分配同意4.二次分配不同意5.提现通过6.提现退回
	 */
	private String AuditType;
	/*
	 * 随机时间戳.
	 */
	private String RandomTimeStamp;
	/*
	 * 自定义备注.
	 */
	private String Remark1;
	/*
	 * 自定义备注.
	 */
	private String Remark2;
	/*
	 * 自定义备注.
	 */
	private String Remark3;
	/*
	 * 页面返回网址.
	 */
	private String ReturnURL;
	/*
	 * 后台通知网址.
	 */
	private String NotifyURL;
	/*
	 * 签名信息.
	 */
	private String SignInfo;
	public final String getLoanNoList() {
		return LoanNoList;
	}
	public final void setLoanNoList(String loanNoList) {
		LoanNoList = loanNoList;
	}
	public final String getPlatformMoneymoremore() {
		return PlatformMoneymoremore;
	}
	public final void setPlatformMoneymoremore(String platformMoneymoremore) {
		PlatformMoneymoremore = platformMoneymoremore;
	}
	public final String getAuditType() {
		return AuditType;
	}
	public final void setAuditType(String auditType) {
		AuditType = auditType;
	}
	public final String getRandomTimeStamp() {
		return RandomTimeStamp;
	}
	public final void setRandomTimeStamp(String randomTimeStamp) {
		RandomTimeStamp = randomTimeStamp;
	}
	public final String getRemark1() {
		return Remark1;
	}
	public final void setRemark1(String remark1) {
		Remark1 = remark1;
	}
	public final String getRemark2() {
		return Remark2;
	}
	public final void setRemark2(String remark2) {
		Remark2 = remark2;
	}
	public final String getRemark3() {
		return Remark3;
	}
	public final void setRemark3(String remark3) {
		Remark3 = remark3;
	}
	public final String getReturnURL() {
		return ReturnURL;
	}
	public final void setReturnURL(String returnURL) {
		ReturnURL = returnURL;
	}
	public final String getNotifyURL() {
		return NotifyURL;
	}
	public final void setNotifyURL(String notifyURL) {
		NotifyURL = notifyURL;
	}
	public final String getSignInfo() {
		return SignInfo;
	}
	public final void setSignInfo(String signInfo) {
		SignInfo = signInfo;
	}
}
