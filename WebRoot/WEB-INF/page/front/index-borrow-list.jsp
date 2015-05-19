<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<s:if test="#request.carOrHouseList.size>0">
			<dl>
				<s:iterator value="#request.carOrHouseList" var="finance">
						<div
							style="height: 67px; border-bottom: solid; border-width: 1px; border-color: #dde2e6">
							<dd style="padding-left: 20px;  width: 33%; height: 67px; float: left; background-color: #ffffff;">
							<img src="images/img_littegraypig.png" style="top: 16px;position: relative;" />
								<a href="financeDetail.do?id=${finance.id}" target="_blank" style="top: 22px;position: relative;">${finance.borrowTitle}</a>
							</dd>
							<dd
								style=" width: 15%; height: 67px; float: left; text-align: center; background-color: #ffffff;">
								<img src="images/img_reward1.png" style="top: 2px;left:119px;position: relative;" />
								<span style="top: 22px;position: relative;">${finance.borrowAmount}</span>
							</dd>
							<dd
								style=" width: 15%; height: 67px;float: left; text-align: center; color: #ff7400; background-color: #ffffff;">
								<span style="top: 22px;position: relative;">${finance.annualRate}%</span>
							</dd>
							<dd
								style=" width: 10%; height: 67px;float: left; text-align: center; background-color: #ffffff;">
								<span style="top: 22px;position: relative;">
								${finance.deadline}
								<s:if test="%{#finance.isDayThe ==1}">个月</s:if>
								<s:else>天</s:else>
								</span>
							</dd>
							<dd style=" width: 10%; height: 67px;float: left; text-align: center; background-color: #ffffff;vertical-align: middle;">
								
									<canvas class="process" width="40px" height="40px" style="top: 18px;position: relative;"><fmt:formatNumber
									type="number" value="${finance.schedules}"
									maxFractionDigits="0" />%</canvas>
							</dd>
							<dd
								style=" width: 15%; height: 67px;float: left; text-align: center; background-color: #ffffff;">
								<div class="btn5">
									<s:if test="%{#finance.borrowStatus == 1}">
										初审中
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
		 <s:else>
      <div text-align="center">没有数据</div>
  </s:else>