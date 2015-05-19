<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>体验金账户</title>
<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
<link href="../css/admin/admin_custom_css.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
<script type="text/javascript">
		  $(function(){
				param["pageBean.pageNum"] = 1;
				initListInfo(param);
			}); 
			
			function initListInfo(param){
		 		$.shovePost("queryExperiencemoneyPage.do",param,initCallBack);

		 	}
		 	
 		 	function initCallBack(data){
				$("#dataInfo").html(data);
			} 
						
		</script>
</head>
<body>
<div id="dataInfo"></div>
</body>
</html>