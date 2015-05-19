<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<jsp:include page="/include/head.jsp"></jsp:include>
<script type="text/javascript" src="script/jquery-1.7.1.min.js"></script>
<script type="text/javascript">
	$(function() {
		dqzt(2);
	})
</script>



</head>
<body>
	<!-- 引用头部公共部分 -->
	<jsp:include page="/include/top.jsp"></jsp:include>
	<div style="width: 100%; height: 1000px">
		<div class="kinds-list"
			style="position: relative; width: 100%; height: 190px; background-color: #7ecef4; top: -4px">

			<div style="width: 1000px; position: relative; margin: 0 auto 0 auto">
				<img src="images/timeHint.png" style="margin: 40px"></img>
				<div
					style="position: relative; font-size: 28px; color: white; width: 850px; top: -130px; left: 144px">
					<p>如果您是深圳的客户请直接联系：4007182183 
						<br>如果您是长沙的客户请直接联系：0731-12345678</p>
				</div>
				<!-- <div
					style="position: relative; top: -80px; margin: 0 auto; width: 294px; height: 52px; text-align: center; background-color: #fbd542">
					<a href="creditingInit.do"
						style="font-size: 20px; color: white; line-height: 50px">申请信用额度</a>
				</div> -->
			</div>
		</div>

			<div
				style="position: relative; width: 100%; height: 500px; background-color: #f3f3f3; top: -16px">
				<div style="position: relative; width: 1000px; margin: 0 auto">
					<div
						style="position: absolute; width: 308px; height: 330px; background-color: #fff; margin: 86px 0">
						<div
							style="position: realtive;">
							<div style=" width: 308px; height: 110px; background-color: #c1c6c9"  class="changefd" data="1">
							<img src="images/F1.png" style="margin: 26px 60px"></img>
							</div>
							
							<div
								style="position: relative; margin: 55px auto; letter-spacing: 1px; color: #515457">
								<p>针对名下有房产的客户（抵押类必须要有2套房产），为抵押房产办理他项抵押贷款。</p>
							</div>
							<div
								style="position: relative; width: 120px; height: 40px; text-align: center; background-color: #e85801; border-radius: 5px; margin: -10px auto" class="changeyes">
								<a href="addBorrowInit.do?t=4"
									style="font-size: 18px; color: white; line-height: 40px">立即申请</a>
							</div>
						</div>
					</div>

					<div
						style="position: absolute; width: 308px; height: 330px; background-color: #fff; margin: 86px 338px">
						<div
							style="position: realtive;">
							<div style="width: 308px; height: 110px; background-color: #c1c6c9" class="changefd" data="2" ><img src="images/F2.png" style="margin: 26px 60px"></img></div>
							
							<div
								style="position: relative; margin: 55px 1px; letter-spacing: 1px; color: #515457">
								<p>汽车抵押贷款咨询服务是以借款人或第三人的汽车或自购车作为抵押物向金融机构或贷款公司取得贷款业务的服务 副本</p>
							</div>
							<div
								style="position: relative; width: 120px; height: 40px; text-align: center; background-color: #e85801; border-radius: 5px; margin: -26px auto" class="changeyes">
								<a href="addBorrowInit.do?t=4"
									style="font-size: 18px; color: white; line-height: 40px">立即申请</a>
							</div>
						</div>
					</div>
					
					<div
						style="position: absolute; width: 308px; height: 330px; background-color: #fff; margin: 86px 675px">
						<div
							style="position: realtive;">
							<div style="width: 308px; height: 110px; background-color: #c1c6c9" class="changefd" data="3">
							<img src="images/F3.png" style="margin: 26px 60px"></img>
							</div>
							<div
								style="position: relative; margin: 55px auto; letter-spacing: 1px; color: #515457">
								<p>富余宝是将用户站岗资金自动转入富壹代平台补息渠道的收益性产品。</p>
							</div>
							<div
								style="position: relative; width: 120px; height: 40px; text-align: center; background-color: #e85801; border-radius: 5px; margin: -6px auto" class="changeyes">
								<a href="addBorrowInit.do?t=4"
									style="font-size: 18px; color: white; line-height: 40px">立即申请</a>
							</div>
						</div>
					</div>


				</div>
			</div>
			
			<div style="width: 100%; height: 250px; background-color: #fff; position: relative">
				<div style="position: relative; margin: 0 auto; width: 1000px; top: 60px"><img src="images/step.png"></img></div>
			</div>



	</div>
	<script>

		$(function(){
			
			$(".changefd").hover(			
					function(){			
						if($(this).attr("data")=='1'){						 
							$(this).css('background-color','#9200B5');	
							$(this).find('img').attr("src",'images/F1a.png');
						}else if($(this).attr("data")=='2'){						 
							$(this).css('background-color','#9200B5');	
							$(this).find('img').attr("src",'images/F2a.png');
						}else{
							$(this).css('background-color','#9200B5');	
							$(this).find('img').attr("src",'images/F3a.png');
						}					
					},
					function(){
						if($(this).attr("data")=='1'){						 
							$(this).css('background-color','#c1c6c9');	
							$(this).find('img').attr("src",'images/F1.png');
						}else if($(this).attr("data")=='2'){						 
							$(this).css('background-color','#c1c6c9');	
							$(this).find('img').attr("src",'images/F2.png');
						}else{
							$(this).css('background-color','#c1c6c9');	
							$(this).find('img').attr("src",'images/F3.png');
						}	
					}
			);
			
			$(".changeyes").hover(
				function(){
					$(this).css("background-color","#E8380D");
					
				},
				function(){
					$(this).css("background-color","#e85801");
				}
				
			);
			
			
		});
	</script>








	<!-- <div class="tz-step"><img src="images/wyjk_guide.jpg" /></div> -->
	<!-- <div class="sqxyed"><div>如果您不急于贷款，可以通过这里先申请信用额度，这将方便您日后迅速获得额度内的贷款</div><a href="creditingInit.do"></a></div> -->
	<%-- <ul class="kindsnr-list">
       <li><div class="kindsnr-cnet">
        <h3 class="kind4">净值借款</h3>
        借款人可以发布的不超过其平台账号净值额度的借款金额，但借款人提现将受到限制。这是一种安全系数相对较高的借款标，因此利率方面可能比较低。净值标通常用于临时周转，使资金利用率最大化。
        <div class="kindsnr-link4"><a href="addBorrowInit.do?t=1">净值借款</a></div>
        </div></li>
        <!-- 
        <li><div class="kindsnr-cnet">
        <h3 class="kind5">秒还借款</h3>
       借款人标满瞬间送出利息，免手续费、自动审核、自动还款，不定期送出秒还标。
        <div class="kindsnr-link5"><a href="addBorrowInit.do?t=2">秒还标</a></div>
        </div></li>   -->
          <li><div class="kindsnr-cnet">
        <h3 class="kind2">信用借款</h3>
       信用借款标是一种将个人的信用运用于借款之中，免抵押、免担保的小额个人信用贷款标，SP2P网贷通过严格审核，对借款人给予信用评级，授予信用额度，允许其在平台发布贷款信息。主要面向对象一般为18-60周岁有稳定收入，具有较强偿还能力的公民。
        <div class="kindsnr-link2"><a href="addBorrowInit.do?t=3">信用借款</a></div>
        </div></li>
          <li><div class="kindsnr-cnet">
        <h3 class="kind3">实地考察借款</h3>
       小微企业现场考察审批借款；汽车、房产、货物等借款；逾期24小时内赔付。
        <div class="kindsnr-link3"><a href="addBorrowInit.do?t=4">实地考察借款</a></div>
        </div></li>
    	<li><div class="kindsnr-cnet">
        <h3 class="kind1">担保借款</h3>
        是指富壹代的合作伙伴为相应的借款提供连带保证，并负有连带保证责任的借款。
（机构担保标需要通过机构担保认证） 
        <div class="kindsnr-link"><a href="addBorrowInit.do?t=5">担保借款</a></div>
        </div></li>
         <li><div class="kindsnr-cnet">
        <h3 class="kind5">流转标</h3>
       流转标投标即成功,到期网站垫付,零风险
        <div class="kindsnr-link5"><a href="addBorrowInit.do?t=6">流转标</a></div>
        </div></li>   
	</ul> --%>

	<!-- 引用底部公共部分 -->
	<jsp:include page="/include/footer.jsp"></jsp:include>
</body>
</html>
