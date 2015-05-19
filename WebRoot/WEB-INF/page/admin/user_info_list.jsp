<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<html>
	<head>
		<title>管理首页</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3" />
		<meta http-equiv="description" content="This is my page" />
		<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" type="text/css" href="../common/date/calendar.css"/>
		<script type="text/javascript" src="../common/date/calendar.js"></script>
		<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
		<script type="text/javascript" src="../script/jquery.shove-1.0.js"></script>
	<div>
		<table id="gvNews" style="width: 100%; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th scope="col" width="50px">
						序号
					</th>
					<th scope="col" width="80px">
						用户账号
					</th>
					<th scope="col" width="75px">
						真实姓名
					</th>
					<th scope="col" width="60px"> 
						身份证
					</th>
					<th scope="col" width="80px">
						注册时间
					</th>
					<th scope="col" width="100px">
						个人信息
					</th>
					<th scope="col" width="100px">
						工作信息
					</th>
						<th scope="col" width="100px">
						联系信息
					</th>
						<th scope="col" width="80px">
						审核人
					</th>
						<th scope="col" width="80px">
						操作
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="10">暂无数据</td>
					</tr>
				</s:if>
				<s:else>
				<s:set name="counts" value="#request.pageNum"/>
				<s:iterator value="pageBean.page" var="bean" status="s">
					<tr class="gvItem">
						<td>
						<s:property value="#s.count+#counts"/>
						</td>
						<td align="center">
							<a>${username}</a>
						</td>
							<td>
						${realName }
					</td>
						<td>
							${idNo}
						</td>
						<td>
						<s:date name="#bean.createTime" format="yyyy-MM-dd" />
						<!-- 
						  <s:if test="#bean.auditStatus==1">
							等待审核
							</s:if>
							<s:elseif test="#bean.status==3">
							审通过
							</s:elseif>
							<s:else>
							审核通过
							</s:else>
						
							<s:date name="#bean.addDate" format="yyyy-MM-dd HH:mm:ss" />
							 -->
						</td>
						<td><!--
						 个人信息状态 -->
                           <s:if test="#bean.personauditStatus==1">基本信息完整<a style="color: gray;">(待审核)</a></s:if>
                           <s:elseif  test="#bean.personauditStatus==2">基本信息完整<a style="color: red;">(失败)</a></s:elseif>
                           <s:elseif  test="#bean.personauditStatus==3">基本信息完整<a style="color: blue;">(成功)</a></s:elseif>
                            <s:else>未填写</s:else>
						</td>
						<td>
						 工作信息状态 
						    <s:if test="#bean.workauditStatus==1"><a style="color: gray;">(待审核)</a></s:if>
                           <s:elseif  test="#bean.workauditStatus==2"><a style="color: red;">(失败)</a></s:elseif>
                           <s:elseif  test="#bean.workauditStatus==3"><a style="color: blue;">(成功)</a></s:elseif>
                            <s:else>未填写</s:else>
					</td>
						<td>
						 联系信息审核 
							     
							   <s:if test="#bean.workauditStatus!=null">
							       <s:if test="#bean.directedStatus==1">
							        <a style="color: gray;">(待审核)</a>
							       </s:if>
							       <s:elseif test="#bean.directedStatus==2">
							         <a style="color: red;">(失败)</a>
							       </s:elseif>
							       <s:elseif test="#bean.directedStatus==3">
							            <s:if test="#bean.otherStatus==1">
							               <a style="color: gray;">(待审核)
							            </s:if>  
							            <s:elseif test="#bean.otherStatus==2">
							               <a style="color: red;">(失败)</a>
							            </s:elseif>
							            <s:elseif test="#bean.otherStatus==3">
							                <s:if test="#bean.moredStatus==1">
							                   <a style="color: gray;">(待审核)
							                </s:if>
							                <s:elseif test="#bean.moredStatus==2">
							                   <a style="color: red;">(失败)</a>
							                </s:elseif>
							                <s:elseif test="#bean.moredStatus==3">
							                   <a style="color: blue;">(成功)</a>
							                </s:elseif>
							                <s:else>更多联系人信息未填写</s:else>
							            </s:elseif>
							            <s:else>其他联系人未填写</s:else>
							       </s:elseif>
							       <s:else>直系联系人信息未填写</s:else>
							    </s:if>
							    <s:else>未填写</s:else>
                          --><!-- 
                           <s:elseif  test="#bean.workauditStatus==2">基本信息完整<a style="color: red;">(失败)</a></s:elseif>
                           <s:elseif  test="#bean.workauditStatus==3">基本信息完整<a style="color: blue;">(成功)</a></s:elseif>
                            <s:else>未填写</s:else>
							 -->
							
							
					</td>
						<td>
							<s:if test='#bean.service!=null'>
							${service }
							</s:if>
							<s:else>
							未分配
							</s:else>
							
					</td>
						<td>
						<a href="adminBase.do?uid=${id}">查看</a>
					</td>
					</tr>
				</s:iterator>
				</s:else>
			</tbody>
		</table>
	</div>
<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>