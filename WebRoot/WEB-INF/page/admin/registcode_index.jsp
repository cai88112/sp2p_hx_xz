<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>注册码参数</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
		
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		<link rel="stylesheet" href="../kindeditor/themes/default/default.css" />
		<link rel="stylesheet" href="../kindeditor/plugins/code/prettify.css" />
		<script charset="utf-8" src="../kindeditor/kindeditor.js"></script>
		<script charset="utf-8" src="../kindeditor/lang/zh_CN.js"></script>
		<script charset="utf-8" src="../kindeditor/plugins/code/prettify.js"></script>
		<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
		<script type="text/javascript" src="../common/dialog/popup.js"></script>
				
	</head>
	<body>
			<div id="right">
				<div style="padding: 15px 10px 0px 10px;">
					<div>
						<table  border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td  width="18%" height="28"  class="main_alll_h2">
									<a href="">注册码设置</a>
								</td>
							
							</tr>
						</table>
					</div>
					 
					<div id="showcontent" style="width: auto; background-color: #FFF; padding: 10px;">
						<!-- 内容显示位置 -->
						
						
						<table width="50%" border="0" cellspacing="1" cellpadding="3" align="center">
							<tr style="height: 20px;"></tr>
							<tr>
								<td style="width: 100px; height: 25px;" align="right"
									class="blue12">
									注册ID：
								</td>
								<td align="left" class="f66">
									<%=IConstants.SERIAL_KEY %>
									<input type="hidden" value="<%=IConstants.SERIAL_KEY %>" id="serial_key"/>
									
								</td>
							</tr>	
								<tr>
								<td style="width: 100px; height: 25px;" align="right"
									class="blue12">
									注册序列号:
								</td>
								<td align="left" class="f66">
									<input type="text" id="serial_number"  style="width: 250px" value="<%=IConstants.SERIAL_NUMBER %>"/>
									<span class="require-field" id="e_port">*</span>
								</td>
							</tr>	
							<tr>
							<td height="36" align="right" class="blue12">
									<s:hidden name="action"></s:hidden>
									&nbsp;
								</td>
								<td >
									
									<button id="btn_tijiao"
										style="background-image: url('../images/admin/btn_queding.jpg'); width: 70px; height: 26px; border: 0px"></button>
									&nbsp; &nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<img id="img" src="../images/NoImg.GIF"
										style="display: none; width: 100px; height: 100px;" />
								</td>
							</tr>
						</table>
						
						
						<br />
					</div>
				</div>
			</div>
		<script>
		$(function(){
		  	 
		  	 $('#btn_tijiao').click(function(){
		  	  $("#btn_tijiao").attr("disabled",true);
			  	 
			  	 if($("#serial_number").val()==""){
				  		$("#e_port").html("注册序列号不能为空!");
				  		 $("#btn_tijiao").attr("disabled",false);
						return false;
					  }else
							  {
							  $("#e_port").html("*");
							  }
			  	 
		  	   
		   var param ={};
		  param["paramMap.serial_key"] = $("#serial_key").val();
		  param["paramMap.serial_number"] = $("#serial_number").val();
		  
          $.post("updateRegistCode.do",param,function(data){
               var callBack = data.msg;  
                
                  if(callBack == 1){
		          alert("操作成功!");
		           $("#btn_tijiao").attr("disabled",false);
		          window.location.reload();
		          return false;
		          }
		          //alert(callBack);    
                  
          });
		  	 

          });
		
	    
		});
		
		
		</script>
	</body>
</html>
