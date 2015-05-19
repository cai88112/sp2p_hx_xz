package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.fp2p.helper.shove.SMSHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.service.admin.SMSInterfaceService;
import com.sp2p.service.admin.SendSMSService;

/**
 * 网站公告Action
 * 
 * @author zhongchuiqing
 * 
 */
@SuppressWarnings("unchecked")
public class SendSMSAction extends BasePageAction {

	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(SendSMSAction.class);

	private SendSMSService sendSMSService;

	private SMSInterfaceService sMSInterfaceService;

	public SendSMSService getSendSMSService() {
		return sendSMSService;
	}

	public void setSendSMSService(SendSMSService sendSMSService) {
		this.sendSMSService = sendSMSService;
	}

	public SMSInterfaceService getSMSInterfaceService() {
		return sMSInterfaceService;
	}

	public void setSMSInterfaceService(SMSInterfaceService interfaceService) {
		sMSInterfaceService = interfaceService;
	}

	/**
	 * 初始化分页用户列表
	 * 
	 * @return
	 */
	public String queryUserListInit() {
		return SUCCESS;
	}

	/**
	 * 初始化短信发送列表
	 * 
	 * @return
	 */
	public String querySendSMSListInit() {
		return SUCCESS;
	}

	/**
	 * 初始化短信详情类别
	 * 
	 * @return
	 */
	public String getSendSMSByDetailpageInit() {
		return SUCCESS;
	}

	/**
	 * 分页查询用户列表列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserListPage() throws Exception {
		String userName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("userName"));
		String realName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("realName"));
		try {
			sendSMSService.queryUserPage(pageBean, userName, realName, null);
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 分页查询短信发送列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String querySendSMSListPage() throws Exception {
		String beginTime = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("beginTime"));
		String endTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("endTime"));

		try {
			sendSMSService.querySendSMSPage(pageBean, beginTime, endTime);
			List<Map<String, Object>> list = pageBean.getPage();
			if (list != null) {
				for (Map<String, Object> map : list) {
					String[] num = (map.get("splitId")+"").split(",");
					map.put("nums", num.length);
				}
			}
			int pageNum = (int) (pageBean.getPageNum() - 1)
					* pageBean.getPageSize();
			request().setAttribute("pageNum", pageNum);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 获取编辑内容
	 * 
	 * @return
	 */
	public String getSendSMSContent() {
		Object object = session().getAttribute("content");

		if (object != null) {
			request().setAttribute("contents", object);
		}

		return SUCCESS;
	}

	/**
	 * 添加发送短信内容信息
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String addsendSMSContent() throws SQLException, DataException,
			IOException {

		String content = SqlInfusionHelper.filteSqlInfusion(paramMap.get("content"));
		if (StringUtils.isNotBlank(content)) {
			session().setAttribute("content", content);
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");

			return null;
		} else {

			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}

	}

	/**
	 *根据Id获取网发送短信信息详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getSendSMSByDetailpage() throws Exception {
		Long id = Convert.strToLong(request("id"), 0);
		String userName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("userName"));
		String realName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("realName"));
		try {
			Map<String, String> map = sendSMSService.getSendSMSByDetail(id);
			if(map == null){
				map = new HashMap<String, String>();
			}
			sendSMSService.queryUserPage(pageBean, userName, realName, map
					.get("splitId"));

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	/**
	 * 发送短信
	 * 
	 * @return
	 * @throws Exception
	 */
	public String SendSMSs() throws Exception {

		String ids = SqlInfusionHelper.filteSqlInfusion(paramMap.get("id"));// id拼接
		// 用，隔开

		String cellphones = "";
		Object object = session().getAttribute("content");
		String content = "";
		if (object != null) {
			content = object.toString();
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}

		try {
			// 根据id集合获取用户phone
			sendSMSService.queryUserPage(pageBean, null, null, ids);
			List<Map<String, Object>> list = pageBean.getPage();
			int count = 0;
			if (list != null && list.size() == 1) {
				for (Map<String, Object> map : list) {
					if (map.get("cellPhone") == null) {
						ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
						return null;
					}
					String phone = SqlInfusionHelper.filteSqlInfusion(map.get(
							"cellPhone")+"");
					cellphones += phone + ",";
				}
			} else if (list.size() > 1) {
				for (Map<String, Object> map : list) {
					if (map.get("cellPhone") == null) {
						count++;
						if (count == list.size()) {
							ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
							return null;
						}
						continue;
					}
					String phone = SqlInfusionHelper.filteSqlInfusion(map.get(
							"cellPhone")+"");
					cellphones += phone + ",";
				}
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
				return null;
			}

			Map<String, String> map = sMSInterfaceService.getSMSById(1);
			
			if(map == null){
				map = new HashMap<String, String>();
			}
			//			
			// //获取短信接口url
			// String url=SMSUtil.getSMSPort(map.get("url"), map.get("UserID"),
			// map.get("Account"), map.get("Password"), null, content,
			// cellphones, null);
			// //发送短信
			// String retCode = SMSUtil.sendSMS(url);

			// //获取短信接口url
			// String url=SMSUtil.getSMSPort(map.get("url"), map.get("UserID"),
			// map.get("Account"), map.get("Password"), null, content,
			// cellphones, null);
			// //发送短信
			// String retCode = SMSUtil.sendSMS(url);
			StringBuffer buffer = new StringBuffer();
			buffer.append(cellphones);
			buffer.delete(buffer.lastIndexOf(","), buffer.lastIndexOf(",") + 1);
//			String retCode = SMSHelper.sendSMS(SqlInfusionHelper.filteSqlInfusion(map
//					.get("Account")), SqlInfusionHelper.filteSqlInfusion(map
//					.get("Password")), SqlInfusionHelper.filteSqlInfusion(content),
//					SqlInfusionHelper.filteSqlInfusion(buffer+""), null);
			
			String retCode = SMSHelper.sendSMSUseXuanWu(SqlInfusionHelper.filteSqlInfusion(map
					.get("Account")), SqlInfusionHelper.filteSqlInfusion(map
					.get("Password")), SqlInfusionHelper.filteSqlInfusion(content),
					SqlInfusionHelper.filteSqlInfusion(buffer+""), null);
			
			
			if (retCode.equals("Sucess")) {
				// 添加短信记录
				@SuppressWarnings("unused")
				Long result = sendSMSService.SendSMSs(SqlInfusionHelper
						.filteSqlInfusion(content), SqlInfusionHelper
						.filteSqlInfusion(ids), SqlInfusionHelper
						.filteSqlInfusion(cellphones));

				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

	}

}
