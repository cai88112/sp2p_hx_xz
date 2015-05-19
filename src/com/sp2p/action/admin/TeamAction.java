package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.PublicModelService;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.CacheManager;
import com.shove.web.action.BasePageAction;

/**
 * 团队管理
 * 
 * @author zhongchuiqing
 * 
 */
@SuppressWarnings("unchecked")
public class TeamAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(DownloadAction.class);

	private PublicModelService teamService;

	public PublicModelService getTeamService() {
		return teamService;
	}

	public void setTeamService(PublicModelService teamService) {
		this.teamService = teamService;
	}

	/**
	 * 初始化分页查询团队信息列表
	 * 
	 * @return
	 */
	public String queryTeamListInit() {
		return SUCCESS;
	}

	/**
	 * 分页查询团队信息列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryteamListPage() throws Exception {
		try {
			teamService.queryTeamPage(pageBean);
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
	 * 添加团队信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addTeam() throws Exception {
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName"));
		Integer sort = Convert.strToInt(paramMap.get("sort"), 1);
		String imgPath = SqlInfusionHelper.filteSqlInfusion(paramMap.get("imgPath"));
		String intro = SqlInfusionHelper.filteSqlInfusion(paramMap.get("intro"));
		String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		@SuppressWarnings("unused")
		String message = "添加失败";
		Long result = -1L;
		try {
			result = teamService.addTeam(sort, userName, imgPath, intro,
					publishTime);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			if (result > 0) {
				message = "添加成功";
				operationLogService.addOperationLog("t_team", admin
						.getUserName(), IConstants.INSERT, admin.getLastIP(),
						0, "新增团队信息成功", 2);
				// 清除列表信息
				CacheManager.clearByKey(IConstants.CACHE_TDJS_ALL);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加团队信息初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addTeamInit() throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String publishTime = format.format(new Date());
		paramMap.put("publishTime", publishTime);

		// -----add by houli 给出默认序列号值
		Map<String, String> map = teamService.getTeamMaxSerial();
		if (map == null || map.get("sortId") == null) {
			paramMap.put("sort", String.valueOf(1));
		} else {
			int sortId = Convert.strToInt(map.get("sortId"), 1);
			paramMap.put("sort", String.valueOf(sortId + 1));
		}
		// -----------

		return SUCCESS;
	}

	/**
	 * 更新初始化，根据Id获取团队信息详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateTeamInit() throws Exception {
		Long id = Convert.strToLong(request("id"), 0);
		try {
			paramMap = teamService.getTeamById(id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	/**
	 * 更新团队信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateTeam() throws Exception {
		Long id = Convert.strToLong(paramMap.get("id"), -1L);
		String userName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName"));
		Integer sort = Convert.strToInt(paramMap.get("sort"), 1);
		String imgPath = SqlInfusionHelper.filteSqlInfusion(paramMap.get("imgPath"));
		String intro = SqlInfusionHelper.filteSqlInfusion(paramMap.get("intro"));
		String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		@SuppressWarnings("unused")
		String message = "更新失败";
		try {
			long result = teamService.updateTeam(id, sort, userName, imgPath,
					intro, publishTime);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			if (result > 0) {
				message = "更新成功";
				operationLogService.addOperationLog("t_team", admin
						.getUserName(), IConstants.UPDATE, admin.getLastIP(),
						0, "更新团队信息成功", 2);
				// 清除列表信息
				CacheManager.clearByKey(IConstants.CACHE_TDJS_ALL);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;

	}

	/**
	 * 删除团队介绍数据
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String deleteTeam() throws Exception {
		String dynamicIds = request("id");
		String[] newsids = dynamicIds.split(",");
		if (newsids.length > 0) {
			long tempId = 0;
			for (String str : newsids) {
				tempId = Convert.strToLong(str, -1);
				if (tempId == -1) {
					return INPUT;
				}
			}
		} else {
			return INPUT;
		}

		try {
			teamService.deleteTeam(dynamicIds, ",");
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_team", admin.getUserName(),
					IConstants.UPDATE, admin.getLastIP(), 0, "删除团队介绍数据", 2);
			// 清除列表信息
			CacheManager.clearByKey(IConstants.CACHE_TDJS_ALL);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * add by houli 判断所填写的sort是否是唯一的
	 * 
	 * @return
	 * @throws Exception
	 */
	public String isExistSortId() throws Exception {
		int id = Convert.strToInt(request("sort"), -1);
		try {
			Long result = teamService.isExistSortId(id);

			if (result <= 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}

	public String isExistToUpdate() throws Exception {
		int id = Convert.strToInt(paramMap.get("sortId"), -1);
		int originalId = Convert.strToInt(paramMap.get("originalId"), -1);
		try {
			Long result = teamService.isExistToUpdate(id, originalId);

			if (result <= 0) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
}
