package com.sp2p.service.test;

import java.util.List;
import java.util.Map;

import com.shove.base.BaseTest;
import com.sp2p.service.UserIntegralService;
import com.sp2p.service.admin.SEOConfigService;
import com.sp2p.task.JobTaskService;

public class TestBorrowManageService extends BaseTest{

	private SEOConfigService SEOConfigService;
	


	public void testBorrowFullScal() throws Exception{ 
		long result = SEOConfigService.updateRegistCode("MID-88089-97412-74087-65951-49988", "MID-88089-97412-74087-65951-49988");
		log.info(result);

	}



	public void setSEOConfigService(SEOConfigService sEOConfigService) {
		SEOConfigService = sEOConfigService;
	}



	public SEOConfigService getSEOConfigService() {
		return SEOConfigService;
	}



	


	


	



}
