<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<script type="text/javascript">
 $(function(){
   $("#excel").click(function(){  
       
      window.location.href="exportRechargeRecord.do";
   });
 });
</script>
	<div>
		<table id="help" style="width: 100%; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
				 
					<th style="width: 30px;" scope="col">
						序号
					</th>
					<th style="width: 200px;" scope="col">
						用户名
					</th>
					<th style="width: 150px;" scope="col">
						充值类型
					</th>
					
					<th style="width: 100px;" scope="col">
						充值金额
					</th>
					<th style="width: 100px;" scope="col">
						手续费
					</th>
					<th style="width: 100px;" scope="col">
						到账金额
					</th>
					<th style="width: 120px;" scope="col">
						充值时间
					</th>
					
					<th style="width: 80px;" scope="col">
						状态
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
							<s:if test="#bean.type==1">支付宝支付</s:if>
							<s:if test="#bean.type==2">环迅支付</s:if>
							<s:if test="#bean.type==3">国付宝</s:if>
							<s:if test="#bean.type==4">手动充值</s:if>
							<s:if test="#bean.type==6">线下充值</s:if>
							<s:if test="#bean.type==51">手工充值</s:if>
							<s:if test="#bean.type==52">虚拟充值</s:if>
							<s:if test="#bean.type==53">奖励充值</s:if>
							</td>
							<td>
								${bean.rechargeMoney}
							</td>
							<td>
								${bean.cost}
							</td>
							<td>
								${bean.realMoney}
							</td>
							<td>
								<s:date name="#bean.rechargeTime" format="yyyy-MM-dd HH:mm:ss" ></s:date>
							</td>
							<td>
							<s:if test="#bean.result==0">失败</s:if>
							<s:if test="#bean.result==1">成功</s:if>
							<s:if test="#bean.result==2">审核中</s:if>
							</td>
							
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	</div>
	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
