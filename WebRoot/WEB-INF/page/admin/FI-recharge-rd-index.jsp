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
		<script type="text/javascript" src="../css/admin/popom.js"></script>
		<script type="text/javascript">
			$(function(){
				param["pageBean.pageNum"] = 1;
				initListInfo(param);
				$("#bt_search").click(function(){
					param["pageBean.pageNum"] = 1;
					initListInfo(param);
				});
				
				$("#excel").click(function(){ 
				       var userId=$("#userId").val();
                       window.location.href="exportBackCash.do?userId="+userId;
                });
			});
			
			function initListInfo(param){
				param["paramMap.userId"] = $("#userId").val();
				param["paramMap.checkUser"] = $("#checkUser").val();
				param["paramMap.startTime"] = $("#startTime").val();
				param["paramMap.endTime"] = $("#endTime").val();
				param["paramMap.type"] = $("#type").val();
		 		$.shovePost("queryBackCashList.do",param,initCallBack);
		 	}
		 	
		 	function initCallBack(data){
				$("#dataInfo").html(data);
			}
			
			function addRecharge(){
			  var userId = $('#userId').val();
			  window.location.href= "addBackRechargeInit.do?userId="+userId;
			}
			
			function addWithdraw(){
			   var userId = $('#userId').val();
			  window.location.href= "addBackWithdrawInit.do?userId="+userId;
			}
			
			function show(id){
   			  var url = "queryR_WShow.do?rwId="+id;
              ShowIframe("充值扣费详情显示",url,600,450);
   			}
									
		</script>
	</head>
	<body>
	<s:hidden id="userId" name="paramMap.userId"  ></s:hidden>
		<div id="right">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<table  border="0" cellspacing="0" cellpadding="0">
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
								<a href="javascript:void(0)">充值扣费</a>
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
						<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
							width="100%" border="0">
							<tbody>
								<tr>
									<td class="f66" align="left" width="50%" height="36px">
										<!--  用户名：&nbsp;&nbsp;
										<input id="userName" name="paramMap.username" type="text"/>
										&nbsp;&nbsp;-->
										操作员：&nbsp;&nbsp;
										<input id="checkUser" name="paramMap.checkUser" type="text"/>
										&nbsp;&nbsp;
										操作时间：
										<input id="startTime" name="paramMap.startTime" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:'readOnly'})"/>
										&nbsp;&nbsp;
										到
										<input id="endTime" name="paramMap.endTime" type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:'readOnly'})"/>
										&nbsp;&nbsp;
										类型：
										<s:select id="type" list="operateType" name="paramMap.type" listKey="typeId" listValue="tvalue" headerKey="-100" headerValue="--请选择--" />
										&nbsp;&nbsp;
										
										<input id="bt_search" type="button" value="搜 索"  />&nbsp;&nbsp;
										<input id="excel"  type="button"
											value="导出EXCEL" name="btnExportExcel" />
									</td>
								</tr>
							</tbody>
						</table>
						<span id="dataInfo"> </span>
					</div>
				<table>
				 <tr>
				  <td>&nbsp;
				  </td>
				  <td>&nbsp;
				  </td>
				  <td>&nbsp;
				  </td>
				  <td>&nbsp;
				  </td>
				  <td >
				    <input onclick="addRecharge();" id="bt_addRecharge" type="button" value="添加充值"  />
					<input onclick="addWithdraw();" id="bt_delCost" type="button" value="扣除费用"  />
					<%--<input id="bt_exportExcel" type="button" value="导出EXCEL"  />
				  --%></td>
				 </tr>
				</table>
				</div>
			</div>
			</div>
	</body>
</html>
