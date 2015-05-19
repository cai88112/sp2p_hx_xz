package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.ips.security.utility.IpsCrypto;
import com.shove.Convert;
import com.shove.config.IPayConfig;
import com.shove.data.DataException;
import com.shove.web.action.IPaymentAction;
import com.shove.web.action.IPaymentConstants;
import com.shove.web.action.IPaymentUtil;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.DataApproveService;
import com.sp2p.service.FinanceService;
import com.sp2p.service.OperationLogService;
import com.sp2p.service.SelectedService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;
import com.sp2p.service.admin.ShoveBorrowTypeService;


/**
 * @ClassName: FrontMyFinanceAction.java
 * @Author: gang.lv
 * @Date: 2013-3-4 上午11:16:33
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 我的借款控制层
 */
public class BorrowManageAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(BorrowManageAction.class);
	private static final long serialVersionUID = 1L;

	private BorrowManageService borrowManageService;
	// ---add by houli
	private DataApproveService dataApproveService;
	// ---add by C_J
	private ShoveBorrowTypeService shoveBorrowTypeService;
	private FinanceService financeService;
	private SelectedService selectedService;
	private UserService userService;
	private FundManagementService fundManagementService;

	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	private Map<String, String> borrowMFADetail;
	private Map<String, String> borrowMTenderInDetail;
	private Map<String, String> borrowMFullScaleDetail;
	private Map<String, String> borrowMFlowMarkDetail;
	private Map<String, String> borrowMAllDetail;
	private Map<String, String> borrowCirculationDetail;
	private List<Map<String, Object>> cirList;
	private Object borrowId = "";

	// 下拉列表
	private List<Map<String, Object>> borrowPurposeList;
	private List<Map<String, Object>> borrowDeadlineList;
	private List<Map<String, Object>> borrowAmountList;
	private List<Map<String, Object>> borrowRaiseTermList;
	private List<Map<String, Object>> sysImageList;
	private List<Map<String, Object>> borrowTurnlineList;

	/**
	 * @MethodName: borrowManageFistAuditInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-11 上午09:54:00
	 * @Return:
	 * @Descb: 后台借款管理初审初始化
	 * @Throws:
	 */
	public String borrowManageFistAuditInit() throws SQLException,
			DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFistAuditInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午10:58:57
	 * @Return:
	 * @Descb: 后台借款管理初审列表
	 * @Throws:
	 */
	public String borrowManageFistAuditList() throws Exception {
		String pageNums = SqlInfusionHelper.filteSqlInfusion((String) (request().getParameter("curPage") == null ? ""
				: request().getParameter("curPage")));
		if (StringUtils.isNotBlank(pageNums)) {
			pageBean.setPageNum(pageNums);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowWay = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay"));
		int borrowWayInt = Convert.strToInt(borrowWay, -1);

		// 暂时性的修改 用于修改显示所有初审中的借款
		// 不用做判断处理~
		// borrowManageService.queryBorrowAllByCondition(userName, borrowWayInt,
		// 1,pageBean);
		// 做了判断的处理
		borrowManageService.queryBorrowFistAuditByCondition(userName,
				borrowWayInt, pageBean);

		Map<String, String> repaymentMap = borrowManageService
				.queryBorrowTotalFistAuditDetail();
		request().setAttribute("repaymentMap", repaymentMap);
		// 统计当前页应收款
		double fistAuditAmount = 0;
		List<Map<String, Object>> payList = pageBean.getPage();
		if (payList != null) {
			for (Map<String, Object> map : payList) {
				fistAuditAmount = fistAuditAmount
						+ Convert.strToDouble(map.get("borrowAmount") + "", 0);
			}
		}
		DecimalFormat fmt = new DecimalFormat("0.00");
		request().setAttribute("fistAuditAmount", fmt.format(fistAuditAmount));
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return "success";
	}

	public String borrowManageWaitingAuditList() throws Exception {
		String pageNums = SqlInfusionHelper.filteSqlInfusion((String) (request().getParameter("curPage") == null ? ""
				: request().getParameter("curPage")));
		if (StringUtils.isNotBlank(pageNums)) {
			pageBean.setPageNum(pageNums);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowWay = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay"));
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		borrowManageService.queryBorrowWaitingAuditByCondition(userName,
				borrowWayInt, pageBean);

		Map<String, String> waitTotalAmount = borrowManageService
				.queryBorrowTotalWait();
		request().setAttribute("waitTotalAmount", waitTotalAmount);
		// 统计当前页等待
		double waitingAuditAmount = 0;
		List<Map<String, Object>> payList = pageBean.getPage();
		if (payList != null) {
			for (Map<String, Object> map : payList) {
				waitingAuditAmount = waitingAuditAmount
						+ Convert.strToDouble(map.get("borrowAmount") + "", 0);
			}
		}
		DecimalFormat fmt = new DecimalFormat("0.00");
		request().setAttribute("waitingAuditAmount",
				fmt.format(waitingAuditAmount));
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFistAuditDetail
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午11:02:22
	 * @Return:
	 * @Descb: 后台借款管理中的借款详情
	 * @Throws:
	 */
	public String borrowManageFistAuditDetail() throws Exception {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id") == null ? "" : request()
				.getParameter("id"));
		long idLong = Convert.strToLong(id, -1);
		Map<String, String> TypeLogMap = null;
		if (borrowMFADetail == null) {
			// 初审中的借款详情
			borrowMFADetail = borrowManageService
					.queryBorrowFistAuditDetailById(idLong);
			String nid_log = SqlInfusionHelper.filteSqlInfusion(borrowMFADetail.get("nid_log"));
			if (StringUtils.isNotBlank(nid_log)) {
				TypeLogMap = shoveBorrowTypeService
						.queryBorrowTypeLogByNid(nid_log.trim());
				int stauts = Convert.strToInt(TypeLogMap
						.get("subscribe_status"), -1);
				request().setAttribute("subscribes", stauts);

			}
		}
		return "success";
	}

	/**
	 * @throws DataException
	 * @throws IOException
	 * @throws Exception 
	 * @MethodName: updateBorrowF
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午03:58:28
	 * @Return:
	 * @Descb: 审核借款中的初审记录
	 * @Throws:
	 */
	public String updateBorrowF() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		String reciver = SqlInfusionHelper.filteSqlInfusion(paramMap.get("reciver"));
		long reciverLong = Convert.strToLong(reciver, -1);
		String status = SqlInfusionHelper.filteSqlInfusion(paramMap.get("status"));
		int statusLong = Convert.strToInt(status, -1);
//		String isatuo = SqlInfusion.FilteSqlInfusion(paramMap.get("isauto"));
//		int isatuoLong = Convert.strToInt(isatuo, -1);
//		String autoTime = SqlInfusion.FilteSqlInfusion(paramMap.get("autoTime"));
//		autoTime = Convert.strToStr(autoTime, "");
		String msg = SqlInfusionHelper.filteSqlInfusion(paramMap.get("msg"));
		String auditOpinion = SqlInfusionHelper.filteSqlInfusion(paramMap.get("auditOpinion"));
		long result = -1;

		try {
			if (statusLong == IConstants.BORROW_STATUS_2) {
				result = borrowManageService.updateBorrowFistAuditStatus(idLong,
						reciverLong, statusLong, msg, auditOpinion, admin.getId(),
						getBasePath());
			} else {
				//解冻保证金
				Map<String, String> data = guaranteeUnfreeze(idLong);
				if (data != null && data.size() > 0) {
					String pErrCode = data.get("pErrCode");
					if ("-100".equals(pErrCode)) {
						// 撤消初审中的借款                       
						result = borrowManageService.reBackBorrowFistAudit(idLong, admin.getId(), getBasePath(), msg,
								auditOpinion, null);
					} else {
						String pMerCode = data.get("pMerCode");
						String pErrMsg = data.get("pErrMsg");
				
						String p3DesXmlPara = data.get("p3DesXmlPara");
						Map<String, String> parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
						String pMerBillNo = parseXml.get("parseXml");
						String pSign = data.get("pSign");// 签名
						boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
						if(!sign){
							obj.put("msg", "签名失败");
							JSONHelper.printObject(obj);
							return null;
						}else{
							Map<String,String> fundMap = fundManagementService.queryFundRecord(pMerBillNo);
							if(fundMap == null){
								if (!pErrCode.equals("0000")) {
									// 操作失败
									obj.put("msg", pErrMsg);
									JSONHelper.printObject(obj);
									return null;
								} else {
									// 撤消初审中的借款                       
									result = borrowManageService.reBackBorrowFistAudit(idLong, admin.getId(), getBasePath(), msg,
											auditOpinion, pMerBillNo);
								}
							}
						}
					}
				} else {
					//环迅输出参数为null，跳转到环迅维护页面
					obj.put("msg", "环迅系统维护中！");
					JSONHelper.printObject(obj);
					return null;
				}
			}
			
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		if (result <= 0) {
			// 操作失败提示
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
		}
		// 前台跳转地址
		obj.put("msg", "1");
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * @MethodName: borrowManageTenderInInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:01
	 * @Return:
	 * @Descb: 后台借款管理招标中初始化
	 * @Throws:
	 */
	public String borrowManageTenderInInit() throws SQLException, DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageTenderInList
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:32
	 * @Return:
	 * @Descb: 后台借款招标中的记录
	 * @Throws:
	 */
	public String borrowManageTenderInList() throws Exception {
		String pageNums = SqlInfusionHelper.filteSqlInfusion((String) (request().getParameter("curPage") == null ? ""
				: request().getParameter("curPage")));
		if (StringUtils.isNotBlank(pageNums)) {
			pageBean.setPageNum(pageNums);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowWay = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay"));
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		borrowManageService.queryBorrowTenderInByCondition(userName,
				borrowWayInt, pageBean);

		Map<String, String> repaymentMap = borrowManageService
				.queryBorrowTotalTenderDetail();
		request().setAttribute("repaymentMap", repaymentMap);
		// 统计当前页等待
		double tenderAmount = 0;
		List<Map<String, Object>> payList = pageBean.getPage();
		if (payList != null) {
			for (Map<String, Object> map : payList) {
				tenderAmount = tenderAmount
						+ Convert.strToDouble(map.get("borrowAmount") + "", 0);
			}
		}
		DecimalFormat fmt = new DecimalFormat("0.00");
		request().setAttribute("tenderAmount", fmt.format(tenderAmount));
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFistAuditDetail
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午11:02:22
	 * @Return:
	 * @Descb: 后台借款管理招标中的借款详情
	 * @Throws:
	 */
	public String borrowManageTenderInDetail() throws Exception {
		return "success";
	}

	/**
	 * @throws Exception 
	 * @throws Exception
	 * @MethodName: updateBorrowF
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午03:58:28
	 * @Return:
	 * @Descb: 审核借款中的招标中记录
	 * @Throws:
	 */
	public String updateBorrowTenderIn() throws Exception  {
		JSONObject obj = new JSONObject();
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		String auditOpinion = SqlInfusionHelper.filteSqlInfusion(paramMap.get("auditOpinion"));
		long result = -1;
		result = borrowManageService.updateBorrowTenderInStatus(idLong,
				auditOpinion);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result <= 0) {
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			operationLogService.addOperationLog("t_borrow",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "审核借款中的招标中记录，操作失败", 2);
			return null;
		}
		// 前台跳转地址
		obj.put("msg", "1");
		JSONHelper.printObject(obj);
		operationLogService.addOperationLog("t_borrow", admin.getUserName(),
				IConstants.UPDATE, admin.getLastIP(), 0, "审核借款中的招标中记录，操作成功", 2);
		return null;		
	}
	
	/**
	 * 描述: 撤销初审中的借款
	 * 时间: 2014-1-22  下午03:43:57
	 * 返回值类型: String
	 * @return
	 * @throws Exception
	 */
	public String reBackBorrowFistAudit() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		long result = -1;
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id") == null ? "" : paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		//解冻保证金
		Map<String, String> data = guaranteeUnfreeze(idLong);
		if (data != null && data.size() > 0) {
			String pErrCode = data.get("pErrCode");
			if ("-100".equals(pErrCode)) {
				// 调用撤消服务
				result = borrowManageService.reBackBorrowFistAudit(idLong, admin
						.getId(), getBasePath(), "", "", null);

				if (result <= 0) {
					// 操作失败
					obj.put("msg", IConstants.ACTION_FAILURE);
					JSONHelper.printObject(obj);
					return null;
				}
			} else {
				String pMerCode = data.get("pMerCode");
				String pErrMsg = data.get("pErrMsg");
		
				String p3DesXmlPara = data.get("p3DesXmlPara");
				Map<String, String> parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
				String pMerBillNo = parseXml.get("parseXml");
				String pSign = data.get("pSign");// 签名
				boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
				if(!sign){
					obj.put("msg", "签名失败");
					JSONHelper.printObject(obj);
					return null;
				}else{
					Map<String,String> fundMap = fundManagementService.queryFundRecord(pMerBillNo);
					if(fundMap == null){
						if (!pErrCode.equals("0000")) {
							// 操作失败
							obj.put("msg", pErrMsg);
							JSONHelper.printObject(obj);
							return null;
						} else {
							// 调用撤消服务
							result = borrowManageService.reBackBorrowFistAudit(idLong, admin
									.getId(), getBasePath(), "", "", pMerBillNo);

							if (result <= 0) {
								// 操作失败
								obj.put("msg", IConstants.ACTION_FAILURE);
								JSONHelper.printObject(obj);
								return null;
							}
						}
					}
				}
			}
		} else {
			//环迅输出参数为null，跳转到环迅维护页面
		}
		
		// 操作成功
		obj.put("msg", "1");
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * @throws DataException
	 * @throws SQLException
	 * @throws IOException
	 * @MethodName: reBackBorrowTenderIn
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 下午04:00:42
	 * @Return:
	 * @Descb: 撤消招标中的借款
	 * @Throws:
	 */
	public String reBackBorrowTenderIn() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		long result = -1;
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id") == null ? "" : paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		String tradeNo = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tradeNo"));
		double hasInvestAmount = Convert.strToDouble(SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasInvestAmount") == null ? "" : paramMap.get("hasInvestAmount")), 0.00);
		Map<String, String> data = null;
		
		//解冻借款人冻结保证金
		data = guaranteeUnfreeze(idLong);
		if (data != null && data.size() > 0) {
			String pErrCode = data.get("pErrCode");
			if ("-100".equals(pErrCode)) {
				if (hasInvestAmount > 0) {
					IPaymentAction ips = new IPaymentAction();
					//解冻投资人投资金额
					data = ips.checkTrade(tradeNo, "6", tradeNo);
					if (data != null && data.size() > 0) {
						pErrCode = data.get("pErrCode");
						String pMerCode = data.get("pMerCode");
						String pErrMsg = data.get("pErrMsg");

						String p3DesXmlPara = data.get("p3DesXmlPara");
						Map<String, String> parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
						String pMerBillNo = parseXml.get("parseXml");
						String pSign = data.get("pSign");// 签名
						boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
						if(!sign){
							obj.put("msg", "签名失败");
							JSONHelper.printObject(obj);
							return null;
						}else{
							Map<String, String>fundMap = fundManagementService.queryFundRecord(pMerBillNo);
							if(fundMap == null){
								if (!pErrCode.equals("0000")) {
									// 操作失败
									obj.put("msg", pErrMsg);
									JSONHelper.printObject(obj);
									return null;
								} else {
									// 调用撤消服务
									result = borrowManageService.reBackBorrow(idLong, admin.getId(),
											getBasePath(),pMerBillNo);
								}
							}
						}
					}
				} else {
					// 调用撤消服务
					result = borrowManageService.reBackBorrow(idLong, admin.getId(),
							getBasePath(),null);
				}
				if (result < 0) {
					// 操作失败
					obj.put("msg", IConstants.ACTION_FAILURE);
					JSONHelper.printObject(obj);
					return null;
				}
			} else {
				String pMerCode = data.get("pMerCode");
				String pErrMsg = data.get("pErrMsg");

				String p3DesXmlPara = data.get("p3DesXmlPara");
				Map<String, String> parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
				String pMerBillNo = parseXml.get("parseXml");
				String pSign = data.get("pSign");// 签名
				boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
				if(!sign){
					obj.put("msg", "签名失败");
					JSONHelper.printObject(obj);
					return null;
				}else{
					Map<String,String> fundMap = fundManagementService.queryFundRecord(pMerBillNo);
					if(fundMap == null){
						if (!pErrCode.equals("0000")) {
							// 操作失败
							obj.put("msg", pErrMsg);
							JSONHelper.printObject(obj);
							return null;
						} else {
							if (hasInvestAmount > 0) {
								IPaymentAction ips = new IPaymentAction();
								//解冻投资人投资金额
								data = ips.checkTrade(tradeNo, "6", tradeNo);
								if (data != null && data.size() > 0) {
									pErrCode = data.get("pErrCode");
									pMerCode = data.get("pMerCode");
									pErrMsg = data.get("pErrMsg");

									p3DesXmlPara = data.get("p3DesXmlPara");
									parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
									pMerBillNo = parseXml.get("parseXml");
									pSign = data.get("pSign");// 签名
									sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
									if(!sign){
										obj.put("msg", "签名失败");
										JSONHelper.printObject(obj);
										return null;
									}else{
										fundMap = fundManagementService.queryFundRecord(pMerBillNo);
										if(fundMap == null){
											if (!pErrCode.equals("0000")) {
												// 操作失败
												obj.put("msg", pErrMsg);
												JSONHelper.printObject(obj);
												return null;
											} else {
												// 调用撤消服务
												result = borrowManageService.reBackBorrow(idLong, admin.getId(),
														getBasePath(),pMerBillNo);
											}
										}
									}
								}
							} else {
								// 调用撤消服务
								result = borrowManageService.reBackBorrow(idLong, admin.getId(),
										getBasePath(),pMerBillNo);
							}
							if (result < 0) {
								// 操作失败
								obj.put("msg", IConstants.ACTION_FAILURE);
								JSONHelper.printObject(obj);
								return null;
							}
						}
					}
				}
			}
		} else {
			//环迅输出参数为null，跳转到环迅维护页面
			obj.put("msg", "环迅系统维护中！");
			JSONHelper.printObject(obj);
			return null;
		}
		
		// 操作成功
		obj.put("msg", "1");
		JSONHelper.printObject(obj);
		return null;
	}
	
	/**
	 * 描述: 解冻保证金
	 * 时间: 2014-1-22  下午03:47:08
	 * 返回值类型: Map<String,String>
	 * @param idLong
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> guaranteeUnfreeze(long idLong) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		Map<String, String> borrowDetailMap = borrowManageService.queryBorrowInfo(idLong);

		String tradeNo = "";
		String pFreezeMerBillNo = "";
		String pIdentNo = "";
		String pRealName = "";
		String pIpsAcctNo = "";
		String pUnfreezeAmt = "";
		if(borrowDetailMap != null && borrowDetailMap.size() > 0) {
			tradeNo = borrowDetailMap.get("tradeNo");
			pFreezeMerBillNo =borrowDetailMap.get("guaranteeNo");
			pIdentNo = borrowDetailMap.get("idNo");
			pRealName = borrowDetailMap.get("realName");
			pIpsAcctNo = borrowDetailMap.get("portMerBillNo");
			double frozen_margin = Convert.strToDouble(borrowDetailMap.get("frozen_margin"), 0.00);
			String borrowWay = borrowDetailMap.get("borrowWay");
			if ("2".equals(borrowWay)) {
				Map<String, Object> platformCostMap = getPlatformCost();
				double costFee = Convert.strToDouble(platformCostMap
						.get(IAmountConstants.BORROW_FEE_RATE_1)
						+ "", 0);
				double annualRate = Convert.strToDouble(borrowDetailMap.get("annualRate"), 0.00);
				double borrowAmount = Convert.strToDouble(borrowDetailMap.get("borrowAmount"), 0.00);
				// 秒还借款冻结借款+借款利息+借款手续费     + 冻结保证金
				double fee = borrowAmount * ((annualRate*0.01)/12.0) + (borrowAmount * costFee);
				frozen_margin = frozen_margin + fee;
			}
			pUnfreezeAmt = String.format("%.2f", frozen_margin);
		}
		if ("".equals(pUnfreezeAmt) || "0.00".equals(pUnfreezeAmt)) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("pErrCode", "-100");
			return data;
		}
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", tradeNo);
		map.put("pFreezeMerBillNo", pFreezeMerBillNo);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pUnfreezeDate", DateFormatUtils.format(new Date(), "yyyyMMdd"));
		map.put("pUnfreezeUserType", "1");
		map.put("pAcctType", "1");
		map.put("pIdentNo", pIdentNo);
		map.put("pRealName", pRealName);
		map.put("pIpsAcctNo", pIpsAcctNo);
		map.put("pUnfreezeAmt", pUnfreezeAmt);
		map.put("pS2SUrl", IPaymentConstants.url+"/reGuaranteeUnfreezeUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		String str3DesXmlPana = IPaymentUtil.parseMapToXml(map);
		str3DesXmlPana = IpsCrypto.triDesEncrypt(str3DesXmlPana, IPayConfig.des_key, IPayConfig.des_iv);
		str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "");
		str3DesXmlPana = str3DesXmlPana.replaceAll("\n", "");
		String strSign = IpsCrypto.md5Sign(IPayConfig.terraceNoOne + str3DesXmlPana + IPayConfig.cert_md5);
		String soap = IPaymentUtil.getSoapAuditTender(str3DesXmlPana, IPaymentConstants.guaranteeUnfreeze, "argMerCode", "arg3DesXmlPara", "argSign", strSign);
		log.info("********"+soap);
		String url = IPayConfig.ipay_web_gateway + IPaymentConstants.guaranteeUnfreeze;
		Map<String, String> data = IPaymentUtil.webService(soap, url);	
		return data;
	}

	/**
	 * @MethodName: borrowManageFullScaleInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:01
	 * @Return:
	 * @Descb: 后台借款管理满标初始化
	 * @Throws:
	 */
	public String borrowManageFullScaleInit() throws SQLException,
			DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFullScaleList
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:32
	 * @Return:
	 * @Descb: 后台借款满标的记录
	 * @Throws:
	 */
	public String borrowManageFullScaleList() throws Exception {
		String pageNums = SqlInfusionHelper.filteSqlInfusion((String) (request().getParameter("curPage") == null ? ""
				: request().getParameter("curPage")));
		if (StringUtils.isNotBlank(pageNums)) {
			pageBean.setPageNum(pageNums);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowWay = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay"));
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		borrowManageService.queryBorrowFullScaleByCondition(userName,
				borrowWayInt, pageBean);

		Map<String, String> repaymentMap = borrowManageService
				.queryBorrowTotalFullScaleDetail();
		request().setAttribute("repaymentMap", repaymentMap);
		// 统计当前页应收款
		double fullScaleAmount = 0;
		List<Map<String, Object>> payList = pageBean.getPage();
		if (payList != null) {
			for (Map<String, Object> map : payList) {
				fullScaleAmount = fullScaleAmount
						+ Convert.strToDouble(map.get("borrowAmount") + "", 0);
			}
		}
		DecimalFormat fmt = new DecimalFormat("0.00");
		request().setAttribute("fistAuditAmount", fmt.format(fullScaleAmount));
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFistAuditDetail
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午11:02:22
	 * @Return:
	 * @Descb: 后台借款管理满标的借款详情
	 * @Throws:
	 */
	public String borrowManageFullScaleDetail() throws Exception {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: updateBorrowF
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午03:58:28
	 * @Return:
	 * @Descb: 审核借款中的满标记录
	 * @Throws:
	 */
	public String updateBorrowFullScale() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		//借款id
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		//4：复审通过   6：复审不通过
		String status = SqlInfusionHelper.filteSqlInfusion(paramMap.get("status"));
		long statusLong = Convert.strToLong(status, -1);
		
		//风控意见
		String auditOpinion = SqlInfusionHelper.filteSqlInfusion(paramMap.get("auditOpinion"));
		String tradeNo = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tradeNo"));
		
		//解冻保证金
		Map<String, String> data = guaranteeUnfreeze(idLong);
		if (data != null && data.size() > 0) {
			String pErrCode = data.get("pErrCode");
			if ("-100".equals(pErrCode)) {
				Map<String, String> retMap = borrowManageService
				.updateBorrowFullScaleStatus(idLong, statusLong, auditOpinion,
						admin.getId(), getBasePath(), tradeNo);
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
			} else {
				String pMerCode = data.get("pMerCode");
				String pErrMsg = data.get("pErrMsg");
		
				String p3DesXmlPara = data.get("p3DesXmlPara");
				Map<String, String> parseXml = IPaymentUtil.parseXmlToJson(p3DesXmlPara);
				String pMerBillNo = parseXml.get("parseXml");
				String pSign = data.get("pSign");// 签名
				boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg, p3DesXmlPara, pSign);
				if(!sign){
					obj.put("msg", "签名失败");
					JSONHelper.printObject(obj);
					return null;
				}else{
					Map<String,String> fundMap = fundManagementService.queryFundRecord(pMerBillNo);
					if(fundMap == null){
						if (!pErrCode.equals("0000")) {
							// 操作失败
							obj.put("msg", pErrMsg);
							JSONHelper.printObject(obj);
							return null;
						} else {
							Map<String, String> retMap = borrowManageService
							.updateBorrowFullScaleStatus(idLong, statusLong, auditOpinion,
									admin.getId(), getBasePath(), tradeNo);
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
				}
			}
		} else {
			//环迅输出参数为null，跳转到环迅维护页面
			obj.put("msg", "环迅系统维护中！");
			JSONHelper.printObject(obj);
			return null;
		}
		
		Map<String, String> retMap = borrowManageService
				.updateBorrowFullScaleStatus(idLong, statusLong, auditOpinion,
						admin.getId(), getBasePath(), tradeNo);
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
	
	/**
	 * @MethodName: borrowManageFlowMarkInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:01
	 * @Return:
	 * @Descb: 后台借款管理流标初始化
	 * @Throws:
	 */
	public String borrowManageFlowMarkInit() throws SQLException, DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFlowMarkList
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:32
	 * @Return:
	 * @Descb: 后台借款流标的记录
	 * @Throws:
	 */
	public String borrowManageFlowMarkList() throws Exception {
		String pageNums = SqlInfusionHelper.filteSqlInfusion((String) (request().getParameter("curPage") == null ? ""
				: request().getParameter("curPage")));
		if (StringUtils.isNotBlank(pageNums)) {
			pageBean.setPageNum(pageNums);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowWay = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay"));
		String timeStart = paramMap.get("timeStart") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeStart"));
		String timeEnd = paramMap.get("timeEnd") == null ? "" : SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("timeEnd"));
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		borrowManageService.queryBorrowFlowMarkByCondition(userName,
				borrowWayInt, timeStart, timeEnd, pageBean);
		Map<String, String> repaymentMap = borrowManageService
				.queryBorrowFlowMarkDetail();
		request().setAttribute("repaymentMap", repaymentMap);
		// 统计当前页应收款
		double flowmarkAmount = 0;
		List<Map<String, Object>> payList = pageBean.getPage();
		if (payList != null) {
			for (Map<String, Object> map : payList) {
				flowmarkAmount = flowmarkAmount
						+ Convert.strToDouble(map.get("borrowAmount") + "", 0);
			}
		}
		DecimalFormat fmt = new DecimalFormat("0.00");
		request().setAttribute("flowmarkAmount", fmt.format(flowmarkAmount));
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFistAuditDetail
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午11:02:22
	 * @Return:
	 * @Descb: 后台借款管理流标的借款详情
	 * @Throws:
	 */
	public String borrowManageFlowMarkDetail() throws Exception {
		return "success";
	}

	/**
	 * @MethodName: borrowManageAllInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:01
	 * @Return:
	 * @Descb: 后台借款管理初始化
	 * @Throws:
	 */
	public String borrowManageAllInit() throws SQLException, DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageAllList
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午11:36:32
	 * @Return:
	 * @Descb: 后台借款的记录
	 * @Throws:
	 */
	public String borrowManageAllList() throws Exception {
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowStatus = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowStatus") == null ? ""
				: paramMap.get("borrowStatus"));
		int borrowStatusInt = Convert.strToInt(borrowStatus, -1);
		String borrowWay = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowWay") == null ? "" : paramMap
				.get("borrowWay"));
		int borrowWayInt = Convert.strToInt(borrowWay, -1);
		borrowManageService.queryBorrowAllByCondition(userName, borrowWayInt,
				borrowStatusInt, pageBean);
		// ----add by houli 对等待资料的借款进行标记
		List<Map<String, Object>> lists = borrowManageService
				.queryAllWaitingBorrow();
		Vector<String> ids = new Vector<String>();
		if (lists != null && lists.size() > 0) {
			for (Map<String, Object> map : lists) {
				ids.add(map.get("id")+"");
			}
		}
		List<Map<String, Object>> lls = pageBean.getPage();
		if (lls != null && lls.size() > 0) {
			for (Map<String, Object> map : lls) {
				if (ids.contains(map.get("id").toString())) {
					map.put("flag", "0");
				} else {
					map.put("flag", "1");
				}
			}
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return "success";
	}

	// 校验提交借款参数
	@SuppressWarnings("unchecked")
	public boolean isValidate(double amountDouble, String excitationType,
			double sumRateDouble, double annualRateDouble) throws Exception {
		String t = SqlInfusionHelper.filteSqlInfusion((String) session().getAttribute("t"));
		// 获取借款的范围
		Map<String, String> tempBorrwBidMessage = new HashMap<String, String>();
		tempBorrwBidMessage = shoveBorrowTypeService
				.queryShoveBorrowTypeByNid(IConstants.BORROW_TYPE_FLOW);
		// 取得按借款金额的比例进行奖励
		double accountfirst = Convert.strToDouble(tempBorrwBidMessage
				.get("award_account_first")
				+ "", 0);
		double accountend = Convert.strToDouble(tempBorrwBidMessage
				.get("award_account_end")
				+ "", 0);
		if (StringUtils.isNotBlank(excitationType)) {
			// 按借款金额比例奖励
			if (StringUtils.isNumericSpace(excitationType)
					&& "2".equals(excitationType)) {
				if (sumRateDouble < accountfirst || sumRateDouble > accountend) {
					this.addFieldError("paramMap['sum']", "固定总额奖励填写不正确");
					return false;
				}
			}
		}
		// 如果选择金额的话，则按此奖励的金额范围
		double scalefirst = Convert.strToDouble(tempBorrwBidMessage
				.get("award_scale_first")
				+ "", 0);
		double scaleend = Convert.strToDouble(tempBorrwBidMessage
				.get("award_scale_end")
				+ "", 0);
		if (StringUtils.isNotBlank(excitationType)) {
			// 按借款金额比例奖励
			if (StringUtils.isNumericSpace(excitationType)
					&& "3".equals(excitationType)) {
				if (sumRateDouble < scalefirst || sumRateDouble > scaleend) {
					this.addFieldError("paramMap['sumRate']", "奖励比例填写不正确");
					return false;
				}
			}
		}
		// 借款额度
		double borrowMoneyfirst = Convert.strToDouble(tempBorrwBidMessage
				.get("amount_first")
				+ "", 0);
		double borrowMoneyend = Convert.strToDouble(tempBorrwBidMessage
				.get("amount_end")
				+ "", 0);
		if (borrowMoneyfirst > amountDouble || borrowMoneyend < amountDouble) {
			this.addFieldError("paramMap['amount']", "输入的借款总额不正确");
			return false;
		}
		// 借款额度倍数
		double accountmultiple = Convert.strToDouble(tempBorrwBidMessage
				.get("account_multiple")
				+ "", -1);
		if (accountmultiple != 0) {
			if (amountDouble % accountmultiple != 0) {
				this.addFieldError("paramMap['amount']", "输入的借款总额的倍数不正确");
				return false;
			}
		}
		// 年利率
		double aprfirst = Convert.strToDouble(tempBorrwBidMessage
				.get("apr_first")
				+ "", 0);
		double aprend = Convert.strToDouble(tempBorrwBidMessage.get("apr_end")
				+ "", 0);
		if (aprfirst > annualRateDouble || aprend < annualRateDouble) {
			this.addFieldError("paramMap['annualRate']", "输入的年利率不正确");
			return false;
		}
		return true;
	}

	/**
	 * @MethodName: circulationBorrowInit
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 上午11:33:18
	 * @Return:
	 * @Descb: 流转标借款初始化
	 * @Throws:
	 */
	public String circulationBorrowInit() throws SQLException, DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: circulationBorrowList
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 上午11:35:50
	 * @Return:
	 * @Descb: 流转标借款
	 * @Throws:
	 */
	public String circulationBorrowList() throws Exception {
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String undertaker = SqlInfusionHelper.filteSqlInfusion(paramMap.get("undertaker") == null ? "" : paramMap
				.get("undertaker"));
		String borrowStatus = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowStatus") == null ? ""
				: paramMap.get("borrowStatus"));
		int borrowStatusInt = Convert.strToInt(borrowStatus, -1);
		borrowManageService.queryAllCirculationByCondition(userName, -1,
				borrowStatusInt, undertaker, pageBean);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowCirculationDetail
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 下午01:44:03
	 * @Return:
	 * @Descb: 流转标借款详情
	 * @Throws:
	 */
	public String borrowCirculationDetail() throws Exception {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id") == null ? "" : request()
				.getParameter("id"));
		long idLong = Convert.strToLong(id, -1);
		if (borrowCirculationDetail == null) {
			// 初审中的借款详情
			borrowCirculationDetail = borrowManageService
					.queryBorrowCirculationDetailById(idLong);
		}
		return "success";
	}

	/**
	 * @MethodName: updateBorrowCirculation
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 下午03:23:42
	 * @Return:
	 * @Descb:
	 * @Throws:
	 */
	public String updateBorrowCirculation() throws IOException {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long borrowId = Convert.strToLong(id, -1);
		String reciver = SqlInfusionHelper.filteSqlInfusion(paramMap.get("reciver"));
		long reciverId = Convert.strToLong(reciver, -1);
		String status = SqlInfusionHelper.filteSqlInfusion(paramMap.get("status"));
		long statusLong = Convert.strToLong(status, -1);
		String auditOpinion = SqlInfusionHelper.filteSqlInfusion(paramMap.get("auditOpinion"));
		long result = -1;
		if (statusLong == -1) {
			obj.put("msg", "请选择审核状态");
			JSONHelper.printObject(obj);
			return null;
		}
		if (!StringUtils.isNotBlank(auditOpinion)) {
			obj.put("msg", "请填写风险控制措施");
			JSONHelper.printObject(obj);
			return null;
		} else if (auditOpinion.length() > 500) {
			obj.put("msg", "风险控制措施内容不能超过500字符");
			JSONHelper.printObject(obj);
			return null;
		}
		try {
			result = borrowManageService.updateBorrowCirculationStatus(
					borrowId, reciverId, statusLong, auditOpinion, admin
							.getId(), getBasePath(), getPlatformCost());
			operationLogService.addOperationLog("t_borrow",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "更新流转标的状态", 2);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		if (result <= 0) {
			// 操作失败提示
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
		}
		// 前台跳转地址
		obj.put("msg", "1");
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * @throws Exception
	 * @MethodName: borrowManageFistAuditDetail
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-10 下午11:02:22
	 * @Return:
	 * @Descb: 后台借款管理的借款详情
	 * @Throws:
	 */
	public String borrowManageAllDetail() throws Exception {
		return "success";
	}

	public BorrowManageService getBorrowManageService() {
		return borrowManageService;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public Map<String, String> getBorrowMFADetail() throws SQLException,
			DataException {
		return borrowMFADetail;
	}

	public Map<String, String> getBorrowMTenderInDetail() throws Exception {
		String id = request().getParameter("id") == null ? "" : request()
				.getParameter("id");
		long idLong = Convert.strToLong(id, -1);
		Map<String, String> TypeLogMap = null;
		if (borrowMTenderInDetail == null) {
			// 招标中的借款详情
			borrowMTenderInDetail = borrowManageService
					.queryBorrowTenderInDetailById(idLong);
			if(borrowMFlowMarkDetail == null){
				borrowMFlowMarkDetail = new HashMap<String, String>();
			}
			String nid_log = SqlInfusionHelper.filteSqlInfusion(borrowMTenderInDetail.get("nid_log"));
			if (StringUtils.isNotBlank(nid_log)) {
				TypeLogMap = shoveBorrowTypeService
						.queryBorrowTypeLogByNid(nid_log.trim());
				if(TypeLogMap == null){
					TypeLogMap = new HashMap<String, String>();
				}
				int stauts = Convert.strToInt(TypeLogMap
						.get("subscribe_status"), -1);
				request().setAttribute("subscribes", stauts);
			}
		}
		// ---add by houli 屏蔽链接
		String mailContent = SqlInfusionHelper.filteSqlInfusion(borrowMTenderInDetail.get("mailContent"));
		String newStr = SqlInfusionHelper.filteSqlInfusion(changeStr2Str(mailContent));
		borrowMTenderInDetail.put("mailContent", newStr);
		// ---------end
		return borrowMTenderInDetail;
	}

	public Map<String, String> getBorrowMFullScaleDetail() throws Exception {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id") == null ? "" : request()
				.getParameter("id"));
		long idLong = Convert.strToLong(id, -1);
		Map<String, String> TypeLogMap = null;
		if (borrowMFullScaleDetail == null) {
			// 满标的借款详情
			borrowMFullScaleDetail = borrowManageService
					.queryBorrowFullScaleDetailById(idLong);
			if(borrowMFlowMarkDetail == null){
				borrowMFlowMarkDetail = new HashMap<String, String>();
			}
			String nid_log = SqlInfusionHelper.filteSqlInfusion(borrowMFullScaleDetail.get("nid_log"));
			if (StringUtils.isNotBlank(nid_log)) {
				TypeLogMap = shoveBorrowTypeService
						.queryBorrowTypeLogByNid(nid_log.trim());
				if(TypeLogMap == null){
					TypeLogMap = new HashMap<String, String>();
				}
				int stauts = Convert.strToInt(TypeLogMap
						.get("subscribe_status"), -1);
				request().setAttribute("subscribes", stauts);
			}
		}
		// ---add by houli 屏蔽链接
		String mailContent = SqlInfusionHelper.filteSqlInfusion(borrowMFullScaleDetail.get("mailContent"));
		String newStr = SqlInfusionHelper.filteSqlInfusion(changeStr2Str(mailContent));
		borrowMFullScaleDetail.put("mailContent", newStr);
		// ---------end
		return borrowMFullScaleDetail;
	}

	public Map<String, String> getBorrowMFlowMarkDetail() throws Exception {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id") == null ? "" : request()
				.getParameter("id"));
		long idLong = Convert.strToLong(id, -1);
		Map<String, String> TypeLogMap = null;
		if (borrowMFlowMarkDetail == null) {
			// 流标的借款详情
			borrowMFlowMarkDetail = borrowManageService
					.queryBorrowFlowMarkDetailById(idLong);
			if(borrowMFlowMarkDetail == null){
				borrowMFlowMarkDetail = new HashMap<String, String>();
			}
			String nid_log = SqlInfusionHelper.filteSqlInfusion(borrowMFlowMarkDetail.get("nid_log"));
			if (StringUtils.isNotBlank(nid_log)) {
				TypeLogMap = shoveBorrowTypeService
						.queryBorrowTypeLogByNid(nid_log.trim());
				if(TypeLogMap == null){
					TypeLogMap = new HashMap<String, String>();
				}
				int stauts = Convert.strToInt(TypeLogMap
						.get("subscribe_status"), -1);
				request().setAttribute("subscribes", stauts);
			}
		}
		return borrowMFlowMarkDetail;
	}

	public String circulationRepayRecordInit() throws SQLException,
			DataException {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: circulationBorrowList
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 上午11:35:50
	 * @Return:
	 * @Descb: 流转标借款
	 * @Throws:
	 */
	public String circulationRepayRecordList() throws Exception {
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName") == null ? "" : paramMap
				.get("userName"));
		String borrowTitle = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowTitle") == null ? ""
				: paramMap.get("borrowTitle"));
		String borrowStatus = SqlInfusionHelper.filteSqlInfusion(paramMap.get("borrowStatus") == null ? ""
				: paramMap.get("borrowStatus"));
		int borrowStatusInt = Convert.strToInt(borrowStatus, -1);
		borrowManageService.queryAllCirculationRepayRecordByCondition(userName,
				borrowStatusInt, borrowTitle, pageBean);
		return "success";
	}

	/**
	 * @MethodName: circulationRepayForAdd
	 * @Param: BorrowManageAction
	 * @Return:
	 * @Descb: 流转标还款详情添加初始化
	 * @Throws:
	 */
	public String circulationRepayForAdd() {
		return "success";
	}

	/**
	 * @throws Exception
	 * @MethodName: circulationRepayAdd
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-7-23 下午03:48:45
	 * @Return:
	 * @Descb: 添加流转标详情
	 * @Throws:
	 */
	public String circulationRepayAdd() throws Exception {
		Admin admin = (Admin) session().getAttribute("admin");
		String amount = SqlInfusionHelper.filteSqlInfusion(paramMap.get("amount"));
		double amountDouble = Convert.strToDouble(amount, -1);
		String remark = SqlInfusionHelper.filteSqlInfusion(paramMap.get("remark"));
		Object id = session().getAttribute("repayId");
		long repayId = Convert.strToLong(id + "", -1);
		if (repayId == -1) {
			this.addFieldError("paramMap['action']", "操作失败");
			return "input";
		}
		if (amountDouble == -1 || amountDouble <= 0) {
			this.addFieldError("paramMap['amount']", "金额格式错误");
			return "input";
		}
		if (remark.length() > 500) {
			this.addFieldError("paramMap['remark']", "备注不能超过500字符");
			return "input";
		}
		long returnId = -1;
		returnId = borrowManageService.addCirculationRepay(repayId,
				amountDouble, admin.getId(), remark);
		operationLogService.addOperationLog("t_borrow", admin.getUserName(),
				IConstants.INSERT, admin.getLastIP(), 0, "添加流转标还款记录", 2);

		if (returnId < 1) {
			this.addFieldError("paramMap['action']", "操作失败");
			return "input";
		}
		borrowId = session().getAttribute("borrowId");
		;
		return "success";
	}

	public Map<String, String> getBorrowMAllDetail() throws Exception {
		String id = SqlInfusionHelper.filteSqlInfusion(request().getParameter("id") == null ? "" : request()
				.getParameter("id"));
		long idLong = Convert.strToLong(id, -1);
		if (borrowMAllDetail == null) {
			// 所以的借款详情
			borrowMAllDetail = borrowManageService
					.queryBorrowAllDetailById(idLong);
		}
		return borrowMAllDetail;
	}

	/**
	 * 验证用户资料信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String isNotUnderCoirculationBorrow() throws Exception {
		long uId = Convert.strToLong(request().getParameter("i"), -1);
		Map<String, String> userMap;
		try {
			userMap = userService.queryUserById(uId);
			if(userMap == null){
				userMap = new HashMap<String, String>();
			}
			if (Convert.strToInt(userMap.get("vipStatus"), 0) == IConstants.UNVIP_STATUS) {// 没有成为VIP
				ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
				return null;
			}
			if (Convert.strToInt(userMap.get("authStep"), 0) == 1) {
				// 基本信息认证步骤
				ServletHelper.returnStr(ServletActionContext.getResponse(), "7");
				return null;
			} else if (Convert.strToInt(userMap.get("authStep"), 0) == 2) {
				// 工作信息认证步骤
				ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
				return null;
			} else if (Convert.strToInt(userMap.get("authStep"), 0) == 3) {
				// VIP申请认证步骤
				ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
				return null;
			} else if (Convert.strToInt(userMap.get("authStep"), 0) == 4) {
				// 上传资料认证步骤
				ServletHelper.returnStr(ServletActionContext.getResponse(), "6");
				return null;
			}
			Map<String, String> map = dataApproveService.querySauthId(uId,
					IConstants.FLOW_PHONE);
			if (map == null) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");// 手机认证
				return null;
			} else {
				Long sauthId = Convert.strToLong(map.get("id"), -1L);
				Long status = dataApproveService.queryApproveStatus(sauthId);
				if (status < 0) {
					ServletHelper.returnStr(ServletActionContext.getResponse(), "8");// 手机认证待审核
					return null;
				}
			}
			// 条件满足
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @throws Exception
	 * @MethodName: underCirculationBorrow
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-5-20 下午04:38:02
	 * @Return:
	 * @Descb: 代发流转标
	 * @Throws:
	 */
	@SuppressWarnings("unchecked")
	public String underCirculationBorrow() throws Exception {
		String uId = SqlInfusionHelper.filteSqlInfusion(request().getParameter("i") == null ? "" : request()
				.getParameter("i"));
		session().setAttribute("uId", uId);

		Map<String, String> tempBorrwBidMessage = new HashMap<String, String>();
		try {
			tempBorrwBidMessage = shoveBorrowTypeService
					.queryShoveBorrowTypeByNid(IConstants.BORROW_TYPE_FLOW);
			if(tempBorrwBidMessage == null){
				tempBorrwBidMessage = new HashMap<String, String>();
			}
			// 取得按借款金额的比例进行奖励
			paramMap.put("scalefirst", tempBorrwBidMessage
					.get("award_scale_first")
					+ "");
			paramMap.put("scaleend", tempBorrwBidMessage.get("award_scale_end")
					+ "");
			// 如果选择金额的话，则按此奖励的金额范围
			paramMap.put("accountfirst", tempBorrwBidMessage
					.get("award_account_first")
					+ "");
			paramMap.put("accountend", tempBorrwBidMessage
					.get("award_account_end")
					+ "");
			// 借款额度
			paramMap.put("borrowMoneyfirst", tempBorrwBidMessage
					.get("amount_first")
					+ "");
			paramMap.put("borrowMoneyend", tempBorrwBidMessage
					.get("amount_end")
					+ "");
			// 借款额度倍数
			paramMap.put("accountmultiple", tempBorrwBidMessage
					.get("account_multiple")
					+ "");
			// 年利率
			paramMap.put("aprfirst", tempBorrwBidMessage.get("apr_first") + "");
			paramMap.put("aprend", tempBorrwBidMessage.get("apr_end") + "");
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
		}

		return "success";
	}

	public DataApproveService getDataApproveService() {
		return dataApproveService;
	}

	public void setDataApproveService(DataApproveService dataApproveService) {
		this.dataApproveService = dataApproveService;
	}

	private String changeStr2Str(String mailContent) {
		if (mailContent != null && !mailContent.equals("")) {
			int ind1 = mailContent.indexOf("<");
			int ind2 = mailContent.indexOf(">");
			if (ind1 < 0 || ind2 < 0 || ind2 <= ind1) {
				return mailContent;
			}
			String newStr = SqlInfusionHelper.filteSqlInfusion(mailContent.substring(0, ind1)
					+ mailContent.substring(ind2 + 1));
			// 处理<a>链接的结束标签
			newStr.replace("</a>", "");
			return newStr;
		}
		return mailContent;
	}

	public void setShoveBorrowTypeService(
			ShoveBorrowTypeService shoveBorrowTypeService) {
		this.shoveBorrowTypeService = shoveBorrowTypeService;
	}

	public List<Map<String, Object>> getCirList() {
		return cirList;
	}

	public void setCirList(List<Map<String, Object>> cirList) {
		this.cirList = cirList;
	}

	public Map<String, String> getBorrowCirculationDetail() {
		return borrowCirculationDetail;
	}

	public void setBorrowCirculationDetail(
			Map<String, String> borrowCirculationDetail) {
		this.borrowCirculationDetail = borrowCirculationDetail;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public Object getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(Object borrowId) {
		this.borrowId = borrowId;
	}

	public static Log getLog() {
		return log;
	}

	public List<Map<String, Object>> getBorrowPurposeList() throws Exception {
		if (borrowPurposeList == null) {
			// 借款目的列表
			borrowPurposeList = selectedService.borrowPurpose();
		}
		return borrowPurposeList;
	}

	public List<Map<String, Object>> getBorrowDeadlineList() throws Exception {
		if (borrowDeadlineList == null) {
			// 借款期限列表
			borrowDeadlineList = selectedService.borrowDeadline();
		}
		return borrowDeadlineList;
	}

	public List<Map<String, Object>> getBorrowRaiseTermList() throws Exception {
		if (borrowRaiseTermList == null) {
			// 筹款期限列表
			borrowRaiseTermList = selectedService.borrowRaiseTerm();
		}
		return borrowRaiseTermList;
	}

	public List<Map<String, Object>> getSysImageList() throws Exception {
		if (sysImageList == null) {
			// 系统列表
			sysImageList = selectedService.sysImageList();
		}
		return sysImageList;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}

	public List<Map<String, Object>> getBorrowAmountList() {
		return borrowAmountList;
	}

	public void setBorrowAmountList(List<Map<String, Object>> borrowAmountList) {
		this.borrowAmountList = borrowAmountList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public ShoveBorrowTypeService getShoveBorrowTypeService() {
		return shoveBorrowTypeService;
	}

	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setBorrowMFADetail(Map<String, String> borrowMFADetail) {
		this.borrowMFADetail = borrowMFADetail;
	}

	public void setBorrowMTenderInDetail(
			Map<String, String> borrowMTenderInDetail) {
		this.borrowMTenderInDetail = borrowMTenderInDetail;
	}

	public void setBorrowMFullScaleDetail(
			Map<String, String> borrowMFullScaleDetail) {
		this.borrowMFullScaleDetail = borrowMFullScaleDetail;
	}

	public void setBorrowMFlowMarkDetail(
			Map<String, String> borrowMFlowMarkDetail) {
		this.borrowMFlowMarkDetail = borrowMFlowMarkDetail;
	}

	public void setBorrowMAllDetail(Map<String, String> borrowMAllDetail) {
		this.borrowMAllDetail = borrowMAllDetail;
	}

	public void setBorrowPurposeList(List<Map<String, Object>> borrowPurposeList) {
		this.borrowPurposeList = borrowPurposeList;
	}

	public void setBorrowDeadlineList(
			List<Map<String, Object>> borrowDeadlineList) {
		this.borrowDeadlineList = borrowDeadlineList;
	}

	public void setBorrowRaiseTermList(
			List<Map<String, Object>> borrowRaiseTermList) {
		this.borrowRaiseTermList = borrowRaiseTermList;
	}

	public void setSysImageList(List<Map<String, Object>> sysImageList) {
		this.sysImageList = sysImageList;
	}

	public List<Map<String, Object>> getBorrowTurnlineList() throws Exception {
		if (borrowTurnlineList == null) {
			// 借款期限列表
			// borrowDeadlineList = selectedService.borrowDeadline();
			// 获取的到相应的map
			Map<String, String> borrowTrunlineMap;
			try {
				borrowTrunlineMap = shoveBorrowTypeService
						.queryShoveBorrowTypeByNid(IConstants.BORROW_TYPE_FLOW);
				if(borrowTrunlineMap == null){
					borrowTrunlineMap = new HashMap<String, String>();
				}
				borrowTurnlineList = new ArrayList<Map<String, Object>>();
				if (borrowTrunlineMap != null && borrowTrunlineMap.size() > 0) {
					String trunmonth = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(borrowTrunlineMap
							.get("period_month")
							+ "", ""));
					String[] trunmonths = trunmonth.split(",");// 截取;符号
					// 放入String数组
					if (trunmonths != null) {
						String str = " 个月";
						for (String file : trunmonths) {// 遍历String数组
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("key", file.trim());
							map.put("value", file + str);
							borrowTurnlineList.add(map);
						}
					}

				}
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}

		}
		return borrowTurnlineList;
	}

	public void setBorrowTurnlineList(
			List<Map<String, Object>> borrowTurnlineList) {
		this.borrowTurnlineList = borrowTurnlineList;
	}

	public OperationLogService getOperationLogService() {
		return operationLogService;
	}

	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}
}