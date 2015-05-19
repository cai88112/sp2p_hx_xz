<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
	<div>
		<table id="help" style="width: 100%; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th style="width: 50px;" scope="col">
						序号
					</th>
					<th style="width: 80px;" scope="col">
						借款类型
					</th>
					<th style="width: 80px;" scope="col">
						名称
					</th>
					<th style="width: 50px;" scope="col">
						标识符
					</th>
					<th style="width: 150px;" scope="col">
						年利率
					</th>
					<th style="width: 100px;" scope="col">
						借款期限(月/天)
					</th>
					<th style="width: 80px;" scope="col">
						有效期
					</th>
					<th style="width: 70px;" scope="col">
						冻结保证金
					</th>
					<th style="width: 150px;" scope="col">
						还款方式
					</th>
					<th scope="col" align="center" style="width: 40px;" >
						状态
					</th>
					<th style="width: 50px;" scope="col">
						操作
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="11">暂无数据</td>
					</tr>
				</s:if>
				<s:else>
				<s:set name="counts" value="#request.pageNum"/>
					<s:iterator value="pageBean.page" var="bean" status="s">
						<tr class="gvItem">
							<td>
								<s:property value="#s.count+#counts"/>
							</td>
							<td>
							<s:iterator var="map" value="#request.mapTypeList">
							<s:if test="#map.id==#bean.amount_type">
								${map.name }
							</s:if>
							</s:iterator>
							</td>
								<td align="center">
						   		${title }
							</td>
							<td>
								${nid}
							</td>
							<td align="center">
						   		${apr_first }%~${apr_end }%
							</td>	
							<td align="center">
						   		${period_month }月<br />
						   		${period_day}天
							</td>	
							<td align="center">
							<s:if test='#bean.validate == "0" '>无限制</s:if>
							<s:else>
						   		${validate }天
						   	</s:else>
							</td>	
							<td align="center">
						   		${vip_frost_scale }%
							</td>
							<td align="center">
								<s:if test="%{#bean.nid eq 'seconds'}">
									秒还				
								</s:if><s:elseif test="%{#bean.nid eq 'flow'}">一次性还款</s:elseif>
								<s:else>
									${titles }
								</s:else>
							</td>
							<td align="center">
							<s:if test="%{status == 1}">
								开启
							</s:if>
						    <s:else>
						    	关闭
						    </s:else>
							</td>	
							<td>
							<a href="updateShoveTypeInit.do?id=${id}">修改</a>
							</td>		
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	</div>
	
<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
	