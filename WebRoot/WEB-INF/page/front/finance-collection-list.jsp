<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th align="center">期数</th>
    <th align="center">应还金额</th>
    <th align="center">催收结果</th>
    <th align="center">催收时间</th>
  </tr>
  <s:if test="%{#request.collectionList !=null && #request.collectionList.size()>0}">
      <s:iterator value="#request.collectionList" id="bean">
      <tr>
          <td align="center">${bean.repayPeriod}</td>
          <td align="center">${bean.forPI}</td>
          <td align="center">${bean.colResult} </td>
          <td align="center">${bean.collectionDate} </td>
      </tr>
  </s:iterator>        
  </s:if>
  <s:else>
      <td colspan="4" align="center">没有数据</td>
  </s:else>
</table> 