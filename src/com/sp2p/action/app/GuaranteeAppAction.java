package com.sp2p.action.app;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.JSONHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.service.GuaranteeService;

public class GuaranteeAppAction extends BaseAppAction {
	public static Log log = LogFactory.getLog(GuaranteeAppAction.class);
	private GuaranteeService guaranteeService;

	public void setGuaranteeService(GuaranteeService guaranteeService) {
		this.guaranteeService = guaranteeService;
	}

	public String guaranteeaindexMethod() {
		return SUCCESS;
	}

	/**
	 * 点击投资人显示投资人的详细信息
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String userMegMethod() throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			Map<String, String> userMsg = null;
			Map<String,String> userVipPicture = null;
			Map<String,String> BorrowRecode = null;
			Map<String,String> inverseRecorde = null;
			Long userId = Convert.strToLong(infoMap.get("id"), -1);
			userVipPicture = guaranteeService.queryUserVipPicture(userId);
			userMsg = guaranteeService.queryUserInformation(userId);
			BorrowRecode = guaranteeService.queryUserBorrowRecode(userId);
			inverseRecorde = guaranteeService.queryUserInerseRecode(userId);
			jsonMap.put("userMsg", userMsg);
			jsonMap.put("userVipPicture", userVipPicture);
			jsonMap.put("BorrowRecode", BorrowRecode);
			jsonMap.put("inverseRecorde", inverseRecorde);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
			log.error(e);
		}
		return null;
	}

	/**
	 * 查看用户的信用详情
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String userCriditMethod() throws Exception {
		Map<String, String> userMsg = null;
		Map<String, String> map = null;
		Map<String,String> criditMap = null;
		Map<String,String> userVipPicture = null;
		Map<String,String> BorrowRecode = null;
		Map<String,String> inverseRecorde = null;
		Map<String,String> UserBorrowmap1 = null;
		Map<String,String> UserBorrowmap2 = null;
		Map<String,String> UserBorrowmap3 = null;
		Map<String,String> UserBorrowmap4 = null;
		Map<String,String> UserBorrowmap5 = null;
		Map<String,String> UserBorrowmap6 = null;
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			Map<String, String> authMap = this.getAppAuthMap();
			Long userId = Convert.strToLong(authMap.get("uid"), -1);
			if (userId == null || userId == -1) {
				jsonMap.put("error", "2");
				jsonMap.put("msg", "请先登陆");
				JSONHelper.printObject(jsonMap);
				return null;
			}
			criditMap = guaranteeService.queryUserCriditPicture(userId);
			userMsg = guaranteeService.queryUserInformation(userId);
			userVipPicture = guaranteeService.queryUserVipPicture(userId);
			map = guaranteeService.queryPerUserCreditfornt(userId);
			BorrowRecode = guaranteeService.queryUserBorrowRecode(userId);
			inverseRecorde = guaranteeService.queryUserInerseRecode(userId);
			if(map != null && map.size() > 0){
				map.put("tpcriditing", "0");
				int criditing = 0;
				if(map.get("tpauditStatus").equals("3")){
					map.put("tpcriditing", "10");
				}
				if(map.get("twauditStatus").equals("3")){
					criditing += 10;
				}
				if(map.get("twdirectedStatus").equals("3")){
					criditing += 10;
				}
				if(map.get("twotherStatus").equals("3")){
					criditing += 10;
				}
				if(map.get("twmoredStatus").equals("3")){
					criditing += 10;
				}
				map.put("twcriditing", criditing+"");
			}
			//=====统计还款分数
			UserBorrowmap1 = guaranteeService.queryUserBorrowAndInver15(userId);
			UserBorrowmap2 = guaranteeService.queryUserBorrowAndInver16(userId);
			UserBorrowmap3 = guaranteeService.queryUserBorrowAndInver10(userId);
			UserBorrowmap4 = guaranteeService.queryUserBorrowAndInver30(userId);
			UserBorrowmap5 = guaranteeService.queryUserBorrowAndInver90(userId);
			UserBorrowmap6 = guaranteeService.queryUserBorrowAndInver90up(userId);
			map.put("creditrating", userMsg.get("creditrating"));
			jsonMap.put("UserBorrowmap1", UserBorrowmap1);
			jsonMap.put("UserBorrowmap2", UserBorrowmap2);
			jsonMap.put("UserBorrowmap3", UserBorrowmap3);
			jsonMap.put("UserBorrowmap4", UserBorrowmap4);
			jsonMap.put("UserBorrowmap5", UserBorrowmap5);
			jsonMap.put("UserBorrowmap6", UserBorrowmap6);
			//=========
			jsonMap.put("userMsg", userMsg);
			jsonMap.put("map", map);
			jsonMap.put("criditMap", criditMap);
			jsonMap.put("userVipPicture", userVipPicture);
			jsonMap.put("BorrowRecode", BorrowRecode);
			jsonMap.put("inverseRecorde", inverseRecorde);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(jsonMap);
			log.error(e);
		}
		return null;
	}

	/**
	 * 查看用户的认证
	 * 
	 * @return
	 * @throws Exception 
	 */
	public String userRenRenMethod() throws Exception {
		/*Map<String,String> criditMap = null;
		Map<String,String> UserBorrowmap1 = null;
		Map<String,String> UserBorrowmap2 = null;
		Map<String,String> UserBorrowmap3 = null;
		Map<String,String> UserBorrowmap4 = null;
		Map<String,String> UserBorrowmap5 = null;
		Map<String,String> UserBorrowmap6 = null;
		Map<String, String> userMsg = null;
		Map<String, String> map = null;
		Map<String, String> creditmap = null;
		List<Integer> typeList = new ArrayList<Integer>();
		Long userId = -1L;
		User user = (User) session().getAttribute("user");
		if (user == null) {
			return LOGIN;
		}
		userId = user.getId();

		userMsg = guaranteeService.queryUserInformation(userId);
		map = guaranteeService.queryPerUserCreditfornt(userId);
		//=====统计还款分数
		UserBorrowmap1 = guaranteeService.queryUserBorrowAndInver15(userId);
		UserBorrowmap2 = guaranteeService.queryUserBorrowAndInver16(userId);
		UserBorrowmap3 = guaranteeService.queryUserBorrowAndInver10(userId);
		UserBorrowmap4 = guaranteeService.queryUserBorrowAndInver30(userId);
		UserBorrowmap5 = guaranteeService.queryUserBorrowAndInver90(userId);
		UserBorrowmap6 = guaranteeService.queryUserBorrowAndInver90up(userId);
		criditMap = guaranteeService.queryUserCriditPicture(userId);
		request().setAttribute("UserBorrowmap1", UserBorrowmap1);
		request().setAttribute("UserBorrowmap2", UserBorrowmap2);
		request().setAttribute("UserBorrowmap3", UserBorrowmap3);
		request().setAttribute("UserBorrowmap4", UserBorrowmap4);
		request().setAttribute("UserBorrowmap5", UserBorrowmap5);
		request().setAttribute("UserBorrowmap6", UserBorrowmap6);
		request().setAttribute("criditMap", criditMap);
		//=========
		request().setAttribute("userMsg", userMsg);
		request().setAttribute("map", map);*/
		String flag = request().getParameter("flag");
		request().setAttribute("flag", flag);//区别从什么地方访问的
		return SUCCESS;
	}

	/**
	 * 查询用户的关注用户列表
	 * 
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public String queryUserFriendList() throws SQLException, Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			String id = infoMap.get("uid");
			String attention = infoMap.get("attention");
			pageBean.setPageNum(infoMap.get("curPage"));
			if (StringUtils.isNotBlank(id)) {
				Long userId = Convert.strToLong(id, -1L);
				guaranteeService.queryUserFriends(pageBean, userId);
			}
			if(StringUtils.isNotBlank(attention)){
				jsonMap.put("attention", attention);
				jsonMap.put("id", id);
				jsonMap.put("pageBean", pageBean);
				jsonMap.put("error", "-1");
				jsonMap.put("msg", "查询好友成功");
				JSONHelper.printObject(jsonMap);				
			}
			jsonMap.put("attention", attention);
			jsonMap.put("id", id);
			jsonMap.put("pageBean", pageBean);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询他关注的成功");
			JSONHelper.printObject(jsonMap);
		} catch (IOException e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(paramMap);
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 删除关注好友
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws Exception
	 */
	public String deleteUserFriend()throws SQLException,DataException,Exception{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			Map<String, String> authMap = this.getAppAuthMap();
			Long userId=Convert.strToLong(authMap.get("uid"), -1L);
			Long attentionUserId=Convert.strToLong(infoMap.get("attentionId"),-1L);
			guaranteeService.DeteleUserFriends(attentionUserId, userId);
			infoMap.put("attention", "attention");
			infoMap.put("uid", userId.toString());
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "成功");
			JSONHelper.printObject(jsonMap);
			queryUserFriendList();
		} catch (IOException e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(paramMap);
			log.error(e);
		}
		return null;
	}
	/**
	 * 查询用户的借款列表
	 * @return
	 * @throws DataException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public String queryUserBorrowLists() throws Exception{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			String userIdStr = infoMap.get("id");
			if(StringUtils.isNotBlank(userIdStr)){
				request().setAttribute("id", userIdStr);
				Long userId = Convert.strToLong(userIdStr,-1L);
			//myborrowlist = guaranteeService.queryMyBorrowList(userId);
				guaranteeService.queryMyBorrowList(pageBean, userId);
			}
			jsonMap.put("pageBean", pageBean);
			jsonMap.put("id", userIdStr);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(paramMap);
			log.error(e);
		}
		return null;
	}
	/**
	 * 查询用户的投资记录
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException 
	 */
	public String queryMyborrowRecorde() throws Exception{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			String userIdStr = infoMap.get("id");
			if(StringUtils.isNotBlank(userIdStr)){
				Long userId = Convert.strToLong(userIdStr,-1L);
			//myborrowlist = guaranteeService.queryMyBorrowList(userId);
				guaranteeService.queryMyborrowRecorde(pageBean, userId);
			}
			jsonMap.put("pageBean", pageBean);
			jsonMap.put("id", userIdStr);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(paramMap);
			log.error(e);
		}
		return null;
	}
	
	/**
	 * 查询用户动态
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws IOException 
	 */
	public String queryUserRecore() throws Exception{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			Long userId = Convert.strToLong(infoMap.get("id"),-1L);
			guaranteeService.queryUserRecore(pageBean, userId);
			jsonMap.put("pageBean", pageBean);
			jsonMap.put("id", userId);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(paramMap);
			log.error(e);
		}
		return null;
	}
	/**
	 * 查询用户好友的动态
	 * @return
	 * @throws DataException 
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public String queryfrendsRecore() throws Exception{
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			Map<String, String> infoMap = this.getAppInfoMap();
			Long userId = Convert.strToLong(infoMap.get("id"),-1L);
			if(userId!=null&&userId!=-1){
				guaranteeService.queryfrendsRecore(pageBean, userId);
			}
			jsonMap.put("pageBean", pageBean);
			jsonMap.put("id", userId);
			jsonMap.put("error", "-1");
			jsonMap.put("msg", "查询成功");
			JSONHelper.printObject(jsonMap);
		} catch (Exception e) {
			jsonMap.put("error", "1");
			jsonMap.put("msg", "未知异常");
			JSONHelper.printObject(paramMap);
			log.error(e);
		}
		return null;
	}
	
	
	
	

}
