package com.sp2p.action.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.fp2p.helper.shove.ShoveOptionHelper;
import com.shove.Convert;
import com.shove.config.GopayConfig;
import com.shove.config.IPayConfig;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.admin.FundManagementService;

public class AccountPaymentAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(AfterCreditManageAction.class);
	/* private AccountPaymentService accountPaymentService; */
	private FundManagementService fundManagementService;

	public FundManagementService getFundManagementService() {
		return fundManagementService;
	}

	public void setFundManagementService(
			FundManagementService fundManagementService) {
		this.fundManagementService = fundManagementService;
	}

	/**
	 * 查询初始化
	 * 
	 * @return
	 */
	public String queryAccountPayInit() {

		return SUCCESS;
	}

	/**
	 * 查询
	 * 
	 * @return
	 */
	public String queryAccountPayInfo() {
		try { 
			fundManagementService.queryAccountPaymentPage(pageBean);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	/**
	 * 修改初始化
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String updateAccountPayInit() {
		String nid = Convert.strToStr(request("nid"), "");
		try {
			paramMap = fundManagementService.queryAccountPaymentById(SqlInfusionHelper.filteSqlInfusion(nid));
			String config = paramMap.get("config");
			if (StringUtils.isNotBlank(config)) {
				Map<String, String> map = (Map<String, String>) JSONObject
						.toBean(JSONObject.fromObject(config), HashMap.class);
				if (map != null) {
					paramMap.putAll(map);
				}
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateAccountPay() throws Exception {
		try {
			Map<String, String> map = new HashMap<String, String>();
			String name = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("name"), ""));
			String litpic = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("litpic"), ""));
			int orders = Convert.strToInt(paramMap.get("orders"), -1);
			String description = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request().getParameter(
					"paramMap.description")
					+ "", ""));
			long id = Convert.strToLong(paramMap.get("id"), -1L);
			String merchantID = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("merchantID"), ""));
			String virCardNoIn = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("virCardNoIn"),
					""));
			String VerficationCode = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap
					.get("VerficationCode"), ""));
			String privatekey = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("privatekey"), ""));
			String customerID = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("customerID"), ""));
			
			String terraceNoOne = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("terraceNoOne"), ""));//平台商户号
			String des_key = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("des_key"), ""));//密钥
			String pubKey = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("pubKey"), ""));//公钥
			String cert_md5 = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("cert_md5"), ""));//MD5证书
			String des_iv = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("des_iv"), ""));//证书号码
			String ipay_gateway = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("ipay_gateway"), ""));//环迅http测试地址
			String ipay_web_service = SqlInfusionHelper.filteSqlInfusion(Convert
					.strToStr(paramMap.get("ipay_web_service"), ""));//环迅webservice测试地址
			
			String nid = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("nid"), ""));
			if ("IPS".equals(nid)) {
				map.put("customerID", customerID);
				map.put("privatekey", privatekey);
				map.put("terraceNoOne", terraceNoOne);
				map.put("des_key", des_key);
				map.put("pubKey", pubKey);
				map.put("cert_md5", cert_md5);
				map.put("des_iv", des_iv);
				map.put("ipay_gateway", ipay_gateway);
				map.put("ipay_web_service", ipay_web_service);
				
			}
			if ("gopay".equals(nid)) {
				map.put("virCardNoIn", virCardNoIn);
				map.put("VerficationCode", VerficationCode);
				map.put("merchantID", merchantID);
			}
			String json = SqlInfusionHelper.filteSqlInfusion(JSONObject.fromObject(map).toString());
			long result = fundManagementService.updateAccountPaymentPage(id,
					name, litpic, json, description, orders);
			if (result > 0) {
				if ("IPS".equals(nid)) {
					IPayConfig.ipay_mer_code = customerID;
					IPayConfig.ipay_certificate = privatekey;
					IPayConfig.des_key = des_key;
					IPayConfig.pubKey = pubKey;
					IPayConfig.cert_md5 = cert_md5;
					IPayConfig.des_iv = des_iv;
					IPayConfig.ipay_gateway = ipay_gateway;
					IPayConfig.ipay_web_service = ipay_web_service;
					IPayConfig.terraceNoOne = terraceNoOne;
				}
				if ("gopay".equals(nid)) {
					GopayConfig.gopay_virCardNoIn = virCardNoIn;
					GopayConfig.gopay_verficationCode = VerficationCode;
					GopayConfig.gopay_merchantID = merchantID;
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_account_payment", admin
				.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
				"修改支付类型", 2);
		return SUCCESS;
	}

	/**
	 * 删除
	 * 
	 * @return
	 * @throws Exception
	 */
	public String delteAccountPay() throws Exception {
		long id = Convert.strToLong(request("id"), -1);
		long status = Convert.strToLong(request("status"), 1);
		long result = -1L;
		try {
			result = fundManagementService.deleteAccountPaymentPage(id, status);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
		}
		if (result > 0) {
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_account_payment", admin
					.getUserName(), IConstants.DELETE, admin.getLastIP(), 0,
					"删除id为" + id + "的支付类型", 2);
			return SUCCESS;
		} else
			return INPUT;
	}

}
