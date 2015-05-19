<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<th>姓名</th>
		<th>时间</th>
		<th>电话</th>
		<th>提现账号</th>
		<th>提现金额</th>
		<th>手续费</th>
		<th>状态</th>
		<th>操作</th>
	</tr>

	<s:if test="pageBean.page!=null && pageBean.page.size>0">
		<s:iterator value="pageBean.page" var="bean">
			<tr>
				<td align="center">${bean.name}</td>
				<td align="center"><s:date name="#bean.applyTime"
						format="yyyy-MM-dd HH:mm:ss" /></td>
				<td align="center">${bean.cellPhone}</td>
				<td align="center">${bean.acount}</td>
				<td align="center">${bean.sum}</td>
				<td align="center">${bean.poundage}</td>
				<s:if test="#bean.status == 1">
					<td align="center">审核中</td>
					<td align="center"><a href="javascript:void(0);"
						onclick="deleteWithdraw(${bean.id},${bean.sum})">取消</a></td>
				</s:if>
				<s:else>
					<s:if test="#bean.status ==2">
						<td align="center">已提现</td>
						<td align="center">--</td>
					</s:if>
					<s:else>
						<s:if test="#bean.status ==4">
							<td align="center">通过</td>
							<td align="center" bgcolor="#FFFFFF"><a
								href="javascript:DoWithdraw(${bean.sum},${bean.id},${bean.checkId},${bean.poundage})">提现</a></td>
						</s:if>
						<s:else>
							<td align="center">失败</td>
							<td align="center">--</td>
						</s:else>
					</s:else>
				</s:else>
			</tr>
		</s:iterator>
	</s:if>
	<s:else>
		<tr>
			<td colspan="7" align="center">暂无信息</td>
		</tr>
	</s:else>
</table>
<div class="page">
	<p>
		<shove:page curPage="${pageBean.pageNum}" showPageCount="6"
			pageSize="${pageBean.pageSize }" theme="jsNumber"
			totalCount="${pageBean.totalNum}"></shove:page>
	</p>
</div>
<div id = "resultData" style="display:none;"></div>
<script>
   function deleteWithdraw(id,sum){
      param["paramMap.wId"] = id;
      param["paramMap.money"] = sum;
      if(!window.confirm("确定要取消提现记录")){
        return;
      }
      $.shovePost("deleteWithdraw.do",param,function(data){
           if(data.msg == 1){
             alert("取消成功");
             jumpUrl('withdrawLoad.do');
           }else{
             alert(data.msg);
             return;
           }
      });
   }

   function DoWithdraw(amt,wid,checkId,pdg){
	 //  window.location.href="withdrawalMon.do?wid="+wid+"&amt="+amt+"&checkId="+checkId+"&pdg="+pdg;
	 var parm = {};
	 parm["paramMap.id"] = wid;
	 parm["paramMap.checkId"] = checkId;
	   $.shovePost("withdrawalMon.do",parm,function(data){
           $("#resultData").html(data);
      });
	   
	   
	   
	}
</script>
