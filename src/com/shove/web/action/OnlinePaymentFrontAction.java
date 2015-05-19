package com.shove.web.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.shove.config.onlinePayConfig;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.RechargeDetailService;
import com.sp2p.service.RechargeService;
import com.sp2p.system.exception.FrontHelpMessageException;



public class OnlinePaymentFrontAction extends BasePageAction{
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(OnlinePaymentFrontAction.class);

	private RechargeDetailService rechargeDetailService;
	private RechargeService rechargeService;
	

	public void setRechargeDetailService(RechargeDetailService rechargeDetailService) {
		this.rechargeDetailService = rechargeDetailService;
	}


	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}


	// 网银在线充值
	public String onlinePayment() throws Exception {
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
		Map<String, String> result = rechargeService.addRecharge(paramMap, 10);
		long nunber = Convert.strToInt(result.get("result"), -1);

		if (nunber != -1) {
			String html = createonlineUrl("在线充值", nunber, userId, bankCode,
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
	/**
	 * 生成订单编号
	 */
	public String createOrderSort(long recharId,String v_mid){
	  String  v_oid = "";
	 SimpleDateFormat sf = new SimpleDateFormat("yyyymmdd");
	  v_oid = sf.format(new Date()).trim()+"-"+v_mid.trim()+"-"+recharId;
	  return v_mid;
	} 
	
	private String createonlineUrl(String body, long recharId, long userId,
			String bankCode, String tranDateTime, BigDecimal money)
			throws Exception {
		log.info("12");
		// 组装接口参数，并进行加密

		String v_mid = onlinePayConfig.v_mid;//商户编号
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String v_oid = sf.format(new Date()).trim()+"-"+v_mid.trim()+"-"+recharId;//不可为空值，订单编号标准格式为：订单生成日期(yyyymmdd)-商户编号-商户流水号。订单编号所有字符总和不可超过64位。
		String v_amount = money + "";//订单总金额
		String v_moneytype = onlinePayConfig.v_moneytype;//币种
		String v_url = onlinePayConfig.v_url;//URL地址(前台)
		String v_md5_key =  onlinePayConfig.v_md5_key;//商户提供md5 - key值
		//MD5加密开始
		String v_md5info = "";//MD5校验码
		StringBuffer md5info = new StringBuffer();
		md5info.append(money);
		md5info.append(v_moneytype);
		md5info.append(v_oid);
		md5info.append(v_mid);
		md5info.append(v_url);
		md5info.append(v_md5_key);
		v_md5info = DigestUtils.md5Hex(md5info.toString().trim());
	    //MD5加密结束
	    String remark1 = userId+"";//备注1
	    String remark2 = recharId+"";//备注2
	    String v_rcvname = "";//收货人姓名
	    String v_rcvaddr ="";//收货人地址
	    String v_rcvtel ="";//收货人电话
	    String v_rcvpost ="";//收货人邮编
	    String v_rcvemail="";//收货人Email
	    String v_rcvmobile ="";//收货人手机号
	    String v_ordername  ="";//订货人姓名
	    String v_orderaddr = "";//订货人地址
	    String v_ordertel = "";//订货人电话
	    String v_orderpost = "";//订货人邮编
	    String v_orderemail = "";//订货人Email
	    String v_ordermobile = "";//订货人手机号
		Map<String, String> Frommap = new HashMap<String, String>();
		Frommap.put("v_mid", v_mid);
		Frommap.put("v_oid", v_oid);
		Frommap.put("v_amount", v_amount);
		Frommap.put("v_moneytype", v_moneytype);
		Frommap.put("v_url", v_url);
		Frommap.put("v_md5info", v_md5info);
		Frommap.put("remark1", remark1);
		Frommap.put("remark2", remark2);
		Frommap.put("v_rcvname", v_rcvname);
		Frommap.put("v_rcvaddr", v_rcvaddr);
		Frommap.put("v_rcvtel", v_rcvtel);
		Frommap.put("v_rcvpost", v_rcvpost);
		Frommap.put("v_rcvemail", v_rcvemail);
		Frommap.put("v_rcvmobile", v_rcvmobile);
		Frommap.put("v_ordername", v_ordername);
		Frommap.put("v_orderaddr", v_orderaddr);
		Frommap.put("v_ordertel", v_ordertel);
		Frommap.put("v_orderpost", v_orderpost);
		Frommap.put("v_orderemail", v_orderemail);
		Frommap.put("v_ordermobile", v_ordermobile);
		log.info("======>MD信息md5info:"+v_md5info);
		/*// 交易代码
		String tranCode = GopayConfig.gopay_tranCode;
		String merchantID = GopayConfig.gopay_merchantID;
		String merOrderNum = recharId + ""; // 订单号 ---- 支付流水号
		String tranAmt = money + "";
		String feeAmt = "0";
		String frontMerUrl = GopayConfig.gopay_frontMerUrl;
		String backgroundMerUrl = GopayConfig.gopay_backgroundMerUrl;
		String tranIP = GopayUtils.getIpAddr(request());
		String verficationCode = GopayConfig.gopay_verficationCode;
		String gopayServerTime = GopayUtils.getGopayServerTime();
	
		Map<String, String> map = new HashMap<String, String>();
		map.put("v_mid", v_mid);
		map.put("v_mid", v_mid);
		//map.put("charset", "2");
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
		String signValue = GopayUtils.md5(plain.toString());
		map.put("signValue", signValue);*/

		return HtmlHelper.buildHtmlForm(Frommap, onlinePayConfig.v_online_gateway, "post");
	}

	/**
	 * 前台调用函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontgroundonlineUrl() throws Exception {
		log.info("1--frontgroundonlineUrl");
		return backgroundonlineUrl();
	}
	
	/**
	 * 在线支付回调函数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String backgroundonlineUrl() throws Exception {
		log.info("1-----backgroundonlineUrl");
        //接受返回值
		String v_oid = request("v_oid");//订单编号
		String v_pstatus = request("v_pstatus");//支付状态
		String v_pstring = request("v_pstring");//支付结果信息
		String v_pmode = request("v_pmode");//支付方式
		//String v_md5str = request("v_md5str");//订单MD5校验码
		String v_amount = request("v_amount");//订单总金额
		String v_moneytype = request("v_moneytype");//币种
		String remark1 = request("remark1");//备注字段1
		String remark2 = request("remark2");//备注字段2
		log.info("v_pstatus支付状态--"+v_pstatus);
      /*if("20".equals(v_pstatus)){
			log.info("long"+result);
			createHelpMessage("订单处理中，请耐心等待！", "返回首页", "index.do");
		}*/
		if("30".equals(v_pstatus)){
			log.info("充值失败30=========================>30");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		
		if (!validateSign()) {
			log.info("充值校验失败=========================>充值校验失败");
			createHelpMessage("支付失败！", "返回首页", "index.do");
		}
		
		
		
		/*String respCode = request("respCode");// 
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
				"utf-8");*/
		String paybank =	v_pmode;
		if (StringUtils.isBlank(paybank)) {// 如果没有银行编号说明是支付宝直接支付的
			paybank = "网银在线充值";
		}
		/*String paybank = GopayConfig.bankMap.get(request("bankCode"));// 
		if (StringUtils.isBlank(paybank)) {// 如果没有银行编号说明是支付宝直接支付的
			paybank = "国付宝充值";
		}*/

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

	/*	String attach = request("merRemark1");
		double money = Convert.strToDouble(request("tranAmt"), 0);
		String in_paynumber = request("merOrderNum");
		long userId = Convert.strToLong(Encrypt.decryptSES(attach,
				GopayConfig.gopay_see_key), -1);*/
		log.info("remark1==============================用户id>+"+remark1);
		log.info("v_amount==============================用户金额>+"+v_amount);
		log.info("v_oid==============================用户流水>+"+v_oid);
		log.info("paybank==============================用户银行>+"+paybank);
		Map<String, String> resultMap = rechargeService.addUseraddmoney(Convert.strToLong(remark1, -1),
				Convert.strToDouble(v_amount,0),remark2, paybank,null);// add null
		String result = resultMap.get("result");
		String description = resultMap.get("description");
		log.info("result==============================充值返回结果>+"+result);
		log.info("description==============================充值返回结果描述>+"+description);

		HttpServletResponse httpServletResponse = ServletActionContext
				.getResponse();
		httpServletResponse.setCharacterEncoding("utf-8");
		PrintWriter pw = httpServletResponse.getWriter();
		String msg = description;
/*		if (!"0".endsWith(result)) {
			log.info("6--");
			pw.println("fail");
			createHelpMessage(msg, "返回首页", "index.do");
		}*/
		msg = "充值成功";
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
		/*StringBuffer plain = new StringBuffer();
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
		String sign = GopayUtils.md5(plain.toString());*/
		String v_md5info = "";//MD5校验码
		StringBuffer md5info = new StringBuffer();
		md5info.append(request("v_oid"));
		md5info.append(request("v_pstatus"));
		md5info.append(request("v_amount"));
		md5info.append(onlinePayConfig.v_moneytype);
		//md5info.append(onlinePayConfig.v_mid);
		md5info.append(onlinePayConfig.v_md5_key);
//		v_md5info = md5.getMD5ofStr(md5info.toString().trim());
		v_md5info = DigestUtils.md5Hex(md5info.toString().trim());		
	    //v_md5info = GopayUtils.md5(md5info.toString().trim());
	    log.info("request(v_oid)==============>"+request("v_oid"));
	    log.info("request(v_pstatus)==============>"+request("v_pstatus"));
	    log.info("request(v_amount)==============>"+request("v_amount"));
	    log.info("v_md5info---校验==============>"+v_md5info);
	    log.info("传过来的---校验==============>"+request("v_md5str"));
		return v_md5info.equals(request("v_md5str"));
	}

}
