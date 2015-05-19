package com.sp2p.system.interceptor;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.shove.Convert;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.admin.CloseNetWorkService;

	/**
	 * 网站是否已经关闭判断拦截
	 * 
	 * @author 钟垂青
	 * @Create Jun 3, 2011
	 * 
	 */
	public class CloseNetWorkInterceptor implements Interceptor {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(CloseNetWorkInterceptor.class);
	private HttpSession session;
	private HttpServletResponse response;
	private HttpServletRequest request;

	public void destroy() {
	}

	public void init() {
	}
	
	
	public String isCloseNetWork(ActionInvocation invocation)throws Exception{
		
		ServletContext application = request.getSession().getServletContext();
		Map<String,String> map=(Map<String, String>)application.getAttribute(IConstants.Session_CLOSENETWORK);
		Integer status=Convert.strToInt(map.get("status"),-1);
		if(status==2){
			session.setAttribute("netWork",map.get("content"));
			return IConstants.Session_CLOSENETWORK;
		}
		return invocation.invoke();
	}

	public String intercept(ActionInvocation invocation) throws Exception {
		request = ServletActionContext.getRequest();
		response = ServletActionContext.getResponse();
		session = request.getSession();
		String retUrl = request.getHeader("Referer");  
		log.info("-------------------"+retUrl);
		log.info(request.getRequestURL());
		if (isAjaxRequest()) {
			return ajaxIntercept(invocation);
		}
		return isCloseNetWork(invocation);
	    
	}
	
	// ajax请求拦截 没登录返回 NoLogin 登录流程继续
	private String ajaxIntercept(ActionInvocation invocation) throws Exception {
		log.info("ajax拦截");
		ServletContext application = request.getSession().getServletContext();
		Map<String,String> map=(Map<String, String>)application.getAttribute(IConstants.Session_CLOSENETWORK);
		Integer status=Convert.strToInt(map.get("status"),-1);
		if(status==2){
			session.setAttribute("netWork",map.get("content"));
			response.setContentType("text/html");
			response.getWriter().print(IConstants.Session_CLOSENETWORK);
			log.info("No Login");
			return null;
		}
		return invocation.invoke();
	}
	
	private boolean isAjaxRequest() {
		String header = request.getHeader("X-Requested-With");
		if (header != null && "XMLHttpRequest".equals(header)) {
			return true;
		}
		return false;
	}

}
