<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />
<link rel="stylesheet" href="css/video-js.min.css" />
<script type="text/javascript" src="script/video.js"></script>
<script>
	videojs.options.flash.swf = "script/video-js.swf";
</script>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/Site2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/banner_style.css" />
<script type="text/javascript" src="script/jquery.min.js"></script>
<script type="text/javascript" src="script/modernizr.custom.js"></script>

</head>
<body>
	<%
		request.setAttribute("bbsUrl", IConstants.BBS_URL);
	%>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div id="userName" style="display: none;">${user.userName}</div>
	<div class="container">
		<div id="slides">
			<s:if test="%{#request.bannerList!=null}">
				<s:iterator value="#request.bannerList" var="banner">
					<div>
						<a  href="${banner.companyURL}" target="_blank"><img
							width="100%" src="${banner.companyImg }"></a>
					</div>
				</s:iterator>
			</s:if>
			<s:else>
				<div>
					<a id="dfdfd" href="http://www.baidu.com" target="_blank"><img
						width="100%" src="images/tr.png"></a>
				</div>
				<div>
					<a href="http://www.baidu.com" target="_blank"><img
						width="100%" src="images/tr.png"></a>
				</div>
				<div>
					<a href="http://www.baidu.com" target="_blank"><img
						width="100%" src="images/tr.png"></a>
				</div>
		
		
			</s:else>
			<a href="#" class="slidesjs-previous slidesjs-navigation">
			<i class="icon-chevron-left icon-large"></i>
			</a> 
			
			<a href="#"	class="slidesjs-next slidesjs-navigation">
			<i class="icon-chevron-right icon-large"></i>
			</a>
		</div>
		
	</div>
	<div class="main"
		style="font-size: 12px; position: relative; padding-top: 0px; padding-left: 20px;  width: 1000px;padding-bottom: 0px;">
<!-- <div style="position: absolute; width: 100px; height: 23px; /* color: #595757; */ font-weight: bold;vertical-align: middle;padding-top: 7px;">
			<img src="images/newicon.png" /><span style="vertical-align: middle;color: #595757;font-size: 15px;">&nbsp;最新公告&nbsp;&nbsp;</span>
	</div> -->
		
		 <div style="position: absolute; left: 115px; width: 20px; height: 23px;font-size: 15px;color: #efeff0;padding-top: 7px;">&nbsp;&nbsp;|</div>
		
		<div id="news" style="position: absolute; left: 145px; width: 860px; height: 30px;font-size: 15px;">
			<s:iterator value="#request.newsList" var="bean">
				<div style="display: none">
					<a href="frontNewsDetails.do?id=${bean.id}" target="_blank"
						style="vertical-align: baseline; color: #6d6e71">${bean.title}</a>
				</div>
			</s:iterator>
		</div> 
	<!-- <div style="position: absolute; left: 945px; width: 100px; height: 50px;"> -->
			<!-- <a href="initNews.do" target="_blank" style="vertical-align: baseline; float:right;
			       border: 1px solid #cfd0d1;  border-radius: 10px;  color: #595757;padding: 6px;padding-right: 10px;padding-left: 10px;font-size: 12px;">
			       查看更多</a> -->
	<!-- </div> -->
	</div>
	<div style="background-color: #f3f3f3">
	<div class="main"	style="position: relative; height: 230px; width: 1002px; background-color: #f3f3f3;padding-top: 10px;">
		
		<div style="position: absolute;  width: 222px; height: 168px;text-align: center;">
			<img src="images/left2.png" style="padding-top: 5px;"/>
			
		</div>
		
		
		
		
		<div style="position: absolute;  width: 222px; height: 168px;text-align: center;left:120px;">
			<img src="images/zhihui1.png" style="padding-top: 5px;" width="110px;" class="changezlt" data="1"/>
			<div style="font-size: 20px;padding-top: 8px;color: #e85801;font-weight: bolder;">${investMap.usernum}</div>
			<div style="font-size: 15px;color: #929a9f;">智慧的投资人加入</div>
		</div>
		
		<div style="position: absolute;  width: 222px; height: 168px;text-align: center;left:230px;">
			<img src="images/left2.png" style="padding-top: 5px;"/>
			
		</div>
		
		
		<div style="position: absolute; left: 266.6px; width: 222px; height: 168px;text-align: center;left:380px;">
			<img src="images/toup1.png" style="padding-top: 5px;" width="110px;" class="changezlt" data="2" />
			<div style="font-size: 20px;padding-top: 8px;color: #e85801;font-weight: bolder;">￥${investMap.totalInvestment}</div>
			<div style="font-size: 15px;color: #929a9f;">累计成功投资金额</div>
		</div>
		
		<div style="position: absolute;  width: 222px; height: 168px;text-align: center;left:516px;">
			<img src="images/right22.png" style="padding-top: 5px;"/>
			
		</div>
		
		<div style="position: absolute; left: 609.2px; width: 222px; height: 168px;text-align: center;">
			<!-- <img src="images/icon_income.png" style="padding-top: 10px;"  /> -->
			<img src="images/toum1.png" style="padding-top: 5px;" width="110px" class="changezlt" data="3" />
			<div style="font-size: 20px;padding-top: 8px;color: #e85801;font-weight: bolder;">￥${investMap.investSumInterest}</div>
			<div style="font-size: 15px;color: #929a9f;">投资人已赚取收益</div>
		</div>
		
		<div style="position: absolute;  width: 222px; height: 168px;text-align: center;left:735px;">
			<img src="images/right22.png" style="padding-top: 5px;"/>
			
		</div>
		<%-- <div style="position: absolute; left: 799.8px; width: 222px; height: 168px;text-align: center;">
			<img src="images/icon_totalmoney.png" style="padding-top: 10px;"  />
			<div style="font-size: 20px;padding-top: 8px;color: #e85801;font-weight: bolder;">￥${investMap.dueinfund}</div>
			<div style="font-size: 15px;color: #929a9f;">总待收金额</div>
		</div> --%>
	</div>
	</div>
	
	
	<div style="background-color: #f3f3f3">
		<div class="main"	style="position: relative; height: 590px; width: 100%;background-color: #f3f3f3;padding-top: 10px;background-image: url('images/movpic2.png');background-repeat: no-repeat;background-size:100%;">
			
			<div style="position:relative;left:475px;top:328px;">
			<a href="javascript:;"><span style="border-radius:4px;padding: 7px 18px;font-size:15px;letter-spacing:4px;background-color: #DBE000;box-shadow: 0px 2px 2px #888888;">立即登录</span></a>
			<a href="javascript:;"><span style="border-radius:4px;padding: 7px 18px;font-size:15px;letter-spacing:4px;background-color: #6BCB9F;box-shadow: 0px 2px 2px #888888;">免费注册</span></a>
			</div>
		<!-- <div style="position: absolute;  width: 222px; height: 168px;text-align: center;">
			<img src="images/movpic2.png" style="padding-top: 5px;"/>
			
		</div> -->
			<s:if test="#request.activityStatus =='true' ">
				<div class="main"
					style="position: relative; height: 80px; width: 1020px; border-color: #DDDDDD; border-width: 1px; border-style: solidp;margin-top:400px;">
					<div style="position: absolute; width: 1020px; height: 80px">
						<img src="images/activityBanner-data.png" width="100%" />
					</div>
				</div>
			</s:if>
			<s:else>
				<div class="main"
					style="position: relative; width: 1020px; font-size: 15px;padding-bottom: 22px;margin-top:389px;">

					<div style="height: 50px">
						<dl>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 35%; float: left; text-align: center">借款标题</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 15%; float: left; text-align: center">金额</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 15%; float: left; text-align: center">年利率</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 10%; float: left; text-align: center">期限</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 10%; float: left; text-align: center">进度</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 15%; float: left; text-align: center">状态</dd>
						</dl>
					</div>

					<dl>
						<s:iterator value="#request.activityBorrowList" var="finance">
							<div
								style="height: 50px; /* border-bottom: solid; border-width: 1px; border-color: #F5F5F5 */">
								<dd style="padding-left: 20px; width: 33%;height: 67px; float: left;background-color: #bae3f9;">
									<img src="images/img_littepig.png" style="top: 16px;position: relative;" class="changehuo" />
									<a href="financeDetail.do?id=${finance.id}" target="_blank" style="top: 15px;position: relative;">${finance.borrowTitle}</a>
								</dd>
								<dd style="width: 15%;height: 67px; float: left; text-align: center;background-color: #bae3f9;">
									<img src="images/img_reward1.png" style="top: 2px;left:200px;position: relative;" />
									<span style="top: 22px;position: relative;">${finance.borrowAmount}</span>
								</dd>
								<dd
									style="width: 15%;height: 67px; float: left; text-align: center; color: #ff7400;;background-color: #bae3f9;">
									
									<span style="top: 22px;position: relative;">${finance.annualRate}%</span></dd>
								<dd
									style="width: 10%;height: 67px; float: left; text-align: center;background-color: #bae3f9;">
									<span style="top: 22px;position: relative;">${finance.deadline}
									
									<s:if
										test="%{#finance.isDayThe ==1}">个月</s:if>
									<s:else>天</s:else>
									</span>
								</dd>
								<dd
									style="width: 10%;height: 67px; float: left; text-align: center;background-color: #bae3f9;vertical-align: middle;">
										
										<div class="divProcess"><canvas  width="40px" height="40px" class="process" style="top: 12px;position: relative;" ><fmt:formatNumber type="number" value="${finance.schedules}"
										maxFractionDigits="0" />%</canvas></div>
										<img class ="imgCanvas" data-process="${finance.schedules}" style="display:none;" src="" alt=""  width="40px" height="40px"/>
								</dd>
								<dd
									style="width: 15%;height: 67px; float: left; text-align: center;background-color: #bae3f9;">
									<div class="btn5">
										<s:if test="%{#finance.borrowStatus == 1}">
											<span style="top: 22px;position: relative;">初审中</span>
										</s:if>
										<s:elseif test="%{#finance.borrowStatus == 2}">
											<s:if test="%{#finance.borrowShow == 1}">
												<a href="javascript:void(0);"  class="changecolor" 
													onclick="checkTou(${finance.id},1)" style="top: 18px;position: relative;background: #E85801;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">立即投标</a>
											</s:if>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 3}">
										<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													复审中</span>
								</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 4}">
											<s:if test="%{#finance.borrowShow == 2}">
											<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													回购中</span>
											</s:if>
											<s:else>
											<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													还款中</span>
											</s:else>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 5}">
										<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													已还完</span>
								</s:elseif>
										<s:else><span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													流标</span></s:else>
									</div>
								</dd>
							</div>
						</s:iterator>
					</dl>
				</div>

			</s:else>
		
		</div>
	</div>
	
<%-- 	<div style="background-color: #f3f3f3">
	<div class="main" style="position: relative; height: 296px; width: 1022px;padding-top: 15px;background-color: #f3f3f3;">	
	<s:if test="#request.activityBorrowList.size>0">
		<img src="images/img_bluelogo1.png" style="left: 474px;position: absolute;top: 258px;">					
	</s:if>				
	<div style="position: absolute;background-image: url(images/img_tvshow.png);height: 294px; width: 358px;"> 
	<div style="position: absolute;  width: 275px; height: 187px; background-color: black;left: 44px;top: 58px;">
			<video id="example_video_1" class="video-js vjs-default-skin"
				controls autoplay="autoplay" width="275" height="181"
				poster="http://video-js.zencoder.com/oceans-clip.png"
				data-setup="{}"> <source
				src="http://video-js.zencoder.com/oceans-clip.mp4" type='video/mp4' />
			<source src="http://video-js.zencoder.com/oceans-clip.webm"
				type='video/webm' /> <source
				src="http://video-js.zencoder.com/oceans-clip.ogv" type='video/ogg' />
			<track kind="captions" src="demo.captions.vtt" srclang="en"
				label="English"></track><!-- Tracks need an ending tag thanks to IE9 -->
			<track kind="subtitles" src="demo.captions.vtt" srclang="en"
				label="English"></track><!-- Tracks need an ending tag thanks to IE9 -->
			</video>
		</div>
		</div> 
		<a href="#">
		<div class="bannerWord"
			style="position: absolute; width: 254px; height: 238px;left:342px;">
			<img src="images/img_investment.png"
				style="position: absolute; left: 83px; top: 75px" />
			<div
				style="position: absolute; top: 170px; width: 254px; text-align: center; font-size: 25px;color: #595757;">投资理财</div>
			<div
				style="position: absolute; left: 20px; top: 205px; width: 214px; font-size: 15px;text-align: center;font-size: 12px;">年18%超高收益</div>
		</div>
		</a>
		
		<a href="#">
		<div class="bannerWord"
			style="position: absolute; left: 598px; width: 254px; height: 238px">
			<img src="images/img_safety.png"
				style="position: absolute; left: 83px; top: 75px" />
			<div
				style="position: absolute; top: 170px; width: 254px; text-align: center; font-size: 25px;color: #595757;">安全保障</div>
			<div
				style="position: absolute; left: 20px; top: 205px; width: 214px; font-size: 15px;text-align: center;font-size: 12px;">100%保本保息</div>
		</div>
		</a>
<!-- 		<div
			style="position: absolute; left: 510px; top: 105px; width: 2px; height: 115px; background-color: #ccc">
		</div> -->
		<a href="#">
		<div class="bannerWord"
			style="position: absolute; left: 844px; width: 254px; height: 238px">
			<img src="images/img_security.png"
				style="position: absolute; left: 83px; top: 75px" />
			<div
				style="position: absolute; top: 170px; width: 254px; text-align: center; font-size: 25px;color: #595757;">技术保障</div>
			<div
				style="position: absolute; left: 20px; top: 205px; width: 214px; font-size: 15px;text-align: center;font-size: 12px;">银行级技术保障</div>
		</div>
		</a>

	</div>
	</div> --%>
	
	<script>
	$(function(){
		$(".changezlt").hover(
			function(){
				if($(this).attr("data")=='1'){
					 $(this).attr("src",'images/zhihui2.png');
				}else if($(this).attr("data")=='2'){
					 $(this).attr("src",'images/toup2.png');
				}else{
					 $(this).attr("src",'images/toum2.png');
				}
				
			},	
			function(){
				if($(this).attr("data")=='1'){
					 $(this).attr("src",'images/zhihui1.png');
				}else if($(this).attr("data")=='2'){
					 $(this).attr("src",'images/toup1.png');
				}else{
					 $(this).attr("src",'images/toum1.png');
				}
			}
		
		);
	});
	$(".changecolor").hover(
			
		function(){
			$(this).css('background-color', '#E8380D');
			
		},
		function(){
			$(this).css('background-color', '#E85801');
		}
	);
	
	$(".bannerWord").bind("mouseover", function() {
		var imgsrc = $(this).find("img").attr("src");
		if (imgsrc == "images/invest.jpg") {
			$(this).find("img").attr("src", "images/invest_s.jpg");
		} else if (imgsrc == "images/security.jpg") {
			$(this).find("img").attr("src", "images/security_s.jpg");
		} else if (imgsrc == "images/technique.jpg") {
			$(this).find("img").attr("src", "images/technique_s.jpg");
		}
	})
	
	$(".bannerWord").bind("mouseout", function() {
		var imgsrc = $(this).find("img").attr("src");
		if (imgsrc == "images/invest_s.jpg") {
			$(this).find("img").attr("src", "images/invest.jpg");
		} else if (imgsrc == "images/security_s.jpg") {
			$(this).find("img").attr("src", "images/security.jpg");
		} else if (imgsrc == "images/technique_s.jpg") {
			$(this).find("img").attr("src", "images/technique.jpg");
		}
	})
	</script>
	<div>
		<s:if
			test="%{#request.mapNoviceist.id !=null && #request.mapNoviceist.id != ''}">
			<div class="main"
				style="position: relative; padding-top: 7px; padding-left: 20px; height: 23px; width: 1000px; border-color: #DDDDDD; border-width: 1px; border-style: solid">
				<div
					style="position: absolute; width: 105px; height: 23px; color: #BB0119; font-size: 18px;">新手体验标</div>
				<div
					style="position: absolute; left: 130px; width: 870px; height: 23px; padding-top: 5px">
					更多惊喜等你来拿</div>
			</div>
			<div class="main"
				style="position: relative; height: 237px; width: 1020px; border-color: #DDDDDD; border-width: 1px; border-style: solid">
				<div style="position: absolute; width: 510px; height: 247px">
					<img src="images/smallbanner.jpg" width="100%" />
				</div>
				<div
					style="position: absolute; left: 575px; top: 30px; width: 870px; height: 23px; font-size: 15px">
					已有<font style="color: #E9AB00; font-size: 20px">${mapNoviceist.investorNum}</font>人加入&nbsp;&nbsp;&nbsp;<font
						style="color: #E9AB00; font-size: 15px">预计年化收益率：${mapNoviceist.annualRate}%</font>&nbsp;&nbsp;&nbsp;<a
						style="vertical-align: baseline"><font
						style="color: #E9AB00; font-size: 15px">了解活动详情</font></a>
				</div>
				<div
					style="position: absolute; left: 550px; top: 75px; width: 445px; height: 120px; border-color: #DDDDDD; border-width: 1px; border-style: solid; border-left-style: none; border-right-style: none">
					<div
						style="position: absolute; left: 25px; top: 25px; width: 445px; height: 120px; font-size: 15px">
						最低万份收益<font style="color: #EC1B21; font-size: 50px">${mapNoviceist.annualAmount}元</font>
					</div>
					<div
						style="position: absolute; left: 280px; top: 45px; width: 120px; height: 35px; padding-top: 5px; font-size: 20px; background-color: #EBAB03; color: white; text-align: center">
						<a href="financeInvestInit.do?id=${mapNoviceist.id}">立刻投资</a>
					</div>
				</div>
				<div
					style="position: absolute; left: 890px; top: 205px; width: 870px; height: 23px; font-size: 15px">
					当前可投<font style="color: #E9AB00; font-size: 15px">${mapNoviceist.canInvest}</font>元
				</div>
			</div>
		</s:if>
	</div>

	<%-- <div style="background-color: #7ecef4;" id="gg">
	
	
		<s:if test="#request.activityBorrowList.size>0">
			<!--<div class="main"
				style="position: relative; padding-top: 7px; padding-left: 20px; height: 23px; width: 1000px; border-color: #DDDDDD; border-width: 1px; border-style: solid">
	 			<div
					style="position: absolute; width: 105px; height: 23px; color: #BB0119; font-size: 18px;">活动标</div>
				<div
					style="position: absolute; left: 130px; width: 870px; height: 23px; padding-top: 5px">
					更多惊喜等你来拿</div> 
			</div>-->
			<div class="main"
				style="position: relative; height: 360px; width: 1020px; /* border-color: #DDDDDD; border-width: 1px; border-style: solid */">
				<img src="images/img_bluelogo1.png" style="left: 473px;position: absolute;top: -53px;">
				<div style="position: absolute;top: 51px;" >
				<img src="images/img_activity.png"/>					
				</div>
				<div style="position: absolute;top:145px;left:122px;">
				<img src="images/img_activity_article.png" />					
				</div>
				<div style="position: absolute;top:277px;left:122px;background: #fbd542; 
    				height: 55px;width:149px;">
				<a href="login.do"  style="float:left; middle;color:#584917;padding-left: 43px;padding-top: 17px; font-size: 19px;display: block;text-align: center;">立即登录</a>				
				</div>
				<div style="position: absolute;top:277px;left:271px;background: #fbd542; 
    				height: 55px;width:2px;">
				<span style="position: relative;top: 15px;font-size: 20px;">|</span>
				</div>
				<div style="position: absolute;top:277px;left:273px;background: #fbd542; 
    				height: 55px;width:149px;">
				 <a href="reg.do" style="float:right; middle; color:#584917;padding-right: 43px;padding-top: 17px; font-size: 19px;display: block;text-align: center;">免费注册</a>
				</div>
				<div style="position: absolute;left: 600px;top: 51px;">
				<img src="images/img_bidding.png"  />
					
				</div>
				
			</div>
			<s:if test="#request.activityStatus =='true' ">
				<div class="main"
					style="position: relative; height: 80px; width: 1020px; border-color: #DDDDDD; border-width: 1px; border-style: solid">
					<div style="position: absolute; width: 1020px; height: 80px">
						<img src="images/activityBanner-data.png" width="100%" />
					</div>
				</div>
			</s:if>
			<s:else>
				<div class="main"
					style="position: relative; width: 1020px; font-size: 15px;padding-bottom: 22px;">

					<div style="height: 50px">
						<dl>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 35%; float: left; text-align: center">借款标题</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 15%; float: left; text-align: center">金额</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 15%; float: left; text-align: center">年利率</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 10%; float: left; text-align: center">期限</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 10%; float: left; text-align: center">进度</dd>
							<dd
								style="padding: 15px 0; background-color: #9fd9f6; width: 15%; float: left; text-align: center">状态</dd>
						</dl>
					</div>

					<dl>
						<s:iterator value="#request.activityBorrowList" var="finance">
							<div
								style="height: 50px; /* border-bottom: solid; border-width: 1px; border-color: #F5F5F5 */">
								<dd style="padding-left: 20px; width: 33%;height: 67px; float: left;background-color: #bae3f9;">
									<img src="images/img_littepig.png" style="top: 16px;position: relative;" />
									<a href="financeDetail.do?id=${finance.id}" target="_blank" style="top: 15px;position: relative;">${finance.borrowTitle}</a>
								</dd>
								<dd style="width: 15%;height: 67px; float: left; text-align: center;background-color: #bae3f9;">
									<img src="images/img_reward1.png" style="top: 2px;left:119px;position: relative;" />
									<span style="top: 22px;position: relative;">${finance.borrowAmount}</span>
								</dd>
								<dd
									style="width: 15%;height: 67px; float: left; text-align: center; color: #ff7400;;background-color: #bae3f9;">
									
									<span style="top: 22px;position: relative;">${finance.annualRate}%</span></dd>
								<dd
									style="width: 10%;height: 67px; float: left; text-align: center;background-color: #bae3f9;">
									<span style="top: 22px;position: relative;">${finance.deadline}
									
									<s:if
										test="%{#finance.isDayThe ==1}">个月</s:if>
									<s:else>天</s:else>
									</span>
								</dd>
								<dd
									style="width: 10%;height: 67px; float: left; text-align: center;background-color: #bae3f9;vertical-align: middle;">
										
										<div class="divProcess"><canvas  width="40px" height="40px" class="process" style="top: 18px;position: relative;" ><fmt:formatNumber type="number" value="${finance.schedules}"
										maxFractionDigits="0" />%</canvas></div>
										<img class ="imgCanvas" data-process="${finance.schedules}" style="display:none;" src="" alt=""  width="40px" height="40px"/>
								</dd>
								<dd
									style="width: 15%;height: 67px; float: left; text-align: center;background-color: #bae3f9;">
									<div class="btn5">
										<s:if test="%{#finance.borrowStatus == 1}">
											<span style="top: 22px;position: relative;">初审中</span>
										</s:if>
										<s:elseif test="%{#finance.borrowStatus == 2}">
											<s:if test="%{#finance.borrowShow == 1}">
												<a href="javascript:void(0);"
													onclick="checkTou(${finance.id},1)" style="top: 18px;position: relative;background: #e66432;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">立即投标</a>
											</s:if>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 3}">
										<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													复审中</span>
								</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 4}">
											<s:if test="%{#finance.borrowShow == 2}">
											<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													回购中</span>
											</s:if>
											<s:else>
											<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													还款中</span>
											</s:else>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 5}">
										<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													已还完</span>
								</s:elseif>
										<s:else><span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													流标</span></s:else>
									</div>
								</dd>
							</div>
						</s:iterator>
					</dl>
				</div>

			</s:else>
		</s:if>
	</div> --%>
	<div style="background-color: #f2f2f2;background-image: url('images/youlicai8.jpg');background-repeat: no-repeat;height:727px;background-size:100%;">
		<div class="main"
			style="position: relative; height: 250px; width: 1020px">
			<!-- <img src="images/img_youlicai.png" style="left:380px;top:26px;position: relative;"></img> -->
			<%-- <img src="images/img_suxian.png" style="left:120px;top:26px;position: relative;"></img>
			<img src="images/img_youlicai_art.png" style="left:189px;top:26px;position: relative;"></img>
			<div
				style="position: absolute;width: 548px;height: 28px;text-align: center;left: 436px;font-size: 25px;top: 105px;">
				本月累计投资<span style="color:#e85801">${investMap.investSumInPeriod}</span>
			</div> --%>
			<!-- <div
				style="position: absolute; width: 1020px; height: 40px; text-align: center; font-size: 30px; padding-top: 20px">
				我们精心为您准备了以下投资理财项目</div> -->
		</div>
<%-- 		<div class="main"
			style="position: relative; height: 60px; width: 1020px">
			<div
				style="position: absolute; width: 1020px; height: 40px; text-align: center; font-size: 20px; padding-top: 20px">
				本月累计投资<span>${investMap.investSumInPeriod}</span>
			</div>
		</div --%>>
		<div class="main"
			style="position: relative; height: 85px; width: 805px">
			<!-- <div dataIndex="true" onmouseover="blackTab(this);"
				onmouseout="whiteTab(this);" onclick="clickTab(this);"
				style="cursor: pointer; position: absolute; width: 200px; height: 60px">
				<img src="images/tzxm.jpg" style="width: 100%"></img>
			</div> -->
			<div id="houseBorrow" onmouseover="blackTab(this);"
				onmouseout="whiteTab(this);" onclick="clickTab(this);"
				style="cursor: pointer; position: absolute; height: 90px;  ">
				<img src="images/img_fufangdai.png" style="height: 82%" class="changeyoufu" data="1"></img>
			</div>
			<div id="carBorrow" onmouseover="blackTab(this);"
				onmouseout="whiteTab(this);" onclick="clickTab(this);"
				style="cursor: pointer; position: absolute; left: 270px; height: 90px;">
				<img src="images/img_fuchedai.png" style="height: 82%" class="changeyoufu" data="2"></img>
			</div>
			<div id="fuyubao" onmouseover="blackTab(this);" onmouseout="whiteTab(this);"
				onclick="clickTab(this);"
				style="cursor: pointer; position: absolute; left: 540px; height: 90px;">
				<img src="images/img_fuyubao.png" style="height: 82%" class="changeyoufu" data="3"></img>
			</div>
		</div>
		<script>
		$(function(){
			$(".changeyoufu").hover(
				function(){
					if($(this).attr("data")=='1'){						 
						  $(this).attr("src",'images/img_fufangdai2.png');
					}else if($(this).attr("data")=='2'){						 
						  $(this).attr("src",'images/img_fuchedai2.png');
					}else{
						 $(this).attr("src",'images/img_fuyubao2.png');
					}
				},
				function(){
					if($(this).attr("data")=='1'){						 
						  $(this).attr("src",'images/img_fufangdai.png');
					}else if($(this).attr("data")=='2'){						 
						  $(this).attr("src",'images/img_fuchedai.png');
					}else{
						 $(this).attr("src",'images/img_fuyubao.png');
					}
				}
			);
		});
	var whiteTab = function(myTab) {
		var index = $(myTab).attr("dataIndex");
		if (index != "true") {
			var imgsrc = $(myTab).find("img").attr("src");
			if (imgsrc == "images/tzxm_s.jpg") {
				$(myTab).find("img").attr("src", "images/tzxm.jpg");
			} else if (imgsrc == "images/ffd_s.jpg") {
				$(myTab).find("img").attr("src", "images/ffd.jpg");
			} else if (imgsrc == "images/fcd_s.jpg") {
				$(myTab).find("img").attr("src", "images/fcd.jpg");
			} else if (imgsrc == "images/fyb_s.jpg") {
				$(myTab).find("img").attr("src", "images/fyb.jpg");
			}
		}
	}
	
	var blackTab = function(myTab) {
		var index = $(myTab).attr("dataIndex");
		if (index != "true") {
			var imgsrc = $(myTab).find("img").attr("src");
			if (imgsrc == "images/tzxm.jpg") {
				$(myTab).find("img").attr("src", "images/tzxm_s.jpg");
			} else if (imgsrc == "images/img_fufangdai.png") {
				$(myTab).find("img").attr("src", "images/img_fufangdai.png");
			} else if (imgsrc == "images/img_fuchedai.png") {
				$(myTab).find("img").attr("src", "images/img_fuchedai.png");
			} else if (imgsrc == "images/img_fuyubao.png") {
				$(myTab).find("img").attr("src", "images/img_fuyubao.png");
			}
		}
	}
	
	var clickTab = function(myTab) {
		$(myTab).attr("dataIndex", "true");
		$(myTab).siblings().each(function(index, element) {
			$(element).attr("dataIndex", "");
			whiteTab(element);
		});
		var param = {};
		if(myTab.id == "houseBorrow"){
			$("#otherBorrow").attr("style","display:none;");
			param["subject_matter"] = 2;

			$.shovePost('queryLastestCarOrHouseBorrow.do', param, function(data) {
				$('#allBorrow').html(data);
				$("#otherBorrow").html("");
				drawProcess();
			}); 
		}else if(myTab.id == "carBorrow"){
			$("#otherBorrow").attr("style","display:none;");
			param["subject_matter"] = 1;

			$.shovePost('queryLastestCarOrHouseBorrow.do', param, function(data) {
				$('#allBorrow').html(data);
				$("#otherBorrow").html("");
				drawProcess();
			}); 
		}else if(myTab.id == "fuyubao"){
			$('#allBorrow').html("<div text-align='center'>筹备中...</div>");
			$("#otherBorrow").attr("style","display:none;");
			$("#otherBorrow").html("");
		}
		
		else{
			$('#allBorrow').html("");
			$("#otherBorrow").attr("style","display:block;");
		}
	
		
	}
	</script>
		<div class="main"
			style="position: relative; width: 805px; font-size: 15px">
			<div style="height: 50px">
				<dl>
					<dd
						style="padding: 15px 0; background-color: #dde2e6; width: 35%; float: left; text-align: center">借款标题</dd>
					<dd
						style="padding: 15px 0; background-color: #dde2e6; width: 13%; float: left; text-align: center">金额</dd>
					<dd
						style="padding: 15px 0; background-color: #dde2e6; width: 14%; float: left; text-align: center">年利率</dd>
					<dd
						style="padding: 15px 0; background-color: #dde2e6; width: 10%; float: left; text-align: center">期限</dd>
					<dd
						style="padding: 15px 0; background-color: #dde2e6; width: 10%; float: left; text-align: center">进度</dd>
					<dd
						style="padding: 15px 0; background-color: #dde2e6; width: 17.3%; float: left; text-align: center">状态</dd>
				</dl>
			</div>
			<div id="allBorrow"></div>
			<div id="otherBorrow">
				<s:if test="#request.mapList.size>0">
					<dl>
						<s:iterator value="#request.mapList" var="finance">
							<div
								style="height: 67px; border-bottom: solid; border-width: 1px; border-color: #dde2e6">
								<dd style="padding-left: 20px;  width: 33%; height: 67px; float: left; background-color: #ffffff;">
								<img src="images/img_littegraypig.png" style="top: 16px;position: relative;" class="changehuo"/>
									<a href="financeDetail.do?id=${finance.id}" target="_blank"  style="top: 17px;position: relative;">${finance.borrowTitle}</a>
								</dd>
								<dd style=" width: 13%; height: 67px; float: left; text-align: center; background-color: #ffffff;">
									
									<span style="top: 22px;position: relative;">${finance.borrowAmount}</span>
								</dd>
								<dd	style=" width: 14%; height: 67px;float: left; text-align: center; color: #ff7400; background-color: #ffffff;margin-left:-23px;">
									<img src="images/img_reward1.png" style="top: 2px;left:70px;position: relative;" />
									<span style="top: 22px;position: relative;">${finance.annualRate}%</span>
								</dd>
								<dd
									style=" width: 15%; height: 67px;float: left; text-align: center; background-color: #ffffff;">
									<span style="top: 22px;position: relative;">
									${finance.deadline}
									<s:if test="%{#finance.isDayThe ==1}">个月</s:if>
									<s:else>天</s:else>
									</span>
								</dd>
								<dd style=" width: 10%; height: 67px;float: left; text-align: center; background-color: #ffffff;margin-left:-20px;">
									
										<div class="divProcess"><canvas class="process" width="40px" height="40px" style="top: 18px;position: relative;"><fmt:formatNumber
										type="number" value="${finance.schedules}"
										maxFractionDigits="0" />%</canvas></div>
										<img class ="imgCanvas" data-process="${finance.schedules}" style="display:none;" src="" alt=""  width="40px" height="40px"/>
								</dd>
								<dd
									style=" width: 15%; height: 67px;float: left; text-align: center; background-color: #ffffff;">
									<div class="btn5">
										<s:if test="%{#finance.borrowStatus == 1}">
											初审中
										</s:if>
										<s:elseif test="%{#finance.borrowStatus == 2}">
											<s:if test="%{#finance.borrowShow == 1}">
											<a href="javascript:void(0);"  class="changecolor"
													onclick="checkTou(${finance.id},1)" style="top: 18px;position: relative;background: #e66432;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">立即投标</a>
										
											</s:if>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 3}">
									<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													复审中</span>
								</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 4}">
											<s:if test="%{#finance.borrowShow == 2}">
											<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													回购中</span>
											</s:if>
											<s:else>
											<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													还款中</span>
											</s:else>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 5}">
									<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													已还完</span>
								</s:elseif>
										<s:else>
										<span style="top: 18px;position: relative;background: #c1c6c9;left: 29px;  color: white;  width: 100px;  display: block;  text-align: center;  
													height: 30px;  vertical-align: middle;  float: left;  line-height: 30px;  font-size: 14px;  border-radius: 4px; ">
													流标</span>
										</s:else>
									</div>
								</dd>
							</div>
						</s:iterator>
					</dl>
				</s:if>
			</div>
		</div>
		<div class="main"
			style="height: 88px; width: 1020px; text-align: center; padding-bottom: 19px;">
			<div
				style="width: 200px; height: 20px; padding-top: 20px; margin-left: auto; margin-right: auto; font-size: 15px;padding-bottom: 11px;">
		
				<a href="finance.do" target="_blank" class="changesee"  style="vertical-align: baseline;margin: 40px auto ! important;  
			       border: 1px solid #cfd0d1;  border-radius: 10px;  color: #595757;padding: 6px;padding-right: 10px;padding-left: 10px;font-size: 12px;">
					<img src="images/img_moreload.png" />
					<span >查看更多投资项目列表</span></a>
			</div>
			<!-- <div style="height: 77px; width: 1020px; text-align: center;background-color: #f2f2f2;">
			<img src="images/img_bluelogo1.png" style="padding-left: 32px;padding-top: 3px;" />
			</div> -->
		</div >
		<div></div>
	</div>
	
	<div style="background-color: #f2f2f2;background-image: url('images/wincoo.png');background-repeat: no-repeat;height:580px;background-size:100%;">
	</div>
<%-- 	<div style="background-color: #7ecef4;">
	<div class="main"
		style="position: relative; height: 160px; width: 1020px;background-color: #7ecef4">
		<img src="images/img_bluelogo1.png" style="left: 473px;position: absolute;top: -53px;" />
		<img src="images/img_meitibaodao.png" style="padding-top: 40px;" />
		
	</div>

	<div class="main"
		style="position: relative; height: 367px; width: 1020px">
		<div
			style="position: absolute;left: 711px;width: 400px;height: 187px;top: 27px;">
			<img src="images/img_meitiwin.png" style="width: 290px;" />
		</div>
		<div
			style="position: absolute; left: 55px; width: 500px; height: 187px; line-height: 25px">
			<div style="padding-bottom: 5px;margin-bottom: 5px;padding-left: 71px;color: #fff;padding-top: 60px;font-size: 13px">
				富壹代是在“平台经济、平台战略”理念引领下，在“市场主导、共建平台、协作唱戏”的背景下，在“中国旅游文化资源开发促进会”等政府、事业单位的关怀指导下，由“中国服务业发展中心”、“VGM集团”等众多资源型、实力超群的企业集团联合发起设立的、有政府、金融、社团、产业、企业各价值创造主体共同投资控股的一站式、超级产融结合跨界互联网金融服务平台，由深圳市富爸爸投资发展有限公司独立开发及运营。
				<s:iterator value="#request.newsMediaList" var="beans">

					<a href="frontNewsDetails.do?id=${beans.id}" target="_blank"
						style="vertical-align: baseline"><font style="color: blue"></font>
						</a>
						${beans.content}
					
			</s:iterator>
				<div style="float: right; color: #CABDB2">${beans.publishTime}</div>
			</div>
			<a href="frontNewsDetails.do?id=${beans.id}">
				<div
					style="position: absolute;left: 71px;top: 262px;width: 144px;height: 40px;line-height: 40px;background-color: #e85801;color: white;text-align: center;border-radius: 8px;font-size: 16px;">
					查看详情</div>
			</a>
		</div>

		
	</div>
	</div> --%>
<!-- 	<div style="background-color: #f2f2f2">
		<div class="main"
			style="position: relative; height: 60px; width: 1020px">
			<div
				style="position: absolute; width: 1020px; height: 40px; text-align: center; font-size: 30px; padding-top: 20px">
				关于富壹代</div>
		</div>
		<div class="main"
			style="position: relative; height: 60px; width: 1020px">
			<div
				style="position: absolute; width: 1020px; height: 40px; text-align: center; font-size: 20px; padding-top: 20px">
				安全透明、全方位保障投资者安全</div>
		</div>
		<div class="main"
			style="position: relative; height: 500px; width: 1020px">
			<div
				style="position: absolute; left: 55px; width: 400px; height: 187px">
				<img src="images/img_guanyu.png" style="width:410px; height: 417px;padding-top: 50px;" />
			</div>
			

			<div style="position: absolute; left: 540px; width: 408px; height: 187px; line-height: 30px">
				<div style="width:355px; height: 129px;padding-top: 47px;padding-bottom: 27px;">
				<img src="images/img_abouttxt.png" style="width:355px; height: 129px;" />
				</div>
				<span style="padding-top: 47px;">
				富壹代是在“平台经济、平台战略”理念引领下，在“市场主导、共建平台、协作唱戏”的背景下，在“中国旅游文化资源开发促进会”等政府、事业单位的关怀指导下，由“中国服务业发展中心”、“VGM集团”等众多资源型、实力超群的企业集团联合发起设立的、有政府、金融、社团、产业、企业各价值创造主体共同投资控股的一站式、超级产融结合跨界互联网金融服务平台，由深圳市富爸爸投资发展有限公司独立开发及运营。
				</span>
			</div>
			<a href="getMessageBytypeId.do?typeId=4">
				<div style="position: absolute; left: 838px; top: 409px; width: 130px; height: 36px;
							 line-height: 36px; background-color: #e85801; color: white; text-align: center;font-size: 16px;border-radius: 4px">
					查看详情
				</div>
				
			</a>
		</div>
		<div class="banner" style="width: 100%; height: 439px;background-color: #fff;border-color: #fff;border-bottom-width: 31px;">
			<div style=" text-align:center;">
 				 <center>
 				 <img src="images/img_yellowlogo.png" style="position: absolute;top: -63px;left: 50%;margin-left: -52px;" />
 				 </center>	
			</div>
			<img src="images/img_welcomehome.png" width="100%" style="border-color: #f7ab00;top: 122px;" /> 
			
			<a href="#" style="left: 44.6%;  top: 68%;width: 133px;  height: 36px;">
				<div style="position: absolute; left: 44.6%; top: 68%; width: 133px; height: 36px;
							 line-height: 36px; /* background-color: #e85801; */ color: white; text-align: center;font-size: 16px;border-radius: 4px">
					
				</div>
				
			</a>
		</div>
	</div> -->
<!-- 	<div class="main"
		style="width: 100%; height: auto;padding-bottom: 40px;padding-top: 0px;">
		<img src="images/img_hezuotxt.png" style="padding-left: 297px;"></img>
		
		<div
			style="position: absolute; width: 1020px; height: 40px; text-align: center; font-size: 30px; padding-top: 20px">
			合作共赢</div>
	</div> -->

	<!-- <div class="main"
		style="position: relative; height: 363px; width: 1020px">
		<div style="position: absolute; width: 1020px; height: 266px">
			<img src="images/friends.jpg" width="100%" />
		</div>
		<img src="images/img_bluelogo.png" style="left: 473px;position: absolute;top: 309px;">
	</div> -->
<%-- 	<div class="main" style="width: 1020px">
		<div style="width: 440px; font-size: 15px; float: left">
			<div style="padding-bottom: 5px; margin-bottom: 5px">
				最新公告
				<div style="float: right">
					<a href="initNews.do" target="_blank"
						style="vertical-align: baseline; color: blue">更多>></a>
				</div>
			</div>
			<s:iterator value="#request.newsList" var="bean">
				<div style="padding-bottom: 5px; margin-bottom: 5px">
					<a href="frontNewsDetails.do?id=${bean.id}" target="_blank"
						style="vertical-align: baseline"><font style="color: blue">></font>
						${bean.title}</a>
					<div style="float: right; color: #CABDB2">2014-09-22</div>
				</div>
			</s:iterator>
		</div>
		<div style="left: 510px; width: 440px; font-size: 15px; float: right">
			<div style="padding-bottom: 5px; margin-bottom: 5px">
				最新公告
				<div style="float: right">
					<a href="initNews.do" target="_blank"
						style="vertical-align: baseline; color: blue">更多>></a>
				</div>
			</div>
			<s:iterator value="#request.newsList" var="bean">
				<div style="padding-bottom: 5px; margin-bottom: 5px">
					<a href="frontNewsDetails.do?id=${bean.id}" target="_blank"
						style="vertical-align: baseline"><font style="color: blue">></font>
						${bean.title}</a>
					<div style="float: right; color: #CABDB2">2014-09-22</div>
				</div>
			</s:iterator>
		</div>
	</div> --%>
<!-- 	<div style="background-color: #7ecef4;">
		<div class="main" style="position: relative; height: 500px; width: 1020px;">
		<img src="images/img_bluelogo.png" style="left: 473px;position: absolute;top: -55px;">
			<div style="position: absolute;  width: 470px; height: 487px">		
				<div style="border-bottom: 1px solid #fff;padding-top: 50px;padding-bottom: 3px;width: 470px;height: 74px;">
					<img src="images/img_hangyelogo.png"></img>
					<img src="images/img_hangyedongtai.png"  style="padding-right: 184px;"></img>
					<a href="#" target="_blank" style=" vertical-align: baseline;margin: 23px auto ! important;   
			       border: 1px solid #fff; color:#fff;  border-radius: 10px;  padding: 6px;padding-right: 10px;padding-left: 10px;font-size: 12px;">更多</a>
				
				</div>
				<div>
				<ul>
				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>
				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>
				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>
					<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>
					<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>

				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>
				</ul>
				</div>
			</div>
			
			<div style="position: absolute; left: 550px; width: 470px; height: 487px">
				<div style="border-bottom: 1px solid #fff;padding-top: 50px;padding-bottom: 3px;width: 470px;height: 74px;">
					<img src="images/img_luntanlogo.png"></img>
					<img src="images/img_luntanjinghua.png" style="padding-right: 184px;"></img>
					<a href="#" target="_blank" style=" vertical-align: baseline;margin: 23px auto ! important;   
			       border: 1px solid #fff; color:#fff;  border-radius: 10px;  padding: 6px;padding-right: 10px;padding-left: 10px;font-size: 12px;">更多</a>
				</div>
				<div>
				<ul>
				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-21</span>
				   		</div>	
				   	</li>
				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-22</span>
				   		</div>	
				   	</li>
				   <li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-22</span>
				   		</div>	
				   	</li>
				   <li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-22</span>
				   		</div>	
				   	</li>
				   	<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-22</span>
				   		</div>	
				   	</li>
					<li style="background: none;border-bottom: #ddd 1px dashed;font-size: 15px;color: #fff;line-height: 46px;width: 470px;height: 47px;">
				   		<div style=" width: 75%; float: left; word-wrap: b; overflow: hidden; height: 47px;">
                          <img src="images/img_forum_arrow.png" />
                          <a href="#" style="background-image: none;font-size: 15px;color: #fff;overflow: hidden;width: 400px;height: 47px;">荣获2014年度互联网金融企业</a></div>			   		
				   		<div style=" float: right;height: 47px;">
				   		<span>2014-09-22</span>
				   		</div>	
				   	</li>
				</ul>
				</div>
				
			</div>
			
			
			
		</div>
	</div> -->
	<!-- 引用底部公共部分-->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
	<!-- 	<script type="text/javascript" src="script/add_all.js"></script> -->
	<script type="text/javascript" src="script/MSClass.js"></script>
	<script type="text/javascript" src="script/scroll.js"></script>
	<script type="text/javascript">


		$(function() {
			$(".changecolor").hover(			
					function(){
						
						$(this).css('background-color', '#E8380D');
						
					},
					function(){
						$(this).css('background-color', '#E85801');
					}
			);
			$(".changehuo").hover(			
					function(){			
						$(this).attr("src",'images/img_littegraypig3.png');					
					},
					function(){
						$(this).attr("src",'images/img_littegraypig.png');
					}
			);
			
			$(".changesee").hover(			
					function(){			
						$(this).css('background-color','#C1C6C9');	
						$(this).find('span').css('color','#FFFFFF');
					
					},
					function(){
						$(this).css('background-color','#FFFFFF');
						$(this).find('span').css('color','#A5A6A6');
					}
			);
			
			
			dqzt(0);
			var tim;
			$("#scrollleft ul").css("width", $("#scrollleft li").length * 125);
			function scrooatu() {
				clearTimeout(tim);
				$("#scrollleft").animate({
					scrollLeft : $("#scrollleft").scrollLeft() + 122
				}, "slow", function() {
					$("#scrollleft").animate({
						scrollLeft : 0
					}, 0);
					$("#scrollleft li:first").appendTo($("#scrollleft ul"));
				});

				tim = setTimeout(scrooatu, 3000);
			}
			scrooatu();
			$("#code").bind('keyup', function(event) {
				if (event.keyCode == "13") {
					login();
				}
			});
			$("#scrollleft").hover(function() {
				clearTimeout(tim);
			}, function() {
				setTimeout(scrooatu, 1000);
			});

		});
		//初始化
		function switchCode() {
			var timenow = new Date();
			$("#codeNum").attr("src",
					"admin/imageCode.do?pageId=userlogin&d=" + timenow);
		};
		$(document).ready(function() {
			$("#email").focus(function() {
				$("#email").val("");
			});
			$("#code").focus(function() {
				$("#code").val("");
			});

		});
		function login() {
			$(this).attr('disabled', true);
			$('#btn_login').attr('value', '登录中...');
			var param = {};
			param["paramMap.pageId"] = "userlogin";
			param["paramMap.email"] = $("#email").val();
			param["paramMap.password"] = $("#password").val();
			param["paramMap.code"] = $("#code").val();
			param["paramMap.afterLoginUrl"] = "${afterLoginUrl}";
			$.post("logining.do", param, function(data) {
				if (data == 1) {
					window.location.href = 'home.do';
				} else if (data == 2) {
					$('#btn_login').attr('value', '登录');
					alert("验证码错误!");
					switchCode();
					$("#btn_login").attr('disabled', false);
				} else if (data == 3) {
					$('#btn_login').attr('value', '登录');
					alert("用户名或密码错误!");
					switchCode();
					$("#btn_login").attr('disabled', false);
				} else if (data == 4) {
					$('#btn_login').attr('value', '登录');
					$("#s_email").attr("class", "formtips onError");
					alert("该用户已被禁用!");
					switchCode();
					$("#btn_login").attr('disabled', false);
				}
			});

		};
		function checkTime(num) {
			if (num == 0) {
				$('#touzi_now').attr('class', 'cur');
				$('#touzi_year').attr('class', '');
				$('#touzi_quarter').attr('class', '');
				$('#touzi_month').attr('class', '');
				$('#touzi_week').attr('class', '');
			} else if (num == 1) {
				$('#touzi_now').attr('class', '');
				$('#touzi_year').attr('class', 'cur');
				$('#touzi_quarter').attr('class', '');
				$('#touzi_month').attr('class', '');
				$('#touzi_week').attr('class', '');
			} else if (num == 2) {
				$('#touzi_now').attr('class', '');
				$('#touzi_year').attr('class', '');
				$('#touzi_quarter').attr('class', 'cur');
				$('#touzi_month').attr('class', '');
				$('#touzi_week').attr('class', '');
			} else if (num == 3) {
				$('#touzi_now').attr('class', '');
				$('#touzi_year').attr('class', '');
				$('#touzi_quarter').attr('class', '');
				$('#touzi_month').attr('class', 'cur');
				$('#touzi_week').attr('class', '');
			} else if (num == 4) {
				$('#touzi_now').attr('class', '');
				$('#touzi_year').attr('class', '');
				$('#touzi_quarter').attr('class', '');
				$('#touzi_month').attr('class', '');
				$('#touzi_week').attr('class', 'cur');
			}
			var param = {};
			param["paramMap.number"] = num;
			$.post("investRank.do", param, function(data) {
				$("#touzib").html(data);
			});
		}
		function checkTou(id, type) {
			var param = {};
			param["id"] = id;
			$.shovePost('financeInvestInit.do', param, function(data) {
				var callBack = data.msg;
				if (callBack != undefined) {
					alert(callBack);
				} else {
					if (type == 2) {
						var url = "subscribeinit.do?borrowid=" + id;
						$.jBox("iframe:" + url, {
							title : "我要购买",
							width : 450,
							height : 450,
							buttons : {}
						});
					} else {
						window.location.href = 'financeInvestInit.do?id=' + id;
					}
				}
			});
		}
		function closeMthod() {
			window.jBox.close();
			window.location.reload();
		}
	</script>
	<script type="text/javascript">
		$(function() {
			var self = $("#scroll ul")
			var sd = null;
			var leng = parseInt($("#scroll ul").css("width",
					self.find("li").length * 459).css("width"));
			$("#scroll ul").css("width", self.find("li").length * 459);
			function scrollatuo() {
				clearTimeout(sd);
				if ($("#scroll").scrollLeft() >= leng - 490) {
					$("#scroll").animate({
						scrollLeft : 0
					}, "slow");
					$(".control a").removeClass("cur")
					$(".control a:first").addClass("cur")
					sd = setTimeout(scrollatuo, 3000);
				} else {
					$("#scroll").animate({
						scrollLeft : $("#scroll").scrollLeft() + 459
					}, "slow", function() {
						var insd = parseInt($("#scroll").scrollLeft() / 459);
						$(".control a").removeClass("cur")
						$(".control a").eq(insd).addClass("cur")
					});
				}
				sd = setTimeout(scrollatuo, 3000);

			}
			sd = setTimeout(scrollatuo, 1000);
			//控制区
			$(".control a").hover(function() {
				var sf = $(".control a").index(this);
				$("#scroll").animate({
					scrollLeft : 459 * sf
				}, "slow");
				clearTimeout(sd);
				$(".control a").not(this).removeClass("cur")
				$(this).addClass("cur");
			}, function() {
				sd = setTimeout(scrollatuo, 3000);
			});
			//选项卡
			$("#second-tal li").click(function() {
				var sd = $("#second-tal li").index(this);

				$(".job-list").hide();
				$(".job-list").eq(sd).show();
				$("#second-tal li").removeClass("cur");
				$(this).addClass("cur");
			})

		})

		function regdo() {
			window.location.href = "reg.do";

		}
	</script>
	<script>
		var newsIndex = 0;
		$(document).ready(function() {
			var news = $("#news")[0].children;
			$(news[0]).css("display", "inline");
			var newsIntervalFun = function() {
				var temp = newsIndex - 1;
				if (newsIndex == news.length) {
					newsIndex = 0;
				}
				$(news[temp]).fadeOut("slow", function() {
					$(news[newsIndex]).fadeIn();
				});
				newsIndex++;
			}
			setInterval(newsIntervalFun, 2000);
		});
	</script>
	<script type="text/javascript" src="script/jquery.slides.min.js"></script>
	<script>
	   $(function() {
		      $('#slides').slidesjs({
		        width: 1920,
		        height: 700,
		        preload: true,
		        play: {auto:true},
		      navigation:false

		      });
		    });
	
</script>

<script>
window.onload =drawProcess;

function drawProcess() {
	if(Modernizr.canvas){
		$('canvas.process').each(function() {
			var text = $(this).text();
			var process = text.substring(0, text.length - 1);

			var canvas = this;
			var context = canvas.getContext('2d');
			context.clearRect(0, 0, 40, 40);

			context.beginPath();
			context.moveTo(20, 20);
			context.arc(20, 20, 20, 0, Math.PI * 2, false);
			context.closePath();
			context.fillStyle = '#ddd';
			context.fill();
			context.beginPath();
			context.moveTo(20, 20);
			context.arc(20, 20, 20, 0, Math.PI * 2 * process / 100, false);
			context.closePath();
			context.fillStyle = '#6699cc';
			context.fill();

			context.beginPath();
			context.moveTo(20, 20);
			context.arc(20, 20, 17, 0, Math.PI * 2, true);
			context.closePath();
			context.fillStyle = 'rgba(255,255,255,1)';
			context.fill();

			context.beginPath();
			context.arc(20, 20, 14.5, 0, Math.PI * 2, true);
			context.closePath();
			context.strokeStyle = '#ddd';
			context.stroke();

			context.font = "bold 6pt Arial";
			context.fillStyle = '#9fa0a0';
			context.textAlign = 'center';
			context.textBaseline = 'middle';
			context.moveTo(20, 20);
			context.fillText(text, 20, 20);

		});
	}else{
		$('div.divProcess').each(function(){
			$(this).attr("style","display:none");
		});	
		$('img.imgCanvas').each(function(){
			$(this).attr("style","display:block;margin-top:10px;");
			var process2 = $(this).attr("data-process");
            if(process2 == 0){
         	   $(this).attr("src","images/process/process-index-0.png"); 
            }else if(process2 <= 10){
         	   $(this).attr("src","images/process/process-index-10.png"); 
            }else if(process2 <= 20){
         	   $(this).attr("src","images/process/process-index-10.png"); 
            }else if(process2 <= 30){
         	   $(this).attr("src","images/process/process-index-20.png"); 
            }else if(process2 <= 40){
         	   $(this).attr("src","images/process/process-index-30.png"); 
            }else if(process2 <= 50){
         	   $(this).attr("src","images/process/process-index-40.png"); 
            }else if(process2 <= 60){
         	   $(this).attr("src","images/process/process-index-50.png"); 
            }else if(process2 <= 70){
         	   $(this).attr("src","images/process/process-index-60.png"); 
            }else if(process2 <= 80){
         	   $(this).attr("src","images/process/process-index-70.png"); 
            }else if(process2 <= 90){
         	   $(this).attr("src","images/process/process-index-80.png"); 
            }else if(process2 < 100){
         	   $(this).attr("src","images/process/process-index-90.png"); 
            }else if(process2 == 100){
         	   $(this).attr("src","images/process/process-index-100.png"); 
            }
		});	
	}
   
	
	
}

</script>
</body>
</html>
