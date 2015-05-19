<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <jsp:include page="/include/head.jsp"></jsp:include>
</head>
<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	

<div class="nymain">
  <div class="lcnav">
    <div class="tab">
    
		<div class="tabmain">
	  		<ul><li class="on">利息计算器</li> <li>IP查询</li> 
		    <li>手机号码查询</li> 
		    <li><span onclick="initList();" id="borrowClause">借款协议范本</span></li> 
		    </ul>
	    </div>
    </div>
    <div class="line">
    </div>
  </div>
  <div class="lcmain">
    <div class="lcmain_l" style="float:none; margin-left:0px; width:auto; padding:10px 25px 50px;">
    <div class="box">
    <div class="gjxtab">
    <table width="90%" border="0" cellspacing="0" cellpadding="0">
	  <tr>
	    <td>借款金额：<input id="borrowSum" type="text" class="inp100x" /></td>
	    <td>年利率：
	      <input id="yearRate" type="text" class="inp100x" />%</td>
	      
	    <td>借款期限：
	      <input id="borrowTime" type="text" class="inp100x" /><span id="timeType">月</span></td>
	    <td>还款方式：
	      <select id="repayWay" name="select" >
	        <option selected="selected">按月还款</option>
	        <option>先息后本</option>
	         <option>一次还本付</option>
	        </select>      
	     <a href="javascript:rateCalculate();" class="chaxun">计算</a></td>
	     <td>
	      <input id="manual" class="helpId" type="checkbox"
									value="" name="cb_ids" onclick="changeStatus();" />&nbsp;天标
	     </td>
	    </tr>
  	</table>
    </div>
    <div id="totalResult"  >
    	<p class="gjjg">每个月将偿还：<strong id="aa">0.00元</strong>  月利率：<strong id="bb">0%</strong>  
    	  还款本息总额：<strong id="cc">0元</strong></p>
    	  </div>
    	  
    	  <div id="result" style="display: none">
    	    <p class="gjjg"> 
    	  	还款本息总额：<strong id="dd">0元</strong></p>
    	  </div>
    	<br />
    	<strong><span style="color: red; float: none;" class="formtips" id="show_error"></span></strong>
    	<span id="showPayTable"></span>
    </div>
    <div class="box" style="display:none;">
    <p class="tips">在下面输入框中输入您要查询的IP地址,点击查询按钮即可查询该IP所在地区。</p>
    <div class="gjxtab">
    <table width="90%" border="0" cellspacing="0" cellpadding="0">
  	<tr>
    <td>IP地址：
      <input type="text" class="inp280" id="ipAddress" /> <a href="javascript:void(0);" class="chaxun" onclick="javascript:queryIP();" >查询</a></td>
    </tr>
	</table>

    </div>
    <p class="gjjg">查询结果：<strong>
		    <span id="queryIPInfo">暂无查询信息</span>
		    </strong></p>
    </div>
    <div class="box" style="display:none;">
	    <div class="gjxtab">
	    <table width="90%" border="0" cellspacing="0" cellpadding="0">
		  	<tr>
		    	<td>请输入手机号：
		      <input type="text" class="inp280" id="phoneNum" /> <a href="javascript:void(0);" class="chaxun" onclick="javascript:queryPhone();">查询</a></td>
		    </tr>
		</table>
	    </div>
	    <p class="gjjg" >查询结果：
		    <strong>
		    <span id="queryPhoneInfo">暂无查询信息</span></strong></p>
		    
		    </div>
		    <div class="box">		       
		      <span id="borrow_clause"></span>
		    </div>
		    </div>
  </div>
	<!-- 引用底部公共部分 -->     
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="script/nav-lc.js"></script>
    <script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
<script>
$(function(){
    //样式选中
     $("#licai_hover").attr('class','nav_first');
	<%-- $("#licai_hover div").removeClass('none');
	 $('.tabmain').find('li').click(function(){
	    $('.tabmain').find('li').removeClass('on');
	    $(this).addClass('on');
	 });--%>	 
});		     

	
</script>
    <script type="text/javascript">
    
    function initList(){
	   $.post("getMessageBytypeId.do?typeId=15", function(data) {
			$("#borrow_clause").html(data);
		});
	}
	
        function queryPhone(){//手机归属地查询
        	param["paramMap.phoneNum"] = $("#phoneNum").val();
        	$.shovePost("queryPhoneInfo.do",param,queryPhoneBack);
        }
        
        function queryPhoneBack(data){
        	$("#queryPhoneInfo").html(data);
            //alert(data);
	 		/*var array=data.split("|");
	 		if(array[1]=="true"){//新窗口打开查询结果
	 		  $("#queryPhoneInfo").html("");
	 		  window.open(array[0]);
	 		}else{//显示错误信息
	 		   $("#queryPhoneInfo").html(array[0]);
	 		}*/
	 		//window.location = $("#queryPhoneInfo").text();
	 		//document.getElementById("myPhone").href=$("#queryPhoneInfo").text();
	 		
	 	}
	 	
	 	function queryIP(){//IP地址查询
        	param["paramMap.ipAddress"] = $("#ipAddress").val();
        	$.shovePost("queryIPInfo.do",param,queryIPBack);
        }
        
        function queryIPBack(data){
	 		$("#queryIPInfo").html(data);
	 		/*var array=data.split("|");
	 		if(array[1]=="true"){//新窗口打开查询结果
	 		  $("#queryIPInfo").html("");
	 		  window.open(array[0]);
	 		}else{//显示错误信息
	 		   $("#queryIPInfo").html(array[0]);
	 		}*/
	 	}
	 	
	 	function rateNumJudge(){//验证利息计算输入数字是否正确
	 	   if($("#borrowSum").val()==""){
	 	      $("#show_error").html("借款金额不能为空");
	 	      $("#showPayTable").html("");
	 	      return;
	 	   }else if(!/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($("#borrowSum").val())){
	 	       $("#show_error").html("请输入正确的数字进行计算");
	 	       $("#showPayTable").html("");
	 	       return;
	 	   }else if($("#yearRate").val()==""){
	 	      $("#show_error").html("年利率不能为空");
	 	      $("#showPayTable").html("");
	 	      return;
	 	   }else if(!/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($("#yearRate").val())){
	 	      $("#show_error").html("请输入正确的年利率");
	 	      $("#showPayTable").html("");
	 	      return;
	 	   }
	 	   if($("#manual").attr("checked")){
	 	      if($("#borrowTime").val()==""){
		 	       $("#show_error").html("借款期限不能为空");
		 	       $("#showPayTable").html("");
			 	   return;
		 	   }else if(isNaN($("#borrowTime").val())){
			 	    $("#show_error").html("请输入正确借款期限");
			 	    $("#showPayTable").html("");
			 	     return;
		 	   }else if($("#borrowTime").val() > 25){
		 	      $("#show_error").html("天标时间不能超过25天");
			 	    $("#showPayTable").html("");
			 	     return;
		 	   }
	 	   }else{
	 	      if($("#borrowTime").val()==""){
		 	       $("#show_error").html("借款期限不能为空");
		 	       $("#showPayTable").html("");
			 	   return;
		 	   }else if(!/^[0-9]*[1-9][0-9]*$/.test($("#borrowTime").val())){
			 	    $("#show_error").html("请输入正确借款期限");
			 	    $("#showPayTable").html("");
			 	     return;
		 	   }
	 	   }
	 	   
	 	   $("#show_error").html("");
	 	}
	    function rateCalculate(){//利息计算
		    rateNumJudge();
	        
	        var aa = 0;
			var bb = 0;
			var cc = 0;
			if($("#show_error").text()!=""){
			   return;
			}
			if($("#manual").attr("checked")){//天标计算
			    var _array = [];
		        _array.push("<table>");
			    _array.push("<tr><th style='width: 150px;'>期限</th><th style='width: 150px;'>所还本息</th><th style='width: 150px;'>所还本金</th>"
			    +"<th style='width: 150px;'>所还利息</th><th style='width: 150px;'>总还款额</th></tr>");
		        param["paramMap.borrowSum"] = $("#borrowSum").val();
		        param["paramMap.yearRate"] = $("#yearRate").val();
		        param["paramMap.borrowTime"] = $("#borrowTime").val();
		        param["paramMap.repayWay"] = $("#repayWay").get(0).selectedIndex;
		    
		    
				$.shovePost("toolsCalculateDay.do",param,function(data){
				   if(data == 1){
				      $("#show_error").html("请填写正确信息");
				      $("#showPayTable").html("");
				      return;
				   }else if(data == 2){
				      $("#show_error").html("年利率太低，请重新输入");
				      $("#showPayTable").html("");
				      return;
				   }
			         aa = data.map.monForRateA;
			         bb = data.map.monRate;
			         cc = data.map.allPay;
				         
						_array.push("<tr><td align='center'>"+data.map.mon+"天</td><td align='center'>"+data.map.monForRateA+"</td>"
						+"<td align='center'>"+data.map.monForA+"</td>"
						+"<td align='center'>"+data.map.monForRate+"</td><td align='center'>"+cc+"</td></tr>");
					
					_array.push("</table>");
					$("#showPayTable").html(_array.join(""));
					$("#result").show();
					$("#totalResult").hide();
					$("#dd").html(cc);
				});
			
		    }else{
		    
			    var _array = [];
		        _array.push("<table>");
			    _array.push("<tr><th style='width: 150px;'>期数</th><th style='width: 150px;'>月还本息</th><th style='width: 150px;'>月还本金</th>"
			    +"<th style='width: 150px;'>月还利息</th><th style='width: 150px;'>本息余额</th></tr>");
		        param["paramMap.borrowSum"] = $("#borrowSum").val();
		        param["paramMap.yearRate"] = $("#yearRate").val();
		        param["paramMap.borrowTime"] = $("#borrowTime").val();
		        param["paramMap.repayWay"] = $("#repayWay").get(0).selectedIndex;
		    
		    
				$.shovePost("frontfinanceTools.do",param,function(data){
				   if(data == 1){
				      $("#show_error").html("请填写正确信息");
				      $("#showPayTable").html("");
				      return;
				   }else if(data == 2){
				      $("#show_error").html("年利率太低，请重新输入");
				      $("#showPayTable").html("");
				      return;
				   }
				    for(var i = 0; i < data.length; i ++){
				      if(i == 0){
				         aa = data[i].monForRateA;
				         bb = data[i].monRate;
				         cc = data[i].allPay;
				      }
						_array.push("<tr><td align='center'>"+data[i].mon+"</td><td align='center'>"+data[i].monForRateA+"</td>"
						+"<td align='center'>"+data[i].monForA+"</td>"
						+"<td align='center'>"+data[i].monForRate+"</td><td align='center'>"+data[i].rateARemain+"</td></tr>");
					}
					_array.push("</table>");
					$("#showPayTable").html(_array.join(""));
					$("#result").hide();
					$("#totalResult").show();
					$("#aa").html(aa);
					$("#bb").html(bb+"%");
					$("#cc").html(cc);
				});
			}
	 	}
	 	
	 	
	 	function changeStatus(){
		 	if($("#manual").attr("checked")){
		 	   $("#timeType").html("天");
		 	   $("#borrowTime").attr('value','');
		 	   $("#repayWay").attr('disabled','disabled');
		 	}else{
		 	  $("#timeType").html("月");
		 	  $("#repayWay").removeAttr('disabled');
		 	}
	 	}
	 	
	 	/*function rateCalculate(){//利息计算
	 	    param["paramMap.borrowSum"] = $("#borrowSum").val();
	        param["paramMap.yearRate"] = $("#yearRate").val();
	        param["paramMap.borrowTime"] = $("#borrowTime").val();
	        param["paramMap.repayWay"] = $("#repayWay").get(0).selectedIndex;
			$.shovePost("frontfinanceTools.do",param,function(data){
			   //alert(data);
		 	    var array=data.split("|");
		 		$("#aa").html(array[0]);
		 		$("#bb").html(array[1]+"%");
		 		$("#cc").html(array[2]);
			});
	 	}*/
	 	
	 	/*function initCallBack(data){
	 	    alert(data);
	 	    var array=data.split("|");
	 		$("#aa").html(array[0]);
	 		$("#bb").html(array[1]);
	 		$("#cc").html(array[2]);
	 	}*/
	 	
	 	$(function(){
			$('.tabmain').find('li').click(function(){
			$('.tabmain').find('li').removeClass('on');
			$(this).addClass('on');
			$('.lcmain_l').children('div').hide();
		    $('.lcmain_l').children('div').eq($(this).index()).show();
			})
		});
		
		//加载借款协议范本
		/*function queryBrrowClause(){
		    //alert($("#borrowClause").text());
		    param["paramMap.title"] = $("#borrowClause").text();
        	$.shovePost("queryBorrowClause.do",param,function(data){
        	   //alert(data);
        	    var array=data.split("|");
        	    $("#borrowClauseTitle").html(array[0]);
        	    $("#borrowClauseContent").html(array[1]);
        	});
        }*/
	</script>
</div>
</body>
</html>
