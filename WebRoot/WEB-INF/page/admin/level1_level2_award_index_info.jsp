<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
	<div>
		<table id="gvNews" style="width: 100%; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th scope="col">
						序号
					</th>
					<th scope="col">
						经纪人
					</th>
					<th scope="col">
						真实姓名
					</th>
					<th scope="col">
						身份证
					</th>
					<th scope="col">
						投资奖励提成
					</th>
					<th scope="col">
					加入时间
					</th>
					<th scope="col">
					经纪人状态
					</th>
				</tr>
				<s:if test="pageBean.page==null || pageBean.page.size==0">
					<tr align="center" class="gvItem">
						<td colspan="7">暂无数据</td>
					</tr>
				</s:if>
				<s:else>
				
				<s:iterator value="pageBean.page" var="bean" status="st">
					<tr class="gvItem">
						<td>
						${st.count }
						</td>
						<td>
						${userName}
						</td>
						<td>
						${relaName }
						</td>
						<td>
						${card }
						</td>
						<td>
						￥${level2moneys==null?0:level2moneys }
						</td>
						<td>
						<s:date name="#bean.addDate" format="yyyy-MM-dd"/>
						</td>
						<td>
						${enable==1?'启用':'禁用' }
						</td>
					</tr>
				</s:iterator>
				</s:else>
				<tr>
				<td  colspan="1"><strong>合计项</strong></td>
				<td colspan="1" align="right" >经济人提成奖励总计:</td>
				<td  colspan="1" >￥<s:if test="%{paramMap.level2moneySum==''}">0</s:if><s:else>
				${paramMap.level2moneySum }	</s:else></td>
				<td colspan="1" align="right" >当前页的提成奖励总合计:</td>
				<td  align="center" >￥${countMonmey}</td>		
				</tr>
			</tbody>
		</table>
	</div>

	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
