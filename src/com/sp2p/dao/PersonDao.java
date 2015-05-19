package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.sp2p.database.Dao;

/**
 * 操作t_person表
 * @author lw
 *
 */
public class PersonDao {
     /**
      * @param conn
      * @param realName  真实姓名
      * @param cellPhone 手机号码
      * @param sex 性别(男 女)
      * @param birthday 出生日期 
      * @param highestEdu 最高学历 
      * @param eduStartDay 入学年份
      * @param school 毕业院校 
      * @param maritalStatus 婚姻状况(已婚 未婚)
      * @param hasChild 有无子女(有 无)
      * @param hasHourse 是否有房(有 无)
      * @param hasHousrseLoan 有无房贷(有 无)
       * @param hasCar 是否有车 (有 无)
      * @param hasCarLoan 有无车贷 (有 无)
      * @param nativePlacePro 籍贯省份(默认为-1) 
      * @param nativePlaceCity 籍贯城市 (默认为-1)
      * @param registedPlacePro 户口所在地省份(默认为-1)
      * @param registedPlaceCity 户口所在地城市(默认为-1)
      * @param address 居住地址
      * @param telephone 居住电话 
      * @param userId 用户id
      * @param personalHead 个人头像 
      * @param idNo 身份证号码
      * @param auditStatus 认证状态(1 默认审核中或等待审核 2 审核不通过 3 审核通过)
      * @param flag 1为填写 2 为未填写
      * @return
     * @throws SQLException 
      */
	 public Long addPerson(Connection conn,String realName,String cellPhone,String sex,String birthday,String highestEdu,String eduStartDay,String school,
			 String maritalStatus,String hasChild,String hasHourse,String hasHousrseLoan,String hasCar,String hasCarLoan,Integer nativePlacePro,
			 Integer nativePlaceCity,Integer registedPlacePro,Integer registedPlaceCity,String address,String telephone,Long userId,String personalHead,
			 String idNo,Integer auditStatus,Integer flag
			 ) throws SQLException{
		 Dao.Tables.t_person person = new Dao().new Tables().new t_person();
		 if(realName!=null){
			 person.realName.setValue(realName);
		 }
		 if(cellPhone!=null){
			 person.cellPhone.setValue(cellPhone);
		 }
		 if(sex!=null){
			 person.sex.setValue(sex);
		 }
		 if(birthday!=null){
			 person.birthday.setValue(birthday);
		 }
		 if(highestEdu!=null){
			 person.highestEdu.setValue(highestEdu);
		 }
		 if(eduStartDay!=null){
			 person.eduStartDay.setValue(eduStartDay);
		 }
		 if(school!=null){
			 person.school.setValue(school);
		 }
		 if(maritalStatus!=null){
			 person.maritalStatus.setValue(maritalStatus);
		 }
		 
		 if(hasChild!=null){
			 person.hasChild.setValue(hasChild);
		 }
		 if(hasHourse!=null){
			 person.hasHourse.setValue(hasHourse);
		 }
		 if(hasHousrseLoan!=null){
			 person.hasHousrseLoan.setValue(hasHousrseLoan);
		 }
		 if(hasCar!=null){
			 person.hasCar.setValue(hasCar);
		 }
		 
		 if(hasCarLoan!=null){
			 person.hasCarLoan.setValue(hasCarLoan);
		 }	
		 if(nativePlacePro!=null){
			 person.nativePlacePro.setValue(nativePlacePro);
		 }
		 if(nativePlaceCity!=null){
			 person.nativePlaceCity.setValue(nativePlaceCity);
		 }
		 if(registedPlacePro!=null){
			 person.registedPlacePro.setValue(registedPlacePro);
		 }
		 if(registedPlaceCity!=null){
			 person.registedPlaceCity.setValue(registedPlaceCity);
		 }
		 
		 if(address!=null){
			 person.address.setValue(address);
		 }
		 if(telephone!=null){
			 person.telephone.setValue(telephone);
		 }
		 if(userId!=null){
			 person.userId.setValue(userId);
		 }
		 if(personalHead!=null){
			 person.personalHead.setValue(personalHead);
		 }
		 if(idNo!=null){
			 person.idNo.setValue(idNo);
		 }
		 if(auditStatus!=null){
			 person.auditStatus.setValue(auditStatus);
		 }
		 if(flag!=null){
			 person.flag.setValue(flag);
		 }
		 
		 return person.insert(conn);
	 }
	 
	 /**
	  * 根据手机号码查询信息
	  * @param conn
	  * @param cellphone
	  * @return
	 * @throws DataException 
	 * @throws SQLException 
	  */
	 public Map<String,String>  querCellPhone(Connection  conn,String cellphone) throws DataException, SQLException{
		 Dao.Tables.t_person  t_person = new Dao().new Tables().new t_person();
		 DataSet  ds = t_person.open(conn, " * ", " cellPhone = '"+StringEscapeUtils.escapeSql(cellphone)+"' ", "", -1, -1);
		 ds.tables.get(0).rows.genRowsMap();
		  
		 return DataSetHelper.dataSetToMap(ds);
	 }
	 
}
