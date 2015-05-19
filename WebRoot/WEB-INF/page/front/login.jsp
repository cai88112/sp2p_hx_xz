<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>富壹代-登录</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="css/css.css" rel="stylesheet" type="text/css" />
    <style type="text/css">
      ul,li{margin:0;padding:0}
      #scrollDiv{height:182px;overflow:hidden}
    </style>
</head>
<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>
<div class="nymain">
			<div class="bigbox">
				<div class="til">
					会员登录
				</div>
				<div class="sqdk"
					style="padding-top: 55px; padding-bottom: 80px; padding-left: 60px;">
					<div class="logintab">
						<form action="logining.do" method="post">
							<table width="100%" border="0" cellspacing="0" cellpadding="0" style="width: 500px;">
								<tr>
									<!--<th align="right">
										邮箱／手机／账号：
									</th>  -->
									<th align="right" style=" width: 110px;">邮箱/手机/账号：</th>
									<td>
										<input type="text" class="inp202" name="paramMap.email"
											id="email" />
										<span id="email_" style="display: none;"><a id="retake_email"  href="javascript:reSendEmail();">发送激活邮件</a>
										</span>	
									</td>
								</tr>
								<tr><th></th><td><span style="color: red;" id="s_email" class="formtips"></span></td></tr>
								<tr>
									<th align="right">
										密码：
									</th>
									<td class="mima">
										<input type="password" class="inp202" name="paramMap.password"
											id="password" />
										<span id="retake_password"><a  href="forgetpassword.do">忘记密码</a>
										</span>
									</td>
								</tr>
									<tr><th></th><td><span style="color: red;" id="s_password" class="formtips"></span></td></tr>
								<tr>
									<th align="right">
										验证码：
									</th>
									<td>
										<input type="text" class="inp100" name="paramMap.code"
											id="code" />
										<img src="admin/imageCode.do?pageId=userlogin" title="点击更换验证码"
											style="cursor: pointer;" id="codeNum" width="46" height="18"
											onclick="javascript:switchCode()" /> &nbsp;&nbsp; <a href="javascript:void()" onclick="switchCode()" style="color: blue;">换一换?</a>
									</td>
								</tr>
									<tr><th></th><td><span style="color: red;" id="s_code" class="formtips"></span></td></tr>
								<tr>
									<td>&nbsp;
										
									</td>
									<td class="zctd">
										还没有加入富壹代？<a href="reg.do">立即注册</a>
									</td>
								</tr>
								
								<!-- Start: Inject JS code for Sync to UCenter -->
								<tr>
									<td></td>
									<td><span id="sync_result"></span></td>
								</tr>
								<!-- End -->
								
								<tr>
									<td>&nbsp;
										
									</td>
									<td>
										<input type="button" value="登录" class="dlbtn"
											style="cursor: pointer;background-color: #3d3c8c;border-radius: 2px;background-image: none;" id="btn_login" />
									</td>
								</tr>
							</table>
						</form>
					</div>
					<div class="tip">
						 <img src="images/reg_right.jpg" />
						<!--  <ul>
							<li>
								帮助他人 快乐自己 收获利息
							</li>
							<li>
								助您创业 资金周转 分期偿还
							</li>
							<li>
								收益稳定可靠回报高
							</li>
							<li>
								交易安全快捷有保障
							</li>
						</ul> -->
					</div>
					<!-- <div class="renpic">-->
					</div>
				</div>
			</div>
		</div>

<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<script>
$(function(){
	$("#code").bind('keyup', function(event){
			   if (event.keyCode=="13"){
			      login();  
			   }
			});	
	$("#email").bind('keyup', function(event){
			   if (event.keyCode=="13"){
			      login();  
			   }
			});	
	$("#password").bind('keyup', function(event){
			   if (event.keyCode=="13"){
			      login();  
			   }
			});		
	})
	
		//初始化
		function switchCode(){
			var timenow = new Date();
			$("#codeNum").attr("src","admin/imageCode.do?pageId=userlogin&d="+timenow);
		}
		$(document).ready(function(){
		//===========input失去焦点
		     $("form :input").blur(function(){
		       //email
	                 if($(this).is("#email")){   
		            if(this.value==""){   
		                $("#s_email").attr("class", "formtips onError");
		                $("#s_email").html("*请输入用户名或邮箱地址");
		            }else if(/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(this.value)){ //判断用户输入的是否是邮箱地址
		            	checkRegister('email');
		            }else if((/^1[3,5,8]\d{9}$/.test(this.value))){
		                checkRegister('cellphone');
		            }else{ 
		            	checkRegister('userName');
		            	  $("#s_email").attr("class", "formtips");
		            }   
		        }  
		     //password
		   	   if($(this).attr("id")=="password"){
		   	    pwd=this.value; 
		     if(this.value==""){
		      	$("#s_password").attr("class", "formtips onError");
		        $("#s_password").html("*请輸入您的密码");  
		     }else if(this.value.length<6){ 
		      	$("#s_password").attr("class", "formtips onError");
		      $("#s_password").html("*密码长度为6-20个字符"); 
		     }else{
		     $("#s_password").html(""); 
		      	$("#s_password").attr("class", "formtips");
		     }
		   }
		   
		   	  //验证码
		   	   if($(this).attr("id")=="code"){
		     if(this.value==""){
		     $("#s_code").attr("class", "formtips onError");
		      $("#s_code").html("*请输入验证码"); 
		     }else{   
		        		$("#s_code").attr("class", "formtips");
		                $("#s_code").html(""); 
		            } 
		   }		       
		      });
    });
				
		//===验证数据
       function checkRegister(str){
          var param = {};
          	if(str == "userName"){
				param["paramMap.email"]  = "";
				param["paramMap.userName"] = $("#email").val();
			}else if(str=="email"){
				param["paramMap.email"] = $("#email").val();
				param["paramMap.userName"] = "";
			}else{
				param["paramMap.email"] = "";
				param["paramMap.userName"] = "";
				param["paramMap.cellphone"] = $("#email").val();
			}
			$.post("ajaxCheckLog.do",param,function(data){
			    $("#email_").hide();
	              if(data == 2 && str == "userName"){
	                 $("#s_email").html("*无效用户！");
	              }else if(data == 3 && str == "userName"){
	                 $("#s_email").html("*该用户还没激活！");
	                 //add by houli
	                 $("#email_").show();
	              }else if(data == 4&& str == "userName"){
	              	$("#s_email").attr("class", "formtips");
                	$("#s_email").html("");
	              }
	              if(data == 0 && str == "email"){
	                 $("#s_email").html("*无效邮箱！");
	              }else if(data == 1 && str == "email"){
	                 $("#s_email").html("*该邮箱用户还没激活！");
	                 //add by houli
	                 $("#email_").show();
	              }else if(data == 4&& str == "email"){
	              	$("#s_email").attr("class", "formtips");
                	$("#s_email").html("");
	              } 
		          if(data == 5 && str == "cellphone"){
	                 $("#s_email").html("*用户不存在！");
	              }else if(data == 4 && str == "cellphone"){
	                $("#s_email").html("");
	              }
			});
       }   
</script>
<script>

function login(){               
     $(this).attr('disabled',true);
      if($("#email").val()==""){   
              $("#s_email").attr("class", "formtips onError");
              $("#s_email").html("*请输用户名或邮箱地址");
          }
              if($("#password").val()==""){   
          	$("#s_password").attr("class", "formtips onError");
              $("#s_password").html("*请输入密码");   
             // $("#retake_password").hide();
          }  
       $('#btn_login').attr('value','登录中...');
      var param = {};
	param["paramMap.pageId"] = "userlogin";
	param["paramMap.email"] = $("#email").val();
	param["paramMap.password"] = $("#password").val();
	param["paramMap.code"] = $("#code").val();
	param["paramMap.afterLoginUrl"]="${afterLoginUrl}";
     $.post("logining.do",param,function(data){
    	if(data == 1){		
    		
    		// sync to ucenter
      	   var syncparam={};
      	   syncparam["paramMap.email"] = $("#email").val();
      	   syncparam["paramMap.password"] = $("#password").val();
      	   $.post("synctoucenter.do",syncparam,function(data){
      		   $('#sync_result').html(data);
      		   alert(data);
      	   });
    		
    		
    	        var afterLoginUrl='${afterLoginUrl}';
    	        if(afterLoginUrl != ''){
		   window.location.href='${afterLoginUrl}';
    	        }else{
		   window.location.href='home.do';
    	        }
	}else if (data == 2) {
	     $('#btn_login').attr('value','登录');
		$("#s_code").attr("class", "formtips onError");
              $("#s_code").html("*验证码错误！"); 
              switchCode();
               $("#btn_login").attr('disabled',false); 
	} else if (data == 3) {
	 $('#btn_login').attr('value','登录');
		$("#s_email").attr("class", "formtips onError");
              $("#s_email").html("*用户名或密码错误！"); 
               switchCode();
               $("#btn_login").attr('disabled',false); 
	} else if (data == 4) {
	 $('#btn_login').attr('value','登录');
		$("#s_email").attr("class", "formtips onError");
		 switchCode();
              $("#s_email").html("*该用户已被禁用！"); 
               $("#btn_login").attr('disabled',false); 
	}
     });
}

function reSendEmail(){
   if($("#email").val()==""){
     alert("请输入邮箱");
     return;
   }
   window.location.href = "reActivateEmail.do?email="+$("#email").val();
}

 $("#btn_login").click(function(){
   login();  
 });
</script>
</body>
</html>
