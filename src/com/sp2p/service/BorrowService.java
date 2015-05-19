package com.sp2p.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;

import com.fp2p.helper.DateHelper;
import com.ips.security.utility.IpsCrypto;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.constants.IFundConstants;
import com.sp2p.constants.IInformTemplateConstants;
import com.sp2p.dao.AccountUsersDao;
import com.sp2p.dao.BorrowDao;
import com.sp2p.dao.FundRecordDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.RechargeDao;
import com.sp2p.dao.UserDao;
import com.sp2p.dao.admin.ReBackExperienceBorrowDao;
import com.sp2p.database.Dao.Functions;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.service.admin.BorrowManageService;
import com.sp2p.service.admin.FundManagementService;

import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.config.IPayConfig;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.Database;
import com.shove.data.dao.MySQL;
import com.shove.security.Encrypt;
import com.shove.vo.PageBean;
import com.shove.web.action.IPaymentAction;
import com.shove.web.action.IPaymentConstants;
import com.shove.web.action.IPaymentUtil;

/**
 * @ClassName: FinanceService.java
 * @Author: gang.lv
 * @Date: 2013-3-4 上午11:14:21
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 借款业务处理层
 */
public class BorrowService extends BaseService {

	public static Log log = LogFactory.getLog(BorrowService.class);

	private BorrowDao borrowDao;
	private RechargeDao rechargeDao;
	private FundRecordDao fundRecordDao;
	private SelectedService selectedService;
	private BorrowManageService borrowManageService;
	private AccountUsersDao accountUsersDao;
	private UserDao userDao;
	private OperationLogDao operationLogDao;
	private FundManagementService fundManagementService;
	private IPaymentService iPaymentService;
	private ReBackExperienceBorrowDao reBackExperienceBorrowDao;

	public ReBackExperienceBorrowDao getReBackExperienceBorrowDao() {
		return reBackExperienceBorrowDao;
	}

	public void setReBackExperienceBorrowDao(
			ReBackExperienceBorrowDao reBackExperienceBorrowDao) {
		this.reBackExperienceBorrowDao = reBackExperienceBorrowDao;
	}

	public IPaymentService getiPaymentService() {
		return iPaymentService;
	}

	public void setiPaymentService(IPaymentService iPaymentService) {
		this.iPaymentService = iPaymentService;
	}

	/**
	 * @param i
	 * @throws DataException
	 * @MethodName: addBorrow
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-7 下午05:02:31
	 * @Return:
	 * @Descb: 添加借款业务处理
	 * @Throws:
	 */
	public Long addBorrow(String title, String imgPath, int borrowWay,
			int purpose, int deadLine, int paymentMode, double amount,
			double annualRate, double minTenderedSum, double maxTenderedSum,
			int raiseTerm, int excitationType, double sum, String detail,
			int excitationMode, String investPWD, int hasPWD, String remoteIP,
			long publisher, double fee, int daythe, String basePath,
			String username, double smallestFlowUnit, int circulationNumber,
			int hasCirculationNumber, int subscribe_status, String nid_log,
			double frozen_margin, String json, String jsonState,
			String tradeNo, String guaranteeNo, int subject_matter,
			int subject_activity, int subject_novice) throws Exception {
		Connection conn = MySQL.getConnection();
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		Map<String, String> maps = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			Procedures.p_borrow_initialization(conn, ds, outParameterValues,
					publisher, title, imgPath, borrowWay, "", deadLine,
					paymentMode, new BigDecimal(amount), new BigDecimal(
							annualRate), new BigDecimal(minTenderedSum),
					new BigDecimal(maxTenderedSum), new BigDecimal(raiseTerm),
					detail, 1, publisher, excitationType, new BigDecimal(sum),
					excitationMode, purpose, hasPWD, investPWD, new Date(),
					remoteIP, daythe, new BigDecimal(smallestFlowUnit),
					circulationNumber, nid_log, new BigDecimal(frozen_margin),
					"", basePath, new BigDecimal(fee), json, jsonState, "", "",
					"", "", "", 1, 0, tradeNo, guaranteeNo, subject_matter,
					subject_activity, subject_novice, "");
			result = Convert.strToLong(outParameterValues.get(0) + "", -1);
			maps.put("ret", result + "");
			maps.put("ret_desc", outParameterValues.get(1) + "");
			if (result <= 0) {
				conn.rollback();
				return -1L;
			}
			userMap = userDao.queryUserById(conn, publisher);
			operationLogDao
					.addOperationLog(conn, "t_borrow",
							Convert.strToStr(userMap.get("username"), ""),
							IConstants.INSERT,
							Convert.strToStr(userMap.get("lastIP"), ""), 0,
							"用户发布借款", 1);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: queryBorrowConcernAppByCondition
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午11:45:15
	 * @Return:
	 * @Descb: app关注的借款列表查询
	 * @Throws:
	 */
	public void queryBorrowConcernAppByCondition(String title,
			String publishTimeStart, String publishTimeEnd, long userId,
			PageBean pageBean, String deadline, String borrowWay)
			throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and borrowTitle LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(publishTimeStart)) {
			condition.append(" and publishTime >'"
					+ StringEscapeUtils.escapeSql(publishTimeStart.trim())
					+ "'");
		}
		if (StringUtils.isNotBlank(deadline)) {
			condition.append(" and deadline ='"
					+ StringEscapeUtils.escapeSql(deadline.trim()) + "'");
		}
		if (StringUtils.isNotBlank(borrowWay)) {
			condition.append(" and borrowWay ='"
					+ StringEscapeUtils.escapeSql(borrowWay.trim()) + "'");
		}
		if (StringUtils.isNotBlank(publishTimeEnd)) {
			condition.append(" and publishTime <'"
					+ StringEscapeUtils.escapeSql(publishTimeEnd.trim()) + "'");
		}
		condition.append(" and userId =" + userId);
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_borrow_concern", resultFeilds,
					" order by id desc", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: queryCreditingByCondition
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-8 下午05:13:17
	 * @Return:
	 * @Descb: 根据条件查询信用申请信息
	 * @Throws:
	 */
	public void queryCreditingByCondition(long userId, PageBean pageBean)
			throws Exception {
		String resultFeilds = " id,creditingName,applyAmount,applyDetail,status,agreeAmount";
		StringBuffer condition = new StringBuffer();
		condition.append(" and applyer =" + userId);
		Connection conn = MySQL.getConnection();

		try {
			dataPage(conn, pageBean, " v_t_crediting_list", resultFeilds,
					" order by id desc", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: queryCreditingApply
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-8 下午07:53:11
	 * @Return:
	 * @Descb: 查询能够再次申请信用额度的记录
	 * @Throws:
	 */
	public Map<String, String> queryCreditingApply(long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowDao.queryCreditingApply(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * @throws DataException
	 * @MethodName: addCrediting
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-8 下午04:29:23
	 * @Return:
	 * @Descb: 添加信用申请
	 * @Throws:
	 */
	public Long addCrediting(double applyAmount, String applyDetail, long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			result = borrowDao.addCrediting(conn, applyAmount, applyDetail,
					userId);
			if (result <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_crediting",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"发布额度申请", 1);
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * @MethodName: queryBorrowTypeNetValueCondition
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-9 下午01:00:07
	 * @Return:
	 * @Descb: 查询用户的净值借款条件记录
	 * @Throws:
	 */
	public Map<String, String> queryBorrowTypeNetValueCondition(long userId,
			double borrowSum) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		double amount = 0;
		// 待收金额
		double forpaySum = 0;
		// 待还金额
		double forRepaySum = 0;
		// 可用金额
		double usableSum = 0;

		try {
			// 待收借款
			Map<String, String> forpayBorrowMap = borrowDao.queryForpayBorrow(
					conn, userId);
			if (forpayBorrowMap == null) {
				forpayBorrowMap = new HashMap<String, String>();
			}
			forpaySum = Convert.strToDouble(forpayBorrowMap.get("forpaySum")
					+ "", 0);
			// 待还借款
			Map<String, String> forRePaySumMap = borrowDao.queryForRepayBorrow(
					conn, userId);
			if (forRePaySumMap == null) {
				forRePaySumMap = new HashMap<String, String>();
			}
			forRepaySum = Convert.strToDouble(forRePaySumMap.get("forRepaySum")
					+ "", 0);
			// 用户可用金额
			Map<String, String> userAmountMap = borrowDao.queryUserAmount(conn,
					userId);

			if (userAmountMap == null) {
				userAmountMap = new HashMap<String, String>();
			}
			usableSum = Convert.strToDouble(
					userAmountMap.get("usableSum") + "", 0);
			amount = usableSum + forpaySum - forRepaySum;
			// 净值金额大于10000才可以发布
			if (amount >= 10000) {
				if (borrowSum > 0) {
					// 发布借款的上限额
					amount = amount * 0.7;
					if (borrowSum <= amount) {
						map.put("result", "1");
					} else {
						map.put("result", "0");
					}
				} else {
					map.put("result", "1");
				}
			} else {
				map.put("result", "0");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * @MethodName: queryBorrowTypeSecondsCondition
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-9 下午01:01:16
	 * @Return:
	 * @Descb: 查询用户的秒还借款条件记录
	 * @Throws:
	 */
	public Map<String, String> queryBorrowTypeSecondsCondition(
			double borrowAmount, double borrowAnnualRate, long userId,
			Map<String, Object> platformCostMap, double frozenMargin)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowDao.queryBorrowTypeSecondsCondition(conn, borrowAmount,
					borrowAnnualRate, userId, platformCostMap, frozenMargin);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 查询用户可以资金是否小于保障金额
	 * 
	 * @param frozen
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryBorrowFinMoney(double frozen, long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowDao.queryBorrowFinMoney(conn, frozen, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * @MethodName: refreshBorrowTime
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-17 上午10:58:33
	 * @Return:
	 * @Descb: 刷新借款时间
	 * @Throws:
	 */
	public Long refreshBorrowTime() throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		// 借款id
		long id = -1;
		// 用户id
		SimpleDateFormat sf = new SimpleDateFormat(DateHelper.DATE_SIMPLE);
		String date = "";
		Map<String, String> maxTime = null;
		Map<String, String> minTime = null;
		Map<String, String> borrowStatus = null;
		try {
			List<Map<String, Object>> borrowList = borrowDao.queryBorrow(conn);
			for (Map<String, Object> borrowMap : borrowList) {
				date = sf.format(new Date());
				id = Convert.strToLong(borrowMap.get("id") + "", -1);
				// 当前时间小于剩余结束时间,剩余开始时间为当前时间
				maxTime = borrowDao.queryMaxTime(conn, id, date);
				if (maxTime != null && maxTime.size() > 0) {
					borrowDao.updateTime(conn, id, date);
				}
				// 当前时间大于剩余结束时间,剩余开始时间为剩余结束时间
				minTime = borrowDao.queryMinTime(conn, id, date);
				if (minTime != null && minTime.size() > 0) {
					// 借款总额等于投资总额,则为满标,否则流标
					borrowStatus = borrowDao.queryBorrowState(conn, id);
					if (borrowStatus != null && borrowStatus.size() > 0) {
						// 更新借款为满标 满标sorts 为 5
						borrowDao.updateBorrowState(conn, id,
								IConstants.BORROW_STATUS_3, 5);
					} else {
						// 借款标的信息
						Map<String, String> map2 = borrowDao
								.queryBorrowhasInvestAmount(conn, id);
						int isDayThe = Convert
								.strToInt(map2.get("isDayThe"), 1);
						// 天标处理
						if (isDayThe == 2) {
							log.info("体验金流标处理.....");
							double hasInvestAmount = Convert.strToDouble(
									map2.get("hasInvestAmount"), 0.00);
							if (hasInvestAmount > 0) {
								// 如果有人投资
								this.reBackExperienceBorrow(conn, id, borrowMap);
							} else {
								// 如果有人投资 没有人投资
								long ret = -1;
								// 修改标状态
								ret = reBackExperienceBorrowDao
										.updateBorrowStatus(conn, 6, id);
								if (ret <= 0) {
									conn.rollback();
									log.info("体验标流标更改标的状态时失败");
								}
							}
							log.info("体验金流标处理结束.....");
						} else {
							// 非天标处理
							// 解冻保证金
							Map<String, String> data = guaranteeUnfreeze(id);
							if (data != null && data.size() > 0) {
								String pErrCode = data.get("pErrCode");
								if ("-100".equals(pErrCode)) {
									// 解冻投资人投资金额
									Map<String, String> map = borrowDao
											.queryBorrowhasInvestAmount(conn,
													id);
									if (map != null && map.size() > 0) {
										double hasInvestAmount = Convert
												.strToDouble(
														map.get("hasInvestAmount"),
														0.00);
										String tradeNo = map.get("tradeNo");
										if (hasInvestAmount > 0) {
											// IPaymentAction ips = new
											// IPaymentAction();
											// 解冻投资人投资金额
											data = iPaymentService.checkTrade(
													tradeNo, "6", tradeNo);
											if (data != null && data.size() > 0) {
												pErrCode = data.get("pErrCode");
												String pMerCode = data
														.get("pMerCode");
												String pErrMsg = data
														.get("pErrMsg");

												String p3DesXmlPara = data
														.get("p3DesXmlPara");
												Map<String, String> parseXml = IPaymentUtil
														.parseXmlToJson(p3DesXmlPara);
												String pMerBillNo = parseXml
														.get("parseXml");
												String pSign = data
														.get("pSign");// 签名
												boolean sign = IPaymentUtil
														.checkSign(pMerCode,
																pErrCode,
																pErrMsg,
																p3DesXmlPara,
																pSign);
												if (!sign) {
													log.info("流标解冻投资人投资金额签名失败！");
												} else {
													Map<String, String> fundMap = fundManagementService
															.queryFundRecord(pMerBillNo);
													if (fundMap == null) {
														if (!pErrCode
																.equals("0000")) {
															// 操作失败
															log.info("流标解冻投资人投资金额环迅处理失败！");
														} else {
															// 更新借款为流标
															borrowManageService
																	.reBackBorrow(
																			id,
																			-1,
																			IConstants.WEB_URL,
																			pMerBillNo);
														}
													}
												}
											}
										} else {
											// 更新借款为流标
											borrowManageService.reBackBorrow(
													id, -1, IConstants.WEB_URL,
													null);
										}
									}
								} else {
									String pMerCode = data.get("pMerCode");
									String pErrMsg = data.get("pErrMsg");
									String p3DesXmlPara = data
											.get("p3DesXmlPara");
									Map<String, String> parseXml = IPaymentUtil
											.parseXmlToJson(p3DesXmlPara);
									String pMerBillNo = parseXml
											.get("parseXml");
									String pSign = data.get("pSign");// 签名
									boolean sign = IPaymentUtil.checkSign(
											pMerCode, pErrCode, pErrMsg,
											p3DesXmlPara, pSign);
									if (!sign) {
										log.info("流标签名失败！");
									} else {
										Map<String, String> fundMap = fundManagementService
												.queryFundRecord(pMerBillNo);
										if (fundMap == null) {
											if (!pErrCode.equals("0000")) {
												// 操作失败
												log.info("流标环迅操作失败");
											} else {
												// 解冻投资人投资金额
												Map<String, String> map = borrowDao
														.queryBorrowhasInvestAmount(
																conn, id);
												if (map != null
														&& map.size() > 0) {
													double hasInvestAmount = Convert
															.strToDouble(
																	map.get("hasInvestAmount"),
																	0.00);
													String tradeNo = map
															.get("tradeNo");
													if (hasInvestAmount > 0) {
														// IPaymentAction ips =
														// new
														// IPaymentAction();
														// 解冻投资人投资金额
														data = iPaymentService
																.checkTrade(
																		tradeNo,
																		"6",
																		tradeNo);
														if (data != null
																&& data.size() > 0) {
															pErrCode = data
																	.get("pErrCode");
															pMerCode = data
																	.get("pMerCode");
															pErrMsg = data
																	.get("pErrMsg");
															p3DesXmlPara = data
																	.get("p3DesXmlPara");
															parseXml = IPaymentUtil
																	.parseXmlToJson(p3DesXmlPara);
															pMerBillNo = parseXml
																	.get("parseXml");
															pSign = data
																	.get("pSign");// 签名
															sign = IPaymentUtil
																	.checkSign(
																			pMerCode,
																			pErrCode,
																			pErrMsg,
																			p3DesXmlPara,
																			pSign);
															if (!sign) {
																log.info("流标解冻投资人投资金额签名失败！");
															} else {
																fundMap = fundManagementService
																		.queryFundRecord(pMerBillNo);
																if (fundMap == null) {
																	if (!pErrCode
																			.equals("0000")) {
																		// 操作失败
																		log.info("流标解冻投资人投资金额环迅处理失败！");
																	} else {
																		// 更新借款为流标
																		borrowManageService
																				.reBackBorrow(
																						id,
																						-1,
																						IConstants.WEB_URL,
																						pMerBillNo);
																	}
																}
															}
														}
													} else {
														// 更新借款为流标
														borrowManageService
																.reBackBorrow(
																		id,
																		-1,
																		IConstants.WEB_URL,
																		pMerBillNo);
													}
												}
											}
										}
									}
								}
							} else {
								// 环迅输出参数为null，跳转到环迅维护页面
								log.info("操作失败，环迅系统维护中！");
							}
						}
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 体验标流标处理
	 * 
	 * @param conn
	 * @param id
	 * @param borrowMap
	 * @throws SQLException
	 * @throws DataException
	 */
	private void reBackExperienceBorrow(Connection conn, long id,
			Map<String, Object> borrowMap) throws SQLException, DataException {

		long ret = -1;
		// 修改标状态
		ret = reBackExperienceBorrowDao.updateBorrowStatus(conn, 6, id);
		if (ret <= 0) {
			conn.rollback();
			log.info("体验标流标更改标的状态时失败");
		} else {
			// 获取通知用户的信息
			long publisher = Convert.strToLong(
					String.valueOf(borrowMap.get("publisher")), 0);
			Map<String, String> borrowInfoMap = reBackExperienceBorrowDao
					.getBorrowerInfo(conn, publisher);
			if (borrowInfoMap != null && borrowInfoMap.size() > 0) {
				String username_borrower = borrowInfoMap.get("username");
				// 通知模板
				Map<String, String> mailTemplateForBorrowerMap = reBackExperienceBorrowDao
						.getNoticeTemplate(conn, "borrow_over");
				Map<String, String> eMailTemplateForBorrowerMap = reBackExperienceBorrowDao
						.getNoticeTemplate(conn, "e_borrow_over");
				Map<String, String> smsTemplateForBorrowerMap = reBackExperienceBorrowDao
						.getNoticeTemplate(conn, "s_borrow_over");
				int borrowWay = Convert.strToInt(
						String.valueOf(borrowMap.get("borrowWay")), 0);
				String borrowTitle = String.valueOf(borrowMap
						.get("borrowTitle"));
				String payment_mode = "";
				if (borrowWay == 1) {
					payment_mode = "净值借款";
				} else if (borrowWay == 2) {
					payment_mode = "秒还借款";
				} else if (borrowWay == 3) {
					payment_mode = "普通借款";
				} else if (borrowWay == 4) {
					payment_mode = "实地考察借款";
				} else if (borrowWay == 5) {
					payment_mode = "机构担保借款";
				} else if (borrowWay == 6) {
					payment_mode = "流转标借款";
				} else {
					payment_mode = "";
				}
				String strmailTemplateForBorrower = mailTemplateForBorrowerMap
						.get("template");
				String streMailTemplateForBorrower = eMailTemplateForBorrowerMap
						.get("template");
				String strsmsTemplateForBorrower = smsTemplateForBorrowerMap
						.get("template");
				strmailTemplateForBorrower = strmailTemplateForBorrower
						.replace("秒还", payment_mode).replace("title",
								borrowTitle);
				streMailTemplateForBorrower = streMailTemplateForBorrower
						.replace("秒还", payment_mode).replace("title",
								borrowTitle);
				strsmsTemplateForBorrower = strsmsTemplateForBorrower
						.replace("秒还", payment_mode)
						.replace("title", borrowTitle)
						.replace("title", borrowTitle)
						.replace("date", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"))
						.replace("username", username_borrower);

				// 插入通知
				ret = reBackExperienceBorrowDao.insertNoticeTask(conn,
						publisher, username_borrower,
						borrowInfoMap.get("email"),
						borrowInfoMap.get("cellphone"),
						strmailTemplateForBorrower,
						streMailTemplateForBorrower, strsmsTemplateForBorrower,
						id);

				// 获取所有投资人
				List<Map<String, String>> investorsList = reBackExperienceBorrowDao
						.getAllInvestors(conn, id);

				if (investorsList != null && investorsList.size() > 0) {
					// 解冻投资人投资金额
					for (Map<String, String> investor : investorsList) {
						double realAmount = Convert.strToDouble(
								String.valueOf(investor.get("realAmount")), -1);
						long investorId = Convert.strToLong(
								String.valueOf(investor.get("investor")), -1);
						long investId = Convert.strToLong(
								String.valueOf(investor.get("id")), -1);
						String username_investor = String.valueOf(investor
								.get("username"));
						Map<String, String> investInfoMap = reBackExperienceBorrowDao
								.getBorrowerInfo(conn, investorId);
						ret = reBackExperienceBorrowDao
								.updateInvestorExpreienceMoney(conn,
										realAmount, investorId);
						if (ret <= 0) {
							conn.rollback();
							log.info("体验标流标回款失败");
							break;
						}
						// // 添加资金记录
						// String t_content = "借款[<a href=\""
						// + WebUtil
						// .getWebPath()
						// + "/financeDetail.do?id="
						// + id
						// + "\"  target=\"_blank\">"
						// + borrowTitle
						// + "</a>]已撤消["
						// + String.format(
						// "%.2f",
						// realAmount)
						// + "]元";
						//
						// reBackExperienceBorrowDao
						// .insertUpdateInvestorExpreienceMoneyRecord(
						// conn,
						// realAmount,
						// investorId,
						// investId,
						// t_content,
						// "");
						// 发送通知
						// 通知模板
						Map<String, String> mailTemplateForInvestorMap = reBackExperienceBorrowDao
								.getNoticeTemplate(conn, "tender_over");
						Map<String, String> eMailTemplateForInvestorMap = reBackExperienceBorrowDao
								.getNoticeTemplate(conn, "e_tender_over");
						Map<String, String> smsTemplateForInvestorMap = reBackExperienceBorrowDao
								.getNoticeTemplate(conn, "s_tender_over");

						String strmailTemplateForInvestor = mailTemplateForInvestorMap
								.get("template");
						String streMailTemplateForInvestor = eMailTemplateForInvestorMap
								.get("template");
						String strsmsTemplateForInvestor = smsTemplateForInvestorMap
								.get("template");
						strmailTemplateForInvestor = strmailTemplateForInvestor
								.replace("title", borrowTitle).replace(
										"rebackAmount",
										String.format("%.2f", realAmount));
						streMailTemplateForInvestor = streMailTemplateForInvestor
								.replace("title", borrowTitle).replace(
										"rebackAmount",
										String.format("%.2f", realAmount));
						strsmsTemplateForInvestor = strsmsTemplateForInvestor
								.replace("title", borrowTitle)
								.replace("rebackAmount",
										String.format("%.2f", realAmount))
								.replace("username", username_investor)
								.replace("date",
										DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));

						// 插入通知
						ret = reBackExperienceBorrowDao.insertNoticeTask(conn,
								investorId, username_investor,
								investInfoMap.get("email"),
								investInfoMap.get("cellphone"),
								strmailTemplateForInvestor,
								streMailTemplateForInvestor,
								strsmsTemplateForInvestor, id);
					}
				}
			}
		}
	}

	/**
	 * 解冻保证金
	 * 
	 * @param tradeNo
	 * @param status
	 * @param pContractNo
	 * @param pFreezeMerBillNo
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> guaranteeUnfreeze(long idLong) throws Exception {
		LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		String pMerBillNo = IPaymentUtil.getIn_orderNo();
		Map<String, String> borrowDetailMap = borrowManageService
				.queryBorrowInfo(idLong);

		String tradeNo = "";
		String pFreezeMerBillNo = "";
		String pIdentNo = "";
		String pRealName = "";
		String pIpsAcctNo = "";
		String pUnfreezeAmt = "";
		if (borrowDetailMap != null && borrowDetailMap.size() > 0) {
			tradeNo = borrowDetailMap.get("tradeNo");
			pFreezeMerBillNo = borrowDetailMap.get("guaranteeNo");
			pIdentNo = borrowDetailMap.get("idNo");
			pRealName = borrowDetailMap.get("realName");
			pIpsAcctNo = borrowDetailMap.get("portMerBillNo");
			double frozen_margin = Convert.strToDouble(
					borrowDetailMap.get("frozen_margin"), 0.00);
			String borrowWay = borrowDetailMap.get("borrowWay");
			if ("2".equals(borrowWay)) {
				Map<String, Object> platformCostMap = getPlatformCost();
				double costFee = Convert.strToDouble(
						platformCostMap.get(IAmountConstants.BORROW_FEE_RATE_1)
								+ "", 0);
				double annualRate = Convert.strToDouble(
						borrowDetailMap.get("annualRate"), 0.00);
				double borrowAmount = Convert.strToDouble(
						borrowDetailMap.get("borrowAmount"), 0.00);
				// 秒还借款冻结借款+借款利息+借款手续费 + 冻结保证金
				double fee = borrowAmount * ((annualRate * 0.01) / 12.0)
						+ (borrowAmount * costFee);
				frozen_margin = frozen_margin + fee;
			}
			pUnfreezeAmt = String.format("%.2f", frozen_margin);
		}
		if ("".equals(pUnfreezeAmt) || "0.00".equals(pUnfreezeAmt)) {
			Map<String, String> data = new HashMap<String, String>();
			data.put("pErrCode", "-100");
			return data;
		}
		map.put("pBidNo", tradeNo);
		map.put("pContractNo", tradeNo);
		map.put("pFreezeMerBillNo", pFreezeMerBillNo);
		map.put("pMerBillNo", pMerBillNo);
		map.put("pUnfreezeDate", DateFormatUtils.format(new Date(), "yyyyMMdd"));
		map.put("pUnfreezeUserType", "1");
		map.put("pAcctType", "1");
		map.put("pIdentNo", pIdentNo);
		map.put("pRealName", pRealName);
		map.put("pIpsAcctNo", pIpsAcctNo);
		map.put("pUnfreezeAmt", pUnfreezeAmt);
		map.put("pS2SUrl", IPaymentConstants.url + "/reGuaranteeUnfreezeUrl.do");
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
				IPaymentConstants.guaranteeUnfreeze, "argMerCode",
				"arg3DesXmlPara", "argSign", strSign);
		log.info("********" + soap);
		String url = IPayConfig.ipay_web_gateway
				+ IPaymentConstants.guaranteeUnfreeze;
		Map<String, String> data = IPaymentUtil.webService(soap, url);
		return data;
	}

	/**
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: queryBorrowConcernByCondition
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午11:45:15
	 * @Return:
	 * @Descb: 关注的借款列表查询
	 * @Throws:
	 */
	public void queryBorrowConcernByCondition(String title,
			String publishTimeStart, String publishTimeEnd, long userId,
			PageBean pageBean) throws Exception {
		String resultFeilds = " * ";
		StringBuffer condition = new StringBuffer();
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(publishTimeStart)) {
			condition.append(" and publishTime >'"
					+ StringEscapeUtils.escapeSql(publishTimeStart.trim())
					+ "'");
		}
		if (StringUtils.isNotBlank(publishTimeEnd)) {
			condition.append(" and publishTime <'"
					+ StringEscapeUtils.escapeSql(publishTimeEnd.trim()) + "'");
		}
		condition.append(" and userId =" + userId);
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_borrow_concern", resultFeilds,
					" order by id desc", condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @throws DataException
	 * @MethodName: delBorrowConcern
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-19 上午12:24:56
	 * @Return:
	 * @Descb: 删除关注的借款
	 * @Throws:
	 */
	public Long delBorrowConcern(long idLong, Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			result = borrowDao.delBorrowConcern(conn, idLong, userId);
			if (result < 0) {
				conn.rollback();
				return result;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_concern",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.DELETE,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"删除关注的借款", 1);
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * @MethodName: queryCreditLimit
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-3-25 下午10:09:55
	 * @Return:
	 * @Descb: 查询可用信用额度
	 * @Throws:
	 */
	public Map<String, String> queryCreditLimit(double amountDouble, Long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowDao.queryCreditLimit(conn, amountDouble, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * houli 查询是否有未满标审核的借款标的
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long queryBorrowStatus(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> lists;
		int count = 0;
		try {
			lists = borrowDao.queryBorrowStatus(conn, userId);

			if (lists == null || lists.size() <= 0) {// 如果没有该用户的借款信息，那么该用户可以发布借款
				return 1L;
			} else {
				for (Map<String, Object> map : lists) {
					int status = Convert.strToInt(map.get("borrowStatus")
							.toString(), -1);
					if (status > 3) {// 如果该用户的借款标的已经满标审核通过
						count++;
					}
				}
				return count == lists.size() ? 1L : -1L;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * add by houli
	 * 
	 * @param userId
	 * @param status
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long queryBaseApprove(Long userId, int status) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			Map<String, String> map = borrowDao.queryBaseApprove(conn, userId,
					status);

			if (map == null || map.size() <= 0) {
				return -1L;
			}
			return 1L;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询5项认证信息
	* @Title: queryBaseFiveApprove 
	* @param
	* @return Long    返回类型 
	* @throws
	 */
	public Long queryBaseFiveApprove(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			Map<String, String> map = borrowDao.queryBaseFiveApprove(conn,
					userId);

			if (map == null || map.size() <= 0) {
				return -1L;
			}
			int status = Convert.strToInt(map.get("auditStatus"), -1);
			if (status < 15) {
				return -1L;
			}
			return 1L;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @throws SQLException
	 * @throws DataException
	 * @MethodName: addCirculationBorrow
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-5-17 下午05:06:07
	 * @Return:
	 * @Descb: 添加流转标借款
	 * @Throws:
	 */
	/*
	 * public long addCirculationBorrow(String title, String imgPath, int
	 * borrowWay, int purposeInt, int deadLineInt, int paymentMode, double
	 * amountDouble, double annualRateDouble, String remoteIP, int
	 * circulationNumber, double smallestFlowUnitDouble, Long id, String
	 * businessDetail, String assets, String moneyPurposes, int dayThe, String
	 * basePath, String userName, int excitationTypeInt, double sumInt, String
	 * json, String jsonState, String nid, String agent, String counterAgent,
	 * double fee, String tradeNo) throws Exception { Connection conn =
	 * MySQL.getConnection(); DataSet ds = new DataSet(); List<Object>
	 * outParameterValues = new ArrayList<Object>(); Map<String, String> maps =
	 * new HashMap<String, String>(); Map<String, String> userMap = new
	 * HashMap<String, String>(); Long result = -1L; try {
	 * Procedures.p_borrow_initialization(conn, ds, outParameterValues, id,
	 * title, imgPath, borrowWay, "", deadLineInt, paymentMode, new
	 * BigDecimal(amountDouble), new BigDecimal(annualRateDouble), new
	 * BigDecimal(-1), new BigDecimal(-1), new BigDecimal(deadLineInt), "", 1,
	 * id, excitationTypeInt, new BigDecimal(sumInt), 1, purposeInt, -1, "", new
	 * Date(), remoteIP, dayThe, new BigDecimal(smallestFlowUnitDouble),
	 * circulationNumber, nid, new BigDecimal(fee), "", basePath, new
	 * BigDecimal(fee), json, jsonState, agent, counterAgent, businessDetail,
	 * assets, moneyPurposes, 2, 0, tradeNo, "");
	 * 
	 * result = Convert.strToLong(outParameterValues.get(0) + "", -1);
	 * maps.put("ret", result + ""); maps.put("ret_desc",
	 * outParameterValues.get(1) + ""); if (result <= 0) { conn.rollback();
	 * return -1L; } // 添加操作日志 userMap = userDao.queryUserById(conn, id);
	 * operationLogDao.addOperationLog(conn, "t_borrow", Convert.strToStr(
	 * userMap.get("username"), ""), IConstants.INSERT, Convert
	 * .strToStr(userMap.get("lastIP"), ""), 0, "用户发布借款", 1);
	 * 
	 * conn.commit(); } catch (Exception e) { log.error(e); e.printStackTrace();
	 * conn.rollback();
	 * 
	 * throw e; } finally { conn.close(); } return result; }
	 */

	/**
	 * @MethodName: queryCurrentCreditLimet
	 * @Param: BorrowService
	 * @Author: gang.lv
	 * @Date: 2013-5-11 下午04:47:31
	 * @Return:
	 * @Descb: 查询当前可用信用额度
	 * @Throws:
	 */
	public Map<String, String> queryCurrentCreditLimet(Long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowDao.queryCreditLimit(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 根据id查询状态
	* @Title: queryBorrowStatusByborrowId 
	* @param
	 * @Author:yinzisong
	 * @Date: 2015-1-13 下午04:47:31
	* @return Map<String,String>    返回类型 
	* @throws
	 */
	public Map<String, String> queryBorrowStatusByborrowId(Long borrowId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrowDao.queryBorroeById(conn, borrowId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}
	
	
	
	public void setAccountUsersDao(AccountUsersDao accountUsersDao) {
		this.accountUsersDao = accountUsersDao;
	}

	public FundRecordDao getFundRecordDao() {
		return fundRecordDao;
	}

	public void setFundRecordDao(FundRecordDao fundRecordDao) {
		this.fundRecordDao = fundRecordDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public BorrowManageService getBorrowManageService() {
		return borrowManageService;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}
	
	public RechargeDao getRechargeDao() {
		return rechargeDao;
	}

	public void setRechargeDao(RechargeDao rechargeDao) {
		this.rechargeDao = rechargeDao;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}
	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
	}
	public BorrowDao getBorrowDao() {
		return borrowDao;
	}

	public void setBorrowDao(BorrowDao borrowDao) {
		this.borrowDao = borrowDao;
	}
}
