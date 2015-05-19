package com.sp2p.action.front;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.JSONHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.User;
import com.sp2p.service.AssignmentDebtService;
import com.sp2p.service.FinanceService;
import com.sp2p.service.PublicModelService;
import com.sp2p.service.admin.BorrowManageService;


/**
 * 信息管理模块
 * 
 * @author Administrator
 * 
 */
public class FrontMessageAction extends BaseFrontAction {

	private static final long serialVersionUID = 1L;
	public static Log log = LogFactory.getLog(FrontMessageAction.class);
	private BorrowManageService borrowManageService;
	private PublicModelService publicModelService;
	private FinanceService financeService;
	private AssignmentDebtService assignmentDebtService;

	private List<Map<String, Object>> teamList;

	public List<Map<String, Object>> getTeamList() {
		return teamList;
	}

	public PublicModelService getPublicModelService() {
		return publicModelService;
	}

	public void setPublicModelService(PublicModelService publicModelService) {
		this.publicModelService = publicModelService;
	}

	public void setTeamList(List<Map<String, Object>> teamList) {
		this.teamList = teamList;
	}

	public String initMessage() {
		return SUCCESS;
	}

	/**
	 * 根据信息类型查询信息详情
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String getMessageBytypeId() throws Exception {
		Integer typeId = Convert.strToInt(request("typeId"), -1);
		request().setAttribute("typeId", typeId);
		paramMap = publicModelService.getMessageByTypeId(typeId);
		if (typeId == 1) {
			return "jkxy"; // 借款协议
		}
		if (typeId == 2) {
			return "gsjj"; // 公司简介
		} else if (typeId == 3) {
			return "ptyl"; // 平台原理
		} else if (typeId == 4) {
			return "gywm"; // 关于我们
		} else if (typeId == 5) {
			return "flzc"; // 法律政策
		} else if (typeId == 6) {
			return "zfsm"; // 资费说明
		} else if (typeId == 7) {
			return "lxwm"; // 联系我们
		} else if (typeId == 8) {// 如何理财
			return "rhlc";
		} else if (typeId == 9) {// 本金保障
			return "bjbz";
		} else if (typeId == 15) {// 借款协议
			return "jkxy";
		} else {
			return SUCCESS;
		}
	}

	public String queryProtocol() throws Exception{
		Integer typeId = Convert.strToInt(request("typeId"), -1);
		List<Map<String, Object>> investMaps = null;
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> mapContent = new HashMap<String, String>();
		Map<String, String> Content = new HashMap<String, String>();
		Map<String, String> sumMap = null;
		List<Map<String, Object>> user_invest_map = null;
		List<Map<String, Object>> borrow_map = null;
		// 得到session 对象
		long borrowId = Convert.strToLong(request("borrowId"), -1);
		long invest_id = Convert.strToLong(request("investId"), -1);
		long styles = Convert.strToLong(request("styles"), -1);
		User user = (User)session().getAttribute("user");
		long userId= user.getId();
		//校验用户是否有权限查看协议
		Map<String,String> authProtocolMap = borrowManageService.queryAuthProtocol(borrowId,invest_id);
		if(authProtocolMap == null){
			authProtocolMap = new HashMap<String, String>();
		}
		long investor = Convert.strToLong(authProtocolMap.get("investor"), -1);
		long borrower = Convert.strToLong(authProtocolMap.get("publisher"), -1);
		long oriInvestor = Convert.strToLong(authProtocolMap.get("oriInvestor"), -1);
		if(userId != investor && userId != borrower && userId !=oriInvestor){
			return "404";
		}
		if (typeId == 15 || typeId == 1) {
			
			sumMap = borrowManageService.queryBorrowSumMomeny(borrowId,
					invest_id);
			map = borrowManageService.queryBorrowMany(borrowId);
			investMaps = borrowManageService.queryInvestMomey(borrowId,
					invest_id);
			user_invest_map = borrowManageService.queryUsername(borrowId,
					invest_id);
			// 得到还款记录
			borrow_map = financeService.queryRepayment(borrowId);
			// 替换设定的参数值
			// 得到借款协议
			mapContent = publicModelService.getMessageByTypeId(1);
			Content = publicModelService.getMessageByTypeId(18);
			String map_cont = mapContent.get("content")+"";
			if(null != map_cont){
				map_cont = map_cont.replace("[corporation]", IConstants.PRO_GLOBLE_NAME+"");
			}
			mapContent.put("content", map_cont);

			String cont_cont = Content.get("content")+"";
			if(null != cont_cont){
				cont_cont = cont_cont.replace("[corporation]", IConstants.PRO_GLOBLE_NAME+"");
			}
			Content.put("content", cont_cont);
			request().setAttribute("mapContent", mapContent);
			request().setAttribute("investMaps", investMaps);
			request().setAttribute("contentMap", Content);
			request().setAttribute("map", map);
			request().setAttribute("sumMap", sumMap);
			request().setAttribute("styles", styles);
			request().setAttribute("borrow_map", borrow_map);
			request().setAttribute("user_invest_map", user_invest_map);
			return "jkxy";
		}
		if (typeId == 24) {// 债权转让协议
			paramMap = publicModelService.getMessageByTypeId(typeId);
			long aid = Convert.strToLong(request("aid"), -1);
			map = assignmentDebtService.queryDebtUserName(aid);
			if (map != null) {
				String cont_cont = Convert.strToStr(paramMap.get("content"), "");
				if(null != cont_cont){
					cont_cont = cont_cont.replace("[ppusername]", IConstants.PRO_GLOBLE_NAME+"");
					cont_cont = cont_cont.replace("[pusername]", Convert
						.strToStr(map.get("dbusername"), "未填写"));
					cont_cont = cont_cont.replace("[yusername]", Convert
						.strToStr(map.get("realName"), ""));
					cont_cont = cont_cont.replace("[endtime]", Convert
						.strToStr(map.get("auctionEndTime"), ""));
				}
				paramMap.put("content", cont_cont);
				// 得到借款Id
				borrowId = Convert.strToLong(map.get("borrowId"), -1);
				// 得到投资Id
				invest_id = Convert.strToLong(map.get("investId"), -1);
				sumMap = borrowManageService.queryBorrowSumMomeny(borrowId,invest_id);
				mapContent = borrowManageService.queryBorrowInfo(borrowId);
				SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
				Date date = dd.parse(mapContent.get("auditTime"));
				// 得到借款期限
				int isdayThe = Convert.strToInt(mapContent.get("isDayThe"),0);
				int deadline = Convert.strToInt(mapContent.get("deadline"),0);
				if (isdayThe == 1) {
					date = DateUtils.addMonths(date, deadline);    
				} else {
					date = DateUtils.addDays(date, deadline);
				}
				String stime = dd.format(date);
				mapContent.put("auditTime", stime);
				request().setAttribute("map", map);
				request().setAttribute("sumMap", sumMap);
				request().setAttribute("mapContent", mapContent);
			}
			return "zqjkxy";
		}
		return null;
	}
	
	/**
	 * 根据信息类型查询信息详情
	 * 
	 * @return
	 * @throws Exception
	 */
	public String qureygetMessageBytypeId() {
		Integer typeId = Convert.strToInt(request("typeId"), -1);
		try {
			Map<String, String> map = null;
			map = publicModelService.getMessageByTypeId(typeId);
			JSONHelper.printObject(map);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 分页查询团队介绍信息
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String listTeamPage() throws SQLException, DataException {
		try {
			teamList = publicModelService.findListTeam();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	public BorrowManageService getBorrowManageService() {
		return borrowManageService;
	}

	public void setBorrowManageService(BorrowManageService borrowManageService) {
		this.borrowManageService = borrowManageService;
	}

	public FinanceService getFinanceService() {
		return financeService;
	}

	public void setFinanceService(FinanceService financeService) {
		this.financeService = financeService;
	}

	public AssignmentDebtService getAssignmentDebtService() {
		return assignmentDebtService;
	}

	public void setAssignmentDebtService(
			AssignmentDebtService assignmentDebtService) {
		this.assignmentDebtService = assignmentDebtService;
	}

}
