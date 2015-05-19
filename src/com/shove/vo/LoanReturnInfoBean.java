package com.shove.vo;

/**
 * 网贷平台 转账返回 操作信息
 * 
 * @author
 * 
 */
public class LoanReturnInfoBean
{
	/*
	 * 付款人标识
	 */
	private String LoanOutMoneymoremore;
	
	/*
	 * 收款人标识
	 */
	private String LoanInMoneymoremore;
	
	/*
	 * 乾多多流水号
	 */
	private String LoanNo;
	
	/*
	 * 平台订单号
	 */
	private String OrderNo;
	
	/*
	 * 平台标号
	 */
	private String BatchNo;
	
	/*
	 * 流转标标号
	 */
	private String ExchangeBatchNo = "";
	
	/**
	 * 垫资标号
	 */
	private String AdvanceBatchNo = "";
	
	/*
	 * 金额
	 */
	private String Amount;
	
	/*
	 * 用途
	 */
	private String TransferName;
	
	/*
	 * 备注
	 */
	private String Remark;
	
	/*
	 * 二次分配列表
	 */
	private String SecondaryJsonList = "";
	
	public String getLoanOutMoneymoremore()
	{
		return LoanOutMoneymoremore;
	}
	
	public void setLoanOutMoneymoremore(String loanOutMoneymoremore)
	{
		LoanOutMoneymoremore = loanOutMoneymoremore;
	}
	
	public String getLoanInMoneymoremore()
	{
		return LoanInMoneymoremore;
	}
	
	public void setLoanInMoneymoremore(String loanInMoneymoremore)
	{
		LoanInMoneymoremore = loanInMoneymoremore;
	}
	
	public String getOrderNo()
	{
		return OrderNo;
	}
	
	public void setOrderNo(String orderNo)
	{
		OrderNo = orderNo;
	}
	
	public String getAmount()
	{
		return Amount;
	}
	
	public void setAmount(String amount)
	{
		Amount = amount;
	}
	
	public String getRemark()
	{
		return Remark;
	}
	
	public void setRemark(String remark)
	{
		Remark = remark;
	}
	
	public String getBatchNo()
	{
		return BatchNo;
	}
	
	public void setBatchNo(String batchNo)
	{
		BatchNo = batchNo;
	}
	
	public String getTransferName()
	{
		return TransferName;
	}
	
	public void setTransferName(String transferName)
	{
		TransferName = transferName;
	}
	
	public String getLoanNo()
	{
		return LoanNo;
	}
	
	public void setLoanNo(String loanNo)
	{
		LoanNo = loanNo;
	}
	
	public String getSecondaryJsonList()
	{
		return SecondaryJsonList;
	}
	
	public void setSecondaryJsonList(String secondaryJsonList)
	{
		SecondaryJsonList = secondaryJsonList;
	}
	
	public String getExchangeBatchNo()
	{
		return ExchangeBatchNo;
	}
	
	public void setExchangeBatchNo(String exchangeBatchNo)
	{
		ExchangeBatchNo = exchangeBatchNo;
	}
	
	public String getAdvanceBatchNo()
	{
		return AdvanceBatchNo;
	}
	
	public void setAdvanceBatchNo(String advanceBatchNo)
	{
		AdvanceBatchNo = advanceBatchNo;
	}
	
}
