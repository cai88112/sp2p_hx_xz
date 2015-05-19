<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
    <link href="css/inside.css"  rel="stylesheet" type="text/css" />
</head>

<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	

<div class="nymain">
  <div class="wdzh">
    <div class="l_nav">
      <div class="box">
        <!-- 引用我的帐号主页左边栏 -->
         <%@include file="/include/left.jsp" %>
      </div>
    </div>
    <div class="r_main">
      <div class="tabtil" style="width: 778px">
        <ul><li class="on">邀请好友</li>
        <li id="attention">关注用户</li>
        </ul>
        </div>
      <div class="box">
        <div class="boxmain2">
         <p class="tips">温馨提示：请不要发送邀请信给不熟悉的人士,避免给别人带来不必要的困扰。

请把下边的链接地址发给您的好友，这样您就成了他的邀请者。

您邀请的好友注册vip并成功付费后，那么您可以一次性获得10元的奖金。

每月结算已充值成功后，通过网站充值方式打到您的账上。</p>
   <p class="tips">邀请链接：<span id="yq_address_input">${url}reg.do?param=${userId}</span><a  id="yq_address_btn" class="scbtn" style="margin-left:20px;">复制</a></p>
         <div class="biaoge" id="hyyq">
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
    </div>
    </div>   
</div>  
	  <div id="userfrends" class="box" style="display:none;">    
	        <!--关注好友部分-->
	  </div>
    </div>
  </div>
</div>

 <!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
<script type="text/javascript" src="script/nav-zh.js"></script>
<script type="text/javascript" src="script/jquery.zclip.min.js"></script>	
 <script>
$(function(){
    $("#zh_hover").attr('class','nav_first');
	$("#zh_hover div").removeClass('none');
	$('#li_6').addClass('on');
	  
    $("#attention").click(function(){
            var param = {};
          param["paramMap.id"] =${user.id}
          param["paramMap.attention"] ="attention";
          $.shovePost("userFrends.do",param,function(data){
           $("#userfrends").html(data);
         });
    });
    $('#yq_address_btn').click(function(){
        if($.browser.msie){
           var txt = '复制文本到剪贴板:\n\n';
           txt = txt+$('#yq_address_input').html();
           window.clipboardData.setData('text', $('#yq_address_input').html());
           alert(txt);
        }
    });
  
	$('.tabtil').find('li').click(function(){
	    $('.tabtil').find('li').removeClass('on');
	    $(this).addClass('on');
	    $('.tabtil').nextAll('div').hide();
         $('.tabtil').nextAll('div').eq($(this).index()).show();
	});
	init();
});
function init(){
  if(!$.browser.msie){
      $('#yq_address_btn').zclip({
         path:'script/ZeroClipboard.swf',
         copy:function(){return $('#yq_address_input').html();}
      });
  }
}   
function initListInfo(){
 	$.shovePost("friendManagerList.do",param,function(data){
 	   $("#hyyq").html(data);
	});
}
</script>
</body>
</html>

