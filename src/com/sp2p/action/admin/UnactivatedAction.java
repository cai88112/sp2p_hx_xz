package com.sp2p.action.admin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.OperationLogService;
import com.sp2p.service.UserService;
import com.sp2p.service.admin.UnactivatedService;
/**
 * 后台管理用户 - 未激活用户
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UnactivatedAction extends BaseFrontAction{
	public static Log log = LogFactory.getLog(UnactivatedAction.class);
	private UnactivatedService unactivatedService;
	private UserService  userService;
	
	/**
	 * 查询未激活用户
	 * @return
	 */
	public String unactivatedindex(){
		return SUCCESS;
	}
	@SuppressWarnings("unchecked")
	public String unactivatedinfo() throws SQLException, Exception{
		String userName = paramMap.get("userName");
		String createtimeStart = paramMap.get("createtimeStart");
		String createtimeEnd = paramMap.get("createtimeEnd");
		String email = paramMap.get("email");
		unactivatedService.queryUserUnactivated(pageBean,userName,createtimeStart,createtimeEnd,email);
		
		int pageNum = (int) (pageBean.getPageNum() - 1)* pageBean.getPageSize();
		request().setAttribute("pageNum", pageNum);

		return SUCCESS;
	}
	/**
	 * 激活账户
	 * @return
	 */
	public String updateUserActivate(){
		long userId =Convert.strToLong(request("userId"),-1L);
		Map<String,String> userMap = new  HashMap<String, String>();
		try {
			Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
			long result = userService.updateUserActivate(userId);
			if(result > 0 ){
				userMap = userService.queryUserById(userId);
				operationLogService.addOperationLog("t_user", admin.getUserName(),IConstants.UPDATE, admin.getLastIP(), 0, "管理员激活用户:"+Convert.strToStr(userMap.get("username"), ""), 2);
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		} 
		return SUCCESS;
	}
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public UnactivatedService getUnactivatedService() {
		return unactivatedService;
	}

	public void setUnactivatedService(UnactivatedService unactivatedService) {
		this.unactivatedService = unactivatedService;
	}
}
