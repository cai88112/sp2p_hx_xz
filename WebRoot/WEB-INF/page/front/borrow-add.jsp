<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="script/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="ueditor/ueditor.config.js"></script>
<script type="text/javascript" src="ueditor/ueditor.all.min.js"></script>
<script type="text/javascript" src="ueditor/ueditor/lang/zh_cn/zh_cn.js"></script>
<script>
$(function(){
UE.getEditor('tr_content');
})
	

</script>
</head>
<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<form id="form" action="addBorrow.do" method="post">
		<div class="nymain">
			<div class="bigbox" style="border: none">
				<div class="sqdk" style="background: none;">
					<div class="l-nav">
						<ul>
							<li style="background: none;border-bottom: #ddd 1px dashed;"><a href="querBaseData.do?from=1" style="background-image: none;"><span>step1</span>
									基本资料</a></li>
							<li style="background: none;border-bottom: #ddd 1px dashed;"><a href="portUserAcct.do" style="background-image: none;"><span>step2</span> 绑定账号</a></li>
							<li style="background: none;border-bottom: #ddd 1px dashed;"><a href="userPassData.do?from=1" style="background-image: none;"><span>step3</span>
									上传资料</a></li>
							<!-- <li style="background: none;border-bottom: #ddd 1px dashed;"><a href="creditingInit.do" style="background-image: none;"><span>step4</span> 申请额度</a></li> -->
							<li class="on last" style="background: none;border-bottom: #ddd 1px dashed;"><a href="javascript:void(0);" style="background-image: none;"><span>step4</span>
									发布贷款</a></li>
						</ul>
					</div>
					<div class="r-main">
						<div class="til01">
							<ul id="ul">
								<li class="on">发布贷款</li>
							</ul>
							<span class="fred"
								style="color: red; font-size: 12px; padding-left: 80px; line-height: 50px;"><s:fielderror
									fieldName="enough"></s:fielderror></span>
						</div>
						<div class="rmainbox">

							<p class="tips">
								<span class="fred">*</span> 为必填项，所有资料均会严格保密。
							</p>
							<div class="tab">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<th colspan="2" align="left">借款基本信息</th>
									</tr>
									<tr>
										<td align="right">借款标题：<span class="fred">*</span></td>
										<td><input name="paramMap.title" type="text"
											class="inp280" maxlength="100" value="${paramMap.title}" /> <span
											class="fred"><s:fielderror
													fieldName="paramMap['title']"></s:fielderror></span></td>
									</tr>
									<tr>
										<td align="right">借款图片：<span class="fred"></span><input
											type="hidden" id="imgPath" name="paramMap.imgPath"
											value="${paramMap.imgPath}" />
										</td>
										<td><input type="hidden" id="personalImg"
											value="${user.personalHead}" /> <label><input
												type="radio" name="radio" id="r_1" checked="checked"
												value="1" />上传借款图片</label> <label><input type="radio"
												name="radio" id="r_2" value="2" />使用用户头像 </label> <label><input
												type="radio" name="radio" id="r_3" value="3" />使用系统头像</label> <span
											class="fred"><s:fielderror
													fieldName="paramMap['imgPath']"></s:fielderror></span> <input
											type="hidden" id="radioval" name="paramMap.radioval"
											value="${paramMap.radioval}" /></td>
									</tr>
									<tr>
										<td align="right">&nbsp;</td>
										<td>
											<div id="tab_1">
												<a href="javascript:void(0);" id="btn_personalHead"
													class="scbtn">上传图片</a>
											</div>
											<div id="tab_2" style="display: none;"></div>
											<div id="tab_3" style="display: none;">
												<table id="sysimg" style="width: 400px;">
													<tr>
														<td class="tx"><s:iterator var="sysImage"
																value="sysImageList">
																<span><img src="${sysImage.selectName}"
																	class="selimg" /></span>
															</s:iterator></td>
													</tr>
												</table>
											</div>
										</td>
									</tr>
									<tr>
										<td align="right">&nbsp;</td>
										<td class="tx"><img id="img" src="${headImg}" width="62"
											height="62" /></td>
									</tr>
									<tr>
										<td align="right">&nbsp;</td>
										<td class="tishi">（推荐使用您的生活近照，或其他与借款用途相关的图片，<br />
											有助增加借款成功几率。严禁使用他人照片）
										</td>
									</tr>
									<tr>
										<td align="right">借款标的：</td>
										<td><span id="typeName" class="fred">${session.typeName}</span></td>
									</tr>
									<tr>
										<td align="right">借款目的：<span class="fred">*</span></td>
										<td class="tishi"><s:select list="borrowPurposeList"
												id="purpose" name="paramMap.purpose" cssClass="sel_140"
												listKey="selectValue" listValue="selectName" headerKey=""
												headerValue="--请选择--"></s:select> <span class="fred"><s:fielderror
													fieldName="paramMap['purpose']"></s:fielderror></span></td>
									</tr>

									<tr>
										<td align="right">标的类型：<span class="fred"></span></td>
										<td class="tishi"><select name="paramMap.subject_matter"
											id="subject_matter" class="sel_140">
												<option value="0">--请选择--</option>
												<option value="1">车标</option>
												<option value="2">房标</option>
												<option value="3">富余宝</option>
										</select></td>
									</tr>

<tr>
										<td align="right">是否活动标：<span class="fred"></span></td>
										<td class="tishi">
										<span><input type="radio" name="paramMap.subject_activity"
												checked="checked" id="activity_1" value="0" /> 
												否</span><span style="margin-left:20px;"><input type="radio" name="paramMap.subject_activity"
												id="activity_2" value="1" />是 </span>
										</td>
									</tr>


<tr>
										<td align="right">是否新手标：<span class="fred"></span></td>
										<td class="tishi">
										<span><input type="radio" name="paramMap.subject_novice"
												checked="checked" id="novice_1" value="0" /> 
												否</span><span style="margin-left:20px;"><input type="radio" name="paramMap.subject_novice"
												id="novice_2" value="1" />是 </span>
										</td>
									</tr>

									<tr>
										<td align="right">借款期限：<span class="fred">*</span></td>
										<td><s:select list="borrowDeadlineMonthList"
												id="deadLine" name="paramMap.deadLine" cssClass="sel_140"
												listKey="key" listValue="value" headerKey=""
												headerValue="--请选择--"></s:select> <s:select
												list="borrowDeadlineDayList" id="deadDay"
												cssStyle="display:none;" name="paramMap.deadDay"
												cssClass="sel_140" listKey="key" listValue="value"
												headerKey="" headerValue="--请选择--"></s:select> <span
											class="fred"><s:fielderror
													fieldName="paramMap['deadLine']"></s:fielderror></span></td>
									</tr>
									<tr>
										<td></td>
										<td><label><s:checkbox name="paramMap.daythe"
													id="daythe" />&nbsp;置为天标</label></td>
									</tr>
									<tr>
										<td align="right">还款方式：<span class="fred">*</span></td>
										<td><s:select id="paymentMode"
												name="paramMap.paymentMode" list="borrowRepayWayList"
												cssClass="sel_140" listKey="id" listValue="name"
												headerKey="" headerValue="--请选择--"></s:select> <span
											class="fred"><s:fielderror
													fieldName="paramMap['paymentMode']"></s:fielderror></span></td>
									</tr>
									<tr>
										<td align="right">借款总额：<span class="fred">*</span></td>
										<td><input type="text" id="amount" name="paramMap.amount"
											class="inp100x" value="${paramMap.amount}" /><span
											class="fred"><s:fielderror
													fieldName="paramMap['amount']"></s:fielderror></span></td>
									</tr>
									<tr>
										<td align="right">年利率：<span class="fred">*</span></td>
										<td><input type="text" name="paramMap.annualRate"
											maxlength="5" value="${paramMap.annualRate}" class="inp100x" />%<span
											class="fred"><s:fielderror
													fieldName="paramMap['annualRate']"></s:fielderror></span></td>
									</tr>
									<s:if test="#request.subscribeStatus!=1">
										<tr>
											<td align="right">最低投标金额：<span class="fred">*</span></td>
											<td><s:select list="borrowMinAmountList"
													name="paramMap.minTenderedSum" cssClass="sel_140"
													listKey="key" listValue="value"></s:select> <span
												class="fred"><s:fielderror
														fieldName="paramMap['minTenderedSum']"></s:fielderror></span></td>
										</tr>
										<tr>
											<td align="right">最多投标金额：</td>
											<td><s:select list="borrowMaxAmountList"
													name="paramMap.maxTenderedSum" cssClass="sel_140"
													listKey="key" listValue="value" headerKey=""
													headerValue="没有限制"></s:select></td>
										</tr>
									</s:if>
									<s:else>
										<td align="right">最小认购单位：<span class="fred">*</span></td>
										<td><s:textfield name="paramMap.subscribe"
												cssClass="inp100x" />元 <span class="fred"><s:fielderror
													fieldName="paramMap['subscribe']"></s:fielderror></span> <input
											name="subscribeStatus" type="hidden" id="subscribeStatus"
											value="${subscribeStatus}" /></td>
									</s:else>
									<tr>
										<td align="right">筹标期限：<span class="fred">*</span></td>
										<td><input type="hidden" name="validateDay"
											value="${validateDay }" /> <s:if
												test="#request.validateDay==1">
												<input type="hidden" name="paramMap.raiseTerm" value="0" />无限制
    </s:if> <s:else>
												<s:select list="borrowRaiseTermList" id="raiseTerm"
													name="paramMap.raiseTerm" cssClass="sel_140" listKey="key"
													listValue="value" headerKey="" headerValue="--请选择--"></s:select>
												<span class="fred"><s:fielderror
														fieldName="paramMap['raiseTerm']"></s:fielderror></span>
											</s:else></td>
									</tr>
									<tr>
										<td align="right">借款详情：</td>
										<td>
										<textarea id="tr_content"   class="textareash"
											style="width: 400px; padding:5px;">
											${paramMap.detail}</textarea>
										</td>
										<input type="hidden" name="paramMap.detail" id="content">
										
										<%-- <td><textarea name="paramMap.detail" class="txt420">${paramMap.detail}</textarea></td> --%>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td><span class="fred"><s:fielderror
													fieldName="paramMap['detail']"></s:fielderror></span></td>
									</tr>
									<s:if test="#request.award_status==1">
										<tr>
											<th colspan="2" align="left">投标奖励</th>
										</tr>
										<tr>
											<td align="right">&nbsp;<input type="hidden"
												name="award_status" value="${award_status }" /><input
												type="hidden" id="excitation" name="paramMap.excitationType"
												value="${paramMap.excitationType}" /></td>
											<td><input type="radio" name="excitationType"
												checked="checked" id="radio_1" value="1" /> 不设置奖励</td>
										</tr>
										<tr>
											<td align="right">&nbsp;</td>
											<td><input type="radio" name="excitationType"
												id="radio_2" value="2" /> 固定总额按投标比例分配奖励 <input type="text"
												id="sum" name="paramMap.sum" value="${paramMap.sum}"
												class="inp100x gray" disabled="disabled" /> 元<span
												class="fred"><s:fielderror
														fieldName="paramMap['sum']"></s:fielderror></span></td>
										</tr>
										<tr>
											<td align="right">&nbsp;<input type="hidden"
												id="excitationMode" name="paramMap.excitationMode"
												value="${paramMap.excitationMode}" /></td>
											<td><input type="radio" name="excitationType"
												id="radio_3" value="3" /> 借款总额百分比分配奖励 <input type="text"
												id="sumRate" name="paramMap.sumRate" maxlength="20"
												value="${paramMap.sumRate}" class="inp100x gray"
												disabled="disabled" /> %<span class="fred"><s:fielderror
														fieldName="paramMap['sumRate']"></s:fielderror></span></td>
										</tr>
									</s:if>
									<s:if test="#request.password_status == 1">
										<tr>
											<th colspan="2" align="left">设置投标密码</th>
										</tr>
										<tr>
											<td align="right">&nbsp;<input type="hidden"
												name="password_status" value="${password_status }" /></td>
											<td>投标密码： <input type="password" id="investPWD"
												value="${paramMap.investPWD }" disabled="disabled"
												name="paramMap.investPWD" maxlength="20" class="inp100x" />
												&nbsp; <label> <input id="hasPWD"
													name="paramMap.hasPWD" type="checkbox" />勾选
											</label>
											</td>
										</tr>
										<tr>
											<td><span class="fred"><s:fielderror
														fieldName="paramMap['investPWD']"></s:fielderror> </span></td>
										</tr>
									</s:if>
									<tr>
										<th colspan="2" align="left">提交借款信息</th>
									</tr>
									<tr>
										<td align="right">验证码：</td>
										<td><input type="text" class="inp100x"
											name="paramMap.code" id="code" /> <img src="" title="点击更换验证码"
											style="cursor: pointer;" id="codeNum" width="46" height="18"
											onclick="javascript:switchCode()" /> <span class="fred"><s:fielderror
													fieldName="paramMap['code']"></s:fielderror></span></td>
									</tr>
									<tr>
										<td align="center" colspan="2"><span class="fred"><s:fielderror
													fieldName="paramMap['allError']"></s:fielderror></span></td>
									</tr>
									<tr>
										<td align="right">&nbsp;</td>
										<td style="padding-top: 20px;"><a id="bcbtn"
											style="cursor: pointer;" class="bcbtn">提交发布</a></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</form>
	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="script/jquery-1.7.1.min.js"></script>
	
		
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		
		
		<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
		<script type="text/javascript" src="../common/dialog/popup.js"></script>
		<script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
	<script>
		// var editor_content;
			
		// KindEditor.ready(function(K) {
		// 	editor_content = K.create('textarea[name="paramMap.detail"]', {
		// 		cssPath : '../kindeditor/plugins/code/prettify.css',
		// 		uploadJson : 'kindEditorUpload.do',
		// 		fileManagerJson : '../kindEditorManager.do',
		// 		allowFileManager : true
		// 	});
		// });

		var flag = true;
		$(document).ready(function() {
			var sd = parseInt($(".l-nav").css("height"));
			var sdf = parseInt($(".r-main").css("height"));
			$(".l-nav").css("height", sd > sdf ? sd : sdf - 15);

			var img = '${paramMap.imgPath}';
			if (img == "") {
				img = "images/default-img.jpg";
			}
			$('#imgPath').val(img);
			$("#img").attr("src", img);
			//$('#imgPath').val('${paramMap.imgPath}');
			// $("#img").attr("src",'${paramMap.imgPath}');
			$('#purpose').val('${paramMap.purpose}');
			$('#deadLine').val('${paramMap.deadLine}');
			$('#paymentMode').val('${paramMap.paymentMode}');
			$('#raiseTerm').val('${paramMap.raiseTerm}');
			$('#excitation').val('${paramMap.excitationType}');
			$('#excitationMode').val('${paramMap.excitationMode}');
			$('#radioval').val('${paramMap.radioval}');
			var excitation = $('#excitation').val();
			var mode = $('#excitationMode').val();
			var radioval = $('#radioval').val();
			if (radioval != '') {
				$('#r_' + radioval).attr('checked', 'true');
				if (radioval == 1) {
					$('#tab_1').css('display', 'block');
				}
				if (radioval == 2) {
					$('#tab_2').css('display', 'block');
					$('#tab_1').css('display', 'none');
				}
				if (radioval == 3) {
					$('#tab_3').css('display', 'block');
					$('#tab_1').css('display', 'none');
				}
			}
			if (excitation != '') {
				$('#radio_' + excitation).attr('checked', 'true');
				if (excitation == 2) {
					$('#sum').removeClass('gray');
					$('#sum').removeAttr('disabled');
					$('#sumRate').addClass('gray');
					$('#sumRate').attr('disabled');
					$('#sumRate').val('');
				} else if (excitation == 3) {
					$('#sumRate').removeClass('gray');
					$('#sumRate').removeAttr('disabled');
					$('#sum').addClass('gray');
					$('#sum').attr('disabled');
					$('#sum').val('');
				}
			} else {
				$('#excitation').val('1');
			}
			if (mode == '2') {
				$('#mode').attr('checked', 'true');
			} else {
				$('#excitationMode').val("1");
			}
		});
		var paymentModeHtml;
		$(function() {
			//样式选中
			paymentModeHtml = $("#paymentMode").html();
			$('input[name="excitationType"]').click(function() {
				if ($(this).val() == 2) {
					$('#sum').removeClass('gray');
					$('#sum').removeAttr('disabled');
					$('#sumRate').addClass('gray');
					$('#sumRate').attr('disabled');
					$('#sumRate').val('');
				} else if ($(this).val() == 3) {
					$('#sumRate').removeClass('gray');
					$('#sumRate').removeAttr('disabled');
					$('#sum').addClass('gray');
					$('#sum').attr('disabled');
					$('#sum').val('');
				} else {
					$('#sumRate').addClass('gray');
					$('#sumRate').attr('disabled');
					$('#sumRate').val('');
					$('#sum').addClass('gray');
					$('#sum').attr('disabled');
					$('#sum').val('');
				}
				$('#excitation').val($(this).val());
			});
			$('#mode').click(function() {
				var check = $(this).attr('checked');
				if (check == 'checked') {
					$('#excitationMode').val('2');
				} else {
					$('#excitationMode').val('1');
				}
			});
			$('#hasPWD').click(function() {
				var hasPWD = $('#hasPWD').attr('checked');
				if (hasPWD == 'checked') {
					$('#investPWD').attr('disabled', false);
				} else {
					$('#investPWD').attr('disabled', true);
				}
				$('#investPWD').val('');
			});

			$('#daythe').click(function() {
				showdays();
			});
			$('#bcbtn').click(function() {
				
				if ($("#subscribeStatus").val() != 1) {
					var arStart = $('#paramMap_minTenderedSum').val();
					var amount = $('#amount').val();
					arStart = parseFloat(arStart);
					amount = parseFloat(amount);
					var arEnd = $('#paramMap_maxTenderedSum').val();
					arEnd = parseFloat(arEnd);
					if (arStart > arStart) {
						alert('最多投标金额不能小于最低投标金额!');
						$('#paramMap_maxTenderedSum')[0].selectedIndex = 0;
						return false;
					}
					if (arStart > amount) {
						alert('最低投标金额不能超过借款金额!');
						$('#paramMap_minTenderedSum')[0].selectedIndex = 0;
						return false;
					}
					if (arEnd > amount) {
						alert('最多投标金额不能超过借款金额!');
						$('#paramMap_maxTenderedSum')[0].selectedIndex = 0;
						return false;
					}
				}
				//  alert("您申请的借款已经提交管理人员进行审核，谢谢！")
				if (flag) {
					$("#content").val(UE.getEditor('tr_content').getContent());
					$('#form').submit();
					flag = false;
				}
			});
			$("#paramMap_maxTenderedSum").change(function() {
				var arStart = $('#paramMap_minTenderedSum').val();
				arStart = parseFloat(arStart);
				var arEnd = $(this).val();
				arEnd = parseFloat(arEnd);
				if (arEnd < arStart) {
					alert('最多投标金额不能小于最低投标金额!');
					$('#paramMap_maxTenderedSum')[0].selectedIndex = 0;
				}
			});
			$("#paramMap_minTenderedSum").change(function() {
				$('#paramMap_maxTenderedSum')[0].selectedIndex = 0;
			});
			//上传图片
			$("#btn_personalHead")
					.click(
							function() {
								var dir = getDirNum();
								var json = "{'fileType':'JPG,BMP,GIF,TIF,PNG','fileSource':'user/"
										+ dir
										+ "','fileLimitSize':0.5,'title':'上传图片','cfn':'uploadCall','cp':'img'}";
								json = encodeURIComponent(json);
								window.showModalDialog(
										"uploadFileAction.do?obj=" + json,
										window,
										"dialogWidth=500px;dialogHeight=400px");
								var headImgPath = $("#img").attr("src");
								if (headImgPath == "") {
									alert("上传失败！");
								}
							});
			$('#sysimg img').click(function() {
				$('#imgPath').val($(this).attr('src'));
				$('#img').attr('src', $(this).attr('src'));
			});
			$('#r_1').click(function() {
				$('#tab_1').css('display', 'block');
				$('#tab_2').css('display', 'none');
				$('#tab_3').css('display', 'none');
				$('#radioval').val('1');
			});
			$('#r_2').click(function() {
				var personalImg = $('#personalImg').val();
				$('#tab_1').css('display', 'none');
				$('#tab_2').css('display', 'block');
				$('#tab_3').css('display', 'none');
				$('#radioval').val('2');
				$('#img').attr('src', personalImg);
				$('#imgPath').val(personalImg);
			});
			$('#r_3').click(function() {
				$('#tab_1').css('display', 'none');
				$('#tab_2').css('display', 'none');
				$('#tab_3').css('display', 'block');
				$('#radioval').val('3');
			});
			$('#paymentMode').change(function() {
				var check = $('#daythe').attr('checked');
				if (check == true) {
					$('#paymentMode').get(0).selectedIndex = 1;
				}
			});

			switchCode();
			showdays();

			//add by houli 进入页面发布借款的时候给用户提示信息
			var msg = '${request.msg}';
			var ulimit = '${request.usableCreditLimit}';
			var limit = '${request.creditLimit}';
			if (msg != null && msg != "") {
				//alert("   您的信用额度是" + limit + "，可用借款额度是" + ulimit + "。 \n" + msg);
			}
			if ($("#investPWD").val() != '') {
				$('#investPWD').attr('disabled', false);
				$('#hasPWD').attr('checked', true);
			}
		});
		function switchCode() {
			var timenow = new Date();
			$('#codeNum').attr('src',
					'admin/imageCode.do?pageId=borrow&d=' + timenow);
		};

		function showdays() {
			var check = $('#daythe').attr('checked');
			if (check == 'checked') {
				$('#deadLine').css('display', 'none');
				$('#deadDay').css('display', 'block');
				$('#paymentMode').attr('disabled', true);
				// $('#paymentMode').get(0).selectedIndex = 1;

				$("#paymentMode").html("<option value='1'>到期还本付息</option>");
			} else {
				$('#paymentMode').attr('disabled', false);
				$('#paymentMode').get(0).selectedIndex = 0;
				$('#deadLine').css('display', 'block');
				$('#deadDay').css('display', 'none');
				$("#paymentMode").html("");
				$("#paymentMode").html(paymentModeHtml);
			}
		}
	</script>
	<script>
function uploadCall(basepath,fileName,cp){
	if(cp == "img"){
		var path = "upload/"+basepath+"/"+fileName;
		$("#img").attr("src",path);
		$("#setImg").attr("src",path);
		$("#imgPath").val(path);
	}
}
function getDirNum(){
	var date = new Date();
	var m = date.getMonth()+1;
	var d = date.getDate();
	if(m<10){
		m = "0"+m;
    }
	if(d<10){
	   d = "0"+d;
	}
	var dirName = date.getFullYear()+""+m+""+d;
	return dirName; 
}
</script>
</body>
</html>