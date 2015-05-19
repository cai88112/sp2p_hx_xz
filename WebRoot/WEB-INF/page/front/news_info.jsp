<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>

<div class="lbox">
<h2>网站动态</h2>
<div class="dongtai" style="padding-bottom:30px">
   <ul>
     	<s:iterator value="#request.newsList" var="bean">
         <li><a href="frontNewsDetails.do?id=${bean.id }" title="${bean.title }" ><shove:sub value="title" size="19" /></a></li>
     	</s:iterator>        
  </ul>
</div>
</div>               
<!--中间右侧 结束-->

