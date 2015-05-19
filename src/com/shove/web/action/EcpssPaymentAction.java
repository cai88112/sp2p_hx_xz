package com.shove.web.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
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
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.config.EcpssPayConfig;
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
 * 汇潮在线充值
 * 
 * @author Administrator
 * 
 */
public class EcpssPaymentAction extends BasePageAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(EcpssPaymentAction.class);


	private RechargeService rechargeService;

	private String urlParam = "";// 接口拼接的参数

	// 在线充值
	public String escpssPayment() throws Exception {
		User user = (User) session(IConstants.SESSION_USER);
		if (user == null) {// 未登陆
			return IConstants.ADMIN_AJAX_LOGIN;
		}

		String bankCode = SqlInfusionHelper.filteSqlInfusion(request("bankCode"));
		String money = SqlInfusionHelper.filteSqlInfusion(request("money"));
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
		paramMap.put("bankName", bankCode);
		Date date = new Date();
		paramMap.put("addTime", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		Map<String, String> result = rechargeService.addRecharge(paramMap, 3);
		long nunber = Convert.strToInt(result.get("result"), -1);

		if (nunber != -1) {
			String html = createEcpsspayUrl("在线充值", nunber, userId, bankCode,
					DateFormatUtils.format(date, "yyyyMMddHHmmss"), moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
			sendHtml(html);
			return null;
		} else {
			createHelpMessage("支付失败！" + result.get("description"), "返回首页",
					"index.do");
			return null;
		}

	}

	/*public String ecpssPayQuery() throws Exception {
		User user = (User) session(IConstants.SESSION_USER);
		if (user == null) {// 未登陆
			return IConstants.ADMIN_AJAX_LOGIN;
		}

		String bankCode = SqlInfusion.FilteSqlInfusion(request("bankCode"));
		String money = SqlInfusion.FilteSqlInfusion(request("money"));
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
		String html = createEcpsspayQuery("在线充值", 1, userId, bankCode,
					DateUtil.YYYYMMDDHHMMSS.format(date), moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
		sendHtml(html);
		return null;

	}
	*/
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

	private String createEcpsspayUrl(String body, long recharId, long userId,
			String bankCode, String tranDateTime, BigDecimal money)
			throws Exception {
		log.info("12");
		// 组装接口参数，并进行加密
		/**
		 * BillNo订单号
		 * 商户号 MerNo
		 * Amount 金额 以元为单位
		 * ReturnURL
		 * AdviceURL
		 * 签名信息SignInfo
		 * 交易时间orderTime
		 * defaultBankNumber银行编码
		 * Remark 备注信息
		 * products 商品名字
		 */
		String MerNo = EcpssPayConfig.ecpssPay_merchantID;
		long BillNo=recharId;
		BigDecimal Amount=money;
		String ReturnURL=EcpssPayConfig.ecpssPay_frontMerUrl;
		String AdviceURL=EcpssPayConfig.ecpssPay_backgroundMerUrl;
		String orderTime=tranDateTime;
		String defaultBankNumber=bankCode;
//		String Remark="";
		String products="";
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("MerNo", MerNo);
		map.put("BillNo", BillNo+"");
		map.put("Amount", Amount+"");
		map.put("ReturnURL",ReturnURL);
		map.put("AdviceURL", AdviceURL);
		map.put("orderTime", orderTime);
		map.put("defaultBankNumber",defaultBankNumber);
		map.put("Remark", userId+","+bankCode);
		map.put("products", products);
		if (!"DEFAULT".equals(bankCode)) {
			map.put("bankCode", bankCode);
			map.put("userType", "1");
		} else {
			map.put("bankCode", bankCode);
		}
		
		// 组织加密明文
//		MerNo&BillNo&Amount&ReturnURL&MD5key
		String MD5key=EcpssPayConfig.ecpssPay_mdkey;
		String md5src;  //加密字符串    
	    md5src = MerNo +"&"+ BillNo +"&"+ Amount +"&"+ ReturnURL +"&"+ MD5key ;
	  //MD5检验结果
	    String SignInfo = DigestUtils.md5Hex(md5src);
	    map.put("SignInfo", SignInfo);
	

		return HtmlHelper.buildHtmlForm(map, EcpssPayConfig.ecpssPay_gateway, "post");
	}
	
	/*private String createEcpsspayQuery(String body, long recharId, long userId,
			String bankCode, String tranDateTime, BigDecimal money)
			throws Exception {
		// 组装接口参数，并进行加密
		*//**
		 * 接口参数
		 * MerNo   BillNo  Amount  ReturnURL  AdviceURL  SignInfo
		 * orderTime   defaultBankNumber  Remark  products
		 *//*
		String tranCode = GopayConfig.gopay_query_tranCode;
		String merchantID = GopayConfig.gopay_merchantID;
		String merOrderNum = recharId + ""; // 订单号 ---- 支付流水号
		String tranIP = GopayUtils.getIpAddr(request());
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
		
		String signValue = GopayUtils.sign(plain.toString());
		map.put("signValue", signValue);

		return FormUtil.buildHtmlForm(map, GopayConfig.gopay_gateway, "post");
	}
*/
	/**
	 * 前台调用函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontEccpssMerUrl() throws Exception {
		log.info("1--frontMerUrl");
		return backgroundEccpssMerUrl();
	}

	/**
	 * 汇潮回调函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String backgroundEccpssMerUrl() throws Exception {
		log.info("1-----backgroundMerUrl");

		String Succeed = request("Succeed");// 
		log.info("2--" + Succeed);
		if (!"88".equals(Succeed) ) {
			log.info("3--");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		
		String merchantID = request("merchantID");// 商户号
		log.info("4--" + merchantID);
		if (!validateSign()) {
			log.info("5--validate sign fail");
			createHelpMessage("支付失败！", "返回首页", "index.do");
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

		String attach = "";
		double money = Convert.strToDouble(request("Amount"), 0);//交易金额
		long in_paynumber = Convert.strToLong(request("BillNo"), -1);//订单号
		Map<String, String> resultMap1=rechargeService.getRechargeDetail(in_paynumber);
		 String userId="";
		if (resultMap1!=null) {
			  userId=resultMap1.get("userId");
		}
		long userId2=Convert.strToLong(userId, -1);

		String paybank = EcpssPayConfig.bankMap.get(resultMap1.get("bankName"));// 
		if (StringUtils.isBlank(paybank)) {// 如果没有银行编号说明是支付宝直接支付的
			paybank = "汇潮充值";
		}

		Map<String, String> resultMap = rechargeService.addUseraddmoney(userId2,
				money, in_paynumber+"", paybank,null); //add null
		String result = resultMap.get("result");
		String description = resultMap.get("description ");

		HttpServletResponse httpServletResponse = ServletActionContext
				.getResponse();
		httpServletResponse.setCharacterEncoding("utf-8");
		PrintWriter pw = httpServletResponse.getWriter();
		String msg = description;
		if ("0".equals(result)) {
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
		log.info("validateSign================");
		StringBuffer plain = new StringBuffer();
		//BillNo +"&"+ Amount +"&"+ Succeed +"&"+ MD5key ;
		plain.append(SqlInfusionHelper.filteSqlInfusion(request("BillNo")));
		plain.append(SqlInfusionHelper.filteSqlInfusion(request("Amount")));
		plain.append(SqlInfusionHelper.filteSqlInfusion(request("Succeed")));
		plain.append(EcpssPayConfig.ecpssPay_mdkey);
		String MD5info=SqlInfusionHelper.filteSqlInfusion(request("MD5info"));
		log.info("返回的====MD5info*****"+MD5info);
	  //MD5检验结果
	    String SignInfo = DigestUtils.md5Hex(plain.toString());
	    log.info("plain======>>>>>>>"+plain);
	    log.info("加密后的====SignInfo++++"+SignInfo);
		return MD5info.equals(SignInfo);
	}

	public String getUrlParam() {
		return urlParam;
	}

	public void setUrlParam(String urlParam) {
		this.urlParam = urlParam;
	}

	public RechargeService getRechargeService() {
		return rechargeService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}
}
