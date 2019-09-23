package com.jingshuiqi.dto;

import javax.validation.constraints.NotNull;

public class TypeDatePage extends PageObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull(message = "类型不能为空")
	private Integer type;

	private String Date;

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
