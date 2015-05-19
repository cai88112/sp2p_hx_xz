<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<div>
	<table id="help" style="width: 100%; color: #333333;" cellspacing="1"
		cellpadding="3" bgcolor="#dee7ef" border="0">
		<tbody>
			<tr class=gvHeader>
				<th scope="col">
					选中
				</th>
				<th scope="col">
					序号
				</th>
				<th scope="col">
					排序
				</th>
				<th scope="col">
					图片
				</th>
				<th scope="col">
					类型
				</th>
				<th scope="col">
					操作
				</th>
			</tr>
			<s:if test="pageBean.page==null || pageBean.page.size==0">
				<tr align="center" class="gvItem">
					<td colspan="8">
						暂无数据
					</td>
				</tr>
			</s:if>
			<s:else>
				<s:set name="counts" value="#request.pageNum" />
				<s:iterator value="pageBean.page" var="bean" status="s">
					<tr class="gvItem">
						<td>
							<input id="gvNews_ctl02_cbID" class="helpId" type="checkbox"
								value="${bean.serialCount }" name="cb_ids" />
						</td>
						<td align="center" style="width: 100px;">
							<s:property value="#s.count+#counts" />
						</td>
						<td>
							${bean.ordershort}
						</td>
						<td>
							<a href="../${bean.companyImg}" target="_bank" title="查看原图"><img
									src="../${bean.companyImg}" width="500px" height="120px">
							</a>
						</td>
						<td>
							<s:if test="#bean.type==1"> 友情链接</s:if>
							<s:elseif test="#bean.type==2">投资广告</s:elseif>
							<s:elseif test="#bean.type==3">首页滚动图片</s:elseif>
						</td>
						<td>
							<a href="updateIndexRollImgInit.do?commonId=${bean.serialCount }"
								target="main"> 编辑 </a> &nbsp;&nbsp;
							<a href="javascript:del(${bean.serialCount})"> 删除 </a>
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
				<input id="chkAll" onclick="checkAll(this); " type="checkbox"
					value="checkbox" name="chkAll" />
				全选 &nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnDel" onclick="dels();" type="button" value="删除选中记录"
					name="btnDel" />
			</td>
		</tr>
	</tbody>
</table>
<shove:page curPage="${pageBean.pageNum}" showPageCount="10"
	pageSize="${pageBean.pageSize }" theme="jsNumber"
	totalCount="${pageBean.totalNum}"></shove:page>
