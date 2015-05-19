<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<div style="padding: 15px 10px 0px 10px;">
	<table id="gvNews" style="width: 100%; color: #333333;" cellspacing="1"
		cellpadding="3" bgcolor="#dee7ef" border="0">
		<tbody>
			<tr class=gvHeader>
				<th style="width: 50px;" scope="col">ID</th>
				<th style="width: 150px;" scope="col">用户名</th>
				<th style="width: 150px;" scope="col">真实姓名</th>
				<th style="width: 150px;" scope="col">体验金金额</th>
				<th style="width: 200px;" scope="col">可用体验金</th>
				<th style="width: 150px;" scope="col">冻结体验金</th>
				<th style="width: 150px;" scope="col">待收体验金</th>
				<th style="width: 150px;" scope="col">状态</th>
				<th style="width: 150px;" scope="col">添加时间</th>
				<th style="width: 150px;" scope="col">到期时间</th>
			</tr>
			<s:if test="pageBean.page==null || pageBean.page.size==0">
				<tr align="center" class="gvItem">
					<td colspan="12">暂无数据</td>
				</tr>
			</s:if>
			<s:else>
				<s:set name="counts" value="#request.pageNum" />
				<s:iterator value="pageBean.page" var="bean" status="s">
					<tr class="gvItem">
						<td align="center" style="width: 100px;"><s:property
								value="#s.count+#counts" /></td>
						<td align="center">${bean.username}</td>
						<td align="center">${bean.realName}</td>
						<td align="center">${bean.usableMoney+bean.freezeMoney+bean.collectMoney}</td>
						<td align="center">${bean.usableMoney}</td>
						<td align="center">${bean.freezeMoney}</td>
						<td align="center">${bean.collectMoney}</td>
						<td align="center"><s:if test='#bean.status == "0"'>
						未到期
				        </s:if> <s:else>
				                     已到期
				        </s:else></td>

						<td align="center">${bean.timeStart}</td>
						<td align="center">${bean.timeEnd}</td>

					</tr>
				</s:iterator>
			</s:else>
		</tbody>
	</table>
</div>
<shove:page curPage="${pageBean.pageNum}" showPageCount="10"
	pageSize="${pageBean.pageSize }" theme="jsNumber"
	totalCount="${pageBean.totalNum}"></shove:page>
