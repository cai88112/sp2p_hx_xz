/**   
 * @Title: QianduoduoPayAction.java 
 * @Package com.shove.web.action 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2014年12月31日 下午6:01:58 
 * @version V1.0   
 */
package com.shove.web.action;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.BeanHelper;
import com.fp2p.helper.DateHelper;
import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.google.gson.Gson;
import com.ips.security.utility.IpsCrypto;
import com.shove.Convert;
import com.shove.config.IPayConfig;
import com.shove.config.QianduoduoConfig;
import com.shove.data.dao.MySQL;
import com.shove.services.QianduoduoPayService;
import com.shove.services.QianduoduoPayUtil;
import com.shove.shuanquanUtil.Common;
import com.shove.shuanquanUtil.HttpClientUtil;
import com.shove.shuanquanUtil.MD5;
import com.shove.shuanquanUtil.RsaHelper;
import com.shove.vo.LoanInfoBean;
import com.shove.vo.LoanInfoSecondaryBean;
import com.shove.vo.LoanRechangeBean;
import com.shove.vo.LoanRegisterbindBean;
import com.shove.vo.LoanTransferBean;
import com.shove.vo.LoantransferauditBean;
import com.shove.vo.LoanwithdrawBean;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.admin.ProtectOldUserDao;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.database.Dao.Views.intentionfund_user;
import com.sp2p.entity.Admin;
import com.sp2p.entity.User;
import com.sp2p.service.BorrowService;
import com.sp2p.service.ExperiencemoneyService;
import com.sp2p.service.FinanceService;
import com.sp2p.service.FrontMyPaymentService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserIntegralService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;
import com.sp2p.service.admin.ProtectOldUserService;
import com.sp2p.service.admin.UserManageServic;

/**
 * 乾多多同步接口.
 * 
 * @ClassName: QianduoduoPayAction
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2014年12月31日 下午6:01:58
 * 
 */
public class QianduoduoPayAction extends BasePageAction {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * 
	 * @since JDK 1.7
	 */
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(QianduoduoPayAction.class);
	private UserService userService;
	private UserIntegralService userIntegralService;
	private QianduoduoPayService qianduoduoPayService;
	private FundManagementService fundManagementService;
	private FinanceService financeService;
	private ProtectOldUserDao protectOldUserDao;
	private ExperiencemoneyService experiencemoneyService;
	private UserManageServic userManageServic;
	private BorrowManageService borrowManageService;
	private BorrowService borrowService;
	private RechargeService rechargeService;
	private FrontMyPaymentService frontpayService;
	private ProtectOldUserService protectOldUserService;

	/**
	 * 开户
	 * 
	 * @return pMerBillNo商户开户流水号 pIdentType证件类型 pIdentNo 证件号码 pRealName真实姓名
	 *         pMobileNo手机号码
	 * @throws Exception
	 */
	public String registerbind() throws Exception {
		User user = (User) request().getSession().getAttribute("user");

		String pReturn = "绑定失败";
		if (user == null) {
			pReturn = "未登录，请登录系统再次绑定！";
			String url = "/login.do";
			getOut().print(
					"<script>alert('" + pReturn + "');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");
			return null;
		}
		String SubmitURL = "";
		// 判断正式还是测试环境
		if ("2".equals(IConstants.ISDEMO)) {
			// 正式
			SubmitURL = QianduoduoConfig.submitURLPrefix
					+ QianduoduoPayConstants.createIpsAcct;
		} else {
			// 测试
			SubmitURL = QianduoduoConfig.submitURLPrefix_test
					+ QianduoduoPayConstants.createIpsAcct;
		}
		// 回调地址
		// String returnURL = QianduoduoPayConstants.url.substring(0,
		// QianduoduoPayConstants.url.lastIndexOf("/"))
		// + "/registerbindReturn.do";
		String returnURL = QianduoduoPayConstants.url
				+ "/registerbindReturn.do";

		// 通知地址
		// String notifyURL = QianduoduoPayConstants.url.substring(0,
		// QianduoduoPayConstants.url.lastIndexOf("/"))
		// + "/registerbindNotify.do";
		String notifyURL = QianduoduoPayConstants.url
				+ "/registerbindNotify.do";

		// 订单号
		String orderNo = QianduoduoPayUtil.getOrderNo("");
		// 时间戳
		String randomTimeStamp = "";
		if ("1".equals(QianduoduoConfig.antistate)) {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			randomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
		}
		// 第三方开户参数
		LoanRegisterbindBean loanRegisterbindBean = new LoanRegisterbindBean();
		loanRegisterbindBean.setAccountType("");
		loanRegisterbindBean.setEmail(paramMap.get("activeEmail"));
		loanRegisterbindBean.setIdentificationNo(paramMap.get("cadkey"));
		loanRegisterbindBean.setImage1("");
		loanRegisterbindBean.setImage2("");
		loanRegisterbindBean.setLoanPlatformAccount(orderNo);
		loanRegisterbindBean.setMobile(paramMap.get("mobileNo"));
		loanRegisterbindBean.setNotifyURL(notifyURL);
		loanRegisterbindBean
				.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);

		loanRegisterbindBean.setRandomTimeStamp("");

		loanRegisterbindBean.setRealName(paramMap.get("realName"));
		loanRegisterbindBean.setRegisterType(QianduoduoConfig.registerType);
		loanRegisterbindBean.setRemark1("平台开户");
		loanRegisterbindBean.setRemark2("");
		loanRegisterbindBean.setRemark3("");
		loanRegisterbindBean.setReturnURL(returnURL);

		String dataStr = QianduoduoConfig.registerType
				+ loanRegisterbindBean.getAccountType()
				+ loanRegisterbindBean.getMobile()
				+ loanRegisterbindBean.getEmail()
				+ loanRegisterbindBean.getRealName()
				+ loanRegisterbindBean.getIdentificationNo()
				+ loanRegisterbindBean.getImage1()
				+ loanRegisterbindBean.getImage2()
				+ loanRegisterbindBean.getLoanPlatformAccount()
				+ loanRegisterbindBean.getPlatformMoneymoremore()
				+ loanRegisterbindBean.getRandomTimeStamp()
				+ loanRegisterbindBean.getRemark1()
				+ loanRegisterbindBean.getRemark2()
				+ loanRegisterbindBean.getRemark3()
				+ loanRegisterbindBean.getReturnURL()
				+ loanRegisterbindBean.getNotifyURL();
		// 签名
		RsaHelper rsa = RsaHelper.getInstance();
		MD5 md5 = new MD5();
		if ("1".equals(QianduoduoConfig.antistate)) {
			dataStr = md5.getMD5Info(dataStr);
		}
		String signInfo = rsa.signData(dataStr,
				QianduoduoConfig.privateKeyPKCS8);
		loanRegisterbindBean.setSignInfo(signInfo);

		// 转为map
		Map<String, String> loanRegisterbindMap = BeanHelper
				.beanToMap(loanRegisterbindBean);
		Map<String, String> loanRegisterbindMap2 = new HashMap<String, String>();
		for (String key : loanRegisterbindMap.keySet()) {
			char[] keyCharArr = key.toCharArray();
			keyCharArr[0] = Character.toUpperCase(keyCharArr[0]);
			String newStr = new String(keyCharArr);
			loanRegisterbindMap2.put(newStr, loanRegisterbindMap.get(key));
		}
		// 存入订单记录
		userService.saveQianduoduoOrderNo(user.getId(), "",
				loanRegisterbindMap.get("loanPlatformAccount"),
				loanRegisterbindMap);
		// 全自动
		if ("1".equals(QianduoduoConfig.registerType)) {

			pReturn = qianduoduoPayService.registerbind(user, paramMap,
					loanRegisterbindMap2, SubmitURL);

			session().setAttribute("user", user);
			getOut().print("<script>alert('" + pReturn + "');</script>");
			return null;
		} else {
			// 半自动
			request().setAttribute("loanRegisterbindMap", loanRegisterbindMap2);
			request().setAttribute("SubmitURL", SubmitURL);
			return SUCCESS;
		}
	}

	/**
	 * 投标申请校验
	 * 
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
		long borrowId = Convert.strToLong(id, -1);
		String amount = paramMap.get("amount") == null ? "" : paramMap
				.get("amount");
		double amountDouble = Convert.strToDouble(amount, 0);// 投资的金额
		String hasPWD = SqlInfusionHelper.filteSqlInfusion(""
				+ session().getAttribute("hasPWD"));
		String investPWD = paramMap.get("investPWD") == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(paramMap.get("investPWD"));
		int status = Convert.strToInt(paramMap.get("subscribes"), 2);
		if (user.getAuthStep() < 5) {
			String pReturn = "未开通第三方账户!";
			String url = "/portUserAcct.do";
			getOut().print(
					"<script>alert('" + pReturn + "');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");

			return null;
		}
		if (borrowId == -1) {
			// 非法操作直接返回
			obj.put("msg", IConstants.ACTOIN_ILLEGAL);
			JSONHelper.printObject(obj);
			return null;
		}
		Map<String, String> investMaps = financeService
				.getInvestStatus(borrowId);
		if (investMaps == null || investMaps.size() <= 0) {
			// 不满足投标条件,返回
			obj.put("msg", "该借款投标状态已失效");
			JSONHelper.printObject(obj);
			return null;
		}

		Map<String, String> investDetailMap = financeService
				.queryBorrowInvest(borrowId);
		int isDayThe = 0;
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
					borrowId, investPWD);
			if (investPWDMap == null || investPWDMap.size() == 0) {
				obj.put("msg", "投标密码错误");
				JSONHelper.printObject(obj);
				return null;
			}
		}
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
			isDayThe = Convert.strToInt(investDetailMap.get("isDayThe"), -1);
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
		}
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

		String rsString = "";
		
		// if (rsString != null) {
		// obj.put("msg", rsString);
		// obj.put("dateThe", "1");
		// }
		// JSONHelper.printObject(obj);
		if(user.getIsprivate()==1){
			LoanTransferBean loanTransferBean = new LoanTransferBean();
			//String uuid=UUID.randomUUID().toString().replaceAll("-", "");
			String LoanJsonList = "";
			String purpose = "";// 借款用途
			String pTIpsAcctNo = "";// 借款人IPS账号
			String tradeNo = "";// 标的流水号

			Map<String, String> investDetailMap1 = financeService
					.queryBorrowInvest(borrowId);
			if (investDetailMap1 != null && investDetailMap1.size() > 0) {

				purpose = investDetailMap1.get("purpose");
				pTIpsAcctNo = investDetailMap1.get("portMerBillNo");
				tradeNo = investDetailMap1.get("tradeNo");
			}

			String pTrdAmt = String.format("%.2f", amountDouble);// 投资金额
			String pMerBillNo = QianduoduoPayUtil.getOrderNo("");// 商户流水号
			String borrowAmount = investDetailMap1.get("borrowAmount");

			List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
			LoanInfoBean loanInfoBean = new LoanInfoBean();
			loanInfoBean.setLoanOutMoneymoremore(user.getPortMerBillNo());// 付款人乾多多标志
			loanInfoBean.setLoanInMoneymoremore(pTIpsAcctNo);// 收款人乾多多标志
			loanInfoBean.setOrderNo(pMerBillNo);// 商户流水号
			loanInfoBean.setBatchNo(String.valueOf(borrowId));// 网贷平台标号 标id
			loanInfoBean.setExchangeBatchNo("");// 流转标标号
			loanInfoBean.setAdvanceBatchNo("");// 垫资标号
			loanInfoBean.setAmount(pTrdAmt);// 投资金额
			loanInfoBean.setFullAmount(borrowAmount);// 满标标额
			loanInfoBean.setTransferName(purpose);// 借款用途
			loanInfoBean.setRemark("1");
			loanInfoBean.setSecondaryJsonList("");// 二次转账
			listmlib.add(loanInfoBean);
			// 编码
			LoanJsonList = new Gson().toJson(listmlib);
			loanTransferBean.setLoanJsonList(LoanJsonList);// 转账列表
			loanTransferBean
					.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 平台乾多多标识
			loanTransferBean.setTransferAction(1);// 1.投标2.还款3.其他
			loanTransferBean.setAction(2);// 1.手动转账2.自动转账
			loanTransferBean.setTransferType(2);// 1.桥连2.直连
			loanTransferBean.setNeedAudit("");// 空.需要审核1.自动通过
			loanTransferBean.setRemark1("");
			loanTransferBean.setRemark2("");
			loanTransferBean.setRemark3("");
			String randomTimeStamp = "";
			if ("1".equals(QianduoduoConfig.antistate)) {
				Date d = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				randomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
			}
			loanTransferBean.setRandomTimeStamp(randomTimeStamp);

			String dataStr = loanTransferBean.getLoanJsonList()
					+ loanTransferBean.getPlatformMoneymoremore()
					+ loanTransferBean.getTransferAction()
					+ loanTransferBean.getAction()
					+ loanTransferBean.getTransferType()
					+ loanTransferBean.getNeedAudit()
					+ loanTransferBean.getRandomTimeStamp()
					+ loanTransferBean.getRemark1() + loanTransferBean.getRemark2()
					+ loanTransferBean.getRemark3()
					+ loanTransferBean.getReturnURL()
					+ loanTransferBean.getNotifyURL();
			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}
			loanTransferBean.setSignInfo(rsa.signData(dataStr,
					QianduoduoConfig.privateKeyPKCS8));

			// 编码
			loanTransferBean.setLoanJsonList(URLEncoder.encode(
					loanTransferBean.getLoanJsonList(), "utf-8"));
			Map<String, String> loanTransferMap = BeanHelper
					.beanToMap(loanTransferBean);
			userService.saveQianduoduoOrderNo(user.getId(),
					user.getPortMerBillNo(), pMerBillNo, loanTransferMap);
			Long investorId = -1L;
			double amount1 = 0.0;
			Gson gson = new Gson();
			List<Map<String, String>> list= gson
					.fromJson(
							LoanJsonList,
							new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
							}.getType());

			for (Map<String, String> map : list) {
				investorId = Convert.strToLong(
						map.get("Remark"), -1);
				amount1 = Convert.strToDouble(map.get("Amount"),
						0.00);
				Long borrowid = Convert.strToLong(
						map.get("BatchNo"), -1);
				String orderNoLoanJson = map.get("OrderNo");
					Connection conn = MySQL.getConnection();
					try {
						StringBuffer command = new StringBuffer();
						command.append("update t_user set usableSum = ifnull(usableSum, 0.00) + "+amount1+" where id = "+investorId+";");
						long result = MySQL.executeNonQuery(conn, command.toString());
						long returnId = -1;
						command=new StringBuffer();
						command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
						command.append(" select "+investorId+",now(), -1 ,'双乾支付',"+amount1+", a.usableSum, a.freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0), a.dueoutSum,"+investorId+",'平台补偿老米投标利息',");
						command.append(" 0,-1,'双乾支付',"+amount1+","+amount1+","+borrowId+",'"+orderNoLoanJson+"' ");
						command.append(" from t_user a left join t_invest b on a.id = b.investor where a.id = "+investorId+" group by a.id;");
						returnId = MySQL.executeNonQuery(conn, command.toString());
						conn.commit();
					} catch (Exception e) {
						log.error(e);
						conn.rollback();
					} finally {
						conn.close();
					}
				}
			Map<String, String> resultMap = qianduoduoPayService
					.tenderTradeDeal(list.get(0), getBasePath());
			getOut().print(
					"<script>alert('" + resultMap.get("pReturn")
							+ "');parent.location.href='"
							+ request().getContextPath() + ""
							+ resultMap.get("url") + "';</script>");
			return null;
		}else{
			if (isDayThe == 2) {
				// 天标
				rsString = this.tenderTradeDayThe(borrowId, amountDouble, num);
			} else {
				// 其他标
				rsString = this.tenderTrade(borrowId, amountDouble, num, status);
			}
			return rsString;//小号
		}
	}

	/**
	 * 投标申请(非流转标,非天标)
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

		LoanTransferBean loanTransferBean = new LoanTransferBean();
		String LoanJsonList = "";
		String purpose = "";// 借款用途
		String pTIpsAcctNo = "";// 借款人IPS账号
		String tradeNo = "";// 标的流水号

		Map<String, String> investDetailMap = financeService
				.queryBorrowInvest(borrowId);
		if (investDetailMap != null && investDetailMap.size() > 0) {

			purpose = investDetailMap.get("purpose");
			pTIpsAcctNo = investDetailMap.get("portMerBillNo");
			tradeNo = investDetailMap.get("tradeNo");
		}

		String pTrdAmt = String.format("%.2f", amountDouble);// 投资金额
		String pMerBillNo = QianduoduoPayUtil.getOrderNo("");// 商户流水号
		String borrowAmount = investDetailMap.get("borrowAmount");

		List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();
		LoanInfoBean loanInfoBean = new LoanInfoBean();
		loanInfoBean.setLoanOutMoneymoremore(user.getPortMerBillNo());// 付款人乾多多标志
		loanInfoBean.setLoanInMoneymoremore(pTIpsAcctNo);// 收款人乾多多标志
		loanInfoBean.setOrderNo(pMerBillNo);// 商户流水号
		loanInfoBean.setBatchNo(String.valueOf(borrowId));// 网贷平台标号 标id
		loanInfoBean.setExchangeBatchNo("");// 流转标标号
		loanInfoBean.setAdvanceBatchNo("");// 垫资标号
		loanInfoBean.setAmount(pTrdAmt);// 投资金额
		loanInfoBean.setFullAmount(borrowAmount);// 满标标额
		loanInfoBean.setTransferName(purpose);// 借款用途
		loanInfoBean.setRemark("1");
		loanInfoBean.setSecondaryJsonList("");// 二次转账
		listmlib.add(loanInfoBean);
		// 编码
		LoanJsonList = new Gson().toJson(listmlib);

		// 转账接口参数
		loanTransferBean.setLoanJsonList(LoanJsonList);// 转账列表
		loanTransferBean
				.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 平台乾多多标识
		loanTransferBean.setTransferAction(1);// 1.投标2.还款3.其他
		loanTransferBean.setAction(1);// 1.手动转账2.自动转账
		loanTransferBean.setTransferType(2);// 1.桥连2.直连
		loanTransferBean.setNeedAudit("");// 空.需要审核1.自动通过
		loanTransferBean.setRemark1("");
		loanTransferBean.setRemark2("");
		loanTransferBean.setRemark3("");
		// 回调地址
		loanTransferBean.setReturnURL(QianduoduoPayConstants.url
				+ "/tenderTradeReturn.do");

		// 通知地址
		loanTransferBean.setNotifyURL(QianduoduoPayConstants.url
				+ "/tenderTradeNotify.do");
		// 调用转账接口
		Map<String, String> loanTransferMap = qianduoduoPayService
				.loanTransfer(loanTransferBean, pMerBillNo, user, listmlib);

		// 手动转账
		if (loanTransferMap != null) {
			request().setAttribute("loanTransferMap", loanTransferMap);
			return SUCCESS;
		}

		// 自动转账
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
	 * @param num
	 * @param pMerBillNo
	 *            订单.
	 * @return
	 * @throws Exception
	 */
	private String tenderTradeDayThe(long borrowId, double investAmount, int num)
			throws Exception {
		log.info("天标处理,体验标reFinanceInvestDayThe");
		User user = (User) request().getSession().getAttribute("user");
		// 借款标的信息
		Map<String, String> borrowMap = borrowManageService
				.queryBorrowByid(borrowId);
		// 用户信息
		Map<String, String> userinfoMap = userManageServic
				.queryUserManageInnerMsg(user.getId());
		Map<String, String> user_map = userService.queryUserById(user.getId());
		long userId = user.getId();
		String username = user_map.get("username").toString();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();// 商户流水号;
		int version = Integer.parseInt(borrowMap.get("version"));
		String borrowTitle = borrowMap.get("borrowTitle").toString();
		int deadline = Integer.parseInt(borrowMap.get("deadline"));
		double monthRate = Double.parseDouble(borrowMap.get("annualRate")) / 12;
		String email = userinfoMap.get("email").toString();
		String telphone = userinfoMap.get("telephone").toString();

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
		return "<script>alert('" + pReturn + "');parent.location.href='"
				+ request().getContextPath() + "" + url + "';</script>";
	}

	/**
	 * 充值.
	 * 
	 * @Title: recharge
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public String loanRecharge() {

		User user = (User) request().getSession().getAttribute("user");
		JSONObject obj = new JSONObject();
		if (user == null) {
			return "noLogin";
		}
		String money = request("money");
		if (StringUtils.isBlank(money)) {// 判断是否为空
			obj.put("msg", "金额为不能空");
			JSONHelper.printObject(obj);
			return null;
		}
		BigDecimal moneyDecimal = null;

		try {
			moneyDecimal = new BigDecimal(money);
		} catch (RuntimeException e) {
			obj.put("msg", "非法金额格式");
			JSONHelper.printObject(obj);
			return null;
		}
		int temp = moneyDecimal.compareTo(new BigDecimal("1"));// 最小金额为1元
		if (temp < 0) {
			obj.put("msg", "最小金额为1元");
			JSONHelper.printObject(obj);
			return null;
		}
		// 生成订单
		paramMap.put("rechargeMoney", moneyDecimal + "");
		paramMap.put("rechargeType", "5");
		paramMap.put("userId", user.getId() + "");
		paramMap.put("result", "0");
		paramMap.put("addTime",
				DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

		Map<String, String> result;
		try {
			result = rechargeService.addRecharge(paramMap, 3);

			long nunber = Convert.strToLong(result.get("result"), -1);

			if (nunber != -1) {

				LoanRechangeBean loanRechargBean = new LoanRechangeBean();
				String orderNo = IPaymentUtil.getIn_orderNo();// 商户流水号
				String pTrdAmt = String.format("%.2f", moneyDecimal);// 充值金额
				String pMerFee = "0.00";// 平台手续费

				Map<String, String> usersMap = userService.queryUserById(user
						.getId());
				int vipStatus = Convert.strToInt(usersMap.get("vipStatus"), 1);
				int feeStatus = Convert.strToInt(usersMap.get("feeStatus"), 1);
				if (feeStatus == 1) {
					Map<String, Object> platformCostMap = getPlatformCost();
					double vipFee = Convert.strToDouble(
							platformCostMap.get(IAmountConstants.VIP_FEE_RATE)
									+ "", 0);
					java.text.DecimalFormat df = new java.text.DecimalFormat(
							"0.00");
					if (vipStatus == 2 || vipStatus == 3) {
						pMerFee = df.format(Convert.strToDouble(pMerFee, 0)
								+ vipFee);
					}
				}

				String submitURL = "";
				// 判断正式还是测试环境
				if ("2".equals(IConstants.ISDEMO)) {
					// 正式
					submitURL = QianduoduoConfig.submitURLPrefix
							+ QianduoduoPayConstants.recharge;
				} else {
					// 测试
					submitURL = QianduoduoConfig.submitURLPrefix_test
							+ QianduoduoPayConstants.recharge;
				}

				String randomTimeStamp = "";
				if ("1".equals(QianduoduoConfig.antistate)) {
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmssSSS");
					randomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
				}

				RsaHelper rsa = RsaHelper.getInstance();

				loanRechargBean.setRechargeMoneymoremore(user
						.getPortMerBillNo());// 用户乾多多号
				loanRechargBean
						.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 平台乾多多标识
				loanRechargBean.setOrderNo(orderNo);// 平台的充值订单号
				loanRechargBean.setAmount(pTrdAmt);// 充值金额
				loanRechargBean.setRechargeType(""); // 空.网银充值1.代扣充值2.快捷支付3.汇款充值4.企业网银
				loanRechargBean.setFeeType("");// 1.充值成功时从充值人账户全额扣除2.充值成功时从平台自有账户全额扣除3.充值成功时从充值人账户扣除与提现手续费的差值
				loanRechargBean.setCardNo("");// 代扣充值时需要填这个字段 ;为空则从乾多多后台默认的银行卡中扣
												// ;原卡号需经过加密后抛送;SignInfo中的卡号是未经加密的原卡号
				loanRechargBean.setRandomTimeStamp(randomTimeStamp);// 随机时间戳
				loanRechargBean.setRemark1("1");// 自定义备注
				loanRechargBean.setRemark2("2");// 自定义备注
				loanRechargBean.setRemark3("3");// 自定义备注
				loanRechargBean.setNotifyURL(QianduoduoPayConstants.url
						+ "loan/loanRechargeNotify.do");// 充值通知回调地址
				loanRechargBean.setReturnURL(QianduoduoPayConstants.url
						+ "loan/loanRechargeReturn.do");// 充值页面回调地址

				String dataStr = loanRechargBean.getRechargeMoneymoremore()
						+ loanRechargBean.getPlatformMoneymoremore()
						+ loanRechargBean.getOrderNo()
						+ loanRechargBean.getAmount()
						+ loanRechargBean.getRechargeType()
						+ loanRechargBean.getFeeType()
						+ loanRechargBean.getCardNo()
						+ loanRechargBean.getRandomTimeStamp()
						+ loanRechargBean.getRemark1()
						+ loanRechargBean.getRemark2()
						+ loanRechargBean.getRemark3()
						+ loanRechargBean.getReturnURL()
						+ loanRechargBean.getNotifyURL();
				// 签名
				MD5 md5 = new MD5();
				if ("1".equals(QianduoduoConfig.antistate)) {
					dataStr = md5.getMD5Info(dataStr);
				}
				String SignInfo = rsa.signData(dataStr,
						QianduoduoConfig.privateKeyPKCS8);
				loanRechargBean.setSignInfo(SignInfo);

				Map<String, String> loanRechargBeanMap = BeanHelper
						.beanToMap(loanRechargBean);
				loanRechargBeanMap.put("nunber", result.get("result"));
				// 订单记录
				userService.saveQianduoduoOrderNo(user.getId(),
						user.getPortMerBillNo(), orderNo, loanRechargBeanMap);// 保存输入参数记录

				loanRechargBeanMap.put("SubmitURL", submitURL);
				request()
						.setAttribute("loanRechargBeanMap", loanRechargBeanMap);
				return SUCCESS;

			} else {
				getOut().print(
						"<script>alert('支付失败！');parent.location.href='"
								+ request().getContextPath()
								+ "/queryFundrecordInit.do';</script>");
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 满标审核.
	 * 
	 * @throws Exception
	 * @MethodName: updateBorrowF
	 * @Param: BorrowManageAction
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午03:58:28
	 * @Return:
	 * @Descb: 审核借款中的满标记录
	 * @Throws:
	 */
	public String checkTrade() throws Exception {
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		JSONObject obj = new JSONObject();
		if (admin == null) {
			return "nologin";
		}
		String orderNo = QianduoduoPayUtil.getOrderNo("");// 审核流水号
		// 借款id
		String id = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));
		long idLong = Convert.strToLong(id, -1);
		// 4：复审通过 6：复审不通过
		String status = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("status"));
		long statusLong = Convert.strToLong(status, -1);

		// 风控意见
		String auditOpinion = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("auditOpinion"));
		String tradeNo = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("tradeNo"));

		// 满标审核前判断处理
		Map<String, String> map = borrowManageService.borrowAuthFullscale(
				idLong, statusLong);
		Long ret = Convert.strToLong(map.get("ret"), -1);
		if (ret <= 0) {
			obj.put("msg", map.get("ret_desc"));
			JSONHelper.printObject(obj);
			return null;
		}

		// 判断是天标还是正常标

		Map<String, String> borrowMap = borrowService
				.queryBorrowStatusByborrowId(idLong);
		if ("2".equals(borrowMap.get("isDayThe"))) {
			// 天标处理
			obj.put("msg", map.get("系统没有对天标进行处理！"));
			JSONHelper.printObject(obj);
			return null;
		}

		// 判断通过进行第三方处理
		String auditType = "2";
		if (statusLong == 4) {
			auditType = "1";
		}
		if (statusLong == 6) {
			auditType = "2";
		}

		// 查询投资列表（乾多多流水号列表）
		List<Map<String, Object>> invest_loanNoList = borrowManageService
				.queryInvestLoanNoByBorrowId(idLong);
		String loanNoList = "";
		int size = invest_loanNoList.size();
		for (int i = 0; i < size; i++) {
			Map<String, Object> loanNoMap = invest_loanNoList.get(i);
			loanNoList += loanNoMap.get("loanNo").toString();
			if (i != size - 1) {
				loanNoList += ",";
			}
		}
		String randomTimeStamp = "";
		if ("1".equals(QianduoduoConfig.antistate)) {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			randomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
		}

		LoantransferauditBean loantransferauditBean = new LoantransferauditBean();
		loantransferauditBean.setAuditType(auditType);
		loantransferauditBean.setLoanNoList(loanNoList);
		loantransferauditBean.setNotifyURL(QianduoduoPayConstants.url
				+ "/loantransferauditNotify.do");
		loantransferauditBean.setReturnURL(QianduoduoPayConstants.url
				+ "/loantransferauditReturn.do");
		loantransferauditBean
				.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);
		loantransferauditBean.setRandomTimeStamp(randomTimeStamp);
		loantransferauditBean.setRemark1(orderNo);// 审核流水号
		loantransferauditBean.setRemark2(QianduoduoPayUtil.getOrderNo(""));// 资金操作流水号.
		loantransferauditBean.setRemark3("3");

		String dataStr = loantransferauditBean.getLoanNoList()
				+ loantransferauditBean.getPlatformMoneymoremore()
				+ loantransferauditBean.getAuditType()
				+ loantransferauditBean.getRandomTimeStamp()
				+ loantransferauditBean.getRemark1()
				+ loantransferauditBean.getRemark2()
				+ loantransferauditBean.getRemark3()
				+ loantransferauditBean.getReturnURL()
				+ loantransferauditBean.getNotifyURL();
		// 签名
		MD5 md5 = new MD5();
		RsaHelper rsa = RsaHelper.getInstance();
		if ("1".equals(QianduoduoConfig.antistate)) {
			dataStr = md5.getMD5Info(dataStr);
		}
		String signInfo = rsa.signData(dataStr,
				QianduoduoConfig.privateKeyPKCS8);

		loantransferauditBean.setSignInfo(signInfo);

		Map<String, String> loantransferauditMap = BeanHelper
				.beanToMap(loantransferauditBean);
		String SubmitURL = "";
		// 判断正式还是测试环境
		if ("2".equals(IConstants.ISDEMO)) {
			// 正式
			SubmitURL = QianduoduoConfig.submitURLPrefix
					+ QianduoduoPayConstants.transferaudit;
		} else {
			// 测试
			SubmitURL = QianduoduoConfig.submitURLPrefix_test
					+ QianduoduoPayConstants.transferaudit;
		}
		// 追加审核信息，存入订单
		loantransferauditMap.put("borrowId", id);// 借款id
		loantransferauditMap.put("auditOpinion", auditOpinion);// 风控意见
		loantransferauditMap.put("tradeNo", tradeNo);// 借款流水
		loantransferauditMap.put("status", status);// 复审状态
		loantransferauditMap.put("adminId", admin.getId().toString());// 复审人id
		// 订单记录

		userService.saveQianduoduoOrderNo(admin.getId(),
				QianduoduoConfig.platformMoneymoremore, orderNo,
				loantransferauditMap);// 保存输入参数记录

		request().setAttribute("loantransferauditMap", loantransferauditMap);
		request().setAttribute("SubmitURL", SubmitURL);
		return SUCCESS;
	}

	/**
	 * 还款.
	 * 
	 * @Title: toRepayment
	 * @param
	 * @return String 返回类型
	 * @throws Exception
	 */
	public String toRepayment() throws Exception {
		User user = (User) session().getAttribute("user");

		if (user == null) {

			return "nologin";
		}

		String repayIdStr = request("id") == null ? "" : request("id");// 还款表id
		long repayId = Convert.strToLong(repayIdStr, -1L);
		String bId = request("bId") == null ? "" : request("bId");// 借款id
		long bIdLong = Convert.strToLong(bId, -1L);
		String needSum = request("needSum") == null ? "" : request("needSum");
		Double excitationSum = 0.00;// 奖励金额
		Double borrowAmount = 0.00;

		Map<String, String> tUserMap = userService.queryUserById(user.getId());

		// 判断是否是天标
		Map<String, String> borrowInfoMap = borrowManageService
				.queryBorrowByid(bIdLong);
		int isDayThe = Convert.strToInt(borrowInfoMap.get("isDayThe"), 1);
		if (isDayThe == 2) {
			this.toRepaymentExperienceMoney(repayId);

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
		Map<String, String> statusMap = frontpayService
				.queryInvestStatus(bIdLong);
		int statusCount = 0;// 第一次还款标识
		if (statusMap != null && statusMap.size() > 0) {
			statusCount = Convert.strToInt(statusMap.get("statusCount"), 0);
		}
		// 借款信息
		Map<String, String> investDetailMap = financeService
				.queryBorrowInvest(bIdLong);
		if (investDetailMap != null && investDetailMap.size() > 0) {
			borrowAmount = Convert.strToDouble(
					investDetailMap.get("borrowAmount"), 0.00);
			if (statusCount == 0) {
				// 奖励方式
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
		List<Map<String, Object>> investorList = frontpayService
				.queryRepay(repayId);

		List<LoanInfoBean> listmlib = new ArrayList<LoanInfoBean>();

		List<LoanInfoBean> compensationLib = new ArrayList<LoanInfoBean>();
		// 平台年利率
		Double annualRate = Convert.strToDouble(
				borrowInfoMap.get("annualRate"), 0.00);
		// 总共罚息
		double sumRecivedFI = 0.00;
		// 平台收取的罚息总和
		double remainRecivedFI = 0.00;
		// 循环投资人
		for (int i = 0; i < investorList.size(); i++) {
			LoanInfoBean loanInfoBean = new LoanInfoBean();
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

			String repaymentOrderNo = QianduoduoPayUtil.getOrderNo("");// 还款流水号
			Double awardAmount = 0.00;// 奖励
			Double investAmount = Convert.strToDouble(
					investorMap.get("investAmount").toString(), 0);
			if (statusCount == 0) {
				if (excitationSum > 0) {
					awardAmount = (investAmount / borrowAmount) * excitationSum;
				}
			}
			String pTTrdAmt = String.format("%.2f", amt + awardAmount);// 还款金额
			String pTTrdFee = String.format("%.2f", Convert.strToDouble(
					investorMap.get("iManageFee").toString(), 0));// 投资管理费
																	// 借款人还款手续费

			LoanInfoSecondaryBean loanInfoSecondaryBean = new LoanInfoSecondaryBean();
			loanInfoSecondaryBean.setAmount(pTTrdFee);
			loanInfoSecondaryBean
					.setLoanInMoneymoremore(QianduoduoConfig.platformMoneymoremore);
			loanInfoSecondaryBean.setRemark("二次分配");
			loanInfoSecondaryBean.setTransferName("投资管理费");

			String secondaryJsonList = new Gson().toJson(loanInfoSecondaryBean);

			loanInfoBean.setLoanOutMoneymoremore(tUserMap.get("portMerBillNo"));// 付款人乾多多标志
			loanInfoBean.setLoanInMoneymoremore(investorMap
					.get("portMerBillNo").toString());// 收款人乾多多标志
			loanInfoBean.setOrderNo(repaymentOrderNo);// 还款流水号
			loanInfoBean.setBatchNo(bId);// 网贷平台标号 标id
			loanInfoBean.setExchangeBatchNo("");// 流转标标号
			loanInfoBean.setAdvanceBatchNo("");// 垫资标号
			loanInfoBean.setAmount(pTTrdAmt);// 还款金额
			loanInfoBean.setFullAmount(String.valueOf(borrowAmount));// 满标标额
			loanInfoBean.setTransferName("还款");// 借款用途
			loanInfoBean.setRemark("1");
			loanInfoBean.setSecondaryJsonList("");// 二次转账

			// 判断是否活动标,如果不是活动标，启动老米计划
			int subject_activity = Convert.strToInt(
					borrowInfoMap.get("subject_activity"), 1);
			compensationLib = this.dealProtectOldUserPlan(bId, excitationSum,
					borrowAmount, borrowInfoMap, statusCount, annualRate,
					investorMap, awardAmount, investAmount, subject_activity);

			listmlib.add(loanInfoBean);
		}
		String pMerBillNo = QianduoduoPayUtil.getOrderNo("");// 还款订单号

		// 编码
		String LoanJsonList = new Gson().toJson(listmlib);

		// 转账接口参数
		LoanTransferBean loanTransferBean = new LoanTransferBean();
		loanTransferBean.setLoanJsonList(LoanJsonList);// 转账列表
		loanTransferBean
				.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 平台乾多多标识
		loanTransferBean.setTransferAction(2);// 1.投标2.还款3.其他
		loanTransferBean.setAction(1);// 1.手动转账2.自动转账
		loanTransferBean.setTransferType(2);// 1.桥连2.直连
		loanTransferBean.setNeedAudit("1");// 空.需要审核1.自动通过
		loanTransferBean.setRemark1(pMerBillNo);
		loanTransferBean.setRemark2("");
		loanTransferBean.setRemark3("");
		// 回调地址
		loanTransferBean.setReturnURL(QianduoduoPayConstants.url
				+ "/toRepaymentReturn.do");

		// 通知地址
		loanTransferBean.setNotifyURL(QianduoduoPayConstants.url
				+ "/toRepaymentNotify.do");

		// 老米计划，平台垫付
		String padFundedInfoOrderNo = this.savePadFundedInfo(compensationLib,
				user);

		String[] strarr = new String[3];
		strarr[0] = repayIdStr;
		strarr[1] = bId;
		strarr[2] = padFundedInfoOrderNo;
		// 调用转账接口
		Map<String, String> loanTransferMap = qianduoduoPayService
				.loanTransfer(loanTransferBean, pMerBillNo, user, listmlib,
						strarr);

		// 手动转账
		if (loanTransferMap != null) {
			request().setAttribute("loanTransferMap", loanTransferMap);
			return SUCCESS;
		}
		// 自动转账（未处理）
		compensationLib = null;
		return null;
	}

	/**
	 * 保存平台垫资信息.
	 * 
	 * @author yingzisong
	 * @since JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	private String savePadFundedInfo(List<LoanInfoBean> compensationLib,
			User user) {
		String padFundedInfoOrderNo = "";// 平台垫资流水号
		if (compensationLib.size() > 0) {
			try {
				padFundedInfoOrderNo = QianduoduoPayUtil.getOrderNo("");// 平台垫资流水号
				// 存入订单记录
				Map<String, String> listmlibMap = new HashMap<String, String>();
				// 编码
				String LoanJsonList = new Gson().toJson(compensationLib);
				listmlibMap.put("LoanJsonList", LoanJsonList);
				userService.saveQianduoduoOrderNo(user.getId(),
						user.getPortMerBillNo(), padFundedInfoOrderNo,
						listmlibMap);
			} catch (Exception e) {
				e.printStackTrace();
			}// 保存输入参数记录
		}
		return padFundedInfoOrderNo;

	}

	/**
	 * 启用老米护盾计划.
	 * 
	 * @author 殷梓淞
	 * @param bId
	 * @param excitationSum
	 * @param borrowAmount
	 * @param borrowInfoMap
	 * @param statusCount
	 * @param listmlib
	 * @param annualRate
	 * @param investorMap
	 * @param repaymentOrderNo
	 * @param awardAmount
	 * @param investAmount
	 * @param subject_activity
	 * @throws Exception
	 * @since JDK 1.7
	 */
	private List<LoanInfoBean> dealProtectOldUserPlan(String bId,
			Double excitationSum, Double borrowAmount,
			Map<String, String> borrowInfoMap, int statusCount,
			Double annualRate, Map<String, Object> investorMap,
			Double awardAmount, Double investAmount, int subject_activity)
			throws Exception {
		List<LoanInfoBean> compensationLib = new ArrayList<LoanInfoBean>();
		if (subject_activity == 0) {
			long investor = Convert.strToLong(
					String.valueOf(investorMap.get("investor")), -1);

			// 老米护盾计划，平台转账给投资人
			// 判断老米
			Map<String, String> mapUserDetail = protectOldUserService
					.getProtectUserDetailByUserId(investor);
			if (mapUserDetail != null) {
				// 判断规则（不超过100w）
				Map<String, String> mapDueinSum = protectOldUserService
						.getDueinSumOfMonth(investor);
				double dueinSum = Convert.strToDouble(
						mapDueinSum.get("dueinSumOfMonth"), 0.00);
				if (dueinSum < 1000000.00) {
					// 计算补偿金额
					int deadline = Convert.strToInt(
							borrowInfoMap.get("deadline"), -1);
					Map<String, String> mapProtectRate = protectOldUserService
							.getProtectRateByMonth(deadline);
					if (mapProtectRate != null) {
						double rate = Convert.strToDouble(
								mapProtectRate.get("rate"), 0.00);// 老米年利率
						double awardrate = Convert.strToDouble(
								mapProtectRate.get("awardrate"), 0.00);// 老米奖励利率
						double compensation = 0.00;// 补偿金额
						double compensationAward = 0.00;// 补偿奖励
						double compensationRate = 0.00;// 补偿利息

						// 计算补偿奖励
						if (statusCount == 0 && excitationSum > 0) {
							// 第一次还款，投标有奖励
							compensationAward = investAmount * awardrate / 100;
							// 如果借款人给的奖励多于平台的奖励，按借款人给的奖励算
							if (awardAmount > compensationAward) {
								compensationAward = 0.00;
							} else {
								// 如果不足，平台补足
								compensationAward = compensationAward
										- awardAmount;
							}
						} else {
							// 非第一次还款，或者第一次还款投标没有奖励 = 投标金额*奖励利息
							compensationAward = investAmount * awardrate / 100
									/ 12;
						}
						// 计算补偿利息
						compensationRate = investAmount * (rate - annualRate)
								/ 100 / 12;
						if (compensationRate < 0) {
							// 如果借款人给的利息大于老米利息，按照借款人利息算
							compensationRate = 0.00;
						}
						// 计算补偿金额
						compensation = compensationAward + compensationRate;// 补偿奖励+补偿利息

						String repaymentOrderNo = QianduoduoPayUtil
								.getOrderNo("");// 垫付流水号
						// 添加资金流水

						// new loanInfoBean
						LoanInfoBean compensationLoanInfoBean = new LoanInfoBean();
						// 设置loanInfoBean
						compensationLoanInfoBean
								.setLoanOutMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 乾多多标志
						compensationLoanInfoBean
								.setLoanInMoneymoremore(investorMap.get(
										"portMerBillNo").toString());// 收款人乾多多标志
						compensationLoanInfoBean.setOrderNo(repaymentOrderNo);// 垫付流水号
						compensationLoanInfoBean.setBatchNo(bId);// 网贷平台标号 标id
						compensationLoanInfoBean.setExchangeBatchNo("");// 流转标标号
						compensationLoanInfoBean.setAdvanceBatchNo("");// 垫资标号
						compensationLoanInfoBean.setAmount(String.format(
								"%.2f", compensation));// 还款金额
						compensationLoanInfoBean.setFullAmount(String
								.valueOf(borrowAmount));// 满标标额
						compensationLoanInfoBean.setTransferName("垫付老米护盾利息");// 借款用途
						compensationLoanInfoBean.setRemark(String
								.valueOf(investorMap.get("investor")));
						compensationLoanInfoBean.setSecondaryJsonList("");// 二次转账
						// add进去compensationLib
						compensationLib.add(compensationLoanInfoBean);
					}
				}
			}
		}
		return compensationLib;
	}

	/**
	 * 还款(体验标）
	 * 
	 * @throws Exception
	 */
	public String toRepaymentExperienceMoney(long idLong) throws Exception {
		log.info("体验标还款处理");
		// 查出来需要还款的金额
		List<Map<String, Object>> investorList = frontpayService
				.queryRepay(idLong);
		// 调用第三方奖励接口

		getOut().print(
				"<script>alert('天标还款需要调用第三方处理！');parent.location.href='"
						+ request().getContextPath()
						+ "/queryAllDetails.do';</script>");
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

		if (user == null) {
			return null;
		}
		String wid = paramMap.get("id") == null ? "" : paramMap.get("id");// 提现表的id
		String checkId = paramMap.get("checkId") == null ? "" : paramMap
				.get("checkId");// 审核人id

		Long widLong = Convert.strToLong(wid, -1);
		String orderNo = QianduoduoPayUtil.getOrderNo("");

		Map<String, String> withdrawMap = rechargeService
				.getWithdrawByWithdrawId(widLong);
		String SubmitURL = "";
		// 判断正式还是测试环境
		if ("2".equals(IConstants.ISDEMO)) {
			// 正式
			SubmitURL = QianduoduoConfig.submitURLPrefix
					+ QianduoduoPayConstants.withdraws;
		} else {
			// 测试
			SubmitURL = QianduoduoConfig.submitURLPrefix_test
					+ QianduoduoPayConstants.withdraws;
		}

		String returnURL = QianduoduoPayConstants.url
				+ "/loanwithdrawsReturn.do";
		String notifyURL = QianduoduoPayConstants.url
				+ "/loanwithdrawsNotify.do";

		String randomTimeStamp = "";
		if ("1".equals(QianduoduoConfig.antistate)) {
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			randomTimeStamp = Common.getRandomNum(2) + sdf.format(d);
		}

		RsaHelper rsa = RsaHelper.getInstance();
		String cardNo = withdrawMap.get("cardNo").toString();
		String cardNoRsa = rsa.encryptData(cardNo, QianduoduoConfig.publicKey);

		LoanwithdrawBean loanwithdrawBean = new LoanwithdrawBean();
		loanwithdrawBean.setAmount(withdrawMap.get("sum"));// 提现金额
		loanwithdrawBean.setBankCode(withdrawMap.get("bankCode"));// 银行编码
		loanwithdrawBean.setBranchBankName(withdrawMap.get("branchBankName"));// 支行名称
		loanwithdrawBean.setCardNo(cardNoRsa);// 银行卡号
		loanwithdrawBean.setCardType(withdrawMap.get("cardType"));// 银行卡类型
		loanwithdrawBean.setCity(withdrawMap.get("cityCode"));// 城市编号
		loanwithdrawBean.setFeeMax("");// 用户承担的最高手续费
		loanwithdrawBean.setFeePercent("0");// 平台承担的手续费比例
		loanwithdrawBean.setFeeRate("");// 上浮费率
		loanwithdrawBean.setNotifyURL(notifyURL);
		loanwithdrawBean.setOrderNo(orderNo);
		loanwithdrawBean
				.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);
		loanwithdrawBean.setProvince(withdrawMap.get("provinceCode"));
		loanwithdrawBean.setRandomTimeStamp(randomTimeStamp);
		loanwithdrawBean.setRemark1("1");
		loanwithdrawBean.setRemark2("2");
		loanwithdrawBean.setRemark3("3");
		loanwithdrawBean.setReturnURL(returnURL);
		loanwithdrawBean.setWithdrawMoneymoremore(user.getPortMerBillNo());

		String dataStr = loanwithdrawBean.getWithdrawMoneymoremore()
				+ loanwithdrawBean.getPlatformMoneymoremore()
				+ loanwithdrawBean.getOrderNo() + loanwithdrawBean.getAmount()
				+ loanwithdrawBean.getFeePercent()
				+ loanwithdrawBean.getFeeMax() + loanwithdrawBean.getFeeRate()
				+ cardNo + loanwithdrawBean.getCardType()
				+ loanwithdrawBean.getBankCode()
				+ loanwithdrawBean.getBranchBankName()
				+ loanwithdrawBean.getProvince() + loanwithdrawBean.getCity()
				+ loanwithdrawBean.getRandomTimeStamp()
				+ loanwithdrawBean.getRemark1() + loanwithdrawBean.getRemark2()
				+ loanwithdrawBean.getRemark3()
				+ loanwithdrawBean.getReturnURL()
				+ loanwithdrawBean.getNotifyURL();
		// 签名
		MD5 md5 = new MD5();
		if ("1".equals(QianduoduoConfig.antistate)) {
			dataStr = md5.getMD5Info(dataStr);
		}
		String signInfo = rsa.signData(dataStr,
				QianduoduoConfig.privateKeyPKCS8);
		loanwithdrawBean.setSignInfo(signInfo);

		Map<String, String> loanwithdrawMap = BeanHelper
				.beanToMap(loanwithdrawBean);
		// 添加回调需要的参数
		loanwithdrawMap.put("wid", wid);
		loanwithdrawMap.put("checkId", checkId);
		loanwithdrawMap.put("userId", user.getId().toString());
		userService.saveQianduoduoOrderNo(user.getId(),
				user.getPortMerBillNo(), orderNo, loanwithdrawMap);
		request().setAttribute("loanwithdrawMap", loanwithdrawMap);
		request().setAttribute("SubmitURL", SubmitURL);
		return SUCCESS;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public UserIntegralService getUserIntegralService() {
		return userIntegralService;
	}

	public void setUserIntegralService(UserIntegralService userIntegralService) {
		this.userIntegralService = userIntegralService;
	}

	public QianduoduoPayService getQianduoduoPayService() {
		return qianduoduoPayService;
	}

	public void setQianduoduoPayService(
			QianduoduoPayService qianduoduoPayService) {
		this.qianduoduoPayService = qianduoduoPayService;
	}

	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
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

	public BorrowManageService getBorrowManageService() {
		return borrowManageService;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public RechargeService getRechargeService() {
		return rechargeService;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public final BorrowService getBorrowService() {
		return borrowService;
	}

	public final void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public final FrontMyPaymentService getFrontpayService() {
		return frontpayService;
	}

	public final void setFrontpayService(FrontMyPaymentService frontpayService) {
		this.frontpayService = frontpayService;
	}

	public final ProtectOldUserService getProtectOldUserService() {
		return protectOldUserService;
	}

	public final void setProtectOldUserService(
			ProtectOldUserService protectOldUserService) {
		this.protectOldUserService = protectOldUserService;
	}
}
