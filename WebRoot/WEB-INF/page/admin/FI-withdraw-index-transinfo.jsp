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
			
			
			function trans(){//转账  
			 	
			  param['paramMap.wid'] = $("#wid").val();
			  param['paramMap.status'] = $("input[name='status']:checked").val();
			  param['paramMap.adminId'] = '${sessionScope.admin.id }';
			  param['paramMap.userId'] = $("#userId").val();
			  param['paramMap.sum'] = ${sum };
			  param['paramMap.poundage'] = ${poundage};
			  param['paramMap.check'] = 'false';
			  param['paramMap.oldStatus'] = '4';//正在转账状态
			  $("#btnSave").attr("disabled",'disabled');
			  $.shovePost("updateWithdrawTransfer.do",param,function(data){
			     if(data.msg == 1){
			     	alert("操作成功");
			     	var para1 = {};
			     	window.parent.initListInfo(para1);
			     	window.parent.close();//关闭弹出窗口
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
					<strong>提现</strong>
					<div
						style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: 1120px; padding-top: 10px; background-color: #fff;">
						
						<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
							width="100%" border="0">
							
							<tbody>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
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
									<td class="f66" align="left" width="80%" height="36px">
									充值成功总额：${r_total }
									&nbsp;&nbsp;
									&nbsp;&nbsp;
									提现成功总额：${w_total }
									</td>
									
								</tr>
								<tr>
									<td class="f66" align="left" width="80%" height="36px">
									投标成功总额：${ real_Amount}
									&nbsp;&nbsp;
									&nbsp;&nbsp;
									提现上额限制：${withdraw_max }
									</td>
									
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									可用余额：${usableSum }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									开户名：${realName }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									提现支行：${branchBankName }
									</td>
									<td class="f66" align="left" width="50%" height="36px">
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									提现账号：${cardNo }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									提现总额：${sum }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									到账金额：${realMoney }
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									<td class="f66" align="left" width="80%" height="36px">
									手续费：${poundage }
									&nbsp;&nbsp;
									&nbsp;&nbsp;
									状态：${status }
									</td>
									
								</tr>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
									添加时间/IP：
									${applyTime }/${ipAddress}
									<!-- <s:date name="paramMap.applyTime" format="yyyy-MM-dd HH:mm:ss" />-->
									</td>
									<td>
									&nbsp;
									</td>
								</tr>
								<tr>
									 <td class="f66" align="left" width="80%" height="36px">
									   审核信息：&nbsp;&nbsp;
									   ${rk }
									 </td>
								</tr>
								<tr>
									 <td class="f66" align="left" width="50%" height="36px">
									   转账状态：&nbsp;&nbsp;
									   <s:radio id="radio1" name="status" list="#{2:'转账成功',5:'转账失败'}" value="2"></s:radio>
									 </td>
								</tr>
				   <!-- 		<tr>
                            <td height="36" align="left" class="blue12">
                                &nbsp;</td>
                            <td height="36" align="left" class="blue12">
                                    <button id="btnSave"  style="background-image: url('../images/admin/btn_queding.jpg');width: 70px;border: 0;height: 26px" onclick="trans();"></button>
                                   
                            </td>
                        </tr> -->
							</tbody>
						</table>
						
						
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					 <button id="btnSave"  style="background-image: url('../images/admin/btn_queding.jpg');width: 70px;border: 0;height: 26px" onclick="trans();"></button>
					</div>
				</div>
			</div>
			</div>
	</body>
</html>
