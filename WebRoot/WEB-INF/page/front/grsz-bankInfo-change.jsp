<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<div class="nymain" style="width: 600px;">
		<div class="wdzh" style="width: 600px;">
			<div class="r_main" style="width: 600px;">
				<div class="box" style="width: 600px;">
					<div class="boxmain2" style="padding-top: 10px; width: 600px;">
						<div class="biaoge2" style="margin-top: 0px; width: 600px;">
							<table width="600px;" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<th colspan="2" align="left" style="padding-top: 0px;">
										变更银行卡信息</th>
								</tr>
								<tr>
									<td width="18%" align="right">真实姓名：</td>
									<td width="83%">${realName }<span class="txt"></span></td>
								</tr>
								<tr>
									<td width="18%" align="right">原银行卡号：</td>
									<td width="83%">${bankCard }<span class="txt"></span></td>
								</tr>
								<tr>
									<td align="right">新的开户行：</td>
									<td><!-- <input type="text" class="inp188" id="bankName_" />  -->
										<s:select list="#request.qianduoduobankList" id="bankName1"
											name="paramMap.bankcode" cssClass="inp188" listKey="bankcode"
											listValue="bankname" headerKey="" headerValue="--请选择--"></s:select>
									<span
										class="txt">输入您的开户银行名称</span></td>
								</tr>
								<tr>
									<td align="right">新的支行：</td>
									<td><input type="text" class="inp188" id="subBankName_" />
										<span class="txt">输入您的开户支行</span></td>
								</tr>
								<tr>
									<td align="right">新的卡号：</td>
									<td><input type="text" class="inp188" id="bankCard_" /> <span
										class="txt">输入您的卡号</span><br /></td>
								</tr>
								<tr>
									<td align="right"><span class="fred">*</span>银行卡类型：<br /></td>
                                     <td><input type="radio" name = "cardType" checked="checked" value ="0" />借记卡
                                     <input type="radio" name="cardType"  value ="1" />信用卡
                                     <br /></td>
								</tr>
								<tr>
									<td align="right">开卡行地区：<span class="fred">*</span></td>
									<td><span><s:select
												list="#request.qianduoduoareaList" id="province"
												name="paramMap.province" cssClass="sel_140" listKey="code"
												listValue="name" headerKey="" headerValue="--请选择--"
												onchange="selectAreaOnchange(this)"></s:select>省
									</span> <span id = "city"> 
												<select class="sel_140" id ="citySel" >
												<option >--请选择--</option>
												</select>市
									</span>
									</td>
								</tr>
								
								
								<tr>
									<td align="right">&nbsp;</td>
									<td style="padding-top: 10px;"><a
										href="javascript:changeBankInfo();" class="bcbtn">变更</a></td>
								</tr>
								<tr>
									<td colspan="2"><span style="color: red; float: none;"
										id="bk1_tip_" class="formtips"></span></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript" src="script/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
	<script>
		$(function() {

			//$("#bankName_").focus();
			$("#bankName1").blur(function() {
				if ($("#bankName1").find("option:selected").text() == "--请选择--") {
					$("#bk1_tip_").html("请选择开户银行");
				} else {
					$("#bk1_tip_").html("");
				}
			});
			
			$("#province").blur(function() {
				if ($("#province").find("option:selected").text() == "--请选择--") {
					$("#bk1_tip_").html("请选择开户省份");
				} else {
					$("#bk1_tip_").html("");
				}
			});
			
			$("#city").blur(function() {
				if ($("#city").find("option:selected").text() == "--请选择--") {
					$("#bk1_tip_").html("请选择开户城市");
				} else {
					$("#bk1_tip_").html("");
				}
			});
			
			
			
			$("#subBankName_").blur(function() {
				if ($("#subBankName_").val() == "") {
					$("#bk1_tip_").html("开户支行不能为空");
				} else {
					$("#bk1_tip_").html("");
				}
			});

			$("#bankCard_").blur(function() {
				if ($("#bankCard_").val() == "") {
					$("#bk1_tip_").html("卡号不能为空");
				} else if (isNaN($("#bankCard_").val())) {
					$("#bk1_tip_").html("请输入正确的银行卡号");
				} else {
					$("#bk1_tip_").html("");
				}
			});
		});

		function changeBankInfo() {
			if ($("#bankName_").val() == "" || $("#subBankName_").val() == ""
					|| $("#bankCard_").val() == "") {
				$("#bk1_tip_").html("请填写完整信息");
				return;
			}

			if ($("#bk1_tip_").text() != "") {
				return;
			}
			if ($("#province").find("option:selected").val() == "" ||$("#city").find("option:selected").val() == "" ) {
				   $("#bk1_tip_").html("请选择省份或者城市");
				   return;
			    }
			
			if (!window.confirm("确定要变更吗?")) {
				return;
			}
			param["paramMap.bankId"] = ${bankId};
			param["paramMap.bankName"] = $("#bankName1").find("option:selected").text();
			param["paramMap.bankCode"] = $("#bankName1").find("option:selected").val();
			param["paramMap.mSubBankName"] = $("#subBankName_").val();
			param["paramMap.mBankCard"] = $("#bankCard_").val();
			
			param["paramMap.province"] = $("#province").find("option:selected").text();
			param["paramMap.provinceCode"] = $("#province").find("option:selected").val();
			param["paramMap.city"] = $("#city").find("option:selected").text();
			param["paramMap.cityCode"] = $("#city").find("option:selected").val();
			param["paramMap.cardType"] = $("input[name='cardType']:checked").val();
			$.shovePost("updateBankInfo.do", param, function(data) {
				if (data == 1) {
					window.parent.tipjBox("变更失败，请重新变更");
					return;
				}else if(data == 2){
					window.parent.tipjBox("请输入有效的银行卡信息");
					return;
				}
				alert("变更成功");
				window.parent.history.go(0);
				window.parent.window.jBox.close();
			});
		}
	function selectAreaOnchange(data){
			
			if(data.value == ""){
				$("#citySel").empty();
				var opt = document.createElement ("option"); 
			    opt.value = ""; 
			    opt.innerText = "--请选择--"; 
			    $("#citySel").append(opt);
				return;
			}
			var parm = {};
			parm["paramMap.parentid"] = data.value;
			$.shovePost("queryarealist.do", parm, function(data) {
				$("#citySel").empty();
				var opt = document.createElement ("option"); 
			    opt.value = ""; 
			    opt.innerText = "--请选择--"; 
			    $("#citySel").append(opt);
				var len = data.length ;
				for (var i = 0;i<len;i++){
					 var opt = document.createElement ("option"); 
					    opt.value = data[i].code; 
					    opt.innerText = data[i].name; 
					    $("#citySel").append(opt);
				}
				
			});
		}
	</script>
</body>
</html>
