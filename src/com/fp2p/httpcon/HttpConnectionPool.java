/**
 * Copyright (c) 2007-2014 .  All rights reserved.  版权所有.
 * 请勿修改或删除版权声明及文件头部.
 */
package com.fp2p.httpcon;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;

/**
 * httpclient的连接池.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class HttpConnectionPool extends
		MultiThreadedHttpConnectionManager {
	/**
	 * 设置对单host最大的连接数(默认所有链接都对同一个host),由bean factory设置，缺省为30秒钟.
	 */
	private static final int DEFAULT_MAX_CONPERHOST = 30;

	/**
	 * 默认总的最大连接数，由bean factory设置，缺省为80秒钟.
	 */
	private static final int DEFAULT_MAX_TOTALCONN = 80;

	/**
	 * 闲置连接超时时间, 由bean factory设置，缺省为60秒钟.
	 */
	private static final int DEFAULT_IDLE_CONNTIMEOUT = 60000;

	{
		try {
			getParams().setDefaultMaxConnectionsPerHost(DEFAULT_MAX_CONPERHOST);
			getParams().setMaxTotalConnections(DEFAULT_MAX_TOTALCONN);

			IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
			ict.addConnectionManager(this);
			ict.setConnectionTimeout(DEFAULT_IDLE_CONNTIMEOUT);
			ict.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
