package com.sp2p.service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.web.context.ServletConfigAware;

import com.sp2p.dao.MyHomeInfoSettingDao;
import com.sp2p.entity.EMailSet;

@SuppressWarnings("serial")
public class GetAppliactionService implements ServletContextAware,
		ServletConfigAware {
	
	public static Log log = LogFactory.getLog(GetAppliactionService.class);
	private ServletContext application;

	public EMailSet getEMailSet() {
		log.info("ssssss");
		log.info(application + "==");
		return null;
	}

	public void setServletContext(ServletContext arg0) {
		log.info("ssssffsd");
		this.application = arg0;
	}

	public void setServletConfig(ServletConfig arg0) {

	}
}
