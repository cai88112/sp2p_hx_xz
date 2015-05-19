<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>网贷转账 - 乾多多</title>
</head>
<body>
	<form id="form1" name="form1" action="${loanTransferMap.SubmitURL}" method="post">
		<input id="LoanJsonList" name="LoanJsonList" value="${loanTransferMap.loanJsonList}" type="hidden" />
		<br/>
		<input id="PlatformMoneymoremore" name="PlatformMoneymoremore" value="${loanTransferMap.platformMoneymoremore}" type="hidden" />
		<br/>
		<input id="TransferAction" name="TransferAction" value="${loanTransferMap.transferAction}" type="hidden" />
		<br/>
		<input id="Action" name="Action" value="${loanTransferMap.action}" type="hidden" />
		<br/>
		<input id="TransferType" name="TransferType" value="${loanTransferMap.transferType}" type="hidden" />
		<br/>
		<input id="NeedAudit" name="NeedAudit" value="${loanTransferMap.needAudit}" type="hidden" />
		<br/>
		<input id="RandomTimeStamp" name="RandomTimeStamp" value="${loanTransferMap.randomTimeStamp}" type="hidden" />
		<br/>
		<input id="Remark1" name="Remark1" value="${loanTransferMap.remark1}" type="hidden" />
		<br/>
		<input id="Remark2" name="Remark2" value="${loanTransferMap.remark2}" type="hidden" />
		<br/>
		<input id="Remark3" name="Remark3" value="${loanTransferMap.remark3}" type="hidden" />
		<br/>
		<input id="ReturnURL" name="ReturnURL" value="${loanTransferMap.returnURL}" type="hidden" />
		<br/>
		<input id="NotifyURL" name="NotifyURL" value="${loanTransferMap.notifyURL}" type="hidden" />
		<br/>
		<input id="SignInfo" name="SignInfo" value="${loanTransferMap.signInfo}" type="hidden" />
	</form>
</body>
<script type="text/javascript">
	$(function(){document.form1.submit();});
</script>
</html>
