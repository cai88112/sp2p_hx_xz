package com.sp2p.action.admin;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.CacheManager;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.NewsAndMediaReportService;

/**
 * 网站公告Action
 * 
 * @author zhongchuiqing
 * 
 */
@SuppressWarnings("unchecked")
public class NewsAction extends BasePageAction {

	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(NewsAction.class);

	private NewsAndMediaReportService newsService;

	public NewsAndMediaReportService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsAndMediaReportService newsService) {
		this.newsService = newsService;
	}

	/**
	 * 初始化分页查询网站公告列表
	 * 
	 * @return
	 */
	public String queryNewsListInit() {
		return SUCCESS;
	}

	/**
	 * 分页查询网站公告列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryNewsListPage() throws Exception {
		try {
			newsService.queryNewsPage(pageBean);
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
	 * 招贤纳士
	 * 
	 * @return
	 * @throws Exception
	 */
	
	public String queryRecruitmentInfoInit() {
		return SUCCESS;
	}
	
	public String queryRecruitmentInfo() throws Exception {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map = newsService.queryRecruitmentInfo();
			if(map == null){
				map = new HashMap<String, String>();
			}
            request().setAttribute("map", map.get("content"));
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加网站公告信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addNews() throws Exception {
		Admin user = (Admin) session().getAttribute("admin");
		Integer sort = Convert.strToInt(paramMap.get("sort"), 1);
		String title = SqlInfusionHelper.filteSqlInfusion(paramMap.get("title"));
		String content = SqlInfusionHelper.filteSqlInfusion(paramMap.get("content"));
		

		Long userId = -1L;
		if (user != null) {
			userId = user.getId();
		}
		String visits = SqlInfusionHelper.filteSqlInfusion(paramMap.get("visits"));
		String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		String noticeType = SqlInfusionHelper.filteSqlInfusion(paramMap.get("noticeType"));
		@SuppressWarnings("unused")
		String message = "添加失败";
		Long result = -1L;
		try {
			result = newsService.addNews(sort, title, content, userId, visits,
					publishTime,noticeType);
			if (result > 0) {
				message = "添加成功";
				// 清空分页，列表数据
				CacheManager.clearByKey(IConstants.CACHE_WZGG_INDEX);
				CacheManager.clearByKey(IConstants.CACHE_WZGG_WZDT);
				CacheManager.clearStartsWithAll(IConstants.CACHE_WZGG_PAGE_);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加网站公告信息初始化
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addNewsInit() throws Exception {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String publishTime = SqlInfusionHelper.filteSqlInfusion(format.format(new Date()));
		paramMap.put("publishTime", publishTime);

		// -----add by houli 给出默认序列号值
		Map<String, String> map = newsService.getMaxSerial();
		if (map == null || map.get("sortId") == null) {
			paramMap.put("sort", String.valueOf(1));
		} else {
			int sortId = Convert.strToInt(map.get("sortId"), 1);
			paramMap.put("sort", String.valueOf(sortId + 1));
		}
		// 新添加的公告信息浏览量默认为0
		paramMap.put("visits", String.valueOf(0));
		// -----------

		return SUCCESS;
	}

	/**
	 * 更新初始化，根据Id获取网站公告信息详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateNewsInit() throws Exception {
		Long id = Convert.strToLong(request("id"), 0);
		try {
			paramMap = newsService.getNewsById(id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}

	/**
	 * 预览（在添加或更新中）
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String PreviewNews() throws SQLException, DataException,
			UnsupportedEncodingException {

		Admin user = (Admin) session().getAttribute("admin");
		String userName = null;
		if (user != null) {
			userName = user.getUserName();
		}
		Long id = Convert.strToLong(request().getParameter("id"), 0);
		/*String sort = request().getParameter("sort");
		String title = request().getParameter("title");
		String content = request().getParameter("content");
		String visits = request().getParameter("visits");
		String publishTime = request().getParameter("publishTime");
		title = URLDecoder.decode(title, "UTF-8");
		content = URLDecoder.decode(content, "UTF-8");
		visits = URLDecoder.decode(content, "UTF-8");
		publishTime = URLDecoder.decode(publishTime, "UTF-8");*/
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = newsService.getNewsById(id);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		map.put("userName", userName);
		/*map.put("sort", sort);
		map.put("title", title);
		map.put("content", content);
		map.put("visits", visits);
		map.put("pubiishTime", publishTime);*/
		request().setAttribute("newsPreview", map);
		return SUCCESS;
	}

	/**
	 * 更新网站公告信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateNews() throws Exception {
		Admin user = (Admin) session().getAttribute("admin");
		Long id = Convert.strToLong(paramMap.get("id"), 0);
		Integer sort = Convert.strToInt(paramMap.get("sort"), 1);
		String title = SqlInfusionHelper.filteSqlInfusion(paramMap.get("title"));
		String content = SqlInfusionHelper.filteSqlInfusion(paramMap.get("content"));
		String noticeType = SqlInfusionHelper.filteSqlInfusion(paramMap.get("noticeType"));
		Long userId = -1L;
		if (user != null) {
			userId = user.getId();
		}

		Integer visits = Convert.strToInt(paramMap.get("visits"), -1);
		String publishTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		@SuppressWarnings("unused")
		String message = "更新失败";

		try {

			long result = newsService.updateNews(id, sort, title, content,
					userId, visits, publishTime,noticeType);
			if (result > 0) {
				message = "更新成功";
				// 清空分页，列表数据，当前明细
				CacheManager.clearByKey(IConstants.CACHE_WZGG_INDEX);
				CacheManager.clearByKey(IConstants.CACHE_WZGG_WZDT);
				CacheManager.clearByKey(IConstants.CACHE_WZGG_INFO_ + id);
				CacheManager.clearStartsWithAll(IConstants.CACHE_WZGG_PAGE_);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;

	}

	/**
	 * 删除网站公告数据
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String deleteNews() throws Exception {
		String dynamicIds = SqlInfusionHelper.filteSqlInfusion(request("id"));
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
			newsService.deleteNews(dynamicIds, ",");
			// 清空分页，列表数据，当前明细
			CacheManager.clearByKey(IConstants.CACHE_WZGG_INDEX);
			CacheManager.clearByKey(IConstants.CACHE_WZGG_WZDT);
			CacheManager.clearStartsWithAll(IConstants.CACHE_WZGG_PAGE_);
			for (String id : newsids) {
				CacheManager.clearByKey(IConstants.CACHE_WZGG_INFO_ + id);
			}

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
			Long result = newsService.isExistSortId(id);

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
			Long result = newsService.isExistToUpdate(id, originalId);

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
