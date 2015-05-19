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
 * 体验标满标审核.
 * @author 殷梓淞
 * @since v1.0.0
 *
 */
public class AuditExperienceMoneyDao {

	/**
	 * 从预还款表中查出该条借款的还款信息
	 * @param conn
	 * @param identify 满标审核的唯一标识.
	 * @return
	 * @throws DataException 
	 */
	public Map<String, String> getPreRepaymentInfo(Connection conn, String identify) throws SQLException, DataException{
		StringBuffer command = new StringBuffer();
		command.append(" select	totalSum,totalAmount,stillInterest,stillPrincipal,mRate,repayPeriod,repayDate,principalBalance,interestBalance ");
		command.append(" from t_pre_repayment where	identify = '"+identify+"' limit	0,1; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 查询该标的的信息
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 * @throws DataException 
	 */
	public Map<String, String> getBorrowInfo(Connection conn, long borrowId) throws SQLException, DataException{
		StringBuffer command = new StringBuffer();
		command.append(" select  a.publisher,a.borrowTitle,a.annualRate,a.paymentMode,a.borrowAmount,a.deadline,a.borrowWay,a.excitationType,a.excitationSum,a.isDayThe,b.username,a.frozen_margin,a.version ,nid_log ");
		command.append(" from t_borrow a left join t_user b on a.publisher=b.id where a.id ="+borrowId+"; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	//并发处理，update t_borrow set version	=	version	+	1	where id = in_bid and borrowStatus	= 3	and version	=	@version;
    //该处体验金没有用到（防止第三方同步和异步回调重复操作）
	
//	处理审核通过
	/**
	 * 更新借款标的状态.
	 * @param conn
	 * @param in_status 
	 * @param borrowid
	 */
	public long updateBorrowStatus(Connection conn, long in_status, long borrowid) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_borrow set borrowStatus="+in_status+", auditTime = now() where id = "+borrowid+" and borrowStatus	= 3;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 查询计算借款手续费需要的参数
	 * @param conn
	 * @param nid_log .
	 * @return
	 * @throws DataException 
	 */
	public Map<String, String> getParmFromBorrowTypeLog(Connection conn,String nid_log) throws DataException{
		StringBuffer command = new StringBuffer();
		command.append(" select locan_fee,locan_month,locan_fee_month,day_fee ");
		command.append(" from t_borrow_type_log where identifier = '"+nid_log+"' ; ");
		DataSet dataSet = MySQL.executeQuery(conn, command.toString());
		command = null;
		return DataSetHelper.dataSetToMap(dataSet);
	}
	
	/**
	 * 插入风险保障金.
	 * @return
	 * @throws SQLException
	 */
	/**
	 * 
	 * @param conn
	 * @param borrowId 标的id
	 * @param borrowerId 借款人
	 * @return
	 * @throws SQLException
	 */
	public long insertRiskDetil(Connection conn,long borrowerId, long borrowId) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("insert into t_risk_detail(riskBalance,	riskInCome,	riskDate,	riskType,	resource,	trader,	borrowId) ");
		command.append("select sum(riskInCome-riskSpending),	t_risk_amount,	now(),	'收入',	t_resource,"+ borrowerId +","+borrowId+" from t_risk_detail;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
	
	/**
	 * 生成还款记录.
	 * @param conn
	 * @param identify 满标审核的唯一标识.
	 * @return
	 * @throws SQLException
	 */
	public long createRepaymentRecord(Connection conn,String identify) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("insert into	t_repayment(repayPeriod,stillPrincipal,stillInterest,borrowId,principalBalance,interestBalance, repayDate,identify) ");
		command.append("select repayPeriod, stillPrincipal, stillInterest,	borrowId,principalBalance,interestBalance,repayDate,identify ");
		command.append("from t_pre_repayment where identify = '"+identify+"'  order by sort asc;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 生成投资收款记录
	 * @param conn
	 * @param borrowAmount 借款金额.
	 * @param borrowId 借款id.
	 * @return
	 * @throws SQLException
	 */
	public long createInvestGatheringRecord(Connection conn,double borrowAmount,double in_ifeerate,long borrowId) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_invest_repayment(repayId,repayPeriod,repayDate,recivedPrincipal,recivedInterest,principalBalance,interestBalance,invest_id,`owner`,ownerlist,iManageFee,iManageFeeRate,borrow_id) ");
		command.append(" select b.id,b.repayPeriod,b.repayDate,((a.investAmount/"+borrowAmount+")*b.stillPrincipal),((a.investAmount/"+borrowAmount+")*b.stillInterest),((a.investAmount/"+borrowAmount+")*b.principalBalance),");
		command.append(" ((a.investAmount/"+borrowAmount+")*b.interestBalance),a.id,a.investor,a.investor,(((a.investAmount/"+borrowAmount+")*b.stillInterest)*"+in_ifeerate+"),"+in_ifeerate+",b.borrowId  ");
		command.append(" from t_invest a left join t_repayment b on a.borrowId = b.borrowId where b.id is not null and a.circulationForpayStatus = -1 and b.borrowId = "+borrowId+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 纠偏投资应收款明细资金.
	 * @param conn
	 * @param borrowId 借款标的id.
	 * @return
	 * @throws SQLException
	 */
	public long correctInvestScheduleOfAccountsReceivable(Connection conn, long borrowId) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_invest_repayment t1,(select c.minId,	(a.stillPrincipal-b.recivedPrincipal) check_principal,	(a.stillInterest-b.recivedInterest) check_interest  ");
		command.append(" from (select id,	repayPeriod,	stillPrincipal,	stillInterest	from t_repayment	where borrowId	=	"+borrowId+") a left join (select a.repayId, ");
		command.append(" sum(a.recivedPrincipal) recivedPrincipal,	sum(a.recivedInterest) recivedInterest from t_invest_repayment a where a.borrow_id	=	"+borrowId+"	group by a.repayId) ");
		command.append(" b on a.id	=	b.repayId left join (select min(a.id) minId,a.repayId from t_invest_repayment a where a.borrow_id	=	"+borrowId+"	group by a.repayId) c on ");
		command.append(" b.repayId=c.repayId) t2 set	t1.recivedPrincipal	=	t1.recivedPrincipal	+	t2.check_principal,	t1.recivedInterest	=	t1.recivedInterest	+	t2.check_interest where t1.id = t2.minId;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}
			
	/**
	 * 处理投资应收款. 
	 * @param conn
	 * @param borrowId 借款标的id.
	 * @return
	 * @throws SQLException
	 */
	public long dealInvestAccountReceivable(Connection conn, long borrowId) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_invest a,(select invest_id,sum(recivedInterest) as recivedInterest from t_invest_repayment a where a.borrow_id = "+borrowId+"	group by a.invest_id) b ");
		command.append(" set a.check_principal = a.realAmount,	a.check_interest = b.recivedInterest where a.id = b.invest_id; ");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	
	}	
	
	/**
	 * 纠偏投资收款
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 */
	public long correctInvestGathering(Connection conn, long borrowId) throws SQLException{
		long returnId = -1;	
		StringBuffer command = new StringBuffer();
		command.append(" update t_invest a,(select max(a.id) max_id,a.invest_id from t_invest_repayment a where a.borrow_id = "+borrowId+" group by ");
		command.append(" a.invest_id) b set a.max_invest_id = b.max_id where a.id=b.invest_id; ");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	
	}
	
	/**
	 * 更新投资表
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 */
	public long updateTInvest(Connection conn, long borrowId) throws SQLException{
		long returnId = -1;	
		StringBuffer command = new StringBuffer();
		command.append(" update t_invest a,(select a.invest_id,	sum(a.recivedPrincipal) recivedPrincipal from t_invest_repayment a where a.borrow_id = "+borrowId);
		command.append(" group by a.invest_id) b set a.adjust_principal = b.recivedPrincipal where a.id=b.invest_id; ");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	
	}
	
	/**
	 * 更新投资还款表
	 * @param conn
	 * @param borrowId
	 * @return
	 * @throws SQLException
	 */
	public long updateTinvestRepayment(Connection conn, long borrowId) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_invest_repayment	t1,(select (check_principal-adjust_principal)	check_principal,max_invest_id	from	t_invest	where	borrowId	=	"+borrowId+")	t2 ");
		command.append(" set	recivedPrincipal	=	recivedPrincipal	+	check_principal	where t1.id = t2.max_invest_id; ");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	
	}	
			
	/**
	 * 纠偏投资管理费	
	 * @param conn
	 * @param in_ifeerate 投资管理费.
	 * @return
	 * @throws SQLException
	 */
	public long correctInvestRepayment(Connection conn,double in_ifeerate) throws SQLException{
		long returnId = -1;	
		StringBuffer command = new StringBuffer();
		command.append(" update t_invest_repayment set iManageFee = recivedInterest*"+in_ifeerate+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	
	}
	/**
	 * 解冻秒还冻结资金
	 * @param conn
	 * @param fee
	 * @param borrowerId 借款人id
	 * @return
	 * @throws SQLException
	 */
	public long updateSecondFreezeFund(Connection conn,double fee, long borrowerId)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_experiencemoney set usableMoney = usableMoney + "+fee+",freezeMoney=freezeMoney - "+fee+"	where userid = "+borrowerId+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
	
	/**
	 * 插入解冻秒还冻结资金记录.
	 * @param conn
	 * @param fee
	 * @param borrowerId
	 * @param borrow_id
	 * @param t_content 描述.
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertFundRecord(Connection conn,double fee, long borrowerId,long borrow_id,String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost , income,borrow_id, pMerBillNo) ");
		command.append("select "+borrowerId+", now(), 105, f_moneyDecode(105), "+fee+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0),");
		command.append("-1,0,'"+t_content+"', 0, "+borrow_id+", '', 0 , "+fee+",b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+borrowerId+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
	
	/**
	 * 返还发布借款时的冻结金额(与 解冻秒还冻结资金updateSecondFreezeFund相同）
	 * @param conn
	 * @param fee
	 * @param borrowerId 借款人id
	 * @return
	 * @throws SQLException
	 */
	public long returnSecondFreezeFund(Connection conn,double frozen_margin, long borrowerId)throws SQLException{
		long returnId = -1;	
		StringBuffer command = new StringBuffer();
		command.append(" update t_experiencemoney set usableMoney = usableMoney + "+frozen_margin+",freezeMoney=freezeMoney - "+frozen_margin+"	where userid = "+borrowerId+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;
	}

	/**
	 * 插入返还发布借款时的冻结金额记录.
	 * @param conn
	 * @param fee
	 * @param borrowerId
	 * @param borrow_id
	 * @param t_content 描述.
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertReturnSecondFreezeFundRecord(Connection conn,double frozen_margin, long borrowerId,long borrow_id,String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append("insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost ,income,borrow_id,pMerBillNo) ");
		command.append(" select "+borrowerId+", now(), 104, f_moneyDecode(104), round("+frozen_margin+"),a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0),");
		command.append("-1,0,'"+t_content+"', 0, "+borrow_id+", '', 0 , round("+frozen_margin+"),b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+borrowerId+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
	
	
//	#借款成功	
	/**
	 * 更新体验金
	 * @param conn
	 * @param borrowAmount
	 * @param borrowerId
	 * @return
	 * @throws SQLException
	 */
	public long updateExperiencemoneyFund(Connection conn,double borrowAmount, long borrowerId)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_experiencemoney set usableMoney = usableMoney + "+borrowAmount+"	where userid = "+borrowerId+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
	/**
	 * 借款成功资金记录.
	 * @param conn
	 * @param frozen_margin
	 * @param borrowerId
	 * @param borrow_id
	 * @param t_content
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertBorrowFundRecord(Connection conn,double borrowAmount, long borrowerId,long borrow_id,String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
		command.append(" select "+borrowerId+", now(), 101, f_moneyDecode(101), "+borrowAmount+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0),");
		command.append("-1,0,'"+t_content+"', 0, "+borrow_id+", '', 0 , "+borrowAmount+",b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+borrowerId+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
		
	/**
	 * 扣除借款人借款管理费及担保费
	 * @param conn
	 * @param t_b_mfee
	 * @param gt_fee
	 * @param borrowerId
	 * @return
	 * @throws SQLException
	 */
	public long deductExperiencemoneyManagementCost(Connection conn,double t_b_mfee,double gt_fee, long borrowerId)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_experiencemoney set usableMoney = usableMoney - "+t_b_mfee+" - "+gt_fee+"	where userid = "+borrowerId+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}

//	#扣除借款人借款管理费资金记录
	/**
	 * 扣除借款人借款管理费资金记录
	 * @param conn
	 * @param t_b_mfee 借款管理费.
	 * @param borrowerId
	 * @param borrow_id
	 * @param t_content
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
	public long insertDeductExperiencemoneyManagementCostRecord(Connection conn,double t_b_mfee, long borrowerId,long borrow_id,String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
		command.append(" select "+borrowerId+", now(), 601, f_moneyDecode(601), "+t_b_mfee+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0),");
		command.append("-1,0,'"+t_content+"', 0, "+borrow_id+", '', 0 , "+t_b_mfee+",b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+borrowerId+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
//	扣除借款人担保费资金记录
	 /**
	  * 扣除借款人担保费资金记录
	  * @param conn
	  * @param gt_fee 担保费.
	  * @param borrowerId
	  * @param borrow_id
	  * @param t_content
	  * @param in_pMerBillNo
	  * @return
	  * @throws SQLException
	  */
	public long insertDeductExperiencemoneySecurityCostRecord(Connection conn,double gt_fee, long borrowerId,long borrow_id,String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
		command.append(" select "+borrowerId+", now(), 606, f_moneyDecode(606), "+gt_fee+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0),");
		command.append("-1,0,'"+t_content+"', 0, "+borrow_id+", '', 0 , "+gt_fee+",b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+borrowerId+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}
	
//	更新借款管理费及担保费
	public long updateBorrowSecurityAndManagementCost(Connection conn,double t_b_mfee,double gt_fee,long borrow_id) throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_borrow set manageFee = "+t_b_mfee+",guarantee_fee="+gt_fee+"	where id = "+borrow_id+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}

	/**
	 * 扣除借款奖励
	 * @param conn
	 * @param t_excitation_sum 奖励金额.
	 * @param borrowerId
	 * @return
	 * @throws SQLException
	 */
	public long deductExperiencemoneyBorrowAward(Connection conn,double t_excitation_sum,long borrowerId)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" update t_experiencemoney set usableMoney =usableMoney- "+t_excitation_sum+" where userid = "+borrowerId+";");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}

	/**
	 * 扣除借款奖励资金记录.
	 * @param conn
	 * @param t_excitation_sum 奖励金额.
	 * @param borrowerId
	 * @param borrow_id
	 * @param t_content
	 * @param in_pMerBillNo
	 * @return
	 * @throws SQLException
	 */
public long insertDeductExperiencemoneyBorrowAwardRecord(Connection conn,double t_excitation_sum, long borrowerId,long borrow_id,String t_content,String in_pMerBillNo)throws SQLException{
		long returnId = -1;
		StringBuffer command = new StringBuffer();
		command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
		command.append(" select "+borrowerId+", now(), 751, f_moneyDecode(751), "+t_excitation_sum+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, ifnull(sum(b.recivedPrincipal + b.recievedInterest - b.hasPrincipal - b.hasInterest),0),");
		command.append("-1,0,'"+t_content+"', 0, "+borrow_id+", '', 0 , "+t_excitation_sum+",b.borrowId,'"+in_pMerBillNo+"' ");
		command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+borrowerId+" group by a.userid;");
		returnId = MySQL.executeNonQuery(conn, command.toString());
		command = null;
		return returnId;	
	}

//处理投资人资金失败

//call p_invest_action(in_bid,	-1,	2,	1,	0,	@borrowWay,	@borrowAmount,	in_ifeerate,	t_repay_period,	in_auth_time,	t_still_principal,
//		t_still_interest,	t_principal_balance,	t_interest_balance,	t_excitation_sum,	t_mrate,	in_basepath, in_pMerBillNo,	@ret,	@ret_desc);

//#生成通知模版
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
	command.append(" select template from t_approve_notice_template where nid = 'borrow_full_success'; ");
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
	command.append(" select template from t_approve_notice_template where nid = 'e_borrow_full_success'; ");
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
	command.append(" select template from t_approve_notice_template where nid = 's_borrow_full_success'; ");
	DataSet dataSet = MySQL.executeQuery(conn, command.toString());
	command = null;
	return DataSetHelper.dataSetToMap(dataSet);
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

/**
 * 修改投资者应收本金和应收利息
 * @param conn
 * @param investorId 投资人id
 * @return
 * @throws SQLException
 */
public long updateInvestorExpreienceMoney(Connection conn, long investorId)throws SQLException{
	long returnId = -1;
	StringBuffer command = new StringBuffer();
	command.append(" update t_invest set recivedPrincipal = check_principal, recievedInterest = check_interest where id = "+investorId+"; ");
	returnId = MySQL.executeNonQuery(conn, command.toString());
	command = null;
	return returnId;
}

/**
 * 更新用户冻结金额
 * @param conn
 * @param t_invest_amount 投资金额.
 * @param investorId
 * @return
 * @throws SQLException
 */
public long updateInvestorExpreienceMoney(Connection conn,double t_invest_amount, long investorId)throws SQLException{
	long returnId = -1;
	StringBuffer command = new StringBuffer();
	command.append(" update t_experiencemoney set freezeMoney = freezeMoney-"+t_invest_amount+",collectMoney = collectMoney + "+t_invest_amount+" where userid = "+investorId+"; ");
	returnId = MySQL.executeNonQuery(conn, command.toString());
	command = null;
	return returnId;
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
public long insertUpdateInvestorExpreienceMoneyRecord(Connection conn,double t_invest_amount, long t_investor,long t_invest_id,Long borrowerId, String t_content,String in_pMerBillNo)throws SQLException{
	long returnId = -1;
	StringBuffer command = new StringBuffer();
	command.append(" insert into t_fundrecord (userId, recordTime, operateType, fundMode, handleSum, usableSum, freezeSum, dueinSum,trader, dueoutSum, remarks, oninvest, paynumber, paybank, cost,income,borrow_id,pMerBillNo) ");
	command.append(" select "+t_investor+", now(), 654, '扣除投标体验金冻结金额', "+t_invest_amount+",a.usableMoney as usableSum, a.freezeMoney as freezeSum, sum(b.recivedPrincipal +b.recievedInterest - b.hasPrincipal - b.hasInterest),");
	command.append( borrowerId+",0,'"+t_content+"', 0, "+t_invest_id+", '', 0 , "+t_invest_amount+",b.borrowId,'"+in_pMerBillNo+"' ");
	command.append(" from t_experiencemoney a left join t_invest b on a.userid = b.investor  where a.userid = "+t_investor+" group by a.userid;");
	returnId = MySQL.executeNonQuery(conn, command.toString());
	command = null;
	return returnId;	
}
//奖励处理
//获取投资成功通知模板
public Map<String, String> getNoticeTemplate(Connection conn,String templateType)
		throws SQLException, DataException {
	StringBuffer command = new StringBuffer();
	command.append(" select template from t_approve_notice_template where nid = '"+templateType+"'; ");
	DataSet dataSet = MySQL.executeQuery(conn, command.toString());
	command = null;
	return DataSetHelper.dataSetToMap(dataSet);
}




//#审核失败 处理
//1.this.updateBorrowStatus  更新状态update t_borrow set borrowStatus	=	6,	sort	=	0 where id	=	in_bid;
//2.this.returnSecondFreezeFund() #返还发布借款时的冻结金额
//3.this.insertReturnSecondFreezeFundRecord()
//4.通知任务


//call p_borrow_cancel(in_bid,	in_aid,	in_status,	in_audit_opinion,	in_basepath, in_pMerBillNo,	@ret,	@ret_desc);

}
