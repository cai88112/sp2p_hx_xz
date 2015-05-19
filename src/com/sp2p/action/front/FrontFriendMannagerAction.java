package com.sp2p.action.front;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.DesHelper;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.RecommendUserService;

/**
 * 好友管理
 * @author Administrator
 *
 */
public class FrontFriendMannagerAction extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontFriendMannagerAction.class);
	private static final long serialVersionUID = 1L;
	
	private RecommendUserService recommendUserService;
	
	//邀请好友列表
	private List<Map<String, Object>> recommendUserList;
	
	
	/**
	 * 加载邀请的好友列表
	 * @return
	 */
	public String FriendManagerInit(){
		//获取用户的信息
		try {
			User user = (User) session().getAttribute(IConstants.SESSION_USER);
			Long userId=user.getId();
			recommendUserService.queryfindRecommendUserPage(pageBean, userId);
			List<Map<String,String>>  list=pageBean.getPage();
			String userI = DesHelper.encrypt(userId.toString());
		    String uri=getPath();
		    
			request().setAttribute("url", uri);
			request().setAttribute("userId", userI);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	/**
	 * 加载邀请的好友列表
	 * @return
	 */
	public String FriendManagerList(){
		//获取用户的信息
		try {
			User user = (User) session().getAttribute(IConstants.SESSION_USER);
			Long userId=user.getId();
			recommendUserService.queryfindRecommendUserPage(pageBean, userId);
			List<Map<String,String>>  list=pageBean.getPage();
			String userI = DesHelper.encrypt(userId.toString());
		    String uri=getPath();
		    
			request().setAttribute("url", uri);
			request().setAttribute("userId", userI);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	

	public RecommendUserService getRecommendUserService() {
		return recommendUserService;
	}

	public void setRecommendUserService(RecommendUserService recommendUserService) {
		this.recommendUserService = recommendUserService;
	}

	public List<Map<String, Object>> getRecommendUserList() {
		return recommendUserList;
	}

	public void setRecommendUserList(List<Map<String, Object>> recommendUserList) {
		this.recommendUserList = recommendUserList;
	}



}
