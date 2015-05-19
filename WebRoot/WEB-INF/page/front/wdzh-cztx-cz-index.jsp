<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
<link id="skin" rel="stylesheet" href="css/jbox/Gray/jbox.css" />
<style>
body {
	font-size: 12px;
	color: #333
}

table {
	line-height: 33px;
	width: 720px;
}

table tr {
	
}

table tr td {
	line-height: 33px;
}

table tr td input {
	height: 33px;
	cursor: pointer;
	margin: 1px;
	float: left
}

table tr td label {
	line-height: 33px;
	height: 33px
}

.border_ {
	border: #eeeeee 1px double;
	width: 720px;
	overflow: hidden;
	margin-left: 0px;
	margin-right: 0px;
}

.border_ table tr td .border_ table tr td strong {
	font-size: 14px;
}
</style>
</head>

<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div class="nymain">
		<div class="wdzh">
			<div class="l_nav">
				<div class="box">
					<!-- 引用我的帐号主页左边栏 -->
					<%@include file="/include/left.jsp"%>
				</div>
			</div>
			<div class="r_main">
				<div class="tabtil">
					<ul>
						<li onclick="jumpUrl('queryFundrecordInit.do');">资金记录</li>
						<li class="on" dataIndext="true"
							onclick="jumpUrl('rechargeInit.do');">充值</li>
						<li onclick="jumpUrl('withdrawLoad.do');">提现</li>
					</ul>
				</div>
				<div class="box">
					<div class="boxmain2">

						<p class="tips">
							温馨提示:凡是在富壹代充值未投标的用户，15天以内提现收取本金0.5%，15天以后提现免费
							富壹代禁止信用卡套现、虚假交易等行为,一经发现将予以处罚,包括但不限于：限制收款、冻结账户、永久停止服务,并有可能影响相关信用记录。
						</p>
						<div class="biaoge2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="11%">真实姓名：</td>
									<td width="89%"><strong>${realName }</strong></td>
								</tr>
								<tr>
									<td>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</td>
									<td><strong>${email }</strong></td>
								</tr>
								<tr>
									<td>充值金额：</td>
									<td><input id="money" type="text" class="inp140" /> <span
										style="color: red; float: none;" id="money_tip"
										class="formtips"></span></td>
								</tr>
								<%--  <tr>
   	<td >支付银行：</td>
   	<td>
   		<s:select list="banksLst" id="bankType" name="bankType" listKey="bankCode" listValue="bankName"   headerKey="" headerValue="--请选择银行--"></s:select>
   	</td>
   </tr> --%>
								<tr>
									<td align="right" height="30"></td>
									<td><input type="button" value="提 交" class="bcbtn"
										onclick="addRechargeInfo();" /></td>
								</tr>
							</table>
							<!-- 
	 <div class="wxtx1">
             	国付宝简介:国付宝信息科技有限公司（以下简称“国付宝”）是商务部中国国际电子商务中心（以下简称“CIECC”）与海航商业控股有限公司（以下简称“海航商业”）合资成立，针对政府及企业的需求和电子商务的发展，精心打造的国有背景的，引入社会诚信体系的独立第三方电子支付平台，也是“金关工程”的重要组成部分。国付宝信息科技有限公司成立于2011年1月25日，由商务部中国国际电子商务中心与海航商业控股有限公司合作成立，主要经营第三方支付业务。公司注册资本14285.72万元，主要经营第三方支付业务，互联网支付及移动电话支付（全国）。
     </div>  -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id = "alert" style="display: none;"></div>
	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="/script/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/nav-zh.js"></script>
	<script language="javascript" type="text/javascript"
		src="My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript" src="css/popom.js"></script>
	<script type="text/javascript" src="script/tab.js"></script>
	<script>
		//样式选中
		$(function() {
			$('#a_2').addClass('hov8');
		})

		function addRechargeInfo() {
			if ($("#money").val() == "") {
				$("#money_tip").html("请输入充值金额");
				return;
			} else if (!/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/
					.test($("#money").val())) {
				$("#money_tip").html("请输入正确的提现金额，必须为大于0的数字");
				return;
			} else if ($("#money").val() < 0.01) {
				$("#money_tip").html("最小金额不能低于0.01");
				return;
			} else {
				$("#money_tip").html("");
			}
			var bankType = "";

			bankType = $("#bankType").val();

			/* 	if(bankType=="" || bankType==undefined){
			  	alert("请选择支付银行");
			  	return;
				}   
			 */
			if (!window.confirm("确定进行帐户充值")) {
				return;
			}

			var money = $("#money").val();
			var type = "";

			//window.open("ipayPayment.do?divType=li_2&money="+money+"&bankCode="+bankType);
			var param = {
				"money" : money
			};
			$.shovePost("ipayPayment.do", param, function(data) {
				$("#alert").html(data);
			})

		}

		function jumpUrl(obj) {
			window.location.href = obj;
		}
	</script>
</body>
</html>
