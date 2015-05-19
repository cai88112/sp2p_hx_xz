package com.sp2p.action.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

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
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.action.front.FrontMyFinanceAction;
import com.sp2p.action.front.FrontMyPaymentAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.OperationLogService;
import com.sp2p.service.SelectedService;
import com.sp2p.service.SendMessageService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.AdminService;
import com.sp2p.service.admin.FundManagementService;


/**
 * 后台财务管理
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings( { "serial", "unchecked" })
public class FIManageAction extends BasePageAction {

	public static Log log = LogFactory.getLog(FrontMyFinanceAction.class);
	private UserService userService;
	private AdminService adminService;
	private SelectedService selectedService;
	private SendMessageService sendMessageService;
	private FundManagementService fundManagementService;

	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	/**
	 * 充值扣费
	 */

	private long userId;
	/**
	 * 财务管理 充值提现审核 全部提现状态
	 */
	private List<Map<String, Object>> operateType;
	private List<Map<String, Object>> status;
	private List<Map<String, Object>> results;
	/**
	 * 财务管理 充值记录管理
	 */
	private List<Map<String, Object>> rechargeTypes;

	public String queryRechargeRecordInit() {
		return SUCCESS;
	}

	/**
	 * 财务管理 充值记录查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryRechargeRecordList() throws Exception {

		String userName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("userName"), null));
		String startTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("startTime"),
				null));
		String endTime = SqlInfusionHelper.filteSqlInfusion(FrontMyPaymentAction.changeEndTime(Convert.strToStr(
				paramMap.get("endTime"), null)));
		int rechargeType = Convert.strToInt(paramMap.get("rechargeType"), -100);
		/*String reEndTime = SqlInfusion.FilteSqlInfusion(FrontMyPaymentAction.changeEndTime(Convert.strToStr(
				paramMap.get("rechargeTime"), null)));*/
		Integer result = paramMap.get("status") == null ? -100 : Convert
				.strToInt(paramMap.get("status"), -100);
		try {

			Map<String, String> map = fundManagementService.queryRechargeRecordList(pageBean, userName,
					startTime, endTime, rechargeType, result);


			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
			request().setAttribute("totalNum", pageBean.getTotalNum());
			request().setAttribute("map", map);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 导出充值记录查询
	 * 
	 * @return
	 */
	public String exportRechargeRecord() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			String userName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request().getParameter(
					"userName"), ""));
			userName = URLDecoder.decode(userName, "UTF-8");
			String rechargeTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request().getParameter(
					"rechargeTime"), null));
			String reEndTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request().getParameter(
			"reEndTime"), null));
			if (StringUtils.isBlank(reEndTime)&&StringUtils.isNotBlank(rechargeTime)) {
				reEndTime = FrontMyPaymentAction.changeEndTime(rechargeTime);
			}
			int rechargeType = Convert.strToInt(request().getParameter("rechargeType"),
					-100);
			int statss = Convert.strToInt(request().getParameter("statss"), -1);
			// 待还款详情
			fundManagementService.queryRechargeRecordList(pageBean, userName,
					rechargeTime, reEndTime, rechargeType, statss);
			if (pageBean.getPage() == null) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (pageBean.getPage().size() > IConstants.EXCEL_MAX) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}
			fundManagementService.changeFigure(pageBean);
			HSSFWorkbook wb = ExcelHelper.exportExcel("充值记录",
					pageBean.getPage(), new String[] { "用户名", "充值类型", "充值金额",
							"费率", "到账金额", "充值时间", "状态" }, new String[] {
							"username", "rechargeType", "rechargeMoney",
							"cost", "realMoney", "rechargeTime", "result" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog(
					"v_t_user_rechargedetails_list", admin.getUserName(),
					IConstants.EXCEL, admin.getLastIP(), 0, "导出充值记录列表", 2);
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
	 * 财务管理 充值记录 第一次充值查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryRechargeFirstList() throws Exception {

		String userName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("userName"), null));
		String reStartTime = SqlInfusionHelper.filteSqlInfusion(Convert
				.strToStr(paramMap.get("reStartTime"), null));
		int rechargeType = Convert.strToInt(paramMap.get("rechargeType"), -100);
		String reEndTime = SqlInfusionHelper.filteSqlInfusion(FrontMyPaymentAction.changeEndTime(Convert.strToStr(
				paramMap.get("reEndTime"), null)));
		try {

			fundManagementService.queryRechargeFirstList(pageBean, userName,
					reStartTime, reEndTime, rechargeType);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 导出充值记录 第一次充值查询
	 * 
	 * @return
	 */
	public String exportRechargeFirst() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			String userName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request().getParameter(
					"userName"), null));
			if (StringUtils.isNotBlank(userName)) {
				userName = URLDecoder.decode(userName, "UTF-8");
			}
			String reStartTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request().getParameter(
					"reStartTime"), null));
			int rechargeType = Convert.strToInt(paramMap.get("rechargeType"),
					-100);
			String reEndTime = SqlInfusionHelper.filteSqlInfusion(FrontMyPaymentAction.changeEndTime(Convert
					.strToStr(request().getParameter("reEndTime"), null)));

			// 待还款详情
			fundManagementService.queryRechargeFirstList(pageBean, userName,
					reStartTime, reEndTime, rechargeType);
			if (pageBean.getPage() == null) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (pageBean.getPage().size() > IConstants.EXCEL_MAX) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}
			fundManagementService.changeFigure(pageBean);
			HSSFWorkbook wb = ExcelHelper.exportExcel("充值记录",
					pageBean.getPage(), new String[] { "用户名", "充值类型", "充值金额",
							"费率", "到账金额", "充值时间", "状态" }, new String[] {
							"username", "rechargeType", "rechargeMoney",
							"cost", "realMoney", "rechargeTime", "result" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("v_t_user_rechargefirst_lists",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出充值记录第一次充值查询", 2);
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
	 * 查看第一次充值数据
	 * 
	 * @throws Exception
	 */
	public String queryOneFirstChargeDetails() throws Exception {
		Long rechargeId = request("rechargeId") == null ? -100 : Convert
				.strToLong(request("rechargeId"), -100);
		try {
			paramMap = fundManagementService.queryOneFirstChargeDetails(
					rechargeId, false);
			if (paramMap != null) {
				String resultId = paramMap.get("result")+"";
				if (resultId.equals(0 + "")) {// 失败
					paramMap.put("realMoney", "0.00");
				}
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 财务管理 充值提现审核 全部提现
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryAllWithdrawList() throws Exception {

		String userName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("userName"), null));
		String startTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("startTime"), null));
		String endTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("endTime"), null));
		endTime = FrontMyPaymentAction.changeEndTime(endTime);

		Integer status = paramMap.get("status") == null ? -100 : Convert
				.strToInt(paramMap.get("status"), -100);

		try {
			Map<String, String> map = fundManagementService.queryAllWithdrawList(pageBean, userName,
					startTime, endTime, status);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
			request().setAttribute("map", map);
			request().setAttribute("totalNum", pageBean.getTotalNum());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 提现 --- 导出
	 * 
	 * @return
	 */
	public String exportAllWithdraw() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		Integer status = Convert.strToInt(request().getParameter("statss"), -1);
		String exporName = "";
		try {
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			String userName = SqlInfusionHelper.filteSqlInfusion(request().getParameter("userName") == null ? ""
					: request().getParameter("userName"));
			String startTime = SqlInfusionHelper.filteSqlInfusion(request().getParameter("startTime") == null ? ""
					: request().getParameter("startTime"));
			String endTime = SqlInfusionHelper.filteSqlInfusion(request().getParameter("endTime") == null ? ""
					: request().getParameter("endTime"));
			userName = URLDecoder.decode(userName, "UTF-8");// 中文乱码转换
			startTime = URLDecoder.decode(startTime, "UTF-8");
			endTime = URLDecoder.decode(endTime, "UTF-8");
			if (StringUtils.isNotBlank(endTime)) {
				endTime = FrontMyPaymentAction.changeEndTime(endTime);
			}
			// 提现详情
			fundManagementService.queryAllWithdrawList(pageBean, userName,
					startTime, endTime, status);
			if (pageBean.getPage() == null) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (pageBean.getPage().size() > IConstants.EXCEL_MAX) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}

			if (status == 0) {
				exporName = "全部提现列表";
			} else if (status == 1) {
				exporName = "待审核提现列表";
			} else if (status == 4) {
				exporName = "转账中的提现列表";
			} else if (status == 2) {
				exporName = "成功的提现列表";
			} else if (status == 5) {
				exporName = "失败的提现列表";
			}
			HSSFWorkbook wb = ExcelHelper.exportExcel(exporName, pageBean
					.getPage(), new String[] { "用户名", "真实姓名", "提现账号", "提现银行",
					"支行", "提现总额", "到账金额(￥)", "手续费", "提现时间", "状态" },
					new String[] { "name", "realName", "acount", "bankName",
							"branchBankName", "sum", "realMoney", "poundage",
							"applyTime", "status", });
			this.export(wb, new Date().getTime() + ".xls");
			// 系统操作日志
			operationLogService.addOperationLog("v_t_user_withdraw_lists",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出" + exporName, 2);
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
	 * 财务管理 充值记录
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserCashList() throws Exception {
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? null : Convert
				.strToStr(paramMap.get("userName"), null));
		try {
			Map<String, String> map = fundManagementService.queryUserCashList(pageBean, userName);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
			request().setAttribute("map", map);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 财务管理 充值管理 充值/扣费
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */

	public String queryBackCashInit() {
		String userId = request("userId");
		if (userId != null)
			paramMap.put("userId", userId);

		return SUCCESS;
	}

	public String queryBackCashList() throws Exception {
		String userId = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userId"));
		Long uid = -100L;
		if (userId != null) {
			uid = Convert.strToLong(userId, -100);
		}
		String checkUser = SqlInfusionHelper.filteSqlInfusion(paramMap.get("checkUser") == null ? null : Convert
				.strToStr(paramMap.get("checkUser"), null));// 操作人员
		String startTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("startTime") == null ? null : Convert
				.strToStr(paramMap.get("startTime"), null));
		String endTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("endTime") == null ? null : Convert
				.strToStr(paramMap.get("endTime"), null));
		endTime = FrontMyPaymentAction.changeEndTime(endTime);
		Integer type = paramMap.get("type") == null ? -100 : Convert.strToInt(
				paramMap.get("type"), -100);// 操作类型
		String uname = SqlInfusionHelper.filteSqlInfusion(paramMap.get("uname") == null ? null : Convert.strToStr(
				paramMap.get("uname"), null));// 用户名

		try {
			fundManagementService.queryBackCashList(pageBean, uid, checkUser,
					startTime, endTime, type, uname);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
			request().setAttribute("totalNum", pageBean.getTotalNum());
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 导出充值明细
	 * 
	 * @return
	 */
	public String exportBackCash() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		String userId = SqlInfusionHelper.filteSqlInfusion(request("userId"));
		Long uid = -100L;
		if (userId != null) {
			uid = Convert.strToLong(userId, -100);
		}

		try {
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			String checkUser = SqlInfusionHelper.filteSqlInfusion(request().getParameter("checkUser") == null ? ""
					: request().getParameter("checkUser"));
			String startTime = SqlInfusionHelper.filteSqlInfusion(request().getParameter("startTime") == null ? ""
					: request().getParameter("startTime"));
			String endTime = SqlInfusionHelper.filteSqlInfusion(request().getParameter("endTime") == null ? ""
					: request().getParameter("endTime"));
			if (StringUtils.isNotBlank(endTime)) {
				endTime = FrontMyPaymentAction.changeEndTime(endTime);
			}
			Integer type = Convert.strToInt(request().getParameter("type"),
					-100);
			String uname = SqlInfusionHelper.filteSqlInfusion(request().getParameter("userName") == null ? ""
					: request().getParameter("userName"));
			// 中文乱码转换
			checkUser = URLDecoder.decode(checkUser, "UTF-8");
			uname = URLDecoder.decode(uname, "UTF-8");
			// 待还款详情
			fundManagementService.queryBackCashList(pageBean, uid, checkUser,
					startTime, endTime, type, uname);
			if (pageBean.getPage() == null) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (pageBean.getPage().size() > IConstants.EXCEL_MAX) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}
			fundManagementService.changeFigure2(pageBean);
			HSSFWorkbook wb = ExcelHelper.exportExcel("充值明细",
					pageBean.getPage(), new String[] { "用户名", "真实姓名", "类型",
							"操作金额", "备注", "操作人员", "操作时间" }, new String[] {
							"uname", "realName", "type", "dealMoney", "remark",
							"userName", "checkTime" });
			this.export(wb, new Date().getTime() + ".xls");
			// 添加系统操作日志
			operationLogService.addOperationLog("v_t_user_backrw_lists", admin
					.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0,
					"导出充值明细", 2);
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

	public String queryR_WShow() throws Exception {
		Long rwId = request("rwId") == null ? -100 : Convert.strToLong(
				request("rwId"), -100);

		try {
			paramMap = fundManagementService.queryR_WInfo(rwId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String addBackRechargeInit() throws Exception {
		String userId = SqlInfusionHelper.filteSqlInfusion(request("userId"));

		try {
			if (userId != null) {
				Map<String, String> map = userService.queryUserById(Convert
						.strToLong(userId, -100));
				if (map != null) {
					paramMap.put("userName", map.get("username"));
					paramMap.put("userId", userId);
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String addBackWithdrawInit() throws Exception {
		String userId = SqlInfusionHelper.filteSqlInfusion(request("userId"));
		try {
			if (userId != null) {
				Map<String, String> map = userService.queryUserById(Convert
						.strToLong(userId, -100));
				if (map != null) {
					paramMap.put("userName", map.get("username"));// request().setAttribute("userName",
					// map.get("username"));//
					paramMap.put("userId", userId);
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加扣费
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addBackWithdraw() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		Long adminId = admin.getId();
		String userIdParam = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userId"));
		long userId = Convert.strToLong(userIdParam, -1);
		String dealMoneyParam = SqlInfusionHelper.filteSqlInfusion(paramMap.get("dealMoney"));
		double dealMoney = Convert.strToDouble(dealMoneyParam, 0);
		String remark = SqlInfusionHelper.filteSqlInfusion(paramMap.get("remark"));

		String _code = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("code"), null));
		String pageId = SqlInfusionHelper.filteSqlInfusion(paramMap.get("pageId")); // 验证码
		String code = SqlInfusionHelper.filteSqlInfusion((String) session().getAttribute(pageId + "_checkCode"));
		if (code == null || !_code.equals(code)) {
			return null;
		}
		if (dealMoney <= 0) {
			this.addFieldError("paramMap['dealMoney']", "操作金额错误");
			return "input";
		}
		try {
			Map<String, String> userMap = userService.queryUserById(userId);
			String addIP = SqlInfusionHelper.filteSqlInfusion(ServletHelper.getIpAddress(ServletActionContext.getRequest()));
			long result1 = fundManagementService.addBackW(userId, adminId,
					IConstants.WITHDRAW, dealMoney, remark, new Date(),
					IConstants.FUNDMODE_WITHDRAW_HANDLE, addIP, userMap
							.get("username"), "手动扣费，备注：" + remark);
			this.setUserId(userId);
			operationLogService.addOperationLog("t_recharge_withdraw_info",
					admin.getUserName(), IConstants.INSERT, admin.getLastIP(),
					dealMoney, " 进行充值扣费信息添加处理", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	public String addBackRecharge() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		Long adminId = admin.getId();
		Long userId = paramMap.get("userId") == null ? -100 : Convert
				.strToLong(paramMap.get("userId"), -100);
		Double dealMoney = paramMap.get("dealMoney") == null ? 0 : Convert
				.strToDouble(paramMap.get("dealMoney"), 0);
		String remark = SqlInfusionHelper.filteSqlInfusion(paramMap.get("remark") == null ? null : Convert
				.strToStr(paramMap.get("remark"), null));
		String type = SqlInfusionHelper.filteSqlInfusion(paramMap.get("recharType"));
		if (StringUtils.isBlank(type)) {
			this.addFieldError("paramMap['recharType']", "充值类型错误");
			return "input";
		}
		int recharType = Convert.strToInt(type, -1);
		if (dealMoney <= 0) {
			this.addFieldError("paramMap['dealMoney']", "操作金额错误");
			return "input";
		}
		try {

			Map<String, String> userMap = userService.queryUserById(userId);
			if(userMap == null){
				userMap = new HashMap<String, String>();
			}
			String addIP = SqlInfusionHelper.filteSqlInfusion(ServletHelper.getIpAddress(ServletActionContext.getRequest()));

			long result1 = fundManagementService.addBackR(userId, adminId,
					recharType, dealMoney, remark, new Date(),
					IConstants.FUNDMODE_RECHARGE_HANDLE, addIP, userMap
							.get("username"), "备注：" + remark);
			if (result1 < 0) {
				this.addFieldError("paramMap['dealMoney']", "操作失败");
				return "input";
			}
			operationLogService.addOperationLog("t_recharge_withdraw_info",
					admin.getUserName(), IConstants.INSERT, admin.getLastIP(),
					dealMoney, "手动充值,对象" + userMap.get("username"), 2);
			this.setUserId(userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	public String updateWithdraws() throws SQLException, DataException,
			UnsupportedEncodingException {
		String wids = SqlInfusionHelper.filteSqlInfusion(request("wids"));
		Long adminId = Convert.strToLong(request("adminId"), -100);
		Integer oldStatus = request("oldStatus") == null ? -1 : Convert
				.strToInt(request("oldStatus"), -1);
		String[] allIds = wids.split(",");// 进行全选删除的时候获得多个id值
		if (allIds.length <= 0) {
			return INPUT;
		}

		String[] strs = null;
		double mm, sum;
		float poundage;
		Long wid = 0L, userId = -100L;
		long result = -1L;

		for (int i = 0, n = allIds.length; i < n; i++) {
			result = -1L;
			strs = allIds[i].split(";");
			wid = Convert.strToLong(strs[0], -1);
			mm = sum = Convert.strToDouble(strs[1], 0);
			poundage = Convert.strToFloat(strs[2], 0);
			userId = Convert.strToLong(strs[3], -1);
			if (poundage > 0) {// 有手续费的时候，操作金额为减掉手续费的值
				mm = sum - poundage;
			}
		}
		return SUCCESS;
	}

	public List<Map<String, Object>> getRechargeTypes() {
		if (rechargeTypes == null) {
			rechargeTypes = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
//			mp = new HashMap<String, Object>();
//			mp.put("typeId", 1);
//			mp.put("typeValue", "支付宝支付");
//			rechargeTypes.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 2);
			mp.put("typeValue", "环迅支付");
			rechargeTypes.add(mp);

//			mp = new HashMap<String, Object>();
//			mp.put("typeId", 3);
//			mp.put("typeValue", "国付宝");
//			rechargeTypes.add(mp);
//
//			mp = new HashMap<String, Object>();
//			mp.put("typeId", 4);
//			mp.put("typeValue", "线下充值");
//			rechargeTypes.add(mp);
//
//			mp = new HashMap<String, Object>();
//			mp.put("typeId", 5);
//			mp.put("typeValue", "手工充值");
//			rechargeTypes.add(mp);
//
//			mp = new HashMap<String, Object>();
//			mp.put("typeId", 6);
//			mp.put("typeValue", "虚拟充值");
//			rechargeTypes.add(mp);
//
//			mp = new HashMap<String, Object>();
//			mp.put("typeId", 7);
//			mp.put("typeValue", "奖励充值");
//			rechargeTypes.add(mp);

		}
		return rechargeTypes;
	}

	public void setRechargeTypes(List<Map<String, Object>> rechargeTypes) {
		this.rechargeTypes = rechargeTypes;
	}

	public List<Map<String, Object>> getResults() {
		if (results == null) {
			results = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			mp = new HashMap<String, Object>();
			mp.put("resultId", -100);
			mp.put("resultValue", "全部");
			results.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("resultId", 1);
			mp.put("resultValue", "成功");
			results.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("resultId", 0);
			mp.put("resultValue", "失败");
			results.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("resultId", 2);
			mp.put("resultValue", "审核中");
			results.add(mp);
		}
		return results;
	}

	public void setResults(List<Map<String, Object>> results) {
		this.results = results;
	}

	public List<Map<String, Object>> getStatus() {
		if (status == null) {
			status = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			mp = new HashMap<String, Object>();
			mp.put("statusId", 0);
			mp.put("statusValue", "全部");
			status.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("statusId", 1);
			mp.put("statusValue", "审核中");
			status.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("statusId", 2);
			mp.put("statusValue", "成功");
			status.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("statusId", 3);
			mp.put("statusValue", "取消");
			status.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("statusId", 4);
			mp.put("statusValue", "转账中");
			status.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("statusId", 5);
			mp.put("statusValue", "失败");
			status.add(mp);
		}
		return status;
	}

	public void setStatus(List<Map<String, Object>> status) {
		this.status = status;
	}

	public List<Map<String, Object>> getOperateType() {
		if (operateType == null) {
			operateType = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			mp = new HashMap<String, Object>();
			mp.put("typeId", 1);
			mp.put("tvalue", "手动充值");
			operateType.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 2);
			mp.put("tvalue", "手动扣费");
			operateType.add(mp);
		}
		return operateType;
	}

	public void setOperateType(List<Map<String, Object>> operateType) {
		this.operateType = operateType;
	}

	public String queryWithdrawInfo() throws Exception {
		Long wid = request("wid") == null ? -100 : Convert.strToLong(
				request("wid"), -100);

		try {
			getWithdrawInfo(wid, true);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String queryWithdrawTransInfo() throws Exception {
		Long wid = request("wid") == null ? -100 : Convert.strToLong(
				request("wid"), -100);

		try {
			getWithdrawInfo(wid, false);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	private void getWithdrawInfo(Long wid, boolean check) throws Exception {
		Map<String, String> b_map = fundManagementService.queryOneWithdraw(wid);
		request().setAttribute("wid", wid);

		String checkId = "", checkTime = "", remark = "";
		if (b_map != null) {
			request().setAttribute("realName", b_map.get("realName"));
			request().setAttribute("branchBankName",
					b_map.get("bankName") + " " + b_map.get("branchBankName"));
			request().setAttribute("cardNo", b_map.get("acount"));
			request().setAttribute("sum", b_map.get("sum"));
			request().setAttribute("realMoney", b_map.get("realMoney"));
			request().setAttribute("poundage", b_map.get("poundage"));
			request().setAttribute("applyTime", b_map.get("applyTime"));
			request().setAttribute("ipAddress", b_map.get("ipAddress"));
			request().setAttribute("userId", b_map.get("userId"));
			String status = SqlInfusionHelper.filteSqlInfusion(b_map.get("status"));
			if (status.equals(IConstants.WITHDRAW_CHECK + "")) {
				request().setAttribute("status", IConstants.WITHDRAW_CHECK_STR);
			} else if (status.equals(IConstants.WITHDRAW_SUCCESS + "")) {
				request().setAttribute("status",
						IConstants.WITHDRAW_SUCCESS_STR);
			} else if (status.equals(IConstants.WITHDRAW_TRANS + "")) {
				request().setAttribute("status", IConstants.WITHDRAW_TRANS_STR);
			} else if (status.equals(IConstants.WITHDRAW_FAIL + "")) {
				request().setAttribute("status", IConstants.WITHDRAW_FAIL_STR);
			} else if(status.equals(IConstants.WITHDRAW_CANCEL + "")){
				request().setAttribute("status", IConstants.WITHDRAW_CANCEL_STR);
			}
			userId = Convert.strToLong(b_map.get("userId"), -100);
			checkId = SqlInfusionHelper.filteSqlInfusion(b_map.get("checkId"));
			checkTime = SqlInfusionHelper.filteSqlInfusion(b_map.get("checkTime"));
			remark = SqlInfusionHelper.filteSqlInfusion(b_map.get("remark"));
		}
		String defaultValue = "0.00";
		Map<String, String> rw_map = fundManagementService.queryUserRWInfo(
				userId, IConstants.RECHARGE_SUCCESS,
				IConstants.WITHDRAW_SUCCESS);
		if (rw_map != null) {
			if (rw_map.get("r_total").equals(""))
				request().setAttribute("r_total", defaultValue);// 充值成功总额
			else
				request().setAttribute("r_total", rw_map.get("r_total"));// 充值成功总额

			if (rw_map.get("w_total").equals(""))
				request().setAttribute("w_total", defaultValue);// 提现成功总额
			else
				request().setAttribute("w_total", rw_map.get("w_total"));// 提现成功总额

			if (rw_map.get("real_Amount").equals(""))
				request().setAttribute("real_Amount", defaultValue);// 投标成功总额
			else
				request()
						.setAttribute("real_Amount", rw_map.get("real_Amount"));// 投标成功总额

			request().setAttribute("withdraw_max", IConstants.WITHDRAW_MAX);
		}

		Map<String, String> u_map = userService.queryUserById(userId);
		if (u_map != null) {
			request().setAttribute("username", u_map.get("username"));
			request().setAttribute("usableSum", u_map.get("usableSum"));
		}

		
		
		if (!check) {// 查看跟审核的时候，还要查询审核信息
			Map<String, String> ms = adminService.queryAdminById(Convert
					.strToLong(checkId, -100));
			String username = null;
			if(ms==null){
				username="";
			}else
				username = ms.get("userName");
			
			String rk = "审核人:" + SqlInfusionHelper.filteSqlInfusion(username) + ",   审核时间:" + checkTime
					+ ",   审核备注:" + SqlInfusionHelper.filteSqlInfusion(remark);
			request().setAttribute("rk", rk);
		}
	}

	/**
	 * 转账中的提现
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateWithdrawTransfer() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		String ipAddress = ServletHelper.getIpAddress(ServletActionContext.getRequest());
		JSONObject obj = new JSONObject();
		Long adminId = admin.getId();
		String statusType = "";
		Integer status = paramMap.get("status") == null ? -1 : Convert
				.strToInt(paramMap.get("status"), -1);
		Long wid = paramMap.get("wid") == null ? -1 : Convert.strToLong(
				paramMap.get("wid"), -1);
		Map<String, String> retMap = fundManagementService
				.updateWithdrawTransfer(wid, status, adminId, SqlInfusionHelper.filteSqlInfusion(ipAddress),"");
		long retVal = -1;
		if(retMap == null){
			retMap = new HashMap<String, String>();
		}

		retVal = Convert.strToLong(retMap.get("ret") + "", -1);
		session().removeAttribute("randomCode");
		if (retVal <= 0) {
			obj.put("msg", retMap.get("ret_desc"));
			JSONHelper.printObject(obj);
			return null;
		} else {
			if (status == 2) {
				statusType = "成功";
			} else {
				statusType = "失败";
			}
			operationLogService.addOperationLog("t_withdraw", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"审核转账中的提现,审核状态" + SqlInfusionHelper.filteSqlInfusion(statusType), 2);
			obj.put("msg", "1");
			JSONHelper.printObject(obj);
			return null;
		}
	}

	/**
	 * 转账中的提现
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateWithdrawCheck() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		Integer status = paramMap.get("status") == null ? -1 : Convert
				.strToInt(paramMap.get("status"), -1);
		Double money = paramMap.get("money") == null ? 0 : Convert.strToDouble(
				paramMap.get("money"), 0);
		float poundage = paramMap.get("poundage") == null ? 0 : Convert
				.strToFloat(paramMap.get("poundage"), 0);
		String remark = SqlInfusionHelper.filteSqlInfusion(paramMap.get("remark") == null ? "" : paramMap
				.get("remark"));
		Long wid = paramMap.get("wid") == null ? -1 : Convert.strToLong(
				paramMap.get("wid"), -1);
		Long adminId = admin.getId();
		Long userId = paramMap.get("userId") == null ? -1 : Convert.strToLong(
				paramMap.get("userId"), -1);
		String ipAddress = SqlInfusionHelper.filteSqlInfusion(ServletHelper.getIpAddress(ServletActionContext.getRequest()));

		if (money <= 0) {
			obj.put("msg", "到账金额格式错误");
			JSONHelper.printObject(obj);
			return null;
		}

		if (poundage < 0) {
			obj.put("msg", "手续费格式错误");
			JSONHelper.printObject(obj);
			return null;
		}
		Map<String, String> retMap = fundManagementService.updateWithdraw(wid,
				money, poundage, status, remark, adminId, userId, ipAddress);
		long retVal = -1;
		if(retMap == null){
			retMap = new HashMap<String, String>();
		}

		retVal = Convert.strToLong(retMap.get("ret") + "", -1);
		session().removeAttribute("randomCode");
		if (retVal <= 0) {
			obj.put("msg", retMap.get("ret_desc"));
			JSONHelper.printObject(obj);
			return null;
		} else {
			// 添加操作日志
			operationLogService.addOperationLog("t_withdraw", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), money,
					"提现审核", 2);
			obj.put("msg", "1");
			JSONHelper.printObject(obj);
			return null;
		}
	}

	/**
	 * 根据用户通知，发送站内信、邮件、手机短信提醒
	 * 
	 * @throws Exception
	 */
	private void sendMessage(String title, String content, Long userId,
			String mode, boolean flag) throws Exception {
		try {
			// String title = "资金变动提醒";
			// 查找通知类型的通知状态
			List<Map<String, Object>> lists = selectedService.queryNoticeMode(
					userId, mode);
			if (lists != null && lists.size() > 0) {

				// [通知方式(1 邮件 2 站内信 3 短信]
				if (lists.get(0).get("flag").toString().equals(
						String.valueOf(IConstants.NOTICE_ON))) {
					sendMessageService
							.emailSendTemplate(SqlInfusionHelper.filteSqlInfusion(title), SqlInfusionHelper.filteSqlInfusion(content), userId);
				}
				if (lists.get(1).get("flag").toString().equals(
						String.valueOf(IConstants.NOTICE_ON))) {
					sendMessageService.mailSend(title, content, userId);
				}
				if (lists.get(2).get("flag").toString().equals(
						String.valueOf(IConstants.NOTICE_ON))) {
					if (flag) {// 有余额可扣的清空下，才进行短信的发送
						String dateStr = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
						Map<String, String> u_map = userService
								.queryUserById(userId);
						String userName = "";
						if (u_map != null && u_map.size() > 0) {
							userName = u_map.get("username");
						}
						String newContent = "尊敬的" + userName + ":[" + dateStr
								+ "]" + content;
						@SuppressWarnings("unused")
//						Long result = sendMessageService.noteSend(SqlInfusionHelper.filteSqlInfusion(newContent),
//								userId);
						Long result = sendMessageService.noteSendUseXuanWu(SqlInfusionHelper.filteSqlInfusion(newContent),
								userId);
					}
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
	}

	public String queryBackCashDetailsInit() {
		return SUCCESS;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public SendMessageService getSendMessageService() {
		return sendMessageService;
	}

	public void setSendMessageService(SendMessageService sendMessageService) {
		this.sendMessageService = sendMessageService;
	}

	public OperationLogService getOperationLogService() {
		return operationLogService;
	}

	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}

}
