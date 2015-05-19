<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
	<head>
		<title>管理首页</title>
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
		<script type="text/javascript" language="javascript">
	    $(function(){
	    	param["pageBean.pageNum"]=1;
		    initListInfo(param);
		    
		    $("#search").click(function(){
		    param["pageBean.pageNum"] = 1;
			initListInfo(param);
		    	});
		    }
	    )
	    
	    //加载留言信息
	   function initListInfo(praData) {
			praData["paramMap.level2userName"] = $("#level2userName").val();
	   		$.shovePost("queryLeve2SumCount.do",praData,initCallBack);
   		}
   		
   		function initCallBack(data){
		 	$("#dataInfo").html(data);
   		}
   		
   		//分页
   		function pageInfo(pageId){
   			param["pageBean.pageNum"] = pageId;
   			initListInfo(param);
   		}
	</script>

	</head>
	<body>
		<div id="right"
			style="background-image: url(images/admin/right_bg_top.jpg); background-position: top; background-repeat: repeat-x;">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="150" height="28" align="center"  
								class="main_alll_h2">
								<a href="queryLeve2SumInit.do">经济人提成奖励管理</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
				</div>
				<div
					style="padding-right: 10px; padding-left: 10px; padding-bottom: 10px; width: auto; padding-top: 10px; background-color: #fff;">
					<table style="margin-bottom: 8px;" cellspacing="0" cellpadding="0"
						width="100%" border="0">
						<tbody>
							<tr>
								<td class="f66" align="left" width="50%" height="36px">
									团队长账号：
									<input id="level1userName" />&nbsp;&nbsp; 
									<input id="search" type="button" value="搜索" name="search" />
								</td>
							</tr>
							<tr>
							</tr>
						</tbody>
					</table>
					<span id="dataInfo"> </span>
				</div>
			</div>
		</div>
	</body>
</html>
