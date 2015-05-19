package com.sp2p.action.admin;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.BeanHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.JSONUtils;
import com.shove.data.DataException;
import com.shove.security.Encrypt;
import com.shove.web.action.BasePageAction;

import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.BBSRegisterService;
import com.sp2p.service.OperationLogService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.AdminService;
import com.sp2p.service.admin.RoleRightsService;
import com.sp2p.service.admin.RoleService;

@SuppressWarnings( { "serial", "unchecked" })
public class AdminAction extends BasePageAction {

	public static Log log = LogFactory.getLog(AdminAction.class);

	private AdminService adminService;
	private RoleService roleService;
	private RoleRightsService roleRightsService;
	private UserService userService;
	private List<Map<String, Object>> roleList;
	private BBSRegisterService bbsRegisterService;

	public BBSRegisterService getBbsRegisterService() {
		return bbsRegisterService;
	}

	public void setBbsRegisterService(BBSRegisterService bbsRegisterService) {
		this.bbsRegisterService = bbsRegisterService;
	}

	/**
	 * 论坛后台登陆
	 * 
	 * @return
	 * @throws Exception
	 */
	public String logging() throws Exception {
		System.out.println("adsfa");
		getOut().print(
				"<script>parent.location.href='" + IConstants.BBS_URL
						+ "logging.do?action=toLogin&admin=admin';</script>");
		System.out.println("zzz");
		return null;
	}

	/**
	 * 查询管理员初始化
	 * 
	 * @return
	 */
	public String queryAdminInit() {
		return SUCCESS;
	}

	/**
	 * 查询管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryAdminInfo() throws Exception {
		String userName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("userName"));
		Integer enable = Convert.strToInt(paramMap.get("enable"), -1);
		Long roleId = Convert.strToLong(paramMap.get("roleId"), -1);
		adminService.queryAdminPage(userName, enable, roleId, pageBean);
		return SUCCESS;
	}

	/**
	 * 查询管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String isenableAdmin() throws Exception {
		long id = Convert.strToLong(request("id"), -1);
		int enable = Convert.strToInt(request("enable"), -1);
		long result = adminService.isenableAdmin(id, enable);
		if (result > 0) {
			return SUCCESS;
		}
		return INPUT;
	}

	/**
	 * 添加管理员初始化
	 * 
	 * @return
	 */
	public String addAdminInit() {
		paramMap.put("enable", 1 + "");
		return SUCCESS;
	}

	/**
	 * 添加管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addAdmin() throws Exception {
		String userName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("userName"));
		String password = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("password"));
		String realName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("realName"));
		String telphone = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("telphone"));
		String qq = SqlInfusionHelper.filteSqlInfusion(paramMap.get("qq"));
		String email = SqlInfusionHelper.filteSqlInfusion(paramMap.get("email"));
		String img = SqlInfusionHelper.filteSqlInfusion(paramMap.get("img"));
		String isLeader = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("isLeader"));
		Integer enable = Integer.parseInt(paramMap.get("enable"));
		long roleId = Convert.strToLong(paramMap.get("roleId"), -1);
		Long returnId = -1L;
		try {
			returnId = adminService.addAdmin(userName, password, enable,
					roleId, realName, telphone, qq, email, img, isLeader);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		if (returnId == -2) {
			this.addFieldError("paramMap.userName", "用户名重复");
			return INPUT;
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_admin", admin.getUserName(),
				IConstants.INSERT, admin.getLastIP(), 0, "添加新管理员", 2);
		return SUCCESS;
	}

	/**
	 * 修改管理员初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateAdminInit() throws Exception {
		Long id = Long.parseLong(request("id"));
		paramMap = adminService.queryAdminById(id);
		String key = Encrypt.encryptSES(id+"-"+new Date().getTime()+"",IConstants.BBS_SES_KEY);
		String md5Id = Encrypt.MD5(key+IConstants.BBS_SES_KEY).substring(0,10)+key;
		paramMap.put("key",md5Id);
		return SUCCESS;
	}

	/**
	 * 修改管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateAdmin() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		String key = paramMap.get("key");
		Long id = -1l;
		String mdKey = key.substring(0,10);
		String mdValue = key.substring(10,key.length());
		String mdCompare = Encrypt.MD5(mdValue+IConstants.BBS_SES_KEY).substring(0,10);
		String valAll = Encrypt.decryptSES(mdValue, IConstants.BBS_SES_KEY);
		if(!mdKey.equals(mdCompare)){
			ServletHelper.returnStr(ServletActionContext.getResponse(), "签名错误");
			return null;
		}
		String[] keys = valAll.split("-");
		String md5 = keys[0].toString();
		id = Convert.strToLong(md5, -1);
		String dateTime = keys[1].toString();
		long curTime = new Date().getTime();
		// 当用户点击时间大于于1分钟
		if (curTime - Long.valueOf(dateTime) >= 60 * 1000) {
			obj.put("mailAddress", "已超时");
			JSONUtils.printObject(obj);
			return null;
		}
		String password = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("password"));
		String realName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("realName"));
		String telphone = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("telphone"));
		String qq = SqlInfusionHelper.filteSqlInfusion(paramMap.get("qq"));
		String email = SqlInfusionHelper.filteSqlInfusion(paramMap.get("email"));
		String img = SqlInfusionHelper.filteSqlInfusion(paramMap.get("img"));
		String isLeader = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("isLeader"));
		Integer enable = Integer.parseInt(paramMap.get("enable"));
		long roleId = Convert.strToLong(paramMap.get("roleId"), -1);
		try {
			adminService.updateAdmin(id, password, enable, null, roleId,
					realName, telphone, qq, email, img, isLeader);
			bbsRegisterService.doUpdatePwdByAsynchronousMode(admin
					.getUserName(), password, admin.getPassword(), 1);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		
		operationLogService.addOperationLog("t_admin", admin.getUserName(),
				IConstants.UPDATE, admin.getLastIP(), 0, "修改管理员信息", 2);
		return SUCCESS;

	}

	/**
	 * 删除管理员
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteAdmin() throws Exception {
		String adminIds = SqlInfusionHelper.filteSqlInfusion(request("id"));

		String[] adminids = adminIds.split(",");
		int length = adminids.length;
		if (length <= 0) {
			return SUCCESS;
		}
		long[] teacherid = new long[length];
		for (int i = 0; i < adminids.length; i++) {
			teacherid[i] = Convert.strToLong(adminids[i], -1);
			if (teacherid[i] == -1) {
				return SUCCESS;
			}
		}
		try {
			adminService.deleteAdmin(adminIds);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_admin", admin.getUserName(),
					IConstants.DELETE, admin.getLastIP(), 0, "删除id为" + adminIds
							+ "的管理员", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 登陆
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 * @throws Exception
	 * @throws AdminHelpMessageException
	 */
	public String adminLogin() throws Exception {
		String pageId = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"pageId"));
		String code = SqlInfusionHelper.filteSqlInfusion((String) session()
				.getAttribute(pageId + "_checkCode"));
		String _code = SqlInfusionHelper.filteSqlInfusion((paramMap.get("code")
				+"").trim());
		if (code == null || !_code.equals(code)) {
			this.addFieldError("paramMap.code", "验证码错误！");
			return INPUT;
		}
		String userName = SqlInfusionHelper.filteSqlInfusion((paramMap.get("userName")
				+"").trim());
		String password = SqlInfusionHelper.filteSqlInfusion((paramMap.get("password")
				+"").trim());

		Admin admin = null;
		try {
			admin = adminService.adminLogin(userName, password, ServletHelper
					.getIpAddress(ServletActionContext.getRequest()));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		if (admin == null) {
			this.addFieldError("paramMap.userName", "用户名或密码错误");
			return INPUT;
		}
		if (admin.getEnable() != 1) {
			this.addFieldError("paramMap.password", "你的帐号被停用请联系站点管理员");
			return INPUT;
		}
		long roleId = admin.getRoleId();

		// 后台登录初始页面
		// --审核管理
		Map<String, String> map = adminService.queryCheckCount(admin.getId());
		session().setAttribute("map", map);
		// 天添加后台操作日志
		operationLogService.addOperationLog("t_admin", admin.getUserName(),
				IConstants.UPDATE, admin.getLastIP(), 0, "后台管理员登陆", 2);

		List<Map<String, Object>> list = roleRightsService
				.queryAdminRoleRightMenu(roleId);
		session().setAttribute("index", -1);
		session().setAttribute("adminRoleMenuList", list);
		session().setAttribute(IConstants.SESSION_ADMIN, admin);
		return SUCCESS;
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public String adminLoginOut() {
		session().removeAttribute(IConstants.SESSION_ADMIN);
		return SUCCESS;
	}

	/**
	 * 修改密码初始化
	 * 
	 * @return
	 */
	public String updatePasswordInit() {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		paramMap = BeanHelper.beanToMap(admin);
		paramMap.put("password", "");
		paramMap.put("oldPassword", "");
		return SUCCESS;
	}

	/**
	 * 同步用户
	 * 
	 * @return
	 * @throws Exception
	 */
	public String syncBBSUser() throws Exception {
		try {
			List<Map<String, Object>> list = userService.queryUserAll();
			if (list != null) {
				String strURL = SqlInfusionHelper.filteSqlInfusion(IConstants.BBS_URL
						.endsWith("/") ? IConstants.BBS_URL
						+ "otherweb.do?action=memberInitAdd"
						: IConstants.BBS_URL
								+ "/otherweb.do?action=memberInitAdd");
				URL url = new URL(strURL);
				for (Map<String, Object> map : list) {

					String parameters = SqlInfusionHelper
							.filteSqlInfusion("groupid=10&regsubmit=yes&alipay=&answer=&bday=0000-00-00&bio=&dateformat=0&email="
									+ URLEncoder.encode(map.get("email") + "",
											"UTF-8")
									+ "&formHash=6a36c78f&gender=0&icq=&location=&msn=&newsletter=1&password="
									+ URLEncoder.encode(map.get("password")
											+ "", "UTF-8")
									+ "&password2="
									+ URLEncoder.encode(map.get("password")
											+ "", "UTF-8")
									+ "&pmsound=1&ppp=0&qq=&questionid=0&showemail=1&signature=&site=&styleid=0&taobao=&timeformat=0&timeoffset=9999&tpp=0&username="
									+ URLEncoder.encode(map.get("userName")
											+ "", "UTF-8")
									+ "&yahoo=&k="
									+ Encrypt.encryptSES(IConstants.BBS_KEY,
											IConstants.BBS_SES_KEY));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setAllowUserInteraction(false);
					conn.setRequestProperty("User-Agent", "Internet Explorer");
					BufferedOutputStream buf = new BufferedOutputStream(conn
							.getOutputStream());
					buf.write(parameters.getBytes(), 0, parameters.length());
					buf.flush();
					buf.close();
					String cookie = SqlInfusionHelper.filteSqlInfusion(conn
							.getHeaderField("Set-Cookie"));
					String sessionId = SqlInfusionHelper.filteSqlInfusion(cookie
							.substring(0, cookie.indexOf(";")));
					conn.disconnect();
				}

			}
		} catch (DataException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		OutputStream output = this.response().getOutputStream();
		PrintWriter pw = new PrintWriter(output);
		pw.write("同步成功！");
		pw.flush();
		pw.close();
		output.close();
		return null;
	}

	/**
	 * 修改当前用户密码
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String updatePassword() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		String oldPassword = SqlInfusionHelper.filteSqlInfusion(paramMap.get("oldPassword").trim());
		String password = SqlInfusionHelper.filteSqlInfusion(paramMap.get("password").trim());
		if ("1".equals(IConstants.ENABLED_PASS)) {
			oldPassword = Encrypt.MD5(oldPassword.trim());
		} else {
			oldPassword = Encrypt.MD5(oldPassword.trim() + IConstants.PASS_KEY);
		}

		String confirmPassword = SqlInfusionHelper.filteSqlInfusion(paramMap.get("confirmPassword").trim());
		if (!admin.getPassword().equals(oldPassword)) {
			this.addFieldError("paramMap.oldPassword", "旧密码输入错误");
			return INPUT;
		} else if (!password.equals(confirmPassword)) {
			this.addFieldError("paraMap.oldPassword", "确认密码与新密码不一致");
			return INPUT;
		} else {
			try {
				adminService.updateAdmin(admin.getId(), password, null, null,
						null, null, null, null, null, null, null);
				// 后台操作日志
				operationLogService.addOperationLog("t_admin", admin
						.getUserName(), IConstants.UPDATE, admin.getLastIP(),
						0, "管理员修改密码", 2);
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				throw e;
			}
		}

		return SUCCESS;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public List<Map<String, Object>> getRoleList() throws Exception {
		if (roleList != null) {
			return roleList;
		}
		roleList = roleService.queryRoleList();
		return roleList;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setRoleRightsService(RoleRightsService roleRightsService) {
		this.roleRightsService = roleRightsService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public OperationLogService getOperationLogService() {
		return operationLogService;
	}

	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public RoleRightsService getRoleRightsService() {
		return roleRightsService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setRoleList(List<Map<String, Object>> roleList) {
		this.roleList = roleList;
	}

}
