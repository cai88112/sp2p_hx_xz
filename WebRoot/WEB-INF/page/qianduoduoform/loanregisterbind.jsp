<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="Pragma" content="no-cache" /> 
<meta http-equiv="Cache-Control" content="no-cache" /> 
<meta http-equiv="Expires" content="0" /> 
<title>网贷注册绑定 - 乾多多</title>

</head>
<body>
	<form id="form1" name="form1" action="${SubmitURL}" method="post">
		<input id="RegisterType" name="RegisterType" value="${loanRegisterbindMap.RegisterType}" type="hidden" />
		<br/>
		<input id="AccountType" name="AccountType" value="${loanRegisterbindMap.AccountType}" type="hidden" />
		<br/>
		<input id="Mobile" name="Mobile" value="${loanRegisterbindMap.Mobile}" type="hidden" />
		<br/>
		<input id="Email" name="Email" value="${loanRegisterbindMap.Email}" type="hidden" />
		<br/>
		<input id="RealName" name="RealName" value="${loanRegisterbindMap.RealName}" type="hidden" />
		<br/>
		<input id="IdentificationNo" name="IdentificationNo" value="${loanRegisterbindMap.IdentificationNo}" type="hidden" />
		<br/>
		<input id="Image1" name="Image1" value="${loanRegisterbindMap.Image1}" type="hidden" />
		<br/>
		<input id="Image2" name="Image2" value="${loanRegisterbindMap.Image2}" type="hidden" />
		<br/>
		<input id="LoanPlatformAccount" name="LoanPlatformAccount" value="${loanRegisterbindMap.LoanPlatformAccount}" type="hidden" />
		<br/>
		<input id="PlatformMoneymoremore" name="PlatformMoneymoremore" value="${loanRegisterbindMap.PlatformMoneymoremore}" type="hidden" />
		<br/>
		<input id="RandomTimeStamp" name="RandomTimeStamp" value="${loanRegisterbindMap.RandomTimeStamp}" type="hidden" />
		<br/>
		<input id="Remark1" name="Remark1" value="${loanRegisterbindMap.Remark1}" type="hidden" />
		<br/>
		<input id="Remark2" name="Remark2" value="${loanRegisterbindMap.Remark2}" type="hidden" />
		<br/>
		<input id="Remark3" name="Remark3" value="${loanRegisterbindMap.Remark3}" type="hidden" />
		<br/>
		<input id="ReturnURL" name="ReturnURL" value="${loanRegisterbindMap.ReturnURL}" type="hidden" />
		<br/>
		<input id="NotifyURL" name="NotifyURL" value="${loanRegisterbindMap.NotifyURL}" type="hidden" />
		<br/>
		<input id="SignInfo" name="SignInfo" value="${loanRegisterbindMap.SignInfo}" type="hidden" />
	</form>
</body>
<script type="text/javascript">
	$(function(){document.form1.submit();});
</script>
</html>
