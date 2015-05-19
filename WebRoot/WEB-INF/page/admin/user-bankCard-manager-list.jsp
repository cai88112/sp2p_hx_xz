<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
	<div> 
		<table id="help" style=" color: #333333;width:100%"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th style="width: 50px;" scope="col">
						序号
					</th>
					<th style="width: 120px;" scope="col">
						用户名
					</th>
					<th style="width: 150px;" scope="col">
						真实姓名
					</th>
					
					<th style="width: 100px;" scope="col">
						身份证
					</th>
					<th style="width: 100px;" scope="col">
						开户行
					</th>
					<th style="width: 100px;" scope="col">
						支行
					</th>
					<th style="width: 80px;" scope="col">
						卡号
					</th>
					<th style="width: 80px;" scope="col">
						审核人
					</th>
					<th style="width: 120px;" scope="col">
						提交时间
					</th>
					<th style="width: 120px;" scope="col">
						审核时间
					</th>
					<th style="width: 80px;" scope="col">
						状态
					</th>
					<th style="width: 80px;" scope="col">
						操作
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="12">暂无数据</td>
					</tr>
				</s:if>
				<s:else>
				<s:set name="counts" value="#request.pageNum"/>
					<s:iterator value="pageBean.page" var="bean" status="s">
						<tr class="gvItem">
							<td align="center">
								<s:property value="#s.count+#counts"/>
							</td>
							<td>
								${bean.username}
							</td>
							<td>
								${bean.realName}
							</td>
							<td>
								${bean.idNo}
							</td>
							<td>
								${bean.bankName}
							</td>
							<td>
								${bean.branchBankName}
							</td>
							<td>
								${bean.cardNo}
							</td>
							<td>
								${bean.checkUser}
							</td>
							
							<td>
								<!--  ${bean.modifiedTime}-->
								<s:date name="#bean.commitTime" format="yyyy-MM-dd HH:mm:ss" />
							</td>
							<td>
								<s:date name="#bean.checkTime" format="yyyy-MM-dd HH:mm:ss" />
							</td>
							
							<s:if test="#bean.cardStatus==2">
							  <td>审核中</td>
							   <td> <a href="queryBankCardInfo.do?bankId=${bean.id }" target="main">
								审核
								</a></td>
								&nbsp;&nbsp;
							</s:if>
							<s:elseif test="#bean.cardStatus==3">
							 <td>失败</td>
								<td>--</td>
							</s:elseif>
							<s:else>
							 <td>成功</td>
								<td><a href="javascript:show(${bean.id });" target="main">
								查看
								</a></td>
								
							</s:else>
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	

	
	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
</div>