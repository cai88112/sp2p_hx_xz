package com.sp2p.action.admin;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.shove.Convert;
import com.shove.web.action.BasePageAction;
import com.sp2p.service.admin.PlatformCostService;

/**
 * 平台收费标准
 * @author C_J
 *
 */
public class PlatformCostAction  extends BasePageAction {

	private static final long serialVersionUID = 1L;
	
	public static Log log = LogFactory.getLog(PlatformCostAction.class);
	
	private PlatformCostService platformCostService;

	
	/**
	 *  平台收费初始化
	 * @return
	 * @throws Exception
	 */
	public String showPlatformCostInit() throws Exception {
		int type = Convert.strToInt(request().getParameter("typess"), 1);
		request().setAttribute("type", type);
		return SUCCESS;
	}
	
	/**
	 * 所有平台收费情况
	 * @return
	 * @throws Exception
	 */
	public String showPlatformCostList() throws Exception {
		int type = Convert.strToInt(request().getParameter("type"), 1);
		platformCostService.queryPlatformCostAll(pageBean,type);//分页显示
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}
  
	/**
	 * 修改初始化
	 * @return
	 * @throws Exception
	 */
	public String updatePlatformCostbyIdInit() throws Exception {
		request().setAttribute("id", request().getParameter("id"));
		return SUCCESS;
	}
	
	
	/**
	 * 根据ID修改平台收费情况
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String updatePlatformCost() throws Exception {
	  int id=Convert.strToInt(request().getParameter("id"),0);
	  double costFee=Convert.strToDouble(request().getParameter("costFee"), 0);
	  int   costMode=Convert.strToInt(request().getParameter("costMode"), 0);
	  if(costMode==1)
	  {
		  if(!(costFee>=0 && costFee <=100))
		  {
			  ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
			  return null;
		  }
	  }else
	  {
		  if(costFee<0)
		  {
			  ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
			  return null;
		  }
	  }
	  Long result= platformCostService.updatePlatformCostById(costFee, id,getPlatformCost());
	  if(result<0){
			ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
			return null;
		}else
		{
			ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
			
			return null;
		}	
		
	}
	
	
	 /**
	  * 修改 隐藏显示
	  * @return
	  */
	public String updateShow_view() {
		int id= Convert.strToInt( request("id"),-1);
		int show_view = Convert.strToInt(request("show_view"), 0);
		long result = -1L;
		try {
			result = platformCostService.updateShow_view(id, show_view);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		 if  (result > 0) 
			 return INPUT;
		 
		 return SUCCESS;
	}
	/**
	 * 根据ID查询平台收费情况
	 * @return
	 * @throws Exception
	 */
	public String updatePlatformCostbyIdInfo() throws Exception {
		 int id=Convert.strToInt(request("paramMap.id"),0);
		 Map<String, String> map=platformCostService.queryPlatformCostById(id);
		 request().setAttribute("maps", map);
		return SUCCESS;
	}


	public void setPlatformCostService(PlatformCostService platformCostService) {
		this.platformCostService = platformCostService;
	}
	
}
