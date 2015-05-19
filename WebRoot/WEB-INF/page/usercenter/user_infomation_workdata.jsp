<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="/include/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="common/date/calendar.css" />
		<script src="script/jquery-1.7.1.min.js" type="text/javascript"></script>
		<script type="text/javascript">
		 $(document).ready(function(){
		        $("#li_geren").click(function(){
		        var from = '${from}';
		            window.location.href='querBaseData.do?from='+from;
		        });
      
		               $("#li_work").click(function(){
		               var from = '${from}';
		           window.location.href='querWorkData.do?from='+from;
		        });
		        
		               $("#li_vip").click(function(){
		               var from = '${from}';
		       window.location.href='quervipData.do?from='+from;
		    });		        
               });
		</script>
		<script>		
		  $(function(){
		  if('${allworkmap.audi}'=='12'){
		  
		     $("#workPro").attr("disabled","true");
		     $("#workCity").attr("disabled","true");
		  }	  
		  });		
		</script>		
	</head>

	<body>
		<!-- 引用头部公共部分 -->
		<jsp:include page="/include/top.jsp"></jsp:include>
		<div class="nymain">
			<div class="bigbox" style="border:none">
				
				<div class="sqdk" style="background: none;">
					<div class="l-nav">
						<ul>
							<li class="on" style="background: none;border-bottom: #ddd 1px dashed;">
								<a href="querBaseData.do?from=${from}" style="background-image: none;"><span>step1 </span>基本资料</a>
							</li>
							<li style="background: none;border-bottom: #ddd 1px dashed;">
								<a href="portUserAcct.do" style="background-image: none;"><span>step2 </span>绑定账号</a>
							</li>
							<li style="background: none;border-bottom: #ddd 1px dashed;">
								<a href="userPassData.do?from=${from}" style="background-image: none;"><span>step3 </span>上传资料</a>
							</li>
							<%-- <li style="background: none;border-bottom: #ddd 1px dashed;">
								<a href="creditingInit.do?from=${from}" style="background-image: none;"><span>step4 </span>申请额度</a>
							</li> --%>
							<li style="background: none;border-bottom: #ddd 1px dashed;">
								<a href="addBorrowInit.do?t=${session.t}" style="background-image: none;"><span>step5 </span>发布贷款</a>
							</li>
						</ul>
				 <!-- 	<ul>
							<li class="on">
								<a><span>step1 </span> 基本资料</a>
							</li>
							 <li><a href="portUserAcct.do"><span>step2 </span>绑定账号</a></li>
							<li>
							<s:if test="#request.from != null && #request.from!='' && #session.user.authStep>=2">
							  <a href="userPassData.do?from=${from }"><span>step3 </span> 上传资料</a>
							</s:if>
							
							<s:else>
								<s:if test="#session.user.authStep>=4">
									<a href="userPassData.do"> <span>step3 </span>上传资料</a>
								</s:if>	
								<s:else>
								     <a><span>step3</span> 上传资料</a>
								</s:else>
							</s:else>
							</li>
							<li>
							<s:if test="#request.from != null && #request.from!='' && #session.user.authStep>=2">
								<s:if test="#session.stepMode ==1">
								     <a href="addBorrowInit.do?t=${session.borrowWay}"><span>step4 </span> 发布贷款</a>							  
								  </s:if>
								  <s:elseif test="#session.stepMode ==2">
								     <a href="creditingInit.do"><span>step4 </span> 申请额度</a>
								  </s:elseif>
								  <s:else>
								    <a href="addBorrowInit.do?t=0"><span>step4 </span> 发布贷款</a>	
								  </s:else>
							</s:if>
							<s:else>
								<s:if test="#session.user.authStep>=5">
								  <s:if test="#session.stepMode ==1">
								     <a href="addBorrowInit.do?t=${session.borrowWay}"><span>step4</span> 发布贷款</a>							  
								  </s:if>
								  <s:elseif test="#session.stepMode ==2">
								     <a href="creditingInit.do"><span>step4 </span> 申请额度</a>
								  </s:elseif>
								  <s:else>
								    <a href="addBorrowInit.do?t=0"><span>step4 </span> 发布贷款</a>	
								  </s:else>
								</s:if>	
								<s:else>
								  <s:if test="#session.stepMode ==1">
								       <a><span>step4 </span> 发布贷款</a>							  
								  </s:if>
								  <s:elseif test="#session.stepMode ==2">
								     <a><span>step4</span> 申请额度</a>
								  </s:elseif>
								</s:else>
							</s:else>
							</li>
						</ul>  -->
					</div>
					<div class="r-main">
						<div class="til01">
							<ul>
								<li  id="li_geren" >
									个人详细信息
								</li>
								<%-- <li  id="li_work" class="on">								
									<s:if test="#session.user.authStep>=2">
											工作认证信息
							      </s:if>	
							 <s:else>							     
							 </s:else>							
								</li> --%>
								<li  id="li_vip">
								<s:if test="#request.from != null && #request.from!='' && #session.user.authStep>=2">
									申请vip
								</s:if>
								<s:else>	
									  <s:if test="#session.user.authStep>=3">
												申请vip
								      </s:if>	
							     </s:else>
							 <s:else>						     
							 </s:else>							
								</li>
							</ul>
						</div>
						<div class="rmainbox">
						<!--  -->
						<!-- 基础资料div -->	
							<!-- 是工作认证资料 -->
							<div class="box01"  id="div_work">                        
								<p class="tips">
									<span class="fred">*</span> 为必填项，所有资料均会严格保密。
								</p>
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
												<strong>*</strong>		单位名称：
												</td>
												<td>
													<input type="text" name="paramMap.orgName" id="orgName"
														class="inp188" value="${map.orgName}" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>													
												</td>
											</tr>
											<tr>
												<td align="right">
												<strong>*</strong>	职业状态：
												</td>
												<td>
													<select name="paramMap.occStatus" id="occStatus" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>>
														<option value="">
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
													<strong>*</strong>工作城市：
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
													<strong>*</strong>公司类别：
												</td>
												<td>
													<select id="companyType" name="paramMap.companyType" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>>
														<option value="">
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
												<strong>*</strong>	公司行业：
												</td>
												<td>
													<select id="companyLine" name="paramMap.companyLine" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>>
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
													<strong>*</strong>公司规模：
												</td>
												<td>
													<select name="paramMap.companyScale" id="companyScale" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>>
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
													<strong>*</strong>职 位：
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.job"
														id="job" value="${map.job }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>													
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>月收入：
												</td>
												<td>
													<select name="paramMap.monthlyIncome" id="monthlyIncome" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>>
														<option value="" >
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
															20000以上
														</option>
													</select>
													
												</td>
											</tr>
											<tr>
												<td align="right"  width="20%">
													<strong>*</strong>现单位工作年限：
												</td>
												<td>
													<select name="paramMap.workYear" id="workYear" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>>
														<option value="">
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
												<strong>*</strong>	公司电话：
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.companyTel" id="companyTel"  value="${map.companyTel }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												格式：0755-29556666&nbsp;	
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>工作邮箱：
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.workEmail"
														id="workEmail" value="${map.workEmail }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
													
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>公司地址：
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.companyAddress" id="companyAddress" value="${map.companyAddress }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												
												</td>
											</tr>
											<tr>
												<th colspan="2" align="left">
													直系亲属联系人（在您贷款成功后，我们会通过电话核实您的紧急联系人信息）
												</th>
											</tr>
											<tr>
												<td align="right">
												<strong>*</strong>	姓 名：
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.directedName" id="directedName" value="${map.directedName }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>关 系：
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.directedRelation" id="directedRelation"  value="${map.directedRelation }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
													
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>手 机：
												</td>
												<td>
													<input type="text" class="inp188" 
														name="paramMap.directedTel" id="directedTel" value="${map.directedTel }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												
												</td>
											</tr>
											<tr>
												<th colspan="2" align="left">
													其他联系人
												</th>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>姓 名：
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.otherName"
														id="otherName"  value="${map.otherName }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												
												</td>
											</tr>
											<tr>
												<td align="right">
												<strong>*</strong>	关 系：
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.otherRelation" id="otherRelation" value="${map.otherRelation }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
													
												</td>
											</tr>
											<tr>
												<td align="right">
												<strong>*</strong>	手 机：
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.otherTel"
														id="otherTel" value="${map.otherTel }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												
												</td>
											</tr>
											<tr>
												<th colspan="2" align="left">
													更多其他联系人
												</th>
											</tr>
											<tr>
												<td align="right">
												<strong>*</strong>	姓 名：
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.moredName"
														id="moredName" value="${map.moredName }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												</td>
											</tr>
											<tr>
												<td align="right">
													<strong>*</strong>关 系：
												</td>
												<td>
													<input type="text" class="inp188"
														name="paramMap.moredRelation" id="moredRelation" value="${map.moredRelation }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												</td>
											</tr>
											<tr>
												<td align="right">
												<strong>*</strong>	手 机：
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.moredTel"
														id="moredTel" value="${map.moredTel }" <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>
												</td>
											</tr>
											<tr>
												<td align="right">&nbsp;
													
												</td>
												<td class="tishi">
													请确保您填写的资料真实有效，所有资料将会严格保密。
													<br />
													一旦被发现所填资料有虚假，将会影响您在富壹代的信用，对以后借款造成影响。
												</td>
											</tr>
											<tr>
												<td align="right">&nbsp;
													
												</td>
												<td style="padding-top: 20px;">
													<input type="button" value="保存并继续" class="bcbtn"
														id="work_btn"  <s:if test='#request.allworkmap.audi ==12'>disabled="disabled"</s:if>/>

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

		<!-- 引用底部公共部分 -->
		<jsp:include page="/include/footer.jsp"></jsp:include>

		<script type="text/javascript" src="common/date/calendar.js"></script>
		<script>

			$(function(){		
			    //样式选中
		   var sd=parseInt($(".l-nav").css("height"));
    var sdf=parseInt($(".r-main").css("height"));
	 $(".l-nav").css("height",sd>sdf?sd:sdf-15);
				//上传图片
				$("#btn_personalHead").click(function(){
					var dir = getDirNum();
					var json = "{'fileType':'JPG,BMP,GIF,TIF,PNG','fileSource':'user/"+dir+"','fileLimitSize':0.5,'title':'上传图片','cfn':'uploadCall','cp':'img'}";
					json = encodeURIComponent(json);
					 window.showModalDialog("uploadFileAction.do?obj="+json,window,"dialogWidth=500px;dialogHeight=400px");
					var headImgPath = $("#img").attr("src");
					if(headImgPath!=""){
						alert("上传成功！");	
						//window.location.href="updateHeadImg.do?userHeadImg="+headImgPath;	
					}else{ 
						alert("上传失败！");	
					}
				});
				
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
		$.post("ajaxqueryRegion.do",param,function(data){
			for(var i = 0; i < data.length; i ++){
				_array.push("<option value='"+data[i].regionId+"'>"+data[i].regionName+"</option>");
			}
			$("#workCity").html(_array.join(""));
		});
	}
			
			function uploadCall(basepath,fileName,cp){
		  		if(cp == "img"){
		  			var path = "upload/"+basepath+"/"+fileName;
					$("#img").attr("src",path);
					$("#setImg").attr("src",path);
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
			//======================工作认证
			
			$("#work_btn").click(function(){
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
			      
    if($("#companyTel").val() ==""){
     alert('请填写公司电话');
     return ;
    }else if((!/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/.test($("#companyTel").val()))){
     alert('请正确填写公司电话');
        return ;
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
        
        
    if($("#otherName").val() ==""){
     alert('请填写其他联系人姓名');
     return ;
    }
   if($("#otherRelation").val() ==""){
     alert('请填写其他联系人关系');
     return ;
    }
        
           if($("#otherTel").val() ==""){
        alert('请输入你的其他联系人手机号');
          return;
       }else if((!/^1[3,5,8]\d{9}$/.test($("#otherTel").val()))){ 
          alert('你输入的其他联系人手机号不正确');
            return;
        }       
        
    if($("#moredName").val() ==""){
     alert('请填写更多其他联系人姓名');
     return ;
    }
   if($("#moredRelation").val() ==""){
     alert('请填写更多其他联系人关系');
     return ;
    }
               
           if($("#moredTel").val() ==""){
        alert('请输入你的更多联系人手机号');
          return;
       }else if((!/^1[3,5,8]\d{9}$/.test($("#moredTel").val()))){ 
          alert('你输入的更多联系人手机号不正确');
            return;
        }
			               var param = {};
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
			                      //add by houli
			                      var btype = '${btype}';
			                    $.post("updatework.do",param,function(data){
			                         if(data.msg=="vip保存成功"){
			                            alert("保存成功");
			                            window.location.href='userPassData.do?btype='+btype;
			                         }
			                          if(data.msg=="保存成功"){
			                            alert("保存成功");
			                            window.location.href='quervipData.do?btype='+btype;
			                          }
			                              if(data.msg=="请正确填写公司名字"){
			                            alert("请正确填写单位名字");
			                          }
			                          if(data.msg=="22"){
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
			                          } 
			                          if(data.msg=="querBaseData"){
			                            window.location.href='querBaseData.do';
			                          }
			                          
			                    });  			
			});
		</script>
	</body>
</html>
