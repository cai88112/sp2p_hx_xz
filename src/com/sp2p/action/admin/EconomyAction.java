package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.RegionService;
import com.sp2p.service.admin.AdminService;
import com.sp2p.service.admin.RelationService;

/**
 * 经纪人
 * 
 * @author md002
 * 
 */
@SuppressWarnings( { "unchecked", "serial" })
public class EconomyAction extends BasePageAction {

	public static Log log = LogFactory.getLog(EconomyAction.class);

	private AdminService adminService;
	private RegionService regionService;
	private RelationService relationService;

	private List<Map<String, Object>> provinceList;
	private List<Map<String, Object>> parentList;

	public String addEconomyInit() {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		paramMap.put("enable", "1");
		paramMap.put("sex", "1");
		paramMap.put("parentId", admin.getId() + "");
		return SUCCESS;
	}

	public String addEconomy() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		String userName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("userName"), null));
		String password = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("password"), null));
		String realName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("realName"), null));
		String telphone = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("telphone"), null));
		String email = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("email"), null));
		String img = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("img"), null));
		String card = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("card"), null));
		String summary = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("summary"), null));
		String address = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("address"), null));
		String qq = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("qq"), null));
		int enable = Convert.strToInt(paramMap.get("enable"), -1);
		long roleId = IConstants.ADMIN_ROLE_ECONOMY;// 经纪人角色默认为1
		int sex = Convert.strToInt(paramMap.get("sex"), -1);
		int nativePlacePro = Convert.strToInt(paramMap.get("nativePlacePro"),
				-1);
		int nativePlaceCity = Convert.strToInt(paramMap.get("nativePlaceCity"),
				-1);
		long parentId = Convert.strToLong(paramMap.get("parentId"), -1);
		long returnId;
		try {
			returnId = adminService.addAdminGroup(userName, password, enable,
					roleId, realName, telphone, qq, email, img, null, sex,
					card, summary, nativePlacePro, nativePlaceCity, address,
					parentId, IConstants.RELATION_LEVEL2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			this.addFieldError("paramMap.allError", "添加失败!");
			operationLogService.addOperationLog("t_admin", admin.getUserName(),
					IConstants.INSERT, admin.getLastIP(), 0, "新增团队长或经纪人失败", 2);
			return INPUT;
		}
		if (returnId <= 0) {
			this.addFieldError("paramMap.allError", "添加失败!");
			operationLogService.addOperationLog("t_admin", admin.getUserName(),
					IConstants.INSERT, admin.getLastIP(), 0, "新增团队长或经纪人成功", 2);
			return INPUT;
		}

		return SUCCESS;
	}

	public String queryEconomyInit() {
		return SUCCESS;
	}

	/**
	 * 经济人管理
	 * 
	 * @return
	 */
	public String queryEconomyInfo() {
		String userName1 = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("userName1"), null));
		String userName2 = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("userName2"), null));
		String realName2 = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("realName2"), null));
		String startDate = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("startDate"), null));
		String endDate = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("endDate"), null));
		long parentId = Convert.strToLong(request("parentId"), -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
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
			relationService.queryRelationBy2(userName1, userName2, realName2,
					startDate, endDate, parentId, pageBean, admin.getRoleId(),
					admin.getUserName());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String updateEconomyInit() throws Exception {
		long id = Convert.strToLong(request("id"), -1);
		if (id <= 0) {
			return SUCCESS;
		}
		List<Map<String, Object>> list = relationService
				.queryRelationByPeopleId(id);
		if (list == null || list.size() <= 0) {
			return SUCCESS;
		}
		paramMap = adminService.queryAdminById(id);
		paramMap.put("parentId", list.get(0).get("parentId") + "");
		paramMap.put("relationId", list.get(0).get("id") + "");
		return SUCCESS;
	}

	public String updateEconomy() throws Exception {
		long adminId = Convert.strToLong(paramMap.get("id"), -1);
		if (adminId <= 0) {
			this.addFieldError("paramMap.allError", "修改失败,数据错误！");
			return INPUT;
		}
		String password = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("password"), null));
		String realName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("realName"), null));
		String telphone = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("telphone"), null));
		String email = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("email"), null));
		String img = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("img"), null));
		String card = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("card"), null));
		String summary = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("summary"), null));
		String address = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("address"), null));
		String qq = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("qq"), null));
		int enable = Convert.strToInt(paramMap.get("enable"), -1);
		long roleId = IConstants.ADMIN_ROLE_ECONOMY;// 军团长角色默认为1
		int sex = Convert.strToInt(paramMap.get("sex"), -1);
		int nativePlacePro = Convert.strToInt(paramMap.get("nativePlacePro"),
				-1);
		int nativePlaceCity = Convert.strToInt(paramMap.get("nativePlaceCity"),
				-1);
		long parentId = Convert.strToLong(paramMap.get("parentId"), -1);
		long relationId = Convert.strToLong(paramMap.get("relationId"), -1);
		try {
			adminService.updateAdminGroup(adminId, null, password, enable,
					roleId, realName, telphone, qq, email, img, null, sex,
					card, summary, nativePlacePro, nativePlaceCity, address,
					relationId, parentId);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			this.addFieldError("paramMap.allError", "修改失败，数据错误！");
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_admin", admin.getUserName(),
				IConstants.UPDATE, admin.getLastIP(), 0, "修改团队长或经纪人信息", 2);
		return SUCCESS;
	}

	public String updateRelationRelation() throws Exception {
		JSONObject obj = new JSONObject();
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		long id = Convert.strToLong(request("id"), -1);
		if (id <= 0) {
			obj.put("msg", "解除失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		int enable = IConstants.RELATION_ENABLE_FALSE;
		try {
			relationService.updateRelationEnable(id, enable);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (enable == IConstants.RELATION_ENABLE_FALSE) {
			obj.put("text", "进行关联");
			obj.put("enable", IConstants.RELATION_ENABLE_TRUE);
			operationLogService.addOperationLog("t_relation", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"添加与经纪人关联", 2);
		} else {
			obj.put("text", "解除关系");
			obj.put("enable", IConstants.RELATION_ENABLE_FALSE);
			operationLogService.addOperationLog("t_relation", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"解除与经纪人的关联关系", 2);
		}
		obj.put("msg", 1);
		JSONHelper.printObject(obj);

		return null;

	}

	public String updateRelationParentId() throws Exception {
		JSONObject obj = new JSONObject();
		long id = Convert.strToLong(request("id"), -1);
		long parentId = Convert.strToLong(request("parentId"), -1);
		if (id <= 0 || parentId <= 0) {
			obj.put("msg", "重置失败，数据错误！");
			JSONHelper.printObject(obj);
			return null;
		}
		long returnId = -1;
		try {
			returnId = relationService.updateRelation(id, null, parentId, null,
					IConstants.RELATION_ENABLE_TRUE);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_relation", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"修改角色之间的关系", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			obj.put("msg", "重置失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		if (returnId <= 0) {
			obj.put("msg", "重置失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		obj.put("msg", 1);
		JSONHelper.printObject(obj);
		return null;
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
		String jsonStr = SqlInfusionHelper.filteSqlInfusion(JSONArray.toJSONString(list));
		ServletHelper.returnStr(ServletActionContext.getResponse(), jsonStr);
		return null;
	}

	/**
	 * 重置投资人的经纪人
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateAgentParentId() throws Exception {
		JSONObject obj = new JSONObject();
		long id = Convert.strToLong(request("id"), -1);
		long parentId = Convert.strToLong(request("parentId"), -1);
		if (id <= 0 || parentId <= 0) {
			obj.put("msg", "重置失败，数据错误！");
			JSONHelper.printObject(obj);
			return null;
		}
		long returnId = -1;
		try {
			returnId = relationService.updateRelation(id, null, parentId, null,
					IConstants.RELATION_ENABLE_TRUE);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_relation", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"重置投资人的经纪人", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			obj.put("msg", "重置失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		if (returnId <= 0) {
			obj.put("msg", "重置失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		obj.put("msg", 1);
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * 重置理财人的投资人
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateInvestorParentId() throws Exception {
		JSONObject obj = new JSONObject();
		long id = Convert.strToLong(request("id"), -1);
		long parentId = Convert.strToLong(request("parentId"), -1);
		if (id <= 0 || parentId <= 0) {
			obj.put("msg", "重置失败，数据错误！");
			JSONHelper.printObject(obj);
			return null;
		}
		long returnId = -1;
		try {
			returnId = relationService.updateRelation(id, null, parentId, null,
					IConstants.RELATION_ENABLE_TRUE);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_relation", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"重置理财人的投资人", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			obj.put("msg", "重置失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		if (returnId <= 0) {
			obj.put("msg", "重置失败！");
			JSONHelper.printObject(obj);
			return null;
		}
		obj.put("msg", 1);
		JSONHelper.printObject(obj);
		return null;
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

	public List<Map<String, Object>> getParentList() throws Exception {
		if (parentList == null) {
			parentList = adminService
					.queryAdminByRoleId(IConstants.ADMIN_ROLE_GROUP);
		}
		return parentList;
	}

	public void setRelationService(RelationService relationService) {
		this.relationService = relationService;
	}

}
