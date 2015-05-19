<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
<link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />

</head>
<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div>
	<img src="images/investList-banner.png" width="100%" alt="" />
	</div>
	<div class="nymain">
		<div class="lcnav">
			<div class="tab">
				<div class="srh">
					标题： <input type="text" id="titles" class="inp200" maxlength="200" /><a
						href="javascript:void(0);" id="searchLink" class="chaxun"
						style="margin-left: 6px;">查询</a>
				</div>
				<div class="tabmain">
					<ul>
						<li id="tab_1">全部借款列表</li>
						<li id="tab_2">实地认证</li>
						<li id="tab_3">信用认证</li>
						<li id="tab_4">机构担保</li>
						<li id="tab_5">最近成功的借款</li>
					</ul>
				</div>
			</div>
			<div class="line"></div>
		</div>
		<div class="lcmain">
			<div class="lcmain_l" style="padding: 0">
				<div class="lctab" id="content">
					<ul id="lastestborrow">
						<s:if test="pageBean.page!=null || pageBean.page.size>0">
							<s:iterator value="pageBean.page" var="finance">
								<li><s:if test="%{#finance.borrowStatus == 1}">
										<div class="bottm2">初审中</div>
									</s:if> <s:elseif test="%{#finance.borrowStatus == 2}">
										<div class="bottm">
											<s:if test="%{#finance.borrowShow == 2}">
												<a href="javascript:void(0);"
													onclick="checkTou(${finance.id},2)">立即认购</a>
											</s:if>
											<s:else>
												<a href="javascript:void(0);"
													onclick="checkTou(${finance.id},1)">立即投标</a>
											</s:else>
										</div>
									</s:elseif> <s:elseif test="%{#finance.borrowStatus == 3}">
										<div class="bottm2">复审中</div>&nbsp;
		        </s:elseif> <s:elseif test="%{#finance.borrowStatus == 4}">
										<div class="bottm2">
											<s:if test="%{#finance.borrowShow == 2}">回购中</s:if>
											<s:else>还款中</s:else>
										</div>&nbsp;
		        </s:elseif> <s:elseif test="%{#finance.borrowStatus == 5}">
										<div class="bottm2">已还完</div>&nbsp;
		        </s:elseif> <s:else>
										<div class="bottm2">流标</div>&nbsp;
		        </s:else>
									<div class="list_tx">
										<a href="financeDetail.do?id=${finance.id}" target="_blank">
											<shove:img src="${finance.imgPath}"
												defaulImg="images/hslogo_42.jpg" width="80" height="79"></shove:img>
										</a>
									</div>

									<div class="list_txt">
										<table width="560" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<th colspan="3" align="left"><a
													href="financeDetail.do?id=${finance.id}" target="_blank">
														<shove:sub size="15" value="#finance.borrowTitle"
															suffix="..." />
												</a> &nbsp; <s:if test="#finance.borrowWay ==1">
														<img src="images/neiye1_53.jpg" width="15" height="16"
															title="净值借款" />
													</s:if> <!-- 
                      <s:if test="#finance.borrowWay ==2">
                    	  <img src="images/neiye1_55.jpg" width="15" height="16" title="秒还借款"  />
                      </s:if>  --> <s:if test="#finance.borrowWay ==4">
														<img src="images/tubiao2.png" title="实地考察借款" />
													</s:if> <s:if test="#finance.borrowWay ==5">
														<img src="images/tubiao1.png" title="机构担保借款" />
													</s:if> &nbsp; <s:if test="#finance.hasPWD ==1">
														<img src="images/lock.png" width="15" height="16"
															title="投标时需要填写密码" />
													</s:if> <s:if test="#finance.isDayThe ==2">
														<img src="images/neiye1_67.jpg" width="15" height="16"
															title="天标" />
													</s:if> <s:if test="#finance.auditStatus ==3">
														<img src="images/neiye1_62.jpg" width="15" height="16"
															title="该用户通过抵押认证" />
													</s:if> <s:if test="#finance.excitationType==2">
														<span class="list_txtjl"><span>￥${finance.excitationSum }</span></span>
													</s:if> <s:if test="#finance.excitationType==3">
														<span class="list_txtjl"><span>${finance.excitationSum }%</span></span>
													</s:if></th>
											</tr>
											<tr>
												<td width="151">借款金额：<span>￥ <s:property
															value="#finance.borrowAmount" default="0" />
												</span></td>
												<td width="213">年利率：<span>￥ <s:property
															value="#finance.annualRate" default="0" /> %
												</span></td>
												<td width="196">借款期限：<span> <s:property
															value="#finance.deadline" default="0" /> <s:if
															test="%{#finance.isDayThe ==1}">个月</s:if> <s:else>天</s:else>
												</span></td>
											</tr>
											<tr>
												<td>信用等级：<img
													src="images/ico_<s:property value="#finance.credit" default="---"/>.jpg"
													title="<s:property value="#finance.creditrating" default="0"/>分" /></td>
												<td><div>
														<div style="float: left;">借款进度：</div>
														<div class="progeos">
															<div
																style="width: <s:property value="#finance.schedules" default="0"/>%"></div>
														</div>
														<div style="float: left;">
															<span> <s:property value="#finance.schedules"
																	default="0" /> %
															</span>
														</div>
													</div></td>
												<td>还需：<span> <s:property
															value="#finance.investNum" default="---" />
												</span></td>
											</tr>
										</table>
									</div>
									<div style="clear: both"></div></li>
							</s:iterator>
						</s:if>
					</ul>
					<s:else>
						<li
							style="text-align: center; padding-top: 20px; padding-bottom: 20px;">没有数据</li>
					</s:else>
					<s:if test="pageBean.page!=null || pageBean.page.size>0">
						<div class="page">
							<p>
								<shove:page url="finance.do" curPage="${pageBean.pageNum}"
									showPageCount="7" pageSize="${pageBean.pageSize }"
									theme="number" totalCount="${pageBean.totalNum}">
									<s:param name="m">${paramMap.m}</s:param>
									<s:param name="title">${paramMap.title}</s:param>
									<s:param name="paymentMode">${paramMap.paymentMode}</s:param>
									<s:param name="purpose">${paramMap.purpose}</s:param>
									<s:param name="raiseTerm">${paramMap.raiseTerm}</s:param>
									<s:param name="reward">${paramMap.reward}</s:param>
									<s:param name="arStart">${paramMap.arStart}</s:param>
									<s:param name="arEnd">${paramMap.arEnd}</s:param>
									<s:param name="type">${paramMap.type}</s:param>
								</shove:page>
							</p>
						</div>
					</s:if>
				</div>
			</div>
			<div class="lcmain_r">
				<div class="lbox">
					<h2>借款标志说明</h2>
					<div class="lboxmain"
						style="background-image: url(images/neiye1_50.jpg); background-position: 136px 35px; background-repeat: no-repeat;">
						<ul>
							<li>信用借款：无标志</li>
							<li></li>
							<li>净值借款：<img src="images/neiye1_53.jpg" width="15"
								height="16" /></li>
							<!--  <li>秒还借款：<img src="images/neiye1_55.jpg" width="15" height="16" /></li>  -->
							<li>担保借款：<img src="images/tubiao1.png" alt="" /></li>
							<li>通过抵押认证：<img src="images/neiye1_62.jpg" width="15"
								height="16" /></li>
							<li>实地考察：<img src="images/tubiao2.png" /></li>
							<li>按天借款：<img src="images/neiye1_67.jpg" width="15"
								height="16" /></li>
						</ul>
					</div>
				</div>
				<div class="lbox">
					<h2>按条件搜索</h2>
					<div class="lboxmain">
						<form id="searchForm" action="finance.do">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td align="right">还款方式：</td>
									<td><input id="pageNum" name="curPage" type="hidden" /> <input
										name="m" type="hidden" value="${paramMap.m }" /> <input
										name="title" type="hidden" value="${ paramMap.title}" /> <s:select
											id="paymentMode" name="paymentMode"
											list="#{'':'没有限制',1:'按月分期还款',2:'按月付息,到期还本',4:'一次性还款'}"
											value="paramMap.paymentMode" cssClass="sel_140"></s:select>
									</td>
								</tr>
								<tr>
									<td align="right">借款目的：</td>
									<td><s:select name="purpose" value="paramMap.purpose"
											list="borrowPurposeList" listKey="selectValue"
											listValue="selectName" headerKey="" headerValue="没有限制"
											cssClass="sel_140"></s:select></td>
								</tr>
								<tr>
									<td align="right">期限：</td>
									<td><s:select name="deadline" value="paramMap.deadline"
											list="borrowDeadlineList" listKey="selectValue"
											listValue="selectName" headerKey="" headerValue="没有限制"
											cssClass="sel_140"></s:select></td>
								</tr>
								<tr>
									<td align="right">奖励：</td>
									<td><s:select id="reward" name="reward"
											list="#{'':'没有限制',2:'只显示有奖励的',1:'只显示无奖励的'}"
											value="paramMap.reward" cssClass="sel_140"></s:select></td>
								</tr>
								<tr>
									<td align="right">金额范围：</td>
									<td><s:select name="arStart" value="paramMap.arStart"
											list="borrowAmountList" listKey="selectValue"
											listValue="selectName" headerKey="" headerValue="没有限制"
											cssClass="sel_70"></s:select> 到 <s:select name="arEnd"
											value="paramMap.arEnd" list="borrowAmountList"
											listKey="selectValue" listValue="selectName" headerKey=""
											headerValue="没有限制" cssClass="sel_70"></s:select></td>
								</tr>
								<tr>
									<td align="right">只显示：</td>
									<td><input type="checkbox" name="ck_mode" id="ck_mode_1"
										value="1" /> <img src="images/neiye1_53.jpg" width="15"
										height="16" /> <!-- <input type="checkbox" name="ck_mode" id="ck_mode_2" value="2"/>
      <img src="images/neiye1_55.jpg" width="15" height="16" />   --> <input
										type="checkbox" name="ck_mode" id="ck_mode_4" value="4" /> <img
										src="images/tubiao2.png" /> <input type="checkbox"
										name="ck_mode" id="ck_mode_3" value="3" /> <img
										src="images/neiye1_62.jpg" /> <input type="checkbox"
										name="ck_mode" id="ck_mode_5" value="5" /> <img
										src="images/tubiao1.png" width="15" height="16" /> <input
										id="type" name="type" type="hidden" /></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td><a href="javascript:void(0);" id="search"
										class="chaxun">确认</a></td>
								</tr>
							</table>
						</form>
					</div>
				</div>
				<div class="lbox" style="padding-bottom: 88px;">
					<h2>收益计算器</h2>
					<div class="lboxmain">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td align="right">投资金额：</td>
								<td><input type="text" class="inp140" id="borrowSum" /></td>
							</tr>
							<tr>
								<td align="right">年利率：</td>
								<td><input type="text" class="inp140" id="yearRate" />%</td>
							</tr>
							<tr>
								<td align="right">投资期限：</td>
								<td><input type="text" class="inp140" id="borrowTime" />月</td>
							</tr>
							<tr>
								<td align="right">还款方式：</td>
								<td><select name="select3" class="sel_140" id="repayWay">
										<option selected="selected">按月还款</option>
										<option>先息后本</option>
										<option>一次性还款</option>
								</select></td>
							</tr>
							<tr>
								<td align="right">投标奖励：</td>
								<td><input type="text"
									style="text-align: center; height: 20px; width: 50px; border: 1px solid #ccc; line-height: 20px;"
									id="bidReward" value="0" />%&nbsp; 加现金<input type="text"
									style="text-align: center; height: 20px; width: 50px; border: 1px solid #ccc; line-height: 20px;"
									id="bidRewardMoney" value="0" />元</td>
							</tr>
							<tr>
								<td align="right">&nbsp;</td>
								<td><a href="javascript:void(0);" class="chaxun"
									onclick="javascript:rateCalculate()">计算</a></td>
							</tr>
							<tr>
								<td colspan="2"><strong><span
										style="color: red; float: none;" class="formtips"
										id="show_error"></span></strong></td>
							</tr>
						</table>
						<span id="showIncome"></span>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript">
$(document).ready(function(){
    var ck_type = "${paramMap.type}";
    var no = ck_type.split(',');
    if(no != ''){
       for(var i=0;i<no.length;i++){
          var x = no[i];
          $('#ck_mode_'+x).attr('checked','checked');
       }
    }	
});

function  checkTou(id,type){
	 var param = {};
	 param["id"] = id;
     $.shovePost('financeInvestInit.do',param,function(data){
	   var callBack = data.msg;
	   if(callBack !=undefined){
	     alert(callBack);
	   }else{
		   if(type==2){
				var url = "subscribeinit.do?borrowid="+id;
		     	 $.jBox("iframe:"+url, {
			    		title: "我要购买",
			    		width: 450,
			    		height: 450,
			    		buttons: {  }
			    		});
			}else{
				 window.location.href= 'financeInvestInit.do?id='+id;
		   }
	   }
	 });
}
function closeMthod(){
	window.jBox.close();
	window.location.reload();
}

function clearComponentVal(){
   param = {};
   $('#titles').val('');
   $('#paymentMode').get(0).selectedIndex=0;
   $('#purpose').get(0).selectedIndex=0;
   $('#deadline').get(0).selectedIndex=0;
   $('#reward').get(0).selectedIndex=0;
   $('#arStart').get(0).selectedIndex=0;
}

		function rateNumJudge(){//验证利息计算输入数字是否正确
	 	   if($("#borrowSum").val()==""){
	 	      $("#show_error").html("&nbsp;&nbsp;投资金额不能为空");
	 	      $("#showIncome").html("");
	 	   }else 
	 	   if(!/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($("#borrowSum").val())){
	 	       $("#show_error").html("&nbsp;&nbsp;请输入正确的投资金额进行计算");
	 	       $("#showIncome").html("");
	 	   }else 
	 	   if($("#yearRate").val()==""){
	 	      $("#show_error").html("&nbsp;&nbsp;年利率不能为空");
	 	      $("#showIncome").html("");
	 	   }else 
	 	   if(!/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($("#yearRate").val())){
	 	      $("#show_error").html("&nbsp;&nbsp;请输入正确的年利率");
	 	      $("#showIncome").html("");
	 	   }else 
	 	   if($("#borrowTime").val()==""){
	 	       $("#show_error").html("&nbsp;&nbsp;投资期限不能为空");
	 	       $("#showIncome").html("");
	 	   }else 
	 	   if(!/^[0-9]*[1-9][0-9]*$/.test($("#borrowTime").val())){
		 	    $("#show_error").html("&nbsp;&nbsp;请输入正确投资期限");
		 	    $("#showIncome").html("");
	 	   }else 
	 	    if(!/^[0-9].*$/.test($("#bidReward").val())){
		 	      $("#show_error").html("&nbsp;&nbsp;请输入正确的投资奖励");
		 	      $("#showIncome").html("");
	 	   }else 
		 	if(!/^[0-9].*$/.test($("#bidRewardMoney").val())){
			 	      $("#show_error").html("&nbsp;&nbsp;请输入正确的现金奖励 ");
			 	      $("#showIncome").html("");
	 	    }else{
	 	      $("#show_error").html("");
	 	   }
	 	}
	 	
	 	function rateCalculate(){//利息计算
	 	    var str = rateNumJudge();
	 	    param["paramMap.borrowSum"] = $("#borrowSum").val();
	        param["paramMap.yearRate"] = $("#yearRate").val();
	        param["paramMap.borrowTime"] = $("#borrowTime").val();
	        param["paramMap.repayWay"] = $("#repayWay").get(0).selectedIndex;
	        param["paramMap.bidReward"] = $("#bidReward").val();
	        param["paramMap.bidRewardMoney"] = $("#bidRewardMoney").val();
	        
	        var _array = [];
	        
			if($("#show_error").text()!=""){
			   return;
			}
			$.shovePost("incomeCalculate.do",param,function(data){
			   //只有一条记录
			   if(data == 1){
			     $("#show_error").html("请填写完整信息进行计算");
			     return;
			   }
			   _array.push("<table>");
			    for(var i = 0; i < data.length; i ++){
			    	data[i].income2year = data[i].income2year < 1 ? "0" + data[i].income2year : data[i].income2year;
			    	data[i].rateSum = data[i].rateSum < 1 ? "0" + data[i].rateSum : data[i].rateSum;
					_array.push("<tr><td style='padding-left:20px'><span>投标奖励：</span><span>"+data[i].reward+"元</span><br/>"
					+"<span>年化收益：</span><span>"+data[i].income2year+"%</span><br/>"
					+"<span>总计利息：</span><span>"+data[i].rateSum+"元</span><br/>"
					+"<span>每月还款：</span><span>"+data[i].monPay+"元</span><br/>"
					+"<span>总共收益：</span><span>"+data[i].allSum+"元</span><br/>"
					+"<span>总计收益：</span><span>"+data[i].netIncome+"元(扣除10%管理费)</span></td></tr>");
					/*_array.push("<p>投标奖励："+data[i].reward+"元</p><br /><br />"
					+"<p>年化收益："+data[i].income2year+"元</p><br /><br />"
					+"<p>总收益："+data[i].allSum+"元</p><br /><br />"
					+"<p>每月还款："+data[i].monPay+"元</p><br /><br />"
					+"<p>总计收益(扣除10%管理费)："+data[i].netIncome+"元</p>");*/
				}
				//_array.push("</table>");
				$("#showIncome").html(_array.join(""));
			});
	 	}
</script>
	<script type="text/javascript">
		$(function(){
			dqzt(1);
			$("span#tit").each(function(){
				if($(this).text().length > 6){
					$(this).text($(this).text().substring(0,8)+"..");
				}
			});
	
			var m = '${paramMap.m}';
			if(m == ''){
				m = 1;
			}
			$("#tab_"+m).addClass("on");
			$("#tab_1").click(function(){
			    window.location.href = "finance.do?m=1"
			});
			$("#tab_2").click(function(){
			   window.location.href = "finance.do?m=2"
			});
			$("#tab_3").click(function(){
               window.location.href = "finance.do?m=3"
			});
			$("#tab_4").click(function(){
			   window.location.href = "finance.do?m=4"
			});
			$("#tab_5").click(function(){
			   window.location.href = "finance.do?m=5"
			});
			$("#searchLink").click(function(){
			  	window.location.href = "finance.do?title="+$("#titles").val();
			});
			$("#search").click(function(){
				var chk_value = [];
				$('input[name="ck_mode"]:checked').each(function(){  
		        	chk_value.push($(this).val());  
		        });
		        if(chk_value.length != 0){
		            param['paramMap.m'] = '1';
		            $("#type").val(chk_value);                  
		        }else{
		            $("#type").val("");
		        }
                $("#searchForm").submit();
			});
			$("#arEnd").change(function(){
			    var arStart = $('#arStart').val();
			    arStart = parseInt(arStart);
			    var arEnd = $(this).val();
			    arEnd = parseInt(arEnd);
			    if(arEnd < arStart){
			      alert('金额范围不能小于开始!');
			    }
			});
			
			$("#jumpPage").click(function(){
			     var curPage = $("#curPageText").val();
			    
				 if(isNaN(curPage)){
					alert("输入格式不正确!");
					return;
				 }
				 $("#pageNum").val(curPage);
   				var chk_value = [];
				$('input[name="ck_mode"]:checked').each(function(){  
		             chk_value.push($(this).val());  
		        });
		        if(chk_value.length != 0){
		             param['paramMap.m'] = '1';
		             $("#type").val(chk_value);                  
		        }else{
		            $("#type").val("");
		        }
                $("#searchForm").submit();
			});
		});
</script>
<script>
//($('#search a').attr("href","javascript:"));
($('#search a').attr("onclick","searchBorrow(this);"));
function searchBorrow(data){
	
	//alert(data.innerHTML);
}

</script>
</body>
</html>
