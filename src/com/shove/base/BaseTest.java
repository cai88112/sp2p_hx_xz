package com.shove.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * 测试基类
 */
public class BaseTest extends AbstractDependencyInjectionSpringContextTests {
	public static Log log = LogFactory.getLog(BaseTest.class);
	public String[] getConfigLocations() {
		setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath:beans_all.xml" };
	}
}
