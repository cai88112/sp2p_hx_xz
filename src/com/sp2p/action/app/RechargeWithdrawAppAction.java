package com.sp2p.action.app;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.shove.Convert;
import com.shove.config.AlipayConfig;
import com.shove.data.DataException;
import com.shove.security.Encrypt;
import com.sp2p.action.front.FrontMyFinanceAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.HomeInfoSettingService;
import com.sp2p.service.RechargeService;
import com.sp2p.service.UserService;


public class RechargeWithdrawAppAction extends BaseAppAction {
	private static final long serialVersionUID = -676064526203960258L;
	public static Log log = LogFactory.getLog(RechargeWithdrawAppAction.class);

	private RechargeService rechargeService;
	private HomeInfoSettingService homeInfoSettingService;
	private UserService userService;

	public String queryWithdraw() throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			Map<String, String> authMap = getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "用户不存在");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			Map<String, String> pmap = userService.queryPersonById(userId);
			if (pmap == null) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", "用户的个人信息未填写,无法申请提现");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			int authStep = Convert.strToInt(pmap.get("authStep"), -1);// 需要填写个人资料之后才能申请提现
			// if (authStep < 2) {
			// jsonMap.put("error", "2");
			// jsonMap.put("msg", "用户的个人信息未填写,无法申请提现");
			// JSONUtils.printObject(jsonMap);
			// return null;
			// }

			// 需要加载信息 真实姓名 手机号码 帐户余额 可用余额 冻结总额 提现银行
			String realName = pmap.get("realName");

			int status = -1;
			String[] vals = moneyVal(userId);

			String bindingPhone = this.getBidingPhone(userId);
			if (StringUtils.isBlank(bindingPhone)) {
				jsonMap.put("error", "3");
				jsonMap.put("msg", "手机号未设定");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			// 绑定的银行卡信息单独查询
			List<Map<String, Object>> bankList = homeInfoSettingService
					.querySuccessBankInfoList(userId);
			if (bankList == null || bankList.size() == 0) {
				jsonMap.put("error", "4");
				jsonMap.put("msg", "未添加银行卡信息");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			jsonMap.put("handleSum", vals[0]);
			jsonMap.put("usableSum", vals[1]);
			jsonMap.put("freezeSum", vals[2]);
			jsonMap.put("max_withdraw", IConstants.WITHDRAW_MAX);// 最大充值金额，超过之后要收取手续费
			jsonMap.put("bankList", bankList);
			jsonMap.put("realName", realName);
			jsonMap.put("bindingPhone", bindingPhone);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("msg", "5");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
	}

	private String getBidingPhone(long userId) throws Exception {
		List<Map<String, Object>> lists = rechargeService.withdrawLoad(userId);

		String bindingPhone = null;
		int status = -1;
		String[] vals = moneyVal(userId);

		if (lists != null && lists.size() > 0) {
			if (lists.get(0).get("bindingPhone") != null) {
				bindingPhone = lists.get(0).get("bindingPhone").toString();
			}
			if (lists.get(0).get("status") != null) {
				status = Convert.strToInt(
						lists.get(0).get("status").toString(), -1);
			}

			// 如果设置的绑定号码为空，或者绑定的手机号码还未审核通过 则都使用用户注册时的手机号码
			if (bindingPhone == null || status != IConstants.PHONE_BINDING_ON) {
				bindingPhone = lists.get(0).get("cellPhone").toString();
			}
		}
		return bindingPhone;
	}

	/**
	 * 加载提现记录信息
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 * @throws IOException
	 */
	public String queryWithdrawList() throws SQLException, DataException,
			IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			Map<String, String> authMap = getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "用户不存在");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			Map<String, String> appInfoMap = getAppInfoMap();
			String startTime = "";
			int months = Convert.strToInt(appInfoMap.get("months"), -1);
			if (months != -1) {
				Date endDate = DateUtils.addMonths(new Date(), months);  
				
				startTime = DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss");
			}

			double money = Convert.strToDouble(appInfoMap.get("money"), -1);

			pageBean.setPageSize(15);
			rechargeService.queryWithdrawListApp(pageBean, userId);

			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			jsonMap.put("pageBean", pageBean);
			JSONHelper.printObject(jsonMap);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("msg", "2");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);

		}
		return null;
	}

	public String addWithdraw() throws IOException {

		Map<String, Object> jsonMap = new HashMap<String, Object>();

		try {
			Map<String, String> authMap = getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "用户不存在");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			Map<String, String> appInfoMap = getAppInfoMap();
			String dealpwd = appInfoMap.get("dealpwd").toLowerCase();
			if (StringUtils.isBlank(dealpwd)) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", "提现密码不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			String code = appInfoMap.get("code");

			if (StringUtils.isBlank(code)) {
				jsonMap.put("error", "3");
				jsonMap.put("msg", "验证码不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			String randomCode = appInfoMap.get("randomCode");
			if (!code.equals(Encrypt.decryptSES(randomCode,
					AlipayConfig.ses_key))) {
				jsonMap.put("error", "4");
				jsonMap.put("msg", "验证码不正确");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			double money = Convert.strToDouble(appInfoMap.get("money"), 0.0);
			if (money < 0) {
				jsonMap.put("error", "5");
				jsonMap.put("msg", "提现金额不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			long bankId = Convert.strToLong(appInfoMap.get("bankId"), -1);

			String cellPhone = appInfoMap.get("cellPhone");

			if (bankId < 0) {
				jsonMap.put("error", "6");
				jsonMap.put("msg", "银行卡不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			String recivePhone = appInfoMap.get("recivePhone");

			String bindingPhone = this.getBidingPhone(userId);
			if (bindingPhone != null
					&& !bindingPhone.equals(Encrypt.decryptSES(recivePhone,
							AlipayConfig.ses_key))) {
				jsonMap.put("error", "7");
				jsonMap.put("msg", "绑定手机号跟接收验证码手机号不一致");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			
			
			Map<String, String> map = userService.queryUserById(userId);
			String userDealPwd = map.get("dealpwd").toLowerCase();
			if (userDealPwd == null) {// 如果用户没有设置交易密码，则默认为登录密码
				userDealPwd = map.get("password");
			}
			if (!dealpwd.equals(userDealPwd)) {// 交易密码错误
				jsonMap.put("error", "8");
				jsonMap.put("msg", "交易密码错误");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			// ------modify 2013-05-02 判断该用户所属的组是否能申请提现
			Long toWithdraw = homeInfoSettingService.queryUserCashStatus(
					userId, IConstants.CASH_STATUS_OFF);
			if (toWithdraw <= 0) {// 该用户所属组的提现权利被限制
				jsonMap.put("error", "9");
				jsonMap.put("msg", "很抱歉，您暂时不能提现");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			// ----------

			String cardNo = "";
			Map<String, String> mp = homeInfoSettingService
					.queryBankInfoById(bankId);
			if (mp != null) {
				cardNo = mp.get("cardNo");
			}
			long result = -1L;

			String username = map.get("username");
			// ip地址
			String ipAddress = ServletHelper.getIpAddress(ServletActionContext.getRequest());

			Map<String, String> map1 = rechargeService.withdrawApply(userId, money,
					userDealPwd, bankId, "1", ipAddress);
			result = Convert.strToLong(map1.get("ret"), -1l);

			if (result < 0) {// 提现失败
				jsonMap.put("error", "10");
				jsonMap.put("msg", "添加提现失败");
				JSONHelper.printObject(jsonMap);
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "添加提现成功");
				JSONHelper.printObject(jsonMap);
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "3");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);

		}
		return null;
	}

	public String deleteWithdraw() throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> authMap = getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "用户不存在");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			Map<String, String> appInfoMap = getAppInfoMap();
			long withDrawId = Convert.strToLong(appInfoMap.get("withDrawId"),
					-1);
			if (withDrawId < 0) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", "提现记录不存在");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			// 修改提现记录状态
			Map<String, String> resultMap = rechargeService.deleteWithdraw(
					userId, withDrawId);
			long result = -1;
			result = Convert.strToLong(resultMap.get("ret"), -1);
			if (result < 0) {
				jsonMap.put("error", "3");
				jsonMap.put("msg", "删除失败");
				JSONHelper.printObject(jsonMap);
				return null;
			} else {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "删除成功");
				JSONHelper.printObject(jsonMap);
				return null;
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "3");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}

		return null;
	}

	public String queryFundsRecodeList() throws SQLException, DataException,
	IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> authMap = getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1"); 
				jsonMap.put("msg", "用户不存在");
				JSONHelper.printObject(jsonMap);
				return null;
			}
		
			Map<String, String> appInfoMap = getAppInfoMap();
			String startTime = "";
			int days = Convert.strToInt(appInfoMap.get("days"), 0);
			if (days != 0) {
				Date endDate = DateUtils.addDays(new Date(), days);
				startTime = DateFormatUtils.format(endDate, "yyyy-MM-dd HH:mm:ss");
			}
			pageBean.setPageSize(IConstants.PAGE_SIZE_10);
			pageBean.setPageNum(appInfoMap.get("curPage"));
			rechargeService.queryFundrecordsList(pageBean, userId, startTime,
					"");
		
			String[] vals = fundVal(userId);
			jsonMap.put("sum", vals[0]);
			jsonMap.put("handleSum", vals[0]);
			jsonMap.put("usableSum", vals[1]);
			jsonMap.put("freezeSum", vals[2]);
			jsonMap.put("totalSum", vals[3]);
			jsonMap.put("pageBean", pageBean);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "5");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
}


	private String[] fundVal(long userId) throws Exception {
		String[] val = new String[4];
		for (int i = 0; i < val.length; i++) {
			val[i] = "0";
		}
		try {
			Map<String, String> map = rechargeService.queryFund(userId);
			if (map != null) {
				double usableSum = Convert.strToDouble(map.get("usableSum"), 0);
				double freezeSum = Convert.strToDouble(map.get("freezeSum"), 0);
				double dueinSum = Convert.strToDouble(map.get("forPI"), 0);
				double handleSum = usableSum + freezeSum + dueinSum;
				double totalSum = usableSum + freezeSum;
				val[0] = String.format("%.2f", handleSum);
				val[1] = String.format("%.2f", usableSum);
				val[2] = String.format("%.2f", freezeSum);
				val[3] = String.format("%.2f", totalSum);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return val;
	}

	private String[] moneyVal(long userId) throws Exception {
		String[] val = new String[3];
		for (int i = 0; i < val.length; i++) {
			val[i] = "0";
		}
		try {
			Map<String, String> map = userService.queryUserById(userId);
			if (map != null) {
				double usableSum = Convert.strToDouble(map.get("usableSum"), 0);
				double freezeSum = Convert.strToDouble(map.get("freezeSum"), 0);
				double handleSum = usableSum + freezeSum;
				val[0] = String.format("%.2f", handleSum);// df.format(handleSum);
				val[1] = String.format("%.2f", usableSum);// df.format(usableSum);
				val[2] = String.format("%.2f", freezeSum);// df.format(freezeSum);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return val;
	}

	public void setRechargeService(RechargeService rechargeService) {
		this.rechargeService = rechargeService;
	}

	public void setHomeInfoSettingService(
			HomeInfoSettingService homeInfoSettingService) {
		this.homeInfoSettingService = homeInfoSettingService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
