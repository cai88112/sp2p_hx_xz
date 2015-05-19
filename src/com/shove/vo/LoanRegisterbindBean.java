package com.shove.vo;

/**
 * 开户接口参数.
 * 
 * @ClassName: LoanRegisterbindBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月6日 下午3:57:27
 *
 */
public class LoanRegisterbindBean {
	private String registerType;
	private String accountType;
	private String mobile;
	private String email;
	private String realName;
	private String identificationNo;
	private String image1;
	private String image2;
	private String loanPlatformAccount;
	private String platformMoneymoremore;
	private String randomTimeStamp;
	private String remark1;
	private String remark2;
	private String remark3;
	private String returnURL;
	private String notifyURL;
	private String signInfo;

	/**
	 * @return 注册类型
	 */
	public final String getRegisterType() {
		return registerType;
	}

	/**
	 * @param registerType
	 *            要设置的 注册类型
	 */
	public final void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	/**
	 * @return the 账户类型
	 */
	public final String getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType
	 *            要设置的 账户类型
	 */
	public final void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the 手机号
	 */
	public final String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            要设置的 手机号
	 */
	public final void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return 邮箱
	 */
	public final String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            要设置的 邮箱
	 */
	public final void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the 真实姓名
	 */
	public final String getRealName() {
		return realName;
	}

	/**
	 * @param realName
	 *            要设置的 真实姓名
	 */
	public final void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the 身份证号/营业执照号
	 */
	public final String getIdentificationNo() {
		return identificationNo;
	}

	/**
	 * @param identificationNo
	 *            要设置的 身份证号/营业执照号
	 */
	public final void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	/**
	 * @return the 身份证/营业执照正面
	 */
	public final String getImage1() {
		return image1;
	}

	/**
	 * @param image1
	 *            要设置的 身份证/营业执照正面
	 */
	public final void setImage1(String image1) {
		this.image1 = image1;
	}

	/**
	 * @return the 身份证/营业执照反面
	 */
	public final String getImage2() {
		return image2;
	}

	/**
	 * @param image2
	 *            要设置的 身份证/营业执照反面
	 */
	public final void setImage2(String image2) {
		this.image2 = image2;
	}

	/**
	 * @return the 用户在网贷平台的账号
	 */
	public final String getLoanPlatformAccount() {
		return loanPlatformAccount;
	}

	/**
	 * @param loanPlatformAccount
	 *            要设置的 用户在网贷平台的账号
	 */
	public final void setLoanPlatformAccount(String loanPlatformAccount) {
		this.loanPlatformAccount = loanPlatformAccount;
	}

	/**
	 * @return the 平台乾多多标识
	 */
	public final String getPlatformMoneymoremore() {
		return platformMoneymoremore;
	}

	/**
	 * @param platformMoneymoremore
	 *            要设置的 平台乾多多标识
	 */
	public final void setPlatformMoneymoremore(String platformMoneymoremore) {
		this.platformMoneymoremore = platformMoneymoremore;
	}

	/**
	 * @return the 随机时间戳
	 */
	public final String getRandomTimeStamp() {
		return randomTimeStamp;
	}

	/**
	 * @param randomTimeStamp
	 *            要设置的 随机时间戳
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
	 * @param remark1
	 *            要设置的 自定义备注
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
	 * @param remark2
	 *            要设置的 自定义备注
	 */
	public final void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	/**
	 * @return the 自定义备注
	 */
	public final String getRemark3() {
		return remark3;
	}

	/**
	 * @param remark3
	 *            要设置的 自定义备注
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
	 * @param returnURL
	 *            要设置的 页面返回网址
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
	 * @param notifyURL
	 *            要设置的 后台通知网址
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
	 * @param signInfo
	 *            要设置的 签名信息
	 */
	public final void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}
}
