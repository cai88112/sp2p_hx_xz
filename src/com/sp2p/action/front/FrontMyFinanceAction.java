package com.sp2p.action.front;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.AmountHelper;
import com.fp2p.helper.DateHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.sp2p.service.BorrowService;
import com.sp2p.action.admin.UserAdminAction;
import com.sp2p.constants.IAmountConstants;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.entity.User;
import com.sp2p.service.FinanceService;
import com.sp2p.service.NewsAndMediaReportService;
import com.sp2p.service.PublicModelService;
import com.sp2p.service.SelectedService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.ShoveBorrowTypeService;

/**
 * 我的理财控制层
 * @ClassName: FrontMyFinanceAction.java
 * @Author: 殷梓淞.
 * @Date: 2014-12-19 上午10:26:33
 * @Version: V1.0.3
 * @Descrb: 我的理财控制层
 */
public class FrontMyFinanceAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontMyFinanceAction.class);
	private static final long serialVersionUID = 1L;

	private FinanceService financeService;
	private SelectedService selectedService;
	private Map<String, String> investDetailMap;
	private NewsAndMediaReportService newsService;
	// -add by C_J -- 标种类型 历史记录
	private ShoveBorrowTypeService shoveBorrowTypeService;
	private PublicModelService publicModelService;

	private UserService userService;
	// --
	// -add by houli
	private BorrowService borrowService;
	private BorrowManageService borrowManageService;
	// --
	private List<Map<String, Object>> borrowMSGMap;

	private List<Map<String, Object>> borrowPurposeList;
	private List<Map<String, Object>> borrowDeadlineList;
	private List<Map<String, Object>> borrowAmountList;
	private List<Map<String, Object>> linksList;
	private List<Map<String, Object>> meikuList;
	private List<Map<String, Object>> meikuStick;
	private List<Map<String, Object>> listsGGList;
	private List<Map<String, Object>> bannerList;

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: financeInit
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-4 上午11:16:54
	 * @Return: String
	 * @Descb: 我的理财初始化(此方法不用了)
	 * @Throws:
	 */
	@Deprecated
	public String financeInit() throws SQLException, DataException {
		String mode = request().getParameter("m") == null ? "1" : SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("m"));
		request().setAttribute("m", mode);
		String curPage = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"curPage"));
		if (StringUtils.isNotBlank(curPage)) {
			request().setAttribute("curPage", curPage);
		}

		// 初始化查询条件
		String title = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"title"));
		String paymentMode = SqlInfusionHelper.filteSqlInfusion(request()
				.getParameter("paymentMode"));
		String purpose = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"purpose"));
		String raiseTerm = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"raiseTerm"));
		String reward = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"reward"));
		String arStart = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"arStart"));
		String arEnd = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"arEnd"));
		String type = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"type"));
		request().setAttribute("title", title);
		request().setAttribute("paymentMode", paymentMode);
		request().setAttribute("purpose", purpose);
		request().setAttribute("raiseTerm", raiseTerm);
		request().setAttribute("reward", reward);
		request().setAttribute("arStart", arStart);
		request().setAttribute("arEnd", arEnd);
		request().setAttribute("type", type);

		// 获取页面上需要的动态下拉列表

		request().setAttribute("borrowPurposeList", borrowPurposeList);
		request().setAttribute("borrowDeadlineList", borrowDeadlineList);
		request().setAttribute("borrowAmountList", borrowAmountList);
		return "success";
	}

	/**
	 * @MethodName: financeList
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-4 下午08:44:15
	 * @Return:
	 * @Descb: 我的理财列表
	 * @Throws:
	 */
	public String financeList() throws Exception {
		// 前台显示列表类型
		String mode = SqlInfusionHelper.filteSqlInfusion(request("m"));
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		String paymentMode = SqlInfusionHelper
				.filteSqlInfusion(request("paymentMode"));
		String purpose = SqlInfusionHelper.filteSqlInfusion(request("purpose"));
		String deadline = SqlInfusionHelper.filteSqlInfusion(request("deadline"));
		String reward = SqlInfusionHelper.filteSqlInfusion(request("reward"));
		String arStart = SqlInfusionHelper.filteSqlInfusion(request("arStart"));
		String arEnd = SqlInfusionHelper.filteSqlInfusion(request("arEnd"));
		String type = SqlInfusionHelper.filteSqlInfusion(request("type"));

		pageBean.setPageNum(request("curPage"));

		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String borrowWay = "";
		String borrowStatus = "";
		String borrowType = "";
		// 截取查询的类型，防止非常规操作
		if (StringUtils.isNotBlank(type)) {
			String[] types = type.split(",");
			if (types.length > 0) {
				for (int n = 0; n < types.length; n++) {
					// 是数字类型则添加到borrowType中
					if (StringUtils.isNumericSpace(types[n])) {
						borrowType += "," + types[n];
					}
				}
				if (StringUtils.isNotBlank(borrowType)) {
					borrowType = borrowType.substring(1, borrowType.length());
				}
			} else {
				if (StringUtils.isNumericSpace(type)) {
					borrowType = type;
				}
			}
		}
		if ("1".equals(mode) || "".equals(mode)) {
			// 全部借款列表,显示1 等待资料 2 正在招标中 3 已满标
			borrowStatus = "(2,3,4,5)";
			// 查询条件中的借款方式
			if (StringUtils.isNotBlank(borrowType)) {
				borrowWay = "(" + borrowType + ")";
			}
		} else if ("2".equals(mode)) {
			// 实地认证的借款
			borrowWay = "(" + IConstants.BORROW_TYPE_FIELD_VISIT + ")";
		} else if ("3".equals(mode)) {
			// 信用认证的借款
			borrowWay = "(" + IConstants.BORROW_TYPE_GENERAL + ")";
		} else if ("4".equals(mode)) {
			// 机构担保的借款
			borrowWay = "(" + IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE
					+ ")";
		} else if ("5".equals(mode)) {
			// 最近成功的借款列表，显示4还款中 5 已还完
			borrowStatus = "(4,5)";
		} else {
			borrowStatus = "(2,3,4,5)";
		}
		financeService.queryBorrowByCondition(borrowStatus, borrowWay, title,
				paymentMode, purpose, deadline, reward, arStart, arEnd,
				IConstants.SORT_TYPE_DESC, pageBean);
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 我的理财列表
	 * @MethodName: financeList
	 * @Param: FrontMyFinanceAction
	 * @Author: 殷梓淞
	 * @Date: 2014-12-18 下午08:44:15
	 * @Return:
	 * @Descb: 我的理财列表
	 * @Throws:
	 */
		public String financeConditionList() throws Exception {
			//条件  前台显示列表类型
			//种类
			String subjectMatterString = request().getParameter("subjectMatter") == null ? "" : SqlInfusionHelper
					.filteSqlInfusion(request().getParameter("subjectMatter"));
			
			//金额范围
			String borrowAmountString = request().getParameter("borrowAmount") == null ? "" : SqlInfusionHelper
					.filteSqlInfusion(request().getParameter("borrowAmount"));
			//利率范围
			String yearRateString = request().getParameter("yearRate") == null ? "" : SqlInfusionHelper
					.filteSqlInfusion(request().getParameter("yearRate"));
			//期限范围
			String deadlineString= request().getParameter("deadlineString") == null ? "" : SqlInfusionHelper
					.filteSqlInfusion(request().getParameter("deadlineString")); 

			pageBean.setPageNum(request("curPage"));

			pageBean.setPageSize(IConstants.PAGE_SIZE_10);
			request().setCharacterEncoding("UTF-8");
			request().setAttribute("subjectMatterString", subjectMatterString);
			request().setAttribute("borrowAmountString", borrowAmountString);
			request().setAttribute("yearRateString", yearRateString);
			request().setAttribute("deadlineString", deadlineString);
			
			financeService.queryBorrowByCondition_New(subjectMatterString,borrowAmountString, yearRateString, deadlineString, pageBean);
			this.setRequestToParamMap();
			return SUCCESS;
		}
	
	/**
	 * @MethodName: financeLastestList
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 上午09:29:33
	 * @Return:
	 * @Descb: 最新借款列表前10条记录
	 * @Throws:
	 */
	public String financeLastestList() throws Exception {
		try {
			List<Map<String, Object>> mapList = financeService
					.queryLastestBorrow();
			request().setAttribute("mapList", mapList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * @MethodName: investRank
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 上午11:24:23
	 * @Return:
	 * @Descb: 投资排名前20条记录
	 * @Throws:
	 */
	public String investRank() {
		List<Map<String, Object>> rankList = new ArrayList<Map<String, Object>>();
		try {
			int number = Convert.strToInt(paramMap.get("number"), 1);
			// 当前年
			rankList = financeService.investRank(number, 8);
			request().setAttribute("rankList", rankList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 最新4条房标或者车标
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public String queryLastestCarOrHouseBorrow() throws DataException, Exception {
		String subject_matterStr = request().getParameter("subject_matter") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("subject_matter"));
		int subject_matter = Convert.strToInt(subject_matterStr, -1);
		// 富车贷
		List<Map<String, Object>> carOrHouseList = financeService
				.queryLastestCarOrHouseBorrow(4, subject_matter);
		request().setAttribute("carOrHouseList", carOrHouseList);
		return SUCCESS;
	}
	
	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: index
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午01:46:12
	 * @Return:
	 * @Descb: 首页加载内容
	 * @Throws:
	 */
	public String index() throws Exception {
		Map<String, String> totalRiskMap = financeService.queryTotalRisk();
		Map<String, String> currentRiskMap = financeService.queryCurrentRisk();
		request().setAttribute("totalRiskMap", totalRiskMap);
		request().setAttribute("currentRiskMap", currentRiskMap);
		// 最新借款列表
		List<Map<String, Object>> mapList = financeService.queryLastestBorrow();
		request().setAttribute("mapList", mapList);
		
        // 今天最新的体验标
		Map<String, String> mapNoviceist = financeService.queryLastestNoviceBorrow();
		request().setAttribute("mapNoviceist", mapNoviceist);
		// 最新两条的活动标
		List<Map<String, Object>> activityBorrowList = financeService
				.queryLastestActivityBorrow(2);
		request().setAttribute("activityBorrowList", activityBorrowList);
		
		//活动标抢光标志
		String activityStatus = "true";
		for (Map<String, Object> activitymap : activityBorrowList) {
			if( Convert.strToInt(String.valueOf(activitymap.get("borrowStatus")) , 0) ==2){
				activityStatus = "false";
			
				break;
			}
		}
		request().setAttribute("activityStatus", activityStatus);
		// 排名前8条记录
		// 全部
		List<Map<String, Object>> rankList = financeService.investRank(0, 8);
		request().setAttribute("rankList", rankList);
		// 公告
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		pageBean.setPageSize(5);
		newsService.frontQueryNewsPage(pageBean);
		newsList = pageBean.getPage();
		pageBean.setPage(null);
		request().setAttribute("newsList", newsList);
		
		List<Map<String,Object>> newsMediaList = new ArrayList<Map<String,Object>>();
		pageBean.setPageSize(1);
		newsService.frontQueryNewsMediaPage(pageBean);
		newsMediaList = pageBean.getPage();
		pageBean.setPage(null);
		if(newsMediaList != null){
			for (int i = 0; i < newsMediaList.size(); i++) {
				Map<String, Object> map = newsMediaList.get(i);
				Object content = newsMediaList.get(0).get("content");
				//log.info("内容：" + content +"结束content.lenth=" + content.toString().length());
				String content2 = "";
				if(content.toString().length() >190){
					content2 = content.toString().substring(0, 185) + "...";
				}else{
					content2 =content.toString();
				}
				//log.info("内容：" + content2);
				map.put("content", content2);
				newsMediaList.remove(i);
				newsMediaList.add(map);
			}
		}
		
		 
		request().setAttribute("newsMediaList", newsMediaList);
		
//		//成功故事
//		List<Map<String,Object>> storyList = new ArrayList<Map<String,Object>>();
//		pageBean.setPageSize(2);
	///    successStoryService.querySuccessStoryPage(pageBean);
		/*publicModelService.querySuccessStoryPage(pageBean);
	    storyList = pageBean.getPage();
		pageBean.setPage(null);*/
		/*request().setAttribute("storyList", storyList);*/
		//友情链接
		// //成功故事
		// List<Map<String,Object>> storyList = new
		// ArrayList<Map<String,Object>>();
		// pageBean.setPageSize(2);
		// / successStoryService.querySuccessStoryPage(pageBean);
		/*
		 * publicModelService.querySuccessStoryPage(pageBean); storyList =
		 * pageBean.getPage(); pageBean.setPage(null);
		 */
		/* request().setAttribute("storyList", storyList); */
		// 友情链接
		pageBean.setPageSize(100);
		// / linksService.queryLinksPage(pageBean);
		publicModelService.queryLinksPage(pageBean);
		linksList = pageBean.getPage();
		pageBean.setPage(null);
		request().setAttribute("linksList", linksList);
		// 媒体报道 取4条记录
		pageBean.setPageSize(4);
		// / mediaReportService.queryMediaReportPage(pageBean,1);
		newsService.queryMediaReportPage(pageBean, 1);

		meikuList = pageBean.getPage();
		pageBean.setPage(null);
		request().setAttribute("meikuList", meikuList);
		// 投资广告
		pageBean.setPageSize(3);
		// / linksService.queryLinksGGPage(pageBean);
		publicModelService.queryLinksGGPage(pageBean);
		listsGGList = pageBean.getPage();
		pageBean.setPage(null);
		request().setAttribute("listsGGList", listsGGList);
		// 图片滚动
		pageBean.setPageSize(3);
		// / linksService.queryBannerListPage(pageBean);
		publicModelService.queryBannerListPage(pageBean);
		bannerList = pageBean.getPage();
		pageBean.setPage(null);
		request().setAttribute("bannerList", bannerList);
		// 得到置顶的媒体报道
		pageBean.setPageSize(2);
		// / mediaReportService.queryMediaReportPage(pageBean,2);
		newsService.queryMediaReportPage(pageBean, 2);
		meikuStick = pageBean.getPage();
		pageBean.setPage(null);
		request().setAttribute("meikuStick", meikuStick);
		// 得到用户对象
		User user = (User) session().getAttribute("user");
		if (user != null) {
			paramMap = userService.queryUserById(user.getId());
		}

		return "success";
	}

	/*
	 * @MethodName: financeToolInit
	 * 
	 * @Param: FrontMyFinanceAction
	 * 
	 * @Author: gang.lv
	 * 
	 * @Date: 2013-3-4 下午01:30:25
	 * 
	 * @Return:理财工具箱
	 * 
	 * @Descb:
	 * 
	 * @Throws:
	 */
	public String financeToolInit() {
		return "success";
	}

	/**
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: financeDetail
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-5 下午03:40:38
	 * @Return:
	 * @Descb: 理财中的借款明细
	 * @Throws:
	 */
	public String financeDetail() throws Exception {
		//殷梓淞添加 
		User user = (User) session().getAttribute("user");
		if(user == null){
			return "nologin";
		}
		//殷梓淞添加  end
		
		session().setAttribute("DEMO", IConstants.ISDEMO);// 得到是不是演示版本

		String idStr = request().getParameter("id") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("id"));
		if (!"".equals(idStr) && StringUtils.isNumericSpace(idStr)) {
			Long id = Convert.strToLong(idStr, -1);
			// 借款详细
			Map<String, String> borrowDetailMap = financeService
					.queryBorrowDetailById(id);
			if (borrowDetailMap == null) {
				borrowDetailMap = new HashMap<String, String>();
			}
			// -- 7 - 9
			// 查询借款信息得到借款时插入的平台收费标准
			Map<String, String> map = borrowManageService.queryBorrowInfo(id);
			if (map == null) {
				return "404";
			}
			// 得到收费标准的json代码
			String feelog = Convert.strToStr(map.get("feelog"), "");
			Map<String, Double> feeMap = (Map<String, Double>) JSONObject
					.toBean(JSONObject.fromObject(feelog), HashMap.class);
			// --end
			String nid_log = borrowDetailMap.get("nid_log");
			Map<String, String> TypeLogMap = null;
			if (StringUtils.isNotBlank(nid_log)) {
				TypeLogMap = shoveBorrowTypeService
						.queryBorrowTypeLogByNid(nid_log.trim());
				int stauts = Convert.strToInt(
						TypeLogMap.get("subscribe_status"), -1);
				request().setAttribute("subscribes", stauts);
			}
			if (borrowDetailMap != null && borrowDetailMap.size() > 0) {
				double borrowSum = Convert.strToDouble(
						borrowDetailMap.get("borrowSum") + "", 0);
				double annualRate = Convert.strToDouble(
						borrowDetailMap.get("annualRate") + "", 0);
				int deadline = Convert.strToInt(borrowDetailMap.get("deadline")
						+ "", 0);
				int paymentMode = Convert.strToInt(
						borrowDetailMap.get("paymentMode") + "", -1);
				int isDayThe = Convert.strToInt(borrowDetailMap.get("isDayThe")
						+ "", 1);
				double investAmount = 10000;
				String earnAmount = "";
				if (borrowSum < investAmount) {
					investAmount = borrowSum;
				}
				// AmountUtil au = new AmountUtil();
				Map<String, String> earnMap = null;
				double costFee = Convert.strToDouble(
						feeMap.get(IAmountConstants.INVEST_FEE_RATE) + "", 0);
				if (paymentMode == 1) {
					// 按月等额还款
					earnMap = AmountHelper.earnCalculateMonth(investAmount,
							borrowSum, annualRate, deadline, 0, isDayThe, 2,
							costFee);
					earnAmount = earnMap.get("msg") + "";
				} else if (paymentMode == 2) {
					// 先息后本
					earnMap = AmountHelper.earnCalculateSum(investAmount,
							borrowSum, annualRate, deadline, 0, isDayThe, 2);
					earnAmount = earnMap.get("msg") + "";
				} else if (paymentMode == 3) {
					// 秒还
					earnMap = AmountHelper.earnSecondsSum(investAmount,
							borrowSum, annualRate, deadline, 0, 2);
					earnAmount = earnMap.get("msg") + "";
				} else if (paymentMode == 4) {
					// 一次性还款
					earnMap = AmountHelper.earnCalculateOne(investAmount,
							borrowSum, annualRate, deadline, 0, isDayThe, 2,
							costFee);
					earnAmount = earnMap.get("msg") + "";
				}
				// ----------add by houli 借款类型判断，前台借款详细信息中需要显示
				String borrowWay = borrowDetailMap.get("borrowWay");
				if (borrowWay.equals(IConstants.BORROW_TYPE_NET_VALUE)) {
					request().setAttribute("borrowWay",
							IConstants.BORROW_TYPE_NET_VALUE_STR);
				} else if (borrowWay.equals(IConstants.BORROW_TYPE_SECONDS)) {
					request().setAttribute("borrowWay",
							IConstants.BORROW_TYPE_SECONDS_STR);
				} else if (borrowWay.equals(IConstants.BORROW_TYPE_GENERAL)) {
					request().setAttribute("borrowWay",
							IConstants.BORROW_TYPE_GENERAL_STR);
				} else if (borrowWay.equals(IConstants.BORROW_TYPE_FIELD_VISIT)) {
					request().setAttribute("borrowWay",
							IConstants.BORROW_TYPE_FIELD_VISIT_STR);
				} else if (borrowWay
						.equals(IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
					request().setAttribute("borrowWay",
							IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
				} else if (borrowWay
						.equals(IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {// 流转标
					request().setAttribute("borrowWay",
							IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
				}
				// 催收记录
				List<Map<String, Object>> collection = financeService
						.queryCollectionByid(id);
				if (collection != null && collection.size() > 0)
					request().setAttribute("colSize", collection.size());

				request().setAttribute("earnAmount", earnAmount);
				// 每次点击借款详情时新增浏览量
				financeService.addBrowseCount(id);
				request().setAttribute("borrowDetailMap", borrowDetailMap);
				// 借款人资料
				Map<String, String> borrowUserMap = financeService
						.queryUserInfoById(id);
				request().setAttribute("borrowUserMap", borrowUserMap);
				// 借款人认证资料
				List<Map<String, Object>> list = financeService
						.queryUserIdentifiedByid(id);
				request().setAttribute("list", list);
				// 投资记录
				List<Map<String, Object>> investList = financeService
						.queryInvestByid(id);
				request().setAttribute("investList", investList);
				request().setAttribute("idStr", idStr);
				Map<String, String> borrowRecordMap = financeService
						.queryBorrowRecord(id);
				request().setAttribute("borrowRecordMap", borrowRecordMap);
				// -----------add by houli
				String wStatus = judgeStatus(Convert.strToInt(borrowWay, -1),
						Convert.strToLong(borrowDetailMap.get("publisher"),
								-100));
				if (wStatus == null) {
					request().setAttribute("wStatus", "");
				} else {
					request().setAttribute("wStatus", wStatus);
				}
				
				//殷梓淞添加 
//				User user = (User) session().getAttribute("user");
				Map<String, String> userMap = financeService.queryUserMonney(user
						.getId());
				request().setAttribute("userMap", userMap);
				//--- 殷梓淞添加 end
				
				// 借款显示类型，如果是流转标就跳转到流转标显示页面
				String cicuration = borrowDetailMap.get("borrowShow") + "";
				if (cicuration.equals("2")) {
					return "cicuration";
				}
			} else {
				return "404";
			}
		} else {
			return "404";
		}
		return "success";
	}

	/**
	 * 点击查看详情的时候，判断某标的的状态
	 * 
	 * @param tInt
	 * @return
	 * @throws Exception
	 */
	private String judgeStatus(int tInt, Long userId) throws Exception {
		if (tInt < 3) {// 秒还、净值标的
			Long aa = borrowService.queryBaseApprove(userId, 3);
			if (aa < 0) {
				return "waitBorrow";

			}
		} else {// 其它借款
			Long aa = borrowService.queryBaseApprove(userId, 3);
			if (aa < 0) {
				return "waitBorrow";
			} else {
				Long bb = borrowService.queryBaseFiveApprove(userId);
				if (bb < 0) {
					return "waitBorrow";
				}
			}
		}
		return null;
	}

	/**
	 * 债权转让借款详情
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public String queryDebtBorrowDetail() throws Exception {
		return financeDetail();
	}

	/**
	 * @MethodName: financeAudit
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-20 上午08:26:02
	 * @Return:
	 * @Descb: 借款人认证资料
	 * @Throws:
	 */
	public String financeAudit() throws Exception {
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		// 借款人认证资料
		List<Map<String, Object>> list = financeService
				.queryUserIdentifiedByid(idLong);
		request().setAttribute("auditList", list);
		return "success";
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @MethodName: financeRepay
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-20 上午08:27:02
	 * @Return:
	 * @Descb: 借款人还款记录
	 * @Throws:
	 */
	public String financeRepay() throws Exception {
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		// 借款人还款记录
		List<Map<String, Object>> list = financeService.queryRePayByid(idLong);
		request().setAttribute("repayList", list);
		return "success";
	}

	/**
	 * @MethodName: financeCollection
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-20 上午08:29:12
	 * @Return:
	 * @Descb: 借款人催款记录
	 * @Throws:
	 */
	public String financeCollection() throws Exception {
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		// 借款人催款记录
		List<Map<String, Object>> list = financeService
				.queryCollectionByid(idLong);
		request().setAttribute("collectionList", list);
		return "success";
	}

	/**
	 * 投标处理.
	 * @MethodName: financeInvestInit
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-20 上午08:20:57
	 * @Return:
	 * @Descb: 理财投标初始化
	 * @Throws:
	 */
	public String financeInvestInit() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String id = request().getParameter("id") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(request().getParameter("id"));
		if (!StringUtils.isNumericSpace(id)) {
			return INPUT;
		}
		long idLong = Convert.strToLong(id, -1);
		if (idLong == -1) {
			// 非法操作直接返回
			obj.put("msg", IConstants.ACTOIN_ILLEGAL);
			JSONHelper.printObject(obj);
			return null;
		}
		Map<String, String> investMaps = financeService.getInvestStatus(idLong);
		String nid_log = "";
		if (investMaps != null && investMaps.size() > 0) {
			nid_log = Convert.strToStr(investMaps.get("nid_log"), "");
			Map<String, String> typeLogMap = null;
			if (StringUtils.isNotBlank(nid_log)) {
				typeLogMap = shoveBorrowTypeService
						.queryBorrowTypeLogByNid(nid_log.trim());
				int stauts = Convert.strToInt(
						typeLogMap.get("subscribe_status"), -1);
				request().setAttribute("subscribes", stauts);
				request().setAttribute("investMaps", investMaps);
			}
		}

		if (investMaps != null && investMaps.size() > 0) {
			String hasPWD = investMaps.get("hasPWD") == null ? "-1"
					: investMaps.get("hasPWD");
			investDetailMap = financeService.queryBorrowInvest(idLong);
			double residue = Convert.strToDouble(
					investDetailMap.get("residue"), 0);
			double minTenderedSum = Convert.strToDouble(
					investDetailMap.get("minTenderedSum"), 0);
			if (residue < minTenderedSum) {
				request().setAttribute("minTenderedSum", residue);
			} else {
				request().setAttribute("minTenderedSum", minTenderedSum);
			}
			String userId = investDetailMap.get("userId") == null ? ""
					: investDetailMap.get("userId");
			
			//自己的标
			if (userId.equals(user.getId().toString())) {
				// 不满足投标条件,返回
				 obj.put("msg", "不能投标自己发布的借款");
				 JSONHelper.printObject(obj);
				 return SUCCESS;
			}
			session().setAttribute("investStatus", "ok");
			Map<String, String> userMap = financeService.queryUserMonney(user
					.getId());
			request().setAttribute("userMap", userMap);
			session().setAttribute("hasPWD", hasPWD);
		} else {
			// 不满足投标条件,返回
			obj.put("msg", "该借款投标状态已失效");
			JSONHelper.printObject(obj);
			return null;
		}
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: financeInvestLoad
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-4-5 下午05:04:52
	 * @Return:
	 * @Descb: 输入密码后的投标
	 * @Throws:
	 */
	public String financeInvestLoad() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		String investPWD = paramMap.get("investPWD") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("investPWD"));
		long idLong = Convert.strToLong(id, -1);

		if (idLong == -1) {
			// 非法操作直接返回
			obj.put("msg", IConstants.ACTOIN_ILLEGAL);
			JSONHelper.printObject(obj);
			return null;
		}
		if ("".equals(investPWD)) {
			this.addFieldError("paramMap['investPWD']", "请输入投标密码");
			return "input";
		}
		Map<String, String> investPWDMap = financeService.getInvestPWD(idLong,
				investPWD);
		if (investPWDMap == null || investPWDMap.size() == 0) {
			this.addFieldError("paramMap['investPWD']", "投标密码错误");
			return "input";
		}
		// 判断是否进行了资料审核
		Object object = session().getAttribute("investStatus");
		if (object == null) {
			return null;
		}
		Map<String, String> investMaps = financeService.getInvestStatus(idLong);
		if (investMaps != null && investMaps.size() > 0) {
			investDetailMap = financeService.queryBorrowInvest(idLong);

			String userId = investDetailMap.get("userId") == null ? ""
					: investDetailMap.get("userId");
			if (userId.equals(user.getId().toString())) {
				// 不满足投标条件,返回
				obj.put("msg", "不能投标自己发布的借款");
				JSONHelper.printObject(obj);
				return null;
			}
			Map<String, String> userMap = financeService.queryUserMonney(user
					.getId());
			request().setAttribute("userMap", userMap);
		} else {
			// 不满足投标条件,返回
			obj.put("msg", "该借款投标状态已失效");
			JSONHelper.printObject(obj);
			return null;
		}
		return "success";
	}

	/**
	 * @MethodName: financeInvest
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-30 下午03:53:34
	 * @Return:
	 * @Descb: 投标借款
	 * @Throws:
	 */
	public String financeInvest() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		String amount = paramMap.get("amount") == null ? "" : paramMap
				.get("amount");
		double amountDouble = Convert.strToDouble(amount, 0);
		String hasPWD = SqlInfusionHelper.filteSqlInfusion(""
				+ session().getAttribute("hasPWD"));
		String investPWD = paramMap.get("investPWD") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("investPWD"));
		int status = Convert.strToInt(paramMap.get("subscribes"), 2);
		investDetailMap = financeService.queryBorrowInvest(idLong);
		if ("1".equals(hasPWD)) {
			Map<String, String> investPWDMap = financeService.getInvestPWD(
					idLong, investPWD);
			if (investPWDMap == null || investPWDMap.size() == 0) {
				if (status == 1) {
					obj.put("msg", "投标密码错误");
					JSONHelper.printObject(obj);
					return null;
				}
				this.addFieldError("paramMap['investPWD']", "投标密码错误");
				return "input";
			}
		}

		if (amountDouble > Convert.strToDouble(investDetailMap.get("residue"),
				0.00)) {
			obj.put("msg", "投标金额超过本轮剩余投标金额！");
			JSONHelper.printObject(obj);
			return null;
		}
		if (amountDouble < Convert.strToDouble(
				investDetailMap.get("minTenderedSum"), 0.00)) {
			obj.put("msg", "投标金额低于本轮最低投标金额！");
			JSONHelper.printObject(obj);
			return null;
		}

		if (amountDouble > Convert.strToDouble(
				investDetailMap.get("borrowAmount"), 0.00)) {
			obj.put("msg", "投标金额超过本轮最高投标金额！");
			JSONHelper.printObject(obj);
			return null;
		}

		int num = 0;
		if (status == 1) {
			double smallestFlowUnit = Convert.strToDouble(
					paramMap.get("smallestFlowUnit"), 0.0);
			if (smallestFlowUnit == 0) {
				obj.put("msg", "操作失败");
				JSONHelper.printObject(obj);
				return null;
			}
			String result = Convert.strToStr(paramMap.get("result"), "");
			if (StringUtils.isBlank(result)) {
				obj.put("msg", "请输入购买的份数");
				JSONHelper.printObject(obj);
				return null;
			}
			boolean b = result.matches("[0-9]*");
			if (!b) {
				obj.put("msg", "请正确输入购买的份数");
				JSONHelper.printObject(obj);
				return null;
			}

			String userId = investDetailMap.get("userId") == null ? ""
					: investDetailMap.get("userId");

			if (userId.equals(user.getId().toString())) {
				obj.put("msg", "不能投标自己发布的借款");
				JSONHelper.printObject(obj);
				return null;
			}
			num = Integer.parseInt(result);
			if (num < 1) {
				obj.put("msg", "请正确输入购买的份数");
				JSONHelper.printObject(obj);
				return null;
			}
			amountDouble = num * smallestFlowUnit;
		}
		Map<String, String> result = financeService.addBorrowInvest(idLong,
				user.getId(), "", amountDouble, getBasePath(),
				user.getUserName(), status, num, null,"");

		String resultMSG = result.get("ret_desc");
		if ("".equals(resultMSG)) {
			// 查询是否满标，是否满标自动审核
			Map<String, String> autoMap = financeService
					.queryBorrowDetailById(idLong);
			int auditpass = Convert.strToInt(autoMap.get("auditpass"), 2);
			int borrowStatus = Convert
					.strToInt(autoMap.get("borrowStatus"), -1);
			if (borrowStatus == 3) {
				if (auditpass == 1) {
					Admin admin = (Admin) session().getAttribute(
							IConstants.SESSION_ADMIN);
					Map<String, String> retMap = borrowManageService
							.updateBorrowFullScaleStatus(idLong, 4l, "满标自动审核",
									admin.getId(), getBasePath(),
									autoMap.get("tradeNo"));
					long retVal = -1;
					retVal = Convert.strToLong(retMap.get("ret") + "", -1);
					session().removeAttribute("randomCode");
					if (retVal <= 0) {
						obj.put("msg", retMap.get("ret_desc"));
						JSONHelper.printObject(obj);
						return null;
					} else {
						obj.put("msg", "1");
						JSONHelper.printObject(obj);
						return null;
					}
				}
			}
			obj.put("msg", 1);
		} else {
			obj.put("msg", result.get("ret_desc") + "");
		}

		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * @MethodName: borrowMSGInit
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午11:08:51
	 * @Return:
	 * @Descb: 借款留言初始化
	 * @Throws:
	 */
	public String borrowMSGInit() throws Exception {
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		String pageNum = SqlInfusionHelper.filteSqlInfusion(paramMap.get("curPage"));
		if (StringUtils.isNotBlank(pageNum)) {
			pageBean.setPageNum(pageNum);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_6);
		if (idLong == -1) {
			return "404";
		}
		financeService.queryBorrowMSGBord(idLong, pageBean);
		request().setAttribute("id", id);
		return "success";
	}

	/**
	 * @throws IOException
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: addBorrowMSG
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午11:09:06
	 * @Return:
	 * @Descb: 添加借款留言
	 * @Throws:
	 */
	public String addBorrowMSG() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String code = (String) session().getAttribute("msg_checkCode");
		String _code = paramMap.get("code") == null ? "" : paramMap.get("code");
		if (!code.equals(_code)) {
			this.addFieldError("paramMap['code']", IConstants.CODE_FAULS);
			return "input";
		}
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		String msgContent = paramMap.get("msg") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("msg"));
		long returnId = -1;
		returnId = financeService
				.addBorrowMSG(idLong, user.getId(), msgContent);
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
	 * @throws IOException
	 * @throws DataException
	 * @MethodName: focusOnBorrow
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-16 上午09:06:16
	 * @Return:
	 * @Descb: 我关注的借款
	 * @Throws:
	 */
	public String focusOnBorrow() throws Exception, DataException {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		long returnId = -1L;
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		if (idLong == -1) {
			obj.put("msg", IConstants.ACTOIN_ILLEGAL);
			JSONHelper.printObject(obj);
			return null;
		}

		Map<String, String> map = financeService.hasFocusOn(idLong,
				user.getId(), IConstants.FOCUSON_BORROW);
		if (map != null && map.size() > 0) {
			obj.put("msg", "您已关注过该借款");
			JSONHelper.printObject(obj);
			return null;
		}

		returnId = financeService.addFocusOn(idLong, user.getId(),
				IConstants.FOCUSON_BORROW);
		if (returnId <= 0) {
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
		} else {
			obj.put("msg", "关注成功!");
			JSONHelper.printObject(obj);
			return null;
		}
	}

	/**
	 * @throws IOException
	 * @throws DataException
	 * @MethodName: focusOnUser
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-16 上午09:07:20
	 * @Return:
	 * @Descb: 我关注的用户
	 * @Throws:
	 */
	public String focusOnUser() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		long returnId = -1L;
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		if (idLong == -1) {
			obj.put("msg", IConstants.ACTOIN_ILLEGAL);
			JSONHelper.printObject(obj);
			return null;
		}

		Map<String, String> map = financeService.hasFocusOn(idLong,
				user.getId(), IConstants.FOCUSON_USER);
		if (map != null && map.size() > 0) {
			obj.put("msg", "您已关注过该用户");
			JSONHelper.printObject(obj);
			return null;
		}
		returnId = financeService.addFocusOn(idLong, user.getId(),
				IConstants.FOCUSON_USER);
		if (returnId <= 0) {
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
		} else {
			obj.put("msg", "关注成功!");
			JSONHelper.printObject(obj);
			return null;
		}
	}

	/**
	 * @MethodName: mailInit
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午06:23:31
	 * @Return:
	 * @Descb: 发送站内信初始化
	 * @Throws:
	 */
	public String mailInit() {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id"));
		String userName = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"username"));
		request().setAttribute("id", id);
		request().setAttribute("userName", userName);
		return "success";
	}

	/**
	 * @MethodName: reportInit
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午06:23:48
	 * @Return:
	 * @Descb: 举报用户初始化
	 * @Throws:
	 */
	public String reportInit() {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id"));
		String userName = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"username"));
		request().setAttribute("id", id);
		request().setAttribute("userName", userName);
		return "success";
	}

	public String mailAdd() throws Exception {
		User user = (User) session().getAttribute("user");

		JSONObject obj = new JSONObject();

		String code = (String) session().getAttribute("code_checkCode");
		String _code = paramMap.get("code") == null ? "" : paramMap.get("code");
		if (!code.equals(_code)) {
			this.addFieldError("paramMap['code']", IConstants.CODE_FAULS);
			return "input";
		}
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long reciver = Convert.strToLong(id, -1);
		String title = paramMap.get("title") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("title"));
		String content = paramMap.get("content") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("content"));
		long returnId = -1;
		Integer enable = user.getEnable();
		if (enable == 3) {
			obj.put("msg", "8");
			JSONHelper.printObject(obj);
			return null;
		}
		returnId = financeService.addUserMail(reciver, user.getId(), title,
				content, IConstants.MALL_TYPE_COMMON);
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
	 * @throws DataException
	 * @MethodName: reportAdd
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午10:16:11
	 * @Return:
	 * @Descb: 添加用户举报
	 * @Throws:
	 */
	public String reportAdd() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String code = (String) session().getAttribute("code_checkCode");
		String _code = paramMap.get("code") == null ? "" : paramMap.get("code");
		if (!code.equals(_code)) {
			this.addFieldError("paramMap['code']", IConstants.CODE_FAULS);
			return "input";
		}
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long reporter = Convert.strToLong(id, -1);
		String title = paramMap.get("title") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("title"));
		String content = paramMap.get("content") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("content"));
		long returnId = -1;
		returnId = financeService.addUserReport(reporter, user.getId(), title,
				content);
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
	 * @MethodName: showImg
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-4-16 上午11:24:03
	 * @Return:
	 * @Descb: 查看图片
	 * @Throws:
	 */
	public String showImg() throws Exception {
		String typeId = request().getParameter("typeId") == null ? ""
				: SqlInfusionHelper
						.filteSqlInfusion(request().getParameter("typeId"));
		String userId = request().getParameter("userId") == null ? ""
				: SqlInfusionHelper
						.filteSqlInfusion(request().getParameter("userId"));
		long typeIdLong = Convert.strToLong(typeId, -1);
		long userIdLong = Convert.strToLong(userId, -1);
		List<Map<String, Object>> imgList = financeService.queryUserImageByid(
				typeIdLong, userIdLong);
		request().setAttribute("imgList", imgList);
		return "success";
	}

	/**
	 * 跳转流转标购买 页面
	 * 
	 * @throws Exception
	 */
	public String subscribeinit() throws Exception {
		long borrowid = Convert.strToLong(request("borrowid"), -1);
		try {
			Map<String, String> borrowDetailMap = financeService
					.queryBorrowDetailById(borrowid);
			request().setAttribute("borrowDetailMap", borrowDetailMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String enzeindex() throws Exception{
		return SUCCESS;
	}
	/**
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: subscribe
	 * @Param: FrontMyBorrowAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 下午08:22:15
	 * @Return:
	 * @Descb: 认购流转标
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public String subscribe() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String id = paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		String result = paramMap.get("result");
		int resultLong = Convert.strToInt(result, -1);

		if (idLong == -1) {
			obj.put("msg", "无效认购标的");
			JSONHelper.printObject(obj);
			return null;
		}
		if (resultLong == -1) {
			obj.put("msg", "非法的认购份数");
			JSONHelper.printObject(obj);
			return null;
		} else if (resultLong <= 0) {
			obj.put("msg", "请输入正确的认购份数");
			JSONHelper.printObject(obj);
			return null;
		}
		obj.put("msg", "1");
		JSONHelper.printObject(obj);

		// String resultStr = financeService.subscribeSubmit(idLong, resultLong,
		// user.getId(), getBasePath(), user.getUserName(),getPlatformCost());
		// obj.put("msg", resultStr);
		// JSONUtils.printObject(obj);
		return null;
	}

	public Map<String, String> getInvestDetailMap() throws Exception {
		String id = request().getParameter("id") == null ? "" : request()
				.getParameter("id");
		long idLong = Convert.strToLong(id, -1);
		if (investDetailMap == null) {
			investDetailMap = financeService.queryBorrowInvest(idLong);
		}
		return investDetailMap;
	}

	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public NewsAndMediaReportService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsAndMediaReportService newsService) {
		this.newsService = newsService;
	}

	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public List<Map<String, Object>> getBorrowPurposeList() throws Exception {
		borrowPurposeList = selectedService.borrowPurpose();

		return borrowPurposeList;
	}

	public List<Map<String, Object>> getBorrowDeadlineList() throws Exception {
		borrowDeadlineList = selectedService.borrowDeadline();

		return borrowDeadlineList;
	}

	public List<Map<String, Object>> getBorrowAmountList() throws Exception {
		borrowAmountList = selectedService.borrowAmountRange();
		return borrowAmountList;
	}

	public void setShoveBorrowTypeService(
			ShoveBorrowTypeService shoveBorrowTypeService) {
		this.shoveBorrowTypeService = shoveBorrowTypeService;
	}

	public void setBorrowMSGMap(List<Map<String, Object>> borrowMSGMap) {
		this.borrowMSGMap = borrowMSGMap;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public void setInvestDetailMap(Map<String, String> investDetailMap) {
		this.investDetailMap = investDetailMap;
	}

	public void setBorrowPurposeList(List<Map<String, Object>> borrowPurposeList) {
		this.borrowPurposeList = borrowPurposeList;
	}

	public void setBorrowDeadlineList(
			List<Map<String, Object>> borrowDeadlineList) {
		this.borrowDeadlineList = borrowDeadlineList;
	}

	public void setBorrowAmountList(List<Map<String, Object>> borrowAmountList) {
		this.borrowAmountList = borrowAmountList;
	}

	// public void setLinksService(LinksService linksService) {
	// this.linksService = linksService;
	// }
	// public LinksService getLinksService() {
	// return linksService;
	// }

	public List<Map<String, Object>> getLinksList() {
		return linksList;
	}

	public PublicModelService getPublicModelService() {
		return publicModelService;
	}

	public void setPublicModelService(PublicModelService publicModelService) {
		this.publicModelService = publicModelService;
	}

	public void setLinksList(List<Map<String, Object>> linksList) {
		this.linksList = linksList;
	}

	// public void setMediaReportService(MediaReportService mediaReportService)
	// {
	// this.mediaReportService = mediaReportService;
	// }

	public List<Map<String, Object>> getMeikuList() {
		return meikuList;
	}

	public void setMeikuList(List<Map<String, Object>> meikuList) {
		this.meikuList = meikuList;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<Map<String, Object>> getMeikuStick() {
		return meikuStick;
	}

	public void setMeikuStick(List<Map<String, Object>> meikuStick) {
		this.meikuStick = meikuStick;
	}

	public List<Map<String, Object>> getListsGGList() {
		return listsGGList;
	}

	public void setListsGGList(List<Map<String, Object>> listsGGList) {
		this.listsGGList = listsGGList;
	}

	public List<Map<String, Object>> getBannerList() {
		return bannerList;
	}

	public void setBannerList(List<Map<String, Object>> bannerList) {
		this.bannerList = bannerList;
	}

}
