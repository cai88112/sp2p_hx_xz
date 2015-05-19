<%@page import="com.sun.org.apache.xml.internal.serialize.Printer"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@include file="taglib.jsp"%>
<link rel="shortcut icon" href="favicon.ico">
<script type="text/javascript" src="script/jquery.min.js"></script>
<style>
.top-tel a {
	font-size: 18px;
	color: #808284;
	font-family: 微软雅黑;
}

.top-tel a:hover {
	color: #f49440;
}
.top-tel span{
padding-left:20px;
}
.top-bar a {
	padding: 3px;
}

.top-bar a:hover {
	
	color: #E85801;
	
}
</style>
<!--顶部状态栏 开始-->
<div class="l_top_top">
	<div class="l_top_top_center">
		<div class="l_top_top_center_left">
			<a href="javascript:addCookie();">设为首页</a> <span>|</span> <a
				href="javascript:setHomepage();">加入收藏</a> <span>|</span> <a
				href="callcenter.do?type=true&cid=1">帮助中心</a>
		</div>
		<div class="l_top_top_center_right">
			<s:if test="#session.user !=null">
				     Hi,<a href="owerInformationInit.do">[${user.userName}]</a>
				<span></span>
				<a href="logout.do">退出</a>
			</s:if>
			<s:else>
				<a href="login.do">登录</a>
				<span>|</span>
				<a href="reg.do">注册</a>&nbsp;
				   </s:else>
			<span> | </span><a href="callcenter.do?type=true&cid=1" class="kf">客服</a>
		</div>
	</div>
</div>
<div
	style="height: 211px; width: 100%; background-color: #dfdfdf">
	<div class="top"
		style="position: relative; padding-top: 24px; width: 1000px; height: 68px;padding-bottom: 10px;">
		<a href="index.jsp"><img src="images/top-logo.png"></a>
		
		<div style="position: absolute; top: 32px; font-size: 16px; left: 210px">
			全国客服热线：
			<span style="font-size: 22px;left: 6px;top: 4px;position: relative;color: #595757">4007 182 183</span>
		</div>
		<!-- <div class="relationship" style="position: absolute; top: 8px; left: 190px">
			<img src="images/weixinwhiteblack.png">
			<div style="position: absolute; top: 30px; left: -57px; display: none; z-index: 99">
				<img src="images/weixinqr.png">
			</div>
		</div>
		<div class="relationship" style="position: absolute; top: 8px; left: 238px">
			<img src="images/qqweibowhiteblack.png">
			<div style="position: absolute; top: 30px; left: -57px; display: none; z-index: 99">
				<img src="images/qqweiboqr.png">
			</div>
		</div>
		<div class="relationship" style="position: absolute; top: 8px; left: 286px">
			<img src="images/sinaweibowhiteblack.png">
			<div style="position: absolute; top: 30px; left: -57px; display: none; z-index: 99">
				<img src="images/sinaweiboqr.png">
			</div>
		</div> -->
		<div class="top-right top-bar" style=" position: absolute;  top: 24px;  font-size: 16px;  left: 769px;">
		<%-- 您好！ <span class="red">${user.userName}</span> --%>
		<s:if test="#session.user==null">
			<span style="width: 92px;height: 40px;"><a href="home.do"
				style="border-radius: 5px;position: absolute; /* float: right; */ vertical-align: baseline; width:92px; height: 40px; font-weight: bold;font-size: 20px; text-align: center; line-height: 38px; background-color: #e85801; color: white">
				登录</a></span>
		</s:if>
		 <s:else>
		 <div style="position:absolute; left:-140px;top:14px;">
			<span style="color: #9fa0a0">欢迎您 ！</span><a href="home.do" style="vertical-align: baseline"><span
				style="padding-left: 0px; color:#e85801">${user.userName}</span></a>
				<a href="#" style="vertical-align: baseline"><span
				style="padding-left: 15px; color:#9fa0a0">消息</span></a>
			<a href="logout.do" style="vertical-align: baseline;"><span
				style="padding-left: 15px; color:#9fa0a0;">退出登录</span></a>
				</div>
		</s:else>

		<s:if test="#session.user==null">
			<span style="padding-left: 110px;width: 92px;height: 40px;"><a href="reg.do"
				style="border-radius: 5px;position: absolute; /* float: right; */ vertical-align: baseline; width:92px; height: 40px; font-weight: bold;font-size: 20px; text-align: center; line-height: 38px; background-color: #aab300; color: white">
				注册</a></span>
		</s:if>
		<!-- <span style="padding: 3px 3px 3px 15px;"><a href="#"
			style="vertical-align: baseline">财米商城</a></span> <span
			style="padding: 3px 3px 3px 15px;"><a href="#"
			style="vertical-align: baseline">财米社区</a></span> <span
			style="padding: 3px 3px 3px 15px;"><a href="#"
			style="vertical-align: baseline">壹代女生</a></span> -->
	    </div>
	</div>
	    <div style="width: 100%; background-color: #eeeeee; height: 109px">
			<div class="main"
				style="position: relative; height: 99px; width: 1000px; background-color: #eeeeee; ">
				   <div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 15px; text-align: center">
				   <a href="borrow.do" style="color: #929A9F">
				   
				       <img src="images/icona1.png" style="padding-bottom: 3px;" class="changepic" data="1"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 16px; text-align: center; left: 125px">
				   <a href="finance.do" style="color: #929A9F">
				       <img src="images/icona2.png" style="padding-bottom: 3px;" class="changepic" data="2"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 16px; text-align: center; left: 250px">
				   <a href="home.do" style="color: #929A9F">
				       <img src="images/icona3.png" style="padding-bottom: 3px;" class="changepic" data="3"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 16px; text-align: center; left: 375px">
				   <a href="getMessageBytypeId.do?typeId=4" style="color: #929A9F">
				       <img src="images/icona4.png" style="padding-bottom: 3px;" class="changepic" data="4"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 17px; text-align: center; left: 500px">
				   <!-- <a href="enzeindex.do" style="color: #929A9F"> -->
				   <a href="charity.do" style="color: #929A9F">
				       <img src="images/icona5.png" style="padding-bottom: 3px;" class="changepic" data="5"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 15px; text-align: center; left: 625px">
				   <a href="choirice.do" style="color: #929A9F">
				       <img src="images/icona6.png" style="padding-bottom: 3px;" class="changepic" data="6"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 11px; text-align: center; left: 750px">
				   <a href="choihome.do" style="color: #929A9F">
				       <img src="images/icona7.png" style="padding-bottom: 3px;" class="changepic" data="7"/>
				    </a>
				</div>
				<div style="position: absolute; font-size: 16px;  width: 12.5%; height: 80%; top: 11px; text-align: center; left: 875px">
				   <a href="goddess.do" style="color: #929A9F">
				       <img src="images/icona8.png" style="padding-bottom: 3px;" class="changepic" data="8"/>
				    </a>
				</div>
				
			</div>
		</div>

	<script>
	$(function(){
		$(".changepic").hover(						
				  function () {  
					 
					  if($(this).attr("data")=='1'){						 
						  $(this).attr("src",'images/iconb1.png');
					  }else if($(this).attr("data")=='2'){
						  $(this).attr("src",'images/iconb2.png');
					  }else if($(this).attr("data")=='3'){
						  $(this).attr("src",'images/iconb3.png');
					  }else if($(this).attr("data")=='4'){
						  $(this).attr("src",'images/iconb4.png');
					  }	else if($(this).attr("data")=='5'){
						  $(this).attr("src",'images/iconb5.png');
					  }	else if($(this).attr("data")=='6'){
						  $(this).attr("src",'images/iconb6.png');
					  }	else if($(this).attr("data")=='7'){
						  $(this).attr("src",'images/iconb7.png');
					  }	else if($(this).attr("data")=='8'){
						  $(this).attr("src",'images/iconb8.png');
					  }						  			  
				  },
				  function () {
					  if($(this).attr("data")=='1'){						 
						  $(this).attr("src",'images/icona1.png');
					  }else if($(this).attr("data")=='2'){
						  $(this).attr("src",'images/icona2.png');
					  }else if($(this).attr("data")=='3'){
						  $(this).attr("src",'images/icona3.png');
					  }else if($(this).attr("data")=='4'){
						  $(this).attr("src",'images/icona4.png');
					  }	else if($(this).attr("data")=='5'){
						  $(this).attr("src",'images/icona5.png');
					  }	else if($(this).attr("data")=='6'){
						  $(this).attr("src",'images/icona6.png');
					  }	else if($(this).attr("data")=='7'){
						  $(this).attr("src",'images/icona7.png');
					  }	else if($(this).attr("data")=='8'){
						  $(this).attr("src",'images/icona8.png');
					  }	
					  
				  }
				);
		
	});
	$(".relationship").bind("mouseover", function() {
		var imgsrc = $(this).find("img").attr("src");
		if (imgsrc == "images/weixinwhiteblack.png") {
			$(this).children("img").attr("src", "images/weixincolorful.png");
		} else if (imgsrc == "images/qqweibowhiteblack.png") {
			$(this).children("img").attr("src", "images/qqweibocolorful.png");
		} else if (imgsrc == "images/sinaweibowhiteblack.png") {
			$(this).children("img").attr("src", "images/sinaweibocolorful.png");
		}
		$(this).children("div").css("display", "inline");
	});
	$(".relationship").bind("mouseout", function() {
		var imgsrc = $(this).find("img").attr("src");
		if (imgsrc == "images/weixincolorful.png") {
			$(this).children("img").attr("src", "images/weixinwhiteblack.png");
		} else if (imgsrc == "images/qqweibocolorful.png") {
			$(this).children("img").attr("src", "images/qqweibowhiteblack.png");
		} else if (imgsrc == "images/sinaweibocolorful.png") {
			$(this).children("img").attr("src", "images/sinaweibowhiteblack.png");
		}
		$(this).children("div").css("display", "none");
	});
	</script>
	
</div>
<!-- <div style="width: 100%; background-color: #fff">
	<div class="top">
		<div class="logo">
			<a href=""><img src="images/index9_03.png" /> </a>
		</div>
		<div class="top-right">
			<div class="top-tel" style="padding-top: 45px">
				<span><a href="index.jsp">首页</a></span> <span><a
					href="finance.do">我要理财</a></span> <span><a href="borrow.do">我要借款</a></span>
				<span><a href="home.do">我的账户</a></span> <span><a
					href="capitalEnsure.do">本金保障</a></span> <span><a
					href="callcenter.do?type=true&cid=1">客服中心</a></span>
			</div>
		</div>
	</div>
</div> -->
<!--顶部主导航 结束-->
