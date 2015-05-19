package com.sp2p.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class User implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private	String userName;
	private String password;
	private int enable ;
	private Date lastTime;
	private String lastIP;
	private BigDecimal balances;
	private int vipStatus;
	private String email;
	private String servicePerson;
	private String realName;
	private int authStep;//用户基本资料验证
    private String kefuname;
	private String headImage ;
	private String personalHead;
	private String creditLimit;
	private String imgHead;
	private int kefuid;
	private int virtual; // 1为虚拟用户
	private String encodeP; // 加密后的密码,用来登录论坛
	private String portMerBillNo;//接口开户ID
	private String pIpsBillNo;//签署协议号
	
	
	public int getVirtual() {
		return virtual;
	}
	public void setVirtual(int virtual) {
		this.virtual = virtual;
	}
	public int getKefuid() {
		return kefuid;
	}
	public void setKefuid(int kefuid) {
		this.kefuid = kefuid;
	}
	public String getHeadImage() {
		return headImage;
	}
	public void setHeadImage(String headImage) {
		this.headImage = headImage;
	}
	public int getAuthStep() {
		return authStep;
	}
	public void setAuthStep(int authStep) {
		this.authStep = authStep;
	}
	public String getServicePerson() {
		return servicePerson;
	}
	public void setServicePerson(String servicePerson) {
		this.servicePerson = servicePerson;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getVipStatus() {
		return vipStatus;
	}
	public void setVipStatus(int vipStatus) {
		this.vipStatus = vipStatus;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getEnable() {
		return enable;
	}
	public void setEnable(int enable) {
		this.enable = enable;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public String getLastIP() {
		return lastIP;
	}
	public void setLastIP(String lastIP) {
		this.lastIP = lastIP;
	}
	public BigDecimal getBalances() {
		return balances;
	}
	public void setBalances(BigDecimal balances) {
		this.balances = balances;
	}
	public String getPersonalHead() {
		return personalHead;
	}
	public void setPersonalHead(String personalHead) {
		this.personalHead = personalHead;
	}
	
	
	public String getCreditLimit() {
		return creditLimit;
	}
	public void setCreditLimit(String creditLimit) {
		this.creditLimit = creditLimit;
	}
	public String getImgHead() {
		return imgHead;
	}
	public void setImgHead(String imgHead) {
		this.imgHead = imgHead;
	}
	public String getKefuname() {
		return kefuname;
	}
	public void setKefuname(String kefuname) {
		this.kefuname = kefuname;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getEncodeP() {
		return encodeP;
	}
	public void setEncodeP(String encodeP) {
		this.encodeP = encodeP;
	}
	public String getPortMerBillNo() {
		return portMerBillNo;
	}
	public void setPortMerBillNo(String portMerBillNo) {
		this.portMerBillNo = portMerBillNo;
	}
	public String getpIpsBillNo() {
		return pIpsBillNo;
	}
	public void setpIpsBillNo(String pIpsBillNo) {
		this.pIpsBillNo = pIpsBillNo;
	}
	
	
	
}
