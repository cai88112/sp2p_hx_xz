<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
 <script type="text/javascript" src="script/jquery-1.7.1.min.js"></script>
 <script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
 <style>
 #info1 .biaoge{
 position: relative;
 left: 0px;
 }
 </style>
 
 
 
<script>
$(function(){
	$('.tabtil').find('li').click(function(){
	$('.tabtil').find('li').removeClass('on');
	$(this).addClass('on');
	$('.tabtil').nextAll('div').hide();
    $('.tabtil').nextAll('div').eq($(this).index()).show();
	})
	})
</script>
<script>
$(function(){
	$('.otherh2x span').click(function(){
		
		$('.otherh2x').nextAll('div').hide();
		$('.otherh2x').nextAll('div').eq($(this).index()).show();
		})
	})
</script>
<script>
//加载用户信用信息info
$(function(){
  if('1'=='${flag}'){
   $.post("queryUserIntegral.do",null,function(data){
    $("#info1").html("");
     $("#info1").html(data);
   });
   }else if('2'=='${flag}'){
   $('#vi').trigger("click");
   
     $.post("queryUservip.do",null,function(data){
     $("#info2").html("");
     $("#info2").html(data);
    
  }); 
   }else{
     /*  $.post("queryUserIntegral.do",null,function(data){
    $("#info1").html("");
     $("#info1").html(data);
     }); */
      
      
      $.post("queryUservip.do",null,function(data){
    	     $("#info2").html("");
    	     $("#info2").html(data);
      });
   }
});
</script>
<script>
//点击查看用户的vip信息
$(function(){
    $("#vi").click(function(){
      var param = {};
      param["pageBean.pageNum"] = 1;
      initListInfo(param);
    });
  //点击查看用户的信用积分信息
  $("#cr").click(function(){
    $.post("queryUserIntegral.do",null,function(data){
    $("#info1").html("");
     $("#info1").html(data);
   });
     });
});
</script>
<script>
//分页
function initListInfo(praData) {
	$.post("queryUservip.do",praData,initCallBack);
}
	
function initCallBack(data){
	$("#info2").html("");
	$("#info2").html(data);
}
</script>
 <jsp:include page="/include/head.jsp"></jsp:include>
</head>

<body>
<br /><jsp:include page="/include/top.jsp"></jsp:include>
<div class="nymain">
  <div class="wdzh">
    <div class="l_nav">
      <div class="box">
           <!-- 引用我的帐号主页左边栏 -->
         <%@include file="/include/left.jsp" %>
      </div>
    </div>
    <div class="r_main" >
      <div class="box" >   
        <div class="tabtil" style="width: 778px" ><ul><!-- <li class="on" dataIndex="true" id="cr" >基本认证</li> --><li id="vi">会员积分记录</li>      
        </ul>
        </div>
      <!-- <div class="boxmain2">      
       <div class="biaoge" style="margin-top:0px;" id="info1"> 
          </div>
       </div> -->
       <div class="boxmain2" style="margin-top:0px; ">
            <div class="biaoge" id="info2">
          </div>
          </div>
</div>
    </div>
  </div>
</div>

<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<script type="text/javascript" src="script/nav-zh.js"></script>
<script type="text/javascript" src="script/tab.js"></script>
<script>
$(function(){
    //样式选中
     $("#zh_hover").attr('class','nav_first');
	 $("#zh_hover div").removeClass('none');
	 $('#li_3').addClass('on');
});
</script>
</body>
</html>
