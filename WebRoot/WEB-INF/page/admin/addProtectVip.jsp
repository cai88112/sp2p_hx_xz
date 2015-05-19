<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<head>
<script type="text/javascript" src="../script/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="../css/admin/common/dialog/popup.js"></script>

<style type="text/css">
.m table {
	font-size: 12px;
	font-family: serif;
}

.m table tr {
	height: 10px;
}
</style>
</head>
<div class="m">
	<table id="help" style="width: 100%; color: #333333;" bgcolor="#dee7ef"
		border="0">
		<tbody>
			<tr height="2px;">
				<th colspan="2"><strong>添加为老米成员</strong></th>
			</tr>
			<tr>
				<td>用户名：${popmap.username }</td>
			</tr>
			<tr>
				<td>真实姓名：${popmap.realName }</td>
			</tr>
			<tr>
				<td>信用积分：${popmap.creditrating }</td>
			</tr>
			<tr>
				<td>会员积分：${popmap.rating }</td>
			</tr>
			<tr>
				<td>待收金额：${popmap.dueinSum }</td>
			</tr>
			<tr>
				<td>选择批次：<select id="selectBatch" name="selectBatch">
						<s:iterator value="#request.maplist" status="stuts" id="map">
							<option value='<s:property value="#map.batch" />'><s:property
									value="#map.batch" /></option>
						</s:iterator>
						</select>
			</tr>

			<tr>
				<td colspan="2"><input type="button" value="添加" id="mybut">
				</td>
			</tr>
		</tbody>
	</table>
</div>
<script>
	$(function() {
		$("#mybut").click(function() {
			var param = {};
			param["paramMap.id"] = ${popmap.id} 
			param["paramMap.batch"] = $("#selectBatch").val();
			$.post("addProtectVip.do", param, function(data) {
				if (data == 3) {
					alert("添加成功");
					window.parent.ffff();
				}
				if (data == 1) {
					alert("选择会员批次不能为空");
				}
				if (data == 2) {
					alert("选择会员批次不能为非数字");
				}
				if (data == 4) {
					alert("添加失败");
				}
				if (data == 5) {
					alert("该用户已是老米用户.");
				}

			});
		});
	});
</script>
<script>
	
</script>



