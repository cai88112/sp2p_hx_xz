package com.sp2p.action.front;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;



import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.vo.PageBean;
import com.shove.web.Cache;
import com.shove.web.CacheManager;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.DownloadService;
import com.sp2p.service.OperationLogService;


/**
 * 前台下载专区
 * @author Administrator
 *
 */
public class FrontDownloadAction extends BasePageAction {
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(FrontDownloadAction.class);
	private DownloadService downloadService;
	private OperationLogService  operationLogService;
	private Map<String, String> previousDate;  //上一条
	private Map<String,String> lastDate;  //下一条
	
	
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
	/**
	 * 初始化下载数据
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String frontQueryDownloadInit()throws SQLException,DataException{
		return SUCCESS;
	}
	
	/**
	 * 查询下载资料列表
	 * @return String
	 * @throws SQLException
	 * @throws DataException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public String frontQueryDownlaodList() throws Exception{

		String pageNum = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("pageNum")), null);
		if(StringUtils.isNotBlank(pageNum)){
			pageBean.setPageNum(pageNum);
		}
		pageBean.setPageSize(10);
		downloadService.queryDownloadPage(pageBean, null);
		return SUCCESS;
	}
	
	/**
	 * 根据Id获取下载资料详情
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String frontQueryDownloadById()throws Exception{
		Long id=Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(request("id")),-1);
		try {
			//上一条
			previousDate=downloadService.frontDownloadPreById(id);
			//下一条
			lastDate=downloadService.frontDownloadAfterById(id);
			
			paramMap=downloadService.frontDownloadById(id);
			//浏览次数增加
			downloadService.updateDownload(id, null, null, null, 
					Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(paramMap.get("visits")),-1)+1, null, null, null, null);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	/**
	 * @throws Exception 
	* 下载文件
	* @return String
	 * @throws DataException 
	 * @throws SQLException 
	* @throws
	*/
	@SuppressWarnings("unused")
	public String downloads() throws Exception{
		
		try {
			User user = (User)session().getAttribute("user");
			Long id = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(request("id")), -1);
			paramMap = downloadService.frontDownloadById(id);
				
				String path = SqlInfusionHelper.filteSqlInfusion(paramMap.get("attachment"));
				String result=null;
				if(path!=null){
					result=downloadFile(path);
				}
				if(result.equals("success")){
					operationLogService.addOperationLog("v_t_download_detail", user.getUserName(), IConstants.DOWNLOAD, user.getLastIP(), 0, "用户下载资料", 1);
					return SUCCESS;
				}else{
					request().setAttribute("failMessage", "该文件不存在");
					return "fail";
					
				}
				
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return SUCCESS;
			
	
	
	}
	
	/**
	 * 文件下载处理
	 */
	private String downloadFile(String fielPath){
		String result="";
		try {
			String path = ServletActionContext.getServletContext().getRealPath("/") + fielPath;
		//	 path = path.replace('\\', '/');
			log.info("------------------"+path);
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();
			// 取得文件的后缀名。

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response().reset();
			// 设置response的Header
			response().addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));
			response().addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response().getOutputStream());
			response().setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			result="success";
			
		} catch (IOException ex) {
			ex.printStackTrace();
			result="fail";
			
		}
		return result;
	}



	public OperationLogService getOperationLogService() {
		return operationLogService;
	}
	public void setOperationLogService(OperationLogService operationLogService) {
		this.operationLogService = operationLogService;
	}

	public DownloadService getDownloadService() {
		return downloadService;
	}
	public void setDownloadService(DownloadService downloadService) {
		this.downloadService = downloadService;
	}
	
}
