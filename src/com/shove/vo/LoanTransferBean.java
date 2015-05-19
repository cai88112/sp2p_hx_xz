/**   
* @Title: LoanTransfer.java 
* @Package com.shove.vo 
* @Description: TODO(用一句话描述该文件做什么) 
* @author yinzisong 690713748@qq.com   
* @date 2015年1月7日 下午2:35:03 
* @version V1.0   
*/ 
package com.shove.vo;

/** 
 * 转账接口参数bean.
 * @ClassName: LoanTransfer 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author yinzisong 690713748@qq.com 
 * @date 2015年1月7日 下午2:35:03 
 *  
 */
public class LoanTransferBean {

	/*
	 * 转账列表 JSON对象列表.
	 */
	private String loanJsonList;
	/*
	 * 平台乾多多标识.
	 */
	private String platformMoneymoremore;
	/*
	 * 转账类型1.投标2.还款3.其他.
	 */
	private Integer transferAction;
	
	/*
	 * 操作类型1.手动转账2.自动转账.
	 */
	private Integer action;
	/*
	 * 转账方式 1.桥连2.直连.
	 */
	private Integer transferType;
	/*
	 * 通过是否需要审核  空.需要审核1.自动通过.
	 */
	private String needAudit;
	/*
	 * 随机时间戳  启用防抵赖时必填.
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

	/**
	 * @return the 转账列表
	 */
	public final String getLoanJsonList() {
		return loanJsonList;
	}

	/** 
	 * @param loanJsonList 要设置的 转账列表 
	 */
	public final void setLoanJsonList(String loanJsonList) {
		this.loanJsonList = loanJsonList;
	}

	/**
	 * @return the 平台乾多多标识
	 */
	public final String getPlatformMoneymoremore() {
		return platformMoneymoremore;
	}

	/** 
	 * @param platformMoneymoremore 要设置的 平台乾多多标识 
	 */
	public final void setPlatformMoneymoremore(String platformMoneymoremore) {
		this.platformMoneymoremore = platformMoneymoremore;
	}

	/**
	 * @return the 转账类型
	 */
	public final Integer getTransferAction() {
		return transferAction;
	}

	/** 
	 * @param transferAction 要设置的 转账类型 
	 */
	public final void setTransferAction(Integer transferAction) {
		this.transferAction = transferAction;
	}

	/**
	 * @return the 操作类型
	 */
	public final Integer getAction() {
		return action;
	}

	/** 
	 * @param action 要设置的 action 
	 */
	public final void setAction(Integer action) {
		this.action = action;
	}

	/**
	 * @return the 转账方式
	 */
	public final Integer getTransferType() {
		return transferType;
	}

	/** 
	 * @param transferType 要设置的 转账方式 
	 */
	public final void setTransferType(Integer transferType) {
		this.transferType = transferType;
	}

	/**
	 * @return the 通过是否需要审核
	 */
	public final String getNeedAudit() {
		return needAudit;
	}

	/** 
	 * @param needAudit 要设置的 通过是否需要审核 
	 */
	public final void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}

	/**
	 * @return the 随机时间戳
	 */
	public final String getRandomTimeStamp() {
		return randomTimeStamp;
	}

	/** 
	 * @param randomTimeStamp 要设置的 随机时间戳 
	 */
	public final void setRandomTimeStamp(String randomTimeStamp) {
		this.randomTimeStamp = randomTimeStamp;
	}

	/**
	 * @return the 自定义备注
	 */
	public final String getRemark1() {
		return remark1;
	}

	/** 
	 * @param remark1 要设置的 自定义备注 
	 */
	public final void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	/**
	 * @return the 自定义备注
	 */
	public final String getRemark2() {
		return remark2;
	}

	/** 
	 * @param remark2 要设置的 自定义备注 
	 */
	public final void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	/**
	 * @return the remark3
	 */
	public final String getRemark3() {
		return remark3;
	}

	/** 
	 * @param remark3 要设置的 自定义备注 
	 */
	public final void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	/**
	 * @return the 页面返回网址
	 */
	public final String getReturnURL() {
		return returnURL;
	}

	/** 
	 * @param 页面返回网址 要设置的 页面返回网址 
	 */
	public final void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	/**
	 * @return the 后台通知网址
	 */
	public final String getNotifyURL() {
		return notifyURL;
	}

	/** 
	 * @param notifyURL 要设置的 后台通知网址 
	 */
	public final void setNotifyURL(String notifyURL) {
		this.notifyURL = notifyURL;
	}

	/**
	 * @return the 签名信息
	 */
	public final String getSignInfo() {
		return signInfo;
	}

	/** 
	 * @param signInfo 要设置的 签名信息 
	 */
	public final void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}
}
