<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <jsp:include page="/include/head.jsp"></jsp:include>
    <link href="css/inside.css"  rel="stylesheet" type="text/css" />
</head>
<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	
<div class="nymain">
  <div class="ifbox1">
  <h2>债权详情
 <img src="images/neiye2_06.jpg" width="4" height="7" /></h2>
  <div class="boxmain">
    <div class="pic-info">
      <div class="tx">
       <shove:img src="${paramMap.imgPath }" defaulImg="images/default-img.jpg" width="180" height="180" ></shove:img>
      </div>
           <!-- Baidu Button BEGIN -->
<div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
<a class="bds_qzone"></a>
<a class="bds_tsina"></a>
<a class="bds_tqq"></a>
<a class="bds_renren"></a>
<a class="bds_t163"></a>
<span class="bds_more">更多</span>
<a class="shareCount"></a>
</div>
<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=6638061" ></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000)
</script>
<!-- Baidu Button END -->
      
      </div>
      <div class="xqbox">
      <h3>${paramMap.borrowTitle }</h3>
      <div class="xqboxmain">
      <div class="xqtop">
        <div class="money">
        <p>债权总额：<strong>￥${paramMap.debtSum }元</strong></p>
        <s:hidden id="debtId" name="paramMap.debtId"></s:hidden>
		<p>竞拍底价：<strong style="font-size:12px;">￥${paramMap.auctionBasePrice }元</strong></p>
        </div>
        <div class="tbbtn">
        <s:if test='%{paramMap.debtStatus==3||paramMap.remainDays=="过期"}'>
        	<img src="images/tubiao5.png" />
        </s:if>
        <s:else>
	         <s:if test="%{#session.user==null}">
		        <s:if test='%{paramMap.debtStatus==2&&paramMap.remainDays!="过期"}'>
		        	<input type="button" class="jjiabtn" value="" onclick="location.href='auctingDebtInit.do?debtId=${paramMap.debtId }'"/>
		        </s:if>		      
	        </s:if>
	        <s:else>
		        <s:if test="%{#session.user.id!=paramMap.alienatorId && #session.user.id!=paramMap.borrowerId}">
			        <s:if test='%{paramMap.debtStatus==2&&paramMap.remainDays!="过期"}'>
			        	<input type="button" class="jjiabtn" value="" onclick="location.href='auctingDebtInit.do?debtId=${paramMap.debtId }'"/>
			        </s:if>
		        </s:if>
	        </s:else>
        </s:else>
        <s:hidden id="debtStatus" name="paramMap.debtStatus"></s:hidden>
        </div>
      </div>
      <div class="xqbottom">
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>债权期限：${paramMap.debtLimit }个月
  </td>
    <td>竞拍模式：<s:if test="%{paramMap.auctionMode==1}">
    				明拍</s:if>
    				<s:else>暗拍</s:else>
  </td>   
  </tr>
  <tr>
    <td colspan="2">竞拍数：<s:if test="paramMap.auctionCount==\"\"">0</s:if><s:else>${paramMap.auctionCount}</s:else>   浏览量：${paramMap.viewCount }</td>
    <td>剩余时间：<span id="span_remainDays">${paramMap.remainDays }</span>
    	<s:hidden id="remainDays" name="paramMap.remainDays"></s:hidden>
    	<s:hidden id="maxAuctionPrice" name="paramMap.maxAuctionPrice"></s:hidden>
    	<s:hidden id="auctionMode" name="paramMap.auctionMode"></s:hidden>
    </td>
  </tr>
  <tr>
    <td colspan="3">发布时间：${paramMap.publishTime }
</td>
    </tr>
</table>
      </div>
      </div>
      </div>
      <div class="reninfo">
        <div class="rinfomain">
        <div class="tx">
        	<shove:img src="${userMap.personalHead}" defaulImg="images/default-img.jpg" width="62" height="62" ></shove:img>
        </div>
        <div class="jfico">
        <img src="images/ico_r_${userMap.ratingIco}.gif"/></div>
        <div class="name">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>用户名：${userMap.username}
    </td>
  </tr>
  <tr>
    <td>居住地： ${userMap.address} 
    </td>
  </tr>
  <tr>
    <td>信用指数： <img src="images/ico_${userMap.creditratingIco}.jpg" title="${userMap.creditrating}分" width="33" height="22" /></td>
  </tr>
  <tr>
    <td>注册时间：${userMap.createTime} </td>
  </tr>
  <tr>
    <td>最后登录：${userMap.lastDate}</td>
  </tr>
</table>
        </div>
        </div>
      </div>
  </div>
  </div>
  <div class="ifbox2">
    <div class="til" style="background-image: url(images/neiye2_32.jpg); background-repeat: repeat-x;">
    <ul><li class="on">转让描述
</li>
    </ul>
    <div class="til_bot">
    </div>
    </div>
    <div class="boxmain">
    <p class="txt">${paramMap.details }</p>
    </div>
  </div>
  <span id="borrow_detail">
 	 <img src="images/load.gif" class="load" alt="加载中..." />
  </span> 
   </div>
<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<script type="text/javascript" src="script/nav-zq.js"></script>
<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
<script>
$(function(){
    //样式选中
     $("#zq_hover").attr('class','nav_first');
	 $("#zq_hover div").removeClass('none');
	  
	 if($("#debtStatus").val() != 2){
	 	$("#span_remainDays").html("");
	 }
	 var param = {};
	 param["id"] = '${paramMap.borrowId}';
	 $.shovePost("queryDebtBorrowDetail.do",param,function(data){
	 	$("#borrow_detail").html(data);
	 });
});		     
</script>
</body>
</html>
