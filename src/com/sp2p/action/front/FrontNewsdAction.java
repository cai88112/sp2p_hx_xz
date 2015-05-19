package com.sp2p.action.front;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.service.NewsAndMediaReportService;

/**
 * 前台下载专区
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("unchecked")
public class FrontNewsdAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(FrontNewsdAction.class);
	private NewsAndMediaReportService newsService;

	private Map<String, String> previousDate; // 上一条
	private Map<String, String> lastDate; // 下一条

	public String initNews() throws SQLException, DataException {
		return SUCCESS;
	}

	/**
	 * 查询网站公告列表(每次显示五条)
	 * 
	 * @return String
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public String frontQueryNewsList() throws Exception {
		Integer id = Convert.strToInt(request("id"), -1);
		List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
		pageBean.setPageSize(5);
		newsService.frontQueryNewsPage(pageBean);
		newsList = pageBean.getPage();
		request().setAttribute("newsList", newsList);
		if (id == 2) {
			return "ganggao";
		} else {
			return SUCCESS;
		}

	}

	/**
	 * 分页显示网站公告列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontQueryNewsListPage() throws Exception {
		try {
			newsService.queryNewsPage(pageBean);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 根据Id获取网站公告详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontQueryNewsById() throws Exception {
		Long id = Convert.strToLong(request("id"), -1);
		try {
			// 上一条
			previousDate = newsService.getNewsById(id,-1);
			// 下一条
			lastDate = newsService.getNewsById(id,1);
			paramMap = newsService.getNewsById(id);
			if(paramMap != null && paramMap.size() >0){
			// 浏览次数增加
				newsService.updateNews(id, null, null, null, null, Convert
						.strToInt(paramMap.get("visits"), -1) + 1, null,null);
	
				// 添加缓存后，浏览次数会改变
				paramMap.put("visits", Convert
						.strToLong(paramMap.get("visits"), -1)
						+ 1 + "");
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public NewsAndMediaReportService getNewsService() {
		return newsService;
	}

	public void setNewsService(NewsAndMediaReportService newsService) {
		this.newsService = newsService;
	}

	public Map<String, String> getPreviousDate() {
		return previousDate;
	}

	public void setPreviousDate(Map<String, String> previousDate) {
		this.previousDate = previousDate;
	}

	public Map<String, String> getLastDate() {
		return lastDate;
	}

	public void setLastDate(Map<String, String> lastDate) {
		this.lastDate = lastDate;
	}

}
