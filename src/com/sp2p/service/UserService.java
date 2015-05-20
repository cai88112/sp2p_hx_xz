package com.sp2p.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.Convert;
import com.shove.base.BaseService;
import com.shove.data.DataException;
import com.shove.data.DataRow;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;
import com.shove.vo.PageBean;
import com.sp2p.constants.IConstants;
import com.sp2p.dao.BeVipDao;
import com.sp2p.dao.CoinDao;
import com.sp2p.dao.MyHomeInfoSettingDao;
import com.sp2p.dao.OperationLogDao;
import com.sp2p.dao.ReturnedmoneyDao;
import com.sp2p.dao.UserDao;
import com.sp2p.dao.admin.RelationDao;
import com.sp2p.dao.admin.ShoveBorrowAmountTypeDao;
import com.sp2p.database.Dao;
import com.sp2p.database.Dao.Procedures;
import com.sp2p.database.Dao.Tables;
import com.sp2p.database.Dao.Tables.t_hx_user;
import com.sp2p.database.Dao.Tables.t_user;
import com.sp2p.entity.LoginVerify;
import com.sp2p.entity.User;
import com.sp2p.service.admin.SendmsgService;
import com.sun.jndi.cosnaming.CNNameParser;

public class UserService extends BaseService {

	public static Log log = LogFactory.getLog(UserService.class);

	private UserDao userDao;
	private RelationDao relationDao;
	private SendmsgService sendmsgService;
	private BeVipDao beVipDao;
	private OperationLogDao operationLogDao;
	private MyHomeInfoSettingDao myHomeInfoSettingDao;
	private ShoveBorrowAmountTypeDao shoveBorrowAmountTypeDao;
	private ReturnedmoneyDao returnedmoneyDao;
	private CoinDao coinDao;

	/**
	 * 添加用户
	 * 
	 * @param email
	 * @param userName
	 * @param password
	 * @param refferee
	 *            推荐人
	 * @param lastDate
	 * @param lastIP
	 * @param dealpwd
	 *            交易密码
	 * @param mobilePhone
	 * @param rating
	 *            网站积分
	 * @param creditrating
	 * @param vipstatus
	 * @param vipcreatetime
	 * @param creditlimit
	 * @param authstep
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addUser(String email, String userName, String password,
			String refferee, String lastDate, String lastIP, String dealpwd,
			String mobilePhone, Integer rating, Integer creditrating,
			Integer vipstatus, String vipcreatetime, Integer creditlimit,
			Integer authstep, String headImg, Integer enable,
			Long servicePersonId) throws Exception {

		Connection conn = MySQL.getConnection();
		long userId = -1L;
		try {
			// 得到信息额度类型
			Map<String, String> map = shoveBorrowAmountTypeDao
					.queryBorrowAmountByNid(conn, "credit");
			double creditLimit = Convert.strToDouble(map.get("creditLimit"), 0);
			userId = userDao.addUser(conn, email, userName, password, refferee,
					lastDate, lastIP, dealpwd, mobilePhone, rating,
					creditrating, vipstatus, vipcreatetime, authstep, headImg,
					enable, servicePersonId, creditLimit);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return userId;
	}

	/**
	 * 描述: 查询认证步骤 时间: 2014-2-17 上午10:38:26 返回值类型: int
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int queryStepById(Long id) throws Exception {
		Connection conn = MySQL.getConnection();
		int authStep = 0;
		try {
			Map<String, String> user = new HashMap<String, String>();
			user = userDao.queryUserById(conn, id);
			authStep = Convert.strToInt(user.get("authStep"), -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return authStep;
	}

	/**
	 * 修改T_user pIpsBillNo
	 * 
	 * @param userId
	 * @param portNo
	 * @return
	 * @throws Exception
	 */
	public Long updatePIpsBillNo(String userId, String pIpsBillNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Long retut = -1L;
		try {
			user.pIpsBillNo.setValue(pIpsBillNo);
			retut = user.update(conn, " id=" + userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return retut;
	}

	/**
	 * 添加认证图片
	 * 
	 * @param materAuthTypeId
	 * @param imgPath
	 * @param auditStatus
	 * @param userId
	 * @param authTime
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addImage(long materAuthTypeId, String imgPath, long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long ImageId = -1L;
		try {
			ImageId = userDao.addImage(conn, materAuthTypeId, imgPath, userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return ImageId;
	}

	/**
	 * 修改T_user 接口No
	 * 
	 * @param userId
	 * @param portNo
	 * @return
	 * @throws Exception
	 */
	public Long updatePortNo(String userId, String portNo) throws Exception {
		Connection conn = MySQL.getConnection();
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Long retut = -1L;
		try {
			user.portMerBillNo.setValue(portNo);
			retut = user.update(conn, " id=" + userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return retut;
	}

	/**
	 * 修改认证步骤
	 * 
	 * @param userId
	 * @param authStep
	 * @return
	 * @throws Exception
	 */
	public Long updateAuthStep(String userId, int authStep) throws Exception {
		Connection conn = MySQL.getConnection();
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Long retut = -1L;
		try {
			user.authStep.setValue(authStep);
			retut = user.update(conn, " id=" + userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return retut;
	}

	/**
	 * 用户注册(存储过程处理)
	 * 
	 * @param email
	 * @param username
	 * @param password
	 * @param refferee
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws DataException
	 */
	public Long userRegister1(String email, String userName, String password,
			String refferee, Map<String, Object> userMap, int typeLen,
			String mobilePhone,int isprivate) throws Exception {
		Connection conn = MySQL.getConnection();
		int demo = Convert.strToInt(IConstants.ISDEMO, 2);
		long userId = -1L;

		Map<String, String> map = new HashMap<String, String>();
		Long ret = -1l;
		Long rs = -1l;
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.p_user_register(conn, ds, outParameterValues, email,
					userName, password, refferee, demo, -1, "",isprivate);
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");

			// userId = userDao.addUser(conn, email, userName, password,
			// refferee,
			// lastDate, lastIP, dealpwd, mobilePhone, rating,
			// creditrating, vipstatus, vipcreatetime, authstep, headImg,
			// enable, servicePersonId, creditLimit);
			if (ret <= 0) {
				return ret;
			}
			// Map<String, String> userinfoMap = userDao.queryIdByEmail(conn,
			// email);
			// userId = Convert.strToLong(String.valueOf(userinfoMap.get("id")),
			// -1);
			// if (userId <= 0) {
			// map.put("ret", ret + "");
			// map.put("ret_desc","执行异常,请重新注册！");
			// conn.rollback();
			// return ret;
			// }

			// 添加回款扩展记录
			rs = returnedmoneyDao.addReturnedMoney(conn, ret);
			if (rs <= 0) {
				map.put("ret", rs + "");
				map.put("ret_desc", "执行异常,请重新注册！");
				conn.rollback();
				return -1l;
			}

			// 添加金币扩展记录
			rs = coinDao.addCoin(conn, ret);
			if (rs <= 0) {
				map.put("ret", rs + "");
				map.put("ret_desc", "执行异常,请重新注册！");
				conn.rollback();
				return -1l;
			}

			if (userMap != null) {
				relationDao.addRelation(conn, ret,
						(Long) userMap.get("parentId"),
						(Integer) userMap.get("level"), 1);
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return ret;
	}

	/**
	 * 用户注册
	 * 
	 * @param email
	 * @param username
	 * @param password
	 * @param refferee
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 * @throws DataException
	 */
	public Long userRegister(String email, String userName, String password,
			String refferee, Map<String, Object> userMap, int typeLen,
			String mobilePhone) throws Exception {
		Connection conn = MySQL.getConnection();
		String dealpwd = null; // 交易密码
		Integer rating = 0;// 网站积分
		Integer creditrating = 0;// 信用积分
		String lastIP = "";
		String lastDate = null;// 最后登录时间
		Integer vipstatus = 1;// VIP会员状态 1是非vip 2是vip
		String vipcreatetime = null;// VIP创建时间
		double creditlimit = 0;// 信用额度 如果是vip 那么初始creditlimit = 3000；
		Integer authstep = 1;// 认证步骤(默认是1 个人详细信息 2 工作认证 3上传
		String headImg = "";// 用户头型
		// 系统给予默认头型
		Integer enable = 2; // 是否禁用 1、启用 2、禁用
		// 测试--跳过验证
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 得到信息额度类型
		Map<String, String> map = shoveBorrowAmountTypeDao
				.queryBorrowAmountByNid(conn, "credit");
		double creditLimit = Convert.strToDouble(map.get("init_credit"), 0);
		if (IConstants.ISDEMO.equals("1")) {
			authstep = 1;
			enable = 1;
			vipstatus = 2;
			vipcreatetime = df.format(new Date());
			creditlimit = creditLimit;
		}
		Long servicePersonId = null;
		long userId = -1L;

		try {

			userId = userDao.addUser(conn, email, userName, password, refferee,
					lastDate, lastIP, dealpwd, mobilePhone, rating,
					creditrating, vipstatus, vipcreatetime, authstep, headImg,
					enable, servicePersonId, creditLimit);
			if (userId <= 0) {
				return -1L;
			}
			// 初始化验证资料
			for (long i = 1; i <= typeLen; i++) {
				userDao.addMaterialsauth1(conn, userId, i);
			}
			if (userMap != null) {
				relationDao.addRelation(conn, userId,
						(Long) userMap.get("parentId"),
						(Integer) userMap.get("level"), 1);
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return userId;
	}

	/**
	 * 手机用户注册
	 * 
	 * @param email
	 * @param userName
	 * @param password
	 * @param refferee
	 * @param userMap
	 * @param typeLen
	 * @param mobilePhone
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long userAppRegister(String email, String userName, String password,
			String refferee, Map<String, Object> userMap, int typeLen,
			String mobilePhone) throws Exception {
		Connection conn = MySQL.getConnection();
		String dealpwd = null; // 交易密码
		Integer rating = 0;// 网站积分
		Integer creditrating = 0;// 信用积分
		String lastIP = "";
		String lastDate = null;// 最后登录时间
		Integer vipstatus = 1;// VIP会员状态 1是非vip 2是vip
		// DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String vipcreatetime = null;// VIP创建时间
		double creditlimit = 0;// 信用额度 如果是vip 那么初始creditlimit = 3000；
		Integer authstep = 1;// 认证步骤(默认是1 个人详细信息 2 工作认证 3上传
		String headImg = "";// 用户头型
		// 系统给予默认头型
		Integer enable = 1; // 是否禁用 1、启用 2、禁用
		// 测试--跳过验证
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 得到信息额度类型
		Map<String, String> map = shoveBorrowAmountTypeDao
				.queryBorrowAmountByNid(conn, "credit");
		double creditLimit = Convert.strToDouble(map.get("init_credit"), 0);
		if (IConstants.ISDEMO.equals("1")) {
			authstep = 1;
			enable = 1;
			vipstatus = 2;
			vipcreatetime = df.format(new Date());
			creditlimit = creditLimit;
		}
		Long servicePersonId = null;
		long userId = -1L;

		try {
			userId = userDao.addUser(conn, email, userName, password, refferee,
					lastDate, lastIP, dealpwd, mobilePhone, rating,
					creditrating, vipstatus, vipcreatetime, authstep, headImg,
					enable, servicePersonId, creditLimit);
			if (userId <= 0) {
				return -1L;
			}
			// 初始化验证资料
			for (long i = 1; i <= typeLen; i++) {
				userDao.addMaterialsauth1(conn, userId, i);
			}
			if (userMap != null) {
				relationDao.addRelation(conn, userId,
						(Long) userMap.get("parentId"),
						(Integer) userMap.get("level"), 1);
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return userId;
	}

	/**
	 * 投资人填写个人信息
	 * 
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
	public long updateUserBaseTT(String realName, String cellPhone, String sex,
			String birthday, String highestEdu, String eduStartDay,
			String school, String maritalStatus, String hasChild,
			String hasHourse, String hasHousrseLoan, String hasCar,
			String hasCarLoan, Long nativePlacePro, Long nativePlaceCity,
			Long registedPlacePro, Long registedPlaceCity, String address,
			String telephone, String personalHead, Long userId, String idNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long personId = -1L;
		try {
			personId = userDao.updateUserBaseWWc(conn, realName, cellPhone,
					sex, birthday, highestEdu, eduStartDay, school,
					maritalStatus, hasChild, hasHourse, hasHousrseLoan, hasCar,
					hasCarLoan, nativePlacePro, nativePlaceCity,
					registedPlacePro, registedPlaceCity, address, telephone,
					personalHead, userId, idNo);
			// add by houli
			// add by houli 如果个人信息填写成功，将填写的手机号码同步到T_PHONE_BINDING_INFO表中
			Map<String, String> p_map = myHomeInfoSettingDao
					.queryBindingInfoByUserId(conn, userId, -1, -1);
			Long result1 = -1L;
			if (p_map == null || p_map.size() <= 0) {// 如果没有记录则插入手机绑定数据，否则进行更新
				result1 = myHomeInfoSettingDao.addBindingMobile(conn,
						cellPhone, userId, IConstants.PHONE_BINDING_CHECK,
						"个人信息资料填写申请手机绑定", IConstants.INSERT_BASE_TYPE, null);
			} else {
				result1 = myHomeInfoSettingDao.updateBindingMobile(conn,
						cellPhone, userId, IConstants.PHONE_BINDING_CHECK,
						"个人信息资料填写申请手机绑定", IConstants.INSERT_BASE_TYPE);
			}
			if (result1 <= 0) {
				conn.rollback();
				return -1L;
			}
			// end
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return personId;
	}

	/**
	 * 存储过程添加或更新用户基本资料
	 * 
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
	 * @param num
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> updateUserBaseData1(String realName,
			String cellPhone, String sex, String birthday, String highestEdu,
			String eduStartDay, String school, String maritalStatus,
			String hasChild, String hasHourse, String hasHousrseLoan,
			String hasCar, String hasCarLoan, Long nativePlacePro,
			Long nativePlaceCity, Long registedPlacePro,
			Long registedPlaceCity, String address, String telephone,
			String personalHead, Long userId, String idNo, String num)
			throws Exception {
		Connection conn = MySQL.getConnection();
		conn.setAutoCommit(false);// 后面添加这么一句即可,设置无法自动提交,手动提交
		Map<String, String> map = new HashMap<String, String>();
		Long ret = -1l;
		Map<String, String> user = new HashMap<String, String>();
		user = userDao.queryUserById(conn, userId);
		String userName = Convert.strToStr(user.get("username"), "");
		String lastip = Convert.strToStr(user.get("lastIP"), "");
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.p_userInfo_update(conn, ds, outParameterValues,
					realName, cellPhone, sex, birthday, highestEdu,
					eduStartDay, school, maritalStatus, hasChild, hasHourse,
					hasHousrseLoan, hasCar, hasCarLoan, nativePlacePro,
					nativePlaceCity, registedPlacePro, registedPlaceCity,
					address, telephone, personalHead, userId, idNo, userName,
					lastip, num, -1, "");
			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			if (ret < 0) {
				conn.rollback();
				return map;
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	/**
	 * 更新用户基础资料
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserBaseData(String realName, String cellPhone,
			String sex, String birthday, String highestEdu, String eduStartDay,
			String school, String maritalStatus, String hasChild,
			String hasHourse, String hasHousrseLoan, String hasCar,
			String hasCarLoan, Long nativePlacePro, Long nativePlaceCity,
			Long registedPlacePro, Long registedPlaceCity, String address,
			String telephone, String personalHead, Long userId, String idNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> user = new HashMap<String, String>();
		long personId = -1L;
		try {
			user = userDao.queryUserById(conn, userId);
			personId = userDao.updateUserBaseData(conn, realName, cellPhone,
					sex, birthday, highestEdu, eduStartDay, school,
					maritalStatus, hasChild, hasHourse, hasHousrseLoan, hasCar,
					hasCarLoan, nativePlacePro, nativePlaceCity,
					registedPlacePro, registedPlaceCity, address, telephone,
					personalHead, userId, idNo,
					Convert.strToStr(user.get("username"), ""),
					Convert.strToStr(user.get("lastIP"), ""));
			if (personId <= 0) {
				conn.rollback();
				return -1L;
			}

			// add by houli
			// add by houli 如果个人信息填写成功，将填写的手机号码同步到T_PHONE_BINDING_INFO表中
			Map<String, String> p_map = myHomeInfoSettingDao
					.queryBindingInfoByUserId(conn, userId, -1, -1);
			Long result1 = -1L;
			if (p_map == null || p_map.size() <= 0) {// 如果没有记录则插入手机绑定数据，否则进行更新
				result1 = myHomeInfoSettingDao.addBindingMobile(conn,
						cellPhone, userId, IConstants.PHONE_BINDING_CHECK,
						"个人信息资料填写申请手机绑定", IConstants.INSERT_BASE_TYPE, null);
			} else {
				result1 = myHomeInfoSettingDao.updateBindingMobile(conn,
						cellPhone, userId, IConstants.PHONE_BINDING_CHECK,
						"个人信息资料填写申请手机绑定", IConstants.INSERT_BASE_TYPE);
			}
			if (result1 <= 0) {
				conn.rollback();
				return -1L;
			}
			// end
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return personId;

	}

	/**
	 * 审核用户基础资料
	 * 
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserBaseDataCheck(long userId, int auditStatus,
			Long adminId) throws Exception {
		Connection conn = MySQL.getConnection();
		StringBuffer msg = new StringBuffer();
		long personId = -1L;
		try {
			personId = userDao.updateUserBaseDataCheck(conn, userId,
					auditStatus);// 更新用户的工作信息认证审核状态

			if (personId <= 0) {
				conn.rollback();
				return -1L;
			} else {
				int phoneStatus = 2;// 默认审核中
				if (auditStatus == 2) {// 失败
					phoneStatus = 4;// bangding 失败
				} else if (auditStatus == 3) {
					phoneStatus = 1;// 审核成功
				}
				beVipDao.updatePhoneBanding(conn, userId, phoneStatus);
				// 更新用户绑定手机状态
				personId = beVipDao.updatePhoneBanding(conn, userId,
						phoneStatus);
				if (personId <= 0) {
					conn.rollback();
					return -1L;
				} else {

					// 发站内信
					String m = "";
					if (auditStatus == 2) {
						m = "不通过";
					} else if (auditStatus == 3) {
						m = "通过";
					}
					msg.append("您的基本信息审核状况:");
					msg.append(m);
					// 发站内信
					personId = sendmsgService.sendCheckMail(userId,
							" 基本信息审核通知", msg.toString(), 2, adminId);// 2管理员信件
					if (personId <= 0) {
						conn.rollback();
						return -1L;
					}
				}
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return personId;
	}

	/**
	 * 该用户上传资料的类型的审核状态
	 * 
	 * @param id
	 * @param materAuthTypeId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updateUserPicturStatus(Long id, Long materAuthTypeId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long personId = -1L;
		try {
			personId = userDao
					.updateUserPicturStatus(conn, id, materAuthTypeId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return personId;
	}

	/**
	 * 增加用户的图片
	 * 
	 * @param auditStatus
	 * @param uploadingTime
	 * @param imagePath
	 * @param materialsauthid
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long addUserImage(Integer auditStatus, String uploadingTime,
			List<Long> lists, List<String> imgListsy, List<String> imgListsn,
			Long materialsauthid, Long id, Long materAuthTypeId, Long tmid,
			int addCount) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long personId = -1L;
		Long userId = -1L;
		Long userIdauthd = -1L;
		try {
			// 将用户选择可见的设置为可见
			// if(lists.size()>0&&lists!=null){
			personId = userDao.updatematerialImagedetalvisiable(conn,
					materialsauthid);// 将重置图片的可见性 设置为不可见
			for (Long list : lists) {
				personId = userDao.updatevisiable(conn, list);// 根据传来的id集合重新设置哪个可见
				if (personId <= 0) {
					conn.rollback();
					return -1L;
				}
			}
			// 插于图片为可见的
			for (String vimg : imgListsy) {// 遍历集合
				personId = userDao.addUserImage(conn, auditStatus,
						uploadingTime, vimg, materialsauthid, 1);// t_materialImagedetal添加图片
				// 1 为可见
				if (personId <= 0) {
					conn.rollback();
					return -1L;
				}
			}
			// 插于图片为不可见的
			for (String vimg : imgListsn) {// 遍历集合
				personId = userDao.addUserImage(conn, auditStatus,
						uploadingTime, vimg, materialsauthid, 2);// t_materialImagedetal添加图片
				// 1 为可见
				if (personId <= 0) {
					conn.rollback();
					return -1L;
				}
			}// --------modify by houli 如果未新添加上传图片，那么不修改总审核状态
			if (addCount > 0) {
				userId = userDao.updateUserPicturStatus(conn, id,
						materAuthTypeId);// 更新用户总证件状态
				if (userId <= 0) {
					conn.rollback();
					return -1L;
				}
			}
			userId = userDao.updateUserPicturStatus(conn, id, materAuthTypeId);// 更新用户总证件状态
			if (userId <= 0) {
				conn.rollback();
				return -1L;
			}
			// 更新user authod表中的状态
			userIdauthd = userDao.updateUserauthod(conn, id);// 当5大基本资料上传完成后更新用户的认证步骤
			if (userIdauthd <= 0) {
				conn.rollback();
				return -1L;
			}
			userMap = userDao.queryUserById(conn, id);
			operationLogDao.addOperationLog(conn, "t_materialimagedetal",
					Convert.strToStr(userMap.get("username"), ""),
					IConstants.UPDATE,
					Convert.strToStr(userMap.get("lastIP"), ""), 0, "上传图片", 1);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return personId;
	}

	/**
	 * 更新用户上传图片的可见性
	 * 
	 * @param tmdid
	 *            //主图片类型下的图片明细id
	 * @param tmid
	 *            //主图片类型id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long updatevisiable(Long tmid, List<Long> lists) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			long personId = -1L;
			Long resetvisable = -1L;

			resetvisable = userDao.updatematerialImagedetalvisiable(conn, tmid);// 将重置图片的可见性
			// 重置为不可见
			if (resetvisable <= 0) {
				conn.rollback();
				return -1L;
			}
			for (Long list : lists) {
				personId = userDao.updatevisiable(conn, list);// 根据传来的id集合重新设置哪个可见
				if (personId <= 0) {
					conn.rollback();
					return -1L;
				}
			}
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return -1L;
	}

	/**
	 * 添加用户的基础资料
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Long userBaseData(String realName, String cellPhone, String sex,
			String birthday, String highestEdu, String eduStartDay,
			String school, String maritalStatus, String hasChild,
			String hasHourse, String hasHousrseLoan, String hasCar,
			String hasCarLoan, Long nativePlacePro, Long nativePlaceCity,
			Long registedPlacePro, Long registedPlaceCity, String address,
			String telephone, String personalHead, Long userId, String idNo)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long personId = -1L;
		long uId = -1L;
		try {
			personId = userDao.addUserBaseData(conn, realName, cellPhone, sex,
					birthday, highestEdu, eduStartDay, school, maritalStatus,
					hasChild, hasHourse, hasHousrseLoan, hasCar, hasCarLoan,
					nativePlacePro, nativePlaceCity, registedPlacePro,
					registedPlaceCity, address, telephone, personalHead,
					userId, idNo);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return personId;
	}

	/**
	 * 添加用户工作认证信息
	 * 
	 * @throws SQLException
	 */
	public Long addUserWorkData(String orgName, String occStatus, Long workPro,
			Long workCity, String companyType, String companyLine,
			String companyScale, String job, String monthlyIncome,
			String workYear, String companyTel, String workEmail,
			String companyAddress, String directedName,
			String directedRelation, String directedTel, String otherName,
			String otherRelation, String otherTel, String moredName,
			String moredRelation, String moredTel, Long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long workDataId = -1L;
		try {
			workDataId = userDao.addUserWorkData(conn, orgName, occStatus,
					workPro, workCity, companyType, companyLine, companyScale,
					job, monthlyIncome, workYear, companyTel, workEmail,
					companyAddress, directedName, directedRelation,
					directedTel, otherName, otherRelation, otherTel, moredName,
					moredRelation, moredTel, userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return workDataId;
	}

	/**
	 * 修改用户工作认证信息
	 * 
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> updateUserWorkData1(String orgName,
			String occStatus, Long workPro, Long workCity, String companyType,
			String companyLine, String companyScale, String job,
			String monthlyIncome, String workYear, String companyTel,
			String workEmail, String companyAddress, String directedName,
			String directedRelation, String directedTel, String otherName,
			String otherRelation, String otherTel, String moredName,
			String moredRelation, String moredTel, Long userId,
			Integer vipStatus, Integer newutostept) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long workDataId = -1L;
		Map<String, String> map = new HashMap<String, String>();
		Long ret = -1l;
		try {
			userMap = userDao.queryUserById(conn, userId);
			String lastip = Convert.strToStr(userMap.get("lastIP"), "");

			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.p_userWorkInfo_update(conn, ds, outParameterValues,
					orgName, occStatus, workPro, workCity, companyType,
					companyLine, companyScale, job, monthlyIncome, workYear,
					companyTel, workEmail, companyAddress, directedName,
					directedRelation, directedTel, otherName, otherRelation,
					otherTel, moredName, moredRelation, moredTel, userId,
					vipStatus, newutostept, lastip, -1, "");

			ret = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", ret + "");
			map.put("ret_desc", outParameterValues.get(1) + "");

			if (ret <= 0) {
				conn.rollback();
				return map;
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return map;
	}

	public Long updateUserWorkData(String orgName, String occStatus,
			Long workPro, Long workCity, String companyType,
			String companyLine, String companyScale, String job,
			String monthlyIncome, String workYear, String companyTel,
			String workEmail, String companyAddress, String directedName,
			String directedRelation, String directedTel, String otherName,
			String otherRelation, String otherTel, String moredName,
			String moredRelation, String moredTel, Long userId,
			Integer vipStatus, Integer newutostept) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> userMap = new HashMap<String, String>();
		long workDataId = -1L;
		try {
			workDataId = userDao.updateUserWorkData(conn, orgName, occStatus,
					workPro, workCity, companyType, companyLine, companyScale,
					job, monthlyIncome, workYear, companyTel, workEmail,
					companyAddress, directedName, directedRelation,
					directedTel, otherName, otherRelation, otherTel, moredName,
					moredRelation, moredTel, userId);

			userMap = userDao.queryUserById(conn, userId);
			workDataId = operationLogDao.addOperationLog(conn, "t_workauth",
					Convert.strToStr(userMap.get("username"), ""),
					IConstants.INSERT,
					Convert.strToStr(userMap.get("lastIP"), ""), 0.00,
					"填写工作认证信息", 1);
			if (workDataId <= 0) {
				conn.rollback();
				return -1L;
			}

			// 跟新用户的认证步骤
			if (newutostept == 2) {
				workDataId = beVipDao.updateUserAustep(conn, userId, 3);// 3为填写完了工作信息的认证步骤
				if (workDataId <= 0) {
					conn.rollback();
					return -1L;
				}
			}
			if (vipStatus != 1) {// 如果此时用户的vip状态为会员 那么要更新user的认证步骤
				if (newutostept <= 3) {
					workDataId = beVipDao.updateUserAustep(conn, userId, 4);// 4为填写完了vip的认证步骤
					if (workDataId <= 0) {
						conn.rollback();
						return -1L;
					}
				}
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return workDataId;
	}

	/**
	 * 修改用户
	 * 
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
	public Long updateUser(Long id, String email, String userName,
			String password, String name, String gender, String mobilePhone,
			String qq, Long provinceId, Long cityId, Long areaId,
			String postalcode, String headImg, Integer status, String balances,
			Integer enable, Integer rating, String lastDate, String lastIP)
			throws Exception {
		Connection conn = MySQL.getConnection();
		long userId = -1;

		try {
			userId = userDao.updateUser(conn, id, email, userName, password,
					name, gender);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return userId;
	}

	/**
	 * 判断重复登录
	 * 
	 * @param email
	 * @param userName
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long isExistEmailORUserName(String email, String userName)
			throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = userDao.isUserEmaiORUseName(conn, email, userName);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return list.size() <= 0 ? -1L : 1L;
	}

	// =====================================
	/**
	 * 用户登录时候验证邮箱和用户名是否激活
	 */
	public Long isUEjihuo(String email, String userName) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = userDao.isUEjihuo(conn, email, userName);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return list.size() <= 0 ? -1L : 1L;
	}

	/**
	 * 用户登录时候验证邮箱和用户名是否激活
	 */
	public Long isUEjihuo_(String email, String userName) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = userDao.isUEjihuo_(conn, email, userName);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return list.size() <= 0 ? -1L : 1L;
	}

	// =========================================

	/**
	 * 用户登录处理
	 * 
	 * @param userId
	 *            用户id
	 * @param userName
	 *            用户名称
	 * @param password
	 *            用户密码
	 * @param lastIP
	 *            最后登录ip
	 * @param loginType
	 *            登录类型，1用户名或邮箱登录，
	 * @return User
	 * @throws SQLException
	 * @throws DataException
	 */
	public User userLogin1(String userName, String password, String lastIP,
			String lastTime) throws Exception {
		Connection conn = MySQL.getConnection();
		password = StringEscapeUtils.escapeSql(password.trim());
		if ("1".equals(IConstants.ENABLED_PASS)) {
			password = com.shove.security.Encrypt.MD5(password.trim());
		} else {
			password = com.shove.security.Encrypt.MD5(password.trim()
					+ IConstants.PASS_KEY);
		}
		User user = null;
		Map<String, String> map = new HashMap<String, String>();
		Long userid = -1L;
		try {
			DataSet ds = new DataSet();
			List<Object> outParameterValues = new ArrayList<Object>();
			Procedures.p_user_login(conn, ds, outParameterValues, userName,
					password, lastIP, -1, "");
			userid = Convert.strToLong(outParameterValues.get(0) + "", -1);
			map.put("ret", userid + "");
			map.put("ret_desc", outParameterValues.get(1) + "");
			log.info(map);
			if (userid <= 0) {
				if (userid == -5) {
					conn.close();
					user = new User();
					user.setEnable(2);
					return user;
				} else {
					conn.close();
					return null;
				}
			}

			Map<String, String> usermap = new HashMap<String, String>();
			usermap = userDao.queryUserById(conn, userid);

			Dao.Views.v_t_user_loginsession_user sessionuser = new Dao().new Views().new v_t_user_loginsession_user();
			DataSet dataSet = sessionuser.open(conn, "", " id=" + userid, "",
					-1, -1);
			Map<String, String> sessionmap = new HashMap<String, String>();
			sessionmap = DataSetHelper.dataSetToMap(dataSet);
			if (sessionmap != null && sessionmap.size() > 0) {
				user = new User();
				user.setAuthStep(Convert.strToInt(sessionmap.get("authStep"),
						-1));
				user.setEmail(Convert.strToStr(sessionmap.get("email"), null));
				user.setPassword(Convert.strToStr(sessionmap.get("password"),
						null));
				user.setId(Convert.strToLong(sessionmap.get("id"), -1L));
				user.setRealName(Convert.strToStr(sessionmap.get("realName"),
						null));
				user.setKefuname(Convert.strToStr(sessionmap.get("kefuname"),
						null));
				user.setUserName(Convert.strToStr(sessionmap.get("username"),
						null));
				user.setVipStatus(Convert.strToInt(sessionmap.get("vipStatus"),
						-1));
				user.setHeadImage(Convert.strToStr(usermap.get("headImg"), null));
				user.setEnable(Convert.strToInt(usermap.get("enable"), -1));
				user.setPersonalHead(Convert.strToStr(
						sessionmap.get("personalHead"), null));
				user.setKefuid(Convert.strToInt(sessionmap.get("tukid"), -1));
				user.setCreditLimit(Convert.strToStr(
						sessionmap.get("usableCreditLimit"), null));
				user.setPortMerBillNo(Convert.strToStr(
						sessionmap.get("portMerBillNo"), null));
				user.setpIpsBillNo(Convert.strToStr(
						sessionmap.get("pIpsBillNo"), null));
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			conn.close();
		}
		return user;
	}

	/**
	 * 用户登录处理
	 * 
	 * @param userId
	 *            用户id
	 * @param userName
	 *            用户名称
	 * @param password
	 *            用户密码
	 * @param lastIP
	 *            最后登录ip
	 * @param lastTime
	 *            最后登录时间
	 * @param loginType
	 *            登录类型，1用户名或邮箱登录，
	 * @return User
	 * @throws SQLException
	 * @throws DataException
	 */
	public User userLogin(String userName, String password, String lastIP,
			String lastTime) throws Exception {
		Connection conn = MySQL.getConnection();
		User user = null;
		try {
			password = StringEscapeUtils.escapeSql(password.trim());
			if ("1".equals(IConstants.ENABLED_PASS)) {
				password = com.shove.security.Encrypt.MD5(password.trim());
			} else {
				password = com.shove.security.Encrypt.MD5(password.trim()
						+ IConstants.PASS_KEY);
			}
			Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();
			DataSet ds = t_user
					.open(conn,
							"id,username,headImg,enable,vipStatus,email,authStep,lastIP ",
							"(email ='"
									+ StringEscapeUtils.escapeSql(userName
											.trim())
									+ "' OR username='"
									+ StringEscapeUtils.escapeSql(userName
											.trim())
									+ "' OR mobilePhone='"
									+ StringEscapeUtils.escapeSql(userName
											.trim()) + "') AND password = '"
									+ password + "' AND enable != 2", "", -1,
							-1);
			int returnId = ds.tables.get(0).rows.getCount();
			if (returnId <= 0) {
				conn.close();
				return null;
			} else {
				user = new User();
				DataRow dr = ds.tables.get(0).rows.get(0);

				// ======
				Map<String, String> sessionmap = new HashMap<String, String>();

				Dao.Views.v_t_user_loginsession_user sessionuser = new Dao().new Views().new v_t_user_loginsession_user();

				DataSet dataSet = sessionuser.open(conn, "", " id="
						+ (Long) (dr.get("id") == null ? -1l : dr.get("id")),
						"", -1, -1);
				sessionmap = DataSetHelper.dataSetToMap(dataSet);

				if (sessionmap != null && sessionmap.size() > 0) {
					user = new User();
					user.setAuthStep(Convert.strToInt(
							sessionmap.get("authStep"), -1));
					user.setEmail(Convert.strToStr(sessionmap.get("email"),
							null));
					user.setPassword(Convert.strToStr(
							sessionmap.get("password"), null));
					user.setId(Convert.strToLong(sessionmap.get("id"), -1L));
					user.setRealName(Convert.strToStr(
							sessionmap.get("realName"), null));
					user.setKefuname(Convert.strToStr(
							sessionmap.get("kefuname"), null));
					user.setUserName(Convert.strToStr(
							sessionmap.get("username"), null));
					user.setVipStatus(Convert.strToInt(
							sessionmap.get("vipStatus"), -1));
					user.setHeadImage((String) (dr.get("headImg") == null ? ""
							: dr.get("headImg")));
					user.setEnable((Integer) (dr.get("enable") == null ? -1
							: dr.get("enable")));
					user.setPersonalHead(Convert.strToStr(
							sessionmap.get("personalHead"), null));
					user.setKefuid(Convert.strToInt(sessionmap.get("tukid"), -1));
					user.setCreditLimit(Convert.strToStr(
							sessionmap.get("usableCreditLimit"), null));
				}
				if (StringUtils.isNotBlank(lastIP)) {
					t_user.lastDate.setValue(lastTime);
					t_user.lastIP.setValue(lastIP);
					t_user.update(conn, " id=" + user.getId());
				}

			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return user;
	}

	/**
	 * 虚拟用户登录
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public User userVirtualLogin(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		User user = null;

		try {
			Dao.Tables.t_user t_user = new Dao().new Tables().new t_user();
			DataSet ds = t_user
					.open(conn,
							"id  ,username ,headImg  ,enable  ,vipStatus  ,email ,authStep  ",
							" id=" + id, "", -1, -1);
			int returnId = ds.tables.get(0).rows.getCount();
			if (returnId <= 0) {
				conn.close();
				return null;
			} else {
				user = new User();
				DataRow dr = ds.tables.get(0).rows.get(0);

				// ======
				Map<String, String> sessionmap = new HashMap<String, String>();

				Dao.Views.v_t_user_loginsession_user sessionuser = new Dao().new Views().new v_t_user_loginsession_user();

				DataSet dataSet = sessionuser.open(conn, "",
						" id =" + (dr.get("id") == null ? -1l : dr.get("id")),
						"", -1, -1);
				sessionmap = DataSetHelper.dataSetToMap(dataSet);

				if (sessionmap != null && sessionmap.size() > 0) {
					user = new User();
					user.setAuthStep(Convert.strToInt(
							sessionmap.get("authStep"), -1));
					user.setEmail(Convert.strToStr(sessionmap.get("email"),
							null));
					user.setPassword(Convert.strToStr(
							sessionmap.get("password"), null));
					user.setId(Convert.strToLong(sessionmap.get("id"), -1L));
					user.setRealName(Convert.strToStr(
							sessionmap.get("realName"), null));
					user.setKefuname(Convert.strToStr(
							sessionmap.get("kefuname"), null));
					user.setUserName(Convert.strToStr(
							sessionmap.get("username"), null));
					user.setVipStatus(Convert.strToInt(
							sessionmap.get("vipStatus"), -1));
					user.setHeadImage((String) (dr.get("headImg") == null ? ""
							: dr.get("headImg")));
					user.setEnable(Convert.strToInt(dr.get("enable") + "", -1));
					user.setPersonalHead(Convert.strToStr(
							sessionmap.get("personalHead"), null));
					user.setKefuid(Convert.strToInt(sessionmap.get("tukid"), -1));
					user.setCreditLimit(Convert.strToStr(
							sessionmap.get("usableCreditLimit"), null));
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return user;
	}

	/**
	 * @MethodName: loginCountReFresh
	 * @Param: UserService
	 * @Author: gang.lv
	 * @Date: 2013-4-4 上午10:34:45
	 * @Return:
	 * @Descb: 刷新登录计数
	 * @Throws:
	 */
	public void loginCountReFresh(long userId) throws Exception {
		if (userId > -1) {
			Connection conn = MySQL.getConnection();
			try {
				MySQL.executeNonQuery(conn,
						" update t_user set loginCount = loginCount + 1 where id="
								+ userId);
				conn.commit();
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				conn.rollback();

				throw e;
			} finally {
				conn.close();
			}
		}
	}

	// =================登陆中初始化LoginVerify
	public LoginVerify getLoginVerify(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		LoginVerify loginVerify = null;
		Map<String, String> spmap = new HashMap<String, String>();
		Map<String, String> vpmap = new HashMap<String, String>();
		try {
			Dao.Views.v_t_user_loginsession_user sessond = new Dao().new Views().new v_t_user_loginsession_user();
			Dao.Views.v_t_login_session_verify verify = new Dao().new Views().new v_t_login_session_verify();
			DataSet sdataSet = sessond.open(conn, "", " id=" + userId, "", -1,
					-1);
			spmap = DataSetHelper.dataSetToMap(sdataSet);
			DataSet vdataSet = verify.open(conn, "", " id=" + userId, "", -1,
					-1);
			vpmap = DataSetHelper.dataSetToMap(vdataSet);
			if (spmap != null && spmap.size() > 0 && vpmap != null
					&& vpmap.size() > 0) {
				loginVerify = new LoginVerify();
				loginVerify.setJbStatus(Convert.strToInt(
						spmap.get("tpauditStatus"), -1));
				loginVerify.setAllworkjbStatus(Convert.strToInt(
						spmap.get("twsum"), -1));
				loginVerify.setIdentyVerifyStatus(Convert.strToInt(
						vpmap.get("identyauditStatus"), -1));
				loginVerify.setWorkVerifyStatus(Convert.strToInt(
						vpmap.get("workauditStatus"), -1));
				loginVerify.setAddressVerifyStatus(Convert.strToInt(
						vpmap.get("addryauditStatus"), -1));
				loginVerify.setResponseVerifyStatus(Convert.strToInt(
						vpmap.get("responsauditStatus"), -1));
				loginVerify.setIncomeVerifyStatus(Convert.strToInt(
						vpmap.get("incomeauditStatus"), -1));
				loginVerify.setFangchanVerifyStatus(Convert.strToInt(
						vpmap.get("fcyauditStatus"), -1));
				loginVerify.setGcVerifyStatus(Convert.strToInt(
						vpmap.get("gcauditStatus"), -1));
				loginVerify.setJhVerifyStatus(Convert.strToInt(
						vpmap.get("jhauditStatus"), -1));
				loginVerify.setXlVerifyStatus(Convert.strToInt(
						vpmap.get("xlauditStatus"), -1));
				loginVerify.setJsVerifyStatus(Convert.strToInt(
						vpmap.get("jsauditStatus"), -1));
				loginVerify.setSjVerifyStatus(Convert.strToInt(
						vpmap.get("sjauditStatus"), -1));
				loginVerify.setWbVerifyStatus(Convert.strToInt(
						vpmap.get("wbauditStatus"), -1));
				loginVerify.setSpVerifyStatus(Convert.strToInt(
						vpmap.get("spauditStatus"), -1));
				loginVerify.setXcVerifyStatus(Convert.strToInt(
						vpmap.get("xcauditStatus"), -1));
				loginVerify.setDbVerifyStatus(Convert.strToInt(
						vpmap.get("dbauditStatus"), -1));
				loginVerify.setDyVerifyStatus(Convert.strToInt(
						vpmap.get("dyauditStatus"), -1));
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return loginVerify;
	}

	public User jumpToWorkData(Long userId) throws Exception {
		// 更新user表中的认证状态
		User user = null;
		long uId = -1L;
		Connection conn = MySQL.getConnection();
		Map<String, String> sessionmap = new HashMap<String, String>();
		try {
			Dao.Views.v_t_user_loginsession_user sessionuser = new Dao().new Views().new v_t_user_loginsession_user();

			DataSet dataSet = sessionuser.open(conn, "", " id=" + userId, "",
					-1, -1);
			sessionmap = DataSetHelper.dataSetToMap(dataSet);

			if (sessionmap != null && sessionmap.size() > 0) {
				user = new User();
				user.setAuthStep(Convert.strToInt(sessionmap.get("authStep"),
						-1));
				user.setEmail(Convert.strToStr(sessionmap.get("email"), null));
				user.setPassword(Convert.strToStr(sessionmap.get("password"),
						null));
				user.setId(Convert.strToLong(sessionmap.get("id"), -1L));
				user.setRealName(Convert.strToStr(sessionmap.get("realName"),
						null));
				user.setKefuname(Convert.strToStr(sessionmap.get("kefuname"),
						null));
				user.setUserName(Convert.strToStr(sessionmap.get("username"),
						null));
				user.setVipStatus(Convert.strToInt(sessionmap.get("vipStatus"),
						-1));
				user.setKefuid(Convert.strToInt(sessionmap.get("tukid"), -1));

			}
		} catch (Exception e) {
		} finally {
			conn.close();
		}

		return user;

		// ===============================================================================

	}

	// 查询用户的最新状态
	public Map<String, String> querynewStatus(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> sessionmap = null;
		try {
			Dao.Views.v_t_user_loginsession_user sessionuser = new Dao().new Views().new v_t_user_loginsession_user();

			DataSet dataSet = sessionuser.open(conn, "", " id=" + userId, "",
					-1, -1);
			sessionmap = DataSetHelper.dataSetToMap(dataSet);

		} catch (Exception e) {
		} finally {
			conn.close();
		}

		return sessionmap;

		// ===============================================================================
	}

	/**
	 * 更新申请vip时候的步骤和状态
	 * 
	 * @param userId
	 * @param authStep
	 *            认证步骤
	 * @param vipStatus
	 *            会员状态
	 * @return User实体
	 * @throws Exception
	 */
	public Long updataUserVipStatus(Long userId, int authStep, int vipStatus,
			int servicePersonId, String content, String vipFee, String username)
			throws Exception {
		StringBuffer msg = new StringBuffer();
		long uId = -1L;
		Connection conn = MySQL.getConnection();
		try {
			uId = userDao.updateUser(conn, userId, authStep, vipStatus,
					servicePersonId, content, vipFee);
			if (uId <= 0) {
				conn.rollback();
				return -1L;
			} else {
				// 发送站内信
				msg.append("尊敬的" + username + ",你申请vip成功");
				// 发站内信
				uId = sendmsgService.sendCheckMail(userId, " 申请vip审核通知",
						msg.toString(), 2, -1);// 2管理员信件 -1 后台管理员
				if (uId <= 0) {
					conn.rollback();
					return -1L;
				}
			}

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return uId;
	}

	public Long frontVerificationEmial(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Long retut = -1L;

		user.enable.setValue(1);
		try {
			retut = user.update(conn, " id=" + userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return retut;
	}

	public Long frontUpdateUser(Long id, String name, String gender,
			String mobilePhone, String qq, Long provinceId, Long cityId,
			Long areaId, String postalcode) throws Exception {
		Long retut = -1L;
		try {
			retut = updateUser(id, null, null, null, name, gender, mobilePhone,
					qq, provinceId, cityId, areaId, postalcode, null, null,
					null, null, null, null, null);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
		return retut;
	}

	/**
	 * 处理前台用户修改头像
	 * 
	 * @param userId
	 *            用户id
	 * @param headImg
	 *            图片地址
	 * @return Long
	 * @throws SQLException
	 */
	public Long frontUpdateUserHeadImg(Long userId, String headImg)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Long retut = -1L;
		try {
			user.headImg.setValue(headImg);
			retut = user.update(conn, " id=" + userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return retut;
	}

	/**
	 * 处理前台用户修改密码
	 * 
	 * @param userId
	 *            用户id
	 * @param password
	 *            新密码
	 * @return Long
	 * @throws SQLException
	 */
	public Long frontUpdatePassword(Long userId, String password)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Dao.Tables.t_user user = new Dao().new Tables().new t_user();
		Long retut = -1L;
		try {
			user.password.setValue(password);
			retut = user.update(conn, " id=" + userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return retut;
	}

	/**
	 * 修改用户邮箱验证状态
	 * 
	 * @Title: updateUserEmailStatus
	 * @param id
	 *            用户ID
	 * @param status
	 *            标志邮箱是否验证通过 (0:未通过1:通过)
	 * @throws SQLException
	 * @return Long
	 */
	public Long updateUserEmailStatus(Long id, Integer status) throws Exception {
		Connection conn = MySQL.getConnection();
		long userId = -1;

		try {
			userId = userDao.updateUserEmailStatus(conn, id, status);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return userId;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param id
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public Long updateUserPassword(Long id, String password) throws Exception {
		Connection conn = MySQL.getConnection();
		long userId = -1;

		try {
			userId = userDao.updateUserPassword(conn, id, password);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return userId;
	}

	/**
	 * 根据用户id查询用户信息
	 * 
	 * @param id
	 * @throws DataException
	 * @throws SQLException
	 * @return Map<String,String>
	 */
	public Map<String, String> queryUserById(long id) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryUserById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询用户的五大基本资料的计数
	 * 
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> queryPicturStatuCount(long id) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryPicturStatuCount(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询前台上传图片的图片状态
	 * 
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> queryUserPictureStatus(long id) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryUserPictureStatus(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询用户前台五大基本资料信息和显示详细图片的第一张
	 * 
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryBasePicture(long id) throws Exception {
		Connection conn = MySQL.getConnection();

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = userDao.queryBasePicture(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询用户前台可选大基本资料信息和显示详细图片的第一张
	 * 
	 * @param id
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> querySelectPicture(long id)
			throws Exception {
		Connection conn = MySQL.getConnection();

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = userDao.querySelectPicture(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询每一个证件的上传类型的图片数据显示
	 * 
	 * @param tmid
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryPerTyhpePicture(Long tmid)
			throws Exception {
		Connection conn = MySQL.getConnection();

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = userDao.queryPerTyhpePicture(conn, tmid);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询图片类型
	 * 
	 * @param userId
	 * @param tmid
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */

	public Map<String, String> queryPitcturTyep(Long userId, long tmid)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryPitcturTyep(conn, tmid, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询图片id
	 * 
	 * @param userId
	 * @param tmid
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */

	public Map<String, String> queryPitcturId(Long userId, long tmid)
			throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryPitcturId(conn, tmid, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public Map<String, String> queryIdByUser(String userName) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryIdByUser(conn, userName);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	public Map<String, String> queryPassword(String email) throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryPassword(conn, email);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询客服列表
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> querykefylist() throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			map = userDao.querykefylist(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 用户基本信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryPersonById(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryPersonById(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 查询vip页面状态参数
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryVipParamList(long id) throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.queryVipParamList(conn, id);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * add by houli 查找用户资金管理信息
	 * 
	 * @param userDao
	 */
	public void queryUserFundInfo(PageBean<Map<String, Object>> pageBean,
			String userName) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态为空
		String command = " ";
		if (userName != null) {
			command += " and username like '%"
					+ StringEscapeUtils.escapeSql(userName) + "%' ";
		}
		try {
			dataPage(conn, pageBean, "v_t_user_fund_lists", "*", "", command);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询黑名单用户
	 * 
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryBlacklistUser(PageBean<Map<String, Object>> pageBean,
			String userName) throws Exception {
		Connection conn = MySQL.getConnection();
		// 手机变更状态为空
		StringBuffer command = new StringBuffer();
		command.append("and enable=3");
		if (StringUtils.isNotBlank(userName) && !userName.equals("")) {
			command.append(" and username like '%");
			command.append(StringEscapeUtils.escapeSql(userName));
			command.append("%'");
		}

		try {
			dataPage(conn, pageBean, "v_blacklist_list", "*", "",
					command.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
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
	public Long updateEnable(Long id, Integer enable) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = userDao.updateEnable(conn, id, enable);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	/**
	 * 查询锁定用户或未锁定用户
	 * 
	 * @param userName
	 *            用户名
	 * @param realName
	 *            真实名字
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @param lockType
	 *            判断是否锁定，1为未锁定，2为锁定
	 * @param pageBean
	 * @throws SQLException
	 * @throws DataException
	 */
	public void queryLockUsers(String userName, String realName,
			String startTime, String endTime, int lockType, PageBean pageBean)
			throws Exception {

		Connection conn = MySQL.getConnection();
		StringBuilder condition = new StringBuilder();
		if (StringUtils.isNotBlank(userName)) {
			condition.append(" and username like '%");
			condition.append(StringEscapeUtils.escapeSql(userName));
			condition.append("%' ");
		}
		if (StringUtils.isNotBlank(realName)) {
			condition.append(" and realName like '%");
			condition.append(StringEscapeUtils.escapeSql(realName));
			condition.append("%' ");
		}
		if (lockType == 1) {// 启用
			if (StringUtils.isNotBlank(startTime)) {
				condition.append(" and createTime >= '");
				condition.append(StringEscapeUtils.escapeSql(startTime));
				condition.append("' ");
			}
			if (StringUtils.isNotBlank(endTime)) {
				condition.append(" and createTime <= '");
				condition.append(StringEscapeUtils.escapeSql(endTime));
				condition.append("' ");
			}
			condition.append(" and enable=1 ");
		} else if (lockType == 2) { // 锁定
			if (StringUtils.isNotBlank(startTime)) {
				condition.append(" and lockTime >= '");
				condition.append(StringEscapeUtils.escapeSql(startTime));
				condition.append("' ");
			}
			if (StringUtils.isNotBlank(endTime)) {
				condition.append(" and lockTime <= '");
				condition.append(StringEscapeUtils.escapeSql(endTime));
				condition.append("' ");
			}
			condition.append(" and enable=2 ");
		}

		try {
			dataPage(conn, pageBean, "v_t_user_lock", "*", "",
					condition.toString());
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	public Map<String, String> querymaterialsauthtypeCount() throws Exception {
		Connection conn = MySQL.getConnection();

		Map<String, String> map = new HashMap<String, String>();
		try {
			map = userDao.querymaterialsauthtypeCount(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return map;
	}

	/**
	 * 更新锁定用户状态
	 * 
	 * @param ids
	 * @param enable
	 * @return
	 * @throws SQLException
	 */
	public long updateLockedStatus(String ids, int enable) throws Exception {
		Connection conn = MySQL.getConnection();
		Long result = -1L;
		try {
			result = userDao.updateLockedStatus(conn, ids, enable);

			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * add by houli 查看用户是否已经绑定了手机号码
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public Map<String, String> queryBindingMobleUserInfo(Long userId)
			throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			return myHomeInfoSettingDao.queryBindingMobleUserInfo(conn, userId,
					-1, -1);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 查询所有用户
	 * 
	 * @return
	 * @throws DataException
	 * @throws SQLException
	 */
	public List<Map<String, Object>> queryUserAll() throws Exception {

		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> result = null;
		try {
			result = userDao.queryUserAll(conn);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return result;

	}

	public long queryUserIdByPhone(String cellPhone) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = userDao.queryUserIdByPhone(conn, cellPhone);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 修改用户密码
	 * 
	 * @param id
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public long updateLoginPwd(Long userId, String password) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = userDao.updatePwd(conn, userId, password, 1);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	public Long updateDealPwd(long userId, String password) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1;
		try {
			result = userDao.updatePwd(conn, userId, password, 2);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 查询用户信息
	 * 
	 * @param userName
	 *            邮箱号，手机号，用户名
	 * @param pwd
	 *            密码
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> queryUserByUserAndPwd(String userName, String pwd)
			throws Exception {
		Connection conn = MySQL.getConnection();
		Map<String, String> result = null;
		try {
			result = userDao.queryUserByUserAndPwd(conn, userName, pwd);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	public Map<String, String> queryUserAmount(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			return userDao.queryUserAmount(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 手机查询
	 * 
	 * @param cellphone
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Long isPhoneExist(String cellphone) throws Exception {
		Connection conn = MySQL.getConnection();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = userDao.isPhoneExist(conn, cellphone);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return list.size() <= 0 ? -1L : 1L;
	}

	public long updateEmalByid(long id, String email) throws Exception {
		long result = -1;
		Connection conn = MySQL.getConnection();
		try {
			result = userDao.updateEmalByid(conn, id, email);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}

		return result;
	}

	public Map<String, String> queEmailUser(Long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		try {
			return myHomeInfoSettingDao.queEmailUser(conn, userId);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 激活账户
	 * 
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public long updateUserActivate(long userId) throws Exception {
		Connection conn = MySQL.getConnection();
		long result = -1L;
		try {
			result = userDao.updateUserActivate(conn, userId);
			conn.commit();
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			conn.rollback();

			throw e;
		} finally {
			conn.close();
		}
		return result;
	}

	/**
	 * 记环迅orderNo yijiUserId userId的记录表
	 * 
	 * @param userId
	 *            userId
	 * @param hxUserId
	 *            环迅商户号
	 * @param orderNo
	 *            流水号
	 * @throws Exception
	 */
	public void savehxOrderNo(long userId, String yiJiUserId, String orderNo,
			Map<String, Object>... yuBuMap) throws Exception {

		Connection conn = MySQL.getConnection();
		try {
			JSONArray json = JSONArray.fromObject(yuBuMap);
			Dao.Tables.t_hx_user t_yiji_user = new Dao().new Tables().new t_hx_user();
			t_yiji_user.userId.setValue(userId);
			t_yiji_user.hxUserId.setValue(yiJiUserId);
			t_yiji_user.orderNo.setValue(orderNo);
			t_yiji_user.hxBuMap.setValue(json.toString());
			t_yiji_user.insert(conn);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 记乾多多orderNo yijiUserId userId的记录表
	 * 
	 * @param userId
	 *            userId
	 * @param hxUserId
	 *            乾多多商户号
	 * @param orderNo
	 *            流水号
	 * @throws Exception
	 */
	public void saveQianduoduoOrderNo(long userId, String QianduoduoAccount,
			String orderNo, Map<String, String>... yuBuMap) throws Exception {

		Connection conn = MySQL.getConnection();
		try {
			JSONArray json = JSONArray.fromObject(yuBuMap);
			Dao.Tables.t_hx_user t_yiji_user = new Dao().new Tables().new t_hx_user();
			t_yiji_user.userId.setValue(userId);
			t_yiji_user.hxUserId.setValue(QianduoduoAccount);
			t_yiji_user.orderNo.setValue(orderNo);
			t_yiji_user.hxBuMap.setValue(json.toString());
			t_yiji_user.insert(conn);
			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	/**
	 * 根据UserID查询环迅商户号
	 * 
	 * @param hxUserId
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> quePortUserId(String hxUserId, String pMerBillNo)
			throws Exception {
		log.info("进入quePortUserId方法--根据UserID查询环迅商户号");
		Connection conn = MySQL.getConnection();
		try {
			return myHomeInfoSettingDao.quePortUserId(conn, hxUserId,
					pMerBillNo);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
	}

	/**
	 * 根据IPS账号修改用户信息
	 * 
	 * @param pIpsBillNo
	 * @return
	 * @throws Exception
	 */
	public boolean queryUserByIpsBillNo(String pIpsBillNo) throws Exception {
		Connection conn = MySQL.getConnection();

		boolean flag = false;
		try {
			flag = userDao.queryUserByIpsBillNo(conn, pIpsBillNo);
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} finally {
			conn.close();
		}
		return flag;
	}

	/**
	 * @MethodName: addUserUsableAmount
	 * @Param: UserServiceDao
	 * @Author: daizhiyue
	 * @Date: 2015-1-20 下午05:01:09
	 * @Return:
	 * @Descb: 给用户添加可用资金
	 * @Throws:
	 */
	public long addUserUsableAmount(Connection conn, double amount, long userId) throws Exception{
		long res = -1l;
		try {
			// 得到信息额度类型
			res = userDao.addUserUsableAmount(conn,amount,userId);

		
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();			

			throw e;
		} 

		return res;
	}

	public void setRelationDao(RelationDao relationDao) {
		this.relationDao = relationDao;
	}

	public void setShoveBorrowAmountTypeDao(
			ShoveBorrowAmountTypeDao shoveBorrowAmountTypeDao) {
		this.shoveBorrowAmountTypeDao = shoveBorrowAmountTypeDao;
	}

	public OperationLogDao getOperationLogDao() {
		return operationLogDao;
	}

	public void setOperationLogDao(OperationLogDao operationLogDao) {
		this.operationLogDao = operationLogDao;
	}

	public MyHomeInfoSettingDao getMyHomeInfoSettingDao() {
		return myHomeInfoSettingDao;
	}

	public void setMyHomeInfoSettingDao(
			MyHomeInfoSettingDao myHomeInfoSettingDao) {
		this.myHomeInfoSettingDao = myHomeInfoSettingDao;
	}

	public void setBeVipDao(BeVipDao beVipDao) {
		this.beVipDao = beVipDao;
	}

	public void setSendmsgService(SendmsgService sendmsgService) {
		this.sendmsgService = sendmsgService;
	}

	public ReturnedmoneyDao getReturnedmoneyDao() {
		return returnedmoneyDao;
	}

	public void setReturnedmoneyDao(ReturnedmoneyDao returnedmoneyDao) {
		this.returnedmoneyDao = returnedmoneyDao;
	}

	public final CoinDao getCoinDao() {
		return coinDao;
	}

	public final void setCoinDao(CoinDao coinDao) {
		this.coinDao = coinDao;
	}
}
