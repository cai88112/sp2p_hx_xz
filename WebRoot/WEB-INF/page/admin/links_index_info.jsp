<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
	<div>
		<table id="help" style="width: 100%; color: #333333;"
			cellspacing="1" cellpadding="3" bgcolor="#dee7ef" border="0">
			<tbody>
				<tr class=gvHeader>
					<th style="width: 35px;" scope="col">
						选中
					</th>
					<th style="width: 200px;" scope="col">
						序号
					</th>
					<th style="width: 150px;" scope="col">
						名称
					</th>
					<th style="width: 150px;" scope="col">
						网址
					</th>
					<th style="width: 80px;" scope="col">
						操作
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
							<td>
								<input id="gvNews_ctl02_cbID" class="helpId" type="checkbox"
									value="${bean.serialCount }" name="cb_ids" />
							</td>
							<td align="center">
								<s:property value="#s.count+#counts"/>
							</td>
							<td>
								${bean.companyName}
							</td>
							<td>
								${bean.companyURL}
							</td>
							<td>
								<a href="updateLinksInit.do?commonId=${bean.serialCount }" target="main">
								编辑
								</a>
								&nbsp;&nbsp;
								  <a href="javascript:del(${bean.serialCount})" > 
								删除
								</a>
							</td>
						</tr>
					</s:iterator>
				</s:else>
			</tbody>
		</table>
	</div>
	<table style="border-collapse: collapse; border-color: #cccccc"
		cellspacing="1" cellpadding="3" width="100%" align="center" border="1">
		<tbody>
			<tr>
				<td class="blue12" style="padding-left: 8px" align="left">
					<input id="chkAll" onclick="checkAll(this); " type="checkbox" value="checkbox" name="chkAll" />
					全选 &nbsp;&nbsp;&nbsp;&nbsp;
					<input id="btnDel" onclick="dels();" type="button"
						value="删除选中记录" name="btnDel" />
				</td>
			</tr>
		</tbody>
	</table>
	<shove:page curPage="${pageBean.pageNum}" showPageCount="10" pageSize="${pageBean.pageSize }" theme="jsNumber" totalCount="${pageBean.totalNum}"></shove:page>
