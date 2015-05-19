<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <jsp:include page="/include/head.jsp"></jsp:include>
</head>
<body>
<div class="nymain" style="width:700px;" >
  <div class="wdzh"  style="width:700px;">
    <div class="r_main"  style="width:700px;">
<div class="box" style="border:none;">
  <div class="boxmain2">
         <div class="biaoge">
   <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th style="width: 40px;">期数</th>
    <th style="width: 85px;">还款时间</th>
    <th style="width: 100px;">本期应收本金</th>
    <th style="width: 100px;">本期应收利息</th>
    <th style="width: 70px;">剩余本金</th>
    <th style="width: 85px;">利息管理费</th>
    <th style="width: 70px;">是否逾期</th>
    <th style="width: 70px;">逾期天数</th>
    <th style="width: 70px;">逾期罚息</th>
    <th style="width: 40px;">收益</th>
    <th style="width: 80px;">还款人</th>
    </tr>
  	<s:if test="request.listMap==null || request.listMap.size==0">
	   <tr align="center">
		  <td colspan="11">暂无数据</td>
	   </tr>
	</s:if>
	<s:else>
	<s:iterator value="#request.listMap" var="bean">
	 <tr>
	   <td align="center">${bean.repayPeriod}</td>
	   <td align="center">${bean.repayDate}</td>
	   <td align="center">${bean.forpayPrincipal}</td>
	   <td align="center">${bean.forpayInterest}</td>
	   <td align="center">${bean.principalBalance}</td>
	   <td align="center">${bean.manage}</td>
	   <td align="center"><s:if test="%{#bean.isLate == 1}">否</s:if><s:else>是</s:else></td>
	   <td align="center">${bean.lateDay}</td>
	   <td align="center">${bean.forFI}</td>
	   <td align="center">${bean.earn}</td>
	   <td align="center"><s:if test="%{#bean.isWebRepay == 2}">网站垫付</s:if><s:else>${bean.username}</s:else></td>
       </tr>
     </s:iterator>
	</s:else>
</table>
          </div>
    </div>
</div>
    </div>
  </div>
</div>
</body>
</html>
