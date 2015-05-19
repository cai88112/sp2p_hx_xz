<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
	<head>
		<title>财务管理-充值管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
		<script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
			$(function(){
			   var adminId = '${sessionScope.admin.id }';
			   $("#adminId").attr("value",adminId);
			   $("#pageId").attr("value","userlogin");
			   $("#adminId").attr("name","paramMap.adminId");
			   $("#pageId").attr("name","paramMap.pageId");
			   
			   //提交表单
				$("#btn_save").click(function(){
					$(this).hide();
					$("#addBackWithdraw").submit();
					return false;
				});
			});
			
			
			function cancel(){
			  $("#tb_remark").attr("value","");
			  $("#tb_money").attr("value","");
			  $("#remark_tip").html("");
			  $("#money_tip").html("");
			}
			
			function switchCode(){
				var timenow = new Date();
				$("#codeNum").attr("src","admin/imageCode.do?pageId=userlogin&d="+timenow);
			}
									
		</script>
	</head>
	<body>
	<form id="addBackWithdraw" name="addBackWithdraw" action="addBackWithdraw.do" method="post">
	
		<div id="right">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="120" height="28" class="xxk_all_a">
								<a href="queryUserCashListInit.do">充值管理</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td width="120" class="xxk_all_a">
								<a href="queryBackCashDetailsInit.do">充值明细</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td width="120" class="main_alll_h2">
								<a href="">充值扣费</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
					<div
						style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: 1120px; padding-top: 10px; background-color: #fff;">
						<table width="100%" border="0" cellspacing="1" cellpadding="3">
							<tr>
								<td style="width: 120px; height: 35px;" align="right"
									class="blue12">
									用户名：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_userName" name="paramMap.userName"
										cssClass="in_text_2" cssStyle="width: 150px;height:20px;" theme="simple" readonly="true" ></s:textfield>
									<s:textfield id="tb_userId" name="paramMap.userId"
										cssClass="in_text_2" cssStyle="width: 150px;height:20px;display:none" 
										theme="simple" readonly="true" ></s:textfield>
										 	 
									</td>
							</tr>
							
							<tr>
								<td style="width: 120px; height: 35px;" align="right" class="blue12">
									扣除金额：
								</td>
								<td align="left" class="f66">
								<s:textfield id="tb_money" name="paramMap.dealMoney"
										cssClass="in_text_2" cssStyle="width: 150px;height:20px;" theme="simple" ></s:textfield>
										<span id="money_tip" class="require-field">*<s:fielderror fieldName="paramMap['dealMoney']"></s:fielderror></span>
									</td>
							</tr>
							<tr>
								 <td  style="width: 120px; height: 35px;" align="right" class="blue12">
								    扣除备注：
										</td>
										<td>
										&nbsp;
										</td>
								 
							</tr>
							<tr>
							<td colspan="2" class="f66">
								 <s:textarea id="tb_remark" cssStyle="margin-left:80px;" name="paramMap.remark" value="" cols="30" rows="5"></s:textarea>
										<span id="remark_tip" class="require-field">*<s:fielderror fieldName="paramMap['remark']"></s:fielderror></span>
								 </td>
							</tr>
							<tr>
								 <td  style="width: 120px; height: 35px;" align="right" class="blue12">
								    验证码：
										</td>
										<td colspan="2" class="f66">
										<s:textfield id="tb_code" name="paramMap.code"
										cssClass="in_text_2" cssStyle="width: 150px;height:20px;" theme="simple" ></s:textfield>
										 
										 <img src="admin/imageCode.do?pageId=userlogin" title="点击更换验证码"
											style="cursor: pointer;" id="codeNum" width="46" height="18"
											onclick="javascript:switchCode()" />
											
										<span id="code_tip" class="require-field">*<s:fielderror fieldName="paramMap['code']"></s:fielderror></span>
										
										<!-- <s:hidden id="adminId" name="paramMap.adminId"></s:hidden>
										<s:hidden id="pageId" name="paramMap.pageId"></s:hidden>-->
										
										<s:textfield id="adminId" name="paramMap.adminId" 
										cssClass="in_text_2" cssStyle="width: 150px;height:20px;display:none" theme="simple" ></s:textfield>
										<s:textfield id="pageId" name="paramMap.pageId" 
										cssClass="in_text_2" cssStyle="width: 150px;height:20px;display:none" theme="simple" ></s:textfield>
										</td>
								 
							</tr>
							
							<tr>
							<td colspan="2">
									<button id="btn_save" 
										style="background-image: url('../images/admin/btn_queding.jpg'); width: 70px; border: 0; height: 26px"></button>
									&nbsp;
									
									<button id="btn_cancel" onclick="cancel();"
										style="background-image: url('../images/admin/btn_reback.jpg'); width: 70px; border: 0; height: 26px"></button>
									&nbsp;
							</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			</div>
			</form>
	</body>
</html>
