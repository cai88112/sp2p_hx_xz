package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.action.front.FrontMyPaymentAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.admin.AdminService;
import com.sp2p.service.admin.FundManagementService;

@SuppressWarnings("serial")
public class UserBankManagerAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(LinksAction.class);
	private AdminService adminService;
	private List<Map<String,Object>> checkers;
	 private FundManagementService fundManagementService;
		public FundManagementService getFundManagementService() {
			return fundManagementService;
		}

		public void setFundManagementService(FundManagementService fundManagementService) {
			this.fundManagementService = fundManagementService;
		}
		
	public String queryUserBankInit() throws SQLException, DataException{
		String types = Convert.strToStr(request("types"), "-1");
		request().setAttribute("types", types);
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String queryUserBankList() throws Exception{
		String userName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")), null);
		String realName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("realName")), null);
		//
		String checkUser = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("checkUser")), null);
		String moStartTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("modifiedTimeStart")), null);
		String moEndTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("modifiedTimeEnd")), null);
		String checkStartTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("checkTimeStart")), null);
		String checkTimeEnd = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("checkTimeEnd")), null);
		
		moEndTime = FrontMyPaymentAction.changeEndTime(moEndTime);
		checkTimeEnd = FrontMyPaymentAction.changeEndTime(checkTimeEnd);
		
		Long checkUserId = -100L;
		if(checkUser!=null){
			checkUserId = Convert.strToLong(checkUser, -100L);
		}
		try {
			//加载银行卡信息
			fundManagementService.queryBankCardInfos(pageBean,userName,realName,checkUserId,moStartTime,moEndTime,
					checkStartTime,checkTimeEnd);
			fundManagementService.changeNumToName(pageBean);
			int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		return SUCCESS;
	}
	
	@SuppressWarnings("unchecked")
	public String queryUserModifiyBankList() throws Exception{
		String userName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")), null);
		String realName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("realName")), null);
		//username  需要转换成 id 去搜条件
		String checkUser = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("checkUser")), null);
		String cStartTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("commitTimeStart")), null);
		String cEndTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("commitTimeEnd")), null);
		int cardStatus = Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(paramMap.get("cardStatus")), -1);
		
		cEndTime = FrontMyPaymentAction.changeEndTime(cEndTime);
		
		Long checkUserId = -100L;
		if(checkUser!=null){
			checkUserId = Convert.strToLong(checkUser, -100L);
		}
		try {
			//加载银行卡信息
			fundManagementService.queryModifyBankCardInfos(pageBean,userName,realName,checkUserId,
					cStartTime,cEndTime,cardStatus);
			
			fundManagementService.changeNumToName(pageBean);
			int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		return SUCCESS;
	}
	
	
	public String queryOneBankCardInfo() throws Exception{
		Long bankId = request("bankId")==null?-1:Convert.strToLong(request("bankId"), -1);
		try {
			//加载银行卡信息
			paramMap = fundManagementService.queryOneBankCardInfo( bankId);
			if(paramMap != null && paramMap.size() > 0){
				if(paramMap.get("mobilePhone") == null ||
						paramMap.get("mobilePhone").trim().equals("")){
					paramMap.put("mobilePhone", paramMap.get("cellPhone"));
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
	 * 银行卡变更
	 * @return
	 * @throws Exception 
	 */
	public String queryModifyBankInfo() throws Exception{
		Long bankId = request("bankId")==null?-100L:Convert.strToLong(request("bankId"), -100);
		
		try {
			paramMap = fundManagementService.queryOneBankCardInfo( bankId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		request().setAttribute("bankId", bankId);
		return SUCCESS;
	}
	
	/**
	 * 银行卡审核
	 * @return
	 * @throws Exception 
	 */
	public String updateUserBankInfo() throws Exception{
		Long checkUserId = paramMap.get("userName")==null?
				null:Convert.strToLong(paramMap.get("userName"), -100);
		String remark =SqlInfusionHelper.filteSqlInfusion(paramMap.get("remark")==null?null:Convert.strToStr(paramMap.get("remark"), null));
		Integer check_result = paramMap.get("status")==null?-100:
			Convert.strToInt(paramMap.get("status"), -100);
		Long bankId = paramMap.get("bankId")==null?null:Convert.strToLong(paramMap.get("bankId"), -1);	
		Admin  admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		try{

		 Long result = fundManagementService.updateBankInfo(checkUserId, bankId, remark, check_result,admin.getUserName(),admin.getLastIP());
			if(result < 0){
				ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
				return null;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		
		return SUCCESS;
	}
	
	/**
	 * 银行卡变更审核
	 * @return
	 * @throws Exception 
	 */
	public String updateUserModifyBank() throws Exception{
		Long checkUserId = paramMap.get("userName")==null?
				null:Convert.strToLong(paramMap.get("userName"), -100);
		String remark =SqlInfusionHelper.filteSqlInfusion(paramMap.get("remark")==null?null:Convert.strToStr(paramMap.get("remark"), null));
		Integer check_result = paramMap.get("status")==null?-100:
			Convert.strToInt(paramMap.get("status"), -100);
		Long bankId = paramMap.get("bankId")==null?null:Convert.strToLong(paramMap.get("bankId"), -1);		
		try{
		 Admin admin  = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
			@SuppressWarnings("unused")
			Long result = -1L;
			if(check_result == 1){//审核成功
				result = fundManagementService.updateModifyBankInfo(checkUserId, 
						bankId, remark, check_result, paramMap.get("modifiedBankName"), 
						paramMap.get("modifiedBranchBankName"), paramMap.get("modifiedCardNo"),
						paramMap.get("modifiedTime"), true,paramMap.get("modifiedBankCode"),
						paramMap.get("modifiedProvinceCode"),
						paramMap.get("modifiedCityCode"),paramMap.get("modifiedCardType")); 
				//添加操作日志
				operationLogService.addOperationLog("t_bankcard", admin.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0, "银行卡变更审核成功", 2);
			}else{//审核失败
				result = fundManagementService.updateModifyBankInfo(checkUserId, 
						bankId, remark, check_result, paramMap.get("modifiedBankName"), 
						paramMap.get("modifiedBranchBankName"), paramMap.get("modifiedCardNo"),
						paramMap.get("modifiedTime"), false,paramMap.get("modifiedBankCode"),
						paramMap.get("modifiedProvinceCode"),
						paramMap.get("modifiedCityCode"),paramMap.get("modifiedCardType"));
				//添加操作日志
				operationLogService.addOperationLog("t_bankcard", admin.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0, "银行卡变更审核失败", 2);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} 
		return SUCCESS;
	}
	
	public AdminService getAdminService() {
		return adminService;
	}

	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}

/*	public UserBankManagerService getUserBankService() {
		return userBankService;
	}

	public void setUserBankService(UserBankManagerService userBankService) {
		this.userBankService = userBankService;
	}*/

	public List<Map<String, Object>> getCheckers() throws Exception {
		if(checkers == null){
			//加载审核人员列表
			checkers = adminService.queryAdministors(IConstants.ADMIN_ENABLE);
		}
		return checkers;
	}

	public void setCheckers(List<Map<String, Object>> checkers) {
		this.checkers = checkers;
	}
	
}
