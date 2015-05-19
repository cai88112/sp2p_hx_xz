<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>富壹代-手机注册</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link href="css/css.css" rel="stylesheet" type="text/css" />
    <link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />
    <style type="text/css">
      ul,li{margin:0;padding:0}
      #scrollDiv{height:182px;overflow:hidden}
      .checkRight{width: 25px;visibility: visible;}
      .checkError{width: 25px;visibility: hidden;}
      .resms1{margin:40px auto ! important; width:30%; ; height:30px;border:none;border-radius:5px;font-size:1em;}
      .resms{margin:40px auto ! important; width:30%; ; height:30px;background-color:#26bc73  ! important; color:#FFFFFF;border:none;border-radius:5px;font-size:1em;}      
 	</style>
</head>
<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	

<div class="nymain"  style="padding-top: 14px;">
  <div class="bigbox" style="background: white;">
  <div class="til">手机会员注册</div>
  
  <div style="
    border: red;
    border-bottom: red;
    border-bottom-width: initial;
    padding-left: 80px;
    padding-top: 40px;
    border-bottom-color: re;
    width: 500px;
    height: 50px;

">
  <div style="
    width: 650px;
    height: 30px;
    border-bottom: 1px solid #e66432;
">

 <a href="reg.do"  style="
    background: #ebebeb;
    color: #3e3c8f;
    display: block;  text-align: center;  
    width: 100px;
    height: 30px;  vertical-align: middle;
    float: left;
    line-height: 30px;
    font-size: 14px;
    border-top-left-radius: 2px;
	border-top-right-radius: 2px;
">邮箱注册</a>

  <a style="
    background: #e66432;
    color: white;
    width: 100px;
    display: block;
    text-align: center;
    height: 30px;
    vertical-align: middle;
    float: left;
    line-height: 30px;
    font-size: 14px;
    border-top-left-radius: 2px;
	border-top-right-radius: 2px;
    margin-left: 3px;
">手机注册</a>
    </div>
</div>
  
  
  
  
  <div class="sqdk" style="padding-top:35px; padding-bottom:80px; padding-left:60px;">
    <div class="logintab">
      <form action="register.do" method="post">
      <s:hidden name="paramMap.param" id="param"  />
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
    <th align="right" ><span style="color: #968784;">*&nbsp;</span>手机号：</th>
    <td><input type="text" class="inp202"  name="paramMap.cellphone" id="cellphone"/>
    <img id="imgCheckTel" name="imgCheckTel"  src="images/checkRight.jpg" class="checkError" />
    </td>

  </tr>
  <tr>
    <th align="right"></th>
    <td> <span style="color: red" id="s_cellphone" class="formtips"></span>
    </td>
  </tr>
  <tr>
    <th align="right"><span style="color: #968784;">*&nbsp;</span>短信验证码：</th>
    
      <td><input type="text" class="inp100" style="width: 150px;" name="paramMap.cellcode" id="cellcode" />
      
    <!--  <img src="admin/imageCode.do?pageId=cellphone" title="点击更换验证码" style="cursor: pointer;width: 75px;height: 20px;margin-left: 10px;" id="codeNum" width="46" height="18" onclick="javascript:switchCode()" />
		-->
		<input type="button" class="resms" id="sendCode" value="获取手机验证码" />							
									</td>
     <!--  <td width="80px"> <span style="color: red" id="s_code" class="formtips"></span>
    </td>-->
  </tr>
  <tr>
    <th align="right"></th>
    <td> <span style="color: red" id="s_cellcode" class="formtips"></span>
    </td>
  </tr>
   <tr>
    <th align="right" style="width: 90px;"><span style="color: #968784;">*&nbsp;</span>用户名：</th>
    <td><input type="text" class="inp202"  name="paramMap.userName" id="userName"/>
    </td>
  </tr>
    <tr>
    <th align="right"></th>
    <td> <span style="color: red" id="s_userName" class="formtips"></span>
    </td>
  </tr>
    <tr>
    <th align="right" style="width: 90px;"><span style="color: #968784;">*&nbsp;</span>密码：</th>
    <td><input type="password" class="inp202"  name="paramMap.password" id="password"/>
    </td>
  </tr>
   <tr>
    <th align="right"></th>
    <td> <span style="color: red" id="s_password" class="formtips"></span>
    </td>
  </tr>
   <tr>
    <th align="right" style="width: 95px;"><span style="color: #968784;">*&nbsp;</span>确认密码：</th>
    <td><input type="password"  class="inp202"  name="paramMap.confirmPassword" id="confirmPassword"/>
    </td>
  </tr>
  <tr>
    <th align="right"></th>
    <td> <span style="color: red" id="s_confirmPassword" class="formtips"></span>
    </td>
  </tr>
    <tr>
    <th align="right">推荐人：</th>
    <td>
      <input type="text" class="inp202" name="paramMap.refferee" id="refferee" value="${paramMap.refferee }" />
    	 <span id="s_refferee11" class="fred"></span>
    </td>
  </tr>
  <tr>
    <th align="right"></th>
    <td><span id="s_refferee" class="formtips"></span>
    </td>
  </tr>
  
  
  	<s:if test="#session.DEMO==1">
  	<tr>
  	<td>&nbsp;</td>
	  	<td ><span style="color:red;font-size: 12px">* 演示站点不发送短信</span></td>
		</tr>
	</s:if>
  <tr>
    <th align="right">&nbsp;</th>
    <td class="tyzc" ><input type="checkbox" id="agre" checked="checked"/>我已经阅读并同意<a style="cursor: pointer;" onclick="fff()">使用条款</a>和<a style="cursor: pointer;" onclick="ffftip()">隐私条款</a></td>
  </tr>
  <tr>
    <th align="right">&nbsp;</th>
   <!--  <td><a href="yxjihuo.html" class="zcbtn">免费注册</a></td> -->
   <td>
   <input type="button" id="btn_cellreg" value="提交" class="zcbtn" style="cursor: pointer;"/>
   &nbsp;&nbsp;&nbsp;&nbsp;已有账号？<a  href="login.do" style="color: #3d3c8c;">直接登录</a>
   </td>
  </tr>
    </table>
  </form> 
    </div>
    <div class="tip">
   <img src="images/reg_right.jpg" />
    <!--  <ul><li>帮助他人 快乐自己 收获利息</li>
	<li>助您创业 资金周转 分期偿还</li>
	<li>收益稳定可靠回报高</li>
	<li>交易安全快捷有保障</li></ul>
    <div class="loginbtn">
    <a href="login.do" class="dlbtn">马上登录</a>
    </div>
    -->
    </div>
    <!--<div class="renpic" style="top:50px;">  
    </div>-->
    
    </div>
  </div>
  </div>
</div>
<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<script type="text/javascript" src="script/nav-zh.js"></script>
  <script type="text/javascript" src="css/popom.js"></script>
  <script src="script/jquery-1.7.1.min.js" type="text/javascript"></script>
  <script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
  <script>
  //弹出隐私条款和使用条款
 function fff(){
	  jQuery.jBox.open("post:querytips.do", "使用条款", 600,400,{ buttons: { } });
}
function ffff(){
  ClosePop();
}
</script>
<script>
 function ffftip(){
	 jQuery.jBox.open("post:querytip.do", "隐私条款", 600,400,{ buttons: { } });
		    }
</script>
  <script>
//回车登录
document.onkeydown=function(e){
  if(!e)e=window.event;
  if((e.keyCode||e.which)==13){
   cellreg();
  }
}
</script>
  <script>
  $(function(){
 $("form :input").blur(function(){ 
 //手机号码
 if($(this).attr("id")=="cellphone"){
		  
   if((!/^1[3,5,8]\d{9}$/.test($("#cellphone").val()))){
         $("#s_cellphone").attr("class", "formtips onError");
		 $("#s_cellphone").html("手机号码格式错误"); 
		 $("#imgCheckTel").attr("class", "checkError");
   }else{
	   	  var param = {};
	      param["paramMap.cellphone"]= $("#cellphone").val();
	      param["paramMap.pageId"] = "cellphone";
	      param["paramMap.code"]= $("#code").val();
		 $.post("checkIsExitPhone.do",param,function(da){
			 //alert("da"+da);
			 if(da==5){				 
			   $("#s_cellphone").attr("class", "formtips onError");
	      	   $("#s_cellphone").html("手机号已存在!");
	      	   $("#imgCheckTel").attr("class", "checkError");
	         }
			 else{
				
				 $("#imgCheckTel").attr("class", "checkRight");
				 $("#s_cellphone").attr("class", "formtips");
				 $("#s_cellphone").html(""); 
			 }
		 });
        	 	   	
   }
 }
 
 //验证码
 if($(this).attr("id")=="code"){
   if($(this).val()==""){
     	 $("#s_code").attr("class", "formtips onError");
		 $("#s_code").html("请填写短信校验码"); 
   }else{
        $("#s_code").attr("class", "formtips");
		 $("#s_code").html(""); 
   }
 }
 
 //userName
 if($(this).attr("id")=="userName"){
   if(this.value==""){
   $("#s_userName").attr("class", "formtips onError");
     //alert("请输入登录用户名");
      $("#s_userName").html("请输入登录用户名");
   }else if(this.value.length<2){ 
   $("#s_userName").attr("class", "formtips onError");
    //alert("用户名长度为5-20个字符");
    $("#s_userName").html("用户名长度为2-20个字符"); 
  // }else if(!/^[^@\/\'\\\"#$%&\^\*]+$/.test(this.value)){
     }else if(!/^[\u4E00-\u9FA5A-Za-z0-9_]+$/.test(this.value)){
      $("#s_userName").attr("class", "formtips onError");
      $("#s_userName").html("用户名不能含有特殊字符"); 
   } else{
   $("#s_userName").html(""); 
    checkRegister('userName');
    $("#s_userName").attr("class", "formtips");
   }
 }
 
 //password
	 if($(this).attr("id")=="password"){
	    pwd=this.value; 
 if(this.value==""){
  	$("#s_password").attr("class", "formtips onError");
   //alert("请设置您的密码");
    $("#s_password").html("请设置您的密码");  
 }else if(this.value.length<6){ 
  	$("#s_password").attr("class", "formtips onError");
  //alert("密码长度为6-20个字符");
  $("#s_password").html("密码长度为6-20个字符"); 
 }else{
 $("#s_password").html(""); 
  checkRegister('password');
  	$("#s_password").attr("class", "formtips");
 }
}

 

//confirmPassword

 if($(this).attr("id")=="confirmPassword"){
 if(this.value==""){
 $("#s_confirmPassword").attr("class", "formtips onError");
   //alert("请再次输入密码确认");
   $("#s_confirmPassword").html("请再次输入密码确认"); 
 }else if(this.value!=pwd){ 
 $("#s_confirmPassword").attr("class", "formtips onError");
  $("#s_confirmPassword").html("两次输入的密码不一致"); 
 }else{
  $("#s_confirmPassword").attr("class", "formtips");
  $("#s_confirmPassword").html(""); 
 }
}

 //----add by houli  推荐人 refferee
	 if($(this).attr("id")=="refferee"){
		 if(this.value!=""){//如果推荐人不为null，则进行判断，判断经纪人是否有效
		 $.post("queryValidRecommer.do",{refferee:this.value},function(data){
		        if(data == 1){
		       $("#s_refferee11").html("推荐人不存在");
		    }else{
		      $("#s_refferee11").html("");
		    }
		 });
					  } 
	  }
 

 });  
  });
  
//验证数据
	function checkRegister(str){
	  var param = {};
	  if(str == "userName"){
      param["paramMap.email"] = "";
	    param["paramMap.userName"] = $("#userName").val();    		     	  
	  }else{
	     param["paramMap.email"] = $("#email").val();
	     param["paramMap.userName"] = "";
	  }
	  $.post("ajaxCheckRegister.do",param,function(data){
	  if(data == 3 || data == 4){
	     if(str=="userName"){
	     $("#s_userName").html("该用户已存在");
	     }else{
	      $("#s_email").html("该邮箱已注册");
	     }
	  }
	  });
	  
	}
  
  </script>
  <script>
//初始化验证码
//function switchCode(){
//	var timenow = new Date();
//	$("#codeNum").attr("src","admin/imageCode.do?pageId=cellphone&d="+timenow);
// }
  </script>
  <script>
   var falg = true;

  var t1;
  var second = 60;
  
      $('#sendCode').bind('click', function() {
    	// alert("testsdfdsfdsqqqqqq");
    	 falg = true;
    	  if(falg){
              falg = false;
               if($("#cellphone").val()==""){
                 $("#s_cellphone").html("手机号码格式错误");
                 falg = true;
                
                 return false;
               }
               
               if(!$("#agre").attr("checked")){
		            alert("请勾选我已阅读并同意《使用条款》和《隐私条款》");
		            falg = true;
		           return false;
		           }               
               

     				 
               var param = {};
               param["paramMap.cellphone"]= $("#cellphone").val();
               param["paramMap.pageId"] = "cellphone";
               param["paramMap.code"]= $("#code").val();
               
               $.post("checkIsExitPhone.do",param,function(da){
            	  // alert('da'+da);
               if(da!=5){
                 var cellph = $("#cellphone").val();
                 if('${DEMO}'!=1){                	
	                 /**发送手机验证码**/
	               	
	                 var phone = $("#cellphone").val();
			          var test={};
			          test["paramMap.phone"] = phone;
			          test["paramMap.pageId"] = "cellphone";//只有手机注册页面需要pageId 
			         
			          $.post("sendSMSNew.do",test,function(data){
			        	//  alert('data:'+data);
			        	
			            if(data==1){
			            	 clearInterval(t1);
			                 $("#sendCode").removeClass("resms");
			            	 $("#sendCode").toggleClass("resms1");
			            	 $('#sendCode').attr("disabled","disabled");
			                 t1 = setInterval(function() {
			                     setval();
			                 }, 1000);
			            }else{
			              alert("手机验证码发送失败");
			            }
			          });
	         
	           }else{
	        	   
	                	window.location.href='cellPhoneinit.do?cp='+cellph;
	           			}
	        }else if(da==3){
	               $("#s_cellphone").html("手机号码格式错误");
	                 falg = true;
	                
	               }else if(da==5){
	            	   $("#s_cellphone").html("手机号已存在!");
	                   falg = true;
	                  
	               }
	               });           
	              } 
          
      });
      
      function setval(val) {
          second -= 1;
        
          if (second <= 0) {
              second = 60;
              clearInterval(t1);
              $("#sendCode").val("获取短信验证码");             
              $("#sendCode").removeClass("resms1");
              $("#sendCode").toggleClass("resms");
              $('#sendCode').removeAttr("disabled");
              return;
          }
          if (second >= 2) {
              var str = "倒计时" + second + " 秒";
              $("#sendCode").val(str);

          }

      }

      
      
      
      function cellreg(){
    	  falg = true;
    	   var errornum=$("form .onError").length;
    	              if(falg){
    	              falg = false;
    	               if($("#cellphone").val()==""){
    	                 $("#s_cellphone").html("手机号码格式错误");
    	                 falg = true;
    	                 switchCode();
    	                 return false;
    	               }
    	               if($("#cellcode").val()==""){
    	                 $("#s_cellcode").html("手机验证码不正确"); 
    	                 falg = true;
    	                 switchCode();
    	                 return false;
    	               }
    	               
    	               if(!$("#agre").attr("checked")){
    			            alert("请勾选我已阅读并同意《使用条款》和《隐私条款》");
    			            falg = true;
    			           return false;
    			           }
    	               
    	                  if($("#userName").val()==""){
    	            	     $("#s_userName").html("请输入登录用户名");
    	            	     falg = true;
    	            	     return false;
    	            	   }
						   if($("#password").val()==""){
    	            	   $("#s_password").html("请设置您的密码"); 
    	            	   falg = true;
    	            	   return false;
    	            	   }
						   if($("#confirmPassword").val()==""){
    	            	     $("#s_confirmPassword").html("请再次输入密码确认"); 
    	            	     falg = true;
    	            	     return false;
    	            	   } 
						   if(errornum > 0){   
							  	alert('请正确填写注册信息');
							  	falg = true;
							      return false;
							}
						   if($("#cellphone").val()==""){
							      alert("手机号码错误");
							      falg = true;
							      return false;
							   } 
						   $('#btn_cellreg').attr('value','注册中...');
    	               var param = {};
    	               param["paramMap.cellphone"]= $("#cellphone").val();
    	               param["paramMap.cellcode"] = $("#cellcode").val();
    	               param["paramMap.userName"] = $("#userName").val();
    	               param["paramMap.password"] = $("#password").val();
    	               param["paramMap.refferee"] = $("#refferee").val();
    	               param["paramMap.confirmPassword"] = $("#confirmPassword").val();
    	               param["paramMap.param"] = $("#param").val();
    	               $.post("cellreginfo.do",param,function(data){
    	            	  
    	            	     if(data.mailAddress=='0'){
    	            	       $("#s_code").html("验证码输入有误，请重新输入");
    	            	        $('#btn_cellreg').attr('value','免费注册');
    	            	        falg = true;
    	            	     }else if(data.mailAddress=='1'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      $("#s_confirmPassword").html("两次输入的密码不一致"); 
    	            	      falg = true;
    	            	     }else if(data.mailAddress=='2'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      $("#s_userName").html("该用户已存在");
    	            	      falg = true;
    	            	     }else if(data.mailAddress=='3'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	     // $("#s_email").html("该邮箱已注册");
    	            	      falg = true;
    	            	     }else if(data.mailAddress=='4'){
    	            	      alert("注册失败！");
    	            	       $('#btn_cellreg').attr('value','免费注册');
    	            	       falg = true;
    	            	     }else if(data.mailAddress=='5'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      falg = true;
    	            	     	alert("推荐人填写错误！");
    	            	     }else if(data.mailAddress=='12'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      falg = true;
    	            	       $("#s_email").html("请输入您的邮箱地址");
    	            	     }
    	            	     else if(data.mailAddress=='13'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      falg = true;
    	            	        $("#s_userName").html("请输入登录用户名");
    	            	     }
    	            	     else if(data.mailAddress=='14'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	        $("#s_password").html("请设置您的密码"); 
    	            	        falg = true;
    	            	     }
    	            	       else if(data.mailAddress=='15'){
    	            	        $('#btn_cellreg').attr('value','免费注册');
    	            	       $("#s_confirmPassword").html("请再次输入密码确认"); 
    	            	       falg = true;
    	            	     }else if(data.mailAddress=='16'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      falg = true;
    	            	      alert("注册失败");
    	            	     }
    	            	     else if(data.mailAddress=='18'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      $("#s_userName").html("用户名长度为2-20个字符"); 
    	            	      falg = true;
    	            	     }
    	            	     else if(data.mailAddress=='20'){
    	            	     $('#btn_cellreg').attr('value','免费注册');
    	            	     $("#s_userName").html("用户名不能含有特殊字符"); 
    	            	      falg = true;
    	            	     }
    	            	      else if(data.mailAddress=='21'){
    	            	      $('#btn_cellreg').attr('value','免费注册');
    	            	      $("#s_userName").html("用户名第一个字符不能是下划线"); 
    	            	      falg = true;
    	            	     }else if(data.mailAddress=='手机已存在'){
    	            	             alert("该手机号码已经注册了");	
    	            	             window.location.href='cellPhoneinit.do';
    	            	             //将手机短信验证码移除
    	            	             $.post("removecellCode.do","",function(data){});//删除手机验证码
    	            	     }
    	            	     else if(data.mailAddress=='请输入正确的验证码'){
    	            	     	 $('#btn_cellreg').attr('value','免费注册');
    	            	       $("#s_cellcode").html("短信校验码错误");
    	            	        falg = true;
    	            	     }
    	            	     else if(data.mailAddress=='注册成功'){
    	            	      alert("恭喜你!注册成功");
    	            	      $.post("removecellCode.do","",function(data){
    	            	        window.location.href="login.do";
    	            	      });//删除手机验证码
    	            	     }
    	            	   });
    	           
    	              } 
    	  } 
  
  
  </script>
  <script>
  //提交
  $(function(){
     $("#btn_cellreg").click(function(){
     cellreg();
     });
  });
  </script>
</body>
</html>
