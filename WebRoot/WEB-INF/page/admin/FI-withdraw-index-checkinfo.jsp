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
		<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
		<script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
			$(function(){
			
			  $("#tb_poundage").attr("value",${poundage});
			  $("#tb_realMoney").attr("value",${realMoney});
			  
			  $("#tb_poundage").blur(function(){
			     var aa = $("#tb_poundage").val();
			     var bb = ${sum } - aa;
			     $("#tb_realMoney").attr("value",bb);
			  });
			  
			  $("#tb_realMoney").blur(function(){
			     var aa = $("#tb_realMoney").val();
			     var bb = ${sum } - aa;
			     $("#tb_poundage").attr("value",bb);
			  });
			  
			});
			
			
			function changeTxtStatus(){
			   if($("#manual").attr("checked")){//更改文本框的状态
   				 $("#tb_realMoney").removeAttr("readonly"); 
   				 $("#tb_poundage").removeAttr("readonly");  
   				 $("#tb_poundage").focus();
   			   }else{
   			     $("#tb_realMoney").attr("readonly","readonly"); 
   			     $("#tb_poundage").attr("readonly","readonly"); 
   			     $("#tb_realMoney").attr('value',${realMoney});
   			     $("#tb_poundage").attr('value',${poundage});
   			   }
			}
			
			function check(){//审核  
			 
			 if(isNaN($("#tb_realMoney").val()) || isNaN($("#tb_poundage").val())){
			   alert("请填写正确的数字");
			   return;
			 }
			 	
			  param['paramMap.sum'] = ${sum};
			  param['paramMap.poundage'] = $("#tb_poundage").val();
			  param['paramMap.money'] = $("#tb_realMoney").val();	 
			  param['paramMap.remark'] = $("#remark").val();
			  param['paramMap.wid'] = $("#wid").val();
			  param['paramMap.adminId'] = '${sessionScope.admin.id }';
			  param['paramMap.status'] = $("input[name='status']:checked").val();
			  param['paramMap.userId'] = $("#userId").val();
			  param['paramMap.check'] = 'true';
			  param['paramMap.oldStatus'] = '1';//审核状态
			  $.shovePost("updateWithdrawCheck.do",param,function(data){
			  
			     if(data.msg == 1){
			     	alert("操作成功");
			     	var para1 = {};
			     	window.parent.initListInfo(para1);
			        window.parent.close();
			        return;
			     }else{
			       	alert(data.msg);
			        return;
			     }
			     
			  });
			}
						
		</script>
	</head>
	<body>
		<div id="right"
			style="background-image: url(images/admin/right_bg_top.jpg); background-position: top; background-repeat: repeat-x;">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<strong>提现信息</strong>
					<div
						style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: 1120px; padding-top: 10px; background-color: #fff;">
						
						<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
							width="100%" border="0">
							
							<tbody>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
										用户名：&nbsp;&nbsp;
										${username }
										&nbsp;&nbsp;
										<s:hidden id="wid" name="#request.wid" />
										<s:hidden id="userId" name="#request.userId" />
								  </td>
								  <td>
								    &nbsp;
								  </td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									充值成功总额：${r_total }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									提现成功总额：${w_total }
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									投标成功总额：${real_Amount }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									提现上额限制：${withdraw_max }
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									可用余额：${usableSum }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									开户名：${realName }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									提现支行：${branchBankName }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									提现账号：${cardNo }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									提现总额：${sum }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									到账金额：${realMoney }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									手续费：${poundage }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									状态：${status }
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="30%" height="36px">
									添加时间/IP：
									${applyTime }/${ipAddress}
									<!-- <s:date name="paramMap.applyTime" format="yyyy-MM-dd HH:mm:ss" />-->
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
							</tbody>
						</table>
						
					</div>
					
					<strong>审核</strong>
					<div
					 style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: 1120px; padding-top: 10px; background-color: #fff;">
					<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
							width="100%" border="0">
					
					<tr>
						 <td class="f66" align="left" width="50%" height="36px">
						   状态：&nbsp;&nbsp;
						   <!-- <s:radio id="radio1" name="status" list="#{1:'等待审核',4:'审核通过',5:'审核不通过'}" value="1"></s:radio> -->
						   <s:radio id="radio1" name="status" list="#{4:'审核通过',5:'审核不通过'}" value="5"></s:radio>
						 </td>
					</tr>
					<tr>
					   <td class="f66" align="left" width="80%" height="36px">
					      到账金额：
					      <input style="width:100px;" id="tb_realMoney"  readonly="readonly" />
					      手续费：
					      <input style="width:100px;" id="tb_poundage"  readonly="readonly" />
					   &nbsp;&nbsp;
					   <input id="manual" class="helpId" type="checkbox"
									value="" name="cb_ids" onclick="changeTxtStatus();" />&nbsp;手动更改
					   </td>
					</tr>
					<tr>
					   <td class="f66" align="left" width="50%" height="36px">
					      备注：
					   </td>
					</tr>
					<tr>
					   <td class="f66" align="left" width="50%" height="36px">
					      <s:textarea id="remark" cssStyle="margin-left:80px;" 
					      name="paramMap.remark" value="" cols="30" rows="5"></s:textarea>
					   </td>
					</tr>
					</table>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 <button id="btnSave" style="background-image: url('../images/admin/btn_queding.jpg');width: 70px;border: 0;height: 26px" onclick="check();"></button>
					</div>
				</div>
			</div>
			</div>
	</body>
</html>
