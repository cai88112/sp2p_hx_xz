<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>管理首页</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"/>
	<meta http-equiv="description" content="This is my page"/>
	<link href="../css/admin/admin_css.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="../css/user_css.css" />

</head>



<body>
<div class="main_alll">
	<h2 class="main_alll_h2">审核管理</h2>
    <div class="main_div_a">
    	<strong><b>借款管理</b></strong>
        <p>借款等待资料审核： (<a href="borroww.do"><span style="color:red;">${session.map.ddcount }</span></a>)条</p>
        <p>借款初审审核：  (<a href="borrowf.do"><span style="color:red;">${session.map.pcount }</span></a>)条</p>
        <p>借款满标审核： (<a href="borrowFullScale.do"><span style="color:red;">${session.map.fcount}</span></a>)条</p>
        <p>申请债权转让： (<a href="queryApplyDebtInit.do"><span style="color:red;">${session.map.zccount }</span></a>)条</p>
        <p>债权转让中：(<a href="queryAuctingAssignmentDebtInit.do"><span style="color:red;"> ${session.map.zacount }</span></a>)条</p>
    </div>
    <div class="main_div_a">
    	<strong><b>认证管理</b></strong>
        <p>用户基本信息审核(待审核)： (<a href="queryPersonInfolistindex.do?types=1"><span style="color:red;">${session.map.jbxxcount }</span></a>)条</p>
        <p>用户基本资料审核(待审核)：  (<a href="rechargeecordsInit.do?types=1"><span style="color:red;">${session.map.jbzlcount }</span></a>)条</p>
        <p>手机变更申请(审核中)： (<a href="updatephoneIndexChange.do?types=2"><span style="color:red;">${session.map.sjcount}</span></a>)条</p>
        <p>额度申请(审核中)： (<a href="querycreditindex.do?types=1"><span style="color:red;">${session.map.edcount }</span></a>)条</p>
        <p>可选资料认证：(<a href="queryselectInitindex.do?types=4"><span style="color:red;"> ${session.map.kxcount }</span></a>)条</p>
    </div>
    <div class="main_div_a">
    	<strong><b>资金管理</b></strong>
        <p>等待审核的提现(审核中)： (<a href="queryCheckInit.do"><span style="color:red;">${session.map.ddtxcount }</span></a>)条</p>
        <p>转账中的提现(转账中)：  (<a href="queryTransInit.do"><span style="color:red;">${session.map.zctxcount }</span></a>)条</p>
        <p>银行卡变更申请(审核中)： (<a href="queryModifyBankInit.do?types=2"><span style="color:red;">${session.map.yhbgcount}</span></a>)条</p>
    </div>
</div>
</body>



    <!--<br />借款管理：<br />
    借款等待资料审核： (<a href="borroww.do"><span style="color:red;">${session.map.ddcount }</span></a>)条 	
    借款初审审核：  (<a href="borrowf.do"><span style="color:red;">${session.map.pcount }</span></a>)条
    借款满标审核： (<a href="borrowFullScale.do"><span style="color:red;">${session.map.fcount}</span></a>)条
    申请债权转让： (<a href="queryApplyDebtInit.do"><span style="color:red;">${session.map.zccount }</span></a>)条
    债权转让中：(<a href="queryAuctingAssignmentDebtInit.do"><span style="color:red;"> ${session.map.zacount }</span></a>)条
    <br />认证管理：<br />
    用户基本信息审核(待审核、失败)：(<a href="queryPersonInfolistindex.do"><span style="color:red;">${session.map.jbxxcount }</span></a>)条
    用户基本资料审核(待审核、失败)：(<a href="rechargeecordsInit.do"><span style="color:red;">${session.map.jbzlcount }</span></a>)条
    手机变更申请(审核中)：(<a href="updatephoneIndexChange.do"><span style="color:red;">${session.map.sjcount }</span></a>)条
    额度申请(审核中)：(<a href="querycreditindex.do"><span style="color:red;">${session.map.edcount }</span></a>)条
    可选资料认证：(<a href="queryselectInitindex.do"><span style="color:red;">${session.map.kxcount }</span></a>)条
    <br />资金管理：<br />
    等待审核的提现(审核中)：(<a href="queryCheckInit.do"><span style="color:red;">${session.map.ddtxcount }</span></a>)条
    转账中的提现(转账中)：(<a href="queryTransInit.do"><span style="color:red;">${session.map.zctxcount }</span></a>)条
    银行卡变更申请(审核中)：(<a href="queryModifyBankInit.do"><span style="color:red;"> ${session.map.yhbgcount }</span></a>)条-->

</html>


