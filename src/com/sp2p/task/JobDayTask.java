package com.sp2p.task;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.sp2p.constants.IAmountConstants;
import com.sp2p.service.FrontMyPaymentService;
import com.shove.data.DataException;

public class JobDayTask extends QuartzJobBean {
	
	private static Log log = LogFactory.getLog(JobDayTask.class);
	private static boolean isRunning = false;
	
	private Object getBean(String beanName) {
		return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
	}
	
	private Object getApplicationMap(String attrName){
		return ContextLoader.getCurrentWebApplicationContext().getServletContext().getAttribute(attrName);
	}
	
	private void setApplicationMap(String attrName,Map<String,String> map){
		 ContextLoader.getCurrentWebApplicationContext().getServletContext().setAttribute(attrName, map);
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		long start = System.currentTimeMillis();    
 	    //获取平台收费
	    Map<String,Object> platformCostMap = (Map<String, Object>) getApplicationMap(IAmountConstants.FEE_APPLICATION);
	    FrontMyPaymentService frontMyPayment = (FrontMyPaymentService) getBean("frontpayService");
	    
	    JobTaskService jobTaskService =  (JobTaskService) getBean("jobTaskService");
		
	    try {
	    	Map<String,String>  investMap = frontMyPayment.querInvesttou();
	    	setApplicationMap("investMap", investMap);//放入全局变量中   用于前台二级菜单 金额的显示
			if(!isRunning){
				isRunning = true;
				log.info("每日任务处理开始");
				//自动更新VIP续费和扣除首次申请成为VIP会费
				jobTaskService.autoDeductedVIPFee();
				log.info("自动更新VIP续费和扣除首次申请成为VIP会费OK");
				//好友推荐奖励
//				jobTaskService.inviteFriendsReward();
//				log.info("好友推荐奖励OK");
				//逾期的还款
				jobTaskService.updateOverDueRepayment();
				log.info("逾期的还款OK");
				//逾期的投资还款记录
				jobTaskService.updateOverDueInvestRepayment();
				log.info("逾期的投资还款记录OK");
				
				log.info("处理体验金开始");
				jobTaskService.automaticDealExperienctmoney();
				log.info("处理到期体验金OK");
				
				log.info("处理全部发放续投奖励开始.");
				jobTaskService.autoGiveContinuedInvestmentReward();
				log.info("处理全部发放续投奖OK.");
						
				
				//自动扣除学历认证费用
//				jobTaskService.autoDeductedXLFee();
//				log.info("自动扣除学历认证费用OK");
				//流转标自动还款
			//	jobTaskService.automaticPayment();
				//log.info("流转标自动还款OK");
				log.info("每日任务处理结束");
				isRunning = false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			platformCostMap = null;
		}
		log.info("用时 : " + (System.currentTimeMillis() - start) + "毫秒"
				+"SystemMemery:freeMemory"+Runtime.getRuntime().freeMemory()+"-------maxMemory"+Runtime.getRuntime().maxMemory()+"-------totalMemory"+Runtime.getRuntime().totalMemory());
	}
}
