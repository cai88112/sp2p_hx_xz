package com.sp2p.action.front;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import net.sf.json.JSONObject;

import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.FrontMyPaymentService;
import com.sp2p.service.UserService;

public class FrontMyPaymentAction extends BaseFrontAction {

	private FrontMyPaymentService frontpayService;
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public FrontMyPaymentService getFrontpayService() {
		return frontpayService;
	}

	public void setFrontpayService(FrontMyPaymentService frontpayService) {
		this.frontpayService = frontpayService;
	}

	/**
	 * 成功的借款 
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String queryMySuccessBorrowList() throws Exception {
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		pageBean.setPageNum(request("curPage"));
		String startTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("startTime")), null);
		String endTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("endTime")), null);
		String title = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("title")), null);
		
		int borrowStatus = request("borrowStatus") == null ? -1 : Convert
				.strToInt(request("borrowStatus"), -1);
		endTime = changeEndTime(endTime);

		

		if (borrowStatus == -1) {
			frontpayService.queryMySuccessBorrowList(pageBean, userId,
					startTime, endTime, title, -1);
		} else {// 还款中的借款 已还完的借款
			frontpayService.queryMySuccessBorrowList(pageBean, userId,
					startTime, endTime, title, borrowStatus);
		}

		List<Map<String, Object>> lists = pageBean.getPage();

		if (lists != null) {
			for (Map<String, Object> map : lists) {
				if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_NET_VALUE)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_NET_VALUE_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_SECONDS)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_SECONDS_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_GENERAL)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_GENERAL_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_FIELD_VISIT)) {
					map.put("borrowWay",IConstants.BORROW_TYPE_FIELD_VISIT_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
					map.put("borrowWay",IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
				}else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
					map.put("borrowWay",IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
				}

				if (map.get("borrowStatus").toString().equals(
						IConstants.BORROW_STATUS_4 + "")) {
					map.put("borrowStatus", IConstants.BORROW_STATUS_4_STR);
				} else if (map.get("borrowStatus").toString().equals(
						IConstants.BORROW_STATUS_5 + "")) {
					map.put("borrowStatus", IConstants.BORROW_STATUS_5_STR);
				}
			}
		}
		this.setRequestToParamMap();
		return SUCCESS;
	}
	
	/**
	 * 导出成功借款,正在还款的借款，已还借款 的数据excel
	 * @return
	 */
	public String exportSuccessBorrow(){
		
		Long userId = this.getUserId();// 获得用户编号
		Integer status=Convert.strToInt(request("status"), -1);
		pageBean.pageNum = 1;
		pageBean.setPageSize(5000);
		try {               
			String startTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("startTime")),null);
			String endTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("endTime")),null);
			endTime = changeEndTime(endTime);
			String title =Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("title")),null);
			//中文乱码转换
			if(StringUtils.isNotBlank(title)){
				title = URLDecoder.decode(title,"UTF-8");
			}
			//成功借款
			frontpayService.queryMySuccessBorrowList(pageBean, userId,
					startTime, endTime, title, status);
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
			List<Map<String, Object>> list = pageBean.getPage();
			if (list == null) {
				list = new ArrayList<Map<String, Object>>();
			}
			if (list != null) {
				for (Map<String, Object> map : list) {
					if (map.get("borrowWay").toString().equals(
							IConstants.BORROW_TYPE_NET_VALUE)) {
						map.put("borrowWay", IConstants.BORROW_TYPE_NET_VALUE_STR);
					} else if (map.get("borrowWay").toString().equals(
							IConstants.BORROW_TYPE_SECONDS)) {
						map.put("borrowWay", IConstants.BORROW_TYPE_SECONDS_STR);
					} else if (map.get("borrowWay").toString().equals(
							IConstants.BORROW_TYPE_GENERAL)) {
						map.put("borrowWay", IConstants.BORROW_TYPE_GENERAL_STR);
					} else if (map.get("borrowWay").toString().equals(
							IConstants.BORROW_TYPE_FIELD_VISIT)) {
						map
								.put("borrowWay",
										IConstants.BORROW_TYPE_FIELD_VISIT_STR);
					} else if (map.get("borrowWay").toString().equals(
							IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
						map.put("borrowWay",
								IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
					}

					if (map.get("borrowStatus").toString().equals(
							IConstants.BORROW_STATUS_4 + "")) {
						map.put("borrowStatus", IConstants.BORROW_STATUS_4_STR);
					} else if (map.get("borrowStatus").toString().equals(
							IConstants.BORROW_STATUS_5 + "")) {
						map.put("borrowStatus", IConstants.BORROW_STATUS_5_STR);
					}
				}
			}
			HSSFWorkbook wb=null;
			if(status==-1){
				wb = ExcelHelper.exportExcel("成功借款", pageBean.getPage(),
						new String[] { "标题", "借款类型", "借款金额(￥)", "年利率(%)", "还款期限(月)", "借款时间",
						          "偿还本息(￥)","已还本息(￥)","未还本息(￥)",
								"状态" }, new String[] { "borrowTitle", "borrowWay",
								"borrowAmount", "annualRate", "deadline",
								"publishTime", "stillTotalSum","hasPI","hasSum","borrowStatus" });
			}else if(status==4){
				wb = ExcelHelper.exportExcel("正在还款的借款", pageBean.getPage(),
						new String[] { "标题", "借款类型", "借款金额(￥)", "年利率(%)", "还款期限(月)", "借款时间",
						          "偿还本息(￥)","已还本息(￥)","未还本息(￥)"
								 }, new String[] { "borrowTitle", "borrowWay",
								"borrowAmount", "annualRate", "deadline",
								"publishTime", "stillTotalSum","hasPI","hasSum" });
			}else if(status==5){
				
				wb = ExcelHelper.exportExcel("已还完的借款", pageBean.getPage(),
						new String[] { "标题", "借款类型", "借款金额(￥)", "年利率(%)", "还款期限(月)", "借款时间",
						          "偿还本息(￥)","已还本息(￥)","已还逾期罚息(￥)"
								 }, new String[] { "borrowTitle", "borrowWay",
								"borrowAmount", "annualRate", "deadline",
								"publishTime", "stillTotalSum","stillTotalSum","hasFI" });
			}
			
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
	 * 正在还款的借款
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String queryMyPayingBorrowList() throws Exception {
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		pageBean.setPageNum(request("curPage"));
		String startTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("startTime")), null);
		String endTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("endTime")), null);
		String title = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("title")), null);
		/*int borrowStatus = paramMap.get("borrowStatus") == null ? -1 : Convert
				.strToInt(paramMap.get("borrowStatus"), -1);*/
		
		int borrowStatus = IConstants.BORROW_STATUS_4;
		endTime = changeEndTime(endTime);

		if (borrowStatus == -1) {
			frontpayService.queryMySuccessBorrowList(pageBean, userId,
					startTime, endTime, title, -1);
		} else {// 还款中的借款 已还完的借款
			frontpayService.queryMySuccessBorrowList(pageBean, userId,
					startTime, endTime, title, borrowStatus);
		}

		List<Map<String, Object>> lists = pageBean.getPage();

		if (lists != null) {
			for (Map<String, Object> map : lists) {
				if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_NET_VALUE)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_NET_VALUE_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_SECONDS)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_SECONDS_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_GENERAL)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_GENERAL_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_FIELD_VISIT)) {
					map.put("borrowWay",IConstants.BORROW_TYPE_FIELD_VISIT_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
					map.put("borrowWay",IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
				}else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
					map.put("borrowWay",IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
				}			
			}
		}
		
		this.setRequestToParamMap();
		return SUCCESS;
	}
	
	/**
	 * 已还完借款
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String queryMyPayoffBorrowList() throws Exception {
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		pageBean.setPageNum(request("curPage"));
		String startTime = SqlInfusionHelper.filteSqlInfusion(request("startTime"));
		String endTime = SqlInfusionHelper.filteSqlInfusion(request("endTime"));
		String title = SqlInfusionHelper.filteSqlInfusion(request("title"));
		int borrowStatus = IConstants.BORROW_STATUS_5;
		endTime = changeEndTime(endTime);

		frontpayService.queryMySuccessBorrowList(pageBean, userId,
					startTime, endTime, title, borrowStatus);
		
		List<Map<String, Object>> lists = pageBean.getPage();

		if (lists != null) {
			for (Map<String, Object> map : lists) {
				if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_NET_VALUE)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_NET_VALUE_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_SECONDS)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_SECONDS_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_GENERAL)) {
					map.put("borrowWay", IConstants.BORROW_TYPE_GENERAL_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_FIELD_VISIT)) {
					map
							.put("borrowWay",
									IConstants.BORROW_TYPE_FIELD_VISIT_STR);
				} else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_INSTITUTION_GUARANTEE_STR);
				}else if (map.get("borrowWay").toString().equals(
						IConstants.BORROW_TYPE_INSTITUTION_FLOW)) {
					map.put("borrowWay",
							IConstants.BORROW_TYPE_INSTITUTION_FLOW_STR);
				}

				map.put("borrowStatus", IConstants.BORROW_STATUS_5_STR);
				
			}
		}
		this.setRequestToParamMap();
		return SUCCESS;
	}

	/**
	 * 正在还款的借款详情
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String queryPayingDetails() throws Exception {
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		Long borrowId = paramMap.get("borrowId") == null ? -1 : Convert.strToLong(paramMap.get("borrowId"), -1);// 
		
		int status = -1;
		if(paramMap.get("status") != null){
			status = Convert.strToInt(paramMap.get("status"), -1);
		}
		// 获得统计信息
		Map<String, String> map = null;

		pageBean.setPageSize(IConstants.PAGE_SIZE_6);

		if(borrowId == -1){
			return null;
		}
		frontpayService.queryPayingDetails(pageBean, borrowId,status);
		map = frontpayService.queryOneBorrowInfo(userId,
				borrowId);
		
		List<Map<String, Object>> lists = pageBean.getPage();
		if (lists != null) {
			for (Map<String, Object> mp : lists) {
				if (Convert.strToInt(mp.get("repayStatus").toString(), -1) == IConstants.PAYING_STATUS_NON) {
					mp.put("repayStatus", IConstants.PAYING_STATUS_NON_STR);
				} else if (Convert.strToInt(mp.get("repayStatus").toString(),
						-1) == IConstants.PAYING_STATUS_PAYING) {
					mp.put("repayStatus", IConstants.PAYING_STATUS_PAYING_STR);
				}else if(Convert.strToInt(mp.get("repayStatus").toString(),
						-1) == IConstants.PAYING_STATUS_SUCCESS){//已偿还完
					mp.put("repayStatus", IConstants.PAYING_STATUS_SUCCESS_STR);
				}
			}
		}

		// map 首次加载时，为Null
		if(map != null){
			request().setAttribute("borrowTitle", map.get("borrowTitle"));
			request().setAttribute("borrowAmount", map.get("borrowAmount"));
			request().setAttribute("annualRate", map.get("annualRate"));
			request().setAttribute("deadline", map.get("deadline"));
			request().setAttribute("isDayThe", map.get("isDayThe"));
			request().setAttribute("paymentMode", map.get("paymentMode"));
			request().setAttribute("borrowId", map.get("id"));
//			if (Convert.strToInt(map.get("paymentMode").toString(), -1) == IConstants.PAY_WAY_MONTH) {
//				request().setAttribute("paymentMode", 1);
//			} 
//			else if(Convert.strToInt(map.get("paymentMode").toString(), -1) == IConstants.PAY_WAY_ONE){
//				request().setAttribute("paymentMode", 4);
//			}
//			else if(Convert.strToInt(map.get("paymentMode").toString(), -1) == IConstants.PAY_WAY_RATE){
//				request().setAttribute("paymentMode", 2);}
//			else if(Convert.strToInt(map.get("paymentMode").toString(), -1) == IConstants.PAY_WAY_SECONDS){
//				request().setAttribute("paymentMode",3);
//			}
			request().setAttribute("publishTime", map.get("publishTime"));		
			request().setAttribute("auditTime", map.get("auditTime").substring(0, 10));
		}
		return SUCCESS;
	}

	/**
	 * 还款明细账
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public String queryAllDetails() throws Exception {
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		pageBean.setPageNum(request("curPage"));
		String startTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("startTime")), null);
		String endTime = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("endTime")), null);
		String title = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request("title")), null);
		endTime = changeEndTime(endTime);

		pageBean.setPageSize(IConstants.PAGE_SIZE_10);

		frontpayService.queryAllDetails(pageBean, userId, startTime, endTime,
				title);
		this.setRequestToParamMap();
		return SUCCESS;
	}
	
	/**
	 * 还款明细账，的数据导出excel文件
	 * @return
	 */
	public String exportrepayment(){
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		
		pageBean.pageNum = 1;
		pageBean.setPageSize(5000);
		try {
			//还款明细账
			String startTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("startTime")),null);
			String endTime=Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("endTime")),null);
			endTime = changeEndTime(endTime);
			String title =Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(request().getParameter("title")),null);
			//中文乱码转换
			if(StringUtils.isNotBlank(title)){
				title = URLDecoder.decode(title,"UTF-8");
			}
			frontpayService.queryAllDetails(pageBean, userId, startTime, endTime,
					title);
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
			List<Map<String, Object>> list = pageBean.getPage();
			if (list == null) {
				list = new ArrayList<Map<String, Object>>();
			}
			if (list != null) {
				for (Map<String, Object> map : list) {		

					if (map.get("repayStatus").toString().equals(
							1+ "")) {
						map.put("repayStatus","未偿还");
					} else  {
						map.put("repayStatus","已偿还");
					}
				}
			}			
			
			HSSFWorkbook wb= ExcelHelper.exportExcel("还款明细账", pageBean.getPage(),
						new String[] { "标题", "第几期", "应还款日期", "实际还款日期", "本期应还本息(￥)", "利息(￥)",
						          "逾期罚款(￥)","逾期天数(天)","还款状态"
								 }, new String[] { "borrowTitle", "repayPeriod",
								"repayDate", "realRepayDate", "forPI",
								"stillInterest", "lateFI","lateDay","repayStatus" });						
			this.export(wb, new Date().getTime() + ".xls");
			
		} catch (Exception e) {
		
			e.printStackTrace();
		} 
		return null;
	}
	
	//检验验证码
	public String checkrRepayCode() throws Exception{
		JSONObject obj = new JSONObject();
		String code = (String) session().getAttribute("invest_checkCode");
		String _code = paramMap.get("code") == null ? "" : paramMap
				.get("code");
		if (!code.equals(_code)) {
			obj.put("msg", "验证码错误");
			JSONHelper.printObject(obj);
			return null;
		}
		obj.put("msg", "");
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * 借款明细账
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String queryBorrowInvestorInfo() throws Exception {
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		Long userId = user.getId();// 获得用户编号
		pageBean.setPageNum(request("curPage"));
		String investor = SqlInfusionHelper.filteSqlInfusion(request("investor"));

		frontpayService.queryBorrowInvestorInfo(pageBean, userId, investor);
		this.setRequestToParamMap();
		return SUCCESS;
	}
	
	/**
	 * 还款数据显示
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public String queryMyPayData() throws Exception{
		long payId = request("payId") == null?-1:Convert.strToLong(request("payId"), -1);
		Map<String,String> payMap = frontpayService.queryMyPayData(payId);
		request().setAttribute("payMap", payMap);
		return SUCCESS;
	}
	
	/**
	 * 提交还款记录
	 * @return
	 * @throws Exception 
	 */
	public String submitPay() throws Exception{
		User user = (User)session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String code = (String) session().getAttribute("invest_checkCode");
		String _code = paramMap.get("code") == null ? "" : paramMap
				.get("code");
		String id = paramMap.get("id") == null ? "" : paramMap
				.get("id");
		long idLong = Convert.strToLong(id, -1L);
		String pwd = paramMap.get("pwd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("pwd"));
		if (StringUtils.isBlank(pwd.trim())) {
			obj.put("msg", "密码不能为空");
			JSONHelper.printObject(obj);
			return null;
		}
		if (!code.equals(_code)) {
			obj.put("msg", "验证码错误");
			JSONHelper.printObject(obj);
			return null;
		}
		Map<String, String> map = frontpayService.submitPay(idLong,user.getId(),pwd,getBasePath(),user.getUserName());
		if(map == null){
			map = new HashMap<String, String>();
		}
		String result =Convert.strToStr( map.get("ret_desc"),"");
		obj.put("msg", result);
		JSONHelper.printObject(obj);
		return null;
	}

	public static String changeEndTime(String endTime) {
		if (endTime != null && !endTime.equals("")) {
			String[] strs = endTime.split("-");
			// 结束日期往后移一天,否则某天0点以后的数据都不呈现
			Date date = new Date();// 取时间
			long time = Date.UTC(Convert.strToInt(strs[0], -1) - 1900, Convert
					.strToInt(strs[1], -1) - 1, Convert.strToInt(strs[2], -1),
					0, 0, 0);
			date.setTime(time);
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(date);
			calendar.add(Calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			date = calendar.getTime();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(date);
		}
		return null;
	}
	
	public static Date changeStrToDate(String dateTime){
		if(dateTime != null){
			String[] strs = dateTime.split("-");
			int ind = strs[2].indexOf(" ");
			if(ind >= 0){
				strs[2] = strs[2].substring(0,ind+1);
			}
			Date date = new Date();// 取时间
			long time = date.UTC(Convert.strToInt(strs[0], -1) - 1900, Convert
					.strToInt(strs[1], -1) - 1, Convert.strToInt(strs[2].trim(), -1),
					0, 0, 0);
			date.setTime(time);
			return date;
		}
		return null;
	}
	
}
