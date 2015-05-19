<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
	<div style="overflow-x:auto;overflow-y:auto;width:100%;height: 420px;"> 
		<table id="help" style="width: 1010px; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th style="width: 50px;" scope="col">
						序号
					</th>
					<th style="width: 120px;" scope="col">
						用户名
					</th>
					<th style="width: 120px;" scope="col">
						真实姓名
					</th>
					<th style="width: 100px;" scope="col">
						可用余额
					</th>
					<th style="width: 100px;" scope="col">
						冻结金额
					</th>
					<th style="width: 80px;" scope="col">
						待收金额
					</th>
					<th style="width: 80px;" scope="col">
						待还金额
					</th>
					<th style="width: 80px;" scope="col">
						总金额
					</th>
					<th style="width: 300px;" scope="col">
						操作
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="9">暂无数据</td>
					</tr>
				</s:if>
				<s:else>
				<s:set name="counts" value="#request.pageNum"/>
					<s:iterator value="pageBean.page" var="bean" status="s">
						<tr class="gvItem">
							<td align="center" style="width:100px;">
								<s:property value="#s.count+#counts"/>
							</td>
							<td>
								${bean.username}
							</td>
							<td>
								${bean.realName}
							</td>
							<td>
								${bean.usableSum}
							</td>
							<td>
								${bean.freezeSum}
							</td>
							<td>
								${bean.dueinSum}
							</td>
							<td>
								${bean.dueoutSum}
							</td>
							<td>
								${bean.freezeSum+bean.usableSum+bean.dueinSum}
							</td>
							<td>
							<a href="queryUserFundRechargeInit.do?userId=${bean.userId }" target="main" >充值记录</a>&nbsp;&nbsp;
							  <a href="queryUserFundWithdrawInit.do?userId=${bean.userId }" target="main" >提现管理</a>&nbsp;&nbsp;
							  <a href="queryUserFundrecordInit.do?userId=${bean.userId }&userName=${bean.username }" 
							  target="main" >资金记录</a>
							</td>
						</tr>
					</s:iterator>
					<tr>
						<td>总计：</td>
						<td colspan="2">可用总额：${map.usableSums }</td>
						<td colspan="2">冻结总额：${map.freezeSums }</td>
						<td colspan="2">待收总额：${map.dueinSums }</td>
						<td colspan="2">待还总额：${map.dueoutSums }</td>
					</tr>
				</s:else>
			</tbody>
		</table>
	
	
	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
</div>