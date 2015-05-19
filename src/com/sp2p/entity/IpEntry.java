package com.sp2p.entity;

import java.util.Map;

public class IpEntry {
	private String code;
	private Map<String, String> data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Map<String, String> getData() {
		return data;
	}
	public void setData(Map<String, String> data) {
		this.data = data;
	}
}
