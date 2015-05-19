<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <jsp:include page="/include/head.jsp"></jsp:include>
    <link href="css/inside.css"  rel="stylesheet" type="text/css" />
    <style type="text/css">
		p{
		text-align:right;
		padding:0px;
	}
</style>
</head>

<body>
<!-- 引用头部公共部分 -->
<jsp:include page="/include/top.jsp"></jsp:include>	
<div class="nymain">
  <div class="wdzh">
    <div class="l_nav">
      <div class="box">
         <!-- 引用我的帐号主页左边栏 -->
         <%@include file="/include/left.jsp" %>
      </div>
    </div>
    <div class="r_main">
      <div class="box">
      <div class="tabtil">
       <ul><li class="on">自动投标</li></ul></h2>
        </div>
      <div class="boxmain2">
      <div class="biaoge2" style="margin-top:0px;">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
     <tr>
     <p style="text-align: left; line-height: 22px">1、自动投标设置中若没有设置预留金额，系统将默认用户账户内全部资金用于自动投标。</p>
     <p style="text-align: left">2、自动投标设置之后，系统将按照用户的设置严格执行，请谨慎操作。 </p>
    </tr>
  <tr >
    <th colspan="2" align="left" style="position: relative; left: -14px; color: #000"> 设置我的自动投标工具</th>
    </tr>
  <tr >
    <td align="right" ><p >自动投标状态：</p></td>
    <td><p id="statusText" style="text-align: left"><s:if test="%{automaticBidMap.bidStatus ==2}">开启状态</s:if><s:else>关闭状态</s:else></p></td>
  </tr>
  <tr>
    <td align="right"><p>您的账户余额：</p></td>
    <td>${automaticBidMap.usableSum}元
    	<input type="hidden" value="${automaticBidMap.usableSum}" id="usableSum" name="usableSum" />
    </td>
  </tr>
  <tr>
    <td align="right"><p>每次投标金额：<span class="fred">*</span></p></td>
    <td><input type="text" class="inp100x" id="bidAmount" maxlength="20"  value="${automaticBidMap.bidAmount}"/>
      元</td>
  </tr>
  <tr>
    <td align="right"><p>利率范围：<span class="fred">*</span></p></td>
    <td><input type="text" class="inp100x" id="rateStart" maxlength="20"  value="${automaticBidMap.rateStart}"/>
      % --
        <input type="text" class="inp100x" id="rateEnd" maxlength="20"  value="${automaticBidMap.rateEnd}"/>
%</td>
  </tr>
  <tr>
    <td align="right"><p>借款期限：</p></td>
    <td>
    <s:select list="#{-1:'1天',-15:'15天',-25:'25天',1:'1个月',2:'2个月',3:'3个月',4:'4个月',5:'5个月',6:'6个月',7:'7个月',8:'8个月',9:'9个月',10:'10个月',11:'11个月',12:'12个月',18:'18个月',24:'24个月',30:'30个月',36:'36个月'}" id="deadlineStart"  cssClass="sel_70" listKey="key" listValue="value" name="automaticBidMap.deadlineStart"></s:select>
       --
    <s:select list="#{-1:'1天',-15:'15天',-25:'25天',1:'1个月',2:'2个月',3:'3个月',4:'4个月',5:'5个月',6:'6个月',7:'7个月',8:'8个月',9:'9个月',10:'10个月',11:'11个月',12:'12个月',18:'18个月',24:'24个月',30:'30个月',36:'36个月'}" id="deadlineEnd"  cssClass="sel_70" listKey="key" listValue="value" name="automaticBidMap.deadlineEnd"></s:select>  
</td>
  </tr>
  <tr>
    <td align="right"><p>信用等级范围：</p></td>
    <td>
    <s:select list="#{1:'HR',2:'E',3:'D',4:'C',5:'B',6:'A',7:'AA'}" id="creditStart"  cssClass="sel_70" listKey="key" listValue="value" value="automaticBidMap.creditStart"></s:select>
    --
    <s:select list="#{1:'HR',2:'E',3:'D',4:'C',5:'B',6:'A',7:'AA'}" id="creditEnd"  cssClass="sel_70" listKey="key" listValue="value" name="automaticBidMap.creditEnd"></s:select>
    </td>
  </tr>
  <tr>
    <td align="right"><p>账户保留金额：<span class="fred">*</span></p></td>
    <td><input type="text" class="inp100x" id="remandAmount" maxlength="20"  value="${automaticBidMap.remandAmount}"/>
元</td>
  </tr>
  <tr>
    <td align="right"><p>借款类型：<span class="fred">*</span></p></td>
    <td>
     <s:checkboxlist name="checkList.id" value="checkList.{#this.id}" list="#{1:'净值借款',3:'信用借款',4:'实地考察借款',5:'机构担保借款'}"  listKey="key" listValue="value"></s:checkboxlist>
</td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td style="padding-top:5px;">
<%--    <a href="javascript:void(0);" id="setbtn" class="bcbtn"><s:if test="%{automaticBidMap.bidStatus ==2}">关闭自动投标</s:if><s:else>开启自动投标</s:else></a>--%>
    <%-- <span><input type="button" value="<s:if test='%{automaticBidMap.bidStatus ==2}'>关闭自动投标</s:if><s:else>开启自动投标</s:else>" class="bcbtn" id="setbtn"/></span> --%>
    <span><input type="button" id="savebtn" class="bcbtn" value="保存设置" />
<%--    <a href="javascript:void(0);" id="savebtn" class="bcbtn">保存设置</a></td>--%>
  </tr>
    </table>
    </div>
    <p class="tips" style="margin-top:15px;text-align: left; color: f8f8f8"><strong>自动投标工具说明</strong><br/>
    
1、贷款进入招标中十五分钟后，才会启动自动投标。<br/>

2、投标进度达到95%时停止自动投标。若投标后投标进度超过95%，则按照投标进度达到95%的金额向下取50的倍数金额值投标。<br/>

3、单笔投标金额若超过该标贷款总额的20%，则按照20%比例的金额向下取50的倍数金额值投标。<br/>

4、满足自动投标规则的金额小于设定的每次投标金额，也会进行自动投标。<br/>

5、贷款用户在获得贷款时会自动关闭自动投标，以避免借款被用作自动投标资金。<br/>

6、投标排序规则如下：<br/>

<span style="padding-left: 30px;">a）投标序列按照开启自动投标的时间先后进行排序。</span><br/>
<span style="padding-left: 30px;">b）每个用户每个标仅自动投标一次，投标后，排到队尾。</span><br/>
<span style="padding-left: 30px;">c）轮到用户投标时没有符合用户条件的标，也视为投标一次，重新排队。</span></p>
        </div>
</div>
    </div>
  </div>
</div>
<!-- 引用底部公共部分 -->     
<jsp:include page="/include/footer.jsp"></jsp:include>
<input type="hidden" id="s" value="${automaticBidMap.bidStatus}"/>
<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
<script>
$(function(){
    //样式选中
    	  $('#li_13').addClass('on');
	 $('#savebtn').click(function(){
	     var chk_value =[];  
         $('input[name="checkList.id"]:checked').each(function(){  
             chk_value.push($(this).val());  
         });
         param['paramMap.usableSum'] = $('#usableSum').val();
	     param['paramMap.bidAmount'] = $('#bidAmount').val();
	     param['paramMap.rateStart'] = $('#rateStart').val();
	     param['paramMap.rateEnd'] = $("#rateEnd").val();
	     param['paramMap.deadlineStart'] = $('#deadlineStart').val();
	     param['paramMap.deadlineEnd'] = $('#deadlineEnd').val();
	     param['paramMap.creditStart'] = $('#creditStart').val();
	     param['paramMap.creditEnd'] = $('#creditEnd').val();
	     param['paramMap.remandAmount'] = $('#remandAmount').val();
	     param['paramMap.borrowWay']=''+chk_value;
	     $.shovePost('automaticBidModify.do',param,function(data){
		   alert(data.msg);
		 });
	 }); 
<%--	 $('#setbtn').click(function(){--%>
<%--	     var str = $('#s').val();--%>
<%--	     param['paramMap.s']=str;--%>
<%--	     $.shovePost('automaticBidSet.do',param,function(data){--%>
<%--		   if(data.msg == 1){--%>
<%--		      alert('操作成功');--%>
<%--		      window.location.href='automaticBidInit.do';--%>
<%--		      return false;--%>
<%--		   }--%>
<%--		   alert(data.msg);--%>
<%--		 });--%>
<%--	 });--%>
	 $('#setbtn').click(function(){
	     var str = $('#s').val();
		 window.location.href="pautomaticBidSet.do?s="+str;
	 });
});	     
</script>
</body>
</html>