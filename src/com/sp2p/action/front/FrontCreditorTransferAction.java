package com.sp2p.action.front;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sp2p.constants.IConstants;
import com.sp2p.service.HelpAndServicerService;

/**   
 * @ClassName: FrontMyHomeAction.java
 * @Author: gang.lv
 * @Date: 2013-3-13 下午10:21:30
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 我的主页控制层
 */
public class FrontCreditorTransferAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontCreditorTransferAction.class);
	private static final long serialVersionUID = 1L;
	private HelpAndServicerService callCenterService;

	public String creditorInit() throws Exception{
		/**
		 * add by houli
		 */
		try{
			List<Map<String,Object>> lists = callCenterService.queryQuestionsLimit5(IConstants.CREDITOR_TRANS);
			request().setAttribute("lists", lists);
		}catch(Exception e){
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		
		return "success";
	}

	public String creditorListInit(){
		return "success";
	}

	public HelpAndServicerService getCallCenterService() {
		return callCenterService;
	}

	public void setCallCenterService(HelpAndServicerService callCenterService) {
		this.callCenterService = callCenterService;
	}

}
