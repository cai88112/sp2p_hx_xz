<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
<head>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<style type="text/css">

.fullbg{
background:url(images/Catch.gif) center center #969d9f no-repeat;
display:none;
z-index:3;
position:fixed;
left:0px;
top:0px;
width:100%;
height:100%;
filter:Alpha(Opacity=30);
/* IE */
-moz-opacity:0.4;
/* Moz + FF */
opacity: 0.4;
}

</style>

<style>
.rmainbox .tcbox .tcmain .ysctab table tr td {
 font-size:  12px;
}
.nymain a{
 font-size:  12px;
}
.scbtn{
 font-size:  12px;
}
</style>
<script src="script/jquery-1.7.1.min.js" type="text/javascript"></script>
<script type="text/javascript" src="script/jbox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="script/jbox/jquery.jBox-zh-CN.js"></script>
<script>
function showloading(){
    jQuery("body").append("<div class='fullbg'></div>");
    jQuery(".fullbg").css("display","block");
}

// 隐藏层
function hideloading(){
    jQuery(".fullbg").css("display","none");
}
</script>
  <script>
$(function(){
	$('.tabmain').find('li').click(function(){
	$('.tabmain').find('li').removeClass('on');
	$(this).addClass('on');
	$('.lcmain_l').children('div').hide();
    $('.lcmain_l').children('div').eq($(this).index()).show();
	})
	})
</script>
<script>
$(function(){
	$('.til01 li').click(
	function(){
		$('.til01 li').removeClass('on');
		$(this).addClass('on');
		$('.rmainbox').children('div').hide();
		$('.rmainbox').children('div').eq($(this).index()).show();
	}
	)
})
</script>
<script> 
function dd(data){
/*
 var i = $.layer({
    type : 1,
     //move : ['#ddss'+data , true],
    title : ['图片查看',false],
    fix : false,
    offset:['50%' , '50%'],
    area : ['400px','400px'],
    page : {dom : '#ddss'+data}
});
*/
//获取img 的scr
//$("#ddss"+data+" img").attr("src");
//alert($("#ddss"+data+" img").attr("src"));
window.parent.showbigpictur($("#ddss"+data+" img").attr("src"))//调用父类的方法
}

$('#gggd').on('click',function(){
 layer.close(i);
});

</script> 
<script>
$(function(){
	$('#scbtn01').click(
	function(){
		$('.tcbox').show();
	}
	)
	$('#gbtck').click(
	function(){
		$('.tcbox').hide();
	}
	)
})
</script>
</head>
<body>
<div class="nymain" style="width: 540px;margin:0px;">
  <div class="bigbox" style="border:none;">
  <div class="sqdk" style="background:none;height:auto;padding: 0px;">
    <div class="r-main" style="border:none; float:none; width:auto;">
    <div class="rmainbox" style="padding-left:0px; padding-right:0px; padding:0px;">
    <div class="tcbox" style="display:-none; position:static; margin:0px auto; padding:0px;">
      <div class="tcmain">
       
      <s:if test="#request.typmap.materAuthTypeId==6">
            <h3>房产认证：</h3>
      <p>房产证明是证明借入者资产及还款能力的重要凭证,富壹代会根据借款者提供的房产证明给与借入者一定的信用加分。</p>
      <p><strong>认证说明：</strong><br />
1、 请上传以下任意一项或多项资料。 <br>
a) 购房合同以及发票<br>
b) 银行按揭贷款合同 <br>
c) 房产局产调单及收据	
<br />
<p>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==1">
            <h3>身份认证：</h3>
      <p>您上传的身份证扫描件需和您绑定的身份证一致，否则将无法通过认证。</p>
      <p><strong>认证说明：</strong><br />
1.请您上传本人身份证原件的正、反两面的第二代身份证照片。 <br>
2.户口本，须包括全家户口信息；<br>
3.本人近期生活照；<br>
4. 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M。
<br />
</p>
</s:if>
      <s:if test="#request.typmap.materAuthTypeId==2">
            <h3>工作认证：</h3>
      <p>您的工作状况是富壹代评估您信用状况的主要依据。请您填写真实可靠的工作信息。 </p>
      <p><strong>认证说明：</strong><br />
      
  1、工薪阶层（入职须6个月以上）：<br>
  a）为必须上传资料，b）和c）任选一项或者两项<br>
  a）正式劳动合同<br>
b）盖有单位公章的在职证明<br>
c）带有姓名及照片的工作证<br>
  2、企业私营业主（经营须一年以上）：<br>
  请上传以下全部资料的照片<br>
  a）企业的营业执照（彩色）<br>
b）企业的税务登记证<br>
c）企业的机构代码证<br>
d）店面照片（照片内需能看见营业执照）<br>
<p>
3、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==3">
            <h3>居住地认证：</h3>
      <p>居住地的稳定性，是富壹代考核借款人的主要评估因素之一，通过富壹代居住地证明，您将获得一定的信用加分。 </p>
      <p><strong>认证说明：</strong><br />
1、请上传以下可证明现居住地址的证明文件原件的照片。 <br>
a）居住证（暂住证）<br>
b）房屋租赁合同<br>
c）近3个月的水、电、煤气缴费单据<br>
<br />
<p>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==4">
            <h3>央行信用认证：</h3>
      <p>个人信用报告是由中国人民银行出具，全面记录个人信用活动，反映个人信用基本状况的文件。本报告是富壹代了解您信用状况的一个重要参考资料。 您信用报告内体现的信用记录，和信用卡额度等数据，将在您发布借款时经富壹代工作人员整理，在充分保护您隐私的前提下披露给富壹代借出者，作为借出者投标的依据。</p>
      <p><strong>认证说明：</strong><br />
1、上传近20日内的央行个人信用报告 <br>
2、上传您的个人信用报告原件的照片，每页信用报告须独立照相，并将整份信用报告按页码先后顺序完整上传。<br>
<br />
<p>
3、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==5">
            <h3>收入认证：</h3>
      <p>您的银行流水单以及完税证明，是证明您收入情况的主要文件，也是富壹代评估您还款能力的主要依据之一。 </p>
      <p><strong>认证说明：</strong><br />
      1、工薪阶层（入职须6个月以上）：<br />
请上传以下一项或者多项资料的照片<br />
a)近半年有工资、奖金等收入证明的银行流水网银电脑截图，须有同时显示客户姓名和账号的界面<br />
b)社保卡正反面原件的照片以及最近连续六个月缴费记录<br />
c)如果工资用现金形式发放，请提供近半年的常用银行储蓄账户流水单，须有银行盖章<br />
 2、企业私营业主（经营须一年以上）：<br />
请上传以下一项或者多项资料的照片<br />
a)近半年企业银行流水网银电脑截图，须有同时显示企业名称和账号的界面或者纸质流水，须有银行盖章<br />
b)近半年个人银行流水网银电脑截图，须有同时显示企业名称和账号的界面或者纸质流水，须有银行盖章<br />
<br />
<p>
3、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==7">
            <h3>车辆认证：</h3>
      <p>购车证明是证明借入者资产及还款能力的重要凭证之一，富壹代会根据借入者提供的购车证明给与借入者一定的信用加分。</p>
      <p><strong>认证说明：</strong><br />
1、请您上传购买车辆的车辆登记证、行驶证原件照片；<br />2、请您上传购买车辆的发票或者银行按揭合同；
<br />
3、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M<br />

</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==8">
            <h3>结婚认证：</h3>
      <p>借入者的婚姻状况的稳定性，是富壹代考核借款人信用的评估因素之一，通过富壹代结婚认证，您将获得一定的信用加分。</p>
      <p><strong>认证说明：</strong><br />
 1、	请您上传以下资料<br />
a）您本人结婚证原件照片<br />b）您配偶正面、反面第二代身份证原件照片
<br />c）您和配偶的近照合影一张
<br />
<p>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==9">
            <h3>学历认证：</h3>
      <p>借出者在选择借款列表投标时，借入者的学历也是一个重要的参考因素。为了让借出者更好、更快地相信您的学历是真实的，强烈建议您对学历进行在线验证。</p>
      <p><strong>认证说明：</strong><br />
1、您上传的资料须是学信网您个人学历信息截图；
<br />
<p>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==10">
            <h3>技术职称认证：</h3>
      <p>技术职称是经专家评审、反映一个人专业技术水平并作为聘任专业技术职务依据的一种资格，不与工资挂钩，是富壹代考核借款人信用的评估因素之一，通过富壹代技术职称认证证明，您将获得一定的信用加分。</p>
      <p><strong>认证说明：</strong><br />
1、请上传技术职称证书（国家承认的二级及以上等级证书，如会计证、律师证、工程证书等）；
<br />
<p>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==11">
            <h3>手机认证：</h3>
      <p>手机流水单是最近一段时间内的详细通话记录，是富壹代用以验证借入者真实性的重要凭证之一。您的手机详单不会以任何形式被泄露。</p>
      <p><strong>认证说明：</strong><br />
1、请您上传您最近6个月的手机通话记录清单，用户姓名与号码须显示在同一界面；
<br />
<p>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
 
      <s:if test="#request.typmap.materAuthTypeId==12">
            <h3>微博认证：</h3>
      <p><strong>认证说明：</strong><br />
<br />
1、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>

      <s:if test="#request.typmap.materAuthTypeId==13">
            <h3>现场认证：</h3><br/>
           <p> 请上传盖有富壹代公章的现场认证书</p>
      <p><strong>认证说明：</strong><br />
1、该认证书是考察人员现场考察后发放的<br/>
2、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
      <s:if test="#request.typmap.materAuthTypeId==14">
            <h3>抵押认证：</h3>
      <p><strong>认证说明：</strong><br />
<br />
1、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
      <s:if test="#request.typmap.materAuthTypeId==15">
            <h3>担保认证：</h3>
      <p><strong>认证说明：</strong><br />
<br />
1、 请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M
</p>
</s:if>
<s:if test="#request.typmap.materAuthTypeId==16">
            <h3>其他资料：</h3>
   <!--     <p><strong>认证说明：</strong><br /> -->
   <p>
 其他能说明您收入、资产、职务或素质的有效资料（凡不属于以上内容的都放在此）<br />
 认证说明：<br />
 1、这些资料必须属于您本人<br />
 2、请确认您上传的资料是清晰的、未经修改的照片，每张照片最大限制为1M<br />
</p>
</s:if>
    

      <h3>已上传资料：</h3>
      <div class="ysctab">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" id="sssd">
  <tr id="fg">
    <th align="center">选中</th>
    <th align="center">序号</th>
    <th align="center">名称</th>
    <th align="center">图片</th>
    <th align="center">状态</th>
    <th align="center">操作</th>
  </tr>
  <s:if test="#request.userPicDate==null || request.userPicDate.size == 0 ">
					<tr align="center" class="gvItem">
						<td colspan="6">暂无数据</td>
					</tr>
				</s:if>
				<s:else>
  <s:iterator value="#request.userPicDate" var="bean" status="st">
  <tr  id='s_${st.count }'>
   <!--  
    <input type="hidden" name="hii" value="${imagePath }" id="h_${st.count }" chd=""/>
    -->
    <td align="center"><input type="checkbox" name="items" id="${st.count }" value="${id}"     <s:if test="#bean.visiable==1">checked="checked"</s:if> /></td>
    <td align="center">${st.count }</td>
    <td align="center">${tmyname }${st.count }</td>
    <td align="center" class="pic" >
    
    <!-- 点击放大 
    <a href="javascript:dd(${st.count})" id="dds${st.count }"  title="查看图片"> <img  src="${imagePath }" width="62" height="62"/></a>
    -->
       <a href="${imagePath }" target="_blank" title="查看图片"><img  src="${imagePath }" width="62" height="62"/></a>
    </td>
    <td align="center">
    <s:if test="#bean.auditStatus==1">审核中</s:if>
    <s:if test="#bean.auditStatus==2">失败</s:if>
    <s:if test="#bean.auditStatus==3">通过</s:if>
    </td>
    <td align="center">
    -
    </td>
  </tr>
  </s:iterator>
  </s:else>
    <tr id="ggggs">
    <td colspan="5" style="border:none; padding-top:10px;">
    <a style="padding-left:6.5%">
    <input type="checkbox" name="checkbox4" id="select"  /> 全选
    </a>
   <a style="color: red"> 提示：选中资料可见</a>
    <!--  
      <a style="cursor: pointer;" class="yzmbtn" id="sub">确认选中资料可见</a>
      -->
    </td>
    </tr>
</table >

      </div>
      <div class="xzzl">
      <form action="addpastPicturdate.do" id="myform" method="post">
       <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr id="mytable">
    <th align="left" width="19%">选择资料：</th>
    <td align="left">
    <input type="hidden" value="${tmid }" name="paramMap.tm">
    <input type="hidden" value="${typmap.materAuthTypeId}" name="paramMap.materAuthTypeId">
    <!-- 
    <input type="text" class="inp188" name='rmlink' id="img1"  disabled="disabled"/> 
     -->
      <input type="button" class="bcbtn" value="上传" id="shanc" onclick="updateFiledatefirest(this)" ></td>
  </tr>
  <!--  
  <tr id="mytable">
    <td>&nbsp;</td>
    <td><input type="text" class="inp188" />
      <a href="#" class="scbtn">浏览</a>
      <a href="#" class="shanchu">删除</a></td>
  </tr>
  -->
  <tr>
    <td>&nbsp;</td>
    <td style="padding-top:20px;" align="center">
    <input type="button" value="提交审核" class="bcbtn" id="bt_sub">
    </td>
  </tr>
   <tr style="width: 100%">
    <td valign="top"><img src="images/warning.png" ></td>
    <td  style="color: red;font-size: 12px" >富壹代是一个注重诚信的网络平台。如果我们发现您上传的资料系伪造或有人工修改痕迹，富壹代会将你加入系统黑名单，永久取消您在富壹代的借款资格。</td>
  </tr>
</table>
</form>
 <!-- <table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
    <td>&nbsp;</td>
    <!-- 
    <td><a  href='javascript:void(0)' class="shanchu" id="addInput">添加一个附件</a></td> -->
  <!-- </tr>
  <tr>
  <p></p>
    <td>&nbsp;</td>
    <td style="padding-top:20px;" align="center">
    <input type="button" value="提交审核" class="bcbtn" id="bt_sub">
    </td>
  </tr>
   <tr style="width: 100%">
    <td valign="top"><img src="images/warning.png" ></td>
    <td  style="color: red;font-size: 12px" >富壹代是一个注重诚信的网络平台。如果我们发现您上传的资料系伪造或有人工修改痕迹，富壹代会将你加入系统黑名单，永久取消您在富壹代的借款资格。</td>
  </tr>
  
</table> -->
      </div>
      </div>
    </div>
    </div>
    </div>
  </div>
  </div>
</div>
</body>
<script>

</script>
<script>
//放大图片
function ddimg(data){
/*
 var i = $.layer({
    type : 1,
  //   move : ['#ddssa'+data , true],
    title : ['图片查看',false],
    fix : false,
    offset:['50%' , '50%'],
    area : ['400px','400px'],
    page : {dom : '#ddssa'+data}
});
*/
window.parent.showbigpictur($("#ddssa"+data+" img").attr("src"))//调用父类的方法
}

</script>
<script>
function chan(data){
  var va =  $(data).attr("id");
  var gee = $("#hh_"+va).attr("value");
 
  if(data.checked){
     $(data).attr("value",gee+".v");
   }else{
     $(data).attr("value",gee);
   }
}
</script>
<script>
//上传资料 - 提交图片资料审核
$(function(){
  var parama = {};
  var typed ='${typmap.materAuthTypeId}';
  $("#bt_sub").click(function(){
    var visable1 = 0;
    var visable3 = 0;
   //获取本来在数据库的图片获取他们的id
     $("input[name=items]").each(function() { 
       visable3++;
     });
   
    $("input[name=items]").each(function() { 
      if ($(this).attr("checked")) {  
                visable1++;
                parama["paramMap.id"+visable1] = $(this).val();
            }  
     });
     //取上传到tomcat的图片的地址和是否选择
       var visable2 = 0;
       $("input[name=itemss]").each(function() { 
       //获取图片选矿的值
         visable2++;
         parama["paramMap.ids"+visable2] = $(this).val();
     });
     //控制上传个数
     if(typed=='1'){//身份证
       if((visable2+visable3)>5){
        alert("身份证认证图片只能上传5张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
        if(typed=='2'){//工作
       if((visable2+visable3)>10){
        alert("工作认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
        if(typed=='3'){//居住认证
       if((visable2+visable3)>5){
        alert("居住认证图片只能上传5张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
        if(typed=='4'){//收入认证
       if((visable2+visable3)>30){
        alert("收入认证图片只能上传30张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
        if(typed=='5'){//信用报告
       if((visable2+visable3)>10){
        alert("信用报告认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
        if(typed=='6'){//房产
       if((visable2+visable3)>10){
        alert("房产认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
        if(typed=='7'){//购车
       if((visable2+visable3)>10){
        alert("购车认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
             if(typed=='8'){//结婚
       if((visable2+visable3)>5){
        alert("结婚认证图片只能上传5张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
             if(typed=='9'){//学历
       if((visable2+visable3)>5){
        alert("学历认证图片只能上传5张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
               if(typed=='10'){//技术
       if((visable2+visable3)>10){
        alert("技术认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
               if(typed=='11'){//手机
       if((visable2+visable3)>5){
        alert("手机认证图片只能上传5张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
               if(typed=='12'){//微博
       if((visable2+visable3)>5){
        alert("微博认证图片只能上传5张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
                   if(typed=='13'){//现场认证
       if((visable2+visable3)>10){
        alert("现场认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
                   if(typed=='14'){//抵押认证
       if((visable2+visable3)>10){
        alert("抵押认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
     }
                        if(typed=='15'){//机构担保
       if((visable2+visable3)>10){
        alert("机构担保认证图片只能上传10张,如需再上传,请联系客服!谢谢");
        return false;
       }
       
      
     }
      if(typed=='16'){//其他资料
       if((visable2+visable3)>30){
        alert("其他资料认证图片只能上传30张,如需再上传,请联系客服!谢谢");
        return false;
       }
      }
     
     //将数据传到后台处理
     parama["paramMap.tmid"] ='${tmid }';
     parama["paramMap.materAuthTypeId"] = '${typmap.materAuthTypeId}';
     parama["paramMap.listlen"] = '${len}';
      parama["paramMap.len"] = visable2;//将要上传的图片个数
     $.post("addpastPicturdate.do",parama,function(data){
      if(data==123){
      alert('申请认证成功');
      window.parent.ffff()//关闭弹出窗口
    }    
     });   
  });
});
</script>

<script>
//保存图片
$(function(){
  var paramimg = {};
  var gimg = 0;
  $("#bt_sub_1").click(function(){
    //$("#myform").submit();
    $("input[name=rmlink]").each(function() {  
            gimg++;
            paramimg["paramMap.img"+gimg] = $(this).val();
      }); 
      
    paramimg["paramMap.tmid"] ='${tmid }';
    paramimg["paramMap.materAuthTypeId"] = '${typmap.materAuthTypeId}';
    paramimg["paramMap.len"] = gimg;
    paramimg["paramMap.listlen"] = '${len}';
    $.post("addpastPicturdate.do",paramimg,function(data){
    if(data==1){ alert("身份证只能上传5张图片");}
    if(data==2){  alert("工作认证只能上传10张图片");}
    if(data==3){ alert("居住地认证只能上传5张图片");}
    if(data==4){ alert("收入认证只能上传30张图片");}
    if(data==5){ alert("信用报告只能上传10张图片");}
    if(data==6){  alert("房产认证只能上传10张图片");}
    if(data==7){ alert("购车认证只能上传5张图片");}
    if(data==8){  alert("结婚认证只能上传5张图片");}
    if(data==9){alert("学历认证只能上传5张图片");}
    if(data==10){alert("技术认证只能上传5张图片");}
    if(data==11){alert("手机验证只能上传5张图片");}
    if(data==12){ alert("微博认证只能上传5张图片");}
    if(data==13){ alert("视频认证只能上传5张图片"); }
    if(data==14){alert("现场认证只能上传10张图片");}
    if(data==15){alert("抵押认证只能上传10张图片");}
    if(data==16){ alert("机构担保认证只能上传5张图片");}
    if(data==17){ alert("上传失败");}
    if(data==18){ alert("上传失败");}
    if(data==321){ alert("上传失败");}
    if(data==123){
     window.parent.ffff()//关闭弹出窗口
    }
   })   
   gimg = 0 ;     
  });
});

</script>
<script>
$(function(){
 $("#dd").click(function(){
 var ttt = $("#tat").val();
 window.parent.ffff(ttt,'${id}')
 });
});
</script>
<script>
//删除图片
function deltemp(data){
 var dt = $(data).attr("id")
 $("#s_"+dt).remove();
}
</script>
<script >
//上传图片
        var flag = false;
        var gv1 = 0 ;
        var gv2 = 0 ;
        var allgv = 0;
		
			function updateFiledatefirest(data){
			showloading();
			var dir = getDirNum();
			var json = "{'fileType':'JPG,BMP,GIF,TIF,PNG','fileSource':'user/"+dir+"','fileLimitSize':1,'title':'上传图片','cfn':'uploadCall2','cp':'img'}";
			json = encodeURIComponent(json);
		    window.showModalDialog("uploadFileAction.do?obj="+json,window,"dialogWidth=500px;dialogHeight=400px");
					//var headImgPath = $("#img").attr("value");
				   //if(headImgPath!=""){
			       //}
			hideloading();
		    }

		function uploadCall2(basepath,fileName,cp){
		   		//检测上传个数 如果达到了上传上限 那么给予提示
			$("input[name=items]").each(function() {  
                  gv1++;
            }); 
			 $("input[name=itemss]").each(function() {  
                 gv2++;
             });  
			
			allgv = gv1+gv2;
		  
		  	if(cp == "img"){
		  	var path = "upload/"+basepath+"/"+fileName;
		  		//$("#img1").attr("value",path);
		  		//处理文件上传
		  	var Idd = $('#sssd tr:last').prev().attr("id");
		  	var te =  parseInt($("#"+Idd+" td:eq(1)").text());
		  	if(isNaN(te)){
		  	te = 0;
		  	}
		  	var charStr = "";
		  	if('${typmap.materAuthTypeId}'=='1'){charStr="身份证"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='2'){charStr="工作认证"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='3'){charStr="居住地认证"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='4'){charStr="信用报告"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='5'){charStr="收入认证"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='6'){charStr="房产"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='7'){charStr="购车"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='8'){charStr="结婚"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='9'){charStr="学历"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='10'){charStr="技术"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='11'){charStr="手机"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='12'){charStr="微博"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='13'){charStr="现场"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='14'){charStr="抵押认证"+(te+1)}
		  	if('${typmap.materAuthTypeId}'=='15'){charStr="机构担保"+(te+1)}
		  //$("#"+Idd).after('<tr  id="s_'+(allgv+1)+'"><td align="center"><input type="checkbox" name="itemss" id="'+(allgv+1)+'" value="'+path+'"  onchange="chan(this)"   /><input type="hidden" value="'+path+'" id="hh_'+(allgv+1)+'"/></td><td align="center"></td><td align="center"></td> <td align="center" class="pic" ><img src="'+path+'" width="62" height="62"/></td><td align="center"> 待提交 </td><td align="center"><a  href="javascript:void(0)"   onclick="deltemp(this)" id ="'+(allgv+1)+'">删除</a></td>');	
		  $("#"+Idd).after('<tr  id="s_'+(allgv+1)+'"><td align="center"><input type="checkbox" name="itemss" id="'+(allgv+1)+'" value="'+path+'"  onchange="chan(this)"   /><input type="hidden" value="'+path+'" id="hh_'+(allgv+1)+'"/></td><td align="center">'+(te+1)+'</td><td align="center">'+charStr+'</td> <td align="center" class="pic" ><a href="'+path+'" target="_blank" title="查看图片"><img    src="'+path+'" width="62" height="62"/></a>  <a  id="ddssa'+(allgv+1)+'" style="display: none;"> <img src="'+path+'" width="400" height="400"/></a></td><td align="center"> 待提交 </td><td align="center"><a  href="javascript:void(0)"   onclick="deltemp(this)" id ="'+(allgv+1)+'">删除</a></td>');			  
		    te = 0;
		   hideloading();
		  		}
			}
</script>
<script>
  var gggg;
			//上传函数
			function updateFiledate(data){
			  gggg = "img"+$(data).attr("id");
			  var dir = getDirNum();
					var json = "{'fileType':'JPG,BMP,GIF,TIF,PNG','fileSource':'user/"+dir+"','fileLimitSize':0.5,'title':'上传图片','cfn':'uploadCall','cp':'img'}";
					json = encodeURIComponent(json);
					 window.showModalDialog("uploadFileAction.do?obj="+json,window,"dialogWidth=500px;dialogHeight=400px");		
			}

			function uploadCall(basepath,fileName,cp){
		  		if(cp == "img"){
		  		var path = "upload/"+basepath+"/"+fileName;
				$("#"+gggg).val(path);
				return ;
		  		}
			}
			
			function getDirNum(){
		      var date = new Date();
		 	  var m = date.getMonth()+1;
		 	  var d = date.getDate();
		 	  if(m<10){
		 	  	m = "0"+m;
		 	  }
		 	  if(d<10){
		 	  	d = "0"+d;
		 	  }
		 	  var dirName = date.getFullYear()+""+m+""+d;
		 	  return dirName; 
			}			
		</script>
		
<script>
$(function() {  
    $("#select").click(function() {  
        if ($(this).attr("checked")) {  
            $("input[name=items]").each(function() {  
                $(this).attr("checked", true);  
            });  
              $("input[name=itemss]").each(function() {  
                $(this).attr("checked", true);  
                 var ip = $(this).attr("id");
                 var p =  $("#hh_"+ip).attr("value");
                 $(this).attr("value", p+".v"); 
            });  
            
        } else {  
            $("input[name=items]").each(function() {  
                $(this).attr("checked", false);  
            });  
              $("input[name=itemss]").each(function() {  
                $(this).attr("checked", false); 
                       var ip = $(this).attr("id");
                 var p =  $("#hh_"+ip).attr("value");
                 $(this).attr("value", p);  
            });
        }  
        });
 });  
</script>

<script>
  var  type  ;
   var gouble = 2;
   var gd = 1;
$(function(){

   //处理类型身份证5张 等等其他的
  $("#addInput").click(function(){
   type =${typmap.materAuthTypeId};
     
    //获取input框内的值的个数
        var countinput = 0;
         $("input[name=rmlink]").each(function() {  
           countinput++
         });         
         
   if(type == 1){
      if(countinput>=5){
      alert("身份证只能上传5张图片");
      }
      if(countinput<5){
       if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
             }
        }
        
     if(type == 2){
        if(countinput>=10){
      alert("工作认证只能上传10张图片");
      }
      if(countinput<10){
          if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }        
             }
        }  
        
      if(type == 3){
          if(countinput>=5){
      alert("居住地认证只能上传5张图片");
      }
      if(countinput<5){
           if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }      
             }
        }  
       if(type == 4){
      if(countinput>=30){
      alert("收入认证只能上传30张图片");
      }
      if(countinput<30){
          if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }        
             }
        } 
        
       if(type == 5){
      if(countinput>=10){
      alert("信用报告只能上传10张图片");
      }
      if(countinput<10){ 
      if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
             }
        }        
        
       if(type == 6){
      if(countinput>=10){
      alert("房产认证只能上传10张图片");
      }
      if(countinput<10){
          if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }       
             }
        } 
        
        
      if(type == 7){
      if(countinput>=5){
      alert("购车认证只能上传5张图片");
      }
      if(countinput<5){
       if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
      } 
        
        
      if(type == 8){
      if(countinput>=5){
      alert("结婚认证只能上传5张图片");
      }
      if(countinput<5){
         if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
        
            if(type == 9){
      if(countinput>=5){
      alert("学历认证只能上传5张图片");
      }
      if(countinput<5){
         if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
        
            if(type == 10){
      if(countinput>=5){
      alert("技术认证只能上传5张图片");
      }
      if(countinput<5){
       if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
        
        
      if(type == 11){//手机验证
      if(countinput>=5){
      alert("手机验证只能上传5张图片");
      }
      if(countinput<5){
       if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
        
      if(type == 12){
      if(countinput>=5){
      alert("微博认证只能上传5张图片");
      }
      if(countinput<5){
       if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
        if(type == 13){//视频
      if(countinput>=5){
      alert("视频认证只能上传5张图片");
      }
      if(countinput<5){
       if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
      if(type == 14){
      if(countinput>=10){
      alert("现场认证只能上传10张图片");
      }
      if(countinput<10){
        if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
        
        if(type == 15){
      if(countinput>=10){
      alert("抵押认证只能上传10张图片");
      }
      if(countinput<10){
           if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        } 
        
      if(type == 16){
      if(countinput>=10){
      alert("机构担保认证只能上传5张图片");
      }
      if(countinput<10){
         if(gd==1){
       $("#mytable").after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
         gouble++; 
          gd++;
      }else{
      $("#tr"+(gouble-1)).after("<tr  id='tr"+gouble+"'><td>&nbsp;</td><td><input type='text' class='inp188' id='img"+gouble+"' name='rmlink' disabled='disabled'/>  <a href='javascript:void(0)' class='scbtn' id='"+gouble+"' onclick='updateFiledate(this)'>浏览</a>  <a  href='javascript:void(0)' class='shanchu' onclick='del(this)' id ='"+gouble+"'>删除</a></td></tr>");
          gouble++;  
      }
         
             }
        }   
    }); 

});

</script>
<script>
 //得到选中的值，ajax操作使用  
 $(function(){
    var gv = 0 ;
     var param = {};
     $("#sub").click(function() {  
        var text="";  
        $("input[name=items]").each(function() {  
            if ($(this).attr("checked")) {  
               // alert($(this).val()); 
                gv++;
                param["paramMap.id"+gv] = $(this).val();
            }  
        });  
           param["paramMap.len"] = gv;
           if(gv!=0){
           param["paramMap.tmidStr"]= ${tmidStr};
           $.post("updatevisiable.do",param,function(data){
             alert("操作成功");
             window.location.reload();//刷新页面
              }); 
               }
           gv=0;
    }); 
 });
</script>
<script>
function del(data){
 var dt = $(data).attr("id")
 $("#tr"+dt).remove();
 gouble = $(data).attr("id");
}
</script>
<script>
function sure(data){
var llen = '${len}';
 $('#s_'+llen).after("<tr  id='s_"+(llen+1)+"> <td align='center'>--</td><td align='center'>12</td><td align='center'>5454</td><td align='center' class='pic'>img</td><td align='center'>4545<td><tr>");
}
</script>




    
    
