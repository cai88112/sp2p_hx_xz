package com.sp2p.action.admin;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.AssignmentDebtService;
import com.sp2p.service.FinanceService;


/**
 * 债权转让
 */
public class DebtManageAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(DebtManageAction.class);
	private static final long serialVersionUID = 1L;

	private AssignmentDebtService assignmentDebtService;
	private FinanceService financeService;

	/**
	 * 申请中的债权转让初始化
	 * @return
	 */
	public String queryApplyDebtInit(){
		return SUCCESS;
	}
	
	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	/**
	 * 申请中的债权转让
	 * @throws DataException 
	 */
	public String queryApplyDebtInfo() throws Exception{
		String debtStatus = "1";
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowerName"));
		String alienatorName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("alienatorName"));
		try {
			assignmentDebtService.queryApplyDebt(borrowerName,alienatorName,debtStatus,pageBean);
			Map<String, String> repaymentMap = assignmentDebtService.queryApplyDebtDetail();
	         request().setAttribute("repaymentMap",repaymentMap);
			//统计当前页应收款
			double debtSumm = 0;
			List<Map<String,Object>> payList = pageBean.getPage();
			if (payList!=null){
				for (Map<String, Object> map : payList) {
					debtSumm = debtSumm +Convert.strToDouble(map.get("debtSum")+"",0);
				}
			}
			DecimalFormat fmt=new DecimalFormat("0.00");
			request().setAttribute("debtSumm", fmt.format(debtSumm));
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}
	
	/**
	 * 债权转让审核
	 * @return
	 * @throws Exception 
	 */
	public String auditDebt() throws Exception {
		String auditStatus = SqlInfusionHelper.filteSqlInfusion(paramMap.get("auditStatus"));
		long id = Convert.strToLong(paramMap.get("id"), -1);
		Map<String,String> map = new HashMap<String, String>();
		map.put("publishTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		Admin  admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if("1".equals(auditStatus)){
			map.put("debtStatus", "2");
		}else{
			map.put("debtStatus", "6");
		}
		try {
			assignmentDebtService.updateAssignmentDebt(id,1, map);
			if("1".equals(auditStatus)){
				operationLogService.addOperationLog("t_assignment_debt", admin.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0, "债权转让审核成功", 2);
			}else{
				operationLogService.addOperationLog("t_assignment_debt", admin.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0, "债权转让审核失败", 2);
			}
			
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
		
	}
	
	/**
	 * 跳转到审核页面
	 * @return
	 */
	public String queryApplyDebtAuditDetail() throws Exception{
		long id = Convert.strToLong(request("id"), -1);
		try {
			paramMap.putAll(assignmentDebtService.getAssignmentDebt(id));
			Map<String ,String> borrow = new HashMap<String, String>();
			long borrowId = Convert.strToLong(paramMap.get("borrowId"), -1);
			long userId = Convert.strToLong(paramMap.get("alienatorId"), -1);
			long investId = Convert.strToLong(paramMap.get("investId"), -1);
			borrow = financeService.queryBorrowDetailById(borrowId);
			if(borrow != null && borrow.size() > 0){
				paramMap.put("borrowTitle", borrow.get("borrowTitle"));
			}
			List<Map<String,Object>> list = assignmentDebtService.queryDebtBacking(borrowId,userId,investId);
			request().setAttribute("list", list);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	/**
	 * 查询正在转让中的债权初始化
	 * @return
	 */
	public String queryAuctingAssignmentDebtInit(){
		return SUCCESS;
	}
	
	/**
	 * 查询正在转让中的债权
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String queryAuctingAssignmentDebtInfo() throws Exception{
		String debtStatus = "2";
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowerName"));
		String alienatorName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("alienatorName"));
		try {
			assignmentDebtService.queryAssignmentDebt(borrowerName,alienatorName,debtStatus,pageBean);
			assignmentDebtService.changeDateToStr(pageBean);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}
	
	/**
	 * 查询正在转让中的债权初始化
	 * @return
	 */
	public String querySuccessAssignmentDebtInit(){
		return SUCCESS;
	}
	
	/**
	 * 查询转让成功的债权
	 * @return
	 * @throws DataException 
	 */
	public String querySuccessAssignmentDebtInfo()  throws Exception{
		String debtStatus = "3";
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowerName"));
		String alienatorName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("alienatorName"));
		try {
			assignmentDebtService.queryAssignmentDebt(borrowerName,alienatorName,debtStatus,pageBean);
			Map<String, String> repaymentMap = assignmentDebtService.queryApplySuccessDebtDetail();
	         request().setAttribute("repaymentMap",repaymentMap);
			//统计当前页应收款
			double debtSumm = 0;
			List<Map<String,Object>> payList = pageBean.getPage();
			if (payList!=null){
				for (Map<String, Object> map : payList) {
					debtSumm = debtSumm +Convert.strToDouble(map.get("debtSum")+"",0);
				}
			}
			DecimalFormat fmt=new DecimalFormat("0.00");
			request().setAttribute("debtSumm", fmt.format(debtSumm));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		
		return SUCCESS;
	}
	
	/**
	 * 查询失败的债权初始化
	 * @return
	 */
	public String queryFailDebtInit(){
		return SUCCESS;
	}
	
	/**
	 * 查询失败的债权
	 * @return
	 * @throws DataException 
	 */
	public String queryFailDebtInfo()  throws Exception{
		String debtStatus = "4,5,6,7";
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowerName"));
		String alienatorName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("alienatorName"));
		try {
			assignmentDebtService.queryAssignmentDebt(borrowerName,alienatorName,debtStatus,pageBean);
			Map<String, String> repaymentMap = assignmentDebtService.queryApplyFailDebtDetail();
	         request().setAttribute("repaymentMap",repaymentMap);
			//统计当前页应收款
			double debtSumm = 0;
			List<Map<String,Object>> payList = pageBean.getPage();
			if (payList!=null){
				for (Map<String, Object> map : payList) {
					debtSumm = debtSumm +Convert.strToDouble(map.get("debtSum")+"",0);
				}
			}
			DecimalFormat fmt=new DecimalFormat("0.00");
			request().setAttribute("debtSumm", fmt.format(debtSumm));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}
	
	/**
	 * 结束竞拍
	 * @return
	 * @throws Exception 
	 */
	public String debtEndSuccess() throws Exception {
		long id = Convert.strToLong(request("id"), -1);
		Admin   admin =  (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		try {
			assignmentDebtService.auctingDebtSuccess(id,admin.getId(),2);  //区分前后台用户
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} 
		return SUCCESS;
       
	}
	
	/**
	 * 撤回债权转让
	 * @return
	 */
	public String cancelManageDebt() throws Exception{
		
		long id = Convert.strToLong(request("id"), -1);
		Admin  admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		try {
			assignmentDebtService.cancelAssignmentDebt(id, 5,admin.getId(),2);  //区分前后台用户
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
        
	}
	
	/**
	 * 债权转让详情
	 * @return
	 */
	public String queryManageDebtDetail() throws Exception{
		long id = Convert.strToLong(request("id"), -1);
		try {
			paramMap.putAll(assignmentDebtService.getAssignmentDebt(id));
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public void setAssignmentDebtService(
			AssignmentDebtService assignmentDebtService) {
		this.assignmentDebtService = assignmentDebtService;
	}

	
	public AssignmentDebtService getAssignmentDebtService() {
		return assignmentDebtService;
	}

}