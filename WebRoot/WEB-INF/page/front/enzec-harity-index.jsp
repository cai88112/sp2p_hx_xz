<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>恩泽慈善</title>
<style>
#body{
	width:100%;
	height:auto;
	background-image:url("images/zeen.png");
	margin:0px auto;
	background-size:100% auto;
	background-repeat:no-repeat;
	height:3300px;
	background-color: #f1f2f6;
}

#header{
	
	width:100%;
	height:48px;
	margin:0px auto;
	background-color:#24183E;
}
#header .h{
	width:950px;
	margin:0px auto;
}
#header .h .left{
	float:left;
}
#header .h .right{
	float:right;
}
#header a{
	color:#EBE9ED;	
	font-size:16px;
	line-height:45px; 
}


#bodycontent{
	width:1700px;
	height:1700px;;
	background-image:url("images/zeen.png");
	margin:0px auto;
	background-size:1700px auto;
	background-repeat:no-repeat;
}
#bodycontent .content{
	width:1050px;
	margin:0px auto;
	height:1000px;
	
}
.hui2 li{
	line-height:45px;
	width:120px; 
	background-color:#C1C6C9; 
	list-style-type: none;
	float:left;
	text-align: center;
	margin-left:1.5px;
	font-size: 16px;
	color:#70787E;
}
.hui2{
	width:45.7%;
	margin:0px auto;
	height:45px;
	
}
.leftsys{
	width:68%;
	background-color: #ffffff;
	
}
.hui2 ul{
	margin-left:-40px;margin-top:0px;
}
.current2{
	background-color:#C3DA4E !important;
}

.lsys{
	width:100%;
	height:590px;
	background-color: #ffffff;
	
}
.content .hui li{
	line-height:45px;
	width:120px; 
	background-color:#C1C6C9; 
	list-style-type: none;
	float:left;
	text-align: center;
	margin-left:1px;
	
}

.content .current{
	
	background-color:#C3DA4E !important; 
}
ul{
	list-style-type: none;
}
	
}

.showwhite{
	background-color:#FFFFFF
}
.showimg{
	background-image: url("images/blackboy.png");
	height:250px;
	width:200px;
}
.mosh ul{
	
	
	margin-left:-30px;
	margin-top:100px;
}
.mosh li{
	float:left;
	width:80px;
	line-height:25px; 
	height:25px;
	margin-left:10px;
}
.mosh{
	float:left;
	margin-left:30px;
	margin-top:25px;
}
.xiang{
border-radius:5px;border:1px solid #C3DA4E;text-align: center;color:#C3DA4E;font-size:13px;
}
.juan{
backgrborder-radius:5px;
border:1px solid #C3DA4E;
text-align: center;
font-size:13px;
background-color:#C3DA4E ;
border-radius:5px;
}
.pageno li{
	width:70px;
	float:left;
}
.contentsys{
	width:45.7%;
	margin:0px auto;
	height:2300px;
	
}

.rightysys{
  width:30%;
}


.main dl {
 margin: 0px;
 padding: 0px;
 
}
.main dt {
 
 
 float: left;
 margin: 10px 0px 10px 10px;
 display: inline;
}
.main img {
 width:175px;
 border: 0px;
}
.main dd {
 float: left;
 height: 70px;
 width: 440px;
 margin: 10px;
 color: #A7A7A7;
 font-size: 17px;
}
.whit{
	color:#ffffff;
}
.alpha{
	background-color: black;
	height:70px;
	opacity:0.3;
}
</style>
</head>
<body>
 <div id="body">
 		<div id="header">
				<div class="h">
					<div class="left">
						<a><img src="images/enzelog.png" style="width:124;height:45px;"></a>
					</div>
					<div class="right">
						<a>富壹代首页</a>&nbsp;<span ></span>&nbsp;<a>登录</a>&nbsp;<span ></span>&nbsp;<a>注册</a>
					</div>	
					
				</div>
			
		</div>
		<div style="height:680px;border:1px solid none;"></div>
		<div class="hui2">	
						<ul>
							<li class="current2">助学助养名单</li>
							<li >扶贫送温暖</li>
							<li >新建希望小学</li>
						</ul>
					</div>
		<div class="contentsys" style="background-color:#F1F2F6;">
			<div class="leftsys" style="float:left;">
				<div  style="height:25px;padding-left:20px;padding-top:35px;border-bottom: 3px solid #F1F2F6;">
					
					<span style="color:#B5BABD;">本期助学名单</span>
					
				</div>
				<div class="lsys" >
						<div class="showimg mosh">
							<div style="height:180px;"></div>
							<div class="alpha" style="opacity:">
							
							</div>
							<div style="position:relative;top:-160px;">
								<ul style="">
									<li class="whit">张三四&nbsp;男</li>
									<li class="whit">编号：001</li>
									<li class="xiang">查看详情</li>
									<li class="juan">立即捐赠</li>
								</ul>
							</div>
						</div>
							<div class="showimg mosh">
							<div style="height:180px;"></div>
							<div class="alpha" style="opacity:">
							
							</div>
							<div style="position:relative;top:-160px;">
								<ul style="">
									<li class="whit">张三四&nbsp;男</li>
									<li class="whit">编号：001</li>
									<li class="xiang">查看详情</li>
									<li class="juan">立即捐赠</li>
								</ul>
							</div>
						</div>
							<div class="showimg mosh">
							<div style="height:180px;"></div>
							<div class="alpha" style="opacity:">
							
							</div>
							<div style="position:relative;top:-160px;">
								<ul style="">
									<li class="whit">张三四&nbsp;男</li>
									<li class="whit">编号：001</li>
									<li class="xiang">查看详情</li>
									<li class="juan">立即捐赠</li>
								</ul>
							</div>
						</div>
							<div class="showimg mosh">
							<div style="height:180px;"></div>
							<div class="alpha" style="opacity:">
							
							</div>
							<div style="position:relative;top:-160px;">
								<ul style="">
									<li class="whit">张三四&nbsp;男</li>
									<li class="whit">编号：001</li>
									<li class="xiang">查看详情</li>
									<li class="juan">立即捐赠</li>
								</ul>
							</div>
						</div>
							<div class="showimg mosh">
							<div style="height:180px;"></div>
							<div class="alpha" style="opacity:">
							
							</div>
							<div style="position:relative;top:-160px;">
								<ul style="">
									<li class="whit">张三四&nbsp;男</li>
									<li class="whit">编号：001</li>
									<li class="xiang">查看详情</li>
									<li class="juan">立即捐赠</li>
								</ul>
							</div>
						</div>
							<div class="showimg mosh">
							<div style="height:180px;"></div>
							<div class="alpha" style="opacity:">
							
							</div>
							<div style="position:relative;top:-160px;">
								<ul style="">
									<li class="whit">张三四&nbsp;男</li>
									<li class="whit">编号：001</li>
									<li class="xiang">查看详情</li>
									<li class="juan">立即捐赠</li>
								</ul>
							</div>
						</div>
					
					
					
				</div>
				<div style="">
					<div  style="height:25px;padding-left:20px;padding-top:35px;border-bottom: 3px solid #F1F2F6;">
					
					<span style="color:#B5BABD;font-size:18px;">慈善专题</span>
					
					</div>
					<div>
						<img alt="" src="images/play.png" width="693px;">
					</div>
				</div>
				
				<div style="">
					<div  style="height:25px;padding-left:20px;padding-top:35px;border-bottom: 3px solid #F1F2F6;">
					
					<span style="color:#B5BABD;font-size:18px;">我在现场</span>
					
					</div>
					<div style="height:20px;"></div>
					<div>
						<img alt="" src="images/locap.png" width="690px">
					</div>
				</div>
				
					<div  >
					<div  style="height:25px;padding-left:20px;padding-top:35px;border-bottom: 3px solid #F1F2F6;">
					
					<span style="color:#B5BABD;font-size:18px;">视频</span>
					
					</div>
					<div style="height:20px;"></div>
					<div>
						<h2 style="color:#525559;">《凡人大爱之盲人IT工程师》</h2>
						<div  class="main">
							<dl>
								<dt><img alt="凡人大爱之盲人IT工程师" src="images/mang.png" ></dt>
								<dd>广东卫视《凡人大爱》栏木于7月14日播出《盲人IT工程师》，继续走进三位盲人工程师的世界。互联网无障碍投计有了他们的参于，带来的不仅是三天光明。</dd>
								<dd style="height:30px;">2014-07-16&nbsp;15:37:31更新</dd>
							</dl>	
							
							<dl>
								<dt><img alt="凡人大爱之盲人IT工程师" src="images/mang.png" ></dt>
								<dd>广东卫视《凡人大爱》栏木于7月14日播出《盲人IT工程师》，继续走进三位盲人工程师的世界。互联网无障碍投计有了他们的参于，带来的不仅是三天光明。</dd>
								<dd style="height:30px;">2014-07-16&nbsp;15:37:31更新</dd>
							</dl>
							<dl>
								<dt><img alt="凡人大爱之盲人IT工程师" src="images/mang.png" ></dt>
								<dd>广东卫视《凡人大爱》栏木于7月14日播出《盲人IT工程师》，继续走进三位盲人工程师的世界。互联网无障碍投计有了他们的参于，带来的不仅是三天光明。</dd>
								<dd style="height:30px;">2014-07-16&nbsp;15:37:31更新</dd>
							</dl>		
							<dl >
								<dt><img alt="" src="" ></dt>
								<dd></dd>
								<dd style="height:30px;"></dd>
							</dl>																													
						</div>			
					</div>
					
						
					</div>
					
			</div>
			
			<div class="rightsys" style="margin-top: -0px;margin-left: 31.5%;position:absolute;">
				<div>
					<img alt="" src="images/right1.png" style="width:330px;height:360px;">
					
				</div>	
				<div style="height:20px;"></div>
				<div>
					<img alt="" src="images/right2.png" style="width:330px;" >
					
				</div>	
				<div style="height:20px;"></div>
				<div>
					<img alt="" src="images/right3.png" style="width:330px;height:360px;">
					
				</div>	
			</div>
			
		</div>
		
 </div>
</body>
</html>