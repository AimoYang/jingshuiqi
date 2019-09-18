package com.jingshuiqi.bean;

public class JsonResult {
	private Integer result = 0;

	private String msg = null;

	private Object data = null;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public JsonResult() {
		super();
	}

	public JsonResult(Integer result, String msg) {
		super();
		this.result = result;
		this.msg = msg;
	}

	public JsonResult(Integer result, String msg, Object data) {
		super();
		this.result = result;
		this.msg = msg;
		this.data = data;
	}

	@Override
	public String toString() {
		return "JsonResult [result=" + result + ", msg=" + msg + ", data="
				+ data + "]";
	}
}
