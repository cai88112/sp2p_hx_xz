package com.sp2p.action.admin;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.RegionService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.AdminService;

/**
 * 团队长
 * 
 * @author md002
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class GroupCaptainAction extends BasePageAction {

	public static Log log = LogFactory.getLog(GroupCaptainAction.class);

	private AdminService adminService;
	private RegionService regionService;
	// add by houli 后台团队长的名字不能与前台存在的用户名一样
	private UserService userService;

	private List<Map<String, Object>> provinceList;

	public String addGroupCaptainInit() {
		paramMap.put("enable", "1");
		paramMap.put("sex", "1");
		return SUCCESS;
	}

	public String addGroupCaptain() {

		String userName = Convert.strToStr(paramMap.get("userName"), null);
		String password = Convert.strToStr(paramMap.get("password"), null);
		String realName = Convert.strToStr(paramMap.get("realName"), null);
		String telphone = Convert.strToStr(paramMap.get("telphone"), null);
		String email = Convert.strToStr(paramMap.get("email"), null);
		String img = Convert.strToStr(paramMap.get("img"), null);
		String card = Convert.strToStr(paramMap.get("card"), null);
		String summary = Convert.strToStr(paramMap.get("summary"), null);
		String address = Convert.strToStr(paramMap.get("address"), null);
		String qq = Convert.strToStr(paramMap.get("qq"), null);
		int enable = Convert.strToInt(paramMap.get("enable"), -1);
		long roleId = IConstants.ADMIN_ROLE_GROUP;// 军团长角色默认为1
		int sex = Convert.strToInt(paramMap.get("sex"), -1);
		int nativePlacePro = Convert.strToInt(paramMap.get("nativePlacePro"),
				-1);
		int nativePlaceCity = Convert.strToInt(paramMap.get("nativePlaceCity"),
				-1);

		long returnId;
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		try {
			returnId = adminService.addAdminGroup(SqlInfusionHelper
					.filteSqlInfusion(userName), SqlInfusionHelper
					.filteSqlInfusion(password), enable, roleId, SqlInfusionHelper
					.filteSqlInfusion(realName), SqlInfusionHelper
					.filteSqlInfusion(telphone), SqlInfusionHelper.filteSqlInfusion(qq), SqlInfusionHelper.filteSqlInfusion(email), SqlInfusionHelper.filteSqlInfusion(img), null, sex,
					SqlInfusionHelper.filteSqlInfusion(card), SqlInfusionHelper.filteSqlInfusion(summary), nativePlacePro, nativePlaceCity, SqlInfusionHelper.filteSqlInfusion(address),
					-1, IConstants.RELATION_LEVEL1);
			operationLogService.addOperationLog("t_admin", admin.getUserName(),
					IConstants.INSERT, admin.getLastIP(), 0, "添加用户为团队长", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			this.addFieldError("paramMap.allError", "添加失败!");
			return INPUT;
		}
		if (returnId <= 0) {
			this.addFieldError("paramMap.allError", "添加失败!");
			return INPUT;
		}

		return SUCCESS;
	}

	public String queryGroupCaptainInit() {
		return SUCCESS;
	}

	public String queryGroupCaptainInfo() {
		Admin admin = (Admin) session().getAttribute("admin");
		String userName = Convert.strToStr(request("userName"), null);
		String realName = Convert.strToStr(request("realName"), null);
		String startDate = Convert.strToStr(request("startDate"), null);
		String endDate = Convert.strToStr(request("endDate"), null);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(admin.getRoleId() != -1){
			userName = admin.getUserName();
		}
		if (StringUtils.isNotBlank(startDate)) {
			try {
				sdf.parse(startDate);
			} catch (Exception e) {
				log.info(e);
				e.printStackTrace();
				startDate = null;
			}
		}
		if (StringUtils.isNotBlank(endDate)) {
			try {
				sdf.parse(endDate);
			} catch (Exception e) {
				log.info(e);
				e.printStackTrace();
				endDate = null;
			}
		}
		try {
			adminService.queryGroupCaptain(SqlInfusionHelper.filteSqlInfusion(userName), SqlInfusionHelper.filteSqlInfusion(realName), SqlInfusionHelper.filteSqlInfusion(startDate),
					SqlInfusionHelper.filteSqlInfusion(endDate), pageBean);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String updateGroupCaptainInit() throws Exception {
		long id = Convert.strToLong(request("id"), -1);
		if (id <= 0) {
			return SUCCESS;
		}
		paramMap = adminService.queryAdminById(id);
		return SUCCESS;
	}

	public String updateGroupCaptain() throws Exception {
		long adminId = Convert.strToLong(paramMap.get("id"), -1);
		if (adminId <= 0) {
			this.addFieldError("paramMap.allError", "修改失败,数据错误！");
			return INPUT;
		}
		String password = Convert.strToStr(paramMap.get("password"), null);
		String realName = Convert.strToStr(paramMap.get("realName"), null);
		String telphone = Convert.strToStr(paramMap.get("telphone"), null);
		String email = Convert.strToStr(paramMap.get("email"), null);
		String img = Convert.strToStr(paramMap.get("img"), null);
		String card = Convert.strToStr(paramMap.get("card"), null);
		String summary = Convert.strToStr(paramMap.get("summary"), null);
		String address = Convert.strToStr(paramMap.get("address"), null);
		String qq = Convert.strToStr(paramMap.get("qq"), null);
		int enable = Convert.strToInt(paramMap.get("enable"), -1);
		long roleId = 1;// 军团长角色默认为1
		int sex = Convert.strToInt(paramMap.get("sex"), -1);
		int nativePlacePro = Convert.strToInt(paramMap.get("nativePlacePro"),
				-1);
		int nativePlaceCity = Convert.strToInt(paramMap.get("nativePlaceCity"),
				-1);
		try {
			adminService.updateAdminGroup(adminId, null, SqlInfusionHelper.filteSqlInfusion(password), enable,
					roleId, SqlInfusionHelper.filteSqlInfusion(realName), SqlInfusionHelper.filteSqlInfusion(telphone), SqlInfusionHelper.filteSqlInfusion(qq), SqlInfusionHelper.filteSqlInfusion(email), SqlInfusionHelper.filteSqlInfusion(img), null, sex,
					SqlInfusionHelper.filteSqlInfusion(card), SqlInfusionHelper.filteSqlInfusion(summary), nativePlacePro, nativePlaceCity, SqlInfusionHelper.filteSqlInfusion(address),
					null, null);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_admin", admin.getUserName(),
					IConstants.UPDATE, admin.getLastIP(), 0, "修改团队长信息", 2);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			this.addFieldError("paramMap.allError", "修改失败，数据错误！");
		}
		return SUCCESS;
	}

	// ======地区列表
	public String ajaxqueryRegionAdmin() throws Exception {
		Long parentId = Convert.strToLong(request("parentId"), -1);
		Integer regionType = Convert.strToInt(request("regionType"), -1);
		List<Map<String, Object>> list;
		try {
			list = regionService.queryRegionList(-1L, parentId, regionType);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		String jsonStr = JSONArray.toJSONString(list);
		ServletHelper.returnStr(ServletActionContext.getResponse(), jsonStr);
		return null;
	}

	/**
	 * add by houli 判断用户名是否重复
	 * 
	 * @return
	 * @throws Exception
	 */
	public String judgeName() throws Exception {
		String userName = request("userName") == null ? null : Convert
				.strToStr(request("userName"), null);
		try {
			if (userName == null) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			}
			Map<String, String> adminMap = adminService.queryIdByUser(SqlInfusionHelper.filteSqlInfusion(userName));
			if (adminMap == null || adminMap.size() <= 0) {
				// 从前台用户表查询是否有重复的用户名
				Map<String, String> userMap = userService
						.queryIdByUser(SqlInfusionHelper.filteSqlInfusion(userName));
				if (userMap == null || userMap.size() <= 0) {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
					return null;
				} else {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
					return null;
				}
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<Map<String, Object>> getProvinceList() throws Exception {
		if (provinceList == null) {
			try {
				provinceList = regionService.queryRegionList(-1L, 1L, 1);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		return provinceList;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
