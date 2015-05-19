package com.sp2p.action.front;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Operator;
import com.sp2p.entity.User;
import com.sp2p.service.ExperiencemoneyService;
import com.sp2p.service.FinanceService;
import com.sp2p.service.MyHomeService;
import com.sp2p.service.SelectedService;
import com.sp2p.service.UserService;

/**
 * @ClassName: FrontMyHomeAction.java
 * @Author: gang.lv
 * @Date: 2013-3-13 下午10:21:30
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 我的主页控制层
 */
public class FrontMyHomeAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontMyHomeAction.class);
	private static final long serialVersionUID = 1L;

	private MyHomeService myHomeService;
	private SelectedService selectedService;
	private List<Map<String, Object>> borrowDeadlineList;
	private Map<String, String> automaticBidMap;
	private List<Operator> checkList;
	private UserService userService;
	private FinanceService financeService;
	private ExperiencemoneyService experiencemoneyService;

	/**
	 * @throws Exception
	 * @MethodName: homeInit
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午08:53:19
	 * @Return:
	 * @Descb: 我的主页初始化
	 * @Throws:
	 */
	public String homeInit() throws Exception {
		User user = (User) session().getAttribute("user");
		Map<String, String> homeMap = myHomeService.queryHomeInfo(user.getId());
		request().setAttribute("homeMap", homeMap);
		Map<String, String> accmountStatisMap = myHomeService
				.queryAccountStatisInfo(user.getId());
		request().setAttribute("accmountStatisMap", accmountStatisMap);
		Map<String, String> repayMap = myHomeService.queryRepaymentByOwner(user
				.getId());
		Map personMap = userService.queryPersonById(user.getId());
		String[] val = moneyVal(user.getId());
		Map<String, String> sumMap = new HashMap<String,String>();
		sumMap.put("SumSum", val[0]);//总金额
		sumMap.put("SumusableSum", val[1]);//可用金额
		sumMap.put("SumfreezSum", val[2]);//冻结金额
		sumMap.put("SumdueinSum", val[3]);//待收金额
		request().setAttribute("sumMap", sumMap);
		request().setAttribute("repayMap", repayMap);
		session().setAttribute("user_person", personMap);
		// 最新两条的活动标
		List<Map<String, Object>> activityBorrowList = financeService
				.queryLastestActivityBorrow(2);
		request().setAttribute("activityBorrowList", activityBorrowList);
		
		//体验金
		Map<String, String> experiencemoneyMap = experiencemoneyService.queryExperiencemoneyByUserID(user.getId());
		request().setAttribute("experiencemoneyMap", experiencemoneyMap);
		return "success";
	}

	public String[] moneyVal(long userId) throws Exception {
		String[] val = new String[4];
		for (int i = 0; i < val.length; i++) {
			val[i] = "0";
		}
		try {
			Map<String, String> map = userService.queryUserById(userId);
			if (map != null) {
				double usableSum = 0;
				double freezeSum = 0;
				double dueinSum = 0;
				
//				LinkedHashMap<String, Object> xmlMap = new LinkedHashMap<String, Object>();
//				String argIpsAccount = map.get("portMerBillNo");
//				xmlMap.put("argIpsAccount", argIpsAccount);
//				String strSign = IpsCrypto.md5Sign(IPayConfig.terraceNoOne + argIpsAccount + IPayConfig.cert_md5);
//				String soap = IPaymentUtil.getSoapAuditTender(argIpsAccount, IPaymentConstants.queryForAccBalance, "argMerCode", "argIpsAccount",  "argSign", strSign);
//				String url = IPayConfig.ipay_web_gateway + IPaymentConstants.queryForAccBalance;
//				Map<String, String> data = IPaymentUtil.webService(soap, url);
//				if (data != null && data.size() > 0) {
//					String pErrCode = data.get("pErrCode");
//					if ("0000".equals(pErrCode)) {
//						usableSum = Convert.strToDouble(data.get("pBalance"), 0);
//						freezeSum = Convert.strToDouble(data.get("pLock"), 0);
//						dueinSum = Convert.strToDouble(data.get("pNeedstl"), 0);
//					} else {
//						usableSum = Convert.strToDouble(map.get("usableSum"), 0);
//						freezeSum = Convert.strToDouble(map.get("freezeSum"), 0);
//						dueinSum = Convert.strToDouble(map.get("dueinSum"), 0);
//					}
//				} else {
					usableSum = Convert.strToDouble(map.get("usableSum"), 0);
					freezeSum = Convert.strToDouble(map.get("freezeSum"), 0);
					dueinSum = Convert.strToDouble(map.get("dueinSum"), 0);
//				}
				double SumSum = usableSum + freezeSum + dueinSum;
				
				val[0] = String.format("%.2f", SumSum);// 总金额
				val[1] = String.format("%.2f", usableSum);// 可用金额
				val[2] = String.format("%.2f", freezeSum);// 冻结金额
				val[3] = String.format("%.2f", dueinSum);// 待收金额
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return val;
	}
	
	/**
	 * @MethodName: homeBorrowPublishInit
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午08:53:29
	 * @Return:
	 * @Descb: 已经发布的借款初始化
	 * @Throws:
	 */
	public String homeBorrowPublishInit() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: loanStatisInit
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午02:57:19
	 * @Return:
	 * @Descb: 借款统计
	 * @Throws:
	 */
	public String loanStatisInit() throws Exception {
		User user = (User) session().getAttribute("user");
		Map<String, String> loanStatisMap = myHomeService.queryLoanStatis(user
				.getId());
		request().setAttribute("loanStatisMap", loanStatisMap);
		return "success";
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: financeStatisInit
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午02:57:31
	 * @Return:
	 * @Descb: 理财统计
	 * @Throws:
	 */
	public String financeStatisInit() throws SQLException, DataException {
		User user = (User) session().getAttribute("user");
		Map<String, String> financeStatisMap = myHomeService
				.queryFinanceStatis(user.getId());
		request().setAttribute("financeStatisMap", financeStatisMap);
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowBackAcount
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-4-2 上午09:12:22
	 * @Return:
	 * @Descb: 查询借款回账
	 * @Throws:
	 */
	public String homeBorrowBackAcount() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String title = paramMap.get("title") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("title"));
		String publishTimeStart = paramMap.get("publishTimeStart") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTimeStart"));
		if (StringUtils.isNotBlank(publishTimeStart)) {
			publishTimeStart = publishTimeStart + " 00:00:00";
		}
		String publishTimeEnd = paramMap.get("publishTimeEnd") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTimeEnd"));
		if (StringUtils.isNotBlank(publishTimeStart)) {
			publishTimeEnd = publishTimeEnd + " 23:59:59";
		}
		Map<String, String> map = myHomeService.queryBackAcountStatis(user
				.getId(), publishTimeStart, publishTimeEnd, title);
		if(map == null){
			map = new HashMap<String, String>();
		}
		String allForPIOneMonth = map.get("allForPIOneMonth") == null ? "0"
				: map.get("allForPIOneMonth");
		String allForPIThreeMonth = map.get("allForPIThreeMonth") == null ? "0"
				: map.get("allForPIThreeMonth");
		String allForPIYear = map.get("allForPIYear") == null ? "0" : map
				.get("allForPIYear");
		String allForPI = map.get("allForPI") == null ? "0" : map
				.get("allForPI");
		obj.put("allForPIOneMonth", allForPIOneMonth);
		obj.put("allForPIThreeMonth", allForPIThreeMonth);
		obj.put("allForPIYear", allForPIYear);
		obj.put("allForPI", allForPI);
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowInvestList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午08:40:13
	 * @Return:
	 * @Descb: 投资借款列表
	 * @Throws:
	 */
	public String homeBorrowInvestList() throws Exception {
		User user = (User) session().getAttribute("user");

		pageBean.setPageNum(request("curPage"));

		String type = "1";
		String borrowStatus = "";
		if ("1".equals(type)) {
			borrowStatus = IConstants.BORROW_STATUS_4 + ","
					+ IConstants.BORROW_STATUS_5;
		} else if ("2".equals(type)) {
			borrowStatus = "" + IConstants.BORROW_STATUS_2;
		}
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		String publishTimeStart = SqlInfusionHelper.filteSqlInfusion(request("publishTimeStart"));
		String publishTimeEnd = SqlInfusionHelper.filteSqlInfusion(request("publishTimeEnd"));
		myHomeService.queryBorrowInvestByCondition(title, publishTimeStart,
				publishTimeEnd, borrowStatus, user.getId(), pageBean);
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowInvestList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午08:40:13
	 * @Return:
	 * @Descb: 投资借款列表
	 * @Throws:
	 */
	public String homeBorrowTenderInList() throws Exception {
		User user = (User) session().getAttribute("user");

		pageBean.setPageNum(SqlInfusionHelper.filteSqlInfusion(request("curPage")));
		String borrowStatus = IConstants.BORROW_STATUS_2 + "";

		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		String publishTimeStart = SqlInfusionHelper.filteSqlInfusion(request("publishTimeStart"));
		String publishTimeEnd = SqlInfusionHelper.filteSqlInfusion(request("publishTimeEnd"));
		myHomeService.queryBorrowInvestByCondition(title, publishTimeStart,
				publishTimeEnd, borrowStatus, user.getId(), pageBean);
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowRecycleList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午08:41:47
	 * @Return:
	 * @Descb: 待回收借款列表
	 * @Throws:
	 */
	public String homeBorrowRecycleList() throws Exception {
		User user = (User) session().getAttribute("user");
		pageBean.setPageNum(request("curPage"));
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		myHomeService.queryBorrowRecycleByCondition(title, user.getId(),
				pageBean);
		this.setRequestToParamMap();
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowRecycledList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午01:40:27
	 * @Return:
	 * @Descb: 已回收的借款
	 * @Throws:
	 */
	public String homeBorrowRecycledList() throws Exception {
		User user = (User) session().getAttribute("user");
		pageBean.setPageNum(request("curPage"));
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		myHomeService.queryBorrowRecycledByCondition(title, user.getId(),
				pageBean);
		this.setRequestToParamMap();
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowForpayDetail
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午06:53:03
	 * @Return:
	 * @Descb: 查询投资人回收中借款还款详情
	 * @Throws:
	 */
	public String homeBorrowForpayDetail() throws Exception {
		User user = (User) session().getAttribute("user");
		String id = request().getParameter("id") == null ? "" : SqlInfusionHelper.filteSqlInfusion(request()
				.getParameter("id"));
		// add by houli
		String iid = request().getParameter("iid") == null ? "" : SqlInfusionHelper.filteSqlInfusion(request()
				.getParameter("iid"));
		long idLong = Convert.strToLong(id, -1);
		long iidLong = Convert.strToLong(iid, -1);
		List<Map<String, Object>> listMap = myHomeService
				.queryBorrowForpayById(idLong, user.getId(), iidLong);
		/*
		 * DecimalFormat df_two = new DecimalFormat("#0.00"); //-----add by
		 * houli 2013-04-25将数字进行格式化，保留两位小数 for(Map<String,Object> map : listMap
		 * ){
		 * 
		 * map.put("forpayPrincipal", df_two.format(
		 * map.get("forpayPrincipal"))); map.put("forpayInterest",
		 * df_two.format( map.get("forpayInterest")));
		 * map.put("principalBalance", df_two.format(
		 * map.get("principalBalance"))); map.put("manage", df_two.format(
		 * map.get("manage"))); map.put("earn", String.format("%.2f",
		 * map.get("earn"))); } //
		 */request().setAttribute("listMap", listMap);
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowHaspayDetail
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午06:57:20
	 * @Return:
	 * @Descb: 查询投资人已回收借款还款详情
	 * @Throws:
	 */
	public String homeBorrowHaspayDetail() throws Exception {
		User user = (User) session().getAttribute("user");
		String id = request().getParameter("id") == null ? "" : request()
				.getParameter("id");
		String iid = request().getParameter("iid") == null ? "" : request()
				.getParameter("iid");

		long idLong = Convert.strToLong(id, -1);
		long iidLong = Convert.strToLong(iid, -1);
		List<Map<String, Object>> listMap = myHomeService
				.queryBorrowHaspayById(idLong, user.getId(), iidLong);
		request().setAttribute("listMap", listMap);
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowBackAcountList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-26 下午08:43:24
	 * @Return:
	 * @Descb: 借款回账查询列表
	 * @Throws:
	 */
	public String homeBorrowBackAcountList() throws Exception {
		User user = (User) session().getAttribute("user");

		Map<String, String> backAcountStatisMap = myHomeService
				.queryBackAcountStatis(user.getId(), "", "", "");
		request().setAttribute("backAcountStatisMap", backAcountStatisMap);
		// 回账类型
		request().setAttribute("type", "5");

		pageBean.setPageNum(request("curPage"));
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		String publishTimeStart = SqlInfusionHelper.filteSqlInfusion(request("publishTimeStart"));
		String publishTimeEnd = SqlInfusionHelper.filteSqlInfusion(request("publishTimeEnd"));
		myHomeService.queryBorrowBackAcountByCondition(title, publishTimeStart,
				publishTimeEnd, user.getId(), pageBean);

		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowPublishList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午09:03:01
	 * @Return:
	 * @Descb: 审核中的借款
	 * @Throws:
	 */
	public String homeBorrowAuditList() throws Exception {
		String borrowStatus = IConstants.BORROW_STATUS_1 + ","
				+ IConstants.BORROW_STATUS_3;
		return queryBrrowPublishList(borrowStatus);
	}

	/**
	 * @throws Exception
	 * @MethodName: homeBorrowPublishList
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午09:03:01
	 * @Return:
	 * @Descb: 已经发布的借款列表
	 * @Throws:
	 */
	public String homeBorrowingList() throws Exception {
		String borrowStatus = "" + IConstants.BORROW_STATUS_2;
		return queryBrrowPublishList(borrowStatus);
	}

	private String queryBrrowPublishList(String borrowStatus) throws Exception {
		User user = (User) session().getAttribute("user");
		pageBean.setPageNum(SqlInfusionHelper.filteSqlInfusion(request("curPage")));
		pageBean.setPageSize(1);
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		String publishTimeStart = SqlInfusionHelper.filteSqlInfusion(request("publishTimeStart"));
		String publishTimeEnd = SqlInfusionHelper.filteSqlInfusion(request("publishTimeEnd"));
		myHomeService.queryBorrowFinishByCondition(title, publishTimeStart,
				publishTimeEnd, borrowStatus, user.getId(), pageBean);
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * @MethodName: automaticBidInit
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-28 下午03:09:53
	 * @Return:
	 * @Descb: 查询用户自动投标设置
	 * @Throws:
	 */
	public String automaticBidInit() throws SQLException, DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: automaticBidSet
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-28 下午04:33:53
	 * @Return:
	 * @Descb: 自动投标设置
	 * @Throws:
	 */
	public String automaticBidSet() throws Exception {
		String bidStatus = paramMap.get("s") == null ? "1" : SqlInfusionHelper.filteSqlInfusion(paramMap.get("s"));
		long bidStatusLong = Convert.strToLong(bidStatus, 1);
		JSONObject obj = new JSONObject();
		User user = (User) session().getAttribute("user");
		long returnId = -1;
		returnId = myHomeService.automaticBidSet(bidStatusLong, user.getId());

		if (returnId <= 0) {
			obj.put("msg", "未保存自动投标设置");
		} else {
			obj.put("msg", 1);
		}
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * @throws Exception
	 * @MethodName: automaticBidModify
	 * @Param: FrontMyHomeAction
	 * @Author: gang.lv
	 * @Date: 2013-3-28 下午05:04:24
	 * @Return:
	 * @Descb: 修改自动投标内容
	 * @Throws:
	 */
	public String automaticBidModify() throws Exception {
		JSONObject obj = new JSONObject();
		User user = (User) session().getAttribute("user");
		long returnId = -1;

		double usableSum = Convert.strToDouble(paramMap.get("usableSum"), 0);
		String bidAmount = paramMap.get("bidAmount") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("bidAmount"));
		String rateStart = paramMap.get("rateStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("rateStart"));
		String rateEnd = paramMap.get("rateEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("rateEnd"));
		String deadlineStart = paramMap.get("deadlineStart") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("deadlineStart"));
		String deadlineEnd = paramMap.get("deadlineEnd") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("deadlineEnd"));
		String creditStart = paramMap.get("creditStart") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("creditStart"));
		String creditEnd = paramMap.get("creditEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("creditEnd"));
		String remandAmount = paramMap.get("remandAmount") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("remandAmount"));
		String borrowWay = paramMap.get("borrowWay") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("borrowWay"));
		Double bidAmountDouble = Convert.strToDouble(bidAmount, 0);
		Double rateStartDouble = Convert.strToDouble(rateStart, 0);
		Double rateEndDouble = Convert.strToDouble(rateEnd, 0);
		Double deadlineStartDouble = Convert.strToDouble(deadlineStart, 0);
		Double deadlineEndDouble = Convert.strToDouble(deadlineEnd, 0);
		Double creditStartDouble = Convert.strToDouble(creditStart, 0);
		Double creditEndDouble = Convert.strToDouble(creditEnd, 0);
		Double remandAmountDouble = Convert.strToDouble(remandAmount, 0);

		if (StringUtils.isBlank(bidAmount)) {
			obj.put("msg", "每次投标金额不可为空");
			JSONHelper.printObject(obj);
			return null;
		} else if (bidAmountDouble == 0) {
			obj.put("msg", "每次投标金额格式错误");
			JSONHelper.printObject(obj);
			return null;
		} else if (bidAmountDouble < 50) {
			obj.put("msg", "每次投标金额不能低于50元");
			JSONHelper.printObject(obj);
			return null;
		}
		if (StringUtils.isBlank(rateStart)) {
			obj.put("msg", "利率范围开始不可为空");
			JSONHelper.printObject(obj);
			return null;
		} else if (rateStartDouble == 0) {
			obj.put("msg", "利率范围开始格式错误");
			JSONHelper.printObject(obj);
			return null;
		} else if (rateStartDouble < 0.1 || rateStartDouble > 24) {
			obj.put("msg", "利率范围0.1%~24%");
			JSONHelper.printObject(obj);
			return null;
		}
		if (StringUtils.isBlank(rateEnd)) {
			obj.put("msg", "利率范围结束不可为空");
			JSONHelper.printObject(obj);
			return null;
		} else if (rateEndDouble == 0) {
			obj.put("msg", "利率范围结束格式错误");
			JSONHelper.printObject(obj);
			return null;
		} else if (rateEndDouble < 0.1 || rateEndDouble > 24) {
			obj.put("msg", "利率范围0.1%~24%");
			JSONHelper.printObject(obj);
			return null;
		}
		if (StringUtils.isBlank(remandAmount)) {
			obj.put("msg", "账户保留金额不可为空");
			JSONHelper.printObject(obj);
			return null;
		} else if (remandAmountDouble == 0) {
			obj.put("msg", "账户保留金额格式错误");
			JSONHelper.printObject(obj);
			return null;
		}
		if (StringUtils.isBlank(borrowWay)) {
			obj.put("msg", "请勾选借款类型");
			JSONHelper.printObject(obj);
			return null;
		}
		if (bidAmountDouble > usableSum - remandAmountDouble) {
			obj.put("msg", "投标金额不能大于(可用余额 - 保底金额)");
			JSONHelper.printObject(obj);
			return null;
		}
		returnId = myHomeService.automaticBidModify(bidAmountDouble,
				rateStartDouble, rateEndDouble, deadlineStartDouble,
				deadlineEndDouble, creditStartDouble, creditEndDouble,
				remandAmountDouble, user.getId(), borrowWay);

		if (returnId <= 0) {
			obj.put("msg", IConstants.ACTION_FAILURE);
		} else {
			obj.put("msg", IConstants.ACTION_SUCCESS);
		}
		JSONHelper.printObject(obj);
		return null;

	}

	public MyHomeService getMyHomeService() {
		return myHomeService;
	}

	public void setMyHomeService(MyHomeService myHomeService) {
		this.myHomeService = myHomeService;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public List<Map<String, Object>> getBorrowDeadlineList() throws Exception {
		if (borrowDeadlineList == null) {
			// 借款期限列表
			borrowDeadlineList = selectedService.borrowDeadline();
		}
		return borrowDeadlineList;
	}

	public Map<String, String> getAutomaticBidMap() throws Exception {
		if (automaticBidMap == null) {
			// 自动投标设置
			User user = (User) session().getAttribute("user");
			automaticBidMap = myHomeService.queryAutomaticBid(user.getId());
			checkList = new ArrayList<Operator>();
			if (automaticBidMap != null) {
				// 设置ckBoxList的选中值
				String borrowWay = automaticBidMap.get("borrowWay");
				String[] ckList = borrowWay.split(",");
				if (ckList.length > 0) {
					for (String ck : ckList) {
						checkList.add(new Operator(ck, ""));
					}
				}
			}
		}
		return automaticBidMap;
	}

	public List<Operator> getCheckList() {
		return checkList;
	}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public ExperiencemoneyService getExperiencemoneyService() {
		return experiencemoneyService;
	}

	public void setExperiencemoneyService(
			ExperiencemoneyService experiencemoneyService) {
		this.experiencemoneyService = experiencemoneyService;
	}
	
}
