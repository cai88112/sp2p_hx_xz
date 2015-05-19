package com.sp2p.action.front;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.sp2p.service.ShowShipinService;

/**
 * 视频弹出框
 * 
 * @author Administrator
 * 
 */
public class ShowShipinAction extends BaseFrontAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(ShowShipinAction.class);
	private ShowShipinService showShipinService;

	public void setShowShipinService(ShowShipinService showShipinService) {
		this.showShipinService = showShipinService;
	}

	/**
	 * 视频弹出框
	 * 
	 * @return
	 */
	public String showshiping() {
		String dm = SqlInfusionHelper.filteSqlInfusion(request().getParameter("dm"));
		request().setAttribute("tmid", dm);
		return SUCCESS;
	}

	/**
	 * 更新视频认证审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateShiping() throws Exception {
		Long tmid = Convert.strToLong(paramMap.get("tmid"), -1L);// 资料认证主表的id
		if (tmid == -1L) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		}
		Integer ccc = Convert.strToInt(paramMap.get("ccc"), -1);
		if (ccc == -1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		if (ccc != 1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		Long tmtype = 13L;// 视频
		Long resutl = -1L;
		try {
			resutl = showShipinService.updateShiping(tmid, tmtype);
			if (resutl > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
