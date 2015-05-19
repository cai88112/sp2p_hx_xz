/**
 * 
 */
package com.sp2p.action.admin;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.sp2p.service.UserService;
import com.shove.Convert;
import com.shove.web.action.BasePageAction;
import com.sp2p.service.ExperiencemoneyService;

/**
 * 体验金查询接口.
 * 
 * @author 殷梓淞
 *
 */
public class ExperiencemoneyAction extends BasePageAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(ExperiencemoneyAction.class);

	private ExperiencemoneyService experiencemoneyService;
	private UserService userService;

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	public String queryExperiencemoneyPageAll() {
		log.info("进入查询所有方法queryExperiencemoneyPageAll");
		try {
			experiencemoneyService.queryExperiencemoneyPageAll(pageBean);
			List<Map<String, Object>> list = pageBean.getPage();
			if (list != null) {
				for (Map<String, Object> map : list) {
					long userid = (Long) map.get("userid");
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
		}
		log.info("退出查询所有方法queryExperiencemoneyPageAll");
		return SUCCESS;
	}
	
	/**
	 * 初始化.
	 * @return
	 */
	public String experiencemoneyPageInit() {
		log.info("初始化页面experiencemoneyPageInit");
		return SUCCESS;
	}
	

	/**
	 * 查找分页列表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String queryExperiencemoneyPage() throws Exception {
		try {
			experiencemoneyService.queryExperiencemoneyPageAll(pageBean);
			List<Map<String, Object>> list = pageBean.getPage();
			if (list != null) {
				for (Map<String, Object> map : list) {
					long userid = (Long) map.get("userid");
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public ExperiencemoneyService getExperiencemoneyService() {
		return experiencemoneyService;
	}

	public void setExperiencemoneyService(
			ExperiencemoneyService experiencemoneyService) {
		this.experiencemoneyService = experiencemoneyService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
