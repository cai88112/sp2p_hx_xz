package com.sp2p.action.admin;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.service.PublicModelService;

/**
 * 信息管理Action
 * 
 * @author zhongchuiqing
 * 
 */
@SuppressWarnings("unchecked")
public class MessageAction extends BasePageAction {

	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(MessageAction.class);

	private PublicModelService messageService;

	public PublicModelService getMessageService() {
		return messageService;
	}

	public void setMessageService(PublicModelService messageService) {
		this.messageService = messageService;
	}

	private List<Map<String, Object>> list;

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	/**
	 * 初始化查询信息管理列表
	 * 
	 * @return
	 */
	public String queryMessageListInit() {
		return SUCCESS;
	}

	/**
	 *查询信息管理列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryMessageList() throws Exception {
		try {
			list = messageService.findMessageList();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加信息管理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addMessage() throws Exception {

		Integer sort = Convert.strToInt(paramMap.get("sort"), 1);
		String columName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("columName"));
		String content = SqlInfusionHelper.filteSqlInfusion(paramMap.get("content"));
		String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		@SuppressWarnings("unused")
		String message = "添加失败";
		Long result = -1L;
		try {
			messageService.addMessage(sort, columName, content, publishTime);
			if (result > 0) {
				message = "添加成功";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加信息管理初始化
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String addMessageInit() throws SQLException, DataException {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String publishTime = SqlInfusionHelper.filteSqlInfusion(format.format(new Date()));
		paramMap.put("publishTime", publishTime);

		return SUCCESS;
	}

	/**
	 * 更新初始化，根据Id获取信息管理信息详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMessageInit() throws Exception {
		Long id = Convert.strToLong(request("id"), 0);
		try {
			paramMap = messageService.getMessageById(id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	/**
	 * 更新信息管理
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateMessage() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		Integer sort = Convert.strToInt(paramMap.get("sort"), 1);
		String columName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("columName"));
		String content = SqlInfusionHelper.filteSqlInfusion(paramMap.get("content"));
		String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		@SuppressWarnings("unused")
		Long userId = -1L;
		@SuppressWarnings("unused")
		String message = "更新失败";
		try {

			long result = messageService.updateMessage(id, sort, columName,
					content, publishTime);
			if (result > 0) {
				message = "更新成功";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;

	}

}
