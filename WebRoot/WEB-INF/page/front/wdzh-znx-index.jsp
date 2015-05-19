<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css"  rel="stylesheet" type="text/css" />
<link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />
</head>

<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	
<div class="nymain">
  <div class="wdzh">
    <div class="l_nav">
      <div class="box">
        <!-- 引用我的帐号主页左边栏 -->
         <%@include file="/include/left.jsp" %>
      </div>
    </div>
    <div class="r_main">
      <div class="tabtil" style="width: 776px">
        <ul><li class="on" dataIndex="true" onclick="showSysMails();">系统消息</li>
        <li onclick="showReceiveMails();">收件箱</li>
        <li onclick="showSendMails();">发件箱</li>
        <li>发信息</li>
        </ul>
        </div>
      <div class="box">
        <span id="sysInfo"></span>
     </div>
	  <div class="box" style="display:none;">
        <span id="receiveInfo"></span>
    </div>
      <div class="box" style="display:none;">
        <span id="sendInfo"></span>
     </div>
      <jsp:include page="wdzh-znx-sendMail.jsp"></jsp:include>
    </div>
  </div>
</div>
<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>

 <script type="text/javascript" src="${path}/script/jquery-1.7.1.min.js"></script>
 <script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
 <script type="text/javascript" src="script/nav-zh.js"></script>	
<script type="text/javascript" src="css/popom.js"></script>
 <script type="text/javascript" src="script/tab.js"></script> 
<script>
$(function(){
//样式选中
        $("#zh_hover").attr('class','nav_first');
	    $("#zh_hover div").removeClass('none');
	    $('#li_4').addClass('on');
	$('.tabmain').find('li').click(function(){
	   $('.tabmain').find('li').removeClass('on');
	   $(this).addClass('on');
	   $('.lcmain_l').children('div').hide();
           $('.lcmain_l').children('div').eq($(this).index()).show();
        })
})
</script>
 <script>
    $(function(){
	  $('.tabtil').find('li').click(function(){
	    $('.tabtil').find('li').removeClass('on');
	    $(this).addClass('on');
	    $('.tabtil').nextAll('div').hide();
        $('.tabtil').nextAll('div').eq($(this).index()).show();
   	  });
   	   initListInfo();   	 
	});
	
	 function initListInfo(){
	 	$.shovePost("querySysMailsInit.do",param,function(data){
          		$("#sysInfo").html(data);
       	});
	 }
	
	function switchCode(){
		var timenow = new Date();
		$("#codeNum").attr("src","admin/imageCode.do?pageId=userlogin&d="+timenow);
	}
      //收件人
       $("#receiver").blur(function(){
	     if($("#receiver").val()==""){
	       $("#s_receiver").html("*收件人不能为空");
	     }else{
	       checkRegister();
	     }
	  });
	  //标题
       $("#title").blur(function(){
	     if($("#title").val()==""){
	       $("#s_title").html("*标题不能为空");
	     }else{
	       $("#s_title").html("");
	     }
	  });
	  //验证码
       $("#code").blur(function(){
	     if($("#code").val()==""){
	       $("#s_code").html("*验证码不能为空");
	     }else{
	       $("#s_code").html("");
	     }
	  });
       
       //内容框
       $("#content").blur(function(){
	     if($("#content").val()==""){
	       $("#s_content").html("*内容不能为空");
	     }else{
	       $("#s_content").html("");
	     }
	  });   
</script>
<script>
   //检查用户名是否有效
    function checkRegister(){
        param["paramMap.receiver"] = $("#receiver").val();
		$.post("judgeUserName.do",param,function(data){
              if(data == 1 ){
                 $("#s_receiver").html("*收件人不存在或者还不是您的好友！");
              }else{
                 $("#s_receiver").html("");
              }
		});
     }
       function returnToPage_(pNum,type){
       if(type == 2){ //系统邮件
         returnToPage_s(pNum);
         return;
       }else if(type == 1){//收件箱
         returnToPage_r(pNum);
         return;
       }else if(type ==100){//发件箱
          returnToPage_ss(pNum);
          return;
       }
	}
     function addMail(){
     	param["paramMap.receiver"] = $("#receiver").val();
     	param["paramMap.title"] = $("#title_s").val();
     	param["paramMap.content"] = $("#content").val();
     	param["paramMap.code"] = $("#code").val();
     	param["paramMap.pageId"] = "userlogin";
     	if($("#receiver").val()==""){
	       $("#s_receiver").html("*收件人不能为空");
	       return;
	     }
	     if($("#title_s").val()==""){
	       $("#s_title").html("*标题不能为空");
	       return;
	     }
	     if($("#content").val()==""){
	       $("#s_content").html("*内容不能为空");
	       return;
	     }
	     if($("#code").val()==""){
	       $("#s_code").html("*验证码不能为空");
	       return;
	     }
	     //有错误提示的时候不提交
	     if($("#s_receiver").text()!="" || $("#s_title").text()!="" ||$("#s_content").text()!=""
	       ||$("#s_code").text()!=""){
	          return;
	       }
     	$.shovePost("addMail.do",param,function(data){
     	   if(data == 5){
     	     $("#s_code").html("验证码错误");
     	     return;
     	   }else if(data == 1){
     	     alert("邮件发送失败，请重新发送");
     	     return;
     	   }else if(data == 8){
     	     alert("你是黑名单用户不能发生站内信");
     	     return;
     	   }else{
     	   	 alert("邮件发送成功");
     	   	 $("#title_s").attr("value","");
     	   	 $("#code").attr("value","");
     	   	 $("#receiver").attr("value","");
     	   	 $("#content").attr("value","");
     	   }
     	});
     }
     
     function showReceiveMails(){
        param["pageBean.pageNum"]=1;
        $.shovePost("queryReceiveMailsInit.do",null,function(data){
           $("#receiveInfo").html(data);
        });
     }
     
     function showSendMails(){
        param["pageBean.pageNum"]=1;
        $.shovePost("querySendMailsInit.do",null,function(data){
           $("#sendInfo").html(data);
        });
     }
     //显示系统消息
     function showSysMails(){
        param["pageBean.pageNum"]=1;
        $.shovePost("querySysMailsInit.do",null,function(data){
           $("#sysInfo").html(data);
        });
     }
     
     //收件箱全选
     function checkAll_Receive(e){
   		if(e.checked){
   			$(".re").attr("checked","checked");
   		}else{
   			$(".re").removeAttr("checked");
   		}
   	 }
   	 
   	 function checkAll_Send(e){
   		if(e.checked){
   			$(".se").attr("checked","checked");
   		}else{
   			$(".se").removeAttr("checked");
   		}
   	 }
   	 
   	 function checkAll_Sys(e){
   		if(e.checked){
   			$(".sys").attr("checked","checked");
   		}else{
   			$(".sys").removeAttr("checked");
   		}
   	 }
   	 
	//弹出窗口关闭
   		function close(){
   			 ClosePop();  			  
   		}
	
	
	//设置邮件信息为已读
	/*function readedSends(){
 		if(!window.confirm("确定要将所选邮件标记为已读吗?")){
 			return;
 		}
 		var stIdArray = [];
		$("input[name='sendMail']:checked").each(function(){
			stIdArray.push($(this).val());
		});
		if(stIdArray.length == 0){
			alert("请选择要标记的内容");
			return ;
		}
		var ids = stIdArray.join(",");
		$.shovePost("updateSend2Readed.do",{ids:ids},function(data){
           $("#sendInfo").html(data);
        });
	}*/
	
	//设置邮件信息为未读
	/*function unReadSends(){
 		if(!window.confirm("确定要将所选邮件标记为未读吗?")){
 			return;
 		}
 		var stIdArray = [];
		$("input[name='sendMail']:checked").each(function(){
			stIdArray.push($(this).val());
		});
		if(stIdArray.length == 0){
			alert("请选择要标记的内容");
			return ;
		}
		var ids = stIdArray.join(",");
		$.shovePost("updateSend2UNReaded.do",{ids:ids},function(data){
           $("#sendInfo").html(data);
        });
	}*/
</script>
</body>
</html>
