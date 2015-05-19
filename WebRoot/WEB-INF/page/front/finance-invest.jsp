<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<style>
.container {
	width: 600px;
	margin: 0 auto;
}

#slides {
	display: none;
	position: relative;
}

/*方向图标*/
#slides .slidesjs-navigation {
	position: absolute;
	top: 50%;
	width: 19px;
	height: 30px;
	display: block;
	z-index: 1010;
}

#slides .slidesjs-previous {
	left: 1%;
	background-image: url(images/prearrow.png);
	width: 35px;
	height: 35px;
}

#slides .slidesjs-next {
	right: 1%;
	background-image: url(images/nextarrow.png);
	width: 35px;
	height: 35px;
}

.slidesjs-pagination {
	position: absolute;
	bottom: 20px;
	margin: 6px 0 0;
	text-align: center;
	left: 50%;
	list-style: none;
	z-index: 1010;
	margin: 0 0 0 -50px;
}
/*焦点开始*/
.slidesjs-pagination li {
	float: left;
	margin: 0 1px;
}

.slidesjs-pagination li a {
	display: block;
	width: 24px;
	height: 0px;
	padding-top: 5px;
	background: #fff;
	float: left;
	overflow: hidden;
}

.slidesjs-pagination li a.active, .slidesjs-pagination li a:hover.active
	{
	background: #ff9c27;
}

.slidesjs-pagination li a:hover {
	background: #ff9c27;
}
/*焦点结束*/
#slides a:link, #slides a:visited {
	color: #333
}

#slides a:hover, #slides a:active {
	color: #9e2020
}

.navbar {
	overflow: hidden
}
</style>
<style type="text/css">
ol li {
	margin: 8px
}

#con {
	font-size: 12px;
	margin: 0px auto;
	width: 1000px
}

#tags {
	margin: 0px -2px;
	height: 40px;
}

#tags li {
	float: left;
	font-weight: bold;
	font-size: 16px;
	list-style-type: none;
}

#tags li a {
	float: left;
	color: #686767;
	line-height: 26px;
	margin: 5px 18px;
	text-decoration: none;
}

#tags li.emptyTag {
	background: none transparent scroll repeat 0% 0%;
	WIDTH: 4px
}

#tags li.selectTag {
	background-image: url(images/tab.png);
}

#tags li.selectTag a {
	background-position: right top;
	color: #686767;
	line-height: 25px;
	height: 25px
}

#tagContent {
	/*     margin-top: -20px; */
	
}

#tagContent div.selectTag {
	display: block
}

#tagContent0 tr {
	height: 30px;
}

#investRecord table, #contentList table {
	width: 98%;
	border: 1px solid #dcdcdc;
	margin: 20px 10px 20px 10px;
}

#investRecord table thead tr, #contentList table thead tr {
	background: #f1edec;
	height: 40px;
	border: 2px solid white;
}

#investRecord table tbody tr, #contentList table tbody tr {
	height: 30px;
	background: #f6f6f6;
	border: 2px solid white;
}

#investRecord table th, #investRecord table td, #contentList table th,
	#contentList table td {
	border: 2px solid white;
}

#contentRepayList table th {
	background: #f1edec;
	height: 40px;
	border: 2px solid white;
}

#contentRepayList table tr {
	height: 30px;
	background: #f6f6f6;
	border: 2px solid white;
	background: #f6f6f6;
}

#contentRepayList table td {
	border: 2px solid white;
}
</style>

<jsp:include page="/include/head.jsp"></jsp:include>


</head>
<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<!-- 内容 -->
	<div style="position: relative;">
		<!-- 简介 -->
		<div style="position: relative; font-size: 14px">
			<div
				style="width: 1000px; height: 405px; margin: 0 auto; margin-top: 10px; background: #fff;">
				<!-- <div style="padding: 5px 10px;">项目理财详情页： >> 查看详情</div> -->
				<!--左侧  -->
				<div
					style="float: left; height: 405px; width: 698px; background: #fff; border-top: 1px solid #DFE2E2; border-left: 1px solid #DFE2E2; border-bottom: 1px solid #DFE2E2; border-top-left-radius: 5px; border-bottom-left-radius: 5px">
					<!--标题  -->
					<div style="padding: 10px 10px">
						<span><img src="images/pig.png" /></span> <span>
							<h1
								style="position: relative; top: 4px; left: -12px; display: inline; font-size: 22px; color: #807f80;">
								${investDetailMap.borrowTitle}</h1>
						</span> <span> <img src="images/flow.png" /></span> <span> <img
							src="images/reward.png" /></span>
					</div>
					<div style="text-align: right; margin: -30px">
						<span style="font-size: 14px; position: relative; left: -40px">借款用户：${investDetailMap.username}<span
							style="margin: 0 10px; position: relative; top: -1px"><img
								src="images/AA.png" /></span></span>
					</div>
					<div style="position: relative; top: 34px; text-align: center">
						<img src="images/line1.png" />
					</div>

					<!-- 新行 -->
					<div>
						<div
							style="position: relative; top: 50px; float: left; height: 120px; width: 160px; text-align: center">
							<div>
								<span style="color: #e85801; font-size: 22px">￥<fmt:formatNumber
										type="number" value="${investDetailMap.borrowAmount } "
										maxFractionDigits="0" /></span>
							</div>
							<div>借款金额</div>
						</div>
						<span style="position: relative; top: 42px; float: left"><img
							src="images/line3.png" /></span>
						<div
							style="position: relative; top: 50px; float: left; height: 120px; width: 160px; text-align: center">
							<div>
								<span style="font-size: 22px; color: #e85801">${investDetailMap.annualRate}
									%</span>
							</div>
							<div>利率/年</div>
						</div>
						<span style="position: relative; top: 42px; float: left"><img
							src="images/line3.png" /></span>
						<div
							style="position: relative; top: 50px; float: left; height: 120px; width: 160px; text-align: center">
							<div>
								<s:if test="%{#request.borrowDetailMap.excitationType == 1}">
									<span style="font-size: 22px; color: #e85801">无</span>
								</s:if>
								<s:elseif test="%{#request.borrowDetailMap.excitationType ==2}">
									<span style="font-size: 22px; color: #e85801">${borrowDetailMap.schedules}</span>元</s:elseif>
								<s:elseif test="%{#request.borrowDetailMap.excitationType ==3}">
									<span style="font-size: 22px; color: #e85801">${borrowDetailMap.excitationSum}
										%</span>
								</s:elseif>
							</div>
							<div>
								<span>奖励</span>
							</div>
						</div>
						<span style="position: relative; top: 42px; float: left"> <img
							src="images/line3.png" /></span>
						<div
							style="position: relative; top: 50px; float: left; height: 120px; width: 160px; text-align: center">
							<div>
								<span style="font-size: 22px; color: #e85801">${investDetailMap.deadline}</span>
								<span style="font-size: 22px; color: #e85801"><s:if
										test="%{investDetailMap.isDayThe ==1}">个月</s:if> <s:else>天</s:else></span>
							</div>
							<div>借款期限</div>
						</div>

						<div style="text-align: center">
							<img src="images/line1.png" />
						</div>
						<!-- 				<hr /> -->
						<!-- 新行 -->
						<div>
							<div style="float: left; height: 155px; width: 320px;">
								<div style="position: relative; top: 20px; margin: 6px 10px;">
									<img src="images/icon9.png" /> 还款方式：<span><s:if
											test="%{investDetailMap.paymentMode == 1}">
											按月分期还款
											</s:if> <s:elseif test="%{investDetailMap.paymentMode ==2}">
											按月付息,到期还本
											</s:elseif> <s:elseif test="%{investDetailMap.paymentMode ==3}">
											秒还
											</s:elseif> <s:elseif test="%{investDetailMap.paymentMode ==4}">
											一次性还款
											</s:elseif></span>
								</div>

							</div>
						</div>
						<!-- 新行 -->
						<div style="position: relative; top: -78px">
							<img src="images/line2.png" />
						</div>
						<div
							style="float: left; height: 50px; width: 640px; position: relative; top: -70px; margin: 11px 11px">
							<img src="images/icon10.png" /> <span>担保机构：</span><span>中管盛业100%担保</span>&nbsp;&nbsp;
						</div>
						<div style="position: relative; top: -76px">
							<img src="images/line2.png" />
						</div>

						<div
							style="float: left; height: 50px; width: 640px; position: relative; top: -70px; margin: 11px">
							<img src="images/icon11.png" /> <span>&nbsp;&nbsp;投标次数：</span><span
								style="font-size: 18px; color: #e85801;"><s:property
									value="#request.borrowDetailMap.investNum" default="0" /></span>&nbsp;&nbsp;
							<span>&nbsp;&nbsp;已经借入：</span><span
								style="font-size: 18px; color: #e85801;">${borrowDetailMap.hasInvestAmount}</span><span>元</span>&nbsp;&nbsp;
							<span>&nbsp;&nbsp;还差：</span><span
								style="font-size: 18px; color: #e85801;"><s:property
									value="#request.investDetailMap.investNum" default="0" /></span><span>元</span>
						</div>
						<div style="position: relative; float: right; margin: -300px 55px">
						<div id="divCanvas"><canvas id="canvas" class="process" width="160px" height="160px">${investDetailMap.schedules}%</canvas></div>	
						<img id="imgCanvas" style="display:none;" src="" alt=""  width="160px" height="160px"/>
						</div>
						<div
							style="position: relative; float: right; margin: -126px 90px; width: 72px; height: 80px">
							<s:if test="%{#request.borrowDetailMap.borrowStatus == 2}">
								<s:if test="%{#request.borrowDetailMap.borrowWay != 4}">
									<span>剩余时间</span>
									<span id="remainTimeOne" style="color: #e85801"></span>
								</s:if>
							</s:if>
							<s:else>
								<span>剩余时间</span>
								<span id="" style="color: #e85801">投标结束</span>
							</s:else>

						</div>
					</div>
				</div>
				<!--左侧  end-->
				<!--右侧  -->
				<div
					style="float: left; height: 405px; width: 300px; background: #f7f8f8; border-bottom-right-radius: 12px">
					<div
						style="background: #eaedef; height: 80px; border-top-right-radius: 12px">
						<div
							style="font-size: 22px; color: #807f80; position: absolute; margin: 20px 0 10px 10px;">
							<img src="images/hit.png" /> <span
								style="position: relative; left: 10px">我要投标</span>
						</div>
						<div style="float: right; margin-top: 60px;">
							<a href="#"><span
								style="color: #e85801; font-size: 14px; position: relative; top: -4px">借款协议（范本）</span></a>
						</div>
					</div>
					<div style="height: 125px; padding: 15px;">
						<div style="height: 40px; font-size: 16px">
							账户总额：&nbsp; <span style="color: #e85801">${userMap.totalSum}</span>
							&nbsp;元
						</div>
						<div style="height: 40px; font-size: 16px">
							可用余额：&nbsp; <span style="color: #e85801">${userMap.usableSum}</span>&nbsp;
							元
						</div>
						<div style="float: right;">
							<a type="button" href="rechargeInit.do">
								<div
									style="margin-top: -82px; width: 60px; height: 30px; background: #7ecef4; color: white; -moz-border-radius: 5px; -webkit-border-radius: 5px; border-radius: 5px; line-height: 2; text-align: center; font-size: 15px">
									充值</div>
							</a>
						</div>
						<div style="height: 40px; font-size: 16px">
							可投资额度：<span style="color: #e85801">￥</span><span
								style="color: #e85801">${investDetailMap.investNum}</span>
						</div>
					</div>

					<div
						style="width: 242px; height: 40px; border-radius: 5px; margin: -10px 28px; border: 1px solid #cacaca">
						<input type="text"
							placeholder='最低投标金<s:property 
							value="#request.borrowDetailMap.minTenderedSum" default="0" />元'
							id="amount" name="paramMap.amount1" maxlength="20"
							style="padding: 14px 80px 10px 5px" />&nbsp;
						<div style="position: relative; top: -44px; margin: 0 220px">元</div>
					</div>
					<s:if test="#request.borrowDetailMap.hasPWD != -1">

						<div
							style="width: 242px; height: 40px; border-radius: 5px; margin: 18px 28px; border: 1px solid #cacaca">
							<input id="investPWD" maxlength="20" name="paramMap.investPWD"
								type="password" placeholder="请输入投标密码"
								style="padding: 14px 80px 10px 5px;" />&nbsp;&nbsp;&nbsp;
						</div>
					</s:if>

					<div style="padding: 10px 40px;">
						<input type="button" value="确认提交" id="btnsave" type="button"
							style="width: 242px; height: 40px; color: white; font-size: 18px; background: #e85801; -moz-border-radius: 8px; -webkit-border-radius: 8px; border-radius: 5px; margin: 10px -10px" />
					</div>
					<div style="margin: 20px 30px; font-size: 12px; color: #CBD0D0">注意：点击按钮表示您将确认投标金额并支付</div>
				</div>
				<!--右侧 end -->
			</div>

		</div>
		<!-- 简介end -->

	</div>
	<!-- 内容结束 -->

	<!-- 选项卡 -->
	<div
		style="margin: 30px auto; width: 1000px; height: 100%; border: 1px solid #DFE2E2; border-radius: 5px">

		<div id="con">
			<ul id="tags">
				<li class="selectTag"><a
					onClick="selectTag('tagContent0',this)" href="javascript:void(0)">借款详情</a></li>
				<li><a onClick="selectTag('tagContent1',this)"
					href="javascript:void(0)">借款资料</a></li>
				<li><a onClick="selectTag('tagContent2',this)"
					href="javascript:void(0)">还款记录</a></li>
				<li><a onClick="selectTag('tagContent3',this)"
					href="javascript:void(0)">投标记录</a></li>
				<li><a onClick="selectTag('tagContent4',this)"
					href="javascript:void(0)">担保机构</a></li>
			</ul>

			<!-- 借款详情 -->
			<div id="tagContent">
				<div class="tagContent selectTag" id="tagContent0">
					<span style="position: relative; top: -9px"><img
						src="images/line4.png" /></span>
					<div style="margin: 18px 0px; line-height: 30px;">
						<div style="color: #686767; font-weight: bold">
							<h3>一、基本情况</h3>
						</div>
						<div style="margin: 10px 0;">
							<p>
								&nbsp;&nbsp;&nbsp;&nbsp;
								<s:property value="#request.borrowDetailMap.borrowInfo"
									default="" escape="false" />
							</p>
						</div>
						<div style="color: #686767; font-weight: bold">
							<h3>二、借款用途</h3>
						</div>
						<div style="margin: 10px 0;">
							<p>&nbsp;&nbsp;&nbsp;&nbsp;${borrowDetailMap.purpose}</p>
						</div>
						<div style="color: #686767; font-weight: bold">
							<h3>三、风控意见</h3>
						</div>
						<div style="margin: 10px 0;">
							<p>&nbsp;&nbsp;&nbsp;&nbsp;审批意见：${borrowDetailMap.auditOpinion}</p>
						</div>

					</div>

					<div id="contentList">
						<table cellspacing="0" cellpadding="0">
							<thead>
								<tr>
									<th align="center">审核项目</th>
									<th align="center">状态</th>
									<th align="center">通过时间</th>

								</tr>
							</thead>
							<tbody>
								<s:if test="%{#request.list !=null && #request.list.size()>0}">

									<s:set name="auditStatusNum" value="0"></s:set>

									<s:iterator value="#request.list" id="bean">
										<s:if test="#bean.auditStatus == 3">
											<tr>
												<td align="center">${bean.name}</td>
												<td align="center">
													<%-- <s:if test="#bean.auditStatus == 1">审核中</s:if>
												<s:elseif test="#bean.auditStatus == 2"> 审核失败 </s:elseif> <s:elseif
													test="#bean.auditStatus == 3"> --%> <img
													src="images/neiye2_44.jpg" width="14" height="15" /> <%-- </s:elseif> <s:else> 等待资料上传</s:else> --%>
												</td>
												<td align="center">${bean.passTime}</td>
											</tr>
											<s:set name="auditStatusNum" value="1"></s:set>
										</s:if>

									</s:iterator>
									<s:if test="#auditStatusNum==0">
										<tr>
											<td colspan="6" align="center">没有数据</td>
										</tr>
									</s:if>
								</s:if>
								<s:else>
									<td colspan="4" align="center">没有数据</td>
								</s:else>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<span style="position: relative; top: -9px"><img
				src="images/line4.png" /></span>
			<!--  借款资料 -->
			<div class="tagContent" id="tagContent1">
				<div class="container">
					<div id="slides">
						<div>
							<img width="100%" src="images/borrowDetail1.png" />
						</div>
						<div>
							<img width="100%" src="images/borrowDetail2.png" />
						</div>
						<div>
							<img width="100%" src="images/borrowDetail1.png" />
						</div>

						<a href="#" class="slidesjs-previous slidesjs-navigation"><i
							class="icon-chevron-left icon-large"></i></a> <a href="#"
							class="slidesjs-next slidesjs-navigation"><i
							class="icon-chevron-right icon-large"></i></a>
					</div>
				</div>

			</div>
			<!-- 还款记录 -->
			<div class="tagContent" id="tagContent2">
				<div id="contentRepayList" style="margin: 30px"></div>
			</div>

			<!--投标记录  -->
			<div class="tagContent" id="tagContent3">
				<div style="padding: 20px 10px;">
					<div style="float: left; width: 300px;">
						目前总投标金额：￥${borrowDetailMap.hasInvestAmount}</div>
					<div style="float: left; width: 300px;">
						剩余投标金额：￥${borrowDetailMap.investAmount}</div>
					<div style="float: left; width: 300px;">
						<s:if test="%{#request.borrowDetailMap.borrowStatus == 2}">
									剩余投标时间：<span id="remainTimeTwo"></span>
						</s:if>
					</div>

				</div>
				<div id="investRecord" style="margin: 20px 10px 10px 10px;">
					<table cellspacing="0" cellpadding="0">
						<thead>
							<tr>
								<th align="center">序号</th>
								<th align="center">投资人</th>
								<th align="center">当前利率</th>
								<th align="center">投资金额</th>
								<th align="center">状态</th>
								<th align="center">投资时间</th>
							</tr>
						</thead>
						<tbody>
							<s:if
								test="%{#request.investList !=null && #request.investList.size()>0}">
								<s:iterator value="#request.investList" id="investList"
									status="s">
									<tr>
										<td align="center" style="width: 100px;"><s:property
												value="#s.count" /></td>

										<td align="center" class="name"><s:property
												value="#investList.username" default="---" /> <!--   creditedStatus==2 代表该用户在转让债权 -->
											<s:if
												test="#investList.creditedStatus !=null && #investList.creditedStatus==2 ">
												<img src="images/zrico.jpg" width="30" height="16" />
											</s:if></td>
										<td align="center">${investDetailMap.annualRate}%</td>

										<td align="center" class="fred">￥<s:property
												value="#investList.investAmount" /></td>
										<td align="center">通过</td>
										<td align="center"><s:property
												value="#investList.investTime" /></td>
									</tr>

								</s:iterator>
							</s:if>
							<s:else>
								<td colspan="6" align="center">没有数据</td>
							</s:else>
						</tbody>
					</table>
				</div>
			</div>
			<!--担保机构  -->
			<div class="tagContent" id="tagContent4">
				<div style="margin: 20px 10px 10px 10px;">中管盛业100%担保</div>
			</div>
		</div>
	</div>

	<div id="dateThe"></div>
	<div id="content"></div>
	<div id="remainSeconds">${borrowDetailMap.remainTime}</div>
	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<s:hidden name="id" value="%{investDetailMap.id}"></s:hidden>
	<s:hidden name="annualRate" value="%{investDetailMap.annualRate}"></s:hidden>
	<s:hidden name="deadline" value="%{investDetailMap.deadline}"></s:hidden>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/jquery.slides.min.js"></script>
		<script type="text/javascript" src="script/modernizr.custom.js"></script>
	<script type="text/javascript">

$('#btnsave').click(function() {
	var step = '${session.user.authStep}';
	var param = {}; 
	 if (step < 5) {
		alert("未开通第三方账户");
		window.location.href = 'portUserAcct.do';
		return false;
	} 
	var amount = $('#amount').val();
	if (amount == '') {
		alert("请输入投标金额！");
		return false;
	}
	param = {};
	if (confirm("是否确定投标，投标金额为" + amount + "元")) {
		var id = $("#id").val();
		param['paramMap.id'] = id;
		param["paramMap.code"] = $("#code").val();
		param['paramMap.annualRate'] = $("#annualRate").val();
		param["paramMap.deadline"] = $("#deadline").val();
		param['paramMap.amount'] = $('#amount').val();
		param['paramMap.investPWD'] = $('#investPWD').val();
		if (flag) {
			flag = false;
			$.shovePost('tenderTrade.do', param, function(data) {
				var callBack = data.msg;
				// alert("callBack="+callBack);
				if (callBack == undefined || callBack == '') {
					// alert("data="+data);
					$('#content').html(data);
					flag = true;
				} else {
					var dataThe = data.dateThe;
					// alert("dataThe="+dataThe);
					if (dataThe == undefined || dataThe == '') {
						if (callBack == 1) {
							alert("操作成功!");
							window.location.href = 'financeDetail.do?id=' + id;
							flag = false;
							return false;
						}
						alert(callBack);
						flag = true;
					} else {
						$('#dateThe').html(data.msg);
						flag = true;
					}
				}
			});
		}
	}
});


</script>
	<script type="text/javascript">
			var flag = true;
			$(function() {
				//样式选中
				$("#licai_hover").attr('class', 'nav_first');
				$("#licai_hover div").removeClass('none');
				$('.tabmain').find('li').click(function() {
					$('.tabmain').find('li').removeClass('on');
					$(this).addClass('on');
					$('.lcmain_l').children('div').hide();
					$('.lcmain_l').children('div').eq($(this).index()).show();
				});
				$('#amount').click(function() {
					$('#amount').val("");
				});
				
				//样式选中
				var param = {};
				$('#repay').click(function() {
					var id = $('#idStr').val();
					$(this).addClass('on');
					$('#audit').removeClass('on');
					$('#collection').removeClass('on');
					param['paramMap.id'] = id;
					$.shovePost('financeRepay.do', param, function(data) {
						$('#contentRepayList').html(data);
					});
				});
				
				document.getElementById("tagContent0").style.display = "block";
				document.getElementById("tagContent1").style.display = "none";
				document.getElementById("tagContent2").style.display = "none";
				document.getElementById("tagContent3").style.display = "none";
				document.getElementById("tagContent4").style.display = "none";
			});
			
			function selectTag(showContent, selfObj) {
				if(showContent == "tagContent2"){
				var id = $('#idStr').val();
				param['paramMap.id'] = id;
				$.shovePost('financeRepay.do', param, function(data) {
					$('#contentRepayList').html(data);
				});
				}
				// 操作标签
				var tag = document.getElementById("tags").getElementsByTagName(
						"li");
				var taglength = tag.length;
				for (i = 0; i < taglength; i++) {
					tag[i].className = "";
				}
				selfObj.parentNode.className = "selectTag";
				// 操作内容
				for (i = 0; j = document.getElementById("tagContent" + i); i++) {
					j.style.display = "none";
				}
				
				document.getElementById(showContent).style.display = "block";
			}
			
			function showImg(typeId, userId, ids) {
				var session = '<%=session.getAttribute("user")%>';
				if (session == 'null') {
					window.location.href = 'login.do';
					return;
				}
				var url = "showImg.do?typeId=" + typeId + "&userId=" + userId;
				$.jBox("iframe:" + url, {
					title : "查看认证图片(点击图片放大)",
					width : 500,
					height : 428,
					buttons : {}
				});
			}
			function close() {
				ClosePop();
			}
</script>
	<script type="text/javascript">
			var SysSecond;
			var InterValObj;

			$(document).ready(function() {
				SysSecond = parseInt($("#remainSeconds").html()); //这里获取倒计时的起始时间 
				InterValObj = window.setInterval(SetRemainTime, 1000); //间隔函数，1秒执行 
			});

			//将时间减去1秒，计算天、时、分、秒 
			function SetRemainTime() {
				if (SysSecond > 0) {
					SysSecond = SysSecond - 1;
					var second = Math.floor(SysSecond % 60); // 计算秒     
					var minite = Math.floor((SysSecond / 60) % 60); //计算分 
					var hour = Math.floor((SysSecond / 3600) % 24); //计算小时 
					var day = Math.floor((SysSecond / 3600) / 24); //计算天 
					var times = day + "天" + hour + "小时" + minite + "分" + second
							+ "秒";
					$("#remainTimeOne").html(times);
					$("#remainTimeTwo").html(times);
				} else {//剩余时间小于或等于0的时候，就停止间隔函数 
					window.clearInterval(InterValObj);
					$("#remainTimeTwo").html("投标结束");
					$("#remainTimeOne").html("投标结束");
					//这里可以添加倒计时时间为0后需要执行的事件 
				}
			}
</script>
	<script>
		$(function() {
		     $('#slides').slidesjs({
		          width: 600,
		          height: 849,
		          preload: true,
		          play: {auto:false},
		        navigation:false

		        });
			//画进度
		if(Modernizr.canvas){
			$("#canvas").each(function() {
				var text = $(this).text();
				var process = text.substring(0, text.length - 1);

				var canvas = this;
				var context = canvas.getContext('2d');
				context.clearRect(0, 0, 40, 40);

				context.beginPath();
				context.moveTo(40, 40);
				context.arc(80, 80, 80, 0, Math.PI * 2, false);
				context.closePath();
				context.fillStyle = '#ddd';
				context.fill();

				context.beginPath();
				context.moveTo(40, 40);
				context.arc(80, 80, 80, 0, Math.PI * 2 * process / 100, false);
				context.closePath();
				context.fillStyle = '#52c3f1';
				context.fill();

				context.beginPath();
				context.moveTo(40, 40);
				context.arc(80, 80, 65, 0, Math.PI * 2, true);
				context.closePath();
				context.fillStyle = 'rgba(255,255,255,1)';
				context.fill();

				context.font = "bold 25pt Arial";
				context.fillStyle = '#9fa0a0';
				context.textAlign = 'center';
				context.textBaseline = 'middle';
				context.fillText(text, 80, 80);

			});}else{
					 $("#divCanvas").attr("style","display:none"); 
					 $("#imgCanvas").attr("style","display:block"); 
	               var process2 = ${investDetailMap.schedules};
	               if(process2 == 0){
	            	   $("#imgCanvas").attr("src","images/process/process-big-0.png"); 
	               }else if(process2 <= 10){
	            	   $("#imgCanvas").attr("src","images/process/process-big-10.png"); 
	               }else if(process2 <= 20){
	            	   $("#imgCanvas").attr("src","images/process/process-big-10.png"); 
	               }else if(process2 <= 30){
	            	   $("#imgCanvas").attr("src","images/process/process-big-20.png"); 
	               }else if(process2 <= 40){
	            	   $("#imgCanvas").attr("src","images/process/process-big-30.png"); 
	               }else if(process2 <= 50){
	            	   $("#imgCanvas").attr("src","images/process/process-big-40.png"); 
	               }else if(process2 <= 60){
	            	   $("#imgCanvas").attr("src","images/process/process-big-50.png"); 
	               }else if(process2 <= 70){
	            	   $("#imgCanvas").attr("src","images/process/process-big-60.png"); 
	               }else if(process2 <= 80){
	            	   $("#imgCanvas").attr("src","images/process/process-big-70.png"); 
	               }else if(process2 <= 90){
	            	   $("#imgCanvas").attr("src","images/process/process-big-80.png"); 
	               }else if(process2 < 100){
	            	   $("#imgCanvas").attr("src","images/process/process-big-90.png"); 
	               }else if(process2 == 100){
	            	   $("#imgCanvas").attr("src","images/process/process-big-100.png"); 
	               }
			}
		});
	</script>
<!-- 	<script type="text/javascript" src="script/nav-lc.js"></script> -->
</body>
</html>