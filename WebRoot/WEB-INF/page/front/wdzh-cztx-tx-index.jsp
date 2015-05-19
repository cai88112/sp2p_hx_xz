<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<jsp:include page="/include/head.jsp"></jsp:include>
<link href="css/inside.css" rel="stylesheet" type="text/css" />
</head>

<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div class="nymain">
		<div class="wdzh">
			<div class="l_nav">
				<div class="box">
					<!-- 引用我的帐号主页左边栏 -->
					<%@include file="/include/left.jsp"%>
				</div>
			</div>
			<div class="r_main">
				<div class="tabtil">
					<ul>
						<li onclick="jumpUrl('queryFundrecordInit.do');">资金记录</li>
						<li onclick="jumpUrl('rechargeInit.do');">充值</li>
						<li class="on" dataIndex="true"
							onclick="jumpUrl('withdrawLoad.do');">提现</li>
					</ul>
				</div>

				<div class="box" style="display: -none;">
					<div class="boxmain2">

						<p class="tips">
							凡是在富壹代网贷充值未投标的用户，提现第三方收取本金0.25%，富壹代平台不收取提现费用。<br />
							注：1、请输入您要取出金额,系统将在1至3个工作日(国家节假日除外)之内将钱自动转入您网站上填写的银行账号。 <br />
							2、如你急需要把钱转到你的账号或者24小时之内网站未将钱转入到你的银行账号,请联系客服中心。 <br />
							3、确保您的银行账号的姓名和您的网站上的真实姓名一致。 <br /> 4、在钱打到您账号时会发一封站内信通知你。 <br />
							5、每笔提现金额至少为100元以上（备注：如101、632等）<br />
							6、每笔提现金额最高不能超过${max_withdraw}元。 <br /> 7、您目前能提取的最高额度是
							<s:property value="#request.usableSum" default="---"></s:property>
							元。
						</p>
						<div class="biaoge2">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="11%">真实姓名：</td>
									<td width="89%"><strong><s:property
												value="#request.realName" default="---"></s:property></strong></td>
								</tr>
								<tr>
									<td>电话号码：</td>
									<td><strong> <s:property
												value="#request.bindingPhone" default="---"></s:property>
									</strong><br /></td>
								</tr>
								<tr>
									<td>账户余额：</td>
									<td><strong><s:property
												value="#request.handleSum" default="---"></s:property>元 </strong></td>
								</tr>
								<tr>
									<td>可用余额：</td>
									<td><strong><s:property
												value="#request.usableSum" default="---"></s:property>元 </strong></td>
								</tr>
								<tr>
									<td>冻结总额：</td>
									<td><strong><s:property
												value="#request.freezeSum" default="---"></s:property>元 </strong></td>
								</tr>

								<tr>
									<td>提现的银行：</td>
									<td><span style="color: red; float: none;"
										class="formtips"> * 以下是绑定的银行卡信息，如果没有银行卡请先进行提现银行卡设置</span></td>
								</tr>
								<s:if test="#request.banks!=null && #request.banks.size>0">
									<s:iterator value="#request.banks" var="bean" status="sta">

										<!-- 并且银行卡的状态为绑定状态 -->
										<s:if test="#bean.cardStatus==1">
											<tr>
												<td>&nbsp;</td>
												<td><input type="radio" name="wbank"
													value="${ bean.id}" />${bean.bankName}&nbsp;&nbsp;${bean.branchBankName}</td>
											</tr>
										</s:if>
									</s:iterator>
								</s:if>

								<s:else>
									<tr>
										<td colspan="9" align="left"><a href="yhbound.do">暂未设置提现银行，请先进行设置</a>
									    </td>
									</tr>
								</s:else>
								<tr>
									<td>提现金额：<strong>*</strong></td>
									<td><input type="text" class="inp140" id="dealMoney"  placeholder="输入大于100的金额"/> <span
										style="color: red; float: none;" id="money_tip"
										class="formtips"></span></td>
								</tr>
								<tr>
									<td>交易密码：<strong>*</strong></td>
									<td><input type="password" class="inp140" id="dealpwd" placeholder="默认为登录密码" />
										<span style="color: red; float: none;" id="pwd_tip"
										class="formtips"></span></td>
								</tr>
								<tr>
									<td>验证码：&nbsp;&nbsp;<strong>*</strong></td>
									<td><input type="text" class="inp100x" id="code" /> <a
										id="clickCode" class="yzmbtn" href="javascript:void(0);">发送手机验证码</a>
										<input id="type" name="type" value="drawcash" type="hidden" />
										<span style="color: red; float: none;" id="code_tip"
										class="formtips"> <s:if test="#request.ISDEMO==1">* 演示站点不发送短信</s:if>
									</span></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td style="padding-top: 20px;"><a
										href="javascript:void(0);" id="btn_submit" class="bcbtn"
										onclick="addWithdraw();">确认提交</a></td>
								</tr>
								<tr>
									<td>&nbsp;</td>
									<td class="txt">* 温馨提示：禁止信用卡套现</td>
								</tr>
							</table>
						</div>
						<div class="biaoge" style="margin-top: 25px;">
							<span id="withdraw"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
	<script type="text/javascript" src="/script/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="script/jquery.shove-1.0.js"></script>
	<script type="text/javascript" src="script/nav-zh.js"></script>
	<script language="javascript" type="text/javascript"
		src="My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="script/tab.js"></script>
	<script>
	var flags = false;
	  //手机号码绑定
		  var timers=60;
		  var tipId;
		 
		   $(function(){
		       $("#clickCode").click(function(){
	                 var phone=${request.bindingPhone};
	                 if($("#clickCode").html()=="重新发送验证码"||$("#clickCode").html()=="发送手机验证码") {  
	                     $.post("phoneCheck.do","phone="+phone,function(datas){
                         if(datas.ret != 1){
                            alert(datas.msg);
                            return;
                         }
                         phone = datas.phone;
                         var test={};
                         test["paramMap.phone"] = phone;
                         $.post("sendSMS.do",test,function(data){
                            if(data == "virtual"){
      						window.location.href = "noPermission.do";
      						return ;
        				 }
                         if(data==1){
                           timers=60;
                           tipId=window.setInterval(timer,1000);
                         }else{
                           alert("手机验证码发送失败");
                         }
                       });
                     });
	               }                   
		       });
		   });
		   
		   //定时
		   function timer(){
		    
		       if(timers>=0){
		         $("#clickCode").html("验证码获取："+timers+"秒");
		         timers--;
		       }else{
		          window.clearInterval(tipId);
		           $("#clickCode").html("重新发送验证码");
		           $.post("removeCode.do","",function(data){});	           
		       }
		   }
		</script>

	<script type="text/javascript">
var flag = false;
  $(function(){
        $("#zh_hover").attr('class','nav_first');
	      $('#li_2').addClass('on');
		  $('.tabmain').find('li').click(function(){
		  $('.tabmain').find('li').removeClass('on');
        
       }); 
       param["pageBean.pageNum"] = 1;
	    initListInfo(param);
	 });
  
   function initListInfo(praData){
		$.shovePost("queryWithdrawList.do",praData,initCallBack);
	}
	function initCallBack(data){
		$("#withdraw").html(data);
	}
	
	function jumpUrl(obj){
       window.location.href=obj;
    }
	 
	$("#dealpwd").blur(function(){
	     if($("#dealpwd").val()==""){
	       $("#pwd_tip").html("交易密码不能为空");
	     }else{
	       $("#pwd_tip").html("");
	     }
    });
    
    $("#dealMoney").blur(function(){
	     if($("#dealMoney").val()=="" ){
	       $("#money_tip").html("提现金额不能为空");
	     }else if(!/^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test($("#dealMoney").val())){
	       $("#money_tip").html("请输入正确的提现金额，必须为大于0的数字"); 
	     }else if($("#dealMoney").val()<=100 ){
	       $("#money_tip").html("提现金额不得低于100"); 
	     }else if( $("#dealMoney").val()> ${usableSum}){
	      //alert(${usableSum});
	       $("#money_tip").html("提现金额不能大于可用余额"); 
	     }else{
	       $("#money_tip").html(""); 
	     }
    });
    
    $("#code").blur(function(){
	     if($("#code").val()==""){
	       $("#code_tip").html("验证码不能为空");
	     }else{
	       $("#code_tip").html("");
	    }
    });
    
    function addWithdraw(){
       if($("input:radio[name='wbank'][checked]").val()==undefined){
         alert("请设置或者选择提现银行卡信息");
            return;
       }else

         if($("#dealpwd").val()==""){
           alert("请填写交易密码");
             return;
         }else if($("#dealMoney").val()=="" ){
            alert("请填写提现金额");
             return;
         }
         if($("#money_tip").text() != ""){
           alert("请填写正确的金额");
           $("#dealMoney").attr("value","")
             return;
         }
        
        if(!window.confirm("确定添加提现记录")){
          return;
        }
        
         param["paramMap.dealpwd"] = $("#dealpwd").val();
         param["paramMap.money"] = $("#dealMoney").val();
         param["paramMap.cellPhone"] = '${bindingPhone}';
         param["paramMap.code"] = $("#code").val();
         param["paramMap.bankId"] = $("input:radio[name='wbank'][checked]").val();
         param["paramMap.type"] = $("#type").val();
         if(flag)
            return;
       	 $.shovePost("addWithdraw.do",param,function(data){
    	   flag = false;
	       if(data.msg == 1){
              flag  = true;
              window.clearInterval(tipId);
              $("#clickCode").html("发送手机验证码");
              alert("申请提现成功");
              jumpUrl('withdrawLoad.do');
          }else{
          	  alert(data.msg);
          }
       });
    }
    
    $("#send_phoneCode").click(function(){
		var param = {"type":"drawcash"};
		$.shovePost("sendPhoneCode.do",param,function(data){
			if(data==1){
				alert("发送成功");
			}
			alert("验证码：" + data);
		});
	});
</script>
</body>
</html>
