package com.shove.web.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.HtmlHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.httpclient.HttpClientHelper;
import com.shove.Convert;
import com.shove.config.GopayConfig;
import com.shove.config.IPayConfig;
import com.shove.data.DataException;
import com.shove.security.Encrypt;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.RechargeDetailService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserService;
import com.sp2p.system.exception.FrontHelpMessageException;


/**
 * 国付宝在线充值
 * 
 * @author Administrator
 * 
 */
public class GoPaymentAction extends BasePageAction {

	private static Log log = LogFactory.getLog(GoPaymentAction.class);

	private UserService userService;

	private RechargeDetailService rechargeDetailService;
	private RechargeService rechargeService;

	private String urlParam = "";// 接口拼接的参数

	// 在线充值
	public String gopayPayment() throws Exception {
		User user = (User) session(IConstants.SESSION_USER);
		if (user == null) {// 未登陆
			return IConstants.ADMIN_AJAX_LOGIN;
		}

		String bankCode = request("bankCode");
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
		int temp = moneyDecimal.compareTo(new BigDecimal("0.01"));// 最小金额为0.01元
		if (temp < 0) {
			return INPUT;
		}
		long userId = this.getUserId();
		// 生成订单
		paramMap.put("rechargeMoney", moneyDecimal + "");
		paramMap.put("userId", userId + "");
		paramMap.put("result", "0");
		Date date = new Date();
		paramMap.put("addTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		Map<String, String> result = rechargeService.addRecharge(paramMap, 3);
		long nunber = Convert.strToInt(result.get("result"), -1);

		if (nunber != -1) {
		
			String html = createGopayUrl("在线充值", nunber, userId, bankCode,
					DateFormatUtils.format(date, "yyyyMMddHHmmss"), moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
			sendHtml(html);
			return null;
		} else {
			createHelpMessage("支付失败！" + result.get("description"), "返回首页",
					"index.do");
			return null;
		}

	}

	public String gopayQuery() throws Exception {
		User user = (User) session(IConstants.SESSION_USER);
		if (user == null) {// 未登陆
			return IConstants.ADMIN_AJAX_LOGIN;
		}

		String bankCode = request("bankCode");
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
		int temp = moneyDecimal.compareTo(new BigDecimal("0.01"));// 最小金额为0.01元
		if (temp < 0) {
			return INPUT;
		}
		
		long userId = this.getUserId();
		Date date = new Date();
		String html = createGopayQuery("在线充值", 1, userId, bankCode,
				DateFormatUtils.format(date, "yyyyMMddHHmmss"), moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
		sendHtml(html);
		return null;

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
		/* helpMessage.setTitle("用户不存在"); */
		helpMessage.setMsg(new String[] { "返回首页" });
		helpMessage.setUrl(new String[] { "index" });
		helpMessage.setTitle(title);
		/*
		 * helpMessage.setMsg(new String[]{msg}); helpMessage.setUrl(new
		 * String[]{url});
		 */
		throw new FrontHelpMessageException();
	}

	private String createGopayUrl(String body, long recharId, long userId,
			String bankCode, String tranDateTime, BigDecimal money)
			throws Exception {
		log.info("12");
		// 组装接口参数，并进行加密

		String version = GopayConfig.gopay_version;
		String charset = GopayConfig.gopay_input_charset;
		String language = "";
		String signType = GopayConfig.gopay_signtype;
		// 交易代码
		String tranCode = GopayConfig.gopay_tranCode;
		String merchantID = GopayConfig.gopay_merchantID;
		String merOrderNum = recharId + ""; // 订单号 ---- 支付流水号
		String tranAmt = money + "";
		String feeAmt = "0";
		String frontMerUrl = GopayConfig.gopay_frontMerUrl;
		String backgroundMerUrl = GopayConfig.gopay_backgroundMerUrl;
		String tranIP = ServletHelper.getIpAddress(request());
		String verficationCode = GopayConfig.gopay_verficationCode;
		Map<String, Object> httpMap = new HashMap<String, Object>();
		httpMap.put("cookiePolicy", CookiePolicy.RFC_2109);
		httpMap.put("timeout", 10000);
		httpMap.put("url", GopayConfig.gopay_server_time_url);
		httpMap.put("contentCharset", "GBK");
		String gopayServerTime = HttpClientHelper.executeForString(httpMap);
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("version", version);
		map.put("charset", "2");
		map.put("language", "1");
		map.put("signType", "1");
		map.put("tranCode", tranCode);
		map.put("merchantID", merchantID);
		map.put("merOrderNum", merOrderNum);
		map.put("tranAmt", tranAmt);
		map.put("feeAmt", feeAmt);
		map.put("tranDateTime", tranDateTime);
		map.put("frontMerUrl", frontMerUrl);
		map.put("backgroundMerUrl", backgroundMerUrl);
		map.put("currencyType", "156");
		map.put("virCardNoIn", GopayConfig.gopay_virCardNoIn);
		if (!"DEFAULT".equals(bankCode)) {
			map.put("bankCode", bankCode);
			map.put("userType", "1");
		} else {
			map.put("bankCode", bankCode);
		}

		map.put("orderId", "");
		map.put("gopayOutOrderId", "");
		map.put("tranIP", tranIP);
		map.put("respCode", "");
		map.put("VerficationCode", verficationCode);
		map.put("gopayServerTime", gopayServerTime);
		map.put("merRemark1", com.shove.security.Encrypt.encryptSES(
				userId + "", GopayConfig.gopay_see_key));
		
		// 组织加密明文

		StringBuffer plain = new StringBuffer();
		plain.append("version=[");
		plain.append(version);
		plain.append("]tranCode=[");
		plain.append(tranCode);
		plain.append("]merchantID=[");
		plain.append(merchantID);
		plain.append("]merOrderNum=[");
		plain.append(merOrderNum);
		plain.append("]tranAmt=[");
		plain.append(tranAmt);
		plain.append("]feeAmt=[");
		plain.append(feeAmt);
		plain.append("]tranDateTime=[");
		plain.append(tranDateTime);
		plain.append("]frontMerUrl=[");
		plain.append(frontMerUrl);
		plain.append("]backgroundMerUrl=[");
		plain.append(backgroundMerUrl);
		plain.append("]orderId=[]gopayOutOrderId=[]tranIP=[");
		plain.append(tranIP);
		plain.append("]respCode=[]gopayServerTime=[");
		plain.append(gopayServerTime);
		plain.append("]VerficationCode=[");
		plain.append(verficationCode);
		plain.append("]");
		log.info("plain==>" + plain);
		String signValue = DigestUtils.md5Hex(plain.toString().getBytes(GopayConfig.gopay_input_charset));
		map.put("signValue", signValue);

		return HtmlHelper.buildHtmlForm(map, GopayConfig.gopay_gateway, "post");
	}
	
	private String createGopayQuery(String body, long recharId, long userId,
			String bankCode, String tranDateTime, BigDecimal money)
			throws Exception {
		// 组装接口参数，并进行加密
		String tranCode = GopayConfig.gopay_query_tranCode;
		String merchantID = GopayConfig.gopay_merchantID;
		String merOrderNum = recharId + ""; // 订单号 ---- 支付流水号
		String tranIP = ServletHelper.getIpAddress(request());
		String verficationCode = GopayConfig.gopay_verficationCode;
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("tranCode",tranCode);
		map.put("tranDateTime",tranDateTime);
		map.put("merOrderNum",merOrderNum);
		map.put("merchantID",merchantID);
		map.put("orgOrderNum",merOrderNum);
		map.put("orgtranDateTime","20131106135701");
		map.put("orgTxnType","");
		map.put("orgtranAmt","");
		map.put("orgTxnStat","");
		map.put("authID","");
		map.put("isLocked","");
		map.put("respCode","");
		map.put("tranIP", tranIP);
		map.put("msgExt","");
		// 组织加密明文
		StringBuffer plain = new StringBuffer();
		plain.append("tranCode=["+tranCode);
		plain.append("]merchantID=["+merchantID);
		plain.append("]merOrderNum=["+merOrderNum);
		plain.append("]tranAmt=[");
		plain.append("]ticketAmt=[]tranDateTime=["+tranDateTime);
		plain.append("]currencyType=[");
		plain.append("]merURL=[");
		plain.append("]customerEMail=[");
		plain.append("]authID=[");
		plain.append("]orgOrderNum=["+merOrderNum);
		plain.append("]orgtranDateTime=["+"20131106135701");
		plain.append("]orgtranAmt=[");
		plain.append("]orgTxnType=[");
		plain.append("]orgTxnStat=[");
		plain.append("]msgExt=[]virCardNo=[]virCardNoIn=[");
		plain.append("]tranIP=[" + tranIP);
		plain.append("]isLocked=[]feeAmt=[]respCode=[");
		plain.append("]VerficationCode=[" + verficationCode + "]");
		
		String signValue = DigestUtils.md5Hex(plain.toString().getBytes("UTF-8"));;
		map.put("signValue", signValue);

		return HtmlHelper.buildHtmlForm(map, GopayConfig.gopay_gateway, "post");
	}

	/**
	 * 前台调用函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontMerUrl() throws Exception {
		log.info("1--frontMerUrl");
		return backgroundMerUrl();
	}

	/**
	 * 国付宝回调函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String backgroundMerUrl() throws Exception {
		log.info("1-----backgroundMerUrl");

		String respCode = request("respCode");// 
		log.info("2--" + respCode);
		if (!"0000".equals(respCode) && !"9999".equals(respCode)) {
			log.info("3--");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		if ("9999".equals(respCode)) {
			log.info("4--");
			createHelpMessage("订单处理中，请耐心等待！", "返回首页", "index.do");
		}
		String merchantID = request("merchantID");// 商户号
		log.info("4--" + merchantID);
		if (!validateSign()) {
			log.info("5--validate sign fail");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}

		// 国付宝支付编号
		String orderId = URLDecoder.decode(request("orderId"), "utf-8");

		// 交易完成时间
		String tranFinishTime = URLDecoder.decode(request("tranFinishTime"),
				"utf-8");

		String paybank = GopayConfig.bankMap.get(request("bankCode"));// 
		if (StringUtils.isBlank(paybank)) {// 如果没有银行编号说明是支付宝直接支付的
			paybank = "国付宝充值";
		}

		// Map<String, Object> map = new HashMap<String, Object>();
		// map.put("merOrderNum", request("merOrderNum"));
		// map.put("tranAmt", request("tranAmt"));
		// map.put("feeAmt", request("feeAmt"));
		// map.put("orderId", orderId);
		// map.put("tranFinishTime", tranFinishTime);
		// map.put("buyerName", request("buyerName"));
		// map.put("merRemark1", com.shove.security.Encrypt.decryptSES(
		// request("merRemark1"), GopayConfig.gopay_see_key));
		// map.put("signValue", request("signValue"));
		// map.put("paybank", paybank);

		String attach = request("merRemark1");
		double money = Convert.strToDouble(request("tranAmt"), 0);
		String in_paynumber = request("merOrderNum");
		long userId = Convert.strToLong(Encrypt.decryptSES(attach,
				GopayConfig.gopay_see_key), -1);
		Map<String, String> resultMap = rechargeService.addUseraddmoney(userId,
				money, in_paynumber, paybank,null); // add null
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
			createHelpMessage(msg, "返回首页", "index.do");
		}
		msg = "充值成功";
		pw.println("success");
		log.info("7--");
		createHelpMessage(msg + "", "返回首页", "index.do");

		return "message";

	}
	
	public String merUrl() throws Exception {
		log.info("1-----backgroundMerUrl");

		String respCode = request("respCode");// 
		log.info("2--" + respCode);
		
		createHelpMessage(respCode + "", "返回首页", "index.do");
		return "message";
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

	/**
	 * 返回验证参数
	 * 
	 * @return
	 */
	public boolean validateSign() {
		StringBuffer plain = new StringBuffer();
		plain.append("version=[");
		plain.append(request("version"));
		plain.append("]tranCode=[");
		plain.append(request("tranCode"));
		plain.append("]merchantID=[");
		plain.append(request("merchantID"));
		plain.append("]merOrderNum=[");
		plain.append(request("merOrderNum"));
		plain.append("]tranAmt=[");
		plain.append(request("tranAmt"));
		plain.append("]feeAmt=[");
		plain.append(request("feeAmt"));
		plain.append("]tranDateTime=[");
		plain.append(request("tranDateTime"));
		plain.append("]frontMerUrl=[");
		plain.append(request("frontMerUrl"));
		plain.append("]backgroundMerUrl=[");
		plain.append(request("backgroundMerUrl"));
		plain.append("]orderId=[");
		plain.append(request("orderId"));
		plain.append("]gopayOutOrderId=[");
		plain.append(request("gopayOutOrderId"));
		plain.append("]tranIP=[");
		plain.append(request("tranIP"));
		plain.append("]respCode=[");
		plain.append(request("respCode"));
		plain.append("]gopayServerTime=[]VerficationCode=[");
		plain.append(GopayConfig.gopay_verficationCode);
		plain.append("]");
		String sign = null;
		try {
			sign = DigestUtils.md5Hex(plain.toString().getBytes(GopayConfig.gopay_input_charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sign.equals(request("signValue"));
	}

	public String getUrlParam() {
		return urlParam;
	}

	public void setUrlParam(String urlParam) {
		this.urlParam = urlParam;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setRechargeDetailService(
			RechargeDetailService rechargeDetailService) {
		this.rechargeDetailService = rechargeDetailService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

}
