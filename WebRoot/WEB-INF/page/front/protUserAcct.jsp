<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div class="nymain">
		<div class="bigbox" style="border: none">
			<div class="sqdk" style="background: none;">
				<div class="l-nav">
					<ul>
						<li style="background: none; border-bottom: #ddd 1px dashed;">
							<a href="querBaseData.do?from=${from}"
							style="background-image: none;"><span>step1 </span>基本资料</a>
						</li>
						<li class="on last"
							style="background: none; border-bottom: #ddd 1px dashed;"><a
							href="portUserAcct.do" style="background-image: none;"><span>step2
							</span>绑定账号</a></li>
						<li style="background: none; border-bottom: #ddd 1px dashed;">
							<a href="userPassData.do?from=${from}"
							style="background-image: none;"><span>step3 </span>上传资料</a>
						</li>
						<!-- <li style="background: none;border-bottom: #ddd 1px dashed;">
								<a href="creditingInit.do" style="background-image: none;"><span>step4 </span>申请额度</a>
							</li> -->
						<li style="background: none; border-bottom: #ddd 1px dashed;">
							<a href="addBorrowInit.do?t=${session.t}"
							style="background-image: none;"><span>step4 </span>发布贷款</a>
						</li>
					</ul>
				</div>
				<div class="r-main">
					<div class="rmainbox">
						<p class="edtxt">绑定账号</p>
						<form id="createIpsAcct" method="get">
							<div class="tab">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">

									<tr>
										<td height="30" align="right">用户名：</td>
										<td><strong>${user.userName}</strong></td>
									</tr>
									<tr>
										<td height="30" align="right">身份证号码：</td>
										<td><input type="text" id="cadkey"
											value="${personMap.idNo }" name="cadkey" readonly="readonly" />
										</td>
									</tr>
									<tr>
										<td height="30" align="right">真实姓名：</td>
										<td><input type="text" id="realName"
											value="${personMap.realName }" name="realName"
											readonly="readonly" /></td>
									</tr>
									<tr>
										<td height="30" align="right">手机号码：</td>
										<td><input type="text" id="mobileNo"
											value="${personMap.cellPhone }" name="mobileNo" /></td>
									</tr>
									<tr>
										<td height="30" align="right">激活邮件：</td>
										<td><input type="text" id="activeEmail"
											value="${user.email }" name="activeEmail" /></td>
									</tr>
									<tr>
										<td></td>
										<td style="padding-top: 20px;">
											<!-- 	<input id = "btnSubmit" type="submit" value="确定" class="bcbtn"/> -->
											<s:if test="#request.authStep > 4">
												<span style="color: red;">已经绑定第三方账号</span>
											</s:if> <s:else>
												<input id="btnSubmit" type="button" value="确定" class="bcbtn" />
											</s:else> <!--
    <a href="javascript:void(0);" id="bcbtn" class="bcbtn">确 定</a>
    -->
										</td>
									</tr>
								</table>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="alert" style="display: none;"></div>
	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript">
	$(function() {
		//样式选中
		var sd = parseInt($(".l-nav").css("height"));
		var sdf = parseInt($(".r-main").css("height"));
		$(".l-nav").css("height", sd > sdf ? sd : sdf - 15);
	});
</script>
	<script type="text/javascript">
$("#jumpPage").attr("href", "javascript:void(null)");
$("#jumpPage").click(
	function() {
		var curPage = $("#curPageText").val();
		if (isNaN(curPage)) {
			alert("输入格式不正确!");
			return;
		}
		window.location.href = "creditingInit.do?curPage=" + curPage
				+ "&pageSize=${pageBean.pageSize}";
});
</script>
	<script type="text/javascript">
	var flag = true;
	$(function() {
		<!-- action="createIpsAcct.do"  -->
		var param = {};
		param['paramMap.realName'] =  $("#realName").val();
		param['paramMap.cadkey'] =  $("#cadkey").val();
		param['paramMap.mobileNo'] =  $("#mobileNo").val();
		param['paramMap.activeEmail'] =  $("#activeEmail").val();
		$("#btnSubmit").click(function(){
			$.shovePost('createIpsAcct.do', param, function(data) {
				$("#alert").html(data);
			});
			
		});
		
		
		$("#bcbtn").click(function() {
			if (flag) {
				flag = false;
				param['paramMap.applyAmount'] = $('#applyAmount').val();
				param['paramMap.applyDetail'] = $('#applyDetail').val();
				//--add by houli 增加信用类型
				param['paramMap.creditingName'] = '信用额度';
				//--end
				$.shovePost('addCrediting.do', param, function(data) {
					var callBack = data.msg;
					if (callBack == 1) {
						alert("操作成功!");
						window.location.href = 'creditingInit.do';
						return false;
					}
					flag = true;
					alert(callBack);
				});
			}
		});
	});
</script>
</body>
</html>
