/**
 * 
 */
package com.sp2p.dao.admin;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.fp2p.helper.shove.DataSetHelper;
import com.shove.data.DataException;
import com.shove.data.DataSet;
import com.shove.data.dao.MySQL;

/**
 * 体验标流标处理.
 * @author 殷梓淞
 * @since V1.0.0
 *
 */
public class ReBackExperienceBorrowDao {

	/**
	 * 获取通知模板.
	 * @param conn
	 * @param templateType
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getNoticeTemplate(Connection conn,String templateType)
			throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append(" select template from t_approve_notice_template where nid = '"+templateType+"'; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 增加资金记录
	 * @param conn
	 * @param t_invest_amount
	 * @param t_investor
	 * @param t_invest_id
	 * @param borrowerId
	 * @param t_content
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertUpdateInvestorExpreienceMoneyRecord(Connection conn,double t_real_amount, long t_investor,long t_invest_id, String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
		command.append(" select "+t_investor+", now(), 154, '解冻投资体验标冻结金额', "+t_real_amount+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, round(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),2),");
		command.append( "-1,0,'"+t_content+"', 0, "+t_invest_id+", '', 0 , "+t_real_amount+",b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+t_investor+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	
	/**
	 * 更新用户体验金金额.
	 * @param conn
	 * @param t_invest_amount 投资金额.
	 * @param investorId
	 * @return
	 * @throws SQLException
	 */
	public long updateInvestorExpreienceMoney(Connection conn,double t_invest_amount, long investorId)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_experiencemoney set freezeMoney = freezeMoney-"+t_invest_amount+",usableMoney = usableMoney + "+t_invest_amount+" where userid = "+investorId+"; ");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	
	/**
	 * 更新借款标的状态.
	 * @param conn
	 * @param in_status 
	 * @param borrowid
	 */
	public long updateBorrowStatus(Connection conn, long in_status, long borrowid) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_borrow set borrowStatus="+in_status+", sort = 0 where id = "+borrowid+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	/**
	 * 获取通知借款人需要的信息.	
	 * @param conn
	 * @param borrowerId
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public Map<String, String> getBorrowerInfo(Connection conn, long borrowerId)throws SQLException, DataException {
		StringBuffer command = new StringBuffer();
		command.append(" select a.userId,a.cellphone, b.email,b.username FROM t_user b left join t_person a on a.userId = b.id where b.id = "+borrowerId+"; ");
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
				+ ", '借款成功报告','loanSucEnable','" + username + "','"
				+ email + "','" + telphone + "','" + t_mail_template + "','"
				+ t_email_template + "','" + t_sms_template + "'," + borrowId
				+ ",	now(),	'borrow');");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 获取所有投资人
	 * @param conn
	 * @param borrowId 借款标的Id
	 * @return
	 * @throws SQLException
	 * @throws DataException
	 */
	public List<Map<String, String>> getAllInvestors(Connection conn, long borrowId)throws SQLException, DataException{
		StringBuffer command = new StringBuffer();
		command.append("select a.id,a.deadline,a.monthRate, a.investAmount, a.realAmount, a.investor, b.vipStatus,b.username, a.recivedPrincipal, a.recievedInterest, a.check_principal, a.check_interest  from t_invest a left join t_user b on a.investor = b.id  where a.borrowId="+borrowId+"; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		dataSet.tables.get(0).rows.genRowsMap();
		command = null;
		return dataSet.tables.get(0).rows.rowsMap;	
	}
}
