/**   
 * @Title: QianduoduoPayReturnAction.java 
 * @Package com.shove.web.action 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月6日 下午7:18:33 
 * @version V1.0   
 */
package com.shove.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.BeanHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.google.gson.Gson;
import com.shove.Convert;
import com.shove.config.QianduoduoConfig;
import com.shove.services.QianduoduoPayService;
import com.shove.services.QianduoduoPayUtil;
import com.shove.shuanquanUtil.MD5;
import com.shove.shuanquanUtil.RsaHelper;
import com.shove.vo.LoanInfoBean;
import com.shove.vo.LoanTransferBean;
import com.sp2p.entity.User;
import com.sp2p.service.CoinService;
import com.sp2p.service.FrontMyPaymentService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;
import com.sp2p.service.admin.ReturnedmoneyService;

/**
 * 乾多多页面回调.
 * 
 * @ClassName: QianduoduoPayReturnAction
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月6日 下午7:18:33
 * 
 */
public class QianduoduoPayReturnAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(QianduoduoPayAction.class);
	// 公共
	private String PlatformMoneymoremore = "";
	private String SignInfo = "";
	private String ResultCode = "";
	private String ReturnTimes = "";
	private String RandomTimeStamp = "";

	// 注册
	private String AccountType = "";
	private String AccountNumber = "";
	private String Mobile = "";
	private String Email = "";
	private String RealName = "";
	private String IdentificationNo = "";
	private String AuthFee = "";
	private String AuthState = "";

	// 绑定
	private String LoanPlatformAccount = "";
	private String MoneymoremoreId = "";
	private String Remark1 = "";
	private String Remark2 = "";
	private String Remark3 = "";
	private String Message = "";

	// 转账
	private String LoanJsonList = "";
	private String Action = "";

	// 充值
	private String RechargeMoneymoremore = "";
	private String OrderNo = "";
	private String Amount = "";
	private String RechargeType = "";
	private String FeeType = "";
	private String CardNoList = "";
	private String LoanNo = "";
	private String Fee = "";

	// 审核
	private String LoanNoList = "";
	private String LoanNoListFail = "";
	private String AuditType = "";

	// 提现
	private String WithdrawMoneymoremore = "";
	private String FeePercent = "";
	private String FreeLimit = "";
	private String FeeMax = "";
	private String FeeWithdraws = "";
	private String FeeRate = "";
	private String FeeSplitting = "";
	private String CardNo = "";
	private String CardType = "";
	private String BankCode = "";
	private String BranchBankName = "";
	private String Province = "";
	private String City = "";

	private QianduoduoPayService qianduoduoPayService;
	private BorrowManageService borrowManageService;
	private FrontMyPaymentService frontpayService;
	private UserService userService;
	private FundManagementService fundManagementService;
	private ReturnedmoneyService returnedmoneyService;
	private CoinService coinService;

	/**
	 * 接收开户页面返回信息
	 * 
	 * @return
	 */
	public String registerbindReturn() {
		log.info("账户绑定-接收开户页面返回信息");
		User user = (User) session().getAttribute("user");
		String pReturn = "绑定成功！";
		String url = "/portUserAcct.do";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			String publickey = QianduoduoConfig.publicKey;

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = AccountType + AccountNumber + Mobile + Email
					+ RealName + IdentificationNo + LoanPlatformAccount
					+ MoneymoremoreId + PlatformMoneymoremore + AuthFee
					+ AuthState + RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}
			System.out.println("dataStr=" + dataStr);
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					publickey);
			System.out.println("页面返回:" + Boolean.toString(verifySignature));
			System.out.println("返回码:" + ResultCode);
			if (verifySignature) {
				if (ResultCode.equals("88")) {
					url = "/userPassData.do?from=&btype=";

					pReturn = qianduoduoPayService.registerbindBaseDeal(
							MoneymoremoreId, ResultCode, Message,
							LoanPlatformAccount, user);
				} else {
					url = "/portUserAcct.do";
					pReturn = Message;
				}
			} else {
				url = "/portUserAcct.do";
				pReturn = Message;
			}
			log.debug("pReturn.equals" + pReturn.equals("通知接口已处理"));
			if (pReturn.equals("通知接口已处理")) {
				getOut().print(
						"<script>alert('" + "请重新登录绑定账号生效！"
								+ "');parent.location.href='"
								+ request().getContextPath() + "/logout.do"
								+ "';</script>");

			} else {
				session().setAttribute("user", user);
				getOut().print(
						"<script>alert('" + pReturn
								+ "');parent.location.href='"
								+ request().getContextPath() + "" + url
								+ "';</script>");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 接收开户后台通知信息
	 * 
	 * @return
	 */
	public void registerbindNotify() {
		log.info("账户绑定-接收开户后台通知信息");
		String rsStatu = "SUCCESS";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			String publickey = QianduoduoConfig.publicKey;

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = AccountType + AccountNumber + Mobile + Email
					+ RealName + IdentificationNo + LoanPlatformAccount
					+ MoneymoremoreId + PlatformMoneymoremore + AuthFee
					+ AuthState + RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					publickey);
			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			if (verifySignature) {
				if (ResultCode.equals("88")) {
					String rsString = qianduoduoPayService
							.registerbindBaseDealNotify(MoneymoremoreId,
									ResultCode, Message, LoanPlatformAccount);
				}
			} else {
				rsStatu = "FALSE";
			}
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(rsStatu);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 投标页面回调.
	 * 
	 * @Title: loantransferReturn
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public String tenderTradeReturn() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			LoanJsonList = URLDecoder.decode(LoanJsonList, "utf-8");

			if (Action == null) {
				Action = "";
			}

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				if ("88".equals(ResultCode)) {
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									LoanJsonList,
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					if (list.size() > 0) {
						Map<String, String> resultMap = qianduoduoPayService
								.tenderTradeDeal(list.get(0), getBasePath());
						getOut().print(
								"<script>alert('" + resultMap.get("pReturn")
										+ "');parent.location.href='"
										+ request().getContextPath() + ""
										+ resultMap.get("url") + "';</script>");
					} else {
						getOut().print(
								"<script>alert('投标失败，乾多多平台操作错误。');</script>");
					}
				} else {
					getOut().print("<script>alert('投标失败，乾多多平台签名错误。');</script>");
				}

				System.out.println("返回码:" + ResultCode);
			} else {
				System.out.println("返回码:" + ResultCode);
				getOut().print(
						"<script>alert('投标失败，乾多多平台错误。错误码：'" + ResultCode
								+ ");</script>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 接收投标后台通知回调信息.
	 * 
	 * @Title: loantransferNotify
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public void tenderTradeNotify() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			LoanJsonList = URLDecoder.decode(LoanJsonList, "utf-8");

			if (Action == null) {
				Action = "";
			}

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {

				Gson gson = new Gson();
				List<Map<String, String>> list = gson
						.fromJson(
								LoanJsonList,
								new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
								}.getType());
				if (list.size() > 0) {
					qianduoduoPayService.tenderTradeDeal(list.get(0),
							getBasePath());
				}
			}

			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("SUCCESS");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收充值页面回调.
	 * 
	 * @Title: loanRechargeNotify
	 * @param
	 * @return void 返回类型
	 * @throws
	 */
	public String loanRechargeReturn() {
		String pReturn = "";
		String url = "/queryFundrecordInit.do";
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();

			if (StringUtils.isNotBlank(CardNoList)) {
				CardNoList = rsa.decryptData(CardNoList,
						QianduoduoConfig.privateKeyPKCS8);
				if (StringUtils.isBlank(CardNoList)) {
					CardNoList = "";
				}
			}
			String dataStr = RechargeMoneymoremore + PlatformMoneymoremore
					+ LoanNo + OrderNo + Amount + Fee + RechargeType + FeeType
					+ CardNoList + RandomTimeStamp + Remark1 + Remark2
					+ Remark3 + ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				if ("88".equals(ResultCode)) {
					pReturn = qianduoduoPayService.loanRecharge(OrderNo,
							ResultCode);
				} else {
					pReturn = Message;
				}
			} else {
				pReturn = "签名失败！";
			}
			getOut().print(
					"<script>alert('" + pReturn + "');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");

			System.out.println("返回码:" + ResultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 接收充值通知回调.
	 * 
	 * @Title: loanRechargeNotify
	 * @param
	 * @return void 返回类型
	 * @throws
	 */
	public void loanRechargeNotify() {
		try {
			String pReturn = "SUCCESS";
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();

			if (StringUtils.isNotBlank(CardNoList)) {
				CardNoList = rsa.decryptData(CardNoList,
						QianduoduoConfig.privateKeyPKCS8);
				if (StringUtils.isBlank(CardNoList)) {
					CardNoList = "";
				}
			}
			String dataStr = RechargeMoneymoremore + PlatformMoneymoremore
					+ LoanNo + OrderNo + Amount + Fee + RechargeType + FeeType
					+ CardNoList + RandomTimeStamp + Remark1 + Remark2
					+ Remark3 + ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);

			if (verifySignature) {
				qianduoduoPayService.loanRecharge(OrderNo, ResultCode);

			} else {
				pReturn = "Fail";
			}
			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(pReturn);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收投标审核页面返回.
	 * 
	 * @Title: loantransferauditNotify
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public String loantransferauditReturn() {
		try {
			String pReturn = "SUCCESS";
			String url = "/admin/default.do";
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail
					+ PlatformMoneymoremore + AuditType + RandomTimeStamp
					+ Remark1 + Remark2 + Remark3 + ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);

			Map<String, String> resultMap = new HashMap<String, String>();
			if (verifySignature) {
				if ("88".equals(ResultCode)) {
					resultMap = qianduoduoPayService.loantransferaudit(Remark1,
							getBasePath(), Remark2);
					long retVal = -1;
					retVal = Convert.strToLong(resultMap.get("ret") + "", -1);
					pReturn = Convert.strToStr(resultMap.get("ret_desc"),
							"NULL");

					if (retVal <= 0 && !"NULL".equals(pReturn)) {
						pReturn = resultMap.get("ret_desc");
						url = "/admin/default.do";
					} else if (retVal <= 0 && "NULL".equals(pReturn)) {
						// 通知线程已经处理
						pReturn = "操作成功!";
						url = "/admin/default.do";
					} else {
						// 添加回款
						returnedmoneyService.addReturnMoneyDetail(Remark1,
								getBasePath(), Remark2);
						// 添加金币
						coinService.addCoin(Remark1, getBasePath(), Remark2);
						pReturn = "操作成功!";
						url = "/admin/default.do";
					}
				}
			} else {
				pReturn = "签名失败！";
			}
			getOut().print(
					"<script>alert('" + pReturn + "');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");
			System.out.println("页面返回:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 接收投标审核回调通知.
	 * 
	 * @Title: loantransferauditNotify
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public void loantransferauditNotify() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			String pReturn = "SUCCESS";
			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanNoList + LoanNoListFail
					+ PlatformMoneymoremore + AuditType + RandomTimeStamp
					+ Remark1 + Remark2 + Remark3 + ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			Map<String, String> resultMap = new HashMap<String, String>();
			if (verifySignature) {
				if ("88".equals(ResultCode)) {
					resultMap = qianduoduoPayService.loantransferaudit(Remark1,
							getBasePath(), Remark2);
					long retVal = -1;
					retVal = Convert.strToLong(resultMap.get("ret") + "", -1);
					if (retVal <= 0) {
						pReturn = "FALSE";
					} else {
						// 添加回款
						returnedmoneyService.addReturnMoneyDetail(Remark1,
								getBasePath(), Remark2);

						// 添加金币
						coinService.addCoin(Remark1, getBasePath(), Remark2);
						pReturn = "SUCCESS";
					}
				}
			} else {
				pReturn = "FALSE";
			}

			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(pReturn);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收还款页面回调.
	 * 
	 * @Title: toRepaymentReturn
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public String toRepaymentReturn() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			LoanJsonList = URLDecoder.decode(LoanJsonList, "utf-8");
			String url = "/queryMyPayingBorrowList.do";
			if (Action == null) {
				Action = "";
			}

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				if (!"88".equals(ResultCode)) {
					getOut().print(
							"<script>alert('" + Message
									+ "');parent.location.href='"
									+ request().getContextPath() + "" + url
									+ "';</script>");
				} else {
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									LoanJsonList,
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					if (list.size() > 0) {
						String pMerBillNo = Remark1;
						Map<String, String> userMap = userService
								.quePortUserId(null, pMerBillNo);
						// 是否已经异步
						Map<String, String> fundMap = fundManagementService
								.queryFundRecord(pMerBillNo);
						if (fundMap == null) {
							long repaymentId = -1;// 还款id
							long borrowId = -1;// 借款id
							long userId = -1;
							String padFundedInfoOrderNo = "";
							Map<String, String> hxBuMapXml = new HashMap<String, String>();
							if (userMap != null && userMap.size() > 0) {
								List<Map<String, String>> list2 = gson
										.fromJson(
												userMap.get("hxBuMap"),
												new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
												}.getType());
								if (list2 != null && list2.size() > 0) {

									hxBuMapXml = list2.get(0);
									repaymentId = Convert.strToLong(
											hxBuMapXml.get("repayId"), -1);
									userId = Convert.strToLong(
											userMap.get("userId"), -1);
									borrowId = Convert.strToLong(
											hxBuMapXml.get("borrowId"), -1);
									padFundedInfoOrderNo = Convert
											.strToStr(
													hxBuMapXml
															.get("padFundedInfoOrderNo"),
													"");

								}

							}
							Map<String, String> map = frontpayService
									.submitPay(repaymentId, userId,
											getBasePath(), "", pMerBillNo);
							String result = Convert.strToStr(
									map.get("ret_desc"), "");

							returnedmoneyService
									.updateSumReturnedMoneyByUserId(
											repaymentId, borrowId);

							// 处理老米护盾垫资
							User user = new User();
							user.setId(userId);
							this.platDealOldUserPlan(padFundedInfoOrderNo, user);

							if (!"".equals(result)) {
								getOut().print(
										"<script>alert('" + result
												+ "');parent.location.href='"
												+ request().getContextPath()
												+ "" + url + "';</script>");
							}
							getOut().print(
									"<script>parent.location.href='"
											+ request().getContextPath() + ""
											+ url + "';</script>");
						}
						getOut().print(
								"<script>parent.location.href='"
										+ request().getContextPath() + "" + url
										+ "';</script>");
					} else {
						getOut().print(
								"<script>alert('还款失败，乾多多平台操作错误。');</script>");
					}
				}

			} else {
				getOut().print(
						"<script>alert('签名失败');parent.location.href='"
								+ request().getContextPath() + "" + url
								+ "';</script>");
			}

			System.out.println("返回码:" + ResultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 接收还款后台通知回调信息.
	 * 
	 * @Title: toRepaymentNotify
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public void toRepaymentNotify() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			String resultStr = "SUCCESS";
			LoanJsonList = URLDecoder.decode(LoanJsonList, "utf-8");

			if (Action == null) {
				Action = "";
			}

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				if (!"88".equals(ResultCode)) {
					resultStr = "FALSE";
				} else {
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									LoanJsonList,
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					if (list.size() > 0) {
						String pMerBillNo = Remark1;
						Map<String, String> userMap = userService
								.quePortUserId(null, pMerBillNo);
						// 是否已经异步
						Map<String, String> fundMap = fundManagementService
								.queryFundRecord(pMerBillNo);
						if (fundMap == null) {
							long repaymentId = -1;// 还款id
							long userId = -1;
							long borrowId = -1;// 借款id
							String padFundedInfoOrderNo = "";
							Map<String, String> hxBuMapXml = new HashMap<String, String>();
							if (userMap != null && userMap.size() > 0) {
								List<Map<String, String>> list2 = gson
										.fromJson(
												userMap.get("hxBuMap"),
												new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
												}.getType());
								if (list2 != null && list2.size() > 0) {

									hxBuMapXml = list2.get(0);
									repaymentId = Convert.strToLong(
											hxBuMapXml.get("repayId"), -1);
									userId = Convert.strToLong(
											userMap.get("userId"), -1);
									borrowId = Convert.strToLong(
											hxBuMapXml.get("borrowId"), -1);
									padFundedInfoOrderNo = Convert
											.strToStr(
													hxBuMapXml
															.get("padFundedInfoOrderNo"),
													"");
								}
							}
							frontpayService.submitPay(repaymentId, userId,
									getBasePath(), "", pMerBillNo);

							// 处理回款
							returnedmoneyService
									.updateSumReturnedMoneyByUserId(
											repaymentId, borrowId);

							// 处理老米护盾垫资
							User user = new User();
							user.setId(userId);
							this.platDealOldUserPlan(padFundedInfoOrderNo, user);
						}
					} else {
						resultStr = "FALSE";
					}
				}
			}

			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(resultStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 平台垫付老米护盾.
	 *
	 * @author 殷梓淞
	 * @throws Exception
	 * @since JDK 1.7
	 */
	private void platDealOldUserPlan(String padFundedInfoOrderNo, User user)
			throws Exception {
		List<LoanInfoBean> compensationLib = new ArrayList<LoanInfoBean>();
		Map<String, String> userMap = userService.quePortUserId(null,
				padFundedInfoOrderNo);
		if (userMap != null && userMap.size() > 0) {
			Gson gson = new Gson();
			List<Map<String, String>> mlibMapList = gson
					.fromJson(
							userMap.get("hxBuMap"),
							new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
							}.getType());

			if (mlibMapList != null && mlibMapList.size() > 0) {
				String pMerBillNo = QianduoduoPayUtil.getOrderNo("");// 垫付订单号
				// 编码
				String LoanJsonList = mlibMapList.get(0).get("LoanJsonList");

				// 转账接口参数
				LoanTransferBean loanTransferBean = new LoanTransferBean();
				loanTransferBean.setLoanJsonList(LoanJsonList);// 转账列表
				loanTransferBean
						.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 平台乾多多标识
				loanTransferBean.setTransferAction(3);// 1.投标2.还款3.其他
				loanTransferBean.setAction(2);// 1.手动转账2.自动转账
				loanTransferBean.setTransferType(2);// 1.桥连2.直连
				loanTransferBean.setNeedAudit("1");// 空.需要审核1.自动通过
				loanTransferBean.setRemark1("平台垫付老米护盾利息");
				loanTransferBean.setRemark2(pMerBillNo);
				loanTransferBean.setRemark3("");
				loanTransferBean.setNotifyURL(QianduoduoPayConstants.url
						+ "platformCompensateNotify.do");
				loanTransferBean.setReturnURL(QianduoduoPayConstants.url
						+ "platformCompensateReturn.do");
				// 调用转账接口
				// Map<String, String> loanTransferMap =
				qianduoduoPayService.loanTransfer(loanTransferBean, pMerBillNo,
						user, compensationLib);
			}
		}
	}

	/**
	 * 接收提现页面回调.
	 * 
	 * @Title: loanwithdrawsReturn
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public String loanwithdrawsReturn() {
		String resultStr = Message;
		String url = "/withdrawLoad.do";

		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = WithdrawMoneymoremore + PlatformMoneymoremore
					+ LoanNo + OrderNo + Amount + FeeMax + FeeWithdraws
					+ FeePercent + Fee + FreeLimit + FeeRate + FeeSplitting
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				Map<String, String> userMap = userService.quePortUserId(null,
						OrderNo);

				// 是否已经同步
				Map<String, String> fundMap = fundManagementService
						.queryFundRecord(OrderNo);
				if (fundMap == null) {
					if ("88".equals(ResultCode)) {// 提现申请成功
						// 改变提现状态，增加成功提现记录
						Gson gson = new Gson();
						List<Map<String, String>> list = gson
								.fromJson(
										userMap.get("hxBuMap"),
										new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
										}.getType());

						Map<String, String> hxBuMapXml = list.get(0);
						long wid = Convert.strToLong(hxBuMapXml.get("wid"), 0);// 提现id
						long checkId = Convert.strToLong(
								hxBuMapXml.get("checkId"), 0);
						long userId = Convert.strToLong(
								hxBuMapXml.get("userId"), -1l);

						String ipAddress = ServletHelper
								.getIpAddress(ServletActionContext.getRequest());
						Map<String, String> retMap = fundManagementService
								.updateWithdrawTransfer(wid, 2, checkId,
										SqlInfusionHelper
												.filteSqlInfusion(ipAddress),
										OrderNo);

						long retVal = Convert.strToLong(retMap.get("ret") + "",
								-1);
						resultStr = "提现操作成功";
						if (retVal > 0) {
							long res1 = returnedmoneyService.withdrawCash(
									userId, wid);
						}

						if (retVal != 1) {
							resultStr = retMap.get("ret_desc");
						}
					}
					resultStr = Message;
				}

			}
			getOut().print(
					"<script>alert('" + resultStr + "');parent.location.href='"
							+ request().getContextPath() + "" + url
							+ "';</script>");
			System.out.println("页面返回:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * 接收提现页面回调.
	 * 
	 * @Title: loanwithdrawsNotify
	 * @param
	 * @return String 返回类型
	 * @throws
	 */
	public void loanwithdrawsNotify() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = WithdrawMoneymoremore + PlatformMoneymoremore
					+ LoanNo + OrderNo + Amount + FeeMax + FeeWithdraws
					+ FeePercent + Fee + FreeLimit + FeeRate + FeeSplitting
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}

			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);

			if (verifySignature) {
				Map<String, String> userMap = userService.quePortUserId(null,
						OrderNo);

				// 是否已经同步
				Map<String, String> fundMap = fundManagementService
						.queryFundRecord(OrderNo);
				if (fundMap == null) {
					if ("88".equals(ResultCode)) {// 提现申请成功
						// 改变提现状态，增加成功提现记录
						Gson gson = new Gson();
						List<Map<String, String>> list = gson
								.fromJson(
										userMap.get("hxBuMap"),
										new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
										}.getType());

						Map<String, String> hxBuMapXml = list.get(0);
						long wid = Convert.strToLong(hxBuMapXml.get("wid"), 0);// 提现id
						long checkId = Convert.strToLong(
								hxBuMapXml.get("checkId"), 0);
						long userId = Convert.strToLong(
								hxBuMapXml.get("userId"), -1l);
						String ipAddress = ServletHelper
								.getIpAddress(ServletActionContext.getRequest());

						Map<String, String> retMap = fundManagementService
								.updateWithdrawTransfer(wid, 2, checkId,
										SqlInfusionHelper
												.filteSqlInfusion(ipAddress),
										OrderNo);

						long retVal = Convert.strToLong(retMap.get("ret") + "",
								-1);

						if (retVal > 0) {
							long res1 = returnedmoneyService.withdrawCash(
									userId, wid);
						}

					}
				}

			}

			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write("SUCCESS");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * returnedmoneyNotify:回款续投通知接口. <br/>
	 *
	 * @author yinzisong
	 * @since JDK 1.7
	 */
   public void returnedmoneyNotify(){
	   log.debug("进入回款续投方法returnedmoneyNotify");
	   try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			String resultStr = "SUCCESS";
			LoanJsonList = URLDecoder.decode(LoanJsonList, "utf-8");

			if (Action == null) {
				Action = "";
			}

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				if (!"88".equals(ResultCode)) {
					resultStr = "FALSE";
				} else {
					//判断是否已经处理
					Map<String, String> fundMap = fundManagementService
							.queryFundRecord(Remark2);
					if (fundMap == null) {
					returnedmoneyService.giveContinuedInvestmentReward(Remark2);
					}
				}
			}

			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(resultStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	   log.debug("退出回款续投方法returnedmoneyNotify");
	   
   }
	
	
	

	/**
	 * 平台补偿用户通知接口.
	 *
	 * @author 殷梓淞
	 * @since JDK 1.7
	 */
	public void platformCompensateNotify() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			request.setCharacterEncoding("UTF-8");
			String resultStr = "SUCCESS";
			LoanJsonList = URLDecoder.decode(LoanJsonList, "utf-8");

			if (Action == null) {
				Action = "";
			}

			MD5 md5 = new MD5();
			RsaHelper rsa = RsaHelper.getInstance();
			String dataStr = LoanJsonList + PlatformMoneymoremore + Action
					+ RandomTimeStamp + Remark1 + Remark2 + Remark3
					+ ResultCode;
			if ("1".equals(QianduoduoConfig.antistate)) {
				dataStr = md5.getMD5Info(dataStr);
			}
			// 签名
			boolean verifySignature = rsa.verifySignature(SignInfo, dataStr,
					QianduoduoConfig.publicKey);
			if (verifySignature) {
				if (!"88".equals(ResultCode)) {
					resultStr = "FALSE";
				} else {
					Gson gson = new Gson();
					List<Map<String, String>> list = gson
							.fromJson(
									LoanJsonList,
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					if (list.size() > 0) {
						String pMerBillNo = Remark1;
						dealOldUsrPlan(gson, pMerBillNo);
					} else {
						resultStr = "FALSE";
					}
				}
			}

			System.out.println("后台通知:" + verifySignature);
			System.out.println("返回码:" + ResultCode);
			System.out.println("返回次数:" + ReturnTimes);

			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(resultStr);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理老米护盾本地资金记录. <br/>
	 *
	 * @author 殷梓淞
	 * @param gson
	 * @param pMerBillNo
	 * @throws Exception
	 * @since JDK 1.7
	 */
	private void dealOldUsrPlan(Gson gson, String pMerBillNo) throws Exception {
		Map<String, String> userMap = userService.quePortUserId(null,
				pMerBillNo);
		// 是否已经处理
		Map<String, String> fundMap = fundManagementService
				.queryFundRecord(pMerBillNo);
		if (fundMap == null) {
			long repaymentId = -1;// 还款id
			long userId = -1;
			long borrowId = -1;// 借款id
			Map<String, String> hxBuMapXml = new HashMap<String, String>();
			if (userMap != null && userMap.size() > 0) {
				List<Map<String, String>> list2 = gson
						.fromJson(
								userMap.get("hxBuMap"),
								new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
								}.getType());
				if (list2 != null && list2.size() > 0) {

					hxBuMapXml = list2.get(0);
					repaymentId = Convert.strToLong(hxBuMapXml.get("repayId"),
							-1);
					userId = Convert.strToLong(userMap.get("userId"), -1);
					borrowId = Convert
							.strToLong(hxBuMapXml.get("borrowId"), -1);
				}
			}
			frontpayService.submitPay(repaymentId, userId, getBasePath(), "",
					pMerBillNo);

		}
	}

	public void platformCompensateReturn() {

	}

	public String getPlatformMoneymoremore() {
		return PlatformMoneymoremore;
	}

	public void setPlatformMoneymoremore(String platformMoneymoremore) {
		PlatformMoneymoremore = platformMoneymoremore;
	}

	public String getSignInfo() {
		return SignInfo;
	}

	public void setSignInfo(String signInfo) {
		SignInfo = signInfo;
	}

	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public String getReturnTimes() {
		return ReturnTimes;
	}

	public void setReturnTimes(String returnTimes) {
		ReturnTimes = returnTimes;
	}

	public String getRandomTimeStamp() {
		return RandomTimeStamp;
	}

	public void setRandomTimeStamp(String randomTimeStamp) {
		RandomTimeStamp = randomTimeStamp;
	}

	public String getAccountType() {
		return AccountType;
	}

	public void setAccountType(String accountType) {
		AccountType = accountType;
	}

	public String getAccountNumber() {
		return AccountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		AccountNumber = accountNumber;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public String getIdentificationNo() {
		return IdentificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		IdentificationNo = identificationNo;
	}

	public String getAuthFee() {
		return AuthFee;
	}

	public void setAuthFee(String authFee) {
		AuthFee = authFee;
	}

	public String getAuthState() {
		return AuthState;
	}

	public void setAuthState(String authState) {
		AuthState = authState;
	}

	public String getLoanPlatformAccount() {
		return LoanPlatformAccount;
	}

	public void setLoanPlatformAccount(String loanPlatformAccount) {
		LoanPlatformAccount = loanPlatformAccount;
	}

	public String getMoneymoremoreId() {
		return MoneymoremoreId;
	}

	public void setMoneymoremoreId(String moneymoremoreId) {
		MoneymoremoreId = moneymoremoreId;
	}

	public String getRemark1() {
		return Remark1;
	}

	public void setRemark1(String remark1) {
		Remark1 = remark1;
	}

	public String getRemark2() {
		return Remark2;
	}

	public void setRemark2(String remark2) {
		Remark2 = remark2;
	}

	public String getRemark3() {
		return Remark3;
	}

	public void setRemark3(String remark3) {
		Remark3 = remark3;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getLoanJsonList() {
		return LoanJsonList;
	}

	public void setLoanJsonList(String loanJsonList) {
		LoanJsonList = loanJsonList;
	}

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	public QianduoduoPayService getQianduoduoPayService() {
		return qianduoduoPayService;
	}

	public void setQianduoduoPayService(
			QianduoduoPayService qianduoduoPayService) {
		this.qianduoduoPayService = qianduoduoPayService;
	}

	public final String getRechargeMoneymoremore() {
		return RechargeMoneymoremore;
	}

	public final void setRechargeMoneymoremore(String rechargeMoneymoremore) {
		RechargeMoneymoremore = rechargeMoneymoremore;
	}

	public final String getOrderNo() {
		return OrderNo;
	}

	public final void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}

	public final String getAmount() {
		return Amount;
	}

	public final void setAmount(String amount) {
		Amount = amount;
	}

	public final String getRechargeType() {
		return RechargeType;
	}

	public final void setRechargeType(String rechargeType) {
		RechargeType = rechargeType;
	}

	public final String getFeeType() {
		return FeeType;
	}

	public final void setFeeType(String feeType) {
		FeeType = feeType;
	}

	public final String getCardNoList() {
		return CardNoList;
	}

	public final void setCardNoList(String cardNoList) {
		CardNoList = cardNoList;
	}

	public final String getLoanNo() {
		return LoanNo;
	}

	public final void setLoanNo(String loanNo) {
		LoanNo = loanNo;
	}

	public final String getFee() {
		return Fee;
	}

	public final void setFee(String fee) {
		Fee = fee;
	}

	public final String getLoanNoList() {
		return LoanNoList;
	}

	public final void setLoanNoList(String loanNoList) {
		LoanNoList = loanNoList;
	}

	public final String getLoanNoListFail() {
		return LoanNoListFail;
	}

	public final void setLoanNoListFail(String loanNoListFail) {
		LoanNoListFail = loanNoListFail;
	}

	public final String getAuditType() {
		return AuditType;
	}

	public final void setAuditType(String auditType) {
		AuditType = auditType;
	}

	public final BorrowManageService getBorrowManageService() {
		return borrowManageService;
	}

	public final void setBorrowManageService(
			BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public final FrontMyPaymentService getFrontpayService() {
		return frontpayService;
	}

	public final void setFrontpayService(FrontMyPaymentService frontpayService) {
		this.frontpayService = frontpayService;
	}

	public final UserService getUserService() {
		return userService;
	}

	public final void setUserService(UserService userService) {
		this.userService = userService;
	}

	public final FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public final void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public final String getWithdrawMoneymoremore() {
		return WithdrawMoneymoremore;
	}

	public final void setWithdrawMoneymoremore(String withdrawMoneymoremore) {
		WithdrawMoneymoremore = withdrawMoneymoremore;
	}

	public final String getFeePercent() {
		return FeePercent;
	}

	public final void setFeePercent(String feePercent) {
		FeePercent = feePercent;
	}

	public final String getFreeLimit() {
		return FreeLimit;
	}

	public final void setFreeLimit(String freeLimit) {
		FreeLimit = freeLimit;
	}

	public final String getFeeMax() {
		return FeeMax;
	}

	public final void setFeeMax(String feeMax) {
		FeeMax = feeMax;
	}

	public final String getFeeWithdraws() {
		return FeeWithdraws;
	}

	public final void setFeeWithdraws(String feeWithdraws) {
		FeeWithdraws = feeWithdraws;
	}

	public final String getFeeRate() {
		return FeeRate;
	}

	public final void setFeeRate(String feeRate) {
		FeeRate = feeRate;
	}

	public final String getFeeSplitting() {
		return FeeSplitting;
	}

	public final void setFeeSplitting(String feeSplitting) {
		FeeSplitting = feeSplitting;
	}

	public final String getCardNo() {
		return CardNo;
	}

	public final void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public final String getCardType() {
		return CardType;
	}

	public final void setCardType(String cardType) {
		CardType = cardType;
	}

	public final String getBankCode() {
		return BankCode;
	}

	public final void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	public final String getBranchBankName() {
		return BranchBankName;
	}

	public final void setBranchBankName(String branchBankName) {
		BranchBankName = branchBankName;
	}

	public final String getProvince() {
		return Province;
	}

	public final void setProvince(String province) {
		Province = province;
	}

	public final String getCity() {
		return City;
	}

	public final void setCity(String city) {
		City = city;
	}

	public ReturnedmoneyService getReturnedmoneyService() {
		return returnedmoneyService;
	}

	public void setReturnedmoneyService(
			ReturnedmoneyService returnedmoneyService) {
		this.returnedmoneyService = returnedmoneyService;
	}

	public final CoinService getCoinService() {
		return coinService;
	}

	public final void setCoinService(CoinService coinService) {
		this.coinService = coinService;
	}

}
