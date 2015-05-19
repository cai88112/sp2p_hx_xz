package com.sp2p.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;

import com.fp2p.helper.AmountHelper;
import com.fp2p.helper.DateHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.sp2p.dao.InvestDao;
import com.sp2p.dao.RepayMentDao;
import com.sp2p.dao.admin.BorrowManageDao;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.dao.FrontMyPaymentDao;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.security.Encrypt;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.constants.IFundConstants;
import com.sp2p.constants.IInformTemplateConstants;
import com.sp2p.dao.AccountUsersDao;
import com.sp2p.dao.BorrowDao;
import com.sp2p.dao.FinanceDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.TenderExperienceMoneyDao;
import com.sp2p.dao.UserDao;
import com.sp2p.database.Dao.Procedures;

/**
 * @ClassName: FinanceService.java
 * @Author: gang.lv
 * @Date: 2013-3-4 上午11:14:21
 * @Copyright: 2013 www.emis.com Inc. All rights reserved.
 * @Version: V1.0.1
 * @Descrb: 理财业务处理层
 */
public class FinanceService extends BaseService {

	public static Log log = LogFactory.getLog(FinanceService.class);

	private FinanceDao financeDao;
	private AwardService awardService;
	private SelectedService selectedService;
	private UserDao userDao;
	private OperationLogDao operationLogDao;
	private RepayMentDao repayMentDao;
	private InvestDao investDao;
	private BorrowDao borrowDao;
	private TenderExperienceMoneyDao tenderExperienceMoneyDao;

	/**
	 * @MethodName: queryBorrowByCondition
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-4 下午05:01:31
	 * @Return:
	 * @Descb: 根据条件查询借款信息
	 * @Throws:
	 */
	public void queryBorrowByCondition(String borrowStatus, String borrowWay,
			String title, String paymentMode, String purpose, String deadline,
			String reward, String arStart, String arEnd, String order,
			PageBean pageBean) throws Exception {
		String resultFeilds = " id,borrowShow,purpose,imgPath,borrowWay,investNum,borrowTitle,username,vipStatus,credit,creditrating,borrowAmount,annualRate,deadline,excitationType,excitationSum,borrowStatus,schedules,vip,hasPWD,isDayThe,auditStatus ";
		StringBuffer condition = new StringBuffer();
		condition.append(" and sorts!= 0 ");
		Connection conn = MySQL.getConnection();
		if (StringUtils.isNotBlank(borrowStatus)) {
			condition.append(" and borrowStatus in"
					+ StringEscapeUtils.escapeSql(borrowStatus));
		}
		if (StringUtils.isNotBlank(borrowWay)) {
			condition.append(" and borrowWay in"
					+ StringEscapeUtils.escapeSql(borrowWay));
		}
		if (StringUtils.isNotBlank(title)) {
			condition.append(" and borrowTitle  LIKE CONCAT('%','"
					+ StringEscapeUtils.escapeSql(title.trim()) + "','%')");
		}
		if (StringUtils.isNotBlank(paymentMode)
				&& StringUtils.isNumericSpace(paymentMode)) {
			condition.append(" and paymentMode ="
					+ StringEscapeUtils.escapeSql(paymentMode));
		}
		if (StringUtils.isNotBlank(purpose)
				&& StringUtils.isNumericSpace(purpose)) {
			condition.append(" and purpose ="
					+ StringEscapeUtils.escapeSql(purpose));
		}
		if (StringUtils.isNotBlank(deadline)
				&& StringUtils.isNumericSpace(deadline)) {
			condition.append(" and deadline ="
					+ StringEscapeUtils.escapeSql(deadline));
		}
		if (StringUtils.isNotBlank(reward)
				&& StringUtils.isNumericSpace(reward)) {
			if ("1".equals(reward)) {
				condition.append(" and excitationType ="
						+ StringEscapeUtils.escapeSql(reward));
			} else {
				condition.append(" and excitationType > 1 ");
			}
		}
		if (StringUtils.isNotBlank(arStart)
				&& StringUtils.isNumericSpace(arStart)) {
			condition.append(" and amount > "
					+ StringEscapeUtils.escapeSql(arStart));
		}
		if (StringUtils.isNotBlank(arEnd) && StringUtils.isNumericSpace(arEnd)) {
			condition.append(" and amount <"
					+ StringEscapeUtils.escapeSql(arEnd));
		}
		try {
			dataPage(conn, pageBean, " v_t_borrow_list", resultFeilds,
					" order by sorts desc,schedules asc ,id desc ",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 根据条件查询借款信息.
	 * @MethodName: queryBorrowByCondition_New
	 * @Param: FinanceService
	 * @Author: 殷梓淞
	 * @Date: 2013-3-4 下午05:01:31
	 * @Return:
	 * @Descb: 
	 * @Throws:
	 */
	public void queryBorrowByCondition_New(String subjectMatter,String borrowAmount,
			String yearRate, String deadline, PageBean pageBean)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String resultFeilds = " id,borrowShow,purpose,imgPath,borrowWay,investNum,borrowTitle,username,vipStatus,credit,creditrating,borrowAmount,annualRate,deadline,excitationType,excitationSum,borrowStatus,schedules,vip,hasPWD,isDayThe,auditStatus ";
		StringBuffer condition = new StringBuffer();
		condition.append(" and sorts!= 0 ");
		// 判断种类
		if (subjectMatter.trim().equals("1")) {
			condition.append(" and subject_matter=1 ");
		}else if (subjectMatter.trim().equals("2")) {
			condition.append(" and subject_matter=2 ");
		}else if (subjectMatter.trim().equals("3")) {
			condition.append(" and subject_matter=3 ");
		}
		
		// 判断金额
		if (borrowAmount.trim().equals("1")) {
			condition.append(" and borrowAmount>5000 and borrowAmount<=10000 ");
		} else if (borrowAmount.equals("2")) {
			condition
					.append(" and borrowAmount>10000 and borrowAmount<=30000 ");
		} else if (borrowAmount.equals("3")) {
			condition
					.append(" and borrowAmount>30000 and borrowAmount<=50000 ");
		} else if (borrowAmount.equals("4")) {
			condition.append(" and borrowAmount>50000 ");
		}

		// 判断年利率
		// 不限0%-5%5%-10%10%-15%15%-20%20%-24%24%以上
		if (yearRate.trim().equals("1")) {
			condition.append(" and annualRate<=5 ");
		} else if (yearRate.trim().equals("2")) {
			condition.append(" and annualRate>5 and annualRate<=10 ");
		} else if (yearRate.trim().equals("3")) {
			condition.append(" and annualRate>10 and annualRate<=15 ");
		} else if (yearRate.trim().equals("4")) {
			condition.append(" and annualRate>15 and annualRate<=20 ");
		} else if (yearRate.trim().equals("5")) {
			condition.append(" and annualRate>20 and annualRate<=24 ");
		} else if (yearRate.trim().equals("6")) {
			condition.append(" and annualRate>24 ");
		}

		// 判断借款期限
		// 不限1-3月3-6月6-12月12月以上
		if (deadline.trim().equals("1")) {
			condition
					.append(" and deadline>1 and deadline<=3 and isDayThe = 1 ");
		} else if (deadline.trim().equals("2")) {
			condition
					.append(" and deadline>3 and deadline<=6 and isDayThe = 1 ");
		} else if (deadline.trim().equals("3")) {
			condition
					.append(" and deadline>6 and deadline<=12 and isDayThe = 1 ");
		} else if (deadline.trim().equals("4")) {
			condition.append(" and deadline>12 and isDayThe = 1 ");
		}
		condition.append(" and borrowStatus in"
				+ StringEscapeUtils.escapeSql(" (2,3,4,5) "));

		try {
			dataPage(conn, pageBean, " v_t_borrow_list", resultFeilds,
					" order by sorts desc,schedules asc ,id desc ",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}
	
	/**
	 * @MethodName: queryBorrowDetailById
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:18:19
	 * @Return:
	 * @Descb: 根据ID查询借款详细信息
	 * @Throws:
	 */
	public Map<String, String> queryBorrowDetailById(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryBorrowDetailById(conn, id);
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
	 * @MethodName: queryUserInfoById
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午06:04:54
	 * @Return:
	 * @Descb: 根据ID查询借款信息发布者个人信息
	 * @Throws:
	 */
	public Map<String, String> queryUserInfoById(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryUserInfoById(conn, id);
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
	 * @MethodName: queryUserIdentifiedByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:00:04
	 * @Return:
	 * @Descb: 根据ID查询用户认证信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryUserIdentifiedByid(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryUserIdentifiedByid(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	/**
	 * @MethodName: queryUserImageByid
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-4-16 上午11:01:28
	 * @Return:
	 * @Descb: 查询用户认证图片
	 * @Throws:
	 */
	public List<Map<String, Object>> queryUserImageByid(long typeId, long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryUserImageByid(conn, typeId, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	/**
	 * @MethodName: queryPaymentByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:03:01
	 * @Return:
	 * @Descb: 根据ID查询本期还款信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryRePayByid(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryRePayByid(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	/**
	 * @MethodName: queryCollectionByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:04:28
	 * @Return:
	 * @Descb: 根据ID查询本期催收信息
	 * @Throws:
	 */
	public List<Map<String, Object>> queryCollectionByid(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryCollectionByid(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return list;
	}

	/**
	 * @MethodName: queryInvestByid
	 * @Param: FinanceDao
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:06:00
	 * @Return:
	 * @Descb: 根据ID查询投资记录
	 * @Throws:
	 */
	public List<Map<String, Object>> queryInvestByid(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryInvestByid(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * @MethodName: queryBorrowMSGBord
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-6 下午08:30:26
	 * @Return:
	 * @Descb: 根据ID查询留言板信息
	 * @Throws:
	 */
	public void queryBorrowMSGBord(long id, PageBean pageBean) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			dataPage(conn, pageBean, " v_t_borrow_msgbord", " * ",
					" order by id desc ", " and modeId=" + id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @MethodName: addBrowseCount
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-5 下午03:54:02
	 * @Return:
	 * @Descb: 添加浏览量处理
	 * @Throws:
	 */
	public void addBrowseCount(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		long returnId = -1L;
		try {
			returnId = financeDao.addBrowseCount(conn, id);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * @throws DataException
	 * @MethodName: addBorrowMSG
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-13 上午08:16:45
	 * @Return:
	 * @Descb: 添加借款留言
	 * @Throws:
	 */
	public long addBorrowMSG(long id, long userId, String msgContent)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long returnId = -1;
		try {
			returnId = financeDao.addBorrowMSG(conn, id, userId, msgContent);
			if (returnId <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_msgboard",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"添加借款留言", 1);
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

		return returnId;
	}

	/**
	 * @MethodName: getInvestStatus
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午06:46:17
	 * @Return:
	 * @Descb: 获取借款的投标状态,条件是正在招标中
	 * @Throws:
	 */
	public Map<String, String> getInvestStatus(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.getInvestStatus(conn, id);
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
	 * @MethodName: valideInvest
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午04:07:59
	 * @Return:
	 * @Descb: 验证投资人是否符合本次投标
	 * @Throws:
	 */
	public String valideInvest(long id, long userId, double amount)
			throws Exception {
		Connection conn = MySQL.getConnection();
		String result = "";
		try {
			result = financeDao.valideInvest(conn, id, userId, amount);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * @MethodName: valideTradePassWord
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午04:07:43
	 * @Return:
	 * @Descb: 验证交易密码
	 * @Throws:
	 */
	public String valideTradePassWord(long userId, String pwd) throws Exception {
		Connection conn = MySQL.getConnection();
		String result = "";
		try {
			result = financeDao.valideTradePassWord(conn, userId, pwd);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	/**
	 * @MethodName: queryBorrowInvest
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-11 下午07:30:26
	 * @Return:
	 * @Descb: 根据ID获取投资的借款信息
	 * @Throws:
	 */
	public Map<String, String> queryBorrowInvest(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryBorrowInvest(conn, id);
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
	 * @MethodName: queryUserMonney
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午08:48:43
	 * @Return:
	 * @Descb: 查询用户的金额
	 * @Throws:
	 */
	public Map<String, String> queryUserMonney(long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryUserMonney(conn, userId);
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
	 * @throws Exception
	 * @MethodName: addBorrowInvest
	 * @Param: id borrowid 借款标id
	 * @Param: userId 投标人id.
	 * @Author: gang.lv
	 * @Date: 2013-3-12 下午05:48:20
	 * @Return:
	 * @Descb: 添加借款投资记录
	 * @Throws:
	 */
	public Map<String, String> addBorrowInvest(long id, long userId,
			String dealPWD, double investAmount, String basePath,
			String username, int status, int num, String pMerBillNo,String loanNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long ret = -1;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Procedures.p_borrow_join(conn, ds, outParameterValues, id, userId,
					basePath, new BigDecimal(investAmount), new Date(), status,
					num, pMerBillNo, null,loanNo, 0, "");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (ret <= 0) {
				conn.rollback();
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_invest",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"用户投标借款", 1);
				// 得到当前用户最新的投资ID
				Map<String, String> maps = investDao.queryInvestId(conn, id,
						userId);
				// 得到借款当前借款信息
				Map<String, String> borrowMap = borrowDao.queryBorroeById(conn,
						id);
				if (borrowMap != null) {
					long borrowWay = Convert.strToLong(
							borrowMap.get("borrowWay"), -1);
					if (borrowWay == 6) {
						// 提成奖励
						ret = awardService.updateMoneyNew(conn, userId,
								new BigDecimal(investAmount),
								IConstants.MONEY_TYPE_1,
								Convert.strToLong(maps.get("investId"), -1));
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

		return map;
	}

	/**
	 * 添加新手体验标投资记录.
	 * 
	 * @param borrowid
	 *            借款标id.
	 * @param userId
	 *            投资人id.
	 * @param dealPWD
	 *            交易密码.
	 * @param investAmount
	 *            投资金额.
	 * @param basePath
	 *            网站路径.
	 * @param username
	 *            用户名称.
	 * @param num
	 * @param pMerBillNo
	 * @param version
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> addExperienceBorrowInvest(long borrowId,
			long userId, double investAmount, String basePath, String username,
			int num, String pMerBillNo, int version, String borrowTitle,
			int deadline, double monthRate, String email, String telphone)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long ret = -1;
		long result = -1;
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		try {
			result = tenderExperienceMoneyDao.versionVerify(conn, version,
					borrowId);
			if (result <= 0) {
				map.put("ret_desc", "执行异常");
			} else {
				result = tenderExperienceMoneyDao.updateTborrow(conn,
						investAmount, borrowId);
				if (result <= 0) {
					map.put("ret_desc", "执行异常");
				} else {
					result = tenderExperienceMoneyDao
							.insertTinvest(conn, investAmount, userId,
									borrowId, deadline, monthRate);
					if (result <= 0) {
						map.put("ret_desc", "执行异常");
					} else {
						result = tenderExperienceMoneyDao
								.freezeInvestorExperienceMoney(conn,
										investAmount, userId);
						if (result <= 0) {
							map.put("ret_desc", "执行异常");
						} else {
							result = tenderExperienceMoneyDao
									.updateTborrowScale(conn, borrowId);
							if (result <= 0) {
								map.put("ret_desc", "执行异常");
							} else {
								Map<String, String> mailTemplateMap = tenderExperienceMoneyDao
										.getMailTemplate(conn);
								Map<String, String> eMailTemplateMap = tenderExperienceMoneyDao
										.getEmailTemplate(conn);
								Map<String, String> smsTemplateMap = tenderExperienceMoneyDao
										.getSmsTemplate(conn);

								// 邮件模板
								String t_mail_template = mailTemplateMap
										.get("template");
								t_mail_template = t_mail_template.replace(
										"title", borrowTitle);
								t_mail_template = t_mail_template.replace(
										"[voluntarily]", "");
								t_mail_template = t_mail_template.replace(
										"date",
										DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
								t_mail_template = t_mail_template.replace(
										"investAmount",
										Double.toString(investAmount));
								// 邮件模板
								String t_email_template = eMailTemplateMap
										.get("template");
								t_email_template = t_email_template.replace(
										"title", borrowTitle);
								t_email_template = t_email_template.replace(
										"[voluntarily]", "");
								t_email_template = t_email_template.replace(
										"date",
										DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
								t_email_template = t_email_template.replace(
										"investAmount",
										Double.toString(investAmount));
								// 短信模板
								String t_sms_template = smsTemplateMap
										.get("template");
								t_sms_template = t_sms_template.replace(
										"username", username);
								t_sms_template = t_sms_template.replace(
										"title", borrowTitle);
								t_sms_template = t_sms_template.replace(
										"[voluntarily]", "");
								t_sms_template = t_sms_template.replace("date",
										DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
								t_sms_template = t_sms_template.replace(
										"investAmount",
										Double.toString(investAmount));

								result = tenderExperienceMoneyDao
										.insertNoticeTask(conn, userId,
												username, email, telphone,
												t_mail_template,
												t_email_template,
												t_sms_template, borrowId);
								if (result <= 0) {
									map.put("ret_desc", "执行异常");
								} else {
									// // 备注模板
									// //插入资金记录
									// String content = "投标新手体验借款[<a href=\""
									// + basePath
									// + "/financeDetail.do?id="
									// + borrowId + "\"  target=\"_blank\">"
									// + borrowTitle + "</a>],冻结体验金投标金额";
									// result = tenderExperienceMoneyDao
									// .insertTfundrecord(conn, userId,
									// investAmount, content,
									// borrowId, pMerBillNo);
									if (result <= 0) {
										map.put("ret_desc", "执行异常");
									} else {
										result = -1;
										result = tenderExperienceMoneyDao
												.setfullBorrow(conn, borrowId);
										if (result < 0) {
											map.put("ret_desc", "执行异常");
										} else {
											result = 1;
											map.put("ret_desc", "");
										}
									}

								}

							}
						}
					}
				}
			}
			ret = result;
			map.put("ret", ret + "");

			if (ret <= 0) {
				conn.rollback();
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_invest",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"用户投标体验金借款", 1);
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

		return map;
	}

	/**
	 * 投标
	 * 
	 * @param conn
	 * @param investAmount
	 * @param id
	 * @param userId
	 * @param basePath
	 * @param username
	 * @param status
	 * @param num
	 * @return
	 */
	private Map<String, String> validateInvest(Connection conn,
			double investAmount, long id, long userId, String basePath,
			String username, int status, int num, String pMerBillNo,
			String pMerBillNoCheck,String loanNo) throws Exception {
		long ret = -1;
		DataSet ds = new DataSet();
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> userMap = new HashMap<String, String>();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			Procedures.p_borrow_join(conn, ds, outParameterValues, id, userId,
					basePath, new BigDecimal(investAmount), new Date(), status,
					num, pMerBillNo, null,loanNo, 0, "");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (ret <= 0) {
				conn.rollback();
				return map;
			}
			// 添加操作日志
			userMap = userDao.queryUserById(conn, userId);
			operationLogDao
					.addOperationLog(conn, "t_invest",
							Convert.strToStr(userMap.get("username"), ""),
							IConstants.INSERT,
							Convert.strToStr(userMap.get("lastIP"), ""), 0,
							"用户投标借款", 1);
			// 得到当前用户最新的投资ID
			Map<String, String> maps = investDao
					.queryInvestId(conn, id, userId);
			// 得到借款当前借款信息
			Map<String, String> borrowMap = borrowDao.queryBorroeById(conn, id);
			if (borrowMap != null) {
				long borrowWay = Convert.strToLong(borrowMap.get("borrowWay"),
						-1);
				if (borrowWay == 6) {
					// 提成奖励
					ret = awardService.updateMoneyNew(conn, userId,
							new BigDecimal(investAmount),
							IConstants.MONEY_TYPE_1,
							Convert.strToLong(maps.get("investId"), -1));
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		}

		return map;
	}

	/**
	 * @throws DataException
	 * @MethodName: addFocusOn
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-16 上午09:00:49
	 * @Return:
	 * @Descb: 添加关注
	 * @Throws:
	 */
	public Long addFocusOn(long id, long userId, int modeType) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			result = financeDao.addFocusOn(conn, id, userId, modeType);
			if (result <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				if (modeType == 1) {
					operationLogDao.addOperationLog(conn, "t_concern",
							Convert.strToStr(userMap.get("username"), ""),
							IConstants.INSERT,
							Convert.strToStr(userMap.get("lastIP"), ""), 0,
							"添加关注用户", 1);
				} else {
					operationLogDao.addOperationLog(conn, "t_concern",
							Convert.strToStr(userMap.get("username"), ""),
							IConstants.INSERT,
							Convert.strToStr(userMap.get("lastIP"), ""), 0,
							"添加关注借款", 1);
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
	 * @throws DataException
	 * @MethodName: hasFocusOn
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-16 上午11:04:13
	 * @Return:
	 * @Descb: 查询用户是否已经有关注
	 * @Throws:
	 */
	public Map<String, String> hasFocusOn(long id, long userId, int moduleType)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.hasFocusOn(conn, id, userId, moduleType);
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
	 * @MethodName: addUserMail
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午10:13:57
	 * @Return:
	 * @Descb: 添加用户站内信
	 * @Throws:
	 */
	public long addUserMail(long reciver, Long userId, String title,
			String content, int mailType) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			result = financeDao.addUserMail(conn, reciver, userId, title,
					content, mailType);
			if (result <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_concern",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"发送站内信", 1);
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
	 * @throws DataException
	 * @MethodName: addUserReport
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-16 下午10:15:05
	 * @Return:
	 * @Descb: 添加用户举报
	 * @Throws:
	 */
	public long addUserReport(long reporter, Long userId, String title,
			String content) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		Long result = -1L;
		try {
			result = financeDao.addUserReport(conn, reporter, userId, title,
					content);
			if (result <= 0) {
				conn.rollback();
				return -1L;
			} else {
				userMap = userDao.queryUserById(conn, userId);
				operationLogDao.addOperationLog(conn, "t_report",
						Convert.strToStr(userMap.get("username"), ""),
						IConstants.INSERT,
						Convert.strToStr(userMap.get("lastIP"), ""), 0,
						"添加用户举报", 1);
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
	 * @MethodName: queryLastestBorrow
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 上午09:28:00
	 * @Return:
	 * @Descb: 查询最新的借款前10条记录
	 * @Throws:
	 */
	public List<Map<String, Object>> queryLastestBorrow() throws Exception,
			DataException {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryLastestBorrow(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * 富车贷,房贷（最新n条）.
	 * 
	 * @param n
	 *            多少条.
	 * @param subject_matter
	 *            标的属性（2房贷，1车贷）
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryLastestCarOrHouseBorrow(int n,
			int subject_matter) throws Exception, DataException {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryLastestCarOrHouseBorrow(conn, n,
					subject_matter);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}

	/**
	 * 获取活动标（最新n条）.
	 * @param n
	 *            多少条.
	 *@param subject_activity
	 *            是否活动标（0-否,1-是）
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryLastestActivityBorrow(int n) throws Exception, DataException {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = financeDao.queryLastestActivityBorrow(conn,n);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}
	
	
	/**
	 * 获取当天最新的新手标.
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public Map<String, String> queryLastestNoviceBorrow() throws Exception, DataException {
		Connection conn = MySQL.getConnection();
		Map<String, String> list = new HashMap<String, String>();
		try {
			list = financeDao.queryLastestNoviceBorrow(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return list;
	}
	
	/**
	 * @MethodName: investRank
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 上午11:11:37
	 * @Return:
	 * @Descb: 投资排名前20条记录
	 * @Throws:
	 */
	public List<Map<String, Object>> investRank(int type, int count)
			throws Exception {
		Connection conn = MySQL.getConnection();
		// List<Map<String, Object>> list = new ArrayList<Map<String,
		// Object>>();
		DataSet ds = new DataSet();
		List<Object> outParameterValues = new ArrayList<Object>();
		try {
			// list = financeDao.investRank(conn, starTime, endTime);
			Procedures.p_get_topinvestment(conn, ds, outParameterValues, type,
					count);

			conn.commit();

			ds.tables.get(0).rows.genRowsMap();

			return ds.tables.get(0).rows.rowsMap;
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

	}

	/**
	 * @MethodName: queryTotalRisk
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午01:36:01
	 * @Return:
	 * @Descb: 查询风险保障金总额
	 * @Throws:
	 */
	public Map<String, String> queryTotalRisk() throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryTotalRisk(conn);
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
	 * @MethodName: queryCurrentRisk
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-18 下午01:36:14
	 * @Return:
	 * @Descb: 查询当日风险保障金收支金额
	 * @Throws:
	 */
	public Map<String, String> queryCurrentRisk() throws Exception,
			DataException {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryCurrentRisk(conn);
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
	 * @throws Exception
	 * @MethodName: queryBorrowRecord
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-3-27 下午11:03:17
	 * @Return:
	 * @Descb: 查询借款记录统计
	 * @Throws:
	 */
	public Map<String, String> queryBorrowRecord(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.pr_getBorrowRecord(conn, ds, outParameterValues, id,
					new Date());
			ds.tables.get(0).rows.genRowsMap();
			map = DataSetHelper.dataSetToMap(ds);
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
	 * @MethodName: subscribeSubmit
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-5-21 上午10:30:15
	 * @Return:
	 * @Descb: 认购提交
	 * @Throws:
	 */
	public String subscribeSubmit(long id, int copies, Long userId,
			String basePath, String username,
			Map<String, Object> platformCostMap, String pMerBillNo,
			String pMerBillNoCheck) throws Exception {
		Connection conn = MySQL.getConnection();
		String msg = "";
		long returnId = -1;
		try {
			Map<String, String> borrowTenderInMap = financeDao
					.queryBorrowTenderIn(conn, id);
			// 认购中的借款
			if (borrowTenderInMap != null && borrowTenderInMap.size() > 0) {
				long remainCirculationNumber = Convert.strToLong(
						borrowTenderInMap.get("remainCirculationNumber") + "",
						0);
				double smallestFlowUnit = Convert.strToDouble(
						borrowTenderInMap.get("smallestFlowUnit") + "", 0);
				if (copies > remainCirculationNumber) {
					// 校验认购份数是否满足
					msg = "只剩下【" + remainCirculationNumber + "】份可认购,请重新选择!";
				} else {
					// 提交的认购总金额
					double investAmount = smallestFlowUnit * copies;
					// 查询账户上的金额是否满足认购的份数
					Map<String, String> usableSumMap = financeDao
							.queryUserUsableSum(conn, userId, investAmount);
					if (usableSumMap != null && usableSumMap.size() > 0) {
						double usableSum = Convert.strToDouble(
								usableSumMap.get("usableSum") + "", 0);
						long minCirculationNumber = 0;
						double needSum = 0;
						if (usableSum < smallestFlowUnit) {
							msg = "您的可用余额少于￥" + smallestFlowUnit + "元，认购失败!";
						} else {
							// 计算向下取数满足最小的认购份数
							for (long n = remainCirculationNumber; n > 0; n--) {
								needSum = smallestFlowUnit * n;
								if (usableSum >= needSum) {
									minCirculationNumber = n;
									break;
								}
							}
							msg = "您的可用余额可认购【" + minCirculationNumber
									+ "】份,请重新选择!";
						}
					} else {
						Map<String, String> map = validateInvest(conn,
								investAmount, id, userId, basePath, username,
								1, copies, pMerBillNo, pMerBillNoCheck,"");
						returnId = Convert.strToLong(map.get("ret"), -1);
						if (returnId <= 0) {
							conn.rollback();
							msg = Convert.strToStr(map.get("ret_desc"), "");
						} else {
							msg = "1";
						}
					}
				}
			} else {
				// 认购已满,更新状态为回购中
				financeDao.updateRepo(conn, id);
				msg = "无效借款投标";
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

		return msg;
	}

	public FinanceDao getFinanceDao() {
		return financeDao;
	}

	public void setFinanceDao(FinanceDao financeDao) {
		this.financeDao = financeDao;
	}

	/**
	 * @throws Exception
	 * @MethodName: getInvestPWD
	 * @Param: FinanceService
	 * @Author: gang.lv
	 * @Date: 2013-4-5 下午05:35:00
	 * @Return:
	 * @Descb: 获取投标密码是否正确
	 * @Throws:
	 */
	public Map<String, String> getInvestPWD(Long idLong, String investPWD)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			investPWD = Encrypt.MD5(investPWD);
			map = financeDao.getInvestPWD(conn, idLong, investPWD);
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
	 * 根据借款Id查询还款记录
	 * 
	 * @param borrowId
	 * @return
	 * @throws Exception
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryRepayment(long borrowId)
			throws Exception {

		List<Map<String, Object>> map = null;
		Connection conn = MySQL.getConnection();
		try {
			map = repayMentDao.queryHasPIAndStillPi(conn, borrowId);
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
	 * 查找投资人信息 add by houli
	 * 
	 * @return
	 * @throws DataException
	 * @throws Exception
	 */
	public Map<String, String> queryInvestorById(long investorId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = financeDao.queryInvestorById(conn, investorId, -1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();

			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public AwardService getAwardService() {
		return awardService;
	}

	public void setAwardService(AwardService awardService) {
		this.awardService = awardService;
	}

	public SelectedService getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(SelectedService selectedService) {
		this.selectedService = selectedService;
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

	public RepayMentDao getRepayMentDao() {
		return repayMentDao;
	}

	public void setRepayMentDao(RepayMentDao repayMentDao) {
		this.repayMentDao = repayMentDao;
	}

	public InvestDao getInvestDao() {
		return investDao;
	}

	public void setInvestDao(InvestDao investDao) {
		this.investDao = investDao;
	}

	public BorrowDao getBorrowDao() {
		return borrowDao;
	}

	public void setBorrowDao(BorrowDao borrowDao) {
		this.borrowDao = borrowDao;
	}

	public TenderExperienceMoneyDao getTenderExperienceMoneyDao() {
		return tenderExperienceMoneyDao;
	}

	public void setTenderExperienceMoneyDao(
			TenderExperienceMoneyDao tenderExperienceMoneyDao) {
		this.tenderExperienceMoneyDao = tenderExperienceMoneyDao;
	}
}
