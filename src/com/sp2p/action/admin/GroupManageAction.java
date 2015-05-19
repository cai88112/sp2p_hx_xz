package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.fp2p.helper.shove.SMSHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.SendMailService;
import com.sp2p.service.admin.GroupService;
import com.sp2p.service.admin.SMSInterfaceService;


/**
 * 用户组管理
 */
public class GroupManageAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(GroupManageAction.class);
	private static final long serialVersionUID = 1L;

	private GroupService groupService;

	private List<Map<String, Object>> groupList;

	private SMSInterfaceService sMsService;

	private SendMailService sendMailService;

	/**
	 * 查询用户组初始化
	 * 
	 * @return
	 */
	public String queryGroupInit() {
		return SUCCESS;
	}

	/**
	 * 查询用户组
	 * 
	 * @return
	 */
	public String queryGroupInfo() {
		try {
			// ----------add by houli
			String groupName = paramMap.get("groupName") == null ? null
					: paramMap.get("groupName");
			// ----------
			groupService.queryGroupForPage(pageBean, SqlInfusionHelper
					.filteSqlInfusion(groupName));
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 添加用户组初始化
	 * 
	 * @return
	 */
	public String addGroupInit() {
		return SUCCESS;
	}

	/**
	 * 从用户组中删除用户关系
	 * 
	 * @return
	 */
	public String deleteGroupUser() {
		String ids = request("id");
		try {
			groupService.deleteGroupUsersByIds(SqlInfusionHelper
					.filteSqlInfusion(ids));
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_group_user", admin
					.getUserName(), IConstants.DELETE, admin.getLastIP(), 0,
					"从用户组中删除用户关系", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 修改用户组成员初始化
	 * 
	 * @return
	 */
	public String queryGroupUserInit() {
		return SUCCESS;
	}

	/**
	 * 修改用户组成员详情
	 * 
	 * @return
	 */
	public String queryGroupUserInfo() {
		String realName = paramMap.get("realName");
		String userName = paramMap.get("userName");
		double startAllSum = Convert.strToDouble(paramMap.get("startAllSum"),
				-1);
		double endAllSum = Convert.strToDouble(paramMap.get("endAllSum"), -1);
		double startUseableSum = Convert.strToDouble(paramMap
				.get("startUseableSum"), -1);
		double endUseableSum = Convert.strToDouble(paramMap
				.get("endUseableSum"), -1);
		try {
			groupService.queryGroupUserForPage(SqlInfusionHelper
					.filteSqlInfusion(userName), SqlInfusionHelper
					.filteSqlInfusion(realName), startAllSum, endAllSum,
					startUseableSum, endUseableSum, -1, pageBean);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 根据用户名查询Id
	 * 
	 * @return
	 */
	public String queryUserIdByUserName() {
		String userName = paramMap.get("userName");
		try {
			if (StringUtils.isNotBlank(userName)) {
				userName = userName.replace("，", ",");
				String[] userNames = userName.split(",");
				StringBuilder newUserName = new StringBuilder();
				for (String name : userNames) {
					newUserName.append("'");
					newUserName.append(name);
					newUserName.append("',");
				}
				int length = newUserName.length();
				if (length > 0) {
					newUserName.delete(length - 1, length);
				}
				List<Map<String, Object>> list = groupService
						.queryUserIdByUserName(SqlInfusionHelper
								.filteSqlInfusion(newUserName+""));
				JSONHelper.printArray(list);
			} else {
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				JSONHelper.printArray(list);
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 向用户组添加用户
	 * 
	 * @return
	 */
	public String addGroupUsers() {
		String ids = request("userId");
		long groupId = Convert.strToLong(request("groupId"), -1);
		long result = -1;
		try {
			if (StringUtils.isNotBlank(ids)) {
				String[] arrayId = ids.split(",");
				if (arrayId != null) {

					List<Long> idList = new ArrayList<Long>();
					for (String id : arrayId) {
						idList.add(Convert.strToLong(id, -1));
					}
					result = groupService.addGroupUser(idList, groupId);
				}

			}
			ServletHelper.returnStr(ServletActionContext.getResponse(), result + "");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_group_user", admin
					.getUserName(), IConstants.INSERT, admin.getLastIP(), 0,
					"向用户组添加用户", 2);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 添加用户组
	 * 
	 * @return
	 */
	public String addGroup() {
		String groupName = paramMap.get("groupName");
		String groupRemark = paramMap.get("groupRemark");
		String userIds = paramMap.get("userId");
		// modify by houli 增加用户的提现状态设置
		String cashStatus = paramMap.get("cashStatus");

		int c_status = IConstants.CASH_STATUS_ON;
		if (cashStatus == null) {
			c_status = IConstants.CASH_STATUS_OFF;
		}
		// ----

		long adminId = ((Admin) session()
				.getAttribute(IConstants.SESSION_ADMIN)).getId();
		try {
			List<Long> userIdList = getIdList(userIds);
			groupService.addGroup(adminId, userIdList, SqlInfusionHelper
					.filteSqlInfusion(groupName), SqlInfusionHelper
					.filteSqlInfusion(groupRemark), c_status);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_group", admin.getUserName(),
					IConstants.INSERT, admin.getLastIP(), 0, "添加新的用户组", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	private List<Long> getIdList(String userIds) {
		List<Long> userIdList = new ArrayList<Long>();
		if (StringUtils.isNotBlank(userIds)) {
			String[] ids = userIds.split(",");
			for (String id : ids) {
				long userId = Convert.strToLong(id, -1);
				if (userId != -1 && !userIdList.contains(userId)) {
					userIdList.add(userId);
				}
			}
		}
		return userIdList;
	}

	/**
	 * 删除用户组
	 * 
	 * @return
	 */
	public String deleteGroup() {
		String id = request("id");
		if (StringUtils.isNotBlank(id)) {
			try {
				groupService.deleteGroup(id);
				Admin admin = (Admin) session().getAttribute(
						IConstants.SESSION_ADMIN);
				operationLogService.addOperationLog("t_group", admin
						.getUserName(), IConstants.DELETE, admin.getLastIP(),
						0, "删除用户组", 2);
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
		}
		return SUCCESS;
	}

	/**
	 * 修改用户组成员初始化
	 * 
	 * @return
	 */
	public String updateGroupInit() {
		return SUCCESS;
	}

	/**
	 * 提现管理初始化
	 * 
	 * @return
	 */
	public String queryDrawCashManagerInit() {
		return SUCCESS;
	}

	/**
	 * 提现管理详情
	 * 
	 * @return
	 */
	public String queryDrawCashManagerInfo() {
		String realName = paramMap.get("realName");
		String userName = paramMap.get("userName");
		double startAllSum = Convert.strToDouble(paramMap.get("startAllSum"),
				-1);
		double endAllSum = Convert.strToDouble(paramMap.get("endAllSum"), -1);
		double startUseableSum = Convert.strToDouble(paramMap
				.get("startUseableSum"), -1);
		double endUseableSum = Convert.strToDouble(paramMap
				.get("endUseableSum"), -1);
		long cashStatus = Convert.strToLong(paramMap.get("cashStatus"), -1);
		try {
			groupService.queryGroupUserForPage(SqlInfusionHelper
					.filteSqlInfusion(userName), SqlInfusionHelper
					.filteSqlInfusion(realName), startAllSum, endAllSum,
					startUseableSum, endUseableSum, cashStatus, pageBean);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 更新用户提现状态
	 * 
	 * @return
	 */
	public String updateUserCashStatus() {
		String cashStatus = request("cashStatus");
		String userId = request("id");
		try {
			groupService.updateUserCashStatus(userId, cashStatus);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_user", admin.getUserName(),
					IConstants.UPDATE, admin.getLastIP(), 0, "更新用户组的提现状态", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 发送短信初始化
	 * 
	 * @return
	 */
	public String sendGroupMessageInit() {
		return SUCCESS;
	}

	/**
	 * 发送邮件初始化
	 * 
	 * @return
	 */
	public String sendGroupEmailInit() {
		return SUCCESS;
	}

	/**
	 * 向组用户发送短信
	 * 
	 * @return
	 */
	public String sendGroupMessage() {
		long groupId = Convert.strToLong(paramMap.get("groupId"), -1);
		String content = paramMap.get("content");

		try {
			if (StringUtils.isBlank(content)) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "-2");
				return null;
			}
			List<Map<String, Object>> phoneList = groupService
					.queryUserPhoneByGroupId(groupId);

			if (phoneList != null && phoneList.size() > 0) {
				StringBuilder phone = new StringBuilder();
				for (Map<String, Object> phoneMap : phoneList) {
					phone.append(phoneMap.get("cellPhone"));
					phone.append(",");
				}
				phone
						.delete(phone.lastIndexOf(","),
								phone.lastIndexOf(",") + 1);

				// 发送短信
				Map<String, String> map = sMsService.getSMSById(1);
				if(map == null){
					map = new HashMap<String, String>();
				}

//				String retCode = SMSHelper.sendSMS(SqlInfusionHelper
//						.filteSqlInfusion(map.get("Account")), SqlInfusionHelper
//						.filteSqlInfusion(map.get("Password")), SqlInfusionHelper
//						.filteSqlInfusion(content), SqlInfusionHelper
//						.filteSqlInfusion(phone+""), null);
				
				String retCode = SMSHelper.sendSMSUseXuanWu(SqlInfusionHelper
						.filteSqlInfusion(map.get("Account")), SqlInfusionHelper
						.filteSqlInfusion(map.get("Password")), SqlInfusionHelper
						.filteSqlInfusion(content), SqlInfusionHelper
						.filteSqlInfusion(phone+""), null);
				if ("Sucess".equals(retCode)) {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
					return null;
				} else {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
					return null;
				}

			}
		} catch (Exception e) {
			log.error(e);

		}
		return null;
	}

	/**
	 * 向组用户发送邮件
	 * 
	 * @return
	 * @throws IOException
	 */
	public String sendGroupEmail() {
		String title = paramMap.get("title");
		String content = paramMap.get("content");
		long groupId = Convert.strToLong(paramMap.get("groupId"), -1);
		long result = -1;
		try {
			if (StringUtils.isBlank(title)) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "-2");
				return null;
			}
			if (StringUtils.isBlank(content)) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "-3");
				return null;
			}
			List<Map<String, Object>> emailList = groupService
					.queryUserEmailByGroupId(groupId);
			if (emailList != null) {
				for (Map<String, Object> map : emailList) {
					sendMailService.sendUserGroupEmail(SqlInfusionHelper.filteSqlInfusion(map.get("email") + ""),
							SqlInfusionHelper.filteSqlInfusion(map.get("username") + ""), SqlInfusionHelper.filteSqlInfusion(title), SqlInfusionHelper.filteSqlInfusion(content));
				}
			}
			result = 1;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		try {
			ServletHelper.returnStr(ServletActionContext.getResponse(), result + "");
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检索用户组成员初始化
	 * 
	 * @return
	 */
	public String queryGroupUsersInit() {
		return SUCCESS;
	}

	/**
	 * 查询用户组成员初始化
	 * 
	 * @return
	 */
	public String queryGroupMemberInit() {
		return SUCCESS;
	}

	/**
	 * 删除用户组成员
	 * 
	 * @return
	 */
	public String deleteGroupMemberUser() {
		return this.deleteGroupUser();
	}

	/**
	 * 查询用户组成员
	 * 
	 * @return
	 */
	public String queryGroupMemberInfo() {
		return queryGroupUsersInfo();
	}

	/**
	 * 检索用户组成员
	 * 
	 * @return
	 */
	public String queryGroupUsersInfo() {
		long groupId = Convert.strToLong(paramMap.get("groupId"), -1);
		String realName = paramMap.get("realName");
		String userName = paramMap.get("userName");
		double startAllSum = Convert.strToDouble(paramMap.get("startAllSum"),
				-1);
		pageBean.setPageSize(Convert.strToInt(request("pageSize"), 10));
		double endAllSum = Convert.strToDouble(paramMap.get("endAllSum"), -1);
		double startUseableSum = Convert.strToDouble(paramMap
				.get("startUseableSum"), -1);
		double endUseableSum = Convert.strToDouble(paramMap
				.get("endUseableSum"), -1);
		try {
			groupService.queryGroupUsersForPage(SqlInfusionHelper.filteSqlInfusion(userName), SqlInfusionHelper.filteSqlInfusion(realName),
					startAllSum, endAllSum, startUseableSum, endUseableSum,
					groupId, pageBean);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 导出选中记录
	 * 
	 * @return
	 */
	public String exportUserMember() {
		String ids = request("id");
		List<Map<String, Object>> list = null;
		try {
			list = groupService.queryGroupUsersByIds(SqlInfusionHelper.filteSqlInfusion(ids));
			if (list.size() == 0) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (list.size() > IConstants.EXCEL_MAX) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}
			HSSFWorkbook hsw = ExcelHelper.exportExcel("sheet1", list,
					new String[] { "用户组", "帐号", "真实姓名", "身份证号", "邮箱", "电话",
							"帐号总金额", "可用金额", "待收金额" }, new String[] {
							"groupName", "username", "realName", "idNo",
							"email", "cellPhone", "allSum", "usableSum",
							"dueinSum" });

			this.export(hsw, DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")
					+ ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("v_t_groupuser_user_person",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出用户组中用户信息", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 更新用户组信息初始化 add by houli。进行用户组信息修改的初始化
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public String modifyGroupInit() throws SQLException, DataException {
		Long groupId = Convert.strToLong(request("groupId"), -100);
		try {
			paramMap = groupService.queryGroupById(groupId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public String modifyGroup() {
		Long groupId = Convert.strToLong(paramMap.get("groupId"), -100L);
		String groupName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("groupName"));
		String groupRemark = SqlInfusionHelper.filteSqlInfusion(paramMap.get("groupRemark"));
		String userIds = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userId"));
		String cashStatus = SqlInfusionHelper.filteSqlInfusion(paramMap.get("cashStatus"));

		int c_status = IConstants.CASH_STATUS_ON;
		if (cashStatus == null) {
			c_status = IConstants.CASH_STATUS_OFF;
		}

		long adminId = ((Admin) session()
				.getAttribute(IConstants.SESSION_ADMIN)).getId();
		try {
			List<Long> userIdList = getIdList(userIds);
			groupService.modifyGroup(groupId, adminId, userIdList, groupName,
					groupRemark, c_status);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_group", admin.getUserName(),
					IConstants.UPDATE, admin.getLastIP(), 0, "修改用户组信息", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public List<Map<String, Object>> getGroupList() throws Exception {
		groupList = groupService.queryAllGroup();
		return groupList;
	}

	public void setGroupList(List<Map<String, Object>> groupList) {
		this.groupList = groupList;
	}

	public void setGroupService(GroupService groupService) {
		this.groupService = groupService;
	}

	public void setSMsService(SMSInterfaceService msService) {
		sMsService = msService;
	}

	public void setSendMailService(SendMailService sendMailService) {
		this.sendMailService = sendMailService;
	}

}