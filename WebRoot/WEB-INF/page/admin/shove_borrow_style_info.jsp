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
						还款方式
					</th>
					<th style="width: 50px;" scope="col">
						标识符
					</th>
					<th scope="col"  style="width: 50px;" align="center">
						标题
					</th>
					<th style="width: 50px;" scope="col">
						状态
					</th>
					<th style="width: 200px;" scope="col">
						算法信息
					</th>
					<th style="width: 100px;" scope="col">
						操作
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="7">暂无数据</td>
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
								${name }
							</td>
							<td>
								${nid}
							</td>
							<td align="center">
						   		${title }
							</td>
							<td align="center">
							<s:if test="%{status == 1}">
								开启
							</s:if>
						    <s:else>
						    	关闭
						    </s:else>
							</td>	
							<td align="center">
						   		${contents }
							</td>	
							<td>
							<a href="updateShoveBorrowStyleInit.do?id=${id}">修改</a>
							</td>		
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	
	</div>
	
<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
		<div style="height: 10px;"></div>