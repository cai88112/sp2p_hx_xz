package com.sp2p.action.app;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.shove.ShoveOptionHelper;
import com.shove.Convert;
import com.shove.config.AlipayConfig;
import com.shove.security.Encrypt;
import com.sp2p.constants.IConstants;
import com.sp2p.service.HomeInfoSettingService;
import com.sp2p.service.MyHomeService;
import com.sp2p.service.admin.FundManagementService;

public class HomeAppAction extends BaseAppAction {
	private static final long serialVersionUID = -8705141732645392945L;
	public static Log log = LogFactory.getLog(HomeAppAction.class);

	private MyHomeService myHomeService;
	private HomeInfoSettingService homeInfoSettingService;
	private FundManagementService fundManagementService;

	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	public String getAppVersion() throws IOException {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			String verNum = Convert.strToStr(infoMap.get("verNum"), "");
			String curNum = Convert.strToStr(
					ShoveOptionHelper.getOption("app_version"), "");
			if (verNum.equals(curNum)) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "当前版本已经是最新版本");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String content = ShoveOptionHelper.getOption("app_upcontent");
			String url = IConstants.APP_URL;
			jsonMap.put("url", url);
			jsonMap.put("updMessage", content);
			jsonMap.put("curNum", curNum + "");
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "2");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
	}

	public String queryHome() throws IOException {
		Map<String, String> jsonMap = new HashMap<String, String>();
		try {
			Map<String, String> authMap = this.getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "请登录");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			Map<String, String> homeMap = myHomeService.queryHomeInfo(userId);
			Map<String, String> accmountStatisMap = myHomeService
					.queryAccountStatisInfo(userId);
			jsonMap.putAll(homeMap);
			jsonMap.putAll(accmountStatisMap);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "2");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
	}

	public String queryBank() throws IOException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> authMap = this.getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == -1) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "请登录");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			List<Map<String, Object>> lists = homeInfoSettingService
					.queryBankInfoList(userId);
			jsonMap.put("lists", lists);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "2");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
	}

	public String addBank() throws IOException {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> authMap = this.getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			Map<String, String> appInfoMap = this.getAppInfoMap();

			String code = appInfoMap.get("code");
			if (StringUtils.isBlank(code)) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "验证码不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String randomCode = appInfoMap.get("randomCode") + "";
			randomCode = Encrypt.decryptSES(randomCode, AlipayConfig.ses_key);
			String phone = session().getAttribute("phone") + "";
			if (!randomCode.equals(code)) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", "验证码不正确");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String recivePhone = appInfoMap.get("recivePhone") + "";
			recivePhone = Encrypt.decryptSES(recivePhone, AlipayConfig.ses_key);
			String cardUserName = appInfoMap.get("cardUserName");
			if (StringUtils.isBlank(cardUserName)) {
				jsonMap.put("error", "3");
				jsonMap.put("msg", "银行卡持有人不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String bankName = appInfoMap.get("bankName");
			if (StringUtils.isBlank(bankName)) {
				jsonMap.put("error", "4");
				jsonMap.put("msg", "银行名称不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String subBankName = appInfoMap.get("subBankName");
			if (StringUtils.isBlank(subBankName)) {
				jsonMap.put("error", "5");
				jsonMap.put("msg", "银行开户行不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String bankCard = appInfoMap.get("bankCard");
			if (StringUtils.isBlank(bankCard)) {
				jsonMap.put("error", "6");
				jsonMap.put("msg", "银行卡号不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			long bankId = Convert.strToLong(appInfoMap.get("bankId"), -1);

			Map<String, String> map = homeInfoSettingService
					.queryCardStatus(userId);
			int bindingCardNum = Convert.strToInt(map.get("count(*)"), 0);

			if (bindingCardNum >= 2) {// 已经绑定两张银行卡，不能再绑定了
				jsonMap.put("error", "7");
				jsonMap.put("msg", "最多绑定两张银行卡");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			// 银行卡验证

			Pattern p = Pattern.compile("[0-9]{19}");
			Matcher m = p.matcher(bankCard);
			if (!m.matches()) {
				ServletHelper
						.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			}

			// 新添加的提现银行卡信息状态为2，表示申请中
			long result = -1;
			if (bankId == -1) {
				result = homeInfoSettingService.addBankCardInfo(userId,
						cardUserName, bankName, subBankName, bankCard,
						IConstants.BANK_CHECK, "", "", "","");
			} else {
				result = fundManagementService.updateChangeBank(bankId,
						bankName, subBankName, bankCard, IConstants.BANK_CHECK,
						new Date(), true, "", "", "","");
			}
			if (result > 0) {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "成功");
				JSONHelper.printObject(jsonMap);
			} else {
				jsonMap.put("error", "8");
				jsonMap.put("msg", "失败");
				JSONHelper.printObject(jsonMap);
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "9");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
	}

	public String updateBank() throws IOException {

		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> authMap = this.getAppAuthMap();
			long userId = Convert.strToLong(authMap.get("uid"), -1);
			Map<String, String> appInfoMap = this.getAppInfoMap();

			String code = appInfoMap.get("code");
			if (StringUtils.isBlank(code)) {
				jsonMap.put("error", "1");
				jsonMap.put("msg", "验证码不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String randomCode = appInfoMap.get("randomCode") + "";
			randomCode = Encrypt.decryptSES(randomCode, AlipayConfig.ses_key);
			String phone = session().getAttribute("phone") + "";
			if (!randomCode.equals(code)) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", "验证码不正确");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String recivePhone = appInfoMap.get("recivePhone") + "";
			recivePhone = Encrypt.decryptSES(recivePhone, AlipayConfig.ses_key);
			String cardUserName = appInfoMap.get("cardUserName");
			String bankName = appInfoMap.get("mBankName");
			if (StringUtils.isBlank(bankName)) {
				jsonMap.put("error", "4");
				jsonMap.put("msg", "银行名称不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String subBankName = appInfoMap.get("mSubBankName");
			if (StringUtils.isBlank(subBankName)) {
				jsonMap.put("error", "5");
				jsonMap.put("msg", "银行开户行不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			String bankCard = appInfoMap.get("mBankCard");
			if (StringUtils.isBlank(bankCard)) {
				jsonMap.put("error", "6");
				jsonMap.put("msg", "银行卡号不能为空");
				JSONHelper.printObject(jsonMap);
				return null;
			}

			long bankId = Convert.strToLong(appInfoMap.get("bankId"), -1);

			Map<String, String> map = homeInfoSettingService
					.queryCardStatus(userId);
			int bindingCardNum = Convert.strToInt(map.get("count(*)"), 0);
			// 银行卡验证

			Pattern p = Pattern.compile("[0-9]{19}");
			Matcher m = p.matcher(bankCard);
			if (!m.matches()) {
				ServletHelper
						.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			}

			// 新添加的提现银行卡信息状态为2，表示申请中
			long result = -1;
			result = fundManagementService.updateChangeBank(bankId, bankName,
					subBankName, bankCard, IConstants.BANK_CHECK, new Date(),
					true, "", "", "","");

			if (result > 0) {
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "更变申请已提交");
				JSONHelper.printObject(jsonMap);
			} else {
				jsonMap.put("error", "8");
				jsonMap.put("msg", "失败");
				JSONHelper.printObject(jsonMap);
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			jsonMap.put("error", "9");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
		}
		return null;
	}

	public void setMyHomeService(MyHomeService myHomeService) {
		this.myHomeService = myHomeService;
	}

	public MyHomeService getMyHomeService() {
		return myHomeService;
	}

	public void setHomeInfoSettingService(
			HomeInfoSettingService homeInfoSettingService) {
		this.homeInfoSettingService = homeInfoSettingService;
	}

	public HomeInfoSettingService getHomeInfoSettingService() {
		return homeInfoSettingService;
	}

}
