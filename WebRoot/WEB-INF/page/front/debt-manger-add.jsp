<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
      <form id="editForm" >
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th width="26%" align="right">竞拍期限：<strong style="color: red;">*</strong></th>
    <td width="74%">
    	<s:select list="auctionDaysList" name="paramMap.auctionDays" listKey="selectValue" listValue="selectName" headerKey="" headerValue="请选择">
    	</s:select><strong style="color: red;"><s:fielderror fieldName="paramMap.auctionDays" /></strong>
    </td>
  </tr>
  <tr>
    <th align="right">竞拍方式：<strong style="color: red;">*</strong></th>
    <td>
    <s:radio list="#{1:'明拍',2:'暗拍'}" theme="simple" name="paramMap.auctionMode"></s:radio>
     <strong style="color: red;"><s:fielderror fieldName="paramMap.auctionMode" /></strong></td>
  </tr>
  <tr>
    <th align="right">债权金额：</th>
    <td><span id="span_debtSum">${paramMap.debtSum }</span>元
    	<s:hidden id="debtSum" name="paramMap.debtSum"></s:hidden>
    	<s:hidden id="investId" name="paramMap.investId"></s:hidden>
    </td>
  </tr>
  <tr>
    <th align="right">债权期限：</th>
    <td><span id="span_debtLimit">${paramMap.debtLimit}</span>个月
    	<input id="debtLimit" name="paramMap.debtLimit" value="${paramMap.debtLimit}" type="hidden" />
    </td>
  </tr>
  <tr>
    <th align="right">竞拍底价：<strong style="color: red;">*</strong>&nbsp;&nbsp;</th>
    <td><input type="text" class="inp90" name="paramMap.auctionBasePrice" value="${ paramMap.auctionBasePrice}" />
    <strong style="color: red;"><s:fielderror fieldName="paramMap.auctionBasePrice" /></strong>
   <input id="borrowId" name="paramMap.borrowId" value="${paramMap.borrowId }" type="hidden" />
    </td>
  </tr>
  <tr>
    <th align="right" valign="top">转让描述：：<strong style="color: red;">*</strong>&nbsp;&nbsp;</th>
    <td><textarea class="txt380" rows="6" cols="30" name="paramMap.details">${paramMap.details }</textarea><br /><strong style="color: red;"> <s:fielderror fieldName="paramMap.details" /></strong></td>
  </tr>
  <tr>
    <td>&nbsp;</td>
    <td style="padding-top:20px;"><a href="javascript:void(0);" id="debt_ok" class="scbtn">确认</a> <a href="javascript:void(0);" id="debt_cancel" class="scbtn" id="qxtck">取消</a></td>
  </tr>
</table>
 </form>

<script>
   $(function(){   		
   		$("#debt_cancel").click(function(){
   			$("#zrzq_div").attr("style","display:none");
   			$("#editForm")[0].reset();
   		});
   		$("#debt_ok").click(function(){
   			var para = $("#editForm").serialize();
   			$.shovePost("addAssignmentDebt.do",para,function(data){
   				if(data == 1){
   					alert("操作成功");
   					$("#debt_cancel").click();
   					$("#btn_search").click();
   				}else if(data == -1){
   					alert("操作失败");
   					$("#debt_cancel").click();
   				}else{
   					$("#debt_add").html(data);
   				}
   			});
   		});
   });
</script>

