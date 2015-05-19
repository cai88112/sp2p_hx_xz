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
					<th style="width: 100px;" scope="col">
						用户名
					</th>
					<th style="width: 150px;" scope="col">
						真实姓名
					</th>
					
					<th style="width: 80px;" scope="col">
						类型
					</th>
					<th style="width: 80px;" scope="col">
						操作金额
					</th>
					
					<th style="width: 80px;" scope="col">
						操作人员
					</th>
					
					<th style="width: 160px;" scope="col">
						操作时间
					</th>
					<th style="width: 140px;" scope="col">
						备注
					</th>
					
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="8">暂无数据</td>
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
								${bean.uname}
							</td>
							<td>
							  
								${bean.realName}
							</td>
							<td>
							  <s:if test="#bean.type==1">
							    手动充值
							  </s:if>
							  <s:else>
							    手动扣费
							  </s:else>
								
							</td>
							<td>
								${bean.dealMoney}
							</td>
							
							<td>
								${bean.userName}
							</td>
							<td>
								<s:date  name="#bean.checkTime" format="yyyy-MM-dd HH:mm:ss" ></s:date>
							</td>
							<td>
								${bean.remark}
							</td>
						</tr>
					</s:iterator>
				</s:else>
				<tr class="gvItem"><td colspan="8" align="left"><font size="2">共有${totalNum }条记录</font></td></tr>
			</tbody>
		</table>
	</div>
	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
