<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>富壹代</title>
<link rel="stylesheet" type="text/css" href="../css/css.css" />
<script src="../script/jquery-1.7.1.min.js" type="text/javascript"></script>
<script>
</script>
</head>
<body>
<div class="">
<div class="nymain">
  <div class="wdzh">
    <div class="r_main">
      <div class="box">
       <h2 class="otherh2x">富壹代认证</h2>
      <div class="boxmain2">
       <div class="biaoge" style="margin-top:0px;">

          </div>
          <div class="biaoge" >
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
    <th colspan="4">${map.usrename} &nbsp;&nbsp;&nbsp;&nbsp;信用信息</th>
    </tr>
  <tr>
    <th colspan="4"> 信用总分:<a>${map.creditrating}</a>&nbsp;&nbsp;&nbsp;&nbsp;   信用额度 ： <a>${map.creditLimit }</a></th>
    </tr>
  <tr>
    <td align="center">&nbsp;</td>
    <td align="center">项目</td>
    <td align="center">状态</td>
    <td align="center">信用分数</td>
    </tr>
  <tr>
    <td align="center">基本信息</td>
    <td align="center">个人详细信息，工作信息</td>
    <td align="center">
     <s:if test="#request.map.tpauditStatus==1">待审核</s:if> 
     <s:elseif test="#request.map.tpauditStatus==2">不通过</s:elseif>
     <s:elseif test="#request.map.tpauditStatus==3">
              <s:if test="#request.map.twauditStatus==1">待审核</s:if>
              <s:if test="#request.map.twauditStatus==2">不通过</s:if>
              <s:if test="#request.map.twauditStatus==3">
                              <s:if test="#request.map.twdirectedStatus==1">待审核</s:if>
                              <s:elseif test="#request.map.twdirectedStatus==2">不通过</s:elseif>
                              <s:elseif test="#request.map.twdirectedStatus==3">
                                                      <s:if test="#request.map.twotherStatus==1">待审核</s:if> 
                                                      <s:elseif test="#request.map.twotherStatus==2">不通过</s:elseif>
                                                      <s:elseif test="#request.map.twotherStatus==3">
                                                                           <s:if test="#request.map.twmoredStatus==1">待审核</s:if> 
                                                                           <s:elseif test="#request.map.twmoredStatus==2">不通过</s:elseif> 
                                                                           <s:elseif test="#request.map.twmoredStatus==3">通过</s:elseif>
                                                                           <s:else>未填写</s:else>
                                                      
                                                      </s:elseif>  
                                                      <s:else>未填写</s:else>
                              
                              </s:elseif>
                              <s:else>未填写</s:else>
              
              </s:if>
              <s:else>未填写</s:else>
     </s:elseif>
     <s:else>未填写</s:else>
     
 
     
    </td>
    <td align="center">
    
    
    
        <s:if test="#request.map.tpauditStatus==1">0</s:if> 
     <s:elseif test="#request.map.tpauditStatus==2">0</s:elseif>
     <s:elseif test="#request.map.tpauditStatus==3">
              <s:if test="#request.map.twauditStatus==1">0</s:if>
              <s:if test="#request.map.twauditStatus==2">0</s:if>
              <s:if test="#request.map.twauditStatus==3">
                              <s:if test="#request.map.twdirectedStatus==1">0</s:if>
                              <s:elseif test="#request.map.twdirectedStatus==2">0</s:elseif>
                              <s:elseif test="#request.map.twdirectedStatus==3">
                                                      <s:if test="#request.map.twotherStatus==1">0</s:if> 
                                                      <s:elseif test="#request.map.twotherStatus==2">0</s:elseif>
                                                      <s:elseif test="#request.map.twotherStatus==3">
                                                                           <s:if test="#request.map.twmoredStatus==1">0</s:if> 
                                                                           <s:elseif test="#request.map.twmoredStatus==2">0</s:elseif> 
                                                                           <s:elseif test="#request.map.twmoredStatus==3">10</s:elseif>
                                                                           <s:else>0</s:else>
                                                      
                                                      </s:elseif>  
                                                      <s:else>0</s:else>
                              
                              </s:elseif>
                              <s:else>0</s:else>
              
              </s:if>
              <s:else>0</s:else>
     </s:elseif>
     <s:else>0</s:else>
    
    
    
    
    </td>
    </tr>
  <tr>
    <td rowspan="5" align="center">必要信用认证</td>
    <td align="center">身份认证</td>
    <td align="center">
       <s:if test="#request.map.tmIdentityauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmIdentityauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmIdentityauditStatus==1">待审核</s:elseif>
      <s:elseif test="#request.map.tmIdentityauditStatus==3">通过</s:elseif>
      <s:else>待上传</s:else>
    </td>
    <td align="center">
     <s:if test="#request.map.tmidentiycriditing==null">
         0
      </s:if>
      <s:else> ${map.tmidentiycriditing }</s:else>
    
    </td>
    </tr>
  <tr>
    <td align="center">工作认证</td>
    <td align="center">
    
       <s:if test="#request.map.tmworkauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmworkauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmworkauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
      
      
      
    
    
    </td>
    <td align="center">
    
    
     <s:if test="#request.map.tmworkcriditing==null">
         0
      </s:if>
      <s:else> ${map.tmworkcriditing }</s:else>
    
    
    
    </td>
    </tr>
  <tr>
    <td align="center">收入认证</td>
    <td align="center">
       

      <s:if test="#request.map.tmincomeeauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmincomeeauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmincomeeauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    
    </td>
    <td align="center">
    
    
    <s:if test="#request.map.tmincomeecriditing==null">
         0
      </s:if>
      <s:else> ${map.tmincomeecriditing }</s:else>
    
    
    </td>
    </tr>
  <tr>
    <td align="center">信用报告认证</td>
    <td align="center">

        <s:if test="#request.map.tmresponseauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmresponseauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmresponseauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    </td>
    <td align="center">
    
    
      
       <s:if test="#request.map.tmresponsecriditing==null">
         0
      </s:if>
      <s:else> ${map.tmresponsecriditing }</s:else>
    
    
    </td>
    </tr>
  <tr>
    <td align="center">居住认证</td>
    <td align="center">
    
 
          <s:if test="#request.map.tmaddressauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmaddressauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmaddressauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    
    </td>
    <td align="center">
    
    
 
       <s:if test="#request.map.tmaddresscriditing==null">
         0
      </s:if>
      <s:else> ${map.tmaddresscriditing }</s:else>
    
    
    </td>
    </tr>
  <tr>
    <td rowspan="10" align="center">可选认证</td>
    <td align="center">房产</td>
    <td align="center">
    
          <s:if test="#request.map.tmhouseauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmhouseauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmhouseauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    
    
    
    
    </td>
    <td align="center">
  
  
    <s:if test="#request.map.tmhousecriditing==null">
         0
      </s:if>
      <s:else> ${map.tmhousecriditing }</s:else>
    
    </td>
  </tr>
  <tr>
    <td align="center">购车</td>
    <td align="center">
       <s:if test="#request.map.tmcarauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmcarauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmcarauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    
    </td>
    <td align="center">
 <s:if test="#request.map.tmcarcriditing==null">
         0
      </s:if>
      <s:else> ${map.tmcarcriditing }</s:else>
    
    
    </td>
  </tr>
  <tr>
    <td align="center">结婚</td>
    <td align="center">
    

           <s:if test="#request.map.tmmerrayauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmmerrayauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmmerrayauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    
    </td>
    <td align="center">
  <s:if test="#request.map.tmmerraycriditing==null">
         0
      </s:if>
      <s:else> ${map.tmmerraycriditing }</s:else>
    </td>
  </tr>
  <tr>
    <td align="center">学历</td>
    <td align="center">
    
          <s:if test="#request.map.tmxueliauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmxueliauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmxueliauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    </td>
    <td align="center">
    
    <s:if test="#request.map.tmxuelicriditing==null">
         0
      </s:if>
      <s:else> ${map.tmxuelicriditing }</s:else>
    
    </td>
  </tr>
  <tr>
    <td align="center">技术</td>
    <td align="center">
  
            <s:if test="#request.map.tmjishuauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmjishuauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmjishuauditStatus==1">待审核</s:elseif>
       <s:else>待上传</s:else>
    
    
    </td>
    <td align="center">
  <s:if test="#request.map.tmjishucriditing==null">
         0
      </s:if>
      <s:else> ${map.tmjishucriditing }</s:else>
</td>
  </tr>
  <tr>
    <td align="center">手机</td>
    <td align="center">
          <s:if test="#request.map.tmtelephoneauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmtelephoneauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmtelephoneauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>

</td>
    <td align="center">
 <s:if test="#request.map.tmtelephonecriditing==null">
         0
      </s:if>
      <s:else> ${map.tmtelephonecriditing }</s:else>
    
    
    </td>
    </tr>
  <tr>
    <td align="center">微博</td>
    <td align="center">
          <s:if test="#request.map.tmweiboauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmweiboauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmweiboauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    </td>
    <td align="center">
   <s:if test="#request.map.tmweibocriditing==null">
         0
      </s:if>
      <s:else> ${map.tmweibocriditing }</s:else>
    
    
    </td>
    </tr>
    <!-- 
  <tr>
    <td align="center">视频</td>
    <td align="center">
     
      <s:if test="#request.map.tmshipingauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmshipingauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmshipingauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
     

    </td>
    <td align="center">
    
 ${map.tmshipingcriditing }
    
    </td>
    </tr>
     -->
  <tr>
    <td align="center">现场</td>
    <td align="center">
       <s:if test="#request.map.tmxcauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmxcauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmxcauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    </td>
    <td align="center">
     <s:if test="#request.map.tmxcriditing==null">
         0
      </s:if>
      <s:else> ${map.tmxcriditing }</s:else>
    
    </td>
    </tr>
  <tr>
    <td align="center">抵押</td>
    <td align="center">
       <s:if test="#request.map.tmdiyaauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmdiyaauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmdiyaauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    </td>
    <td align="center">
    <s:if test="#request.map.tmdiyacriditing==null">
         0
      </s:if>
      <s:else> ${map.tmdiyacriditing }</s:else>
    
    
    </td>
 
      <tr>
          <td align="center">担保</td>
    <td align="center">
       <s:if test="#request.map.tmdanbaoauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmdanbaoauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmdanbaoauditStatus==1">待审核</s:elseif>
      <s:else>待上传</s:else>
    
    </td>
    <td align="center">
    
   <s:if test="#request.map.tmdanbaocriditing==null">
         0
      </s:if>
      <s:else> ${map.tmdanbaocriditing }</s:else>
    
    
    </td>
    </tr> 
    
    
  
 
  
 <!--  
  <tr>
    <td rowspan="8" align="center">基本认证</td>
    <th align="center">项目</th>
    <th align="center">次数</th>
    <th align="center">分数</th>
  </tr>
  <tr>
    <td align="center">提前还款</td>
    <td align="center">2</td>
    <td align="center">20分</td>
  </tr>
  <tr>
    <td align="center">按时还款</td>
    <td align="center">2</td>
    <td align="center">20分</td>
    </tr>
  <tr>
    <td align="center">迟还款（逾期一天以上至10天以内的还款）</td>
    <td align="center">2</td>
    <td align="center">20分</td>
    </tr>
  <tr>
    <td align="center">逾期还款（11-30天）</td>
    <td align="center">2</td>
    <td align="center">20分</td>
    </tr>
  <tr>
    <td align="center">逾期还款（逾期31天以上至90天以内）</td>
    <td align="center">2</td>
    <td align="center">20分</td>
    </tr>
  <tr>
    <td align="center">逾期还款（逾期90天以上的还款）</td>
    <td align="center">2</td>
    <td align="center">20分</td>
    </tr>
    -->
          </table>

          </div>
       </div>
</div>
    </div>
  </div>
</div>
  </div>
</body>
</html>
