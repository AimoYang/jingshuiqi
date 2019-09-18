package com.jingshuiqi.bean;

public class Template {

	private String code;
	private String date;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Template [code=" + code + ", date=" + date + "]";
	}
	
}
