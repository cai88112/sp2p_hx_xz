package com.shove.web.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.DateHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.google.gson.Gson;
import com.ips.security.utility.IpsCrypto;
import com.shove.Convert;
import com.shove.config.GopayConfig;
import com.shove.config.IPayConfig;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.entity.User;
import com.sp2p.service.ExperiencemoneyService;
import com.sp2p.service.FinanceService;
import com.sp2p.service.FrontMyPaymentService;
import com.sp2p.service.HomeInfoSettingService;
import com.sp2p.service.MyHomeService;
import com.sp2p.service.RechargeDetailService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserIntegralService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;
import com.sp2p.service.admin.ShoveBorrowTypeService;
import com.sp2p.service.admin.UserManageServic;
import com.sp2p.system.exception.FrontHelpMessageException;


public class IPaymentAction extends BasePageAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(IPaymentAction.class);

	private UserService userService;
	private UserIntegralService userIntegralService;
	private RechargeService rechargeService;
	private FundManagementService fundManagementService;
	private MyHomeService myHomeService;
	private FinanceService financeService;
	private BorrowManageService borrowManageService;
	private ShoveBorrowTypeService shoveBorrowTypeService;
	private FrontMyPaymentService frontMyPaymentService;
	private HomeInfoSettingService homeInfoSettingService;
	private ExperiencemoneyService experiencemoneyService;
	private UserManageServic userManageServic;

	/**
	 * 开户
	 * 
	 * @return pMerBillNo商户开户流水号 pIdentType证件类型 pIdentNo 证件号码 pRealName真实姓名
	 *         pMobileNo手机号码
	 * @throws Exception
	 */
	public String createIpsAcct() throws Exception {
		User user = (User) request().getSession().getAttribute("user");
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		map.put("pMerBillNo", pMerBillNo);
		map.put("pIdentType", "1");
		map.put("pIdentNo", request("cadkey"));
		map.put("pRealName", request("realName"));
		map.put("pMobileNo", request("mobileNo"));
		map.put("pEmail", request("activeEmail"));
		map.put("pWebUrl", IPaymentConstants.url + "/reCreateIpsAcct.do");
		map.put("pS2SUrl", IPaymentConstants.url + "/reAsyncCreateIpsAcct.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		String url = IPaymentConstants.createIpsAcct;
		try {
			userService.savehxOrderNo(user.getId(), pMerBillNo, "", map);
			sendHtml(IPaymentUtil.createHtml(IPaymentUtil.parseMapToXml(map),
					url, IPayConfig.terraceNoOne, 1));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 账户绑定回调
	public String reCreateIpsAcct() throws Exception {
		log.info("账户绑定同步回调");
		User user = (User) session().getAttribute("user");
		String pReturn = "绑定成功,请及时检查邮箱激活环迅IPS账号！";
		String url = "/userPassData.do?from=&btype=";
		String pErrCode = request("pErrCode");// 开户状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");

		String p3DesXmlPara = request("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");
		String pIpsAcctNo = parseXml.get("pIpsAcctNo");// IPS号

		xx("***********开户返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			pReturn = "签名失败!";
		} else {
			// 本地操作是否成功
			boolean ipsSuccess = userService.queryUserByIpsBillNo(pIpsAcctNo);
			if (ipsSuccess == false) {
				log.info("执行本地绑定操作");
				Map<String, String> userMap = userService.quePortUserId(
						pMerBillNo, null);

				if (!pErrCode.equals("0000")) {// 环迅返回开户失败
					pReturn = "绑定失败，" + request("pErrMsg");
					url = "/userPassData.do?from=&btype=";
				} else {
					// 添加用户IPS账号
					Long result = userService.updatePortNo(
							userMap.get("userId"), pIpsAcctNo);
					// 修改认证步骤
					result = userService.updateAuthStep(userMap.get("userId"),
							5);
					if (result > 0) {
						user.setPortMerBillNo(pIpsAcctNo);
						if (user.getAuthStep() == 4) {
							user.setAuthStep(5);
						}

						long userId = Convert.strToInt(userMap.get("userId")
								+ "", -1);
						Map<String, String> Usermap = userService
								.queryUserById(userId);
						// 查找用户的之前的信用积分
						Integer preScore = Convert.strToInt(
								Usermap.get("rating"), -1);
						// 添加积分
						userIntegralService.updateEscrowRegister(userId, 400,
								preScore);

						pReturn = "绑定成功,请及时检查邮箱激活环迅IPS账号!";
					}
				}
			}
		}
		session().setAttribute("user", user);
		getOut().print(
				"<script>alert('" + pReturn + "');parent.location.href='"
						+ request().getContextPath() + "" + url + "';</script>");

		return null;
	}

	public void xx(String mark) {
		log.info(mark);
		Enumeration<String> keyNames = request().getParameterNames();
		while (keyNames.hasMoreElements()) {
			String attrName = keyNames.nextElement();
			paramMap.put(attrName, request(attrName));
			log.info(attrName
					+ "---"
					+ request(attrName)
					+ "]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		}
		log.info("打印参数结束");
	}

	// 在线充值
	public String ipayPayment() throws Exception {
		User user = (User) session(IConstants.SESSION_USER);
		if (user == null) {// 未登陆
			return IConstants.ADMIN_AJAX_LOGIN;
		}
		String money = request("money");
		if (StringUtils.isBlank(money)) {// 判断是否为空
			return INPUT;
		}
		BigDecimal moneyDecimal = null;

		try {
			moneyDecimal = new BigDecimal(money);
		} catch (RuntimeException e) {
			return INPUT;
		}
		String gatewayType = request("gatewayType");
		int temp = moneyDecimal.compareTo(new BigDecimal("0.01"));// 最小金额为0.01元
		if (temp < 0) {
			return INPUT;
		}

		String bankCode = request("bankCode");

		long userId = this.getUserId();
		// 生成订单
		paramMap.put("rechargeMoney", moneyDecimal + "");
		paramMap.put("rechargeType", "5");
		paramMap.put("userId", userId + "");
		paramMap.put("result", "0");
		Date date = new Date();
		paramMap.put("addTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		Map<String, String> result = rechargeService.addRecharge(paramMap, 2);
		long nunber = Convert.strToLong(result.get("result"), -1);

		if (nunber != -1) {
		
			String html = dpTrade("在线充值", nunber, userId,
					DateFormatUtils.format(date, "yyyyMMdd"), bankCode, moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
			sendHtml(html);
			return null;
		} else {
			createHelpMessage("支付失败！" + result.get("description"), "返回页面",
					"queryFundrecordInit.do");
			return null;
		}
	}

	/**
	 * 跳转拦截
	 * 
	 * @param title
	 * @param msg
	 * @param url
	 * @throws FrontHelpMessageException
	 */
	public void createHelpMessage(String title, String msg, String url)
			throws FrontHelpMessageException {
		helpMessage.setMsg(new String[] { msg });
		helpMessage.setUrl(new String[] { url });
		helpMessage.setTitle(title);
		/*
		 * helpMessage.setMsg(new String[]{msg}); helpMessage.setUrl(new
		 * String[]{url});
		 */
		throw new FrontHelpMessageException();
	}

	/**
	 * 充值
	 * 
	 * @return
	 * @throws Exception
	 *             pMerBillNo 商户充值订单号 pAcctType 账户类型 pIdentNo 证件号码 pRealName 姓名
	 *             pIpsAcctNo IPS账号 pTrdDate 充值日期 pTrdAmt 充值金额 pTrdBnkCode
	 *             pMerFee pIpsFeeType pWebUrl pS2SUrl
	 */
	private String dpTrade(String body, long recharId, long userId,
			String tranDateTime, String bankCode, BigDecimal money)
			throws Exception {
		User user = (User) request().getSession().getAttribute("user");
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

		Map<String, String> userMap = userService.queryPersonById(user.getId());
		String pMerBillNo = IPaymentUtil.getIn_orderNo();// 商户流水号
		String pTrdAmt = String.format("%.2f", money);// 充值金额
		String pMerFee = "0.00";// 平台手续费

		Map<String, String> usersMap = userService.queryUserById(userId);
		int vipStatus = Convert.strToInt(usersMap.get("vipStatus"), 1);
		int feeStatus = Convert.strToInt(usersMap.get("feeStatus"), 1);
		if (feeStatus == 1) {
			Map<String, Object> platformCostMap = getPlatformCost();
			double vipFee = Convert.strToDouble(
					platformCostMap.get(IAmountConstants.VIP_FEE_RATE) + "", 0);
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			if (vipStatus == 2 || vipStatus == 3) {
				pMerFee = df.format(Convert.strToDouble(pMerFee, 0) + vipFee);
			}
		}
		// pMerFee = pMerFee + String.format("%.2f",
		// (money.doubleValue()*0.35)/100) ;//收取平台手续费
		map.put("pMerBillNo", pMerBillNo);
		map.put("pAcctType", "1");
		map.put("pIdentNo", userMap.get("idNo"));
		map.put("pRealName", user.getRealName());
		map.put("pIpsAcctNo", user.getPortMerBillNo());
		map.put("pTrdDate", tranDateTime);
		map.put("pTrdAmt", pTrdAmt);
		map.put("pChannelType", "1");
		map.put("pTrdBnkCode", bankCode);
		map.put("pMerFee", pMerFee);
		map.put("pIpsFeeType", "1");
		map.put("pWebUrl", IPaymentConstants.url + "/merServerUrl.do");
		map.put("pS2SUrl", IPaymentConstants.url + "/merAsyncServerUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		map.put("recharId", recharId);
		map.put("money", money);
		String url = IPaymentConstants.dpTrade;
		try {
			userService.savehxOrderNo(user.getId(), user.getPortMerBillNo(),
					pMerBillNo, map);// 保存输入参数记录
			sendHtml(IPaymentUtil.createHtml(IPaymentUtil.parseMapToXml(map),
					url, IPayConfig.terraceNoOne, 1, "pMerCode",
					"p3DesXmlPara", "pSign"));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 充值回调
	 * 
	 * @return
	 * @throws Exception
	 */
	public String merServerUrl() throws Exception {
		String pErrCode = request("pErrCode");// 充值状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		String p3DesXmlPara = request("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");// 商户订单号

		xx("***********充值返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			createHelpMessage("签名失败", "返回页面", "queryFundrecordInit.do");
		} else {
			log.info("充值签名成功");
			// 本地是否操作
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			if (fundMap == null) {
				if (!"0000".equals(pErrCode) && !"9999".equals(pErrCode)) {// 环迅执行失败
					createHelpMessage("支付失败！", "返回页面", "queryFundrecordInit.do");
				}
				if ("9999".equals(pErrCode)) {
					createHelpMessage("订单处理中，请耐心等待！", "返回页面",
							"queryFundrecordInit.do");
				}
				String paybank = GopayConfig.bankMap
						.get(request("pTrdBnkCode"));// 支付银行编号
				if (StringUtils.isBlank(paybank)) {// 无法查询那家银行支付
					paybank = "环迅支付充值";
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
				double money = 0.00;
				double pMerFeeSum = 0.00;// 总手续费
				double pMerFee = 0.00;// 平台手续费
				if (list != null) {
					Map<String, String> hxBuMapXml = list.get(0);
					in_paynumber = hxBuMapXml.get("recharId");
					money = Convert.strToDouble(hxBuMapXml.get("money"), 0);
					// pMerFee = money*0.35/100 ;//收取平台手续费

				}

				Map<String, String> resultMap = rechargeService
						.addUseraddmoney(
								Convert.strToLong(userMap.get("userId"), -1),
								money, in_paynumber, paybank, pMerBillNo);

				User user = (User) session().getAttribute(
						IConstants.SESSION_USER);
				// vip扣费
				Map<String, String> tUserMap = userService.queryUserById(user
						.getId());
				int vipStatus = Convert.strToInt(tUserMap.get("vipStatus"), 1);
				int feeStatus = Convert.strToInt(tUserMap.get("feeStatus"), 1);
				if (vipStatus != 1 && feeStatus == 1) {
					Map<String, String> renewalVIPMap = homeInfoSettingService
							.renewalVIPSubmit(user.getId(), getPlatformCost());
					if (renewalVIPMap == null) {
						renewalVIPMap = new HashMap<String, String>();
					}
					String result1 = renewalVIPMap.get("result") == null ? ""
							: renewalVIPMap.get("result");
					// 续费成功
					if ("1".equals(result1)) {
						user.setVipStatus(IConstants.VIP_STATUS);
						session().setAttribute(IConstants.SESSION_USER, user);
						log.info("VIP续费成功");
					} else {
						log.info("VIP扣费失败，" + result1);
					}
				}

				String result = resultMap.get("result");
				String description = resultMap.get("description");

				HttpServletResponse httpServletResponse = ServletActionContext
						.getResponse();
				httpServletResponse.setCharacterEncoding("utf-8");
				PrintWriter pw = httpServletResponse.getWriter();
				String msg = description;
				if (!"0".endsWith(result)) {
					log.info("6--");
					pw.println("fail");
					createHelpMessage(msg, "返回页面", "queryFundrecordInit.do");
				}
				msg = "充值成功";
				pw.println("success");
				log.info("7--");
				createHelpMessage(msg + "", "返回页面", "queryFundrecordInit.do");
			}
			createHelpMessage("充值成功" + "", "返回页面", "queryFundrecordInit.do");
		}
		return null;
	}

	/**
	 * 提现
	 * 
	 * @return
	 * @throws Exception
	 */
	public String withdrawalMon() throws Exception {
		User user = (User) request().getSession().getAttribute("user");
		Map<String, String> userMap = userService.queryPersonById(user.getId());
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		String amt = request("amt");//提现金额
		String wid = request("wid");//提现表的id
		String checkId = request("checkId");//审核人id
		String pdg = request("pdg");//手续费
		
		
		
		map.put("pMerBillNo", pMerBillNo);
		map.put("pAcctType", "1");
		map.put("pOutType", "1");
		map.put("pBidNo", "123456");
		map.put("pContractNo", "123456");
		map.put("pDwTo", "123456");
		map.put("pIdentNo", userMap.get("idNo"));
		map.put("pRealName", user.getRealName());
		map.put("pIpsAcctNo", user.getPortMerBillNo());
		map.put("pDwDate", DateFormatUtils.format(new Date(), "yyyyMMdd"));
		map.put("pTrdAmt", amt);
		map.put("pMerFee", pdg);
		map.put("pIpsFeeType", "1");
		map.put("pWebUrl", IPaymentConstants.url + "/remerServerUrl.do");
		map.put("pS2SUrl", IPaymentConstants.url + "/reAsyncmerServerUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		map.put("wid", wid);
		map.put("checkId", checkId);
		String url = IPaymentConstants.dwTrade;
		try {
			userService.savehxOrderNo(user.getId(), user.getPortMerBillNo(),
					pMerBillNo, map);
			sendHtml(IPaymentUtil.createHtml(IPaymentUtil.parseMapToXml(map),
					url, IPayConfig.terraceNoOne, 1, "pMerCode",
					"p3DesXmlPara", "pSign"));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		xx();

		return null;
	}

	/**
	 * 提现回调
	 * 
	 * @return
	 * @throws Exception
	 */
	public String remerServerUrl() throws Exception {
		String pErrCode = request("pErrCode");// 充值状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		String p3DesXmlPara = request("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");
		String pIpsAcctNo = parseXml.get("pIpsAcctNo");
		Map<String, String> userMap = userService.quePortUserId(pIpsAcctNo,
				pMerBillNo);

		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			createHelpMessage(pErrMsg, "返回提现", "withdrawLoad");
		} else {
			// 是否已经同步
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			if (fundMap == null) {
				if (!"0000".equals(pErrCode)) {
					createHelpMessage(pErrMsg, "返回提现", "withdrawLoad");
				}
				if ("0000".equals(pErrCode)) {// 提现申请成功
					// 改变提现状态，增加成功提现记录
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									userMap.get("hxBuMap"),
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());

					Map<String, String> hxBuMapXml = list.get(0);
					long wid = Convert.strToLong(hxBuMapXml.get("wid"), 0);
					long checkId = Convert.strToLong(hxBuMapXml.get("checkId"),
							0);
//					String ipAddress = ServletUtils.getRemortIp();
					String ipAddress = ServletHelper.getIpAddress(ServletActionContext.getRequest());
					Map<String, String> retMap = fundManagementService
							.updateWithdrawTransfer(wid, 2, checkId,
									SqlInfusionHelper.filteSqlInfusion(ipAddress),
									pMerBillNo);
					long retVal = Convert.strToLong(retMap.get("ret") + "", -1);
					String ret_desc = retMap.get("ret_desc");
					if (retVal != 1) {
						log.info("7--");
						createHelpMessage(ret_desc, "返回提现", "withdrawLoad");
					}
				}
				log.info("7--");
			}
		}
		createHelpMessage(pErrMsg, "返回提现", "withdrawLoad");
		return null;
	}

	/**
	 * @MethodName: financeInvest
	 * @Param: FrontMyFinanceAction
	 * @Author: gang.lv
	 * @Date: 2013-3-30 下午03:53:34
	 * @Return:
	 * @Descb: 投标申请校验
	 * @Throws:
	 */
	public String financeInvest() throws Exception {
		User user = (User) session().getAttribute("user");
		JSONObject obj = new JSONObject();
		String id = paramMap.get("id") == null ? "" : paramMap.get("id");
		long idLong = Convert.strToLong(id, -1);
		String amount = paramMap.get("amount") == null ? "" : paramMap
				.get("amount");
		double amountDouble = Convert.strToDouble(amount, 0);// 投资的金额
		String hasPWD = SqlInfusionHelper.filteSqlInfusion(""
				+ session().getAttribute("hasPWD"));
		String investPWD = paramMap.get("investPWD") == null ? "" : SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("investPWD"));
		// String dealPWD = paramMap.get("dealPWD") ==
		// null?"":SqlInfusion.FilteSqlInfusion(paramMap.get("dealPWD"));
		int status = Convert.strToInt(paramMap.get("subscribes"), 2);
		if(user.getAuthStep()<5)
		{
			/*obj.put("msg", "未开通第三方账户");
			JSONUtils.printObject(obj);*/
			
			
			String pReturn = "未开通第三方账户!";
			String url = "/portUserAcct.do";
			getOut().print(
					"<script>alert('" + pReturn
							+ "');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");
			
			return null;
		}
		if (idLong == -1) {
			// 非法操作直接返回
			obj.put("msg", IConstants.ACTOIN_ILLEGAL);
			JSONHelper.printObject(obj);
			return null;
		}
		Map<String, String> investMaps = financeService.getInvestStatus(idLong);
		if (investMaps == null || investMaps.size() <= 0) {
			// 不满足投标条件,返回
			obj.put("msg", "该借款投标状态已失效");
			JSONHelper.printObject(obj);
			return null;
		}

		Map<String, String> investDetailMap = financeService
				.queryBorrowInvest(idLong);
		String borrowPublisher = investDetailMap.get("userId");
		if (borrowPublisher.equals(user.getId().toString())) {
			// 不满足投标条件,返回
			obj.put("msg", "不能投标自己发布的借款");
			JSONHelper.printObject(obj);
			return null;

		}
		if ("1".equals(hasPWD)) {
			if (investPWD == null || "".equals(investPWD)) {
				obj.put("msg", "投标密码不能为空");
				JSONHelper.printObject(obj);
				return null;
			}
			Map<String, String> investPWDMap = financeService.getInvestPWD(
					idLong, investPWD);
			if (investPWDMap == null || investPWDMap.size() == 0) {
				obj.put("msg", "投标密码错误");
				JSONHelper.printObject(obj);
				return null;
			}
		}
		// if (dealPWD == null || "".equals(dealPWD)) {
		// obj.put("msg", "交易密码不能为空");
		// JSONUtils.printObject(obj);
		// return null;
		// }
		double residue = Convert.strToDouble(investDetailMap.get("residue"),
				0.00);// 剩余投标金额
		if (amountDouble > residue) {
			obj.put("msg", "投标金额超过本轮剩余投标金额！");
			JSONHelper.printObject(obj);
			return null;
		}

		double minTenderedSum = Convert.strToDouble(
				investDetailMap.get("minTenderedSum"), 0.00);// 最低投标金额
		if (residue > minTenderedSum) {
			if (amountDouble < minTenderedSum) {
				obj.put("msg", "投标金额小于最小投标金额！");
				JSONHelper.printObject(obj);
				return null;
			}
		}
		// 最高投标金额
		double maxTenderedSum = Convert.strToDouble(
				investDetailMap.get("borrowAmount"), 0.00);
		if (amountDouble > maxTenderedSum) {
			obj.put("msg", "投标金额超过最大投标金额！");
			JSONHelper.printObject(obj);
			return null;
		}
		Map<String, String> userMap = userService.queryUserById(user.getId());
		if (userMap != null) {

			// 天标判断
			int isDayThe = Convert
					.strToInt(investDetailMap.get("isDayThe"), -1);
			if (isDayThe == 2) {
				Map<String, String> experiencemoneyMap = experiencemoneyService
						.queryExperiencemoneyByUserID(user.getId());
				// 判断是否有体验金
				if (experiencemoneyMap == null) {
					obj.put("msg", "您没有体验金，不符合参加新手标活动！");
					JSONHelper.printObject(obj);
					return null;
				}

				// 查看体验金是否过期
				int experienceMoneyStatus = Convert.strToInt(
						experiencemoneyMap.get("status"), 1);
				if (experienceMoneyStatus == 1) {
					obj.put("msg", "对不起，您的体验金已经过期！");
					JSONHelper.printObject(obj);
					return null;
				}
				// 投标金额判断（必须为1000）
				if (amountDouble != 1000) {
					obj.put("msg", "新手标的投标金额限定为1000元！");
					JSONHelper.printObject(obj);
					return null;
				}
				// 体验金可用金额判断
				double usableExperienceMoney = Convert.strToDouble(
						experiencemoneyMap.get("usableMoney"), 0.00);
				if (usableExperienceMoney < amountDouble) {
					obj.put("msg", "体验金账户可用余额少于投标金额！");
					JSONHelper.printObject(obj);
					return null;
				}
				// 天标剩余时间判断
				Date timeEnd = Convert.strToDate(
						experiencemoneyMap.get("timeEnd"), new Date());
				int residueDays = DateHelper.daysBetween(new Date(), timeEnd);
				int deadline = Convert.strToInt(
						investDetailMap.get("isDayThe"), -1);
				if (residueDays < deadline) {
					obj.put("msg", "体验金到期时间小于借款标的借款期限！");
					JSONHelper.printObject(obj);
					return null;
				}
			} else {
				// 其他标的判断
				double usableSum = Convert.strToDouble(
						userMap.get("usableSum"), 0.00);
				if (usableSum < amountDouble) {
					obj.put("msg", "账户可用余额少于投标金额，请充值！");
					JSONHelper.printObject(obj);
					return null;
				}
			}
			// if ("1".equals(IConstants.ENABLED_PASS)) {
			// dealPWD = com.shove.security.Encrypt.MD5(dealPWD.trim());
			// } else {
			// dealPWD = com.shove.security.Encrypt.MD5(dealPWD.trim()
			// + IConstants.PASS_KEY);
			// }
			// if (!dealPWD.equals(userMap.get("dealpwd"))) {
			// obj.put("msg", "交易密码错误");
			// JSONUtils.printObject(obj);
			// return null;
			// }
		}
		// else {
		// this.addFieldError("paramMap['dealPWD']", "交易密码错误");
		// return "input";
		// }
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
		// TODO 存在溢标BUG，第三方支付。！！

		// 调用投标申请接口
		String rsString = this.tenderTrade(idLong, amountDouble, num, status);
		if (rsString != null) {
			obj.put("msg", rsString);
			obj.put("dateThe", "1");
		}
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * 投标申请(非流转标)
	 * 
	 * @param borrowId
	 *            借款标id.
	 * @param amountDouble
	 *            借款金额.
	 * @param num
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String tenderTrade(long borrowId, double amountDouble, int num,
			int status) throws Exception {
		User user = (User) request().getSession().getAttribute("user");
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pBidType = "3";// 借款标类型默认 3.信用标
		String pOutType = "1";// 放款模式 默认 1.普通放款
		String purpose = "";// 借款用途
		String pDeadLine = "";// 满标期限
		String pTIdentNo = "";// 借款人证件号码
		String pTRealName = "";// 借款人真实姓名
		String pTIpsAcctNo = "";// 借款人IPS账号
		String tradeNo = "";// 标的流水号
		int isDayTheFlag = 1; // 天标标识 是否为天标( 默认 1 否，2 是)
		double feeRate = 0.00;// 借款管理费 比例
		double guarantee_fee = 0.00;// 担保费比例

		Map<String, String> investDetailMap = financeService
				.queryBorrowInvest(borrowId);
		if (investDetailMap != null && investDetailMap.size() > 0) {
			String borrowWay = investDetailMap.get("borrowWay");
			if ("1".equals(borrowWay))
				pBidType = "4";// 净值标
			if ("4".equals(borrowWay))
				pBidType = "8";// 其他(实地考察)

			// if ("5".equals(borrowWay)) {//判断如果是担保标，放款模式为 3.担保还款
			// pOutType = "3";
			// }
			purpose = investDetailMap.get("purpose");
			pDeadLine = String.valueOf(Convert.strToInt(
					investDetailMap.get("raiseTerm"), -1) * 24);
			pTIdentNo = investDetailMap.get("idNo");
			pTRealName = investDetailMap.get("realName");
			pTIpsAcctNo = investDetailMap.get("portMerBillNo");
			tradeNo = investDetailMap.get("tradeNo");
			// 计算借款管理费
			Map<String, String> typeMap = shoveBorrowTypeService
					.queryBorrowTypeLogByNid(investDetailMap.get("nid_log"));
			if (typeMap != null && typeMap.size() > 0) {
				int isDayThe = Convert.strToInt(
						investDetailMap.get("isDayThe"), -1);
				isDayTheFlag = isDayThe;
				int deadline = Convert.strToInt(
						investDetailMap.get("deadline"), -1);
				int locan_month = Convert.strToInt(typeMap.get("locan_month"),
						-1);
				guarantee_fee = Convert.strToDouble(
						typeMap.get("guarantee_fee"), -1);
				double day_fee = Convert
						.strToDouble(typeMap.get("day_fee"), -1);
				double locan_fee = Convert.strToDouble(
						typeMap.get("locan_fee"), -1);
				double locan_fee_month = Convert.strToDouble(
						typeMap.get("locan_fee_month"), -1);
				if (isDayThe == 2) {
					feeRate = (day_fee / 365) / 100;
				} else {
					if (deadline > locan_month) {
						feeRate = (locan_fee + (deadline - locan_month)
								* locan_fee_month) / 100;
					} else {
						feeRate = locan_fee / 100;
					}
				}
			}
		}
		Map<String, String> userMap = userService.queryPersonById(user.getId());// 查询投资人信息
		Map<String, String> user_map = userService.queryUserById(user.getId());

		String pTrdAmt = String.format("%.2f", amountDouble);// 投资金额
		String pTTrdFee = String.format("%.2f", (amountDouble * feeRate)
				+ (amountDouble * (guarantee_fee / 100)));// 借款管理费+担保费
		String pMerBillNo = IPaymentUtil.getIn_orderNo();// 商户流水号

		// 天标处理
		if (isDayTheFlag == 2) {
			// 借款标的信息
			Map<String, String> borrowMap = borrowManageService
					.queryBorrowByid(borrowId);
			// 用户信息
			Map<String, String> userinfoMap = userManageServic
					.queryUserManageInnerMsg(user.getId());
			return this.reFinanceInvestDayThe(borrowId, user.getId(),
					amountDouble, user_map.get("username").toString(), num,
					pMerBillNo, Integer.parseInt(borrowMap.get("version")),
					borrowMap.get("borrowTitle").toString(),
					Integer.parseInt(borrowMap.get("deadline")),
					Double.parseDouble(borrowMap.get("annualRate")) / 12,
					userinfoMap.get("email").toString(),
					userinfoMap.get("telephone").toString());
			// return null;
		}
		// 非天标处理
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", tradeNo);
		map.put("pBidType", pBidType);
		map.put("pOutType", pOutType);
		map.put("pUse", purpose);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pBidDate", DateFormatUtils.format(new Date(), "yyyyMMdd"));
		map.put("pDeadLine", pDeadLine);
		map.put("pAgreementNo", "");
		map.put("pFAcctType", "1");
		map.put("pFIdentType", "1");
		map.put("pFIdentNo", userMap.get("idNo"));
		map.put("pFRealName", user.getRealName());
		map.put("pFIpsAcctNo", user.getPortMerBillNo());
		map.put("pTAcctType", "1");
		map.put("pTIdentType", "1");
		map.put("pTIdentNo", pTIdentNo);
		map.put("pTRealName", pTRealName);
		map.put("pTIpsAcctNo", pTIpsAcctNo);
		map.put("pTrdAmt", pTrdAmt);
		map.put("pFTrdFee", "0.00");
		map.put("pTTrdFee", pTTrdFee);
		map.put("pWebUrl", IPaymentConstants.url + "/reFinanceInvestUrl.do");
		map.put("pS2SUrl", IPaymentConstants.url
				+ "/reAsyncFinanceInvestUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		map.put("idLong", borrowId);
		map.put("num", num);
		map.put("status", status);
		String url = IPaymentConstants.tenderTrade;
		try {
			userService.savehxOrderNo(user.getId(), user.getPortMerBillNo(),
					pMerBillNo, map);// 保存输入参数记录
			sendHtml(IPaymentUtil.createHtml(IPaymentUtil.parseMapToXml(map),
					url, IPayConfig.terraceNoOne, 1, "pMerCode",
					"p3DesXmlPara", "pSign"));
			return null;

			// String str3DesXmlPana = IPaymentUtil.parseMapToXml(map);
			// String url = "http://p2p.ips.net.cn/CreditWeb/tenderTrade.aspx";
			// int statusCode = IPaymentUtil.http(url, str3DesXmlPana);
			// log.info("*******"+statusCode);
			// JSONObject obj = new JSONObject();
			// if (statusCode == 200) {
			// //开始本地操作
			// log.info("投标申请(非流转标)环迅执行成功，开始执行本地操作");
			// double investAmount = Convert.strToDouble(pTrdAmt, -1);
			//
			// Map<String,String> result =
			// financeService.addBorrowInvest(idLong, user.getId(), "",
			// investAmount,getBasePath(),user.getRealName(),status,num,pMerBillNo);
			//
			// String resultMSG = result.get("ret_desc");
			// if("".equals(resultMSG)){
			// //查询是否满标，是否满标自动审核
			// Map<String,String> autoMap =
			// financeService.queryBorrowDetailById(idLong);
			// int auditpass = Convert.strToInt(autoMap.get("auditpass"),2);
			// int borrowStatus= Convert.strToInt(autoMap.get("borrowStatus"),
			// -1);
			// if(borrowStatus == 3){
			// if(auditpass == 1){
			// Admin admin = (Admin)
			// session().getAttribute(IConstants.SESSION_ADMIN);
			// Map<String, String> retMap = borrowManageService
			// .updateBorrowFullScaleStatus(idLong, 4l, "满标自动审核",
			// admin.getId(), getBasePath(), autoMap.get("tradeNo"));
			// long retVal = -1;
			// retVal = Convert.strToLong(retMap.get("ret") + "", -1);
			// session().removeAttribute("randomCode");
			// if (retVal <= 0) {
			// obj.put("msg", retMap.get("ret_desc"));
			// JSONUtils.printObject(obj);
			// return null;
			// } else {
			// obj.put("msg", "1");
			// JSONUtils.printObject(obj);
			// return null;
			// }
			// }
			// }
			// obj.put("msg", 1);
			// }else{
			// obj.put("msg", result.get("ret_desc")+"");
			// }
			// } else {
			// obj.put("msg", "操作失败!");
			// JSONUtils.printObject(obj);
			// return null;
			// }
			// JSONUtils.printObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 投标-天标处理(体验标).
	 * 
	 * @param borrowId
	 *            借款标id.
	 * @param userId
	 *            用户id.
	 * @param investAmount投资金额
	 *            .
	 * @param username
	 *            用户名.
	 * @param num
	 * @param pMerBillNo
	 *            订单.
	 * @param version
	 *            版本号.
	 * @param borrowTitle
	 *            标题.
	 * @param deadline
	 * @param monthRate
	 *            月利率.
	 * @param email
	 *            邮件.
	 * @param telphone
	 *            手机号码.
	 * @return
	 * @throws Exception
	 */
	public String reFinanceInvestDayThe(long borrowId, long userId,
			double investAmount, String username, int num, String pMerBillNo,
			int version, String borrowTitle, int deadline, double monthRate,
			String email, String telphone) throws Exception {
		log.info("天标处理,体验标reFinanceInvestDayThe");
		String pReturn = "操作成功";
		String url = "/finance.do";
		Map<String, String> result = financeService.addExperienceBorrowInvest(
				borrowId, userId, investAmount, getBasePath(), username, num,
				pMerBillNo, version, borrowTitle, deadline, monthRate, email,
				telphone);

		String resultMSG = result.get("ret_desc");
		if ("".equals(resultMSG)) {
			// 查询是否满标，是否满标自动审核
			Map<String, String> autoMap = financeService
					.queryBorrowDetailById(borrowId);
			int auditpass = Convert.strToInt(autoMap.get("auditpass"), 2);
			int borrowStatus = Convert
					.strToInt(autoMap.get("borrowStatus"), -1);
			if (borrowStatus == 3) {
				if (auditpass == 1) {
					Admin admin = (Admin) session().getAttribute(
							IConstants.SESSION_ADMIN);
					Map<String, String> retMap = borrowManageService
							.updateBorrowFullScaleStatus(borrowId, 4l,
									"满标自动审核", admin.getId(), getBasePath(),
									autoMap.get("tradeNo"));
					long retVal = -1;
					retVal = Convert.strToLong(retMap.get("ret") + "", -1);
					session().removeAttribute("randomCode");
					if (retVal <= 0) {
						pReturn = result.get("ret_desc");
						url = "/financeDetail.do?id=" + borrowId;
						getOut().print(
								"<script>alert('" + pReturn
										+ "');parent.location.href='"
										+ request().getContextPath() + "" + url
										+ "';</script>");
						return null;
					} else {
						pReturn = "操作成功!";
						url = "/financeDetail.do?id=" + borrowId;
						getOut().print(
								"<script>alert('" + pReturn
										+ "');parent.location.href='"
										+ request().getContextPath() + "" + url
										+ "';</script>");
						return null;
					}
				}
			}
			pReturn = "操作成功!";
			url = "/financeDetail.do?id=" + borrowId;
		} else {
			pReturn = result.get("ret_desc");
			url = "/financeDetail.do?id=" + borrowId;
		}
		// getOut().print(
		// "<script>alert('" + pReturn + "');parent.location.href='"
		// + request().getContextPath() + "" + url + "';</script>");
		// return null;
		return "<script>alert('" + pReturn + "');parent.location.href='"
				+ request().getContextPath() + "" + url + "';</script>";
	}

	/**
	 * 投标申请回调(非流转标)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reFinanceInvestUrl() throws Exception {
		log.info("投标申请(非流转标)同步回调");
		String pReturn = "操作成功";
		String url = "/finance.do";

		String pErrCode = request("pErrCode");// 投标状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");

		String p3DesXmlPara = request("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");// 商户订单号

		xx("***********投资申请(非流转标)返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			pReturn = "签名失败!";
		} else {
			log.info("投标申请(非流转标)签名成功！");
			// 本地是否操作
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			if (fundMap == null) {
				log.info("投标申请(非流转标)同步执行本地操作");
				if (!pErrCode.equals("0000")) {// 环迅返回开户失败
					pReturn = "操作失败，" + request("pErrMsg");
					url = "/finance.do";
				} else {
					// 开始本地操作
					log.info("投标申请(非流转标)环迅执行成功，开始执行本地操作");
					long idLong = -1;
					long userId = -1;
					String userName = "";
					int status = -1;
					int num = -1;
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
						status = Convert.strToInt(hxBuMapXml.get("status"), -1);
						num = Convert.strToInt(hxBuMapXml.get("num"), -1);
					}
					double investAmount = Convert.strToDouble(
							parseXml.get("pTrdAmt"), -1);

					Map<String, String> result = financeService
							.addBorrowInvest(idLong, userId, "", investAmount,
									getBasePath(), userName, status, num,
									pMerBillNo,"");

					String resultMSG = result.get("ret_desc");
					if ("".equals(resultMSG)) {
						// 查询是否满标，是否满标自动审核
						Map<String, String> autoMap = financeService
								.queryBorrowDetailById(idLong);
						int auditpass = Convert.strToInt(
								autoMap.get("auditpass"), 2);
						int borrowStatus = Convert.strToInt(
								autoMap.get("borrowStatus"), -1);
						if (borrowStatus == 3) {
							if (auditpass == 1) {
								Admin admin = (Admin) session().getAttribute(
										IConstants.SESSION_ADMIN);
								Map<String, String> retMap = borrowManageService
										.updateBorrowFullScaleStatus(idLong,
												4l, "满标自动审核", admin.getId(),
												getBasePath(),
												autoMap.get("tradeNo"));
								long retVal = -1;
								retVal = Convert.strToLong(retMap.get("ret")
										+ "", -1);
								session().removeAttribute("randomCode");
								if (retVal <= 0) {
									pReturn = result.get("ret_desc");
									url = "/financeDetail.do?id=" + idLong;
									getOut().print(
											"<script>alert('"
													+ pReturn
													+ "');parent.location.href='"
													+ request()
															.getContextPath()
													+ "" + url + "';</script>");
									return null;
								} else {
									pReturn = "操作成功!";
									url = "/financeDetail.do?id=" + idLong;
									getOut().print(
											"<script>alert('"
													+ pReturn
													+ "');parent.location.href='"
													+ request()
															.getContextPath()
													+ "" + url + "';</script>");
									return null;
								}
							}
						}
						pReturn = "操作成功!";
						url = "/financeDetail.do?id=" + idLong;
					} else {
						pReturn = result.get("ret_desc");
						url = "/financeDetail.do?id=" + idLong;
					}
				}
			}
		}
		getOut().print(
				"<script>alert('" + pReturn + "');parent.location.href='"
						+ request().getContextPath() + "" + url + "';</script>");
		return null;
	}

	/**
	 * 认购流转标验证
	 * 
	 * @return
	 * @throws Exception
	 */
	public String subscribe() throws Exception {
		JSONObject obj = new JSONObject();
		String id = request("id");
		long idLong = Convert.strToLong(id, -1);
		String result = request("result");
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
		tenderCirculationTrade(idLong, resultLong);
		return null;
	}

	/**
	 * 投标申请(流转标)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String tenderCirculationTrade(long idLong, long resultLong)
			throws Exception {
		User user = (User) session().getAttribute("user");
		String tradeNo = "";
		String moneyPurposes = "";
		String pDeadLine = "";
		String pTIdentNo = "";
		String pTRealName = "";
		String pTIpsAcctNo = "";
		String pTrdAmt = "";
		String pTTrdFee = "";
		double feeRate = 0.00;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, String> userMap = userService.queryPersonById(user.getId());
		Map<String, String> borrowDetailMap = financeService
				.queryBorrowDetailById(idLong);
		if (borrowDetailMap != null && borrowDetailMap.size() > 0) {
			tradeNo = borrowDetailMap.get("tradeNo");
			moneyPurposes = borrowDetailMap.get("moneyPurposes");
			pDeadLine = String.valueOf(Convert.strToInt(
					borrowDetailMap.get("raiseTerm"), -1) * 24);
			Map<String, String> borrowUserMap = userService
					.queryUserById(Convert.strToLong(
							borrowDetailMap.get("publisher"), -1L));
			Map<String, String> borrowPersonMap = userService
					.queryPersonById(Convert.strToLong(
							borrowDetailMap.get("publisher"), -1L));
			pTIdentNo = borrowPersonMap.get("idNo");
			pTRealName = borrowPersonMap.get("realName");
			pTIpsAcctNo = borrowUserMap.get("portMerBillNo");
			double amt = resultLong
					* Convert.strToDouble(
							borrowDetailMap.get("smallestFlowUnit"), 0);
			pTrdAmt = String.format("%.2f", amt);

			Map<String, String> typeMap = shoveBorrowTypeService
					.queryBorrowTypeLogByNid(borrowDetailMap.get("nid_log"));
			if (typeMap != null && typeMap.size() > 0) {
				int isDayThe = Convert.strToInt(
						borrowDetailMap.get("isDayThe"), -1);
				int deadline = Convert.strToInt(
						borrowDetailMap.get("deadline"), -1);
				int locan_month = Convert.strToInt(typeMap.get("locan_month"),
						-1);
				double day_fee = Convert.strToInt(typeMap.get("day_fee"), -1);
				double locan_fee = Convert.strToInt(typeMap.get("locan_fee"),
						-1);
				double locan_fee_month = Convert.strToDouble(
						typeMap.get("locan_fee_lontcna"), -1);
				if (isDayThe == 2) {
					feeRate = (day_fee / 365) / 100;
				} else {
					if (deadline > locan_month) {
						feeRate = (locan_fee + (deadline - locan_month)
								* locan_fee_month) / 100;
					} else {
						feeRate = locan_fee / 100;
					}
				}
			}
			pTTrdFee = String.format("%.2f", amt * feeRate);
		}

		Date date = new Date();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		String pContractNo = IPaymentUtil.getIn_orderNo();
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", pContractNo);
		map.put("pBidType", "8");
		map.put("pOutType", "1");
		map.put("pUse", moneyPurposes);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pBidDate", DateFormatUtils.format(date, "yyyyMMdd"));
		map.put("pDeadLine", pDeadLine);
		map.put("pAgreementNo", "");
		map.put("pFAcctType", "1");
		map.put("pFIdentType", "1");
		map.put("pFIdentNo", userMap.get("idNo"));
		map.put("pFRealName", user.getRealName());
		map.put("pFIpsAcctNo", user.getPortMerBillNo());
		map.put("pTAcctType", "1");
		map.put("pTIdentType", "1");
		map.put("pTIdentNo", pTIdentNo);
		map.put("pTRealName", pTRealName);
		map.put("pTIpsAcctNo", pTIpsAcctNo);
		map.put("pTrdAmt", pTrdAmt);
		map.put("pFTrdFee", "0.00");
		map.put("pTTrdFee", "10");
		map.put("pWebUrl", IPaymentConstants.url + "/reTCTradeUrl.do");
		map.put("pS2SUrl", IPaymentConstants.url + "/reAsyncTCTradeUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		map.put("resultLong", resultLong);
		map.put("idLong", idLong);

		String url = IPaymentConstants.tenderTrade;
		try {
			userService.savehxOrderNo(user.getId(), user.getPortMerBillNo(),
					pMerBillNo, map);
			sendHtml(IPaymentUtil.createHtml(IPaymentUtil.parseMapToXml(map),
					url, IPayConfig.terraceNoOne, 1, "pMerCode",
					"p3DesXmlPara", "pSign"));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 投标申请回调(流转标)
	 * 
	 * @return
	 * @throws Exception
	 */
	public String reTCTradeUrl() throws Exception {
		log.info("投标申请(流转标)同步回调");
		String pReturn = "操作成功";
		String url = "/finance.do";

		String pErrCode = request("pErrCode");// 投标状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		String p3DesXmlPara = request("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");// 商户订单号

		xx("***********投资申请(流转标)返回参数");// 打印返回参数
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			pReturn = "投标申请签名失败!";
		} else {
			log.info("投标申请(流转标)签名成功！");
			// 本地是否操作
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			if (fundMap == null) {
				log.info("投标申请(流转标)同步执行本地操作");
				if (!pErrCode.equals("0000")) {
					pReturn = "操作失败，" + pErrMsg;
					url = "/finance.do";
				} else {
					// 开始本地操作
					log.info("投标申请(流转标)环迅执行成功，开始执行本地操作");
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
						resultLong = Convert.strToInt(
								hxBuMapXml.get("resultLong"), -1);
						tradeNo = hxBuMapXml.get("pBidNo");
						pContractNo = hxBuMapXml.get("pContractNo");
					}
					// 调用投标审核(流转标)接口
					Map<String, String> data = checkTrade(tradeNo, "4",
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
							pReturn = "投标审核签名失败!";
						} else {
							log.info("投标审核(流转标)签名成功！");
							// 本地是否操作
							Map<String, String> fundMap1 = fundManagementService
									.queryFundRecord(pMerBillNoCheck);
							if (fundMap1 == null) {
								log.info("投标审核(流转标)同步执行本地操作");
								if (!pErrCode1.equals("0000")) {
									pReturn = "操作失败，" + pErrMsg1;
									url = "/finance.do";
								} else {
									log.info("投标审核(流转标)环迅执行成功，开始执行本地操作");
									String resultStr = financeService
											.subscribeSubmit(idLong,
													resultLong, userId,
													getBasePath(), userName,
													getPlatformCost(),
													pMerBillNo, pMerBillNoCheck);
									if ("1".equals(resultStr)) {
										pReturn = "操作成功!";
									} else {
										pReturn = resultStr;
									}
									url = "/financeDetail.do?id=" + idLong;
								}
							}
						}
					} else {
						// 环迅没有返回参数，跳转到环迅的维护页面
					}
				}
			}
		}
		getOut().print(
				"<script>alert('" + pReturn + "');window.location.href='"
						+ request().getContextPath() + "" + url + "';</script>");
		return null;
	}

	/**
	 * 投标审核
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> checkTrade(String tradeNo, String status,
			String pContractNo) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		String pBidStatus = "9999";
		if ("4".equals(status)) {
			pBidStatus = "0000";
		}
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", pContractNo);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pBidStatus", pBidStatus);
		map.put("pS2SUrl", IPaymentConstants.url + "/reCheckTradeUrl.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		String str3DesXmlPana = IPaymentUtil.parseMapToXml(map);
		str3DesXmlPana = IpsCrypto.triDesEncrypt(str3DesXmlPana,
				IPayConfig.des_key, IPayConfig.des_iv);
		str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "");
		str3DesXmlPana = str3DesXmlPana.replaceAll("\n", "");
		String strSign = IpsCrypto.md5Sign(IPayConfig.terraceNoOne
				+ str3DesXmlPana + IPayConfig.cert_md5);
		String soap = IPaymentUtil.getSoapAuditTender(str3DesXmlPana,
				IPaymentConstants.auditTender, "argMerCode", "arg3DesXmlPara",
				"argSign", strSign);
		String url = IPayConfig.ipay_web_gateway
				+ IPaymentConstants.auditTender;
		// Map<String, String> data =
		// IPaymentUtil.webService(soap,"AuditTenderResponse");

		Map<String, String> data = IPaymentUtil.webService(soap, url);
		return data;
	}

	/**
	 * 担保交易资金划扣（垫付）
	 * 
	 * @param pBidNo
	 *            标号
	 * @param pContractNo
	 *            合同号
	 * @param pFreezeMerBillNo
	 *            冻结订单号
	 * @param pFIdentNo
	 *            出款方身份证
	 * @param pFRealName
	 *            出款方真实姓名
	 * @param pFIpsAcctNo
	 *            出款方ips账号
	 * @param pTIdentNo
	 *            收款方身份证
	 * @param pTRealName
	 *            收款方真实姓名
	 * @param pTIpsAcctNo
	 *            收款方ips账号
	 * @param pTransferAmt
	 *            划扣金额
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> guaranteeTransfer(String pBidNo,
			String pContractNo, String pFreezeMerBillNo, String pFIdentNo,
			String pFRealName, String pFIpsAcctNo, String pTIdentNo,
			String pTRealName, String pTIpsAcctNo, String pTransferAmt)
			throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();// 划扣订单号

		map.put("pBidNo", pBidNo);
		map.put("pContractNo", pContractNo);
		map.put("pFreezeMerBillNo", pFreezeMerBillNo);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pTransferDate", DateFormatUtils.format(new Date(), "yyyyMMdd"));
		map.put("pTransferType", "1");
		map.put("pFAcctType", "1");
		map.put("pFIdentNo", "360321198408247821");
		map.put("pFRealName", "钱婉慧");
		map.put("pFIpsAcctNo", "4021000029105010");
		map.put("pTAcctType", "1");
		map.put("pTIdentNo", pTIdentNo);
		map.put("pTRealName", pTRealName);
		map.put("pTIpsAcctNo", pTIpsAcctNo);
		map.put("pTransferAmt", pTransferAmt);
		map.put("pS2SUrl", IPaymentConstants.url + "/reguaranteeTransfer.do");
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		String str3DesXmlPana = IPaymentUtil.parseMapToXml(map);
		str3DesXmlPana = IpsCrypto.triDesEncrypt(str3DesXmlPana,
				IPayConfig.des_key, IPayConfig.des_iv);
		str3DesXmlPana = str3DesXmlPana.replaceAll("\r", "");
		str3DesXmlPana = str3DesXmlPana.replaceAll("\n", "");
		String strSign = IpsCrypto.md5Sign(IPayConfig.terraceNoOne
				+ str3DesXmlPana + IPayConfig.cert_md5);
		String soap = IPaymentUtil.getSoapAuditTender(str3DesXmlPana,
				IPaymentConstants.guaranTransfer, "argMerCode",
				"arg3DesXmlPara", "argSign", strSign);
		String url = IPayConfig.ipay_web_gateway
				+ IPaymentConstants.guaranTransfer;
		Map<String, String> data = IPaymentUtil.webService(soap,
				"GuaranteeTransferResponse");
		return data;
	}

	/**
	 * 还款
	 * 
	 * @return
	 * @throws Exception
	 */
	public String toRepayment() throws Exception {
		User user = (User) session().getAttribute("user");
		String id = request("id") == null ? "" : request("id");
		long idLong = Convert.strToLong(id, -1L);
		String bId = request("bId") == null ? "" : request("bId");
		long bIdLong = Convert.strToLong(bId, -1L);
		String needSum = request("needSum") == null ? "" : request("needSum");
		Double excitationSum = 0.00;
		Double borrowAmount = 0.00;
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String tradeNo = "";
		Map<String, String> userMap = userService.queryPersonById(user.getId());
		Map<String, String> tUserMap = userService.queryUserById(user.getId());

		// 判断是否是天标
		Map<String, String> borrowInfoMap = borrowManageService
				.queryBorrowByid(bIdLong);
		int isDayThe = Convert.strToInt(borrowInfoMap.get("isDayThe"), 1);
		if (isDayThe == 2) {
			this.toRepaymentExperienceMoney(idLong);

			return null;
		}

		// 判断用户是否由足够还款可用余额
		if (tUserMap != null) {
			if (Convert.strToDouble(needSum, 0.00) > Convert.strToDouble(
					tUserMap.get("usableSum"), 0.00)) {
				getOut().print(
						"<script>alert('帐户可用余额不足，请及时充值！');parent.location.href='"
								+ request().getContextPath()
								+ "/queryAllDetails.do';</script>");
				return null;
			}
		}
		// 是否第一次还款
		Map<String, String> statusMap = frontMyPaymentService
				.queryInvestStatus(bIdLong);
		int statusCount = 0;
		if (statusMap != null && statusMap.size() > 0) {
			statusCount = Convert.strToInt(statusMap.get("statusCount"), 0);
		}
		Map<String, String> investDetailMap = financeService
				.queryBorrowInvest(bIdLong);
		if (investDetailMap != null && investDetailMap.size() > 0) {
			tradeNo = investDetailMap.get("tradeNo");
			borrowAmount = Convert.strToDouble(
					investDetailMap.get("borrowAmount"), 0.00);
			if (statusCount == 0) {
				String excitationType = investDetailMap.get("excitationType");
				if ("2".equals(excitationType)) {
					excitationSum = Convert.strToDouble(
							investDetailMap.get("excitationSum"), 0.00);
				}
				if ("3".equals(excitationType)) {
					excitationSum = borrowAmount
							* Convert.strToDouble(
									investDetailMap.get("excitationSum"), 0.00)
							* 0.01;
				}
			}
		}
		// 查询所有还款对象（投资人）
		List<Map<String, Object>> investorList = frontMyPaymentService
				.queryRepay(idLong);
		// 循环投资人
		String pDetail = "";
		String pRow = "";
		// 总共罚息
		double sumRecivedFI = 0.00;
		// 平台收取的罚息总和
		double remainRecivedFI = 0.00;
		for (int i = 0; i < investorList.size(); i++) {
			Map<String, Object> investorMap = investorList.get(i);
			Double amt = Convert.strToDouble(investorMap
					.get("recivedPrincipal").toString(), 0)
					+ Convert.strToDouble(investorMap.get("recivedInterest")
							.toString(), 0)
					+ Convert.strToDouble(investorMap.get("recivedFI")
							.toString(), 0) / 2;
			sumRecivedFI = sumRecivedFI
					+ Convert.strToDouble(investorMap.get("recivedFI")
							.toString(), 0);
			remainRecivedFI = remainRecivedFI
					+ Convert.strToDouble(String.format("%.2f", Convert
							.strToDouble(investorMap.get("recivedFI")
									.toString(), 0) / 2), 0);
			Double awardAmount = 0.00;
			if (statusCount == 0) {
				Double investAmount = Convert.strToDouble(
						investorMap.get("investAmount").toString(), 0);
				if (excitationSum > 0) {
					awardAmount = (investAmount / borrowAmount) * excitationSum;
				}
			}
			String pTTrdAmt = String.format("%.2f", amt + awardAmount);
			String pTTrdFee = String.format("%.2f", Convert.strToDouble(
					investorMap.get("iManageFee").toString(), 0));// 投资管理费
			pRow = "<pRow>" + "<pTAcctType>1</pTAcctType>" + "<pTIpsAcctNo>"
					+ investorMap.get("portMerBillNo") + "</pTIpsAcctNo>"
					+ "<pTTrdFee>" + pTTrdFee + "</pTTrdFee>" + "<pTTrdAmt>"
					+ pTTrdAmt + "</pTTrdAmt>" + "</pRow>";
			pDetail = pDetail + pRow;
			pRow = "";
		}
		Date date = new Date();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		needSum = String.format("%.2f",
				(Convert.strToDouble(needSum, 0) + excitationSum));
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", tradeNo);
		map.put("pRepaymentDate", DateFormatUtils.format(date, "yyyyMMdd"));
		map.put("pMerBillNo", pMerBillNo);
		map.put("pMerchantBillNo", "");
		map.put("pFAcctType", "1");
		map.put("pFIdentNo", userMap.get("idNo"));
		map.put("pFRealName", user.getRealName());
		map.put("pFIpsAcctNo", tUserMap.get("portMerBillNo"));
		map.put("pFTrdFee",
				String.format("%.2f", sumRecivedFI - remainRecivedFI));
		map.put("pFTrdAmt", needSum);
		map.put("pWebUrl", this.getBasePath() + "/retoRepayment.do");
		map.put("pS2SUrl", this.getBasePath() + "/reAsynctoRepayment.do");
		map.put("pDetails", pDetail);
		map.put("pMemo1", "1");
		map.put("pMemo2", "2");
		map.put("pMemo3", "3");
		map.put("idLong", idLong);
		String url = IPaymentConstants.repaymentTrade;
		try {
			userService.savehxOrderNo(user.getId(),
					tUserMap.get("portMerBillNo"), pMerBillNo, map);
			sendHtml(IPaymentUtil.createHtml(IPaymentUtil.parseMapToXml(map),
					url, IPayConfig.terraceNoOne, 1, "pMerCode",
					"p3DesXmlPara", "pSign"));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		xx();
		return null;
	}

	/**
	 * 还款(体验标）
	 * 
	 * @throws Exception
	 */
	public String toRepaymentExperienceMoney(long idLong) throws Exception {
		log.info("体验标还款处理");
		// 查出来需要还款的金额
		List<Map<String, Object>> investorList = frontMyPaymentService
				.queryRepay(idLong);
		// 调用第三方奖励接口

		getOut().print(
				"<script>alert('天标还款需要调用第三方处理！');parent.location.href='"
						+ request().getContextPath()
						+ "/queryAllDetails.do';</script>");
		return null;
	}

	/**
	 * 还款回调(体验标）
	 * 
	 * @throws Exception
	 */
	public String reToRepaymentExperienceMoney() throws Exception {
		log.info("体验标还款调用第三方回调");

		return null;
	}

	/**
	 * 还款回调
	 * 
	 * @return
	 * @throws Exception
	 */
	public String retoRepayment() throws Exception {
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
		xx("1");
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			getOut().print(
					"<script>alert('签名失败');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");
		} else {
			// 是否已经异步
			Map<String, String> fundMap = fundManagementService
					.queryFundRecord(pMerBillNo);
			if (fundMap == null) {
				if (!"0000".equals(pErrCode)) {
					getOut().print(
							"<script>alert('" + pErrMsg
									+ "');parent.location.href='"
									+ request().getContextPath() + "" + url
									+ "';</script>");
				}
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
					if (list != null && list.size() > 0) {

						hxBuMapXml = list.get(0);
					}
					idLong = Convert.strToLong(hxBuMapXml.get("idLong"), -1);
					userId = Convert.strToLong(userMap.get("userId"), -1);
				}
				Map<String, String> map = frontMyPaymentService.submitPay(
						idLong, userId, getBasePath(), "", pMerBillNo);
				String result = Convert.strToStr(map.get("ret_desc"), "");
				if (!"".equals(result)) {
					getOut().print(
							"<script>alert('" + result
									+ "');parent.location.href='"
									+ request().getContextPath() + "" + url
									+ "';</script>");
				}
				getOut().print(
						"<script>parent.location.href='"
								+ request().getContextPath() + "" + url
								+ "';</script>");
			}
		}
		getOut().print(
				"<script>parent.location.href='" + request().getContextPath()
						+ "" + url + "';</script>");
		return null;
	}

	/**
	 * 自动投标签约
	 */
	public String pautomaticBidSet() throws Exception {
		User user = (User) request().getSession().getAttribute("user");
		String pIpsBillNo = user.getpIpsBillNo();
		String s = request("s");
		long returnId = -1;
		long bidStatusLong = Convert.strToLong(s, 1);
		returnId = myHomeService.automaticBidSet(bidStatusLong, user.getId());

		if (returnId <= 0) {
			getOut().print(
					"<script>alert('未保存自动投标设置');parent.location.href='"
							+ request().getContextPath()
							+ "/automaticBidInit.do';</script>");
			return null;
		} else {
			if (!"0".equals(pIpsBillNo)) {
				getOut().print(
						"<script>alert('操作成功');parent.location.href='"
								+ request().getContextPath()
								+ "/automaticBidInit.do';</script>");
				return null;
			}
			Map<String, String> userMap = userService.queryPersonById(user
					.getId());
			LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
			String pMerBillNo = IPaymentUtil.getIn_orderNo();
			map.put("pMerBillNo", pMerBillNo);
			map.put("pSigningDate", DateFormatUtils.format(new Date(), "yyyyMMdd"));
			map.put("pAcctType", "1");
			map.put("pIdentNo", userMap.get("idNo"));
			map.put("pRealName", user.getRealName());
			map.put("pIpsAcctNo", user.getPortMerBillNo());
			map.put("pWebUrl", IPaymentConstants.url + "/repautomaticBidSet.do");
			map.put("pS2SUrl", IPaymentConstants.url
					+ "/reAsyncpautomaticBidSet.do");
			map.put("pMemo1", "1");
			map.put("pMemo2", "2");
			map.put("pMemo3", "3");
			map.put("s", s);
			String url = IPaymentConstants.autoSigning;
			try {
				userService.savehxOrderNo(user.getId(),
						user.getPortMerBillNo(), pMerBillNo, map);
				sendHtml(IPaymentUtil.createHtml(
						IPaymentUtil.parseMapToXml(map), url,
						IPayConfig.terraceNoOne, 1, "pMerCode", "p3DesXmlPara",
						"pSign"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			xx();
		}
		return null;
	}

	/**
	 * 自动投标签约回调
	 * 
	 * @return
	 * @throws Exception
	 */
	public String repautomaticBidSet() throws Exception {
		String url = "/automaticBidInit.do";
		String pErrCode = request("pErrCode");// 协议状态
		String pMerCode = request("pMerCode");
		String pErrMsg = request("pErrMsg");
		String p3DesXmlPara = request("p3DesXmlPara");
		Map<String, String> parseXml = IPaymentUtil
				.parseXmlToJson(p3DesXmlPara);
		String pMerBillNo = parseXml.get("pMerBillNo");
		Map<String, String> userMap = userService.quePortUserId(null,
				pMerBillNo);
		String pSign = request("pSign");// 签名
		boolean sign = IPaymentUtil.checkSign(pMerCode, pErrCode, pErrMsg,
				p3DesXmlPara, pSign);
		if (!sign) {
			getOut().print(
					"<script>alert('签名失败');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");
		} else {
			// 签约号是否存在
			Map<String, String> userM = userService.queryUserById(Convert
					.strToLong(userMap.get("userId"), -1));
			if (userM.get("pIpsBillNo").equals("0")) {
				if ("0000".equals(pErrCode)) {
					// 修改用户的签署协议号
					Long result = userService.updatePIpsBillNo(
							userMap.get("userId"), pMerBillNo);
				}
				getOut().print(
						"<script>parent.location.href='"
								+ request().getContextPath() + "" + url
								+ "';</script>");
			}
		}
		getOut().print(
				"<script>parent.location.href='" + request().getContextPath()
						+ "" + url + "';</script>");
		return null;
	}

	private static long getDistanceTime(Date one, Date two) {// 判断相隔多少天。
		long day = 0;
		long time1 = one.getTime();
		long time2 = two.getTime();
		long diff;
		diff = time1 - time2;
		day = diff / (24 * 60 * 60 * 1000);
		return day;
	}

	// 打印参数
	public void xx() {
		Enumeration<String> keyNames = request().getParameterNames();
		while (keyNames.hasMoreElements()) {
			String attrName = keyNames.nextElement();
			paramMap.put(attrName, request(attrName));
			log.info(attrName
					+ "---"
					+ request(attrName)
					+ "]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]");
		}
	}

	/**
	 * 开户相关判断
	 * 
	 * @return
	 */
	public String portUserAcct() throws Exception {
		
		User user = (User) session().getAttribute("user");
		if (user != null) {
			// 获取用户认证进行的步骤
			if (user.getAuthStep() == 1) {
				// 个人信息认证步骤
				return "querBaseData";
			} else if (user.getAuthStep() == 2) {
				// 工作信息认证步骤
				return "querWorkData";
			} else if (user.getAuthStep() == 3 && user.getVipStatus() != 2) {
				// VIP申请认证步骤
				return "quervipData";
			}
			// 返回页面信息
			Map<String, String> personMap = userService.queryPersonById(user
					.getId());
			request().setAttribute("personMap", personMap);
			request().setAttribute("authStep", user.getAuthStep());
		} else {
			return LOGIN;
		}
		return SUCCESS;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public void setMyHomeService(MyHomeService myHomeService) {
		this.myHomeService = myHomeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public void setShoveBorrowTypeService(
			ShoveBorrowTypeService shoveBorrowTypeService) {
		this.shoveBorrowTypeService = shoveBorrowTypeService;
	}

	public void setFrontMyPaymentService(
			FrontMyPaymentService frontMyPaymentService) {
		this.frontMyPaymentService = frontMyPaymentService;
	}

	public HomeInfoSettingService getHomeInfoSettingService() {
		return homeInfoSettingService;
	}

	public void setHomeInfoSettingService(
			HomeInfoSettingService homeInfoSettingService) {
		this.homeInfoSettingService = homeInfoSettingService;
	}

	public ExperiencemoneyService getExperiencemoneyService() {
		return experiencemoneyService;
	}

	public void setExperiencemoneyService(
			ExperiencemoneyService experiencemoneyService) {
		this.experiencemoneyService = experiencemoneyService;
	}

	public UserManageServic getUserManageServic() {
		return userManageServic;
	}

	public void setUserManageServic(UserManageServic userManageServic) {
		this.userManageServic = userManageServic;
	}

	public UserIntegralService getUserIntegralService() {
		return userIntegralService;
	}

	public void setUserIntegralService(UserIntegralService userIntegralService) {
		this.userIntegralService = userIntegralService;
	}

}
