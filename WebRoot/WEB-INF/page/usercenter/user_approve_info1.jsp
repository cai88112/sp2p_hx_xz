 <%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/include/taglib.jsp" %>
  <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <th colspan="8">信用等级及信用对应分数</th>
    </tr>
  <tr>
    <td align="center">等级</td>
    <td align="center">AA</td>
    <td align="center">A</td>
    <td align="center">B</td>
    <td align="center">C</td>
    <td align="center">D</td>
    <td align="center">E</td>
    <td align="center">HR</td>
    </tr>
  <tr>
    <td align="center">分数</td>
    <td align="center">100以上</td>
    <td align="center">99-90</td>
    <td align="center">89-80</td>
    <td align="center">79-70</td>
    <td align="center">69-50</td>
    <td align="center">49-30</td>
    <td align="center">30以下</td>
    </tr>
  <tr>
    <td align="center">标志</td>
    <td align="center"><img src="images/ico_15.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_13.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_11.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_09.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_07.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_05.jpg" width="34" height="22" /></td>
    <td align="center"><img src="images/ico_03.jpg" width="34" height="22" /></td>
    </tr>
          </table>
          <div class="biaoge" >
           <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
    <th colspan="4">信用总分：${map.creditrating}分&nbsp;<img src="images/ico_${criditMap.credit}.jpg" title="${map.creditrating}分" width="33" height="22" /> </th>
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
     <s:elseif test="#request.map.tpauditStatus==2"><a href="owerInformationInit.do">不通过</a></s:elseif>
     <s:elseif test="#request.map.tpauditStatus==3">
              <s:if test="#request.map.twauditStatus==1">待审核</s:if>
              <s:elseif test="#request.map.twauditStatus==2"><a href="owerInformationInit.do">不通过</a></s:elseif>
              <s:elseif test="#request.map.twauditStatus==3">
                              <s:if test="#request.map.twdirectedStatus==1">待审核</s:if>
                              <s:elseif test="#request.map.twdirectedStatus==2"><a href="owerInformationInit.do">不通过</a></s:elseif>
                              <s:elseif test="#request.map.twdirectedStatus==3">
                                                      <s:if test="#request.map.twotherStatus==1">待审核</s:if> 
                                                      <s:elseif test="#request.map.twotherStatus==2"><a href="owerInformationInit.do">不通过</a></s:elseif>
                                                      <s:elseif test="#request.map.twotherStatus==3">
                                                                           <s:if test="#request.map.twmoredStatus==1">待审核</s:if> 
                                                                           <s:elseif test="#request.map.twmoredStatus==2"><a href="owerInformationInit.do">不通过</a></s:elseif> 
                                                                           <s:elseif test="#request.map.twmoredStatus==3">通过</s:elseif>
                                                                           <s:else><a href="owerInformationInit.do">未填写</a></s:else>
                                                      
                                                      </s:elseif>  
                                                      <s:else><a href="owerInformationInit.do">未填写</a></s:else>
                              
                              </s:elseif>
                              <s:else><a href="owerInformationInit.do">未填写</a></s:else>
              
              </s:elseif>
              <s:else><a href="owerInformationInit.do">未填写</a></s:else>
     </s:elseif>
     <s:else><a href="owerInformationInit.do">未填写</a></s:else>
    </td>
    <td align="center">
     <s:if test="#request.map.tpauditStatus==1">0</s:if> 
     <s:elseif test="#request.map.tpauditStatus==2">0</s:elseif>
     <s:elseif test="#request.map.tpauditStatus==3">
              <s:if test="#request.map.twauditStatus==1">0</s:if>
              <s:elseif test="#request.map.twauditStatus==2">0</s:elseif>
              <s:elseif test="#request.map.twauditStatus==3">
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
              </s:elseif>
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
      <s:elseif test="#request.map.tmIdentityauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmIdentityauditStatus==1">待审核</s:elseif>
      <s:elseif test="#request.map.tmIdentityauditStatus==3">通过</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">
    ${map.tmidentiycriditing }
    </td>
    </tr>
  <tr>
    <td align="center">工作认证</td>
    <td align="center">
    
       <s:if test="#request.map.tmworkauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmworkauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmworkauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">
      ${map.tmworkcriditing }
    </td>
    </tr>
  <tr>
    <td align="center">收入认证</td>
    <td align="center">
      <s:if test="#request.map.tmincomeeauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmincomeeauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmincomeeauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">  
   ${map.tmincomeecriditing }     
    </td>
    </tr>
  <tr>
    <td align="center">信用报告认证</td>
    <td align="center">
        <s:if test="#request.map.tmresponseauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmresponseauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmresponseauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>   
    </td>
    <td align="center"> 
     ${map.tmresponsecriditing }
    </td>
    </tr>
  <tr>
    <td align="center">居住认证</td>
    <td align="center">
          <s:if test="#request.map.tmaddressauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmaddressauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmaddressauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">
  ${map.tmaddresscriditing }
    </td>
    </tr>
  <tr>
    <td rowspan="10" align="center">可选认证</td>
    <td align="center">房产</td>
    <td align="center">
    
          <s:if test="#request.map.tmhouseauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmhouseauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmhouseauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center"> 
    ${map.tmhousecriditing }   
    </td>
  </tr>
  <tr>
    <td align="center">购车</td>
    <td align="center">
       <s:if test="#request.map.tmcarauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmcarauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmcarauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">
${map.tmcarcriditing }
  </td>
  </tr>
  <tr>
    <td align="center">结婚</td>
    <td align="center">
           <s:if test="#request.map.tmmerrayauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmmerrayauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmmerrayauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">
 ${map.tmmerraycriditing }
    </td>
  </tr>
  <tr>
    <td align="center">学历</td>
    <td align="center">
    
          <s:if test="#request.map.tmxueliauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmxueliauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmxueliauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>    
    </td>
    <td align="center">    
  ${map.tmxuelicriditing }  
    </td>
  </tr>
  <tr>
    <td align="center">技术</td>
    <td align="center">
            <s:if test="#request.map.tmjishuauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmjishuauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmjishuauditStatus==1">待审核</s:elseif>
       <s:else><a href="userPassData.do" >待上传</a></s:else>
    </td>
    <td align="center">
${map.tmjishucriditing }
</td>
  </tr>
  <tr>
    <td align="center">手机</td>
    <td align="center">
          <s:if test="#request.map.tmtelephoneauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmtelephoneauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmtelephoneauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>
</td>
    <td align="center">
${map.tmtelephonecriditing }
    </td>
    </tr>
  <tr>
    <td align="center">微博</td>
    <td align="center">
          <s:if test="#request.map.tmweiboauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmweiboauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmweiboauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>   
    </td>
    <td align="center">
  ${map.tmweibocriditing }
    </td>
    </tr>
   <!--  
  <tr>
    <td align="center">视频</td>
    <td align="center">
     
      <s:if test="#request.map.tmshipingauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmshipingauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
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
      <s:elseif test="#request.map.tmxcauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmxcauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>   
    </td>
    <td align="center"> 
  ${map.tmxcriditing }    
    </td>
    </tr>
  <tr>
    <td align="center">抵押</td>
    <td align="center">
       <s:if test="#request.map.tmdiyaauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmdiyaauditStatus==2"><a href="userPassData.do" >不通过</a></s:elseif>
      <s:elseif test="#request.map.tmdiyaauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do" >待上传</a></s:else>    
</td>
    <td align="center">    
 ${map.tmdiyacriditing }    
    </td>
 </tr>
      <tr>
          <td align="center">担保</td>
    <td align="center">
       <s:if test="#request.map.tmdanbaoauditStatus==3">
         通过
      </s:if>
      <s:elseif test="#request.map.tmdanbaoauditStatus==2">不通过</s:elseif>
      <s:elseif test="#request.map.tmdanbaoauditStatus==1">待审核</s:elseif>
      <s:else><a href="userPassData.do">待上传</a></s:else> 
    </td>
    <td align="center">   
 ${map.tmdanbaocriditing }   
    </td>
    </tr> 
  <tr>
    <td rowspan="7" align="center">富壹代还款积分</td>
    <th align="center">项目</th>
    <th align="center">次数</th>
    <th align="center">分数</th>
  </tr>
   <tr>
    <td align="center">提前还款</td>
    <td align="center">
    <s:if test="#request.UserBorrowmap1.counts!=null">${UserBorrowmap1.counts}</s:if>
    <s:else>0</s:else>
    </td>
    <td align="center">0分</td>
  </tr>
  <tr>
    <td align="center">按时还款</td>
    <td align="center">
	<s:if test="#request.UserBorrowmap2.counts!=null">${UserBorrowmap2.counts }分</s:if>
    <s:else>0</s:else>
    </td>
    <td align="center">
    <s:if test="#request.UserBorrowmap2.fenshu!=null">${UserBorrowmap2.fenshu }分</s:if>
    <s:else>0分</s:else>   
    </td>
    </tr>
  <tr>
    <td align="center">迟还款（逾期一天以上至10天以内的还款）</td>
    <td align="center">    <s:if test="#request.UserBorrowmap3.counts!=null">${UserBorrowmap3.counts }</s:if>
    <s:else>0</s:else></td>
    <td align="center">    <s:if test="#request.UserBorrowmap3.fenshu!=null">${UserBorrowmap3.fenshu }分</s:if>
    <s:else>0分</s:else></td>
    </tr>
  <tr>
    <td align="center">逾期还款（11-30天）</td>
    <td align="center"><s:if test="#request.UserBorrowmap4.counts!=null">${UserBorrowmap4.counts }</s:if>
    <s:else>0</s:else></td>
    <td align="center"><s:if test="#request.UserBorrowmap4.fenshu!=null">${UserBorrowmap4.fenshu }分</s:if>
    <s:else>0分</s:else></td>
    </tr>
  <tr>
    <td align="center">逾期还款（逾期31天以上至90天以内）</td>
    <td align="center"><s:if test="#request.UserBorrowmap5.counts!=null">${UserBorrowmap5.counts }</s:if>
    <s:else>0</s:else></td>
    <td align="center"><s:if test="#request.UserBorrowmap5.fenshu!=null">${UserBorrowmap5.fenshu }分</s:if>
    <s:else>0分</s:else></td>
    </tr>
  <tr>
    <td align="center">逾期还款（逾期90天以上的还款）</td>
    <td align="center"><s:if test="#request.UserBorrowmap6.counts!=null">${UserBorrowmap6.counts }</s:if>
    <s:else>0</s:else></td>
    <td align="center"><s:if test="#request.UserBorrowmap6.fenshu!=null">${UserBorrowmap6.fenshu }分</s:if>
    <s:else>0分</s:else></td>
    </tr>
          </table>
          </div>
          