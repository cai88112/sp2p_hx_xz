package com.sp2p.action.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.action.front.FrontMyPaymentAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.RegionService;
import com.sp2p.service.UserService;
import com.sp2p.service.ValidateService;
import com.sp2p.service.admin.UserManageServic;

/**
 * 后台用户管理
 * 
 * @author lw
 *
 */
public class UserManageAction extends BaseFrontAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(UserManageAction.class);
	private UserManageServic userManageServic;
	private RegionService regionService;
	private UserService userService;
	private ValidateService validateService;
	private List<Map<String, Object>> provinceList;
	private List<Map<String, Object>> cityList;
	private List<Map<String, Object>> regcityList;
	private long workPro = -1L;// 初始化省份默认值
	private long cityId = -1L;// 初始化话默认城市
	private long regPro = -1L;// 户口区域默认值
	private long regCity = -1L;// 户口区域默认值

	public long getWorkPro() {
		return workPro;
	}

	public void setWorkPro(long workPro) {
		this.workPro = workPro;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getRegPro() {
		return regPro;
	}

	public void setRegPro(long regPro) {
		this.regPro = regPro;
	}

	public long getRegCity() {
		return regCity;
	}

	public void setRegCity(long regCity) {
		this.regCity = regCity;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setUserManageServic(UserManageServic userManageServic) {
		this.userManageServic = userManageServic;
	}

	/**
	 * 跳转到用户基本信息管理初始化
	 * 
	 * @return
	 */
	public String queryUserManageBaseInfoindex() {
		return SUCCESS;
	}

	/**
	 * 跳转到用户基本信息管理详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryUserManageBaseInfo() throws Exception {
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("userName"));
		String realName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("realName"));

		userManageServic.queryUserManageBaseInfo(pageBean, userName, realName);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}

	/**
	 * 导出用户列表基本信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportUserManageBaseInfo() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 用户名
			String userName = SqlInfusionHelper.filteSqlInfusion(paramMap
					.get("userName"));
			String realName = SqlInfusionHelper.filteSqlInfusion(paramMap
					.get("realName"));
			// 待还款详情
			userManageServic.queryUserManageBaseInfo(pageBean, userName,
					realName);
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

			HSSFWorkbook wb = ExcelHelper.exportExcel("用户基本信息列表",
					pageBean.getPage(), new String[] { "用户名", "真是姓名", "信用积分",
							"会员积分", "身份证", "投资信息", "个人信息", "工作信息" },
					new String[] { "username", "realName", "creditrating",
							"rating", "idNo", "totalAmount", "tpauditStatus",
							"twauditStatus", });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出用户列表基本信息", 2);

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
	 * 用户基本信息管理初始化
	 * 
	 * @return
	 */
	public String queryUserManageInfoIndex() {
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String queryUserManageInfo() throws Exception {
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("userName"));
		String realName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("realName"));

		userManageServic.queryUserManageInfo(pageBean, userName, realName);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}

	// 用户列表中 查看个人信息
	@SuppressWarnings("unchecked")
	public String queryUserInfo() throws Exception {
		long id = Convert.strToLong(
				SqlInfusionHelper.filteSqlInfusion(request("id")), -1);
		Map<String, String> map = userManageServic.queryUserInfo(id);
		if (map == null) {
			map = new HashMap<String, String>();
		}
		request().setAttribute("username", map.get("username"));
		request().setAttribute("realName", map.get("realName"));
		request().setAttribute("rating", map.get("rating"));
		request().setAttribute("creditrating", map.get("creditrating"));
		request().setAttribute("createTime", map.get("createTime"));
		request().setAttribute("lastIP", map.get("lastIP"));
		request().setAttribute("email", map.get("email"));
		request().setAttribute("cellPhone", map.get("cellPhone"));
		request().setAttribute("qq", map.get("qq"));
		request().setAttribute("userId", map.get("id"));
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String updateUserqq() throws Exception {
		JSONObject obj = new JSONObject();
		String qq = SqlInfusionHelper.filteSqlInfusion(paramMap.get("qq"));
		long userId = Integer.parseInt(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("userId")));
		long result = -1L;
		result = userManageServic.updateUserqq(userId, qq);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);

		try {
			if (result >= 1) {
				operationLogService.addOperationLog("t_person",
						admin.getUserName(), IConstants.UPDATE,
						admin.getLastIP(), 0, "更新用户基本信息（QQ）", 2);
				obj.put("msg", "1");
				JSONHelper.printObject(obj);
				return null;
			} else {
				obj.put("msg", "操作失败");
				JSONHelper.printObject(obj);
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 导出用户列表信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportusermanageinfo() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 用户名
			String userName = SqlInfusionHelper.filteSqlInfusion(paramMap
					.get("userName"));
			String realName = SqlInfusionHelper.filteSqlInfusion(paramMap
					.get("realName"));
			// 待还款详情
			userManageServic.queryUserManageInfo(pageBean, userName, realName);
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

			HSSFWorkbook wb = ExcelHelper.exportExcel("用户列表",
					pageBean.getPage(), new String[] { "用户名", "真是姓名", "邮箱",
							"QQ号码", "手机号码", "注册时间", "最后登录IP" }, new String[] {
							"username", "realName", "email", "qq", "cellPhone",
							"createTime", "lastIP", });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出用户列表基本信息", 2);

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
	 * 跳转到用户信用积分管理
	 * 
	 * @return
	 */
	public String queryUserManageintegralindex() {
		return SUCCESS;
	}

	/**
	 * 跳转到用户信用积分管理详细
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryUserManageintegralinfo() throws Exception {
		String username = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("username"));
		int viprecode = Convert.strToInt(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("viprecode")),
				-1);
		int creditcode = Convert.strToInt(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("creditcode")),
				-1);
		userManageServic.queryUserManageintegralinfo(pageBean, username,
				viprecode, creditcode);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}

	/**
	 * 导出用户积分信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportintegralinfo() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 用户名
			String username = SqlInfusionHelper.filteSqlInfusion(request()
					.getParameter("username") == null ? "" : request()
					.getParameter("username"));
			username = URLDecoder.decode(username, "UTF-8");
			// 会员积分
			int viprecode = Convert.strToInt(
					SqlInfusionHelper.filteSqlInfusion(request().getParameter(
							"viprecode")), -1);
			// 用户积分排序
			int creditcode = Convert.strToInt(
					SqlInfusionHelper.filteSqlInfusion(request().getParameter(
							"creditcode")), -1);
			// 待还款详情
			userManageServic.queryUserManageintegralinfo(pageBean, username,
					viprecode, creditcode);
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

			HSSFWorkbook wb = ExcelHelper.exportExcel("用户积分",
					pageBean.getPage(), new String[] { "用户名", "真是姓名", "信用积分",
							"会员积分", "最后调整时间" },
					new String[] { "username", "realName", "creditrating",
							"rating", "repayDate", });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("v_t_usermanage_integralinfo",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出用户积分信息列表", 2);

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
	 * vip记录表初始化
	 * 
	 * @return
	 */
	public String queryUservipRecoderindex() {
		return SUCCESS;
	}

	/**
	 * vip记录表详细内容
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryUservipRecoderinfo() throws Exception {
		String username = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("username"));
		String appstarttime = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("appstarttime"));
		String appendTime = FrontMyPaymentAction.changeEndTime(Convert
				.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
						.get("appendTime")), null));
		String laststarttime = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("laststarttime"));
		String lastendTime = FrontMyPaymentAction.changeEndTime(Convert
				.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
						.get("lastendTime")), null));
		userManageServic.queryUservipRecoderinfo(pageBean, username,
				appstarttime, laststarttime, appendTime, lastendTime);

		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}

	/**
	 * 用户基本信息管理详细信息里面的详细
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserManageBaseInfoinner() throws Exception {
		Long uerId = Convert
				.strToLong(SqlInfusionHelper.filteSqlInfusion(request()
						.getParameter("i")), -1L);
		Map<String, String> map = null;
		Map<String, String> UserMsgmap = null;//
		String birth = null;
		String rxedate = null;
		map = userService.queryPersonById(uerId);
		UserMsgmap = userManageServic.queryUserManageInnerMsg(uerId);// 用户基本信息里面的查看用户的基本信息
		if (map != null && map.size() > 0) {
			workPro = Convert.strToLong(map.get("nativePlacePro") + "", -1L);
			cityId = Convert.strToLong(map.get("nativePlaceCity") + "", -1L);
			regPro = Convert.strToLong(map.get("registedPlacePro") + "", -1L);
			regCity = Convert.strToLong(map.get("registedPlaceCity") + "", -1L);

			birth = Convert.strToStr(map.get("birthday"), null);
			rxedate = Convert.strToStr(map.get("eduStartDay"), null);
			if (birth != null) {
				birth = birth.substring(0, 10);
			}
			if (rxedate != null) {
				rxedate = rxedate.substring(0, 10);
			}
		}
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		cityList = regionService.queryRegionList(-1L, workPro, 2);
		regcityList = regionService.queryRegionList(-1L, regPro, 2);
		request().setAttribute("map", map);
		request().setAttribute("provinceList", provinceList);
		request().setAttribute("cityList", cityList);
		request().setAttribute("regcityList", regcityList);
		request().setAttribute("birth", birth);
		request().setAttribute("rxedate", rxedate);
		request().setAttribute("UserMsgmap", UserMsgmap);

		return SUCCESS;

	}

	/**
	 * 查询用户管理模块中的基本信息管理中的工作信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserMangework() throws Exception {
		long id = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("uid")), -1L);
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> UserMsgmap = null;//
		map = validateService.queryWorkDataById(id);
		UserMsgmap = userManageServic.queryUserManageInnerMsg(id);// 用户基本信息里面的查看用户的基本信息
		if (map != null && map.size() > 0) {
			workPro = Convert.strToLong(map.get("workPro") + "", -1L);
			cityId = Convert.strToLong(map.get("workCity") + "", -1L);
		}
		request().setAttribute("id", id);
		provinceList = regionService.queryRegionList(-1L, 1L, 1);

		cityList = regionService.queryRegionList(-1L, workPro, 2);

		if (cityId == 0) {
			request().setAttribute("map", map);
			request().setAttribute("provinceList", provinceList);
			cityList = regionService.queryRegionList(-1L, 1L, 1);
			request().setAttribute("cityList", cityList);
		} else {
			request().setAttribute("map", map);
			request().setAttribute("provinceList", provinceList);
			request().setAttribute("cityList", cityList);
		}
		request().setAttribute("UserMsgmap", UserMsgmap);
		return SUCCESS;
	}

	/**
	 * 跳转到投资信息明细
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryUserManageInvest() throws Exception {
		Long uerId = Convert
				.strToLong(SqlInfusionHelper.filteSqlInfusion(request()
						.getParameter("i")), -1L);
		String createtimeStart = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("createtimeStart"));
		String createtimeEnd = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("createtimeEnd"));
		userManageServic.queryUserManageInvest(pageBean, uerId,
				createtimeStart, createtimeEnd);
		Map<String, String> UserMsgmap = null;
		UserMsgmap = userManageServic.queryUserManageInnerMsg(uerId);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		request().setAttribute("UserMsgmap", UserMsgmap);
		request().setAttribute("userId", uerId);
		return SUCCESS;
	}

	/**
	 * 导出用户列表信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportuserInvestInfo() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 用户名
			Long uerId = Convert.strToLong(
					SqlInfusionHelper.filteSqlInfusion(request().getParameter(
							"i")), -1L);
			log.info(uerId);
			String createtimeStart = SqlInfusionHelper
					.filteSqlInfusion(paramMap.get("createtimeStart"));
			String createtimeEnd = SqlInfusionHelper.filteSqlInfusion(paramMap
					.get("createtimeEnd"));
			// 待还款详情
			userManageServic.queryUserManageInvest(pageBean, uerId,
					createtimeStart, createtimeEnd);
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

			userManageServic.changeFigure(pageBean);
			HSSFWorkbook wb = ExcelHelper.exportExcel("用户投资信息列表",
					pageBean.getPage(), new String[] { "用户名", "真时姓名", "手机号码",
							"投资日期", "还款方式", "投资期限", "投资标题", "投资金额" },
					new String[] { "username", "realName", "cellPhone",
							"investTime", "paymentMode", "deadline",
							"borrowTitle", "investAmount", });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("v_t_usermanage_invest",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出用户投资信息列表", 2);
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
	 * 跳转到信用分明细
	 * 
	 * @return
	 */
	public String userintegralcreditindex() {
		String userId = SqlInfusionHelper.filteSqlInfusion(request()
				.getParameter("id"));
		String type = SqlInfusionHelper.filteSqlInfusion(request()
				.getParameter("y"));
		request().setAttribute("i", userId);
		request().setAttribute("y", type);
		return SUCCESS;
	}

	/**
	 * 跳转到会员分明细info
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String userintegralcreditinfo() throws Exception {
		Long userId = Convert
				.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap
						.get("userId")), -1L);
		Integer type = Convert.strToInt(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("type")), -1);
		request().setAttribute("userId", userId);
		request().setAttribute("type", type);
		userManageServic.userintegralcreditinfo(pageBean, userId, type);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}

	/**
	 * 导出用户积分明细信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportuserintegralcreditinfo() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		Long userId = Convert.strToLong(
				SqlInfusionHelper.filteSqlInfusion(request("userId")), -1L);
		Integer type = Convert.strToInt(
				SqlInfusionHelper.filteSqlInfusion(request("type")), -1);

		try {

			// 待还款详情
			userManageServic.userintegralcreditinfo(pageBean, userId, type);
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

			HSSFWorkbook wb = ExcelHelper.exportExcel("用户积分明细",
					pageBean.getPage(), new String[] { "用户名", "真实姓名", "积分类型",
							"备注", "变动类型", "变动分值", "操作时间" }, new String[] {
							"username", "realName", "intergraltype", "remark",
							"changetype", "changerecore", "changtime" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("v_t_usermanage_integralinner",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出用户积分明细", 2);

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
	 * 弹出框初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addintegralindex() throws Exception {
		Map<String, String> popmap = null;
		Long id = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("id")), -1L);
		popmap = userManageServic.queryUserManageaddInteral(id);
		request().setAttribute("popmap", popmap);
		return SUCCESS;
	}

	/**
	 * 弹出框初始添加信用积分
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addintegralreal() throws Exception {
		Long userId = Convert.strToLong(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")), -1L);
		Integer type = Convert.strToInt(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("type")), -1);
		if (type == -1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}
		String scoreStr = SqlInfusionHelper.filteSqlInfusion(paramMap.get("s"));
		if (StringUtils.isBlank(scoreStr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		}
		if (!StringUtils.isNumeric(scoreStr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
			return null;
		}
		String remark = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("remark"));
		if (StringUtils.isBlank(remark)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		String typeStr = null;
		Long result = -1L;
		if (type == 1) {
			typeStr = "手动添加信用积分";
		}
		if (type == 2) {
			typeStr = "手动添加会员积分";
		}
		/*
		 * DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
		 * time=format.format(new Date());
		 */
		String changetype = "增加";// 先设置为增加类型
		Integer score = Convert.strToInt(scoreStr, -1);
		result = userManageServic.addIntervalDelt(userId, score, type, typeStr,
				remark, changetype);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_user/t_userintegraldetail",
				admin.getUserName(), IConstants.INSERT, admin.getLastIP(), 0,
				"添加信用积分及其明细", 2);
		if (result > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
			return null;
		}
	}

	/**
	 * add by houli 获得用户资金信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserCashInfo() throws Exception {
		try {
			Long userId = Convert
					.strToLong(SqlInfusionHelper
							.filteSqlInfusion(request("userId")), -100);
			Map<String, String> map = userManageServic
					.queryUserCashInfo(userId);
			if (map != null) {
				// request().setAttribute("map_", map);
				JSONObject obj = new JSONObject();
				obj.put("map_", map);

				JSONHelper.printObject(obj);
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	public List<Map<String, Object>> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<Map<String, Object>> provinceList) {
		this.provinceList = provinceList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getRegcityList() {
		return regcityList;
	}

	public void setRegcityList(List<Map<String, Object>> regcityList) {
		this.regcityList = regcityList;
	}

	public void setValidateService(ValidateService validateService) {
		this.validateService = validateService;
	}

}
