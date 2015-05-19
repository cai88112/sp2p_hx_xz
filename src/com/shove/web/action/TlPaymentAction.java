package com.shove.web.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.HtmlHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.config.GopayConfig;
import com.shove.config.TlpayConfig;
import com.shove.data.DataException;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.RechargeDetailService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserService;
import com.sp2p.system.exception.FrontHelpMessageException;


/**
 * 通联在线充值
 * 
 * @author Administrator
 * 
 */
public class TlPaymentAction extends BasePageAction {

	private static Log log = LogFactory.getLog(TlPaymentAction.class);

	private UserService userService;

	private RechargeDetailService rechargeDetailService;
	private RechargeService rechargeService;

	private String urlParams= "";// 接口拼接的参数

	// 在线充值
	public String tlpayPayment() throws Exception {
		User user = (User) session(IConstants.SESSION_USER);
		if (user == null) {// 未登陆
			return IConstants.ADMIN_AJAX_LOGIN;
		}
		String money = SqlInfusionHelper.filteSqlInfusion(request("money"));
		if (StringUtils.isBlank(money)) {// 判断是否为空
			return INPUT;
		}
		BigDecimal moneyDecimal= null;
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
		/**
		 * 生成订单            
		 * 把订单加入到数据库中 
		 */
		paramMap.put("rechargeMoney", moneyDecimal + "");
		paramMap.put("userId", userId + "");
		paramMap.put("result", "0");
		paramMap.put("rechargeType", "6");
		String bankName = TlpayConfig.bankMap.get(SqlInfusionHelper.filteSqlInfusion(request("bankCode")));
		paramMap.put("bankName", bankName);
		Date date = new Date();
		paramMap.put("addTime", DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
		long result = rechargeService.addRechargeTl(paramMap);
		if (result != -1) {

			//第三方的接口
			String html =createTlpayUrl(result+"", "在线充值",userId,SqlInfusionHelper.filteSqlInfusion(request("bankCode")),
					DateFormatUtils.format(date, "yyyyMMddHHmmss"), moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
			this.sendHtml(html);
			return null;
		} else {
			createHelpMessage("支付失败！", "返回首页", "index.do");
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
/**
 * 创建支付连接
 */
private String createTlpayUrl(String out_trade_no, String body,long userId,String bankCode,
		String tranDateTime, BigDecimal money) throws Exception {
	log.info("13");
	// 组装接口参数，并进行加密
	String frontMerUrl = TlpayConfig.tlpay_pickupUrl;
	String backgroundMerUrl = TlpayConfig.tlpay_receiveUrl;
	String charset = TlpayConfig.tlpay_inputCharset;
	String version=TlpayConfig.tlpay_version;
	String language = TlpayConfig.tlpay_language;
	String signType = TlpayConfig.tlpay_signType;
	String merchantId=TlpayConfig.tlpay_merchantId;
	String key=TlpayConfig.tlpay_key;
	String orderNo = out_trade_no;//订单号
    Long orderAmount1=money.multiply(new BigDecimal(100)).longValue();
    String orderAmount=orderAmount1+"";
	String orderDatetime = tranDateTime;//订单的时间
	int payType=Integer.parseInt(TlpayConfig.tlpay_payType);
	String payType1=payType+"";
	String myParas=out_trade_no+","+money;//自己定义的参数
	
	// 交易代码

//	String plain ="version="+version+"&" +"language="+language+"&" +"signType="+signType+"&" 
//	+"merchantId="+merchantId+"&" +"merOrderNum="+orderNo+"&" +"orderAmount="+orderAmount+"&" 
//	 +"orderDatetime="+orderDatetime+"&"+"payType="+payType+"&" 
//	 +"myParas="+myParas+"&inputCharset="+charset;
//	log.info("plain=>>>>>>"+plain); 
	com.allinpay.ets.client.RequestOrder requestOrder = new com.allinpay.ets.client.RequestOrder();
	requestOrder.setInputCharset(Integer.parseInt(charset));
	requestOrder.setPickupUrl(frontMerUrl);
	requestOrder.setReceiveUrl(backgroundMerUrl);
	requestOrder.setVersion(version);
	if(null!=language&&!"".equals(language)){  
		requestOrder.setLanguage(Integer.parseInt(language));
	}
	requestOrder.setSignType(Integer.parseInt(signType));
	requestOrder.setMerchantId(merchantId);
	requestOrder.setOrderNo(orderNo);
	requestOrder.setOrderAmount(orderAmount1);
	requestOrder.setOrderDatetime(orderDatetime);
	requestOrder.setPayType(payType);
	requestOrder.setExt1(myParas);
	requestOrder.setKey(key); //key为MD5密钥，密钥是在通联支付网关会员服务网站上设置。
	Map<String, String> map = new HashMap<String, String>();
//	String frontMerUrl= TlpayConfig.tlpay_pickupUrl;
//	String backgroundMerUrl= TlpayConfig.tlpay_receiveUrl;
//	String strSrcMsg = requestOrder.getSrc();//获取原串
	String strSignMsg = requestOrder.doSign();//获取签名
//	log.info(strSignMsg); 
//	log.info(strSrcMsg); 
	map.put("Pid", "");
	map.put("productNum", "");
	map.put("payType", payType1);
	map.put("productId", "");
	map.put("version", version);
	map.put("language", language);
	map.put("signType", signType);
	map.put("merchantId", merchantId);
	map.put("orderNo", orderNo);
	map.put("orderAmount", orderAmount);
	map.put("payerTelephone", "");
	map.put("payerIDCard", "");
	map.put("orderDatetime", orderDatetime);
	map.put("pickupUrl", frontMerUrl);
	map.put("receiveUrl", backgroundMerUrl);
	map.put("payerName", "");
	map.put("payerEmail", "");
	map.put("productDescription","");
	map.put("orderCurrency", "");
	map.put("orderExpireDatetime", "");
	map.put("productName", "");
	map.put("productPrice","");
	map.put("ext1", myParas);
	map.put("ext2", "");
	map.put("issuerId", "");
	map.put("pan", "");
	map.put("inputCharset",TlpayConfig.tlpay_inputCharset);
	map.put("signMsg", strSignMsg);
	return HtmlHelper.buildHtmlForm(map, TlpayConfig.tlpay_gateway, "post"); 
}
/**
 * 前台调用函数
 * @return
 * @throws Exception
 */
public String frontMerUrl() throws Exception{
	log.info("1--frontMerUrl");
	return backgroundMerUrl();
}
/**
 * 回调函数
 * @return
 * @throws Exception
 */
public String backgroundMerUrl() throws Exception {
	log.info("1-----backgroundMerUrl");
	//验签是商户为了验证接收到的报文数据确实是支付网关发送的。
	//构造订单结果对象，验证签名。
//	com.allinpay.ets.client.PaymentResult paymentResult = new com.allinpay.ets.client.PaymentResult();
//	paymentResult.setCertPath("c:/TLCert.cer"); 
	String payResult = SqlInfusionHelper.filteSqlInfusion(request("payResult"));// 
	log.info("2--"+payResult);
	if("0".equals(payResult)){
		log.info("3--");
		createHelpMessage("支付失败！", "返回首页", "index.do");
	}
	
	if (!validateSign()) {
		log.info("5--validate sign fail");
		createHelpMessage("支付失败！", "返回首页", "index.do");
	}
	
	
	//交易完成时间payDatetime
	String tranFinishTime = URLDecoder.decode(SqlInfusionHelper.filteSqlInfusion(request("payDatetime")), "utf-8");
	String payAmount1=SqlInfusionHelper.filteSqlInfusion(request("payAmount"));
	double payAmount=Convert.strToDouble(payAmount1, 0)/100;
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("orderId",SqlInfusionHelper.filteSqlInfusion(request("orderNo")));//订单的编号
	map.put("realAmount",payAmount+"");//支付金额
	map.put("feeAmt", "0");
	map.put("paynumber", "");
	map.put("paymentOrderId", SqlInfusionHelper.filteSqlInfusion(request("paymentOrderId")));//支付编号
	map.put("tranFinishTime",tranFinishTime);
	map.put("buyerName", "");
	map.put("attach", "");
	map.put("paybank", "");
	
	int returnId = 0;
	 try {
		returnId = rechargeDetailService.userPay(map);
	} catch (SQLException e) {
		log.error(e);
		e.printStackTrace();
		throw e;
	} catch (DataException e) {
		log.error(e);
		e.printStackTrace();
		throw e;
	}
	HttpServletResponse httpServletResponse = ServletActionContext
			.getResponse();
	httpServletResponse.setCharacterEncoding("utf-8");
	PrintWriter pw = httpServletResponse.getWriter();
	String msg = "";
	if (returnId <= 0) {
		log.info("6--");
		pw.println("fail");
		if (returnId == -1) {
			msg = "支付失败";
		} else if (returnId == -2) {
			msg = "订单编号错误";
		} else if (returnId == -3) {
			msg = "用户编号错误";
		} else if (returnId == -4) {
			msg = "充值成功";
		} else if (returnId == -5) {
			msg = "充值金额不相等细";
		} else if (returnId == -6) {
			msg = "支付详细不存在";
		} else if (returnId == -7) {
			msg = "订单支付明细，状态修改失败。";
		} else {
			msg = "操作错误！";
		}
		createHelpMessage(msg, "返回首页", "index.do");
	}
	
	msg = "交易成功！";
	pw.println("success");
	log.info("7--");
	createHelpMessage(msg + "", "返回首页", "index.do");
	
	return "message";

}

/**
 * 返回验证参数
 * 
 * @return
 */
public boolean validateSign() {
	//订单的返回值
	log.info("订单的返回值");
	String merchantId=SqlInfusionHelper.filteSqlInfusion(request("merchantId"));
	String version=SqlInfusionHelper.filteSqlInfusion(request("version"));
	String language=SqlInfusionHelper.filteSqlInfusion(request("language"));
	String signType=SqlInfusionHelper.filteSqlInfusion(request("signType"));
	String payType=SqlInfusionHelper.filteSqlInfusion(request("payType"));
	String issuerId=SqlInfusionHelper.filteSqlInfusion(request("issuerId"));
	String paymentOrderId=SqlInfusionHelper.filteSqlInfusion(request("paymentOrderId"));
	String orderNo=SqlInfusionHelper.filteSqlInfusion(request("orderNo"));
	String orderDatetime=SqlInfusionHelper.filteSqlInfusion(request("orderDatetime"));
	String orderAmount=SqlInfusionHelper.filteSqlInfusion(request("orderAmount"));
	String payDatetime=SqlInfusionHelper.filteSqlInfusion(request("payDatetime"));
	String payAmount=SqlInfusionHelper.filteSqlInfusion(request("payAmount"));
	String ext1=SqlInfusionHelper.filteSqlInfusion(request("ext1"));
	String ext2=SqlInfusionHelper.filteSqlInfusion(request("ext2"));
	String payResult=SqlInfusionHelper.filteSqlInfusion(request("payResult"));
	String errorCode=SqlInfusionHelper.filteSqlInfusion(request("errorCode"));
	String returnDatetime=SqlInfusionHelper.filteSqlInfusion(request("returnDatetime"));
	String signMsg=SqlInfusionHelper.filteSqlInfusion(SqlInfusionHelper.filteSqlInfusion(request("signMsg")));
	com.allinpay.ets.client.PaymentResult paymentResult = new com.allinpay.ets.client.PaymentResult();
	paymentResult.setMerchantId(merchantId);
	paymentResult.setVersion(version);
	paymentResult.setLanguage(language);
	paymentResult.setSignType(signType);
	paymentResult.setPayType(payType);
	paymentResult.setIssuerId(issuerId);
	paymentResult.setPaymentOrderId(paymentOrderId);
	paymentResult.setOrderNo(orderNo);
	paymentResult.setOrderDatetime(orderDatetime);
	paymentResult.setOrderAmount(orderAmount);
	paymentResult.setPayDatetime(payDatetime);
	paymentResult.setPayAmount(payAmount);
	paymentResult.setExt1(ext1);
	paymentResult.setExt2(ext2);
	paymentResult.setPayResult(payResult);
	paymentResult.setErrorCode(errorCode);
	paymentResult.setReturnDatetime(returnDatetime);
	log.info("good");
	//signMsg为服务器端返回的签名值。
	paymentResult.setSignMsg(signMsg); 
	//signType为"1"时，必须设置证书路径。
	paymentResult.setCertPath(TlPaymentAction.class.getResource("/").getPath()+"TLCert.cer"); 
	
	//验证签名：返回true代表验签成功；否则验签失败。
	boolean verifyResult = paymentResult.verify();
	
	//验签成功，还需要判断订单状态，为"1"表示支付成功。
	boolean paySuccess = verifyResult && payResult.equals("1");
	
	return paySuccess;
}
	
	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RechargeDetailService getRechargeDetailService() {
		return rechargeDetailService;
	}

	public void setRechargeDetailService(RechargeDetailService rechargeDetailService) {
		this.rechargeDetailService = rechargeDetailService;
	}

	public RechargeService getRechargeService() {
		return rechargeService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public String getUrlParams() {
		return urlParams;
	}

	public void setUrlParams(String urlParams) {
		this.urlParams = urlParams;
	}

}
