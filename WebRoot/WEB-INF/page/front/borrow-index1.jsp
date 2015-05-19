<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <jsp:include page="/include/head.jsp"></jsp:include>
</head>
<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	

<div class="nymain">
  <div class="bigbox" style="border:1px solid #ddd">

  <div class="sqdk" style=" margin:20px auto; width:910px">
    <div class="jk">
      <div class="jkimg">
        <img src="images/neiye5_10.jpg" width="420" height="322" /></div>
       <p class="jkbtnbox">
       <a href="addBorrowInit.do?t=1" class="jzbtn">净值借款</a> 
       <a href="addBorrowInit.do?t=2" class="mhbtn">秒还借款</a> 
       <a href="addBorrowInit.do?t=0" class="qtbtn">其它借款</a></p> 
    </div>
    <div class="jk" style="margin-right:0px;">
      <div class="jkimg">
        <img src="images/neiye5_13.jpg" width="420" height="322" /></div>
       <p class="jkbtnbox"><a href="creditingInit.do" class="sqxybtn">申请信用额度</a></p> 
    </div>
  </div>
  </div>
</div>

<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
</body>
</html>
