package com.sp2p.action.admin;



import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.fp2p.helper.shove.ShoveOptionHelper;
import com.shove.Convert;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.admin.SEOConfigService;

public class SEOConfigAction extends BasePageAction {
	public static Log log = LogFactory.getLog(SEOConfigAction.class);
	
	private SEOConfigService SEOConfigService;

	public SEOConfigService getSEOConfigService() {
		return SEOConfigService;
	}

	public void setSEOConfigService(SEOConfigService configService) {
		SEOConfigService = configService;
	}
	
	/**
	 * 修改app版本号标准初始化
	 * @return
	 * @throws Exception 
	 */
	public String updateAppOptionsInit() throws Exception{
		Map<String, String> appMap = new HashMap<String, String>();
		String content = ShoveOptionHelper.getOption("app_upcontent");
		String curNum = Convert.strToStr(ShoveOptionHelper.getOption("app_version"), "");
		appMap.put("content", content);
		appMap.put("curNum", curNum);
		request().setAttribute("appMap", appMap);
 		return SUCCESS;
	}

	/**
	 * 更新app版本号标准设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateAppOptions() throws Exception{
		JSONObject obj = new JSONObject();
		String curNum = SqlInfusionHelper.filteSqlInfusion(paramMap.get("curNum"));
		String content = SqlInfusionHelper.filteSqlInfusion(paramMap.get("content"));
		if(StringUtils.isBlank(content)){
			obj.put("msg","更新内容不能为空");
			JSONHelper.printObject(obj);
			return null;
		}
		if(StringUtils.isBlank(curNum)){
			obj.put("msg","版本号不能为空");
			JSONHelper.printObject(obj);
			return null;
		}
		Long result = -1L;
		result = SEOConfigService.updateOptions("app_version", curNum);
		if(result < 0){
			obj.put("msg",IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
		}
		result = SEOConfigService.updateOptions("app_upcontent", content);
		if(result < 0){
			obj.put("msg",IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			return null;
			
		}
		obj.put("msg", "1");
		JSONHelper.printObject(obj);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_seoconfig", admin.getUserName(),IConstants.UPDATE, admin.getLastIP(), 0, "更新移动端版本", 2);
		return null;
	}


	/**
	 * 更新SEO标准设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateSEOConfig() throws Exception{
		JSONObject obj = new JSONObject();
		String title = SqlInfusionHelper.filteSqlInfusion(paramMap.get("title"));
		String keywords = SqlInfusionHelper.filteSqlInfusion(paramMap.get("keywords"));
		String description = SqlInfusionHelper.filteSqlInfusion(paramMap.get("description"));
		String otherTags = SqlInfusionHelper.filteSqlInfusion(paramMap.get("otherTags"));
		Long result = -1L;
		result = SEOConfigService.updateSEOConfig( title, description, keywords,1,otherTags);
		if(result > 0){
			IConstants.SEO_TITLE = title;
			IConstants.SEO_KEYWORDS = keywords;
			IConstants.SEO_DESCRIPTION = description;
			IConstants.SEO_SITEMAP = 1;
			IConstants.SEO_OTHERTAGS = otherTags;
			obj.put("msg", "1");
			JSONHelper.printObject(obj);
		}
		else{
			obj.put("msg",IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_seoconfig", admin.getUserName(),IConstants.UPDATE, admin.getLastIP(), 0, "更新ＳＥＯ标准设置", 2);
		return SUCCESS;
	}
	
	/**
	 * 查看SEO标准设置 
	 * @return
	 * @throws Exception 
	 */
	public String querySEOConfig() throws Exception{
		Map<String, String> seoMap = null;
		seoMap = SEOConfigService.querySEOConfig();
		request().setAttribute("seoMap", seoMap);
 		return SUCCESS;
	}
	
	
	/**
	 * 查询平台注册码设置参数信息
	 * @return
	 * @throws Exception 
	 */
	public String queryRegistCode() throws Exception{
		return SUCCESS;
	}
	
	public String updateRegistCode() throws Exception{
		JSONObject obj = new JSONObject();
		String serial_key_value = SqlInfusionHelper.filteSqlInfusion(paramMap.get("serial_key"));
		String serial_number_value = SqlInfusionHelper.filteSqlInfusion(paramMap.get("serial_number"));
		Long result = -1L;
		result = SEOConfigService.updateRegistCode(serial_key_value,serial_number_value);
		if(result > 0){
			IConstants.SERIAL_NUMBER=serial_number_value;
			IConstants.SERIAL_KEY=serial_key_value;
			obj.put("msg", "1");
			JSONHelper.printObject(obj);
			Properties props = new Properties();
			String url = SqlInfusionHelper.filteSqlInfusion(Thread.currentThread().getContextClassLoader().getResource("config.properties").getPath()) ;	
			log.info(url);
			//InputStream fis =this.getClass().getClassLoader().getResourceAsStream("config.properties");
			InputStream fis = new FileInputStream(url);
			 props.load(fis);
			 fis.close();
			 log.info(props.getProperty("com.shove.security.License.SerialNumber"));
			 OutputStream fos = new FileOutputStream(url);
			 props.setProperty("com.shove.security.License.SerialNumber", serial_number_value);
			 props.store(fos, serial_number_value);
			 log.info(props.getProperty("com.shove.security.License.SerialNumber"));
			 fos.flush();
			 fos.close(); // 关闭流
			 

	    /* com.shove.io.file.PropertyFile pro = new com.shove.io.file.PropertyFile("config.properties");  
		pro.write("com.shove.security.License.SerialNumber", serial_number_value);
		*/
		}
		else{
			obj.put("msg",IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
		}
        return SUCCESS;
	}
	
}
