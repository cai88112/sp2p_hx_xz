<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	
		<link rel="stylesheet" type="text/css" href="../common/date/calendar.css" />
		<link rel="stylesheet" type="text/css" href="../css/admin/admin_css.css" />
		<link rel="stylesheet" type="text/css" href="../css/css.css" />
		<script src="../script/jquery-1.7.1.min.js" type="text/javascript"></script>
		<script language="javascript" type="text/javascript" src="../My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
		    $(function(){
		       $("#li_work").click(function(){
		        window.location.href='queryPersonworkmsg.do?uid=${map.userId}';
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
								<a href="javascript:void(0);">基本资料审核</a>
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
		<!--=======================-->
		<div class="nymain" style="margin-top: 0px;">
			<div class="bigbox">
			
				<div class="sqdk" style="background: none; padding:0px">
				
					<div class="r-main" style="margin-left:100px">
						<div class="til01">
							<ul>
								<li class="on" id="li_geren">
									个人详细信息
								</li>
								<li  id="li_work">
										
											工作认证信息
					
								</li>
					
							</ul>
						</div>
						<div class="rmainbox">
						<!-- 基础资料div -->
							<div class="box01" id="jibenziliao">
							
								<div class="tab">
									<form id="BaseDataform">
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td align="right">
													真实姓名：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.realName"
														id="realName" value="${map.realName}" />
													<span style="color: red; float: none;" id="u_realName"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													身份证号：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.idNo"
														id="idNo" value="${map.idNo }" />
													<span style="color: red; float: none;" id="u_idNo"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													手机号码：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.cellPhone"
														id="cellPhone" value="${map.cellPhone }" />
													<span style="color: red; float: none;" id="u_cellPhone"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													性 别：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio"  id="sex" value="男"
														name="paramMap_sex"  class="sex"
														<s:if test='#request.map.sex == "男"'>checked="checked"</s:if>
												<s:else></s:else> 
													onclick="javascript:$('#u_sex').html('')"	 />
													男
													<input type="radio"  id="sex" value="女"
														name="paramMap_sex" class="sex"
														<s:if test='#request.map.sex == "女"'>checked="checked"</s:if>
												<s:else></s:else> 
												
												
													<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else> 
												
													onclick="javascript:$('#u_sex').html('')"	
														/>
													女
													<span style="color: red; float: none;" id="u_sex"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													出生日期：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188 Wdate" name="paramMap.birthday"
													
													
													 value="${birth }"
													 
													 
													 
													id="birthday"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:'readOnly'})"  
													 />
													<span style="color: red; float: none;" id="u_birthday"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													最高学历：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<select name="paramMap.highestEdu" id="highestEdu" onchange="javascript:if($('#highestEdu').val()!=''){$('#u_highestEdu').html('');} " >
														<option value="">
													请选择
														</option>
														<option value="高中或以下"
														<s:if test='#request.map.highestEdu == "高中或以下"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															高中或以下
														</option>
														<option value="大专"
															<s:if test='#request.map.highestEdu == "大专"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															大专
														</option>
														<option value="本科"
															<s:if test='#request.map.highestEdu == "本科"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															本科
														</option>
														<option value="研究生或以上"
															<s:if test='#request.map.highestEdu == "研究生或以上"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															研究生或以上
														</option>
														<option value="其他"
															<s:if test='#request.map.highestEdu == "其他"'>selected="selected"</s:if>
														<s:else></s:else>
														>
															其他
													</option>
													</select>
													<span style="color: red; float: none;" id="u_highestEdu"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													入学年份：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188 Wdate"
														name="paramMap.eduStartDay" id="eduStartDay"
														onclick="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:'readOnly'})" value="${rxedate }" />
													<!--  
    <select name="paramMap.eduStartDay" id="eduStartDay" >
      <option value="">请选择</option>
    </select>
    -->


													<span style="color: red; float: none;" id="u_eduStartDay"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													毕业院校：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.school"
														id="school" value="${map.school }" />
													<span style="color: red; float: none;" id="u_school"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													婚姻状况：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio" name="paramMap_maritalStatus"
														id="maritalStatus" value="已婚" 
														<s:if test='#request.map.maritalStatus == "已婚"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_maritalStatus').html('')"
														
														/>
													已婚
													<input type="radio" name="paramMap_maritalStatus"
														id="maritalStatus" value="未婚" 
															<s:if test='#request.map.maritalStatus == "未婚"'>checked="checked"</s:if>
														<s:else></s:else> 
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_maritalStatus').html('')"
														/>
													未婚
													<span style="color: red; float: none;" id="u_maritalStatus"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													有无子女：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio" name="paramMap_hasChild" id="hasChild"
														value="有"  
															<s:if test='#request.map.hasChild == "有"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_hasChild').html('')"
														 />
													有
													<input type="radio" name="paramMap_hasChild" id="hasChild"
														value="无"  
															<s:if test='#request.map.hasChild == "无"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_hasChild').html('')"
														/>
													无
													<span style="color: red; float: none;" id="u_hasChild"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													是否有房：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio" name="paramMap_hasHourse"
														id="hasHourse" value="有"  
																<s:if test='#request.map.hasHourse == "有"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_hasHourse').html('')"
														/>
													有
													<input type="radio" name="paramMap_hasHourse"
														id="hasHourse" value="无"  
																<s:if test='#request.map.hasHourse == "无"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_hasHourse').html('')"
														/>
													无
													<span style="color: red; float: none;" id="u_hasHourse"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													有无房贷：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio" name="paramMap_hasHousrseLoan"
														id="hasHousrseLoan" value="有"  
																<s:if test='#request.map.hasHousrseLoan == "有"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_hasHousrseLoan').html('')"
														/>
													有
													<input type="radio" name="paramMap_hasHousrseLoan"
														id="hasHousrseLoan" value="无"  
															<s:if test='#request.map.hasHousrseLoan == "无"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
														onclick="javascript:$('#u_hasHousrseLoan').html('')"
														 />
													无
													<span style="color: red; float: none;"
														id="u_hasHousrseLoan" class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													是否有车：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio" name="paramMap_hasCar" id="hasCar"
														value="有"  
															<s:if test='#request.map.hasCar == "有"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
															onclick="javascript:$('#u_hasCar').html('')"
														/>
													有
													<input type="radio" name="paramMap_hasCar" id="hasCar"
														value="无" 
														<s:if test='#request.map.hasCar == "无"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
															onclick="javascript:$('#u_hasCar').html('')"
														 />
													无
													<span style="color: red; float: none;" id="u_hasCar"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													有无车贷：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="radio" name="paramMap_hasCarLoan"
														id="hasCarLoan" value="有" 
														<s:if test='#request.map.hasCarLoan == "有"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
															onclick="javascript:$('#u_hasCarLoan').html('')"
														/>
													有
													<input type="radio" name="paramMap_hasCarLoan"
														id="hasCarLoan" value="无"  
															<s:if test='#request.map.hasCarLoan == "无"'>checked="checked"</s:if>
														<s:else></s:else>
															<s:if test='#request.flag == "1"'>disabled="disabled"</s:if>
												<s:else></s:else>
															onclick="javascript:$('#u_hasCarLoan').html('')"
														/>
													无
													<span style="color: red; float: none;" id="u_hasCarLoan"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													籍 贯：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<s:select id="province" name="workPro"   
														cssStyle="border-left:1px solid #dedede;border-top:1px solid #dedede;border-right:1px solid #dedede;border-bottom:1px solid #dedede;"
														list="#request.provinceList" listKey="regionId"
														listValue="regionName" headerKey="-1"
														headerValue="--请选择--" onchange="javascript:if($('#province').val()!=-1){$('#u_Pro_City').html('')}"/>
													<s:select id="city" name="cityId"
														cssStyle="border-left:1px solid #dedede;border-top:1px solid #dedede;border-right:1px solid #dedede;border-bottom:1px solid #dedede;"
														list="#request.cityList" listKey="regionId"
														listValue="regionName" headerKey="-1"
														headerValue="--请选择--"   onchange="javascript:if($('#city').val()!=-1){$('#u_Pro_City').html('')}"/>

													<span style="color: red; float: none;" id="u_Pro_City"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													户口所在地：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<s:select id="registedPlacePro" 
														name="regPro"
														cssStyle="border-left:1px solid #dedede;border-top:1px solid #dedede;border-right:1px solid #dedede;border-bottom:1px solid #dedede;"
														list="#request.provinceList" listKey="regionId"
														listValue="regionName" headerKey="-1"
														headerValue="--请选择--"  onchange="javascript:if($('#registedPlacePro').val()!=-1){$('#u_reg_Pro_City').html('')}"/>
													<s:select id="registedPlaceCity"
														name="regCity"
														cssStyle="border-left:1px solid #dedede;border-top:1px solid #dedede;border-right:1px solid #dedede;border-bottom:1px solid #dedede;"
														list="#request.regcityList" listKey="regionId"
														listValue="regionName" headerKey="-1"
														headerValue="--请选择--"   onchange="javascript:if($('#registedPlaceCity').val()!=-1){$('#u_reg_Pro_City').html('')}"/>
													<span style="color: red; float: none;" id="u_reg_Pro_City"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													居住地址：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.address"
														id="address" 
														value="${ map.address}" 
														/>
													<span style="color: red; float: none;" id="u_address"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right">
													居住电话：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="text" class="inp188" name="paramMap.telephone"
														id="telephone" value="${map.telephone}" />
													<span style="color: red; float: none;" id="u_telephone"
														class="formtips"></span>
												</td>
											</tr>
											<tr>
												<td align="right" id="personalHead">
													个人头像：<span style="color: red; float: none;" 
														class="formtips">*</span>
												</td>
												<td>
													<input type="button" id="btn_personalHead" class="scbtn"
														style="cursor: pointer;" value="点击上传" />
												</td>
											</tr>
											<tr>
												<td align="right">
													&nbsp;
												</td>
												<td class="tx">
                                                     
                                                  <s:if test="#request.map.personalHead==null">	
                                                  	<img id="img" src="../${headImg}" width="62" height="62"
														name="paramMap.personalHead" /></s:if>
                                                    <s:elseif test="#request.map.personalHead!=null">
                                                    	<img id="img" src="../${map.personalHead}" width="62" height="62"
														name="paramMap.personalHead" />
                                                    </s:elseif>
                                                    <s:else>
                                                      <img id="img" src="../${headImg}" width="62" height="62"
														name="paramMap.personalHead" />
                                                    </s:else>
                                                    <!--  
													<img id="img" src="${headImg}" width="62" height="62"
														name="paramMap.personalHead" />
													 -->
													<!-- - 
    	<img id="img" src="../images/NoImg.GIF" width="300px" height="300px" />
    - -->
        											<img id="setImg" src="${map.personalHead}" style="display: none" width="10px" height="10px"/>
												</td>
											</tr>
											<tr>
												<td align="right">
													&nbsp;
												</td>
												<td class="tishi">
												
												</td>
											</tr>
											
											<tr>
												<td align="right">
													&nbsp;
												</td>
											</tr>
											<tr>
												<td align="right">
													&nbsp;
												</td>
												<td style="padding-top: 20px;">
												<span style="color: red;" id="msg">
												  <s:if test="#request.map.auditStatus==1">该项等待审核</s:if>
												  <s:elseif test="#request.map.auditStatus==2">该项审核未通过</s:elseif>
												  <s:elseif test="#request.map.auditStatus==3">该项审核已通过</s:elseif>
												  <s:else>该用户没有填写任何基本资料信息,无法进行审核</s:else>
												  </span>
												  <a href="javascript:void(0)" id="h">
													<input type="button" value="审核通过" class="bcbtn"
														id="jc_yes"/>
												  	
															<input type="button" value="审核不通过" class="bcbtn"
														id="jc_no" />
															<input type="button" value="修改并保存" class="bcbtn"
														id="jc_btn" />
														<input type="hidden" value="${request.map.auditStatus}" id="hh"/>
													</a>
												</td>
											</tr>
										</table>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
				</div>
			</div>

		<!-- 引用底部公共部分 -->
		<script type="text/javascript" src="../script/nav-jk.js"></script>
		<script type="text/javascript" src="../common/date/calendar.js"></script>
		<script src="../script/jquery-1.7.1.min.js" type="text/javascript"></script>
		
		<script >
		if($("#hh").val() == 3){
			 $("#jc_yes").hide(); 
			 $("#jc_no").val("取消审核");
		}
		//如果没有填写基本信息的时候 将所有
		var flag = '${flag}';//标识用户时候填写了基本信息
		if(flag=="1"){
	       //将所有的输入框给禁用掉
	       $("#realName").attr("disabled","true");
	       $("#idNo").attr("disabled","true");
	       $("#cellPhone").attr("disabled","true");
	       $("#birthday").attr("disabled","true");
	       $("#highestEdu").attr("disabled","true");
	       $("#eduStartDay").attr("disabled","true");
	       $("#school").attr("disabled","true");
	       $("#maritalStatus").attr("disabled","true");
	       $("#hasChild").attr("disabled","true");
	       $("#hasHourse").attr("disabled","true");
	       $("#hasHousrseLoan").attr("disabled","true");
	       $("#hasCar").attr("disabled","true");
	       $("#hasCarLoan").attr("disabled","true");
	       $("#province").attr("disabled","true");
	       $("#city").attr("disabled","true");  
	       $("#registedPlacePro").attr("disabled","true"); 
	       $("#registedPlaceCity").attr("disabled","true"); 
	       $("#address").attr("disabled","true"); 
	       $("#telephone").attr("disabled","true"); 
	       $("#btn_personalHead").attr("disabled","true"); 
	        $("#h").hide();
		}
		
		
		</script>
		
		<script>
$(document).ready(function(){
    $("#BaseDataform :input").blur(function(){
    //验证手机号码
      if($(this).attr("id")=="cellPhone"){
      if( $(this).val() ==""){
         $("#u_cellPhone").attr("class", "formtips onError");
		 $("#u_cellPhone").html("请填写手机号码！");
      }else if((!/^1[3,5,8]\d{9}$/.test($("#cellPhone").val()))){ 
       $("#u_cellPhone").attr("class", "formtips onError");
	     $("#u_cellPhone").html("请正确填写手机号码！");
      }else{
           $("#u_cellPhone").attr("class", "formtips");
	       $("#u_cellPhone").html(""); 
      }
  }
  //真实姓名
  		if($(this).attr("id")=="realName"){  
				if($(this).val() ==""){
			      	$("#u_realName").attr("class", "formtips onError");
			      	$("#u_realName").html("请填写真实姓名！");
			    }else if($(this).val().length <2 || $(this).val().length> 20){   
	            	$("#u_realName").attr("class", "formtips onError");
	                $("#u_realName").html("名字长度为2-20个字符"); 
	            }else{   
	            	$("#u_realName").attr("class", "formtips");
	                $("#u_realName").html(""); 
	            } 
          }
  //========
  //身份号码
  if($(this).attr("id")=="idNo"){
     var isIDCard1 = new Object();
     var isIDCard2 = new Object();
     //身份证正则表达式(15位) 
     isIDCard1=/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/; 
     //身份证正则表达式(18位) 
     isIDCard2=/^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])((\d{4})|\d{3}(x|X))$/; 
    if($(this).val() ==""){
       //$("#u_idNo").attr("class", "formtips onError");
      $("#u_idNo").html("请填写身份证号码");
    }else if(isIDCard1.test($(this).val())||isIDCard2.test($(this).val())){
        $("#u_idNo").html("");
    }else {
        $("#u_idNo").html("身份证号码不正确");
    
      //===
    }
  }
  //========== 验证出生年月
    if($(this).attr("id")=="birthday"){
    if($(this).val() ==""){
    $("#u_birthday").attr("class", "formtips onError");
    $("#u_birthday").html("请填写出生年月");
    }else if(!(!/^[1,2][0-9]\d{2}$/.test($("#birthday").val()))){
    $("#u_birthday").attr("class", "formtips onError");
      $("#u_birthday").html("年份格式错误");
    }else{
    $("#u_birthday").attr("class", "formtips");
       $("#u_birthday").html("");
    }
  }
  //===========验证毕业院校
      if($(this).attr("id")=="school"){
    if($(this).val() ==""){
    $("#u_school").attr("class", "formtips onError");
    $("#u_school").html("请填写毕业院校");
    }else{
    $("#u_school").attr("class", "formtips");
       $("#u_school").html("");
    }
  }
 //==手机验证码
 /*
       if($(this).attr("id")=="vilidataNum"){
    if($(this).val() ==""){
    $("#u_vilidataNum").attr("class", "formtips onError");
    $("#u_vilidataNum").html("请填写手机验证码");
    }else{
    $("#u_vilidataNum").attr("class", "formtips");
       $("#u_vilidataNum").html("");
    }
  }
  */
 //=========最高学历
        if($(this).attr("id")=="highestEdu"){
    if($(this).val() ==""){
    $("#u_highestEdu").attr("class", "formtips onError");
    $("#u_highestEdu").html("请填写最高学历");
    }else{
    $("#u_highestEdu").attr("class", "formtips");
       $("#u_highestEdu").html("");
    }
  }
 //============入学年份
        if($(this).attr("id")=="eduStartDay"){
    if($(this).val() ==""){
    $("#u_eduStartDay").attr("class", "formtips onError");
    $("#u_eduStartDay").html("请填写入学年份");
    }else{
    $("#u_eduStartDay").attr("class", "formtips");
       $("#u_eduStartDay").html("");
    }
  }
 //=====籍　　贯

    if($(this).attr("id")=="nativePlacePro"){
    if($(this).val() ==""){
    $("#u_Pro_City").attr("class", "formtips onError");
    $("#u_Pro_City").html("请填写籍贯");
    }else{
    $("#u_Pro_City").attr("class", "formtips");
       $("#u_Pro_City").html("");
    }
  }
         if($(this).attr("id")=="nativePlaceCity"){
    if($(this).val() ==""){
    $("#u_Pro_City").attr("class", "formtips onError");
    $("#u_Pro_City").html("请填写籍贯");
    }else{
    $("#u_Pro_City").attr("class", "formtips");
       $("#u_Pro_City").html("");
    }
  }

 //======户口所在地
       if($(this).attr("id")=="registedPlacePro"){
    if($(this).val() ==""){
    $("#u_reg_Pro_City").attr("class", "formtips onError");
    $("#u_reg_Pro_City").html("请填写户口所在地");
    }else{
    $("#u_reg_Pro_City").attr("class", "formtips");
       $("#u_reg_Pro_City").html("");
    }
  }
         if($(this).attr("id")=="registedPlaceCity"){
    if($(this).val() ==""){
    $("#u_reg_Pro_City").attr("class", "formtips onError");
    $("#u_reg_Pro_City").html("请填写户口所在地");
    }else{
    $("#u_reg_Pro_City").attr("class", "formtips");
       $("#u_reg_Pro_City").html("");
    }
  }
 
 //============居住地址
        if($(this).attr("id")=="address"){
    if($(this).val() ==""){
    $("#u_address").attr("class", "formtips onError");
    $("#u_address").html("请填写居住地址");
    }else{
    $("#u_address").attr("class", "formtips");
       $("#u_address").html("");
    }
  }
 //================居住电话
        if($(this).attr("id")=="telephone"){
    if($(this).val() ==""){
    $("#u_telephone").attr("class", "formtips onError");
    $("#u_telephone").html("请填写居住电话");
    }else if((!/^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/.test($(this).val()))){
       $("#u_telephone").attr("class", "formtips onError");
       $("#u_telephone").html("请填写正确的居住电话");
    }else{
    $("#u_telephone").attr("class", "formtips");
       $("#u_telephone").html("");
    }
  }
 //============
     });
      
     		$("#province").change(function(){
			var provinceId = $("#province").val();
			citySelectInit(provinceId, 2);
			//$("#area").html("<option value='-1'>--请选择--</option>");
		});
			 $("#registedPlacePro").change(function(){
			var provinceId = $("#registedPlacePro").val();
			registedPlaceCity(provinceId, 2);
			//$("#area").html("<option value='-1'>--请选择--</option>");
		});
		
     

});

	function registedPlaceCity(parentId, regionType){
		var _array = [];
		_array.push("<option value='-1'>--请选择--</option>");
		var param = {};
		param["regionType"] = regionType;
		param["parentId"] = parentId;
		$.post("ajaxqueryRegionadmin.do",param,function(data){
			for(var i = 0; i < data.length; i ++){
				_array.push("<option value='"+data[i].regionId+"'>"+data[i].regionName+"</option>");
			}
			$("#registedPlaceCity").html(_array.join(""));
		});
	}




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
			$("#city").html(_array.join(""));
		});
	}
	
	
	//提交基础资料
	  $("#jc_btn").click(function(){
	  
	     if($("#realName").val()==""){
	    // $("#u_realName").html("请填写真实姓名！");
	     alert("请填写真实姓名！");
	     return false 
	     }
	     if($("#idNo").val()==""){
	    // $("#u_idNo").html("请填写身份号码！");
	     alert("请填写身份号码！");
	     return false
	      }
	     if($("#cellPhone").val()==""){
	     //$("#u_cellPhone").html("请填写手机号码！");
	      alert("请填写手机号码！");
	     return false 
	     }

         if(($("input[name='paramMap_sex']:checked").val())==undefined||($("input[name='paramMap_sex']:checked").val())==""){
	      //$("#u_sex").html("请填写你的性别");
	       alert("请勾选你的性别");
	      return false;
	      }
	     if($("#birthday").val()==""){ 
	     //$("#u_birthday").html("请填写出生年月");
	       alert("请填写出生年月!");
	     return false
	      }
	     if($("#highestEdu").val()==""){
	     //$("#u_highestEdu").html("请填写最高学历！");
	      alert("请填写最高学历！");
	      return false 
	     }
	     if($("#eduStartDay").val()==""){
	     //$("#u_eduStartDay").html("请填写入学年份！");
	     alert("请填写入学年份！");
	     return false 
	     }
	     if($("#school").val()==""){
	     //$("#u_school").html("请填写毕业院校!");
	     alert("请填写毕业院校!");
	     return false 
	     }
	     if(($("input[name='paramMap_maritalStatus']:checked").val())==undefined||($("input[name='paramMap_maritalStatus']:checked").val())==""){
	      //$("#u_maritalStatus").html("请填写你结婚状态");
	      alert("请填写你的结婚状态!");
	      return false;
	     }
	      if(($("input[name='paramMap_hasChild']:checked").val())==undefined||($("input[name='paramMap_hasChild']:checked").val())==""){
	     // $("#u_hasChild").html("请填写你是否有孩子");
	      alert("请填写你是否有孩子");
	      return false;
	    }
	        if(($("input[name='paramMap_hasHourse']:checked").val())==undefined||($("input[name='paramMap_hasHourse']:checked").val())==""){
	      //$("#u_hasHourse").html("请填写你是否有房子");
	      alert("请填写你是否有房子");
	      return false;
	    }
	        if(($("input[name='paramMap_hasHousrseLoan']:checked").val())==undefined||($("input[name='paramMap_hasHousrseLoan']:checked").val())==""){
	    //  $("#u_hasHousrseLoan").html("请填写你是否有房贷");
	       alert("请填写你是否有房贷");
	      return false;
	    }
	     if(($("input[name='paramMap_hasCar']:checked").val())==undefined||($("input[name='paramMap_hasCar']:checked").val())==""){
	      //$("#u_hasCar").html("请填写你是否有车");
	        alert("请填写你是否有车");
	      return false;
	    }
	     if(($("input[name='paramMap_hasCarLoan']:checked").val())==undefined||($("input[name='paramMap_hasCarLoan']:checked").val())==""){
	     // $("#u_hasCarLoan").html("请填写你是否有车贷");
	      alert("请填写你是否有车贷");
	      return false;
	    }
	    
	    if($('#province').val()==-1){
	    //$('#u_Pro_City').html('请选择你的省份');
	     alert("请选择你的籍贯省份");
	     return false;
	     }
	    
	     if($('#city').val()==-1){
	    //$('#u_Pro_City').html('请选择你的城市') ;
	     alert("请选择你的籍贯城市");
	     return false;
	     }
	    if($('#registedPlacePro').val()==-1){
	    //$('#u_reg_Pro_City').html('请选择你的省份');
	     alert("请选择你的户口省份");
	     return false;}
	  if($('#registedPlaceCity').val()==-1){
	 // $('#u_reg_Pro_City').html('请选择你的城市');
	     alert("请选择你的户口城市");
	    return false;
	   }
	   
	 if($('#address').val()==""){
	     alert("请填写居住地址");
	    return false;
	   }
	   
	 if($('#telephone').val()==""){
	     alert("请填写居住电话");
	    return false;
	   }
	   
	   if($("#img").attr("src")==""){
	       alert("请上传你的个人头像");
	       return false;
	   }

	    var param = {};
	    param["paramMap.ui"] = ${map.userId};
	    param["paramMap.realName"]=$("#realName").val();
	    param["paramMap.idNo"]=$("#idNo").val();
	    param["paramMap.cellPhone"]=$("#cellPhone").val();
	    param["paramMap.sex"]=$("input[name='paramMap_sex']:checked").val();
	    param["paramMap.birthday"]=$("#birthday").val();
	    param["paramMap.highestEdu"]=$("#highestEdu").val();
	    param["paramMap.eduStartDay"]=$("#eduStartDay").val();
	    param["paramMap.school"]=$("#school").val();
	    param["paramMap.maritalStatus"]=$("input[name='paramMap_maritalStatus']:checked").val();
	    param["paramMap.hasChild"]=$("input[name='paramMap_hasChild']:checked").val();
	    param["paramMap.hasHourse"]=$("input[name='paramMap_hasHourse']:checked").val();
	    param["paramMap.hasHousrseLoan"]=$("input[name='paramMap_hasHousrseLoan']:checked").val();
	    param["paramMap.hasCar"]=$("input[name='paramMap_hasCar']:checked").val();
	    param["paramMap.hasCarLoan"]=$("input[name='paramMap_hasCarLoan']:checked").val();
	    param["paramMap.nativePlacePro"]=$("#province").val();
	    param["paramMap.nativePlaceCity"]=$("#city").val();
	   
	    param["paramMap.registedPlacePro"]=$("#registedPlacePro").val();
	    param["paramMap.registedPlaceCity"]=$("#registedPlaceCity").val();
	    
	    param["paramMap.address"]=$("#address").val();
	    param["paramMap.telephone"]=$("#telephone").val();
	    //用户头像路径
	    param["paramMap.personalHead"]=$("#setImg").attr("src");
	    $.post("updateUserBaseDataAdmin.do",param,function(data){
	       if(data.msg=="保存成功"){
	         alert("保存成功");
	         window.location.reload();
	       }else{
	         //alert("请正确填写基本资料");
	         if(data.msg=="请正确填写真实名字"){
	         //$("#u_realName").html("请填写真实姓名！")
	         alert("请填写真实姓名！");
	       }
	            if(data.msg=="真实姓名的长度为不小于2和大于20"){
	            //$("#u_realName").html("真实姓名的长度为不小于2和大于20！")
	             alert("真实姓名的长度为不小于2和大于20！");
	       }
	            if(data.msg=="请正确身份证号码"){
	        // $("#u_idNo").html("请正确身份证号码！")
	        alert("请正确身份证号码");
	       }
	            if(data.msg=="请正确填写手机号码"){
	           // $("#u_cellPhone").html("请填写手机号码！");
	              alert("请填写手机号码!");
	       }
	       if(data.msg=="手机号码长度不对"){
	        // $("#u_cellPhone").html("手机号码长度不对！")
	         alert("手机号码长度不对！");
	       }
	       
 
	       
	       
	       
	       }
	       if(data.msg=="请正确填写手机号码"){
	        // $("#u_cellPhone").html("手机号码长度不对！")
	         alert("手机验证码填写错误！");
	       }
	       
	      if(data.msg=="请正确填写性别"){
	        // $("#u_cellPhone").html("手机号码长度不对！")
	         alert("请勾选填写性别！");
	       }
	       if(data.msg=="请正确填写出生日期"){
	         //$("#u_birthday").html("请正确填写出生日期！")
	         alert("请正确填写出生日期！");
	       }
	       if(data.msg=="请正确填写入学年份"){
	        // $("#u_eduStartDay").html("请正确填写入学年份！")
	         alert("请正确填写入学年份！");
	       }
	             if(data.msg=="请正确填写入毕业院校"){
	        // $("#u_school").html("请正确填写入毕业院校！")
	          alert("请正确填写入毕业院校！");
	       }
	             if(data.msg=="请正确填写入学年份"){
	       //  $("#u_eduStartDay").html("请正确填写入学年份！")
	        alert("请正确填写入学年份！");
	       }
	        if(data.msg=="请正确填写最高学历"){
	         //$("#u_highestEdu").html("请正确填写最高学历！")
	          alert("请正确填写最高学历！");
	       }
	         if(data.msg=="请正确填写入籍贯省份"){
	        // $("#u_nativePlacePro").html("请正确填写入籍贯省份！")
	         alert("请正确填写入籍贯省份！");
	       }
	       if(data.msg=="请正确填写入籍贯城市"){
	         //$("#u_nativePlaceCity").html("请正确填写入籍贯城市！")
	            alert("请正确填写入籍贯城市！");
	       }
             if(data.msg=="请正确填写入户口所在地省份"){
	       //  $("#u_registedPlacePro").html("请正确填写入户口所在地省份！")
	          alert("请正确填写入户口所在地省份!");
	       }
	                 if(data.msg=="请正确填写入户口所在地城市"){
	        // $("#u_registedPlaceCity").html("请正确填写入户口所在地城市！")
	           alert("请正确填写入户口所在地城市!");
	       }
	        if(data.msg=="请正确填写入你的家庭电话"){
	         //$("#u_telephone").html("请正确填写入你的家庭电话！")
	        alert("请正确填写入你的家庭电话！");
 
	       }
	      if(data.msg=="你的家庭电话输入不正确"){
	         //$("#u_telephone").html("请正确填写入你的家庭电话！")
	         alert("请正确填写入你的家庭电话");
	       }
	      
	      if(data.msg=="你的家庭电话输入长度不对"){
	         //$("#u_telephone").html("请正确填写入你的家庭电话！")
	         alert("你的家庭电话输入长度不对");
	       }
	       
	    
	           if(data.msg=="请正确上传你的个人头像"){
	         //$("#u_img").html("个人头像不能为空！")
	          alert("个人头像不能为空！");
	       }
	       
	       
	    });
	    
	});
</script>

		<script>
			$(function(){
				//上传图片
				$("#btn_personalHead").click(function(){
					var dir = getDirNum();
					var json = "{'fileType':'JPG,BMP,GIF,TIF,PNG','fileSource':'user/"+dir+"','fileLimitSize':0.5,'title':'上传图片','cfn':'uploadCall','cp':'img'}";
					json = encodeURIComponent(json);
					 window.showModalDialog("uploadFileAction.do?obj="+json,window,"dialogWidth=500px;dialogHeight=400px");
					var headImgPath = $("#img").attr("src");
					if(headImgPath!=""){
						 $("#u_img").html("");
					}else{ 
						alert("上传失败！");	
					}
				});
				
			});
			
			function uploadCall(basepath,fileName,cp){
		  		if(cp == "img"){
		  			var path = "upload/"+basepath+"/"+fileName;
					$("#img").attr("src","../"+path);
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
		</script>
			<script type="text/javascript">
		  $(function(){
		     //样式选中
     $("#jk_hover").attr('class','nav_first');
	 $("#jk_hover div").removeClass('none');
		  });
		</script>

   <script>
  	
      //审核
     $("#jc_yes").click(function(){
	   var param = {};
	   param["paramMap.flag"] = 3;
	   param["paramMap.id"] = ${map.userId};
	    $.post("userBaseDataCheck.do",param,function(data){
	       if(data.msg=="保存成功"){
	       //window.location.href='againJumpToUserInf.do';
	        alert("审核成功");               
	        window.location.reload();
	       }else{
	         alert("审核失败");
	       }
	    });
	});
	
	 $("#jc_no").click(function(){
	   var param = {};
	   param["paramMap.flag"] = 2;
	   param["paramMap.id"] = ${map.userId};
	    $.post("userBaseDataCheck.do",param,function(data){
	       if(data.msg=="保存成功"){
	       //window.location.href='againJumpToUserInf.do';
	        alert("取消审核成功");	       
	        window.location.reload();
	       }else{
	         alert("取消审核失败");
	       }
	    });
	});
	
   </script>
	</body>
</html>
