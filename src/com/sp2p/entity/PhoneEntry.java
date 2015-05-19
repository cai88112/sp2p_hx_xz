package com.sp2p.entity;

import java.util.Map;

public class PhoneEntry {

	private String success;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public Map<String, String> getResult() {
		return result;
	}
	public void setResult(Map<String, String> result) {
		this.result = result;
	}
	private  Map<String, String> result;
}
