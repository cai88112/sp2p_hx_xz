<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
	<head>
		<title>财务管理-用户银行卡管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		
	</head>
	<body>
		<div id="right"
			style="background-image: url(images/admin/right_bg_top.jpg); background-position: top; background-repeat: repeat-x;">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					
					<div
						style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: 1120px; padding-top: 10px; background-color: #fff;">
						<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
							width="100%" border="0">
							<tbody>
							
						         <tr>
									<td class="f66" align="left" width="25%" height="36px">
										<strong>用户名&nbsp;：&nbsp;&nbsp;
										<s:property  value="paramMap.username"></s:property></strong>
									</td>
									
								</tr>
								
								<tr>
									<td class="f66" align="left" width="25%" height="36px">
										<strong>真实姓名：&nbsp;&nbsp;
										<s:property  value="paramMap.realName"></s:property></strong>
									</td>
								</tr>
								
								<tr>
									<td class="f66" align="left" width="25%" height="36px">
										<strong>手机号码：&nbsp;&nbsp;
										  <s:property  value="paramMap.mobilePhone"></s:property>
										</strong>
									</td>
								</tr>
								
								<tr>
									<td class="f66" align="left" width="25%" height="36px">
										<strong>身份证&nbsp;：&nbsp;&nbsp;
										<s:property  value="paramMap.idNo"></s:property></strong>
									</td>
								</tr>
								<tr>
								<td>&nbsp;</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
										开户银行：&nbsp;&nbsp;
										<s:property  value="paramMap.bankName"></s:property>
									</td>
								</tr>
								
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
										支&nbsp;&nbsp;行：&nbsp;&nbsp;
										<s:property  value="paramMap.branchBankName"></s:property>
									</td>
								</tr>
								
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
										银行卡号：&nbsp;&nbsp;
										<s:property  value="paramMap.cardNo"></s:property>
									</td>
								</tr>
								     
							</tbody>
						</table>
					</div>
				</div>
			</div>
			</div>
	</body>
</html>
