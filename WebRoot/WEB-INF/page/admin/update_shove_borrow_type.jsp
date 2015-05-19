<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>借款管理</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
		
		<script src="../script/jquery-1.7.1.min.js" type="text/javascript"></script>
		<script src="../layer/layer.mintest.js" type="text/javascript"></script>
		<script src="../layer/layertest.js" type="text/javascript"></script>
		<script>
		  $(function(){
		      //提交表单
				$("#btn_save").click(function(){
				var stIdArray = [];
	 			$("input[name='paramMapStyles']:checked").each(function(){
	 				stIdArray.push($(this).val());
	 			});
	 			var ids = stIdArray.join(",");
	 			var nid = '${paramMap.nid}';
	 			if(nid=="seconds" || nid =='flow'){
	 				ids =0;
	 			}
	 			var param = {};
	 			if(nid=="flow"){
					var counter = [];
		 			$("input[name='paramMaplistcounter']:checked").each(function(){ //反担保方式
		 				counter.push($(this).val());
		 			});
		 			var counter_ids = counter.join(",");
		 			var listIn = [];
		 			$("input[name='paramMaplistIn']:checked").each(function(){ //反担保方式
		 				listIn.push($(this).val());
		 			});
		 			var listIn_ids = listIn.join(",");
		 			param["paramMapcounter"] = counter_ids;
		 			param["paramMapInstitution"] = listIn_ids;
				}
	 			param["title"] = $("#tb_title").val();//名称
				param["tb_amount_first"] = $("#tb_amount_first").val();//最低借款额度
				param["tb_amount_end"] = $("#tb_amount_end").val();//最高借款额度
				param["tb_account_multiple"] = $("#tb_account_multiple").val();//额度倍数
				param["tb_apr_first"] = $("#tb_apr_first").val();//最低年利率
				param["tb_apr_end"] = $("#tb_apr_end").val();//最高年利率
				param["tb_period_month"] = $("#tb_period_month").val();//借款期限  月标
				param["tb_period_day"] = $("#tb_period_day").val();//借款期限  天标
				param["tb_validate"] = $("#tb_validate").val();//有效期
				param["tb_check_first"] = $("#tb_check_first").val(); //审核最短时间
				param["tb_check_end"] = $("#tb_check_end").val();//审核最长时间
				param["tb_tender_account_min"] = $("#tb_tender_account_min").val(); // 最低投标金额
				param["tb_account_max"] = $("#tb_account_max").val(); // 最高投标金额
				param["tb_award_scale_first"] = $("#tb_award_scale_first").val();//奖励比例最小值
				param["tb_award_scale_end"] = $("#tb_award_scale_end").val();//奖励比例最大值
				param["tb_award_account_first"] = $("#tb_award_account_first").val();//奖励金额最小值
				param["tb_award_account_end"] = $("#tb_award_account_end").val();//奖励金额最大值
				//param["tb_verify_auto_remark"] = $("#tb_verify_auto_remark").val();//初审自动通过备注
				param["tb_vip_frost_scale"] = $("#tb_vip_frost_scale").val();//VIP冻结保证金
				param["tb_all_frost_scale"] = $("#tb_all_frost_scale").val();//普通会员冻结保证金
				param["tb_late_days_month"] = $("#tb_late_days_month").val();//垫付逾期天数 (月标)
				param["tb_late_days_day"] = $("#tb_late_days_day").val();//垫付逾期天数(天标)
				param["tb_vip_late_scale"] = $("#tb_vip_late_scale").val();//vip垫付利息比例
				param["tb_all_late_scale"] = $("#tb_all_late_scale").val();//普通会员垫付利息比例
				param["locan_fee"] = $("#locan_fee").val();//借款费 
				param["locan_month"] = $("#locan_month").val();//借款限定期数
				param["locan_fee_month"] = $("#locan_fee_month").val();//超出限定期数的借款费
				param["day_fee"] = $("#day_fee").val();//天标借款费
				
				param["paramMapStyles"] = ids;// 还款方式
				param["nid"] = '${paramMap.nid}';
				$.post("updateBorrowType.do",param,function(data){
					if(data==55){
						$("#updateShovetypes").submit();
					}
					if(data==1){
						$("#title").html(" *　请输入名称");
						return false;
					}else{
						$("#title").html("*");
					} if(data == 2){
						$("#amount_first").html(" * 请填写最低借款额度");
						return false;
					}else if(data==3){
						$("#amount_first").html(" * 请正确填写借款额度");
						return false;
					}else if(data==4){
						$("#amount_first").html(" * 请填写最高借款额度");
						return false;
					}else if(data==5){
						$("#amount_first").html(" * 请正确填写借款额度");
						return false;
					}else if(data==6){
						$("#amount_first").html(" * 最低借款额度不能大于最大借款额度");
						return false;
					}else{
						$("#amount_first").html(" *");
						} 
					if(data==7){
						$("#account_multiple").html(" * 请填写额度倍数!");
						return false;
					}else if(data==8){
						$("#account_multiple").html(" * 请正确填写额度倍数!");
						return false;
					}else {
						$("#account_multiple").html(" * ");
						}
					if(data==9){
						$("#apr_first").html(" * 请填写最低年利率");
						return false;
					}else if(data==10){
						$("#apr_first").html(" * 请正确填写年利率");
						return false;
					}else if(data==11){
						$("#apr_first").html(" * 请填写最高年利率");
						return false;
					}else if(data==12){
						$("#apr_first").html(" * 请正确填写年利率");
						return false;
					}else if(data==13){
						$("#apr_first").html(" * 最低年利率不能高于最高年利率");
						return false;
					}else if (data ==58){
						$("#apr_first").html(" * 年利率范围0.01%~100%");
						return false;
					}else{
						$("#apr_first").html(" * ");
					} 
				    if(data==14){
						$("#period_month").html(" * 请填写借款期限 (月标)");
						return false;
					}else if(data==15){
						$("#period_month").html(" * 请正确填写借款期限(月标),月标最大月份为60月");
						return false;
					}else{
						$("#period_month").html(" * ");
					} 
					if(data==16){
						$("#period_day").html(" * 请填写借款期限 (天标)");
						return false;
					}else if(data==17){
						$("#period_day").html(" * 请正确填写借款期限(天标),天标最大天数为25天");
						return false;
					}else{
						$("#period_day").html(" *");
						} 
					if(data==18){
						$("#validate").html(" *请填写有效期");
						return false;
					}else if(data==19){
						$("#validate").html(" *请正确填写有效期");
						return false;
					}else{
						$("#validate").html(" *");
					}
					 if(data==20){
						 $("#check_first").html(" *请填写最短审核时间");
						return false;
					}else if(data==21){
						 $("#check_first").html(" *请正确填写审核时间");
						return false;
					}else if(data==22){
						 $("#check_first").html(" *请填写最长审核时间");
						return false;
					}else if(data==23){
						 $("#check_first").html(" *请正确填写审核时间");
						return false;
					}else if(data==24){
						 $("#check_first").html(" *最短审核时间不能大于最长审核时间");
						return false;
					}else{
						$("#check_first").html(" *");
					} if(data==26){
						$("#tender_account_min").html(" *请填写最低投标金额");
						return false;
					}else if(data==27){
						$("#tender_account_min").html(" *请填写最高投标金额");
						return false;
					}else if(data==28){
						$("#tender_account_min").html(" *请正确填写投标金额");
						return false;
					}else{
						$("#tender_account_min").html(" *");
					}
					 if(data==30){
						$("#award_scale_first").html(" *请填写最小投标奖励比例");
						return false;
					}else if(data==31){
						$("#award_scale_first").html(" *请填写最大投标奖励比例");
						return false;
					}else if(data==32){
						$("#award_scale_first").html(" *正确填写投标奖励比例");
						return false;
					}else if(data==33){
						$("#award_scale_first").html(" *最小投标奖励比能大于最大投标奖励");
						return false;
					}else if(data==34){
						$("#award_scale_first").html(" *正确填写投标奖励比例，投标比例范围0.01%~100%");
						return false;
					}else {
						$("#award_scale_first").html(" *");
					}
					if(data==35){
						$("#award_account_first").html(" *请填写固定奖励投标金额最小值");
						return false;
					}else if(data==36){
						$("#award_account_first").html(" *请填写固定奖励投标金额最大值");
						return false;
					}else if(data==37){
						$("#award_account_first").html(" *请正确填写固定奖励投标金额");
						return false;
					}else if(data==38){
						$("#award_account_first").html(" *固定奖励最小金额不能大于最大固定奖励金额");
						return false;
					}else {
						$("#award_account_first").html(" *");
					}if(data==39){
						$("#verify_auto_remark").html(" * 请填写初审自动通过备注");
						return false;
					}else {
						$("#verify_auto_remark").html(" *");
					}if(data==40){
						$("#vip_frost_scale").html(" *请填写Vip保证金比例");
						return false;
					}else if(data==41){
						$("#vip_frost_scale").html(" *请正确填写Vip保证金比例!");
						return false;
					}else if(data==42){
						$("#vip_frost_scale").html(" *保证金比例范围0.01%~100%之间!");
						return false;
					}else{
						$("#vip_frost_scale").html(" *");
						}
					 if(data==43){
						$("#all_frost_scale").html(" *请填写普通会员保证金比例!");
						return false;
					}else if(data==44){
						 $("#all_frost_scale").html(" *请正确填写普通会员保证金比例!");
						return false;
					}else if(data==45){
						$("#all_frost_scale").html(" *保证金比例范围0.01%~100%之间");
						return false;
					}else {
						$("#all_frost_scale").html(" *");
						}
					if(data==46){
						$("#late_days_month").html(" *请填写逾期垫付天数 (月标)!");
						return false;
					}else if(data==47){
						$("#late_days_month").html(" *请正确填写逾期垫付天数 (月标)!");
						return false;
					}else{
						$("#late_days_month").html(" *");
					} if(data==48){
						$("#late_days_day").html(" *请填写逾期垫付天数 (天标)");
						return false;
					}else if(data==49){
						$("#late_days_day").html(" *请正确填写逾期垫付天数 (天标)");
						return false;
					}else{
						$("#late_days_day").html(" *");
						} 
					if(data==50){
						$("#vip_late_scale").html(" *请填写vip垫付本息比例!");
						return false;
					}else if(data==51){
						$("#vip_late_scale").html(" *请正确填写vip垫付本息比例,比例范围0.01%~100%!");
						return false;
					}else {
						$("#vip_late_scale").html(" *");
					}if(data==52){
						$("#all_late_scale").html(" *普通会员垫付利息比例!");
						return false;
					}else if(data==53){
						$("#all_late_scale").html(" *请正确填写普通会员垫付利息比例,比例范围0.01%~100%!");
						return false;
					}else{
						$("#all_late_scale").html(" *");
						} 
					if(data==54){
						$("#style_id").html(" * 请选择还款方式");
						return false;
					}else{
						$("#style_id").html(" *");
					}
					if(data==56){
						$("#listcounter_id").html(" * 请选择担保机构");
						return false;
					}else{
						$("#listcounter_id").html(" *");
					}
					if(data==57){
						$("#listIn_id").html(" * 请选择反担保方式");
						return false;
					}else{
						$("#listIn_id").html(" *");
					}
					if(data==60){
						$("#t_locan_fee").html(" * 请填写借款费");
						return false;
					}else if(data==61){
						$("#t_locan_fee").html(" * 请正确填写借款费");
						return false;
					}else if(data==62){
						$("#t_locan_fee").html(" * 请填写借款限定期限(月标)");
						return false;
					}else if(data==63){
						$("#t_locan_fee").html(" * 请正确填写借款限定期限(月标),最大36个月");
						return false;
					}else if(data==64){
						$("#t_locan_fee").html(" * 请填写超出借款限定期限的借款费");
						return false;
					}else if(data==65){
						$("#t_locan_fee").html(" * 请填写借款限定期限的借款费");
						return false;
					} else{
						$("#t_locan_fee").html(" *");
						}
					if(data==66){
						$("#t_day_fee").html(" * 请填写天标借款费");
						return false;
					} else if(data==67){
						$("#t_day_fee").html(" * 请正确天写天标借款费");
						return false;
					}else{
						$("#t_day_fee").html(" *");
						}
				});
					//$("#updateShovetypes").submit();
				});
		  });
		   
		</script>
		
	</head>
	<body>
		<form id="updateShovetypes" name="updateShovetypes" action="updateShoveTypeInfo.do" method="post">
			<div id="right">
				<div style="padding: 15px 10px 0px 10px;">
					<div>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="100" height="28"  class="main_alll_h2">
									<a href="updateShoveTypeInit.do?id=${paramMap.id}">修改标种类型</a>
								</td>
								<td>
									&nbsp;
								</td>
							</tr>
						</table>
					</div>
					 
					</div>
					<div style="width: auto; background-color: #FFF; padding: 20px;">
						<table width="800" border="0" cellspacing="1" cellpadding="3"  align="center" >
							<tr>
							<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									标种类型：
								</td>
								
								<td align="left" class="f66">
									${paramMap.name}
								</td>
								
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									标识名：
								</td>
								<td align="left" class="f66">
									${paramMap.nid}
								</td>
							</tr>
								<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									名称：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_title" name="paramMap.title"
										cssClass="in_text_250" theme="simple"/>
									<span class="require-field" id="title">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width:100px;  height: 30px;" align="right" class="blue12" valign="top">
									描述：
								</td>
								<td align="left" class="f66">
									<s:textarea name="paramMap.description" cols="40" rows="5" id="description"/>
									<span class="require-field"><s:fielderror fieldName="paramMap['description']"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									状态：
								</td>
								<td align="left" class="f66">
									      <s:radio list="#{'1':'开启','2':'关闭'}" name="paramMap.status"></s:radio>
								</td>
							</tr>
							
								<tr>
								<td style="width:100px;  height: 30px;" align="right" class="blue12" valign="top">
									额度类型：
								</td>
								<td align="left" class="f66">
									<s:iterator  value="#request.mapTypeList" var="mapType" status="st">
									<input type="radio" name="paramMap_amount_type" value="${mapType.id}" 
									   		<s:if test='paramMap.amount_type == #mapType.id'>checked="checked"</s:if>
									<s:else>
									</s:else>
									    />${mapType.title }&nbsp;&nbsp;
								</s:iterator>
									<span class="require-field"><s:fielderror fieldName="paramMap['contents']"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									借款额度：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_amount_first" name="paramMap.amount_first"
										cssClass="in_text_50" theme="simple"/>元
									~
									<s:textfield id="tb_amount_end" name="paramMap.amount_end"
										cssClass="in_text_50" theme="simple"/>元
									<span class="require-field" id="amount_first">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									借款金额倍数：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_account_multiple" name="paramMap.account_multiple"
										cssClass="in_text_50" theme="simple"/>元 (0表示不限)
									<span class="require-field" id="account_multiple">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									年利率：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_apr_first" name="paramMap.apr_first"
										cssClass="in_text_50" theme="simple"/>%
									~
									<s:textfield id="tb_apr_end" name="paramMap.apr_end"
										cssClass="in_text_50" theme="simple"/>%
									<span class="require-field" id="apr_first">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									借款期限(月标)：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_period_month" name="paramMap.period_month"
										cssClass="in_text_250" theme="simple"/>月(多个月请用,号隔开)
									<span class="require-field" id="period_month">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									借款期限(天标)：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_period_day" name="paramMap.period_day"
										cssClass="in_text_250" theme="simple"/>天(多天请用,号隔开)
									<span class="require-field" id="period_day">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
								<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									有效期：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_validate" name="paramMap.validate"
										cssClass="in_text_250" theme="simple"/>天(多天请用,号隔开，0 表示无限期)
									<span class="require-field" id="validate">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr style="display: none;">
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									审核时间：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_check_first" name="paramMap.check_first"
										cssClass="in_text_50" theme="simple"/>天
									~
									<s:textfield id="tb_check_end" name="paramMap.check_end"
										cssClass="in_text_50" theme="simple"/>天
									<span class="require-field" id="check_first">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									最低投标金额：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_tender_account_min" name="paramMap.tender_account_min"
										cssClass="in_text_250" theme="simple"/>元(多个请用,号隔开)
									<span class="require-field" id="tender_account_min">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									最高投标金额：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_account_max" name="paramMap.tender_account_max"
										cssClass="in_text_250" theme="simple"/>元(多个请用,号隔开,0表示不限)
									<span class="require-field" id="account_max">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									是否启用奖励：
								</td>
								<td align="left" class="f66">
									<input type="radio" name="paramMap_award_status" value="1" 
									   		<s:if test='paramMap.award_status == 1'>checked="checked"</s:if>
																					<s:else></s:else>
									    />开启 
									 <input type="radio" name="paramMap_award_status" value="2"
									    <s:if test='paramMap.award_status == 2'>checked="checked"</s:if>
																					<s:else></s:else>
									    />关闭
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									是否启用借款密码：
								</td>
								<td align="left" class="f66">
								<s:if test="%{'flow' eq paramMap.nid}">
									<input type="radio" name="paramMap_password_status" value="2"  checked="checked"
									    />关闭
								</s:if>
								<s:else>
									<input type="radio" name="paramMap_password_status" value="1" 
									   		<s:if test='paramMap.password_status == 1'>checked="checked"</s:if>
																					<s:else></s:else>
									    />开启 
									 <input type="radio" name="paramMap_password_status" value="2"
									    <s:if test='paramMap.password_status == 2'>checked="checked"</s:if>
													
																					<s:else></s:else>
									    />关闭
								 </s:else>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									奖励比例：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_award_scale_first" name="paramMap.award_scale_first"
										cssClass="in_text_50" theme="simple"/>%
									~
									<s:textfield id="tb_award_scale_end" name="paramMap.award_scale_end"
										cssClass="in_text_50" theme="simple"/>%（按借款金额的比例进行奖励）
									<span class="require-field" id="award_scale_first">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									奖励金额：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_award_account_first" name="paramMap.award_account_first"
										cssClass="in_text_50" theme="simple"/>元
									~
									<s:textfield id="tb_award_account_end" name="paramMap.award_account_end"
										cssClass="in_text_50" theme="simple"/>元（如果选择金额的话，则按此奖励的金额范围）
									<span class="require-field" id="award_account_first">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr><%--
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									初审自动通过：
								</td>
								<td align="left" class="f66">
									<input type="radio" name="paramMap_verify_auto_status" value="1" 
									   		<s:if test='paramMap.verify_auto_status == 1'>checked="checked"</s:if>
																					<s:else></s:else>
									    />开启 
									 <input type="radio" name="paramMap_verify_auto_status" value="2"
									    <s:if test='paramMap.verify_auto_status == 2'>checked="checked"</s:if>
																					<s:else></s:else>
									    />关闭
								</td>
							</tr>
							<tr>
								<td style="width: 130px; height: 30px;" align="right"
									class="blue12">
									初审自动通过的审核备注：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_verify_auto_remark" name="paramMap.verify_auto_remark"
										cssClass="in_text_250" theme="simple"/>
									<span class="require-field" id="verify_auto_remark">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>--%>
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									vip冻结保证金：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_vip_frost_scale" name="paramMap.vip_frost_scale"
										cssClass="in_text_50" theme="simple"/>%
									<span class="require-field" id="vip_frost_scale">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									普通会员冻结保证金：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_all_frost_scale" name="paramMap.all_frost_scale"
										cssClass="in_text_50" theme="simple"/>%
									<span class="require-field" id="all_frost_scale">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									垫付逾期天数(月标)：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_late_days_month" name="paramMap.late_days_month"
										cssClass="in_text_50" theme="simple"/>天
									<span class="require-field" id="late_days_month">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									垫付逾期天数(天标)：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_late_days_day" name="paramMap.late_days_day"
										cssClass="in_text_50" theme="simple"/>天
									<span class="require-field" id="late_days_day">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr style="display: none">
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
								vip垫付本息比例：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_vip_late_scale" name="paramMap.vip_late_scale"
										cssClass="in_text_50" theme="simple"/>%
									<span class="require-field" id="vip_late_scale">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr style="display: none">
								<td style="width: 140px; height: 30px;" align="right" 
									class="blue12">
								普通会员垫付本金比例：
								</td>
								<td align="left" class="f66">
									<s:textfield id="tb_all_late_scale" name="paramMap.all_late_scale"
										cssClass="in_text_50" theme="simple"/>%
									<span class="require-field" id="all_late_scale">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr >
								<td style="width: 120px; height: 30px;" align="right" 
									class="blue12">
								借款费扣费公式(月)：
								</td>
								<td align="left" class="f66">
								<s:if test="%{paramMap.nid eq 'seconds'}">
									本金 * <s:textfield id="locan_fee" name="paramMap.locan_fee"  cssClass="in_text_30" theme="simple"/>%<span style="display: none;">+ 本金 *(期数 - <s:textfield id="locan_month" name="paramMap.locan_month"
										cssClass="in_text_30" theme="simple"/>个月 )* <s:textfield id="locan_fee_month" name="paramMap.locan_fee_month"
										cssClass="in_text_30" theme="simple"/>%</span>
								</s:if>
								<s:elseif  test="%{paramMap.nid eq 'flow'}">
								本金 * <s:textfield id="locan_fee" name="paramMap.locan_fee" cssClass="in_text_30" theme="simple"/>%<span style="display: none;">+ 本金 *(期数 - <s:textfield id="locan_month" name="paramMap.locan_month"
										cssClass="in_text_30" theme="simple"/>个月 )* <s:textfield id="locan_fee_month" name="paramMap.locan_fee_month"
										cssClass="in_text_30" theme="simple"/>%</span>
								</s:elseif>
								<s:else>
									本金 * <s:textfield id="locan_fee" name="paramMap.locan_fee"
										cssClass="in_text_30" theme="simple"/>% + 本金 *(期数 - <s:textfield id="locan_month" name="paramMap.locan_month"
										cssClass="in_text_30" theme="simple"/>个月 )* <s:textfield id="locan_fee_month" name="paramMap.locan_fee_month"
										cssClass="in_text_30" theme="simple"/>%
								</s:else>
									<span class="require-field" id="t_locan_fee">*<s:fielderror fieldName="title"></s:fielderror></span>
								<br />
								<span>
								<s:if test="%{paramMap.nid eq 'seconds'}">
								本金 *  借款管理费  
								</s:if>
								<s:elseif  test="%{paramMap.nid eq 'flow'}">
								本金 *  借款管理费  
								</s:elseif>
								<s:else>
								本金 *  借款管理费   +  本金   * (期数  - 限定期数) * 超出限定期数每月的借款费
								</s:else>
								</span>
								</td>
							</tr>
								<s:if test="%{paramMap.nid eq 'seconds'}">
								<tr style="display: none;">
								<td style="width: 120px; height: 30px;" align="right" 
									class="blue12">
								借款费扣费公式(天)：
								</td>
								<td align="left" class="f66">
									本金 * (<s:textfield id="day_fee" name="paramMap.day_fee"
										cssClass="in_text_30" theme="simple"/>% / 365 )
									<span class="require-field" id="t_day_fee">*<s:fielderror fieldName="title"></s:fielderror></span>
									<br />
									<span>
									本金 * 借款管理费 /365天
									</span>
								</td>
							</tr>
								</s:if>
								<s:elseif  test="%{paramMap.nid eq 'flow'}">
								<tr style="display: none;" >
								<td style="width: 120px; height: 30px;" align="right" 
									class="blue12">
								借款费扣费公式(天)：
								</td>
								<td align="left" class="f66">
									本金 * (<s:textfield id="day_fee" name="paramMap.day_fee"
										cssClass="in_text_30" theme="simple"/>% / 365 )
									<span class="require-field" id="t_day_fee">*<s:fielderror fieldName="title"></s:fielderror></span>
									<br />
									<span>
									本金 * 借款管理费 /365天
									</span>
								</td>
							</tr>
								</s:elseif>
								<s:else>
							<tr >
								<td style="width: 120px; height: 30px;" align="right" 
									class="blue12">
								借款费扣费公式(天)：
								</td>
								<td align="left" class="f66">
									本金 * (<s:textfield id="day_fee" name="paramMap.day_fee"
										cssClass="in_text_30" theme="simple"/>% / 365 )
									<span class="require-field" id="t_day_fee">*<s:fielderror fieldName="title"></s:fielderror></span>
									<br />
									<span>
									本金 * 借款管理费 /365天
									</span>
								</td>
							</tr>
								</s:else>
							
							<tr>
								<td style="width: 100px; height: 30px;" align="right"
									class="blue12">
									还款方式：
								</td>
								<td align="left" class="f66">
								<s:if test="%{paramMap.nid eq 'seconds'}">
									秒还
								</s:if>
								<s:elseif test="%{paramMap.nid eq 'flow'}">
								 一次性还款
								</s:elseif>
								<s:else>
									<s:iterator value="#request.mapList" var="map"  status="st">
									<input type="checkbox" name="paramMapStyles" value="${map.id }" 
										<s:iterator value="#request.arr"  var="type" status="stat">
									   		<s:if test='#map.id == #request.arr[#stat.index]'>checked="checked"</s:if>
																					<s:else></s:else>
										</s:iterator>
									    />${map.title } &nbsp;
									  </s:iterator>
									 <span class="require-field" id="style_id">*<s:fielderror fieldName="title"></s:fielderror></span>
								</s:else>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									是否开启认购模式：
								</td>
								<td align="left" class="f66">
							<s:if test="%{'flow' eq paramMap.nid}">
									<input type="radio" name="paramMap_subscribe_status" value="1"  checked="checked"
									    />开启 
							</s:if>
							<s:else>
									<!-- <input type="radio" name="paramMap_subscribe_status" value="1" 
									   		<s:if test='paramMap.subscribe_status == 1'>checked="checked"</s:if>
																					<s:else></s:else>
									    />开启 
									 <input type="radio" name="paramMap_subscribe_status" value="2"
									    <s:if test='paramMap.subscribe_status == 2'>checked="checked"</s:if>
																					<s:else></s:else>
									    />关闭 -->
									    <input type="radio" name="paramMap_subscribe_status" value="2"  checked="checked"
									    />关闭
									    
						</s:else>
								</td>
							</tr>
							<s:if test="%{'flow' eq paramMap.nid}">
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									担保机构：
								</td>
								<td align="left" class="f66">
									<s:iterator value="#request.listInstitution" var="listIn"  status="st">
									<input type="radio" name="paramMaplistIn" value="${listIn.selectValue }" 
										<s:iterator value="#request.arrInsti"  var="type" status="sts">
									   		<s:if test='#listIn.selectValue == #request.arrInsti[#sts.index]'>checked="checked"</s:if>
																					<s:else></s:else>
										</s:iterator>
									    />${listIn.selectName } &nbsp;
									  </s:iterator>
									 <span class="require-field" id="listIn_id">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td style="width: 120px; height: 30px;" align="right"
									class="blue12">
									反担保方式：
								</td>
								<td align="left" class="f66">
									<s:iterator value="#request.listCounter" var="listcounter"  status="st">
									<input type="radio" name="paramMaplistcounter" value="${listcounter.selectValue}" 
										<s:iterator value="#request.arrCounter"  var="type" status="stat">
									   		<s:if test='#listcounter.selectValue == #request.arrCounter[#stat.index]'>checked="checked"</s:if>
																					<s:else></s:else>
										</s:iterator>
									    />${listcounter.selectName} &nbsp;
									  </s:iterator>
									   <span class="require-field" id="listcounter_id">*<s:fielderror fieldName="title"></s:fielderror></span>
								</td>
							</tr>
							</s:if>
							<tr>
								<td height="36" align="right" class="blue12">
									<s:hidden name="action"></s:hidden>
									<input type="hidden" value="${paramMap.id}" name="paramMap.id" id="paramMap.id" />
									&nbsp;
								</td>
								<td>
									<input id="btn_save" type="button"
										style="background-image: url('../images/admin/btn_queding.jpg'); width: 70px; border: 0; height: 26px"></input>
									&nbsp;
									&nbsp; &nbsp;
									<span class="require-field"><s:fielderror fieldName="actionMsg" theme="simple"></s:fielderror></span>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<img id="img" src="../images/NoImg.GIF"
										style="display: none; width: 100px; height: 100px;" />
								</td>
							</tr>
						</table>
						<br />	<br />	<br />
					</div>
				</div>
		</form>
	</body>
</html>
