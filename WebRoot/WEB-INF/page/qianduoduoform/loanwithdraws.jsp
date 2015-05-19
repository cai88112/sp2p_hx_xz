<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>网贷提现测试 - 乾多多</title>
</head>
<body>
	<form id="form1" name="form1" action="${SubmitURL}" method="post">
		<input id="WithdrawMoneymoremore" name="WithdrawMoneymoremore" value="${loanwithdrawMap.withdrawMoneymoremore}" type="hidden" />
		<br/>
		<input id="PlatformMoneymoremore" name="PlatformMoneymoremore" value="${loanwithdrawMap.platformMoneymoremore}" type="hidden" />
		<br/>
		<input id="OrderNo" name="OrderNo" value="${loanwithdrawMap.orderNo}" type="hidden" />
		<br/>
		<input id="FeePercent" name="FeePercent" value="${loanwithdrawMap.feePercent}" type="hidden" />
		<br/>
		<input id="FeeMax" name="FeeMax" value="${loanwithdrawMap.feeMax}" type="hidden" />
		<br/>
		<input id="FeeRate" name="FeeRate" value="${loanwithdrawMap.feeRate}" type="hidden" />
		<br/>
		<input id="Amount" name="Amount" value="${loanwithdrawMap.amount}" type="hidden" />
		<br/>
		<input id="CardNo" name="CardNo" value="${loanwithdrawMap.cardNo}" type="hidden" />
		<br/>
		<input id="CardType" name="CardType" value="${loanwithdrawMap.cardType}" type="hidden" />
		<br/>
		<input id="BankCode" name="BankCode" value="${loanwithdrawMap.bankCode}" type="hidden" />
		<br/>
		<input id="BranchBankName" name="BranchBankName" value="${loanwithdrawMap.branchBankName}" type="hidden" />
		<br/>
		<input id="Province" name="Province" value="${loanwithdrawMap.province}" type="hidden" />
		<br/>
		<input id="City" name="City" value="${loanwithdrawMap.city}" type="hidden" />
		<br/>
		<input id="RandomTimeStamp" name="RandomTimeStamp" value="${loanwithdrawMap.randomTimeStamp}" type="hidden" />
		<br/>
		<input id="Remark1" name="Remark1" value="${loanwithdrawMap.remark1}" type="hidden" />
		<br/>
		<input id="Remark2" name="Remark2" value="${loanwithdrawMap.remark2}" type="hidden" />
		<br/>
		<input id="Remark3" name="Remark3" value="${loanwithdrawMap.remark3}" type="hidden" />
		<br/>
		<input id="ReturnURL" name="ReturnURL" value="${loanwithdrawMap.returnURL}" type="hidden" />
		<br/>
		<input id="NotifyURL" name="NotifyURL" value="${loanwithdrawMap.notifyURL}" type="hidden" />
		<br/>
		<input id="SignInfo" name="SignInfo" value="${loanwithdrawMap.signInfo}" type="hidden" />
	</form>
</body>
<script type="text/javascript">
	$(function(){
				document.form1.submit();
			});
</script>
</html>
