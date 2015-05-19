package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.admin.EmalAndMessageService;

public class EmalAndMessageAction extends BaseFrontAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(EmalAndMessageAction.class);
	@SuppressWarnings("unused")
	private EmalAndMessageService emalAndMessageService;

	public void setEmalAndMessageService(
			EmalAndMessageService emalAndMessageService) {
		this.emalAndMessageService = emalAndMessageService;
	}

	/**
	 * 邮件设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String emailAndMessageinitMethod() throws Exception {
		Map<String, String> mailMap = null;
		mailMap = emalAndMessageService.queryMailsetMaxId();
		mailMap = emalAndMessageService.queryMailSet(Convert.strToInt(mailMap
				.get("id"), 1));
		request().setAttribute("mailMap", mailMap);
		return SUCCESS;
	}

	/**
	 * 提交邮件设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMailSetM() throws Exception {
		JSONObject obj = new JSONObject();
		String maildress = SqlInfusionHelper.filteSqlInfusion(paramMap.get("maildress"));
		String password = SqlInfusionHelper.filteSqlInfusion(paramMap.get("password"));
		String sendEmail = SqlInfusionHelper.filteSqlInfusion(paramMap.get("sendEmail"));
		String username = SqlInfusionHelper.filteSqlInfusion(paramMap.get("username"));
		String port = SqlInfusionHelper.filteSqlInfusion(paramMap.get("port"));
		String host = SqlInfusionHelper.filteSqlInfusion(paramMap.get("host"));
		Long result = -1L;
		result = emalAndMessageService.addUserWorkData(maildress, password,
				sendEmail, username, port, host);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result > 0) {
			IConstants.MAIL_HOST = host;
			IConstants.MAIL_USERNAME = sendEmail;
			IConstants.MAIL_PASSWORD = password;
			IConstants.MAIL_FROM = maildress;

			obj.put("msg", "1");
			JSONHelper.printObject(obj);
			operationLogService.addOperationLog("t_mailset", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"更新邮件设置成功", 2);
			return null;
		} else {
			obj.put("msg", IConstants.ACTION_FAILURE);
			JSONHelper.printObject(obj);
			operationLogService.addOperationLog("t_mailset", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"更新邮件设置失败", 2);
			return null;
		}
	}

	/**
	 * 设置短信参数设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryMessageSetMethod() throws Exception {
		Map<String, String> Messagemap = null;
		Map<String, String> Messagemap2 = null;
		Messagemap = emalAndMessageService.queryMessageSet(1);
		Messagemap2 = emalAndMessageService.queryMessageSet(2);
		request().setAttribute("Messagemap", Messagemap);
		request().setAttribute("Messagemap2", Messagemap2);
		return SUCCESS;
	}

	/**
	 * 增加或修改短信参数设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMessageSetMethod() throws Exception {
		String enablestr = SqlInfusionHelper.filteSqlInfusion(paramMap.get("kaiqi1"));
		if (StringUtils.isBlank(enablestr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "8");
			return null;
		}
		Integer enable = Convert.strToInt(enablestr, 1);// 默认开启

		Long id = Convert.strToLong(paramMap.get("id"), -1);
		String username = SqlInfusionHelper.filteSqlInfusion(paramMap.get("username1"));
		if (StringUtils.isBlank(username)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		if (username.length() < 6) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
			return null;
		}
		String password = SqlInfusionHelper.filteSqlInfusion(paramMap.get("password1"));
		if (StringUtils.isBlank(password)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
			return null;
		}
		if (password.length() < 6) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "6");
			return null;
		}
		String url = SqlInfusionHelper.filteSqlInfusion(paramMap.get("url1"));
		if (StringUtils.isBlank(url)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "7");
			return null;
		}
		Long reLong = -1L;
		reLong = emalAndMessageService.addMessageSet(id, username, password,
				url, enable);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (reLong > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");// 成功
			operationLogService.addOperationLog("t_messageset", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"更新短信参数设置成功", 2);
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			operationLogService.addOperationLog("t_messageset", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"更新短信参数设置失败", 2);
			return null;
		}
	}

	/**
	 * 增加或修改短信参数设置
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMessageSetMethod2() throws Exception {
		String enablestr = SqlInfusionHelper.filteSqlInfusion(paramMap.get("kaiqi2"));
		if (StringUtils.isBlank(enablestr)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "8");
			return null;
		}
		Integer enable = Convert.strToInt(enablestr, 1);// 默认开启

		Long id = Convert.strToLong(paramMap.get("id"), -1);
		String username = SqlInfusionHelper.filteSqlInfusion(paramMap.get("username2"));
		if (StringUtils.isBlank(username)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		if (username.length() < 6) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
			return null;
		}
		String password = SqlInfusionHelper.filteSqlInfusion(paramMap.get("password2"));
		if (StringUtils.isBlank(password)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
			return null;
		}
		if (password.length() < 6) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "6");
			return null;
		}
		String url = SqlInfusionHelper.filteSqlInfusion(paramMap.get("url2"));
		if (StringUtils.isBlank(url)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "7");
			return null;
		}
		Long reLong = -1L;
		reLong = emalAndMessageService.addMessageSet(id, username, password,
				url, enable);
		if (reLong > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");// 成功
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}
	}

	/**
	 * 联动
	 * 
	 * @return
	 */
	public String linkageinitIndex() {
		return SUCCESS;
	}

	/**
	 *联动详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String linkageinfoMethod() throws Exception {
		emalAndMessageService.querySelectInfo(pageBean);
		return SUCCESS;
	}

	/**
	 * 添加借款目的
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addTargMethod() throws Exception {
		String titleName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
		Long result = -1L;
		if (StringUtils.isNotBlank(titleName)) {
			result = emalAndMessageService.addTarage(titleName);
			if (result > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				return null;
			}
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_select", admin.getUserName(),
				IConstants.INSERT, admin.getLastIP(), 0, "添加借款目的", 2);

		return null;
	}

	/**
	 * 修改借款目的
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String updatetageMethod() throws SQLException, DataException,
			IOException {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		request().setAttribute("id", id);
		return SUCCESS;
	}

	/**
	 * 修改
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updatetagerealMethod() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String conte = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tagd"));
		Long result = -1L;
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (id != null && id != -1 && StringUtils.isNotBlank(conte)) {
			result = emalAndMessageService.updateTage(id, conte);
		}
		if (result > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改借款目的成功", 2);
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改借款目的失败", 2);
			return null;
		}
	}

	/**
	 * 删除借款目的
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deletetageMethod() throws Exception {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		if (id != null && id != -1) {
			emalAndMessageService.deleteTage(id);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_select", admin.getUserName(),
				IConstants.INSERT, admin.getLastIP(), 0, "删除借款目的", 2);
		return SUCCESS;
	}

	/**
	 * 担保机构初始化
	 * 
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String queryAcountInitMe() {
		return SUCCESS;
	}

	/**
	 * 查询担保机构列表
	 * 
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public String queryAcountinfoM() throws Exception {
		emalAndMessageService.queryAccountInfo(pageBean);
		return SUCCESS;
	}

	/**
	 * 增加担保方式
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addAssureM() throws Exception {

		String titleName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
		if (StringUtils.isBlank(titleName)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Long result = -1L;
		if (StringUtils.isNotBlank(titleName)) {
			result = emalAndMessageService.addDan(titleName);
			if (result > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				Admin admin = (Admin) session().getAttribute(
						IConstants.SESSION_ADMIN);
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "新增担保方式", 2);
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				return null;
			}
		}
		return null;

	}

	/**
	 * 修改金额范围 传值到弹出窗口的id值
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String updatam() throws SQLException, DataException, IOException {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		request().setAttribute("id", id);
		return SUCCESS;
	}

	/**
	 * 修改担保机构
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updatamreal() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String conte = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tagd"));
		if (StringUtils.isBlank(conte)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		Long result = -1L;
		if (id != null && id != -1 && StringUtils.isNotBlank(conte)) {
			result = emalAndMessageService.updateAccount(id, conte);
		}
		if (result > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改担保机构", 2);
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}
	}

	/**
	 * 删除金额范围
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteaccM() throws Exception {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		if (id != null && id != -1) {
			emalAndMessageService.deleteacc(id);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_select", admin.getUserName(),
				IConstants.DELETE, admin.getLastIP(), 0, "删除金额范围", 2);
		return SUCCESS;
	}

	/**
	 * 反担保方式初始化
	 * 
	 * @return
	 */
	public String queryInversIndexM() {
		return SUCCESS;
	}

	/**
	 * 反担保方式详情
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryInversInfoM() throws Exception {
		emalAndMessageService.queryIversInof(pageBean);
		return SUCCESS;
	}

	/**
	 * 添加投资金额参数
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addInversM() throws Exception {
		String titleName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
//		if (!StringUtils.isNumeric(titleName)) {
//			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
//			return null;
//		}
		Long result = -1L;
		if (StringUtils.isNotBlank(titleName)) {
			result = emalAndMessageService.addInver(titleName);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			if (result > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "添加投资金额参数成功", 2);
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "添加投资金额参数失败", 2);
				return null;
			}
		}
		return null;
	}

	/**
	 * 将id传到弹出框
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String updateinversM() throws SQLException, DataException,
			IOException {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		request().setAttribute("id", id);
		return SUCCESS;
	}

	public String updateinversRealM() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String conte = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tagd"));
		if (StringUtils.isBlank(conte)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		Long result = -1L;
		if (id != null && id != -1 && StringUtils.isNotBlank(conte)) {
			result = emalAndMessageService.updateInvers(id, conte);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改担保机构和反担保方式成功", 2);
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改担保机构和反担保方式失败", 2);
			return null;
		}
	}

	/**
	 * 删除投标金额
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deleteinversM() throws Exception {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		if (id != null && id != -1) {
			emalAndMessageService.deleteacc(id);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_select", admin.getUserName(),
				IConstants.DELETE, admin.getLastIP(), 0, "删除反担保方式", 2);
		return SUCCESS;
	}

	/**
	 * 初始化图片
	 * 
	 * @return
	 */
	public String queryImageIndexM() {
		return SUCCESS;
	}

	/**
	 * 查看图片详细情况
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryImageInfoM() throws Exception {
		emalAndMessageService.querySysImageInfo(pageBean);
		return SUCCESS;
	}

	/**
	 * 增加图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addSysImageM() throws Exception {

		String titleName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
		if (StringUtils.isBlank(titleName)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Long result = -1L;
		if (StringUtils.isNotBlank(titleName)) {
			result = emalAndMessageService.addSysImage(titleName);
			if (result > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				return null;
			}
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_sysimages", admin.getUserName(),
				IConstants.INSERT, admin.getLastIP(), 0, "新增系统头像图片", 2);
		return null;

	}

	/**
	 * 更新系统头像
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateSysImageM() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String conte = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
		if (StringUtils.isBlank(conte)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		Long result = -1L;
		if (id != null && id != -1 && StringUtils.isNotBlank(conte)) {
			result = emalAndMessageService.updateSysImage(id, conte);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result > 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			operationLogService.addOperationLog("t_sysimages", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"修改系统头像图片成功", 2);
			return null;
		} else {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			operationLogService.addOperationLog("t_sysimages", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"修改系统头像图片失败", 2);
			return null;
		}
	}

	/**
	 * 上传系统头像
	 * 
	 * @return
	 * @throws Exception
	 */
	public String deletSysImageM() throws Exception {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		if (id != null && id != -1) {
			emalAndMessageService.deletSImage(id);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_sysimages", admin.getUserName(),
				IConstants.DELETE, admin.getLastIP(), 0, "删除系统头像", 2);
		return SUCCESS;
	}

	public String updateSelectType() throws Exception {
		int typeId = Convert.strToInt(request("typeId"), -1);
		long id = Convert.strToLong(request("id"), -1);
		long result = -1L;
		try {
			result = emalAndMessageService.updateSelectType(typeId, id);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			if (result > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.UPDATE, admin.getLastIP(),
						0, "修改选择类型成功", 2);
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.UPDATE, admin.getLastIP(),
						0, "修改选择类型成功", 2);
				return null;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 借款期限初始化
	 * 
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String queryDeadlineIndex() {
		return SUCCESS;
	}

	/**
	 * 查询借款期限列表
	 * 
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public String queryDeadlineinfoM() throws Exception {
		emalAndMessageService.queryDeadlineInfo(pageBean);
		return SUCCESS;
	}

	/**
	 * 增加借款期限
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addDeadlineM() throws Exception {

		String titleName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
		if(StringUtils.isEmpty(titleName)){
			ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			return null;
		}
		if (!StringUtils.isNumeric(titleName)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Long result = -1L;
		if (StringUtils.isNotBlank(titleName)) {
			result = emalAndMessageService.addDeadline(titleName);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			if (result > 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "新增借款期限成功", 2);
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "新增借款期限失败", 2);
				return null;
			}
		}
		return null;

	}

	/**
	 * 将id传到弹出框
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String updateDeadlineM() throws SQLException, DataException,
			IOException {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		request().setAttribute("id", id);
		return SUCCESS;
	}

	public String updateDeadlineRealM() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String conte = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tagd"));
		if (!StringUtils.isNumeric(conte)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Long result = -1L;
		if (id != null && id != -1 && StringUtils.isNotBlank(conte)) {
			result = emalAndMessageService.updateDeadline(id, conte);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result > 0) {
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改借款期限成功", 2);
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		} else {
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改借款期限失败", 2);
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}
	}

	/**
	 * 金额范围初始化
	 * 
	 * @return
	 * @throws SQLException
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String queryMomeyIndex() {
		return SUCCESS;
	}

	/**
	 * 查询金额范围列表
	 * 
	 * @return
	 * @throws Exception
	 */

	@SuppressWarnings("unchecked")
	public String queryMomeyinfoM() throws Exception {
		emalAndMessageService.queryMomeyInfo(pageBean);
		return SUCCESS;
	}

	/**
	 * 增加金额范围
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addMomeyM() throws Exception {

		String titleName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("titleName"));
		if (!StringUtils.isNumeric(titleName)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Long result = -1L;
		if (StringUtils.isNotBlank(titleName)) {
			result = emalAndMessageService.addMoney(titleName);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			if (result > 0) {
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "增加金额范围成功", 2);
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			} else {
				operationLogService.addOperationLog("t_select", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "增加金额范围失败", 2);
				ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
				return null;
			}
		}
		return null;

	}

	/**
	 * 将id传到弹出框
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException
	 */
	public String updateMomeyM() throws SQLException, DataException,
			IOException {
		Long id = Convert.strToLong(request().getParameter("id"), -1L);
		request().setAttribute("id", id);
		return SUCCESS;
	}

	/**
	 * 修改金额范围
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMomeyRealM() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String conte = SqlInfusionHelper.filteSqlInfusion(paramMap.get("tagd"));
		if (!StringUtils.isNumeric(conte)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			return null;
		}
		Long result = -1L;
		if (id != null && id != -1 && StringUtils.isNotBlank(conte)) {
			result = emalAndMessageService.updateMoney(id, conte);
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result > 0) {
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改金额范围成功", 2);
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		} else {
			operationLogService.addOperationLog("t_select",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "修改金额范围失败", 2);
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		}
	}

}
