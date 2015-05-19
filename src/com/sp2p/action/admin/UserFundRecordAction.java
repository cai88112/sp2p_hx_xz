package com.sp2p.action.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.action.front.FrontMyPaymentAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.OperationLogService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.AdminService;
import com.sp2p.service.admin.FundManagementService;

/**
 * 用户资金管理
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserFundRecordAction extends BaseFrontAction{

	public static Log log = LogFactory.getLog(LinksAction.class);
	private UserService userService;
	private List<Map<String,Object>> status;
	private List<Map<String,Object>> rechargeStatus;
	private List<Map<String,Object>> rechargeType;
	private AdminService adminService;
	private FundManagementService fundManagementService;
	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}
	/**
	 * 用户资金管理页面加载
	 * @return
	 */
	public String userFundInit(){
		return SUCCESS;
	}
	
	/**
	 * 查找用户资金列表信息
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String queryUserFundList() throws Exception{
		try {
			//加载银行卡信息
			String userName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")), null);
			Map<String, String> map = fundManagementService.queryUserCashList(pageBean, userName);
			request().setAttribute("map", map);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}
	
	public String userFundWithdrawInit(){
		String userId = SqlInfusionHelper.filteSqlInfusion(request("userId"));
		paramMap.put("userId", userId);
		return SUCCESS;
	}
	
	/**
	 * 查询提现记录
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public String queryUserFundWithdrawList() throws Exception{
		
		String userName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")), null);
		String startTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("startTime")), null);
		String endTime = FrontMyPaymentAction.changeEndTime(Convert.strToStr(
				paramMap.get("endTime"), null));
		Double sum = paramMap.get("sum")==null?-100: Convert.strToDouble(paramMap.get("sum"), -100);
		Integer status = paramMap.get("status")==null?-100:
			Convert.strToInt(paramMap.get("status"), -100);
		
		Long userId = Convert.strToLong(paramMap.get("userId"), -100);
		try{
			fundManagementService.queryUserFundWithdrawInfo(pageBean,userName,startTime,endTime,
					sum,status,userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String queryUserFundRechargeList() throws Exception{
		
		String startTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("startTime")), null);
		String endTime = FrontMyPaymentAction.changeEndTime(Convert.strToStr(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("endTime")), null));

		Integer status = paramMap.get("status")==null?-100:
			Convert.strToInt(paramMap.get("status"), -100);
		
		Integer rt = paramMap.get("rechargeType")==null?-100:
			Convert.strToInt(paramMap.get("rechargeType"), -100);
		
		Long userId = Convert.strToLong(paramMap.get("userId"), -100);

		try{
			fundManagementService.queryUserFundRechargeInfo(pageBean,startTime,endTime,
					status,userId,rt);
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String exportUserFundRecharge() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		Long userId = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(request("userId")),-1L);
		try {
			Admin admin = (Admin)session().getAttribute(IConstants.SESSION_ADMIN);
			String applyTime =Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("applyTime")), null);
			String endTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("endTime")), null);
			if(StringUtils.isBlank(endTime) &&StringUtils.isNotBlank(applyTime))
			{
				endTime = FrontMyPaymentAction.changeEndTime(applyTime);

			}
			Integer status = Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(request().getParameter("statss")), -100);
			Integer rt = Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(request().getParameter("reType")), -100);
			// 充值记录
			fundManagementService.queryUserFundRechargeInfo(pageBean,applyTime,endTime,
					status,userId,rt);
			if(pageBean.getPage()==null)
			{
				getOut().print("<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return  null;
			}
			if(pageBean.getPage().size()>IConstants.EXCEL_MAX)
			{
			getOut().print("<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
			return  null;
			}
			HSSFWorkbook wb = ExcelHelper.exportExcel("充值记录", pageBean
					.getPage(), new String[] { "用户名", "充值类型", "充值金额", "手续费",
					"到账金额", "充值时间", "状态" }, new String[] { "username",
					"type", "rechargeMoney", "poundage", "realMoney","rechargeTime","result"
					});
			operationLogService.addOperationLog("v_t_user_rechargeall_lists", admin.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0, "导出用户充值记录", 2);
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
 * 导出查询提现记录
 * 
 * @return
 */
	@SuppressWarnings("unchecked")
	public String exportUserFundWithdraw() {
	pageBean.pageNum = 1;
	pageBean.setPageSize(100000);
	Long userId = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(request("userId")),-1L);
	try {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		//String applyTime=request().getParameter("applyTime")==null ? "" :request().getParameter("applyTime");
		String applyTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("applyTime")), null);
		//String  endTime=request().getParameter("endTime")==null ?  "" :request().getParameter("applyTime");
		String endTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("endTime")), null);
		if(StringUtils.isNotBlank(applyTime)&&StringUtils.isBlank(endTime))
		{
		 endTime = FrontMyPaymentAction.changeEndTime(applyTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			endTime = FrontMyPaymentAction.changeEndTime(endTime);
		}
		//String userName=request().getParameter("userName")==null ? "" : request().getParameter("userName");
		String userName=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("userName")), null);
		if(StringUtils.isNotBlank(userName))
		{
			userName=URLDecoder.decode(userName, "UTF-8");
		}
		double sum=Convert.strToDouble(SqlInfusionHelper.filteSqlInfusion(request().getParameter("sum")), -1);
		int statss=Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(request().getParameter("statss")),-1);
		// 提现记录
		fundManagementService.queryUserFundWithdrawInfo(pageBean,userName,applyTime,endTime,
				sum,statss,userId);
		if(pageBean.getPage()==null)
		{
			getOut().print("<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
			return  null;
		}
		if(pageBean.getPage().size()>IConstants.EXCEL_MAX)
		{
		getOut().print("<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
		return  null;
		}
		HSSFWorkbook wb = ExcelHelper.exportExcel("提现记录", pageBean
				.getPage(), new String[] { "用户名", "真实姓名", "提现账号", "提现银行",
				"支行", "提现总额", "到账金额(￥)","手续费","提现时间" }, new String[] { "username",
				"realName", "acount", "bankName", "branchBankName","sum","realAccount","poundage","applyTime"
				});
		this.export(wb, new Date().getTime() + ".xls");
		
		operationLogService.addOperationLog("v_t_user_fundwithdraw_lists", admin.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0, "导出用户提现记录", 2);
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

	
	public String queryAllUserFundRecordInit(){
		return SUCCESS;
	}

	public String queryAllUserFundRecordList() throws Exception{
		try {
			String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName"));
			
			fundManagementService.queryAllUserFundRecordList(pageBean, userName);
			int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
			request().setAttribute("totalNum", pageBean.getTotalNum());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		return SUCCESS;
	}
	
	/**
	 * 导出资金明细
	 * @return
	 */
	public String exportAllUserFundList(){
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		Integer status = Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(request().getParameter("statss")), -1);
		try {
			Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
			String userName = SqlInfusionHelper.filteSqlInfusion(request().getParameter("userName") == null ? ""
					: request().getParameter("userName"));
			userName = URLDecoder.decode(userName, "UTF-8");// 中文乱码转换
			//资金明细
			fundManagementService.queryAllUserFundRecordList(pageBean, userName);
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
			fundManagementService.changeTraderName(pageBean);
			HSSFWorkbook wb = ExcelHelper.exportExcel("资金明细表", pageBean
					.getPage(), new String[] { "ID","用户名", "类型", "操作金额", "总金额",
					"可用金额", "冻结金额", "待收金额", "交易对方", "记录时间" },
					new String[] { "id", "username", "fundMode", "handleSum",
							"totalSum","usableSum", "freezeSum", "dueinSum", "traderName",
							"recordTime",});
			operationLogService.addOperationLog("v_t_user_fundrecord_lists", admin.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0, "导出资金明细列表", 2);
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
	 * 资金管理  资金记录
	 * @return
	 */
	public String userFundRecordInit(){
		String userName = SqlInfusionHelper.filteSqlInfusion(request("userName"));
		String userId = SqlInfusionHelper.filteSqlInfusion(request("userId"));
		paramMap.put("userName", userName);
		paramMap.put("userId", userId);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String queryUserFundRecordList() throws Exception{
		Long userId = paramMap.get("userId")==null?-100:
			Convert.strToLong(paramMap.get("userId"), -100);
		
		String userName = SqlInfusionHelper.filteSqlInfusion(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")));
		if(userName != null)
			request().setAttribute("userName", userName);
		try{
			fundManagementService.queryUserFundRecordList(pageBean,userId);
			int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		
		return SUCCESS;
	}
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<Map<String, Object>> getRechargeStatus() {
		if(rechargeStatus == null){//#{0:'全部',2:'成功',5:'失败',1:'充值中'}"
			rechargeStatus = new ArrayList<Map<String,Object>>();
			Map<String,Object> mp = null;
			mp = new HashMap<String,Object>();
			mp.put("statusId",-100 );
			mp.put("statusValue", "全部");
			rechargeStatus.add(mp);
			
			mp = new HashMap<String,Object>();
			mp.put("statusId",1 );
			mp.put("statusValue", "成功");
			rechargeStatus.add(mp);
			
			mp = new HashMap<String,Object>();
			mp.put("statusId",0 );
			mp.put("statusValue", "失败");
			rechargeStatus.add(mp);
			
		}
		return rechargeStatus;
	}

	public void setRechargeStatus(List<Map<String, Object>> rechargeStatus) {
		this.rechargeStatus = rechargeStatus;
	}

	public List<Map<String, Object>> getRechargeType() {
		if (rechargeType == null) {
			rechargeType = new ArrayList<Map<String, Object>>();
			Map<String, Object> mp = null;
			
			mp = new HashMap<String, Object>();
			mp.put("typeId", -100);
			mp.put("typeValue", "全部");
			rechargeType.add(mp);
			
			mp = new HashMap<String, Object>();
			mp.put("typeId", 1);
			mp.put("typeValue", "支付宝支付");
			rechargeType.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 2);
			mp.put("typeValue", "环迅支付");
			rechargeType.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 3);
			mp.put("typeValue", "国付宝");
			rechargeType.add(mp);
			
			mp = new HashMap<String, Object>();
			mp.put("typeId", 4);
			mp.put("typeValue", "线下充值");
			rechargeType.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 5);
			mp.put("typeValue", "手工充值");
			rechargeType.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 6);
			mp.put("typeValue", "虚拟充值");
			rechargeType.add(mp);

			mp = new HashMap<String, Object>();
			mp.put("typeId", 7);
			mp.put("typeValue", "奖励充值");
			rechargeType.add(mp);

		}
		return rechargeType;
	}

	public void setRechargeType(List<Map<String, Object>> rechargeType) {
		this.rechargeType = rechargeType;
	}

	public List<Map<String, Object>> getStatus() {
		if(status == null){//#{0:'全部',2:'成功',5:'失败',1:'充值中'}"
			status = new ArrayList<Map<String,Object>>();
			Map<String,Object> mp = null;
			mp = new HashMap<String,Object>();
			mp.put("statusId",0 );
			mp.put("statusValue", "全部");
			status.add(mp);
			
			mp = new HashMap<String,Object>();
			mp.put("statusId",2 );
			mp.put("statusValue", "成功");
			status.add(mp);
			
			mp = new HashMap<String,Object>();
			mp.put("statusId",5 );
			mp.put("statusValue", "失败");
			status.add(mp);
			
			mp = new HashMap<String,Object>();
			mp.put("statusId",4 );
			mp.put("statusValue", "充值中");
			status.add(mp);
		}
		return status;
	}

	public void setStatus(List<Map<String, Object>> status) {
		this.status = status;
	}

	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

	
}
