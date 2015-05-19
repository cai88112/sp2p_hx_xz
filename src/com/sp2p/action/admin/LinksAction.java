package com.sp2p.action.admin;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.service.PublicModelService;

/**
 * 客服中心帮助问题处理
 * 
 * @author li.hou
 * 
 */
@SuppressWarnings("unchecked")
public class LinksAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(LinksAction.class);

	private PublicModelService linksService;

	public PublicModelService getLinksService() {
		return linksService;
	}

	public void setLinksService(PublicModelService linksService) {
		this.linksService = linksService;
	}

	/**
	 * 添加帮助中心数据初始化
	 * 
	 * @return String
	 * @throws DataException
	 * @throws SQLException
	 * @throws
	 */
	public String addLinksInit() throws Exception {
		// 获得数据库中最大的序列号，不用用户自己填写
		Map<String, String> map = linksService.getLinkMaxSerial();
		if(map == null){
			map = new HashMap<String, String>();
		}
		long val = Convert.strToLong(map.get("max(serialCount)"), 0) + 1;
		paramMap.put("serialCount", val + "");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		paramMap.put("publishTime", sf.format(new Date()));
		return SUCCESS;
	}

	/**
	 * 添加帮助问题数据
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String addLinks() throws Exception {//		
		String companyName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyName")); // 公司名称
		String companyImg = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyImg"));// 图片
		String companyURL = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyURL"));// 公司网址
		int serialCount = Convert.strToInt(paramMap.get("serialCount"), 1); // 序号(链接Id)
		String dateTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		@SuppressWarnings("unused")
		String messageInfo = "添加失败";
		try {
			long result = linksService.addLinks(companyName, companyImg,
					companyURL, serialCount, dateTime);
			if (result > 0) {
				messageInfo = "添加成功";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 修改初始化
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String updateLinksInit() throws Exception {
		long typeId = Long.parseLong(request("commonId"));
		try {
			paramMap = linksService.queryLinksInfoByid(typeId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		request().setAttribute("commonId", typeId);
		return SUCCESS;
	}

	/**
	 * 修改
	 * 
	 * @return String
	 * @throws DataException
	 * @throws SQLException
	 * @throws Exception
	 */
	public String updateLinks() throws Exception {
		String companyName = SqlInfusionHelper.filteSqlInfusion(Convert
				.strToStr(paramMap.get("companyName"), null)); // 公司名称
		String companyImg = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("companyImg"), null));// 图片
		String companyURL = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("companyURL"), null));// 公司网址
		int serialCount = Convert.strToInt(paramMap.get("serialCount"), 1); // 序号(链接Id)
		String dateTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("publishTime"), null));
		@SuppressWarnings("unused")
		String messageInfo = "修改失败";
		try {
			long result = linksService.updateLinks(companyName, companyImg,
					companyURL, serialCount, dateTime);
			if (result > 0) {
				messageInfo = "修改成功";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String queryLinksListPageInit() throws DataException, SQLException {

		return SUCCESS;
	}

	public String queryLinksListPage() throws Exception {
		try {
			linksService.queryLinksPage(pageBean);
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
	 * 删除
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String deleteLinks() throws Exception {
		String linksIds = SqlInfusionHelper.filteSqlInfusion(request("commonId"));
		String[] allIds = linksIds.split(",");// 进行全选删除的时候获得多个id值
		if (allIds.length > 0) {
			long tempId = 0;
			for (String str : allIds) {
				tempId = Convert.strToLong(str, -1);
				if (tempId == -1) {
					return INPUT;
				}
			}
		} else {
			return INPUT;
		}
		linksService.deleteLinkss(linksIds);
		return SUCCESS;
	}

	/**
	 * 首页滚动图片
	 * 
	 * @return
	 * @author Administrator
	 */
	public String queryIndexRollImgInit() {
		return SUCCESS;
	}

	public String queryIndexRollImgInfo() throws Exception {
		linksService.queryIndexRollImgPage(pageBean);
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}

	public String deleteIndexRollImg() throws Exception {
		String linksIds = SqlInfusionHelper.filteSqlInfusion(request("commonId"));
		String[] allIds = linksIds.split(",");// 进行全选删除的时候获得多个id值
		if (allIds.length > 0) {
			long tempId = 0;
			for (String str : allIds) {
				tempId = Convert.strToLong(str, -1);
				if (tempId == -1) {
					return INPUT;
				}
			}
		} else {
			return INPUT;
		}
		linksService.deleteLinkss(linksIds);
		return SUCCESS;
	}

	public String updateIndexRollImgInfo() throws Exception {
		String companyImg = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("companyImg"), null));// 图片
		int serialCount = Convert.strToInt(paramMap.get("serialCount"), 1); // 序号(链接Id)
		int ordershort = Convert.strToInt(paramMap.get("ordershort"), 1); // 序号(链接Id)
		String dateTime = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("publishTime"), null));
		String companyURL = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("companyURL"), null));// 公司网址
		@SuppressWarnings("unused")
		String messageInfo = "修改失败";
		try {
			long result = linksService.updateIndexRollImg(companyImg,
					serialCount, dateTime, ordershort,companyURL);
			if (result > 0) {
				messageInfo = "修改成功";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	public String updateIndexRollImgInit() throws Exception {
		long typeId = Long.parseLong(request("commonId"));
		try {
			paramMap = linksService.queryLinksInfoByid(typeId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		request().setAttribute("commonId", typeId);
		return SUCCESS;
	}

	public String addIndexRollImgInit() throws Exception {
		// 获得数据库中最大的序列号，不用用户自己填写
		Map<String, String> map = linksService.getMaxIndexRollImgSerial();
		if(map == null){
			map = new HashMap<String, String>();
		}
		long val = Convert.strToLong(map.get("max(serialCount)"), 0) + 1;
		paramMap.put("serialCount", val + "");
		return SUCCESS;
	}

	public String addIndexRollImgInfo() throws Exception {//		
		String companyImg = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyImg"));// 图片
		int serialCount = Convert.strToInt(paramMap.get("serialCount"), 1); // 序号(链接Id)
		int ordershort = Convert.strToInt(paramMap.get("ordershort"), 1); // 序号(链接Id)
		String dateTime = SqlInfusionHelper.filteSqlInfusion(paramMap.get("publishTime"));
		String companyURL = SqlInfusionHelper.filteSqlInfusion(Convert.strToStr(paramMap.get("companyURL"), null));// 公司网址
		int cardStatus = Convert.strToInt(paramMap.get("cardStatus"), -1);// 图片作用，类型
		@SuppressWarnings("unused")
		String messageInfo = "添加失败";
		try {
			long result = linksService.addIndexRollImg(companyImg, serialCount,
					dateTime, ordershort, cardStatus,companyURL);
			if (result > 0) {
				messageInfo = "添加成功";
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
}
