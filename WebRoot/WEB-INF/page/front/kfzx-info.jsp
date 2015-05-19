<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>

<div class="box" style="padding-bottom:20px;">
<h2 style=" text-align: center;  font-size: 16px;color: #fff;  
    height: 40px;  line-height: 40px;  padding: 0;background-image: none; 
    background-color: #fabe00;">联系客服</h2>
 <div class="boxmain">
 
 <s:if test="#request.lists == null || #request.lists.size<=0">
     暂无数据
</s:if>
<s:else>
 <ul class="kefu">
  <s:iterator value="#request.lists" var="bean" status="sta">
        <li>
        <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${QQ}&site=qq&menu=yes" class="tx">
        <shove:img src="${bean.kefuImage }" defaulImg="images/hslogo_42.jpg" title="${bean.name }" width="72" height="72"  ></shove:img>
        </a><br/>
  <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${QQ}&site=qq&menu=yes">${bean.name }</a><br/>
  <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=${QQ}&site=qq&menu=yes">
  <img border="0" src="http://wpa.qq.com/pa?p=1:${QQ}:1" 
  alt="点击这里给我发消息" title="点击这里给我发消息"></a>
        </li>
  </s:iterator>
  </ul>
</s:else>
</div>
</div>
