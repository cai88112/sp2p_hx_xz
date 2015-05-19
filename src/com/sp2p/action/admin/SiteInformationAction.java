package com.sp2p.action.admin;


import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.SiteInformationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SiteInformationAction  extends  BasePageAction{
	
		private static final long serialVersionUID = 1L;
	
		private static Log  log = LogFactory.getLog(SiteInformationAction.class);
		private SiteInformationService  siteInformationService;
		
			/**
			 * 查询初始化
			 * @return
			 */
			public String querySiteInforInit() {
				
				return SUCCESS;
			}
			
		/**
		 * 修改初始化
		 * @return
		 * @throws Exception 
		 */
		public String updateSiteInforInit() {
			 try {
				paramMap= siteInformationService.querySiteAll();
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
			return SUCCESS;
		}
		
		/**
		 * 修改
		 * @return
		 * @throws Exception 
		 */
		public String updateSiteInfor() {
			String siteName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.siteName"),""));
			String companyName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.companyName"),""));
			String postcode = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.postcode"),""));
			String address = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.address"),""));
			String principal = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.principal"),""));
			String contact = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.contact"),""));
			String telephone = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.telephone"),""));
			String cellphone = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.cellphone"),""));
			String fax = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.fax"),""));
			String emial = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.emial"),""));
			String qq = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.qq"),""));
			String servicePhone = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.servicePhone"),""));
			String certificate = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.certificate"),""));
			String regionName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.regionName"),""));
			int id = Convert.strToInt(request("paramMap_id"), -1);
			long result = -1L;
			
			
			
			try {
				result = siteInformationService.updateSiteById(id, siteName, companyName, postcode, address, principal, contact, telephone, cellphone, fax, emial, qq, servicePhone, certificate, regionName);
				Map<String,String> map= siteInformationService.querySiteAll();
				application().setAttribute("sitemap", map);
				Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
				operationLogService.addOperationLog("t_site_information ", admin.getUserName(),IConstants.UPDATE, admin.getLastIP(), 0, "修改站点信息", 2);
				
			} catch (SQLException e) {
				log.error(e);
				e.printStackTrace();
			}catch (Exception e) {
				log.error(e);
				e.printStackTrace();
			}
			if(result > 0 )
			return SUCCESS;
			else
			return INPUT;
		}

		public String updateSiteWorkData() throws IOException{
			JSONObject json = new JSONObject();
			String siteName = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.siteName"),"")); //
			if (StringUtils.isBlank(siteName)) {
				json.put("msg", "请填写站点名称");
				JSONHelper.printObject(json);
				return null;
			}

			String companyName =SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.companyName"),""));
			if (StringUtils.isBlank(companyName)) {
				json.put("msg", "请填写公司名称");
				JSONHelper.printObject(json);
				return null;
			}
			String postcode =SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.postcode"),""));
			if (StringUtils.isBlank(postcode)) {
				json.put("msg", "请填写邮编");
				JSONHelper.printObject(json);
				return null;
			}
			String address =SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.address"),""));
			if (StringUtils.isBlank(address)) {
				json.put("msg", "请填写地址");
				JSONHelper.printObject(json);
				return null;
			}
			String principal =SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.principal"),""));
			if (StringUtils.isBlank(principal)) {
				json.put("msg", "请填写负责人");
				JSONHelper.printObject(json);
				return null;
			}
			String contact =SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.contact"),""));
			if (StringUtils.isBlank(contact)) {
				json.put("msg", "请填写联系人");
				JSONHelper.printObject(json);
				return null;
			}
			String cellphone =SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.cellphone"),""));
			if (StringUtils.isBlank(cellphone)) {
				json.put("msg", "请填写手机号码");
				JSONHelper.printObject(json);
				return null;
			}
			String telephone = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.telephone"),"")); //电话号码
			if (StringUtils.isBlank(telephone)) {
				json.put("msg", "请正确填写公司电话号码");
				JSONHelper.printObject(json);
				return null;
			}
			Pattern pattern = Pattern.compile("^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$");     
	        Matcher m = pattern.matcher(telephone);     
	        if (!m.matches()) {     
		   		json.put("msg", "请正确填写公司电话号码");
				JSONHelper.printObject(json);
				return null;
	        }
	        String fax = SqlInfusionHelper.filteSqlInfusion( Convert.strToStr(request("paramMap.fax"),"")); //传真
	        if (StringUtils.isBlank(fax)) {
				json.put("msg", "请正确填写传真号码");
				JSONHelper.printObject(json);
				return null;
			}
	        m = pattern.matcher(fax);     
	        if (!m.matches()) {     
		   		json.put("msg", "请正确填写传真号码");
				JSONHelper.printObject(json);
				return null;
	        }
	        
	        String emial = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.emial"),"")); //邮箱
	        if (StringUtils.isBlank(emial)) {
				json.put("msg", "请填写邮箱号码");
				JSONHelper.printObject(json);
				return null;
			}
	        String qq = SqlInfusionHelper.filteSqlInfusion( Convert.strToStr(request("paramMap.qq"),"")); //QQ
	        if (StringUtils.isBlank(qq)) {
				json.put("msg", "请填写QQ号码");
				JSONHelper.printObject(json);
				return null;
			}
	        String  servicePhone=  SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.servicePhone"),"")); //服务电话号码
			Pattern pattern2 = Pattern.compile("^\\(?\\d{3,4}[-\\)]?\\d{3,4}?([-]?(\\d){3,4})$");     
	        m = pattern2.matcher(servicePhone);     
	        if (!m.matches()) {     
	        	json.put("msg", "请正确填写服务电话号码");
				JSONHelper.printObject(json);
				return null;
	        }
		  
	        String certificate =  SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.certificate"),"")); //ICP证书号
	        if (StringUtils.isBlank(certificate)) {
				json.put("msg", "请正确填写ICP证书号");
				JSONHelper.printObject(json);
				return null;
			}
	        String regionName =  SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(request("paramMap.regionName"),"")); 
	        if (StringUtils.isBlank(regionName)) {
				json.put("msg", "请填写站点域名");
				JSONHelper.printObject(json);
				return null;
			}else{
				json.put("msg", "资料正确");
				JSONHelper.printObject(json);
				return null;
			}
	        
		}
		public void setSiteInformationService(
				SiteInformationService siteInformationService) {
			this.siteInformationService = siteInformationService;
		}
		
		
		
		
}	
