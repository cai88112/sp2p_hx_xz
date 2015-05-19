<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>网贷审核 - 乾多多</title>

</head>
<body>
	<form id="form1" name="form1" action="${SubmitURL}" method="post" target="_top">
		<input id="LoanNoList" name="LoanNoList" value="${loantransferauditMap.loanNoList}" type="hidden" />
		<br/>
		<input id="PlatformMoneymoremore" name="PlatformMoneymoremore" value="${loantransferauditMap.platformMoneymoremore}" type="hidden" />
		<br/>
		<input id="AuditType" name="AuditType" value="${loantransferauditMap.auditType}" type="hidden" />
		<br/>
		<input id="RandomTimeStamp" name="RandomTimeStamp" value="${loantransferauditMap.randomTimeStamp}" type="hidden" />
		<br/>
		<input id="Remark1" name="Remark1" value="${loantransferauditMap.remark1}" type="hidden" />
		<br/>
		<input id="Remark2" name="Remark2" value="${loantransferauditMap.remark2}" type="hidden" />
		<br/>
		<input id="Remark3" name="Remark3" value="${loantransferauditMap.remark3}" type="hidden" />
		<br/>
		<input id="ReturnURL" name="ReturnURL" value="${loantransferauditMap.returnURL}" type="hidden" />
		<br/>
		<input id="NotifyURL" name="NotifyURL" value="${loantransferauditMap.notifyURL}" type="hidden" />
		<br/>
		<input id="SignInfo" name="SignInfo" value="${loantransferauditMap.signInfo}" type="hidden" />
	</form>
</body>
<script type="text/javascript">
$(function(){
    document.form1.submit();
	});
</script>
</html>
