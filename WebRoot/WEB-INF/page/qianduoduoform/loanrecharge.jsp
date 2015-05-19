<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>网贷充值- 乾多多</title>

</head>
<body>
	<form id="form1" name="form1" action="${loanRechargBeanMap.SubmitURL}" method="post">
		<input id="RechargeMoneymoremore" name="RechargeMoneymoremore" value="${loanRechargBeanMap.rechargeMoneymoremore}" type="hidden" />
		<br/>
		<input id="PlatformMoneymoremore" name="PlatformMoneymoremore" value="${loanRechargBeanMap.platformMoneymoremore}" type="hidden" />
		<br/>
		<input id="OrderNo" name="OrderNo" value="${loanRechargBeanMap.orderNo}" type="hidden" />
		<br/>
		<input id="Amount" name="Amount" value="${loanRechargBeanMap.amount}" type="hidden" />
		<br/>
		<input id="RechargeType" name="RechargeType" value="${loanRechargBeanMap.rechargeType}" type="hidden" />
		<br/>
		<input id="FeeType" name="FeeType" value="${loanRechargBeanMap.feeType}" type="hidden" />
		<br/>
		<input id="CardNo" name="CardNo" value="${loanRechargBeanMap.cardNo}" type="hidden" />
		<br/>
		<input id="RandomTimeStamp" name="RandomTimeStamp" value="${loanRechargBeanMap.randomTimeStamp}" type="hidden" />
		<br/>
		<input id="Remark1" name="Remark1" value="${loanRechargBeanMap.remark1}" type="hidden" />
		<br/>
		<input id="Remark2" name="Remark2" value="${loanRechargBeanMap.remark2}" type="hidden" />
		<br/>
		<input id="Remark3" name="Remark3" value="${loanRechargBeanMap.remark3}" type="hidden" />
		<br/>
		<input id="ReturnURL" name="ReturnURL" value="${loanRechargBeanMap.returnURL}" type="hidden" />
		<br/>
		<input id="NotifyURL" name="NotifyURL" value="${loanRechargBeanMap.notifyURL}" type="hidden" />
		<br/>
		<input id="SignInfo" name="SignInfo" value="${loanRechargBeanMap.signInfo}" type="hidden" />
	</form>
</body>
<script type="text/javascript">

	$(function(){
		
		document.form1.submit();});
</script>
</html>
