package com.sp2p.action.front;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fp2p.helper.JSONHelper;
import com.shove.web.action.BasePageAction;

public class BBSPW extends BasePageAction {

	public String querybbspw() {
		return SUCCESS;
	}
	/*
	 * 加密 dbhost = 127.0.0.1 dbport=3306 dbuser = root dbpw = 123456 dbname =
	 * sp2p_bbs
	 */
	public String ShowBBSPW() throws IOException {
		String dbhost = request("dbhost");
		String dbport = request("dbport");
		String dbuser = request("dbuser");
		String dbpw = request("dbpw");
		String dbname = request("dbname");
		String dbhost_en = com.shove.security.Encrypt
				.encryptSES(dbhost, "hero1234567890123456");
		String dbport_en = com.shove.security.Encrypt
				.encryptSES(dbport, "hero1234567890123456");
		String dbuser_en = com.shove.security.Encrypt
				.encryptSES(dbuser, "hero1234567890123456");
		String dbpw_en = com.shove.security.Encrypt
		.encryptSES(dbpw, "hero1234567890123456");
		String dbname_en = com.shove.security.Encrypt
				.encryptSES(dbname, "hero1234567890123456");
		Map<String,String> dbMsg = new HashMap<String, String>();
		dbMsg.put("dbhost_en", dbhost_en);
		dbMsg.put("dbport_en", dbport_en);
		dbMsg.put("dbuser_en", dbuser_en);
		dbMsg.put("dbpw_en", dbpw_en);
		dbMsg.put("dbname_en", dbname_en);
		JSONHelper.printObject(dbMsg);
		return null;
	}
}
