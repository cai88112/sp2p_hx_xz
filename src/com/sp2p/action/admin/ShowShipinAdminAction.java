package com.sp2p.action.admin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.ValidateService;
import com.sp2p.service.admin.ShowShipinAdminService;

/**
 * 视频弹出框
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class ShowShipinAdminAction extends BaseFrontAction {
	public static Log log = LogFactory.getLog(ShowShipinAdminAction.class);
	private ShowShipinAdminService showShipinAdminService;
	private ValidateService validateService;

	public void setValidateService(ValidateService validateService) {
		this.validateService = validateService;
	}

	public void setShowShipinAdminService(
			ShowShipinAdminService showShipinAdminService) {
		this.showShipinAdminService = showShipinAdminService;
	}

	/**
	 * 视频弹出框
	 * 
	 * @return
	 * @throws Exception
	 */
	public String showshipingpop() throws Exception {
		String tmid = SqlInfusionHelper.filteSqlInfusion(request().getParameter("m"));
		String userid = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id"));
		// 显示图片的列表和
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, String> materialsauthMap = null;
		// 查询审核记录
		if (StringUtils.isNotBlank(userid)) {
			Long userId = Convert.strToLong(userid, -1L);
			list = validateService.queryAdminCheckList(userId, 13);// 审核记录 13
																	// 视屏认证
			request().setAttribute("checkList", list);
			materialsauthMap = validateService.querymaterialsauth(userId, 13);// 查询证件类型
			request().setAttribute("materialsauthMap", materialsauthMap);
		}

		request().setAttribute("tmid", tmid);
		request().setAttribute("i", userid);
		return SUCCESS;
	}

	/**
	 * 视频认证审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateShipingadmin() throws Exception {
		Long tmid = Convert.strToLong(paramMap.get("tmid"), -1L);
		if (tmid == -1L) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}
		String ccc = paramMap.get("ccc");
		if (StringUtils.isBlank(ccc)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		}

		String scostr = paramMap.get("sco");
		if (!StringUtils.isNumeric(scostr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}

		String content = paramMap.get("content");
		if (StringUtils.isBlank(content)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		Integer sco = Convert.strToInt(scostr, -1);
		// 查询主表状态值 如果已经审核失败 或者 通过 都不能再修改了
		Map<String, String> stautmap = null;
		Integer status = -1;
		stautmap = showShipinAdminService.querydateMa1(tmid);
		if (stautmap.size() > 0 && stautmap != null) {
			status = Convert.strToInt(stautmap.get("auditStatus"), -1);
		}
		if (status == -1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
			return null;
		}
		if (status == 2 || status == 3) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
			return null;
		}
		Long rs = -1L;
		Integer cccs = Convert.strToInt(paramMap.get("ccc"), 1);
		Integer statusstr = 1;
		if (cccs == 2) {
			statusstr = 2;
		}
		if (cccs == 1) {
			statusstr = 3;
		}
		if (statusstr == 1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "6");
			return null;
		}
		Long userId = Convert.strToLong(paramMap.get("id"), -1L);
		if (userId == -1L) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "8");
			return null;
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (admin == null) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "9");
			return null;
		}
		rs = showShipinAdminService.updateMa1(tmid, statusstr, content, sco,
				userId, admin.getId());// 更新视频证件类型的认证情况

		if (rs > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "10");
			operationLogService.addOperationLog("t_user", admin.getUserName(),
					IConstants.UPDATE, admin.getLastIP(), 0, "视频认证审核成功", 2);
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "7");
			operationLogService.addOperationLog("t_user", admin.getUserName(),
					IConstants.UPDATE, admin.getLastIP(), 0, "视频认证审核失败", 2);
			return null;
		}

	}

	/**
	 * 统计资料产看单张图片的情况
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countindex() throws Exception {
		Long userid = Convert.strToLong(request().getParameter("ui"), -1L);// 用户id
		request().setAttribute("userid", userid);
		Long typeid = Convert.strToLong(request().getParameter("mt"), -1L);// 证件种类
		Map<String, String> CountMsgMap = null;
		Map<String, String> querytmidMap = null;
		List<Map<String, Object>> PictureList = null;
		Long tmid = -1L;
		try {
			CountMsgMap = showShipinAdminService.queryCountMsg(typeid, userid);// 查询用户的用户名
																				// ,
																				// 认证种类,
																				// 证件图片
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request().setAttribute("CountMsgMap", CountMsgMap);

		// 查询t_materialsauth相对应证件id
		try {
			querytmidMap = showShipinAdminService.querytmid(typeid, userid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (querytmidMap != null && querytmidMap.size() > 0) {
			tmid = Convert.strToLong(querytmidMap.get("id"), -1L);
			try {
				PictureList = showShipinAdminService
						.queryCountPictureList(tmid);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		request().setAttribute("PictureList", PictureList);// 图片列表
		return SUCCESS;
	}

	/**
	 * 查询单张图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryoneindex() throws Exception {
		Long tmdid = request().getParameter("tmdid") == null ? -1L : Long
				.parseLong(request().getParameter("tmdid")+"");
		Long uid = request().getParameter("i") == null ? -1L : Long
				.parseLong(request().getParameter("i")+"");
		Map<String, String> queryonemsgmap = null;
		Map<String, String> queryusermap = null;
		try {
			queryonemsgmap = showShipinAdminService.queryonemsg(tmdid);
			queryusermap = showShipinAdminService.queryuser(uid);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request().setAttribute("queryonemsgmap", queryonemsgmap);// 图片列表
		request().setAttribute("queryusermap", queryusermap);// 用户名称
		return SUCCESS;
	}

}
