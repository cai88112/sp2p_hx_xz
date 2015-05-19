package com.sp2p.action.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.json.simple.JSONArray;

import com.fp2p.helper.JSONHelper;
import com.fp2p.helper.ServletHelper;
import com.fp2p.helper.infusion.SqlInfusionHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.sp2p.action.front.BaseFrontAction;
import com.sp2p.constants.IConstants;
import com.sp2p.entity.Admin;
import com.sp2p.service.RegionService;
import com.sp2p.service.admin.UserAdminService;

@SuppressWarnings("serial")
public class UserAdminAction extends BaseFrontAction {
	public static Log log = LogFactory.getLog(UserAdminAction.class);
	private UserAdminService userAdminService;
	private RegionService regionService;

	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	public void setUserAdminService(UserAdminService userAdminService) {
		this.userAdminService = userAdminService;
	}

	public String updateUserBaseDataAdmin() throws Exception {
		JSONObject json = new JSONObject();

		Long userId = Convert.strToLong(paramMap.get("ui"), -1L);
		if (userId == -1L) {
			json.put("msg", "用户为空");
			JSONHelper.printObject(json);
			return null;
		}
		String realName = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("realName")), "");// 真实姓名
		if (StringUtils.isBlank(realName)) {
			json.put("msg", "请正确填写真实名字");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > realName.length() || 20 < realName.length()) {
			json.put("msg", "真实姓名的长度为不小于2和大于20");
			JSONHelper.printObject(json);
			return null;
		}

		String idNo = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("idNo")), "");// 身份证号码
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

		String cellPhone = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("cellPhone")), "");// 手机号码
		if (StringUtils.isBlank(cellPhone)) {
			json.put("msg", "请正确填写手机号码");
			JSONHelper.printObject(json);
			return null;
		} else if (cellPhone.length() < 9 || cellPhone.length() > 15) {
			json.put("msg", "手机号码长度不对");
			JSONHelper.printObject(json);
			return null;
		}

		String sex = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("sex")), null);// 性别(男 女)
		if (StringUtils.isBlank(sex)) {
			json.put("msg", "请正确填写性别");
			JSONHelper.printObject(json);
			return null;
		}

		String birthday = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion((paramMap.get("birthday")+"")),
				null);// 出生日期
		if (StringUtils.isBlank(birthday)) {
			json.put("msg", "请正确填写出生日期");
			JSONHelper.printObject(json);
			return null;
		}

		String highestEdu = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("highestEdu")), null);// 最高学历
		if (StringUtils.isBlank(highestEdu)) {
			json.put("msg", "请正确填写最高学历");
			JSONHelper.printObject(json);
			return null;
		}

		String eduStartDay = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion((paramMap.get("eduStartDay"))
				+""), null);// 入学年份
		if (StringUtils.isBlank(eduStartDay)) {
			json.put("msg", "请正确填写入学年份");
			JSONHelper.printObject(json);
			return null;
		}

		String school = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("school")), null);// 毕业院校
		if (StringUtils.isBlank(school)) {
			json.put("msg", "请正确填写入毕业院校");
			JSONHelper.printObject(json);
			return null;
		}

		String maritalStatus = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("maritalStatus")),
				null);// 婚姻状况(已婚 未婚)
		if (StringUtils.isBlank(maritalStatus)) {
			json.put("msg", "请正确填写入婚姻状况");
			JSONHelper.printObject(json);
			return null;
		}

		String hasChild = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasChild")), null);// 有无子女(有
																			// 无)

		if (StringUtils.isBlank(hasChild)) {
			json.put("msg", "请正确填写入有无子女");
			JSONHelper.printObject(json);
			return null;
		}
		String hasHourse = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasHourse")), null);// 是否有房(有
																				// 无)
		if (StringUtils.isBlank(hasHourse)) {
			json.put("msg", "请正确填写入是否有房");
			JSONHelper.printObject(json);
			return null;
		}

		String hasHousrseLoan = Convert.strToStr(
				SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasHousrseLoan")), null);// 有无房贷(有 无)
		if (StringUtils.isBlank(hasHousrseLoan)) {
			json.put("msg", "请正确填写入有无房贷");
			JSONHelper.printObject(json);
			return null;
		}

		String hasCar = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasCar")), null);// 是否有车
																		// (有 无)
		if (StringUtils.isBlank(hasCar)) {
			json.put("msg", "请正确填写入是否有车");
			JSONHelper.printObject(json);
			return null;
		}

		String hasCarLoan = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("hasCarLoan")), null);// 有无车贷
																				// (有
																				// 无)
		if (StringUtils.isBlank(hasCarLoan)) {
			json.put("msg", "请正确填写入有无车贷");
			JSONHelper.printObject(json);
			return null;
		}
		Long nativePlacePro = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap.get("nativePlacePro")+""),
				-1);// 籍贯省份(默认为-1)
		if (StringUtils.isBlank(nativePlacePro.toString())) {
			json.put("msg", "请正确填写入籍贯省份");
			JSONHelper.printObject(json);
			return null;
		}
		Long nativePlaceCity = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("nativePlaceCity")), -1);// 籍贯城市 (默认为-1)
		if (StringUtils.isBlank(nativePlaceCity.toString())) {
			json.put("msg", "请正确填写入籍贯城市");
			JSONHelper.printObject(json);
			return null;
		}

		Long registedPlacePro = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("registedPlacePro")), -1);// 户口所在地省份(默认为-1)
		if (StringUtils.isBlank(registedPlacePro.toString())) {
			json.put("msg", "请正确填写入户口所在地省份");
			JSONHelper.printObject(json);
			return null;
		}

		Long registedPlaceCity = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap
				.get("registedPlaceCity")), -1);// 户口所在地城市(默认为-1)

		if (StringUtils.isBlank(registedPlaceCity.toString())) {
			json.put("msg", "请正确填写入户口所在地城市");
			JSONHelper.printObject(json);
			return null;
		}

		String address = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("address")), "");// 所在地
		if (StringUtils.isBlank(address)) {
			json.put("msg", "请正确填写入所在地");
			JSONHelper.printObject(json);
			return null;
		}

		String telephone = Convert.strToStr(SqlInfusionHelper.filteSqlInfusion(paramMap.get("telephone")), "");// 居住电话
		if (StringUtils.isBlank(telephone)) {
			json.put("msg", "请正确填写入你的家庭电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (!StringUtils.isBlank(telephone))
			if (telephone.trim().length() < 7 || telephone.trim().length() > 13) {
				json.put("msg", "你的家庭电话输入长度不对");
				JSONHelper.printObject(json);
				return null;
			}

		/* 用户头像 */
		String personalHead = SqlInfusionHelper.filteSqlInfusion(paramMap.get("personalHead"));// 个人头像 (默认系统头像)
		if (StringUtils.isBlank(personalHead)) {
			personalHead = null;
			json.put("msg", "请正确上传你的个人头像");
			JSONHelper.printObject(json);
			return null;
		}

		long personId = -1L;

		personId = userAdminService.updateUserBaseData(realName, cellPhone,
				sex, birthday, highestEdu, eduStartDay, school, maritalStatus,
				hasChild, hasHourse, hasHousrseLoan, hasCar, hasCarLoan,
				nativePlacePro, nativePlaceCity, registedPlacePro,
				registedPlaceCity, address, telephone, personalHead, userId,
				idNo);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);

		if (personId > 0) {
			json.put("msg", "保存成功");
			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "管理员修改用户基础数据成功", 2);
			JSONHelper.printObject(json);
			return null;
			// 成功
		} else {
			json.put("msg", "保存失败");
			operationLogService.addOperationLog("t_person",
					admin.getUserName(), IConstants.UPDATE, admin.getLastIP(),
					0, "管理员修改用户基础数据失败", 2);
			// 失败
			JSONHelper.printObject(json);
			return null;
		}

	}

	public String updateUserWorkDataAdmin() throws Exception {
		JSONObject json = new JSONObject();
		Long userId = Convert.strToLong(paramMap.get("uid"), -1L);
		if (userId == -1L) {
			json.put("msg", "用户为空");
			JSONHelper.printObject(json);
			return null;
		}
		String orgName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("orgName"));
		if (StringUtils.isBlank(orgName)) {
			json.put("msg", "请正确填写公司名字");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > orgName.length() || 50 < orgName.length()) {
			json.put("msg", "公司名字长度为不小于2和大于50");
			JSONHelper.printObject(json);
			return null;
		}

		String occStatus =SqlInfusionHelper.filteSqlInfusion( paramMap.get("occStatus"));
		if (StringUtils.isBlank(occStatus)) {
			json.put("msg", "请填写职业状态");
			JSONHelper.printObject(json);
			return null;
		}
		Long workPro = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap.get("workPro")), -1L);
		if (workPro == null || workPro == -1L) {
			json.put("msg", "请填写工作城市省份");
			JSONHelper.printObject(json);
			return null;
		}
		Long workCity = Convert.strToLong(SqlInfusionHelper.filteSqlInfusion(paramMap.get("workCity")), -1L);
		if (workCity == null || workCity == -1L) {
			json.put("msg", "请填写工作城市");
			JSONHelper.printObject(json);
			return null;
		}
		String companyType = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyType"));
		if (StringUtils.isBlank(companyType)) {
			json.put("msg", "请填写公司类别");
			JSONHelper.printObject(json);
			return null;
		}
		String companyLine =SqlInfusionHelper.filteSqlInfusion( paramMap.get("companyLine"));
		if (StringUtils.isBlank(companyLine)) {
			json.put("msg", "请填写公司行业");
			JSONHelper.printObject(json);
			return null;
		}
		String companyScale = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyScale"));
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
		String monthlyIncome = SqlInfusionHelper.filteSqlInfusion(paramMap.get("monthlyIncome"));
		if (StringUtils.isBlank(monthlyIncome)) {
			json.put("msg", "请填写月收入");
			JSONHelper.printObject(json);
			return null;
		}
		String workYear = SqlInfusionHelper.filteSqlInfusion(paramMap.get("workYear"));
		if (StringUtils.isBlank(workYear)) {
			json.put("msg", "请填写现单位工作年限");
			JSONHelper.printObject(json);
			return null;
		}
		String companyTel = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyTel"));
		if (StringUtils.isBlank(companyTel)) {
			json.put("msg", "请正确填写公司电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (companyTel.trim().length() < 7 || companyTel.trim().length() > 13) {
			json.put("msg", "请正确填写公司电话");
			JSONHelper.printObject(json);
			return null;
		}

		String workEmail = SqlInfusionHelper.filteSqlInfusion(paramMap.get("workEmail"));
		if (StringUtils.isBlank(workEmail)) {
			json.put("msg", "请填写工作邮箱");
			JSONHelper.printObject(json);
			return null;
		}
		String companyAddress = SqlInfusionHelper.filteSqlInfusion(paramMap.get("companyAddress"));
		if (StringUtils.isBlank(companyAddress)) {
			json.put("msg", "请填写公司地址");
			JSONHelper.printObject(json);
			return null;
		}
		String directedName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("directedName"));
		if (StringUtils.isBlank(directedName)) {
			json.put("msg", "请填写直系人姓名");
			JSONHelper.printObject(json);
			return null;
		} else if (2 > directedName.length() || 50 < directedName.length()) {
			json.put("msg", "直系人姓名长度为不小于2和大于50");
			JSONHelper.printObject(json);
			return null;
		}

		String directedRelation = SqlInfusionHelper.filteSqlInfusion(paramMap.get("directedRelation"));
		if (StringUtils.isBlank(directedRelation)) {
			json.put("msg", "请填写直系人关系");
			JSONHelper.printObject(json);
			return null;
		}
		String directedTel = SqlInfusionHelper.filteSqlInfusion(paramMap.get("directedTel"));
		if (StringUtils.isBlank(directedTel)) {
			json.put("msg", "请正确填写直系人电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (!StringUtils.isNumeric(directedTel)) {
			json.put("msg", "请正确确填写直系人电话");
			JSONHelper.printObject(json);
			return null;
		}
		if (directedTel.trim().length() != 11) {
			json.put("msg", "请正确填写直系人电话长度错误");
			JSONHelper.printObject(json);
			return null;
		}

		String otherName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("otherName"));
		if (!StringUtils.isBlank(otherName.toString())) {
			if (2 > otherName.length() || 50 < otherName.length()) {
				json.put("msg", "其他人姓名长度为不小于2和大于50");
				JSONHelper.printObject(json);
				return null;
			}
		}

		String otherRelation = SqlInfusionHelper.filteSqlInfusion(paramMap.get("otherRelation"));
//		if (StringUtils.isBlank(otherRelation)) {
//			json.put("msg", "请填写其他人关系");
//			JSONUtils.printObject(json);
//			return null;
//		}
		String otherTel = SqlInfusionHelper.filteSqlInfusion(paramMap.get("otherTel"));
//		if (StringUtils.isBlank(otherTel)) {
//			json.put("msg", "请正确填写其他人联系电话");
//			JSONUtils.printObject(json);
//			return null;
//		}
//
//		if (!StringUtils.isNumeric(otherTel)) {
//			json.put("msg", "请正确填写其他人联系电话");
//			JSONUtils.printObject(json);
//			return null;
//		}
//		if (otherTel.trim().length() != 11) {
//			json.put("msg", "请真确填写直系人电话长度错误");
//			JSONUtils.printObject(json);
//			return null;
//		}

		String moredName = SqlInfusionHelper.filteSqlInfusion(paramMap.get("moredName"));
		if (!StringUtils.isBlank(moredName)) {
			if (2 > moredName.length() || 50 < moredName.length()) {
				json.put("msg", "更多联系人姓名长度为不小于2和大于50");
				JSONHelper.printObject(json);
				return null;
			}
		}
		String moredRelation = SqlInfusionHelper.filteSqlInfusion(paramMap.get("moredRelation"));
//		if (StringUtils.isBlank(moredRelation)) {
//			json.put("msg", "morereation");
//			JSONUtils.printObject(json);
//			return null;
//		}
		String moredTel = SqlInfusionHelper.filteSqlInfusion(paramMap.get("moredTel"));
//		if (StringUtils.isBlank(moredTel)) {
//			json.put("msg", "moretel");
//			JSONUtils.printObject(json);
//			return null;
//		}
//		if (!StringUtils.isNumeric(moredTel)) {
//			json.put("msg", "moretel");
//			JSONUtils.printObject(json);
//			return null;
//		}
//		if (moredTel.trim().length() != 11) {
//			json.put("msg", "请真确填写直系人电话长度错误");
//			JSONUtils.printObject(json);
//			return null;
//		}
		// 用户Id
		Long result = -1L;

		result = userAdminService.updateUserWorkData(orgName, occStatus,
				workPro, workCity, companyType, companyLine, companyScale, job,
				monthlyIncome, workYear, companyTel, workEmail, companyAddress,
				directedName, directedRelation, directedTel, otherName,
				otherRelation, otherTel, moredName, moredRelation, moredTel,
				userId);
		Admin admin = (Admin) session().getAttribute(IConstants.SESSION_ADMIN);
		if (result > 0) {
			json.put("msg", "保存成功");
			operationLogService.addOperationLog("t_workauth", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"管理员修改用户工作资料成功", 2);
			JSONHelper.printObject(json);
			return null;
		} else {
			json.put("msg", "保存失败");
			operationLogService.addOperationLog("t_workauth", admin
					.getUserName(), IConstants.UPDATE, admin.getLastIP(), 0,
					"管理员修改用户工作资料失败", 2);
			JSONHelper.printObject(json);
			return null;
		}

	}

	public String ajaxqueryRegionadmin() throws Exception {
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

}
