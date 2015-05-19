<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div class="nymain">
		<div class="wdzh" style="height: 1100px; margin-bottom: 10px;">
			<div class="l_nav">
				<!-- 引用我的帐号主页左边栏 -->
				<%@include file="/include/left.jsp"%>
			</div>
			<div class="r_main"
				style="border: none; margin: 0; width: 761px; background: none; left: 6px; position: relative">
				<div class="box box2" style="border-bottom: none; background: none">
					<div class="accHead"
						style="position: relative; border-bottom: 2px solid #e2e2e2; height: 43px">
						<span class="greeting"
							style="display: inline-block; padding: 14px 20px 0px 22px;">
							<span id="greetTime">您好</span>! ${homeMap.username}
						</span>
						<span><img src="images/accountShow.png"></img></span>
						<span><img src="images/box.png"></img></span>
						<div class="lastLoginTime"
							style="display: inline; padding: 14px 20px 0px 22px; position: relative; float: right">
							<span class="lastIcon"></span><span style="position: relative; left: -10px">我的积分：<span style="color: #e85801; font-size: 16px">${homeMap.rating}</span></span>
							<span class="lastIcon"></span><span>我的米金：<span style="color: #e85801; font-size: 16px">${homeMap.rating}</span></span>
						</div>

					</div>
				</div>
				<%-- <div
					style="position: relative; display: inline-block; padding: 14px 20px 0px 22px; height: 20px; left: 220px">您上次登录的时间为${homeMap.lastDate}</div> --%>
				<div class="boxOne"
					style="margin: 16px 0px; border: 1px solid #c9cacb; height: 258px">
					<div style="position: relative; top: 21px; left: 16px">
						<img src="images/frame.png" style="margin: 10px 10px"></img>
						<div
							style="position: relative; letter-spacing: 1px; top: -149px; font-size: 14px">
							<p>
								<span style="position: relative; left: 40px">可用余额： </span><span
									style="position: relative; color: #eabc46; font-size: 18px; left: 104px; color: #e85801">${sumMap.SumusableSum}</span><span
									style="position: relative; left: 105px">元</span>
							</p>
						</div>
						<div
							style="letter-spacing: 1px; position: relative; left: 44px; top: -119px;">
							<p>
								<span>冻结资金： </span><span
									style="position: relative; font-size: 16px; left: 95px; color: #e85801">${sumMap.SumfreezSum}</span><span
									style="position: relative; left: 96px">元</span>
							</p>
							<p>
								<span>待收本金： </span><span
									style="position: relative; font-size: 16px; left: 64px; color: #e85801">${accmountStatisMap.forPayPrincipal}</span><span
									style="position: relative; left: 65px">元</span>
							</p>
							<p>
								<span>待收收益： </span><span
									style="position: relative; font-size: 16px; left: 84px; color: #e85801">${accmountStatisMap.forPayInterest}</span><span
									style="position: relative; left: 85px">元</span>
							</p>
						</div>
					</div>

					<span
						style="position: relative; font-size: 16px; top: -45px; left: 38px"><strong>=</strong><span
						style="letter-spacing: 1px">账户总资产 ：</span><span
						style="position: relative; color: #e85801; font-size: 18px; left: 50px">
						<fmt:formatNumber type="number" value="${sumMap.SumfreezSum + accmountStatisMap.forPayPrincipal + accmountStatisMap.forPayInterest + sumMap.SumusableSum}"
										maxFractionDigits="2" />
						</span><span
						style="position: relative; left: 50px">元</span></span>

					<div>
						<div
							style="background: #7ecef4; border-radius: 3px; width: 78px; height: 32px; color: white; top: -238px; position: relative; left: 397px;">
							<p style="position: relative; top: 5px; left: 6px; line-height: 22px">
								<a href="rechargeInit.do" target="_self" style="color: white">充值</a>
							</p>
						</div>

						<div
							style="background: #b5b5b6; border-radius: 3px; width: 78px; height: 32px; color: white; top: -224px; position: relative; left: 397px;">
							<p style="position: relative; top: 5px; left: 6px; line-height: 22px"><a href="withdrawLoad.do" target="_self" style="color: white">提现</a></p>
						</div>
						
						<!-- <div
							style="background: #aab300; border-radius: 3px; width: 78px; height: 32px; color: white; top: -208px; position: relative; left: 397px;">
							<p style="position: relative; top: 5px; left: -4px; line-height: 22px">兑换金币</p>
						</div> -->
					</div>
					<img src="images/earnIcon.png"
						style="size: 50px; position: relative; top: -340px; left: 586px;"></img>
					<span
						style="position: relative; top: -270px; left: 512px; color: #e85801; font-size: 18px"><fmt:formatNumber type="number" value="${accmountStatisMap.hasPayInterest + accmountStatisMap.forPayInterest}"
										maxFractionDigits="2" />
						元</span> <span style="position: relative; top: -225px; left: 454px">
						<span style="position: relative; left: 33px"><fmt:formatNumber type="number" value="${accmountStatisMap.hasPayInterest}"
										maxFractionDigits="2" />
						元</span>
						<span style="position: relative; left: -94px"><img src="images/dollar.png"></img></span>
						<span
						style="position: relative; left: -92px">已赚</span>
					</span> <span style="position: relative; top: -196px; left: 382px">${accmountStatisMap.forPayInterest}
						元
						<span style="position: relative; left: -122px"><img src="images/dollarWallet.png"></img></span>
						<span
						style="position: relative; left: -118px">待收</span>
					</span>
				</div>
				<span style="font-size: 14px; position: relative; top: 4px"><img src="images/InvestMoney.png"></img></span>
				<span style="font-size: 14px; position: relative; top: 4px; float: right"><img src="images/ReceiveDay1.png"></img></span>

				<div class="boxTwo"
					style="position: absolute; height: 241px; top: 409px; width: 614px; border: 1px solid #c9cacb">
					<img src="images/timeline.png"></img> <img
						src="images/receiveDay.png"
						style="left: 622px; position: relative; top: -242px;"></img>
				</div>
				<s:if test="#request.activityBorrowList.size>0">
					<span style="position: relative; top: 340px; left: -214px"><img src="images/recommend.png"></img></span>
					<div class="boxThree"
						style="position: absolute; height: 163px; top: 735px; width: 768px; border: 1px solid #c9cacb">
						<div>
							<div class=""
								style="position: relative; width: 100%; font-size: 15px">

								<div style="height: 50px">
									<dl>
										<dd
											style="padding: 15px 0; background-color: #F1F1F1; width: 45%; float: left; text-align: center">借款标题</dd>
										<dd
											style="padding: 15px 0; background-color: #F1F1F1; width: 16%; float: left; text-align: center">金额</dd>
										<dd
											style="padding: 15px 0; background-color: #F1F1F1; width: 16%; float: left; text-align: center">年利率</dd>
										<dd
											style="padding: 15px 0; background-color: #F1F1F1; width: 11%; float: left; text-align: center">期限</dd>
										<dd
											style="padding: 15px 0; background-color: #F1F1F1; width: 12%; float: left; text-align: center">状态</dd>
									</dl>
								</div>
								<dl>
									<s:iterator value="#request.activityBorrowList" var="finance">
										<div
											style="height: 50px; border-bottom: solid; border-width: 1px; border-color: #F5F5F5">
											<dd style="padding: 15px 10px; width: 45%; float: left; text-align: center"><span><img src="images/pig.png"></img></span>
												<a href="financeDetail.do?id=${finance.id}" target="_blank">${finance.borrowTitle}</a>
											</dd>
											<dd
												style="padding: 15px 0; width: 15%; float: left; text-align: center;">${finance.borrowAmount}</dd>
											<dd
												style="padding: 15px 0; width: 15%; float: left; text-align: center; color: #ff7400;">${finance.annualRate}%</dd>
											<dd
												style="padding: 15px 0; width: 10%; float: left; text-align: center;">${finance.deadline}<s:if
													test="%{#finance.isDayThe ==1}">个月</s:if>
												<s:else>天</s:else>
											</dd>

											<dd
												style="padding: 15px 0; width: 10%; float: left; text-align: center;">
												<div class="btn5">
													<s:if test="%{#finance.borrowStatus == 1}">
											初审中
										</s:if>
													<s:elseif test="%{#finance.borrowStatus == 2}">
														<s:if test="%{#finance.borrowShow == 1}">
														<span style="width: 86px; height: 33px; background: #e85801; text-align: center; line-height: 32px; position: relative; top: -6px; border-radius: 5px">
															<a href="javascript:void(0);"
																onclick="checkTou(${finance.id},1)" style="color: #fff">立即投标</a>
														</span>
														</s:if>
													</s:elseif>
													<s:elseif test="%{#finance.borrowStatus == 3}">
									复审中
								</s:elseif>
													<s:elseif test="%{#finance.borrowStatus == 4}">
														<s:if test="%{#finance.borrowShow == 2}">回购中</s:if>
														<s:else>还款中</s:else>
													</s:elseif>
													<s:elseif test="%{#finance.borrowStatus == 5}">
									已还完
								</s:elseif>
													<s:else>流标</s:else>
												</div>
											</dd>
										</div>
									</s:iterator>
								</dl>
							</div>
						</div>
					</div>
				</s:if>
				<%-- <s:if test="#request.activityBorrowList.size>0">
					<div class="boxFour"
						style="position: absolute; height: 163px; top: 921px; width: 768px; border: 1px solid #c9cacb">
				</s:if>
				<s:else>
					<div class="boxFour"
						style="position: absolute; height: 163px; top: 700px; width: 768px; border: 1px solid #c9cacb">
				</s:else>
				<span style="font-size: 14px; position: relative; top: -35px"><strong>体验金</strong></span>
				<span
					style="position: relative; top: 20px; left: 20px; font-size: 14px">体验金总额：
					<span style="color: #eabc46; top: 940px; left: -123px"> <s:if
							test="#request.experiencemoneyMap != null">
							${experiencemoneyMap.totalAmount}
							</s:if> <s:else>
							0.00
							</s:else>
				</span><span>元</span>
				</span> <span
					style="position: relative; top: 48px; left: -136px; font-size: 14px">待收体验金：
					<span style="color: #eabc46; top: 940px; left: -123px"> <s:if
							test="#request.experiencemoneyMap != null">
							${experiencemoneyMap.collectMoney}
							</s:if> <s:else>
							0.00
							</s:else>
				</span><span>元</span>
				</span> <span
					style="position: relative; top: 74px; left: -269px; font-size: 14px;">体验金到期时间：
					<span
					style="color: #eabc46; top: 940px; left: -123px; font-size: 12px; color: #9f77b6; background-image: url(images/dateButton.png)">
						<s:if test="#request.experiencemoneyMap != null">
							${experiencemoneyMap.timeEnd}
							</s:if> <s:else>
							&nbsp;
							</s:else>

				</span>
				</span> <span
					style="position: relative; top: 20px; left: -29px; font-size: 14px">可用体验金：
					<span style="color: #eabc46; top: 940px; left: -123px"> <s:if
							test="#request.experiencemoneyMap != null">
							${experiencemoneyMap.usableMoney}
							</s:if> <s:else>
							0.00
							</s:else>
				</span><span>元</span>
				</span>
				<div
					style="position: relative; top: 28px; left: 545px; font-size: 14px">
					冻结体验金： <span style="color: #eabc46; top: 940px; left: -123px">
						<s:if test="#request.experiencemoneyMap != null">
							${experiencemoneyMap.freezeMoney}
							</s:if> <s:else>
							0.00
							</s:else>
					</span><span>元</span>
				</div>
			</div> --%>
		</div>
	</div>
	</div>
</body>
</html>
