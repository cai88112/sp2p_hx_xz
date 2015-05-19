package com.shove.web.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.HtmlHelper;
import com.shove.Convert;
import com.shove.config.BaopayConfig;
import com.shove.config.GopayConfig;
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
public class BaoPaymentAction extends BasePageAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static Log log = LogFactory.getLog(BaoPaymentAction.class);


	private RechargeDetailService rechargeDetailService;
	private RechargeService rechargeService;

	private String urlParam = "";// 接口拼接的参数

	// 在线充值
	public String baopayPayment() throws Exception {
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

		String MerchantID = BaopayConfig.baopay_merchantID;
		String Md5key =  BaopayConfig.baopay_key;
		String PayID =  BaopayConfig.baopay_payID;
		String NoticeType =BaopayConfig.baopay_noticeType;
		String TransID = recharId + ""; // 订单号 ---- 支付流水号
		String OrderMoney = money.multiply(new BigDecimal(100)) + "";//订单总金额 以分为单位，至少大于等于1
		
		String Merchant_url = BaopayConfig.baopay_frontMerUrl;
		String Return_url = BaopayConfig.baopay_backgroundMerUrl;
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("MerchantID", MerchantID);
		map.put("PayID", PayID);
		map.put("NoticeType",NoticeType);
		map.put("TransID", TransID);
		map.put("OrderMoney", OrderMoney);
		map.put("Merchant_url", Merchant_url);
		map.put("Return_url", Return_url);
		map.put("TradeDate", tranDateTime);
		map.put("ProductName", "");
		map.put("Amount", "1");
		map.put("ProductLogo", "");
		map.put("Username", "");
		map.put("Email", "");
		map.put("Mobile", "");
		map.put("AdditionalInfo", userId+","+bankCode);
		
		String md5 =new String( MerchantID+PayID+tranDateTime+TransID+OrderMoney+Merchant_url+Return_url+NoticeType+Md5key);//MD5签名格式	
		log.info(md5); 
		//计算MD5值
		String Md5Sign = DigestUtils.md5Hex(md5);
		// 组织加密明文

		map.put("Md5Sign", Md5Sign);
			log.info(Md5Sign); 
		return HtmlHelper.buildHtmlForm(map, BaopayConfig.baopay_gateway, "post");
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
	 * 宝付回调函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String backgroundMerUrl() throws Exception {
		log.info("1-----backgroundMerUrl");

		String Result = request("Result");// 
		log.info("2--" + Result);
		if (!"1".equals(Result)) {
			log.info("3--");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		
		String merchantID = request("merchantID");// 商户号
		log.info("4--" + merchantID);
		if (!validateSign()) {
			log.info("5--validate sign fail");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}


		String paybank = BaopayConfig.bankMap.get(request("additionalInfo").split(",")[1]);// 
//		if (StringUtils.isBlank(paybank)) {// 如果没有银行编号说明是支付宝直接支付的
//			paybank = "国付宝充值";
//		}

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

	//	String attach = request("additionalInfo");
		
		double money = Convert.strToDouble(request("factMoney"), -1)/100;
		String in_paynumber = request("TransID");//订单号
	log.info(request("factMoney")); 
		long userId = Convert.strToLong(request("additionalInfo").split(",")[0], -1);
		Map<String, String> resultMap = rechargeService.addUseraddmoney(userId,
				money, in_paynumber, paybank,null);//add null
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
		//StringBuffer plain = new StringBuffer();
		String MerchantID=request("MerchantID");
		String TransID=request("TransID");
		String Result=request("Result");
		String resultDesc=request("resultDesc");
		
		String factMoney=request("factMoney");
		String additionalInfo=request("additionalInfo");
		String SuccTime=request("SuccTime");
		String md5key =  BaopayConfig.baopay_key;
		
		String md5 = MerchantID +TransID+Result+resultDesc+factMoney+additionalInfo+SuccTime+md5key;
		
		//计算MD5值
		String Md5Sign = DigestUtils.md5Hex(md5);
		return Md5Sign.equals(request("Md5Sign"));
	}

	public String getUrlParam() {
		return urlParam;
	}

	public void setUrlParam(String urlParam) {
		this.urlParam = urlParam;
	}


	public void setRechargeDetailService(
			RechargeDetailService rechargeDetailService) {
		this.rechargeDetailService = rechargeDetailService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

}
