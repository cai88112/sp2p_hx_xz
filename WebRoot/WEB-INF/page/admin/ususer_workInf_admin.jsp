<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<!--=======================-->
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/css.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="../common/date/calendar.css"/>
		<link rel="stylesheet" type="text/css" href="../css/admin/admin_css.css" />
		<script type="text/javascript" src="../common/date/calendar.js"></script>
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		<script type="text/javascript">
		 $(document).ready(function(){
		        $("#li_geren").click(function(){
		            window.location.href='adminBase.do?uid=${id}';
		        });
               });
              
            
		</script>
	</head>

	<body>
	
	<div id="right">
			<div style="padding: 15px 10px 0px 10px;">
				<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
						   <td width="100" height="28" class="main_alll_h2">
								<a href="javascript:void(0);" >基本资料审核</a>
							</td>
							<td width="2">
								&nbsp;
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
					</table>
				</div>
						<div>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
				<hr/>
						</tr>
					</table>
				</div>
	
	
		<!-- 引用头部公共部分 -->
		<div class="nymain" style="margin-top: 0px;">
			<div class="bigbox">
				<div class="sqdk" style="background: none;padding:0px">
					<div class="r-main" style="margin-left:100px">
						<div class="til01">
							<ul>
								<li  id="li_geren" >
									个人详细信息
								</li>
								<li  id="li_work" class="on">
											工作认证信息
								</li>
							</ul>
						</div>
						<div class="rmainbox" >
						<!--  -->
						<!-- 基础资料div -->
						
							
							<!-- 是工作认证资料 -->
							<div class="box01"  id="div_work">
								<div class="tab">
									<form id="workform">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<th colspan="2" align="left">
													工作信息
												</th>
											</tr>
											<tr>
												<td align="right">
													单位名称：<strong>*</strong>
												</td>
												<td>
													<input type="text" name="paramMap.orgName" id="orgName"
														class="inp188" value="${map.orgName}" />
												</td>
											</tr>
											<tr>
												<td align="right">
													职业状态：<strong>*</strong>
												</td>
												<td>
													<select name="paramMap.occStatus" id="occStatus" >
														<option value="" 
														<s:if test='#request.map.occStatus == ""'>selected="selected"</s:if>
														<s:else></s:else>
														>
															请选择
														</option>
														<option value="工薪阶层" 
														<s:if test='#request.map.occStatus == "工薪阶层"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															工薪阶层
														</option>
														<option value="私营企业主"
														<s:if test='#request.map.occStatus == "私营企业主"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															私营企业主
														</option>
														<option value="网络商家"
														<s:if test='#request.map.occStatus == "网络商家"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															网络商家
														</option>
														<option value="其他"
														<s:if test='#request.map.occStatus == "其他"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															其他
														</option>
													</select>
												</td>
											</tr>
											<tr>
												<td align="right">
													工作城市：<strong>*</strong>
												</td>
												<td>
													<s:select id="workPro" name="workPro"
														cssStyle="border-left:1px solid #dedede;border-top:1px solid #dedede;border-right:1px solid #dedede;border-bottom:1px solid #dedede;"
														list="#request.provinceList" listKey="regionId"
														listValue="regionName" headerKey="-1"
														headerValue="--请选择--" />
													<s:select id="workCity" name="cityId"
														cssStyle="border-left:1px solid #dedede;border-top:1px solid #dedede;border-right:1px solid #dedede;border-bottom:1px solid #dedede;"
														list="#request.cityList" listKey="regionId"
														listValue="regionName" headerKey="-1"
														headerValue="--请选择--" />

													
												</td>
											</tr>
											<tr>
												<td align="right">
													公司类别：<strong>*</strong>
												</td>
												<td>
													<select id="companyType" name="paramMap.companyType" >
														<option value=""
														<s:if test='#request.map.companyType == ""'>selected="selected"</s:if>
														<s:else></s:else>
														>
															请选择
														</option>
														<option value="事业单位"
															<s:if test='#request.map.companyType == "事业单位"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															事业单位
														</option>
														<option value="国家单位"
														<s:if test='#request.map.companyType == "国家单位"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															国家单位
														</option>
														<option value="央企(包括下级单位)"
															<s:if test='#request.map.companyType == "央企(包括下级单位)"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															央企(包括下级单位)
														</option>
														<option value="地方国资委直属企业"
															<s:if test='#request.map.occStatus == "地方国资委直属企业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															地方国资委直属企业
														</option>
														<option value="世界500强(包括合资企业及下级单位)"
															<s:if test='#request.map.companyType == "世界500强(包括合资企业及下级单位)"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															世界500强(包括合资企业及下级单位)
														</option>
														<option value="外资企业(包括合资企业)"
														
															<s:if test='#request.map.companyType == "外资企业(包括合资企业)"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															外资企业(包括合资企业)
														</option>
														<option value="一般上市公司(包括国外上市公司)"
														<s:if test='#request.map.companyType == "一般上市公司(包括国外上市公司)"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															一般上市公司(包括国外上市公司)
														</option>
														<option value="一般民营企业"
														<s:if test='#request.map.companyType == "一般民营企业"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															一般民营企业
														</option>
													</select>
												
												</td>
											</tr>
											<tr>
												<td align="right">
													公司行业：<strong>*</strong>
												</td>
												<td>
													<select id="companyLine" name="paramMap.companyLine" > 
														<option value="">
															请选择
														</option>
														<option value="农、林、牧、渔业"
															<s:if test='#request.map.companyLine == "农、林、牧、渔业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															农、林、牧、渔业
														</option>
														<option value="采矿业"
														<s:if test='#request.map.companyLine == "采矿业"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															采矿业
														</option>
														<option value="电力、热力、燃气及水的生产和供应业"
														<s:if test='#request.map.companyLine == "电力、热力、燃气及水的生产和供应业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															电力、热力、燃气及水的生产和供应业
														</option>
														<option value="制造业"
															<s:if test='#request.map.companyLine == "制造业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															制造业
														</option>
														<option value="教育"
															<s:if test='#request.map.companyLine == "教育"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															教育
														</option>
														<option value="环境和公共设施管理业"
															<s:if test='#request.map.companyLine == "环境和公共设施管理业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															环境和公共设施管理业
														</option>
														<option value="建筑业"
															<s:if test='#request.map.companyLine == "建筑业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															建筑业
														</option>
														<option value="交通运输、仓储业和邮政业"
														<s:if test='#request.map.companyLine == "交通运输、仓储业和邮政业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															交通运输、仓储业和邮政业
														</option>
															<option value="信息传输、计算机服务和软件业"
														<s:if test='#request.map.companyLine == "信息传输、计算机服务和软件业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															信息传输、计算机服务和软件业
														</option>
															<option value="批发和零售业"
														<s:if test='#request.map.companyLine == "批发和零售业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															批发和零售业
														</option>
															<option value="住宿、餐饮业"
														<s:if test='#request.map.companyLine == "住宿、餐饮业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															住宿、餐饮业
														</option>
															<option value="金融、保险业"
														<s:if test='#request.map.companyLine == "金融、保险业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															金融、保险业
														</option>
														
														<option value="房地产业"
														<s:if test='#request.map.companyLine == "房地产业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															房地产业
														</option>
																<option value="文化、体育、娱乐业"
														<s:if test='#request.map.companyLine == "文化、体育、娱乐业"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															文化、体育、娱乐业
														</option>
																<option value="综合（含投资类、主业不明显)"
														<s:if test='#request.map.companyLine == "综合（含投资类、主业不明显)"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															综合（含投资类、主业不明显)
														</option>
																	<option value="其它"
														<s:if test='#request.map.companyLine == "其它"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															其它
														</option>
													</select>
											
												</td>
											</tr>
											<tr>
												<td align="right">
													公司规模：<strong>*</strong>
												</td>
												<td>
													<select name="paramMap.companyScale" id="companyScale" >
														<option value="50人以下"
														<s:if test='#request.map.companyScale == "50人以下"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															50人以下
														</option>
														<option value="50-100人"
														<s:if test='#request.map.companyScale == "50-100人"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															50-100人
														</option>
														<option value="100-500人"
														<s:if test='#request.map.companyScale == "100-500人"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															100-500人
														</option>
														<option value="500人以上"
														<s:if test='#request.map.companyScale == "500人以上"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															500人以上
														</option>
													</select>
												
												</td>
											</tr>
											<tr>
												<td align="right">
													职 位：<strong>*</strong>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.job" 
														id="job"  value="${map.job }"/>
												
												</td>
											</tr>
											<tr>
												<td align="right">
													月收入：<strong>*</strong>
												</td>
												<td>
													<select name="paramMap.monthlyIncome" id="monthlyIncome" >
														<option value=""
														<s:if test='#request.map.monthlyIncome == ""'>selected="selected"</s:if>
														<s:else></s:else>
														>
															请选择
														</option>
														<option value="1000以下"
														<s:if test='#request.map.monthlyIncome == "1000以下"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															1000以下
														</option>
														<option value="1000-2000"
														<s:if test='#request.map.monthlyIncome == "1000-2000"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															1000-2000
														</option>
														<option value="2000-5000"
														
														<s:if test='#request.map.monthlyIncome == "2000-5000"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															2000-5000
														</option>
														<option value="5000-10000"
														<s:if test='#request.map.monthlyIncome == "5000-10000"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															5000-10000
														</option>
														<option value="10000-20000"
														<s:if test='#request.map.monthlyIncome == "10000-20000"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															10000-20000
														</option>
														<option value="20000以上"
														<s:if test='#request.map.monthlyIncome == "20000以上"'>selected="selected"</s:if>
														<s:else></s:else>
														
														>
															10000-20000
														</option>
													</select>
													
												</td>
											</tr>
											<tr>
												<td align="right">
													现单位工作年限：<strong>*</strong>
												</td>
												<td>
													<select name="paramMap.workYear" id="workYear" >
														<option value=""
														<s:if test='#request.map.workYear == ""'>selected="selected"</s:if>
														<s:else></s:else>
														>
															请选择
														</option>
														<option value="1年"
														<s:if test='#request.map.workYear == "1年"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															1年
														</option>
														<option value="1-3年"
														<s:if test='#request.map.workYear == "1-3年"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															1-3年
														</option>
														<option value="3-5年"
														<s:if test='#request.map.workYear == "3-5年"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															3-5年
														</option>
														<option value="5年以上"
														<s:if test='#request.map.workYear == "5年以上"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															5年以上
														</option>
													</select>
													
												</td>
											</tr>
											<tr>
												<td align="right">
													公司电话：<strong>*</strong>
												</td>
												<td>
													<input type="text" class="inp188" 
														name="paramMap.companyTel" id="companyTel" value="${map.companyTel }"/>
													
												</td>
											</tr>
											<tr>
												<td align="right">
													工作邮箱：<strong>*</strong>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.workEmail" 
														id="workEmail" value="${map.workEmail }"/>
												</td>
											</tr>
											<tr>
												<td align="right">
													公司地址：<strong>*</strong>
												</td>
												<td>
											
													<input type="text" class="inp188"
														name="paramMap.companyAddress" id="companyAddress"  value="${map.companyAddress }" />
															<a id="w3">
													<input type="button" value="审核通过" class="bcbtn"
														id="jc_yes" />
													<input type="button" value="不通过" class="bcbtn"
														id="jc_no" />
													<input type="hidden" value="${request.map.auditStatus}" id="gz"/>
											</a>
												</td>
											</tr>
												<tr>
												<td align="right">
												   <span style="color: red;">
												  <s:if test="#request.map.auditStatus==1">该项等待审核</s:if>
												  <s:elseif test="#request.map.auditStatus==2">该项审核失败</s:elseif>
												  <s:elseif test="#request.map.auditStatus==3">该项审核成功</s:elseif>
												  <s:else>该用户没有填写任何工作信息</s:else>
												  </span>
												</td>
												</tr>
											<tr>
												<th colspan="2" align="left">
													直系亲属联系人（在您贷款成功后，我们会通过电话核实您的紧急联系人信息）
												</th>
											</tr>
											<tr>
												<td align="right">
													姓 名：<strong>*</strong>
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.directedName" id="directedName" value="${map.directedName }" />
												</td>
											</tr>
											<tr>
												<td align="right">
													关 系：<strong>*</strong>
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.directedRelation" id="directedRelation" value="${map.directedRelation }" />
												</td>
											</tr>
											<tr>
												<td align="right">
													手 机：<strong>*</strong>
												</td>
												<td>
												
													<input type="text" class="inp188"
														name="paramMap.directedTel" id="directedTel" value="${map.directedTel }" />
														<a id="w4">
															<input type="button" value="审核通过" class="bcbtn"
														id="jc_yes1" />
													<input type="button" value="不通过" class="bcbtn"
														id="jc_no1" />
														<input type="hidden" value="${request.map.directedStatus}" id="zxqs"/>
														</a>
												</td>
											</tr>
											<tr>
												<td align="right">
												  <span style="color: red;">
												  <s:if test="#request.map.directedStatus==1">该项等待审核</s:if>
												  <s:elseif test="#request.map.directedStatus==2">该项审核失败</s:elseif>
												  <s:elseif test="#request.map.directedStatus==3">该项审核成功</s:elseif>
												   <s:else>该用户没有填写直系亲属联系人信息</s:else>
												  </span>
												</td>
												</tr>
											<tr>
												<th colspan="2" align="left">
													其他联系人
												</th>
											</tr>
											<tr>
												<td align="right">
													姓 名：<strong></strong>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.otherName"
														id="otherName"  value="${map.otherName }" />
												</td>
											</tr>
											<tr>
												<td align="right">
													关 系：<strong></strong>
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.otherRelation" id="otherRelation" value="${map.otherRelation }" />
												
												</td>
											</tr>
											<tr>
												<td align="right">
													手 机：<strong></strong>
												</td>
												<td>
												
													<input type="text" class="inp188" name="paramMap.otherTel"
														id="otherTel" value="${map.otherTel }"  />
															<a id="w1">
															<input type="button" value="审核通过" class="bcbtn"
														id="jc_yes2" />
													<input type="button" value="不通过" class="bcbtn"
														id="jc_no2" />
														<input type="hidden" value="${request.map.otherStatus}" id="other"/>
														</a>
												</td>
											</tr>
											
												<tr>
												<td align="right">
												    <span style="color: red;">
												  <s:if test="#request.map.otherStatus==1">该项等待审核</s:if>
												  <s:elseif test="#request.map.otherStatus==2">该项审核失败</s:elseif>
												  <s:elseif test="#request.map.otherStatus==3">该项审核成功</s:elseif>
												  <s:else>该用户没有填写其他联系人信息</s:else>
												  </span>
												</td>
											</tr>
											<tr>
												<th colspan="2" align="left">
													更多联系人
												</th>
											</tr>
											<tr>
												<td align="right">
													姓 名：<strong></strong>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.moredName"
														id="moredName" value="${map.moredName }" />
												</td>
											</tr>
											<tr>
												<td align="right">
													关 系：<strong></strong>
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.moredRelation" id="moredRelation" value="${map.moredRelation }" />
												</td>
											</tr>
											<tr>
												<td align="right">
													手 机：<strong></strong>
												</td>
												<td>
												
													<input type="text" class="inp188" name="paramMap.moredTel"
														id="moredTel" value="${map.moredTel }" />
														<a id="w2">
																	<input type="button" value="审核通过" class="bcbtn"
														id="jc_yes3" />
													<input type="button" value="不通过" class="bcbtn"
														id="jc_no3" />
														<input type="hidden" value="${request.map.moredStatus}" id="more"/>
												</a>
												</td>
											</tr>
											<tr align="center">
												<td align="right">
												 <span style="color: red;" >
												  <s:if test="#request.map.moredStatus==1">该项等待审核</s:if>
												  <s:elseif test="#request.map.moredStatus==2">该项审核失败</s:elseif>
												  <s:elseif test="#request.map.moredStatus==3">该项审核成功</s:elseif>
												  <s:else>该用户没有填写更多联系人信息</s:else>
												  </span>
												</td>
											</tr>
											<tr>
												<td align="right">
													&nbsp;
												</td>
											</tr>
												<tr>
												<td align="right"><br /><br /></td>
													<td>
													<a id="w5">
													<input type="button" value="修改并保存" class="bcbtn"
														id="uuu" />
														</a>
												</td>
											</tr>
										</table>
									</form>
								</div>
							</div>
							
							<!--  -->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	
		
		<script src="../script/jquery-1.7.1.min.js" type="text/javascript"></script>
   
   <script >
   if($("#gz").val()==3){
   		$("#jc_yes").hide();
   		$("#jc_no").val("取消审核")
   }
   if($("#zxqs").val()==3){
   		$("#jc_yes1").hide();
   		$("#jc_no1").val("取消审核")
   }
   if($("#other").val()==3){
   		$("#jc_yes2").hide();
   		$("#jc_no2").val("取消审核")
   }
   if($("#more").val()==3){
   		$("#jc_yes3").hide();
   		$("#jc_no3").val("取消审核")
   }
		//如果没有填写基本信息的时候 将所有
		var flag = '${flagw}';//标识用户时候填写了基本信息
		if(flag=="1"){
	       //将所有的输入框给禁用掉
	       $("#orgName").attr("disabled","true");
	       $("#occStatus").attr("disabled","true");
	       $("#workPro").attr("disabled","true");
	       $("#workCity").attr("disabled","true");
	       $("#companyType").attr("disabled","true");
	       $("#companyLine").attr("disabled","true");
	       $("#companyScale").attr("disabled","true");
	       $("#job").attr("disabled","true");
	       $("#workYear").attr("disabled","true");
	       $("#companyTel").attr("disabled","true");
	       $("#workEmail").attr("disabled","true");
	       $("#companyAddress").attr("disabled","true");
	       $("#directedName").attr("disabled","true");
	       $("#directedRelation").attr("disabled","true");
	       $("#directedTel").attr("disabled","true");  
	       $("#otherName").attr("disabled","true"); 
	       $("#otherRelation").attr("disabled","true"); 
	       $("#otherTel").attr("disabled","true"); 
	       $("#moredName").attr("disabled","true"); 
	       $("#moredRelation").attr("disabled","true"); 
	       $("#moredTel").attr("disabled","true"); 
	       $("#monthlyIncome").attr("disabled","true");
	       $("#w1").hide();
	       $("#w2").hide();
	       $("#w3").hide();
	       $("#w4").hide();
	       $("#w5").hide();
		}
		
		
		</script>
   
   

		<script>

			$(function(){
			
			    //样式选中
               $("#jk_hover").attr('class','nav_first');
	           $("#jk_hover div").removeClass('none');
				
				
			           //省份改变
						$("#workPro").change(function(){
			            var provinceId = $("#workPro").val();
			           citySelectInit(provinceId, 2);
		                	//$("#area").html("<option value='-1'>--请选择--</option>");
		               });
				
	
				
			});
			
			//城市跟随改变
				function citySelectInit(parentId, regionType){
		      var _array = [];
		_array.push("<option value='-1'>--请选择--</option>");
		var param = {};
		param["regionType"] = regionType;
		param["parentId"] = parentId;
		$.post("ajaxqueryRegionadmin.do",param,function(data){
			for(var i = 0; i < data.length; i ++){
				_array.push("<option value='"+data[i].regionId+"'>"+data[i].regionName+"</option>");
			}
			$("#workCity").html(_array.join(""));
		});
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
			//======================工作认证
			
	$("#uuu").click(function(){
	 if($("#orgName").val() ==""){
     alert('请填写单位名称');
     return ;
    }
    
   
	 if($("#occStatus").val() ==""){
     alert('请填写职业状态');
     return ;
    }
    if($("#workPro").val() ==""){
     alert('请填写工作省份');
     return ;
    }
    if($("#workCity").val() == '-1'){
     alert('请填写工作城市');
     return ;
    }
   if($("#companyType").val() ==""){
     alert('请填写公司类别');
     return ;
    }
       if($("#companyLine").val() ==""){
     alert('请填写公司行业');
     return ;
    }
   
    if($("# companyScale").val() ==""){
     alert('请填写公司规模');
     return ;
    }
   if($("#job").val() ==""){
     alert('请填写职位');
     return ;
    }
       if($("#monthlyIncome").val() ==""){
     alert('请填写月收入');
     return ;
    }
           if($("#workYear").val() ==""){
     alert('请填写现单位工作年限');
     return ;
    }
               if($("#workYear").val() ==""){
     alert('请填写现单位工作年限');
     return ;
    }
    
    
    
    
			
			      
     var mddd =/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
	   if(!mddd.test($('#companyTel').val())){
	       alert('请正确填写公司电话');
	       return false;
	    }
    
    
			if($("#workEmail").val()==''){
			   alert("工作邮箱不能为空");
			   return ;
		   	}
			if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test($("#workEmail").val())){
                 alert("请正确填写工作邮箱");
                 return ;
              }
       if($("#companyAddress").val() ==""){
     alert('请填写现公司地址');
     return ;
    }
          if($("#directedName").val() ==""){
     alert('请填写直系亲属联系人姓名');
     return ;
    }
   if($("#directedRelation").val() ==""){
     alert('请填写直系亲属联系人关系');
     return ;
    }
      
        if($("#directedTel").val() ==""){
        alert('请输入你的直系联系人手机号');
        return;
       }else if((!/^1[3,5,8]\d{9}$/.test($("#directedTel").val()))){ 
          alert('你输入的直系联系人手机号不正确');
            return;
        }
        
        
   /* if($("#otherName").val() ==""){
     alert('请填写其他联系人姓名');
     return ;
    }
   if($("#otherRelation").val() ==""){
     alert('请填写其他联系人关系');
     return ;
    }*/
        
        
        
        
        
        
           if($("#otherTel").val() !=""){
              if((!/^1[3,5,8]\d{9}$/.test($("#otherTel").val()))){ 
          alert('你输入的其他联系人手机号不正确');
            return;
           }
        }
        
        
   /* if($("#moredName").val() ==""){
     alert('请填写更多其他联系人姓名');
     return ;
    }
   if($("#moredRelation").val() ==""){
     alert('请填写更多其他联系人关系');
     return ;
    }*/
        
        
           if($("#moredTel").val() !=""){
        if((!/^1[3,5,8]\d{9}$/.test($("#moredTel").val()))){ 
          alert('你输入的更多联系人手机号不正确');
            return;
            }
        }
        
     
			               var param = {};
			               param["paramMap.uid"] = ${uid};
			               param["paramMap.orgName"]=$("#orgName").val();
			              param["paramMap.occStatus"]=$("#occStatus").val();
			             param["paramMap.workPro"]=$("#workPro").val();
			          param["paramMap.workCity"]=$("#workCity").val();
			           param["paramMap.companyType"]=$("#companyType").val();
			           param["paramMap.companyLine"]=$("#companyLine").val();
			            param["paramMap.companyScale"]=$("#companyScale").val();
			             param["paramMap.job"]=$("#job").val();
			              param["paramMap.monthlyIncome"]=$("#monthlyIncome").val();
			               param["paramMap.workYear"]=$("#workYear").val();
			                param["paramMap.companyTel"]=$("#companyTel").val();
			                 param["paramMap.workEmail"]=$("#workEmail").val();
			                  param["paramMap.companyAddress"]=$("#companyAddress").val();
			                  param["paramMap.directedName"]=$("#directedName").val();
			                  param["paramMap.directedRelation"]=$("#directedRelation").val();
			                  param["paramMap.directedTel"]=$("#directedTel").val();
			                   param["paramMap.otherName"]=$("#otherName").val();
			                    param["paramMap.otherRelation"]=$("#otherRelation").val();
			                     param["paramMap.otherTel"]=$("#otherTel").val();
			                      param["paramMap.moredName"]=$("#moredName").val();
			                      param["paramMap.moredRelation"]=$("#moredRelation").val();
			                      param["paramMap.moredTel"]=$("#moredTel").val();
			                      param["paramMap.moredName"]=$("#moredName").val();
			                    $.post("updateUserWorkDataAdmin.do",param,function(data){
			                          if(data.msg=="保存成功"){
			                            alert("保存成功");
			                            window.location.reload();
			                          }else if(data.msg=="querBaseData"){
					                            window.location.href='querBaseData.do';
					                  }else{
						                  alert(data.msg);
						               }
			                 /*             if(data.msg=="请正确填写公司名字"){
			                            alert("请正确填写单位名字");
			                          }
			                          if(data.msg=="真实姓名的长度为不小于2和大于50"){
			                            alert("真实姓名的长度为不小于2和大于50");
			                          }
			                              if(data.msg=="请填写职业状态"){
			                            alert("请填写职业状态");
			                          }
			                              if(data.msg=="请填写工作城市省份"){
			                            alert("请填写工作城市省份");
			                          }
			                              if(data.msg=="请填写工作城市"){
			                            alert("请填写工作城市");
			                          }
			                              if(data.msg=="直系人姓名长度为不小于2和大于50"){
			                            alert("直系人姓名长度为不小于2和大于50");
			                          }
			                          
			                          
			                            if(data.msg=="请填写公司类别"){
			                            alert("请填写公司类别");
			                          }
			                              if(data.msg=="请填写公司行业"){
			                            alert("请填写公司行业");
			                          }
			                              if(data.msg=="请填写公司规模"){
			                            alert("请填写公司规模");
			                          }
			                              if(data.msg=="请填写职位"){
			                            alert("请填写职位");
			                          }
			                              if(data.msg=="请填写月收入"){
			                            alert("请填写月收入");
			                          }
			                          
			                              if(data.msg=="请填写现单位工作年限"){
			                            alert("请填写现单位工作年限");
			                          }
			                              if(data.msg=="请正确填写公司电话"){
			                            alert("请正确填写公司电话");
			                          }
			                              if(data.msg=="请填写工作邮箱"){
			                            alert("请填写工作邮箱");
			                          }
			                              if(data.msg=="请填写公司地址"){
			                            alert("请填写公司地址");
			                          }
			                              if(data.msg=="请填写直系人姓名"){
			                            alert("请填写直系人姓名");
			                          }
			                              if(data.msg=="请填写直系人关系"){
			                            alert("请填写直系人关系");
			                          }
			                              if(data.msg=="请正确填写直系人电话"){
			                            alert("请正确填写直系人手机");
			                          }
			                              if(data.msg=="请填写其他人姓名"){
			                            alert("请填写其他人姓名");
			                          }
			                              if(data.msg=="请填写其他人关系"){
			                            alert("请填写其他人关系");
			                          }
			                             if(data.msg=="请正确填写其他人联系电话"){
			                            alert("请正确填写其他人联系手机");
			                          }
			                          if(data.msg=="moretel"){
			                            alert("请正确填写其他联系手机");
			                          }
			                             if(data.msg=="morename"){
			                            alert("请正确填写其他联系人名字");
			                          }
			                           if(data.msg=="morereation"){
			                            alert("请正确填写其他联系人关系");
			                          }
			                         if(data.msg=="请正确填写直系人电话长度错误"){
			                            alert("请正确填写直系人电话长度");
			                          }
			                         if(data.msg=="其他人姓名长度为不小于2和大于50"){
			                            alert(" 其他人姓名长度为不小于2和大于50");
			                          }
			                          if(data.msg=="更多联系人姓名长度为不小于2和大于50"){
			                            alert(" 其他人姓名长度为不小于2和大于50");
			                          } */
			                          
			                          
			                    });  
			
			});
		
		</script>
		<script>
		//审核
		     $("#jc_yes").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = 3;
                      param["paramMap.directedStatus"] = "";
                     param["paramMap.otherStatus"] = "";
                    param["paramMap.moredStatus"] = "";
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("审核成功");
	            window.location.reload();
	       }else{
	         alert("审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
	
	
	     $("#jc_no").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = 2;
                      param["paramMap.directedStatus"] = "";
                     param["paramMap.otherStatus"] = "";
                    param["paramMap.moredStatus"] = "";
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("取消审核成功");
	            window.location.reload();
	       }else{
	         alert("取消审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
	
	
		     $("#jc_yes1").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = "";
                      param["paramMap.directedStatus"] = 3;
                     param["paramMap.otherStatus"] = "";
                    param["paramMap.moredStatus"] = "";
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("审核成功");
	            window.location.reload();
	       }else{
	         alert("审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
	
			     $("#jc_no1").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = "";
                      param["paramMap.directedStatus"] = 2;
                     param["paramMap.otherStatus"] = "";
                    param["paramMap.moredStatus"] = "";
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("取消审核成功");
	            window.location.reload();
	       }else{
	         alert("取消审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
			     $("#jc_yes2").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = "";
                      param["paramMap.directedStatus"] = "";
                     param["paramMap.otherStatus"] = 3;
                    param["paramMap.moredStatus"] = "";
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("审核成功");
	            window.location.reload();
	       }else{
	         alert("审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
	
	
				     $("#jc_no2").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = "";
                      param["paramMap.directedStatus"] = "";
                     param["paramMap.otherStatus"] = 2;
                    param["paramMap.moredStatus"] = "";
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("取消审核成功");
	            window.location.reload();
	       }else{
	         alert("取消审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
		
				     $("#jc_yes3").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = "";
                      param["paramMap.directedStatus"] = "";
                     param["paramMap.otherStatus"] = "";
                    param["paramMap.moredStatus"] = 3;
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("审核成功");
	            window.location.reload();
	       }else{
	         alert("审核失败");
	             window.location.reload();
	       }
	    });
	});
	
	
					     $("#jc_no3").click(function(){
	   var param = {};
	   
	                 param["paramMap.userId"] = ${map.userId};
	                 param["paramMap.workauthId"] = ${map.id};
	                param["paramMap.auditStatus"] = "";
                      param["paramMap.directedStatus"] = "";
                     param["paramMap.otherStatus"] = "";
                    param["paramMap.moredStatus"] = 2;
	    $.post("updatework.do",param,function(data){
	       if(data==1){
	        alert("取消审核成功");
	            window.location.reload();
	       }else{
	         alert("取消审核失败");
	             window.location.reload();
	       }
	    });
	});
		
		
		</script>
	</body>
</html>
