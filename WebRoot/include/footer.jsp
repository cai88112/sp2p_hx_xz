<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!--底部快捷导航 开始-->
<div class="footz"
	style="padding-bottom: 35px;padding-top: 35px; background-image: none; background-color: #dde2e6;margin-top: 0px;">
	<div class="footer01" style="margin-top: 20px; color: #dde2e6">
		<ul>
			<li >
				<dl>
					<dt class="dt00"
						style="background-image: url(images/about.png); color: #9da4a9;font-size: 16px;">关于我们</dt>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4; " style="font-size: 14px;">平台简介</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">管理团队</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">合作伙伴</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">联系我们</a>
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt class="dt00"
						style="background-image: url(images/help.png);  color: #9da4a9; font-size: 16px;">帮助中心</dt>
					<dd>
						<a href="callcenter.do?type=true&cid=1" style="font-size: 14px;">常见问题</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">项目介绍</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">资费说明</a>
					</dd>
				</dl>
			</li>
			<li>
				<dl>
					<dt class="dt00"
						style="background-image: url(images/compass.png); color: #9da4a9;font-size: 16px;">新手指南</dt>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">安全保障</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">投资流程</a>
					</dd>
					<dd>
						<a href="getMessageBytypeId.do?typeId=4" style="font-size: 14px;">注册流程</a>
					</dd>
				</dl>
			</li>
			
				
			
			<li style="width:209px;">
			<img src="images/img_suxian.png" style="float:left" />
				<div style="background-color: black; width: 130px;float:right;">
				
					<img src="images/qr.jpg" style="width: 100%">
				</div>
			</li>
			<li style="padding-right: 0px; padding-left: 0px;">
				<div style="font-size: 18px; color: #929a9f; font-weight: bold">全国理财热线</div>
				<div style="text-align: left; position: relative">
					<div style="font-size: 28px; color: #e85801">4007 182 183</div>
					<div style="padding-top: 10px;font-size: 14px;color:#929a9f;">周一至周五（9：00-18：00）</div>
					<div style="font-size: 14px;color:#929a9f;">周六 周日（10：00-22：00）</div>
				</div>
			</li>
		</ul>
	</div>
</div>
<div class="footz"
	style="margin-top: 0px; background-image: none; background-color: #929a9f">
	<div class="footer02">
		<p style="color: #c1c6c9; font-size: 14px; letter-spacing: 2px">
			版权所有  © 2015 ${sitemap.companyName}  备案号：<span>${sitemap.certificate }</span><br>
			<span style="font-size:16px;">深圳市南山区前海路振业国际商务中心1808</span>
			<br>
			<!-- 法律顾问单位：国浩律师（深圳）事务所 地址：深圳市南山区前海路振业国际商务中心1808<br> --><br>
			<span><img src="images/gswj.jpg"></span>
			<span><img src="images/secure.png"></span>
			<span><img src="images/identity.png"></span>
		<div style="clear: both;"></div>
	</div>
</div>
<!--底部footer 结束-->
<script type="text/javascript" src="script/jqueryV172.js"></script>
<script type="text/javascript" src="script/xl.js"></script>
<script type="text/javascript">
function addCookie()
{
 if (document.all){
       window.external.addFavorite('<%=application.getAttribute("basePath")%>','富壹代');
    }
    else if (window.sidebar) {
       window.sidebar.addPanel('富壹代', '<%=application.getAttribute("basePath")%>', "");
    }else{
       alert('请手动设为首页');
    }
}

function setHomepage(){
    if (document.all){
        document.body.style.behavior='url(#default#homepage)';
        document.body.setHomePage('<%=application.getAttribute("basePath")%>');
    }else if (window.sidebar){
        if(window.netscape){
         try{  
            netscape.security.PrivilegeManager.enablePrivilege("UniversalXPConnect");  
         }  
         catch (e)  
         {  
            alert( "该操作被浏览器拒绝，如果想启用该功能，请在地址栏内输入 about:config,然后将项 signed.applets.codebase_principal_support 值该为true" );  
         }
    }else{
        alert('请手动添加收藏');
    }
    var prefs = Components.classes['@mozilla.org/preferences-service;1'].getService(Components. interfaces.nsIPrefBranch);
    prefs.setCharPref('browser.startup.homepage','<%=application.getAttribute("basePath")%>');
		}
	}
	$(function() {
		$(window).scroll(function() {
			if ($(window).scrollTop() >= 109) {
				$(".nav-zdh").css("position", "fixed")
			} else {
				$(".nav-zdh").css("position", "relative")
			}
		})
	})
</script>
