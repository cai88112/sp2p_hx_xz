package com.sp2p.action.front;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**   
 * @ClassName: FrontMyHomeAction.java
 * @Author: gang.lv
 * @Date: 2013-3-13 下午10:21:30
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 我的主页控制层
 */
public class FrontHomePageAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontHomePageAction.class);
	private static final long serialVersionUID = 1L;

	public String platformInit(){
		return "success";
	}

	public String teamInit(){
		return "success";
	}
	
	public String legalPolicyInit(){
		return "success";
	}
	
	public String aboutUsInit(){
		return "success";
	}
	
	public String downloadInit(){
		return "success";
	}
}
