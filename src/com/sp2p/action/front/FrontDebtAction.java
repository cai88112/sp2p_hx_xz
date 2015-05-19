package com.sp2p.action.front;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.EncryptedPrivateKeyInfo;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.renren.api.client.services.UserService;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.security.Encrypt;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.AssignmentDebtService;
import com.sp2p.service.AuctionDebtService;
import com.sp2p.service.SelectedService;


/**
 * 债权转让
 */
public class FrontDebtAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontDebtAction.class);
	private static final long serialVersionUID = 1L;

	private AssignmentDebtService assignmentDebtService;

	private AuctionDebtService auctionDebtService;

	private SelectedService selectedService;

	/**
	 * 查询前台的债权转让
	 * 
	 * @return
	 */
	public String queryFrontAllDebt()  throws Exception{
		pageBean.setPageNum(request("curPage"));
		long debtSum = Convert.strToLong(request("debtSum"), -1);
		long auctionBasePrice = Convert.strToLong(request("auctionBasePrice"), -1);
		long auctionMode = Convert.strToLong(request("auctionMode"), -1);
		long isLate = Convert.strToLong(request("isLate"), -1);
		long publishDays = Convert.strToLong(request("publishDays"), -1);
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowTitle"));
		try {

			assignmentDebtService.queryAllDebt(borrowTitle,debtSum, auctionBasePrice,
					auctionMode, isLate, publishDays, "2,1,3", pageBean);
			List<Map<String, Object>> list = pageBean.getPage();
			Date nowDate = new Date();
			if (list != null) {
				for (Map<String, Object> map : list) {
					Date date = (Date) map.get("actionTime");
					String remainDays = DateHelper.remainDateToString(nowDate,
							date);
					map.put("remainDays", remainDays);
				}
			}
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		//将参数设置到paramMap
		setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 查询前台的债权转让
	 * 
	 * @return
	 */
	public String queryFrontSuccessDebt() throws Exception {
		pageBean.setPageNum(request("curPage"));
		long debtSum = Convert.strToLong(request("debtSum"), -1);
		long auctionBasePrice = Convert.strToLong(request("auctionBasePrice"), -1);
		long auctionMode = Convert.strToLong(request("auctionMode"), -1);
		long isLate = Convert.strToLong(request("isLate"), -1);
		long publishDays = Convert.strToLong(request("publishDays"), -1);
		String borrowTitle = request("borrowTitle");
		
		try {
			assignmentDebtService.queryAllDebt(borrowTitle,debtSum, auctionBasePrice,
					auctionMode, isLate, publishDays, "3", pageBean);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		//将参数设置到paramMap
		setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 查询债权债权详情
	 * 
	 * @return
	 */
	public String queryDebtDetail() throws Exception {
		long id = Convert.strToLong(request("id"), -1);
		try {

			Map<String, String> map = assignmentDebtService
					.getAssignmentDebt(id);
			if (map != null) {
				long viewCount = Convert.strToLong(map.get("viewCount"), 0);
				viewCount++;
				paramMap.putAll(map);
				long borrowId = Convert.strToLong(map.get("borrowId"), -1);
				long borrowerId = auctionDebtService.queryBorrowerByBorrowId(borrowId);
				paramMap.put("borrowerId", borrowerId+"");
				paramMap.put("viewCount", viewCount + "");
				map = new HashMap<String, String>();
				map.put("viewCount", viewCount + "");
				assignmentDebtService.updateAssignmentDebt(id,-1, map);
				String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
				long auctionDays = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap
						.get("auctionDays")), 0);
				if(StringUtils.isNotBlank(publishTime)){
					String remainDays = DateHelper.remainDateToString(new Date(),
							DateUtils.addDays(DateUtils.parseDate( publishTime, new String[]{"yyyy-MM-dd HH:mm:ss"}),
									(int) auctionDays));
					paramMap.put("remainDays", remainDays);
				}
				long debtId = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")), -1);
				paramMap.put("debtId", SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")));
				paramMap.putAll(auctionDebtService
						.queryAuctionMaxPriceAndCount(debtId));
				long alienatorId = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap
						.get("alienatorId")), -1);
				Map<String, String> userMap = auctionDebtService
						.getUserAddressById(alienatorId);
				request().setAttribute("userMap", userMap);
			}

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
	 * 
	 * 添加留言
	 * @throws DataException 
	 * 
	 */
	public String addDebtMSG() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String code = (String) session().getAttribute("msg_checkCode");
		String _code = SqlInfusionHelper.filteSqlInfusion(paramMap.get("code")) == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap.get("code"));
		if (!code.equals(_code)) {
			this.addFieldError("paramMap['code']", IConstants.CODE_FAULS);
			return "input";
		}
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")) == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		String msgContent = SqlInfusionHelper.filteSqlInfusion(paramMap.get("msg")) == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("msg"));
		long returnId = -1;
		returnId = assignmentDebtService.addDebtMsg(idLong, user.getId(),
				msgContent);
		if (returnId <= 0) {
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
		} else {
			// 添加成功返回值
			obj.put("msg", "1");
			JSONHelper.printObject(obj);
			return null;
		}
	}

	/**
	 * 留言初始化
	 * 
	 */
	public String debtMSGInit() throws Exception{
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")) == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		String pageNum = SqlInfusionHelper.filteSqlInfusion(paramMap.get("curPage"));
		if (StringUtils.isNotBlank(pageNum)) {
			pageBean.setPageNum(pageNum);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_6);
		if (idLong == -1) {
			return "404";
		}
		assignmentDebtService.queryDebtMSGBord(idLong, pageBean);
		request().setAttribute("id", id);
		return "success";
	}

	/**
	 * 竞拍初始化
	 * 
	 * @return
	 */
	public String auctingDebtInit() throws Exception {
		long userId = this.getUserId();
		try {
			paramMap.put("debtId", request("debtId"));
			Map<String, String> map = auctionDebtService.getUserById(userId);
			if (map != null) {
				paramMap.put("usableSum", map.get("usableSum"));
				paramMap.put("totalSum", String.format("%.2f", Convert
						.strToDouble(map.get("freezeSum"), 0.0)
						+ Convert.strToDouble(map.get("usableSum"), 0.0)));
			}
			Map<String,String> debtMap =  assignmentDebtService.getAssignmentDebt(Convert.strToLong(request("debtId"), -1));
			if(debtMap != null){
				paramMap.putAll(debtMap);
			}
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
	 * 参与竞拍
	 * 
	 * @return
	 */
	public String addAuctingDebt() throws Exception {
		long debtId = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap.get("debtId")), -1);
		long userId = this.getUserId();
		try {
			String pwd = SqlInfusionHelper.filteSqlInfusion(paramMap.get("pwd"));
			if ("1".equals(IConstants.ENABLED_PASS)){ //1为默认启用
				pwd =  Encrypt.MD5(pwd.trim());
			}else{
				pwd =  Encrypt.MD5(pwd.trim()+IConstants.PASS_KEY);
			}
			double auctionPrice = Convert.strToDouble(SqlInfusionHelper.filteSqlInfusion(paramMap.get("auctionPrice")), 0.0);
			String code = SqlInfusionHelper.filteSqlInfusion(paramMap.get("code"));
			String sessionCode = (String) session().getAttribute(
					"auction_checkCode");
			if (sessionCode == null || !sessionCode.equals(code)) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "-1"); // 验证码错误
				return null;
			}
			Map<String, String> debtMap = assignmentDebtService
					.getAssignmentDebt(debtId);
			Map<String, String> userMap = auctionDebtService
					.getUserById(userId);
			
			if (debtMap != null && userMap != null) {
				if (debtMap.get("alienatorId").equals(userId + "")) {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-2"); // 不能投自己转让的的债权
					return null;
				}
				long borrowId = Convert.strToLong(debtMap.get("borrowId"), -1);
			
				
			
				if (!pwd.equals(userMap.get("dealpwd"))) {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-3"); // 交易密码不对
					return null;
				}
				Map<String,String> aucctionMap = auctionDebtService.getAuctionDebt(debtId,userId);
				double oldAuctionPrice = 0.0;
				if(aucctionMap != null){
					oldAuctionPrice = Convert.strToDouble(aucctionMap.get("auctionPrice"),0.0);
				}
				
				double usableSum = Convert.strToDouble(userMap.get("usableSum"), 0.0);
				if (usableSum < (auctionPrice-oldAuctionPrice)) {
					// 可用余额不足
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-4");
					return null;
				}
				double debtSum = Convert.strToDouble(debtMap.get("debtSum"),
						0.0);
				if (debtSum < auctionPrice) {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-5");// 大于债权金额
					return null;
				}

				double auctionBasePrice = Convert.strToDouble(debtMap
						.get("auctionBasePrice"), 0.0);
				if (auctionBasePrice > auctionPrice) {
					// 小于最小竞拍金额
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-6");
					return null;
				}
				
				long actionTimes = auctionDebtService.queryAuctionUserTimes(debtId, userId);
				if (actionTimes >= 2) {
					// 该债权您只能竞拍两次
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-8");
					return null;
				}
				
				long borrowerId = auctionDebtService.queryBorrowerByBorrowId(borrowId);
				if(borrowerId==userId){
					// 借款者不能竞拍该债权
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-9");
					return null;
				}
				
				if(oldAuctionPrice >= auctionPrice){
					// 第二次竞拍金额应大于第一次竞拍金额
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-10");
					return null;
				}
				double auctionHighPrice = Convert.strToDouble(debtMap.get("auctionHighPrice"), -1);
				if(auctionHighPrice != -1 && auctionHighPrice > auctionPrice){
					// 竞拍金额要大于最高竞拍金额
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-11");
					return null;
				}
				if(!"2".equals(debtMap.get("debtStatus"))){
					//竞拍失败
					ServletHelper.returnStr(ServletActionContext.getResponse(), "-7");
					return null;
				}
				
				Map<String,String> pro_map = auctionDebtService.procedure_Debts(debtId, userId, auctionPrice, pwd, this.getBasePath());
				if(pro_map == null){
					pro_map = new HashMap<String, String>();
				}
				long result = -1;
				result =Convert.strToLong( pro_map.get("ret"),-1);
				if(result == 1){
					ServletHelper.returnStr(ServletActionContext.getResponse(), "1");//竞拍成功
				}else{
					//竞拍失败
					ServletHelper.returnStr(ServletActionContext.getResponse(), result + "");
				}
				
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

	/**
	 * 查询竞拍记录
	 * 
	 * @return
	 */
	public String queryAcutionRecordInfo() throws Exception {
		long id = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap.get("id")), -1);
		try {
			List<Map<String, Object>> list = auctionDebtService
					.queryAuctionDebtByDebtId(id);
			request().setAttribute("list", list);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public void setAssignmentDebtService(
			AssignmentDebtService assignmentDebtService) {
		this.assignmentDebtService = assignmentDebtService;
	}

	public void setAuctionDebtService(AuctionDebtService auctionDebtService) {
		this.auctionDebtService = auctionDebtService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

}
