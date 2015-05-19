<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>老米护盾管理</title>
<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
<link href="../css/admin/admin_custom_css.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
<script type="text/javascript">
		  $(function(){
				param["pageBean.pageNum"] = 1;
				$("#bt_search").click(function(){
					param["pageBean.pageNum"] = 1;
					initListInfo(param);
				});
				initListInfo(param);
 				$("#excel").click(function(){ 
				       window.location.href=encodeURI(encodeURI("exportforProtectOldUser.do?userName="+$("#userName").val()));
				}); 
 				$("#queryNotProtectVipInfoIndex").click(function(){ 
				       window.location.href=encodeURI(encodeURI("queryNotProtectVipInfoIndex.do?userName="+$("#userName").val()));
				}); 
 				
			}); 
			
			function initListInfo(param){
				param["paramMap.userName"] = $("#userName").val();
		 		$.shovePost("queryProtectOldUserPage.do",param,initCallBack);
		 	}
		 	
 		 	function initCallBack(data){
				$("#dataInfo").html(data);
			} 
						
		</script>
</head>
<body>
	 <div style="margin:10px 30px">
		<table cellspacing="0" cellpadding="0"
			width="100%" border="0">
			<tbody>
				<tr>
					<td class="f66" align="left" width="50%" height="36px">用户名： <input
						id="userName" name="paramMap.username" type="text" /> &nbsp;&nbsp;
						<input id="bt_search" type="button" value="查找" />
						<input id="excel" type="button" value="导出Excel" name="excel">
						<input id="queryNotProtectVipInfoIndex" type="button" value="添加老米" name="queryNotProtectVipInfoIndex">
					</td>
				</tr>

			</tbody>
		</table>
	</div> 
	<div id="dataInfo"></div>
</body>
</html>