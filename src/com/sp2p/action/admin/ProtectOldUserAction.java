package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.CacheManager;
import com.shove.web.action.BasePageAction;
import com.sp2p.action.front.FrontMyPaymentAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.ProtectOldUserService;
import com.sp2p.service.admin.ReturnedmoneyService;

/**
 * 回款续投管理.
 * 
 * @ClassName: protectOldUserAction
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Daizhiyue
 * @date 2015年03月16日 上午10:29:55
 * 
 */
public class ProtectOldUserAction extends BasePageAction {

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ProtectOldUserAction.class);
	private ProtectOldUserService protectOldUserService;
	private UserService userService;

	/**
	 * 初始化回款续投页面.
	 * 
	 * @Title: retrunedmoneyPayeInit
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String returnedmoneyPageInit() {
		log.info("初始化页面retrunedmoneyPageInit");
		return SUCCESS;
	}

	/**
	 * 初始化老米护盾管理页面.
	 * 
	 * @Title: protectOldUserPageInit
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryProtectOldUserPageInit() {
		log.info("初始化页面protectOldUserPageInit");
		return SUCCESS;
	}

	/**
	 * 查找分页列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryProtectOldUserPage() throws Exception {
		try {
			String userName = Convert.strToStr(SqlInfusionHelper
					.filteSqlInfusion(paramMap.get("userName")), null);
			protectOldUserService.queryProtectOldUserPage(pageBean, userName);
			List<Map<String, Object>> list = pageBean.getPage();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}

	/**
	 * 删除老米护盾
	 * @return
	 * @throws Exception
	 */
	public String deleteOldUser() throws Exception{
		Long id = Convert.strToLong(request("userId"),-1L);
		protectOldUserService.deleteOldUser(id);
		return SUCCESS;
	}
	@SuppressWarnings("unchecked")
	public String exportforProtectOldUser() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		String userName = Convert.strToStr(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")),
				null);
		try {
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			String applyTime = Convert.strToStr(
					SqlInfusionHelper.filteSqlInfusion(request().getParameter(
							"applyTime")), null);
			String endTime = Convert.strToStr(
					SqlInfusionHelper.filteSqlInfusion(request().getParameter(
							"endTime")), null);
			if (StringUtils.isBlank(endTime)
					&& StringUtils.isNotBlank(applyTime)) {
				endTime = FrontMyPaymentAction.changeEndTime(applyTime);

			}
			// 老米护盾
			protectOldUserService.queryProtectOldUserPage(pageBean, userName);
			if (pageBean.getPage() == null) {
				getOut().print(
						"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (pageBean.getPage().size() > IConstants.EXCEL_MAX) {
				getOut().print(
						"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}
			HSSFWorkbook wb = ExcelHelper.exportExcel("老米护盾列表",
					pageBean.getPage(), new String[] { "批次", "用户名", "真实姓名",
							"待收标准", "状态" }, new String[] { "batch", "username",
							"realName", "duestandard", "status" });
			operationLogService.addOperationLog("exportUserFundRecharge",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出老米护盾列表", 2);
			this.export(wb, new Date().getTime() + ".xls");
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (DataException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 初始化查看非老米会员列表管理页面.
	 * 
	 * @Title: protectOldUserPageInit
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String queryNotProtectVipInfoIndex() {
		log.info("初始化页面notProtectVipInfoIndex");
		return SUCCESS;
	}

	/**
	 * 查找"非老米用户列表"分页列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryNotProtectVipInfo() throws Exception {
		try {
			String userName = Convert.strToStr(SqlInfusionHelper
					.filteSqlInfusion(paramMap.get("username")), null);
			protectOldUserService.queryNotProtectVipPage(pageBean, userName);
			List<Map<String, Object>> list = pageBean.getPage();
			request().setAttribute("protectOldUserList", list);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}

	/**
	 * 弹出框初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addProtectVipIndex() throws Exception {
		Map<String, String> popmap = null;
		List<Map<String, String>> maplist = new ArrayList<Map<String, String>>();
		Long id = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("id")), -1L);
		popmap = protectOldUserService.queryProtectVipAddInteral(id);
		maplist = protectOldUserService.getBatchList();
		request().setAttribute("popmap", popmap);
		request().setAttribute("maplist", maplist);
		return SUCCESS;
	}

	/**
	 * 弹出框初始添加老米用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addProtectVip() throws Exception {
		Long userId = Convert.strToLong(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")), -1L);
		String batchStr = SqlInfusionHelper.filteSqlInfusion(paramMap.get("batch"));
		if (StringUtils.isBlank(batchStr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		}
		if (!StringUtils.isNumeric(batchStr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Map<String, String> map = protectOldUserService
				.getProtectUserDetailByUserId(userId);
		if (map != null) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
			return null;
		}

		Long result = -1L;

		String changetype = "增加";// 先设置为增加类型
		Integer batch = Convert.strToInt(batchStr, -1);
		result = protectOldUserService.addProtectVip(userId, batch);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_protectVip",
				admin.getUserName(), IConstants.INSERT, admin.getLastIP(), 0,
				"添加老米用户", 2);
		if (result > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
			return null;
		}
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ProtectOldUserService getProtectOldUserService() {
		return protectOldUserService;
	}

	public void setProtectOldUserService(
			ProtectOldUserService protectOldUserService) {
		this.protectOldUserService = protectOldUserService;
	}

}
