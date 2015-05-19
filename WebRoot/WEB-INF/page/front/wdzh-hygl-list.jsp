<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<link href="css/css.css" rel="stylesheet" type="text/css" />

 <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th>邀请的好友</th>
    <th>注册时间</th>
    <th>VIP成功付费时间</th>
    <th>奖励</th>
    </tr>
    <s:if test="pageBean.page==null || pageBean.page.size==0">
		<tr align="center" class="gvItem">
			<td colspan="4">暂无数据</td>
		</tr>
	</s:if>
	<s:else>
	   <s:iterator value="pageBean.page" var="bean" >
        <tr>
          <td align="center">${bean.username}</td>
          <td align="center">${bean.createTime }</td>
          <td align="center">${bean.vipCreateTime}</td>
          <td colspan="6" align="center">${bean.money}</td>
        </tr>
       </s:iterator>
	</s:else>
    </table>
<div class="fenye">
  	<div class="fenyemain">
    	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
	</div>
</div>