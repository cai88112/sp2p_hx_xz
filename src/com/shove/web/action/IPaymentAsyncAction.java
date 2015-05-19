package com.shove.web.action;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.google.gson.Gson;
import com.shove.Convert;
import com.shove.config.GopayConfig;
import com.shove.config.IPayConfig;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.entity.User;
import com.sp2p.service.FinanceService;
import com.sp2p.service.FrontMyPaymentService;
import com.sp2p.service.HomeInfoSettingService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserIntegralService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;

/**
 * 异步通知接口
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
public class IPaymentAsyncAction extends BasePageAction {
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(IPaymentAction.class);
	private UserIntegralService userIntegralService;
	private UserService userService;
	private FrontMyPaymentService frontMyPaymentService;
	private FundManagementService fundManagementService;
	private RechargeService rechargeService;
	private FinanceService financeService;
	private BorrowManageService borrowManageService;
	private HomeInfoSettingService homeInfoSettingService;
	/**
	 * 开户异步通知
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reAsyncCreateIpsAcct() throws Exception {
		log.info("******************开户异步通知开始执行************************");
		// User user = (User) session().getAttribute("user");
		Long result = -1l;
		String pErrCode = request("pErrCode");// 开户状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		// String p3DesXmlPara = request("p3DesXmlPara");
		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");
		String pIpsAcctNo = parseXml.get("pIpsAcctNo");// IPS号
		xx("***********开户异步通知返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("开户异步签名失败！");
		} else {
			log.info("开户异步签名成功！");
			// 本地是否操作
			boolean ipsSuccess = userService.queryUserByIpsBillNo(pIpsAcctNo);
			log.info("ipsSuccess值：" + ipsSuccess);

			if (!pErrCode.equals("0000")) {// 环迅返回开户失败
				log.info("异步用户绑定账号失败");
			} else {
				log.info("异步用户绑定账号成功");
				// 添加用户IPS账号
				if (ipsSuccess == false) {
					log.info("异步执行本地绑定操作");
					Map<String, String> userMap = userService.quePortUserId(
							pMerBillNo, null);
					result = userService.updatePortNo(userMap.get("userId"),
							pIpsAcctNo);

					// 修改认证步骤
					result = userService.updateAuthStep(userMap.get("userId"),
							5);
				}

				if (result > 0) {
					// if (user.getAuthStep() == 4) {
					// user.setAuthStep(5);
					// }
					Map<String, String> userMap = userService.quePortUserId(
							pMerBillNo, null);
					long userId = Convert.strToInt(userMap.get("userId") + "", -1);
					Map<String, String>  Usermap = userService.queryUserById(userId);
					// 查找用户的之前的信用积分		
					Integer preScore = Convert.strToInt(Usermap.get("rating"),-1);
					// 添加积分
					userIntegralService.updateEscrowRegister(userId,400,preScore);
					
					log.info("本地异步执行用户绑定账号成功");
				}
			}

		}
		log.info("******************开户异步通知执行结束************************");
		return null;
	}

	/**
	 * 充值异步通知
	 * 
	 * @return
	 * @throws Exception
	 */
	public String merAsyncServerUrl() throws Exception {
		log.info("******************充值异步通知开始执行************************");
		String pErrCode = request("pErrCode");// 充值状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");// 商户订单号

		xx("充值异步通知返回参数");
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("充值异步签名失败");
		} else {
			log.info("充值异步签名成功");
			// 本地是否操作
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			log.info("fundMap值：" + fundMap);
			if (fundMap == null) {
				log.info("开始执行充值异步回调");
				if (!"0000".equals(pErrCode) && !"9999".equals(pErrCode)) {// 环迅执行失败
					log.info("异步处理充值失败！");
					return null;
				}
				if ("9999".equals(pErrCode)) {
					log.info("异步充值订单正在处理中");
					return null;
				}
				// 开始执行本地操作
				Map<String, String> userMap = userService.quePortUserId(null,
						pMerBillNo);
				Gson gson = new Gson();
				List<Map<String, String>> list = gson
						.fromJson(
								userMap.get("hxBuMap"),
								new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
								}.getType());
				String in_paynumber = "";
				if (list != null) {
					Map<String, String> hxBuMapXml = list.get(0);
					in_paynumber = hxBuMapXml.get("recharId");
				}

				String paybank = GopayConfig.bankMap
						.get(request("pTrdBnkCode"));// 支付银行编号
				if (StringUtils.isBlank(paybank)) {// 无法查询那家银行支付
					paybank = "环迅支付充值";
				}
				double money = Convert.strToDouble(parseXml.get("pTrdAmt"), 0);

				Map<String, String> resultMap = rechargeService
						.addUseraddmoney(Convert.strToLong(userMap
								.get("userId"), -1), money, in_paynumber,
								paybank, pMerBillNo);

				String result = resultMap.get("result");
				if (!"0".endsWith(result)) {
					log.info("本地异步执行充值失败");
					return null;
				}
				// vip扣费
				Map<String, String> tUserMap = userService
						.queryUserById(Convert.strToLong(userMap.get("userId"),
								-1));
				int vipStatus = Convert.strToInt(tUserMap.get("vipStatus"), 1);
				int feeStatus = Convert.strToInt(tUserMap.get("feeStatus"), 1);
				if (vipStatus != 1 && feeStatus == 1) {
					Map<String, String> renewalVIPMap = homeInfoSettingService
							.renewalVIPSubmit(Convert.strToLong(userMap
									.get("userId"), -1), getPlatformCost());
					if (renewalVIPMap == null) {
						renewalVIPMap = new HashMap<String, String>();
					}
					String result1 = renewalVIPMap.get("result") == null ? ""
							: renewalVIPMap.get("result");
					// 续费成功
					if ("1".equals(result1)) {
						log.info("VIP续费成功");
					} else {
						log.info("VIP扣费失败，" + result1);
					}
				}
				log.info("本地异步执行充值成功");
			}
		}
		log.info("******************充值异步通知执行结束************************");
		return null;
	}

	/**
	 * 自动投标签约回调(异步)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reAsyncpautomaticBidSet() throws Exception {
		log
				.info("----------------------自动投标签约回调（异步）开始--------------------------");
		xx("自动投标签约回调（异步）返回参数打印");
		// String url = "/automaticBidInit.do";
		String pErrCode = request("pErrCode");// 协议状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pIpsAcctNo");
		Map<String, String> userMap = userService.quePortUserId(pMerBillNo,
				null);

		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("异步提现回调签名失败");
		} else {
			log.info("异步提现回调签名成功");
			// 是否已经同步
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			log.info("fundMap值：" + fundMap);
			if (fundMap == null) {
				log.info("开始执行提现回调异步");
				if ("0000".equals(pErrCode)) {// 提现申请成功
					log.info("提现申请成功");
					// 改变提现状态，增加成功提现记录
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									userMap.get("hxBuMap"),
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					log.info(list);
					Map<String, String> hxBuMapXml = list.get(0);
					long wid = Convert.strToLong(hxBuMapXml.get("wid"), 0);
					long checkId = Convert.strToLong(hxBuMapXml.get("checkId"),
							0);
					String ipAddress = ServletHelper.getIpAddress(ServletActionContext.getRequest());
					Map<String, String> retMap = fundManagementService
							.updateWithdrawTransfer(wid, 2, checkId,
									SqlInfusionHelper.filteSqlInfusion(ipAddress),
									pMerBillNo);
					long retVal = Convert.strToLong(retMap.get("ret") + "", -1);
					String ret_desc = retMap.get("ret_desc");
					log.info("本地提现操作结果retVal:" + retVal);
				}

			}
		}
		log.info("----------------------提现回调（异步）结束--------------------------");
		return null;
	}

	/**
	 * 还款回调（异步）
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reAsynctoRepayment() throws Exception {
		log.info("----------------------还款回调（异步）开始--------------------------");
		xx("还款回调（异步）返回参数打印");
		String url = "/queryMyPayingBorrowList.do";
		String pErrCode = request("pErrCode");// 还款状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");

		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");
		Map<String, String> userMap = userService.quePortUserId(null,
				pMerBillNo);
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("异步还款回调签名失败");
		} else {
			log.info("异步还款回调签名成功");
			// 是否已经同步
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			log.info("fundMap值：" + fundMap);
			if (fundMap == null) {
				log.info("执行异步开始");
				if ("0000".equals(pErrCode)) {
					long idLong = -1;
					long userId = -1;
					Map<String, String> hxBuMapXml = new HashMap<String, String>();
					if (userMap != null && userMap.size() > 0) {
						Gson gson = new Gson();
						List<Map<String, String>> list = gson
								.fromJson(
										userMap.get("hxBuMap"),
										new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
										}.getType());
						log.info(list);
						if (list != null && list.size() > 0) {
							hxBuMapXml = list.get(0);
						}
						idLong = Convert
								.strToLong(hxBuMapXml.get("idLong"), -1);
						userId = Convert.strToLong(userMap.get("userId"), -1);
					}
					Map<String, String> map = frontMyPaymentService.submitPay(
							idLong, userId, getBasePath(), "", pMerBillNo);
					String result = Convert.strToStr(map.get("ret_desc"), "");
					log.info("本地还款操作结果result：" + result);
				}
			}
		}
		log.info("----------------------还款回调（异步）开始--------------------------");
		return null;
	}

	/**
	 * 提现回调(异步)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reAsyncmerServerUrl() throws Exception {
		log.info("----------------------提现回调（异步）开始--------------------------");
		xx("提现回调（异步）返回参数打印");
		String pErrCode = request("pErrCode");// 充值状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");
		Map<String, String> userMap = userService.quePortUserId(pMerBillNo,
				null);

		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("异步提现回调签名失败");
		} else {
			log.info("异步提现回调签名成功");
			// 是否已经同步
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			log.info("fundMap值：" + fundMap);
			if (fundMap == null) {
				log.info("开始执行提现回调异步");
				if ("0000".equals(pErrCode)) {// 提现申请成功
					log.info("提现申请成功");
					// 改变提现状态，增加成功提现记录
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									userMap.get("hxBuMap"),
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					log.info(list);
					Map<String, String> hxBuMapXml = list.get(0);
					long wid = Convert.strToLong(hxBuMapXml.get("wid"), 0);
					long checkId = Convert.strToLong(hxBuMapXml.get("checkId"),
							0);
					String ipAddress = ServletHelper.getIpAddress(ServletActionContext.getRequest());
					Map<String, String> retMap = fundManagementService
							.updateWithdrawTransfer(wid, 2, checkId,
									SqlInfusionHelper.filteSqlInfusion(ipAddress),
									pMerBillNo);
					long retVal = Convert.strToLong(retMap.get("ret") + "", -1);
					String ret_desc = retMap.get("ret_desc");
					log.info("本地提现操作结果retVal:" + retVal);
				}
			}
		}
		log.info("----------------------提现回调（异步）结束--------------------------");
		return null;
	}

	/**
	 * 投标申请(非流转标)异步通知
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reAsyncFinanceInvestUrl() throws Exception {
		log
				.info("******************投标申请(非流转标)异步通知开始执行************************");
		String pErrCode = request("pErrCode");// 投标状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");

		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");// 商户订单号
		Map<String, String> userMap = userService.quePortUserId(null,
				pMerBillNo);
		xx("***********投资申请异步通知返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("投标申请(非流转标)异步签名失败！");
		} else {
			log.info("投标申请(非流转标)异步签名成功！");
			// 本地是否操作
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			log.info("fundMap值：" + fundMap);
			if (fundMap == null) {
				log.info("开始执行投标申请(非流转标)异步回调");
				if (!pErrCode.equals("0000")) {// 环迅返回投标申请失败
					log.info("环迅投标申请(非流转标)异步处理失败");
					return null;
				} else {
					// 开始本地操作
					log.info("环迅投标申请(非流转标)异步处理成功，开始执行本地操作");
					long idLong = -1;
					long userId = -1;
					String userName = "";
					int status = -1;
					int num = -1;
					Map<String, String> hxBuMapXml = new HashMap<String, String>();
					if (userMap != null && userMap.size() > 0) {
						Gson gson = new Gson();
						List<Map<String, String>> list = gson
								.fromJson(
										userMap.get("hxBuMap"),
										new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
										}.getType());
						if (list != null && list.size() > 0) {
							hxBuMapXml = list.get(0);
						}
						idLong = Convert
								.strToLong(hxBuMapXml.get("idLong"), -1);
						userId = Convert.strToLong(userMap.get("userId"), -1);
						userName = hxBuMapXml.get("pFRealName");
						status = Convert.strToInt(hxBuMapXml.get("status"), -1);
						num = Convert.strToInt(hxBuMapXml.get("num"), -1);
					}
					double investAmount = Convert.strToDouble(parseXml
							.get("pTrdAmt"), -1);

					Map<String, String> result = financeService
							.addBorrowInvest(idLong, userId, "", investAmount,
									getBasePath(), userName, status, num,
									pMerBillNo,"");

					String resultMSG = result.get("ret_desc");
					if ("".equals(resultMSG)) {
						// 查询是否满标，是否满标自动审核
						Map<String, String> autoMap = financeService
								.queryBorrowDetailById(idLong);
						int auditpass = Convert.strToInt(autoMap
								.get("auditpass"), 2);
						int borrowStatus = Convert.strToInt(autoMap
								.get("borrowStatus"), -1);
						if (borrowStatus == 3) {
							if (auditpass == 1) {
								Admin admin = (Admin) session().getAttribute(
										IConstants.SESSION_ADMIN);
								Map<String, String> retMap = borrowManageService
										.updateBorrowFullScaleStatus(idLong,
												4l, "满标自动审核", admin.getId(),
												getBasePath(), autoMap
														.get("tradeNo"));
								long retVal = -1;
								retVal = Convert.strToLong(retMap.get("ret")
										+ "", -1);
								session().removeAttribute("randomCode");
								if (retVal <= 0) {
									log.info("满标自动审核执行结果"
											+ result.get("ret_desc"));
									return null;
								} else {
									log.info("满标自动审核成功");
									return null;
								}
							}
						}
						log.info("投标申请(非流转标)本地执行结果：" + result.get("ret_desc"));
					} else {
						log.info("投标申请(非流转标)本地执行通知结果：" + resultMSG);
					}
				}
			}
			log
					.info("******************投标申请(非流转标)异步通知执行结束************************");
		}
		return null;
	}

	/**
	 * 投标申请(流转标)异步通知
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reAsyncTCTradeUrl() throws Exception {
		log.info("******************投标申请(流转标)异步通知开始执行************************");
		String pErrCode = request("pErrCode");// 投标状态(流转标)
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		HttpServletRequest request = ServletActionContext.getRequest();
		String p3DesXmlPara = request.getParameter("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");// 商户订单号

		xx("***********投资申请(流转标)返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			log.info("投标申请(流转标)异步签名失败！");
			return null;
		} else {
			log.info("投标申请(流转标)异步签名成功！");
			// 本地是否操作
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			if (fundMap == null) {
				log.info("开始执行投标申请(流转标)异步回调");
				if (!pErrCode.equals("0000")) {// 环迅返回投标申请失败
					log.info("环迅投标申请(流转标)异步处理失败");
					return null;
				} else {
					// 开始本地操作
					log.info("环迅投标申请(流转标)异步处理成功，开始执行本地操作");
					long idLong = -1;
					long userId = -1;
					String userName = "";
					int resultLong = -1;
					String tradeNo = "";
					String pContractNo = "";

					Map<String, String> userMap = userService.quePortUserId(
							null, pMerBillNo);

					Map<String, String> hxBuMapXml = new HashMap<String, String>();
					if (userMap != null && userMap.size() > 0) {
						Gson gson = new Gson();
						List<Map<String, String>> list = gson
								.fromJson(
										userMap.get("hxBuMap"),
										new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
										}.getType());
						if (list != null && list.size() > 0) {
							hxBuMapXml = list.get(0);
						}
						idLong = Convert
								.strToLong(hxBuMapXml.get("idLong"), -1);
						userId = Convert.strToLong(userMap.get("userId"), -1);
						userName = hxBuMapXml.get("pFRealName");
						resultLong = Convert.strToInt(hxBuMapXml
								.get("resultLong"), -1);
						tradeNo = hxBuMapXml.get("pBidNo");
						pContractNo = hxBuMapXml.get("pContractNo");
					}
					// 调用投标审核(流转标)接口
					IPaymentAction ipa = new IPaymentAction();
					Map<String, String> data = ipa.checkTrade(tradeNo, "4",
							pContractNo);
					if (data != null && data.size() > 0) {
						String pErrCode1 = data.get("pErrCode");// 投标审核状态
						String pMerCode1 = data.get("pMerCode");
						String pErrMsg1 = data.get("pErrMsg");

						String p3DesXmlPara1 = data.get("p3DesXmlPara");
						Map<String, String> parseXml1 = IPaymentUtil
								.parseXmlToJson(p3DesXmlPara1);
						String pMerBillNoCheck = parseXml1.get("pMerBillNo");// 投标审核商户订单号

						xx("***********投资审核(流转标)返回参数");// 打印返回参数
						String pSign1 = data.get("pSign");// 签名
						boolean sign1 = IPaymentUtil.checkSign(pMerCode1,
								pErrCode1, pErrMsg1, p3DesXmlPara1, pSign1);
						if (!sign1) {
							log.info("投标审核(流转标)签名失败！");
							return null;
						} else {
							log.info("投标审核(流转标)签名成功！");
							// 本地是否操作
							Map<String, String> fundMap1 = fundManagementService
									.queryFundRecord(pMerBillNoCheck);
							if (fundMap1 == null) {
								log.info("投标审核(流转标)执行本地操作");
								if (!pErrCode1.equals("0000")) {
									log.info("环迅投标申请(流转标)处理失败");
									return null;
								} else {
									log.info("投标审核(流转标)环迅执行成功，开始执行本地操作");
									String resultStr = financeService
											.subscribeSubmit(idLong,
													resultLong, userId,
													getBasePath(), userName,
													getPlatformCost(),
													pMerBillNo, pMerBillNoCheck);
									if ("1".equals(resultStr)) {
										log.info("本地执行投标审核操作成功");
									} else {
										log.info("本地执行投标审核操作结果：" + resultStr);
									}
								}
							}
						}
					} else {
						// 环迅没有返回参数，跳转到环迅的维护页面
						log.info("环迅投标审核没有返回参数");
					}

				}
			}
		}
		log.info("******************投标申请(流转标)异步通知执行结束************************");
		return null;
	}

	// 打印参数
	public void xx(String mark) {
		log.info(mark);
		Enumeration<String> keyNames = request().getParameterNames();
		while (keyNames.hasMoreElements()) {
			String attrName = keyNames.nextElement();
			paramMap.put(attrName, request(attrName));
			log
					.info(attrName
							+ "---"
							+ request(attrName)
							+ "]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		}
		log.info("打印参数结束");
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setFrontMyPaymentService(
			FrontMyPaymentService frontMyPaymentService) {
		this.frontMyPaymentService = frontMyPaymentService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public HomeInfoSettingService getHomeInfoSettingService() {
		return homeInfoSettingService;
	}

	public void setHomeInfoSettingService(
			HomeInfoSettingService homeInfoSettingService) {
		this.homeInfoSettingService = homeInfoSettingService;
	}

	public UserIntegralService getUserIntegralService() {
		return userIntegralService;
	}

	public void setUserIntegralService(UserIntegralService userIntegralService) {
		this.userIntegralService = userIntegralService;
	}
	
}
