<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th align="center">序号</th>
    <th align="center">还款日期</th>
    <th align="center">已还本息</th>
    <th align="center">待还本息</th>
    <th align="center">已付罚息</th>
    <th align="center">待还罚息</th>
    <th align="center">状态</th>
  </tr>
  <s:if test="%{#request.repayList !=null && #request.repayList.size()>0}">
      <s:iterator value="#request.repayList" id="bean">
      <tr>
          <td align="center">${bean.repayPeriod}</td>
          <td align="center">${bean.repayDate}</td>
          <td align="center">${bean.hasPI} </td>
          <td align="center">${bean.stillPI} </td>
          <td align="center">${bean.hasFI} </td>
          <td align="center">${bean.lateFI} </td>
          <td align="center">
          <s:if test="#bean.repayStatus == 1">
                                      未偿还
          </s:if>
          <s:elseif test="#bean.repayStatus == 2">
                                       已偿还
          </s:elseif>
          <s:elseif test="#bean.repayStatus == 3">
                                       偿还中
          </s:elseif>
          </td>
      </tr>
  </s:iterator>        
  </s:if>
  <s:else>
      <td colspan="7" align="center">没有数据</td>
  </s:else>
</table>
