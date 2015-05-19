package com.sp2p.action.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.fp2p.helper.shove.SMSHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.security.Encrypt;
import com.shove.web.action.BasePageAction;
import com.sp2p.constants.IAmountConstants;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.entity.User;
import com.sp2p.service.AwardMoneyService;
import com.sp2p.service.BeVipService;
import com.sp2p.service.IDCardValidateService;
import com.sp2p.service.PublicModelService;
import com.sp2p.service.RecommendUserService;
import com.sp2p.service.RegionService;
import com.sp2p.service.UserService;
import com.sp2p.service.ValidateService;
import com.sp2p.service.admin.SMSInterfaceService;
import com.sp2p.task.JobTaskService;


@SuppressWarnings("unchecked")
public class UserAction extends BasePageAction {
	public static Log log = LogFactory.getLog(UserAction.class);
	private static final long serialVersionUID = 1L;

	public UserService userService;
	private RegionService regionService;
	private ValidateService validateService;
	private AwardMoneyService awardMoneyService;
	private SMSInterfaceService sMsService;
	private RecommendUserService recommendUserService;
	@SuppressWarnings("unused")
	private IDCardValidateService iDCardValidateService;
	private BeVipService beVipService;
	// add by houli vip扣费处理
	private JobTaskService jobTaskService;
	//
	private List<Map<String, Object>> provinceList;
	private List<Map<String, Object>> cityList;
	private List<Map<String, Object>> regcityList;
	private List<Map<String, Object>> areaList;
	private List<Map<String, Object>> typeList;//
	private long workPro = -1L;// 初始化省份默认值
	private long cityId = -1L;// 初始化话默认城市
	private long regPro = -1L;// 户口区域默认值
	private long regCity = -1L;// 户口区域默认值
	private Map<String, String> maps;

	private PublicModelService agreementService;

	public PublicModelService getAgreementService() {
		return agreementService;
	}

	public void setAgreementService(PublicModelService agreementService) {
		this.agreementService = agreementService;
	}

	public void setBeVipService(BeVipService beVipService) {
		this.beVipService = beVipService;
	}

	public void setIDCardValidateService(
			IDCardValidateService cardValidateService) {
		iDCardValidateService = cardValidateService;
	}

	public Map<String, String> getMaps() throws Exception {
		maps = userService.queryPersonById(62);
		return maps;
	}

	public AwardMoneyService getAwardMoneyService() {
		return awardMoneyService;
	}

	public void setAwardMoneyService(AwardMoneyService awardMoneyService) {
		this.awardMoneyService = awardMoneyService;
	}

	public RecommendUserService getRecommendUserService() {
		return recommendUserService;
	}

	public void setRecommendUserService(
			RecommendUserService recommendUserService) {
		this.recommendUserService = recommendUserService;
	}

	public List<Map<String, Object>> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<Map<String, Object>> provinceList) {
		this.provinceList = provinceList;
	}

	public List<Map<String, Object>> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<Map<String, Object>> typeList) {
		this.typeList = typeList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public void setAreaList(List<Map<String, Object>> areaList) {
		this.areaList = areaList;
	}

	public SMSInterfaceService getSMsService() {
		return sMsService;
	}

	public void setSMsService(SMSInterfaceService msService) {
		sMsService = msService;
	}

	// ======地区列表
	public String ajaxqueryRegion() throws Exception {
		// Long regionId = Convert.strToLong(paramMap.get("regionId"), -1);
		Long parentId = Convert.strToLong(request("parentId"), -1);
		Integer regionType = Convert.strToInt(request("regionType"), -1);
		List<Map<String, Object>> list;
		try {
			list = regionService.queryRegionList(-1L, parentId, regionType);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		String jsonStr = JSONArray.toJSONString(list);
		ServletHelper.returnStr(ServletActionContext.getResponse(), jsonStr);
		return null;
	}

	/**
	 * 上传页面点击上传按钮 展示用户图片和上传
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserPictur() throws Exception {
		String tmidStr = SqlInfusionHelper.filteSqlInfusion(request().getParameter(
				"dm"));
		// ------add by houli
		String btype = SqlInfusionHelper.filteSqlInfusion(request("btype"));
		// -----------
		List<Map<String, Object>> userPicDate = null;
		Map<String, String> typmap = null;
		User user = null;
		int len = 0;// 集合的大小
		@SuppressWarnings("unused")
		Long materAuthTypeId = null;
		if (StringUtils.isNotBlank(tmidStr)) {
			Long tmid = Convert.strToLong(tmidStr, -1L);
			request().setAttribute("tmid", tmid);
			userPicDate = userService.queryPerTyhpePicture(tmid);
			len = userPicDate.size();
			user = (User) session().getAttribute("user");
			if (user != null) {
				Long userId = user.getId();
				typmap = userService.queryPitcturTyep(userId, tmid);
			}
		}
		request().setAttribute("len", len);
		request().setAttribute("userPicDate", userPicDate);
		request().setAttribute("typmap", typmap);
		request().setAttribute("tmidStr", tmidStr);
		// ----add by houli
		if (btype != null) {
			request().setAttribute("btype", btype);
		}
		return SUCCESS;
	}

	// 用户提交图片审核
	public String addpastPicturdate() throws Exception {
		Long tmid = Convert.strToLong(paramMap.get("tmid"), -1L);// materAuth的id
		Long materAuthTypeId = Convert.strToLong(paramMap
				.get("materAuthTypeId"), -1L);
		;// 证件类型
		Integer len = Convert.strToInt(paramMap.get("len"), -1);// 上传图片个数
		Integer Listlen = Convert.strToInt(paramMap.get("listlen"), -1);// 数据库的图片个数
		Long tmids = Convert.strToLong(paramMap.get("tmidStr"), -1L);
		Long result = -1L;
		if (Listlen == -1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "17");
			return null;
		}
		if (len == -1) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "18");
			return null;
		}
		Integer allPicturecount = len + Listlen;// 用户将要上传的图片和数据库图片的个数的总和
		if (materAuthTypeId == 1) {
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				return null;
			}// 身份证
		}
		if (materAuthTypeId == 2) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}// 工作认证
		}
		if (materAuthTypeId == 3) {
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "3");
				return null;
			}// 居住认证
		}

		if (materAuthTypeId == 4) {
			if (30 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "4");
				return null;
			}// 信用报告
		}
		if (materAuthTypeId == 5) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "5");
				return null;
			}// 收入认证
		}
		if (materAuthTypeId == 6) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "6");
				return null;
			}// 房产
		}
		if (materAuthTypeId == 7) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "7");
				return null;
			}// 购车
		}
		if (materAuthTypeId == 8) {
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "8");
				return null;
			}// 结婚
		}
		if (materAuthTypeId == 9) {
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "9");
				return null;
			}// 学历
		}
		if (materAuthTypeId == 10) {
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "10");
				return null;
			}// 技术
		}
		if (materAuthTypeId == 11) {
			// 手机
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "11");
				return null;
			}// 手机
		}
		if (materAuthTypeId == 12) {
			if (5 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "12");
				return null;
			}// 微博
		}
		if (materAuthTypeId == 13) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "14");
				return null;
			}// 现场认证
		}
		if (materAuthTypeId == 14) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "15");
				return null;
			}// 抵押认证
		}

		if (materAuthTypeId == 15) {
			if (10 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "16");
				return null;
			}// 机构担保
		}

		if (materAuthTypeId == 16) {
			if (30 < allPicturecount) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "160");
				return null;
			}// 其他资料
		}

		User user = (User) session().getAttribute("user");// 获取user实体
		List<Long> lists = new ArrayList<Long>();// 已经上传的图片设置他们的可见性
		if (Listlen != -1 && user != null) {
			for (int i = 1; i <= Listlen; i++) {
				if (Convert.strToInt(paramMap.get("id" + i), -1) != -1) {
					lists.add(Convert.strToLong(paramMap.get("id" + i), -1));
				}
			}
		}

		List<String> imglistsy = new ArrayList<String>();
		List<String> imgListsn = new ArrayList<String>();
		if (len != -1 && user != null) {
			for (int i = 1; i <= len; i++) {// 将要上传图片图片先保存在一个数组里面
				if (Convert.strToStr(paramMap.get("ids" + i), null) != null) {
					// 处理传来的图片值
					String temppicture = Convert.strToStr(paramMap.get("ids"
							+ i), "");
					int v = temppicture.indexOf(".v");
					if (v == -1) {
						imgListsn.add(temppicture);
					} else {
						// 这个是保存可见的picture
						imglistsy.add(temppicture.substring(0, v));
					}
				}
			}
		}
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String uploadingTime = format.format(new Date());// 当前时间上传图片时间
		if (user != null && tmid != -1L && materAuthTypeId != -1L) {
			Long userId = user.getId();
			result = userService.addUserImage(1, uploadingTime, lists,
					imglistsy, imgListsn, tmid, userId, materAuthTypeId, tmids,
					len);// 遍历将image查到数据库中 1 表示向t_materialsauth 插入图片类型 表示等待审核
			// Admin admin = (Admin)
			// session().getAttribute(IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_materialimagedetal", user
					.getUserName(), IConstants.UPDATE, user.getLastIP(), 0,
					"用户提交图片审核", 1);

			if (result > 0) {
				int step = userService.queryStepById(userId);
				// 修改认证步骤
				if (materAuthTypeId >= 5 && step >= 5) {
					result = userService.updateAuthStep(userId.toString(), 6);
					if (result == 1) {
						user.setAuthStep(6);
					}
				}
				// 更新User的状态
				try {
					Map<String, String> newstatusmap = null;
					newstatusmap = userService.querynewStatus(userId);// 查询放到session中去
					if (newstatusmap != null && newstatusmap.size() > 0) {
						user.setAuthStep(Convert.strToInt(newstatusmap
								.get("authStep"), -1));
						user.setEmail(Convert.strToStr(newstatusmap
								.get("email"), null));
						user.setPassword(Convert.strToStr(newstatusmap
								.get("password"), null));
						user.setId(Convert.strToLong(newstatusmap.get("id"),
								-1L));
						user.setRealName(Convert.strToStr(newstatusmap
								.get("realName"), null));
						user.setKefuname(Convert.strToStr(newstatusmap
								.get("kefuname"), null));
						user.setUserName(Convert.strToStr(newstatusmap
								.get("username"), null));
						user.setVipStatus(Convert.strToInt(newstatusmap
								.get("vipStatus"), -1));
						user.setKefuid(Convert.strToInt(newstatusmap
								.get("tukid"), -1));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				ServletHelper.returnStr(ServletActionContext.getResponse(), "123");
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "321");
				return null;
			}
		}
		return null;
	}

	/**
	 * 更新用户的图片是否可见
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updatevisiable() throws Exception {
		int len = Convert.strToInt(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("len")), -1);
		Long tmid = Convert.strToLong(paramMap.get("tmidStr"), -1L);
		List<Long> lists = new ArrayList<Long>();
		User user = (User) session().getAttribute("user");
		if (len != -1 && user != null) {
			for (int i = 1; i <= len; i++) {
				if (Convert.strToInt(paramMap.get("id" + i), -1) != -1) {
					lists.add(Convert.strToLong(paramMap.get("id" + i), -1));
				}
			}
		}
		userService.updatevisiable(tmid, lists);// 将传来的明细id设置为可见
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_materialimagedetal", admin
				.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
				"更新用户的图片的可见性", 2);

		return null;
	}

	/**
	 * 添加用户初始化
	 * 
	 * @return String
	 * @throws
	 */
	public String addUserInit() {
		paramMap.put("balances", "0");
		paramMap.put("enable", "1");
		paramMap.put("gender", "1");
		return SUCCESS;
	}

	/**
	 * 添加用户
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String addUser() throws Exception {
		try {
			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			String email = SqlInfusionHelper.filteSqlInfusion(paramMap.get("email")); // 电子邮箱
			String userName = SqlInfusionHelper.filteSqlInfusion(paramMap
					.get("userName")); // 用户名
			Long result = userService.isExistEmailORUserName(null, userName);
			if (result > 0) {
				this.addFieldError("paramMap['userName']", "该用户已注册！");
				return INPUT;
			}
			result = userService.isExistEmailORUserName(email, null);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_user", admin.getUserName(),
					IConstants.INSERT, admin.getLastIP(), 0, "添加新用户", 2);
			if (result > 0) {
				this.addFieldError("paramMap['email']", "该邮箱已注册！");
				return INPUT;
			}
			// userService.addUser(email, userName, password, name, gender);
		} catch (SQLException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (DataException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 添加认证图片
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addImage() throws Exception {
		Map<String, String> Apcmap = null;// 五大基本资料的计数存放map
		long materAuthTypeId = Convert.strToLong(paramMap
				.get("materAuthTypeId")
				+ "", -1L);
		String imgPath = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("userHeadImg"));

		if (StringUtils.isBlank(imgPath)) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "-1");
			return null;
		}

		long imageId = -1L;
		long userId = -1L;
		User user = (User) session().getAttribute("user");
		// 认证状态
		if (null != user) {
			userId = user.getId();

			imageId = userService.addImage(materAuthTypeId, imgPath, userId);
			Admin admin = (Admin) session().getAttribute(
					IConstants.SESSION_ADMIN);
			operationLogService.addOperationLog("t_materialsauth", admin
					.getUserName(), IConstants.INSERT, admin.getLastIP(), 0,
					"添加认证图片", 2);

			user = userService.jumpToWorkData(userId);// 2表示工作认证步骤 2 工作认证 1
			// 基础资料认证 3 vip认证 //5表示通过资料上传验证
			session().removeAttribute("user");
			session().setAttribute("user", user);
		}
		if (imageId < 0) {
			ServletHelper.returnStr(ServletActionContext.getResponse(), "0");
			return null;
		} else {
			Integer allcount = 0;
			Apcmap = userService.queryPicturStatuCount(user.getId());
			if (Apcmap != null && Apcmap.size() > 0) {
				allcount = Convert.strToInt(Apcmap.get("ccc"), 0);
			}
			if (allcount != 0 && allcount >= 5) {
				response().setCharacterEncoding("UTF-8");
				response().setContentType("text/html; charset=UTF-8");
				PrintWriter out = response().getWriter();
				out.print("addBorrowInit.do?t="
						+ session().getAttribute("borrowWay"));
				out.flush();
				out.close();
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");// 不给跳转
				return null;
			}
		}
	}

	/**
	 * 添加用户基础资料
	 * 
	 * @return
	 * @throws Exception
	 */
	public String addUserBaseData() throws Exception {
		JSONObject json = new JSONObject();
		String realName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("realName"));// 真实姓名
		if (StringUtils.isBlank(realName)) {
			json.put("msg", "请正确填写真实名字");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > realName.length() || 20 < realName.length()) {
			json.put("msg", "真实姓名的长度为不小于2和大于20");
			JSONHelper.printObject(json);
			return null;
		}

		String cellPhone = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("cellPhone"));// 手机号码
		if (StringUtils.isBlank(cellPhone)) {
			json.put("msg", "请正确填写手机号码");
			JSONHelper.printObject(json);
			return null;
		} else if (cellPhone.length() < 9 || cellPhone.length() > 15) {
			json.put("msg", "手机号码长度不对");
			JSONHelper.printObject(json);
			return null;
		}

		String phonecode = null;
		Object objcet = session().getAttribute("phone");
		// 测试--跳过验证码
		if (IConstants.ISDEMO.equals("1")) {

		} else {
			if (objcet != null) {
				phonecode = objcet.toString();
			} else {
				json.put("msg", "请输入正确的验证码");
				JSONHelper.printObject(json);
				return null;
			}
			if (phonecode != null) {
				if (!phonecode.trim().equals(cellPhone.trim())) {
					json.put("msg", "与获取验证码手机号不一致");
					JSONHelper.printObject(json);
					return null;
				}
			}
		}
		// 验证码
		String vilidataNum = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("vilidataNum"));
		if (StringUtils.isBlank(vilidataNum)) {
			json.put("msg", "请填写验证码");
			JSONHelper.printObject(json);
			return null;
		}
		String randomCode = null;
		Object obje = session().getAttribute("randomCode");
		// 测试--跳过验证码
		if (IConstants.ISDEMO.equals("1")) {

		} else {
			if (obje != null) {
				randomCode = obje.toString();
			} else {
				json.put("msg", "请输入正确的验证码");
				JSONHelper.printObject(json);
				return null;
			}
			if (randomCode != null) {
				if (!randomCode.trim().equals(vilidataNum.trim())) {

					json.put("msg", "请输入正确的验证码");
					JSONHelper.printObject(json);
					return null;
				}
			}
		}

		String sex = SqlInfusionHelper.filteSqlInfusion(paramMap.get("sex"));// 性别(男
																		// 女)
		if (StringUtils.isBlank(sex)) {
			json.put("msg", "请正确填写性别");
			JSONHelper.printObject(json);
			return null;
		}

		String birthday = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("birthday"));// 出生日期
		if (StringUtils.isBlank(birthday)) {
			json.put("msg", "请正确填写出生日期");
			JSONHelper.printObject(json);
			return null;
		}

		String highestEdu = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("highestEdu"));// 最高学历
		if (StringUtils.isBlank(highestEdu)) {
			json.put("msg", "请正确填写最高学历");
			JSONHelper.printObject(json);
			return null;
		}

		String eduStartDay = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("eduStartDay"));// 入学年份
		if (StringUtils.isBlank(eduStartDay)) {
			json.put("msg", "请正确填写入学年份");
			JSONHelper.printObject(json);
			return null;
		}

		String school = SqlInfusionHelper.filteSqlInfusion(paramMap.get("school"));// 毕业院校
		if (StringUtils.isBlank(school)) {
			json.put("msg", "请正确填写入毕业院校");
			JSONHelper.printObject(json);
			return null;
		}

		String maritalStatus = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("maritalStatus"));// 婚姻状况(已婚 未婚)
		if (StringUtils.isBlank(maritalStatus)) {
			json.put("msg", "请正确填写入婚姻状况");
			JSONHelper.printObject(json);
			return null;
		}

		String hasChild = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("hasChild"));// 有无子女(有 无)

		if (StringUtils.isBlank(hasChild)) {
			json.put("msg", "请正确填写入有无子女");
			JSONHelper.printObject(json);
			return null;
		}
		String hasHourse = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("hasHourse"));// 是否有房(有 无)
		if (StringUtils.isBlank(hasHourse)) {
			json.put("msg", "请正确填写入是否有房");
			JSONHelper.printObject(json);
			return null;
		}

		String hasHousrseLoan = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("hasHousrseLoan"));// 有无房贷(有 无)
		if (StringUtils.isBlank(hasHousrseLoan)) {
			json.put("msg", "请正确填写入有无房贷");
			JSONHelper.printObject(json);
			return null;
		}

		String hasCar = SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasCar"));// 是否有车
																				// (有
																				// 无)
		if (StringUtils.isBlank(hasCar)) {
			json.put("msg", "请正确填写入是否有车");
			JSONHelper.printObject(json);
			return null;
		}

		String hasCarLoan = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("hasCarLoan"));// 有无车贷 (有 无)
		if (StringUtils.isBlank(hasCarLoan)) {
			json.put("msg", "请正确填写入有无车贷");
			JSONHelper.printObject(json);
			return null;
		}
		Long nativePlacePro = Convert.strToLong(paramMap.get("nativePlacePro"),
				-1);// 籍贯省份(默认为-1)
		if (StringUtils.isBlank(nativePlacePro.toString())) {
			json.put("msg", "请正确填写入籍贯省份");
			JSONHelper.printObject(json);
			return null;
		}
		Long nativePlaceCity = Convert.strToLong(paramMap
				.get("nativePlaceCity"), -1);// 籍贯城市 (默认为-1)
		if (StringUtils.isBlank(nativePlaceCity.toString())) {
			json.put("msg", "请正确填写入籍贯城市");
			JSONHelper.printObject(json);
			return null;
		}

		Long registedPlacePro = Convert.strToLong(paramMap
				.get("registedPlacePro"), -1);// 户口所在地省份(默认为-1)
		if (StringUtils.isBlank(registedPlacePro.toString())) {
			json.put("msg", "请正确填写入户口所在地省份");
			JSONHelper.printObject(json);
			return null;
		}

		Long registedPlaceCity = Convert.strToLong(paramMap
				.get("registedPlaceCity"), -1);// 户口所在地城市(默认为-1)

		if (StringUtils.isBlank(registedPlaceCity.toString())) {
			json.put("msg", "请正确填写入户口所在地城市");
			JSONHelper.printObject(json);
			return null;
		}

		String address = SqlInfusionHelper.filteSqlInfusion(paramMap.get("address"));// 所在地
		if (StringUtils.isBlank(address)) {
			json.put("msg", "请正确填写入所在地");
			JSONHelper.printObject(json);
			return null;
		}

		String telephone = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("telephone"));// 居住电话
		if (StringUtils.isBlank(telephone)) {
			json.put("msg", "请正确填写入你的家庭电话");
			JSONHelper.printObject(json);
			return null;
		}

		/* 用户头像 */
		String personalHead = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("personalHead"));// 个人头像 (默认系统头像)
		if (StringUtils.isBlank(personalHead)) {
			/*
			 * json.put("msg", "请正确填写入所在地"); JSONUtils.printObject(json); return
			 * null;
			 */
			personalHead = null;
		}

		// 用户Id
		// String userId = session("");//用户Id
		User user = (User) session("user");
		/*
		 * 测试 long userId = -1L; userId = user.getId(); if(userId < 0){
		 * json.put("msg", "超时，请重新登录"); JSONUtils.printObject(json); return
		 * null; }
		 */
		String idNo = SqlInfusionHelper.filteSqlInfusion(paramMap.get("idNo"));// 身份证号码
		long len = idNo.length();
		if (StringUtils.isBlank(idNo)) {
			json.put("msg", "请正确身份证号码");
			JSONHelper.printObject(json);
			return null;
		} else if (15 != len) {
			if (18 == len) {
			} else {
				json.put("msg", "请正确身份证号码");
				JSONHelper.printObject(json);
				return null;
			}
		}

		long personId = -1L;

		personId = userService.userBaseData(realName, cellPhone, sex, birthday,
				highestEdu, eduStartDay, school, maritalStatus, hasChild,
				hasHourse, hasHousrseLoan, hasCar, hasCarLoan, nativePlacePro,
				nativePlaceCity, registedPlacePro, registedPlaceCity, address,
				telephone, personalHead, user.getId(), idNo);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (personId > 0) {
			session().removeAttribute("randomCode");
			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.INSERT, admin.getLastIP(),
					0, "添加用户基础资料成功", 2);
			json.put("msg", "保存成功");
			JSONHelper.printObject(json);
			return null;
			// 成功
		} else {
			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.INSERT, admin.getLastIP(),
					0, "添加用户基础资料失败", 2);
			json.put("msg", "保存失败");
			// 失败
			JSONHelper.printObject(json);
			return null;
		}
	}

	/**
	 * 审核基础资料
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updateUserBaseDataCheck() throws Exception {
		JSONObject json = new JSONObject();

		long personId = -1L;
		int auditStatus = 1;// 默认不通过审核
		long userId = Convert.strToLong(paramMap.get("id"), -1l);
		long flag = -1L;
		String statuss = null;
		if (StringUtils.isNotBlank(paramMap.get("flag"))) {
			flag = Long.parseLong(paramMap.get("flag"));
		}
		if (flag == 3) {
			auditStatus = 3;// 通过审核
			statuss = "通过审核";
		} else if (flag == 2) {
			auditStatus = 2;// 审核不通过
			statuss = "审核不通过";
		} else {
			auditStatus = 1;// 等待审核
			statuss = "等待审核";
		}
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		long adminId = admin.getId();
		if (admin != null) {
			personId = userService.updateUserBaseDataCheck(userId, auditStatus,
					adminId);

			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "审核基础资料，" + statuss, 2);

		}
		// 测试---跳过
		if (IConstants.ISDEMO.equals("1")) {
			json.put("msg", "保存成功");
			JSONHelper.printObject(json);
			return null;
		}
		if (personId > 0) {
			json.put("msg", "保存成功");
			JSONHelper.printObject(json);
			return null;
			// 成功
		} else {
			json.put("msg", "保存失败");
			// 失败
			JSONHelper.printObject(json);
			return null;
		}
	}

	/**
	 * 前台借款模块填写基本信息
	 * 
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws DataException
	 * @throws NumberFormatException
	 * @throws ParseException
	 */
	public String updateUserBaseData() throws Exception {
		User user = (User) session("user");
		JSONObject json = new JSONObject();
		if (user == null) {
			json.put("msg", "超时请重新登录");
			JSONHelper.printObject(json);
			return null;
		}
		
		String realName = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("realName"));// 真实姓名
		if (StringUtils.isBlank(realName)) {
			json.put("msg", "请正确填写真实名字");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > realName.length() || 20 < realName.length()) {
			json.put("msg", "真实姓名的长度为不小于2和大于20");
			JSONHelper.printObject(json);
			return null;
		}

		String idNo = SqlInfusionHelper.filteSqlInfusion(paramMap.get("idNo"));// 身份证号码
		long len = idNo.length();
		if (StringUtils.isBlank(idNo)) {
			json.put("msg", "请正确身份证号码");
			JSONHelper.printObject(json);
			return null;
		} else if (15 != len) {
			if (18 == len) {
			} else {
				json.put("msg", "请正确身份证号码");
				JSONHelper.printObject(json);
				return null;
			}
		}
		// 验证身份证
		int sortCode = 0;
		int MAN_SEX = 0;
		if (len == 15) {
			sortCode = Integer.parseInt(idNo.substring(12, 15));
		} else {
			sortCode = Integer.parseInt(idNo.substring(14, 17));
		}
		if (sortCode % 2 == 0) {
			MAN_SEX = 1;// 男性身份证
		} else if (sortCode % 2 != 0) {
			MAN_SEX = 2;// 女性身份证
		} else {
			json.put("msg", "身份证不合法");
			JSONHelper.printObject(json);
			return null;
		}
		String iDresutl = "";
		iDresutl = IDCardValidateService.chekIdCard(MAN_SEX, idNo);
		if (iDresutl != "") {
			json.put("msg", "身份证不合法");
			JSONHelper.printObject(json);
			return null;
		}

		String cellPhone = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("cellPhone"));// 手机号码
		if (StringUtils.isBlank(cellPhone)) {
			json.put("msg", "请正确填写手机号码");
			JSONHelper.printObject(json);
			return null;
		} else if (cellPhone.length() != 11) {
			json.put("msg", "手机号码长度不对");
			JSONHelper.printObject(json);
			return null;
		}

		Map<String, String> pMap = null;
		pMap = beVipService.queryPUser(user.getId());
		if (pMap == null) {
			pMap = new HashMap<String, String>();
		}
		String isno = Convert.strToStr(pMap.get("idNo"), "");
		// 验证手机的唯一性
		Map<String, String> phonemap = new HashMap<String, String>();
		// 验证手机的唯一性
		phonemap = beVipService.queryIsPhone(cellPhone);
		// 测试--跳过验证
		if (IConstants.ISDEMO.equals("1")) {

		} else {
			// add by houli 判断身份证的唯一性

			if (StringUtils.isBlank(isno)) {
				Map<String, String> idNoMap = beVipService.queryIDCard(idNo);
				if (idNoMap != null && !idNoMap.isEmpty()) {
					json.put("msg", "身份证已注册");
					JSONHelper.printObject(json);
					return null;
				}
			}
		}
		String cellp = Convert.strToStr(pMap.get("cellphone"), "");
		if (StringUtils.isBlank(isno)) {
			if (!cellp.equals(cellPhone) && phonemap != null) {
				json.put("msg", "手机已存在");
				JSONHelper.printObject(json);
				return null;
			}
			/*
			 * if (phonemap == null) {//暂时注释 String phonecode = null; try {
			 * Object obje = session().getAttribute("phone"); // 测试--跳过验证码 if
			 * (IConstants.ISDEMO.equals("1")) {
			 * 
			 * } else { if (obje != null) { phonecode = obje.toString(); } else
			 * { json.put("msg", "请输入正确的验证码"); JSONUtils.printObject(json);
			 * return null; } } } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * if (phonecode != null) { if
			 * (!phonecode.trim().equals(cellPhone.trim())) { json.put("msg",
			 * "与获取验证码手机号不一致"); JSONUtils.printObject(json); return null; } } //
			 * 验证码 String vilidataNum =
			 * SqlInfusion.FilteSqlInfusion(paramMap.get("vilidataNum")); if
			 * (StringUtils.isBlank(vilidataNum)) { json.put("msg", "请填写验证码");
			 * JSONUtils.printObject(json); return null; }
			 * 
			 * String randomCode = null; Object objec =
			 * session().getAttribute("randomCode"); // 测试--跳过验证码 if
			 * (IConstants.ISDEMO.equals("1")) {
			 * 
			 * } else { if (objec != null) { randomCode = objec.toString(); }
			 * else { json.put("msg", "请输入正确的验证码"); JSONUtils.printObject(json);
			 * return null; } if (randomCode != null) { if
			 * (!randomCode.trim().equals(vilidataNum.trim())) {
			 * 
			 * json.put("msg", "请输入正确的验证码"); JSONUtils.printObject(json); return
			 * null; } } } }
			 */
		}

		String sex = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("sex")), null);// 性别(男 女)
		String birthday = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("birthday")), null);// 出生日期
		String highestEdu = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("highestEdu")), null);// 最高学历
		String eduStartDay = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("eduStartDay")), null);// 入学年份
		String school = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("school")), null);// 毕业院校
		String maritalStatus = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("maritalStatus")), null);// 婚姻状况(已婚
																		// 未婚)
		String hasChild = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("hasChild")), null);// 有无子女(有 无)
		String hasHourse = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("hasHourse")), null);// 是否有房(有 无)
		String hasHousrseLoan = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("hasHousrseLoan")), null);// 有无房贷(有
																			// 无)
		String hasCar = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("hasCar")), null);// 是否有车(有 无)
		String hasCarLoan = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("hasCarLoan")), null);// 有无车贷(有
																		// 无)
		Long nativePlacePro = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("nativePlacePro")), -1);// 籍贯省份(默认为-1)
		Long nativePlaceCity = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("nativePlaceCity")), -1);// 籍贯城市
																		// (默认为-1)
		Long registedPlacePro = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("registedPlacePro")), -1);// 户口所在地省份(默认为-1)
		Long registedPlaceCity = Convert.strToLong(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("registedPlaceCity")), -1);// 户口所在地城市(默认为-1)
		String personalHead = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("personalHead")), null);// 个人头像
																		// (默认系统头像)
		String address = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("address")), null);// 所在地
		String telephone = Convert.strToStr(SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("telephone")), null);// 居住电话
		String num = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("num")), null);// 获取传过来的页面号码
		long personId = -1L;
		// 判断是否是投资人填写个人资料
		if (num.equals("1")) {
			// 投资人 -- - 添加个人信息
			Map<String, String> resultMap = userService.updateUserBaseData1(
					realName, cellPhone, sex, birthday, highestEdu,
					eduStartDay, school, maritalStatus, hasChild, hasHourse,
					hasHousrseLoan, hasCar, hasCarLoan, nativePlacePro,
					nativePlaceCity, registedPlacePro, registedPlaceCity,
					address, telephone, personalHead, user.getId(), idNo, num);
			if (resultMap == null) {
				resultMap = new HashMap<String, String>();
			}
			personId = Convert.strToLong(resultMap.get("ret") + "", -1);
			request().setAttribute("person", "1");
			if (personId > 0) {
				// 修改用户认证状态
				if (user.getAuthStep() == 1) {
					user.setAuthStep(3);
				}

				session().removeAttribute("randomCode");
				user.setPersonalHead(personalHead);// 将个人头像放到session里面
				json.put("msg", "保存成功2");
				JSONHelper.printObject(json);
				user.setRealName(realName);
				session().setAttribute("user", user);
				return null;
			}
		} else {

			if (StringUtils.isBlank(sex)) {
				json.put("msg", "请正确填写性别");
				JSONHelper.printObject(json);
				return null;
			}
//			if (StringUtils.isBlank(birthday)) {
//				json.put("msg", "请正确填写出生日期");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 最高学历
//			if (StringUtils.isBlank(highestEdu)) {
//				json.put("msg", "请正确填写最高学历");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 入学年份
//			if (StringUtils.isBlank(eduStartDay)) {
//				json.put("msg", "请正确填写入学年份");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 毕业院校
//			if (StringUtils.isBlank(school)) {
//				json.put("msg", "请正确填写入毕业院校");
//				JSONHelper.printObject(json);
//				return null;
//			}
//
//			// 婚姻状况(已婚 未婚)
//			if (StringUtils.isBlank(maritalStatus)) {
//				json.put("msg", "请正确填写入婚姻状况");
//				JSONHelper.printObject(json);
//				return null;
//			}
//
//			// 有无子女(有 无)
//			if (StringUtils.isBlank(hasChild)) {
//				json.put("msg", "请正确填写入有无子女");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 是否有房(有 无)
//			if (StringUtils.isBlank(hasHourse)) {
//				json.put("msg", "请正确填写入是否有房");
//				JSONHelper.printObject(json);
//				return null;
//			}
//
//			// 有无房贷(有 无)
//			if (StringUtils.isBlank(hasHousrseLoan)) {
//				json.put("msg", "请正确填写入有无房贷");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 是否有车 (有 无)
//			if (StringUtils.isBlank(hasCar)) {
//				json.put("msg", "请正确填写入是否有车");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 有无车贷 (有 无)
//			if (StringUtils.isBlank(hasCarLoan)) {
//				json.put("msg", "请正确填写入有无车贷");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 籍贯省份(默认为-1)
//			if (StringUtils.isBlank(nativePlacePro.toString())) {
//				json.put("msg", "请正确填写入籍贯省份");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 籍贯城市 (默认为-1)
//			if (StringUtils.isBlank(nativePlaceCity.toString())) {
//				json.put("msg", "请正确填写入籍贯城市");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 户口所在地省份(默认为-1)
//			if (StringUtils.isBlank(registedPlacePro.toString())) {
//				json.put("msg", "请正确填写入户口所在地省份");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 户口所在地城市(默认为-1)
//			if (StringUtils.isBlank(registedPlaceCity.toString())) {
//				json.put("msg", "请正确填写入户口所在地城市");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 所在地
//			if (StringUtils.isBlank(address)) {
//				json.put("msg", "请正确填写入所在地");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			// 居住电话
//			if (StringUtils.isBlank(telephone)) {
//				json.put("msg", "请正确填写入你的家庭电话");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			if (telephone.trim().length() != 12
//					&& telephone.trim().length() != 13) {
//				json.put("msg", "你的居住电话输入长度不对");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			Pattern pattern = Pattern
//					.compile("^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$");
//			Matcher m = pattern.matcher(telephone);
//			if (!m.matches()) {
//				json.put("msg", "请正确填写入你的家庭电话");
//				JSONHelper.printObject(json);
//				return null;
//			}
//			/* 用户头像 */
//			if (StringUtils.isBlank(personalHead)) {
//				personalHead = null;
//				json.put("msg", "请正确上传你的个人头像");
//				JSONHelper.printObject(json);
//				return null;
//			}
			
		}
		Map<String, String> resultMap = userService.updateUserBaseData1(
				realName, cellPhone, sex, birthday, highestEdu, eduStartDay,
				school, maritalStatus, hasChild, hasHourse, hasHousrseLoan,
				hasCar, hasCarLoan, nativePlacePro, nativePlaceCity,
				registedPlacePro, registedPlaceCity, address, telephone,
				personalHead, user.getId(), idNo, num);
		if (resultMap == null) {
			resultMap = new HashMap<String, String>();
		}
		personId = Convert.strToLong(resultMap.get("ret") + "", -1);
		if (personId > 0) {
			// ==
			if (user.getAuthStep() == 1) {
				user.setAuthStep(3);
			}
			session().removeAttribute("randomCode");
			user.setPersonalHead(personalHead);// 将个人头像放到session里面
			json.put("msg", "保存成功");
			JSONHelper.printObject(json);
			request().setAttribute("person", "1");
			user.setRealName(realName);
			session().setAttribute("user", user);
			return null;
			// 成功
		} else {
			json.put("msg", "保存失败");
			// 失败
			JSONHelper.printObject(json);
			return null;
		}
	}

	/**
	 * 基本信息提交后再次跳转到user_information这个jsp页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String againUserInformation() throws Exception {
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		User user = (User) session().getAttribute("user");

		long userId = user.getId();
		user = userService.jumpToWorkData(userId);// 2表示工作认证步骤 2 工作认证 1
		// 基础资料认证 3 vip认证
		if (user != null) {
			session().removeAttribute("user");
			session().setAttribute("user", user);
			ServletActionContext.getRequest().setAttribute("authStep",
					user.getAuthStep());
			request().setAttribute("provinceList", provinceList);
			return SUCCESS;
		} else {
			return LOGIN;
		}
	}

	/**
	 * 工作信息认证后再次跳user_information这个jsp页面
	 * 
	 * @throws Exception
	 */
	public String againUserInformationTwo() throws Exception {

		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		User user = (User) session().getAttribute("user");
		if (user != null) {
			long userId = user.getId();
			user = userService.jumpToWorkData(userId);// 表示工作认证步骤 2 工作认证 1
			// 基础资料认证 3 vip认证
			session().removeAttribute("user");
			session().setAttribute("user", user);
			ServletActionContext.getRequest().setAttribute("authStep",
					user.getAuthStep());
			ServletActionContext.getRequest().setAttribute("realname",
					user.getRealName());
			int vipStatus = user.getVipStatus();
			if (vipStatus == 1) {
				ServletActionContext.getRequest().setAttribute("msg", "普通会员");
			} else {
				ServletActionContext.getRequest().setAttribute("msg",
						"尊敬的vip会员");
			}

			ServletActionContext.getRequest().setAttribute("email",
					user.getEmail());
			ServletActionContext.getRequest().setAttribute("username",
					user.getUserName());
			request().setAttribute("provinceList", provinceList);
			return SUCCESS;
		} else {
			return LOGIN;
		}
	}

	/**
	 * 更新的工作认证资料添加
	 * 
	 * @throws SQLException
	 * @throws IOException
	 * @throws DataException
	 */
	public String updateUserWorkData() throws Exception {
		JSONObject json = new JSONObject();
		User user = (User) session().getAttribute("user");
		if (user.getAuthStep() == 1) {
			// 个人信息认证步骤
			json.put("msg", "querBaseData");
			JSONHelper.printObject(json);
			return null;
		}

		String orgName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("orgName"));
		if (StringUtils.isBlank(orgName)) {
			json.put("msg", "请正确填写公司名字");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > orgName.length() || 50 < orgName.length()) {
			json.put("msg", "22");
			JSONHelper.printObject(json);
			return null;
		}

		String occStatus = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("occStatus"));
		if (StringUtils.isBlank(occStatus)) {
			json.put("msg", "请填写职业状态");
			JSONHelper.printObject(json);
			return null;
		}
		Long workPro = Convert.strToLong(paramMap.get("workPro"), -1L);
		if (workPro == null || workPro == -1L) {
			json.put("msg", "请填写工作城市省份");
			JSONHelper.printObject(json);
			return null;
		}
		Long workCity = Convert.strToLong(paramMap.get("workCity"), -1L);
		if (workCity == null || workCity == -1L) {
			json.put("msg", "请填写工作城市");
			JSONHelper.printObject(json);
			return null;
		}
		String companyType = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("companyType"));
		if (StringUtils.isBlank(companyType)) {
			json.put("msg", "请填写公司类别");
			JSONHelper.printObject(json);
			return null;
		}
		String companyLine = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("companyLine"));
		if (StringUtils.isBlank(companyLine)) {
			json.put("msg", "请填写公司行业");
			JSONHelper.printObject(json);
			return null;
		}
		String companyScale = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("companyScale"));
		if (StringUtils.isBlank(companyScale)) {
			json.put("msg", "请填写公司规模");
			JSONHelper.printObject(json);
			return null;
		}
		String job = SqlInfusionHelper.filteSqlInfusion(paramMap.get("job"));
		if (StringUtils.isBlank(job)) {
			json.put("msg", "请填写职位");
			JSONHelper.printObject(json);
			return null;
		}
		String monthlyIncome = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("monthlyIncome"));
		if (StringUtils.isBlank(monthlyIncome)) {
			json.put("msg", "请填写月收入");
			JSONHelper.printObject(json);
			return null;
		}
		String workYear = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("workYear"));
		if (StringUtils.isBlank(workYear)) {
			json.put("msg", "请填写现单位工作年限");
			JSONHelper.printObject(json);
			return null;
		}
		String companyTel = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("companyTel"));
		if (StringUtils.isBlank(companyTel)) {
			json.put("msg", "请正确填写公司电话");
			JSONHelper.printObject(json);
			return null;
		}

		Pattern pattern = Pattern
				.compile("^((0\\d{2,3})-)(\\d{7,8})(-(\\d{3,}))?$");
		Matcher m = pattern.matcher(companyTel);
		if (!m.matches()) {
			json.put("msg", "请正确填写公司电话");
			JSONHelper.printObject(json);
			return null;
		}

		String workEmail = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("workEmail"));
		if (StringUtils.isBlank(workEmail)) {
			json.put("msg", "请填写工作邮箱");
			JSONHelper.printObject(json);
			return null;
		}
		String companyAddress = paramMap.get("companyAddress");
		if (StringUtils.isBlank(companyAddress)) {
			json.put("msg", "请填写公司地址");
			JSONHelper.printObject(json);
			return null;
		}
		String directedName = paramMap.get("directedName");
		if (StringUtils.isBlank(directedName)) {
			json.put("msg", "请填写直系人姓名");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > directedName.length() || 50 < directedName.length()) {
			json.put("msg", "直系人姓名长度为不小于2和大于50");
			JSONHelper.printObject(json);
			return null;
		}

		String directedRelation = paramMap.get("directedRelation");
		if (StringUtils.isBlank(directedRelation)) {
			json.put("msg", "请填写直系人关系");
			JSONHelper.printObject(json);
			return null;
		}
		String directedTel = paramMap.get("directedTel");
		if (StringUtils.isBlank(directedTel)) {
			json.put("msg", "请正确填写直系人电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (!StringUtils.isNumeric(directedTel)) {
			json.put("msg", "请正确填写直系人电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (directedTel.trim().length() != 11) {
			json.put("msg", "请正确填写直系人电话长度错误");
			JSONHelper.printObject(json);
			return null;
		}

		String otherName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("otherName"));
		if (!StringUtils.isBlank(otherName.toString())) {
			if (2 > otherName.length() || 50 < otherName.length()) {
				json.put("msg", "其他人姓名长度为不小于2和大于50");
				JSONHelper.printObject(json);
				return null;
			}
		}

		String otherRelation = paramMap.get("otherRelation");
		// if (StringUtils.isBlank(otherRelation)) {
		// json.put("msg", "请填写其他人关系");
		// JSONUtils.printObject(json);
		// return null;
		// }
		String otherTel = paramMap.get("otherTel");
		// if (StringUtils.isBlank(otherTel)) {
		// json.put("msg", "请正确填写其他人联系电话");
		// JSONUtils.printObject(json);
		// return null;
		// }

		// if (!StringUtils.isNumeric(otherTel)) {
		// json.put("msg", "请正确填写其他人联系电话");
		// JSONUtils.printObject(json);
		// return null;
		// }
		// if (otherTel.trim().length() != 11) {
		// json.put("msg", "请正确填写其他人电话长度错误");
		// JSONUtils.printObject(json);
		// return null;
		// }

		String moredName = paramMap.get("moredName");
		if (!StringUtils.isBlank(moredName.toString())) {
			if (2 > moredName.length() || 50 < moredName.length()) {
				json.put("msg", "更多联系人姓名长度为不小于2和大于50");
				JSONHelper.printObject(json);
				return null;
			}
		}
		String moredRelation = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("moredRelation"));
		// if (StringUtils.isBlank(moredRelation)) {
		// json.put("msg", "请填写更多人关系");
		// JSONUtils.printObject(json);
		// return null;
		// }
		String moredTel = SqlInfusionHelper
				.filteSqlInfusion(paramMap.get("moredTel"));
		// if (StringUtils.isBlank(moredTel)) {
		// json.put("msg", "请填写更多联系人手机");
		// JSONUtils.printObject(json);
		// return null;
		// }
		// if (!StringUtils.isNumeric(moredTel)) {
		// json.put("msg", "请填写更多联系人手机");
		// JSONUtils.printObject(json);
		// return null;
		// }
		// if (moredTel.trim().length() != 11) {
		// json.put("msg", "请正确填写更多人电话长度错误");
		// JSONUtils.printObject(json);
		// return null;
		// }
		// 用户Id
		Long userId = user.getId();
		Long result = -1L;
		// 判断用户是否已经是vip
		Map<String, String> vipMap = null;
		vipMap = beVipService.queryUserById(user.getId());
		int vipStatus = 1;// 1 为非vip 2 为vip 3 代扣费vip
		int newutostept = -1;
		if (vipMap.size() > 0 && vipMap != null) {
			vipStatus = Convert.strToInt(vipMap.get("vipStatus"), 1);
			newutostept = Convert.strToInt(vipMap.get("authStep"), -1);// 用户此时的认证步骤状态
		}
		Map<String, String> resultMap = userService.updateUserWorkData1(
				orgName, occStatus, workPro, workCity, companyType,
				companyLine, companyScale, job, monthlyIncome, workYear,
				companyTel, workEmail, companyAddress, directedName,
				directedRelation, directedTel, otherName, otherRelation,
				otherTel, moredName, moredRelation, moredTel, userId,
				vipStatus, newutostept);
		if (resultMap == null) {
			resultMap = new HashMap<String, String>();
		}
		result = Convert.strToLong(resultMap.get("ret") + "", -1);

		if (result > 0) {
			// 保存成功更新认证步骤
			if (user.getAuthStep() == 2) {
				user.setAuthStep(3);
			}

			if (vipStatus != 1) {// 是vip会员
				// 更新用户的session步骤和是更新user表中的认证步骤
				if (user.getAuthStep() == 3) {
					user.setAuthStep(5);
				}
				json.put("msg", "vip保存成功");
				JSONHelper.printObject(json);
				return null;
			} else {
				json.put("msg", "保存成功");
				JSONHelper.printObject(json);
				return null;
			}

		} else {
			json.put("msg", "保存失败");
			JSONHelper.printObject(json);
			return null;
		}

	}

	private String from;
	private String btype;

	public String getBtype() {
		return btype;
	}

	public void setBtype(String btype) {
		this.btype = btype;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	// public String execute(){
	// // log.info("from = " + from);
	// return from;
	// }
	/**
	 * 查询个详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryBaseData() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		User user = (User) session().getAttribute("user");
		String birth = null;
		String rxedate = null;
		// --------modify by houli
		String from = getFrom();
		if (from == null) {
			from = request("from");
		}
		String btype = getBtype();

		if (from != null) {
			request().setAttribute("from", from);
		}
		if (btype != null) {
			request().setAttribute("btype", btype);
		}
		// -----------
		if (user != null) {
			map = userService.queryPersonById(user.getId());
			if (map != null && map.size() > 0) {
				workPro = Convert.strToLong(map.get("nativePlacePro")
						.toString(), -1L);
				cityId = Convert.strToLong(map.get("nativePlaceCity")
						.toString(), -1L);
				regPro = Convert.strToLong(map.get("registedPlacePro")
						.toString(), -1L);
				regCity = Convert.strToLong(map.get("registedPlaceCity")
						.toString(), -1L);
				@SuppressWarnings("unused")
				String birthd = map.get("birthday");
				birth = Convert.strToStr(map.get("birthday"), null);
				rxedate = Convert.strToStr(map.get("eduStartDay"), null);
				if (birth != null) {
					birth = birth.substring(0, 10);
				}
				if (rxedate != null) {
					rxedate = rxedate.substring(0, 10);
				}
			}
		}
		// 判断用户是否已经填写了基本信息
		String flag = "";
		if (map != null && map.size() > 0) {// 用户基本资料有数据但是不一定是已经填写了基本资料信息
			// 还有可能是上传了个人头像
			if (!StringUtils.isBlank(map.get("realName"))) {// 不为空
				flag = "1";
			} else {
				flag = "2";
			}
		} else {
			flag = "2";
		}
		request().setAttribute("flag", flag);
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		cityList = regionService.queryRegionList(-1L, workPro, 2);
		regcityList = regionService.queryRegionList(-1L, regPro, 2);
		request().setAttribute("map", map);
		request().setAttribute("provinceList", provinceList);
		request().setAttribute("cityList", cityList);
		request().setAttribute("regcityList", regcityList);
		request().setAttribute("birth", birth);
		request().setAttribute("rxedate", rxedate);

		request().setAttribute("ISDEMO", IConstants.ISDEMO);

		return SUCCESS;
	}

	/**
	 * 查询后台的审核用户的详细信息
	 */
	/**
	 * 查询baseData
	 * 
	 * @return
	 * @throws Exception
	 */

	public long getRegPro() {
		return regPro;
	}

	public void setRegPro(long regPro) {
		this.regPro = regPro;
	}

	public long getRegCity() {
		return regCity;
	}

	public void setRegCity(long regCity) {
		this.regCity = regCity;
	}

	public long getWorkPro() {
		return workPro;
	}

	public void setWorkPro(long workPro) {
		this.workPro = workPro;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	/**
	 * 查询用户基本资料
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryAdminBasecMessage() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		long id = -1l;
		String birth = null;
		String rxedate = null;
		if (StringUtils.isNotBlank(request().getParameter("uid"))) {
			id = Long.parseLong(request().getParameter("uid"));
		}
		map = userService.queryPersonById(id);
		if (map != null && map.size() > 0) {
			workPro = Convert.strToLong(map.get("nativePlacePro").toString(),
					-1L);
			cityId = Convert.strToLong(map.get("nativePlaceCity").toString(),
					-1L);
			regPro = Convert.strToLong(map.get("registedPlacePro").toString(),
					-1L);
			regCity = Convert.strToLong(
					map.get("registedPlaceCity").toString(), -1L);

			birth = Convert.strToStr(map.get("birthday"), null);
			rxedate = Convert.strToStr(map.get("eduStartDay"), null);
			if (birth != null) {
				birth = birth.substring(0, 10);
			}
			if (rxedate != null) {
				rxedate = rxedate.substring(0, 10);
			}
		}
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		cityList = regionService.queryRegionList(-1L, workPro, 2);
		regcityList = regionService.queryRegionList(-1L, regPro, 2);
		request().setAttribute("map", map);
		if (map == null) {// 如果map是空的话 那么用户没有填写基本信息
			request().setAttribute("flag", "1");
		}
		request().setAttribute("provinceList", provinceList);
		request().setAttribute("cityList", cityList);
		request().setAttribute("regcityList", regcityList);
		request().setAttribute("birth", birth);
		request().setAttribute("rxedate", rxedate);
		return SUCCESS;
	}

	/**
	 * vip页面跳转到workdata页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryVipToWork() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> allworkmap = new HashMap<String, String>();
		User user = (User) session().getAttribute("user");
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		request().setAttribute("provinceList", provinceList);
		// --------add by houli
		String btype = request("btype");
		if (btype != null) {
			request().setAttribute("btype", btype);
		}
		String from = request("from");
		if (from != null && !from.equals("")) {
			request().setAttribute("from", from);
		}
		// --------
		if (user != null) {
			// 获取用户认证进行的步骤
			if (user.getAuthStep() == 1) {
				// 个人信息认证步骤
				return "querBaseData";
			} 
			if(user.getAuthStep() == 2){
				/**
				 * 工作信息认证步骤废弃，跳转到绑定账号页面.
				 * 修改日期：2015-01-06 
				 * 修改人：戴志越
				 * 修改说明：该项目
				 */ 
				System.out.println("queryVipToWork----------------------------------");
				return "quervipData";
			}

			map = validateService.queryWorkDataById(user.getId());
			allworkmap = validateService.queryAllWorkStatus(user.getId());
			if (map != null && map.size() > 0) {
				workPro = Convert.strToLong(map.get("workPro").toString(), -1L);
				cityId = Convert.strToLong(map.get("workCity").toString(), -1L);
			}
			cityList = regionService.queryRegionList(-1L, workPro, 2);
			request().setAttribute("cityList", cityList);
			request().setAttribute("map", map);
			request().setAttribute("allworkmap", allworkmap);
			return SUCCESS;
		} else {
			return LOGIN;
		}
	}

	/**
	 * 基本信息跳转到工作信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryWorkDatabaseAgain() throws Exception {
		@SuppressWarnings("unused")
		User user = (User) session().getAttribute("user");
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		request().setAttribute("provinceList", provinceList);
		int authStep = user.getAuthStep();
		if (authStep == 3 || authStep == 4) {
			return "upWorkData";
		}
		return SUCCESS;
	}

	/**
	 * 在基本资料修改 和 工作认证资料修改 和 vip申请修改中的 资料上传的连接
	 * 
	 * @return
	 * @throws Exception
	 */
	public String upDataMethod() throws Exception {
		User user = (User) session().getAttribute("user");
		int authStep = user.getAuthStep();
		if (authStep == 4) {
			return SUCCESS;
		}
		if (authStep == 2) {
			provinceList = regionService.queryRegionList(-1L, 1L, 1);
			request().setAttribute("provinceList", provinceList);
			return "jibenjiliao";
		}
		if (authStep == 3) {
			provinceList = regionService.queryRegionList(-1L, 1L, 1);
			request().setAttribute("provinceList", provinceList);
			return "workData";
		} else if (authStep == 5) {
			return SUCCESS;
		}
		return LOGIN;

	}

	/**
	 * 基本资料跳转到vip页面
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryBaseToVip() throws Exception {
		
		System.out.println("queryBaseToVip--------------UserActoin1911------------------");
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> vippagemap = null;
		User user = (User) session().getAttribute("user");
		
		// ----------modify houli
		String from = getFrom();// execute();
		if (from == null) {
			request().setAttribute("from", from);
		}
		String btype = getBtype();// request("btype");
		if (btype == null) {
			btype = request("btype");
			if (btype != null && !btype.equals("")) {
				request().setAttribute("btype", btype);
			}
		} else {
			request().setAttribute("btype", btype);
		}
		// ------------

		if (user != null) {
			if (from == null || from.equals("")) {
				if (user.getAuthStep() == 1) {
					// 个人信息认证步骤
					return "querBaseData";
				} 
//				else if (user.getAuthStep() == 2) {
//					// 工作信息认证步骤
//					return "querWorkData";
//				}
			}

			vippagemap = userService.queryVipParamList(user.getId());
			request().setAttribute("vippagemap", vippagemap);

			// ----------add by houli 进行vip申请判断，如果该用户已经申请了vip，则不能再次申请
			if (user.getVipStatus() == IConstants.VIP_STATUS) {
				request().setAttribute("isVip", "true");
			}
			// --------------

			return SUCCESS;
		} else {
			return LOGIN;
		}
	}

	/**
	 * 更新用户vip状态和认证步骤
	 * 
	 * @return
	 * @throws Exception
	 */
	public String updataVipStatus() throws Exception {
		JSONObject obj = new JSONObject();
		User user = (User) session().getAttribute("user");
		// ----------------modify by houli
		String from = SqlInfusionHelper.filteSqlInfusion(paramMap.get("from"));

		if (from == null || from.equals("")) {
			if (user.getAuthStep() == 1) {
				// 个人信息认证步骤
				obj.put("msg", "13");
				JSONHelper.printObject(obj);
				return null;
			} else if (user.getAuthStep() == 2) {
				// 工作信息认证步骤
				obj.put("msg", "14");
				JSONHelper.printObject(obj);
				return null;
			}
		}

		String pageId = SqlInfusionHelper.filteSqlInfusion(paramMap.get("pageId"));
		// String code = (String) session().getAttribute(pageId + "_checkCode");
		// String _code = paramMap.get("code").toString().trim();
		String servicePersonId = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("kefu"));// 客服跟踪人
		Long server = Convert.strToLong(servicePersonId, -1);
		if (StringUtils.isBlank(servicePersonId)) {
			obj.put("msg", "2");
			JSONHelper.printObject(obj);
			return null;
		}

		// if (code == null || !_code.equals(code)) {
		// obj.put("msg", "5");
		// JSONUtils.printObject(obj);
		// return null;
		// }
		Long userId = user.getId();

		Map<String, Object> platformCostMap = getPlatformCost();
		double vipFee = Convert.strToDouble(platformCostMap
				.get(IAmountConstants.VIP_FEE_RATE)
				+ "", 0);
		double money = Convert.strToDouble(platformCostMap
				.get(IAmountConstants.ORDINARY_FRIENDS_FEE)
				+ "", 0);
		Map<String, String> map = beVipService.beVip(userId, server, vipFee,
				money);
		Map<String, String> userMap = userService.queryUserById(userId);
		if (userMap != null) {
			user.setAuthStep(Convert.strToInt(userMap.get("authStep"), user
					.getAuthStep()));
			user.setVipStatus(Convert.strToInt(userMap.get("vipStatus"), user
					.getVipStatus()));
		}
		int ret = Convert.strToInt(map.get("ret") + "", -1);
		if (ret < 1) {
			obj.put("msg", map.get("ret_desc") + "");
			JSONHelper.printObject(obj);
			return null;
		} else {
			// (如果用户已经申请完vip，重新设置user里面的vip状态，因为接下来跳转的地方是从user里面读取vip状态)
			user.setVipStatus(IConstants.VIP_STATUS);
			session().setAttribute("user", user);
			obj.put("msg", "1");
			JSONHelper.printObject(obj);
			return null;
		}
	}

	/**
	 * 查询客服列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String querykefylist() throws Exception {
		List<Map<String, Object>> map = null;
		map = userService.querykefylist();
		if (map != null && map.size() > 0) {
			request().setAttribute("map", map);
		}

		return SUCCESS;
	}

	public String jumpTopage() {
		User user = (User) session().getAttribute("user");
		if (user != null) {
			return SUCCESS;
		} else {
			return LOGIN;
		}
	}

	/**
	 * 跳转到上传页面
	 * 
	 * @throws Exception
	 */
	public String jumpPassDatapage() throws Exception {
		User user = (User) session().getAttribute("user");
		Long userId = -1l; 
		// Map<String,String> pictruemap = null;
		List<Map<String, Object>> basepictur = null;
		List<Map<String, Object>> selectpictur = null;
		// -----------modify by houli
		String from = SqlInfusionHelper.filteSqlInfusion(request("from"));
		String btype = SqlInfusionHelper.filteSqlInfusion(request("btype"));
		// -------------
		if (user != null) {
			if (from == null || from.equals("")) {
				// 获取用户认证进行的步骤
				if (user.getAuthStep() == 1) {
					// 个人信息认证步骤
					return "querBaseData";
				} else if (user.getAuthStep() == 2) {
					// 工作信息认证步骤
					return "querWorkData";
				} else if (user.getAuthStep() == 3 && user.getVipStatus() != 2) {
					// VIP申请认证步骤
					return "quervipData";
				} else if (user.getAuthStep() == 4) {
					// 绑定账号
					return "portUserAcct";
				}
				// ---------add by houli
				else if (user.getAuthStep() == 6
						&& (btype != null && !btype.equals(""))) {
					return "jumpOther";
				}
			} else {// 净值借款跟秒还借款操作步骤
				// 获取用户认证进行的步骤
				if (user.getAuthStep() == 1) {
					// 个人信息认证步骤
					return "querBaseData";
				}

				if (user.getVipStatus() == IConstants.UNVIP_STATUS) {
					return "quervipData";
				}

				// -------add by houli
				// return jumpToBorrow(btype);
				if (IConstants.BORROW_TYPE_NET_VALUE.equals(btype)) {
					return "jumpNet";
				} else if (IConstants.BORROW_TYPE_SECONDS.equals(btype)) {
					return "jumpSeconds";
				}
			}

			userId = user.getId();
			// 获取到图片的地址和图片的状态值
			// pictruemap = userService.queryUserPictureStatus(userId);
			// request().setAttribute("pictruemap", pictruemap);
			basepictur = userService.queryBasePicture(userId);// 五大基本
			selectpictur = userService.querySelectPicture(userId);// 可选
			request().setAttribute("basepictur", basepictur);
			request().setAttribute("selectpictur", selectpictur);

			return SUCCESS;

		} else {
			return LOGIN;
		}
	}

	/**
	 * 注册页面跳转使用条款
	 * 
	 * @return
	 * @throws Exception
	 */
	public String querytipM() throws Exception {
		paramMap = agreementService.getAgreementDetail(1, 2);

		return SUCCESS;
	}

	/**
	 * 注册页面跳转隐私条款
	 * 
	 * @throws Exception
	 * 
	 * @MethodName: querytipsM
	 * @Param: UserAction
	 * @Author: gang.lv
	 * @Date: 2013-5-11 下午02:38:52
	 * @Return:
	 * @Descb:
	 * @Throws:
	 */
	public String querytipsM() throws Exception {
		paramMap = agreementService.getAgreementDetail(1, 1);
		return SUCCESS;
	}

	/**
	 * @throws Exception
	 * @throws SQLException
	 *             删除用户
	 * 
	 * @return String
	 * @throws
	 */
	public String deleteUser() throws Exception {
		String userIds = request("userId");
		String[] userids = userIds.split(",");
		if (userids.length > 0) {
			long tempId = 0;
			for (String str : userids) {
				tempId = Convert.strToLong(str, -1);
				if (tempId == -1) {
					return INPUT;
				}
			}
		} else {
			return INPUT;
		}

		/*
		 * try { // userService.deleteUser(userIds, ","); } catch (DataException
		 * e) { e.printStackTrace(); } catch (SQLException e) {
		 * e.printStackTrace(); }
		 */
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		operationLogService.addOperationLog("t_user", admin.getUserName(),
				IConstants.DELETE, admin.getLastIP(), 0, "删除用户", 2);
		return SUCCESS;
	}

	/**
	 * 分页查询用户信息初始化
	 * 
	 * @return String
	 * @throws
	 */
	public String queryUserListInfoInit() {
		return SUCCESS;
	}

	/**
	 * 分页查询用户
	 * 
	 * @throws SQLException
	 * @throws DataException
	 * @return String
	 * @throws
	 */
	public String queryUserListInfo() throws SQLException, DataException {
		return SUCCESS;
	}

	/**
	 * 发送短信验证
	 * 
	 * @param code
	 * @return
	 */
	public String SendSMS() throws SQLException, DataException {
		try {
			// 清空验证码
			session().removeAttribute("randomCode");
			session().removeAttribute("phone");
			String phone = SqlInfusionHelper.filteSqlInfusion(paramMap.get("phone"));

			String sign = phone.substring(0, 10);
			phone = phone.substring(10, phone.length());
			String signNew = Encrypt.MD5(phone + IConstants.PASS_KEY)
					.substring(0, 10);
			if (!signNew.equals(sign)) {// 签名验证
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}

			phone = Encrypt.decryptSES(phone, IConstants.PASS_KEY);
			String[] t = phone.split("/");
			phone = t[0].toString();
			String time = new String();
			String uid = new String();
			if (t[1] != null) {
				time = t[1];
			}
			if (t[3] != null) {
				uid = t[3];
			}
			User user = (User) session().getAttribute(IConstants.SESSION_USER);
			if (!uid.equals(user.getId() + "")) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}
			long currTime = new Date().getTime();
			long sendTime = Long.valueOf(time);
			if (currTime - sendTime > 30 * 1000) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}
			// 随机产生4位数字
			int intCount = 0;
			intCount = (new Random()).nextInt(9999);// 最大值位9999
			if (intCount < 1000)
				intCount += 1000; // 最小值位1001
			String randomCode = intCount + "";
			// 测试--跳过验证
			if (IConstants.ISDEMO.equals("1")) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				session().setAttribute("randomCode", randomCode);
				session().setAttribute("phone", phone);
				return null;
			}
			// 发送短信
			Map<String, String> map = sMsService.getSMSById(1);
			String content = "尊敬的客户您好,欢迎使用" + IConstants.PRO_GLOBLE_NAME
					+ ",手机验证码为:";
			// //获取短信接口url
			// String url=SMSUtil.getSMSPort(map.get("url"), map.get("UserID"),
			// map.get("Account"), map.get("Password"), randomCode, content,
			// phone, null);
			// //发送短信
			// String retCode = SMSUtil.sendSMS(url);
//			String retCode = SMSHelper.sendSMS(map.get("Account"), map
//					.get("Password"), content, phone, randomCode);
			String retCode = SMSHelper.sendSMSUseXuanWu(map.get("Account"), map
					.get("Password"), content, phone, randomCode);
			if ("Sucess".equals(retCode)) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				session().setAttribute("randomCode", randomCode);
				session().setAttribute("phone", phone);
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 发送短信验证
	 * 
	 * @param code
	 * @return
	 */
	public String sendSMSNew() throws SQLException, DataException {
		try {
			// 清空验证码
			session().removeAttribute("randomCode");
			session().removeAttribute("phone");
			String phone = SqlInfusionHelper.filteSqlInfusion(paramMap.get("phone"));

//			String sign = phone.substring(0, 10);
//			phone = phone.substring(10, phone.length());
//			String signNew = Encrypt.MD5(phone + IConstants.PASS_KEY)
//					.substring(0, 10);
//			if (!signNew.equals(sign)) {// 签名验证
//				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
//				return null;
//			}

//			phone = Encrypt.decryptSES(phone, IConstants.PASS_KEY);
			String pageId = paramMap.get("pageId");
//			String code = (String) session()
//					.getAttribute(pageId + "_checkCode");
//			String[] t = phone.split("/");
//			phone = t[0].toString();
			String time = new String();
//			String codeBak = new String();
//			if (t[1] != null) {
//				time = t[1];
//			}
//			if (t[2] != null) {
//				codeBak = t[2];
//			}
//			if (!codeBak.equals(code)) {
//				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
//				return null;
//			}
//			long currTime = new Date().getTime();
//			long sendTime = Long.valueOf(time);
//			if (currTime - sendTime > 30 * 1000) {
//				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
//				return null;
//			}
			// 随机产生4位数字
			int intCount = 0;
			intCount = (new Random()).nextInt(9999);// 最大值位9999
			if (intCount < 1000)
				intCount += 1000; // 最小值位1001
			String randomCode = intCount + "";
			// 测试--跳过验证
			if (IConstants.ISDEMO.equals("1")) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				session().setAttribute("randomCode", randomCode);
				session().setAttribute("phone", phone);
				return null;
			}
			// 发送短信
			Map<String, String> map = sMsService.getSMSById(1);
			String content = "尊敬的客户您好,欢迎使用" + IConstants.PRO_GLOBLE_NAME
					+ ",手机验证码为:";
			// //获取短信接口url
			// String url=SMSUtil.getSMSPort(map.get("url"), map.get("UserID"),
			// map.get("Account"), map.get("Password"), randomCode, content,
			// phone, null);
			// //发送短信
			// String retCode = SMSUtil.sendSMS(url);
//			String retCode = SMSHelper.sendSMS(map.get("Account"), map
//					.get("Password"), content, phone, randomCode);
			String retCode = SMSHelper.sendSMSUseXuanWu(map.get("Account"), map
					.get("Password"), content, phone, randomCode);
			
			if ("Sucess".equals(retCode)) {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "1");
				session().setAttribute("randomCode", randomCode);
				session().setAttribute("phone", phone);
				return null;
			} else {
				ServletHelper.returnStr(ServletActionContext.getResponse(), "2");
				return null;
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

	public String phoneCheck() throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("ret", -1 + "");
		String phone = Convert.strToStr(request("phone"), "");
		String code = Convert.strToStr(request("code"), "");
		String uid = "-1";
		User user = (User) session().getAttribute(IConstants.SESSION_USER);
		if (user != null) {
			uid = user.getId() + "";
		}
		if (StringUtils.isBlank(phone)) {
			obj.put("msg", "手机号码不能为空");
			JSONHelper.printObject(obj);
			return null;
		}
		// 手机号码验证
		Pattern p = Pattern.compile("^1[3,5,8]\\d{9}$");
		Matcher m = p.matcher(phone);
		if (!m.matches()) {
			obj.put("msg", "请输入正确的手机号码进行查询");
			JSONHelper.printObject(obj);
			return null;
		}
		String time = Convert.strToStr(new Date().getTime() + "", "");
		try {
			phone = Encrypt.encryptSES(phone + "/" + time + "/" + code + "/"
					+ uid, IConstants.PASS_KEY);
			phone = Encrypt.MD5(phone + IConstants.PASS_KEY).substring(0, 10)
					+ phone;
			obj.put("ret", 1 + "");
			obj.put("phone", phone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONHelper.printObject(obj);
		return null;
	}

	/**
	 * 現下充值跳转委托書条款
	 * 
	 * @throws Exception
	 * 
	 * @MethodName: querytipsM
	 * @Param: UserAction
	 * @Author: gang.lv
	 * @Date: 2013-5-11 下午02:38:52
	 * @Return:
	 * @Descb:
	 * @Throws:
	 */
	public String queryRecharTips() throws Exception {
		// paramMap = agreementService.getAgreementDetail(1, 1);
		paramMap = agreementService.getMessageByTypeId(33);
		return SUCCESS;
	}

	/**
	 * 清空session中的验证码
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public String removeSessionCode() throws SQLException, DataException {
		session().removeAttribute("randomCode");
		return null;
	}

	public List<Map<String, Object>> getCityList() {
		if (cityList == null) {
			cityList = new ArrayList<Map<String, Object>>();
		}
		return cityList;
	}

	public List<Map<String, Object>> getAreaList() {
		if (areaList == null) {
			areaList = new ArrayList<Map<String, Object>>();
		}
		return areaList;
	}

	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public List<Map<String, Object>> getRegcityList() {
		return regcityList;
	}

	public void setRegcityList(List<Map<String, Object>> regcityList) {
		this.regcityList = regcityList;
	}

	public void setValidateService(ValidateService validateService) {
		this.validateService = validateService;
	}

	public JobTaskService getJobTaskService() {
		return jobTaskService;
	}

	public void setJobTaskService(JobTaskService jobTaskService) {
		this.jobTaskService = jobTaskService;
	}

}
