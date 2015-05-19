<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
	<head>
		<title>财务管理-用户资金管理</title>
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
		<script type="text/javascript" src="../css/popom.js"></script>
		<script type="text/javascript">
			$(function(){
				param["pageBean.pageNum"] = 1;
				initListInfo(param);
				
			});
			
			function initListInfo(param){
				param["paramMap.userId"] = $("#userId").val();
				param["paramMap.userName"] = $("#userName").val();
		 		$.shovePost("queryUserFundRecordList.do",param,initCallBack);
		 	}
		 	
		 	function initCallBack(data){
				$("#dataInfo").html(data);
			}
						
		</script>
	</head>
	<body>
	<s:hidden id="userId" name="paramMap.userId" ></s:hidden>
	<s:hidden id="userName" name="paramMap.userName" ></s:hidden>
		<div id="right">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<table border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="120" height="28"  class="xxk_all_a">
								<a href="userFundInit.do">资金管理</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td width="120" height="28"  class="xxk_all_a">
								<a href="queryAllUserFundRecordInit.do">资金明细</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td width="120" height="28" class="main_alll_h2">
								<a href="javascript:void(0)">资金记录</a>
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
						
						<span id="dataInfo"> </span>
					</div>
				</div>
			</div>
			</div>
	</body>
</html>
