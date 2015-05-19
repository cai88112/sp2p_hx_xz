<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
<link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />
<style>
#search span {
	font-size: 14px;
	margin-left: 30px;
	/* cursor:pointer; */
	padding: 5px;
}

#search span a:hover {
	background-color: #929a9f;
	padding: 5px 0px;
	color: white;
}

#search span strong {
	background-color: #929a9f;
	padding: 5px 3px;
	color: white;
}

#search span a {
	cursor: pointer;
}

.searchSpan {
	margin-left: 15px;
	line-height: 45px;
	font-size: 14px;
}

.pageDivClass a:hover {
	background-color: #3399ff;
	border: #3399ff;
}

.curPageColor {
	background-color: #3399ff;
	border: #3399ff;
}
</style>

</head>
<body style="background-color: #f7f8f8">
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div
		style="position: relative; width: 100%; height: 300px; background-color: #7ecef4; text-align: center">
		<img src="images/BannerFlow.png" style="margin: 40px auto" />
	</div>
	<div class="nymain">
		<!-- 筛选条件 -->
		<div>
			<div id="search"
				style="background: #fff; border-radius: 5px; box-shadow: 5px 5px 10px #e6e6e6; height: 300px">
				<div style="height: 100px">
					<img src="images/select.png" style="margin: 20px"></img>
				</div>

				<img src="images/line4.png" style="position: relative; top: -12px"></img>

				<div id="catalog" style="height: 40px">
					<span class="searchSpan">种类：</span> 
					<s:if
						test="#request.subjectMatterString==null || #request.subjectMatterString=='' || #request.subjectMatterString==0">
						<span><strong>不限</strong></span>
					</s:if>
					<s:else>
						<span><a id="0">不限</a></span>
					</s:else>
					<s:if test="#request.subjectMatterString==1">
						<span><strong>富车贷</strong></span>
					</s:if>
					<s:else>
						<span><a id="1">富车贷</a></span>
					</s:else>

					<s:if test="#request.subjectMatterString==2">
						<span><strong>富房贷</strong></span>
					</s:if>
					<s:else>
						<span><a id="2">富房贷</a></span>
					</s:else>
					<s:if test="#request.subjectMatterString==3">
						<span><strong>富余宝</strong></span>
					</s:if>
					<s:else>
						<span><a id="3">富余宝</a></span>
					</s:else>
				</div>

				<div id="borrowAmount" style="height: 40px">
					<span class="searchSpan">金额：</span>
					<s:if
						test="#request.borrowAmountString==null || #request.borrowAmountString=='' || #request.borrowAmountString==0">
						<span><strong>不限</strong></span>
					</s:if>
					<s:else>
						<span><a id="0">不限</a></span>
					</s:else>
					<s:if test="#request.borrowAmountString==1">
						<span><strong>5000-10000</strong></span>
					</s:if>
					<s:else>
						<span><a id="1">5000-10000</a></span>
					</s:else>

					<s:if test="#request.borrowAmountString==2">
						<span><strong>1万-3万</strong></span>
					</s:if>
					<s:else>
						<span><a id="2">1万-3万</a></span>
					</s:else>

					<s:if test="#request.borrowAmountString==3">
						<span><strong>3万-5万</strong></span>
					</s:if>
					<s:else>
						<span><a id="3">3万-5万</a></span>
					</s:else>
					<s:if test="#request.borrowAmountString==4">
						<span><strong>5万以上</strong></span>
					</s:if>
					<s:else>
						<span><a id="4">5万以上</a></span>
					</s:else>
				</div>
				<div id="yearRate" style="height: 40px">
					<span class="searchSpan">利率：</span>
					<s:if
						test="#request.yearRateString==null || #request.yearRateString=='' || #request.yearRateString==0">
						<span><strong>不限</strong></span>
					</s:if>
					<s:else>
						<span><a id="0">不限</a></span>
					</s:else>

					<s:if test="#request.yearRateString==1">
						<span><strong>0%-5%</strong></span>
					</s:if>
					<s:else>
						<span><a id="1">0%-5%</a></span>
					</s:else>
					<s:if test="#request.yearRateString==2">
						<span><strong>5%-10%</strong></span>
					</s:if>
					<s:else>
						<span><a id="2">5%-10%</a></span>
					</s:else>
					<s:if test="#request.yearRateString==3">
						<span><strong>10%-15%</strong></span>
					</s:if>
					<s:else>
						<span><a id="3">10%-15%</a></span>
					</s:else>
					<s:if test="#request.yearRateString==4">
						<span><strong>15%-20%</strong></span>
					</s:if>
					<s:else>
						<span><a id="4">15%-20%</a></span>
					</s:else>
					<s:if test="#request.yearRateString==5">
						<span><strong>20%-24%</strong></span>
					</s:if>
					<s:else>
						<span><a id="5">20%-24%</a></span>
					</s:else>
					<s:if test="#request.yearRateString==6">
						<span><strong>24%以上</strong></span>
					</s:if>
					<s:else>
						<span><a id="6">24%以上</a></span>
					</s:else>
				</div>
				<div id="deadline" style="height: 40px">
					<span class="searchSpan">期限：</span>
					<s:if
						test="#request.deadlineString==null || #request.deadlineString=='' || #request.deadlineString==0">
						<span><strong>不限</strong></span>
					</s:if>
					<s:else>
						<span><a id="0">不限</a></span>
					</s:else>

					<s:if test="#request.deadlineString==1">
						<span><strong>1-3月</strong></span>
					</s:if>
					<s:else>
						<span><a id="1">1-3月</a></span>
					</s:else>

					<s:if test="#request.deadlineString==2">
						<span><strong>3-6月</strong></span>
					</s:if>
					<s:else>
						<span><a id="2">3-6月</a></span>
					</s:else>

					<s:if test="#request.deadlineString==3">
						<span><strong>6-12月</strong></span>
					</s:if>
					<s:else>
						<span><a id="3">6-12月</a></span>
					</s:else>

					<s:if test="#request.deadlineString==4">
						<span><strong>12月以上</strong></span>
					</s:if>
					<s:else>
						<span><a id="4">12月以上</a></span>
					</s:else>
				</div>

			</div>

		</div>

		<!-- 筛选条件end -->
		<!-- 列表 -->
		<div style="margin-top: 20px;">
			<div
				style="width: 998px; height: 90px; border-radius: 5px; background-color: #fff; border-top-left-radius: 5px; border-top-right-radius: 5px;">
				<div style="position: absolute; margin: 20px">
					<img src="images/AllInvest.png"></img> <img src="images/title.png"></img>
				</div>
			</div>

		</div>
		<!-- 标的信息 -->
		<div class="main"
			style="position: relative; width: 998px; font-size: 16px; margin-bottom: 20px">
			<div style="height: 50px">
				<dl>
					<dd
						style="padding: 15px 0; background-color: #F1F1F1; width: 35%; float: left; text-align: center">借款标题</dd>
					<dd
						style="padding: 15px 0; background-color: #F1F1F1; width: 15%; float: left; text-align: center">金额</dd>
					<dd
						style="padding: 15px 0; background-color: #F1F1F1; width: 15%; float: left; text-align: center">年利率</dd>
					<dd
						style="padding: 15px 0; background-color: #F1F1F1; width: 10%; float: left; text-align: center">期限</dd>
					<dd
						style="padding: 15px 0; background-color: #F1F1F1; width: 13%; float: left; text-align: center">进度</dd>
					<dd
						style="padding: 15px 0; background-color: #F1F1F1; width: 12%; float: left; text-align: center">状态</dd>
				</dl>
			</div>
			<div id="otherBorrow"
				style="background-color: #fff; border-bottom-left-radius: 5px; border-bottom-right-radius: 5px; box-shadow: 5px 5px 10px #e6e6e6">
				<s:if test="pageBean.page!=null || pageBean.page.size>0">
					<dl>
						<s:iterator value="pageBean.page" var="finance">
							<div
								style="height: 55px; border-bottom: solid; border-width: 1px; border-color: #F5F5F5">
								<dd style="padding: 15px 10px; width: 33%; float: left">
									<a href="financeDetail.do?id=${finance.id}" target="_blank">${finance.borrowTitle}</a>
								</dd>
								<dd
									style="padding: 15px 0; width: 15%; float: left; text-align: center;">${finance.borrowAmount}</dd>
								<dd
									style="padding: 15px 0; width: 15%; float: left; text-align: center; color: #ff7400;">${finance.annualRate}%</dd>
								<dd
									style="padding: 15px 0; width: 10%; float: left; text-align: center;">${finance.deadline}<s:if
										test="%{#finance.isDayThe ==1}">个月</s:if>
									<s:else>天</s:else>
								</dd>
								<dd
									style="padding: 5px 0; width: 13%; float: left; text-align: center;">

									<div class="divProcess">
										<canvas class="process" width="40px" height="40px"> <fmt:formatNumber
											type="number" value="${finance.schedules}"
											maxFractionDigits="0" />%</canvas>
									</div>
									<img class="imgCanvas" data-process="${finance.schedules}"
										style="display: none;" src="" alt="" width="40px"
										height="40px" />

								</dd>
								<dd
									style="padding: 15px 0; width: 11%; float: left; text-align: center;">
									<div class="btn5">
										<s:if test="%{#finance.borrowStatus == 1}">
											初审中
										</s:if>
										<s:elseif test="%{#finance.borrowStatus == 2}">
											<s:if test="%{#finance.borrowShow == 1}">
												<div
													style="position: relative; top: -5px; width: 90px; height: 30px; background-color: #e85801; line-height: 28px; border-radius: 5px; margin: 0 16px">
													<a href="javascript:void(0);"
														onclick="checkTou(${finance.id},1)" style="color: white">立即投标</a>
												</div>
											</s:if>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 3}">
									复审中
								</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 4}">
											<s:if test="%{#finance.borrowShow == 2}">回购中</s:if>
											<s:else>
												<div
													style="position: relative; top: -5px; width: 90px; height: 30px; background-color: #c1c6c9; line-height: 28px; border-radius: 5px; margin: 0 16px; color: white">还款中</div>
											</s:else>
										</s:elseif>
										<s:elseif test="%{#finance.borrowStatus == 5}">
									已还完
								</s:elseif>
										<s:else>流标</s:else>
									</div>
								</dd>
							</div>
						</s:iterator>
					</dl>
				</s:if>
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
								<s:param name="borrowAmount">${borrowAmountString}</s:param>
								<s:param name="yearRate">${yearRateString}</s:param>
								<s:param name="deadlineString">${deadlineString}</s:param>
							</shove:page>
						</p>
					</div>
				</s:if>
			</div>
		</div>
		<!-- 列表end -->
		<!-- 	id,borrowShow,purpose,imgPath,borrowWay,investNum,borrowTitle,
		username,vipStatus,credit,creditrating,borrowAmount,annualRate,
		deadline,excitationType,excitationSum,borrowStatus,schedules,vip,
		hasPWD,isDayThe,auditStatus -->

	</div>

	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>

	<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript" src="script/modernizr.custom.js"></script>
</body>


<script type="text/javascript">

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

</script>
<script>
//($('#search a').attr("href","javascript:"));
($('#catalog a').attr("onclick","searchBorrow(this,0);"));
($('#borrowAmount a').attr("onclick","searchBorrow(this,1);"));
($('#yearRate a').attr("onclick","searchBorrow(this,2);"));
($('#deadline a').attr("onclick","searchBorrow(this,3);"));
function searchBorrow(data,type){
	var url ="finance.do?curPage=1";
   if(type == 0){
		
		url = url+"&subjectMatter="+data.id+"&borrowAmount=${borrowAmountString}&yearRate=${yearRateString}&deadlineString=${deadlineString}";
	}else if(type == 1){
		
		url = url+"&subjectMatter=${subjectMatterString}&borrowAmount="+data.id+"&yearRate=${yearRateString}&deadlineString=${deadlineString}";
	}else if(type == 2){
		url = url+"&subjectMatter=${subjectMatterString}&borrowAmount=${borrowAmountString}&yearRate="+data.id+"&deadlineString=${deadlineString}";	
	}else if(type == 3){
		url = url+"&subjectMatter=${subjectMatterString}&borrowAmount=${borrowAmountString}&yearRate=${yearRateString}&deadlineString="+data.id;
	}
	window.location.href = url;
	//alert(data.innerHTML);
}

</script>
<script>
window.onload =drawProcess;
function drawProcess() {
	if(Modernizr.canvas){
	$('canvas.process').each(function() {
		var text = $(this).text();
		var process = text.substring(0, text.length - 1);

		var canvas = this;
		
		var context = canvas.getContext('2d');
		context.clearRect(0, 0, 40, 40);

		context.beginPath();
		context.moveTo(20, 20);
		context.arc(20, 20, 20, 0, Math.PI * 2, false);
		context.closePath();
		context.fillStyle = '#ddd';
		context.fill();
		context.beginPath();
		context.moveTo(20, 20);
		context.arc(20, 20, 20, 0, Math.PI * 2 * process / 100, false);
		context.closePath();
		context.fillStyle = '#753b93';
		context.fill();

		context.beginPath();
		context.moveTo(20, 20);
		context.arc(20, 20, 17, 0, Math.PI * 2, true);
		context.closePath();
		context.fillStyle = 'rgba(255,255,255,1)';
		context.fill();

		context.beginPath();
		context.arc(20, 20, 14.5, 0, Math.PI * 2, true);
		context.closePath();
		context.strokeStyle = '#ddd';
		context.stroke();

		context.font = "bold 6pt Arial";
		context.fillStyle = '#9fa0a0';
		context.textAlign = 'center';
		context.textBaseline = 'middle';
		context.moveTo(20, 20);
		context.fillText(text, 20, 20);

	});
	}else{
		$('div.divProcess').each(function(){
			$(this).attr("style","display:none");
		});	
		
		$('img.imgCanvas').each(function(){
			$(this).attr("style","display:block;margin:0px atuo;margin-left:50px;");
	   var process2 = $(this).attr("data-process");
       if(process2 == 0){
    	   $(this).attr("src","images/process/process-min-0.png"); 
       }else if(process2 <= 10){
    	   $(this).attr("src","images/process/process-min-10.png"); 
       }else if(process2 <= 20){
    	   $(this).attr("src","images/process/process-min-10.png"); 
       }else if(process2 <= 30){
    	   $(this).attr("src","images/process/process-min-20.png"); 
       }else if(process2 <= 40){
    	   $(this).attr("src","images/process/process-min-30.png"); 
       }else if(process2 <= 50){
    	   $(this).attr("src","images/process/process-min-40.png"); 
       }else if(process2 <= 60){
    	   $(this).attr("src","images/process/process-min-50.png"); 
       }else if(process2 <= 70){
    	   $(this).attr("src","images/process/process-min-60.png"); 
       }else if(process2 <= 80){
    	   $(this).attr("src","images/process/process-min-70.png"); 
       }else if(process2 <= 90){
    	   $(this).attr("src","images/process/process-min-80.png"); 
       }else if(process2 < 100){
    	   $(this).attr("src","images/process/process-min-90.png"); 
       }else if(process2 == 100){
    	   $(this).attr("src","images/process/process-min-100.png"); 
       }
    });
	}
}

</script>
</html>
