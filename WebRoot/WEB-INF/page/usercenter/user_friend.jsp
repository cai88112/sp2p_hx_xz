<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />
<script src="script/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>
$(function(){
	$('.tabmain').find('li').click(function(){
	$('.tabmain').find('li').removeClass('on');
	$(this).addClass('on');
	$('.lcmain_l').children('div').hide();
    $('.lcmain_l').children('div').eq($(this).index()).show();
	})
	})
</script>
 <jsp:include page="/include/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/include/top.jsp"></jsp:include>
<div class="nymain">
  <div class="wdzh">
    <div class="r_main" style="border:none; width:950px">
      <div class="box" >
      <h2 style="font-size:16px">个人信息</h2>
      <div class="box-main">
      <div style="overflow:hidden; height:100%;">
        <div class="pic_info">
          <div class="pic">
          <shove:img defaulImg="images/default-img.jpg" src="${userMsg.personalHead}" width="128" height="128"></shove:img>
          </div>
                    <div class="guanzhu"><a id="focusonUser" href="javascript:void(0);"> 关注此用户</a></div>
                    <div class="znx"> <a href="javascript:void(0);" id="sendmail">发送站内信</a></div>
        </div>
        <div class="xx_info" >
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th align="right">用户名：</th>
    <td>  
      ${userMsg.username } 
    <s:if test="#request.userVipPicture.vipStatus==2">
    </s:if>
    </td>
  </tr>
  <tr>
    <th align="right">注册时间： </th>
    <td>
    ${userMsg.createTime }
    </td>
  </tr>
  <tr>
    <th align="right">籍    贯：</th>
    <td>${userMsg.nativePro }&nbsp;&nbsp;${userMsg.nativeCity }</td>
  </tr>
  <tr>
    <th align="right">居住地：</th>
    <td>${userMsg.regProregionName }&nbsp;&nbsp;${userMsg.regCityregionName }</td>
  </tr>
  <tr>
    <th align="right">最后登录：</th>
    <td>${userMsg.lastDate }</td>
  </tr>
        </table>
        </div>
        <div class="hy_info" style=" float:left;">
        <p style="padding:0">会员到期：<span>${userMsg.vip }</span></p>
          <p style="padding:0">个人统计：${BorrowRecode.publisher }条借款记录，${inverseRecorde.investor }条投标记录</p>
         <a href="userCridit.do?id=${userMsg.id }" class="scbtn">信用报告</a>
        </div>
      </div>
      </div>
      </div>
    </div>
  </div>
  <div class="lcnav" style="margin-top:20px;">
    <div class="tab"> 
<div class="tabmain">
  <ul><li class="on"><span id="Dynamic">最近动态</span></li>
     <li ><span id="frieds">关注用户</span></li>  
    <li> <span id="userDynamic">关注用户动态</span></li> 
    <li><span id="borrow">借款列表</span></li> 
    <li><span id="recorde">投资记录</span></li> 
    </ul>
    </div>
    </div>
    <div class="line">
    </div>
  </div>
  <div class="lcmain">
    <div class="lcmain_l" style="float:none; margin-left:0px; width:auto; ">
<div class="lctab" style="padding:0 10px;">
    <div class="dylist" id="Dynamiclist">
<!--  用户最近动态列表 -->
    </div>
<div class="fenye">
  <div class="fenyemain">
   </div>
  </div>
    </div>
    <div class="lctab" style="padding:0 10px; display:none;">
    <div class="gzfirend">
    <div id="frendlist"></div>
    </div>
<div class="fenye">
  <div class="fenyemain">
   </div>
</div>
    </div>
    <div class="lctab" style="padding:0 10px; display:none;">
    <div class="dylist" id="userDynamiclist">
   <!-- 关注好友动态 -->
    </div>
<div class="fenye">
  <div class="fenyemain">
    </div>
  </div>
    </div>   
    <div class="lctab" style="display:none;" id="browlist">
       <!-- 投资列表 -->
    <div class="fenye">
    <div class="fenyemain">
    </div>
    </div>
    </div>
    <div class="lctab" style="display:none;">
      <div class="biaoge" id="recordelist">    
      <!-- 投资记录 -->
          </div>
    <div class="fenye">
    <div class="fenyemain">
    </div>
    </div>
    </div>
    </div>
  </div>
</div>
<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<script type="text/javascript" src="css/popom.js"></script>
<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>

<script>
     var param = {};
     param["paramMap.id"] = '${userMsg.id }'
     $.post("queryUserRecoreIndex.do",param,function(data){
     	$("#Dynamiclist").html(data);
     }); 
       $(function(){
       $('#focusonUser').click(function(){
       	   var param = {};
	     param['paramMap.id'] = '${userMsg.id }';
	     //modify by houli 不能关注自己
	      var username = '${userMsg.username }';
	      var uname = '${user.userName}';
	      if(username == uname){
	         alert("您不能关注自己");
	         return false;
	      }
	     //
	     $.post('focusonUser.do',param,function(data){
	     	if(data == "virtual"){
	     		window.location.href = "noPermission.do";
	     	}else{
	     		var callBack = data.msg;
		  		alert(callBack);
	     	}
		 });
	   });
	   
	   $('#sendmail').click(function(){
	   var param = {};
	      var id = '${userMsg.id }';
	      var username = '${userMsg.username }';
	      //modify by houli 不能给自己发送站内信
	      var uname = '${user.userName}';
	      if(username == uname){
	         alert("您不能给自己发送站内信");
	         return false;
	      }
	      //
	      var url = "mailInit.do?id="+id+"&username="+username;
	      $.post('mailInit.do',param,function(data){
	       if(data == "virtual"){
	     		window.location.href = "noPermission.do";
	     	}else{
	     		jBox.open("post:"+url, "发送站内信", 500,400,{ buttons: { } });
	        }            
		  });
	   });
       
    });
</script>
<script>
 $(function(){ 
    //点击好友动态
    $("#frieds").click(function(){
      var param = {};
      param["pageBean.pageNum"] = 1;
      param["paramMap.id"] = '${userMsg.id }'
      $.post("userFrends.do",param,function(data){
      $("#frendlist").html(data);
       });
    });
       
    //最近动态
    $("#Dynamic").click(function(){
     var param = {};
     param["paramMap.id"] = '${userMsg.id }'
      $.post("queryUserRecoreIndex.do",param,function(data){
      $("#Dynamiclist").html(data);
       }); 
    });
       
    //借款列表
    $('#borrow').click(function(){
    var param = {};
    param["paramMap.id"] = ${userMsg.id }
    $.post("myBorrowList.do",param,function(data){
      $("#browlist").html(data);
       });     
    }); 
    
    //投资记录
      $('#recorde').click(function(){
    var param = {};
    param["paramMap.id"] = ${userMsg.id }
    $.post("queryBorrowRecode.do",param,function(data){
      $("#recordelist").html(data);
       });     
    });
    
    //关注好友动态
     $('#userDynamic').click(function(){
    var param = {};
    param["paramMap.id"] = ${userMsg.id }
    $.post("queryfrendrecoredIndex.do",param,function(data){
      $("#userDynamiclist").html(data);
       });     
    });
 });
</script>
</body>
</html>
