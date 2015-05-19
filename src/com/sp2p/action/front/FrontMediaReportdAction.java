package com.sp2p.action.front;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.vo.PageBean;
import com.shove.web.Cache;
import com.shove.web.CacheManager;
import com.shove.web.action.BasePageAction;
import com.sp2p.action.admin.DownloadAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.DownloadService;
import com.sp2p.service.NewsAndMediaReportService;

/**
 * 前台媒体报道
 * 
 * @author Administrator
 * 
 */
public class FrontMediaReportdAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(FrontMediaReportdAction.class);
	private NewsAndMediaReportService mediaReportService;

	public NewsAndMediaReportService getMediaReportService() {
		return mediaReportService;
	}

	public void setMediaReportService(
			NewsAndMediaReportService mediaReportService) {
		this.mediaReportService = mediaReportService;
	}

	/**
	 * 初始化下载数据
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String frontQueryMediaReportdInit() throws SQLException,
			DataException {
		return SUCCESS;
	}

	/**
	 * 查询下载资料列表
	 * 
	 * @return String
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public String frontQueryMediaReportdList() throws Exception {
		String pageNum = (String) (SqlInfusionHelper.filteSqlInfusion(request().getParameter("curPage")) == null ? ""
				: SqlInfusionHelper.filteSqlInfusion(request().getParameter("curPage")));
		if (StringUtils.isNotBlank(pageNum)) {
			pageBean.setPageNum(pageNum);
		}
		pageBean.setPageSize(IConstants.PAGE_SIZE_10);
		mediaReportService.queryMediaReportPage(pageBean);
		List<Map<String, Object>> lists = pageBean.getPage();

		// 截取内容字段 houli
		String content = "";
		if (lists != null) {
			for (Map<String, Object> map : lists) {
				if ((map.get("content")+"").length() > 50) {
					content = (map.get("content")+"").substring(0, 50);
					map.put("content", content + "...");
				}
			}
		}

		return SUCCESS;
	}

	/**
	 * 根据Id获取下载资料详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontQueryMediaReportById() throws Exception {
		Long id = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(request("id")), -1);
		try {
			Map<String, String> map = null;
			map = mediaReportService.getMediaReportById(id);
			JSONHelper.printObject(map);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据Id获取媒体报道
	 * 
	 * @return
	 * @throws Exception
	 */
	public String frontMedialinkId() throws Exception {
		Long id = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(request("id")), -1);
		try {
			paramMap = mediaReportService.getMediaReportById(id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

}
