/**   
 * @Title: QianduoduoPayService.java 
 * @Package com.shove.services 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author yinzisong 690713748@qq.com   
 * @date 2015年1月5日 下午3:41:31 
 * @version V1.0   
 */
package com.shove.services;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.BeanHelper;
import com.google.gson.Gson;
import com.shove.Convert;
import com.shove.config.QianduoduoConfig;
import com.shove.data.dao.MySQL;
import com.shove.shuanquanUtil.Common;
import com.shove.shuanquanUtil.HttpClientUtil;
import com.shove.shuanquanUtil.MD5;
import com.shove.shuanquanUtil.RsaHelper;
import com.shove.vo.LoanInfoBean;
import com.shove.vo.LoanRegisterBindReturnBean;
import com.shove.vo.LoanTransferBean;
import com.shove.vo.LoanTransferReturnBean;
import com.shove.web.action.QianduoduoPayConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.admin.ProtectOldUserDao;
import com.sp2p.entity.User;
import com.sp2p.service.BorrowService;
import com.sp2p.service.FinanceService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserIntegralService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;
import com.sp2p.service.admin.ReturnedmoneyService;

/**
 * 乾多多第三方处理service.
 * 
 * @ClassName: QianduoduoPayService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author yinzisong 690713748@qq.com
 * @date 2015年1月5日 下午3:41:31
 * 
 */
public class QianduoduoPayService {

	private static Log log = LogFactory.getLog(QianduoduoPayService.class);
	private UserService userService;
	private UserIntegralService userIntegralService;

	private FundManagementService fundManagementService;
	private RechargeService rechargeService;
	private FinanceService financeService;
	private BorrowManageService borrowManageService;
	private BorrowService borrowService;
	private ProtectOldUserDao protectOldUserDao;
	private ReturnedmoneyService returnedmoneyService;

	/**
	 * 绑定第三方账号（全自动）.
	 * 
	 * @Title: registerbind
	 * @param user
	 *            用户信息.
	 * @param paramMap
	 *            页面提交的参数.
	 * @param SubmitURL
	 *            第三方地址.
	 * @param returnURL
	 *            页面返回地址.
	 * @param notifyURL
	 *            通知接口地址.
	 * @throws Exception
	 *             设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String registerbind(User user, Map<String, String> paramMap,
			Map<String, String> loanRegisterbindMap, String SubmitURL)
			throws Exception {
		String pReturn = "";
		String[] resultarr = HttpClientUtil.doPostQueryCmd(SubmitURL,
				loanRegisterbindMap);

		System.out.println(resultarr[1]);
		// 判断是否有返回值
		if (StringUtils.isNotBlank(resultarr[1])
				&& resultarr[1].startsWith("{")) {
			// 返回信息
			LoanRegisterBindReturnBean lrbrb = (LoanRegisterBindReturnBean) new Gson()
					.fromJson(resultarr[1], LoanRegisterBindReturnBean.class);

			if (lrbrb != null) {
				String publickey = QianduoduoConfig.publicKey;

				String dataStr = lrbrb.getAccountType()
						+ lrbrb.getAccountNumber() + lrbrb.getMobile()
						+ lrbrb.getEmail() + lrbrb.getRealName()
						+ lrbrb.getIdentificationNo()
						+ lrbrb.getLoanPlatformAccount()
						+ lrbrb.getMoneymoremoreId()
						+ lrbrb.getPlatformMoneymoremore() + lrbrb.getAuthFee()
						+ lrbrb.getAuthState() + lrbrb.getRandomTimeStamp()
						+ lrbrb.getRemark1() + lrbrb.getRemark2()
						+ lrbrb.getRemark3() + lrbrb.getResultCode();
				if ("1".equals(QianduoduoConfig.antistate)) {
					MD5 md5 = new MD5();
					dataStr = md5.getMD5Info(dataStr);
				}
				// 签名
				RsaHelper rsa = RsaHelper.getInstance();
				boolean verifySignature = rsa.verifySignature(
						lrbrb.getSignInfo(), dataStr, publickey);
				System.out.println(Boolean.toString(verifySignature));
				if (!verifySignature) {
					pReturn = "签名失败!";
				} else {
					pReturn = this.registerbindBaseDeal(
							lrbrb.getMoneymoremoreId(), lrbrb.getResultCode(),
							lrbrb.getMessage(),
							loanRegisterbindMap.get("loanPlatformAccount"),
							user);
				}
			} else {
				pReturn = "系统繁忙，请稍后绑定结果!";
			}
		} else {
			pReturn = "系统繁忙，请稍后绑定结果!";
		}
		return pReturn;
	}

	/**
	 * 处理绑定第三方账号（本地操作）.
	 * 
	 * @Title: registerbind
	 * @param lrbrb
	 *            返回信息.
	 * @param orderNo
	 *            流水号.
	 * @param @param user 设定文件
	 * @return void 返回类型
	 * @throws Exception
	 */
	public String registerbindBaseDeal(String moneymoremoreId,
			String resultCode, String message, String orderNo, User user) {
		log.info("进入处理绑定第三方账号（本地操作）.registerbindBaseDeal");
		String pReturn = "";
		// 是否有其他进行成功处理本地操作
		boolean baseDealSuccess;
		try {
			baseDealSuccess = userService.queryUserByIpsBillNo(moneymoremoreId);

			if (baseDealSuccess == false) {
				log.info("执行本地绑定操作");
				// 根据订单号查询要处理的用户.
				Map<String, String> userMap = userService.quePortUserId(null,
						orderNo);
				log.info("resultCode=" + resultCode);
				if (!resultCode.equals("88")) {// 乾多多返回开户失败
					pReturn = "绑定失败，" + message + "！请联系客服协助修改信息。";
				} else {
					// 添加用户乾多多账号
					Long result = userService.updatePortNo(
							userMap.get("userId"), moneymoremoreId);
					// 修改认证步骤
					result = userService.updateAuthStep(userMap.get("userId"),
							5);
					log.info("修改前-->user.AuthStep=" + user.getAuthStep());
					if (result > 0) {
						user.setPortMerBillNo(moneymoremoreId);
						if (user.getAuthStep() == 4) {
							user.setAuthStep(5);
						}
						log.info("QianduoduoPayService:user.AuthStep="
								+ user.getAuthStep());
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

						pReturn = "绑定成功!";
					}
				}
			} else {
				pReturn = "通知接口已处理";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

		}
		log.info("操作结果-->pReturn=" + pReturn);
		return pReturn;
	}

	/**
	 * 处理绑定第三方账号（本地操作Notify）.
	 * 
	 * @Title: registerbindBaseDealNotify
	 * @param lrbrb
	 *            返回信息.
	 * @param orderNo
	 *            流水号.
	 * @param @param user 设定文件
	 * @return void 返回类型
	 * @throws Exception
	 */
	public String registerbindBaseDealNotify(String moneymoremoreId,
			String resultCode, String message, String orderNo) throws Exception {
		String pReturn = "";
		// 是否有其他进行成功处理本地操作
		boolean baseDealSuccess = userService
				.queryUserByIpsBillNo(moneymoremoreId);
		if (baseDealSuccess == false) {
			log.info("执行本地绑定操作");
			// 根据订单号查询要处理的用户.
			Map<String, String> userMap = userService.quePortUserId(null,
					orderNo);

			if (!resultCode.equals("88")) {// 乾多多返回开户失败
				pReturn = "绑定失败，" + message + "！请联系客服协助修改信息。";
			} else {
				// 添加用户乾多多账号
				Long result = userService.updatePortNo(userMap.get("userId"),
						moneymoremoreId);
				// 修改认证步骤
				result = userService.updateAuthStep(userMap.get("userId"), 5);
				if (result > 0) {
					long userId = Convert.strToInt(userMap.get("userId") + "",
							-1);
					Map<String, String> Usermap = userService
							.queryUserById(userId);
					// 查找用户的之前的信用积分
					Integer preScore = Convert.strToInt(Usermap.get("rating"),
							-1);
					// 添加积分
					userIntegralService.updateEscrowRegister(userId, 400,
							preScore);

					pReturn = "绑定成功!";
				}
			}
		}
		log.info("异步绑定操作--》pReturn" + pReturn);
		return pReturn;
	}

	/**
	 * 转账接口.
	 * 
	 * @Title: loanTransfer
	 * @param loanTransferBean
	 *            转账参数
	 * @param orderNo
	 *            订单号.
	 * @param user
	 *            用户.
	 * @param repayId
	 *            还款id(还款用)
	 * @return String 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> loanTransfer(LoanTransferBean loanTransferBean,
			String orderNo, User user, List<LoanInfoBean> listmlib,
			String... strings) throws Exception {
		String submitURL = "";
		// 判断正式还是测试环境
		if ("2".equals(IConstants.ISDEMO)) {
			// 正式
			submitURL = QianduoduoConfig.submitURLPrefix
					+ QianduoduoPayConstants.transferAccount;
		} else {
			// 测试
			submitURL = QianduoduoConfig.submitURLPrefix_test
					+ QianduoduoPayConstants.transferAccount;
		}
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
		// 签名
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
		// 存入订单记录
		Map<String, String> loanTransferMap = BeanHelper
				.beanToMap(loanTransferBean);

		// 存入订单
		Map<String, String> listmlibMap = new HashMap<String, String>();
		for (LoanInfoBean loanInfoBean : listmlib) {
			Map<String, String> loanInfoBeanMap = BeanHelper
					.beanToMap(loanInfoBean);
			JSONArray loanInfoBeanJson = JSONArray.fromObject(loanInfoBeanMap);
			listmlibMap.put("loanInfoBeanJson", loanInfoBeanJson.toString());
		}
		JSONArray loanInfoBeanJson = JSONArray.fromObject(listmlibMap);
		loanTransferMap.put("listmlibMapJson", loanInfoBeanJson.toString());
		loanTransferMap.put("userRealName", user.getRealName());
		if (strings.length > 0) {
			// 还款id
			loanTransferMap.put("repayId", strings[0]);
			if (strings.length > 1) {
				// 借款id
				loanTransferMap.put("borrowId", strings[1]);
			}
			if (strings.length > 2) {
				// 平台垫资流水号
				loanTransferMap.put("padFundedInfoOrderNo", strings[2]);
			}
		}

		userService.saveQianduoduoOrderNo(user.getId(),
				user.getPortMerBillNo(), orderNo, loanTransferMap);// 保存输入参数记录
		loanTransferMap.put("SubmitURL", submitURL);

		// 自动转账
		if (loanTransferBean.getAction() == 2) {
			// 参数
			Map<String, String> req = BeanHelper.beanToMap(loanTransferBean);

			// 调用乾多多接口
			String[] resultarr = HttpClientUtil.doPostQueryCmd(submitURL, req);

			System.out.println(resultarr[1]);

			if (StringUtils.isNotBlank(resultarr[1])
					&& (resultarr[1].startsWith("[") || resultarr[1]
							.startsWith("{"))) {
				this.dealAutoTransfer(orderNo, md5, rsa, resultarr);
			}
			return null;
		}
		return loanTransferMap;
	}

	/**
	 * 自动转账结果处理(直接处理).
	 * 
	 * @param orderNo
	 * @param md5
	 * @param rsa
	 * @param resultarr
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public void dealAutoTransfer(String orderNo, MD5 md5, RsaHelper rsa,
			String[] resultarr) throws UnsupportedEncodingException, Exception {
		String dataStr;
		// 转账
		List<Object> loanobjectlist = Common.JSONDecodeList(resultarr[1],
				LoanTransferReturnBean.class);
		if (loanobjectlist != null && loanobjectlist.size() > 0) {
			for (int i = 0; i < loanobjectlist.size(); i++) {
				if (loanobjectlist.get(i) instanceof LoanTransferReturnBean) {
					LoanTransferReturnBean ltrb = (LoanTransferReturnBean) loanobjectlist
							.get(i);
					System.out.println(ltrb);

					ltrb.setLoanJsonList(URLDecoder.decode(
							ltrb.getLoanJsonList(), "utf-8"));

					dataStr = ltrb.getLoanJsonList()
							+ ltrb.getPlatformMoneymoremore()
							+ ltrb.getAction() + ltrb.getRandomTimeStamp()
							+ ltrb.getRemark1() + ltrb.getRemark2()
							+ ltrb.getRemark3() + ltrb.getResultCode();
					if ("1".equals(QianduoduoConfig.antistate)) {
						dataStr = md5.getMD5Info(dataStr);
					}

					// 签名
					boolean verifySignature = rsa.verifySignature(
							ltrb.getSignInfo(), dataStr,
							QianduoduoConfig.publicKey);
					if (verifySignature) {
						if ("88".equals(ltrb.getResultCode())) {
							// 转账列表
							// 开始执行本地操作
							Long investorId = -1L;
							double amount = 0.0;
							String LoanJsonList = ltrb.getLoanJsonList();
							Gson gson = new Gson();
							List<Map<String, String>> list = gson
									.fromJson(
											LoanJsonList,
											new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
											}.getType());

							for (Map<String, String> map : list) {
								investorId = Convert.strToLong(
										map.get("Remark"), -1);
								amount = Convert.strToDouble(map.get("Amount"),
										0.00);
								Long borrowid = Convert.strToLong(
										map.get("BatchNo"), -1);
								String orderNoLoanJson = map.get("OrderNo");
								Map<String, String> fundMap = fundManagementService
										.queryFundRecord(orderNoLoanJson);
								if (fundMap == null) {
									Connection conn = MySQL.getConnection();
									try {
										protectOldUserDao.addInvest(conn,
												investorId, amount);
										protectOldUserDao.insertFundRecord(
												conn, amount, investorId,
												orderNoLoanJson, borrowid);
										conn.commit();
									} catch (Exception e) {
										log.error(e);
										conn.rollback();
									} finally {
										conn.close();
									}
								}
							}
						} else {
							System.out.println("自动转账失败，错误信息:"
									+ ltrb.getMessage());
						}

					}
				}
			}
		}
	}

	/**
	 * 老米护盾异步处理.
	 *
	 * @author 殷梓淞
	 * @param pMerBillNo
	 * @throws Exception
	 * @since JDK 1.7
	 */
	public void dealProtectOldUserPlan(String pMerBillNo) throws Exception {
		Gson gson = new Gson();
		Map<String, String> userMap = userService.quePortUserId(null,
				pMerBillNo);
		Map<String, String> hxBuMapXml = new HashMap<String, String>();
		if (userMap != null && userMap.size() > 0) {
			List<Map<String, String>> list2 = gson
					.fromJson(
							userMap.get("hxBuMap"),
							new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
							}.getType());
			if (list2 != null && list2.size() > 0) {
				for (int i = 0; i < list2.size(); i++) {
					hxBuMapXml = list2.get(i);
					List<Map<String, String>> list3 = gson
							.fromJson(
									hxBuMapXml.get("listmlibMapJson"),
									new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
									}.getType());
					if (list3 != null && list3.size() > 0) {
						Map<String, String> loanInfoBeanJsonMap = new HashMap<String, String>();
						for (int j = 0; j < list3.size(); j++) {
							loanInfoBeanJsonMap = list3.get(j);
							List<Map<String, String>> list4 = gson
									.fromJson(
											loanInfoBeanJsonMap
													.get("loanInfoBeanJson"),
											new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
											}.getType());

							for (Map<String, String> map : list4) {
								Long investorId = Convert.strToLong(
										map.get("remark"), -1);
								Double amount = Convert.strToDouble(
										map.get("amount"), 0.00);
								Long borrowid = Convert.strToLong(
										map.get("batchNo"), -1);
								String orderNoLoanJson = map.get("orderNo");

								Map<String, String> fundMap2 = fundManagementService
										.queryFundRecord(orderNoLoanJson);
								if (fundMap2 == null) {
									Connection conn = MySQL.getConnection();
									try {
										protectOldUserDao.addInvest(conn,
												investorId, amount);
										protectOldUserDao.insertFundRecord(
												conn, amount, investorId,
												orderNoLoanJson, borrowid);
										conn.commit();
									} catch (Exception e) {
										log.error(e);
										conn.rollback();
									} finally {
										conn.close();
									}
								}
							}
						}

					}
				}
			}
		}

	}

	/**
	 * 处理回款续投第三方转账. DealReturnedmoney:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法 – 可选).<br/>
	 *
	 * @author yinzisong
	 * @since JDK 1.7
	 */
	public void dealReturnedmoney(List<Map<String, Object>> needReturnmoneyList) {
		log.info("进入处理回款续投第三方转账方法dealReturnedmoney");
		if (needReturnmoneyList.size() > 0) {
			List<LoanInfoBean> libList = new ArrayList<LoanInfoBean>();
			try {
				for (Map<String, Object> investReMap : needReturnmoneyList) {
					double rewardmoney = Convert.strToDouble(
							investReMap.get("money").toString(), 0.00);
					long id = Convert.strToLong(investReMap.get("id")
							.toString(), -1);
					long userId = Convert.strToLong(investReMap.get("userId")
							.toString(), -1);
					Map<String, String> userMap = userService
							.queryUserById(userId);
					String repaymentOrderNo = QianduoduoPayUtil.getOrderNo("");// 垫付流水号

					LoanInfoBean loanInfoBean = new LoanInfoBean();
					// 设置loanInfoBean
					loanInfoBean
							.setLoanOutMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 乾多多标志
					loanInfoBean.setLoanInMoneymoremore(userMap.get(
							"portMerBillNo").toString());// 收款人乾多多标志
					loanInfoBean.setOrderNo(repaymentOrderNo);// 垫付流水号
					loanInfoBean.setBatchNo(String.valueOf(id));// 网贷平台标号 标id
					loanInfoBean.setExchangeBatchNo("");// 流转标标号
					loanInfoBean.setAdvanceBatchNo("");// 垫资标号
					loanInfoBean.setAmount(String.format("%.2f", rewardmoney));// 还款金额
					loanInfoBean.setFullAmount("");// 满标标额
					loanInfoBean.setTransferName("回款续投平台奖励");// 借款用途
					loanInfoBean.setRemark("");
					loanInfoBean.setSecondaryJsonList("");// 二次转账
					libList.add(loanInfoBean);
				}
				// 编码
				String LoanJsonList = new Gson().toJson(libList);
				String pMerBillNo = QianduoduoPayUtil.getOrderNo("");// 垫付订单号

				// 转账接口参数
				LoanTransferBean loanTransferBean = new LoanTransferBean();
				loanTransferBean.setLoanJsonList(LoanJsonList);// 转账列表
				loanTransferBean
						.setPlatformMoneymoremore(QianduoduoConfig.platformMoneymoremore);// 平台乾多多标识
				loanTransferBean.setTransferAction(3);// 1.投标2.还款3.其他
				loanTransferBean.setAction(2);// 1.手动转账2.自动转账
				loanTransferBean.setTransferType(2);// 1.桥连2.直连
				loanTransferBean.setNeedAudit("1");// 空.需要审核1.自动通过
				loanTransferBean.setRemark1("回款续投平台奖励");
				loanTransferBean.setRemark2(pMerBillNo);
				loanTransferBean.setRemark3("");
				loanTransferBean.setNotifyURL(QianduoduoPayConstants.url
						+ "/returnedmoneyNotify.do");
				loanTransferBean.setReturnURL("");
				// 调用转账接口
				User user = new User();
				user.setId(-1L);

				String submitURL = "";
				// 判断正式还是测试环境
				if ("2".equals(IConstants.ISDEMO)) {
					// 正式
					submitURL = QianduoduoConfig.submitURLPrefix
							+ QianduoduoPayConstants.transferAccount;
				} else {
					// 测试
					submitURL = QianduoduoConfig.submitURLPrefix_test
							+ QianduoduoPayConstants.transferAccount;
				}
				String randomTimeStamp = "";
				if ("1".equals(QianduoduoConfig.antistate)) {
					Date d = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmssSSS");
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
						+ loanTransferBean.getRemark1()
						+ loanTransferBean.getRemark2()
						+ loanTransferBean.getRemark3()
						+ loanTransferBean.getReturnURL()
						+ loanTransferBean.getNotifyURL();
				// 签名
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
				// 存入订单记录
				Map<String, String> loanTransferMap = BeanHelper
						.beanToMap(loanTransferBean);

				// 存入订单
				Map<String, String> listmlibMap = new HashMap<String, String>();
				for (LoanInfoBean loanInfoBean : libList) {
					Map<String, String> loanInfoBeanMap = BeanHelper
							.beanToMap(loanInfoBean);
					JSONArray loanInfoBeanJson = JSONArray
							.fromObject(loanInfoBeanMap);
					listmlibMap.put("loanInfoBeanJson",
							loanInfoBeanJson.toString());
				}
				JSONArray loanInfoBeanJson = JSONArray.fromObject(listmlibMap);
				loanTransferMap.put("listmlibMapJson",
						loanInfoBeanJson.toString());
				loanTransferMap.put("userRealName", user.getRealName());

				userService.saveQianduoduoOrderNo(user.getId(),
						user.getPortMerBillNo(), pMerBillNo, loanTransferMap);// 保存输入参数记录
				loanTransferMap.put("SubmitURL", submitURL);

				// 自动转账
				// 参数
				Map<String, String> req = BeanHelper
						.beanToMap(loanTransferBean);

				// 调用乾多多接口
				String[] resultarr = HttpClientUtil.doPostQueryCmd(submitURL,
						req);

				System.out.println(resultarr[1]);

				if (StringUtils.isNotBlank(resultarr[1])
						&& (resultarr[1].startsWith("[") || resultarr[1]
								.startsWith("{"))) {
					String dataStr2;
					// 转账
					List<Object> loanobjectlist = Common.JSONDecodeList(
							resultarr[1], LoanTransferReturnBean.class);
					if (loanobjectlist != null && loanobjectlist.size() > 0) {
						for (int i = 0; i < loanobjectlist.size(); i++) {
							if (loanobjectlist.get(i) instanceof LoanTransferReturnBean) {
								LoanTransferReturnBean ltrb = (LoanTransferReturnBean) loanobjectlist
										.get(i);
								System.out.println(ltrb);

								ltrb.setLoanJsonList(URLDecoder.decode(
										ltrb.getLoanJsonList(), "utf-8"));

								dataStr2 = ltrb.getLoanJsonList()
										+ ltrb.getPlatformMoneymoremore()
										+ ltrb.getAction()
										+ ltrb.getRandomTimeStamp()
										+ ltrb.getRemark1() + ltrb.getRemark2()
										+ ltrb.getRemark3()
										+ ltrb.getResultCode();
								if ("1".equals(QianduoduoConfig.antistate)) {
									dataStr2 = md5.getMD5Info(dataStr2);
								}

								// 签名
								boolean verifySignature = rsa.verifySignature(
										ltrb.getSignInfo(), dataStr2,
										QianduoduoConfig.publicKey);
								if (verifySignature) {
									if ("88".equals(ltrb.getResultCode())) {

										Map<String, String> fundMap = fundManagementService
												.queryFundRecord(pMerBillNo);
										if (fundMap == null) {
											returnedmoneyService
													.giveContinuedInvestmentReward(pMerBillNo);
										}
									}
								} else {
									System.out.println("自动转账失败，错误信息:"
											+ ltrb.getMessage());
								}

							}

						}

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.info("退出处理回款续投第三方转账方法dealReturnedmoney");

	}

	/**
	 * 充值.
	 * 
	 * @Title: loanRecharge
	 * @param order
	 *            订单号.
	 * @param resultCode
	 *            状态码.
	 * @param money
	 *            金额.
	 * @return String 返回类型
	 * @throws
	 */
	public String loanRecharge(String order, String resultCode)
			throws Exception {

		log.info("充值签名成功");
		String msg = "";
		// 本地是否操作
		Map<String, String> fundMap = fundManagementService
				.queryFundRecord(order);
		if (fundMap == null) {
			if (!"88".equals(resultCode)) {// 乾多多执行失败
				msg = "充值失败！";
			} else {
				String paybank = "乾多多支付充值";// 支付银行
				// 开始执行本地操作
				Map<String, String> userMap = userService.quePortUserId(null,
						order);
				Gson gson = new Gson();
				List<Map<String, String>> list = gson
						.fromJson(
								userMap.get("hxBuMap"),
								new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
								}.getType());
				String in_paynumber = "";
				Double money = 0.0;
				if (list != null) {
					Map<String, String> hxBuMapXml = list.get(0);
					in_paynumber = hxBuMapXml.get("nunber");
					money = Convert.strToDouble(hxBuMapXml.get("amount"), 0);
				}

				Map<String, String> resultMap = rechargeService
						.addUseraddmoney(
								Convert.strToLong(userMap.get("userId"), -1),
								money, in_paynumber, paybank, order);

				String result = resultMap.get("result");
				String description = resultMap.get("description");

				HttpServletResponse httpServletResponse = ServletActionContext
						.getResponse();
				httpServletResponse.setCharacterEncoding("utf-8");
				PrintWriter pw = httpServletResponse.getWriter();
				if (!"0".endsWith(result)) {
					pw.println("fail");
					msg = "充值失败！" + description;
				}
				msg = "充值成功!";
			}
		} else {
			if ("88".equals(resultCode)) {
				msg = "充值成功";
			} else {
				msg = "充值失败";
			}
		}
		// queryFundrecordInit.do
		return msg;
	}

	/**
	 * 投标处理.
	 * 
	 * @Title: tenderTradeDeal
	 * @param
	 * @return String 返回类型
	 * @throws Exception
	 */
	public Map<String, String> tenderTradeDeal(
			Map<String, String> loanInfoRetrunMap, String basePath)
			throws Exception {
		log.info("投标申请(非流转标)同步回调");
		Map<String, String> resultMap = new HashMap<String, String>();
		String pReturn = "操作成功";
		String url = "/finance.do";

		String pMerBillNo = loanInfoRetrunMap.get("OrderNo");// 商户订单号

		log.info("投标申请(非流转标)签名成功！");
		// 本地是否操作
		Map<String, String> fundMap = fundManagementService
				.queryFundRecord(pMerBillNo);
		if (fundMap == null) {
			log.info("投标申请(非流转标)同步执行本地操作");
			// 开始本地操作
			log.info("投标申请(非流转标)环迅执行成功，开始执行本地操作");
			long idLong = -1;
			long userId = -1;
			String userName = "";
			int status = -1;
			int num = -1;
			Map<String, String> userMap = userService.quePortUserId(null,
					pMerBillNo);
			Map<String, String> hxBuMapXml = new HashMap<String, String>();
			Gson gson = new Gson();
			if (userMap != null && userMap.size() > 0) {

				List<Map<String, String>> list = gson
						.fromJson(
								userMap.get("hxBuMap"),
								new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
								}.getType());
				if (list != null && list.size() > 0) {
					hxBuMapXml = list.get(0);
				}

				userId = Convert.strToLong(userMap.get("userId"), -1);// 投资人用户id
				userName = hxBuMapXml.get("userRealName");// 投资人姓名
				status = Convert.strToInt(hxBuMapXml.get("status"), 2);// 是否认购模式
																		// 1为认购
				num = Convert.strToInt(hxBuMapXml.get("num"), 0);// 认购份数
			}

			// 转账列表list
			List<Map<String, String>> listmlibMapList = gson
					.fromJson(
							hxBuMapXml.get("listmlibMapJson"),
							new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
							}.getType());
			Map<String, String> loanInfoBeanListMap = new HashMap<String, String>();
			if (listmlibMapList != null && listmlibMapList.size() > 0) {
				loanInfoBeanListMap = listmlibMapList.get(0);
			}

			List<Map<String, String>> loanInfoBeanList = gson
					.fromJson(
							loanInfoBeanListMap.get("loanInfoBeanJson"),
							new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
							}.getType());
			Map<String, String> loanInfoBeanMap = new HashMap<String, String>();
			if (loanInfoBeanList != null && loanInfoBeanList.size() > 0) {
				loanInfoBeanMap = loanInfoBeanList.get(0);
			}

			idLong = Convert.strToLong(loanInfoBeanMap.get("batchNo"), -1);// 标号

			double investAmount = Convert.strToDouble(
					loanInfoRetrunMap.get("Amount"), -1);

			Map<String, String> result = financeService.addBorrowInvest(idLong,
					userId, "", investAmount, basePath, userName, status, num,
					pMerBillNo, loanInfoRetrunMap.get("LoanNo"));

			String resultMSG = result.get("ret_desc");
			if ("".equals(resultMSG)) {
				// 查询是否满标，是否满标自动审核

				Map<String, String> autoMap = financeService
						.queryBorrowDetailById(idLong);
				int auditpass = Convert.strToInt(autoMap.get("auditpass"), 2);
				int borrowStatus = Convert.strToInt(
						autoMap.get("borrowStatus"), -1);
				if (borrowStatus == 3) {
					if (auditpass == 1) {
						// 满标自动审核
						// Admin admin = (Admin) session().getAttribute(
						// IConstants.SESSION_ADMIN);
						// Map<String, String> retMap = borrowManageService
						// .updateBorrowFullScaleStatus(idLong, 4l,
						// "满标自动审核", admin.getId(), basePath,
						// autoMap.get("tradeNo"));
						// long retVal = -1;
						// retVal = Convert.strToLong(retMap.get("ret") + "",
						// -1);
						// session().removeAttribute("randomCode");
						// if (retVal <= 0) {
						// pReturn = result.get("ret_desc");
						// url = "/financeDetail.do?id=" + idLong;
						// getOut().print(
						// "<script>alert('" + pReturn
						// + "');parent.location.href='"
						// + request().getContextPath() + ""
						// + url + "';</script>");
						// return null;
						// } else {
						// pReturn = "操作成功!";
						// url = "/financeDetail.do?id=" + idLong;
						// getOut().print(
						// "<script>alert('" + pReturn
						// + "');parent.location.href='"
						// + request().getContextPath() + ""
						// + url + "';</script>");
						// return null;
						// }
					}
				}
				pReturn = "操作成功!";
				url = "/financeDetail.do?id=" + idLong;

			} else {
				pReturn = result.get("ret_desc");
				url = "/financeDetail.do?id=" + idLong;
			}
		} else {

		}

		resultMap.put("pReturn", pReturn);
		resultMap.put("url", url);
		return resultMap;
	}

	/**
	 * 投标审核.
	 * 
	 * @param pMerBillNo
	 *            订单流水号.
	 * @param basePath
	 * @param fundOrderNo
	 *            资金流水号.
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> loantransferaudit(String pMerBillNo,
			String basePath, String fundOrderNo) throws Exception {
		long borrowId = -1;
		long adminId = -1;
		long status = -1;
		String auditOpinion = "";
		String tradeNo = "";
		Map<String, String> userMap = userService.quePortUserId(null,
				pMerBillNo);
		Map<String, String> hxBuMapXml = new HashMap<String, String>();
		Gson gson = new Gson();
		if (userMap != null && userMap.size() > 0) {

			List<Map<String, String>> list = gson
					.fromJson(
							userMap.get("hxBuMap"),
							new com.google.gson.reflect.TypeToken<List<Map<String, String>>>() {
							}.getType());
			if (list != null && list.size() > 0) {
				hxBuMapXml = list.get(0);
			}

			adminId = Convert.strToLong(userMap.get("userId"), -1);// 审核人id
			borrowId = Convert.strToLong(hxBuMapXml.get("borrowId"), -1);// 借款id
			auditOpinion = hxBuMapXml.get("auditOpinion");// 风控意见
			tradeNo = hxBuMapXml.get("tradeNo");// 借款流水
			status = Convert.strToLong(hxBuMapXml.get("status"), -1);// 复审状态

		}
		// 判断本地是否已经操作
		Map<String, String> borrowMap = borrowService
				.queryBorrowStatusByborrowId(borrowId);

		// 如果没有操作，执行
		Map<String, String> resultMap = new HashMap<String, String>();
		if ("3".equals(borrowMap.get("borrowStatus"))) {
			resultMap = borrowManageService
					.updateBorrowFullScaleStatusUserQianduoduo(borrowId,
							status, auditOpinion, adminId, basePath, tradeNo,
							fundOrderNo);
		}
		return resultMap;
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

	public final FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public final void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public final RechargeService getRechargeService() {
		return rechargeService;
	}

	public final void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public final FinanceService getFinanceService() {
		return financeService;
	}

	public final void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public final BorrowManageService getBorrowManageService() {
		return borrowManageService;
	}

	public final void setBorrowManageService(
			BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public final BorrowService getBorrowService() {
		return borrowService;
	}

	public final void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public final ProtectOldUserDao getProtectOldUserDao() {
		return protectOldUserDao;
	}

	public final void setProtectOldUserDao(ProtectOldUserDao protectOldUserDao) {
		this.protectOldUserDao = protectOldUserDao;
	}

	/**
	 * @return 获取的returnedmoneyService
	 */
	public final ReturnedmoneyService getReturnedmoneyService() {
		return returnedmoneyService;
	}

	/**
	 * 设置returnedmoneyService的方法.
	 * @param returnedmoneyService 赋值给returnedmoneyService的值
	 */
	public final void setReturnedmoneyService(
			ReturnedmoneyService returnedmoneyService) {
		this.returnedmoneyService = returnedmoneyService;
	}

}
