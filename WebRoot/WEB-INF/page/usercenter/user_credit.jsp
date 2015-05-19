<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>民蕴财富</title>
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
    <div class="r_main" style="width:auto;border:none; float:none;">
      <div class="box" style=" ">
      <h2>个人信息</h2>
      <div class="box-main">
      <div style="overflow:hidden; height:100%;">
        <div class="pic_info">
          <div class="pic">
          <s:if test="#request.userMsg.personalHead != ''">
              <shove:img src="${userMsg.t_personalHead}" defaulImg="images/default-img.jpg"  width="128" height="128" ></shove:img>
            </s:if>
            <s:else>
             <img src="images/default-img.jpg" width="128" height="128"/>
            </s:else>
           </div>
       <div class="guanzhu"><a id="focusonUser" href="javascript:void(0);"> 关注此用户</a></div>
                    <div class="znx"> <a href="javascript:void(0);" id="sendmail">发送站内信</a></div>
        </div>
        <div class="xx_info" >
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th align="right">用户名： </th>
    <td>${userMsg.t_username }     <s:if test="#request.userVipPicture.vipStatus==2">
    </s:if></td>
  </tr>
  <tr>
    <th align="right">注册时间： </th>
    <td>${userMsg.t_createTime }</td>
  </tr>
  <tr>
    <th align="right">籍    贯：</th>
    <td>${userMsg.t_nativePlacePro }&nbsp;&nbsp;${userMsg.t_nativePlaceCity }</td>
  </tr>
  <tr>
    <th align="right">居住地：</th>
    <td>${userMsg.t_registedPlacePro }&nbsp;&nbsp;${userMsg.t_registedPlaceCity }</td>
  </tr>
  <tr>
    <th align="right">最后登录：</th>
    <td>${userMsg.t_lastDate } </td>
  </tr>
        </table>
        </div>
        <div class="hy_info" style="float:right;margin-right:20px;">
        <p style=" padding-left:0">会员到期：<span>${userMsg.t_vipCreateTime }</span></p>
        <p style=" padding-left:0">个人统计：${userMsg.t_borrow_count }条借款记录，${userMsg.t_invest_count}条投标记录</p>
        <a href="userMeg.do?id=${userId }" class="scbtn">返回</a>
        </div>     
      </div>
      </div>
      </div>
    </div>
  </div>
  <div class="lcnav" style="margin-top:20px;">
    <div class="tab">   
<div class="tabmain">
  <ul><li class="on">信用报告</li>
    </ul>
    </div>
    </div>
    <div class="line">
    </div>
  </div>
  <div class="lcmain">
    <div class="lcmain_l" style="float:none; margin-left:0px; width:auto; ">
<div class="lctab" style="padding:10px 10px;">
    <div class="biaoge" style="margin-top:0px;">
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th colspan="8">信用等级及信用对应分数</th>
    </tr>
  <tr>
    <td align="center">等级</td>
    <td align="center">AA</td>
    <td align="center">A</td>
    <td align="center">B</td>
    <td align="center">C</td>
    <td align="center">D</td>
    <td align="center">E</td>
    <td align="center">HR</td>
    </tr>
  <tr>
    <td align="center">分数</td>
    <td align="center">100以上</td>
    <td align="center">99-90</td>
    <td align="center">89-80</td>
    <td align="center">79-70</td>
    <td align="center">69-50</td>
    <td align="center">49-30</td>
    <td align="center">30以下</td>
    </tr>
  <tr>
    <td align="center">标志</td>
    <td align="center"><img src="images/ico_15.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_13.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_11.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_09.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_07.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_05.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_03.jpg" width="34" height="22" /></td>
    </tr>
          </table>
          </div>
         <div class="biaoge" >
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
    <th colspan="4">分信用总分:${userMsg.t_crediting}分&nbsp;<img src="images/ico_${userMsg.t_credit}.jpg" title="${userMsg.t_crediting}分" width="33" height="22" /> </th>
    </tr>
 <tr>
    <td align="center">&nbsp;</td>
    <td align="center">项目</td>
    <td align="center">状态</td>
    <td align="center">信用分数</td>
    </tr>
  <tr>
    <td rowspan="2" align="center">基本信息</td>
    <td align="center">个人详细信息</td>
    <td align="center">
     
     ${userMsg.t_person_status }
    </td>
    <td align="center">

        ${userMsg.t_person_score }
    </td>
    </tr>
    <tr>
    <td align="center">个人工作信息</td>
    <td align="center">
    	${userMsg.t_work_status }
    </td>
    <td align="center">
    	${userMsg.t_work_score }
    </td>
    </tr>
  <tr>
    <td rowspan="5" align="center">必要信用认证</td>
    <td align="center">身份认证</td>
    <td align="center">
      	${userMsg.t_materrial_1_status }
    </td>
    <td align="center">    
    	${userMsg.t_materrial_1_score }    
    </td>
    </tr>
  <tr>
    <td align="center">工作认证</td>
    <td align="center">
    	 ${userMsg.t_materrial_2_status }
    </td>
    <td align="center">
      ${userMsg.t_materrial_2_score }  
    </td>
    </tr>
  <tr>
    <td align="center">收入认证</td>
    <td align="center">
      ${userMsg.t_materrial_5_status }
    </td>
    <td align="center">   
   ${userMsg.t_materrial_5_score }
    </td>
    </tr>
  <tr>
    <td align="center">信用报告认证</td>
    <td align="center">
       ${userMsg.t_materrial_4_status }  
    </td>
    <td align="center">  
     ${userMsg.t_materrial_4_score }
    </td>
    </tr>
  <tr>
    <td align="center">居住认证</td>
    <td align="center">
         ${userMsg.t_materrial_3_status }
    </td>
    <td align="center">
  ${userMsg.t_materrial_3_score }
    </td>
    </tr>
  <tr>
    <td rowspan="10" align="center">可选认证</td>
    <td align="center">房产</td>
    <td align="center">   
          ${userMsg.t_materrial_6_status }
    </td>
    <td align="center"> 
    ${userMsg.t_materrial_6_score }   
    </td>
  </tr>
  <tr>
    <td align="center">购车</td>
    <td align="center">
       ${userMsg.t_materrial_7_status }
    </td>
    <td align="center">
${userMsg.t_materrial_7_score }
    </td>
  </tr>
  <tr>
    <td align="center">结婚</td>
    <td align="center">
          ${userMsg.t_materrial_8_status }
    </td>
    <td align="center">
 ${userMsg.t_materrial_8_score }
    </td>
  </tr>
  <tr>
    <td align="center">学历</td>
    <td align="center">   
          ${userMsg.t_materrial_9_status }   
    </td>
    <td align="center">   
  ${userMsg.t_materrial_9_score }   
    </td>
  </tr>
  <tr>
    <td align="center">技术</td>
    <td align="center"> 
         ${userMsg.t_materrial_10_status }
    </td>
    <td align="center">
${userMsg.t_materrial_10_score }
</td>
  </tr>
  <tr>
    <td align="center">手机</td>
    <td align="center">
          ${userMsg.t_materrial_11_status }
</td>
    <td align="center">
${userMsg.t_materrial_11_score }  
    </td>
    </tr>
  <tr>
    <td align="center">微博</td>
    <td align="center">
         ${userMsg.t_materrial_12_status }   
    </td>
    <td align="center">
  ${userMsg.t_materrial_12_score }
    </td>
    </tr>
   <!--  
  <tr>
    <td align="center">视频</td>
    <td align="center">
     
      <s:if test="#request.map.tmshipingauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmshipingauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmshipingauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    </td>
    <td align="center">   
 ${map.tmshipingcriditing }    
    </td>
    </tr>
   --> 
  <tr>
    <td align="center">现场</td>
    <td align="center">
       ${userMsg.t_materrial_13_status }  
    </td>
    <td align="center">   
  ${userMsg.t_materrial_13_score }    
    </td>
    </tr>
  <tr>
    <td align="center">抵押</td>
    <td align="center">
       ${userMsg.t_materrial_14_status }   
    </td>
    <td align="center">   
 ${userMsg.t_materrial_14_score }
    </td>
      <tr>
          <td align="center">担保</td>
    <td align="center">
       ${userMsg.t_materrial_15_status }   
    </td>
    <td align="center">    
 ${userMsg.t_materrial_15_score }  
    </td>
    </tr> 
   <tr>
    <td rowspan="7" align="center">民蕴财富还款积分</td>
    <th align="center">项目</th>
    <th align="center">次数</th>
    <th align="center">分数</th>
  </tr>
   <tr>
    <td align="center">提前还款</td>
    <td align="center">
    <s:if test="#request.userMsg!=null">${userMsg.t_pre_count}</s:if>
    <s:else>0</s:else>
    </td>
    <td align="center">
    	<s:if test="#request.userMsg!=null">${userMsg.t_pre_score }分</s:if>
    	<s:else>0分</s:else>
    </td>
  </tr>
  <tr>
    <td align="center">按时还款</td>
    <td align="center">
        <s:if test="#request.userMsg!=null">${userMsg.t_pre_16_count }</s:if>
    <s:else>0</s:else>
    </td>
    <td align="center">
    <s:if test="#request.userMsg!=null">${userMsg.t_pre_16_score }分</s:if>
    <s:else>0分</s:else>   
    </td>
    </tr>
  <tr>
    <td align="center">迟还款（逾期一天以上至10天以内的还款）</td>
    <td align="center">    <s:if test="#request.userMsg!=null">${userMsg.t_over_10_count }</s:if>
    <s:else>0</s:else></td>
    <td align="center">    <s:if test="#request.userMsg!=null">
    <s:if test="#request.userMsg.t_over_10_score==0">
    0分
    </s:if>
    <s:else> -${userMsg.t_over_10_score }分</s:else>  
    </s:if>
    <s:else>0分</s:else></td>
    </tr>
  <tr>
    <td align="center">逾期还款（11-30天）</td>
    <td align="center"><s:if test="#request.userMsg!=null">${userMsg.t_over_30_count }</s:if>
    <s:else>0</s:else></td>
    <td align="center"><s:if test="#request.userMsg!=null">
    <s:if test="#request.userMsg.t_over_30_score==0">
    0分
    </s:if>
    <s:else> -${userMsg.t_over_30_score }分</s:else>
    </s:if>
    <s:else>0分</s:else></td>
    </tr>
  <tr>
    <td align="center">逾期还款（逾期31天以上至90天以内）</td>
    <td align="center"><s:if test="#request.userMsg!=null">${userMsg.t_over_90_count }</s:if>
    <s:else>0</s:else></td>
    <td align="center"><s:if test="#request.userMsg!=null">
    <s:if test="#request.userMsg.t_over_90_score==0">
    0分
    </s:if>
    <s:else> -${userMsg.t_over_90_score }分</s:else>
    </s:if>
    <s:else>0分</s:else></td>
    </tr>
  <tr>
    <td align="center">逾期还款（逾期90天以上的还款）</td>
    <td align="center"><s:if test="#request.userMsg!=null">${userMsg.t_over_91_count }</s:if>
    <s:else>0</s:else></td>
    <td align="center"><s:if test="#request.userMsg!=null">
      <s:if test="#request.userMsg.t_over_91_score==0">
    0分
    </s:if>
    <s:else> -${userMsg.t_over_91_score }分</s:else>
    </s:if>
    <s:else>0分</s:else></td>
    </tr>
          </table>
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
       $(function(){
       $('#focusonUser').click(function(){
       	   var param = {};
	     param['paramMap.id'] = '${userMsg.id }';
	     $.post('focusonUser.do',param,function(data){
	       var callBack = data.msg;
		   alert(callBack);
		 });
	   });
	   
	   $('#sendmail').click(function(){
	   var param = {};
	      var id = '${userMsg.id }';
	      var username = '${userMsg.username }';
	      var url = "mailInit.do?id="+id+"&username="+username;
	      $.post('mailInit.do',param,function(data){
	    	  $.jBox("iframe:" + url, {
	 		     title: "发送站内信",
	 		     width: 500,
	 		     height: 400,
	 		     buttons: {  }
	 		  });
		  });
	   });      
    });
</script>
</body>
</html>
