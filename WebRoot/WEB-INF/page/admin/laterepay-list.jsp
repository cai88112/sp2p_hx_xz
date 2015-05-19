<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<div style="padding: 15px 10px 0px 10px;">
		<table id="gvNews" style="width: 100%; color: #333333;" cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th scope="col">
						序号
					</th>
					<th scope="col">
						用户名
					</th>
					<th scope="col">
						真实姓名
					</th>
					<th scope="col">
						借款标题
					</th>
					<th scope="col">
						期数
					</th>
					<th scope="col">
						类型
					</th>
					<th scope="col">
						应还时间
					</th>
						<th scope="col">
						逾期天数
					</th>
						<th scope="col">
						应还金额
					</th>
						<th scope="col">
					    逾期金额
					</th>
						<th scope="col">
					    总还款
					</th>
					    <th scope="col">
					    网站待还
					</th>
					   <th scope="col">
					    还款状态
					</th>
						<th scope="col">
						操作
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="15">暂无数据</td>
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
							${bean.username}
						</td>
						<td>
							${bean.realName}
						</td>
						<td>
						   <a href="${basePath}/financeDetail.do?id=${bean.borrowId}" target="_blank">${bean.borrowTitle}</a>
						</td>
						<td>
						${bean.repayPeriod}
					    </td>
						<td>
							<s:if test="#bean.borrowWay == 1">
							净值借款
							</s:if>
							<s:elseif test="#bean.borrowWay == 2">
							秒还借款
							</s:elseif>
							<s:elseif test="#bean.borrowWay == 3">
							信用借款
							</s:elseif> 
							<s:elseif test="#bean.borrowWay == 4">
							实地考察借款
							</s:elseif>
							<s:elseif test="#bean.borrowWay == 5">
							机构担保借款
							</s:elseif>
						</td>
						
						<td>
							${bean.repayDate}
					</td>
						<td>
							${bean.lateDay}
					</td>
						<td>
							￥${bean.totalSum}
					</td>
						<td>
							￥${bean.lateFI}
					</td>
					    <td>
							￥${bean.repaySum}
					</td>
					<td>
							<s:if test="#bean.isWebRepay==1">
							   否
							</s:if>
						<s:elseif test="#bean.isWebRepay==2">是</s:elseif>
					</td>
					<td>
							<s:if test="#bean.repayStatus==1">
							   未偿还
							</s:if>
							<s:elseif test="#bean.repayStatus==2">
							  已偿还
							</s:elseif>
							<s:elseif test="#bean.repayStatus==3">
							  偿还中
							</s:elseif>
					</td>
					<td>
						 <a href="repaymentDetail.do?id=${bean.id}">查看</a> 
					</td>
					</tr>
				</s:iterator>
				</s:else>
				<tr class="gvItem">
				<td  colspan="2" rowspan="3" align="center" ><strong>合计项</strong></td>
				<td colspan="2" align="right" >逾期应还总金额:</td>
				<td colspan="2">￥<s:if test="#request.lateRepayMap.totalSumm==''">0</s:if><s:else>${lateRepayMap.totalSumm}</s:else></td>
				<td  colspan="2" align="right">当前页逾期应还金额：</td>
				<td  align="center" ><fmt:formatNumber value="${totalSum }" type="number" pattern="￥0.00" /></td>	<td></td><td></td><td></td><td></td><td></td>		
				</tr>
				<tr class="gvItem">
				<td colspan="2" align="right" >逾期总金额:</td>
				<td colspan="2">￥<s:if test="#request.lateRepayMap.totallateFI==''">0</s:if><s:else>${lateRepayMap.totallateFI}</s:else></td>
				<td  colspan="2" align="right">当前页逾期金额：</td>
				<td></td>
				<td  align="center" ><fmt:formatNumber value="${lateFI }" type="number" pattern="￥0.00" /></td><td></td><td></td><td></td><td></td>
				</tr>
				<tr class="gvItem">
				<td colspan="2" align="right" >逾期还款总金额:</td>
				<td colspan="2">￥<s:if test="#request.lateRepayMap.amount==''">0</s:if><s:else>${lateRepayMap.amount}</s:else></td>
				<td  colspan="2" align="right">当前页逾期还款金额：</td>
				<td></td><td></td>
				<td  align="center" ><fmt:formatNumber value="${lateAmount }" type="number" pattern="￥0.00" /></td><td></td><td></td><td></td>
				</tr>
			</tbody>
		</table><p/>
		<!--<span class="require-field">当前页逾期还款金额:￥${lateAmount }元</span>
<span class="require-field">逾期还款总金额:￥<s:if test="#request.lateRepayMap.amount==''">0</s:if><s:else>${lateRepayMap.amount}</s:else>元</span>

--></div>
<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>