/**   
* @Title: returnedmoneyAction.java 
* @Package com.sp2p.action.admin 
* @Description: TODO(用一句话描述该文件做什么) 
* @author yinzisong 690713748@qq.com   
* @date 2014年12月31日 上午10:29:55 
* @version V1.0   
*/ 
package com.sp2p.action.admin;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.fp2p.helper.ExcelHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.ReturnedmoneyService;

/** 
 * 回款续投管理.
 * @ClassName: returnedmoneyAction 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author yinzisong 690713748@qq.com 
 * @date 2014年12月31日 上午10:29:55 
 *  
 */
public class ReturnedmoneyAction extends BasePageAction{

	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(ReturnedmoneyAction.class);
	private ReturnedmoneyService returnedmoneyService;
	private UserService userService;
	/**
	 * 初始化回款续投页面.
	* @Title: retrunedmoneyPayeInit 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String returnedmoneyPageInit(){
		log.info("初始化页面retrunedmoneyPageInit");
		return SUCCESS;
	}	
	
	
	/**
	 * 查找分页列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryreturnedmoneyDetail() throws Exception {
		try {
			String userName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")), null);
			returnedmoneyService.queryReturnedmoneyPageAll(pageBean, userName);
			List<Map<String, Object>> list = pageBean.getPage();
			if (list != null) {
				for (Map<String, Object> map : list) {
					long userid = (Long) map.get("userId");
					Map<String, String> userMap = userService
							.queryUserById(userid);
					map.put("username", userMap.get("username").toString());
					Map<String, String> personMap = userService
							.queryPersonById(userid);
					if (personMap != null) {
						map.put("realName", personMap.get("realName")
								.toString());
					} else {
						map.put("realName", null);
					}
				}
			}
			request().setAttribute("experiencemoneyList", list);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		int pageNum = (int) (pageBean.getPageNum() - 1)
				* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);
		return SUCCESS;
	}

	/**
	 * 导出回款续投详情
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String exportforReturnedmoneyDetail() {
		pageBean.pageNum = 1;
		pageBean.setPageSize(100000);

		try {
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			String userName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("userName")), null);			
			returnedmoneyService.queryReturnedmoneyPageAll(pageBean,userName);
			List<Map<String, Object>> list = pageBean.getPage();
			
			if (list != null) {
				for (Map<String, Object> map : list) {
					long userid = (Long) map.get("userId");
					Map<String, String> userMap = userService
							.queryUserById(userid);
					map.put("username", userMap.get("username").toString());
					Map<String, String> personMap = userService
							.queryPersonById(userid);
					if (personMap != null) {
						map.put("realName", personMap.get("realName")
								.toString());
					} else {
						map.put("realName", null);
					}
				}
			}
			pageBean.setPage(list);
			if (pageBean.getPage() == null) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能为空! ');window.history.go(-1);</script>");
				return null;
			}
			if (pageBean.getPage().size() > IConstants.EXCEL_MAX) {
				getOut()
						.print(
								"<script>alert(' 导出记录条数不能大于50000条 ');window.history.go(-1);</script>");
				return null;
			}
			
			HSSFWorkbook wb = ExcelHelper.exportExcel("回款续投列表", pageBean
					.getPage(), new String[] { "用户名",
					"真实姓名","回款金额", "应收续投奖金", "已收续投奖金"}, 
					new String[] { "username",
					"realName", "sumreturnedmoney", "recievedreinvestreward", "hasreinvestreward" });
			this.export(wb, new Date().getTime() + ".xls");
			operationLogService.addOperationLog("t_returnedmoney", admin
					.getUserName(), IConstants.EXCEL, admin.getLastIP(), 0,
					"导出回款续投列表", 2);

		} catch (SQLException e) {

			e.printStackTrace();
		} catch (DataException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ReturnedmoneyService getReturnedmoneyService() {
		return returnedmoneyService;
	}

	public void setReturnedmoneyService(ReturnedmoneyService returnedmoneyService) {
		this.returnedmoneyService = returnedmoneyService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
