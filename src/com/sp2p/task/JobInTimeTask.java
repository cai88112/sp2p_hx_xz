/*
 * cron4j - A pure Java cron-like scheduler
 * 
 * Copyright (C) 2007-2010 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License 2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.sp2p.task;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.context.ContextLoader;

import com.sp2p.service.AssignmentDebtService;
import com.sp2p.service.BorrowService;
import com.shove.data.DataException;

/**
 * This task counts from 1 to 30.
 */
public class JobInTimeTask extends QuartzJobBean {

	private static Log log = LogFactory.getLog(JobInTimeTask.class);
	private static boolean isRunning = false;
	
    private JobTaskService jobTaskService;
    private BorrowService borrowService;
    private AssignmentDebtService assignmentDebtService;
    private long sleepTime;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		long start = System.currentTimeMillis();    
	    //自动投标
	    JobTaskService jobTaskService =  (JobTaskService) getBean("jobTaskService");
	    //刷新借款时间
	    BorrowService borrowService = (BorrowService)getBean("borrowService");
	    //刷新债权转让时间
		AssignmentDebtService assignmentDebtService =(AssignmentDebtService)getBean("assignmentDebtService");
		
	    try {
			if(!isRunning){
				isRunning = true;
				log.info("自动投标一轮开始");
				jobTaskService.autoBid();
				//jobTaskService.autoBidProTask();
				//jobTaskService.autoBidPro();
				log.info("自动投标一轮结束");
				log.info("发送短信模板开始");
				jobTaskService.sendtoTemple();
				log.info("发送短信模板结束");
				log.info("处理过期借款一轮开始");
				borrowService.refreshBorrowTime();
				log.info("处理过期借款一轮结束");
				log.info("处理过期债权一轮开始");
				assignmentDebtService.checkDueDebt();
				log.info("处理过期债权一轮结束");				
				
//				log.info("----------------------------处理全部发放续投奖励开始.");
//				jobTaskService.autoGiveContinuedInvestmentReward();
//				log.info("----------------------------处理全部发放续投奖OK.");
				
				
				isRunning = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DataException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		}
		log.info("用时 : " + (System.currentTimeMillis() - start) + "毫秒"
				+"SystemMemery:freeMemory"+Runtime.getRuntime().freeMemory()+"-------maxMemory"+Runtime.getRuntime().maxMemory()+"-------totalMemory"+Runtime.getRuntime().totalMemory());
	}
	public boolean canBePaused() {
		
		return true;
	}

	public boolean canBeStopped() {
		return true;
	}

	public boolean supportsCompletenessTracking() {
		return true;
	}

	public boolean supportsStatusTracking() {
		return true;
	}

	private Object getBean(String beanName) {
		return ContextLoader.getCurrentWebApplicationContext().getBean(beanName);
	}
	
	public JobTaskService getJobTaskService() {
		return jobTaskService;
	}

	public void setJobTaskService(JobTaskService jobTaskService) {
		this.jobTaskService = jobTaskService;
	}

	public BorrowService getBorrowService() {
		return borrowService;
	}

	public void setBorrowService(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	public AssignmentDebtService getAssignmentDebtService() {
		return assignmentDebtService;
	}

	public void setAssignmentDebtService(AssignmentDebtService assignmentDebtService) {
		this.assignmentDebtService = assignmentDebtService;
	}

	public long getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
	}
}
