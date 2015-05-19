package com.sp2p.action.front;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.QianduoduoareaDao;
import com.sp2p.entity.User;
import com.sp2p.service.BankService;
import com.sp2p.service.BeVipService;
import com.sp2p.service.IDCardValidateService;
import com.sp2p.service.QianduoduoareaService;
import com.sp2p.service.RegionService;
import com.sp2p.service.UserService;
import com.sp2p.service.ValidateService;

public class FrontMyInformation extends BaseFrontAction {

	public static Log log = LogFactory.getLog(FrontMyInformation.class);
	private static final long serialVersionUID = 1L;

	private UserService userService;

	private RegionService regionService;
	private ValidateService validateService;
	private BeVipService beVipService;
	private IDCardValidateService iDCardValidateService;

	private List<Map<String, Object>> provinceList;
	private List<Map<String, Object>> cityList;
	private List<Map<String, Object>> areaList;
	private List<Map<String, Object>> regcityList;

	private long workPro = -1L;// 初始化省份默认值
	private long cityId = -1L;// 初始化话默认城市
	private long regPro = -1L;// 户口区域默认值
	private long regCity = -1L;// 户口区域默认值

	private QianduoduoareaService qianduoduoareaService;
	private BankService bankService;

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
		} else {
			return "login";
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
		request().setAttribute("realName", user.getRealName());

		String tab_type = request("tab_type") == null ? null
				: request("tab_type");
		if (tab_type != null) {
			request().setAttribute("tab_type", tab_type);
		}

		String yy = request("yy") == null ? null : request("yy");
		if (yy != null) {
			request().setAttribute("yy", yy);
		}
		Map<String, String> pmap = userService.queryPersonById(user.getId());
		if (pmap != null && pmap.size() > 0) {
			request().setAttribute("pass", pmap.get("auditStatus"));
		}
		int authStep = user.getAuthStep();
		if (authStep < 2) {
			request().setAttribute("person", "0");
		} else {
			request().setAttribute("person", "1");
		}
		request().setAttribute("ISDEMO", IConstants.ISDEMO);
		return SUCCESS; // 返回个人资料详情
	}

	// 添加或更新个人信息
	public String updateUserBaseData() throws Exception {
		User user = (User) session("user");

		JSONObject json = new JSONObject();
		String realName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("realName"));// 真实姓名
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
			json.put("msg", "请正确输入身份证号码");
			JSONHelper.printObject(json);
			return null;
		} else if (15 != len) {
			if (18 == len) {
			} else {
				json.put("msg", "请正确输入身份证号码");
				JSONHelper.printObject(json);
				return null;
			}
		}
		// 验证身份证
		String iDresutl = "";
		// iDresutl = iDCardValidateService.IDCardValidate(idNo);
		if (iDresutl != "") {
			json.put("msg", "身份证不合法");
			JSONHelper.printObject(json);
			return null;
		}
		String cellPhone = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("cellPhone"));// 手机号码
		// 判断是否是手机注册用户
		String iscellPhone = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("iscellPhone"));
		if (StringUtils.isBlank(iscellPhone)) {
			if (StringUtils.isBlank(cellPhone)) {
				json.put("msg", "请正确填写手机号码");
				JSONHelper.printObject(json);
				return null;
			} else if (cellPhone.length() < 9 || cellPhone.length() > 15) {
				json.put("msg", "手机号码长度不对");
				JSONHelper.printObject(json);
				return null;
			}

			/**
			 * 判定用户是否已存在记录
			 */
			Map<String, String> pMap = null;

			pMap = beVipService.queryPUser(user.getId());
			// 验证手机的唯一性
			Map<String, String> phonemap = null;
			phonemap = beVipService.queryIsPhone(cellPhone);

			if (pMap == null) {

				if (phonemap != null) {
					json.put("msg", "手机已存在");
					JSONHelper.printObject(json);
					return null;
				}

				if (phonemap == null) {
					String phonecode = null;
					try {
						Object obje = session().getAttribute("phone");
						if (obje != null) {
							phonecode = obje.toString();
						} else {
							json.put("msg", "请输入正确的验证码");
							JSONHelper.printObject(json);
							return null;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (phonecode != null) {
						if (!phonecode.trim().equals(cellPhone.trim())) {
							json.put("msg", "与获取验证码手机号不一致");
							JSONHelper.printObject(json);
							return null;
						}
					}
					// 验证码
					String vilidataNum = paramMap.get("vilidataNum");
					if (StringUtils.isBlank(vilidataNum)) {
						json.put("msg", "请填写验证码");
						JSONHelper.printObject(json);
						return null;
					}

					String randomCode = null;
					Object objec = session().getAttribute("randomCode");
					if (objec != null) {
						randomCode = objec.toString();
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
			}
		}

		String sex = SqlInfusionHelper.filteSqlInfusion(paramMap.get("sex"));// 性别(男
																				// 女)
		if (StringUtils.isBlank(sex)) {
			json.put("msg", "请正确填写性别");
			JSONHelper.printObject(json);
			return null;
		}

		String birthday = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("birthday"));// 出生日期
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

		String school = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("school"));// 毕业院校
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

		String hasChild = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("hasChild"));// 有无子女(有 无)

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

		String hasCar = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("hasCar"));// 是否有车 (有 无)
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
		Long nativePlaceCity = Convert.strToLong(
				paramMap.get("nativePlaceCity"), -1);// 籍贯城市 (默认为-1)
		if (StringUtils.isBlank(nativePlaceCity.toString())) {
			json.put("msg", "请正确填写入籍贯城市");
			JSONHelper.printObject(json);
			return null;
		}

		Long registedPlacePro = Convert.strToLong(
				paramMap.get("registedPlacePro"), -1);// 户口所在地省份(默认为-1)
		if (StringUtils.isBlank(registedPlacePro.toString())) {
			json.put("msg", "请正确填写入户口所在地省份");
			JSONHelper.printObject(json);
			return null;
		}

		Long registedPlaceCity = Convert.strToLong(
				paramMap.get("registedPlaceCity"), -1);// 户口所在地城市(默认为-1)

		if (StringUtils.isBlank(registedPlaceCity.toString())) {
			json.put("msg", "请正确填写入户口所在地城市");
			JSONHelper.printObject(json);
			return null;
		}

		String address = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("address"));// 所在地
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
		if (!StringUtils.isBlank(telephone)) {
			if (!StringUtils.isNumeric(telephone)) {
				json.put("msg", "你的家庭电话输入不正确");
				JSONHelper.printObject(json);
				return null;
			}
			if (telephone.trim().length() < 7 || telephone.trim().length() > 11) {
				json.put("msg", "你的家庭电话输入长度不对");
				JSONHelper.printObject(json);
				return null;
			}
		}
		/* 用户头像 */
		String personalHead = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("personalHead"));// 个人头像 (默认系统头像)
		if (StringUtils.isBlank(personalHead)) {
			personalHead = null;
			json.put("msg", "请正确上传你的个人头像");
			JSONHelper.printObject(json);
			return null;
		}
		if (user == null) {
			json.put("msg", "超时请重新登录");
			JSONHelper.printObject(json);
			return null;
		}

		long personId = -1L;
		if (iscellPhone != null) {
			cellPhone = iscellPhone;
		}

		personId = userService.updateUserBaseData(realName, cellPhone, sex,
				birthday, highestEdu, eduStartDay, school, maritalStatus,
				hasChild, hasHourse, hasHousrseLoan, hasCar, hasCarLoan,
				nativePlacePro, nativePlaceCity, registedPlacePro,
				registedPlaceCity, address, telephone, personalHead,
				user.getId(), idNo);
		if (personId > 0) {
			// ==
			if (user.getAuthStep() == 1) {
				user.setAuthStep(2);
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
	 *
	 * 初始化工作认证信息，
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryWorkInit() throws Exception {

		// ==============================
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> allworkmap = new HashMap<String, String>();
		User user = (User) session().getAttribute("user");
		provinceList = regionService.queryRegionList(-1L, 1L, 1);
		request().setAttribute("provinceList", provinceList);

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
		Map<String, String> pmap = userService.queryPersonById(user.getId());
		if (pmap != null && pmap.size() > 0) {
			request().setAttribute("pass", pmap.get("auditStatus"));
		}
		// 得都用户基本资料信息
		Map<String, String> mapList = userService.queryPersonById(user.getId());
		if (mapList == null) {
			mapList = new HashMap<String, String>();
		}
		int authStep = user.getAuthStep();
		if (authStep < 2) {
			request().setAttribute("person", "0");
		} else {
			request().setAttribute("person", "1");
		}
		request().setAttribute("realName", user.getRealName());

		return SUCCESS;
	}

	/**
	 *
	 * 初始化基本资料信息，
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryBasicInit() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		User user = (User) session().getAttribute("user");
		String birth = null;
		String rxedate = null;
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

		String orgName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("orgName"));
		if (StringUtils.isBlank(orgName)) {
			json.put("msg", "请正确填写公司名字");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > orgName.length() || 50 < orgName.length()) {
			json.put("msg", "公司名字长度为不小于2和大于50");
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
		String workYear = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("workYear"));
		if (StringUtils.isBlank(workYear)) {
			json.put("msg", "请填写现单位工作年限");
			JSONHelper.printObject(json);
			return null;
		}
		String companyTel = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("companyTel"));
		if (StringUtils.isBlank(companyTel)) {
			json.put("msg", "请真确填写公司电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (!StringUtils.isNumeric(companyTel)) {
			json.put("msg", "请真确填写公司电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (companyTel.trim().length() < 7 || companyTel.trim().length() > 11) {
			json.put("msg", "请真确填写公司电话");
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
		String companyAddress = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("companyAddress"));
		if (StringUtils.isBlank(companyAddress)) {
			json.put("msg", "请填写公司地址");
			JSONHelper.printObject(json);
			return null;
		}
		String directedName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("directedName"));
		if (StringUtils.isBlank(directedName)) {
			json.put("msg", "请填写直系人姓名");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > directedName.length() || 50 < directedName.length()) {
			json.put("msg", "直系人姓名长度为不小于2和大于50");
			JSONHelper.printObject(json);
			return null;
		}

		String directedRelation = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("directedRelation"));
		if (StringUtils.isBlank(directedRelation)) {
			json.put("msg", "请填写直系人关系");
			JSONHelper.printObject(json);
			return null;
		}
		String directedTel = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("directedTel"));
		if (StringUtils.isBlank(directedTel)) {
			json.put("msg", "请真确填写直系人电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (!StringUtils.isNumeric(directedTel)) {
			json.put("msg", "请真确填写直系人电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (directedTel.trim().length() != 11) {
			json.put("msg", "请真确填写直系人电话长度错误");
			JSONHelper.printObject(json);
			return null;
		}

		String otherName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("otherName"));
		if (StringUtils.isBlank(workPro.toString())) {
			json.put("msg", "请填写其他人姓名");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > otherName.length() || 50 < otherName.length()) {
			json.put("msg", "其他人姓名长度为不小于2和大于50");
			JSONHelper.printObject(json);
			return null;
		}

		String otherRelation = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("otherRelation"));
		if (StringUtils.isBlank(otherRelation)) {
			json.put("msg", "请填写其他人关系");
			JSONHelper.printObject(json);
			return null;
		}
		String otherTel = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("otherTel"));
		if (StringUtils.isBlank(otherTel)) {
			json.put("msg", "请正确填写其他人联系电话");
			JSONHelper.printObject(json);
			return null;
		}

		if (!StringUtils.isNumeric(otherTel)) {
			json.put("msg", "请正确填写其他人联系电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (otherTel.trim().length() != 11) {
			json.put("msg", "请真确填写直系人电话长度错误");
			JSONHelper.printObject(json);
			return null;
		}

		String moredName = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("moredName"));
		if (StringUtils.isBlank(moredName)) {
			json.put("msg", "morename");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > moredName.length() || 50 < moredName.length()) {
			json.put("msg", "更多联系人姓名长度为不小于2和大于50");
			JSONHelper.printObject(json);
			return null;
		}
		String moredRelation = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("moredRelation"));
		if (StringUtils.isBlank(moredRelation)) {
			json.put("msg", "morereation");
			JSONHelper.printObject(json);
			return null;
		}
		String moredTel = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("moredTel"));
		if (StringUtils.isBlank(moredTel)) {
			json.put("msg", "moretel");
			JSONHelper.printObject(json);
			return null;
		}
		if (!StringUtils.isNumeric(moredTel)) {
			json.put("msg", "moretel");
			JSONHelper.printObject(json);
			return null;
		}
		if (moredTel.trim().length() != 11) {
			json.put("msg", "请真确填写直系人电话长度错误");
			JSONHelper.printObject(json);
			return null;
		}
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
		if (user != null) {
			result = userService.updateUserWorkData(orgName, occStatus,
					workPro, workCity, companyType, companyLine, companyScale,
					job, monthlyIncome, workYear, companyTel, workEmail,
					companyAddress, directedName, directedRelation,
					directedTel, otherName, otherRelation, otherTel, moredName,
					moredRelation, moredTel, userId, vipStatus, newutostept);
		}

		if (result > 0) {
			// 保存成功更新认证步骤
			if (user.getAuthStep() == 2) {
				user.setAuthStep(3);
			}

			if (vipStatus != 1) {// 是vip会员
				// 更新用户的session步骤和是更新user表中的认证步骤
				user.setAuthStep(4);
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

	public String updatePassword() {
		return "success";
	}

	public String szform() {
		return "success";
	}

	/**
	 * (前台)个人设置中修改密码
	 * 
	 * @return
	 */
	public String updatexgmm() {
		User user = (User) session().getAttribute("user");
		try {
			String realName = "";
			Map<String, String> pmap = userService
					.queryPersonById(user.getId());
			if (pmap != null && pmap.size() > 0) {
				request().setAttribute("pass", pmap.get("auditStatus"));
				realName = pmap.get("realName") + "";
			}
			if (StringUtils.isBlank(realName)) {
				return "person";
			}
			// int authStep = user.getAuthStep();
			// if(authStep < 2){
			// request().setAttribute("person", "0");
			// }else{

			// }
			List<Map<String, Object>> qianduoduoareaList = qianduoduoareaService
					.queryProvinceList();
			List<Map<String, Object>> qianduoduobankList = bankService
					.queryBankList();
			request().setAttribute("qianduoduoareaList", qianduoduoareaList);
			request().setAttribute("qianduoduobankList", qianduoduobankList);

			request().setAttribute("emailBound", user.getEmail());
			request().setAttribute("realName", user.getRealName());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}

		return SUCCESS;
	}

	/**
	 * 查询城市
	 * 
	 * @Title: queryarealist
	 * @param
	 * @return void 返回类型
	 * @throws
	 */
	public String queryarealist() {
		String parentid = SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("parentid"));
		try {
			List<Map<String, Object>> qianduoduoareaList = qianduoduoareaService
					.queryCityList(parentid);
			JSONHelper.printArray(qianduoduoareaList);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setiDCardValidateService(
			IDCardValidateService iDCardValidateService) {
		this.iDCardValidateService = iDCardValidateService;
	}

	public UserService getUserService() {
		return userService;
	}

	public ValidateService getValidateService() {
		return validateService;
	}

	public void setValidateService(ValidateService validateService) {
		this.validateService = validateService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public RegionService getRegionService() {
		return regionService;
	}

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public List<Map<String, Object>> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<Map<String, Object>> provinceList) {
		this.provinceList = provinceList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Map<String, Object>> areaList) {
		this.areaList = areaList;
	}

	public List<Map<String, Object>> getRegcityList() {
		return regcityList;
	}

	public void setRegcityList(List<Map<String, Object>> regcityList) {
		this.regcityList = regcityList;
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

	public void setBeVipService(BeVipService beVipService) {
		this.beVipService = beVipService;
	}

	public void setIDCardValidateService(
			IDCardValidateService cardValidateService) {
		iDCardValidateService = cardValidateService;
	}

	public final QianduoduoareaService getQianduoduoareaService() {
		return qianduoduoareaService;
	}

	public final void setQianduoduoareaService(
			QianduoduoareaService qianduoduoareaService) {
		this.qianduoduoareaService = qianduoduoareaService;
	}

	public final BankService getBankService() {
		return bankService;
	}

	public final void setBankService(BankService bankService) {
		this.bankService = bankService;
	}

	public final IDCardValidateService getiDCardValidateService() {
		return iDCardValidateService;
	}
}
