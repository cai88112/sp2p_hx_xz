package com.sp2p.action.admin;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.admin.AdminService;
import com.sp2p.service.admin.RoleRightsService;

public class ShowSubMenuAction extends BasePageAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(ShowSubMenuAction.class);
	private RoleRightsService roleRightsService;
	private AdminService adminService;
	
	public void setRoleRightsService(RoleRightsService roleRightsService) {
		this.roleRightsService = roleRightsService;
	}
	
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	/**
	 * 查询一级菜单下二级菜单的权限
	 * @return
	 * @throws Exception 
	 */
	public String showsubmenu() throws Exception{
		String index = SqlInfusionHelper.filteSqlInfusion(request().getParameter("index"));
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		long roleId = admin.getRoleId();
		List<Map<String, Object>> list = null;
		try {
			list = roleRightsService.queryAdminRoleRightMenu(roleId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		//--审核管理
		Map<String,String> map = adminService.queryCheckCount(admin.getId());
		session().setAttribute("map", map);
		
		session().setAttribute("adminRoleMenuList", list);
		
		session().setAttribute(IConstants.SESSION_ADMIN, admin);
		session().setAttribute("index", index);
		return SUCCESS;
	}
}
