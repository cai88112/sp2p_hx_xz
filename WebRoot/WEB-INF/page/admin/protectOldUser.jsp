<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>


<script type="text/javascript">
			
			function del(id){
				if(!window.confirm("确定要删除吗?")){
		 			return;
		 		}
				window.location.href='deleteOldUser.do?userId='+id;
			}
</script>
<div style="padding: 15px 10px 0px 10px;">
	<table id="gvNews" style="width: 100%; color: #333333;" cellspacing="1"
		cellpadding="3" bgcolor="#dee7ef" border="0">
		<tbody>
			<tr class=gvHeader>
				<th style="width: 50px;" scope="col">序号</th>
				<th style="width: 150px;" scope="col">批次</th>
				<th style="width: 150px;" scope="col">用户名</th>
				<th style="width: 150px;" scope="col">真实姓名</th>	
				<th style="width: 150px;" scope="col">待收标准</th>		
				<th style="width: 200px;" scope="col">状态</th>				
				<th style="width: 200px;" scope="col">操作</th>				
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
						<td align="center">${bean.batch}</td>
						<td align="center">${bean.username}</td>
						<td align="center">${bean.realName}</td>						
						<td align="center">${bean.duestandard}</td>
						<td align="center">
						<s:if test="#bean.status==1">有效</s:if>
						<s:if test="#bean.status==0">无效</s:if>
						</td>
						<td align="center">
							<a id="" href="javascript:del(${bean.userId})">删除</a>
						</td>
					</tr>
				</s:iterator>
			</s:else>
		</tbody>
	</table>
</div>
<shove:page curPage="${pageBean.pageNum}" showPageCount="10"
	pageSize="${pageBean.pageSize }" theme="jsNumber"
	totalCount="${pageBean.totalNum}"></shove:page>
