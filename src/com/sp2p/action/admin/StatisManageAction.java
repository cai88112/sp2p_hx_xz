package com.sp2p.action.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.fp2p.helper.DateHelper;
import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.SelectedService;
import com.sp2p.service.admin.StatisManageService;

/**
 * @ClassName: StatisManageAction.java
 * @Author: gang.lv
 * @Date: 2013-4-4 上午09:16:19
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb:统计管理控制层
 */
public class StatisManageAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(StatisManageAction.class);
	private static final long serialVersionUID = 1L;

	private StatisManageService statisManageService;
	private SelectedService selectedService;
	private List<Map<String, Object>> userGroupList;

	public StatisManageService getStatisManageService() {
		return statisManageService;
	}

	public void setStatisManageService(StatisManageService statisManageService) {
		this.statisManageService = statisManageService;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public List<Map<String, Object>> getUserGroupList() throws Exception {
		if (userGroupList == null) {
			userGroupList = selectedService.userGroup();
		}
		return userGroupList;
	}

	/**
	 * @throws Exception
	 * @MethodName: webStatisInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午09:16:36
	 * @Return:
	 * @Descb: 网站统计初始化
	 * @Throws:
	 */
	public String webStatisInit() throws Exception {
		Map<String, String> webMap = statisManageService.queryWebStatis();
		request().setAttribute("webMap", webMap);
		return "success";
	}

	/**
	 * 导出网站统计 excel
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String exportwebStatis() throws Exception {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 投标排名查询
			Map<String, String> webMap = statisManageService.queryWebStatis();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("covarianceName", "网站会员总金额");
			map1.put("covarianceNum", webMap.get("webUserAmount"));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("covarianceName", "网站会员总冻结金额");
			map2.put("covarianceNum", webMap.get("webUserFreezeAmount"));
			Map<String, Object> map3 = new HashMap<String, Object>();
			map3.put("covarianceName", "网站会员总待收金额");
			map3.put("covarianceNum", webMap.get("webUserForPI"));
			Map<String, Object> map4 = new HashMap<String, Object>();
			map4.put("covarianceName", "网站收入总金额");
			map4.put("covarianceNum", webMap.get("webComeInAmount"));
			Map<String, Object> map5 = new HashMap<String, Object>();
			map5.put("covarianceName", "网站总VIP金额");
			map5.put("covarianceNum", webMap.get("webVIPAmount"));
			Map<String, Object> map8 = new HashMap<String, Object>();
			map8.put("covarianceName", "网站总借款管理费金额");
			map8.put("covarianceNum", webMap.get("borrowManageFee"));
			Map<String, Object> map9 = new HashMap<String, Object>();
			map9.put("covarianceName", "网站总借款逾期罚息金额");
			map9.put("covarianceNum", webMap.get("borrowFI"));
			Map<String, Object> map11 = new HashMap<String, Object>();
			map11.put("covarianceName", "后台手动添加费用");
			map11.put("covarianceNum", webMap.get("backAddAmount"));
			Map<String, Object> map12 = new HashMap<String, Object>();
			map12.put("covarianceName", "后台手动扣除费用");
			map12.put("covarianceNum", webMap.get("backDelAmount"));
			Map<String, Object> map13 = new HashMap<String, Object>();
			map13.put("covarianceName", "网站成功充值总额");
			map13.put("covarianceNum", webMap.get("webSucPrepaid"));
			Map<String, Object> map14 = new HashMap<String, Object>();
			map14.put("covarianceName", "网站线上充值总额");
			map14.put("covarianceNum", webMap.get("onlinePrepaid"));
			Map<String, Object> map15 = new HashMap<String, Object>();
			map15.put("covarianceName", "网站线下充值总额");
			map15.put("covarianceNum", webMap.get("downlinePrepaid"));
			Map<String, Object> map16 = new HashMap<String, Object>();
			map16.put("covarianceName", "网站提现总额");
			map16.put("covarianceNum", webMap.get("cashWith"));
			Map<String, Object> map17 = new HashMap<String, Object>();
			map17.put("covarianceName", "网站提现手续费总额");
			map17.put("covarianceNum", webMap.get("cashWithFee"));
			Map<String, Object> map22 = new HashMap<String, Object>();
			map22.put("covarianceName", "所有借款未还总额");
			map22.put("covarianceNum", webMap.get("borrowForPI"));
			Map<String, Object> map23 = new HashMap<String, Object>();
			map23.put("covarianceName", "所有逾期网站垫付未还款金额");
			map23.put("covarianceNum", webMap.get("webAdvinceForP"));
			Map<String, Object> map24 = new HashMap<String, Object>();
			map24.put("covarianceName", "借款逾期网站未垫付未还款金额");
			map24.put("covarianceNum", webMap.get("borrowForAmount"));
			Map<String, Object> map25 = new HashMap<String, Object>();
			map25.put("covarianceName", "所有借款已还款总额");
			map25.put("covarianceNum", webMap.get("borrowHasAmount"));
			Map<String, Object> map26 = new HashMap<String, Object>();
			map26.put("covarianceName", "所有借款正常还款总额");
			map26.put("covarianceNum", webMap.get("borrowNomalRepayAmount"));
			Map<String, Object> map27 = new HashMap<String, Object>();
			map27.put("covarianceName", "借款逾期网站垫付后已还款总额");
			map27.put("covarianceNum", webMap.get("webAdvinceHasP"));
			Map<String, Object> map28 = new HashMap<String, Object>();
			map28.put("covarianceName", "借款逾期的网站未垫付已还款总额");
			map28.put("covarianceNum", webMap.get("webNoAdvinceHasP"));
			Map<String, Object> map29 = new HashMap<String, Object>();
			map29.put("covarianceName", "借款逾期网站垫付总额");
			map29.put("covarianceNum", webMap.get("webAdviceAmount"));

			list.add(map1);
			list.add(map2);
			list.add(map3);
			list.add(map4);
			list.add(map5);
			list.add(map8);
			list.add(map9);
			list.add(map11);
			list.add(map12);
			list.add(map13);
			list.add(map14);
			list.add(map15);
			list.add(map16);
			list.add(map17);
			list.add(map22);
			list.add(map23);
			list.add(map24);
			list.add(map25);
			list.add(map26);
			list.add(map27);
			list.add(map28);
			list.add(map29);

			HSSFWorkbook wb = ExcelHelper.exportExcel("网站统计", list,
					new String[] { "统计项", "金额" }, new String[] {
							"covarianceName", "covarianceNum" });
			this.export(wb, new Date().getTime() + ".xls");

			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("pr_getWebStatis", admin
					.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0,
					"导出网站统计列表", 2);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @throws Exception
	 * @MethodName: loginStatisList
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午09:45:13
	 * @Return:
	 * @Descb: 登录统计列表
	 * @Throws:
	 */
	public String loginStatisList() throws Exception {
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = paramMap.get("userName") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("userName"));
		String realName = paramMap.get("realName") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("realName"));
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		String count = paramMap.get("count") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("count"));
		int countInt = Convert.strToInt(count, -1);
		statisManageService.queryLoginStatisByCondition(userName, realName,
				timeStart, timeEnd, countInt, pageBean);

		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return "success";
	}

	/**
	 * @MethodName: loginStatisInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午09:16:44
	 * @Return:
	 * @Descb: 登录统计初始化
	 * @Throws:
	 */
	public String loginStatisInit() {
		return "success";
	}

	/**
	 * @MethodName: financeStatisInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午09:16:57
	 * @Return:
	 * @Descb: 投资统计初始化
	 * @Throws:
	 */
	public String financeStatisInit() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: financeStatisList
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-6 下午02:51:14
	 * @Return:
	 * @Descb: 投资统计列表
	 * @Throws:
	 */
	public String financeStatisList() throws Exception {
		String radio = paramMap.get("radio") == null ? "" : paramMap
				.get("radio");
		int radioInt = Convert.strToInt(radio, -1);
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.UNDERLINE_DATE_SHORT);
		SimpleDateFormat sfYear = new SimpleDateFormat(DateHelper.YEAR);
		Date date = new Date();
		if (radioInt == -1) {
			// 没有日期限制
			timeStart = "";
			timeEnd = "";
		} else if (radioInt == 1) {
			// d当日
			timeStart = sf.format(date) + " 00:00:00";
			timeEnd = sf.format(date) + " 23:59:59";
		} else if (radioInt == 2) {
			// 当月
			timeStart = DateFormatUtils.format(DateHelper.getMonthFirstDay(), "yyyy-MM-dd HH:mm:ss");
			timeEnd = DateHelper.getMonthLastDay().toString();
		} else if (radioInt == 3) {
			// 当年
			timeStart = sfYear.format(date) + "-01-01 00:00:00";
			timeEnd = sfYear.format(date) + "-12-31 23:59:59";
		}
		Map<String, String> financeEarnMap = statisManageService
				.queryFinanceEarnStatis(timeStart, timeEnd);
		request().setAttribute("financeEarnMap", financeEarnMap);
		return "success";
	}

	/**
	 * 导出投资统计列表 excel
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String exportfinanceStatis() throws Exception {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		String radio = request("radio") == null ? "" : request("radio");
		int radioInt = Convert.strToInt(radio, -1);
		String timeStart = request("timeStart") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(request("timeStart"));
		String timeEnd = request("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(request("timeEnd"));
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.UNDERLINE_DATE_SHORT);
		SimpleDateFormat sfYear = new SimpleDateFormat(DateHelper.YEAR);
		Date date = new Date();
		if (radioInt == -1) {
			// 没有日期限制
			timeStart = "";
			timeEnd = "";
		} else if (radioInt == 1) {
			// d当日
			timeStart = sf.format(date) + " 00:00:00";
			timeEnd = sf.format(date) + " 23:59:59";
		} else if (radioInt == 2) {
			// 当月
			timeStart = DateFormatUtils.format(DateHelper.getMonthFirstDay(), "yyyy-MM-dd HH:mm:ss");
			timeEnd = DateFormatUtils.format(DateHelper.getMonthLastDay(), "yyyy-MM-dd HH:mm:ss");
		} else if (radioInt == 3) {
			// 当年
			timeStart = sfYear.format(date) + "-01-01 00:00:00";
			timeEnd = sfYear.format(date) + "-12-31 23:59:59";
		}

		try {

			// 投标排名查询
			Map<String, String> financeEarnMap = statisManageService
					.queryFinanceEarnStatis(timeStart, timeEnd);

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("covarianceName", "投资成功待收金额");
			map1.put("covarianceNum", financeEarnMap.get("investForAmount"));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("covarianceName", "投资奖励金额");
			map2.put("covarianceNum", financeEarnMap.get("investRewardAmount"));
			Map<String, Object> map3 = new HashMap<String, Object>();
			map3.put("covarianceName", "借款人逾期罚金金额");
			map3.put("covarianceNum", financeEarnMap.get("borrowLateFAmount"));
			Map<String, Object> map4 = new HashMap<String, Object>();
			map4.put("covarianceName", "用户邀请好友金额");
			map4.put("covarianceNum", financeEarnMap.get("inviteReward"));
			Map<String, Object> map5 = new HashMap<String, Object>();
			map5.put("covarianceName", "借款成功总额");
			map5.put("covarianceNum", financeEarnMap.get("borrowAmount"));
			Map<String, Object> map6 = new HashMap<String, Object>();
			map6.put("covarianceName", "借款管理费总额");
			map6.put("covarianceNum", financeEarnMap.get("borrowManageFee"));
			Map<String, Object> map7 = new HashMap<String, Object>();
			map7.put("covarianceName", "借款利息总额");
			map7.put("covarianceNum", financeEarnMap
					.get("borrowInterestAmount"));
			Map<String, Object> map8 = new HashMap<String, Object>();
			map8.put("covarianceName", "借款奖励总额");
			map8.put("covarianceNum", financeEarnMap.get("borrowRewardAmount"));
			Map<String, Object> map9 = new HashMap<String, Object>();
			map9.put("covarianceName", "借款逾期罚息总额");
			map9.put("covarianceNum", financeEarnMap.get("borrowLateFI"));

			list.add(map1);
			list.add(map2);
			list.add(map3);
			list.add(map4);
			list.add(map5);
			list.add(map6);
			list.add(map7);
			list.add(map8);
			list.add(map9);

			HSSFWorkbook wb = ExcelHelper.exportExcel("投资盈利统计", list,
					new String[] { "统计项", "金额" }, new String[] {
							"covarianceName", "covarianceNum" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("pr_getFinanceEarnStatis",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出投资盈利统计列表", 2);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @throws SQLException
	 * @MethodName: investStatisInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午09:17:07
	 * @Return:
	 * @Descb: 投标统计初始化
	 * @Throws:
	 */
	public String investStatisInit() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: investStatisList
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-5 下午09:13:15
	 * @Return:
	 * @Descb: 投标统计列表
	 * @Throws:
	 */
	public String investStatisList() throws Exception {
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String bTitle = paramMap.get("bTitle") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("bTitle"));
		String investor = paramMap.get("investor") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("investor"));
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		String borrowWay = paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay");
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		String isAutoBid = paramMap.get("isAutoBid") == null ? "" : paramMap
				.get("isAutoBid");
		int isAutoBidInt = Convert.strToInt(isAutoBid, -1);
		String borrowStatus = paramMap.get("borrowStatus") == null ? ""
				: paramMap.get("borrowStatus");
		if ("1".equals(borrowStatus)) {
			borrowStatus = "(1,2,3,6)";
		} else if ("2".equals(borrowStatus)) {
			borrowStatus = "(4,5)";
		}
		String group = paramMap.get("group") == null ? "" : paramMap
				.get("group");
		int groupInt = Convert.strToInt(group, -1);
		statisManageService.queryInvestStatisByCondition(bTitle, investor,
				timeStart, timeEnd, borrowWayInt, isAutoBidInt, borrowStatus,
				groupInt, pageBean);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		request().setAttribute("totalNum", pageBean.getTotalNum());
		return "success";
	}

	/**
	 * 导出投标统计列表
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportinvestStatis() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 得到页面传来的值
			// 借款标题
			String bTitle = request().getParameter("bTitle") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("bTitle"));
			bTitle = URLDecoder.decode(bTitle, "UTF-8");
			// 时间
			String timeStart = request().getParameter("timeStart") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeStart"));
			String timeEnd = request().getParameter("timeEnd") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeEnd"));
			timeStart = URLDecoder.decode(timeStart, "UTF-8");
			timeEnd = URLDecoder.decode(timeEnd, "UTF-8");
			// 用户名
			String investor = request().getParameter("investor") == null ? ""
					: request().getParameter("investor");
			investor = URLDecoder.decode(investor, "UTF-8");
			// 借款类型
			int borrowWay = Convert.strToInt(request()
					.getParameter("borrowWay"), -1);
			// 是否自动投标
			int isAutoBid = Convert.strToInt(request()
					.getParameter("isAutoBid"), -1);

			// 是否投标成功
			String borrowStatus = request().getParameter("borrowStatus") == null ? ""
					: request().getParameter("borrowStatus");
			if ("1".equals(borrowStatus)) {
				borrowStatus = "(1,2,3,6)";
			} else if ("2".equals(borrowStatus)) {
				borrowStatus = "(4,5)";
			}
			// 用户组
			int group = Convert.strToInt(request().getParameter("group"), -1);

			// 已还款记录列表
			statisManageService.queryInvestStatisByCondition(bTitle, investor,
					timeStart, timeEnd, borrowWay, isAutoBid, borrowStatus,
					group, pageBean);
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
			
			statisManageService.changeNumToStr(pageBean);
			HSSFWorkbook wb = ExcelHelper.exportExcel("投标记录",
					pageBean.getPage(),
					new String[] { "用户名", "投标扣除金额(￥)", "交易对方", "借款标题", "借款类型",
							"是否自动投标", "是否投标成功", "投标时间" }, new String[] {
							"investor", "realAmount", "borrower",
							"borrowTitle", "borrowWay", "isAutoBid",
							"borrowStatus", "investTime" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_invest",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出投标统计列表", 2);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @MethodName: investStatisRankInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-5 下午10:34:18
	 * @Return:
	 * @Descb: 投资排名
	 * @Throws:
	 */
	public String investStatisRankInit() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: investStatisRankList
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-5 下午10:34:26
	 * @Return:
	 * @Descb: 投表排名查询
	 * @Throws:
	 */
	public String investStatisRankList() throws Exception {

		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String investor = paramMap.get("investor") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("investor"));
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		String group = paramMap.get("group") == null ? "" : paramMap
				.get("group");
		int groupInt = Convert.strToInt(group, -1);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		statisManageService.queryInvestStatisRankByCondition(investor,
				timeStart, timeEnd, groupInt, pageBean);

		return "success";
	}

	/**
	 * 导出投表排名查询 excel
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportinvestStatisRank() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {
			// 用户名
			String investor = request().getParameter("investor") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("investor"));
			investor = URLDecoder.decode(investor, "UTF-8");
			// 时间
			String timeStart = request().getParameter("timeStart") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeStart"));
			String timeEnd = request().getParameter("timeEnd") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeEnd"));
			timeStart = URLDecoder.decode(timeStart, "UTF-8");
			timeEnd = URLDecoder.decode(timeEnd, "UTF-8");
			// 用户组
			int group = Convert.strToInt(request().getParameter("group"), -1);
			// 投标排名查询
			statisManageService.queryInvestStatisRankByCondition(investor,
					timeStart, timeEnd, group, pageBean);
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
			List<Map<String, Object>> list = pageBean.getPage();
			Long num = 1L;
			if (list != null) {
				for (Map<String, Object> map : list) {
					map.put("count", num);
					num += 1L;

				}
			}

			HSSFWorkbook wb = ExcelHelper.exportExcel("投标排名",
					pageBean.getPage(), new String[] { "排名", "用户名", "真是姓名",
							"期间成功投标金额", "期间投标金额总计", "账号总额型", "可以金额", "待收金额",
							"会员积分", "信用积分" }, new String[] { "count",
							"investor", "realName", "realAmount", "realSum",
							"totalSum", "usableSum", "forPI", "rating",
							"creditrating" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_invest",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出投标排名列表", 2);

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
	 * @MethodName: borrowStatisInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午09:17:15
	 * @Return:
	 * @Descb: 借款统计初始化
	 * @Throws:
	 */
	public String borrowStatisInit() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowStatisList
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-6 上午10:25:18
	 * @Return:
	 * @Descb: 借款投资统计
	 * @Throws:
	 */
	public String borrowStatisList() throws Exception {
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String borrowTitle = paramMap.get("borrowTitle") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowTitle"));
		String borrower = paramMap.get("borrower") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("borrower"));
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		String borrowWay = paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay");
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		statisManageService.queryBorrowStatisByCondition(borrowTitle, borrower,
				timeStart, timeEnd, borrowWayInt, pageBean);
		// 借款管理费统计总额
		Map<String, String> feeMap = statisManageService
				.queryBorrowStatisAmount(borrowTitle, borrower, timeStart,
						timeEnd, borrowWayInt);

		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		request().setAttribute("feeMap", feeMap);
		return "success";
	}

	/**
	 * 导出借款投资统计 excel
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportborrowStatis() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 用户名
			String investor = request().getParameter("borrower") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("borrower"));
			investor = URLDecoder.decode(investor, "UTF-8");
			// 标题
			String borrowTitle = request().getParameter("borrowTitle") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("borrowTitle"));
			borrowTitle = URLDecoder.decode(borrowTitle, "UTF-8");
			// 时间
			String timeStart = request().getParameter("timeStart") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeStart"));
			String timeEnd = request().getParameter("timeEnd") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeEnd"));
			timeStart = URLDecoder.decode(timeStart, "UTF-8");
			timeEnd = URLDecoder.decode(timeEnd, "UTF-8");
			// 借款类型
			int borroWer = Convert.strToInt(
					request().getParameter("borrowWay"), -1);
			statisManageService.queryBorrowStatisByCondition(borrowTitle,
					investor, timeStart, timeEnd, borroWer, pageBean);
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
			statisManageService.changeNumToStr(pageBean);

			HSSFWorkbook wb = ExcelHelper.exportExcel("借款管理费统计", pageBean
					.getPage(), new String[] { "借款用户名", "借款标题", "借款金额(￥)",
					"借款类型", "借款期限", "借款管理费", "复审成功时间" }, new String[] {
					"borrower", "borrowTitle", "borrowAmount", "borrowWay",
					"deadline", "manageFee", "auditTime", });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_borrow",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出借款管理费统计列表", 2);

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
	 * @MethodName: borrowStatisInterestInit
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-6 上午11:36:46
	 * @Return:
	 * @Descb: 投资利息统计初始化
	 * @Throws:
	 */
	public String borrowStatisInterestInit() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowStatisInterestList
	 * @Param: StatisManageAction
	 * @Author: gang.lv
	 * @Date: 2013-4-6 上午11:36:57
	 * @Return:
	 * @Descb: 投资利息查询
	 * @Throws:
	 */
	public String borrowStatisInterestList() throws Exception {
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String investor = paramMap.get("investor") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("investor"));
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		statisManageService.queryborrowStatisInterestByCondition(investor,
				timeStart, timeEnd, pageBean);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return "success";
	}

	/**
	 * 导出借款投资统计 excel
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportborrowStatisInterest() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {

			// 得到页面传来的值
			// 用户名
			String investor = request().getParameter("investor") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("investor"));
			investor = URLDecoder.decode(investor, "UTF-8");
			// 时间
			String timeStart = request().getParameter("timeStart") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeStart"));
			String timeEnd = request().getParameter("timeEnd") == null ? ""
					: SqlInfusionHelper.filteSqlInfusion(request().getParameter("timeEnd"));
			timeStart = URLDecoder.decode(timeStart, "UTF-8");
			timeEnd = URLDecoder.decode(timeEnd, "UTF-8");
			// 借款投资统计
			statisManageService.queryborrowStatisInterestByCondition(investor,
					timeStart, timeEnd, pageBean);

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

			HSSFWorkbook wb = ExcelHelper.exportExcel("投资利息管理费统计", pageBean
					.getPage(), new String[] { "用户名", "真是姓名", "期间投资管理费利息总额",
					"期间收到还款总额", "已赚利息总额 ", "待收利息总额", "待收总额" }, new String[] {
					"investor", "realName", "manageFI", "hasPI", "hasInterest",
					"forInterest", "forPI", });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_invest",
					admin.getUserName(), IConstants.EXCEL, admin.getLastIP(),
					0, "导出投资利息管理费统计", 2);

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
	 * 用户组统计查询初始化
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String queryborrowStatisUserGroupInit() throws SQLException,
			DataException {
		return SUCCESS;
	}

	/**
	 * 用户组统计查询
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryborrowStatisUserGroupPage() throws Exception {
		String groupName = paramMap.get("groupName") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("groupName"));
		try {
			statisManageService.queryborrowStatisUserGroupByCondition(
					groupName, pageBean);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return SUCCESS;

	}

	/**
	 * 导出用户组统计
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	@SuppressWarnings( { "unchecked", "unchecked", "unchecked" })
	public String exportUserGroup() throws SQLException, DataException {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);
		try {
			String groupName = request().getParameter("groupName") == null ? ""
					: request().getParameter("groupName");
			groupName = URLDecoder.decode(groupName, "UTF-8");
			// 导出
			statisManageService.queryborrowStatisUserGroupByCondition(
					groupName, pageBean);
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
			HSSFWorkbook wb = ExcelHelper.exportExcel("用户组统计", pageBean
					.getPage(), new String[] { "序号", "组名", "总金额(元)", "冻结金额(元)",
					"待收金额(元) ", "借款管理费金额", "待收利息总额(元)", "VIP总金额", "已还款总额",
					"投资总额" }, new String[] { "groupId", "groupName",
					"totalSum", "freezeSum", "forPI", "manageFee",
					"forInterest", "vipFee", "hasPI", "realAmount" });
			this.export(wb, new Date().getTime() + ".xls");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("v_t_group_user_amount", admin
					.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0,
					"导出用户组统计", 2);

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

}