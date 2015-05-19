package com.sp2p.action.front;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.AssignmentDebtService;
import com.sp2p.service.AuctionDebtService;
import com.sp2p.service.SelectedService;


/**
 * 债权转让
 */
public class HomeDebtAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(HomeDebtAction.class);
	private static final long serialVersionUID = 1L;

	private AssignmentDebtService assignmentDebtService;

	private AuctionDebtService auctionDebtService;

	private SelectedService selectedService;

	private List<Map<String, Object>> auctionDaysList;

	/**
	 * 可以转让的债权
	 * 
	 * @return
	 */
	public String queryCanAssignmentDebt() throws Exception {
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(request("borrowTitle"));
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(request("borrowerName"));
		pageBean.setPageNum(SqlInfusionHelper.filteSqlInfusion(request("curPage")));	
		long userId = this.getUserId();
		try {
			assignmentDebtService.queryCanAssignmentDebt(userId, borrowTitle,
					borrowerName, pageBean);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		
		this.setRequestToParamMap();
		return SUCCESS;
	}

	
	/**
	 * 结束竞拍
	 * @return
	 */
	public String auctingDebtEnd() throws Exception{
		long debtId = Convert.strToLong(request("debtId"), -1);
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		try {
			assignmentDebtService.auctingDebtSuccess(debtId,user.getId(),1);
		} catch (SQLException e) {
		    log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 查询竞拍中的债权
	 * 
	 * @return
	 */
	public String queryAuctingDebt() throws Exception {
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(request("borrowTitle"));
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(request("borrowerName"));
		pageBean.setPageNum(request("curPage"));
	
		long userId = this.getUserId();
		try {
			assignmentDebtService.queryAuctingDebt(userId, borrowTitle,
					borrowerName, "2", pageBean);
			List<Map<String, Object>> list = pageBean.getPage();

			if (list != null) {
				Date nowDate = new Date();
				for (Map<String, Object> map : list) {
					Date date = (Date) map.get("remainAuctionTime");
					map.put("remainDays", DateHelper.remainDateToString(nowDate,
							date));
				}
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 查询竞拍结束的债权
	 * 
	 * @return
	 */
	public String queryAuctedDebt() throws Exception {
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(request("borrowTitle"));
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(request("borrowerName"));
		pageBean.setPageNum(request("curPage"));
		
		long userId = this.getUserId();
		try {
			assignmentDebtService.queryAuctingDebt(userId, borrowTitle,
					borrowerName, "3", pageBean);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 添加债权转让
	 * 
	 * @return
	 */
	public String addAssignmentDebt() throws Exception {
		long userId = this.getUserId();
		double auctionBasePrice = Convert.strToDouble(paramMap.get("auctionBasePrice"), -1);
		double debtSum = Convert.strToDouble(paramMap.get("debtSum"), -1);
		double lowerPrice = debtSum*0.5;
		
		if(auctionBasePrice < lowerPrice || auctionBasePrice > debtSum){
			NumberFormat  nf= new DecimalFormat("0.00");
			this.addFieldError("paramMap.auctionBasePrice", "竞拍底价范围为"+nf.format(lowerPrice)+"到"+debtSum+"元之间");
			return INPUT;
		}
	
		paramMap.put("alienatorId", userId + "");
		paramMap.put("applyTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		long reslut = -1;
		try {
			reslut = assignmentDebtService.addAssignmentDebt(paramMap);

		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		try {
			if (reslut != -1) {

				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");

			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "-1");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询失败的债权
	 * 
	 * @return
	 */
	public String queryAuctionFailDebt() throws Exception {
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(request("borrowTitle"));
		String borrowerName = SqlInfusionHelper.filteSqlInfusion(request("borrowerName"));
		pageBean.setPageNum(request("curPage"));

		long userId = this.getUserId();
		try {
			assignmentDebtService.queryAuctingDebt(userId, borrowTitle,
					borrowerName, "4,5,6,7", pageBean);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 取消申请债权转让
	 * 
	 * @return
	 */
	public String cancelApplyDebt() throws Exception {
		long debtId = Convert.strToLong(request("debtId"), -1);
		User   user = (User) session().getAttribute(IConstants.SESSION_USER);
		try {
			assignmentDebtService.cancelAssignmentDebt(debtId,5,user.getId(),1);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 参与竞拍的债权
	 * 
	 * @return
	 */
	public String queryBuyingDebt() throws Exception {
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(request("borrowTitle"));
		String startTime = SqlInfusionHelper.filteSqlInfusion(request("startTime"));
		String endTime = SqlInfusionHelper.filteSqlInfusion(request("endTime"));
		long userId = this.getUserId();
		pageBean.setPageNum(request("curPage"));
		
		try {
			auctionDebtService.queryAuctionDebt(borrowTitle, startTime,
					endTime, userId, "", pageBean);
			List<Map<String, Object>> list = pageBean.getPage();

			if (list != null) {
				Date nowDate = new Date();
				for (Map<String, Object> map : list) {
					Date date = (Date) map.get("remainAuctionTime");
					map.put("remainDays", DateHelper.remainDateToString(nowDate,
							date));
				}
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 成功竞拍的债权
	 * 
	 * @return
	 */
	public String querySucessBuyedDebt() throws Exception {
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(request("borrowTitle"));
		String startTime = SqlInfusionHelper.filteSqlInfusion(request("startTime"));
		String endTime = SqlInfusionHelper.filteSqlInfusion(request("endTime"));
		long userId = this.getUserId();
		pageBean.setPageNum(request("curPage"));
		try {
			auctionDebtService.querySuccessAuctionDebt(borrowTitle, startTime,
					endTime, userId, pageBean);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		
		this.setRequestToParamMap();
		return SUCCESS;
	}

	public void setAssignmentDebtService(
			AssignmentDebtService assignmentDebtService) {
		this.assignmentDebtService = assignmentDebtService;
	}

	public void setAuctionDebtService(AuctionDebtService auctionDebtService) {
		this.auctionDebtService = auctionDebtService;
	}

	public List<Map<String, Object>> getAuctionDaysList() throws Exception {
		auctionDaysList = selectedService.getDebtAuctionDays();
		return auctionDaysList;
	}

	public void setAuctionDaysList(List<Map<String, Object>> auctionDaysList) {
		this.auctionDaysList = auctionDaysList;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

}
