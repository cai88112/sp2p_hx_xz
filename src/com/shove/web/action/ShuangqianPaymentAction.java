package com.shove.web.action;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.HtmlHelper;
import com.fp2p.helper.shuangqian.ShuangqianSignHelper;
import com.shove.Convert;
import com.shove.config.GopayConfig;
import com.shove.config.IPayConfig;
import com.shove.config.ShuangqianpayConfig;
import com.shove.data.DataException;
import com.shove.security.Encrypt;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.RechargeDetailService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserService;
import com.sp2p.system.exception.FrontHelpMessageException;


/**
 * 双乾在线充值
 * 
 * @author Administrator
 * 
 */
public class ShuangqianPaymentAction extends BasePageAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(ShuangqianPaymentAction.class);

	private UserService userService;

	private RechargeDetailService rechargeDetailService;
	private RechargeService rechargeService;

	private String urlParam = "";// 接口拼接的参数

	// 在线充值
	public String sqpayPayment() throws Exception {
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
		paramMap.put("addTime", DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss"));
		Map<String, String> result = rechargeService.addRecharge(paramMap, 3);
		long nunber = Convert.strToInt(result.get("result"), -1);

		if (nunber != -1) {

			String html = createShuangqpayUrl("在线充值", nunber, userId, bankCode,
					DateFormatUtils.format(date, "yyyyMMddHHmmss"), moneyDecimal);// paymentId_orderId_userId:支付类型(在线支付/在线充值)_订单编号/_用户编号
			sendHtml(html);
			return null;
		} else {
			createHelpMessage("支付失败！" + result.get("products"), "返回首页",
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
//开始拼接双乾的表单参数
	private String createShuangqpayUrl(String body, long recharId, long userId,
			String bankCode, String tranDateTime, BigDecimal money)
			throws Exception {
		log.info("ShuangqpayUrl");
		// 组装接口参数，并进行加密
		String MerNo=ShuangqianpayConfig.sqpay_MerNo;//商户号
		String BillNo = recharId+"";//订单号
		String  Amount=money.doubleValue()+"";//金额，精确到小数点后两位，元为单位
		log.info(Amount); 
		String PaymentType=bankCode;//支付银行类型
		String PayType=ShuangqianpayConfig.sqpay_PayType;//支付方式
		String ReturnURL=ShuangqianpayConfig.sqpay_ReturnURL;//返回的结果URLNotifyURL
		String NotifyURL=ShuangqianpayConfig.sqpay_NotifyURL;//服务端后台支付状态通知.
//	    ShuangqianSignHelper md5util = new ShuangqianSignHelper();//参数加密串
		
	    String  MD5info = ShuangqianSignHelper.signMap(new String[]{MerNo,BillNo,Amount+"",ReturnURL}, ShuangqianpayConfig.sqpay_MD5key, "REQ");
        log.info(MD5info);
        String products="双乾在线充值";


		Map<String, String> map = new HashMap<String, String>();
		map.put("MerNo", MerNo);
		map.put("BillNo", BillNo);
		map.put("Amount", Amount+"");
		map.put("PaymentType", PaymentType);
		map.put("PayType", PayType);
		map.put("ReturnURL", ReturnURL);
		map.put("MD5info", MD5info);
		map.put("NotifyURL", NotifyURL);
		map.put("MerRemark", userId+","+PaymentType);
		map.put("products", products);
		
		
		return HtmlHelper.buildHtmlForm(map, ShuangqianpayConfig.sqpay_gateway, "post");
	}

	/**
	 * 前台调用函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontSqMerUrl() throws Exception {
		log.info("1--frontMerUrl");
		return backgroundSqMerUrl();
	}

	/**
	 * 双乾回调函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String backgroundSqMerUrl() throws Exception {
		log.info("1-----backgroundMerUrl");

		String Result = request("Succeed");// 
		log.info("2--" + Result);
		if (!"88".equals(Result)) {
			log.info("3--");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		String merchantID =ShuangqianpayConfig.sqpay_MerNo;// 商户号
		log.info("4--" + merchantID);
		if (!validateSign()) {
			log.info("5--validate sign fail");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		
		String paybank = ShuangqianpayConfig.bankMap.get(request("MerRemark").split(",")[1]);// 
		double money = Convert.strToDouble(request("Amount"), 0);
		
		String in_paynumber = request("BillNo");
		long userId = Convert.strToLong(request("MerRemark").split(",")[0], -1);
		Map<String, String> resultMap = rechargeService.addUseraddmoney(userId,
				money, in_paynumber, paybank,null); // add null
		String result = resultMap.get("result");
		String description ="充值成功";

		HttpServletResponse httpServletResponse = ServletActionContext
				.getResponse();
		httpServletResponse.setCharacterEncoding("utf-8");
		PrintWriter pw = httpServletResponse.getWriter();
		String msg = description;
		log.info("14--"+result);
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
		log.info("返回参数");
		String CharacterEncoding = "UTF-8";
		HttpServletRequest request=ServletActionContext.getRequest();
	    try {
			request.setCharacterEncoding(CharacterEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String MerNo =ShuangqianpayConfig.sqpay_MerNo;
	    String MD5key = ShuangqianpayConfig.sqpay_MD5key;
	    String BillNo = request("BillNo");	
	    String Amount = request("Amount");
	    String Succeed = request("Succeed");
	    String  Result = request("Result"); 
	    String MD5info = request("MD5info");
	   	String md5str;
	   	
//	   	ShuangqianSignHelper md5util = new ShuangqianSignHelper();
			md5str = ShuangqianSignHelper.signMap(new String[]{MerNo,BillNo,Amount,String.valueOf(Succeed)}, MD5key, "RES");
		return MD5info.equals(md5str);
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
