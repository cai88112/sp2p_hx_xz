package com.fp2p.helper.shove;

import java.net.URL;

import net._139130.www.WebServiceLocator;
import net._139130.www.WebServiceSoapStub;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shovesoft.SMS;

/**
 * 短信接口，对短信接口地址进行拼接，提供公用.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class SMSHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(SMSHelper.class
			.getName());

	/**
	 * SMS工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private SMSHelper() {
	}

	/**
	 * 发送短信.
	 *
	 * @param userName
	 *            短信平台账号
	 * @param password
	 *            短信平台密码
	 * @param content
	 *            短信内容
	 * @param phone
	 *            手机号码
	 * @param randomCode
	 *            短信验证码
	 * @return 发送短信的结果Sucess为成功Fail为失败
	 */
	 public static String sendSMS(final String userName, final String
	 password,
	 final String content, final String phone, final String randomCode) {
	 logger.debug("进入sendSMS方法");
	 String result = "Fail";
	 try {
	 if (userName == null) {
	 throw new NullPointerException("userName为空指针");
	 }
	 if (password == null) {
	 throw new NullPointerException("password为空指针");
	 }
	 if (content == null) {
	 throw new NullPointerException("content为空指针");
	 }
	 if (phone == null) {
	 throw new NullPointerException("phone为空指针");
	 }
	
	 String tempContent = content;
	 if (randomCode != null) {
	 tempContent = tempContent + randomCode;
	 }
	 SMS.sendMSM(userName, password, tempContent, phone);
	 result = "Sucess";
	 } catch (Exception e) {
	 e.printStackTrace();
	 logger.debug("退出sendSMS方法");
	 return result;
	 }
	 logger.debug("退出sendSMS方法");
	 return result;
	 }

	/**
	 * 发送短信（玄武科技公司的接口）.
	 * 
	 * @Title: sendSMSUseXuanWu
	 * @param @param userName
	 * @param @param password
	 * @param @param content
	 * @param @param phone
	 * @param @param randomCode
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String sendSMSUseXuanWu(final String userName, final String password,
			final String content, final String phone, final String randomCode) {
		logger.debug("进入sendSMS方法");
		String result = "Fail";
		try {
			if (userName == null) {
				throw new NullPointerException("userName为空指针");
			}
			if (password == null) {
				throw new NullPointerException("password为空指针");
			}
			if (content == null) {
				throw new NullPointerException("content为空指针");
			}
			if (phone == null) {
				throw new NullPointerException("phone为空指针");
			}
			String tempContent = content;
			if (randomCode != null) {
				tempContent = tempContent + randomCode;
			}
			WebServiceLocator locator = new WebServiceLocator();
			WebServiceSoapStub stub = (WebServiceSoapStub) locator
					.getWebServiceSoap(new URL(
							"http://211.147.239.62/Service/WebService.asmx?wsdl"));
			stub.postSingle(userName, password, phone, tempContent, null);
			result = "Sucess";
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("退出sendSMS方法");
			return result;
		}
		logger.debug("退出sendSMS方法");
		return result;
	}

}
