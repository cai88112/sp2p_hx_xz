package com.sp2p.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;

/**
 * 体验金投标功能。
 * 
 * @author Administrator
 *
 */
public class TenderExperienceMoneyDao {

	/**
	 * 版本号校验.
	 * 
	 * @param version
	 *            版本号.
	 * @return 如果成功，返回值大于0.
	 * @desc 
	 *       防止在做校验的时候，标已经被其他进程修改后而提交改标。（即：正常是校验到投标整个过程，t_borrow中该标都没有被其他进程进行操作).
	 */
	public long versionVerify(Connection conn, int version, long borrow_id)
			throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_borrow set version = version + 1 where id = "
				+ borrow_id
				+ "  and borrowStatus = 2	and  borrowAmount >	hasInvestAmount	and version	= "
				+ version + ";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 更新借款信息中的已投资总额和数量.
	 * 
	 * @param conn
	 *            sql连接对象.
	 * @param investAmount
	 *            投资金额.
	 * @param borrowId
	 *            借款标id.
	 * @return 如果成功，返回值大于0.
	 */
	public long updateTborrow(Connection conn, double investAmount,
			long borrowId) throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("  update t_borrow set investNum	= investNum	+ 1	,hasInvestAmount = hasInvestAmount + "
				+ investAmount
				+ " where id = "
				+ borrowId
				+ "  and borrowStatus = 2	and borrowAmount > hasInvestAmount;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 增加投资记录.
	 * 
	 * @param conn
	 *            sql连接对象.
	 * @param investAmount
	 *            投资金额.
	 * @param investorId
	 *            投资人id.
	 * @param borrowId
	 *            借款标id.
	 * @param deadline
	 *            借款期限.
	 * @param monthRate
	 *            月利率.
	 * @return 如果成功，返回值大于0.
	 */
	public long insertTinvest(Connection conn, double investAmount,
			long investorId, long borrowId, int deadline, double monthRate)
			throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_invest(investAmount, realAmount, investor, oriInvestor, investTime, borrowId,deadline,monthRate) values("
				+ investAmount
				+ ","
				+ investAmount
				+ ","
				+ investorId
				+ ","
				+ investorId
				+ ",now(),"
				+ borrowId
				+ ","
				+ deadline
				+ ","
				+ monthRate * 100 + ");");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 投资人投资成功冻结体验金.
	 * 
	 * @param conn
	 *            sql连接对象.
	 * @param investAmount
	 *            投资金额.
	 * @param userId
	 *            投资人id.
	 * @return 如果成功，返回值大于0.
	 */
	public long freezeInvestorExperienceMoney(Connection conn,
			double investAmount, long userId) throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("  update t_experiencemoney set usableMoney = usableMoney - "
				+ investAmount
				+ ", freezeMoney = freezeMoney + "
				+ investAmount
				+ " where userid = "
				+ userId
				+ " and status =0;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 更新借款投资进度比例.
	 * 
	 * @param conn
	 *            sql连接对象.
	 * @param borrowId
	 *            借款标id.
	 * @return
	 * @throws SQLException
	 */
	public long updateTborrowScale(Connection conn, long borrowId)
			throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("  update t_borrow  set amount_scale = hasInvestAmount  / borrowAmount  * 100  where  id = "
				+ borrowId + ";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 获取mail模板.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getMailTemplate(Connection conn)
			throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append(" select template from t_approve_notice_template where nid = 'tender'; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 获取e_mail模板.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getEmailTemplate(Connection conn)
			throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append(" select template from t_approve_notice_template where nid = 'e_tender'; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 获取sms模板.
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getSmsTemplate(Connection conn)
			throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append(" select template from t_approve_notice_template where nid = 's_tender'; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}

	/**
	 * 插入通知任务.
	 * 
	 * @param conn
	 * @param userId
	 *            投资人id.
	 * @param username
	 *            投资人用户名.
	 * @param email
	 *            邮箱地址.
	 * @param telphone
	 *            电话.
	 * @param t_mail_template
	 *            mail模板.
	 * @param t_email_template
	 *            email模板.
	 * @param t_sms_template
	 *            sms模板.
	 * @param borrowId
	 *            借款标id.
	 * @return
	 */
	public long insertNoticeTask(Connection conn, long userId, String username,
			String email, String telphone, String t_mail_template,
			String t_email_template, String t_sms_template, long borrowId)
			throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_notice_task(user_id,sendtitle,s_nid,	username, email, telphone, mail_info, email_info, sms_info, operate_id,	add_time, operate_identify) ");
		command.append(" values(" + userId
				+ ", '理财投资报告','capitalChangeEnable','" + username + "','"
				+ email + "','" + telphone + "','" + t_mail_template + "','"
				+ t_email_template + "','" + t_sms_template + "'," + borrowId
				+ ",	now(),	'borrow');");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 添加体验金资金记录.
	 * 
	 * @param conn
	 *            sql连接对接.
	 * @param userId
	 *            投资人id.
	 * @param investAmount
	 *            投资金额.
	 * @param content
	 *            备注.
	 * @param borrowId
	 *            借款标id.
	 * @param pMerBillNo
	 *            订单号.
	 * @return
	 * @throws SQLException
	 */
	public long insertTfundrecord(Connection conn, long userId,
			double investAmount, String content, long borrowId,
			String pMerBillNo) throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,spending,borrow_id,pMerBillNo) ");
		command.append(" select "
				+ userId
				+ ", now(), 653, '冻结体验金投标金额', "
				+ investAmount
				+ ", a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0), ");
		command.append(" -1,0, '"
				+ content
				+ "', 0, "
				+ borrowId
				+ ", '', 0 ,0,b.borrowId,'"
				+ pMerBillNo
				+ "'	from t_experiencemoney a left join t_invest b on a.id = b.investor where a.userid = "
				+ userId + " group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 投标如果达到满标条件就更新为满标状态
	 * 
	 * @param conn
	 *            sql连接对接.
	 * @param borrowId
	 *            借款标id.
	 * @return
	 * @throws SQLException
	 */
	public long setfullBorrow(Connection conn, long borrowId)
			throws SQLException {
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_borrow set borrowStatus =3, remainTimeStart = remainTimeEnd ,sort =5  where id = "
				+ borrowId
				+ " and borrowAmount = hasInvestAmount and borrowStatus = 2;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	//
	// public void tender() {
	//
	//
	//
	//
	//
	// // #除开流转标外的其他标的
	// // if @subscribe_status = 1 then
	// // #修改已投标份数
	// // update t_borrow set hasCirculationNumber = hasCirculationNumber +
	// in_num where id = in_bid;
	// // end if;
	//
	//
	// // #投资人投资成功资金冻结
	//
	// #更新借款投资进度比例
	// select template from t_approve_notice_template where nid = 'tender';
	//
	// set t_mail_template = f_get_notice_template('tender');
	// set t_mail_template = replace(t_mail_template, 'title', @borrowTitle);
	// set t_mail_template = replace(t_mail_template, '[voluntarily]', '');
	// set t_mail_template = replace(t_mail_template, 'date', now());
	// set t_mail_template = replace(t_mail_template, 'investAmount',
	// in_invest_amount);
	//
	// //select template from t_approve_notice_template where nid = 'e_tender';
	// set t_email_template = f_get_notice_template('e_tender');
	// set t_email_template = replace(t_email_template, 'title', @borrowTitle);
	// set t_email_template = replace(t_email_template, '[voluntarily]', '');
	// set t_email_template = replace(t_email_template, 'date', now());
	// set t_email_template = replace(t_email_template, 'investAmount',
	// in_invest_amount);
	//
	// #插入通知任务
	// set @username = '';
	// set @email = '';
	// set @telphone = '';
	// select a.cellphone, b.username, b.email into @telphone, @username, @email
	// from t_user b left join
	// t_person a on a.userId = b.id where b.id = in_uid;
	//
	// //select template from t_approve_notice_template where nid = 'e_tender';
	// set t_sms_template = f_get_notice_template('s_tender');
	// set t_sms_template = replace(t_sms_template, 'username', @username);
	// set t_sms_template = replace(t_sms_template, 'title', @borrowTitle);
	// set t_sms_template = replace(t_sms_template, '[voluntarily]', '');
	// set t_sms_template = replace(t_sms_template, 'date', now());
	// set t_sms_template = replace(t_sms_template, 'investAmount',
	// in_invest_amount);
	//
	//
	// // insert into t_notice_task(user_id,sendtitle,s_nid, username, email,
	// telphone, mail_info, email_info, sms_info, operate_id, add_time,
	// operate_identify)
	// // values(in_uid, '理财投资报告','capitalChangeEnable', @username, @email,
	// @telphone, t_mail_template, t_email_template, t_sms_template, in_bid,
	// now(), 'borrow');
	// set t_content = f_link(in_basepath,in_bid,@borrowTitle,'','','');
	// set t_content = concat('投标借款[', t_content, '],冻结投标金额');
	// #添加资金记录
	// insert into t_fundrecord (userId, recordTime, operateType, fundMode,
	// handleSum, usableSum, freezeSum, dueinSum,
	// trader, dueoutSum, remarks, oninvest, paynumber, paybank,
	// cost,spending,borrow_id,pMerBillNo) select in_uid, now(), 653,
	// f_moneyDecode(653), in_invest_amount,
	// a.usableSum as usableSum, a.freezeSum as freezeSum,
	// ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal -
	// b.hasInterest),0),
	// -1,0, t_content, 0, in_bid, '', 0 ,0,b.borrowId,in_pMerBillNo from t_user
	// a left join t_invest b on a.id = b.investor
	// where a.id = in_uid group by a.id;
	//
	// #本次投标如果达到满标条件就更新为满标状态
	// update t_borrow set borrowStatus =3, remainTimeStart = remainTimeEnd
	// ,sort =5 where id = in_bid and borrowAmount = hasInvestAmount and
	// borrowStatus = 2;
	// }
}
