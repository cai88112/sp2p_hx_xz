package com.shove.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.shove.security.License;
import com.sp2p.constants.IConstants;

/**
 * 平台注册
 * 
 */
public class SystemRegistAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String regisCode;//注册码
	
	public String getRegisCode() {
		return regisCode;
	}
	public void setRegisCode(String regisCode) {
		this.regisCode = regisCode;
	}
	
	public String systemRegist() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		PrintWriter out = null;
		try {
			
			out = response.getWriter();	
			//判断注册码是否为空 
			if(regisCode == null || "".equals(regisCode)) {		
				out.print("<script>alert('请输入注册码!');;window.history.go(-1);</script>");
				return null;
			}	
			
			License.update(request, regisCode);
			
			if(License.getAdminPagesAllow(request)){
				out.print("<script>alert('平台注册成功!');" +
						"window.top.location.href='login.do'"+
						"</script>");
			}else{
				out.print("<script>alert('平台注册失败!');" +
						"window.top.location.href='systemRegist.do'"+
						"</script>");
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		return null;
	}
	
}
