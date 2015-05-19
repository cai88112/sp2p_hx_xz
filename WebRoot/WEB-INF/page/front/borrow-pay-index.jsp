<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
</head>
<body>

	<input type="hidden" id="bId" value="${payMap.borrowId}" />
	<input type="hidden" id="needSum" value="${payMap.needSum}" />
	<input type="hidden" id="payId" value="${payMap.id}" />
	<div class="nymain" style="width: 400px; margin-top: -10px;">
		<div class="wdzh" style="border: none;">
			<div class="r_main" style="border: none;">
				<div class="box" style="border: none;">
					<div class="boxmain2" style="border: none;">
						<div class="biaoge2" style="border: none;">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="11%">账户余额：</td>
									<td width="89%"><strong>${payMap.totalSum}元 </strong></td>
								</tr>
								<tr>
									<td>可用余额：</td>
									<td><strong>${payMap.usableSum }元 </strong></td>
								</tr>
								<tr>
									<td>还款日期：</td>
									<td><strong> ${payMap.repayDate } </strong></td>
								</tr>
								<tr>
									<td>待还本息：</td>
									<td><strong>${payMap.forPI }元</strong></td>
								</tr>
								<tr>
									<td>逾期本息：</td>
									<td><strong>${payMap.lateFI }元</strong></td>
								</tr>
								<tr>
									<td>需还总额：</td>
									<td><strong>${payMap.needSum }元</strong></td>
								</tr>

								<tr>
									<td>验证码：</td>
									<td><input type="text" class="inp100x"
										name="paramMap.code" id="code" /> <img
										src="admin/imageCode.do?pageId=vip" title="点击更换验证码"
										style="cursor: pointer;" id="codeNum" width="46" height="18"
										onclick="javascript:switchCode()" /></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td style="padding-top: 20px;"><a style="cursor: pointer;"
										id="btnsave" class="bcbtn">确认还款</a></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="jumptoqianduoduo" style="display: none;"></div>
	<script type="text/javascript" src="script/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
	<script>
		var flag = true;
		$(function() {
			$('#btnsave').click(
					function() {
						var id = $("#payId").val();
						var bId = $("#bId").val();
						var needSum = $('#needSum').val();
						param["paramMap.code"] = $("#code").val();
						if (flag) {
							$("#btnsave").attr("disabled", true);
							$('#btnsave').attr("value", "受理中...");
							$.shovePost('checkrRepayCode.do', param, function(
									data) {
								var callBack = data.msg;
								if (callBack == '') {
									flag = false;
									/* window.location.href = "toRepayment.do?id="
											+ id + "&bId=" + bId + "&needSum="
											+ needSum; */

									var url = 'toRepayment.do?id=' + id
											+ "&bId=" + bId + "&needSum="
											+ needSum;
									//alert("调用第三方" + url);
									$.shovePost(url, "", function(
											data1) {
										//alert(data1);
										$("#jumptoqianduoduo").html(data1);
									});

									return false;
								}
								alert(callBack);
								switchCode();
								$("#btnsave").attr("disabled", false);
								$('#btnsave').attr("value", "确认还款");
								flag = true;
							});
						}
					});
			switchCode();
		});
		function switchCode() {
			var timenow = new Date();
			$('#codeNum').attr('src',
					'admin/imageCode.do?pageId=invest&d=' + timenow);
		};
	</script>
</body>
</html>
