<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
	<div style="overflow-x:auto;overflow-y:auto;width:100%;  "> 
		<table id="help" style="width: 1020px; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th style="width: 50px;" scope="col">
						序号
					</th>
					<th style="width: 120px;" scope="col">
						交易时间
					</th>
					<th style="width: 180px;" scope="col">
						类型
					</th>
					<th style="width: 220px;" scope="col">
						摘要
					</th>
					<th style="width: 160px;" scope="col">
						用户名
					</th>
						<th  scope="col" style="width: 100px;">
						收入
					</th>
					<th  scope="col" style="width: 100px;">
						支出
					</th>
				   <th style="width: 100px;" scope="col">
						待收金额
					</th>
					<th style="width: 100px;" scope="col">
						冻结金额
					</th>
					<th style="width: 100px;" scope="col">
						可用余额
					</th>
						<th style="width: 120px;" scope="col">
						总金额
					</th>
					<th style="width: 120px;" scope="col">
						交易对方
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
							<td align="center" >
								<s:property value="#s.count+#counts"/>
							</td>
							<td>
							  <s:date name="#bean.recordTime " format="yyyy-MM-dd HH:mm:ss"/>
							</td>
							<td>
								${bean.fundMode}
							</td>
							<td>
							${remarks }
							</td>
							<td>
								${userName}
							</td>
						
							<td>
							<s:if test="#bean.income==0"></s:if>
								<s:else>${bean.income}</s:else>
							</td>
							<td>
								<s:if test="#bean.spending==0"></s:if>
								<s:else>${bean.spending}</s:else>
							</td>
							<td>
								${bean.dueinSum}
							</td>
							<td>
								${bean.freezeSum}
							</td>
							<td>
								${bean.usableSum}
							</td>
							<td>
								${bean.totalSum}
							</td>
							<td>
							${traderName }
							</td>
							
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	
	
	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
</div>