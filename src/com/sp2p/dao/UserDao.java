package com.sp2p.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.fp2p.helper.shove.DBReflectHelper;
import com.fp2p.helper.shove.DataSetHelper;
import com.shove.Convert;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.admin.UserManageDao;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_user;

public class UserDao {

	private UserManageDao userManageDao;
	/**
	 * 添加用户
	 * 
	 * @param conn
	 * @param email
	 * @param userName
	 * @param password
	 * @param refferee
	 * @param lastDate
	 * @param lastIP
	 * @param dealpwd
	 * @param mobilePhone
	 * @param rating
	 * @param creditrating
	 * @param status
	 * @param vipcreatetime
	 * @param creditlimit
	 * @param authstep
	 * @param headImg
	 * @return
	 * @throws SQLException
	 */
	public Long addUser(Connection conn, String email, String userName,
			String password, String refferee, String lastDate, String lastIP,
			String dealpwd, String mobilePhone, Integer rating,
			Integer creditrating, Integer vipstatus, String vipcreatetime,
			Integer authstep, String headImg, Integer enable,
			Long servicePersonId,double creditLimit) throws SQLException {

		Dao.Tables.t_user user = new Dao().new Tables().new t_user();

		user.email.setValue(email);
		user.username.setValue(userName);
		user.password.setValue(password);
		user.lastDate.setValue(lastDate);
		user.refferee.setValue(refferee);
		user.dealpwd.setValue(password);
		if (StringUtils.isNotBlank(lastIP)) {
			user.lastIP.setValue(lastIP);
		}
		
		user.creditLimit.setValue(creditLimit);
		user.usableCreditLimit.setValue(creditLimit);
		user.authStep.setValue(authstep);
		user.mobilePhone.setValue(mobilePhone);
		user.rating.setValue(rating);
		user.creditrating.setValue(creditrating);
		user.vipStatus.setValue(vipstatus);
		user.vipCreateTime.setValue(vipcreatetime);
		user.headImg.setValue(headImg);
		user.enable.setValue(enable);
		user.createTime.setValue(new Date());
		return user.insert(conn);
	}

	/**
	 * 初始化资料认证
	 * 
	 * @param conn
	 * @param userId
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public synchronized Long addMaterialsauth1(Connection conn, Long userId, long type)
			throws Exception {
		// 初始化资料认证
		Dao.Tables.t_materialsauth materialsauth = new Dao().new Tables().new t_materialsauth();
		materialsauth.materAuthTypeId.setValue(type);// 默认为16种类型
		materialsauth.userId.setValue(userId);
		return materialsauth.insert(conn);
	}

	/**
	 * 统计有多少图片类型
	 * 
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> querymaterialsauthtypeCount(Connection conn)
			throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select COUNT(*) as cccc from t_materialsauthtype ");
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql= null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 添加用户的基本资料
	 * 
	 * @return
	 */
	public Long addUserBaseData(Connection conn, String realName,
			String cellPhone, String sex, String birthday, String highestEdu,
			String eduStartDay, String school, String maritalStatus,
			String hasChild, String hasHourse, String hasHousrseLoan,
			String hasCar, String hasCarLoan, Long nativePlacePro,
			Long nativePlaceCity, Long registedPlacePro,
			Long registedPlaceCity, String address, String telephone,
			String personalHead, Long userId, String idNo) throws SQLException {
		Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		person.realName.setValue(realName);
		person.cellPhone.setValue(cellPhone);
		person.sex.setValue(sex);
		person.birthday.setValue(birthday);
		person.highestEdu.setValue(highestEdu);
		person.eduStartDay.setValue(eduStartDay);
		person.school.setValue(school);
		person.maritalStatus.setValue(maritalStatus);
		person.hasChild.setValue(hasChild);
		person.hasHourse.setValue(hasHourse);
		person.hasHousrseLoan.setValue(hasHousrseLoan);
		person.hasCar.setValue(hasCar);
		person.hasCarLoan.setValue(hasCarLoan);
		person.nativePlacePro.setValue(nativePlacePro);
		person.nativePlaceCity.setValue(nativePlaceCity);
		person.registedPlacePro.setValue(registedPlacePro);
		person.registedPlaceCity.setValue(registedPlaceCity);
		person.address.setValue(address);
		person.telephone.setValue(telephone);
		person.userId.setValue(userId);
		person.idNo.setValue(idNo);
		person.personalHead.setValue(personalHead);
		return person.insert(conn);
	}

	/**
	 * 添加图片
	 * 
	 * @param conn
	 * @param materAuthTypeId
	 * @param imgPath
	 * @param auditStatus
	 * @param userId
	 * @param authTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addImage(Connection conn, long materAuthTypeId, String imgPath,
			long userId) throws SQLException, DataException {
		Dao.Tables.t_materialsauth materialsauth = new Dao().new Tables().new t_materialsauth();
		materialsauth.materAuthTypeId.setValue(materAuthTypeId);
		materialsauth.imgPath.setValue(imgPath);

		Map<String, String> map = null;
		Map<String, String> Accmap = null;
		if (userId > 0) {
			DataSet matondataSet = materialsauth.open(conn, "",
					" 1 = 1 AND materAuthTypeId = " + materAuthTypeId
							+ " AND userId =" + userId
							+ " AND imgPath is not null", "", -1, -1);
			map = DataSetHelper.dataSetToMap(matondataSet);
		}
		if (map != null && map.size() > 0) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = format.format(new Date());
			materialsauth.passTime.setValue(date);
			materialsauth.auditStatus.setValue(1);
			// 审核之后不可以修改
			return materialsauth.update(conn, "materAuthTypeId = "
					+ materAuthTypeId + " AND userId = " + userId);
		} else {
			// 如果更新的那么更新user表中的步骤状态值
			materialsauth.userId.setValue(userId);
			Dao.Tables.t_user user = new Dao().new Tables().new t_user();

			Accmap = queryPicturStatuCount(conn, userId);
			Integer alli = 0;
			if (Accmap != null && Accmap.size() > 0) {

				alli = Convert.strToInt(Accmap.get("ccc"), 0);
				if (alli >= 4) {
					user.authStep.setValue(5);
					user.update(conn, " id = " + userId);
				}
			}

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = format.format(new Date());
			materialsauth.passTime.setValue(date);
			materialsauth.auditStatus.setValue(1);
			return materialsauth.update(conn, "materAuthTypeId = "
					+ materAuthTypeId + " AND userId = " + userId);
		}

	}

	public Long updateUserauthod(Connection conn, Long userId) {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Map<String, String> Accmap = null;// 统计t_materialsauth用户类型
		try {
			Accmap = queryPicturStatuCount(conn, userId);
			Integer alli = 0;
			if (Accmap != null && Accmap.size() > 0) {
				alli = Convert.strToInt(Accmap.get("ccc"), 0);
				if (alli >= 5) {
					user.authStep.setValue(5);
					return user.update(conn, " id = " + userId);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		}
		return 1L;
	}
	/**
	 * 增加系统日志 (只用于这个类)
	 * @param conn
	 * @param operation_table
	 * @param operation_user
	 * @param operation_type
	 * @param operation_ip
	 * @param operation_money
	 * @param operation_remarks
	 * @param operation_around
	 * @return
	 * @throws SQLException
	 */
	 public  long  addOperationLog(Connection  conn,String operation_table,String operation_user,int  operation_type,String operation_ip,
			  double operation_money,String operation_remarks ,int operation_around) throws SQLException{
			Dao.Tables.t_operation_log t_opration_log  = new Dao().new Tables().new t_operation_log();
			t_opration_log.operation_table.setValue(operation_table);
			t_opration_log.operation_user.setValue(operation_user);
			t_opration_log.operation_type.setValue(operation_type);
			t_opration_log.operation_ip.setValue(operation_ip);
			t_opration_log.operation_money.setValue(operation_money);
			t_opration_log.operation_remarks.setValue(operation_remarks);
			t_opration_log.operation_around.setValue(operation_around);
			t_opration_log.operation_time.setValue(new Date());
			
			return  t_opration_log.insert(conn);
		}
	/**
	 * 更新用户的基本信息
	 * 
	 * @param conn
	 * @param realName
	 * @param cellPhone
	 * @param sex
	 * @param birthday
	 * @param highestEdu
	 * @param eduStartDay
	 * @param school
	 * @param maritalStatus
	 * @param hasChild
	 * @param hasHourse
	 * @param hasHousrseLoan
	 * @param hasCar
	 * @param hasCarLoan
	 * @param nativePlacePro
	 * @param nativePlaceCity
	 * @param registedPlacePro
	 * @param registedPlaceCity
	 * @param address
	 * @param telephone
	 * @param personalHead
	 * @param userId
	 * @param idNo
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserBaseData(Connection conn, String realName,
			String cellPhone, String sex, String birthday, String highestEdu,
			String eduStartDay, String school, String maritalStatus,
			String hasChild, String hasHourse, String hasHousrseLoan,
			String hasCar, String hasCarLoan, Long nativePlacePro,
			Long nativePlaceCity, Long registedPlacePro,
			Long registedPlaceCity, String address, String telephone,
			String personalHead, Long userId, String idNo,String username,String lastIP) throws SQLException,
			DataException {
		Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		if(StringUtils.isNotBlank(realName)){
		person.realName.setValue(realName);
		}
        person.cellPhone.setValue(cellPhone);
        if(StringUtils.isNotBlank(sex)){	
		person.sex.setValue(sex);
        }
        if(StringUtils.isNotBlank(birthday)){
		person.birthday.setValue(birthday);
        }
		person.highestEdu.setValue(highestEdu);
        
        if(StringUtils.isNotBlank(eduStartDay)){
		person.eduStartDay.setValue(eduStartDay);
        }
		person.school.setValue(school);
		person.maritalStatus.setValue(maritalStatus);
		person.hasChild.setValue(hasChild);
		person.hasHourse.setValue(hasHourse);
		person.hasHousrseLoan.setValue(hasHousrseLoan);
		person.hasCar.setValue(hasCar);
		person.hasCarLoan.setValue(hasCarLoan);
		person.nativePlacePro.setValue(nativePlacePro);
		person.nativePlaceCity.setValue(nativePlaceCity);
		person.registedPlacePro.setValue(registedPlacePro);
		person.registedPlaceCity.setValue(registedPlaceCity);
		person.address.setValue(address);
		person.telephone.setValue(telephone);
		person.userId.setValue(userId);
		person.idNo.setValue(idNo);
		person.personalHead.setValue(personalHead);
		Long result = -1L;
		Map<String, String> map = null;
		try{
		DataSet PersondataSet = person.open(conn, "", "userId = " + userId, "",
				-1, -1);
		map = DataSetHelper.dataSetToMap(PersondataSet);
		}catch (Exception e) {
		  e.printStackTrace();
		  return -1L;
		}
		if (map != null && map.size() > 0) {
			
		    String 	realNamestr = map.get("realName");	
            if(realNamestr!=null&&!realNamestr.equals("")){
            	result =  person.update(conn, "userId = " + userId);	
            	//添加操作日志
				result = addOperationLog(conn, "t_person", username, IConstants.UPDATE, lastIP, 0, "更新个人详细资料", 1);
				return  result;
            }else{
				// 如果更新的那么更新user表中的步骤状态值
		    	Dao.Tables.t_user  user = new Dao().new Tables().new t_user();
		    	
		    		user.authStep.setValue(2);// 2表示填写完基本认证
		    	
				result = user.update(conn, " id = " + userId);
				if (result > 0) {
					result =  person.update(conn, "userId = " + userId);// 如果user表更新成功
					 //添加操作日志
					result = addOperationLog(conn, "t_person", username, IConstants.INSERT, lastIP, 0, "填写个人详细资料", 1);
					return result;
				}
				return result;
		    }
			//return person.update(conn, "userId = " + userId);
		} else {
			// 如果更新的那么更新user表中的步骤状态值
			Dao.Tables.t_user user = new Dao().new Tables().new t_user();
			user.authStep.setValue(2);// 2表示填写完基本认证
			result = user.update(conn, " id = " + userId);
			if (result > 0) {
				result =  person.insert(conn);// 如果user表更新成功
				//添加操作日志
				result = addOperationLog(conn, "t_person", username, IConstants.UPDATE, lastIP, 0, "更新个人详细资料", 1);
				return result;
			}
			return result;
		}
	}
	/** 
	 *    投资人填写个人个信息，后属性状态
	 * @param conn
	 * @param realName
	 * @param cellPhone
	 * @param sex
	 * @param birthday
	 * @param highestEdu
	 * @param eduStartDay
	 * @param school
	 * @param maritalStatus
	 * @param hasChild
	 * @param hasHourse
	 * @param hasHousrseLoan
	 * @param hasCar
	 * @param hasCarLoan
	 * @param nativePlacePro
	 * @param nativePlaceCity
	 * @param registedPlacePro
	 * @param registedPlaceCity
	 * @param address
	 * @param telephone
	 * @param personalHead
	 * @param userId
	 * @param idNo
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserBaseWWc(Connection conn, String realName,
			String cellPhone, String sex, String birthday, String highestEdu,
			String eduStartDay, String school, String maritalStatus,
			String hasChild, String hasHourse, String hasHousrseLoan,
			String hasCar, String hasCarLoan, Long nativePlacePro,
			Long nativePlaceCity, Long registedPlacePro,
			Long registedPlaceCity, String address, String telephone,
			String personalHead, Long userId, String idNo) throws SQLException,
			DataException {
		Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		person.realName.setValue(realName);
		person.cellPhone.setValue(cellPhone);
		person.sex.setValue(sex);
		person.birthday.setValue(birthday);
		person.highestEdu.setValue(highestEdu);
		person.eduStartDay.setValue(eduStartDay);
		person.school.setValue(school);
		person.maritalStatus.setValue(maritalStatus);
		person.hasChild.setValue(hasChild);
		person.hasHourse.setValue(hasHourse);
		person.hasHousrseLoan.setValue(hasHousrseLoan);
		person.hasCar.setValue(hasCar);
		person.hasCarLoan.setValue(hasCarLoan);
		person.nativePlacePro.setValue(nativePlacePro);
		person.nativePlaceCity.setValue(nativePlaceCity);
		person.registedPlacePro.setValue(registedPlacePro);
		person.registedPlaceCity.setValue(registedPlaceCity);
		person.address.setValue(address);
		person.telephone.setValue(telephone);
		person.userId.setValue(userId);
		person.idNo.setValue(idNo);
		person.personalHead.setValue(personalHead);
		Long result = -1L;
		Map<String, String> map = null;
		try{
		DataSet PersondataSet = person.open(conn, "", "userId = " + userId, "",
				-1, -1);
		map = DataSetHelper.dataSetToMap(PersondataSet);
		}catch (Exception e) {
		  e.printStackTrace();
		  return -1L;
		}
		if (map != null && map.size() > 0) {
			
		    String 	realNamestr = map.get("realName");	
            if(realNamestr!=null&&!realNamestr.equals("")){
            	return person.update(conn, "userId = " + userId);	
		    }else{
				// 如果更新的那么更新user表中的步骤状态值
		    	Dao.Tables.t_user  user = new Dao().new Tables().new t_user();
		    	user.authStep.setValue(1);// 2表示填写完基本认证
				result = user.update(conn, " id = " + userId);
				if (result > 0) {
					return person.update(conn, "userId = " + userId);// 如果user表更新成功
				}
				return result;
		    }
		} else {
			// 如果更新的那么更新user表中的步骤状态值
			Dao.Tables.t_user user = new Dao().new Tables().new t_user();
			user.authStep.setValue(1);// 2表示填写完基本认证
			result = user.update(conn, " id = " + userId);
			if (result > 0) {
				return person.insert(conn);// 如果user表更新成功
			}
			return result;
		}
	}
	/**
	 * 
	 * 后台用户基本资料审核
	 * @param conn  
	 * @param userId  用户id
	 * @param auditStatus 审核状态
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserBaseDataCheck(Connection conn, Long userId,
			int auditStatus) throws SQLException, DataException {
		Long result = -1L;
		Integer personauditStatus = -1;
		Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		Map<String, String> Personmap = new HashMap<String, String>();
		try{
		DataSet personds = person.open(conn, "auditStatus", " userId = "
				+ userId, "", -1, -1);
		Personmap = DataSetHelper.dataSetToMap(personds);
		}catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
		Integer precreditrating = -1;// 原来的信用积分
		Map<String, String> map = new HashMap<String, String>();
		DataSet ds = null;
		Map<String, String> wormap = null;
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		try{
		ds = user.open(conn, "creditrating", " id = " + userId, "", -1,
				-1);
		map = DataSetHelper.dataSetToMap(ds);
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
		if (map != null && map.size() > 0) {
			precreditrating = Convert.strToInt(map.get("creditrating"), -1);
		}
	
		
		if (auditStatus == 3){
			user.creditrating.setValue(10 + precreditrating);
			result = user.update(conn, " id = " + userId);// 更新用户的信用积分
			result = addserintegraldetail(conn, userId,10, 
					"用户基本资料审核",1, "用户基本资料审核", "增加");
			if(result<=0){
				return  -1L;
			}
		}else if (auditStatus == 2){
			user.creditrating.setValue(precreditrating - 10);
			result = user.update(conn, " id = " + userId);// 更新用户的信用积分
			result = addserintegraldetail(conn, userId, 10,
					"用户基本资料审核",1, "用户基本资料审核", "减少");
			if(result<=0){
				return  -1L;
			}
		}
			
		person.auditStatus.setValue(auditStatus);
		return person.update(conn, "userId = " + userId);
	}
	
	
	
	/**
	 * 向积分记录表添加记录
	 * @param conn
	 * @param userId
	 * @param score
	 * @param type
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addserintegraldetail(Connection conn,Long userId,Integer score,String typeStr,Integer type,String remark,String changetype) throws SQLException, DataException{
		Dao.Tables.t_userintegraldetail  integraldetail = new Dao().new Tables().new t_userintegraldetail();
		integraldetail.changerecore.setValue(score);
		integraldetail.intergraltype.setValue(typeStr);
		integraldetail.remark.setValue(remark);
		integraldetail.changetype.setValue(changetype);//先设置成增加
		integraldetail.time.setValue(new Date());
		integraldetail.userid.setValue(userId);
		if(type==1){//信用积分
			integraldetail.type.setValue(1);
		}
		if(type==2){//vip积分
			integraldetail.type.setValue(2);
		}
		
		return  integraldetail.insert(conn);
	}
	
	

	/**
	 * 
	 * 添加用户的工作认证资料
	 * 
	 * @param conn
	 * @param orgName
	 * @param occStatus
	 * @param workPro
	 * @param workCity
	 * @param companyType
	 * @param companyLine
	 * @param companyScale
	 * @param job
	 * @param monthlyIncome
	 * @param workYear
	 * @param companyTel
	 * @param workEmail
	 * @param companyAddress
	 * @param directedName
	 * @param directedRelation
	 * @param directedTel
	 * @param otherName
	 * @param otherRelation
	 * @param otherTel
	 * @param moredName
	 * @param moredRelation
	 * @param moredTel
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public Long addUserWorkData(Connection conn, String orgName,
			String occStatus, Long workPro, Long workCity, String companyType,
			String companyLine, String companyScale, String job,
			String monthlyIncome, String workYear, String companyTel,
			String workEmail, String companyAddress, String directedName,
			String directedRelation, String directedTel, String otherName,
			String otherRelation, String otherTel, String moredName,
			String moredRelation, String moredTel, Long userId)
			throws SQLException {
		Dao.Tables.t_workauth workauth = new Dao().new Tables().new t_workauth();
		workauth.orgName.setValue(orgName);
		workauth.occStatus.setValue(occStatus);
		workauth.workPro.setValue(workPro);
		workauth.workCity.setValue(workCity);
		workauth.companyType.setValue(companyType);
		workauth.companyLine.setValue(companyLine);
		workauth.companyScale.setValue(companyScale);
		workauth.job.setValue(job);
		workauth.monthlyIncome.setValue(monthlyIncome);
		workauth.workYear.setValue(workYear);
		workauth.companyTel.setValue(companyTel);
		workauth.workEmail.setValue(workEmail);
		workauth.companyAddress.setValue(companyAddress);
		workauth.directedName.setValue(directedName);
		workauth.directedRelation.setValue(directedRelation);
		workauth.directedTel.setValue(directedTel);
		workauth.otherName.setValue(otherName);
		workauth.otherRelation.setValue(otherName);
		workauth.otherTel.setValue(otherTel);
		workauth.moredName.setValue(moredName);
		workauth.moredRelation.setValue(moredRelation);
		workauth.moredTel.setValue(moredTel);
		workauth.userId.setValue(userId);
		return workauth.insert(conn);
	}

	/**
	 * 修改用户工作认证资料
	 * 
	 * @param conn
	 * @param orgName
	 * @param occStatus
	 * @param workPro
	 * @param workCity
	 * @param companyType
	 * @param companyLine
	 * @param companyScale
	 * @param job
	 * @param monthlyIncome
	 * @param workYear
	 * @param companyTel
	 * @param workEmail
	 * @param companyAddress
	 * @param directedName
	 * @param directedRelation
	 * @param directedTel
	 * @param otherName
	 * @param otherRelation
	 * @param otherTel
	 * @param moredName
	 * @param moredRelation
	 * @param moredTel
	 * @param userId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserWorkData(Connection conn, String orgName,
			String occStatus, Long workPro, Long workCity, String companyType,
			String companyLine, String companyScale, String job,
			String monthlyIncome, String workYear, String companyTel,
			String workEmail, String companyAddress, String directedName,
			String directedRelation, String directedTel, String otherName,
			String otherRelation, String otherTel, String moredName,
			String moredRelation, String moredTel, Long userId)
			throws SQLException, DataException {
		Dao.Tables.t_workauth workauth = new Dao().new Tables().new t_workauth();
		workauth.orgName.setValue(orgName);
		workauth.occStatus.setValue(occStatus);
		workauth.workPro.setValue(workPro);
		workauth.workCity.setValue(workCity);
		workauth.companyType.setValue(companyType);
		workauth.companyLine.setValue(companyLine);
		workauth.companyScale.setValue(companyScale);
		workauth.job.setValue(job);
		workauth.monthlyIncome.setValue(monthlyIncome);
		workauth.workYear.setValue(workYear);
		workauth.companyTel.setValue(companyTel);
		workauth.workEmail.setValue(workEmail);
		workauth.companyAddress.setValue(companyAddress);
		workauth.directedName.setValue(directedName);
		workauth.directedRelation.setValue(directedRelation);
		workauth.directedTel.setValue(directedTel);
		workauth.otherName.setValue(otherName);
		workauth.otherRelation.setValue(otherRelation);
		workauth.otherTel.setValue(otherTel);
		workauth.moredName.setValue(moredName);
		workauth.moredRelation.setValue(moredRelation);
		workauth.moredTel.setValue(moredTel);
		DataSet PersondataSet = workauth.open(conn, "", "userId = " + userId,
				"", -1, -1);
		Map<String, String> map = new HashMap<String, String>();
		try{
		map = DataSetHelper.dataSetToMap(PersondataSet);}
		catch (Exception e) {
			e.printStackTrace();
			return -1L;
		}
		if (map != null && map.size() > 0) {
			return workauth.update(conn, "userId = " + userId);
		} else {
			workauth.userId.setValue(userId);
			return workauth.insert(conn);

		}

	}

	/**
	 * 修改用户
	 * 
	 * @param conn
	 * @param id
	 *            用户编号
	 * @param email
	 *            电子邮箱
	 * @param userName
	 *            用户名
	 * @param password
	 *            用户密码
	 * @param name
	 *            真实姓名
	 * @param gender
	 *            性别
	 * @param mobilePhone
	 *            手机号码
	 * @param qq
	 * @param provinceId
	 *            省Id
	 * @param cityId
	 *            城市id
	 * @param areaId
	 *            区/镇/县id
	 * @param postalcode
	 *            邮政编码
	 * @param headImg
	 *            头像
	 * @param status
	 *            邮箱是否验证通过 (0:未通过1:通过)
	 * @param balances
	 *            E币账户余额
	 * @param enable
	 *            是否禁用 1、启用 2、禁用
	 * @param rating
	 *            会员等级(1:普通会员2:铜牌会员3:银牌会员4:金牌会员)
	 * @param lastDate
	 *            最后登录时间
	 * @param lastIP
	 *            最后登录ip
	 * @throws SQLException
	 * @return Long
	 */
	public Long updateUser(Connection conn, Long id, String email,
			String userName, String password, String lastDate, String lastIP)
			throws SQLException {

		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		if (email != null) {
			user.email.setValue(email);
		}
		if (password != null) {
			password = com.shove.security.Encrypt.MD5(password.trim());
			user.password.setValue(password);
		}
		if (userName != null) {
			user.username.setValue(userName);
		}

		return user.update(conn, " id=" + id);
	}

	// 更新用户的认证状态
	public Long updateUser(Connection conn, Long uerId, int authStep)
			throws SQLException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.authStep.setValue(authStep);
		return user.update(conn, "id = " + uerId);

	}

	// 更新用户的vip状态
	public Long updateUser(Connection conn, Long uerId, int authStep,
			int vipStatus, int servicePersonId, String content, String vipFee)
			throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.authStep.setValue(authStep);
		user.kefuId.setValue(servicePersonId);
		user.content.setValue(content);
		user.vipCreateTime.setValue(new Date());
		user.vipStatus.setValue(2);// 修改vip状态 2为vip状态
		BigDecimal vipFeedecimal = new BigDecimal(vipFee);
		user.vipFee.setValue(vipFeedecimal);
		//----modify by houli 申请vip时，feeStatus=1 1为代扣  2为已扣
		//user.feeStatus.setValue(2);//为扣费用
		user.feeStatus.setValue(1);
		//---------------
		return user.update(conn, "id = " + uerId);
	}

	public int deleteUser(Connection conn, String userIds, String delimiter)
			throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.delete(conn, "id=" + userIds);
		return 0;
	}

	/**
	 * 判断是否用户或者email重复
	 * 
	 * @param conn
	 * @param email
	 * @param userName
	 * @throws SQLException
	 * @throws DataException
	 * @return List<Map<String,Object>>
	 */
	public List<Map<String, Object>> isUserEmaiORUseName(Connection conn,
			String email, String userName) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		StringBuffer condition = new StringBuffer();
		condition.append(" 1=1");
		if (StringUtils.isNotBlank(email)) {
			condition.append(" AND email= '" + StringEscapeUtils.escapeSql(email) + "'");
		}
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" AND userName= '" + StringEscapeUtils.escapeSql(userName) + "'");
		}

		DataSet dataSet = user
				.open(conn, "*", condition.toString(), "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		condition = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}
		

	// ===============================================================
	/**
	 * 用户登录时候验证邮箱和用户名是否激活==========================
	 */
	public List<Map<String, Object>> isUEjihuo(Connection conn, String email,
			String userName) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		StringBuffer condition = new StringBuffer();
		condition.append(" 1=1");
		// 邮箱不空 用户名空
		if (StringUtils.isNotBlank(email) && !StringUtils.isNotBlank(userName)) {
			condition.append(" AND email= '" + StringEscapeUtils.escapeSql(email) + "' AND enable = 2");
			// 邮箱和用户名都不空
		} else if (StringUtils.isNotBlank(email)
				&& StringUtils.isNotBlank(userName)) {
			condition.append(" AND email= '" + StringEscapeUtils.escapeSql(email) + "'");
		}
		// 邮箱空 用户名不空
		if (!StringUtils.isNotBlank(email) && StringUtils.isNotBlank(userName)) {
			condition
					.append(" AND userName= '" + StringEscapeUtils.escapeSql(userName) + "' AND enable = 2");
		}

		DataSet dataSet = user
				.open(conn, "*", condition.toString(), "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		condition = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}

	// 没有检测enable状态
	public List<Map<String, Object>> isUEjihuo_(Connection conn, String email,
			String userName) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		StringBuffer condition = new StringBuffer();
		condition.append(" 1=1");
		if (StringUtils.isNotBlank(email)) {
			condition.append(" AND email= '" + StringEscapeUtils.escapeSql(email) + "'");
		}
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" AND userName= '" + StringEscapeUtils.escapeSql(userName) + "'");
		}

		DataSet dataSet = user
				.open(conn, "*", condition.toString(), "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		condition = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 用户登录时候验证邮箱和用户名是否激活==========================
	 */
	// ======================================================
	/**
	 * 根据用户id查询用户信息
	 * 
	 * @param conn
	 * @param id
	 *            用户编号
	 * @throws SQLException
	 * @throws DataException
	 * @return Map<String,String>
	 */
	public Map<String, String> queryUserById(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet dataSet = user.open(conn, "*", " id=" + id, " id desc ", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 查询用户前台的图片信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryUserPictureStatus(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_user_frontpictur frontMeg = new Dao().new Views().new v_t_user_frontpictur();
		DataSet dataSet = frontMeg.open(conn, "", " id=" + id, "", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 查询用户前台五大基本资料信息和显示详细图片的第一张
	 * 
	 * @param conn
	 * @param id
	 *            用户的id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryBasePicture(Connection conn, Long id)
			throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" tm.userId as id, ");
		sql.append(" tuser.username username, ");
		sql.append(" tmy.`name` as tmyname, ");
		sql
				.append(" tm.auditStatus as auditStatus,tm.id as tmid,tmy.id as tmyid,vts.imagePath as imagePath ");
		sql.append(" from t_materialsauth tm  ");
		sql
				.append(" left join t_materialsauthtype tmy on tm.materAuthTypeId  = tmy.id   ");
		sql.append(" left join t_user tuser on tuser.id = tm.userId ");
		sql
				.append(" left join v_t_user_showfirstpicture vts on vts.tmid = tm.id ");
		sql.append(" where tmy.id <= 5 AND  tuser.id =  " + id);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sql = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
	
	/**
	 * 根据IPS账号查询用户信息
	 * @param conn
	 * @param pIpsBillNo
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public boolean queryUserByIpsBillNo(Connection conn, String pIpsBillNo) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet dataSet = user.open(conn, " * ", " portMerBillNo = '" + pIpsBillNo + "'", "", -1, -1);
		Map<String, String> dataMap = DataSetHelper.dataSetToMap(dataSet);
		return dataMap == null ? false : true;
	}

	/**
	 * 查询用户前台可选大基本资料信息和显示详细图片的第一张
	 * 
	 * @param conn
	 * @param id
	 *            用户的id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> querySelectPicture(Connection conn, Long id)
			throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" tm.userId as id, ");
		sql.append(" tuser.username username, ");
		sql.append(" tmy.`name` as tmyname, ");
		sql
				.append(" tm.auditStatus as auditStatus,tm.id as tmid,tmy.id as tmyid,vts.imagePath as imagePath ");
		sql.append(" from t_materialsauth tm  ");
		sql
				.append(" left join t_materialsauthtype tmy on tm.materAuthTypeId  = tmy.id   ");
		sql.append(" left join t_user tuser on tuser.id = tm.userId ");
		sql
				.append(" left join v_t_user_showfirstpicture vts on vts.tmid = tm.id ");
		sql.append(" where tmy.id > 5 AND  tuser.id =  " + id);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sql = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 查询每一个证件类型下的明细
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> queryPerTyhpePicture(Connection conn,
			Long tmid) throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append(" tmd.id as id,tmd.imagePath, ");
		sql
				.append(" tmd.auditStatus as auditStatus ,tmy.`name` as tmyname,tm.materAuthTypeId as materAuthTypeId,tmd.visiable as visiable  ");
		sql.append(" from t_materialimagedetal tmd ");
		sql
				.append(" left join t_materialsauth tm on  tmd.materialsauthid = tm.id ");
		sql
				.append(" left join t_materialsauthtype tmy on tm.materAuthTypeId = tmy.id ");
		sql.append(" where tmd.materialsauthid =  " + tmid);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		sql = null;
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 查询图片类型
	 * 
	 * @param conn
	 * @param tmId
	 *            证件主表类型的id
	 * @param userId
	 *            用户id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryPitcturTyep(Connection conn, Long tmId,
			Long userId) throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql
				.append("select tm.materAuthTypeId  from t_materialsauth tm  where tm.userId = "
						+ userId + " and tm.id = " + tmId);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 计算用户的图片上传个数基本资料的上传个数
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryPicturStatuCount(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Views.v_t_user_verypictur verypictur = new Dao().new Views().new v_t_user_verypictur();
		DataSet dataSet = verypictur.open(conn, "", " id=" + id, "", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 根据用户名查用户id
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryIdByUser(Connection conn, String userName)
			throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet dataSet = user.open(conn, "id", " username = '" + StringEscapeUtils.escapeSql(userName) + "'" +
				" or email = '" + StringEscapeUtils.escapeSql(userName) + "'" +
				" or mobilePhone = '" + StringEscapeUtils.escapeSql(userName) + "'",
				"", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 根据邮箱查找用户id
	 * @param conn
	 * @param email 邮箱.
	 * @return
	 * @throws DataException
	 */
	public Map<String, String> queryIdByEmail(Connection conn, String email) throws DataException{
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet dataSet = user.open(conn, "id", " email = '" + StringEscapeUtils.escapeSql(email) + "'","", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	
	/**
	 * 查询图片id
	 * 
	 * @param conn
	 * @param tmId
	 *            证件主表类型的id
	 * @param userId
	 *            用户id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryPitcturId(Connection conn, Long tmId,
			Long userId) throws SQLException, DataException {
		StringBuffer sql = new StringBuffer();
		sql
				.append("select tm.id as id from t_materialsauth tm  where tm.userId = "
						+ userId + " and tm.materAuthTypeId = " + tmId);
		DataSet dataSet = MySQL.executeQuery(conn, sql.toString());
		sql = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 忘记密码
	 * 
	 * @param conn
	 * @param username
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryPassword(Connection conn, String email)
			throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet dataSet = user.open(conn, "", " email = '" + StringEscapeUtils.escapeSql(email) + "'", "",
				-1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 客服列表的内容
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, Object>> querykefylist(Connection conn)
			throws SQLException, DataException {
		Dao.Tables.t_user_kefu kefu = new Dao().new Tables().new t_user_kefu();
		DataSet dataSet = kefu.open(conn, "", "", "", -1, -1);
		dataSet.tables.get(0).rows.genRowsMap();
		return dataSet.tables.get(0).rows.rowsMap;
	}

	/**
	 * 用户基本信息
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryPersonById(Connection conn, long id)
			throws SQLException, DataException {
		Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		DataSet dataSet = person.open(conn, "", "userId = " + id, "", -1, -1);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 查询用户的工作状态值
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryWorkState(Connection conn, long id)
			throws SQLException, DataException {
		DataSet dataSet = MySQL
				.executeQuery(
						conn,
						"select COUNT(*) as 'total'  from t_materialsauth tm where tm.userId = "
								+ id
								+ " AND tm.auditStatus = 3  AND tm.materAuthTypeId > 5");
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 查询vip页面状态参数
	 * 
	 * @param conn
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryVipParamList(Connection conn, long id)
			throws SQLException, DataException {
		DataSet dataSet = MySQL
				.executeQuery(
						conn,
						"SELECT tuk.id as kfid, tuser.id as id ,tuser.username as username,tuser.email as email,tuser.vipStatus as vipStatus,tp.realName as realName,tuk.`name` as kefuname from t_user tuser LEFT join t_person tp on tuser.id = tp.userId left join t_user_kefu tuk on tuser.kefuId = tuk.id where tuser.id = "
								+ id);
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 该用户上传资料的类型的审核状态
	 * 
	 * @throws DataException
	 * @throws SQLException
	 */
	public Long updateUserPicturStatus(Connection conn, Long id,
			Long materAuthTypeId) throws SQLException, DataException {
		Dao.Tables.t_materialsauth materialsauth = new Dao().new Tables().new t_materialsauth();
			materialsauth.auditStatus.setValue(1);// 设置主表的状态值1 为等待审核
			return materialsauth.update(conn, " userId = " + id
					+ " AND materAuthTypeId = " + materAuthTypeId);
	}

	/**
	 * 增加用户的图片
	 * 
	 * @param conn
	 * @param id
	 * @param pasttime
	 * @param materAuthTypeId
	 * @param auditStatus
	 * @param uploadingTime
	 * @param imagePath
	 * @param materialsauthid
	 * @return
	 * @throws SQLException
	 */
	public synchronized Long  addUserImage(Connection conn, Integer auditStatus,
			String uploadingTime, String imagePath, Long materialsauthid,Integer visable)
			throws SQLException {
		Dao.Tables.t_materialimagedetal materialImagedetal = new Dao().new Tables().new t_materialimagedetal();
		materialImagedetal.auditStatus.setValue(auditStatus);
		materialImagedetal.imagePath.setValue(imagePath);
		materialImagedetal.visiable.setValue(visable);
		materialImagedetal.uploadingTime.setValue(new Date());
		materialImagedetal.materialsauthid.setValue(materialsauthid);
		return materialImagedetal.insert(conn);
	}

	/**
	 * 重置图片的可见性 重置为不可见
	 * 
	 * @param conn
	 * @param tmid
	 * @return
	 * @throws SQLException
	 */
	public Long updatematerialImagedetalvisiable(Connection conn, Long tmid)
			throws SQLException {
		Dao.Tables.t_materialimagedetal materialImagedetal = new Dao().new Tables().new t_materialimagedetal();
		materialImagedetal.visiable.setValue(2);
		return materialImagedetal.update(conn, " materialsauthid = " + tmid);
	}

	/**
	 * 更新用户上传图片的可见性
	 * 
	 * @param conn
	 * @param tmdid
	 * @return
	 * @throws SQLException
	 */
	public Long updatevisiable(Connection conn, Long tmdid) throws SQLException {
		Dao.Tables.t_materialimagedetal Imagedetal = new Dao().new Tables().new t_materialimagedetal();
		Imagedetal.visiable.setValue(1);// 1为可见 2 为不可见
		return Imagedetal.update(conn, " id = " + tmdid);
	}

	/**
	 * 修改用户邮箱验证状态
	 * 
	 * @param conn
	 * @param id
	 *            用户编号
	 * @param status
	 *            邮箱是否验证通过 (0:未通过1:通过)
	 * @throws SQLException
	 * @return Long
	 */
	public Long updateUserEmailStatus(Connection conn, Long id, Integer status)
			throws SQLException {

		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		if (status != null) {
			// user.status.setValue(status);
		}
		return user.update(conn, " id=" + id);
	}

	/**
	 * 修改用户密码
	 * 
	 * @param conn
	 * @param id
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public Long updateUserPassword(Connection conn, Long id, String password)
			throws SQLException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		if ("1".equals(IConstants.ENABLED_PASS)){
			password = com.shove.security.Encrypt.MD5(password.trim());
		}else{
			password = com.shove.security.Encrypt.MD5(password.trim()+IConstants.PASS_KEY);
		}
		user.password.setValue(password);
		return user.update(conn, " id=" + id);
	}

	/**
	 * 更新黑名单用户
	 * 
	 * @param conn
	 * @param id
	 * @param enable
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateEnable(Connection conn, Long id, Integer enable)
			throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		if (enable != null) {
			user.enable.setValue(enable);
		}
		return user.update(conn, " id=" + id);

	}

	/**
	 * 更新锁定用户状态
	 * 
	 * @param conn
	 * @param ids
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	public Long updateLockedStatus(Connection conn, String ids, int enable)
			throws SQLException {
		String idStr = StringEscapeUtils.escapeSql("'"+ids+"'");
		String idSQL = "-2";
		idStr = idStr.replaceAll("'", "");
		String [] array = idStr.split(",");
		for(int n=0;n<=array.length-1;n++){
		   idSQL += ","+array[n];
		}
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.enable.setValue(enable);
		if (enable == 1) {
			user.lockTime.setValue(null);
		} else {
			user.lockTime.setValue(new Date());
		}
		return user.update(conn, " id in (" +idSQL
				+ ")");
	}

	/**
	 * 更新锁定提现状态
	 * 
	 * @param conn
	 * @param ids
	 * @param cashStatus
	 * @return
	 * @throws SQLException
	 */
	public Long updateUserCashStatus(Connection conn, String ids,
			String cashStatus) throws SQLException {
		String idStr = StringEscapeUtils.escapeSql("'"+ids+"'");
		String idSQL = "-2";
		idStr = idStr.replaceAll("'", "");
		String [] array = idStr.split(",");
		for(int n=0;n<=array.length-1;n++){
		   idSQL += ","+array[n];
		}
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.cashStatus.setValue(StringEscapeUtils.escapeSql(cashStatus));
		return user.update(conn, " id in (" + idSQL
				+ ")");
	}

	/**
	 * 根据用户名查询用户信息
	 * 
	 * @param conn
	 * @param userNames
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryUserIdByUserName(Connection conn,
			String userNames) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		userNames = userNames.replaceAll("'", "");
		String[] userName = userNames.split(",");
		StringBuilder newUserName = new StringBuilder();
		for (String name : userName) {
			newUserName.append("'");
			newUserName.append(name);
			newUserName.append("',");
		}
		int length = newUserName.length();
		if (length > 0) {
			newUserName.delete(length - 1, length);
		}
		if (StringUtils.isNotBlank(userNames)) {
			DataSet ds = user.open(conn, "id,username", " username in ("
					+ newUserName.toString() + ")", "", -1, -1); 
			ds.tables.get(0).rows.genRowsMap();
			return ds.tables.get(0).rows.rowsMap;
		}
		return new ArrayList<Map<String, Object>>();
	}

	public long updateUser(Connection conn, long userId,
			Map<String, String> userMap) throws SQLException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DBReflectHelper.mapToTableValue(user, userMap);

		return user.update(conn, "id=" + userId);
	}
	
	public List<Map<String, Object>> queryUserAll(Connection conn) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet ds = user.open(conn, "userName,email ,password ", "", "", -1, -1);
		ds.tables.get(0).rows.genRowsMap();
		return ds.tables.get(0).rows.rowsMap;
	}
	
	/**
	 * 查询用户数量
	 * @param conn
	 * @return
	 * @throws DataException 
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> queryUserAllCount(Connection conn) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet ds = user.open(conn, "count(*) usernum ", "", "", -1, -1);
		ds.tables.get(0).rows.genRowsMap();
		return ds.tables.get(0).rows.rowsMap;
	}
	/**
	 * 判断用户是否为VIP
	 * @param conn
	 * @param userId
	 * @return
	 * @throws DataException 
	 * @throws SQLException 
	 */
	public boolean isVip(Connection conn, long userId) throws SQLException, DataException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		DataSet ds = user.open(conn, "  vipStatus  ", " id="+userId, "", -1, -1);
		Map<String,String> map = DataSetHelper.dataSetToMap(ds);
		boolean isVip = false;
		if(map != null){
			long vipStatus = Convert.strToLong(map.get("vipStatus"), -1);
			if(vipStatus == 2){
				isVip = true;
			}
		}
		return isVip;
	}
	
	/**
	 * @MethodName: updateInvestorSum
	 * @Param: FrontMyPaymentDao
	 * @Author: gang.lv
	 * @Date: 2013-4-26 下午05:01:09
	 * @Return:
	 * @Descb: 给用户添加可用资金
	 * @Throws:
	 */
	public long addUserUsableAmount(Connection conn, double amount, long userId)
			throws SQLException {
		long returnId = -1;
		String command = "update t_user set usableSum = usableSum + " + amount
				+ " where id =" + userId;
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	
	
	public Map<String, String> queryUserByUserAndPwd(Connection conn,
			String userName, String pwd) throws SQLException, DataException {
		Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();
		DataSet ds = t_user.open(conn,
				"id,username,headImg,enable,vipStatus,email,authStep,usableSum,freezeSum",
				" (email ='" + StringEscapeUtils.escapeSql(userName.trim())
						+ "' OR username='"
						+ StringEscapeUtils.escapeSql(userName.trim())
						+ "' or mobilePhone='"+ StringEscapeUtils.escapeSql(userName.trim())
						+"') AND password = '" + StringEscapeUtils.escapeSql(pwd)
						+ "' AND enable != 2", "", -1, -1);
		return DataSetHelper.dataSetToMap(ds);
	}

	/**
	 * 更改登录密码
	 * @param conn
	 * @param id
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public long updateLoginPwd(Connection conn, Long id, String password) throws SQLException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.password.setValue(password);
		return user.update(conn, " id=" + id);
	}

	/**
	 * 更改登录密码
	 * @param conn
	 * @param id
	 * @param password
	 * @param type 1为登录密码，2为交易密码
	 * @return
	 * @throws SQLException
	 */
	public long updatePwd(Connection conn, Long id, String password,long type) throws SQLException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		if(type == 1){
			user.password.setValue(password);
		}else if(type == 2){
			user.dealpwd.setValue(password);
		}else{
			return 0;
		}
		
		return user.update(conn, " id=" + id);
	}
	
	public long queryUserIdByPhone(Connection conn, String cellPhone) throws SQLException, DataException {
		StringBuilder command = new StringBuilder();
		command.append("select u.id from t_user u left JOIN t_person p on p.userId=u.id ");
		command.append(" LEFT JOIN t_phone_binding_info pb on pb.userId=u.id where u.mobilePhone = '");
		command.append(cellPhone);
		command.append("' or pb.mobilePhone='");
		command.append(cellPhone);
		command.append("' or p.cellPhone='");
		command.append(cellPhone);
		command.append("' ");
		DataSet ds = MySQL.executeQuery(conn, command.toString());
		Map<String,String> map = DataSetHelper.dataSetToMap(ds);
		long userId = -1;
		if(map != null){
			userId = Convert.strToLong(map.get("id"), -1);
		}
		return userId;
	}
	public List<Map<String, Object>> isPhoneExist(Connection conn,
			String cellphone) throws SQLException, DataException {
		DataSet dataSet = MySQL.executeQuery(conn," select u.id,u.username from t_user u left join t_person p on u.id = p.userId where p.cellPhone = '" + StringEscapeUtils.escapeSql(cellphone.trim()) + "' and u.enable = 1"); 
		dataSet.tables.get(0).rows.genRowsMap(); 
		return dataSet.tables.get(0).rows.rowsMap;
	}
	
	// 通过手机更改用户手机号码
	public Long updatepasswordBycellphone(Connection conn, String cellphone,
			String password) throws SQLException {
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		user.password.setValue(com.shove.security.Encrypt.MD5(password.trim()));
		return user.update(conn, " mobilePhone = " + cellphone);
	}
	
	public Map<String, String> queryUserAmount(Connection conn, Long userId)
	throws DataException, SQLException {
	DataSet dataSet = MySQL.executeQuery(conn," select usableSum from t_user where id = " + userId);
	return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 更改邮箱byid
	 * @throws SQLException 
	 */
	public long updateEmalByid(Connection conn,long id,String email) throws SQLException{
		return MySQL.executeNonQuery(conn, " update t_user set t_user.email = '"+StringEscapeUtils.escapeSql(email)+"' where id = "+id);
	}
	/**
	 * @MethodName: deducteUserUsableAmount
	 * @Param: UserDao
	 * @Author: gang.lv
	 * @Date: 2013-5-22 上午11:57:35
	 * @Return:
	 * @Descb: 扣除用户可用资金
	 * @Throws:
	 */
	public long deducteUserUsableAmount(Connection conn, double amount,
			long userId) throws SQLException {
		StringBuffer command = new StringBuffer();
		command.append("update t_user set usableSum = usableSum - " + amount
				+ " where id =" + userId);
		return MySQL.executeNonQuery(conn, command.toString());
	}
	/**
	 * @MethodName: queryUserAmountAfterHander
	 * @Param: BorrowManageDao
	 * @Author: gang.lv
	 * @Date: 2013-4-21 下午09:11:22
	 * @Return:
	 * @Descb: 查询用户 的资金
	 * @Throws:
	 */
	public Map<String, String> queryUserAmountAfterHander(Connection conn,
			long userId) throws SQLException, DataException {
		String command ="select a.usableSum,a.freezeSum,sum(b.recivedPrincipal+b.recievedInterest-b.hasPrincipal-b.hasInterest) forPI "
		+" from t_user a left join t_invest b on a.id = b.investor where a.id="
		+ userId + " group by a.id";
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	
	/**
	 * 激活账户
	 * @param conn
	 * @param userId
	 * @return
	 * @throws SQLException 
	 */
	public long updateUserActivate(Connection conn, long userId) throws SQLException{
		 Dao.Tables.t_user  t_user = new Dao().new Tables().new t_user();
		 t_user.enable.setValue(1);
		 return t_user.update(conn, " id = "+ userId);
	}

	public void setUserManageDao(UserManageDao userManageDao) {
		this.userManageDao = userManageDao;
	}

	public UserManageDao getUserManageDao() {
		return userManageDao;
	}
}
